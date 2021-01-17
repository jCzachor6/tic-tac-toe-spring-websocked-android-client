package czachor.jakub.tictactoe.server.impl.game;

import generic.online.game.server.gogs.impl.rooms.dynamic_room_list.DynamicRoomListOperations;
import generic.online.game.server.gogs.model.rooms.Room;
import generic.online.game.server.gogs.model.rooms.RoomInitializerData;
import generic.online.game.server.gogs.utils.RoomInitializer;

public class GameRoomInitializer implements RoomInitializer<DynamicRoomListOperations> {

    @Override
    public Room initialize(RoomInitializerData initializerData, DynamicRoomListOperations operations) {
        initializerData.setRoomListOperations(operations);
        return new GameRoom(initializerData);
    }
}
