package czachor.jakub.tictactoe.server.models;

import lombok.Data;

@Data
public class Player {
    private String name;
    private Long games;
    private Long wins;
    private Boolean rematch;

    public Player(String name) {
        this.name = name;
        this.games = 0L;
        this.wins = 0L;
        this.rematch = false;
    }

    public void addWin() {
        this.wins++;
    }

    public void addGames() {
        this.games++;
    }
}
