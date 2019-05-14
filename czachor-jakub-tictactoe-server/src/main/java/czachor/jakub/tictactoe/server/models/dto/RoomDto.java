package czachor.jakub.tictactoe.server.models.dto;

import czachor.jakub.tictactoe.server.models.RoomState;
import lombok.Data;

import java.util.List;

@Data
public class RoomDto {
    private Long id;
    private RoomState state;

    private String playerOneName;
    private Long playerOneGames;
    private Long playerOneWins;

    private String playerTwoName;
    private Long playerTwoGames;
    private Long playerTwoWins;

    private List<String> tiles;
}
