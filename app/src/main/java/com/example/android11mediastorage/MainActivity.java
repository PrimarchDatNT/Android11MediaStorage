package com.example.android11mediastorage;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.android11mediastorage.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MAdapter.Callback {

    private static final String TAG = "datnt";

    ActivityMainBinding binding;

    MAdapter mAdapter;

    List<MediaFile> videoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityMainBinding.inflate(this.getLayoutInflater());
        this.setContentView(this.binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 140);
                return;
            }
        }

        this.mAdapter = new MAdapter(this);
        this.mAdapter.setCallback(this);
        this.binding.rvMedia.setAdapter(this.mAdapter);
        this.binding.rvMedia.setLayoutManager(new LinearLayoutManager(this));
        this.init();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 140) {
            this.init();
        }
    }

    private void init() {
        Uri collection;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = null;
        }

        String[] projection = new String[]{MediaStore.Video.Media._ID, MediaStore.Video.Media.DISPLAY_NAME, "duration", MediaStore.Video.Media.SIZE};
        String sortOrder = MediaStore.Video.Media.DISPLAY_NAME + " ASC";

        try (Cursor cursor = getApplicationContext().getContentResolver().query(collection, projection, null, null, sortOrder)) {
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int durationColumn = cursor.getColumnIndexOrThrow("duration");
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                int duration = cursor.getInt(durationColumn);
                int size = cursor.getInt(sizeColumn);

                Uri contentUri = null;
                String mime = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    contentUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL, id);
                    mime = getApplicationContext().getContentResolver().getType(contentUri);
                }

                // Stores column values and the contentUri in a local object
                // that represents the media file.
                this.videoList.add(new MediaFile(contentUri, name, duration, size, mime));
            }
        }

        this.mAdapter.setListItem(this.videoList);

    }

    @Override
    public void onItemClick(int position, MediaFile item) {

    }
}