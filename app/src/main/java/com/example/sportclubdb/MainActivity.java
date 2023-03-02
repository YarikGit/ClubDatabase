package com.example.sportclubdb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sportclubdb.data.ClubContract.*;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ImageView addMember; // button
    private ListView listView; //db view
    private static final int MEMBER_LOADER = 123;
    private ClubCursorAdapter clubCursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createButtonAddMember();


        listView = findViewById(R.id.listViewDisplayDb);
        clubCursorAdapter = new ClubCursorAdapter(this, null, 1);
        listView.setAdapter(clubCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, MemberInfo.class);
                Uri currentMemberUri = ContentUris.withAppendedId(MemberEntry.CONTENT_URI, l);
                intent.setData(currentMemberUri);
                startActivity(intent);

            }
        });

        getSupportLoaderManager().initLoader(MEMBER_LOADER, null, this);
    }

    private void createButtonAddMember(){
        addMember = findViewById(R.id.addMember);

        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MemberInfo.class));
            }
        });
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String[] projection = {
                MemberEntry.KEY_ID,
                MemberEntry.KEY_NAME,
                MemberEntry.KEY_LAST_NAME,
                MemberEntry.KEY_GENDER,
                MemberEntry.KEY_SPORT
        };
        CursorLoader loader = new CursorLoader(this,MemberEntry.CONTENT_URI, projection,
                null, null, null);

        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        clubCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        clubCursorAdapter.swapCursor(null);
    }

}