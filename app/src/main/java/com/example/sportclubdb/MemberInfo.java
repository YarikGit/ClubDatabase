package com.example.sportclubdb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sportclubdb.data.ClubContract;
import com.example.sportclubdb.data.ClubContract.MemberEntry;
import com.example.sportclubdb.data.SportClubContentProvider;
import com.example.sportclubdb.data.SportClubDBOpenHelper;

public class MemberInfo extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ADD_MEMBER = 1;
    private static final int EDIT_MEMBER = 2;
    private static final int EDIT_MEMBER_LOADER = 222;

    private EditText nameEditText, lastNameEditText, sportEditText;
    private Spinner genderSpinner;
    private int gender = 0;
    private ArrayAdapter arrayAdapter;
    private Intent memberIntent;
    private int runFlag = ADD_MEMBER;
    private Uri memberUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info);

        linkViews();
        createSpinner();
        checkRunVariant();

    }


    private void checkRunVariant(){
        memberIntent = getIntent();
        memberUri = memberIntent.getData();

        if(memberUri == null){
            runFlag = ADD_MEMBER;
            setTitle("Add a Member");
            invalidateOptionsMenu();
        }else {
            runFlag = EDIT_MEMBER;
            setTitle("Edit the Member");
            getSupportLoaderManager().initLoader(EDIT_MEMBER_LOADER, null, this);
        }

    }

    private void linkViews(){
        nameEditText = findViewById(R.id.nameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        sportEditText = findViewById(R.id.sportEditText);
        genderSpinner = findViewById(R.id.genderSpinner);
    }

    private void createSpinner(){

        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(arrayAdapter);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s = genderSpinner.getSelectedItem().toString();
                switch(s){
                    case "Unknown" :
                        gender = MemberEntry.GENDER_UNKNOWN;
                        break;
                    case "Male" :
                        gender = MemberEntry.GENDER_MALE;
                        break;
                    case "Female" :
                        gender = MemberEntry.GENDER_FEMALE;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void saveMember(){
        String name = nameEditText.getText().toString().trim();  // - trim() обрізає всі пробіли спереді та позаду
        String lastName = lastNameEditText.getText().toString().trim();
        String sport = sportEditText.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Name Incorrect!", Toast.LENGTH_LONG).show();
            return;
        }else if(TextUtils.isEmpty(lastName)){
            Toast.makeText(this, "Last Name Incorrect!", Toast.LENGTH_LONG).show();
            return;
        }else if(TextUtils.isEmpty(sport)){
            Toast.makeText(this, "Sport Incorrect!", Toast.LENGTH_LONG).show();
            return;
        }else if(gender == MemberEntry.GENDER_UNKNOWN){
            Toast.makeText(this, "Choose Gender!", Toast.LENGTH_LONG).show();
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MemberEntry.KEY_NAME, name);
        contentValues.put(MemberEntry.KEY_LAST_NAME, lastName);
        contentValues.put(MemberEntry.KEY_GENDER, gender);
        contentValues.put(MemberEntry.KEY_SPORT, sport);


        if(runFlag == ADD_MEMBER) {
            ContentResolver contentResolver = getContentResolver();

            Uri uri = contentResolver.insert(MemberEntry.CONTENT_URI, contentValues);

            if (uri == null) {
                Toast.makeText(this, "Insertion of data in the table failed!", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(this, "Data saved!", Toast.LENGTH_LONG).show();
        }else if(runFlag == EDIT_MEMBER){
            int rowsChange = getContentResolver().update(memberUri,contentValues,null, null);

            if(rowsChange == 0){
                Toast.makeText(this, "Saving of data in the table failed!", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "Member update!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showDeleteMemberDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("Do you want delete the member?");
        dialogBuilder.setPositiveButton("Delete!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteMember();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialogInterface != null){
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void deleteMember(){
        if(runFlag == EDIT_MEMBER){
            int rowsDeleted = getContentResolver().delete(memberUri, null, null);
            if (rowsDeleted == 0){
                Toast.makeText(this, "Deleting of data from table failed!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Member is deleting!", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

    private void clearDb(){
        Context context = getApplicationContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want clear all members?");
        builder.setPositiveButton("Clear!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MemberInfo.this, "Database is clear!", Toast.LENGTH_SHORT).show();
                context.deleteDatabase(ClubContract.DATABASE_NAME);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialogInterface != null){
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(runFlag == ADD_MEMBER) {
            MenuItem item = menu.findItem(R.id.delete_action);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        getMenuInflater().inflate(R.menu.edit_member_menu_with_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.save_action:
                saveMember();
                break;
            case R.id.delete_action:
                showDeleteMemberDialog();
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
            case R.id.remove_action:
                clearDb();
                break;
        }

        return true;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if(runFlag == EDIT_MEMBER) {

            String[] projection = {MemberEntry.KEY_ID,
                    MemberEntry.KEY_NAME,
                    MemberEntry.KEY_LAST_NAME,
                    MemberEntry.KEY_GENDER,
                    MemberEntry.KEY_SPORT};

            return new CursorLoader(this, memberUri, projection,
                    null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if(runFlag == EDIT_MEMBER){
            if(data.moveToFirst()) {
                int indexId = data.getColumnIndex(MemberEntry.KEY_ID);
                int indexName = data.getColumnIndex(MemberEntry.KEY_NAME);
                int indexLastName = data.getColumnIndex(MemberEntry.KEY_LAST_NAME);
                int indexGender = data.getColumnIndex(MemberEntry.KEY_GENDER);
                int indexSport = data.getColumnIndex(MemberEntry.KEY_SPORT);

                nameEditText.setText(data.getString(indexName));
                lastNameEditText.setText(data.getString(indexLastName));
                genderSpinner.setSelection(data.getInt(indexGender));
                sportEditText.setText(data.getString(indexSport));
           }
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}