package com.winterfull.asr.domain;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * @author : ytxu5
 * @date: 2023/3/28
 */
public class Lattices {

    private String traceId;

    private List<Lattice> lattices;

    public Lattices(String traceId){
        this.traceId = traceId;
        this.lattices = new LinkedList<>();
    }

    public void add(String response){
        AsrResult result = JSONObject.parseObject(response, AsrResult.class);
        JSONObject content = result.getContent();
        String orderResult = content.getString("orderResult");
        String lattice1 = JSONObject.parse(orderResult).getString("lattice");
        JSONArray array = JSONArray.parseArray(lattice1);
        for (Object o : array) {
            Lattice lattice = JSONObject.parseObject(JSONObject.toJSONString(o), Lattice.class);
            lattice = lattice.convert();
            this.add(lattice);
        }
    }

    public void add(Lattice lattice){
        this.lattices.add(lattice);
    }


}
