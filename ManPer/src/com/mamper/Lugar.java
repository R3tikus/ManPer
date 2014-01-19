package com.mamper;

public class Lugar {
	
	//variables del objeto
	private int _id;
	private String nombre;
	private String descripcion;
	private float latitud;
	private float longitud;
	private String foto;
	
	//constructor por defecto
	public Lugar()
	{
		
	}
	
	//constructor
	public Lugar(int id, String nom, String des, float lat, float longi, String fot)
	{
		this._id = id;
		this.nombre = nom;
		this.descripcion = des;
		this.latitud = lat;
		this.longitud = longi;
		this.foto = fot;		
	}
	public Lugar(String nom, String des, float lat, float longi, String fot)
	{
		this.nombre = nom;
		this.descripcion = des;
		this.latitud = lat;
		this.longitud = longi;
		this.foto = fot;		
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public float getLatitud() {
		return latitud;
	}

	public void setLatitud(float latitud) {
		this.latitud = latitud;
	}

	public float getLongitud() {
		return longitud;
	}

	public void setLongitud(float longitud) {
		this.longitud = longitud;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}
	
	

}
