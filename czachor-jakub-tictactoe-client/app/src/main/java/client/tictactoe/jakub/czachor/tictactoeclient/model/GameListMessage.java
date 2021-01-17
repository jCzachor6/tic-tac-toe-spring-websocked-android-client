package client.tictactoe.jakub.czachor.tictactoeclient.model;

import com.google.gson.Gson;

import java.util.List;

public class GameListMessage {
    private String errorMessage;
    private String state;
    private List<GameRoomDto> rooms;

    public static GameListMessage parse(String json) {
        return new Gson().fromJson(json, GameListMessage.class);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<GameRoomDto> getRooms() {
        return rooms;
    }

    public void setRooms(List<GameRoomDto> rooms) {
        this.rooms = rooms;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
