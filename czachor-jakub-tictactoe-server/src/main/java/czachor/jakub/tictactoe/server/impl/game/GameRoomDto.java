package czachor.jakub.tictactoe.server.impl.game;

import generic.online.game.server.gogs.model.auth.User;
import lombok.Data;

@Data
public class GameRoomDto {
    private String id;
    private String playerOneName;
    private String playerTwoName;

    public GameRoomDto(GameRoom r) {
        id = r.getRoomId();
        User user1 = r.getPlayerX();
        if (user1 != null) {
            playerOneName = user1.getUsername();
        }
        User user2 = r.getPlayerO();
        if (user2 != null) {
            playerTwoName = user2.getUsername();
        }
    }
}
