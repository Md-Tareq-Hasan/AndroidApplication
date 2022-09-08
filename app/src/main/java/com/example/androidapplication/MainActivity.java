package com.example.androidapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText address;
    Button serach_btn;
    TextView transaction_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        address = (EditText) findViewById(R.id.input);
        serach_btn = (Button) findViewById(R.id.button);
        transaction_info = (TextView) findViewById(R.id.Information);
        serach_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.button){
            jsonTask jTask = new jsonTask();
            jTask.execute();
        }

    }

    public class jsonTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            String user_address;
            String user_balance;
            String user_received;
            String user_sent;
            String user_nonce;

            try {
                URL url = new URL("https://mocki.io/v1/a6e6e366-f33d-473f-ab6b-b17ef52da885");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String line = "";
                StringBuffer lastBuffer = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                String file = stringBuffer.toString();
                JSONArray jsonarray = new JSONArray(file);
                for (int i = 0; i<jsonarray.length();i++) {
                    JSONObject arrayobject = (JSONObject) jsonarray.get(i);
                    user_address = arrayobject.getString("address");
                    if (user_address.equals(address.getText().toString())) {
                        user_balance = arrayobject.getString("balance");
                        user_received = arrayobject.getString("total_received");
                        user_sent = arrayobject.getString("total_sent");
                        user_nonce = arrayobject.getString("nonce");
                        lastBuffer.append("Balance: "+user_balance+"\n");
                        lastBuffer.append("Total Received: "+user_received+"\n");
                        lastBuffer.append("Total Sent: "+user_sent+"\n");
                        lastBuffer.append("Nonce: "+user_nonce);
                        return lastBuffer.toString();
                    }
                }
                return "Enter valid address";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                httpURLConnection.disconnect();
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            transaction_info.setText(s);


        }
    }
}
