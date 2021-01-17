package client.tictactoe.jakub.czachor.tictactoeclient.model;

import java.io.Serializable;

public class GameRoomDto implements Serializable {
    private String id;
    private String playerOneName;
    private String playerTwoName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlayerOneName() {
        return playerOneName;
    }

    public void setPlayerOneName(String playerOneName) {
        this.playerOneName = playerOneName;
    }

    public String getPlayerTwoName() {
        return playerTwoName;
    }

    public void setPlayerTwoName(String playerTwoName) {
        this.playerTwoName = playerTwoName;
    }
}
