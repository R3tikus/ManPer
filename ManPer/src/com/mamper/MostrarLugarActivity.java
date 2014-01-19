package com.mamper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.AbstractAction;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class MostrarLugarActivity extends Activity {

	DatabaseHandler db;
	Lugar lug;
	String id;
	private ProgressDialog pDialog;
	ActionBar actionBar;

	// links
	TextView textnom;
	TextView textdes;
	ImageView imagfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mostrarlugar);
		
		//OBTENEMOS EL ID QUE NOS LANZA LA ACTIVIDAD ANTERIOR
		//OBTENEMOS EL OBJETO
		Intent i = getIntent();
		id = i.getStringExtra("id");
		db = new DatabaseHandler(this);
		lug = new Lugar();

		textnom = (TextView) findViewById(R.id.textnombreshow);
		textdes = (TextView) findViewById(R.id.textdescripcionshow);
		imagfo = (ImageView) findViewById(R.id.imageViewshow);

		//PONEMOS EL ACTIONBAR
		actionBar = (ActionBar) findViewById(R.id.actionbar);
		
		actionBar.setHomeAction(new IntentAction(this, Principal
				.createIntent(this), R.drawable.homeicon));
		
		//BOTON COMPARTIR
		final Action share = new IntentAction(this, createShareIntent(),
				android.R.drawable.ic_menu_share);
		actionBar.addAction(share);
		
		//BOTON BORRAR
		actionBar.addAction(new ButtonDelete());
		
		
		//BOTON EDITAR
		Intent intent = new Intent(this, EditarLugarActivity.class);
		intent.putExtra("id", id);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		final Action editplace = new IntentAction(this, intent,
				android.R.drawable.ic_menu_edit);
		actionBar.addAction(editplace);
		
		

		new CargarLugar().execute();

	}

	class CargarLugar extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MostrarLugarActivity.this);
			pDialog.setMessage(getString(R.string.loadplaces));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			// Accedemos y hacemos la consulta
			db.getWritableDatabase();

			lug = db.getPlace(Integer.parseInt(id));
			db.close();

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			actionBar.setTitle(lug.getNombre());

			// asociamos

			textnom.setText(lug.getNombre());
			textdes.setText(lug.getDescripcion());

			Bitmap myBitmap = BitmapFactory.decodeFile(lug.getFoto());
			imagfo.setImageBitmap(myBitmap);

		}

	}

	private Intent createShareIntent() {
		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "Acabo de compartir un sitio favorito.");
		return Intent.createChooser(intent, "Compartir");
	}
	
	//BOTON BORRAR
	private class ButtonDelete extends AbstractAction {

        public ButtonDelete() {
            super(android.R.drawable.ic_menu_delete);
        }

        @Override
        public void performAction(View view) {
        	//Ask the user if they want to quit
            new AlertDialog.Builder(MostrarLugarActivity.this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(getString(R.string.delete))
            .setMessage(getString(R.string.confirmdelete))
            .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
        
                	db.deletePlace(lug);                
                	                  
                	Intent i = new Intent(getApplicationContext(),Principal.class);
                	startActivity(i);
                	
                    
                }

            })
            .setNegativeButton(getString(R.string.cancel), null)
            .show();
        	
        }

    }

}
