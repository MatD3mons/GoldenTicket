package fr.matd3mons.net.goldenticket.Dto;

import fr.matd3mons.net.minecraftbddapi.API.Dto;

public class PlayerDto  extends Dto {

    private String playerUUID;
    private long ticketCount;

    public PlayerDto(String playerUUID, long ticketCount) {
        this.playerUUID = playerUUID;
        this.ticketCount = ticketCount;
    }

    public PlayerDto() {
    }

    public long getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(long ticketCount) {
        this.ticketCount = ticketCount;
    }

    @Override
    public int dataVersion() {
        return 0;
    }

    @Override
    public String uniqueCollectionName() {
        return "bloodygoldenticket_bloodyplayer";
    }
}
