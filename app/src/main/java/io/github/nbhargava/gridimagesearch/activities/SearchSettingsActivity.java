package io.github.nbhargava.gridimagesearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import io.github.nbhargava.gridimagesearch.R;
import io.github.nbhargava.gridimagesearch.models.SearchSettings;

public class SearchSettingsActivity extends AppCompatActivity {

    private SearchSettings searchSettings;

    private Spinner spImageSize;
    private Spinner spColorFilter;
    private Spinner spImageType;
    private EditText etSiteFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_settings);

        searchSettings = getIntent().getParcelableExtra("settings");

        spImageSize = (Spinner) findViewById(R.id.spImageSize);
        spColorFilter = (Spinner) findViewById(R.id.spColorFilter);
        spImageType = (Spinner) findViewById(R.id.spImageType);
        etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);

        updateViewsFromSettings(searchSettings);
    }

    private void updateViewsFromSettings(SearchSettings searchSettings) {
        setSpinnerToValue(spImageSize, searchSettings.getImageSize());
        setSpinnerToValue(spColorFilter, searchSettings.getColorFilter());
        setSpinnerToValue(spImageType, searchSettings.getImageType());
        etSiteFilter.setText(searchSettings.getSiteFilter());
    }

    public void onSettingsSaved(View view) {
        searchSettings.setImageSize(spImageSize.getSelectedItem().toString());
        searchSettings.setColorFilter(spColorFilter.getSelectedItem().toString());
        searchSettings.setImageType(spImageType.getSelectedItem().toString());
        searchSettings.setSiteFilter(etSiteFilter.getText().toString());

        Intent intent = new Intent();
        intent.putExtra("settings", searchSettings);
        setResult(RESULT_OK, intent);

        finish();
    }

    private void setSpinnerToValue(Spinner spinner, String value) {
        SpinnerAdapter adapter = spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(value)) {
                spinner.setSelection(i);
                return;
            }
        }
    }
}
