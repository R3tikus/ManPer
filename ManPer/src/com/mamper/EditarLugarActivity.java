package com.mamper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.AbstractAction;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class EditarLugarActivity extends MapActivity {

	// VARIABLES

	public String PROBLEMA;
	// COMPONENTES
	public ImageView foto;
	public EditText editnombre;
	public EditText editdescription;
	public Button bguardar;
	public Button bcancelar;
	// MAPA
	public MapView mapa;
	public MapController mapControler;
	public MiItemizedOverlay puntosmapa;
	public List<Overlay> mapOverlays;
	public LocationManager lm;
	public MiLocationListener gps;

	// FOTO
	public Uri uriphoto;
	public Drawable drawphoto;
	private static final int SELECT_PICTURE = 1;
	private String selectedImagePath = null;
	String destinationImage;
	boolean cambiado = false;
	String selectedImagePathaux = null; // imagenauxiliar
	String imagedefault = "/mnt/sdcard/manPer/images/default.jpg";

	// BASE DE DATOS
	public Lugar lugar;
	public DatabaseHandler db;
	public String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editplace);

		// ENLAZADOS Y ASIGNACIONES
		db = new DatabaseHandler(EditarLugarActivity.this);
		lugar = new Lugar();

		// COMPONENTES
		foto = (ImageView) findViewById(R.id.imageView1);
		editnombre = (EditText) findViewById(R.id.editTextnombre);
		editdescription = (EditText) findViewById(R.id.editTextdescription);
		bguardar = (Button) findViewById(R.id.buttonGuardar);
		bcancelar = (Button) findViewById(R.id.buttonCancelar);

		// OBTENEMOS EL ID SI NOS LO ENVIA
		// SI OBTENEMOS ID OBTENEMOS LOS DATOS DEL LUGAR
		// SI NO SE CREA COMO NUEVO
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			id = extras.getString("id");
			db.getReadableDatabase();
			lugar = db.getPlace(Integer.parseInt(id));
			db.close();
		}
		if (id != null) {
			Log.e("ACTUALIZAR","ACTUALIZANDO EL ID: "+ id);
			lugar.set_id(Integer.parseInt(id));

		}

		if (lugar.getNombre() != null) {
			editnombre.setText(lugar.getNombre());

		}
		if (lugar.getDescripcion() != null) {
			editdescription.setText(lugar.getDescripcion());
		}
		if (lugar.getFoto() != null) {
			Bitmap myBitmap = BitmapFactory.decodeFile(lugar.getFoto());
			foto.setImageBitmap(myBitmap);
			selectedImagePath = lugar.getFoto();

		}

		// ACTIONBAR
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setHomeAction(new IntentAction(this, Principal
				.createIntent(this), R.drawable.homeicon));
		// COMPROBAMOS SI ES NUEVO
		if (extras != null) {
			actionBar.setTitle(R.string.editplace);
		} else {
			actionBar.setTitle(R.string.newplace);
		}
		//AÑADIMOS BOTON GUARDAR
		actionBar.addAction(new ButtonSave());
		

		// MAPAS
		mapa = (MapView) findViewById(R.id.mapa);
		mapa.displayZoomControls(true);
		mapa.setBuiltInZoomControls(true);
		mapa.setSatellite(true);
		mapControler = mapa.getController();
		mapControler.setZoom(19);
		mapOverlays = mapa.getOverlays();
		// ICONO QUE APARECE EN EL MAPA
		Drawable drawa = this.getResources().getDrawable(R.drawable.homeicon);
		// CREAMOS EL OBJETO CON EL ICONO DESEADO
		puntosmapa = new MiItemizedOverlay(this, drawa);
		mapOverlays.add(puntosmapa);
		actualizar();

		// COMPROBAMOS SI RECIBIMOS LOS DATOS PARA EDITAR

		if (lugar.getLatitud() != 0) {
			Log.e("DENTRO DEL IF", "ESTOY DENTRO DEL IF DE LATITUD");
			puntosmapa.clear();
			puntosmapa.addLocalizacion(lugar.getLatitud(), lugar.getLongitud(),
					"punto2");
			mapOverlays.add(puntosmapa);
			GeoPoint puntacos = new GeoPoint((int) (lugar.getLatitud() * 1E6),
					(int) (lugar.getLongitud() * 1E6));
			mapControler.setCenter(puntacos);
		}
		

		// DESACTIVAMOS EL TECLADO QUE APARECE AUTOMATICAMENTE
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// FUNCIONES ONCLICKS
		// OBTENER FOTO
		foto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent,
						getString(R.string.selecpicture)), SELECT_PICTURE);

			}
		});
		// BOTON GUARDAR
		bguardar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				guardar();

			}
		});
		// BOTON CANCELAR
		bcancelar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

	}

	// FUNCION QUE COMPRUEBA SI HEMOS RECIBIDO ALGO
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// COMPROBAMOS SI HEMOS RECIBIDO UNA FOTO
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				cambiado = true;
				selectedImagePathaux = selectedImagePath;

				Uri selectedImageUri = data.getData();
				selectedImagePath = getPath(selectedImageUri);
				foto.setImageBitmap(decodeSampledBitmapFromFile(
						selectedImagePath, 160, 160));

				

			}
		}

	}

	// FUNCION NECESARIA PARA EL MAPA
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	// FUNCION PARA OBTENER POSICION DEL GPS 
	public void actualizar() {
		gps = new MiLocationListener(this);
		if (gps.canGetLocation()) {

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			GeoPoint puntacos = new GeoPoint((int) (latitude * 1E6),
					(int) (longitude * 1E6));

			puntosmapa.addLocalizacion(latitude, longitude, "punto2");
			mapOverlays.add(puntosmapa);
			mapControler.setCenter(puntacos);

		} else {
			//SI EL GPS ESTA DESACTIVADO
			gps.showSettingsAlert();
		}

	}

	// OBTENEMOS LA DIRECCION DE UNA URI

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	//FUNCION PARA EL BOTON DE GUARDAR
	private class ButtonSave extends AbstractAction {

        public ButtonSave() {
            super(android.R.drawable.ic_menu_save);
        }

        @Override
        public void performAction(View view) {
           guardar();
        }

    }

	
	// FUNCION PARA GUARDAR
	public void guardar() {
		// SI NO RECIBIMOS UNA ID
		// CREAMOS UN LUGAR NUEVO
		if (id == null) {

			// COMPROBAMOS QUE EL CAMPO EDITTEXT TIENE ALGO ESCRITO
			RegExpressionValidator comp = new RegExpressionValidator(
					editnombre, null);
			comp.setExpression(editnombre.getText().toString());
			ValidationResult result = comp.validate();
			Log.e("VALIDATOR_NOMBRE", result.getMessage());
			// EDITTEXT NOMBRE
			if (result.isValid()) {
				RegExpressionValidator comp2 = new RegExpressionValidator(
						editdescription, null);
				comp2.setExpression(editdescription.getText().toString());
				ValidationResult resul2 = comp2.validate();
				Log.e("VALIDATOR_DESCRIPCION", resul2.getMessage());
				// EDITTEXT DESCRIPCION
				if (resul2.isValid()) {

					// copiamos la foto
					destinationImage = "/mnt/sdcard/manPer/images/"
							+ editnombre.getText().toString() + ".jpg";

					if (selectedImagePath != null) {
						Log.e("FOTO", "Hemos cogido foto");
						guardarimagen(selectedImagePath, destinationImage);
					} else {
						Log.e("FOTO", "No hemos cogido foto.");
						destinationImage = "/mnt/sdcard/manPer/images/default.jpg";
						Bitmap bitmap = BitmapFactory.decodeResource(
								getResources(), R.drawable.camera_icon);
						File sd = new File(
								Environment.getExternalStorageDirectory()
										+ "/manPer/images/");
						
						Log.e("DIRECCION", sd.getName());
						String fileName = "default.jpg";
						File dest = new File(sd, fileName);
						try {
							FileOutputStream out;
							out = new FileOutputStream(dest);
							bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
									out);
							out.flush();
							out.close();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					// LOCALICACION
					lugar.setLatitud((float) (puntosmapa.getLatitud()));
					lugar.setLongitud((float) (puntosmapa.getlongitud()));
					// Guardamos todo en la base de datos

					lugar.setNombre(editnombre.getText().toString());
					lugar.setDescripcion(editdescription.getText().toString());
					lugar.setFoto(destinationImage);
					db.addplaces(lugar);
					db.close();
					Toast.makeText(getApplicationContext(),
							getString(R.string.saved), Toast.LENGTH_LONG)
							.show();
					Log.e("NUEVO", "Lugar creado correctamente.");
					finish();
				} else {
					dialogoerror(getString(R.string.description),
							getString(R.string.nodescription));
				}
			} else {
				dialogoerror(getString(R.string.name),
						getString(R.string.noname));
			}
		}
		// SI RECIBIMOS UNA ID EDITAMOS EL LUGAR
		// ACTUALIZAMOS EL LUGAR

		if (id != null) {

			
			// COMPROBAMOS SI HEMOS MODIFICADO LA FOTO

			destinationImage = "/mnt/sdcard/manPer/images/"
					+ editnombre.getText().toString() + ".jpg";

			if (selectedImagePath.equals(destinationImage)) {
				Log.e("ACTUALIZAR", " FOTOS SON IGUALES");

			} else if (cambiado) {
				Log.e("ACTUALIZAR", "NOO SON IGUALES Y CAMBIO DE FOTO");

				if ((destinationImage.equals(selectedImagePathaux))
						|| (imagedefault.equals(selectedImagePathaux))) {
					Log.e("ACTUALIZAR", "CAMBIO DE FOTO  Y MISMO NOMBRE");
					guardarimagen(selectedImagePath, destinationImage);
				} else {
					Log.e("ACTUALIZAR",
							"NOO SON IGUALES Y CAMBIO DE FOTO  Y DISTINTO NOMBRE NOMBRE");
					guardarimagen(selectedImagePath, destinationImage);
					new File(selectedImagePathaux).delete();
				}

			} else {
				if (selectedImagePath.equals(imagedefault)) {
					Log.e("ACTUALIZAR", "CAMBIO DE NOMBRE, SIN FOTO");

					destinationImage = selectedImagePath;
				} else {
					Log.e("ACTUALIZAR",
							"CAMBIO DE NOMBRE, CON FOTO SIN CAMBIAR");
					guardarimagen(selectedImagePath, destinationImage);
					new File(selectedImagePath).delete();

				}

			}

			// LOCALICACION
			lugar.setLatitud((float) (puntosmapa.getLatitud()));
			lugar.setLongitud((float) (puntosmapa.getlongitud()));
			// Guardamos todo en la base de datos

			lugar.setNombre(editnombre.getText().toString());
			lugar.setDescripcion(editdescription.getText().toString());
			lugar.setFoto(destinationImage);
			db.updatePlace(lugar);
			db.close();
			Log.e("ACTUALIZAR", "Lugar actualizado correctamente con id" + id);
			Toast.makeText(getApplicationContext(), getString(R.string.saved),
					Toast.LENGTH_LONG).show();
			finish();
			Intent i = new Intent(getApplicationContext(),Principal.class);
        	startActivity(i);

		}
	}

	//DIALOGO GENERICO DE ERROR
	public void dialogoerror(CharSequence titulo, CharSequence mensaje) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				EditarLugarActivity.this);
		builder.setMessage(mensaje)
				.setTitle(titulo)
				.setPositiveButton(getString(R.string.ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
							}
						});

		AlertDialog error = builder.create();
		error.show();

	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth,
			int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// BitmapFactory.decodeResource(res, resId, options);
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		// return BitmapFactory.decodeResource(res, resId, options);
		return BitmapFactory.decodeFile(path, options);
	}

	//FUNCION PARA GUARDAR LA IMAGEN EN LA SD
	public void guardarimagen(String origen, String destino) {

		Bitmap sal = decodeSampledBitmapFromFile(origen, 160, 160);
		OutputStream outputStream = null;
		File files = new File(destino);
		try {
			outputStream = new FileOutputStream(files);
			sal.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
			outputStream.flush();
			outputStream.close();

		} catch (IOException e) {
			Log.d("COPIANDOIMAGEN", "ERROR");
		}
	}
	
}
