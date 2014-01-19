package com.mamper;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

public class SplashActivity extends Activity {

	// TIEMPO DE MOSTRAR EL SPLASH EN MILISEGUNDOS
	private final int SPLASH_DISPLAY_LENGTH = 4000;
	private final String PATH_DIR = "/manPer/images/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		// CREA EL DIRECTORIO DE LAS IMAGENES SI NO EXISTE
		if(!createDirIfNotExists(PATH_DIR))
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(
					SplashActivity.this);
			builder.setMessage("INSERT A SD")
					.setTitle("SD")
					.setPositiveButton(getString(R.string.ok),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
								   SplashActivity.this.finish();
								}
							});

			AlertDialog error = builder.create();
			error.show();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		
		boolean isSplashEnabled = sp.getBoolean("isSplashEnabled", true);

		if (isSplashEnabled) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					//TERMINAMOS LA ACTIVIDAD PARA NO PODER VOLVER
					SplashActivity.this.finish();
					//EJECUTAMOS LA NUEVA ACTIVIDAD
					Intent mainIntent = new Intent(SplashActivity.this,
							Principal.class);
					SplashActivity.this.startActivity(mainIntent);
				}
			}, SPLASH_DISPLAY_LENGTH);
		} else {
			// SI EL SPLASH NO ESTA ACTIVADO, ENTONCES EJECUTA ACTIVIDAD INMEDIATAMENTE
			finish();
			Intent mainIntent = new Intent(SplashActivity.this, Principal.class);
			SplashActivity.this.startActivity(mainIntent);
		}
	}

	// crea el directorio si no existe para copiar las imagenes
	public static boolean createDirIfNotExists(String path) {
		boolean ret = true;

		

		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_MOUNTED)) {
			File file = new File(Environment.getExternalStorageDirectory(), path);
			if (!file.exists()) {
				if (!file.mkdirs()) {
					Log.e("TravellerLog :: ", "Problem creating Image folder");
					ret = false;
				}
			}

		}
		else
		{
			//NO TIENE SD
			ret=false;
		}
		

		return ret;
	}

}
