package to.us.electr0n.servernews;

import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ServerNewsCommand implements CommandExecutor {
  private final ServerNews plugin;
  public ServerNewsCommand(ServerNews plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length == 0) {
      return false;
    }
    switch (args[0]) {

      case "help":
        if (args.length > 1) {
          sender.sendMessage("Usage: /servernews help");
          return true;
        }
        sender.sendMessage("/servernews subcommands:");
        sender.sendMessage("- /servernews help: shows this message");
        sender.sendMessage("- /servernews listMessages: lists the messages in the system");
        sender.sendMessage("- /servernews clearMessages: clears the messages list");
        sender.sendMessage("- /servernews addMessage <miniMessage>: adds a message to the list and sends it out to the players");
        return true;

      case "listMessages":
        if (args.length > 1) {
          sender.sendMessage("Usage: /servernews listMessages");
          return true;
        }
        if (plugin.messages.isEmpty()) {
          sender.sendMessage("There are no news messages");
        } else {
          sender.sendMessage("News messages:");
          for (NewsMessage msg : plugin.messages) {
            sender.sendRichMessage("- " + msg.getMessage());
          }
        }
        return true;

      case "clearMessages":
        if (args.length > 1) {
          sender.sendMessage("Usage: /servernews clearMessages");
          return true;
        }
        plugin.messages.clear();
        plugin.updateMessagesConfig();

        for (String s : plugin.getPlayersConfig().getConfigurationSection("players").getKeys(false)) {
          plugin.getPlayersConfig().getConfigurationSection("players").set(s, null);
          plugin.getPlayersConfig().getConfigurationSection("players").createSection(s);
        }
        plugin.savePlayers();

        sender.sendMessage("Cleared the message list");
        return true;

      case "addMessage":
        String msg = "";
        if (args.length < 2) {
          sender.sendMessage("Usage: /servernews addMessage <miniMessage>");
          return true;
        } else if (args.length == 2) {
          msg = args[1];
        } else if (args.length > 2) {
          for (int i = 1; i < args.length; i++) {
            msg += args[i];
            if (i != args.length - 1) {
              msg += " ";
            }
          }
        }
        sender.sendRichMessage("Added \"" + msg + "<reset>\" to the messages list");

        msg = "<gray>[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("M/d/yy h:mm a")) + "]<reset> " + msg;

        NewsMessage msgToAdd = new NewsMessage(msg);
        plugin.messages.add(msgToAdd);
        plugin.updateMessagesConfig();

        for (String s : plugin.getPlayersConfig().getConfigurationSection("players").getKeys(false)) {
          List<String> playerMessages = plugin.getPlayersConfig().getConfigurationSection("players").getStringList(s);
          playerMessages.add(msgToAdd.getID().toString());
          plugin.getPlayersConfig().getConfigurationSection("players").set(s, playerMessages);
        }
        plugin.savePlayers();
        for (Player p : plugin.getServer().getOnlinePlayers()) {
          plugin.showInbox(p);
        }
        return true;

    }
    return false;
  }

}
