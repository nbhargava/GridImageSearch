package io.github.nbhargava.gridimagesearch.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.github.nbhargava.gridimagesearch.R;

public class ImageDisplayActivity extends AppCompatActivity {

    private String url = "";
    private ImageView ivDisplayImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        url = getIntent().getStringExtra("url");
        ivDisplayImage = (ImageView) findViewById(R.id.ivDisplayImage);

        Picasso.with(this)
                .load(url)
                .into(ivDisplayImage);
    }

    public void shareImage(View view) {
        Uri bmpUri = getLocalBitmapUri(ivDisplayImage);

        if (bmpUri != null) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            sharingIntent.setType("image/*");
            startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_image_prompt)));
        } else {
            // error handling, etc.
        }
    }

    @Nullable
    private Uri getLocalBitmapUri(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp;

        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) drawable).getBitmap();
        } else {
            return null;
        }

        // Write the file locally
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();

            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bmpUri;
    }
}
