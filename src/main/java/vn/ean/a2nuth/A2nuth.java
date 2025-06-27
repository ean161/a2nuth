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
    List<String> logged = new ArrayList<>();

    public void authRequest(Player player) {
        Lib.sendMessage(player, "Hãy gửi mật khẩu vào khung chat để đăng " + (Lib.getConfig("data", player.getName()).isEmpty() ? "ký" : "nhập"));
    }

    public void authHandler(Player player, String password) {
        String passwordSaved = Lib.getConfig("data", player.getName());
        String prefixType = (passwordSaved.isEmpty() ? "Đăng ký" : "Đăng nhập");
        password = password.trim();

        if (!passwordSaved.isEmpty() && !passwordSaved.equals(password)) {
            Lib.sendMessage(player, "Sai mật khẩu, hãy thử lại");
            return;
        } else if (password.length() < 6) {
            Lib.sendMessage(player, "Mật khẩu quá yếu, hãy thử lại");
            return;
        }
        
        logged.add(player.getName());
        Lib.setConfig("data", player.getName(), password);
        Lib.sendMessage(player, String.format("%s thành công, chúc bạn chơi vui vẻ", prefixType));
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
