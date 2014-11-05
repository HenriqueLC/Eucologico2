package com.b310.grupo10.eucologico2;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.List;

public class BulletGroup extends ItemizedLocation<Place> {
	
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
/*
	@Override
	protected final boolean onTap(int arg0) {
		// TODO Auto-generated method stub
		super.onTap(arg0);
		final PlaceOverlayItem item = getItem(arg0);
		
		AlertDialog alertDialog = new AlertDialog.Builder(mV.getContext()).create();
		alertDialog.setTitle(item.getTitle());
		
		if (item.GetPlace().IsBookmarked(mV.getContext())) {
			alertDialog.setButton("Remover dos Favoritos", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					item.GetPlace().RemoveBookmark(mV.getContext());
					Drawable eee = mV.getResources().getDrawable(R.drawable.pin);
					eee.setBounds(0, 0, eee.getIntrinsicWidth(), eee.getIntrinsicHeight());
					item.setMarker(eee);
					mV.invalidate();
				}
			});
		} else {
			alertDialog.setButton("Adicionar aos Favoritos",
					new DialogInterface.OnClickListener() {
	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					item.GetPlace().Bookmark(mV.getContext());
					Drawable eee = mV.getResources().getDrawable(R.drawable.pinfav);
					eee.setBounds(0, 0, eee.getIntrinsicWidth(), eee.getIntrinsicHeight());
					item.setMarker(eee);
					mV.invalidate();
				}
			});
		}
		
		alertDialog.setMessage(item.getSnippet());
		alertDialog.show();
		
		return true;
	}
	*/
}