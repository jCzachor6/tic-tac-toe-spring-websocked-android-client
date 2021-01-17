package client.tictactoe.jakub.czachor.tictactoeclient.utils;

import client.tictactoe.jakub.czachor.tictactoeclient.model.GameUser;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("/gogs/api/auth/anonymous")
    Call<GameUser> anonymous();

    @POST("/gogs/api/auth/login")
    Call<GameUser> login(@Body GameUser auth);

    @POST("/gogs/api/auth/register")
    Call<GameUser> register(@Body GameUser auth);
}
