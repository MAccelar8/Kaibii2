package com.milan.brtshelper;

import android.content.res.AssetManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BusRecyclerModel {


    private String title;
    private String srcstation;
    private String deststation;
    public static Bus buses1[] = Bus.getBusesData();
    public String getSrcstation() {
        return srcstation;
    }

    public String getDeststation() {
        return deststation;
    }

    public void setDeststation(String deststation) {
        this.deststation = deststation;
    }

    public void setSrcstation(String srcstation) {
        this.srcstation = srcstation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }




    public static List<BusRecyclerModel> busdatalist = new ArrayList<>();


     public static List<BusRecyclerModel> getRecyclerBusList(){

         List<BusRecyclerModel> buslist = new ArrayList<>();

         for (Bus b : buses1) {
             BusRecyclerModel bm = new BusRecyclerModel();
             bm.title = b.busNumber;
             bm.deststation = b.destination;
             bm.srcstation = b.source;
             buslist.add(bm);
             Log.e("hhhhhhh",b.busNumber + " " + b.source + " " + b.destination);
         }

         return buslist;
     }




}
