package czachor.jakub.tictactoe.server.controller;

import czachor.jakub.tictactoe.server.message.Action;
import czachor.jakub.tictactoe.server.message.GameMessage;
import czachor.jakub.tictactoe.server.message.MessageType;
import czachor.jakub.tictactoe.server.models.Player;
import czachor.jakub.tictactoe.server.models.Room;
import czachor.jakub.tictactoe.server.models.dto.RoomDto;
import czachor.jakub.tictactoe.server.service.PlayerService;
import czachor.jakub.tictactoe.server.service.RoomService;
import czachor.jakub.tictactoe.server.util.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;

@Controller
public class WebSocketController {
    private static Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RoomService roomService;
    private final PlayerService playerService;

    @Autowired
    public WebSocketController(SimpMessagingTemplate simpMessagingTemplate, RoomService roomService, PlayerService playerService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.roomService = roomService;
        this.playerService = playerService;
    }

    @MessageMapping("/tictactoe")
    private void game(GameMessage message) {
        Player player = this.playerService.getPlayerByName(message.getPlayerName());
        RoomDto roomDto = new RoomDto();
        if(message.getRoomId() != null){
            Room room = roomService.getById(message.getRoomId());
            room.setCurrentAction(Action.builder()
                    .player(player)
                    .type(message.getType())
                    .build()
            );
            if (message.getType().equals(MessageType.ACTION)) {
                room.setTile(player, message.getTileIndex());
            }
            room.tick();
            roomDto = Mapper.map(room);
        }
        logger.info("Received message: {}", message);
        switch (message.getType()) {
            case ALL:
            case LEAVE:
                this.simpMessagingTemplate.convertAndSend("/rooms/", this.roomsLookup());
                break;
            case CONNECT:
                this.simpMessagingTemplate.convertAndSend("/player/" + message.getPlayerName(), Mapper.map(player));
                break;
            case JOIN:
                this.simpMessagingTemplate.convertAndSend("/rooms/", this.roomsLookup());
                this.simpMessagingTemplate.convertAndSend("/tictactoe/" + roomDto.getId(), roomDto);
                break;
            case ACTION:
            case REMATCH:
            case REFRESH:
                this.simpMessagingTemplate.convertAndSend("/tictactoe/" + roomDto.getId(), roomDto);
                break;
        }
    }

    @Scheduled(fixedRate = 10000)
    public void dropOnTimeout() {
        logger.info("Timeout check. ");
        Date current = new Date();
        int droppedCount = 0;
        int activePlayers = 0;
        for (Room room : roomService.getAll()) {
            if (room.getPlayerOne() != null) {
                activePlayers++;
                long ms = (current.getTime() - room.getPlayerOne().getTimeoutCheck().getTime());
                if (ms > 10000) {
                    room.setCurrentAction(Action.builder()
                            .player(room.getPlayerOne())
                            .type(MessageType.LEAVE)
                            .build());
                    room.tick();
                    droppedCount++;
                }
            }
            if (room.getPlayerTwo() != null) {
                activePlayers++;
                long ms = (room.getPlayerTwo().getTimeoutCheck().getSeconds() - current.getSeconds());
                if (ms > 10) {
                    room.setCurrentAction(Action.builder()
                            .player(room.getPlayerTwo())
                            .type(MessageType.LEAVE)
                            .build());
                    room.tick();
                    droppedCount++;
                }
            }
        }
        if (isAnyoneDropped(droppedCount)) {
            logger.debug("Removing {} afk players from rooms. ", droppedCount);
            this.simpMessagingTemplate.convertAndSend("/rooms/", this.roomsLookup());
        } else {
            logger.debug("Could not find afk players. ");
        }
        logger.debug("Total active players: " + activePlayers);
    }

    private boolean isAnyoneDropped(int droppedCount) {
        return droppedCount > 0;
    }

    private List<RoomDto> roomsLookup() {
        return Mapper.mapLookup(this.roomService.getAll());
    }
}
