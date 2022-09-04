package com.example.dreamfood.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Classes.Video_Adapter;
import com.example.dreamfood.BusinessLayer.Classes.video;
import com.example.dreamfood.R;

import java.util.ArrayList;

public class List_Video_Fragment extends Fragment {
    View v;
    Video_Adapter video_adapter;
    ArrayList<video> videos;
    Strings con=new Strings();

    ListView list;
    public List_Video_Fragment(ArrayList<video> recording_class){
        videos=recording_class;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.videos_list, container, false);
        list=v.findViewById(R.id.list);
        video_adapter=new Video_Adapter(getContext(), videos);
        list.setAdapter(video_adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        return v;
    }
}
