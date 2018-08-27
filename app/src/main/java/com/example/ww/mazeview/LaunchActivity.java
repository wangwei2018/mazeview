package com.example.ww.mazeview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LaunchActivity extends Activity implements View.OnClickListener{

    private EditText et_column;
    private EditText et_row;
    private Button bt_create;
    private int column;
    private int row;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        et_column = (EditText) findViewById(R.id.et_column);
        et_row = (EditText) findViewById(R.id.et_row);
        bt_create = (Button) findViewById(R.id.bt_create);
        bt_create.setOnClickListener(this);




    }

    @Override
    public void onClick(View view) {
        String column_str=et_column.getText().toString();
        String row_str=et_row.getText().toString();

        if(!TextUtils.isEmpty(column_str)&&!TextUtils.isEmpty(row_str)){
            column = Integer.valueOf(column_str);
            row = Integer.valueOf(row_str);
            if(column >0&& row >0){
                Intent intent = new Intent(this,MainActivity.class);
                intent.putExtra("column",column);
                intent.putExtra("row",row);
                startActivity(intent);
            }
        }


    }
}
