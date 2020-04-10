package client.tictactoe.jakub.czachor.tictactoeclient.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Objects;

import client.tictactoe.jakub.czachor.tictactoeclient.R;
import client.tictactoe.jakub.czachor.tictactoeclient.TicTacToeApplication;
import client.tictactoe.jakub.czachor.tictactoeclient.model.GameMessage;
import client.tictactoe.jakub.czachor.tictactoeclient.model.Room;
import client.tictactoe.jakub.czachor.tictactoeclient.model.RoomState;
import client.tictactoe.jakub.czachor.tictactoeclient.utils.RecyclerViewAdapter;

public class RoomsFragment extends Fragment implements RecyclerViewAdapter.ItemClickListener {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private Button refreshButton;

    private String playerName;

    public static RoomsFragment newInstance(String playerName) {
        RoomsFragment f = new RoomsFragment();
        Bundle args = new Bundle();
        args.putString("playerName", playerName);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loadArgs();
        this.subscribe();
    }

    public RoomsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rooms, container, false);
        recyclerView = view.findViewById(R.id.rvRooms);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(Objects.requireNonNull(getActivity()).getApplicationContext())
        );
        refreshButton = view.findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(b -> loadRooms());
        this.loadRooms();
        return view;
    }

    @SuppressLint("CheckResult")
    private void subscribe() {
        TicTacToeApplication.instance().getSubscriptions().addSubscription("/rooms/", topicMessage -> {
            List<Room> roomList = new Gson().fromJson(topicMessage.getPayload(), new TypeToken<List<Room>>() {
            }.getType());
            Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                recyclerViewAdapter = new RecyclerViewAdapter(getActivity().getApplicationContext(), roomList);
                recyclerViewAdapter.setClickListener(this);
                recyclerView.setAdapter(recyclerViewAdapter);
            });
        });
    }

    void loadArgs() {
        Bundle args = getArguments();
        if (args != null) {
            this.playerName = args.getString("playerName");
        }
    }

    private void loadRooms() {
        GameMessage msg = GameMessage.getRoomsMessage(this.playerName);
        TicTacToeApplication
                .instance()
                .getStompClient()
                .send("/topic/tictactoe", msg.json()).subscribe();
    }

    @Override
    public void onItemClick(View view, int position) {
        Room room = recyclerViewAdapter.getItem(position);
        if (RoomState.isFull(room.getState())) {
            Toast.makeText(view.getContext(), R.string.room_full, Toast.LENGTH_SHORT).show();
        } else {
            this.startGameFragment(room, this.playerName);
        }
    }

    private void startGameFragment(Room room, String playerName) {
        GameMessage msg = GameMessage.getJoinGameMessage(this.playerName, room.getId());
        TicTacToeApplication
                .instance()
                .getStompClient()
                .send("/topic/tictactoe", msg.json()).subscribe();
        GameFragment gameFragment = GameFragment.newInstance(room, playerName);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, gameFragment).commit();
    }
}
