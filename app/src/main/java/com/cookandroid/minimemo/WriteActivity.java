package com.cookandroid.minimemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        setTitle("미니 메모장 - 메모장");

        //메모 내용 전달받기
        Intent intent = getIntent();
        String content = intent.getStringExtra("content");
        int position = intent.getIntExtra("position", 0);

        TextView contentView = (TextView) findViewById(R.id.memoText);
        contentView.setText(content);

        //메모 작성 or 수정 저장 버튼
        Button memoButton = (Button) findViewById(R.id.memoBtn);
        memoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editContent = contentView.getText().toString();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("content", editContent);
                intent.putExtra("position", position);

                Log.d("DebugCheck", "메모 저장");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}