package retrofit.service;

import model.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UsuarioService {

    @POST("/api/v1/usuario")
    Call<Usuario> salva(@Body Usuario usuario);
}
