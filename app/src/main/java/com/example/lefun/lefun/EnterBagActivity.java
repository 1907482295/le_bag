package com.example.lefun.lefun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by wangpeng on 16/12/29.
 */

public class EnterBagActivity extends Activity {

    EditText viewEdit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterbag);
        viewEdit = (EditText) this.findViewById(R.id.bag_num);
    }

    public void clickViewBag(View e){
        Intent saveIntent = new Intent(EnterBagActivity.this,MainActivity.class);
        saveIntent.putExtra(Constant.BAGNUMBER, viewEdit.getText().toString());
        this.startActivity(saveIntent);
    }
}