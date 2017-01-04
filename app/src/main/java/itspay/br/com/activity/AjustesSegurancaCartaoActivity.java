package itspay.br.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import itspay.br.com.controller.AjustesSegurancaoCartaoController;
import itspay.br.com.itspay.R;
import itspay.br.com.model.Credencial;

public class AjustesSegurancaCartaoActivity extends AppCompatActivity {

    public Switch switchAvisosNotificacoes;
    public Switch switchBloqueioCartao;
    public Switch switchUsoExterior;
    public Switch switchUsoInternet;
    public Switch switchSaque;

    public TextView textAvisosNotificacoes;
    public TextView textBloqueioCartao;
    public TextView textUsoExterior;
    public TextView textUsoInternet;
    public TextView textSaque;

    private Button trocarSenhaButton;
    private Button comunicarPerdaButton;

    public SwipeRefreshLayout swipeRefreshLayout;


    private AjustesSegurancaoCartaoController controller = new AjustesSegurancaoCartaoController(this);

    public Credencial credencialDetalhe;


    public CompoundButton.OnCheckedChangeListener changeListenerSwitch = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            try {
                Integer estado = new Integer(compoundButton.getTag().toString());
                controller.trocaEstado(estado);
            }catch (NumberFormatException nfe){
                nfe.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes_seguranca_cartao);

        credencialDetalhe = CartaoActivity.credencialDetalhe;

        switchAvisosNotificacoes = (Switch) findViewById(R.id.switch_avisos_notificacoes);
        switchBloqueioCartao = (Switch) findViewById(R.id.switch_bloqueio_cartao);
        switchUsoExterior = (Switch) findViewById(R.id.switch_uso_exterior);
        switchUsoInternet = (Switch) findViewById(R.id.switch_uso_internet);
        switchSaque = (Switch) findViewById(R.id.switch_saque);

        textAvisosNotificacoes = (TextView) findViewById(R.id.subtitle_avisos_notificacoes);
        textBloqueioCartao = (TextView) findViewById(R.id.subtitle_bloqueio_cartao);
        textUsoExterior = (TextView) findViewById(R.id.subtitle_uso_exterior);
        textUsoInternet = (TextView) findViewById(R.id.subtitle_uso_internet);
        textSaque = (TextView) findViewById(R.id.subtitle_saque);

        trocarSenhaButton = (Button) findViewById(R.id.trocar_senha_cartao_button);
        comunicarPerdaButton = (Button) findViewById(R.id.comunicar_perda_button);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                controller.carregaStatusServico();
            }
        });

        trocarSenhaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AjustesSegurancaCartaoActivity.this, TrocarSenhaCartaoActivity.class);
                AjustesSegurancaCartaoActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        controller.carregaStatusServico();
    }
}