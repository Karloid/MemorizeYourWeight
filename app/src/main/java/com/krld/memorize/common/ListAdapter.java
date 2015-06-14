package com.krld.memorize.common;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.krld.memorize.EditorActivity;
import com.krld.memorize.R;
import com.krld.memorize.model.MeasurementLegacy;

import java.text.SimpleDateFormat;
import java.util.List;

public class ListAdapter extends ArrayAdapter<MeasurementLegacy> {

    public ListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    private List<MeasurementLegacy> items;

    public ListAdapter(Context context, int textViewResourceId, List<MeasurementLegacy> items) {
        super(context, textViewResourceId, items);
        Log.d("KRLD", " create listAdapter " + items.size());
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.li_table, null);
        }
        MeasurementLegacy measurementLegacy = items.get(position);
      //  Log.d("KRLD", measurementLegacy.getWeight().toString() + " pos: " + position);

        if (measurementLegacy != null) {
            TextView weightView = (TextView) v.findViewById(R.id.textViewWeight);
            if (weightView != null) {
                weightView.setText(measurementLegacy.getWeight());
            }
            TextView dateView = (TextView) v.findViewById(R.id.textViewDate);
            if (dateView != null) {
                dateView.setText(new SimpleDateFormat("dd.MM.yyyy H:mm").format(measurementLegacy.getDate()));
            }
            Button delButton = (Button) v.findViewById(R.id.deleteButton);
            delButton.setTag(measurementLegacy.getId());
            delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DbService.removeMeasurement((Integer) view.getTag());
                    ((EditorActivity) ListAdapter.this.getContext()).refreshListViewMeasurement();
                }
            });
        }
        return v;
    }
}
