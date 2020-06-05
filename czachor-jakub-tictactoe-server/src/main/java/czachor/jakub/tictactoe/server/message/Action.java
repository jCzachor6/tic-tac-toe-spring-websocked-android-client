package czachor.jakub.tictactoe.server.message;

import czachor.jakub.tictactoe.server.models.Player;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class Action {
    private final MessageType type;
    private final Player player;
    private final int tileIndex;
    @Setter boolean tileSet;
}

