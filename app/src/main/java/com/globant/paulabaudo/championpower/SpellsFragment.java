package com.globant.paulabaudo.championpower;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class SpellsFragment extends ListFragment {

    EditText mEditTextChampion;
    Button mButtonSpells;
    ArrayAdapter mAdapter;
    String[] mChampions;
    final static String LOG_TAG = SpellsFragment.class.getSimpleName();

    public SpellsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        wireUpViews(rootView);
        prepareChampionsArray();
        prepareButton();
        return rootView;
    }

    private void prepareChampionsArray() {
        mChampions = getResources().getStringArray(R.array.champions_array);
    }

    private void wireUpViews(View rootView) {
        mEditTextChampion = (EditText) rootView.findViewById(R.id.edit_text_champion);
        mButtonSpells = (Button) rootView.findViewById(R.id.button_get_spells);
    }

    private void prepareButton() {
        mButtonSpells.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String champion = mEditTextChampion.getText().toString();
                displayToast(champion);
                fetchReposInQueue(champion);
            }

            private void displayToast(String champion) {
                String message = String.format(getString(R.string.getting_spells_for_champion),champion);
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prepareListView();
    }

    private void prepareListView() {
        List<String> spells = new ArrayList<>();
        mAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, spells);
        setListAdapter(mAdapter);
    }

    private void fetchReposInQueue(String champion){
        try {
            int championId = getChampionId(champion);
            URL url = constructURLQuery(championId);
            Request request = new Request.Builder().url(url.toString()).build();
            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String responseString = response.body().string();
                    final List<String> listOfSpells = parseResponse(responseString);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.clear();
                            mAdapter.addAll(listOfSpells);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getChampionId(String champion){
        int i = 0;
        while (i < mChampions.length && !champion.toUpperCase().equals(mChampions[i].toUpperCase())){
            i++;
        }
        i++;
        return i;
    }

    private URL constructURLQuery(int championId) throws MalformedURLException {
        final String RIOT_BASE_URL = "global.api.pvp.net";
        final String API_PATH = "api";
        final String LOL_PATH = "lol";
        final String STATIC_PATH = "static-data";
        final String REGION_PATH = "na";
        final String VERSION_PATH = "v1.2";
        final String CHAMPION_PATH = "champion";
        final String CHAMPION_ID_PATH = Integer.toString(championId);
        final String ENDPOINT = "?champData=spells";
        final String API_KEY = "&api_key=e1452383-1e5a-4842-a15d-f89568f612af";
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https").authority(RIOT_BASE_URL).
                appendPath(API_PATH).
                appendPath(LOL_PATH).
                appendPath(STATIC_PATH).
                appendPath(REGION_PATH).
                appendPath(VERSION_PATH).
                appendPath(CHAMPION_PATH).
                appendPath(CHAMPION_ID_PATH + ENDPOINT + API_KEY);
        Uri uri = builder.build();
        String uriString = uri.toString().replace("%3F","?");
        uriString = uriString.replace("%3D","=");
        uriString = uriString.replace("%26","&");
        Log.d(LOG_TAG, "Built URI: " + uriString);
        return new URL(uriString);
    }

    private List<String> parseResponse(String response){
        final String SPELLS = "spells";
        final String SPELL_DESCRIPTION = "description";
        final String SPELL_NAME = "name";
        List<String> spells = new ArrayList<>();
        String spell;
        try {
            JSONObject responseObject = new JSONObject(response);
            JSONArray spellsArray = responseObject.getJSONArray(SPELLS);
            JSONObject spellObject;

            for (int i = 0; i < spellsArray.length(); i++){
                spellObject = spellsArray.getJSONObject(i);
                spell = spellObject.getString(SPELL_NAME) + ": " +
                        spellObject.getString(SPELL_DESCRIPTION);
                spells.add(spell);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return spells;
    }

}
