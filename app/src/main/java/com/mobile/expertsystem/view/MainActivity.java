package com.mobile.expertsystem.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mobile.expertsystem.R;
import com.mobile.expertsystem.controller.DatabaseHelper;
import com.mobile.expertsystem.controller.XMLParser;
import com.mobile.expertsystem.model.Organ;
import com.mobile.expertsystem.webservice.JavaServlet;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    ListView listOrgan;
    Organ organ [];
    public static Organ selectedOrgan;
    String organName [];
    ArrayAdapter <String> adapter;
    String servletResp;
    Intent i;

    JavaServlet servlet = new JavaServlet();

    DatabaseHelper dh;
    XMLParser xmlParser = new XMLParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dh = new DatabaseHelper(this);
        listOrgan = (ListView) findViewById(R.id.listOrgan);

        //Param 1 means request List of Organ, Param 2 means nothing for now (Organ's case)
        try {
            servletResp = servlet.execute(1, 0).get();
            if(!servletResp.equalsIgnoreCase("disconnect")){
                dh.resetDBOrgan();
                dh.insertOrgan(servletResp);
                Log.e("State", "ONLINE");
            }
            else{
                servletResp = dh.getOrgan().get(0);
                Log.e("State", "OFFLINE");
            }
        }
        catch(ExecutionException e){
            e.printStackTrace();
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }

        organ = xmlParser.xmlParserOrgan(servletResp);

        organName = new String[organ.length];

        for(int i=0; i<organ.length; i++){
            organName[i] = organ[i].getName();
        }

        adapter = new ArrayAdapter <String> (this, android.R.layout.simple_list_item_1, organName);
        listOrgan.setAdapter(adapter);

        listOrgan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            i = new Intent(MainActivity.this, Question.class);
            //publicVar.setSelectedOrgan(organ[position]);
            selectedOrgan = organ[position];
            startActivity(i);
            finish();
            }
        });

    }
}
