package br.com.luizfp.tictactoe.bluetooth;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.luizfp.tictactoe.R;
import butterknife.Bind;
import butterknife.ButterKnife;

public class ListaDevicesActivity extends BluetoothCheckActivity implements AdapterView.OnItemClickListener {

    @Bind(R.id.listView)
    ListView listView;
    private List<BluetoothDevice> lista;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_devices);
        ButterKnife.bind(this);

        // Inicia a lista com os devices pareados
        lista = new ArrayList<BluetoothDevice>(btfAdapter.getBondedDevices());
        // Registrar o receiver para receber a mensagem de dispositivos pareados
        this.registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        // Registra o receiver para receber a mensagem do final da busca
        this.registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Garante que não haja mais nenhuma busca sendo realizada
        if (btfAdapter.isDiscovering())
            btfAdapter.cancelDiscovery();
        // Dispara a busca
        btfAdapter.startDiscovery();
        dialog = ProgressDialog.show(this, "Aguarde", "Buscando dispositivos bluetooth...", false,
                true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Garante que a busca é cancelada ao sair
        if (btfAdapter != null)
            btfAdapter.cancelDiscovery();
        // Cancela o registro do receiver
        this.unregisterReceiver(mReceiver);
    }

    // Receiver para receber os broadcasts do Bluetooth
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        // Quantidade dos dispositivos encontrados
        private int count;
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // Se um device foi encontrado
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Recupera o device da intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Apenas insere na lista os devices que ainda não estão pareados
                if (device.getBondState() == BluetoothDevice.BOND_NONE) {
                    lista.add(device);
                    Toast.makeText(context, "Encontrou: " + device.getName() + ":" +
                        device.getAddress(), Toast.LENGTH_SHORT).show();
                    count++;
               }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                // Iniciou busca
                count = 0;
                Toast.makeText(context, "Busca iniciada", Toast.LENGTH_SHORT).show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                // Terminou busca
                Toast.makeText(context, "Busca finalizada. " + count + " devices encontrados",
                        Toast.LENGTH_LONG).show();
                dialog.dismiss();
                // Atualiza o listview. Agora vai ter todos os devices pareados,
                // mais os novos que foram encontrados
                updateLista();
            }
        }
    };

    private void updateLista() {
        // Cria o array com o nome de cada device
        List<String> nomes = new ArrayList<String>();
        for (BluetoothDevice device : lista) {
            // Neste exemplo, esta variável boolean sempre será true, pois esta lista é
            // somente dos pareados
            boolean pareado = device.getBondState() == BluetoothDevice.BOND_BONDED;
            nomes.add(device.getName() + " - " + device.getAddress() +
                    (pareado ? " *pareado" : ""));
        }

        // Cria o adapter para popular o listView
        int layout = android.R.layout.simple_list_item_1;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, layout, nomes);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Recupera o device selecionado
        BluetoothDevice device = lista.get(position);
        // Vai para a tela para enviar a mensagem
        Intent intent = new Intent(this, BluetoothGameClientActivity.class);
        intent.putExtra(BluetoothDevice.EXTRA_DEVICE, device);
        startActivity(intent);
        //String msg = device.getName() + " - " + device.getAddress();
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
