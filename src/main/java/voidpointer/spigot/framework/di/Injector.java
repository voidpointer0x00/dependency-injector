/*
 *             DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
 *
 *  Copyright (C) 2022 Vasiliy Petukhov <void.pointer@ya.ru>
 *
 *  Everyone is permitted to copy and distribute verbatim or modified
 *  copies of this license document, and changing it is allowed as long
 *  as the name is changed.
 *
 *             DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
 *    TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
 *
 *   0. You just DO WHAT THE FUCK YOU WANT TO.
 */
package voidpointer.spigot.framework.di;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import lombok.val;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Injector {
    public static void inject(final JavaPlugin plugin) {
        Map<String, Object> dependencies = findDependencies(plugin);
        if (dependencies.isEmpty()) {
            plugin.getLogger().info("No @Dependency found for injection.");
            return;
        }
        Map<Field, String> injectTargets = findInjectTargets(plugin);
        if (injectTargets == null)
            return;
        inject(dependencies, injectTargets, plugin.getLogger());
    }

    private static void inject(final Map<String, Object> dependencies,
                               final Map<Field, String> targets,
                               final Logger logger) {
        for (val entry : targets.entrySet()) {
            if (!dependencies.containsKey(entry.getValue())) {
                reportDependencyNotFound(entry, logger);
                continue;
            }
            Field field = entry.getKey();
            try {
                field.setAccessible(true);
                field.set(null, dependencies.get(entry.getValue()));
            } catch (Exception exception) {
                reportFieldSetFailure(field, logger, exception);
            }
        }
    }

    private static void reportFieldSetFailure(final Field field, final Logger logger, final Exception exception) {
        logger.log(Level.WARNING, String.format("Unable to inject dependency into field «%s»", field), exception);
    }

    private static void reportDependencyNotFound(final Entry<Field, String> entry, final Logger logger) {
        logger.log(Level.WARNING, "Dependency for «{0}» with id «{1}» not found",
                new Object[]{entry.getKey(), entry.getValue()});
    }

    private static Map<Field, String> findInjectTargets(final JavaPlugin plugin) {
        ImmutableSet<ClassInfo> classes = getClasses(plugin);
        if (classes == null)
            return null;
        Map<Field, String> injectTargets = Collections.synchronizedMap(new HashMap<>());
        classes.parallelStream().map(info -> Pair.of(info.load(), info.load().getDeclaredFields())).forEach(pair -> {
            for (Field field : pair.second) {
                if (!field.isAnnotationPresent(Autowired.class))
                    continue;
                if (!Modifier.isStatic(field.getModifiers())) {
                    reportNotStatic(plugin.getLogger(), field);
                    continue;
                }
                Autowired autowired = field.getAnnotation(Autowired.class);
                String mapId = autowired.mapId().isEmpty() ? field.getType().getName() : autowired.mapId();
                injectTargets.put(field, mapId);
            }
        });
        return injectTargets;
    }

    private static void reportNotStatic(final Logger logger, final Field field) {
        logger.log(Level.WARNING, "@Autowired field «{0} {1}» is not static",
                new Object[]{field.getDeclaringClass().getName(), field.getName()});
    }

    private static ImmutableSet<ClassInfo> getClasses(final JavaPlugin plugin) {
        try {
            return ClassPath.from(plugin.getClass().getClassLoader())
                    .getTopLevelClassesRecursive(plugin.getClass().getPackageName());
        } catch (IOException ioException) {
            plugin.getLogger().log(Level.WARNING, "Can't inject any @Dependency", ioException);
            return null;
        }
    }

    private static Map<String, Object> findDependencies(final JavaPlugin plugin) {
        Map<String, Object> dependencies = new HashMap<>();
        for (Field field : plugin.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(Dependency.class)) {
                continue;
            }
            Dependency dependency = field.getAnnotation(Dependency.class);
            String id = dependency.id().isEmpty() ? field.getType().getName() : dependency.id();
            Object value = getFieldValue(field, plugin, plugin.getLogger());
            if (value != null) {
                if (!dependencies.containsKey(id))
                    dependencies.put(id, value);
                else
                    reportDuplicate(plugin.getLogger(), id, field);
            }
        }
        return dependencies;
    }

    private static Object getFieldValue(final Field field, final Object obj, final Logger logger) {
        try {
            return field.get(obj);
        } catch (Exception exception) {
            logger.log(Level.WARNING, "Unable to get @Dependency value", exception);
            return null;
        }
    }

    private static void reportDuplicate(final Logger logger, final String id, final Field field) {
        logger.log(Level.WARNING, "Skipping duplicate @Dependency «{0}» named «{1}»",
                new Object[]{id, field.getName()});
    }
}
