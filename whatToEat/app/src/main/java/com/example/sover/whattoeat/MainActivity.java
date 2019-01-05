package com.example.sover.whattoeat;

import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private database db = null;

    Button append, edit, delete, clear, go;
    TextView result;
    EditText food;
    ListView list;
    Cursor cursor;
    long myid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        food = (EditText) findViewById(R.id.food);
        result = (TextView) findViewById(R.id.result);
        list = (ListView) findViewById(R.id.list);
        append = (Button) findViewById(R.id.append);
        edit = (Button) findViewById(R.id.edit);
        delete = (Button) findViewById(R.id.delete);
        clear = (Button) findViewById(R.id.clear);
        go = (Button) findViewById(R.id.go);

        append.setOnClickListener(lr);
        edit.setOnClickListener(lr);
        delete.setOnClickListener(lr);
        clear.setOnClickListener(lr);
        go.setOnClickListener(golr);
        list.setOnItemClickListener(listlr);

        db = new database(this); // 傳入一個自訂class的database型態的值
        db.open();
        cursor = db.getall();
        UpdateAdapter(cursor);
    }
    private ListView.OnItemClickListener listlr=
            new ListView.OnItemClickListener(){
                public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                    ShowData(id);
                    cursor.moveToPosition(position);

                }
            };

    private void ShowData(long id){
        Cursor c = db.get(id);
        myid = id;
        food.setText(c.getString(1));
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        db.close();
    }

    private Button.OnClickListener lr=
            new Button.OnClickListener(){
                public void onClick(View v){
                    try {
                        switch (v.getId()){
                            case R.id.append:{
                                String f = food.getText().toString();
                                if (db.append(f) > 0){
                                    cursor = db.getall();
                                    UpdateAdapter(cursor);
                                    ClearEdit();
                                }
                                break;
                            }
                            case R.id.edit:{
                                String f = food.getText().toString();
                                if (db.update(myid, f)){
                                    cursor = db.getall();
                                    UpdateAdapter(cursor);
                                }
                                break;
                            }
                            case R.id.delete:{
                                if (cursor != null && cursor.getCount() >= 0){
                                    AlertDialog.Builder builder = new
                                            AlertDialog.Builder(MainActivity.this);
                                    builder.setTitle("確定刪除");
                                    builder.setMessage("確定要刪除" + food.getText().toString() + "這筆資料?");
                                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
                                        public void onClick(DialogInterface dialog, int i){

                                        }
                                    });
                                    builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int i){
                                            if (db.delete(myid)){
                                                cursor = db.getall();
                                                UpdateAdapter(cursor);
                                                ClearEdit();
                                            }
                                        }
                                    });
                                    builder.show();
                                }
                                break;
                            }
                            case R.id.clear:{
                                ClearEdit();
                                break;
                            }
                        }
                    }catch (Exception error){
                        Toast.makeText(getApplicationContext(), "資料不正確", Toast.LENGTH_SHORT).show();
                    }
                }
            };

    private Button.OnClickListener golr =
            new Button.OnClickListener(){
                public void onClick(View v){
                    int totalList =  list.getAdapter().getCount(); // 目前總項目數
                    int random = (int)(Math.random()*totalList);
                    ArrayList allid = db.getallid(); // 拿到包含全部id的array
                    int ran = (int)allid.get(random); // 隨機選擇id
                    Cursor c = db.get(ran); // 查詢值
                    result.setText(c.getString(1));
                }
            };

    public void ClearEdit(){
        food.setText("");
    }

    public void UpdateAdapter(Cursor cursor){
        if (cursor != null && cursor.getCount() >= 0){
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1, cursor, new String[] {"food"},
                    new int[] {android.R.id.text1}, 0);
            list.setAdapter(adapter);
        }
    }

}
