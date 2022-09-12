package com.example.dreamfood.Fragments;

import android.app.Dialog;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dreamfood.BusinessLayer.Classes.grade;
import com.example.dreamfood.BusinessLayer.Orders_Adapter.Pick_Materials_Adapter;
import com.example.dreamfood.BusinessLayer.Orders_Adapter.Pick_Quiz_Adapter;
import com.example.dreamfood.BusinessLayer.Quiz;
import com.example.dreamfood.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Hashtable;

public class Graph_fragment extends Fragment {
View view;
Dialog d;

Hashtable<String,Quiz> materials;
    Hashtable<String, Quiz> quizHashtable;
    ArrayList<Quiz> quizHelper;
String s;
    Hashtable<String, grade> grades;
public Graph_fragment(Hashtable<String, grade> grades,Hashtable<String,Quiz> quizHashtable){
    this.grades=grades;
   this.quizHashtable=quizHashtable;
    s="test";
}
public Graph_fragment( Hashtable<String,Quiz> quizHashtable,int i){
    this.quizHashtable=quizHashtable;
s="quiz";
}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.graph_tests, container, false);
        lineChart=view.findViewById(R.id.line1);
        gradeArrayList=new Hashtable<>();
        materials=new Hashtable<>();
        Graph_Settings();



        return view;
    }

    ListView list;
    AutoCompleteTextView autoCompleteTextView,subjects;
    ArrayList<String> items;

    public void Graph_Settings(){
        d=new Dialog(getContext());
        d.setContentView(R.layout.graph_settings);
        autoCompleteTextView=d.findViewById(R.id.auto_complete_txt);
        subjects=d.findViewById(R.id.subjects);
        items=new ArrayList<>();
        items.add("Quiz");items.add("Test");

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,items);
        autoCompleteTextView.setAdapter(arrayAdapter);
       autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               String item=items.get(position);
               s=item;
               if(item.equals("Quiz")){
                   ArrayList<Quiz> quizzes=new ArrayList<>();
                   ArrayList<String> quiz_subjects=new ArrayList<>();
                   for(Quiz q:quizHashtable.values()) {
                       quizzes.add(q);
                       if(!quiz_subjects.contains(q.type)){
                           quiz_subjects.add(q.type);
                       }
                   }
                   ArrayAdapter<String> subjects_adapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,quiz_subjects);
                   subjects.setAdapter(subjects_adapter);
                   subjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                       @Override
                       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                           String sub=parent.getItemAtPosition(position).toString();
                           initQuiz_Graph(sub);
                           d.dismiss();
                       }
                   });
                   materials=new Hashtable<>();

                   Pick_Quiz_Adapter pick_quiz_adapter=new Pick_Quiz_Adapter(getContext(),quizzes,quizHashtable.keySet(),materials);
                   list=d.findViewById(R.id.list);
                   list.setAdapter(pick_quiz_adapter);

               }
               else{
                   ArrayList<grade> quizzes=new ArrayList<>();
                   ArrayList<String> test_subjects=new ArrayList<>();
                   for(grade q:grades.values()) {
                       quizzes.add(q);
                       try {

                      if(q.subject!=null) {
                          if (!test_subjects.contains(q.subject)) {

                              test_subjects.add(q.subject);
                          }
                      }
                       }
                       catch (Exception e){

                       }
                   }


                   ArrayAdapter<String> subjects_adapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,test_subjects);
                   subjects.setAdapter(subjects_adapter);
                   subjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                       @Override
                       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                           String sub=test_subjects.get(position).toString();
                           initTest_Graph(sub);
                           d.dismiss();
                       }
                   });
                   Pick_Materials_Adapter pick_quiz_adapter=new Pick_Materials_Adapter(getContext(),quizzes,grades.keySet(),gradeArrayList);
                   list=d.findViewById(R.id.list);
                   list.setAdapter(pick_quiz_adapter);

               }

           }
       });
        graph=d.findViewById(R.id.graph);

        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==graph){
                    d.dismiss();

                    lineChart.setDragEnabled(true);
                    lineChart.setScaleEnabled(true);
                    ArrayList<Entry> val=new ArrayList<>();
                    int i=1;
                    LineDataSet set1;
                    if(s.equals("test")) {
                        Toast.makeText(getContext(), String.valueOf(gradeArrayList.size()), Toast.LENGTH_SHORT).show();
                        for(grade g:gradeArrayList.values()){

                            val.add(new Entry(i, (int) g.grade));
                            i++;
                        }
                        Toast.makeText(getContext(), String.valueOf(val.size()), Toast.LENGTH_SHORT).show();
                        set1 = new LineDataSet(val, "test grades");
                    }
                    else{
                        for(Quiz q:materials.values()){
                            val.add(new Entry(i, (int) q.grade));
                            i++;
                        }

                        set1 = new LineDataSet(val, "quiz grades");
                    }
                    set1.setFillAlpha(110);
                    ArrayList<ILineDataSet> dataSets=new ArrayList<>();
                    dataSets.add(set1);

                    LineData data=new LineData(dataSets);
                    lineChart.setData(data);

                    Canvas canvas=new Canvas();
                    lineChart.draw(canvas);

                }
            }
        });
        d.show();
    }

    private void initTest_Graph(String sub) {
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        ArrayList<Entry> val=new ArrayList<>();
        int i=1;
        LineDataSet set1;
        for(grade q:grades.values()){
            if(q.subject!=null) {
                if (q.subject.equals(sub)) {
                    val.add(new Entry(i, (int) q.grade));
                    i++;
                }
            }
        }

        set1 = new LineDataSet(val, "Test grades");


        set1.setFillAlpha(110);
        ArrayList<ILineDataSet> dataSets=new ArrayList<>();
        dataSets.add(set1);

        LineData data=new LineData(dataSets);
        lineChart.setData(data);

        Canvas canvas=new Canvas();
        lineChart.draw(canvas);
    }

    Button graph;
    LineChart lineChart;
  Hashtable<String,grade> gradeArrayList;
  public void initQuiz_Graph(String sub){
      lineChart.setDragEnabled(true);
      lineChart.setScaleEnabled(true);
      ArrayList<Entry> val=new ArrayList<>();
      int i=1;
      LineDataSet set1;
       for(Quiz q:quizHashtable.values()){

            if(q.type.equals(sub)) {
                val.add(new Entry(i, (int) q.grade));
                i++;
            }
          }

          set1 = new LineDataSet(val, "Quiz grades");


      set1.setFillAlpha(110);
      ArrayList<ILineDataSet> dataSets=new ArrayList<>();
      dataSets.add(set1);

      LineData data=new LineData(dataSets);
      lineChart.setData(data);

      Canvas canvas=new Canvas();
      lineChart.draw(canvas);
  }

}
