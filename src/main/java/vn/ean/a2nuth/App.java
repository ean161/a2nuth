package vn.ean.a2nuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.Location;

public class App extends JavaPlugin implements Listener {
    List<String> logged = new ArrayList<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("A2nuth enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("A2nuth disabled");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (logged.contains(player.getName()))
            return;

        sendMessage(player, "Bạn chưa đăng nhập, không thể di chuyển");
        event.setCancelled(true);
    }

    public void sendMessage(Player player, String message) {
        player.sendMessage(String.format("[A2nuth] %s", message));
    
        return;
    }
}
