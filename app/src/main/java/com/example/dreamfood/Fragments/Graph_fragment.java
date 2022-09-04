package com.example.dreamfood.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dreamfood.BusinessLayer.Classes.grade;
import com.example.dreamfood.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Hashtable;

public class Graph_fragment extends Fragment {
View v;
    Hashtable<String, grade> grades;
public Graph_fragment(Hashtable<String, grade> grades){
    this.grades=grades;
}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.graph_tests, container, false);
      LineChart lineChart=v.findViewById(R.id.line1);
      lineChart.setDragEnabled(true);
      lineChart.setScaleEnabled(true);
        ArrayList<Entry> val=new ArrayList<>();
        int i=0;
       for(grade g:grades.values()){
           val.add(new Entry(i,(int) g.grade));
           i++;
       }
        LineDataSet set1=new LineDataSet(val,"grades");

set1.setFillAlpha(110);
ArrayList<ILineDataSet> dataSets=new ArrayList<>();
dataSets.add(set1);

        LineData data=new LineData(dataSets);
        lineChart.setData(data);
        return v;
    }
}
