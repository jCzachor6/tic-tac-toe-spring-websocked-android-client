package czachor.jakub.tictactoe.server.impl.user;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "GAME_USERS")
public class GameUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;
}
