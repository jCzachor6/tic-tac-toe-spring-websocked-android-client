package czachor.jakub.tictactoe.server;

import czachor.jakub.tictactoe.server.impl.game.GameRoomInitializer;
import czachor.jakub.tictactoe.server.impl.game.GameRoomListData;
import czachor.jakub.tictactoe.server.impl.user.RandomAnimalUserPrefixGenerator;
import generic.online.game.server.gogs.GenericOnlineGameServer;
import generic.online.game.server.gogs.api.service.RoomManagementService;
import generic.online.game.server.gogs.impl.rooms.dynamic_room_list.DynamicRoomListOperations;
import generic.online.game.server.gogs.impl.rooms.dynamic_room_list.RoomListInitializer;
import generic.online.game.server.gogs.utils.AnonymousUsernameGenerator;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
public class Application {
    public static final String basePackage = "czachor.jakub.tictactoe.server";
    private static final int GAME_ROOMS_SIZE = 10;
    private final RoomManagementService managementService;
    private final GameRoomListData gameRoomListData;

    public static void main(String[] args) {
        GenericOnlineGameServer.start(config -> {
            config.basePackage = Application.basePackage;
            config.serverPort = 8080;
            config.wsServerNamespace = "/ttt";
            config.wsServerPort = 9092;
            config.authRegister = true;
            config.authLogin = true;
            config.authAnonymousUser = true;
            config.jwtSecret = "secret";
            config.jwtEncryptionAlgorithm = SignatureAlgorithm.HS256;
            config.jwtExpirationInMs = 604800000;
        });
    }

    @PostConstruct
    public void init() {

        // create games list room that tracks game room changes.
        DynamicRoomListOperations roomListOperations = (DynamicRoomListOperations) managementService.addRoom(
                "room-list",
                new HashSet<>(),
                new RoomListInitializer(),
                gameRoomListData
        );

        // create empty game rooms
        for (int i = 0; i < GAME_ROOMS_SIZE; i++) {
            managementService.addRoom(
                    Integer.toString(i),
                    new HashSet<>(),
                    new GameRoomInitializer(),
                    roomListOperations
            );
        }
    }

    @Bean
    public AnonymousUsernameGenerator anonymousUsernameGenerator() {
        return new RandomAnimalUserPrefixGenerator();
    }
}
