package com.krld.memorize.common;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.krld.memorize.R;
import com.krld.memorize.model.Measurement;

import java.util.List;

public class ListAdapter extends ArrayAdapter<Measurement> {

    public ListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }
    private List<Measurement> items;
    public ListAdapter(Context context, int textViewResourceId, List<Measurement> items) {
        super(context, textViewResourceId, items);
        Log.e("KRLD"," create listAdapter " + items.size());
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.tablerow, null);
        }
        Measurement measurement = items.get(position);
        Log.d("KRLD", measurement.getWeight().toString() + " pos: " + position);

        if (measurement != null) {
            TextView weightView = (TextView) v.findViewById(R.id.weight);
            if (weightView != null) {
                weightView.setText(measurement.getWeight());
            }
        }
        return v;
    }
}
