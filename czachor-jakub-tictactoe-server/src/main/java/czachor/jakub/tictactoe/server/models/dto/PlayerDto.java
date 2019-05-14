package czachor.jakub.tictactoe.server.models.dto;

import lombok.Data;

@Data
public class PlayerDto {
    private String name;
    private Long games;
    private Long wins;
}
