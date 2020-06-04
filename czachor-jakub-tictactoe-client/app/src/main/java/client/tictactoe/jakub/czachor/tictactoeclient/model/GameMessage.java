package client.tictactoe.jakub.czachor.tictactoeclient.model;

import com.google.gson.Gson;

public class GameMessage {
    private MessageType type;
    private String playerName;
    private Long roomId;
    private Long tileIndex;

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getTileIndex() {
        return tileIndex;
    }

    public void setTileIndex(Long tileIndex) {
        this.tileIndex = tileIndex;
    }

    public static GameMessage getRoomsMessage(String playerName) {
        GameMessage message = new GameMessage();
        message.playerName = playerName;
        message.setType(MessageType.ALL);
        return message;
    }

    public static GameMessage getConnectMessage(String playerName) {
        GameMessage message = new GameMessage();
        message.playerName = playerName;
        message.setType(MessageType.CONNECT);
        return message;
    }

    public static GameMessage getJoinGameMessage(String playerName, Long roomId) {
        GameMessage message = new GameMessage();
        message.playerName = playerName;
        message.roomId = roomId;
        message.setType(MessageType.JOIN);
        return message;
    }

    public static GameMessage getActionMessage(String playerName, Long tileIndex, Long roomId) {
        GameMessage message = new GameMessage();
        message.playerName = playerName;
        message.roomId = roomId;
        message.setType(MessageType.ACTION);
        message.setTileIndex(tileIndex);
        return message;
    }

    public static GameMessage getRematchMessage(String playerName, Long roomId) {
        GameMessage message = new GameMessage();
        message.playerName = playerName;
        message.roomId = roomId;
        message.setType(MessageType.REMATCH);
        return message;
    }

    public static GameMessage getLeaveRoomMessage(String playerName) {
        GameMessage message = new GameMessage();
        message.playerName = playerName;
        message.setType(MessageType.LEAVE);
        return message;
    }

    public static GameMessage getRefreshRoomMessage(Long roomId, String playerName) {
        GameMessage message = new GameMessage();
        message.roomId = roomId;
        message.playerName = playerName;
        message.setType(MessageType.REFRESH);
        return message;
    }

    public static GameMessage getTimeoutCheckMessage(String playerName) {
        GameMessage message = new GameMessage();
        message.playerName = playerName;
        message.setType(MessageType.TIMEOUT_CHECK);
        return message;
    }

    public String json() {
        return new Gson().toJson(this);
    }
}
