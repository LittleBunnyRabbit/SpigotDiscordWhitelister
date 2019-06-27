package spigotdiscordwhitelister.spigot;

// Bukkit
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.server.ServerEvent;

// JDA
import net.dv8tion.jda.core.JDA;

// Local
import spigotdiscordwhitelister.discord.*;

public class McListener implements Listener {
    private BotDiscord bd;
    private BotPlugin bp;
    private JDA jda;

    public McListener(BotPlugin bp, BotDiscord bd, JDA jda) {
        this.bp = bp;
        this.bd = bd;
        this.jda = jda;
    }

    @EventHandler
    public void onchat(AsyncPlayerChatEvent event){
        String player = event.getPlayer().getName();
        String message = event.getMessage();
        bd.sendToDiscord("[**" + player + "**]: " + message, jda);
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        String player = event.getPlayer().getName();
        bd.sendToDiscord(":heavy_plus_sign: **" + player + "** joined the server!", jda);
    }
    
    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        String player = event.getPlayer().getName();
        bd.sendToDiscord(":heavy_minus_sign: **" + player + "** left the server!", jda);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        String player = event.getEntity().getName();
        String deathMsg = event.getDeathMessage();
        bd.sendToDiscord(":skull: **" + player + "** died... " + deathMsg, jda);
    }
    
    @EventHandler
    public void onDeath(PlayerKickEvent event){
        String player = event.getPlayer().getName();
        String reason = event.getReason();
        bd.sendToDiscord(":no_entry_sign: **" + player + "** got kicked! Reason: **" + reason + "**", jda);
    }
}