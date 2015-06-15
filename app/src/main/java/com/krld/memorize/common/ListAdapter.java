package com.krld.memorize.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.krld.memorize.EditorActivity;
import com.krld.memorize.R;
import com.krld.memorize.model.MeasurementLegacy;
import com.krld.memorize.models.Measurement;

import java.text.SimpleDateFormat;
import java.util.List;

public class ListAdapter extends BaseAdapter {


    private final Context context;

    public ListAdapter(Context context) {
        super();
        this.context = context;
    }

    private List<Measurement> items;

    @Override
    public int getCount() {
        if (items == null) return 0;
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            v = vi.inflate(R.layout.li_table, parent, false);
        }

        Measurement obj = (Measurement) getItem(position);

        if (obj != null) {
            TextView weightView = (TextView) v.findViewById(R.id.textViewWeight);
            if (weightView != null) {
                weightView.setText(FormatterHelper.formatDouble(obj.value));
            }
            TextView dateView = (TextView) v.findViewById(R.id.textViewDate);
            if (dateView != null) {
                dateView.setText(FormatterHelper.formatDate(obj.insertDate.getTime()));
            }
        }
        return v;
    }

    public void setItems(List<Measurement> dataList) {
        this.items = dataList;
        notifyDataSetChanged();
    }
}
