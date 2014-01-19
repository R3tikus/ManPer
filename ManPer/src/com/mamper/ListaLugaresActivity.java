package com.mamper;

import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class ListaLugaresActivity extends Activity {

	DatabaseHandler db;
	private ProgressDialog pDialog;
	ListView lista;
	public List<Lugar> lug;
	public ListAdapter customAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// seleccionamos el layout correspiente
		setContentView(R.layout.listalugares);
		// PONEMOS EL ACTIONBAR
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setHomeAction(new IntentAction(this, Principal
				.createIntent(this), R.drawable.homeicon));
		actionBar.setTitle(R.string.listplaces);
		//REFERENCIAMOS LA LISTA
		lista = (ListView) findViewById(R.id.listplaces);
		db = new DatabaseHandler(this);

		new CargarTodosLosLugares().execute();
	}

	class CargarTodosLosLugares extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ListaLugaresActivity.this);
			pDialog.setMessage(getString(R.string.loadplaces));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			db.getWritableDatabase();
			lug = db.getAllPlaces();
			db.close();

			return null;
		}

		protected void onPostExecute(String file_url) {

			runOnUiThread(new Runnable() {
				public void run() {
					//SI NO HAY NINGUN LUGAR
					if( lug.isEmpty())
					{
						dialogoerror(getString(R.string.attention), getString(R.string.emptylist));
					}
					customAdapter = new ListAdapter(ListaLugaresActivity.this,
							R.layout.single_row, lug);
					lista.setAdapter(customAdapter);
					pDialog.dismiss();
				}
			});

			lista.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
					

					//FUNCION CUANDO PULSE UN LUGAR
					//LANZA UNA ACTIVITY
					 Intent intent = new Intent(ListaLugaresActivity.this, MostrarLugarActivity.class);
					 position = position+1;
				
					 String _id=null;
					 Iterator<Lugar> it = lug.iterator();
					 for(int i=0; i<position;i++) {
							Lugar p = it.next();
							_id = Integer.toString(p.get_id());
						}
					// String _id = String.valueOf(position);
					intent.putExtra("id", _id);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				    startActivity(intent);
				     
				     
					Log.e("SELECTED", _id);

				}
			});

		}

	}
	//DIALOGO ERROR GENERICO
	
	public void dialogoerror(CharSequence titulo, CharSequence mensaje) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ListaLugaresActivity.this);
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
