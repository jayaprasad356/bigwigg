package com.gm.bigwigg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gm.bigwigg.helper.ApiConfig;
import com.gm.bigwigg.helper.Constant;
import com.gm.bigwigg.helper.Session;
import com.google.firebase.database.core.Repo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {
    Spinner spinViolence;
    Button btnReport;
    EditText describtion;
    Session session;
    String post_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        post_id = getIntent().getStringExtra(Constant.POST_ID);
        session = new Session(ReportActivity.this);
        btnReport = findViewById(R.id.btnReport);
        spinViolence = findViewById(R.id.spinViolence);
        describtion = findViewById(R.id.describtion);

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                report();
            }
        });
    }
    private void report(){
        Map<String, String> params = new HashMap<>();
        //request
        params.put(Constant.REPORT_TYPE,spinViolence.getSelectedItem().toString());
        params.put(Constant.DESCRIPION,describtion.getText().toString().trim());
        params.put(Constant.USER_ID,session.getData(Constant.ID));
        params.put(Constant.POST_ID,post_id);

        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Toast.makeText(this, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ReportActivity.this, MainActivity.class));
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
        }, ReportActivity.this, Constant.REPORT_URL, params,true);



    }
}