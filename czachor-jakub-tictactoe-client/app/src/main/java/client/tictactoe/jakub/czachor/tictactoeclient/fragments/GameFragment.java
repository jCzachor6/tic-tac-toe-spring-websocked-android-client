package client.tictactoe.jakub.czachor.tictactoeclient.fragments;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.apache.commons.lang3.StringUtils;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import client.tictactoe.jakub.czachor.tictactoeclient.R;
import client.tictactoe.jakub.czachor.tictactoeclient.TicTacToeApplication;
import client.tictactoe.jakub.czachor.tictactoeclient.model.GameMessage;
import client.tictactoe.jakub.czachor.tictactoeclient.model.GameRoomDto;
import client.tictactoe.jakub.czachor.tictactoeclient.model.TileIndexMessage;
import io.socket.client.IO;
import io.socket.emitter.Emitter;

public class GameFragment extends Fragment {
    private GameRoomDto currentRoom;
    private List<Character> tiles;

    private TextView playerNames;
    private TextView playerScores;
    private TextView roomMessage;

    private Button leaveButton;
    private Button rematchButton;
    private List<ImageView> tilesIv = new ArrayList<>(9);

    public static GameFragment newInstance(GameRoomDto room) {
        GameFragment f = new GameFragment();
        Bundle args = new Bundle();
        args.putSerializable("room", room);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loadArgs();
        this.subscribe();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        this.initView(view);
        this.initTiles(view);
        return view;
    }

    private void initTiles(View view) {
        tilesIv.addAll(Arrays.asList(
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
        for (int i = 0; i < tilesIv.size(); i++) {
            TileIndexMessage msg = new TileIndexMessage(i);
            tilesIv.get(i).setOnClickListener(
                    v -> TicTacToeApplication.instance().gameRoomSocket.emit("SET_TILE", msg.json())
            );
        }
        this.refreshTiles(Arrays.asList('n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n'));
    }

    private void initView(View view) {
        leaveButton = view.findViewById(R.id.game_leave);
        leaveButton.setOnClickListener(onLeaveButtonClick);
        rematchButton = view.findViewById(R.id.game_rematch);
        rematchButton.setOnClickListener(onRematchButtonClick);
        playerNames = view.findViewById(R.id.player_names);
        playerScores = view.findViewById(R.id.player_scores);
        roomMessage = view.findViewById(R.id.room_message);
        this.refreshTextViews(this.currentRoom);
    }

    private void refreshTextViews(GameRoomDto gameRoom) {
        String p1 = StringUtils.defaultString(gameRoom.getPlayerOneName(), "empty");
        String p2 = StringUtils.defaultString(gameRoom.getPlayerTwoName(), "empty");
        String playerNames = p1 + " vs. " + p2;
        this.playerNames.setText(playerNames);
        this.currentRoom = gameRoom;
        /*String p1score = this.currentRoom.getPlayerOneWins() != null ? this.currentRoom.getPlayerOneWins().toString() : "?";
        String p2score = this.currentRoom.getPlayerTwoWins() != null ? this.currentRoom.getPlayerTwoWins().toString() : "?";
        String playerScores = p1score + "     :     " + p2score;
        this.playerScores.setText(playerScores);*/
    }

    private View.OnClickListener onLeaveButtonClick = v -> {
        AppMainFragment appMainFragment = new AppMainFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, appMainFragment).commit();
        TicTacToeApplication.instance().gameRoomSocket.off();
        TicTacToeApplication.instance().gameRoomSocket.disconnect();
    };

    private View.OnClickListener onRematchButtonClick = v -> {
        TicTacToeApplication.instance().gameRoomSocket.emit("REMATCH", "{}");
    };

    @SuppressLint("CheckResult")
    private void subscribe() {
        String jwt = TicTacToeApplication.instance().getAuth().getJwt();
        String roomId = currentRoom.getId();
        try {
            IO.Options options = new IO.Options();
            options.forceNew = true;
            TicTacToeApplication.instance().gameRoomSocket =
                    IO.socket("http://10.0.2.2:9092/ttt/" + roomId + "?token=" + jwt, options);
            TicTacToeApplication.instance().gameRoomSocket.on(roomId, onMessageReceived());
            TicTacToeApplication.instance().gameRoomSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private Emitter.Listener onMessageReceived() {
        return args -> {
            for (Object arg : args) { //weird bugfix
                if (arg.toString().startsWith("{")) {
                    GameMessage message = GameMessage.parse(arg.toString());
                    refreshTiles(message.getTiles());
                    refreshTextViews(message.getRoom());
                    setMessage(message);
                }
            }
        };
    }

    private void setMessage(GameMessage message) {
        switch (message.getGameState()) {
            case "WAITING":
                roomMessage.setText(R.string.waiting);
                break;
            case "TURN":
                String auth = TicTacToeApplication.instance().getAuth().getUsername();
                if (message.getPlayerTurn().equals(auth)) {
                    roomMessage.setText(R.string.your_turn);
                } else {
                    roomMessage.setText(String.format("%s%s", message.getPlayerTurn(), getString(R.string.smn_turn)));
                }
                break;
            case "FINISHED":
                if (message.getPlayerRematch() != null) {
                    roomMessage.setText(String.format("%s%s", message.getPlayerRematch(), getString(R.string.ask_rematch)));
                } else if (message.getPlayerWon() != null){
                    roomMessage.setText(String.format("%s%s", message.getPlayerWon(), getString(R.string.won)));
                } else {
                    roomMessage.setText(R.string.game_draw);
                }
                break;
        }
    }

    private void refreshTiles(List<Character> tiles) {
        Resources resources = getResources();
        this.tiles = tiles;
        for (int i = 0; i < tiles.size(); i++) {
            switch (this.tiles.get(i)) {
                case 'n':
                    tilesIv.get(i).setImageDrawable(resources.getDrawable(R.drawable.none));
                    break;
                case 'x':
                    tilesIv.get(i).setImageDrawable(resources.getDrawable(R.drawable.x));
                    break;
                case 'o':
                    tilesIv.get(i).setImageDrawable(resources.getDrawable(R.drawable.o));
                    break;
            }
        }
    }

    private void loadArgs() {
        Bundle args = getArguments();
        if (args != null) {
            this.currentRoom = (GameRoomDto) args.getSerializable("room");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
