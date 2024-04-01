package fr.matd3mons.net.goldenticket.commands;

import fr.matd3mons.net.goldenticket.managers.GoldenTicketManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

public class Commands implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender.hasPermission("goldenticket.admin") || sender.isOp())) {
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage("§6 /goldenticket giveall [amount]");
            sender.sendMessage("§6 /goldenticket add [amount] [player]");
            sender.sendMessage("§6 /goldenticket remove [amount] [player]");
            sender.sendMessage("§6 /goldenticket give [player]");
            sender.sendMessage("§6 /goldenticket reset");
        }
        else if (args.length == 1) {
            switch (args[0]) {
                case "giveall":
                    sender.sendMessage("§6 /goldenticket giveall [amount]");
                    break;
                case "add":
                    sender.sendMessage("§6 /goldenticket add [amount] [player]");
                    break;
                case "remove":
                    sender.sendMessage("§6 /goldenticket remove [amount] [player]");
                    break;
                case "get":
                    sender.sendMessage("§6 /goldenticket give [player]");
                    break;
                case "reset":
                    GoldenTicketManager.clearAllGoldenTickets();
                    break;
            }
        } else if (args.length == 2) {
            if (StringUtils.isNumeric(args[1]) && args[0].equals("giveall")) {
                long amount = Long.parseLong(args[1]);
                GoldenTicketManager.giveAllGoldenTicket(amount);
            } else if (args[0].equals("get")) {
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null || !player.isOnline()) {
                    sender.sendMessage("§6 joueur pas en ligne");
                    return false;
                }
                long amount = GoldenTicketManager.getGoldenTicketForPlayer(player);
                player.sendMessage("§6 Tu as §e" + amount + " ticket(s) d'or !");
            }
        }
        else if (args.length == 3) {
            if (StringUtils.isNumeric(args[1]) && (args[0].equals("add") || args[0].equals("remove"))) {
                long amount = Long.parseLong(args[1]);
                Player player = Bukkit.getPlayer(args[2]);
                if (player == null || !player.isOnline()) {
                    sender.sendMessage("§6 joueur pas en ligne");
                    return false;
                }
                switch (args[0]) {
                    case "add":
                        GoldenTicketManager.addGoldenTicket(player, amount);
                        break;
                    case "remove":
                        GoldenTicketManager.removeGoldenTicket(player, amount);
                        break;
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> completionList = new ArrayList<>();

        if(strings.length == 1)
            completionList.addAll(Arrays.asList("giveall", "reset","add","remove", "get"));

        if(strings.length == 2)
            if(strings[0].equals("get"))
                completionList.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));

        if(strings.length == 3)
            if(strings[0].equals("add") || strings[1].equals("remove"))
                completionList.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));


        return StringUtil.copyPartialMatches(strings[strings.length - 1], completionList, new ArrayList<>());
    }
}