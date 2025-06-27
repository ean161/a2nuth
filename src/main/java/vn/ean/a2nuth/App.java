package vn.ean.a2nuth;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("A2nuth enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("A2nuth disabled");
    }
}
