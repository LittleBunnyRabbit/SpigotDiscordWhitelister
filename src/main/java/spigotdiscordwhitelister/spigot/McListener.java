package spigotdiscordwhitelister.spigot;

// Bukkit
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

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
    public void playerChat(AsyncPlayerChatEvent event){
		bd.sendChatToDiscord(event.getMessage(), jda);
	}
}