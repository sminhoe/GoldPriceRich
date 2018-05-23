package com.example.user8.goldpricerich;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    EditText edtGoldPerGram, edtGramPurchased, edtTotal;
    WebServiceCall wcs = new WebServiceCall();
    JSONObject jsonOb = new JSONObject();
    String strGoldPerGram, strMsg;
    GoldDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new GoldDB(getApplicationContext());
        edtGoldPerGram = findViewById(R.id.editText);
        edtGramPurchased = findViewById(R.id.editText1);
        edtTotal = findViewById(R.id.editText2);

        Runnable run = new Runnable() {
            @Override
            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("selectLogic", "fnGetCurrPrice"));
                try {
                    jsonOb = wcs.makeHttpRequest(wcs.fnGetURL(), "POST", params);
                    strGoldPerGram = jsonOb.getString("varCurrPrice");
                    strMsg = "Successfully retrieve date and time from server.";
                } catch (Exception e) {
                    strMsg = "Error.";
                    strGoldPerGram = new String("No price");
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        fnDisplayToastMsg(strMsg);
                        edtGoldPerGram.setText(strGoldPerGram);
                    }
                });
            }
        };
        Thread thr = new Thread(run);
        thr.start();
    }

    public void fnDisplayToastMsg(String strText) {
        Toast tst = Toast.makeText(getApplicationContext(), strText, Toast.LENGTH_LONG);
        tst.show();
    }

    public void fnCalculatePrice(View vw) {

        String goldPrice = edtGoldPerGram.getText().toString();
        String goldGram = edtGramPurchased.getText().toString();

        Double goldPriceDouble = Double.parseDouble(goldPrice);
        Double goldGramDouble = Double.parseDouble(goldGram);
        Double TotalPriceDouble = goldPriceDouble * goldGramDouble;

        String a = String.format("%.2f", TotalPriceDouble);
        edtTotal.setText("RM " + a);
    }

    public void fnSave(View vw) {
        Runnable run = new Runnable() {
            @Override
            public void run() {

                String goldPrice = edtGoldPerGram.getText().toString();
                String goldGram = edtGramPurchased.getText().toString();

                Double goldPriceDouble = Double.parseDouble(goldPrice);
                Double goldGramDouble = Double.parseDouble(goldGram);
                Double TotalPriceDouble = goldPriceDouble * goldGramDouble;

                db.fnExecuteSql("INSERT INTO Gold(goldpergram,grampur,total)VALUES ('" + goldPriceDouble + "','" + goldGramDouble + "','" + TotalPriceDouble + "');", getApplicationContext());

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast showSuccess = Toast.makeText(getApplicationContext(), "Information Successfully Saved!", Toast.LENGTH_SHORT);
                        showSuccess.show();
                    }
                });
            }
        };

        Thread thrSave = new Thread(run);
        thrSave.start();
    }
}
