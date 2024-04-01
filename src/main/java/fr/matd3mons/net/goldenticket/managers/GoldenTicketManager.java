package fr.matd3mons.net.goldenticket.managers;

import fr.matd3mons.net.goldenticket.dto.PlayerDto;
import fr.matd3mons.net.goldenticket.GoldenTicket;
import fr.matd3mons.net.minecraftbddapi.Core.MinecraftDao;
import fr.matd3mons.net.minecraftbddapi.MinecraftBddAPI;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GoldenTicketManager {

    private static MinecraftDao<PlayerDto> dao;
    private static Map<Player, PlayerDto> playerDtoMap;

    public static void init() {
        dao = MinecraftBddAPI.getController().getOrCreateDao(PlayerDto.class);
        playerDtoMap = new ConcurrentHashMap<>();
    }

    public static void onPlayerJoin(Player player) {
        String playerId = player.getUniqueId().toString();
        dao.getAsync(playerId).thenAsync(bloodyPlayerDto -> {
            if(bloodyPlayerDto == null) {
                PlayerDto dto = new PlayerDto(playerId, 0);
                dao.save(playerId, dto);
                playerDtoMap.put(player, dto);
            }
            else {
                playerDtoMap.put(player, bloodyPlayerDto);
            }
        });
    }

    public static void onPlayerQuit(Player player) {
        playerDtoMap.remove(player);
    }

    public static void addGoldenTicket(Player p, long amount) {
        if(playerDtoMap.containsKey(p)) {
            PlayerDto bloodyPlayerDto = playerDtoMap.get(p);
            bloodyPlayerDto.setTicketCount(bloodyPlayerDto.getTicketCount() + amount);
            dao.updateAsync(p.getUniqueId().toString(), bloodyPlayerDto).then(() -> {
                p.sendMessage("");
                p.sendMessage("§6Félicitation, tu viens d'obtenir §e" + amount + " ticket(s) d'or !");
                p.sendMessage("§7Tu as une chance de remporter un cashprize ! (infos discord)");
                p.sendMessage("");
                p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 1, 0.5f);
            });
        }
    }

    public static void giveAllGoldenTicket(long amount) {
        Bukkit.getScheduler().runTaskAsynchronously(GoldenTicket.instance, ()-> {
            Bukkit.getOnlinePlayers().forEach(p -> {
                if(playerDtoMap.containsKey(p)) {
                    PlayerDto bloodyPlayerDto = playerDtoMap.get(p);
                    bloodyPlayerDto.setTicketCount(bloodyPlayerDto.getTicketCount() + amount);
                    dao.update(p.getUniqueId().toString(), bloodyPlayerDto);
                }
                p.playSound(p.getLocation(), Sound.VILLAGER_YES, 1, 1);
            });
        });
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("§6§l GIVE-ALL DE TICKETS D'OR");
        Bukkit.broadcastMessage("§6Félicitation, tu viens d'obtenir §e" + amount + " ticket(s) d'or !");
        Bukkit.broadcastMessage("§7Tu as une chance de remporter un cashprize ! (infos discord)");
        Bukkit.broadcastMessage("");
    }


    public static void removeGoldenTicket(Player p, long amount) {
        if(playerDtoMap.containsKey(p)) {
            PlayerDto bloodyPlayerDto = playerDtoMap.get(p);
            bloodyPlayerDto.setTicketCount(bloodyPlayerDto.getTicketCount() - amount);
            dao.updateAsync(p.getUniqueId().toString(), bloodyPlayerDto);
        }
    }

    public static void clearAllGoldenTickets() {
        Bukkit.broadcastMessage("§cReset des tickets d'or en cours..");
        dao.deleteAll();
        playerDtoMap.clear();
        Bukkit.broadcastMessage("§eLes tickets d'or ont été reset, bonne chance pour les prochains cashprize");
    }

    public static long getGoldenTicketForPlayer(Player p) {
        if(playerDtoMap.containsKey(p)) {
            return playerDtoMap.get(p).getTicketCount();
        }
        return 0;
    }
}
