package com.example.apdem.photo_album.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.apdem.photo_album.R;

import java.util.List;

public class LocationTagListAdapter extends BaseAdapter {

    private Context context;
    private List<String> locationTagList;

    public LocationTagListAdapter(Context context, List<String> list){
        this.context = context;
        this.locationTagList = list;
    }

    @Override
    public int getCount() {
        return locationTagList.size();
    }

    @Override
    public Object getItem(int i) {
        return locationTagList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.tag_list_item, null);
        String personTagVal = locationTagList.get(position);

        ((TextView) view.findViewById(R.id.tag_list_text)).setText(personTagVal);
        return view;
    }

}
