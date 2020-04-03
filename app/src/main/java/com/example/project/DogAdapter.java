package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import java.util.ArrayList;

public class DogAdapter extends ArrayAdapter<Dog> {

    DogAdapter(@NonNull Context context, ArrayList<Dog> dogs) {
        super(context, 0, dogs);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Dog currDog = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dog_list_item, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.nameItemView);
        TextView tvBreed = convertView.findViewById(R.id.breedItemView);
        TextView tvInfo = convertView.findViewById(R.id.infoItemView);
        ImageView imgUrl = convertView.findViewById(R.id.urlItemView);

        assert currDog != null;
        tvName.setText(String.format("Name: %s", currDog.getName()));
        tvBreed.setText(String.format("Breed: %s", currDog.getBreed()));
        tvInfo.setText(String.format("Info: %s", currDog.getInfo()));

        if (!currDog.getImage_url().equals("No Image")) {
            new ImageDownloader(imgUrl).execute(currDog.getImage_url());
        } else {
            imgUrl.setImageResource(R.drawable.image_not_found);
        }


        return convertView;
    }
}


