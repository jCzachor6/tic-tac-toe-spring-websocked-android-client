package client.tictactoe.jakub.czachor.tictactoeclient.model;

public class Player {
    private String name;
    private Long games;
    private Long wins;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getGames() {
        return games;
    }

    public void setGames(Long games) {
        this.games = games;
    }

    public Long getWins() {
        return wins;
    }

    public void setWins(Long wins) {
        this.wins = wins;
    }
}
