package com.example.cust;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import android.os.CountDownTimer;

import java.util.ArrayList;

public class ViewAquasDraw extends View {

    private static final String url = "jdbc:mysql://dvaken.beget.tech/dvaken_aquarium";
    private static final String user = "dvaken_aquarium";
    private static final String password = "Nekochan123";

    private ArrayList<ArrayList<Sprite>> fishSprites;

    private boolean listed = false;

    private Sprite fish;

    String aquaName1 = "";
    String aquaName2 = "";

    Bitmap bground;
    Bitmap bgroundOrig;
    Matrix bground_sizes;

    int bgroundWidth;
    int bgroundHeight;

    private int viewWidth;
    private int viewHeight;

    private int points = 0;

    private final int timerInterval = 30;
    private ArrayList<String> countOfFishes1;
    private ArrayList<String> countOfFishes2;
    Matrix m = new Matrix();


    public ViewAquasDraw(Context context, AttributeSet attrs) {
        super(context, attrs);

        countOfFishes1 = new ArrayList<>();
        countOfFishes2 = new ArrayList<>();

        bgroundOrig = BitmapFactory.decodeResource(getResources(), R.drawable.bground_aqua);
        bground_sizes = new Matrix();
        bground_sizes.setScale(500, 500);


        /*fish = new Sprite(10, 400, 200, 0, firstFrame, b);

        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 1; j++) {
                if (i ==0 && j == 0) {
                    continue;
                }
                if (i ==2 && j == 3) {
                    continue;
                }
                fish.addFrame(new Rect(j*w, i*h, j*w+w, i*w+w));
            }
        }*/



        Timer t = new Timer();
        t.start();
        ArrayList<Sprite> firstAqua = new ArrayList<>();
        for (String i: countOfFishes1) {
            Bitmap b = getFishBitmap(i.split(":")[0]);
            int w = b.getWidth()/1;
            int h = b.getHeight()/1;
            Rect firstFrame = new Rect(0, 0, w, h);
            for (int j = 0; j < Integer.parseInt(i.split(":")[1]); j++) {
                int[] rCoords = getRandomCoords(1);
                firstAqua.add(new Sprite(rCoords[0], rCoords[1], 200, 0, firstFrame, b));
            }
        }
        ArrayList<Sprite> secondAqua = new ArrayList<>();
        for (String i: countOfFishes2) {
            int[] rCoords = getRandomCoords(2);
            Bitmap b = getFishBitmap(i.split(":")[0]);
            int w = b.getWidth() / 1;
            int h = b.getHeight() / 1;
            Rect firstFrame = new Rect(0, 0, w, h);
            for (int j = 0; j < Integer.parseInt(i.split(":")[1]); j++) {
                rCoords = getRandomCoords(2);
                secondAqua.add(new Sprite(rCoords[0], rCoords[1], 200, 0, firstFrame, b));
            }
        }

        fishSprites = new ArrayList<>();
        fishSprites.add(firstAqua);
        fishSprites.add(secondAqua);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewWidth = w;
        viewHeight = h;


    }

    @Override
    protected void onDraw(Canvas canvas) {
        /*if (!listed) {
            listed = true;
            listing();
        }*/
        super.onDraw(canvas);
        canvas.drawARGB(250, 127, 199, 255);
        bgroundWidth = getWidth();
        bgroundHeight = getHeight();
        bground = Bitmap.createScaledBitmap(bgroundOrig, getWidth(), getHeight() / 2, true);

        Paint p = new Paint();
        canvas.drawBitmap(bground, 0, 0, p);
        canvas.drawBitmap(bground, 0, viewHeight / 2f, p);
        for (int i = 0; i < fishSprites.size(); i++) {
            for (Sprite j : fishSprites.get(i)) {
                j.draw(canvas);
            }
        }
        p.setAntiAlias(true);
        p.setColor(Color.WHITE);
        p.setStrokeWidth(23);
        canvas.drawLine(0, viewHeight / 2f, viewWidth, viewHeight / 2f, p);

        p.setColor(Color.WHITE);
        p.setTextSize(100);

        // Aquarium's names
        canvas.drawText(aquaName1, 10, 125, p);
        canvas.drawText(aquaName2, 10, viewHeight / 2f + 125, p);
    }

    protected void listing() {



        /*int countOfFishes1 = Integer.parseInt(aquas[0][6]);
        int countOfFishes2 = Integer.parseInt(aquas[1][6]);*/
        ArrayList<Sprite> firstAqua = new ArrayList<>();
        for (String i: countOfFishes1) {
            Bitmap b = getFishBitmap(i.split(":")[0]);
            int w = b.getWidth()/1;
            int h = b.getHeight()/1;
            Rect firstFrame = new Rect(0, 0, w, h);
            for (int j = 0; j < Integer.parseInt(i.split(":")[1]); j++) {
                int[] rCoords = getRandomCoords(1);
                firstAqua.add(new Sprite(rCoords[0], rCoords[1], 200, 0, firstFrame, b));
            }
        }
        ArrayList<Sprite> secondAqua = new ArrayList<>();
        for (String i: countOfFishes2) {
            int[] rCoords = getRandomCoords(2);
            Bitmap b = getFishBitmap(i.split(":")[0]);
            int w = b.getWidth() / 1;
            int h = b.getHeight() / 1;
            Rect firstFrame = new Rect(0, 0, w, h);
            for (int j = 0; j < Integer.parseInt(i.split(":")[1]); j++) {
                rCoords = getRandomCoords(2);
                secondAqua.add(new Sprite(rCoords[0], rCoords[1], 200, 0, firstFrame, b));
            }
        }
        fishSprites = new ArrayList<>();
        fishSprites.add(firstAqua);
        fishSprites.add(secondAqua);
    }

    public int[] getRandomCoords(int aquaNumber) {
        int[] a;
        if (aquaNumber == 1) {
            a = new int[]{
                    (int) (Math.random() * (viewWidth)),
                    (int) (Math.random() * (viewHeight / 2))
            };
        } else {
            a = new int[]{
                    (int) (Math.random() * viewWidth),
                    (int) ((Math.random() * (viewHeight / 2)) + viewHeight / 2)
            };
        }
        return a;
    }

    protected void update () {
        if (!listed) {
            listed = true;
            listing();
        }
        for (int j= 0; j < fishSprites.size(); j++) {
            for (Sprite i : fishSprites.get(j)) {
                i.update(timerInterval);

                if (i.getX() + i.getFrameWidth() > viewWidth) {
                    i.setX(viewWidth - i.getFrameWidth());
                    i.setVx(-i.getVx());
                    i.mirrorBitmap();
                } else if (i.getX() < 0) {
                    i.setX(0);
                    i.setVx(-i.getVx());
                    i.mirrorBitmap();
                }
            }
        }


        invalidate();
    }

    public void addToCountOfFishes1(String fishes) {
        countOfFishes1.add(fishes);
    }

    public void addToCountOfFishes2(String fishes) {
        countOfFishes2.add(fishes);
    }

    public void setCountOfFishes1() {
        this.countOfFishes1 = new ArrayList<>();
    }

    public void setCountOfFishes2() {
        this.countOfFishes2 = new ArrayList<>();
    }

    class Timer extends CountDownTimer {

        public Timer() {
            super(Integer.MAX_VALUE, timerInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            update ();
        }

        @Override
        public void onFinish() {

        }
    }

    private Bitmap getFishBitmap(String id) {
        if (id.equals("1")) {
            Bitmap bOrig = BitmapFactory.decodeResource(getResources(), R.drawable.fish);
            Bitmap b = Bitmap.createScaledBitmap(bOrig, 70, 50, true);
            return b;
        }
        else {
            Bitmap bOrig = BitmapFactory.decodeResource(getResources(), R.drawable.neon);
            Bitmap b = Bitmap.createScaledBitmap(bOrig, 90, 50, true);
            return b;
        }
    }
}
