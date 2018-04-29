package com.example.apdem.photo_album;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class PersonTagListAdapter extends BaseAdapter {

    private Context context;
    private List<String> personTagList;

    public PersonTagListAdapter(Context context, List<String> list){
        this.context = context;
        this.personTagList = list;
    }

    @Override
    public int getCount() {
        return personTagList.size();
    }

    @Override
    public Object getItem(int i) {
        return personTagList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.tag_list_item, null);
        String personTagVal = personTagList.get(position);

        ((TextView) view.findViewById(R.id.tag_list_text)).setText(personTagVal);
        return view;
    }
}
