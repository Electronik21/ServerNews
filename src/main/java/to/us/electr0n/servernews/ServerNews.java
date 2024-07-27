package to.us.electr0n.servernews;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Map;
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class ServerNews extends JavaPlugin {

  @Override
  public void onLoad() {
    getComponentLogger().info(MiniMessage.miniMessage().deserialize("<light_purple>Loaded ServerNews!"));
  }

  private File messagesFile;
  private FileConfiguration messagesConfig;
  public FileConfiguration getMessagesConfig() {
    return this.messagesConfig;
  }
  private File playersFile;
  private FileConfiguration playersConfig;
  public FileConfiguration getPlayersConfig() {
    return this.playersConfig;
  }

  public List<NewsMessage> messages = new ArrayList();

  @Override
  public void onEnable() {
    messagesFile = new File(getDataFolder(), "messages.yml");
    if (!messagesFile.exists()) {
      messagesFile.getParentFile().mkdirs();
      saveResource("messages.yml", false);
    }
    messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    playersFile = new File(getDataFolder(), "players.yml");
    if (!playersFile.exists()) {
      playersFile.getParentFile().mkdirs();
      saveResource("players.yml", false);
    }
    playersConfig = YamlConfiguration.loadConfiguration(playersFile);

    for (Map.Entry e : getMessagesConfig().getConfigurationSection("messages").getValues(false).entrySet()) {
      messages.add(new NewsMessage((String) e.getValue(), UUID.fromString((String) e.getKey())));
    }
    getLogger().info("Loaded " + messages.size() + " message(s) from config");

    this.getCommand("servernews").setExecutor(new ServerNewsCommand(this));
    Bukkit.getPluginManager().registerEvents(new JoinListener(this), this);
  }

  public void updateMessagesConfig() {
    getMessagesConfig().set("messages", null);
    getMessagesConfig().createSection("messages");
    for (NewsMessage msg : messages) {
      getMessagesConfig().getConfigurationSection("messages").set(msg.getID().toString(), msg.getMessage());
    }
    try {
      messagesConfig.save(messagesFile);
    } catch (java.io.IOException e) {
      getLogger().severe(e.toString());
    }
  }
  public void savePlayers() {
    try {
      playersConfig.save(playersFile);
    } catch (java.io.IOException e) {
      getLogger().severe(e.toString());
    }
  }
  
  public void showInbox(Player player) {
    String id = player.getUniqueId().toString();
    List<String> unreads = getPlayersConfig().getConfigurationSection("players").getStringList(id);
    if (unreads.size() != 0) {
      if (unreads.size() == 1) {
        player.sendMessage("You have 1 unread message:");
      } else {
        player.sendMessage("You have " + unreads.size() + " unread messages:");
      }
      for (int i = 0; i < unreads.size(); i++) {
        player.sendRichMessage("- " + (String) getMessagesConfig().getConfigurationSection("messages").get(unreads.get(i)));
      }
      getPlayersConfig().getConfigurationSection("players").set(id, null);
      getPlayersConfig().getConfigurationSection("players").createSection(id);
      savePlayers();
    }
  }
}
