package com.bahri.simaling;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.jar.JarException;

public class daftar extends AppCompatActivity {
    private TextView login;
    private EditText noktp,password,notelepon;
    private Button btn_daftar;
    private ProgressDialog progress;
    StringRequest stringRequest;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        login=findViewById(R.id.l_login);
        noktp = findViewById(R.id.noktp);
        password = findViewById(R.id.password);
        notelepon = findViewById(R.id.notelepon);
        btn_daftar = findViewById(R.id.daftar);
        requestQueue = Volley.newRequestQueue(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent baliklogin=new Intent(daftar.this, Login.class);
                startActivity(baliklogin);
            }
        });

        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register() {
        if(!validasi()) return;
        progress = new ProgressDialog(this);
        progress.setMessage("inisialisasi ... ");
        progress.show();
        String url = "http://192.168.56.1/Lingkungan/Api/daftar.php?";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Userparselable userparselable = new Userparselable();
                Log.i("Response JSON", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success")) {
                        noktp.setText("");
                        password.setText("");
                        notelepon.setText("");
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
                        progress.dismiss();
                        Intent intent = new Intent(getApplicationContext(), Utama.class);
                        intent.putExtra("datauser", userparselable);
                        startActivity(intent);
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
                String snik = noktp.getText().toString();
                String spass = password.getText().toString();
                String stlp = notelepon.getText().toString();

                Map<String, String> parameter = new HashMap<>();
                parameter.put("nik", snik);
                parameter.put("password", spass);
                parameter.put("notlp",stlp);
                return parameter;
            }
        };
        requestQueue.add(stringRequest);
    }

    private boolean validasi() {
        boolean valid = true;
        String snik = noktp.getText().toString();
        String spass = password.getText().toString();

        if(snik.isEmpty()|| noktp.length() < 16 || noktp.length() > 16){
            noktp.setError("No KTP tidak boleh kosong No KTP harus diisi sebanyak 16 digit angka");
            valid = false;
        }else {
            noktp.setError(null);
        }

        if(spass.isEmpty()){
            password.setError("Password Tidak boleh kosong");
            valid = false;
        }else {
            password.setError(null);
        }

        return valid;
    }
}
