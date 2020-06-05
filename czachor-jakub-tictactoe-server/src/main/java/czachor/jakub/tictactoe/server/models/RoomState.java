package czachor.jakub.tictactoe.server.models;

import java.util.Arrays;
import java.util.List;

public enum RoomState {
    EMPTY,
    PLAYER_ONE,
    PLAYER_TWO,
    PLAYER_ONE_TURN,
    PLAYER_TWO_TURN,
    PLAYER_ONE_WON,
    PLAYER_TWO_WON,
    PLAYER_ONE_REMATCH,
    PLAYER_TWO_REMATCH,
    DRAW;

    public static List<RoomState> gameEndedStates() {
        return Arrays.asList(
                RoomState.PLAYER_ONE_WON,
                RoomState.PLAYER_TWO_WON,
                RoomState.PLAYER_ONE_REMATCH,
                RoomState.PLAYER_TWO_REMATCH,
                RoomState.DRAW);
    }

    public static List<RoomState> onePlayerInRoom() {
        return Arrays.asList(
                RoomState.PLAYER_ONE,
                RoomState.PLAYER_TWO);
    }

    public static List<RoomState> anyPlayerTurn() {
        return Arrays.asList(
                RoomState.PLAYER_ONE_TURN,
                RoomState.PLAYER_TWO_TURN);
    }
}
