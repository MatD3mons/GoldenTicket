package fr.matd3mons.net.goldenticket.events;

import fr.matd3mons.net.goldenticket.managers.GoldenTicketManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionsEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        GoldenTicketManager.onPlayerJoin(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        GoldenTicketManager.onPlayerQuit(e.getPlayer());
    }
}
