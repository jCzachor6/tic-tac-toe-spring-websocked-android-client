package client.tictactoe.jakub.czachor.tictactoeclient.fragments;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import client.tictactoe.jakub.czachor.tictactoeclient.R;
import client.tictactoe.jakub.czachor.tictactoeclient.TicTacToeApplication;
import client.tictactoe.jakub.czachor.tictactoeclient.model.GameMessage;
import client.tictactoe.jakub.czachor.tictactoeclient.model.Room;
import client.tictactoe.jakub.czachor.tictactoeclient.utils.StringUtils;

public class GameFragment extends Fragment {
    private Handler handler;
    private Runnable timeoutCheck;

    private Room currentRoom;
    private String playerName;

    private TextView playerNames;
    private TextView playerScores;
    private TextView roomMessage;

    private Button leaveButton;
    private Button rematchButton;

    private List<ImageView> tiles = new ArrayList<>(9);
    private Boolean tilesInitialized = false;

    public GameFragment() {
    }

    public static GameFragment newInstance(Room room, String playerName) {
        GameFragment f = new GameFragment();
        Bundle args = new Bundle();
        args.putSerializable("room", room);
        args.putString("playerName", playerName);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loadArgs();
        this.subscribeRoom();
        this.initTimeoutCheck();
    }

    private void initTimeoutCheck() {
        handler = new Handler();
        timeoutCheck = () -> {
            GameMessage msg = GameMessage.getTimeoutCheckMessage(this.playerName);
            TicTacToeApplication
                    .instance()
                    .getStompClient()
                    .send("/topic/tictactoe", msg.json()).subscribe();
            if (timeoutCheck != null) {
                Message m = Message.obtain(handler, timeoutCheck);
                handler.sendMessageDelayed(m, 4000);
            }
        };
        Message m = Message.obtain(handler, timeoutCheck);
        handler.sendMessageDelayed(m, 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        this.initView(view);
        this.initTiles(view);
        this.refreshRoom();
        return view;
    }

    private void refreshRoom() {
        GameMessage msg = GameMessage.getRefreshRoomMessage(currentRoom.getId(), playerName);
        TicTacToeApplication
                .instance()
                .getStompClient()
                .send("/topic/tictactoe", msg.json()).subscribe();
    }

    private void initTiles(View view) {
        tiles.addAll(Arrays.asList(
                view.findViewById(R.id.tile0),
                view.findViewById(R.id.tile1),
                view.findViewById(R.id.tile2),
                view.findViewById(R.id.tile3),
                view.findViewById(R.id.tile4),
                view.findViewById(R.id.tile5),
                view.findViewById(R.id.tile6),
                view.findViewById(R.id.tile7),
                view.findViewById(R.id.tile8)
        ));
        for (int i = 0; i < tiles.size(); i++) {
            int finI = i;
            tiles.get(i).setOnClickListener(v -> {
                GameMessage msg = GameMessage.getActionMessage(playerName, (long) finI, currentRoom.getId());
                TicTacToeApplication
                        .instance()
                        .getStompClient()
                        .send("/topic/tictactoe", msg.json()).subscribe();
            });
        }
        this.tilesInitialized = true;
        this.refreshTiles();
    }

    private void initView(View view) {
        leaveButton = view.findViewById(R.id.game_leave);
        leaveButton.setOnClickListener(onLeaveButtonClick);
        rematchButton = view.findViewById(R.id.game_rematch);
        rematchButton.setOnClickListener(onRematchButtonClick);
        playerNames = view.findViewById(R.id.player_names);
        playerScores = view.findViewById(R.id.player_scores);
        roomMessage = view.findViewById(R.id.room_message);
        this.refreshRoomMessage();
        this.refreshTextViews();
    }

    private void refreshRoomMessage() {
        switch (this.currentRoom.getState()) {
            case EMPTY:
                this.roomMessage.setText(R.string.msg_empty_room);
                break;
            case PLAYER_ONE:
                this.roomMessage.setText(R.string.msg_waiting_for_player_two);
                break;
            case PLAYER_TWO:
                this.roomMessage.setText(R.string.msg_waiting_for_player_one);
                break;
            case PLAYER_ONE_TURN:
                this.roomMessage.setText(R.string.msg_player_one_turn);
                break;
            case PLAYER_TWO_TURN:
                this.roomMessage.setText(R.string.msg_player_two_turn);
                break;
            case PLAYER_ONE_WON:
                this.roomMessage.setText(R.string.msg_player_one_won);
                break;
            case PLAYER_TWO_WON:
                this.roomMessage.setText(R.string.msg_player_two_won);
                break;
            case PLAYER_ONE_REMATCH:
                this.roomMessage.setText(R.string.msg_player_one_rematch);
                break;
            case PLAYER_TWO_REMATCH:
                this.roomMessage.setText(R.string.msg_player_two_rematch);
            case DRAW:
                this.roomMessage.setText(R.string.draw);
                break;
        }
    }

    private void refreshTextViews() {
        String p1 = StringUtils.defaultString(this.currentRoom.getPlayerOneName(), "empty");
        String p2 = StringUtils.defaultString(this.currentRoom.getPlayerTwoName(), "empty");
        String playerNames = p1 + " vs. " + p2;
        this.playerNames.setText(playerNames);
        String p1score = this.currentRoom.getPlayerOneWins() != null ? this.currentRoom.getPlayerOneWins().toString() : "?";
        String p2score = this.currentRoom.getPlayerTwoWins() != null ? this.currentRoom.getPlayerTwoWins().toString() : "?";
        String playerScores = p1score + "     :     " + p2score;
        this.playerScores.setText(playerScores);
    }

    void loadArgs() {
        Bundle args = getArguments();
        if (args != null) {
            this.playerName = args.getString("playerName");
            this.currentRoom = (Room) args.getSerializable("room");
        }
    }

    private View.OnClickListener onLeaveButtonClick = v -> {
        handler.removeCallbacks(timeoutCheck);
        TicTacToeApplication.instance().getSubscriptions().remove("/tictactoe/" + this.currentRoom.getId());
        GameMessage msg = GameMessage.getLeaveRoomMessage(this.playerName);
        TicTacToeApplication
                .instance()
                .getStompClient()
                .send("/topic/tictactoe", msg.json()).subscribe();
        AppMainFragment appMainFragment = AppMainFragment.newInstance(playerName);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, appMainFragment).commit();
    };

    private View.OnClickListener onRematchButtonClick = v -> {
        GameMessage msg = GameMessage.getRematchMessage(playerName, currentRoom.getId());
        TicTacToeApplication
                .instance()
                .getStompClient()
                .send("/topic/tictactoe", msg.json()).subscribe();
    };

    @SuppressLint("CheckResult")
    private void subscribeRoom() {
        TicTacToeApplication
                .instance()
                .getSubscriptions()
                .addSubscription("/tictactoe/" + this.currentRoom.getId(), topicMessage -> {
                    this.currentRoom = new Gson().fromJson(topicMessage.getPayload(), Room.class);
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                        this.refreshTiles();
                        this.refreshTextViews();
                        this.refreshRoomMessage();
                    });
                    System.out.println(topicMessage.getPayload());
                });
    }

    private void refreshTiles() {
        Resources resources = getResources();
        if (tilesInitialized && this.currentRoom.getTiles() != null) {
            for (int i = 0; i < tiles.size(); i++) {
                switch (this.currentRoom.getTiles().get(i)) {
                    case "n":
                        tiles.get(i).setImageDrawable(resources.getDrawable(R.drawable.none));
                        break;
                    case "x":
                        tiles.get(i).setImageDrawable(resources.getDrawable(R.drawable.x));
                        break;
                    case "o":
                        tiles.get(i).setImageDrawable(resources.getDrawable(R.drawable.o));
                        break;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(timeoutCheck);
        GameMessage msg = GameMessage.getLeaveRoomMessage(this.playerName);
        TicTacToeApplication
                .instance()
                .getStompClient()
                .send("/topic/tictactoe", msg.json()).subscribe();
        super.onDestroy();
    }
}
