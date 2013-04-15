package gitmad.app.WhereUAt;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import gitmad.app.WhereUAt.model.CountryEntry;

public class LeaderboardFragment extends Fragment {

    private List<CountryEntry> countryList;
    private Button addButton, refreshButton;
    private EditText editText;
    private ListView leaderboardList;
    private CountryEntryAdapter countryEntryAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        setupFragment(view);
        return view;
    }

    private void setupFragment(View view) {
        editText = (EditText) view.findViewById(R.id.leaderboard_input);
        addButton = (Button) view.findViewById(R.id.leaderboard_add);
        refreshButton = (Button) view.findViewById(R.id.leaderboard_refresh);
        leaderboardList = (ListView) view.findViewById(R.id.leaderboard);

        countryList = new ArrayList<CountryEntry>();

        countryEntryAdapter = new CountryEntryAdapter(getActivity(), R.layout.leaderboard_item, countryList);
        leaderboardList.setAdapter(countryEntryAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typed = editText.getText().toString();

                if (typed.length() > 0) {
                    CountryEntry entry = new CountryEntry(typed, 100);
                    countryList.add(entry);
                    editText.setText("");
                    countryEntryAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void postCountry() {

    }

    public List<CountryEntry> getCountries() {
        return null;
    }
}
