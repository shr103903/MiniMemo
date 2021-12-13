package com.cookandroid.minimemo;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class DrawActivity extends AppCompatActivity {

    Button buttonBack, buttonErase, buttonEraseAll, buttonSave;

    final static int LINE = 1, CIRCLE = 2, RECT = 3,  //선, 원, 사각형 그리기
            RED = 4, YELLOW = 5, GREEN = 6, BLUE = 7, BLACK = 8, WHITE = 9;    //색 변경, 하얀색은 지우개
    static int curShape = LINE,              //기본은 선
            color = BLACK;                   //기본은 검정색

    //그림 도형 배열
    static ArrayList<ShapeLists> shapeArrList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        setTitle("미니 메모장 - 그림판");


        //메인 액티비티로 돌아가기
        buttonBack = (Button) findViewById(R.id.okBtn);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent outIntent = new Intent(getApplicationContext(), MainActivity.class);

                Log.d("DebugCheck", "그림판 닫기");
                finish();
            }
        });

        //마지막 그린 도형만 지우기
        buttonErase = (Button) findViewById(R.id.eraseBtn);
        buttonErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shapeArrList.remove(shapeArrList.size() - 1);
            }
        });

        //모든 도형 지우기
        buttonEraseAll = (Button) findViewById(R.id.eraseAllBtn);
        buttonEraseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shapeArrList.clear();
            }
        });

        //그림 그리는 뷰
        LinearLayout pictureLayout = (LinearLayout) findViewById(R.id.pictureLayout);
        MyGraphicView graphicView = (MyGraphicView) new MyGraphicView(this);
        pictureLayout.addView(graphicView);
    }



    //메뉴
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, "선 그리기");
        menu.add(0, 2, 0, "원 그리기");
        menu.add(0, 3, 0, "사각형 그리기");

        //색 변경 서브 메뉴
        SubMenu submenu = menu.addSubMenu("색 변경");
        submenu.add(0,4,0,"빨강");
        submenu.add(0,5,0,"노랑");
        submenu.add(0,6,0,"초록");
        submenu.add(0,7,0,"파랑");
        submenu.add(0,8,0,"검정");
        submenu.add(0, 9, 0, "지우개");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                curShape = LINE;
                return true;
            case 2:
                curShape = CIRCLE;
                return true;
            case 3:
                curShape = RECT;
                return true;
            case 4:
                color = RED;
                return true;
            case 5:
                color = YELLOW;
                return true;
            case 6:
                color = GREEN;
                return true;
            case 7:
                color = BLUE;
                return true;
            case 8:
                color = BLACK;
                return true;
            case 9:
                color = WHITE;
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    //그림판 뷰
    private static class MyGraphicView extends View {
        int startX = -1, startY = -1, stopX = -1, stopY = -1;
        ShapeLists saveShape;

        public MyGraphicView(Context context) {
            super(context);
        }

        //그리기 액션
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = (int) event.getX();
                    startY = (int) event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    stopX = (int) event.getX();
                    stopY = (int) event.getY();
                    this.invalidate();      //onDraw() 호출
                    break;
            }
            return true;
        }

        //그리기 설정
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(20);
            paint.setStyle(Paint.Style.STROKE);

            switch (color) {
                case RED:
                    paint.setColor(Color.RED);
                    break;
                case YELLOW:
                    paint.setColor(Color.YELLOW);
                    break;
                case GREEN:
                    paint.setColor(Color.GREEN);
                    break;
                case BLUE:
                    paint.setColor(Color.BLUE);
                    break;
                case BLACK:
                    paint.setColor(Color.BLACK);
                    break;
                case WHITE:
                    paint.setColor(Color.WHITE);
                    break;
            }

            //마지막 그린 도형 배열에 추가
            saveShape = new ShapeLists(curShape, startX, startY, stopX, stopY, paint);
            shapeArrList.add(saveShape);


            if (saveShape != null) {
                drawShape(canvas, saveShape);
            }
            drawFull(canvas);        //이전 그린 모양들도 화면에 표시
        }

        //배열값 전달받아 화면에 이전 도형도 모두 같이 표시
        public void drawFull(Canvas canvas) {
            for (ShapeLists a : shapeArrList){
                drawShape(canvas, a);
            }
        }

        //모양 그리기
        public void drawShape(Canvas canvas, ShapeLists shapeLists){
            int curShape = shapeLists.curShape;
            int startX = shapeLists.startX;
            int startY = shapeLists.startY;
            int stopX = shapeLists.stopX;
            int stopY = shapeLists.stopY;
            Paint paint = shapeLists.paint;

            switch (curShape) {
                case LINE:
                    canvas.drawLine(startX, startY, stopX, stopY, paint);
                    break;
                case CIRCLE:
                    int radius = (int)Math.sqrt(Math.pow(stopX - startX, 2) + Math.pow(stopY - startY, 2));
                    canvas.drawCircle(startX, startY, radius, paint);
                    break;
                case RECT:
                    canvas.drawRect(startX, startY, stopX, stopY, paint);
                    break;
            }
        }
    }
}
