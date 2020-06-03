package czachor.jakub.tictactoe.server.util;

import czachor.jakub.statemachine.Condition;
import czachor.jakub.statemachine.Event;
import czachor.jakub.tictactoe.server.message.MessageType;
import czachor.jakub.tictactoe.server.models.Player;
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
                () -> r.getPlayerOne() == null && r.getPlayerTwo() == null && r.getCurrentAction().getType().equals(MessageType.JOIN),
                () -> r.setPlayerOne(r.getCurrentAction().getPlayer())
        );
    }

    private static void singlePlayerToBoth(Room r) {
        r.setTransition(RoomState.PLAYER_ONE, RoomState.PLAYER_ONE_TURN,
                () -> r.getPlayerOne() != null && r.getPlayerTwo() == null && r.getCurrentAction().getType().equals(MessageType.JOIN),
                () -> {
                    Player p = r.getCurrentAction().getPlayer();
                    r.setPlayerTwo(p);
                    r.setWhoStarted("x");
                    r.getPlayerTwo().addGames();
                    r.getPlayerOne().addGames();
                    r.clearRoom();
                });
        r.setTransition(RoomState.PLAYER_TWO, RoomState.PLAYER_TWO_TURN,
                () -> r.getPlayerOne() == null && r.getPlayerTwo() != null && r.getCurrentAction().getType().equals(MessageType.JOIN),
                () -> {
                    Player p = r.getCurrentAction().getPlayer();
                    r.setPlayerOne(p);
                    r.setWhoStarted("o");
                    r.getPlayerTwo().addGames();
                    r.getPlayerOne().addGames();
                    r.clearRoom();
                });
    }

    private static void finishedGameToRematch(Room r) throws Exception {
        boolean isPlayerOne = r.getPlayerOne().equals(r.getCurrentAction().getPlayer());
        List<RoomState> gameFinishedStates = Arrays.asList(RoomState.PLAYER_ONE_WON, RoomState.PLAYER_TWO_WON, RoomState.DRAW);
        r.setTransition(gameFinishedStates, RoomState.PLAYER_ONE_REMATCH,
                () -> isPlayerOne && r.getCurrentAction().getType().equals(MessageType.REMATCH), Event.none);
        r.setTransition(gameFinishedStates, RoomState.PLAYER_TWO_REMATCH,
                () -> !isPlayerOne && r.getCurrentAction().getType().equals(MessageType.REMATCH), Event.none);

        Event rematch = () -> {
            r.clearRoom();
            r.getPlayerOne().addGames();
            r.getPlayerTwo().addGames();
        };
        r.setTransition(RoomState.PLAYER_ONE_REMATCH, RoomState.PLAYER_TWO_TURN,
                rematchCondition(r, !isPlayerOne, "x"), rematch);
        r.setTransition(RoomState.PLAYER_ONE_REMATCH, RoomState.PLAYER_ONE_TURN,
                rematchCondition(r, !isPlayerOne, "o"), rematch);
        r.setTransition(RoomState.PLAYER_TWO_REMATCH, RoomState.PLAYER_TWO_TURN,
                rematchCondition(r, isPlayerOne, "x"), rematch);
        r.setTransition(RoomState.PLAYER_TWO_REMATCH, RoomState.PLAYER_ONE_TURN,
                rematchCondition(r, isPlayerOne, "o"), rematch);
    }

    private static Condition rematchCondition(Room r, boolean isPlayerOne, String whoStarted) {
        return () -> isPlayerOne
                && r.getCurrentAction().getType().equals(MessageType.REMATCH)
                && r.getWhoStarted().equals(whoStarted);
    }

    private static void statesOnLeaveRoom(Room r) throws Exception {
        boolean isPlayerOne = r.getPlayerOne().equals(r.getCurrentAction().getPlayer());
        List<RoomState> gameEndedStates = Arrays.asList(
                RoomState.PLAYER_ONE_WON,
                RoomState.PLAYER_TWO_WON,
                RoomState.PLAYER_ONE_REMATCH,
                RoomState.PLAYER_TWO_REMATCH,
                RoomState.DRAW);
        r.setTransition(gameEndedStates, RoomState.PLAYER_TWO,
                () -> isPlayerOne, () -> r.setPlayerOne(null));
        r.setTransition(gameEndedStates, RoomState.PLAYER_ONE,
                () -> !isPlayerOne, () -> r.setPlayerTwo(null));

        List<RoomState> onePlayer = Arrays.asList(
                RoomState.PLAYER_ONE,
                RoomState.PLAYER_TWO);
        r.setTransition(onePlayer, RoomState.EMPTY,
                () -> r.getCurrentAction().getType().equals(MessageType.LEAVE),
                () -> {
                    r.setPlayerOne(null);
                    r.setPlayerTwo(null);
                });
        r.setTransition(RoomState.PLAYER_ONE_TURN, RoomState.PLAYER_TWO,
                () -> isPlayerOne && r.getCurrentAction().getType().equals(MessageType.LEAVE),
                () -> {
                    r.setPlayerOne(null);
                    r.getPlayerTwo().addWin();
                });
        r.setTransition(RoomState.PLAYER_TWO_TURN, RoomState.PLAYER_TWO,
                () -> !isPlayerOne && r.getCurrentAction().getType().equals(MessageType.LEAVE),
                () -> {
                    r.setPlayerTwo(null);
                    r.getPlayerOne().addWin();
                });
    }

    private static void statesAfterSetTile(Room r) throws Exception {
        boolean isPlayerOne = r.getPlayerOne().equals(r.getCurrentAction().getPlayer());
        List<RoomState> onePlayer = Arrays.asList(
                RoomState.PLAYER_ONE,
                RoomState.PLAYER_TWO);
        r.setTransition(onePlayer, RoomState.DRAW,
                r::checkDraw,
                Event.none);
        r.setTransition(RoomState.PLAYER_ONE_TURN, RoomState.PLAYER_ONE_WON,
                () -> r.checkTiles("x"),
                () -> r.getPlayerOne().addGames());
        r.setTransition(RoomState.PLAYER_TWO_TURN, RoomState.PLAYER_TWO_WON,
                () -> r.checkTiles("o"),
                () -> r.getPlayerTwo().addGames());
    }
}
