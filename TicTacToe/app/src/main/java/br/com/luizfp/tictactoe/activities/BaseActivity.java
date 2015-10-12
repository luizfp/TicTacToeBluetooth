package br.com.luizfp.tictactoe.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

/**
 * Created by liliani on 27/09/15.
 */
public class BaseActivity extends DebugActivity {

    /*
    protected void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    */

    protected void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void openActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    protected void startService(Class c) {
        Intent intent = new Intent(this, c);
        startService(intent);
    }

    protected void stopService(Class c) {
        Intent intent = new Intent(this, c);
        stopService(intent);
    }
}
