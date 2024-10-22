package com.example.noxmusicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ArnabAdapter extends ArrayAdapter<String> {

    private final String[] a;

    public ArnabAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull String[] a) {
        super(context, resource, textViewResourceId, a);
        this.a=a;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return a[position];
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_arnab, parent, false);
        TextView textView = convertView.findViewById(R.id.textView);
        textView.setText(getItem(position));
        return convertView;
    }
}