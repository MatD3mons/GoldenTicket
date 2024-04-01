package fr.matd3mons.net.goldenticket;

import fr.matd3mons.net.goldenticket.commands.Commands;
import fr.matd3mons.net.goldenticket.events.ConnectionsEvents;
import fr.matd3mons.net.goldenticket.managers.GoldenTicketManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class GoldenTicket extends JavaPlugin {

    public static Plugin instance;
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        getServer().getPluginManager().registerEvents(new ConnectionsEvents(), this);
        this.getCommand("goldenTicket").setExecutor(new Commands());


        GoldenTicketManager.init();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
