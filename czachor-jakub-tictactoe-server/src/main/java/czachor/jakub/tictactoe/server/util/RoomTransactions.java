package czachor.jakub.tictactoe.server.util;

import czachor.jakub.statemachine.Event;
import czachor.jakub.tictactoe.server.message.MessageType;
import czachor.jakub.tictactoe.server.models.Room;
import czachor.jakub.tictactoe.server.models.RoomState;

import java.util.Arrays;
import java.util.List;

public class RoomTransactions {
    public static void initTransitions(Room room) {
        try {
            emptyToFirstPlayer(room);
            singlePlayerToBoth(room);
            finishedGameToRematch(room);
            statesOnLeaveRoom(room);
            statesAfterSetTile(room);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    private static void emptyToFirstPlayer(Room r) {
        r.setTransition(RoomState.EMPTY, RoomState.PLAYER_ONE,
                () -> r.getPlayerOne() == null && r.getPlayerTwo() == null && r.is(MessageType.JOIN),
                () -> r.setPlayerOne(r.getCurrentAction().getPlayer())
        );
    }

    private static void singlePlayerToBoth(Room r) {
        r.setTransition(RoomState.PLAYER_ONE, RoomState.PLAYER_ONE_TURN,
                () -> r.getPlayerOne() != null && r.getPlayerTwo() == null && r.is(MessageType.JOIN),
                () -> {
                    r.setWhoStarted("o");
                    r.setPlayerTwo(r.getCurrentAction().getPlayer());
                    r.getPlayerTwo().addGames();
                    r.getPlayerOne().addGames();
                    r.clearRoom();
                });
        r.setTransition(RoomState.PLAYER_TWO, RoomState.PLAYER_TWO_TURN,
                () -> r.getPlayerOne() == null && r.getPlayerTwo() != null && r.is(MessageType.JOIN),
                () -> {
                    r.setWhoStarted("x");
                    r.setPlayerOne(r.getCurrentAction().getPlayer());
                    r.getPlayerTwo().addGames();
                    r.getPlayerOne().addGames();
                    r.clearRoom();
                });
    }

    private static void finishedGameToRematch(Room r) throws Exception {
        List<RoomState> gameFinishedStates = Arrays.asList(RoomState.PLAYER_ONE_WON, RoomState.PLAYER_TWO_WON, RoomState.DRAW);
        r.setTransition(gameFinishedStates, RoomState.PLAYER_ONE_REMATCH,
                () -> r.currentPlayerOne() && r.is(MessageType.REMATCH), Event.none);
        r.setTransition(gameFinishedStates, RoomState.PLAYER_TWO_REMATCH,
                () -> r.currentPlayerTwo() && r.is(MessageType.REMATCH), Event.none);

        Event rematch = () -> {
            r.clearRoom();
            r.getPlayerOne().addGames();
            r.getPlayerTwo().addGames();
        };
        r.setTransition(RoomState.PLAYER_ONE_REMATCH, RoomState.PLAYER_TWO_TURN,
                () -> r.currentPlayerTwo() && r.getWhoStarted().equals("o") && r.is(MessageType.REMATCH),
                () -> {
                    rematch.transition();
                    r.setWhoStarted("x");
                });
        r.setTransition(RoomState.PLAYER_ONE_REMATCH, RoomState.PLAYER_ONE_TURN,
                () -> r.currentPlayerTwo() && r.getWhoStarted().equals("x") && r.is(MessageType.REMATCH),
                () -> {
                    rematch.transition();
                    r.setWhoStarted("o");
                });
        r.setTransition(RoomState.PLAYER_TWO_REMATCH, RoomState.PLAYER_TWO_TURN,
                () -> r.currentPlayerOne() && r.getWhoStarted().equals("o") && r.is(MessageType.REMATCH),
                () -> {
                    rematch.transition();
                    r.setWhoStarted("x");
                });
        r.setTransition(RoomState.PLAYER_TWO_REMATCH, RoomState.PLAYER_ONE_TURN,
                () -> r.currentPlayerOne() && r.getWhoStarted().equals("x") && r.is(MessageType.REMATCH),
                () -> {
                    rematch.transition();
                    r.setWhoStarted("o");
                });
    }

    private static void statesOnLeaveRoom(Room r) throws Exception {
        r.setTransition(RoomState.gameEndedStates(), RoomState.PLAYER_TWO,
                () -> r.currentPlayerOne() && r.is(MessageType.LEAVE),
                () -> r.setPlayerOne(null));
        r.setTransition(RoomState.gameEndedStates(), RoomState.PLAYER_ONE,
                () -> r.currentPlayerTwo() && r.is(MessageType.LEAVE),
                () -> r.setPlayerTwo(null));
        r.setTransition(RoomState.onePlayerInRoom(), RoomState.EMPTY,
                () -> r.is(MessageType.LEAVE),
                () -> {
                    r.setPlayerOne(null);
                    r.setPlayerTwo(null);
                });
        r.setTransition(RoomState.anyPlayerTurn(), RoomState.PLAYER_TWO,
                () -> r.currentPlayerOne() && r.is(MessageType.LEAVE),
                () -> {
                    r.setPlayerOne(null);
                    r.getPlayerTwo().addWin();
                });
        r.setTransition(RoomState.anyPlayerTurn(), RoomState.PLAYER_ONE,
                () -> r.currentPlayerTwo() && r.is(MessageType.LEAVE),
                () -> {
                    r.setPlayerTwo(null);
                    r.getPlayerOne().addWin();
                });
    }

    private static void statesAfterSetTile(Room r) throws Exception {
        r.setTransition(RoomState.anyPlayerTurn(), RoomState.DRAW, r::checkDraw, Event.none);
        r.setTransition(RoomState.PLAYER_ONE_TURN, RoomState.PLAYER_ONE_WON,
                () -> r.checkTiles("x") && r.is(MessageType.ACTION),
                () -> r.getPlayerOne().addWin());
        r.setTransition(RoomState.PLAYER_TWO_TURN, RoomState.PLAYER_TWO_WON,
                () -> r.checkTiles("o") && r.is(MessageType.ACTION),
                () -> r.getPlayerTwo().addWin());
        r.setTransition(RoomState.PLAYER_ONE_TURN, RoomState.PLAYER_TWO_TURN,
                () -> !r.checkTiles("x") && !r.checkTiles("o") && r.getCurrentAction().isTileSet() && r.is(MessageType.ACTION),
                Event.none);
        r.setTransition(RoomState.PLAYER_TWO_TURN, RoomState.PLAYER_ONE_TURN,
                () -> !r.checkTiles("x") && !r.checkTiles("o") && r.getCurrentAction().isTileSet() && r.is(MessageType.ACTION),
                Event.none);
    }
}
