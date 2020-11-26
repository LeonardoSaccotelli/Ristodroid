package controllers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ristodroid.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.TreeMap;

import model.Order;
import persistence.LoadJson;

public class MainActivity extends AppCompatActivity {
    private static Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = "http://192.168.1.115/Ristodroid/Service/getMenu.php?lang=" + Locale.getDefault().getLanguage();
        getJsonResponse(url);
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startActivity(new Intent(getApplicationContext(), Dashboard.class));
    }

    public static void setOrder(Order order) {
        MainActivity.order = order;
    }

    public static Order getOrder() {
        return order;
    }

    /**
     * Procedura per il caricamento del json nel db
     * @param url indirizzo per la richiesta GET
     */
    private void getJsonResponse(String url) {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONObject jsonDb = response.getJSONObject("db");
                TreeMap<String, JSONArray> tables = getDbTablesFromJson(jsonDb);
                LoadJson.insertJsonIntoDb(tables, getApplicationContext());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast toast= Toast.makeText(getApplicationContext(),R.string.SyncFailed,Toast.LENGTH_LONG);
            toast.show();
        });

        Volley.newRequestQueue(this).add(jsonRequest);
    }

    /**
     * Ritorna una mappa chiave (nome tabella) valore (row della rispettiva tabella) del db
     * @param db database
     * @return tables
     * @throws JSONException json exception
     */
    private TreeMap<String, JSONArray> getDbTablesFromJson(JSONObject db) throws JSONException {
        TreeMap<String, JSONArray> tables = new TreeMap<>();
        JSONArray keys = db.names();
        for(int i=0; i< db.length(); i++) {
            if (keys != null) {
                tables.put(keys.getString(i) ,db.getJSONArray(keys.getString(i)));
            }
        }
        return tables;
    }


    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
