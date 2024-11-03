package to.us.electr0n.servernews;

import java.util.List;
import java.util.ArrayList;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import net.kyori.adventure.text.Component;

public class JoinListener implements Listener {
  private ServerNews plugin;
  public JoinListener(ServerNews plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e) {
    boolean isInConfig = false;
    for (String s : plugin.getPlayersConfig().getConfigurationSection("players").getKeys(false)) {
      if (e.getPlayer().getUniqueId().toString().equals(s)) {
        isInConfig = true;
        break;
      }
    }
    if (!isInConfig) {
      List<String> msgs = new ArrayList();
      for (NewsMessage m : plugin.messages) {
        msgs.add(m.getID().toString());
      }
      plugin.getPlayersConfig().getConfigurationSection("players").set(e.getPlayer().getUniqueId().toString(), msgs);
      plugin.savePlayers();
    }

    plugin.showInbox(e.getPlayer());
  }
}
