package com.mamper;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
//CLASE PARA PERSONALIZAR EL ASPECTO DEL MAPALUGARESACTIVITY


public class MapaLugaresOverlay extends ItemizedOverlay<OverlayItem> {
	
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context context;
	
	
	
	public MapaLugaresOverlay(Context context, Drawable defaultMarker)
	{
		super(boundCenterBottom(defaultMarker));
		this.context = context;
	}
	
	public void addLocalizacion(double lat, double lon, String etiqueta)
	{
		int lt = (int) (lat * 1E6);
		int ln = (int) (lon * 1E6);
		
		GeoPoint punto = new GeoPoint(lt, ln);
		OverlayItem item = new OverlayItem(punto, etiqueta, null);
		mOverlays.add(item);
		populate(); ///?????
	}
	public void clear()
	{
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
	protected boolean onTap(int index)
	{
		String etiqueta = mOverlays.get(index).getTitle();
		 Intent intent = new Intent(context, MostrarLugarActivity.class);
		 intent.putExtra("id", etiqueta);
	     context.startActivity(intent);
		
		Toast.makeText(context, etiqueta, Toast.LENGTH_LONG).show();
		return true;
	}

}
