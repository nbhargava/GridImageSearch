package io.github.nbhargava.gridimagesearch.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import io.github.nbhargava.gridimagesearch.R;
import io.github.nbhargava.gridimagesearch.models.ImageResult;

/**
 * Created by nikhil on 9/23/15.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {
    public ImageResultsAdapter(Context context, List<ImageResult> images) {
        super(context, R.layout.item_image_result, images);
    }

    private static class ImageResultViewHolder {
        ImageView ivImage;
        TextView tvImage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResult imageInfo = getItem(position);
        ImageResultViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);

            viewHolder = new ImageResultViewHolder();
            viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
            viewHolder.tvImage = (TextView) convertView.findViewById(R.id.tvTitle);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ImageResultViewHolder) convertView.getTag();
        }

        viewHolder.tvImage.setText(Html.fromHtml(imageInfo.title));

        // Clear out image before loading it in
        viewHolder.ivImage.setImageResource(0);
        Picasso.with(getContext())
                .load(imageInfo.thumbUrl)
                .into(viewHolder.ivImage);

        return convertView;
    }
}
