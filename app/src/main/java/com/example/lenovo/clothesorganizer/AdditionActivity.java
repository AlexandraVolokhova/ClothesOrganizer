package com.example.lenovo.clothesorganizer;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import java.io.File;
import android.net.Uri;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.widget.RadioGroup;

public class AdditionActivity extends ActionBarActivity  {

    String photoPath;
    DBHelper dbHelper;
    EditText edName, edMinTempr, edMaxTempr, edComment;
    ImageView photo;
    RadioGroup rgType;

    //Запуск Addition Activity. При запуке в поле ImageView вставляется картинка, переданная из вызывающей активности (MainActivity)
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addition);

        //сопоставляем все поля на экране с пременными в коде
        photo = (ImageView)findViewById(R.id.imageView);
        edName = (EditText) findViewById(R.id.edThingName);
        rgType = (RadioGroup) findViewById(R.id.rgType);
        edMinTempr = (EditText) findViewById(R.id.edMinTempr);
        edMaxTempr = (EditText) findViewById(R.id.edMaxTempr);
        edComment = (EditText) findViewById(R.id.edComment);

        //вставляем изображение
        photoPath =  getIntent().getExtras().getString("com.example.lenovo.clothesorganizer.PHOTO_PATH");
        File file = new File(photoPath);
        photo.setImageURI(Uri.fromFile(file));

        //создаём объект для создания и управления БД
        dbHelper = new DBHelper(this);
    }

    public void onClick (View view) {
        //создаём объект для данных
        ContentValues cv = new ContentValues();

        String type = null;
        String name = "My thing";
        String minTempr = "-25";
        String maxTempr = "25";
        String comment = "My thing";

        //получаем данные из полей ввода
        name = edName.getText().toString();
        switch (rgType.getCheckedRadioButtonId()) {
            case R.id.rbTypeUp:
                type = "up";
                break;
            case R.id.rbTypeDown:
                type = "down";
                break;
            case R.id.rbTypeFootwear:
                type = "footwear";
                break;
            case R.id.rbTypeAcc:
                type = "accessories";
                break;
            default:
                break;
        }
        minTempr = edMinTempr.getText().toString();
        maxTempr = edMinTempr.getText().toString();
        comment = edComment.getText().toString();

        cv.put("name", name);
        cv.put("type", type);
        cv.put("minTempr", minTempr);
        cv.put("maxTempr", maxTempr);
        cv.put("comment", comment);
        cv.put("pathToImage", photoPath);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert("myThing", null,cv);

        dbHelper.close();

        Intent i = new Intent(AdditionActivity.this, WardrobeActivity.class);
        startActivity(i);
        
    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table myThing ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "type text"
                    + "minTempr text"
                    + "maxTempr text"
                    + "comment text"
                    + "pathToImage text"+ ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_addition, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
