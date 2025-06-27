package vn.ean.a2nuth;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin implements Listener {
    private static App instance;
    public static App getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(new A2nuth(), this);
        getLogger().info("A2nuth enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("A2nuth disabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("a2nuth")) {
            if (sender instanceof Player && sender.isOp()) {
                if (args.length == 2 && args[0].equalsIgnoreCase("reset")) {
                    Lib.setConfig("data", args[1], "");
                    Lib.sendMessage((Player) sender, String.format(Lib.getConfig("config", "reset"), args[1]));
                    return true;
                }
            }
        }
        return false;
    }
}
