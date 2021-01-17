package czachor.jakub.tictactoe.server;

import czachor.jakub.tictactoe.server.impl.game.GameRoomInitializer;
import czachor.jakub.tictactoe.server.impl.game.GameRoomListData;
import generic.online.game.server.gogs.api.service.RoomManagementService;
import generic.online.game.server.gogs.impl.rooms.dynamic_room_list.DynamicRoomListOperations;
import generic.online.game.server.gogs.impl.rooms.dynamic_room_list.RoomListInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
public class SocketController {
    private static final int GAME_ROOMS_SIZE = 10;
    private final RoomManagementService managementService;
    private final GameRoomListData gameRoomListData;

    @PostConstruct
    public void init() {
        // create games list room that tracks game rooms changes.
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
}
