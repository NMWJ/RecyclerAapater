# RecyclerAapater
RecyclerView的单一简单适配器

```
package com.jay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.jay.library.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> list;
    private RecyclerAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        EditText editText = findViewById(R.id.edit_query);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        adapter = new RecyclerAdapter<>(this, R.layout.item, list, new RecyclerAdapter.BindView<String>() {
            @Override
            public void bindView(RecyclerAdapter.ViewHolder holder, String obj, int position) {
                holder.setText(R.id.title,obj);
            }
        });

        recyclerView.setAdapter(adapter);


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        initData();

    }

    private void initData() {
        for(int i=0;i<50;i++){
            list.add("这是第"+i+"条内容");
        }
        adapter.notifyDataSetChanged();
    }

}

```
