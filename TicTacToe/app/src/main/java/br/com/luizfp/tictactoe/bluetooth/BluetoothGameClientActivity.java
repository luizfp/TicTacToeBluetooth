package br.com.luizfp.tictactoe.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import br.com.luizfp.tictactoe.R;
import br.com.luizfp.tictactoe.models.TicTacToGame;
import br.com.luizfp.tictactoe.util.StringUtils;
import butterknife.Bind;
import butterknife.ButterKnife;


public class BluetoothGameClientActivity extends BluetoothCheckActivity implements GameController.ChatListener {
    // Precisa utilizar o mesmo UUID que o servidor utilizou para abrir o socket servidor
    protected static final UUID uuid = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    protected BluetoothDevice device;
    protected TextView tMsg, tMsgRecebidas;
    protected GameController jogo;
    protected
    @Bind({R.id.btnZero, R.id.btnOne, R.id.btnTwo, R.id.btnThree, R.id.btnFour, R.id.btnFive,
            R.id.btnSix, R.id.btnSeven, R.id.btnEight})
    List<Button> mBoardButtons;
    protected TicTacToGame mGameController;
    protected
    @Bind(R.id.txtResultadoJogo)
    TextView txtResultadoJogo;
    protected boolean endGame;
    protected boolean tie;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_jogo);
        ButterKnife.bind(this);
        setupActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Device selecionado na lista
        device = getIntent().getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        try {
            // Faz a conexão se abriu no modo jogo cliente
            if (device != null) {
                getSupportActionBar().setTitle("Conectado: " + device.getName());
                // Faz a conexão utilizando o mesmo UUID que o servidor utilizou
                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
                socket.connect();
                // Inicia o controlador jogo
                Log.d(TAG, "iniciou o controlador");
                jogo = new GameController(socket, this);
                jogo.start();
                //startNewGame();
                //findViewById(R.id.btEnviarMsg).setEnabled(true);
            }
        } catch (IOException e) {
            error("Erro ao conectar: " + e.getMessage(), e);
        }
        mGameController = new TicTacToGame();
        startNewGame();
    }


    public void restartGame(View view) {
        startNewGame();
    }

    private void startNewGame() {
        mGameController.clearBoard();
        txtResultadoJogo.setText("");
        // mBoardButtons.size() retorna 9
        for (int i = 0; i < mBoardButtons.size(); i++) {
            mBoardButtons.get(i).setOnClickListener(new ButtonClickListener(i));
            mBoardButtons.get(i).setEnabled(true);
            mBoardButtons.get(i).setText("");
        }
    }

    private void error(final String msg, final IOException e) {
        Log.e(TAG, "Erro no client: " + e.getMessage(), e);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMessageReceived(final String msg) {
        Log.d(TAG, "onMessageReceived: " + msg);
        // É chamado numa thread, portanto use o runOnUiThread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String position = StringUtils.getDigitFromString(msg);
                String player = StringUtils.removeDigitsFromString(msg);
                setMove(Integer.valueOf(position));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (jogo != null) {
            jogo.stop();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setMove(int position) {
        Log.d(TAG, "ENABLE " + mBoardButtons.get(position).isEnabled());
        if (mBoardButtons.get(position).isEnabled()) {
            if (mGameController.getTurn() == TicTacToGame.PLAYER_1) {
                mBoardButtons.get(position).setText(String.valueOf(TicTacToGame.PLAYER_1));
                mGameController.setMove(TicTacToGame.PLAYER_1, position);
                checkForWinnerOrTie();
                mGameController.setTurn(TicTacToGame.PLAYER_2);
            } else {
                mBoardButtons.get(position).setText(String.valueOf(TicTacToGame.PLAYER_2));
                mGameController.setMove(TicTacToGame.PLAYER_2, position);
                // Fazer alguma coisa com o resultado desse método ainda
                checkForWinnerOrTie();
                mGameController.setTurn(TicTacToGame.PLAYER_1);
            }

            mBoardButtons.get(position).setClickable(false);
        }
    }

    private void checkForWinnerOrTie() {
        if (mGameController.weHaveAWinner()) {
            endGame = true;
            txtResultadoJogo.setText(mGameController.getTurn() + " " +
                    getString(R.string.resultado_jogo_venceu));
            disableButtons();
        } else if (mGameController.weTied()) {
            tie = true;
            txtResultadoJogo.setText(getString(R.string.resultado_jogo_empate));
            disableButtons();
        }
    }

    private void disableButtons() {
        for (Button button : mBoardButtons)
            button.setClickable(false);
    }

    private class ButtonClickListener implements View.OnClickListener {

        private int position;

        public ButtonClickListener(int position) {
            this.position = position;
        }
        @Override
        public void onClick(View v) {
            setMove(position);
            // Envia string composta do jogador que fez a jogada mais posição onde foi realizada
            Log.d(TAG, String.valueOf(mGameController.getTurn()) + position + "");
            String msg = String.valueOf(mGameController.getTurn()) + position + "";
            try {
                jogo.sendMessage(msg);
            } catch (IOException e) {
                error("Erro ao escrever: " + e.getMessage(), e);
            }
        }
    }
}
