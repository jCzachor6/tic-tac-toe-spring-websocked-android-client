package czachor.jakub.tictactoe.server.service;

import czachor.jakub.tictactoe.server.models.Player;
import czachor.jakub.tictactoe.server.models.Room;
import czachor.jakub.tictactoe.server.models.RoomState;
import czachor.jakub.tictactoe.server.repository.PlayerRepository;
import czachor.jakub.tictactoe.server.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAll() {
        return this.roomRepository.getAll();
    }

    public Room getById(Long id) {
        return this.roomRepository.getById(id).orElse(null);
    }

    public Room joinRoom(Player player, Long roomId) {
        Room room = this.roomRepository.getById(roomId).get();
        if (!RoomState.isFull(room.getState())) {
            this.leaveRoom(player.getName());
            player.setTimeoutCheck(new Date());
            switch (room.getState()) {
                case EMPTY:
                    room.setPlayerOne(player);
                    room.setState(RoomState.PLAYER_ONE);
                    break;
                case PLAYER_TWO:
                    room.setPlayerOne(player);
                    room.setState(RoomState.PLAYER_TWO_TURN);
                    room.setWhoStarted("o");
                    room.getPlayerOne().addGames();
                    room.getPlayerTwo().addGames();
                    room.clearRoom();
                    break;
                case PLAYER_ONE:
                    room.setPlayerTwo(player);
                    room.setState(RoomState.PLAYER_ONE_TURN);
                    room.setWhoStarted("x");
                    room.getPlayerTwo().addGames();
                    room.getPlayerOne().addGames();
                    room.clearRoom();
                    break;
            }
        }
        return room;
    }

    public Room rematch(String playerName, Long roomId) {
        Room room = this.roomRepository.getById(roomId).get();
        Boolean isPlayerOne = room.getPlayerOne().getName().equals(playerName);
        if (RoomState.isFinished(room.getState())) {
            if (isPlayerOne) {
                room.setState(RoomState.PLAYER_ONE_REMATCH);
            } else {
                room.setState(RoomState.PLAYER_TWO_REMATCH);
            }
        }
        if ((room.getState().equals(RoomState.PLAYER_ONE_REMATCH) && !isPlayerOne) || (room.getState().equals(RoomState.PLAYER_TWO_REMATCH) && isPlayerOne)) {
            room.rematch();
            room.getPlayerTwo().addGames();
            room.getPlayerOne().addGames();
        }
        return room;
    }

    public Room leaveRoom(String playerName) {
        Optional<Room> roomOptional = this.roomRepository.findRoomByPlayerName(playerName);
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            Boolean isPlayerOne = room.getPlayerOne().getName().equals(playerName);
            switch (room.getState()) {
                case PLAYER_ONE:
                    room.setPlayerOne(null);
                    room.setState(RoomState.EMPTY);
                    break;
                case PLAYER_TWO:
                    room.setPlayerTwo(null);
                    room.setState(RoomState.EMPTY);
                    break;
                case PLAYER_ONE_TURN:
                case PLAYER_TWO_TURN:
                    if (isPlayerOne) {
                        room.setPlayerOne(null);
                        room.setState(RoomState.PLAYER_TWO);
                        room.getPlayerTwo().addWin();
                    } else {
                        room.setPlayerTwo(null);
                        room.setState(RoomState.PLAYER_ONE);
                        room.getPlayerOne().addWin();
                    }
                    break;
                case DRAW:
                case PLAYER_ONE_WON:
                case PLAYER_TWO_WON:
                case PLAYER_ONE_REMATCH:
                case PLAYER_TWO_REMATCH:
                    if (isPlayerOne) {
                        room.setPlayerOne(null);
                        room.setState(RoomState.PLAYER_TWO);
                    } else {
                        room.setPlayerTwo(null);
                        room.setState(RoomState.PLAYER_ONE);
                    }
                    break;
            }
            return room;
        }
        return null;
    }

    public Room setTile(String playerName, int tileIndex, Long roomId) {
        Room room = this.roomRepository.getById(roomId).get();
        Boolean isPlayerOne = room.getPlayerOne().getName().equals(playerName);
        if (isPlayerOne && room.getState().equals(RoomState.PLAYER_ONE_TURN) && room.getTiles().get(tileIndex).equals("n")) {
            room.setTile(tileIndex, true);
            room.setState(RoomState.PLAYER_TWO_TURN);
            if (checkTiles(room.getTiles(), "x")) {
                room.getPlayerOne().addWin();
                room.setState(RoomState.PLAYER_ONE_WON);
            }
        } else if (!isPlayerOne && room.getState().equals(RoomState.PLAYER_TWO_TURN) && room.getTiles().get(tileIndex).equals("n")) {
            room.setTile(tileIndex, false);
            room.setState(RoomState.PLAYER_ONE_TURN);
            if (checkTiles(room.getTiles(), "o")) {
                room.getPlayerTwo().addWin();
                room.setState(RoomState.PLAYER_TWO_WON);
            }
        }
        if(checkDraw(room.getTiles())){
            room.setState(RoomState.DRAW);
        }
        return room;
    }

    private Boolean checkTiles(List<String> tiles, String state) {
        //0|1|2
        //3|4|5
        //6|7|8
        return checkLine(0, 1, 2, state, tiles)
                || checkLine(3, 4, 5, state, tiles)
                || checkLine(6, 7, 8, state, tiles)
                || checkLine(0, 3, 6, state, tiles)
                || checkLine(1, 4, 7, state, tiles)
                || checkLine(2, 5, 8, state, tiles)
                || checkLine(0, 4, 8, state, tiles)
                || checkLine(2, 4, 6, state, tiles);
    }

    private Boolean checkLine(int tile1, int tile2, int tile3, String state, List<String> tiles) {
        return tiles.get(tile1).equals(state) && tiles.get(tile2).equals(state) && tiles.get(tile3).equals(state);
    }

    private Boolean checkDraw(List<String> tiles){
        return tiles.stream().noneMatch(tile -> tile.equals("n"));
    }
}
