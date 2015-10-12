package br.com.luizfp.tictactoe.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import br.com.luizfp.tictactoe.R;
import br.com.luizfp.tictactoe.bluetooth.BluetoothCheckActivity;
import br.com.luizfp.tictactoe.models.TicTacToGame;
import butterknife.Bind;
import butterknife.ButterKnife;

public class JogoActivity extends BluetoothCheckActivity {

    protected @Bind({R.id.btnZero, R.id.btnOne, R.id.btnTwo, R.id.btnThree, R.id.btnFour, R.id.btnFive,
            R.id.btnSix, R.id.btnSeven, R.id.btnEight}) List<Button> mBoardButtons;
    protected TicTacToGame mGameController;
    protected @Bind(R.id.txtResultadoJogo) TextView txtResultadoJogo;
    protected boolean endGame;
    protected boolean tie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);
        ButterKnife.bind(this);
    }




}
