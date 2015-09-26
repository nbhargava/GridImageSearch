package io.github.nbhargava.gridimagesearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nikhil on 9/23/15.
 */
public class ImageResult {
    public String fullUrl;
    public String thumbUrl;
    public String title;

    public ImageResult(JSONObject imageObject) {
        try {
            this.fullUrl = imageObject.getString("url");
            this.thumbUrl = imageObject.getString("tbUrl");
            this.title = imageObject.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<ImageResult> fromJsonArray(JSONArray array) {
        ArrayList<ImageResult> results = new ArrayList<ImageResult>();

        for (int i = 0; i < array.length(); i++) {
            try {
                results.add(new ImageResult(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }
}
