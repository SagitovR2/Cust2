package com.example.cust;

import android.content.Context;
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

public class ViewOneAquaDraw extends View {

    Bitmap bground;
    Bitmap bgroundOrig;
    Matrix bground_sizes;

    int bgroundWidth;
    int bgroundHeight;


    private ArrayList<Sprite> fishSprites;

    private int viewWidth;
    private int viewHeight;

    private int points = 0;

    private final int timerInterval = 30;

    private ArrayList<String> countOfFishes;

    private boolean added = false;

    Rect firstFrame;
    Bitmap b;
    private int w, h;

    public ViewOneAquaDraw(Context context, AttributeSet attrs) {
        super(context, attrs);

        bgroundOrig = BitmapFactory.decodeResource(getResources(), R.drawable.bground_aqua);
        bground_sizes = new Matrix();
        bground_sizes.setScale(500, 500);
        int[] rCoords = getRandomCoords(1);
        System.out.println(rCoords[1]);



        Timer t = new Timer();
        t.start();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewWidth = w;
        viewHeight = h;


    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!added) {
            fishSprites = new ArrayList<>();
            for (String i: countOfFishes) {
                Bitmap b = getFishBitmap(i.split(":")[0]);
                int w = b.getWidth()/1;
                int h = b.getHeight()/1;
                Rect firstFrame = new Rect(0, 0, w, h);
                for (int j = 0; j < Integer.parseInt(i.split(":")[1]); j++) {
                    int[] rCoords = getRandomCoords(1);
                    fishSprites.add(new Sprite(rCoords[0], rCoords[1], 200, 0, firstFrame, b));
                }
            }
            added = true;
        }
        super.onDraw(canvas);
        canvas.drawARGB(250, 127, 199, 255);
        bgroundWidth = getWidth();
        bgroundHeight = getHeight();
        bground = Bitmap.createScaledBitmap(bgroundOrig, getWidth(), getHeight(), true);

        Paint p = new Paint();
        canvas.drawBitmap(bground, 0, 0, p);
        for (int i = 0; i < fishSprites.size(); i++) {
            fishSprites.get(i).draw(canvas);
        }
        p.setAntiAlias(true);
        p.setColor(Color.WHITE);
        p.setStrokeWidth(23);
    }

    protected void update () {
        if (!added) {
            fishSprites = new ArrayList<>();
            ArrayList<Sprite> firstAqua = new ArrayList<>();
            for (String i: countOfFishes) {
                Bitmap b = getFishBitmap(i.split(":")[0]);
                int w = b.getWidth()/1;
                int h = b.getHeight()/1;
                Rect firstFrame = new Rect(0, 0, w, h);
                for (int j = 0; j < Integer.parseInt(i.split(":")[1]); j++) {
                    int[] rCoords = getRandomCoords(1);
                    fishSprites.add(new Sprite(rCoords[0], rCoords[1], 200, 0, firstFrame, b));
                }
            }
            added = true;
        }
        for (int i = 0; i < fishSprites.size(); i++) {
            fishSprites.get(i).update(timerInterval);

            if (fishSprites.get(i).getX() + fishSprites.get(i).getFrameWidth() > viewWidth) {
                fishSprites.get(i).setX(viewWidth - fishSprites.get(i).getFrameWidth());
                fishSprites.get(i).setVx(-fishSprites.get(i).getVx());
                fishSprites.get(i).mirrorBitmap();
            } else if (fishSprites.get(i).getX() < 0) {
                fishSprites.get(i).setX(0);
                fishSprites.get(i).setVx(-fishSprites.get(i).getVx());
                fishSprites.get(i).mirrorBitmap();
            }
        }


        invalidate();
    }

    public int[] getRandomCoords(int aquaNumber) {
        int[] a;
        if (aquaNumber == 1) {
            a = new int[]{
                    (int) (Math.random() * viewWidth),
                    (int) (Math.random() * viewHeight)
            };
        } else {
            a = new int[]{
                    (int) (Math.random() * viewWidth),
                    (int) ((Math.random() * viewHeight / 2) + viewHeight / 2)
            };
        }
        return a;
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

    public void setCountOfFishes() {
        this.countOfFishes = new ArrayList<>();
    }

    public void addToCountOfFishes(String s) {
        countOfFishes.add(s);
    }
}
