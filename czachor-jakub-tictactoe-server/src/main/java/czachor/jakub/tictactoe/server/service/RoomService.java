package czachor.jakub.tictactoe.server.service;

import czachor.jakub.tictactoe.server.models.Room;
import czachor.jakub.tictactoe.server.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAll() {
        return this.roomRepository.getAll();
    }

    public Room getById(Long id) {
        return this.roomRepository.getById(id).orElse(null);
    }
}
