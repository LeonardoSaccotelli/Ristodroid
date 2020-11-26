package nfc;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ristodroid.R;
import com.victor.loading.rotate.RotateLoading;

import controllers.Dashboard;
import controllers.MainActivity;
import model.Order;
import persistence.RistodroidDBSchema;
import persistence.SqLiteDb;


public class SenderActivity extends AppCompatActivity implements OutcomingNfcManager.NfcActivity {

    private NfcAdapter nfcAdapter;
    private String order;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender);

        SQLiteDatabase db = new SqLiteDb(getApplicationContext()).getReadableDatabase();
        Intent intent = getIntent();
        String key = intent.getExtras().getString("order");
        order = Order.getJsonOrderDb(db, key);
        db.delete(RistodroidDBSchema.JsonOrderTable.NAME, RistodroidDBSchema.JsonOrderTable.Cols.ID + "=?", new String[]{key});

        if (!isNfcSupported()) {
            Toast.makeText(this,R.string.nfc_not_supported, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!nfcAdapter.isEnabled()) {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
            alertbox.setTitle(R.string.enableNFCRequestTitle);
            alertbox.setMessage(getString(R.string.enableNFCRequestContent));
            alertbox.setPositiveButton(R.string.TurnOn, (dialog, which) -> {
                Intent intentNfcSetting = new Intent(Settings.ACTION_NFC_SETTINGS);
                startActivity(intentNfcSetting);

            });
            alertbox.setNegativeButton(R.string.Close, (dialog, which) -> {
                MainActivity.getOrder().setConfirmed(false);
                startActivity(new Intent(this, Dashboard.class));
            });
            alertbox.show();
        }else{
            initViews();
            // encapsulate sending logic in a separate class
            OutcomingNfcManager outcomingNfccallback = new OutcomingNfcManager(this);
            this.nfcAdapter.setOnNdefPushCompleteCallback(outcomingNfccallback, this);
            this.nfcAdapter.setNdefPushMessageCallback(outcomingNfccallback, this);
        }
    }

    private void initViews() {
        TextView tvOutcomingMessage = findViewById(R.id.tv_out_label);
        tvOutcomingMessage.setVisibility(View.VISIBLE);
        tvOutcomingMessage.setText(R.string.label_NFC_SEND);
        RotateLoading loading = findViewById(R.id.rotateloading);
        loading.setVisibility(View.VISIBLE);
        loading.start();
        setOutGoingMessage();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private boolean isNfcSupported() {
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        return this.nfcAdapter != null;
    }

    private void setOutGoingMessage() {
        String outMessage = order;
    }

    @Override
    public String getOutcomingMessage() {
        return this.order;
    }

    @Override
    public void signalResult() {
        // this will be triggered when NFC message is sent to a device.
        // should be triggered on UI thread. We specify it explicitly
        // cause onNdefPushComplete is called from the Binder thread
        runOnUiThread(() ->{
                Toast.makeText(SenderActivity.this, R.string.message_beaming_complete, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, Dashboard.class));
        });
    }
}