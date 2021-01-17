package client.tictactoe.jakub.czachor.tictactoeclient.model;

import com.google.gson.Gson;

import java.util.List;

public class GameMessage {
    private List<Character> tiles;
    private String gameState;
    private GameRoomDto room;
    private String playerRematch;
    private String playerWon;
    private boolean draw;
    private String playerTurn;
    private boolean playerLeft;

    public static GameMessage parse(String json) {
        return new Gson().fromJson(json, GameMessage.class);
    }

    public List<Character> getTiles() {
        return tiles;
    }

    public void setTiles(List<Character> tiles) {
        this.tiles = tiles;
    }

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public String getPlayerRematch() {
        return playerRematch;
    }

    public void setPlayerRematch(String playerRematch) {
        this.playerRematch = playerRematch;
    }

    public String getPlayerWon() {
        return playerWon;
    }

    public void setPlayerWon(String playerWon) {
        this.playerWon = playerWon;
    }

    public String getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(String playerTurn) {
        this.playerTurn = playerTurn;
    }

    public boolean isPlayerLeft() {
        return playerLeft;
    }

    public void setPlayerLeft(boolean playerLeft) {
        this.playerLeft = playerLeft;
    }

    public GameRoomDto getRoom() {
        return room;
    }

    public void setRoom(GameRoomDto room) {
        this.room = room;
    }

    public boolean isDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }
}
