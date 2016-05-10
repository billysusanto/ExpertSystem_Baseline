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

import com.mobile.expertsystem.controller.CertainlyFactor;
import com.mobile.expertsystem.controller.DatabaseHelper;
import com.mobile.expertsystem.controller.XMLParser;
import com.mobile.expertsystem.model.Aturan;
import com.mobile.expertsystem.model.Gejala;
import com.mobile.expertsystem.model.Penyakit;
import com.mobile.expertsystem.webservice.JavaServlet;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Result extends AppCompatActivity {

    LinearLayout llMain;
    TableLayout tlHasil;
    ScrollView sv;
    TextView tv [], tvSolusi[], tvPenyakit[];
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
    CertainlyFactor cf = new CertainlyFactor();
    XMLParser xmlParser = new XMLParser();
    DatabaseHelper dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        dh = new DatabaseHelper(this);
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
            if(!servletAturanResp.equalsIgnoreCase("disconnect")){
                dh.resetDBAturan();
                dh.insertAturan(servletAturanResp);
                Log.e("State", "ONLINE");
            }
            else{
                servletAturanResp = dh.getAturan().get(0);
            }
            aturan = xmlParser.xmlParserAturan(servletAturanResp);


            servletPenyakitResp = servletPenyakit.execute(4, 1).get();
            if(!servletPenyakitResp.equalsIgnoreCase("disconnect")){
                dh.resetDBPenyakit();
                dh.insertPenyakit(servletPenyakitResp);
                Log.e("State", "ONLINE");
            }
            else{
                servletPenyakitResp = dh.getPenyakit().get(0);
                Log.e("State", "OFFLINE");
            }
            penyakit = xmlParser.xmlParserPenyakit(servletPenyakitResp);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }

        tv = new TextView[listGejala.size()];

        for(int i=0; i<listGejala.size(); i++){
            String detail = "";
            if(listGejala.get(i).getDetail().size() != 0){
                detail = " : " + listGejala.get(i).getDetail().get(0).getName();
            }
            tv[i] = new TextView(this);
            tv[i].setText(i+1 + ". " + listGejala.get(i).getName() + detail);

            listTextViewGejala.add(tv[i]);
            llMain.addView(tv[i]);
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
            cf.setCf(calculate);
            hasilPersentasePenyakit[i] = cf.calculate();
        }

        llMain.addView(textViewHasilDiagnosa);

        tvPenyakit = new TextView [aturan.length];
        for(int i=0; i<aturan.length; i++){

            tvPenyakit[i] = new TextView(this);
            tvPenyakit[i].setPadding(0, 25, 0, 0);
            if(hasilPersentasePenyakit[i] > 0.0) {
                tvPenyakit[i].setText(penyakit[i].getName() + " : " + hasilPersentasePenyakit[i] + " %");
                llMain.addView(tvPenyakit[i]);

                tvSolusi = new TextView [penyakit[i].getSolution().size()];
                for(int k=0; k<penyakit[i].getSolution().size(); k++) {
                    tvSolusi[k] = new TextView(this);
                    tvSolusi[k].setText("Solusi : " + penyakit[i].getSolution().get(k));
                    llMain.addView(tvSolusi[k]);
                }
            }
        }

        sv.addView(llMain);
    }
}
