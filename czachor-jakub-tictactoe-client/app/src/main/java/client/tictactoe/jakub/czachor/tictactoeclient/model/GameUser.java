package client.tictactoe.jakub.czachor.tictactoeclient.model;

import java.io.Serializable;

public class GameUser implements Serializable {
    private String id;
    private String username;
    private String password;
    private String jwt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
