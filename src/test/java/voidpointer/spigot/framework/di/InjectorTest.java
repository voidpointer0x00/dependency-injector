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

import be.seeseemelk.mockbukkit.MockBukkit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import voidpointer.spigot.framework.di.sub.SecondInjectionTarget;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class InjectorTest {

    @BeforeAll static void setUp() {
        MockBukkit.mock();
        MockBukkit.getMock().setPlayers(20);
    }

    @AfterAll static void tearDown() {
        MockBukkit.unmock();
    }

    @ParameterizedTest
    @MethodSource("getDependencyPlugin")
    void testInjector(final DependencyPlugin plugin) {
        assertNotNull(plugin.pluginName);
        assertNotNull(plugin.pluginVersion);
        assertNotNull(plugin.randomPlayer);
        assertEquals(plugin.pluginName, FirstInjectionTarget.name);
        assertEquals(plugin.pluginName, SecondInjectionTarget.name);
        assertEquals(plugin.pluginVersion, SecondInjectionTarget.version);
        assertEquals(plugin.pluginVersion, FirstInjectionTarget.version);
        assertEquals(plugin.randomPlayer, FirstInjectionTarget.player);
        assertEquals(plugin.randomPlayer, SecondInjectionTarget.player);
    }

    static Stream<Arguments> getDependencyPlugin() {
        return Stream.of(
                arguments(MockBukkit.load(DependencyPlugin.class))
        );
    }
}