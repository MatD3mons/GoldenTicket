package fr.matd3mons.net.goldenticket.commands;

import fr.matd3mons.net.goldenticket.managers.GoldenTicketManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

public class Commands implements CommandExecutor, TabCompleter {

    public static boolean giveall(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length == 2 && StringUtils.isNumeric(args[1])) {
            long amount = Long.parseLong(args[1]);
            GoldenTicketManager.giveAllGoldenTicket(amount);
            return true;
        } else {
            sender.sendMessage("§6 /goldenticket giveall [amount]");
            return false;
        }
    }

    public static boolean add(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length >= 2 && StringUtils.isNumeric(args[1])) {
            long amount = Long.parseLong(args[1]);
            Player player = null;

            if(args.length == 3) {
                player = Bukkit.getPlayer(args[2]);
                if (player == null || !player.isOnline()) {
                    sender.sendMessage("§6Le joueur n'est pas en ligne");
                    return false;
                }
            }
            else if(args.length == 2 && sender instanceof Player)
                player = (Player) sender;
            else{
                sender.sendMessage("§6 /goldenticket add [amount] [player]");
                return false;
            }

            GoldenTicketManager.addGoldenTicket(player, amount);
            return true;
        }
        else {
            sender.sendMessage("§6 /goldenticket add [amount] [player]");
            return false;
        }
    }

    public static boolean remove(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length >= 2 && StringUtils.isNumeric(args[1])) {
            long amount = Long.parseLong(args[1]);
            Player player = null;
            if(args.length == 3) {
                player = Bukkit.getPlayer(args[2]);
                if (player == null || !player.isOnline()) {
                    sender.sendMessage("§6Le joueur n'est pas en ligne");
                    return false;
                }
            }
            else if(args.length == 2 && sender instanceof Player)
                player = (Player) sender;
            else{
                sender.sendMessage("§6 /goldenticket remove [amount] [player]");
                return false;
            }

            GoldenTicketManager.removeGoldenTicket(player, amount);
            return true;
        }
        else {
            sender.sendMessage("§6 /goldenticket remove [amount] [player]");
            return false;
        }
    }

    public static boolean info(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length == 2 && StringUtils.isNumeric(args[1])) {
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null || !player.isOnline()) {
                sender.sendMessage("§6Le joueur n'est pas en ligne");
                return false;
            }
            long amount = GoldenTicketManager.getGoldenTicketForPlayer(player);
            player.sendMessage(player.getDisplayName()+"§6 a §e" + amount + " ticket(s) d'or !");
            return true;
        } else {
            sender.sendMessage("§6 /goldenticket info [player]");
            return false;
        }
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender.hasPermission("goldenticket.admin") || sender.isOp()))
            return false;

        if (args.length >= 1) {
            switch (args[0]) {
                case "giveall":
                    return giveall(sender,cmd,label,args);
                case "add":
                    return add(sender,cmd,label,args);
                case "remove":
                    return remove(sender,cmd,label,args);
                case "info":
                    return info(sender,cmd,label,args);
                case "reset":
                    GoldenTicketManager.clearAllGoldenTickets();
            }
        }
        sender.sendMessage("§6 /goldenticket giveall [amount]");
        sender.sendMessage("§6 /goldenticket add [amount] [player]");
        sender.sendMessage("§6 /goldenticket remove [amount] [player]");
        sender.sendMessage("§6 /goldenticket info [player]");
        sender.sendMessage("§6 /goldenticket reset");
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> completionList = new ArrayList<>();

        if(strings.length == 1)
            completionList.addAll(Arrays.asList("giveall", "reset","add","remove", "info"));

        if(strings.length == 2)
            if(strings[0].equals("info"))
                completionList.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));

        if(strings.length == 3)
            if(strings[0].equals("add") || strings[1].equals("remove"))
                completionList.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));


        return StringUtil.copyPartialMatches(strings[strings.length - 1], completionList, new ArrayList<>());
    }
}