package com.example.android11mediastorage;

import android.net.Uri;

public class MediaFile {
    public Uri uri;
    public int duration;
    public int size;
    public String name;
    public String  mime;

    public MediaFile() {
    }

    public MediaFile(Uri uri, String name, int duration, int size, String mime) {
        this.uri = uri;
        this.name = name;
        this.duration = duration;
        this.size = size;
        this.mime = mime;
    }
}
