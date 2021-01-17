package czachor.jakub.tictactoe.server.impl.game;

import generic.online.game.server.gogs.model.socket.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.boot.json.GsonJsonParser;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class InputMessage extends Message {
    private int tileIndex;

    public InputMessage(String json) {
        Map<String, Object> m = new GsonJsonParser().parseMap(json);
        if (m.containsKey("tileIndex")) {
            this.tileIndex = ((Double) m.get("tileIndex")).intValue();
        }
    }
}
