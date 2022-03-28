package voidpointer.spigot.framework.di;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.Random;

final class DependencyPlugin extends JavaPlugin {
    @Dependency(id="name")
    static String pluginName;
    @Dependency(id="version")
    static String pluginVersion;
    @Dependency
    static Player randomPlayer;

    public DependencyPlugin() {}

    protected DependencyPlugin(@NotNull final JavaPluginLoader loader, @NotNull final PluginDescriptionFile description, @NotNull final File dataFolder, @NotNull final File file) {
        super(loader, description, dataFolder, file);
    }

    @Override public void onLoad() {
        pluginName = getName();
        pluginVersion = getDescription().getVersion();
        randomPlayer = getRandomOnlinePlayer();
    }

    @Override public void onEnable() {
        Injector.inject(this);
    }

    private Player getRandomOnlinePlayer() {
        Collection<? extends Player> onlinePlayers = getServer().getOnlinePlayers();
        int index = new Random(System.currentTimeMillis()).nextInt(onlinePlayers.size());
        for (Player player : onlinePlayers) {
            if (index-- <= 0)
                return player;
        }
        assert false;
        return null;
    }
}
