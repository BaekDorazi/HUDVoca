package com.baekzombie.hudvoca;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.baekzombie.hudvoca.adapter.WordListAdapter;
import com.baekzombie.hudvoca.record.VocaInfo;

import java.util.ArrayList;

public class WordActivity extends AppCompatActivity {
    ListView wordList;
    WordListAdapter adapter;

    Intent intent;
    ArrayList<VocaInfo> vocaInfos = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        init();

        intent = getIntent();
        vocaInfos = (ArrayList<VocaInfo>) intent.getSerializableExtra("vocaInfos");

        adapter = new WordListAdapter();
        for (int i = 0; i < vocaInfos.size(); i++) {
            adapter.addItem(vocaInfos.get(i).getId(), vocaInfos.get(i).getVocabulary(), vocaInfos.get(i).getMean());
        }
        wordList.setAdapter(adapter);
    }

    private void init() {
        wordList = (ListView) findViewById(R.id.word_list);
    }
}
