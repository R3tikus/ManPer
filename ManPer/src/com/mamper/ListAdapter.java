package com.mamper;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter<Lugar> {

	public ListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		// TODO Auto-generated constructor stub
	}

	private List<Lugar> items;

	public ListAdapter(Context context, int resource, List<Lugar> items) {

		super(context, resource, items);

		this.items = items;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;

		if (v == null) {
			

			LayoutInflater vi;
			vi = LayoutInflater.from(getContext());
			v = vi.inflate(R.layout.single_row, null);

		}

		Lugar p = items.get(position);

		if (p != null) {

			ImageView tt = (ImageView) v.findViewById(R.id.imagefotorow);
			TextView tt1 = (TextView) v.findViewById(R.id.textnombrerow);
			TextView tt3 = (TextView) v.findViewById(R.id.texdescriptionrow);

			if (tt != null) {
				Bitmap myBitmap =BitmapFactory.decodeFile(p.getFoto());				
				tt.setImageBitmap(myBitmap);
				
		
			}
			if (tt1 != null) {

				tt1.setText(p.getNombre().toString());
			}
			if (tt3 != null) {

				tt3.setText(p.getDescripcion().toString());
			}
		}

		return v;

	}
}