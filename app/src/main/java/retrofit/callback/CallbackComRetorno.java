package retrofit.callback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static retrofit.callback.MensagemCallback.RESPOSTA_ERRO_NAO_SUCEDIDA;
import static retrofit.callback.MensagemCallback.RESPOSTA_FALHA_COMUNICACAO;

public class CallbackComRetorno<T> implements Callback<T> {

    private final RespostaCallback callback;

    public CallbackComRetorno(RespostaCallback callback) {
        this.callback = callback;
    }

    @Override
    @EverythingIsNonNull
    public void onResponse(Call<T> call, Response<T> response) {

        if(response.isSuccessful()) {
            T resultado = response.body();
            if(resultado != null) {
                //Notifica que tem resposta com sucesso
                callback.quandoSucesso(resultado);
            }
        } else {
            //Notifica Falha
            callback.quandoFalha(RESPOSTA_ERRO_NAO_SUCEDIDA);
        }
    }

    @Override
    @EverythingIsNonNull
    public void onFailure(Call<T> call, Throwable t) {
        //Notifica falha
        callback.quandoFalha(RESPOSTA_FALHA_COMUNICACAO + t.getMessage());
    }

    public interface RespostaCallback <T> {
        void quandoSucesso(T resultado);
        void quandoFalha(String erro);
    }
}
