package client.tictactoe.jakub.czachor.tictactoeclient.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import client.tictactoe.jakub.czachor.tictactoeclient.TicTacToeApplication;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import ua.naiksoftware.stomp.dto.StompMessage;

/**
 * @author jakub
 * Created on 14.05.2019.
 */
public class Subscriptions {
    private Map<String, Disposable> subscriptions = new HashMap<>(5);

    public Disposable addSubscription(String topic, Consumer<? super StompMessage> onNext) {
        Disposable disposable = TicTacToeApplication.instance()
                .getStompClient().topic(topic).subscribe(onNext);
        return subscriptions.put(topic, disposable);
    }

    public Boolean contains(String topic) {
        return this.subscriptions.containsKey(topic);
    }

    public void remove(String topic) {
        Disposable disposable = subscriptions.get(topic);
        if (disposable != null) {
            disposable.dispose();
        }
        this.subscriptions.remove(topic);
    }

    public void removeAll() {
        for (String key : new HashSet<>(subscriptions.keySet())) {
            this.remove(key);
        }
    }
}
