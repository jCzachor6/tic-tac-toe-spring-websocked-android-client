package czachor.jakub.tictactoe.server.models;

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
}
