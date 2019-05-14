package czachor.jakub.tictactoe.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import czachor.jakub.tictactoe.server.message.GameMessage;
import czachor.jakub.tictactoe.server.models.Player;
import czachor.jakub.tictactoe.server.models.dto.PlayerDto;
import czachor.jakub.tictactoe.server.models.dto.RoomDto;
import czachor.jakub.tictactoe.server.service.PlayerService;
import czachor.jakub.tictactoe.server.service.RoomService;
import czachor.jakub.tictactoe.server.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class WebSocketController {
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
        System.out.println("Received message: " + message);
        switch (message.getType()) {
            case ALL:
                this.simpMessagingTemplate.convertAndSend("/rooms/", this.roomsLookup());
                break;
            case CONNECT:
                PlayerDto playerDto = Mapper.map(this.playerService.getPlayerByName(message.getPlayerName()));
                this.simpMessagingTemplate.convertAndSend("/player/" + message.getPlayerName(), playerDto);
                break;
            case JOIN:
                Player player = playerService.getPlayerByName(message.getPlayerName());
                RoomDto joinDto = Mapper.map(this.roomService.joinRoom(player, message.getRoomId()));
                this.simpMessagingTemplate.convertAndSend("/rooms/", this.roomsLookup());
                this.simpMessagingTemplate.convertAndSend("/tictactoe/" + joinDto.getId(), joinDto);
                break;
            case ACTION:
                RoomDto actionDto = Mapper.map(this.roomService.setTile(message.getPlayerName(), message.getTileIndex().intValue(), message.getRoomId()));
                this.simpMessagingTemplate.convertAndSend("/tictactoe/" + actionDto.getId(), actionDto);
                break;
            case REMATCH:
                RoomDto rematchDto = Mapper.map(this.roomService.rematch(message.getPlayerName(), message.getRoomId()));
                this.simpMessagingTemplate.convertAndSend("/tictactoe/" + rematchDto.getId(), rematchDto);
                break;
            case REFRESH:
                RoomDto refreshDto = Mapper.map(this.roomService.getById(message.getRoomId()));
                this.simpMessagingTemplate.convertAndSend("/tictactoe/" + refreshDto.getId(), refreshDto);
                break;
            case LEAVE:
                this.roomService.leaveRoom(message.getPlayerName());
                this.simpMessagingTemplate.convertAndSend("/rooms/", this.roomsLookup());
                break;
        }
    }

    private List<RoomDto> roomsLookup() {
        return Mapper.mapLookup(this.roomService.getAll());
    }


}
