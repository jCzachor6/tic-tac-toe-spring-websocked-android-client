package client.tictactoe.jakub.czachor.tictactoeclient;

import android.app.Application;

import client.tictactoe.jakub.czachor.tictactoeclient.utils.Subscriptions;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;


/**
 * @author jakub
 * Created on 11.05.2019.
 */
public class TicTacToeApplication extends Application {
    private static TicTacToeApplication application;
    private StompClient stompClient;
    private Subscriptions subscriptions;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public void initWebsocket() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://10.0.2.2:8080/ws/websocket");
        stompClient.connect();
        subscriptions = new Subscriptions();
    }

    public void closeConnection() {
        this.subscriptions.removeAll();
        this.stompClient.disconnect();
    }

    public StompClient getStompClient() {
        return stompClient;
    }

    public Subscriptions getSubscriptions() {
        return subscriptions;
    }

    public static TicTacToeApplication instance() {
        return application;
    }
}
