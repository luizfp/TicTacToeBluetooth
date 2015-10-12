package br.com.luizfp.tictactoe.util;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;

/**
 * Created by luiz on 10/9/15.
 */
public class BluetoothUtils {
    public static void makeVisible(Context context, int durationSeconds) {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, durationSeconds);
        context.startActivity(discoverableIntent);
    }
}