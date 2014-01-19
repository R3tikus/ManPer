package com.mamper;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class Principal extends Activity {

	// VARIABLES
	public Button boton_lista;
	public Button boton_mapa;
	int selectedPosition; //OPCION DE LENGUAGE
	Configuration c;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_principal);

		// ACTIONBAR
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle(R.string.home);
		final Action newplace = new IntentAction(this, new Intent(this,
				EditarLugarActivity.class), R.drawable.w_content_new);
		actionBar.addAction(newplace);

		// ENLAZADOS
		boton_lista = (Button) findViewById(R.id.button_lista);
		boton_mapa = (Button) findViewById(R.id.button_map);

		// OBTENEMOS EN QUE IDIOMA ESTAMOS
		c = new Configuration(getResources().getConfiguration());


		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		selectedPosition = prefs.getInt("lenguage", selectedPosition);

		// FUNCIONES ONCLICKS
		boton_lista.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// LANZAMOS ACTIVIDAD LISTALUGARES
				Intent i = new Intent(getApplicationContext(),
						ListaLugaresActivity.class);
				startActivity(i);

			}
		});

		boton_mapa.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// LANZAMOS ACTIVIDAD MAPASLUGARES
				Intent i = new Intent(getApplicationContext(),
						MapaLugaresActivity.class);
				startActivity(i);

			}
		});

	}

	// MENU
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_principal, menu);
		return true;
	}

	// MENU ONCLICKS
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		// IDIOMA
		case R.id.menu_settings:

			String[] arrLanguages = new String[] { "Espanol", "Ingles" };
			new AlertDialog.Builder(this)
					.setSingleChoiceItems(arrLanguages, selectedPosition, null)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									dialog.dismiss();
									selectedPosition = ((AlertDialog) dialog)
											.getListView()
											.getCheckedItemPosition();

									// GUARDAMOS NUESTRA PREFERENCIA
									SharedPreferences.Editor editor = getPreferences(
											MODE_PRIVATE).edit();
									editor.putInt("lenguage", selectedPosition);
									editor.commit();
									Locale spanish = new Locale("es", "ES");
									if (selectedPosition == 0) {

										c.locale = spanish;

									} else if (selectedPosition == 1) {

										c.locale = Locale.ENGLISH;

									}
									getResources().updateConfiguration(c,
											getResources().getDisplayMetrics());

									Intent intent = getIntent();

									overridePendingTransition(0, 0);

									intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

									finish();

									overridePendingTransition(0, 0);

									startActivity(intent);
								}
							}).show();

			return true;
			// ABOUT
		case R.id.about:

			new AlertDialog.Builder(this)
					.setTitle("About")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setMessage("Autor: @juanma_tirado \n Version: 1.0")
					.setNegativeButton("Aceptar",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// do nothing
								}
							}).show();

			return true;
			// SALIR
		case R.id.quit:
			Principal.this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// FUNCION DEL ACTIONBAR QUE NOS DEVULVE AL HOME

	public static Intent createIntent(Context context) {
		Intent i = new Intent(context, Principal.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return i;
	}

}
