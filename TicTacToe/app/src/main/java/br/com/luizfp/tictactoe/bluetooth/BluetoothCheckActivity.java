package br.com.luizfp.tictactoe.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Set;

import br.com.luizfp.tictactoe.R;
import br.com.luizfp.tictactoe.activities.BaseActivity;


public class BluetoothCheckActivity extends BaseActivity {

    protected BluetoothAdapter btfAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_check);

        //Bluetooth adapter
        btfAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btfAdapter == null) {
            Toast.makeText(this, "Bluetooth não disponível neste dispositivo", Toast.LENGTH_LONG)
                    .show();
            // Activity será fechada nesse caso
            finish();
            return;
        }
        if (btfAdapter.isEnabled()) {
            Toast.makeText(this, "Bluetooth está ligado", Toast.LENGTH_LONG).show();
        } else {
            // Se não está ligado pede para o usuário ligar
            // <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, 0);
        }

        Set<BluetoothDevice> pareados = btfAdapter.getBondedDevices();
        for (BluetoothDevice device : pareados) {
            String nome = device.getName();
            String address = device.getAddress();
            Log.i(TAG, nome + " " + address);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (btfAdapter.isEnabled())
            Toast.makeText(this, "Bluetooth foi ligado", Toast.LENGTH_SHORT).show();
    }
}
