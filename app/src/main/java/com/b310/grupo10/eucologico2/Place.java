package com.b310.grupo10.eucologico2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//import com.google.android.maps.GeoPoint;
//import com.google.android.maps.OverlayItem;

public class Place {
	private String descr;
	private String index;
	private int rec[];
	private LatLng gp;
	private int id;
	
	public Place() {
		descr = null;
		index = null;
		gp = null;
		id = 0;
		rec = null;
	}

    public Place(LatLng gp, String index, String descr) {
        this.descr = descr;
        this.index = index;
        this.gp = gp;
        id = 0;
        rec = null;
    }

	public boolean Recycles(int type) {
		for (int i : rec) {
			if (i == type)
				return true;
		}
		
		return false;
	}
	
	public void SetRecycleList(int list[]) {
		rec = list;
	}
	
	public int[] GetRecycleList() {
		return rec;
	}
	
	public void SetID(int id) {
		this.id = id;
	}
	
	public void SetDescription(String descr) {
		this.descr = descr;
	}
	
	public void SetIndex(String index) {
		this.index = index;
	}
	
	public void SetGeoPoint(LatLng gp) { this.gp = gp; }
	
	public String GetDescription() {
		return descr;
	}
	
	public int GetID() {
		return id;
	}
	
	public String GetIndex() {
		return index;
	}
	
	public LatLng GetGeoPoint() {
		return gp;
	}
	
	public boolean IsBookmarked(Context c) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
		
		return sp.getBoolean("PLACE " + String.valueOf(id), false);
	}
	
	public void Bookmark(Context c) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
		SharedPreferences.Editor spe = sp.edit();
		
		spe.putBoolean("PLACE " + String.valueOf(id), true);
		spe.commit();
	}
	
	public void RemoveBookmark(Context c) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
		SharedPreferences.Editor spe = sp.edit();
		
		spe.putBoolean("PLACE " + String.valueOf(id), false);
		spe.commit();
	}

    public void Populate(GoogleMap map){
        map.addMarker(new MarkerOptions().position(gp).title(index).snippet(descr));

    }
}
