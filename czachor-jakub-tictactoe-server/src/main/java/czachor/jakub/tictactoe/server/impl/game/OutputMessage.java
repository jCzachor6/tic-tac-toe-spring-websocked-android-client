package czachor.jakub.tictactoe.server.impl.game;

import generic.online.game.server.gogs.model.socket.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@Data
public class OutputMessage extends Message {
    private List<Character> tiles;
    private GameRoomDto room;
    private String gameState;
    private String playerRematch;
    private String playerWon;
    private boolean draw;
    private String playerTurn;
    private boolean playerLeft;

    public OutputMessage(GameRoomState state, GameRoom gameRoom) {
        this.tiles = Optional
                .ofNullable(gameRoom.getBoard())
                .map(Board::getTiles)
                .orElse(Arrays.asList(Board.EMPTY_BOARD));
        this.gameState = state.toString();
        this.room = new GameRoomDto(gameRoom);
    }
}
