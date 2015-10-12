package br.com.luizfp.tictactoe.activities;

import android.os.Bundle;

import br.com.luizfp.tictactoe.R;
import br.com.luizfp.tictactoe.bluetooth.BluetoothCheckActivity;
import br.com.luizfp.tictactoe.bluetooth.BluetoothGameServerActivity;
import br.com.luizfp.tictactoe.bluetooth.ListaDevicesActivity;
import br.com.luizfp.tictactoe.bluetooth.ListaPareadosActivity;
import br.com.luizfp.tictactoe.util.BluetoothUtils;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnNovoJogo) void novoJogo() {
        openActivity(BluetoothGameServerActivity.class);
    }

    @OnClick(R.id.btnVerificaAtivaBt) void verificaAtivaBt() {
        openActivity(BluetoothCheckActivity.class);
    }

    @OnClick(R.id.btnListaPareados) void listaPareados() {
        openActivity(ListaPareadosActivity.class);
    }

    @OnClick(R.id.btnBuscarDevices) void buscarDevices() {
        openActivity(ListaDevicesActivity.class);
    }

    @OnClick(R.id.btnFicarVisivel) void ficarVisivel() {
        // Garante que alguém pode te encontrar
        BluetoothUtils.makeVisible(this, 300);
    }
}
