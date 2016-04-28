package com.mobile.expertsystem.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mobile.expertsystem.R;
import com.mobile.expertsystem.model.Gejala;
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
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Question extends AppCompatActivity {

    ArrayList<CheckBox> checkBoxGejala = new ArrayList<>();
    ArrayList<RadioGroup> radioGroupGejala = new ArrayList<>();
    ArrayList<RadioButton> radioButtonGejala = new ArrayList<>();
    ArrayList<TextView> textViewGejala = new ArrayList<>();


    ScrollView sv;
    LinearLayout ll;
    Button submit;
    Organ selectedOrgan = MainActivity.selectedOrgan;

    //Variabel dari Singleton
    public static ArrayList <Gejala> listGejala = new ArrayList<>();

    JavaServlet servlet = new JavaServlet();
    String servletResp;
    Gejala gejala [];

    int idRadioButtonCounter = 0, idRadioGroupCounter =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        sv = (ScrollView) findViewById(R.id.scrollViewQuestion);
        ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        try {
            servletResp = servlet.execute(2, selectedOrgan.getId()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        finally{
            gejala = xmlParserGejala(servletResp);
        }

        for(int i=0; i<gejala.length; i++){

            if(gejala[i].getDetail() == null || (gejala[i].getDetail().size() == 0)){
                CheckBox cb = new CheckBox(this);
                cb.setText(gejala[i].getName());
                cb.setId(gejala[i].getId());

                checkBoxGejala.add(cb);
                ll.addView(cb);
            }

            else{
                TextView tv = new TextView(this);
                tv.setText(gejala[i].getName());
                ll.addView(tv);
                textViewGejala.add(tv);

                RadioGroup rg = new RadioGroup(this);
                rg.setId(idRadioGroupCounter);
                rg.setOrientation(RadioGroup.VERTICAL);
                radioGroupGejala.add(rg);

                idRadioGroupCounter++;

                for(int j=0; j<gejala[i].getDetail().size(); j++){
                    RadioButton rb = new RadioButton(this);
                    rb.setText(gejala[i].getDetail().get(j).getName());
                    rb.setId(idRadioButtonCounter);
                    rg.addView(rb);
                    radioButtonGejala.add(rb);

                    idRadioButtonCounter++;
                }
                ll.addView(rg);
            }
        }

        submit = new Button(this);
        submit.setText("Submit Symptom");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelectedSymptom();

                Intent i = new Intent(Question.this, Result.class);
                startActivity(i);
            }
        });

        ll.addView(submit);
        sv.addView(ll);
    }

    public void getSelectedSymptom(){
        for(int i=0; i<checkBoxGejala.size(); i++) {
            if (checkBoxGejala.get(i).isChecked()) {

                for(int j=0; j<gejala.length; j++){
                    if(gejala[j].getId() == checkBoxGejala.get(i).getId()){
                        listGejala.add(gejala[j]);
                    }
                }
            }
        }

        for(int i=0; i<radioGroupGejala.size(); i++){

            int selected = radioGroupGejala.get(i).getCheckedRadioButtonId();
            if(selected != -1) {

                for(int j=0; j<gejala.length; j++){

                    if(gejala[j].getDetail().size() != 0) {
                        for(int k=0; k<gejala[j].getDetail().size(); k++) {
                            if (gejala[j].getDetail().get(k).getName().equalsIgnoreCase(radioButtonGejala.get(selected).getText().toString())) {
                                Gejala gejalaTemp = new Gejala();
                                gejalaTemp.setId(gejala[j].getId());
                                gejalaTemp.setName(gejala[j].getName());

                                gejalaTemp.addDetail(gejala[j].getDetail().get(k).getId(), gejala[j].getDetail().get(k).getName());

                                listGejala.add(gejalaTemp);
                            }
                        }
                    }
                }
            }
        }
    }

    public Gejala[] xmlParserGejala(String xml){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            //replace String as InputSource
            InputSource is = new InputSource(new StringReader(xml));
            Document doc = dBuilder.parse(is);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("name");

            gejala = new Gejala[nList.getLength()];

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                Element e = (Element) nNode;

                gejala[i] = new Gejala();

                NodeList dList = nNode.getChildNodes();

                gejala[i].setId(Integer.parseInt(e.getAttribute("id")));
                gejala[i].setName(dList.item(0).getTextContent());

                if (dList.getLength() > 1) {
                    for (int j = 1; j <= dList.getLength() - 1; j++) {
                        gejala[i].addDetail(Integer.parseInt(dList.item(j).getAttributes().getNamedItem("id").getNodeValue()),                                dList.item(j).getTextContent());
                    }
                }
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

        return gejala;
    }
}
