package client.tictactoe.jakub.czachor.tictactoeclient.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import client.tictactoe.jakub.czachor.tictactoeclient.R;
import client.tictactoe.jakub.czachor.tictactoeclient.TicTacToeApplication;
import client.tictactoe.jakub.czachor.tictactoeclient.model.GameUser;
import client.tictactoe.jakub.czachor.tictactoeclient.utils.AuthService;
import retrofit2.Callback;

public class ConnectFragment extends Fragment {
    private AuthService authService = TicTacToeApplication.instance().getAuthService();
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Callback<GameUser> authSuccessCallback;

    public ConnectFragment(Callback<GameUser> authSuccessCallback) {
        this.authSuccessCallback = authSuccessCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connect, container, false);
        this.usernameEditText = view.findViewById(R.id.username_et);
        this.passwordEditText = view.findViewById(R.id.password_et);
        view.findViewById(R.id.login_btn).setOnClickListener(
                v -> authService.login(buildRequestBody()).enqueue(authSuccessCallback)
        );
        view.findViewById(R.id.register_btn).setOnClickListener(
                v -> authService.register(buildRequestBody()).enqueue(authSuccessCallback)
        );
        view.findViewById(R.id.anonymous_btn).setOnClickListener(
                v -> authService.anonymous().enqueue(authSuccessCallback)
        );
        return view;
    }

    private GameUser buildRequestBody() {
        GameUser req = new GameUser();
        req.setUsername(usernameEditText.getText().toString());
        req.setPassword(passwordEditText.getText().toString());
        return req;
    }
}
