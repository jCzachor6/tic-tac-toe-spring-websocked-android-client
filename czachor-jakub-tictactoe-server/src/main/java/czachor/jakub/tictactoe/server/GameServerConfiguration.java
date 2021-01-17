package czachor.jakub.tictactoe.server;

import czachor.jakub.tictactoe.server.impl.user.RandomAnimalUserPrefixGenerator;
import generic.online.game.server.gogs.impl.PinPasswordGenerator;
import generic.online.game.server.gogs.utils.AnonymousPrefixGenerator;
import generic.online.game.server.gogs.utils.PasswordGenerator;
import generic.online.game.server.gogs.utils.settings.GameUserSettings;
import generic.online.game.server.gogs.utils.settings.JwtSettings;
import generic.online.game.server.gogs.utils.settings.SocketSettings;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "generic.online.game.server.gogs")
public class GameServerConfiguration {

    @Bean
    public GameUserSettings gameUserSettings() {
        return GameUserSettings.builder()
                .anonymousUser(true)
                .build();
    }

    @Bean
    public JwtSettings jwtSettings() {
        return JwtSettings.builder()
                .encryptionAlgorithm(SignatureAlgorithm.HS256)
                .expirationInMs(604800000)
                .secret("secret")
                .build();
    }

    @Bean
    public SocketSettings socketSettings() {
        return SocketSettings.builder()
                .namespace("/ttt")
                .port(9092)
                .build();
    }

    @Bean
    public AnonymousPrefixGenerator anonymousPrefixGenerator() {
        return new RandomAnimalUserPrefixGenerator();
    }

    @Bean
    public PasswordGenerator passwordGenerator() {
        return new PinPasswordGenerator();
    }
}
