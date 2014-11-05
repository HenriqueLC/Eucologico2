package com.b310.grupo10.eucologico2;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.List;
//import com.google.android.maps.MapView;
//import com.google.android.maps.OverlayItem;

public class ItemizedLocation<T extends Place> {
	List<T> places = new ArrayList<T>();
	GoogleMap mV;
	
	public ItemizedLocation(GoogleMap mapView) {
		mV = mapView;
	}
	
	public void Clear() { 
		places.clear();
		PopulateNow();
	}
	
	public void RemoveAt(int index) {
		places.remove(index);
		PopulateNow();
	}
	
	public void AddOverlay(T oi) {
		places.add(oi);
	}
	
	public void PopulateNow() {
        for (T place:places) {
            place.Populate(mV);
        }
		//populate();
		//setLastFocusedIndex(-1);
	}

    public T getItem(int id){
        return places.get(id);
    }

	protected T createItem(int i) {
		// TODO Auto-generated method stub
		 return places.get(i);
	}

	public int size() {
		// TODO Auto-generated method stub
		return places.size();
	}
}