package io.github.nbhargava.gridimagesearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import io.github.nbhargava.gridimagesearch.R;
import io.github.nbhargava.gridimagesearch.adapters.EndlessScrollListener;
import io.github.nbhargava.gridimagesearch.adapters.ImageResultsAdapter;
import io.github.nbhargava.gridimagesearch.models.ImageResult;
import io.github.nbhargava.gridimagesearch.models.SearchSettings;

public class SearchActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SETTINGS = 5000;
    private GridView gvResults;

    private SearchSettings searchSettings;

    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;

    private String activeQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setupViews();
        imageResults = new ArrayList<>();
        aImageResults = new ImageResultsAdapter(this, imageResults);

        searchSettings = new SearchSettings();

        gvResults.setAdapter(aImageResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                activeQuery = query;

                aImageResults.clear();
                executeGoogleImageSearch(activeQuery, 0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.miSettings) {
            Intent searchIntent = new Intent(this, SearchSettingsActivity.class);
            searchIntent.putExtra("settings", searchSettings);
            startActivityForResult(searchIntent, REQUEST_CODE_SETTINGS);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SETTINGS && resultCode == RESULT_OK) {
            searchSettings = data.getParcelableExtra("settings");
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setupViews() {
        gvResults = (GridView) findViewById(R.id.gvResults);

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ImageResult result = imageResults.get(i);

                // Launch the image display activity
                Intent intent = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                intent.putExtra("url", result.fullUrl);
                startActivity(intent);
            }
        });

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemCount) {
                executeGoogleImageSearch(activeQuery, totalItemCount / SearchSettings.PAGE_SIZE);
            }
        });
    }

    private void executeGoogleImageSearch(String query, int page) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = searchSettings.getAPIEndpointForQuery(query, page);

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());

                JSONArray imageResultsJson;
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    aImageResults.addAll(ImageResult.fromJsonArray(imageResultsJson));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
