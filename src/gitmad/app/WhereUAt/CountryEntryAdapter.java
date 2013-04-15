package gitmad.app.WhereUAt;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import gitmad.app.WhereUAt.model.CountryEntry;

public class CountryEntryAdapter extends ArrayAdapter<CountryEntry> {

    private List<CountryEntry> countries;
    int textViewResourceId;
    Context context;

    public CountryEntryAdapter(Context context, int textViewResourceId, List<CountryEntry> objects) {
        super(context, textViewResourceId, objects);
        this.countries = objects;
        this.context = context;
        this.textViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(textViewResourceId, parent, false);
        }

        CountryEntry entry = countries.get(position);

        if (entry != null) {
            TextView country = (TextView) view.findViewById(R.id.countryTitle);
            TextView count = (TextView) view.findViewById(R.id.countryCount);

            country.setText("Country: " + entry.getName());
            count.setText("Score: " + entry.getCount());
        }

        return view;
    }
}
