package vn.ean.a2nuth;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import org.bukkit.entity.Player;

public class App extends JavaPlugin implements Listener {
    List<String> logged = new ArrayList<>();
    File configFile;

    FileConfiguration db;

    @Override
    public void onEnable() {
        setupConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("A2nuth enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("A2nuth disabled");
    }

    public void setupConfig() {
        configFile = new File(getDataFolder(), "data.yml");

        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        db = YamlConfiguration.loadConfiguration(configFile);
    }

    public void saveConfig() {
        try {
            db.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void authRequest(Player player) {
        sendMessage(player, "Hãy gửi mật khẩu vào khung chat để đăng " + (db.contains(player.getName()) ? "nhập" : "ký"));
    }

    public void authHandler(Player player, String password) {
        String prefixType = "Đăng nhập";
        password = password.trim();

        if (db.contains(player.getName())) {
            if (!db.getString(player.getName()).equals(password)) {
                sendMessage(player, "Sai mật khẩu, hãy thử lại");
                return;
            }
        } else {
            prefixType = "Đăng ký";
            if (password.length() < 6) {
                sendMessage(player, "Mật khẩu quá yếu, hãy thử lại");
                return;
            }

            db.set(player.getName(), password);
        }

        logged.add(player.getName());
        sendMessage(player, String.format("%s thành công, chúc bạn chơi vui vẻ", prefixType));
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        authRequest(player);
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        logged.remove(player.getName());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (logged.contains(player.getName()))
            return;

        authRequest(player);
        event.setCancelled(true);
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        String message = PlainTextComponentSerializer.plainText().serialize(event.message());
        Player player = event.getPlayer();
        if (logged.contains(player.getName()))
            return;

        authHandler(player, message);
        event.setCancelled(true);
    }

    public void sendMessage(Player player, String message) {
        player.sendMessage(String.format("[A2nuth] %s", message));
    
        return;
    }
}
