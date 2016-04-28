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
import com.mobile.expertsystem.model.Aturan;
import com.mobile.expertsystem.model.Organ;
import com.mobile.expertsystem.webservice.JavaServlet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    ListView listOrgan;
    Organ organ [];
    public static Organ selectedOrgan;
    String organName [];
    ArrayAdapter <String> adapter;
    String servletResp;
    //Singleton publicVar = Singleton.getInstance();
    Intent i;

    JavaServlet servlet = new JavaServlet();

    Aturan aturan [];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listOrgan = (ListView) findViewById(R.id.listOrgan);

        //Param 1 means request List of Organ, Param 2 means nothing for now (Organ's case)
        try {
            servletResp = servlet.execute(1, 0).get();
        }
        catch(ExecutionException e){
            e.printStackTrace();
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }

        organ = xmlParserOrgan(servletResp);

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

    public Organ[] xmlParserOrgan(String xml){

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            //replace String as InputSource
            InputSource is = new InputSource(new StringReader(xml));
            Document doc = dBuilder.parse(is);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("name");

            organ = new Organ[nList.getLength()];

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                Element e = (Element) nNode;

                organ[i] = new Organ();

                organ[i].setId(Integer.parseInt(e.getAttribute("id")));
                organ[i].setName(nNode.getTextContent());
            }

        }
        catch(ParserConfigurationException e){
            Log.e("ParseConfigExc", e.toString());
        }
        catch(IOException e){
            Log.e("IOException", e.toString());
        }
        catch(SAXException e){
            Log.e("SAXEXception", e.toString());
        }

        return organ;
    }
}
