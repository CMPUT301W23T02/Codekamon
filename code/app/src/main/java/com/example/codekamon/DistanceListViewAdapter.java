package com.example.codekamon;

import static com.example.codekamon.GeoDistanceCalculator.df;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.gavaghan.geodesy.GlobalPosition;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * This class "DistanceListViewAdapter" is used to display a cell in the list of the codekamon and its details
 *
 * Author: Elisandro Cruz Martinez
 *
 */
public class DistanceListViewAdapter extends ArrayAdapter<SpaceBetweenPoints> {

    public DistanceListViewAdapter(Context context, ArrayList<SpaceBetweenPoints> arrayList) {
        super(context,0, arrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewing = convertView;

        if (viewing == null) {
            viewing = LayoutInflater.from(getContext()).inflate(R.layout.adapter_view_codeby, parent, false);
        }

        SpaceBetweenPoints item = getItem(position);

        GlobalPosition code_item_position = item.getLocation();
        Double latitude = code_item_position.getLatitude(), longitude = code_item_position.getLongitude();

        TextView txt_code_name = viewing.findViewById(R.id.code_name);
        txt_code_name.setText(item.getName());

        TextView txt_code_coordinates = viewing.findViewById(R.id.code_coordinates);

        String location = "Location: \n("+ df.format(latitude) + "," + df.format(longitude) + ")";
        txt_code_coordinates.setText(location);

        TextView txt_code_distance = viewing.findViewById(R.id.code_distance);
        String distance = "Distance: \n" + item.getDistance() + "m";
        txt_code_distance.setText(distance);

        return viewing;
    }
}