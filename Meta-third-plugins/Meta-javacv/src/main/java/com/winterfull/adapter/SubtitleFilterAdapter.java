package com.winterfull.adapter;

import com.winterfull.asr.domain.Lattices;
import com.winterfull.filter.SubtitleFilter;

/**
 * @author : ytxu5
 * @date: 2023/3/29
 */
public class SubtitleFilterAdapter implements AutoCloseable{

    private Lattices lattices;

    private SubtitleFilter subtitleFilter;

    public SubtitleFilterAdapter(Lattices lattices){
        this.lattices = lattices;
    }


    @Override
    public void close() throws Exception {

    }
}
