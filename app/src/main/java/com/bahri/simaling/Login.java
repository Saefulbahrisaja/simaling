package com.bahri.simaling;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bahri.simaling.Utils.Userparselable;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import android.util.Log;

import com.bahri.simaling.Preference;

public class Login extends AppCompatActivity {
    public TextView aktivasi;
    Preference sharedPrefManager;
    private ProgressDialog progress;
    private RequestQueue requestQueue;
    StringRequest stringRequest;

    private EditText t_username, t_password;
    private Button btnlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        t_username = (EditText) findViewById(R.id.username);
        t_password = (EditText) findViewById(R.id.password);
        btnlogin = (Button) findViewById(R.id.btn_login);
        aktivasi=(TextView)findViewById(R.id.l_aktivasi);
        requestQueue = Volley.newRequestQueue(this);
        sharedPrefManager = new Preference(this);

        if (sharedPrefManager.getSPSudahLogin()){
            startActivity(new Intent(Login.this, Utama.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
        //Pindah ke aktivasi
        aktivasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent linkaktiv =new Intent(Login.this, daftar.class);
                startActivity(linkaktiv);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();

            }
        });
    }

    private void login() {
        if(!validasi()) return;
            progress = new ProgressDialog(this);
            progress.setMessage("inisialisasi ... ");
            progress.show();
            String url = "http://192.168.56.1/Lingkungan/Api/login.php?";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Userparselable userparselable = new Userparselable();
                    Log.i("Response JSON", "" + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.names().get(0).equals("success")) {
                            t_username.setText("");
                            t_password.setText("");
                            userparselable.setId(jsonObject.getJSONArray("pengguna").getJSONObject(0).getInt("id"));
                            userparselable.setNik(jsonObject.getJSONArray("pengguna").getJSONObject(0).getString("nik"));
                            userparselable.setNama(jsonObject.getJSONArray("pengguna").getJSONObject(0).getString("nama"));
                            userparselable.setAlamat(jsonObject.getJSONArray("pengguna").getJSONObject(0).getString("alamat"));
                            userparselable.setRt(jsonObject.getJSONArray("pengguna").getJSONObject(0).getString("rt"));
                            userparselable.setRw(jsonObject.getJSONArray("pengguna").getJSONObject(0).getString("rw"));
                            userparselable.setKel(jsonObject.getJSONArray("pengguna").getJSONObject(0).getString("kel"));
                            userparselable.setKab(jsonObject.getJSONArray("pengguna").getJSONObject(0).getString("kab"));
                            userparselable.setNegara(jsonObject.getJSONArray("pengguna").getJSONObject(0).getString("negara"));
                            userparselable.setImage(jsonObject.getJSONArray("pengguna").getJSONObject(0).getString("image"));
                            Toast.makeText(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                            String nik = jsonObject.getString("success");
                            sharedPrefManager.saveSPString(Preference.SP_NIK, nik);
                            sharedPrefManager.saveSPBoolean(Preference.SP_SUDAH_LOGIN, true);
                            startActivity(new Intent(getApplicationContext(), Utama.class)
                                    .putExtra("datauser", userparselable)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                            Log.i("Request JSON", "" + jsonObject.getString("error"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progress.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Tidak Ada Koneksi", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    String snik = t_username.getText().toString();
                    String spass = t_password.getText().toString();

                    Map<String, String> parameter = new HashMap<>();
                    parameter.put("nik", snik);
                    parameter.put("password", spass);
                    return parameter;
                }
            };

            requestQueue.add(stringRequest);
    }

    private boolean validasi() {
        boolean valid = true;
        String snik = t_username.getText().toString();
        String spass = t_password.getText().toString();

        if(snik.isEmpty()|| t_username.length() < 16 || t_username.length() > 16){
            t_username.setError("No KTP tidak boleh kosong, No KTP harus 16 digit angka");
            valid = false;
        }else {
            t_username.setError(null);
        }

        if(spass.isEmpty()){
            t_password.setError("Password Tidak boleh kosong");
            valid = false;
        }else {
            t_password.setError(null);
        }

        return valid;
    }

}
