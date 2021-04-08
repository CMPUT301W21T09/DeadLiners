package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SearchUserCustomList extends ArrayAdapter<String> {
    private ArrayList<String> userName;
    private Context context;

    public SearchUserCustomList(Context context, ArrayList<String> userName){
        super(context,0, userName);
        this.userName = userName;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.search_user_content, parent,false);
        }

        String name = userName.get(position);

        TextView nameText = view.findViewById(R.id.user_name_text);

        nameText.setText(name);

        return view;

    }
}
