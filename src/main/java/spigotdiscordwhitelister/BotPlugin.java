package spigotdiscordwhitelister;

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
import org.bukkit.event.player.AsyncPlayerChatEvent;

//JDA
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

// Default
import javax.security.auth.login.LoginException;

public class BotPlugin extends JavaPlugin {
	private final String BOT_TOKEN = "NTg1ODgyNzAxNjU2MDk2ODI4.XPf8IA.zU5IECNpHIkDAFWRJlvICjGql_I";
	private JDA jda;
	private BotDiscord db;
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
			db = new BotDiscord(this);
			jda = new JDABuilder(AccountType.BOT)
				      .setToken(BOT_TOKEN)
		              .addEventListener(db)
					  .buildBlocking();
					  
			System.out.println("[SpigotDiscordWhitelister]: Discord bot it running");

		} catch (LoginException e) {
			e.printStackTrace();
			System.exit(1);

		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	@Override
	public void onDisable() {
        tellConsole("plugin", "Exiting");
	}

	@Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
		if(alias.equalsIgnoreCase("sw")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Missing Arguments!");
				
				return true;
			} 
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("help")) {
					sender.sendMessage(ChatColor.RED + "--SW Help--");
					sender.sendMessage(ChatColor.GOLD + "/sw:" + ChatColor.WHITE +" General SW command");
					sender.sendMessage(ChatColor.GOLD + "/sw help:" + ChatColor.WHITE +" Shows this page");
					sender.sendMessage(ChatColor.GOLD + "/sw whitelist <player>: " + ChatColor.WHITE + " Whitelist a player");
					sender.sendMessage(ChatColor.GOLD + "/sw auto-whitelist <true/false>:" + ChatColor.WHITE + " Toggle Auto-Whitelist");
					
					return true;
				}  else {
					sender.sendMessage(ChatColor.RED + "Invalid Argument!");
					return true;
				}
			} 
			if(args.length == 2) {
				if(args[0].equalsIgnoreCase("whitelist")) {
					if(args[1] != null) {
						String toWhitelist = args[1];
						
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "whitelist add " + toWhitelist);
						sender.sendMessage(ChatColor.GOLD + toWhitelist + " has been whitelisted!");
						
						return true;
					}
				} else if(args[0].equalsIgnoreCase("auto-whitelist")) {
					if(args[1].equalsIgnoreCase("true")) {
						sender.sendMessage(ChatColor.GOLD + "Enabled Auto-Whitelist!");
						autoWhitelist = true;
						tellConsole("plugin", "Auto-Whitelist Enabled!");
						
						this.getConfig().set("auto-whitelist", true);
						saveConfig();
						
						return true;
					} else if(args[1].equalsIgnoreCase("false")) {
						sender.sendMessage(ChatColor.GOLD + "Disabled Auto-Whitelist!");
						autoWhitelist = false;
						tellConsole("plugin", "Auto-Whitelist Disabled!");
						
						this.getConfig().set("auto-whitelist", false);
						saveConfig();
						
						return true;
					} else { //Doesn't work for some reason
						sender.sendMessage(ChatColor.RED + "Invalid Argument!");

						return true;
					}
				}
				return false;
			} else {
				sender.sendMessage(ChatColor.RED + "You messed up something!");
			}

 			return true;
		}
		return false;
	}
	

	
	public void playerChat(AsyncPlayerChatEvent event){
		db.sendChatToDiscord(event.getMessage(), jda);
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