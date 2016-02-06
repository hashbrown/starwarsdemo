package com.bitfarmsoftware.starwars.model;

import android.net.Uri;

import java.util.List;

/**
 * @author Brad Armstrong, Bitfarm Software
 */
public class PagedApiResponse<T> {
    public int count;
    public Uri next;
    public Uri previous;
    public List<T> results;

    public int getCount() {
        return count;
    }

    public Uri getNext() {
        return next;
    }

    public Uri getPrevious() {
        return previous;
    }

    public List<T> getResults() {
        return results;
    }
}
