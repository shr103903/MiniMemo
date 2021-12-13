package com.cookandroid.minimemo;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("미니 메모장 - 메모 목록");

        //리스트뷰 ArrayList, Adapter
        ArrayList<String> memoList = new ArrayList<>();
        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, memoList);
        listView.setAdapter(adapter);

        //launcher
        ActivityResultLauncher<Intent> launcher;
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String content = data.getStringExtra("content");
                        int position = data.getIntExtra("position", 0);

                        //메모 수정
                        if(position < memoList.size()){
                            memoList.set(position, content);
                            Log.d("DebugCheck", "메모 수정 완료");
                        } else {
                            //메모 추가
                            memoList.add(content);
                            Log.d("DebugCheck", "메모 추가 완료");
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

        //메모 추가하기 버튼
        Button buttonWrite = (Button) findViewById(R.id.writeBtn);
        buttonWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WriteActivity.class);
                intent.putExtra("content","");
                intent.putExtra("position", memoList.size());

                Log.d("DebugCheck", "메모 입력 이동");
                launcher.launch(intent);
            }
        });

        //그림판 이동하기 버튼
        Button buttonDraw = (Button) findViewById(R.id.drawBtn);
        buttonDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DrawActivity.class);
                Log.d("DebugCheck", "그림판 이동");
                launcher.launch(intent);
            }
        });

        //리스트뷰 클릭하면 메모 수정
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), WriteActivity.class);
                intent.putExtra("content", memoList.get(position));
                intent.putExtra("position", position);

                Log.d("DebugCheck", "메모 수정 이동");
                launcher.launch(intent);
            }
        });
    }
}