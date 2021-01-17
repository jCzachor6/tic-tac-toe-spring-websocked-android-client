package client.tictactoe.jakub.czachor.tictactoeclient;

import android.app.Application;

import client.tictactoe.jakub.czachor.tictactoeclient.model.GameUser;
import client.tictactoe.jakub.czachor.tictactoeclient.utils.AuthService;
import io.socket.client.Socket;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * @author jakub
 * Created on 11.05.2019.
 */
public class TicTacToeApplication extends Application {
    private static TicTacToeApplication application;
    private GameUser auth;
    private AuthService authService;
    public Socket gameListSocket;
    public Socket gameRoomSocket;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        this.initRetrofit();
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        authService = retrofit.create(AuthService.class);
    }

    public static TicTacToeApplication instance() {
        return application;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public GameUser getAuth() {
        return auth;
    }

    public void setAuth(GameUser auth) {
        this.auth = auth;
    }

    public void disconnect() {
        setAuth(null);
        if (gameListSocket != null) {
            gameListSocket.off();
            gameListSocket.disconnect();
            gameListSocket = null;
        }
        if (gameRoomSocket != null) {
            gameRoomSocket.off();
            gameRoomSocket.disconnect();
            gameRoomSocket = null;
        }
    }
}
