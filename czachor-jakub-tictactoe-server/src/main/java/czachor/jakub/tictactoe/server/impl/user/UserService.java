package czachor.jakub.tictactoe.server.impl.user;

import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.utils.GogsUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service("userService")
public class UserService implements GogsUserService<GameUser> {
    private final UserRepository userRepository;

    @Override
    public User map(GameUser gameUser) {
        return Optional.ofNullable(gameUser).map(u -> {
            User user = new User();
            user.setId(gameUser.getId().toString());
            user.setUsername(gameUser.getUsername());
            user.setPassword(gameUser.getPassword());
            return user;
        }).orElse(null);
    }

    @Override
    public User getOneByUsername(String username) {
        return map(userRepository.getFirstByUsername(username));
    }

    @Override
    public User createOne(String username, String password) {
        GameUser user = new GameUser();
        user.setUsername(username);
        user.setPassword(password);
        return map(userRepository.save(user));
    }

    @Override
    public User editUsername(String id, String username) {
        GameUser found = userRepository.getOne(Long.parseLong(id));
        found.setUsername(username);
        return map(userRepository.save(found));
    }
}
