package czachor.jakub.tictactoe.server.repository;

import czachor.jakub.tictactoe.server.models.Room;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RoomRepository {
    private List<Room> rooms = new ArrayList<>();

    public RoomRepository() {
        for (int i = 0; i < 10; i++) {
            rooms.add(new Room());
        }
    }

    public List<Room> getAll(){
        return this.rooms;
    }

    public Optional<Room> getById(Long id) {
        return this.rooms.stream().filter(room -> room.getId().equals(id)).findFirst();
    }

    public Optional<Room> findRoomByPlayerName(String playerName){
        return this.rooms.stream().filter(room ->
                room.getPlayerOne() != null && room.getPlayerOne().getName().equals(playerName)
                        || room.getPlayerTwo() != null && room.getPlayerTwo().getName().equals(playerName)).findFirst();
    }
}
