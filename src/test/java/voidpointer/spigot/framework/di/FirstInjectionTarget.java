package voidpointer.spigot.framework.di;

import org.bukkit.entity.Player;

final class FirstInjectionTarget {
    @Autowired(mapId="name")
    static String name;
    @Autowired(mapId="version")
    static String version;
    @Autowired static Player player;
}
