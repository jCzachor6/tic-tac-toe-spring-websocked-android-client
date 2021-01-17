package client.tictactoe.jakub.czachor.tictactoeclient.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import client.tictactoe.jakub.czachor.tictactoeclient.R;
import client.tictactoe.jakub.czachor.tictactoeclient.TicTacToeApplication;
import client.tictactoe.jakub.czachor.tictactoeclient.model.GameUser;


public class AccountFragment extends Fragment {
    public AccountFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        GameUser user = TicTacToeApplication.instance().getAuth();
        TextView tvPlayerName = view.findViewById(R.id.account_player_name);
        tvPlayerName.setText(user.getUsername());
        Button disconnectButton = view.findViewById(R.id.account_disconnect);
        disconnectButton.setOnClickListener(v -> disconnect());
        return view;
    }

    private void disconnect() {
        TicTacToeApplication.instance().disconnect();
        Intent mIntent = getActivity().getIntent();
        getActivity().finish();
        startActivity(mIntent);
    }
}
