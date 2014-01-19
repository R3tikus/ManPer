package com.mamper;

import java.util.ArrayList;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

//CLASE PARA PERSONALIZAR EL MAPA DE EDITARLUGARACTIVY

public class MiItemizedOverlay extends ItemizedOverlay<OverlayItem> {
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context context;

	//ULTIMA POSICION
	private double latitud, longitud;

	public MiItemizedOverlay(Context context, Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		this.context = context;
	}

	public void addLocalizacion(double lat, double lon, String etiqueta) {
		int lt = (int) (lat * 1E6);
		int ln = (int) (lon * 1E6);

		GeoPoint punto = new GeoPoint(lt, ln);
		// latitude
		latitud = punto.getLatitudeE6() / 1E6;
		// longitude
		longitud = punto.getLongitudeE6() / 1E6;
		OverlayItem item = new OverlayItem(punto, etiqueta, null);
		mOverlays.add(item);
		populate(); 
	}

	public void clear() {
		mOverlays.clear();
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	@Override
	protected boolean onTap(int index) {
		String etiqueta = mOverlays.get(index).getTitle();
		Toast.makeText(context, etiqueta, Toast.LENGTH_LONG).show();
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {

		if (event.getAction() == 1) {
			mOverlays.clear();
			populate();
			GeoPoint geopoint = mapView.getProjection().fromPixels(
					(int) event.getX(), (int) event.getY());
			OverlayItem item = new OverlayItem(geopoint, "nuevopunto", null);
			
			latitud = geopoint.getLatitudeE6() / 1E6;
		
			longitud = geopoint.getLongitudeE6() / 1E6;
			mOverlays.add(item);
			populate(); 
			return true;
		}
		
	
		return super.onTouchEvent(event, mapView);
	}

	public double getLatitud() {
		return latitud;
	}

	public double getlongitud() {
		return longitud;
	}

}
