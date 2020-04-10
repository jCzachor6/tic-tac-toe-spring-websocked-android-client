package client.tictactoe.jakub.czachor.tictactoeclient.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Objects;

import client.tictactoe.jakub.czachor.tictactoeclient.R;
import client.tictactoe.jakub.czachor.tictactoeclient.TicTacToeApplication;
import client.tictactoe.jakub.czachor.tictactoeclient.model.GameMessage;
import client.tictactoe.jakub.czachor.tictactoeclient.model.Player;


public class AccountFragment extends Fragment {
    private TextView tvPlayerName;
    private TextView tvPlayerWins;
    private TextView tvPlayerGames;
    private Button refreshButton;
    private Button disconnectButton;
    private String playerName;

    public static AccountFragment newInstance(String playerName) {
        AccountFragment f = new AccountFragment();
        Bundle args = new Bundle();
        args.putString("playerName", playerName);
        f.setArguments(args);
        return f;
    }

    public AccountFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loadArgs();
        this.subscribe();
    }

    @SuppressLint("CheckResult")
    private void subscribe() {
        TicTacToeApplication.instance().getSubscriptions().addSubscription("/player/" + this.playerName, topicMessage -> {
            Player player = new Gson().fromJson(topicMessage.getPayload(), Player.class);
            Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                tvPlayerName.setText(player.getName());
                String games = "Games: " + player.getGames().toString();
                tvPlayerGames.setText(games);
                String wins = "Wins: " + player.getWins().toString();
                tvPlayerWins.setText(wins);
            });
        });
    }

    private void loadProfile() {
        GameMessage msg = GameMessage.getConnectMessage(this.playerName);
        TicTacToeApplication
                .instance()
                .getStompClient()
                .send("/topic/tictactoe", msg.json()).subscribe();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        tvPlayerName = view.findViewById(R.id.account_player_name);
        tvPlayerWins = view.findViewById(R.id.account_player_wins);
        tvPlayerGames = view.findViewById(R.id.account_player_games);
        refreshButton = view.findViewById(R.id.account_refresh);
        refreshButton.setOnClickListener(v -> loadProfile());
        disconnectButton = view.findViewById(R.id.account_disconnect);
        disconnectButton.setOnClickListener(v -> disconnect());
        this.loadProfile();
        return view;
    }

    private void disconnect() {
        TicTacToeApplication.instance().closeConnection();
        Intent mIntent = getActivity().getIntent();
        getActivity().finish();
        startActivity(mIntent);
    }

    void loadArgs() {
        Bundle args = getArguments();
        if (args != null) {
            this.playerName = args.getString("playerName");
        }
    }
}
