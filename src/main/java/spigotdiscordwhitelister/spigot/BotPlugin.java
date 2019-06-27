package spigotdiscordwhitelister.spigot;

// Bukkit
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.entity.Player;

//JDA
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

// Default
import javax.security.auth.login.LoginException;

// Local
import spigotdiscordwhitelister.discord.*;

public class BotPlugin extends JavaPlugin {
	private final String BOT_TOKEN = "NTg1ODgyNzAxNjU2MDk2ODI4.XPf8IA.zU5IECNpHIkDAFWRJlvICjGql_I";
	private JDA jda;
	private BotDiscord bd;
	private McListener mcl;
    private boolean autoWhitelist;
    private FileConfiguration config = this.getConfig();
	
	public void tellConsole(String origin, String string) {
		if(origin.equalsIgnoreCase("bot")) {
			System.out.println("[SpigotDiscordWhitelister][Bot] " + string);
		} else if(origin.equalsIgnoreCase("plugin")) {
			System.out.println("[SpigotDiscordWhitelister][Plugin] " + string);
		} else {
			System.out.println("[SpigotDiscordWhitelister][Unkown Origin] " + string);
		}

	}
	
	@Override
	public void onEnable() {
		tellConsole("plugin", "Starting");
		System.out.println("Starting");
		config();
		if (config.getBoolean("autoWhitelist")) {
				autoWhitelist = true;
			} else {
				autoWhitelist = false;
			}

		// Run the discord bots
		try {
			bd = new BotDiscord(this);
			jda = new JDABuilder(AccountType.BOT)
				      .setToken(BOT_TOKEN)
		              .addEventListener(bd)
					  .buildBlocking();
					  
			System.out.println("[SpigotDiscordWhitelister]: Discord bot it running");

		} catch (LoginException e) {
			e.printStackTrace();
			System.exit(1);

		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}

		try {
			mcl = new McListener(this, bd, jda);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Bukkit.getServer().getPluginManager().registerEvents(new McListener(this, bd, jda), this);
		bd.sendToDiscord(":white_check_mark: **Server has started!**", jda);
	}
	
	@Override
	public void onDisable() {
		tellConsole("plugin", "Exiting");
		bd.sendToDiscord(":octagonal_sign: **Server has stopped!**", jda);
	}


	public void config() {
		//TODO
		tellConsole("plugin", "Config Sector");
		
		this.saveDefaultConfig();
		tellConsole("plugin", "done");

	}
	
	// Executes any given command!
	public void runCommand(String command) {
		Bukkit.getScheduler().callSyncMethod(this, () -> Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command));

		tellConsole("Plugin", ChatColor.GREEN + "Yehaw just executed: " + command);
	}
}

