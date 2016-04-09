package com.example.serg.testwork;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.serg.testwork.adapters.RecyclerViewItemListAdapter;
import com.example.serg.testwork.models.MyData;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @Bind(R.id.first_recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<MyData> myData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MyData myData1 = new MyData("url", "name name", "type music type music", "count record and info");
            myData.add(myData1);
        }
        final RecyclerView.Adapter adapter = new RecyclerViewItemListAdapter(myData);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
