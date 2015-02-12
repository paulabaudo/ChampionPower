package com.globant.paulabaudo.championpower;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class SpellsFragment extends ListFragment {

    EditText mEditTextChampion;
    Button mButtonSpells;

    public SpellsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        wireUpViews(rootView);
        prepareButton();
        return rootView;
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
//                fetchReposInQueue(champion);
            }

            private void displayToast(String champion) {
                String message = String.format(getString(R.string.getting_spells_for_champion),champion);
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
