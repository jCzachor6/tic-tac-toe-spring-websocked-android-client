package client.tictactoe.jakub.czachor.tictactoeclient.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URISyntaxException;
import java.util.Objects;

import client.tictactoe.jakub.czachor.tictactoeclient.R;
import client.tictactoe.jakub.czachor.tictactoeclient.TicTacToeApplication;
import client.tictactoe.jakub.czachor.tictactoeclient.model.GameListMessage;
import client.tictactoe.jakub.czachor.tictactoeclient.model.GameRoomDto;
import client.tictactoe.jakub.czachor.tictactoeclient.utils.RecyclerViewAdapter;
import io.socket.client.IO;
import io.socket.emitter.Emitter;

public class RoomsFragment extends Fragment implements RecyclerViewAdapter.ItemClickListener {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        recyclerViewAdapter = new RecyclerViewAdapter(getActivity().getApplicationContext());
        recyclerViewAdapter.setClickListener(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        return view;
    }

    @SuppressLint("CheckResult")
    private void subscribe() {
        String jwt = TicTacToeApplication.instance().getAuth().getJwt();
        try {
            IO.Options options = new IO.Options();
            options.forceNew = true;
            TicTacToeApplication.instance().gameListSocket =
                    IO.socket("http://10.0.2.2:9092/ttt/room-list?token=" + jwt, options);
            TicTacToeApplication.instance().gameListSocket.on("room-list", roomChangesListener());
            TicTacToeApplication.instance().gameListSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        GameRoomDto room = recyclerViewAdapter.getItem(position);
        if (room.getPlayerOneName() != null && room.getPlayerTwoName() != null) {
            Toast.makeText(view.getContext(), R.string.room_full, Toast.LENGTH_SHORT).show();
        } else {
            this.startGameFragment(room);
        }
    }

    private Emitter.Listener roomChangesListener() {
        return args -> {
            for (Object arg : args) { //weird bugfix
                if (arg.toString().startsWith("{")) {
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                        recyclerViewAdapter.updateData(GameListMessage.parse(arg.toString()));
                    });
                }
            }
        };
    }

    private void startGameFragment(GameRoomDto room) {
        GameFragment gameFragment = GameFragment.newInstance(room);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, gameFragment).commit();
    }
}
