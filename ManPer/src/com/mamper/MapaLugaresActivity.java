package com.mamper;

import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class MapaLugaresActivity extends MapActivity {

	public MapView mapa;
	public MapController mapControler;
	public MapaLugaresOverlay puntosmapa;
	public List<Overlay> mapOverlays;
	List<Lugar> lugares;
	DatabaseHandler db;
	int var = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapalugares);
		// PONEMOS EL ACTIONBAR
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setHomeAction(new IntentAction(this, Principal
				.createIntent(this), R.drawable.homeicon));
		actionBar.setTitle(R.string.mapplaces);
		
		accederdb();

		// MAPAS
		if(lugares.isEmpty())
		{
			dialogoerror(getString(R.string.attention), getString(R.string.emptylist));
		}
		mapa = (MapView) findViewById(R.id.mapa2);
		mapa.displayZoomControls(true);
		mapa.setBuiltInZoomControls(true);
		mapa.setSatellite(true);
		mapControler = mapa.getController();
		mapControler.setZoom(12);

		// icono que aparece en el mapa
		mapOverlays = mapa.getOverlays();
		Drawable icono = this.getResources().getDrawable(R.drawable.homeicon);
		// objeto que pasamos al mapa
		puntosmapa = new MapaLugaresOverlay(this, icono);
		//puntosmapa.addLocalizacion(41.669770, -0.812602, "Punto1");
		mapOverlays.add(puntosmapa);
		insertar_mapa();
		//puntosmapa.addLocalizacion(41.669750, -0.812610, "Punto1");
		mapOverlays.add(puntosmapa);

	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void accederdb() {
		db = new DatabaseHandler(this);
		db.getWritableDatabase();
		lugares = db.getAllPlaces();
		db.close();

	}
	//FUNCION QUE INSERTA CADA LUGAR EN EL MAPA
	public void insertar_mapa() {
		
		Iterator<Lugar> it = lugares.iterator();
		while (it.hasNext()) {
			Lugar p = it.next();
			String id = Integer.toString(p.get_id());
			puntosmapa.addLocalizacion(p.getLatitud(),p.getLongitud(), id);
			mapOverlays.add(puntosmapa);
		
			Log.e("ITERATOR", "vALRO  "+ p.get_id());

		}
	}
	//CUADRO ERROR GENERICO
	public void dialogoerror(CharSequence titulo, CharSequence mensaje) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				MapaLugaresActivity.this);
		builder.setMessage(mensaje)
				.setTitle(titulo)
				.setPositiveButton(getString(R.string.ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
							   finish();
							}
						});

		AlertDialog error = builder.create();
		error.show();
	}

}
