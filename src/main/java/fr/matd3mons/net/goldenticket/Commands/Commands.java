package fr.matd3mons.net.goldenticket.Commands;

import fr.matd3mons.net.goldenticket.Manager.GoldenTicketManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender.hasPermission("goldenticket.admin") || sender.isOp()) {
            // goldenticket add player amount
            if(args.length == 2) {
                long amount = Long.parseLong(args[1]);
                if(args[0].equalsIgnoreCase("giveall")) {
                    GoldenTicketManager.giveAllGoldenTicket(amount);
                }
            }
            else if(args.length == 3) {
                long amount = Long.parseLong(args[2]);
                String playerName = args[1];
                Player player = Bukkit.getPlayer(playerName);
                if(player == null || !player.isOnline()) {
                    sender.sendMessage("joueur pas en ligne");
                }

                if(args[0].equalsIgnoreCase("add")) {
                    GoldenTicketManager.addGoldenTicket(player, amount);
                }
                else if(args[0].equalsIgnoreCase("get")) {
                    long a = GoldenTicketManager.getGoldenTicketForPlayer(player);
                    sender.sendMessage("Tu as "+a+" ticket d'or");
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