package czachor.jakub.tictactoe.server.util;

import czachor.jakub.tictactoe.server.models.Player;
import czachor.jakub.tictactoe.server.models.Room;
import czachor.jakub.tictactoe.server.models.dto.PlayerDto;
import czachor.jakub.tictactoe.server.models.dto.RoomDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Mapper {
    public static RoomDto map(Room room) {
        RoomDto dto = new RoomDto();
        dto.setId(room.getId());
        dto.setTiles(new ArrayList<>(room.getTiles()));
        Player p1 = room.getPlayerOne();
        if (p1 != null) {
            dto.setPlayerOneName(p1.getName());
            dto.setPlayerOneGames(p1.getGames());
            dto.setPlayerOneWins(p1.getWins());
        }
        Player p2 = room.getPlayerTwo();
        if (p2 != null) {
            dto.setPlayerTwoName(p2.getName());
            dto.setPlayerTwoGames(p2.getGames());
            dto.setPlayerTwoWins(p2.getWins());
        }
        dto.setState(room.getState());
        return dto;
    }

    public static RoomDto mapLookup(Room room) {
        RoomDto dto = new RoomDto();
        dto.setId(room.getId());
        Player p1 = room.getPlayerOne();
        if (p1 != null) {
            dto.setPlayerOneName(p1.getName());
        }
        Player p2 = room.getPlayerTwo();
        if (p2 != null) {
            dto.setPlayerTwoName(p2.getName());
        }
        dto.setState(room.getState());
        return dto;
    }

    public static List<RoomDto> mapLookup(List<Room> rooms) {
        return rooms.stream().map(Mapper::mapLookup).collect(Collectors.toList());
    }

    public static PlayerDto map(Player player) {
        PlayerDto dto = new PlayerDto();
        dto.setName(player.getName());
        dto.setGames(player.getGames());
        dto.setWins(player.getWins());
        return dto;
    }
}
