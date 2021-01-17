package client.tictactoe.jakub.czachor.tictactoeclient.model;

import com.google.gson.Gson;

public class TileIndexMessage {
    private final int tileIndex;

    public TileIndexMessage(int tileIndex) {
        this.tileIndex = tileIndex;
    }

    public String json() {
        return new Gson().toJson(this);
    }
}
