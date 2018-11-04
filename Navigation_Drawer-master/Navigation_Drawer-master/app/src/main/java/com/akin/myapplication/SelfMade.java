package com.akin.myapplication;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static com.akin.myapplication.Downloads.saveToSdCard;
import static com.akin.myapplication.Share.shareToExternal;
import static java.lang.Integer.parseInt;

/**
 * Created by Mungfali on 01-11-2018.
 */

public class SelfMade extends AppCompatActivity {

    ImageView imageView;
    private RecyclerView recyclerView;
    private FavoriteDbHelper favoriteDbHelper;
    private final AppCompatActivity activity = SelfMade.this;
    public String quote_id;
    public static int count=0;
    public Bitmap image;
    public byte []bytes;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selfmade);
        Fragment1 f =new Fragment1();
        imageView = (ImageView) findViewById(R.id.image);
        Intent intent = getIntent();
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        imageView.setImageBitmap(bmp);
        //int size = image.getRowBytes() * image.getHeight();
        //ByteBuffer byteBuffer = ByteBuffer.allocate(size);
        //image.copyPixelsToBuffer(byteBuffer);
        //bytes = byteBuffer.array();
        //imageView.setImageBitmap(image);
        //BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        //image = drawable.getBitmap();
        //image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();//imageView.getDrawingCache();
        //ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //image.compress(Bitmap.CompressFormat.PNG, 0, stream);
        //bytes=stream.toByteArray();

        quote_id = count+"s";
        count++;

        MaterialFavoriteButton materialFavoriteButtonNice = findViewById(R.id.favorite_button);

        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        materialFavoriteButtonNice.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener(){
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite){
                        if (favorite){
                            SharedPreferences.Editor editor = getSharedPreferences("com.delaroystudios.movieapp.DetailActivity", MODE_PRIVATE).edit();
                            editor.putBoolean("Favorite Added", true);
                            editor.apply();
                            saveFavorite();
                            //new MoviesAdapter(buttonView);
                            Snackbar.make(buttonView, "Added to Favorite",
                                    Snackbar.LENGTH_SHORT).show();
                        }else{
                            SharedPreferences.Editor editor = getSharedPreferences("com.delaroystudios.movieapp.DetailActivity", MODE_PRIVATE).edit();
                            editor.putBoolean("Favorite Removed", true);
                            editor.apply();
                            removeFavorite();
                            //new MoviesAdapter(buttonView);
                            Snackbar.make(buttonView, "Removed from Favorite",
                                    Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        final ImageButton buttonDownload = (ImageButton) findViewById(R.id.download_button);

        buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.buildDrawingCache();
                Bitmap bmap = imageView.getDrawingCache();
                String t= saveToSdCard(bmap,"XYZ");
                if(t.equals("success"))
                    Snackbar.make(buttonDownload, "Quote Downloaded",
                            Snackbar.LENGTH_SHORT).show();
            }
        });

        final ImageButton buttonShare = (ImageButton) findViewById(R.id.share_button);

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.buildDrawingCache();
                Bitmap bmap = imageView.getDrawingCache();
                String t= shareToExternal(bmap);
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("image/jpeg");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(Environment.getExternalStorageDirectory()+"/temp.jpg"));
                startActivity(Intent.createChooser(sharingIntent, "Share Image"));
            }
        });
    }

    public void saveFavorite(){
        favoriteDbHelper = new FavoriteDbHelper(activity);
        favoriteDbHelper.addFavorite(this);
    }

    public void removeFavorite(){
        favoriteDbHelper = new FavoriteDbHelper(activity);
        favoriteDbHelper.deleteFavorite(quote_id);
    }
}
