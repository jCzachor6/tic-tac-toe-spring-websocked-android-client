package czachor.jakub.tictactoe.server.impl.game;

import generic.online.game.server.gogs.api.service.RoomManagementService;
import generic.online.game.server.gogs.impl.rooms.dynamic_room_list.RoomListData;
import generic.online.game.server.gogs.model.rooms.Room;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.function.Predicate;

@Component
public class GameRoomListData extends RoomListData<GameRoomDto> {
    private static final Function<Room, GameRoomDto> roomMapper = room -> new GameRoomDto((GameRoom) room);
    private static final Predicate<Room> roomFilter = (room) -> room.getClass().equals(GameRoom.class);

    public GameRoomListData(RoomManagementService managementService) {
        super(managementService, roomMapper, roomFilter);
    }
}
