package com.baekzombie.hudvoca.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baekzombie.hudvoca.R;
import com.baekzombie.hudvoca.record.WordListItem;

import java.util.ArrayList;

public class WordListAdapter extends BaseAdapter {
    private ArrayList<WordListItem> wordListItemList = new ArrayList<>();

    @Override
    public int getCount() {
        return wordListItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return wordListItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_word, parent, false);
        }

        TextView tvIdx = (TextView) convertView.findViewById(R.id.tv_idx);
        TextView tvVoca = (TextView) convertView.findViewById(R.id.tv_voca);
        TextView tvMean = (TextView) convertView.findViewById(R.id.tv_mean);

        WordListItem listViewItem = wordListItemList.get(position);
        tvIdx.setText(listViewItem.getIdx() + "");
        tvVoca.setText(listViewItem.getVoca());
        tvMean.setText(listViewItem.getMean());

        return convertView;
    }

    public void addItem(int idx, String voca, String mean) {
        WordListItem item = new WordListItem();
        item.setIdx(idx);
        item.setVoca(voca);
        item.setMean(mean);

        wordListItemList.add(item);
    }
}
