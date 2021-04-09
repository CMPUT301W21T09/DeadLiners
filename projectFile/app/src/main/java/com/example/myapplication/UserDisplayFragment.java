package com.example.myapplication;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class UserDisplayFragment extends DialogFragment {
    private OnFragmentInteractionListener listener;
    private String name;
    private String email;
    private String phone;

    UserDisplayFragment(String name, String email, String phone){
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    public interface OnFragmentInteractionListener {
        void onOkPressed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.user_display_fragment, null);

        TextView name_text = view.findViewById(R.id.username_text);
        TextView email_text =  view.findViewById(R.id.email_text);
        TextView phone_text = view.findViewById(R.id.phone_text);

        name_text.setText(name);
        email_text.setText(email);
        phone_text.setText(phone);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Information")
                .setNegativeButton("Back", null).create();
    }
}

