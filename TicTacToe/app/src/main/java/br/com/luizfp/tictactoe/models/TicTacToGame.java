package br.com.luizfp.tictactoe.models;

import android.util.Log;

import java.util.Random;

/**
 * Created by luiz on 10/9/15.
 */
public class TicTacToGame {

    private char mBoard[];
    public static final char PLAYER_1 = 'X';
    public static final char PLAYER_2 = 'O';
    public static final char EMPTY_SPACE = ' ';
    private static final int BOARD_SIZE = 9;
    private char winner;
    private char turn;

    public TicTacToGame() {
        mBoard = new char[BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++)
            mBoard[i] = EMPTY_SPACE;
        selectFirstPlayer();
    }

    public char getTurn() {
        return turn;
    }

    public void setTurn(char turn) {
        this.turn = turn;
    }

    private void selectFirstPlayer() {
        //random.nextInt(max - min + 1) + min
        Random random = new Random();
        int player = random.nextInt(2);
        Log.d("tictactoe", player + "");
        if (player == 0)
            turn = PLAYER_1;
        else
            turn = PLAYER_2;
    }

    public void clearBoard() {
        for (int i = 0; i < BOARD_SIZE; i++)
            mBoard[i] = EMPTY_SPACE;
    }

    public void setMove(char player, int position) {
        mBoard[position] = player;
    }

    public boolean weHaveAWinner() {
        for (int i = 0; i <= 6; i = i + 3) {
            if (mBoard[i] == PLAYER_1 && mBoard[i+1] == PLAYER_1 && mBoard[i+2] == PLAYER_1) {
                setWinner(PLAYER_1);
                return true;
            } else if (mBoard[i] == PLAYER_2 && mBoard[i+1] == PLAYER_2 && mBoard[i+2] == PLAYER_2) {
                setWinner(PLAYER_2);
                return true;
            }
        }
        for (int i = 0; i <= 2; i++) {
            if (mBoard[i] == PLAYER_1 && mBoard[i+3] == PLAYER_1 && mBoard[i+6] == PLAYER_1) {
                setWinner(PLAYER_1);
                return true;
            } else if (mBoard[i] == PLAYER_2 && mBoard[i+3] == PLAYER_2 && mBoard[i+6] == PLAYER_2) {
                setWinner(PLAYER_2);
                return true;
            }
        }

        if ((mBoard[0] == PLAYER_1 && mBoard[4] == PLAYER_1 && mBoard[8] == PLAYER_1) ||
                (mBoard[2] == PLAYER_1 && mBoard[4] == PLAYER_1 && mBoard[6] == PLAYER_1)) {
            setWinner(PLAYER_1);
            return true;
        } else if ((mBoard[0] == PLAYER_2 && mBoard[4] == PLAYER_2 && mBoard[8] == PLAYER_2) ||
                (mBoard[2] == PLAYER_2 && mBoard[4] == PLAYER_2 && mBoard[6] == PLAYER_2)) {
            setWinner(PLAYER_2);
            return true;
        }

        // Caso não haja vencedor
        return false;
    }

    public boolean weTied() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] != PLAYER_1 && mBoard[i] != PLAYER_2)
                return false;
        }
        return true;
    }

    public char getWinner() {
        return winner;
    }

    public void setWinner(char winner) {
        this.winner = winner;
    }

    /**
     *
     * @param position a posição que se deseja verificar
     * @return retorna true caso aquele campo já foi jogado
     */
    public boolean isThisPositionFree(int position) {
        return mBoard[position] != EMPTY_SPACE;
    }
}
