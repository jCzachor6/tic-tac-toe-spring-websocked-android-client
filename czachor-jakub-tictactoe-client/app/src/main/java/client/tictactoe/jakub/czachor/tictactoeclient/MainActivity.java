package client.tictactoe.jakub.czachor.tictactoeclient;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import client.tictactoe.jakub.czachor.tictactoeclient.fragments.AppMainFragment;
import client.tictactoe.jakub.czachor.tictactoeclient.fragments.ConnectFragment;

public class MainActivity extends AppCompatActivity implements ConnectFragment.ConnectionListener {
    private String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initConnectFragment();
    }

    private void initConnectFragment() {
        ConnectFragment connectFragment = new ConnectFragment();
        connectFragment.setCallback(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, connectFragment).commit();
    }

    private void initAppMainFragment() {
        AppMainFragment appMainFragment = AppMainFragment.newInstance(this.playerName);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, appMainFragment).commit();
    }

    @Override
    public void onConnectButtonClicked(String playerName) {
        TicTacToeApplication.instance().initWebsocket();
        this.playerName = playerName;
        this.initAppMainFragment();
    }

    @Override
    public void onBackPressed() {

    }


}
