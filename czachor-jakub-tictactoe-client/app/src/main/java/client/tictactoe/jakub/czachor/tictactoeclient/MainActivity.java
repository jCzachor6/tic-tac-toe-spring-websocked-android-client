package client.tictactoe.jakub.czachor.tictactoeclient;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import client.tictactoe.jakub.czachor.tictactoeclient.fragments.AppMainFragment;
import client.tictactoe.jakub.czachor.tictactoeclient.fragments.ConnectFragment;
import client.tictactoe.jakub.czachor.tictactoeclient.model.GameUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initConnectFragment();
    }

    private void initConnectFragment() {
        ConnectFragment connectFragment = new ConnectFragment(authCallback);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, connectFragment).commit();
    }

    private void initAppMainFragment() {
        AppMainFragment appMainFragment = new AppMainFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, appMainFragment).commit();
    }

    private Callback<GameUser> authCallback = new Callback<GameUser>() {
        @Override
        public void onResponse(Call<GameUser> call, Response<GameUser> response) {
            if (response.isSuccessful()) {
                TicTacToeApplication.instance().setAuth(response.body());
                initAppMainFragment();
            } else {
                authFailureToast();
            }
        }

        @Override
        public void onFailure(Call<GameUser> call, Throwable t) {
            authFailureToast();
        }
    };

    @Override
    public void onBackPressed() {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TicTacToeApplication.instance().disconnect();
    }

    private void authFailureToast() {
        Toast.makeText(getApplicationContext(), R.string.auth_failure, Toast.LENGTH_SHORT).show();
    }
}
