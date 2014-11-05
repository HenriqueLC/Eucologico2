package com.b310.grupo10.eucologico2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MapsActivity extends FragmentActivity  implements LocationListener{
    public static MapsActivity instance;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static String DEFAULT_KML = "pontos.kml";
    private List<Place> map_places;

    // Default location is centered at UNICAMP
    private static final int DEF_ZOOM = 15;
    private static final double DEF_LAT = (-22.817104);
    private static final double DEF_LON = (-47.069731);

    // This SP stores the preferences from the app, like
    // default location...
    SharedPreferences sp;
    SharedPreferences.Editor spe;

    // places
    BulletGroup places;
    ItemizedLocation gps_pos;
    LatLng myPos = null;

    // GPS stuff
    Place user;
    Drawable user_e;
    LocationManager lm;
    Location location;
    String bestProvider;

    Place item = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {


        mMap.setInfoWindowAdapter(new InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                View v = getLayoutInflater().inflate(R.layout.marker, null);

                TextView tittle = (TextView) v.findViewById(R.id.markertittle);

                TextView info= (TextView) v.findViewById(R.id.info);

                tittle.setText(marker.getTitle());
                info.setText(marker.getSnippet());

                return v;
            }

        });
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        // set gps stuff with bestprovider available
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        bestProvider = lm.getBestProvider(criteria, true);
        lm.requestLocationUpdates(bestProvider, 0, 0, this);

        // display the default position on the map
        //map.getController()
        //		.setCenter(
        //				new LatLng(Integer.valueOf(DEF_LAT), Integer
        //						.valueOf(DEF_LON)));
        //map.getController().setZoom(DEF_ZOOM);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(DEF_LAT, DEF_LON)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(DEF_ZOOM), 2000, null);

        // parse the KML file
        ParseKML();
    }

    private void ParseKML() {

        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser spm = spf.newSAXParser();
            XMLReader xr = spm.getXMLReader();

            InputStream iS = getAssets().open(DEFAULT_KML);
            KMLParser kml = new KMLParser();

            xr.setContentHandler(kml);
            xr.parse(new InputSource(iS));

            // using OverlayItem class reduces a lot
            // the maps lag.

            //eee.setBounds(0, 0, eee.getIntrinsicWidth(), eee.getIntrinsicHeight());
            //Drawable eeefav = getResources().getDrawable(R.drawable.pinfav);
            //eeefav.setBounds(0, 0, eeefav.getIntrinsicWidth(), eeefav.getIntrinsicHeight());
            places = new BulletGroup(mMap);
            map_places = kml.getData();
            for (Place gp : map_places) {
                //PlaceOverlayItem ooooi = new PlaceOverlayItem(gp);
                if (gp.IsBookmarked(getApplicationContext())) gp.SetIcon(BitmapDescriptorFactory.fromResource(R.drawable.pinfav));
                else gp.SetIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
                places.AddOverlay(gp);

            }

            //aplica filtro
            Update();
            //places.PopulateNow();
            //map.getOverlays().add(gps_pos);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Location GetCurrentGPSLocation() {
        Location location = lm.getLastKnownLocation(bestProvider);

        return location;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.map_menu, menu);

        return result;
    }

    public void Update() {
        SharedPreferences spp = PreferenceManager.getDefaultSharedPreferences(this);

        BulletGroup bg = new BulletGroup(mMap);
        for (Place poi : places.AplicarFiltro(spp.getInt("FILTRO", 0))) {
            bg.AddOverlay(poi);
        }
        mMap.clear();
        //bg.clear();
        bg.PopulateNow();

        // currently location found by the GPS service
        Location loc = GetCurrentGPSLocation();
        gps_pos = new ItemizedLocation<Place>(mMap);
        if (loc != null) {
            user = new Place(new LatLng(
                    (loc.getLatitude()),
                    (loc.getLongitude())), "Your Location", "");

            //user.setMarker(user_e);
            user.SetIcon(BitmapDescriptorFactory.fromResource(R.drawable.pt));
            gps_pos.AddOverlay(user);
        }
        gps_pos.PopulateNow();
        //map.getOverlays().add(bg);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        Intent i;

        switch(item.getItemId()) {
            case R.id.map_about:
                i = new Intent(this, About.class);
                startActivity(i);
                break;

            case R.id.map_fil:
                i = new Intent(this, Filtros.class);
                startActivity(i);

                break;

            case R.id.map_defp:
                Location loc = GetCurrentGPSLocation();
                gps_pos = new ItemizedLocation<Place>(mMap);
                if (loc != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(loc.getLatitude(), loc.getLongitude())));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(DEF_ZOOM), 2000, null);
                }
                break;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onLocationChanged(Location location) {

		// TODO Auto-generated method stub
		this.location = location;

		//updates current pos
		if (location != null) {
			//map.getOverlays().remove(user);
			user = new Place(new LatLng((int)(location.getLatitude())
					, (int)(location.getLongitude())), "", "");
			gps_pos.Clear();
			gps_pos.AddOverlay(user);
			gps_pos.PopulateNow();
			//map.getOverlays().add(gps_pos);
			//map.invalidate();
		}
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
}
