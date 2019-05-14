package czachor.jakub.tictactoe.server.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class Room {
    private static Long idCounter = 1L;
    public static String[] EMPTY_ROOM = {"n", "n", "n", "n", "n", "n", "n", "n", "n"};

    private Long id;
    private Player playerOne;
    private Player playerTwo;
    private String whoStarted;
    private List<String> tiles; //n - none, x, o
    private RoomState state;

    public Room() {
        this.id = Room.idCounter;
        this.playerOne = null;
        this.playerTwo = null;
        this.whoStarted = null;
        this.tiles = Arrays.asList(Room.EMPTY_ROOM);
        this.state = RoomState.EMPTY;

        Room.idCounter++;
    }

    public void setTile(int index, Boolean isPlayerOne) {
        List<String> newTiles = new ArrayList<>(9);
        for (int i = 0; i < tiles.size(); i++) {
            if (i != index) {
                newTiles.add(tiles.get(i));
            } else {
                if (isPlayerOne.equals(true)) {
                    newTiles.add("x");
                } else {
                    newTiles.add("o");
                }
            }
        }
        this.tiles = newTiles;
    }

    public void rematch() {
        this.clearRoom();
        switch (whoStarted) {
            case "x":
                this.state = RoomState.PLAYER_TWO_TURN;
                break;
            case "o":
                this.state = RoomState.PLAYER_ONE_TURN;
                break;
        }
    }

    public void clearRoom(){
        this.tiles = Arrays.asList(Room.EMPTY_ROOM);
    }
}
