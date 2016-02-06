package com.bitfarmsoftware.starwars.model;

import android.net.Uri;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * @author Brad Armstrong, Bitfarm Software
 */
public class UriTypeAdapter extends TypeAdapter<Uri> {
    @Override
    public void write(JsonWriter out, Uri uri) throws IOException {
        out.value(uri.toString());
    }

    @Override
    public Uri read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL){
            in.nextNull();
            return null;
        }
        return Uri.parse(in.nextString());
    }
}