package retrofit;

import org.jetbrains.annotations.NotNull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit.service.UsuarioService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Connection {

    private static final String URL_BASE = "http://192.168.1.7:8080/";
    private final UsuarioService usuarioService;

    public Connection() {
        OkHttpClient client = configuraClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        usuarioService = retrofit.create(UsuarioService.class);
    }

    @NotNull
    private OkHttpClient configuraClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(logging).build();
    }

    public UsuarioService getUsuarioService() {
        return usuarioService;
    }
}
