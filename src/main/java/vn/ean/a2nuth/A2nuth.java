package vn.ean.a2nuth;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import org.bukkit.entity.Player;

public class A2nuth implements Listener {
    private List<String> logged = new ArrayList<>();

    public boolean isPlayerLogged(String name) {
        return logged.contains(name);
    }

    public void authRequest(Player player) {
        Lib.sendMessage(player, (Lib.getConfig("data", player.getName()).isEmpty()
            ? Lib.getConfig("config", "register.request")
            : Lib.getConfig("config", "login.request")));
    }

    public boolean authHandler(Player player, String password) {
        String passwordSaved = Lib.getConfig("data", player.getName());
        password = password.trim();

        if (!passwordSaved.isEmpty() && !passwordSaved.equals(password)) {
            Lib.sendMessage(player, Lib.getConfig("config", "login.wrong_password"));
            return false;
        } else if (password.length() < Integer.parseInt(Lib.getConfig("config", "register.password_length"))) {
            Lib.sendMessage(player, Lib.getConfig("config", "register.password_length_error"));
            return false;
        } else if (password.contains(" ")) {
            Lib.sendMessage(player, Lib.getConfig("config", "register.password_contain_space_error"));
            return false;
        }
        
        logged.add(player.getName());
        Lib.setConfig("data", player.getName(), password);
        Lib.sendMessage(player, passwordSaved.isEmpty()
            ? Lib.getConfig("config", "register.success")
            : Lib.getConfig("config", "login.success"));

        if (passwordSaved.isEmpty())
            Lib.sendMessage(player, String.format(Lib.getConfig("config", "register.show_password"), password));
        return true;
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
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (logged.contains(player.getName()))
            return;

        authRequest(player);
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (logged.contains(player.getName()))
            return;

        authRequest(player);
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (logged.contains(player.getName()))
            return;

        authRequest(player);
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (logged.contains(player.getName()))
            return;

        authRequest(player);
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (logged.contains(player.getName()))
            return;
        
        authRequest(player);
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getDamager();
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
}
