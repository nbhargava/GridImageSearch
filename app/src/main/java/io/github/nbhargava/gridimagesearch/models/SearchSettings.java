package io.github.nbhargava.gridimagesearch.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by nikhil on 9/24/15.
 */
public class SearchSettings implements Parcelable {
    public static final int PAGE_SIZE = 8;

    private String colorFilter;
    private String imageSize;
    private String imageType;
    private String siteFilter;

    public void setColorFilter(String colorFilter) {
        this.colorFilter = colorFilter;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public void setSiteFilter(String siteFilter) {
        this.siteFilter = siteFilter;
    }

    public String getColorFilter() {
        if (!valueExists(colorFilter)) {
            colorFilter = "any";
        }

        return colorFilter;
    }

    public String getImageSize() {
        if (!valueExists(imageSize)) {
            imageSize = "any";
        }

        return imageSize;
    }

    public String getImageType() {
        if (!valueExists(imageType)) {
            imageType = "any";
        }

        return imageType;
    }

    public String getSiteFilter() {
        if (siteFilter == null) {
            siteFilter = "";
        }

        return siteFilter;
    }


    public String getAPIEndpointForQuery(String query, int page) {
        String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&rsz=" + PAGE_SIZE;
        url += "&start=" + (PAGE_SIZE * page);

        if (valueExists(colorFilter)) {
            url += "&imgc=" + colorFilter;
        }
        if (valueExists(imageSize)) {
            url += "&imgsz=" + imageSize;
        }
        if (valueExists(imageType)) {
            url += "&as_filetype=" + imageType;
        }
        if (siteFilter != null && !siteFilter.equals("")) {
            String escapedSiteFilter;
            try {
                escapedSiteFilter = URLEncoder.encode(siteFilter, "utf-8");
                url += "&as_sitesearch=" + escapedSiteFilter;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return url;
    }

    private boolean valueExists(String value) {
        return value != null && !value.equals("") && !value.equals("any");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.colorFilter);
        dest.writeString(this.imageSize);
        dest.writeString(this.imageType);
        dest.writeString(this.siteFilter);
    }

    public SearchSettings() {
    }

    private SearchSettings(Parcel in) {
        this.colorFilter = in.readString();
        this.imageSize = in.readString();
        this.imageType = in.readString();
        this.siteFilter = in.readString();
    }

    public static final Parcelable.Creator<SearchSettings> CREATOR = new Parcelable.Creator<SearchSettings>() {
        public SearchSettings createFromParcel(Parcel source) {
            return new SearchSettings(source);
        }

        public SearchSettings[] newArray(int size) {
            return new SearchSettings[size];
        }
    };
}
