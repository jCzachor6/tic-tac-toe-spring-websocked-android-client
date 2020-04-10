package client.tictactoe.jakub.czachor.tictactoeclient.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import client.tictactoe.jakub.czachor.tictactoeclient.R;

public class ConnectFragment extends Fragment {
    private Button enterNameButton;
    private ConnectionListener callback;
    private EditText editText;

    public ConnectFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connect, container, false);
        this.editText = view.findViewById(R.id.enter_name_et);
        this.initButton(view);
        return view;
    }

    public interface ConnectionListener {
        void onConnectButtonClicked(String playerName);
    }

    public void setCallback(ConnectionListener callback) {
        this.callback = callback;
    }

    private void initButton(View view) {
        this.enterNameButton = view.findViewById(R.id.enter_name_button);
        this.enterNameButton.setOnClickListener(v -> {
            String playerName = editText.getText().toString();
            if (playerName.length() > 0) {
                callback.onConnectButtonClicked(playerName);
            }
        });
    }
}
