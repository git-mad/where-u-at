package gitmad.app.WhereUAt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import gitmad.app.WhereUAt.model.CountryEntry;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
                    new postCountryTask().execute();

                    new getResultsTask().execute();
                }
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new getResultsTask().execute();
            }
        });
    }

    private class getResultsTask extends AsyncTask<String, Void, List<CountryEntry>> {

        @Override
        protected List<CountryEntry> doInBackground(String... params) {
            JSONArray json =  getCountries();
            List<CountryEntry> countries = convertJSONToList(json);

            //sort list by number of votes
            Collections.sort(countries, new Comparator<CountryEntry>() {
                @Override
                public int compare(CountryEntry lhs, CountryEntry rhs) {
                    return lhs.getCount() - rhs.getCount();
                }
            });
            return countries;
        }

        protected void onPostExecute(List<CountryEntry> result) {
            countryList = result;
            countryEntryAdapter.clear();
            countryEntryAdapter.addAll(countryList);
            countryEntryAdapter.notifyDataSetChanged();
        }
    }

    private class postCountryTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            postCountry();
            return null;
        }
    }

    public void postCountry() {
        URL url = null;
        try {
            url = new URL("http://mad-countries.herokuapp.com/countries/increment_country");
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
//        String url = "http://mad-countries.herokuapp.com/countries/increment_country";
        String country = editText.getText().toString();
        HttpURLConnection connection = null;

        try {
            String leaderboardEntry = new String("{country: {name: \"" + country + "\""+ "}}");
            connection = (HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.getOutputStream().write(leaderboardEntry.getBytes());
            connection.connect();
            connection.getResponseCode();
//            Log.d("resp code", String.valueOf(connection.getResponseCode()));
//            JSONObject temp = new JSONObject();
//            temp.put("name", country);
//            JSONObject temp2 = new JSONObject();
//            temp2.put("country", temp);
//
//            StringEntity se = new StringEntity(temp2.toString());
//
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httppost = new HttpPost(url);
//            httppost.setHeader("Accept", "application/json");
//            httppost.setHeader("Content-type", "application/json");
//
//            httppost.setEntity(se);
//            HttpResponse response = httpclient.execute(httppost);
        } catch (IOException e) {
        } finally {
            if(connection != null)
                connection.disconnect();
        }

    }

    public JSONArray getCountries() {
        String url = "http://mad-countries.herokuapp.com/countries.json";
        InputStream is = null;
        String json = "";
        JSONArray jObj = null;

        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONArray(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }

    private List<CountryEntry> convertJSONToList(JSONArray json) {
        List<CountryEntry> countries = new ArrayList<CountryEntry>();
        for (int ii = 0; ii < json.length(); ii++) {
            CountryEntry countryEntry = null;
            try {
                JSONObject temp = json.getJSONObject(ii);
                String country = temp.getString("name");
                int count = temp.getInt("count");
                countryEntry = new CountryEntry(country, count);
                countries.add(countryEntry);
            } catch (JSONException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
        return countries;
    }
}
