package io.github.nbhargava.gridimagesearch.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import io.github.nbhargava.gridimagesearch.R;

public class ImageDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        String url = getIntent().getStringExtra("url");
        ImageView imageView = (ImageView) findViewById(R.id.ivDisplayImage);

        Picasso.with(this)
                .load(url)
                .into(imageView);
    }
}
