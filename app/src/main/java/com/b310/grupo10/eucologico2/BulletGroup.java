package com.b310.grupo10.eucologico2;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BulletGroup extends ItemizedLocation<Place> implements Iterable<Place> {
	
	public BulletGroup(GoogleMap map) {
		super(map);
	}

	public List<Place> AplicarFiltro(int filtro) {
		List<Place> subset = new ArrayList<Place>();
		
		//nenhum filtro
		if (filtro == 0) {
			for (Place poi: places) {
				subset.add(poi);
			}
			return subset;
		}
		
		for (Place poi: places) {
			for (int tipo : poi.GetRecycleList()) {
				if (tipo == filtro-1) {
					subset.add(poi);
					break;
				}
			}
		}
		
		return subset;
	}


    @Override
    public Iterator<Place> iterator() {
        return places.iterator();
    }
}