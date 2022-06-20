package com.gm.bigwigg;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gm.bigwigg.helper.ApiConfig;
import com.gm.bigwigg.helper.Constant;
import com.gm.bigwigg.helper.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TestLoginActivity extends AppCompatActivity {
    EditText etEmail,etPassword;
    Button btnLogin;
    Activity activity;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_login);
        activity = TestLoginActivity.this;
        session = new Session(activity);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etEmail.getText().toString().trim().equals("jayaprasad356@gmail.com") && etPassword.getText().toString().trim().equals("1234567890")){
                    registerEmail("prasad",etEmail.getText().toString().trim(),Uri.parse("https://www.thefamouspeople.com/profiles/images/sundar-pichai-5.jpg"));
                }
            }
        });
    }

    private void registerEmail(String name, String email, Uri profile) {
        Map<String, String> params = new HashMap<>();
        //request
        params.put(Constant.EMAIL,email);
        params.put(Constant.NAME,name);
        params.put(Constant.PROFILE,String.valueOf(profile));

        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);


                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        session.setBoolean("is_logged_in", true);
                        session.setUserData(jsonArray.getJSONObject(0).getString(Constant.ID),jsonArray.getJSONObject(0).getString(Constant.PROFILE),jsonArray.getJSONObject(0).getString(Constant.NAME), jsonArray.getJSONObject(0).getString(Constant.EMAIL));
                        session.setData(Constant.DESCRIPION,jsonArray.getJSONObject(0).getString(Constant.DESCRIPION));
                        session.setData(Constant.ROLE,jsonArray.getJSONObject(0).getString(Constant.ROLE));
                        startActivity(new Intent(activity, MainActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(this, "Failed"+jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }



            }
            else {
                Toast.makeText(this, String.valueOf(response) +String.valueOf(result), Toast.LENGTH_SHORT).show();

            }
            //pass url
        }, activity, Constant.EMAIL_REGISTER_URL, params,true);



    }

}