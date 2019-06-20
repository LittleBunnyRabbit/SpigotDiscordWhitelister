package spigotdiscordwhitelister.discord;

// JDK
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.JDA;

// Local
import spigotdiscordwhitelister.spigot.*;

public class BotDiscord extends ListenerAdapter {
    private final String REQUIRED_CHANNEL = "590920622457094172";
    private final String WHITELISTER_ROLE = "476550966657679370";
    private final String LOVELIE_ROLE = "472038003481509903";
    private BotPlugin plugin;

    // Initializes the bot
    public BotDiscord(BotPlugin plugin) {
        this.plugin = plugin;
        System.out.println("Discord bot is up and running");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        boolean isBot = event.getAuthor().isBot();
        String content = event.getMessage().getContentDisplay();

        /**
         * Checks if its a bot and
         * if the prefix is corect
         * then stores the arguments in array
         */
        
        if (!isBot) {
            if (content.startsWith("$")) {
                content = content.substring(1);
                String[] contentArr = content.split(" ");
                executeCommand(event, contentArr);
            }
        }
    }

    // Executes the given command
    public void executeCommand(MessageReceivedEvent event, String[] contentArr) {
        switch (contentArr[0].toUpperCase()) {
            case "WHITELIST":
                String currentChannel = event.getChannel().getId();
                boolean isLovelie = false;
                boolean isWhitelister = false;

                for (Role r : event.getMember().getRoles()) {
                    if(r.getId().equals(LOVELIE_ROLE)) {
                        isLovelie = true;
                    }

                    if(r.getId().equals(WHITELISTER_ROLE)) {
                        isLovelie = true;
                        isWhitelister = true;
                        break;
                    }
                }

                if(currentChannel.equals(REQUIRED_CHANNEL) && isLovelie) {
                    // If there are arguments checks if the user has the right role
                    boolean canAddRemove = false;
                    if(contentArr.length > 2 && isWhitelister) canAddRemove = true;

                    String executingUser = event.getAuthor().getName();
                    String whitelistUser = event.getMember().getNickname();
                    String addremove = "add";
                    boolean canExecute = true;
                    
                    // Changes data based on the arguments
                    if(canAddRemove) {
                        addremove = contentArr[1];
                        whitelistUser = contentArr[2];
                        if(!addremove.equals("add") && !addremove.equals("remove")) canExecute = false;
                    } else {
                        if(contentArr.length > 1) canExecute = false;
                    } 
                    
                    if(canExecute) {
                        String command = String.format("whitelist %s %s", addremove, whitelistUser);
                        String log = String.format("**%s** runned: `/%s`", executingUser, command);
                        
                        // Runs the command on the MC server
                        plugin.runCommand(command);

                        if(addremove.equals("remove")) addremove = "remov";

                        // Reply message
                        event.getChannel()
                            .sendMessage(String.format("**%s** was %sed", whitelistUser, addremove))
                            .queue();
                        
                        // Send to bug-report
                        event.getJDA()
                            .getTextChannelById("590928397857849364")
                            .sendMessage(log)
                            .queue();

                    } else {
                        String errorMsg = "Oh oh it looks like you did something wrong... The command is:\n" + 
                                        "**$whitelist** or **$whitelist add/remove {user}** (optional for admins)";

                        event.getChannel()
                             .sendMessage(errorMsg)
                             .queue();
                    }
                }
                break;
            
            // Add more cases for more commands

            default:
                break;
        }
    }

    public void sendChatToDiscord(String message, JDA jda) {
        jda.getTextChannelById("591225138285117446")
           .sendMessage("message")
           .queue();
    }
}