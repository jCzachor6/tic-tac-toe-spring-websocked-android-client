package czachor.jakub.tictactoe.server.message;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class GameMessage {
    private MessageType type;
    private String playerName;
    private Long roomId;
    private int tileIndex;
}
