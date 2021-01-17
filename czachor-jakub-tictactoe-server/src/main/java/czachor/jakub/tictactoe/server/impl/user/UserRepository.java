package czachor.jakub.tictactoe.server.impl.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<GameUser, Long> {
    GameUser getFirstByUsername(String username);
}
