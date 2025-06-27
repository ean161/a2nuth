package vn.ean.a2nuth;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin implements Listener {
    private static App instance;
    A2nuth a2nuth = new A2nuth();

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(a2nuth, this);
        getLogger().info("A2nuth enabled, developed by ean (nghoaian161@gmail.com)");
    }

    @Override
    public void onDisable() {
        getLogger().info("A2nuth disabled");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (!a2nuth.isPlayerLogged(event.getPlayer().getName())) {
            a2nuth.authRequest(event.getPlayer());
            event.setCancelled(true);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("a2nuth")) {
            if (sender instanceof Player && args.length == 2) {
                switch (args[0]) {
                    case "reset":
                        if (sender.isOp()) {
                            Lib.setConfig("data", args[1], "");
                            Lib.sendMessage((Player) sender, String.format(Lib.getConfig("config", "reset"), args[1]));
                            return true;
                        }
                        break;
                    case "cpass":
                        if (a2nuth.isPlayerLogged(sender.getName())) {
                            String oldPassword = Lib.getConfig("data", sender.getName());
                            Lib.setConfig("data", sender.getName(), "");

                            if (!a2nuth.authHandler((Player) sender, args[1]))
                                Lib.setConfig("data", sender.getName(), oldPassword);
                            else
                                Lib.sendMessage((Player) sender, Lib.getConfig("config", "change_pass"));
                            return true;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return false;
    }
}
