package fr.matd3mons.net.goldenticket.commands;

import fr.matd3mons.net.goldenticket.managers.GoldenTicketManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // goldenticket giveall 1
        // goldenticket reset
        // goldenticket give 1 MatD3mons
        // goldenticket remove 1 MatD3mons
        // goldenticket get MatD3mons

        if(sender.hasPermission("goldenticket.admin") || sender.isOp()) {
            if(args.length == 2) {
                if(args[0].equalsIgnoreCase("giveall")) {
                    long amount = Long.parseLong(args[1]);
                    GoldenTicketManager.giveAllGoldenTicket(amount);
                }
                else if(args[0].equalsIgnoreCase("get")){
                    String playerName = args[1];
                    Player player = Bukkit.getPlayer(playerName);
                    if(player == null || !player.isOnline()) {
                        sender.sendMessage("joueur pas en ligne");
                        return false;
                    }
                    long amount = GoldenTicketManager.getGoldenTicketForPlayer(player);
                    player.sendMessage("");
                    player.sendMessage("§6 Tu as §e" + amount + " ticket(s) d'or !");
                    player.sendMessage("");
                }
            }
            else if(args.length == 3) {
                long amount = Long.parseLong(args[1]);
                String playerName = args[2];
                Player player = Bukkit.getPlayer(playerName);
                if(player == null || !player.isOnline()) {
                    sender.sendMessage("joueur pas en ligne");
                }

                if(args[0].equalsIgnoreCase("add")) {
                    GoldenTicketManager.addGoldenTicket(player, amount);
                }
                else if(args[0].equalsIgnoreCase("remove")) {
                    GoldenTicketManager.removeGoldenTicket(player, amount);
                }
            }
            else if(args.length == 1) {
                if(args[0].equalsIgnoreCase("reset")) {
                    GoldenTicketManager.clearAllGoldenTickets();
                }
            }
        }

        return false;
    }
}