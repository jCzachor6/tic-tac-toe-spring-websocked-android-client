package czachor.jakub.tictactoe.server.impl.user;

import generic.online.game.server.gogs.api.auth.model.User;
import generic.online.game.server.gogs.utils.GogsUserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService implements GogsUserService<GameUser, Long> {
    private final UserRepository repository;

    @Override
    public User map(GameUser gameUser) {
        return Optional.ofNullable(gameUser)
                .map(u -> new User(
                        StringUtils.EMPTY,
                        gameUser.getId().toString(),
                        gameUser.getUsername(),
                        gameUser.getPassword(),
                        Collections.singletonList("USER")
                )).orElse(null);
    }

    @Override
    public User getOneById(Long id) {
        return map(repository.getOne(id));
    }

    @Override
    public User getOneByUsername(String username) {
        return map(repository.getFirstByUsername(username));
    }

    @Override
    public User createOne(String username, String password) {
        GameUser user = new GameUser();
        user.setUsername(username);
        user.setPassword(password);
        return map(repository.save(user));
    }
}
