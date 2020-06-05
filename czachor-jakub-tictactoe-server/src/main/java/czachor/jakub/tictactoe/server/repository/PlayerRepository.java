package czachor.jakub.tictactoe.server.repository;

import czachor.jakub.tictactoe.server.models.Player;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PlayerRepository {
    private List<Player> players = new ArrayList<>();

    public Optional<Player> getByName(String name) {
        return this.players.stream().filter(p -> name.equals(p.getName())).findFirst();
    }

    public Player addPlayer(String name) {
        Player newPlayer = new Player(name);
        this.players.add(newPlayer);
        return newPlayer;
    }
}
