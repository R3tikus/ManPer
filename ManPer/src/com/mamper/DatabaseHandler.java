package com.mamper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// Version Base de datos
	private static final int DATABASE_VERSION = 1;

	// Nombre de la base de datos
	private static final String DATABASE_NAME = "ManPer";

	// Nombre de la tabla
	private static final String TABLE_PLACES = "lugares";

	// nombres de las columnas de la tabla lugares
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_DESCRIPTION = "descripcion";
	private static final String KEY_LATITUDE = "latitud";
	private static final String KEY_LONGITUDE = "longitud";
	private static final String KEY_PHOTO = "foto";

	// constructor
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Se ejecuta cuando se crea la base de datos
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_PLACES_TABLE = "CREATE TABLE " + TABLE_PLACES + "("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME
				+ " TEXT," + KEY_DESCRIPTION + " TEXT," + KEY_LATITUDE
				+ " REAL, " + KEY_LONGITUDE + " REAL," + KEY_PHOTO + " TEXT"
				+ ")";
		db.execSQL(CREATE_PLACES_TABLE);
		Log.e("BD", "BASE DE DATOS CREADA");
	}

	// UPGRADING
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Lanza la vieja talba si existe
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);

		// crea la tabla de nuevo
		onCreate(db);
	}

	// AHORA INSERTAREMOS TODAS LAS OPERACIONES A REALIZAR EN LA BASE DE DATOS

	// AÑADIMOS UN LUGAR
	public void addplaces(Lugar lugar) {
		// abrimos la base de datos
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues valores = new ContentValues();
		valores.put(KEY_NAME, lugar.getNombre());// añadimos el nombre
		Log.e("BD", lugar.getNombre());
		valores.put(KEY_DESCRIPTION, lugar.getDescripcion()); // descripcion
		Log.e("BD", lugar.getDescripcion());
		valores.put(KEY_LATITUDE, lugar.getLatitud()); // latitud

		valores.put(KEY_LONGITUDE, lugar.getLongitud()); // longitud

		valores.put(KEY_PHOTO, lugar.getFoto()); // foto
		Log.e("BD", lugar.getFoto());
		// insertamos fila
		db.insert(TABLE_PLACES, null, valores);
		db.close();

	}

	public Lugar getPlace(int id) {
		// abrimos la bd
		SQLiteDatabase db = this.getReadableDatabase();

		// hacemos la consulta
		Cursor cursor = db.query(TABLE_PLACES, null, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Lugar lug = new Lugar(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), cursor.getFloat(3),
				cursor.getFloat(4), cursor.getString(5));

		// devolvemos
		return lug;

	}

	// obtener todos los lugares
	public List<Lugar> getAllPlaces() {
		List<Lugar> placesList = new ArrayList<Lugar>();
		// Seleccioamos toda la tabla
		String selectQuery = "SELECT  * FROM " + TABLE_PLACES;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// ahora vamos leyendo uno a uno y añadiendo en la lista
		if (cursor.moveToFirst()) {
			do {
				Lugar lugar = new Lugar();
				lugar.set_id(Integer.parseInt(cursor.getString(0)));
				lugar.setNombre(cursor.getString(1));
				lugar.setDescripcion(cursor.getString(2));
				lugar.setLatitud(cursor.getFloat(3));
				lugar.setLongitud(cursor.getFloat(4));
				lugar.setFoto(cursor.getString(5));

				// Añadimos el lugar a la lista
				placesList.add(lugar);

			} while (cursor.moveToNext());
		}

		// return 
		return placesList;
	}

	// Obtenemos el numero de lugares
	public int getPlacesCount() {
		String countQuery = "SELECT  * FROM " + TABLE_PLACES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return 
		return cursor.getCount();
	}
	 // Actualizamos un lugar 
	public int updatePlace(Lugar lugar) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(KEY_NAME, lugar.getNombre());
	    values.put(KEY_DESCRIPTION, lugar.getDescripcion());
	    values.put(KEY_LATITUDE, lugar.getLatitud());
	    values.put(KEY_LONGITUDE, lugar.getLongitud());
	    values.put(KEY_PHOTO, lugar.getFoto());
	 
	    // actualizamos fila
	    return db.update(TABLE_PLACES, values, KEY_ID + " = ?",
	            new String[] { String.valueOf(lugar.get_id()) });
	}
	// Borramos un lugar
	public void deletePlace(Lugar lugar) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_PLACES, KEY_ID + " = ?",
	            new String[] { String.valueOf(lugar.get_id()) });
	    db.close();
	}

}
