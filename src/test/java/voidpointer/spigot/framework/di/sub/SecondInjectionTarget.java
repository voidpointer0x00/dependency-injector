package voidpointer.spigot.framework.di.sub;

import org.bukkit.entity.Player;
import voidpointer.spigot.framework.di.Autowired;

public class SecondInjectionTarget {
    @Autowired(mapId="name")
    public static String name;
    @Autowired(mapId="version")
    public static String version;
    @Autowired public static Player player;
}
