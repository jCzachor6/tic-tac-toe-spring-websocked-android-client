package czachor.jakub.tictactoe.server.impl.game;

import czachor.jakub.statemachine.Event;
import czachor.jakub.statemachine.StateMachine;
import generic.online.game.server.gogs.api.auth.model.User;
import generic.online.game.server.gogs.model.rooms.Room;
import generic.online.game.server.gogs.model.rooms.RoomInitializerData;
import generic.online.game.server.gogs.utils.annotations.OnConnect;
import generic.online.game.server.gogs.utils.annotations.OnDisconnect;
import generic.online.game.server.gogs.utils.annotations.OnMessage;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Arrays;

import static czachor.jakub.tictactoe.server.impl.game.GameRoomState.*;

@Getter
public class GameRoom extends Room {
    private StateMachine<GameRoomState> gameState;
    private Board board;
    private User playerX;
    private User playerO;
    private String playerTurn;
    private String playerStarted;
    private boolean playerXasksForRematch;
    private boolean playerOasksForRematch;

    @SneakyThrows
    public GameRoom(RoomInitializerData data) {
        super(data);
        this.init();
    }

    private void init() throws Exception {
        gameState = new StateMachine<>(WAITING);
        gameState
                .setTransition(WAITING, WAITING, () -> playerX == null || playerO == null, onWaiting)
                .setTransition(WAITING, TURN, () -> playerX != null && playerO != null && board == null, onGameStarted)
                .setTransition(TURN, TURN, () -> playerX != null && playerO != null && !board.isGameOver(), onUserPlaced)
                .setTransition(TURN, FINISHED, () -> playerX != null && playerO != null && board.isGameOver(), onGameFinished)
                .setTransition(Arrays.asList(TURN, FINISHED), WAITING, () -> playerX == null || playerO == null, onUserLeft)
                .setTransition(FINISHED, TURN, () -> playerXasksForRematch && playerOasksForRematch, onGameStarted);
    }

    @OnConnect
    public void onConnect(User user) {
        if (playerX == null) {
            playerX = user;
            getDynamicRoomListOperations().changed(getRoomId());
        } else if (playerO == null) {
            playerO = user;
            getDynamicRoomListOperations().changed(getRoomId());
        }
        gameState.tick();
    }

    @OnDisconnect
    public void onDisconnect(User user) {
        if (user.equals(playerX)) {
            playerX = null;
            getDynamicRoomListOperations().changed(getRoomId());
        } else if (user.equals(playerO)) {
            playerO = null;
            getDynamicRoomListOperations().changed(getRoomId());
        }
        gameState.tick();
    }

    @OnMessage("SET_TILE")
    public void processSetTileMessage(User user, InputMessage msg) {
        if (!board.isGameOver()) {
            if (user.getUsername().equals(playerTurn) && board.isTileEmpty(msg.getTileIndex())) {
                this.setTile(user, msg.getTileIndex());
                gameState.tick();
            }
        }
    }

    @OnMessage("REMATCH")
    public void processRematchMessage(User user, InputMessage msg) {
        if (board.isGameOver()) {
            if (user.getUsername().equals(playerX.getUsername()) && !playerXasksForRematch) {
                playerXasksForRematch = true;
                onRematchAsk.transition();
            }
            if (user.getUsername().equals(playerO.getUsername()) && !playerOasksForRematch) {
                playerOasksForRematch = true;
                onRematchAsk.transition();
            }
            gameState.tick();
        }
    }

    private void setTile(User user, int tileIndex) {
        if (playerX.equals(user)) {
            board.setTile(tileIndex, 'x');
            playerTurn = playerO.getUsername();
        } else if (playerO.equals(user)) {
            board.setTile(tileIndex, 'o');
            playerTurn = playerX.getUsername();
        }
    }

    private final Event onRematchAsk = () -> {
        OutputMessage outputMessage = new OutputMessage(FINISHED, this);
        outputMessage.setPlayerRematch(playerOasksForRematch ? playerO.getUsername() : playerX.getUsername());
        getMessenger().sendToAll(this, outputMessage);
    };

    private final Event onWaiting = () -> {
        OutputMessage outputMessage = new OutputMessage(WAITING, this);
        getMessenger().sendToAll(this, outputMessage);
    };

    private final Event onGameStarted = () -> {
        board = new Board();
        playerXasksForRematch = false;
        playerOasksForRematch = false;
        OutputMessage outputMessage = new OutputMessage(GameRoomState.TURN, this);
        if (playerStarted == null) {
            playerStarted = playerO.getUsername();
        }
        if (playerX.getUsername().equals(playerStarted)) {
            playerStarted = playerO.getUsername();
            playerTurn = playerO.getUsername();
        } else {
            playerStarted = playerX.getUsername();
            playerTurn = playerX.getUsername();
        }
        outputMessage.setPlayerTurn(playerTurn);
        getMessenger().sendToAll(this, outputMessage);
    };

    private final Event onUserPlaced = () -> {
        OutputMessage msg = new OutputMessage(GameRoomState.TURN, this);
        msg.setPlayerTurn(playerTurn);
        getMessenger().sendToAll(this, msg);
    };

    private final Event onGameFinished = () -> {
        OutputMessage msg = new OutputMessage(FINISHED, this);
        msg.setPlayerTurn(null);
        if (board.xWon()) {
            msg.setPlayerWon(playerX.getUsername());
        } else if (board.oWon()) {
            msg.setPlayerWon(playerO.getUsername());
        } else if (board.draw()) {
            msg.setDraw(true);
        }
        getMessenger().sendToAll(this, msg);
    };

    private final Event onUserLeft = () -> {
        OutputMessage msg = new OutputMessage(WAITING, this);
        msg.setPlayerLeft(true);
        board = null;
        getMessenger().sendToAll(this, msg);
    };
}
