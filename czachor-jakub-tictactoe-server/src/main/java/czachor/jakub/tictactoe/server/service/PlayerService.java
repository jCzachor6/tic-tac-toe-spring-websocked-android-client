package czachor.jakub.tictactoe.server.service;

import czachor.jakub.tictactoe.server.models.Player;
import czachor.jakub.tictactoe.server.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class PlayerService {
    private static Logger logger = LoggerFactory.getLogger(PlayerService.class);

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player getPlayerByName(String playerName) {
        Optional<Player> player = this.playerRepository.getByName(playerName);
        if (player.isPresent()) {
            player.get().setTimeoutCheck(new Date());
            return player.get();
        } else {
            return this.playerRepository.addPlayer(playerName);
        }
    }
}
