package ru.ryazanoff.sqlitetest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    TextView tv;
    Button bAdd, bShow;
    EditText etName, etPhone, etAge;
    SQLiteDatabase sdb;

    DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView)findViewById(R.id.textView);
        bAdd = (Button)findViewById(R.id.buttonAdd);
        bShow = (Button)findViewById(R.id.buttonShow);
        etName = (EditText)findViewById(R.id.editTextName);
        etAge = (EditText)findViewById(R.id.editTextAge);
        etPhone = (EditText)findViewById(R.id.editTextPhone);


        //creating db
        dbHelper = new DatabaseHelper(this, "mydatabase.db", null, 1);
        sdb = dbHelper.getReadableDatabase();

        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                int phone = Integer.parseInt(etPhone.getText().toString());
                int age = Integer.parseInt(etAge.getText().toString());

                ContentValues newValues = new ContentValues();
                // Задайте значения для каждой строки.
                newValues.put(dbHelper.NAME_COLUMN, name);
                newValues.put(dbHelper.PHONE_COLUMN, phone);
                newValues.put(dbHelper.AGE_COLUMN, age);
                // Вставляем данные в базу
                sdb.insert("students", null, newValues);
            }
        });

        bShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = sdb.query("students", new String[] {dbHelper.NAME_COLUMN, dbHelper.PHONE_COLUMN, dbHelper.AGE_COLUMN},
                        null, null,
                        null, null, null) ;

                /*
                Cursor c = query(
                    true,        //boolean distinct
                    "my_table",   //String table
                    tableColumns,//String[] columns
                    whereClause, //String selection
                    whereArgs,   //String[] selectionArgs
                    null,        //String groupBy
                    null,        //String having
                    null,        //String orderBy
                    null,        //String limit
                    null         //CancellationSignal cancellationSignal
                );
                 */

                String result = "";
                int number = cursor.getCount();
                cursor.moveToFirst();
                for(int i = 0;i<number;i++){
                    String stName = cursor.getString(cursor.getColumnIndex(dbHelper.NAME_COLUMN));
                    int stPhone = cursor.getInt(cursor.getColumnIndex(dbHelper.PHONE_COLUMN));
                    int stAge = cursor.getInt(cursor.getColumnIndex(dbHelper.AGE_COLUMN));
                    result += stName + " " + stPhone + " " + stAge + "\n";
                    if (i==number-1)
                        continue;
                    cursor.moveToNext();
                }
                tv.setText(result);
                cursor.close(); //очищаем ресурсы и делаем недействительным


            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
