package com.mobile.expertsystem.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.mobile.expertsystem.R;

import com.mobile.expertsystem.model.Aturan;
import com.mobile.expertsystem.model.Detail;
import com.mobile.expertsystem.model.Gejala;
import com.mobile.expertsystem.model.Penyakit;
import com.mobile.expertsystem.webservice.JavaServlet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Result extends AppCompatActivity {

    LinearLayout llMain;
    TableLayout tlHasil;
    ScrollView sv;
    ArrayList<TextView> listTextViewGejala = new ArrayList<>();
    TextView textViewListGejala;
    TextView textViewHasilDiagnosa;

    JavaServlet servletAturan = new JavaServlet();
    JavaServlet servletPenyakit = new JavaServlet();

    ArrayList <Gejala> listGejala = Question.listGejala;

    Aturan aturan [];
    Penyakit penyakit [];
    double hasilPersentasePenyakit [];

    String servletAturanResp, servletPenyakitResp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        sv = (ScrollView) findViewById(R.id.scrollViewResult);

        llMain = new LinearLayout(this);
        tlHasil = new TableLayout(this);
        textViewListGejala = new TextView(this);
        textViewHasilDiagnosa = new TextView(this);

        llMain.setOrientation(LinearLayout.VERTICAL);

        textViewListGejala.setText("Gejala yang Anda alami :");
        textViewListGejala.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
        textViewListGejala.setPadding(0, 0, 0, 10);

        textViewHasilDiagnosa.setText("Hasil Diagnosa");
        textViewHasilDiagnosa.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
        textViewHasilDiagnosa.setPadding(0, 30, 0, 10);

        llMain.addView(textViewListGejala);

        try {
            servletAturanResp = servletAturan.execute(3, 1).get();
            aturan = xmlParserAturan(servletAturanResp);

            servletPenyakitResp = servletPenyakit.execute(4, 1).get();
            penyakit = xmlParserPenyakit(servletPenyakitResp);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }

        for(int i=0; i<listGejala.size(); i++){
            String detail = "";
            if(listGejala.get(i).getDetail().size() != 0){
                detail = " : " + listGejala.get(i).getDetail().get(0).getName();
                //int detailId = publicVar.getListGejala().get(i).getDetail().get(0).getId();
            }
            //Log.e("GEJALA RESULT", publicVar.getListGejala().get(i).getName() + detail);
            TextView tv = new TextView(this);
            tv.setText(i+1 + ". " + listGejala.get(i).getName() + detail);

            //Lightweight Pattern
            listTextViewGejala.add(tv);
            llMain.addView(tv);
        }

        hasilPersentasePenyakit = new double[penyakit.length];

        for(int i=0; i<aturan.length; i++){
            ArrayList <Double> calculate = new ArrayList<>();

            for(int j=0; j<aturan[i].getListGejala().size(); j++) {
                for(int k=0; k<listGejala.size(); k++) {
                    if (listGejala.get(k).getDetail().size() > 0) {
                        if (aturan[i].getListGejala().get(j).getId() == listGejala.get(k).getId()) {
                            for(int l=0; l<aturan[i].getListGejala().get(j).getDetail().size(); l++){
                                for(int m=0; m<listGejala.get(k).getDetail().size(); m++){
                                    if(aturan[i].getListGejala().get(j).getDetail().get(l).getId() == listGejala.get(k).getDetail().get(m).getId()){
                                        calculate.add(aturan[i].getListCf().get(j));
                                    }
                                }
                            }
                        }
                    }
                    else{
                        if (aturan[i].getListGejala().get(j).getId() == listGejala.get(k).getId()) {
                            calculate.add(aturan[i].getListCf().get(j));
                        }
                    }
                }
            }
            hasilPersentasePenyakit[i] = CertainlyFactor(calculate);
        }

        llMain.addView(textViewHasilDiagnosa);

        for(int i=0; i<aturan.length; i++){

            TextView tv = new TextView(this);
            tv.setPadding(0, 25, 0, 0);
            if(hasilPersentasePenyakit[i] > 0.0) {
                tv.setText(penyakit[i].getName() + " : " + hasilPersentasePenyakit[i] + " %");
                llMain.addView(tv);
                for(int k=0; k<penyakit[i].getSolution().size(); k++) {
                    tv = new TextView(this);
                    tv.setText("Solusi : " + penyakit[i].getSolution().get(k));
                    llMain.addView(tv);
                }
            }
        }

        sv.addView(llMain);
    }

    public Aturan [] xmlParserAturan(String xml){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            //replace String as InputSource
            InputSource is = new InputSource(new StringReader(xml));
            Document doc = dBuilder.parse(is);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("rule-base");

            aturan = new Aturan[nList.getLength()];

            for (int i = 0; i < nList.getLength(); i++) {
                aturan[i] = new Aturan();

                Node nNode = nList.item(i);
                Element e = (Element) nNode;

                aturan[i].setSicknessId(Integer.parseInt(nNode.getAttributes().getNamedItem("sickness-id").getNodeValue()));

                NodeList symptomNode = e.getElementsByTagName("symptom");

                for(int j=0; j<symptomNode.getLength(); j++){
                    Detail detail;
                    Gejala gejala = new Gejala();

                    int symptomId = Integer.parseInt(symptomNode.item(j).getAttributes().getNamedItem("id").getNodeValue());
                    gejala.setId(symptomId);

                    NodeList cf = e.getElementsByTagName("cf");
                    aturan[i].getListCf().add(Double.parseDouble(cf.item(j).getTextContent()));

                    try{
                        int detailId = Integer.parseInt(
                                symptomNode.item(j).getAttributes().getNamedItem("detail").getNodeValue());

                        gejala.addDetail(detailId, "");
                    }
                    catch(NullPointerException ex){
                        Log.e("NullPointerEx", ex.toString());
                    }

                    aturan[i].addGejala(gejala);
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

        return aturan;
    }

    public Penyakit [] xmlParserPenyakit(String xml){
        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            //replace String as InputSource
            InputSource is = new InputSource(new StringReader(xml));
            Document doc = dBuilder.parse(is);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("sickness");

            penyakit = new Penyakit[nList.getLength()];

            for (int i = 0; i < nList.getLength(); i++) {
                ArrayList<String> solution = new ArrayList<>();

                penyakit[i] = new Penyakit();

                Node nNode = nList.item(i);
                Element e = (Element) nNode;

                penyakit[i].setId(Integer.parseInt(nNode.getAttributes().getNamedItem("id").getNodeValue()));

                NodeList sicknessNameNode = e.getElementsByTagName("name");
                NodeList solutionNode = e.getElementsByTagName("solution");

                for(int j=0; j<sicknessNameNode.getLength(); j++){
                    penyakit[i].setName(sicknessNameNode.item(j).getTextContent());
                }

                for(int j=0; j<solutionNode.getLength(); j++){
                    solution.add(solutionNode.item(j).getTextContent());
                }

                penyakit[i].setSolution(solution);
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

        return penyakit;
    }

    public double CertainlyFactor(ArrayList <Double> cf){

        DecimalFormat df = new DecimalFormat("#.####");

        double result = 0.0;

        if(cf.size() > 1) {
            for (int i = 0; i < cf.size() - 1; i++) {
                result = cf.get(i) + (cf.get(i + 1) * (1.0 - cf.get(i)));
                cf.set(i + 1, result);
            }
        }
        else if (cf.size() == 1){
            result = cf.get(0);
        }
        else if (cf.size() == 0){
            result = 0.0;
        }

        return Double.parseDouble(df.format(result * 100));
    }
}
