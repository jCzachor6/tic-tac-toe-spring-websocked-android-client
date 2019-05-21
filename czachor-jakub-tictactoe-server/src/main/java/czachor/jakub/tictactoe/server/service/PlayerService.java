package czachor.jakub.tictactoe.server.service;

import czachor.jakub.tictactoe.server.models.Player;
import czachor.jakub.tictactoe.server.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player getPlayerByName(String playerName) {
        Optional<Player> player = this.playerRepository.getByName(playerName);
        return player.orElseGet(() -> this.playerRepository.addPlayer(playerName));
    }

    public void refreshTimeoutCheck(String playerName) {
        Optional<Player> player = this.playerRepository.getByName(playerName);
        player.ifPresent(player1 -> player1.setTimeoutCheck(new Date()));
    }
}
