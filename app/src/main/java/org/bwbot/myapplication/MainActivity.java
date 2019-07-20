package org.bwbot.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import javagalileo.GalileoSDK;
import javagalileo.listeners.OnConnectEventListener;
import javagalileo.listeners.OnDisconnectEventListener;
import javagalileo.listeners.OnStatusUpdateEventListener;
import javagalileo.models.GalileoStatus;
import javagalileo.models.ServerInfo;

public class MainActivity extends AppCompatActivity {

    private GalileoSDK sdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        checkPermission();
        sdk = new GalileoSDK();
        sdk.Connect("8FB56D27D6C961E9036F62182ADE9544D71E23C31E5DF4C7DD692B9E4296A131434B1066D365", true, 10000, new OnConnectEventListener() {
            @Override
            public void OnConnected(ServerInfo.GALILEO_RETURN_CODE galileo_return_code, String s) {
                Log.d("Randoms", "Connected");
            }
        }, new OnDisconnectEventListener() {
            @Override
            public void OnDisconnected(ServerInfo.GALILEO_RETURN_CODE galileo_return_code, String s) {
                Log.d("Randoms", "Disconnected");
            }
        });
        sdk.SetCurrentStatusCallback(new OnStatusUpdateEventListener() {
            @Override
            public void OnStatusUpdated(ServerInfo.GALILEO_RETURN_CODE galileo_return_code, GalileoStatus galileoStatus) {
                Log.d("Randoms", galileoStatus.getPower() + "");
                Log.d("Randoms", "status updated");
            }
        });
    }

    private void checkPermission(){
        int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        int permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sdk.Release();
        Log.d("Randoms", "Stopped");
    }
}
