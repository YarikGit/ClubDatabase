package com.example.sportclubdb;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sportclubdb.data.ClubContract.*;

public class ClubCursorAdapter extends android.widget.CursorAdapter {
    private TextView viewId;
    private TextView viewName;
    private TextView viewLastName;
    private TextView viewGender;
    private TextView viewSport;


    public ClubCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.database_row, viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        viewId = view.findViewById(R.id.idTextView);
        viewName = view.findViewById(R.id.nameTextView);
        viewLastName = view.findViewById(R.id.lastNameTextView);
        viewGender = view.findViewById(R.id.genderTextView);
        viewSport = view.findViewById(R.id.sportTextView);

        String currentId = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(MemberEntry.KEY_ID)));
        String currentName = cursor.getString(cursor.getColumnIndexOrThrow(MemberEntry.KEY_NAME));
        String currentLastName = cursor.getString(cursor.getColumnIndexOrThrow(MemberEntry.KEY_LAST_NAME));
        String currentGender = cursor.getString(cursor.getColumnIndexOrThrow(MemberEntry.KEY_GENDER));
        String currentSport = cursor.getString(cursor.getColumnIndexOrThrow(MemberEntry.KEY_SPORT));

        viewId.setText(currentId);
        viewName.setText(currentName);
        viewLastName.setText(currentLastName);
        viewGender.setText(currentGender);
        viewSport.setText(currentSport);

    }
}
