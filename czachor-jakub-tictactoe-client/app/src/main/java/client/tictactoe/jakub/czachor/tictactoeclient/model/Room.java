package client.tictactoe.jakub.czachor.tictactoeclient.model;


import java.io.Serializable;
import java.util.List;

public class Room implements Serializable {
    private Long id;
    private RoomState state;

    private String playerOneName;
    private Long playerOneGames;
    private Long playerOneWins;
    private String playerOneType;

    private String playerTwoName;
    private Long playerTwoGames;
    private Long playerTwoWins;
    private String playerTwoType;

    private List<String> tiles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoomState getState() {
        return state;
    }

    public void setState(RoomState state) {
        this.state = state;
    }

    public String getPlayerOneName() {
        return playerOneName;
    }

    public void setPlayerOneName(String playerOneName) {
        this.playerOneName = playerOneName;
    }

    public Long getPlayerOneGames() {
        return playerOneGames;
    }

    public void setPlayerOneGames(Long playerOneGames) {
        this.playerOneGames = playerOneGames;
    }

    public Long getPlayerOneWins() {
        return playerOneWins;
    }

    public void setPlayerOneWins(Long playerOneWins) {
        this.playerOneWins = playerOneWins;
    }

    public String getPlayerOneType() {
        return playerOneType;
    }

    public void setPlayerOneType(String playerOneType) {
        this.playerOneType = playerOneType;
    }

    public String getPlayerTwoName() {
        return playerTwoName;
    }

    public void setPlayerTwoName(String playerTwoName) {
        this.playerTwoName = playerTwoName;
    }

    public Long getPlayerTwoGames() {
        return playerTwoGames;
    }

    public void setPlayerTwoGames(Long playerTwoGames) {
        this.playerTwoGames = playerTwoGames;
    }

    public Long getPlayerTwoWins() {
        return playerTwoWins;
    }

    public void setPlayerTwoWins(Long playerTwoWins) {
        this.playerTwoWins = playerTwoWins;
    }

    public String getPlayerTwoType() {
        return playerTwoType;
    }

    public void setPlayerTwoType(String playerTwoType) {
        this.playerTwoType = playerTwoType;
    }

    public List<String> getTiles() {
        return tiles;
    }

    public void setTiles(List<String> tiles) {
        this.tiles = tiles;
    }
}
