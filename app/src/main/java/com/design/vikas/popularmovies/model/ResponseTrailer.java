package com.design.vikas.popularmovies.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vikas kumar on 7/29/2016.
 */
public class ResponseTrailer {
    private Integer id;
    private List<ResponseTrailersDatum> results = new ArrayList<ResponseTrailersDatum>();

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The results
     */
    public List<ResponseTrailersDatum> getResults() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setResults(List<ResponseTrailersDatum> results) {
        this.results = results;
    }

}
