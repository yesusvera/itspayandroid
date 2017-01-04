package itspay.br.com.controller;

import android.graphics.Color;
import android.widget.Switch;
import android.widget.TextView;

import itspay.br.com.activity.AjustesSegurancaCartaoActivity;
import itspay.br.com.authentication.IdentityItsPay;
import itspay.br.com.model.CredencialStatus;
import itspay.br.com.model.TrocarEstadoCredencialRequest;
import itspay.br.com.services.ConnectPortadorService;
import itspay.br.com.util.ItsPayConstants;
import itspay.br.com.util.UtilsActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yesus on 03/01/17.
 */

public class AjustesSegurancaoCartaoController extends BaseActivityController<AjustesSegurancaCartaoActivity> {
    public AjustesSegurancaoCartaoController(AjustesSegurancaCartaoActivity activity) {
        super(activity);
    }

    public void carregaStatusServico() {

        activity.swipeRefreshLayout.setRefreshing(true);

        removeChangeListener();

        switchDisabled(activity.switchAvisosNotificacoes,
                activity.switchBloqueioCartao,
                activity.switchSaque,
                activity.switchUsoExterior,
                activity.switchUsoInternet);

        Call<CredencialStatus> call = ConnectPortadorService
                .getService()
                .listaStatusHabilitacao(
                        activity.credencialDetalhe.getIdCredencial(),
                        IdentityItsPay.getInstance().getToken()
                );

        call.enqueue(new Callback<CredencialStatus>() {
            @Override
            public void onResponse(Call<CredencialStatus> call, Response<CredencialStatus> response) {
                if (response.body() != null) {
                    preencheHabilitacoes(response.body());

                    switchEnabled(activity.switchAvisosNotificacoes,
                            activity.switchBloqueioCartao,
                            activity.switchSaque,
                            activity.switchUsoExterior,
                            activity.switchUsoInternet);

                    configuraChangeListener();
                } else {
                    UtilsActivity.alertMsg(response.errorBody(), activity);
                }

                activity.swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<CredencialStatus> call, Throwable t) {
                UtilsActivity.alertIfSocketException(t, activity);
                t.printStackTrace();
                activity.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void trocaEstado(int tipoEstado){
        TrocarEstadoCredencialRequest request = new TrocarEstadoCredencialRequest();
        request.setTipoEstado(tipoEstado);
        request.setIdUsuario(ItsPayConstants.ID_USUARIO);
        request.setIdCredencial(activity.credencialDetalhe.getIdCredencial());

        configuraSubtitulos();

        Call<ResponseBody> call = ConnectPortadorService
                .getService().trocarEstado(request, IdentityItsPay.getInstance().getToken());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body()!=null){
                    UtilsActivity.alertMsg(response.body(), activity);
                    carregaStatusServico();
                }else{
                    UtilsActivity.alertMsg(response.errorBody(), activity);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                UtilsActivity.alertIfSocketException(t, activity);
            }
        });
    }

    private void preencheHabilitacoes(CredencialStatus credencialStatus) {
        activity.switchAvisosNotificacoes.setChecked(credencialStatus.isHabilitaNotificacaoTransacao());
        activity.switchBloqueioCartao.setChecked(!credencialStatus.isHabilitaUsoPessoa());
        activity.switchSaque.setChecked(credencialStatus.isHabilitaSaque());
        activity.switchUsoExterior.setChecked(credencialStatus.isHabilitaExterior());
        activity.switchUsoInternet.setChecked(credencialStatus.isHabilitaEcommerce());

        configuraSubtitulos();
    }

    public void configSubtitleSwitch(TextView subtitulo, boolean estado){
        subtitulo.setText(estado?"Habilitado":"Desabilitado");
        subtitulo.setTextColor(estado? Color.GREEN: Color.RED);
    }

    public void configuraSubtitulos(){

        configSubtitleSwitch(activity.textAvisosNotificacoes, activity.switchAvisosNotificacoes.isChecked());
        configSubtitleSwitch(activity.textSaque, activity.switchSaque.isChecked());
        configSubtitleSwitch(activity.textUsoExterior, activity.switchUsoExterior.isChecked());
        configSubtitleSwitch(activity.textUsoInternet, activity.switchUsoInternet.isChecked());

        activity.textBloqueioCartao.setText(activity.switchBloqueioCartao.isChecked()?"Bloqueado":"Desbloqueado");
        activity.textBloqueioCartao.setTextColor(activity.switchBloqueioCartao.isChecked()? Color.RED: Color.GREEN);
    }

    public void configuraChangeListener(){
        activity.switchAvisosNotificacoes.setOnCheckedChangeListener(activity.changeListenerSwitch);
        activity.switchBloqueioCartao.setOnCheckedChangeListener(activity.changeListenerSwitch);
        activity.switchUsoExterior.setOnCheckedChangeListener(activity.changeListenerSwitch);
        activity.switchUsoInternet.setOnCheckedChangeListener(activity.changeListenerSwitch);
        activity.switchSaque.setOnCheckedChangeListener(activity.changeListenerSwitch);
    }

    public void removeChangeListener(){
        activity.switchAvisosNotificacoes.setOnCheckedChangeListener(null);
        activity.switchBloqueioCartao.setOnCheckedChangeListener(null);
        activity.switchUsoExterior.setOnCheckedChangeListener(null);
        activity.switchUsoInternet.setOnCheckedChangeListener(null);
        activity.switchSaque.setOnCheckedChangeListener(null);
    }


    public void switchEnabled(Switch... switches) {
        for (Switch s : switches) {
            s.setEnabled(true);
        }
    }

    public void switchDisabled(Switch... switches) {
        for (Switch s : switches) {
            s.setEnabled(false);
        }
    }

}
