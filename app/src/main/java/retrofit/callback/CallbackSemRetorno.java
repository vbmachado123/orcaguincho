package retrofit.callback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static retrofit.callback.MensagemCallback.RESPOSTA_ERRO_NAO_SUCEDIDA;
import static retrofit.callback.MensagemCallback.RESPOSTA_FALHA_COMUNICACAO;

public class CallbackSemRetorno implements Callback<Void> {

    private final RespostaCallback callback;

    public CallbackSemRetorno(RespostaCallback callback) {
        this.callback = callback;
    }

    @Override
    @EverythingIsNonNull
    public void onResponse(Call<Void> call, Response<Void> response) {
        if (response.isSuccessful()) {
            callback.quandoSucesso();
        } else {
            callback.quandoFalha(RESPOSTA_ERRO_NAO_SUCEDIDA);
        }
    }

    @Override
    @EverythingIsNonNull
    public void onFailure(Call<Void> call, Throwable t) {
        //notifica falha
        callback.quandoFalha(RESPOSTA_FALHA_COMUNICACAO + t.getMessage());
    }

    public interface RespostaCallback {
        void quandoSucesso();

        void quandoFalha(String erro);
    }
}
