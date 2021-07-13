package com.example.cookieclicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    ConstraintLayout layout;
    ImageView icecream, scoop, shop;
    TextView count, scoopFunds, shopFunds;
    int numClicks, funds, funds2;
    float rand, horiz, vert, horiz2;
    boolean ifClicked, ifClicked2, firstUpgrade, secondUpgrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        icecream = findViewById(R.id.id_icecream);
        scoop = findViewById(R.id.id_scoop);
        shop = findViewById(R.id.id_shop);
        count = findViewById(R.id.id_count);
        scoopFunds = findViewById(R.id.id_scoopText);
        shopFunds = findViewById(R.id.id_shopText);
        layout = findViewById(R.id.id_layout);


        Timer timer = new Timer();
        funds = 10;
        funds2 = 50;
        ifClicked = false;
        ifClicked2 = false;
        firstUpgrade = false;
        secondUpgrade = false;


        scoopFunds.setText(funds + " scoops");
        shopFunds.setText(funds2 + " scoops");
        scoop.setEnabled(false);
        shop.setEnabled(false);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (ifClicked) {
                            upgrade();
                        }

                        else if (ifClicked2) {
                            upgrade2();
                        }
                        check();
                        check2();
                    }
                });

            }
        }, 0, 3000);


        icecream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numClicks++;
                ClickAnimation(icecream);
                count.setText(numClicks + " scoops");

                TextView plusOne = new TextView(MainActivity.this);
                plusOne.setId(View.generateViewId());
                plusOne.setText("+1");
                plusOne.setTextSize(18f);

                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                plusOne.setLayoutParams(params);

                layout.addView(plusOne);

                plusOne.setAlpha(0.5f);
                plusOne.animate()
                        .alpha(0f)
                        .setDuration(800)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                plusOne.setVisibility(View.GONE);
                                layout.removeView(plusOne);
                            }
                        });

                Log.d("TAG_", "Number of Views: "+ layout.getChildCount());

                TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -10.0f);
                animation.setDuration(5000);
                plusOne.startAnimation(animation);

                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(layout);

                constraintSet.connect(plusOne.getId(), ConstraintSet.RIGHT, layout.getId(), ConstraintSet.RIGHT);
                constraintSet.connect(plusOne.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT);
                constraintSet.connect(plusOne.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP);
                constraintSet.connect(plusOne.getId(), ConstraintSet.TOP, icecream.getId(), ConstraintSet.TOP);

                double rand = (float) (Math.random() * 0.4) + 0.3;
                constraintSet.setVerticalBias(plusOne.getId(), (float) (rand));
                constraintSet.setHorizontalBias(plusOne.getId(), (float) (rand));

                constraintSet.applyTo(layout);
            }
        });

        scoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ifClicked = true;
                moveDown(scoop, R.drawable.cutlery, 0);
                decrease(funds);
                count.setText(numClicks + " scoops");
                increaseFunds();
                check();
            }
        });

        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ifClicked2 = true;
                moveDown(shop, R.drawable.icecreamshop, 100);
                decrease(funds2);
                count.setText(numClicks + " scoops");
                increaseFunds2();
            }
        });
    }

    public void upgrade() {
        increaseCount(1);
        count.setText(numClicks + " scoops");
        check();
        ClickAnimation(icecream);
    }

    public void upgrade2() {
        increaseCount(10);
        count.setText(numClicks + " scoops");
        check2();
        ClickAnimation(icecream);
    }

    public synchronized void increaseCount(int addition) {
        numClicks += addition;
    }

    public synchronized void decrease(int subtract) {
        numClicks -= subtract;
    }

    public void setDisabled(ImageView imageView) {
        final ColorMatrix grayscaleMatrix = new ColorMatrix();
        grayscaleMatrix.setSaturation(0);
        imageView.setColorFilter(new ColorMatrixColorFilter(grayscaleMatrix));
    }

    public void setEnabled(ImageView imageView) {
        final ColorMatrix grayscaleMatrix = new ColorMatrix();
        grayscaleMatrix.reset();
        imageView.setColorFilter(new ColorMatrixColorFilter(grayscaleMatrix));
        imageView.setColorFilter(new ColorMatrixColorFilter(grayscaleMatrix));
    }

    public void moveDown(ImageView iv, int image, int xVal) {
        iv = new ImageView(MainActivity.this);
        iv.setId(View.generateViewId());
        iv.setImageResource(image);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        iv.setLayoutParams(params);

        layout.addView(iv);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);

        constraintSet.connect(iv.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP);
        constraintSet.connect(iv.getId(), ConstraintSet.BOTTOM, layout.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(iv.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT);
        constraintSet.connect(iv.getId(), ConstraintSet.RIGHT, layout.getId(), ConstraintSet.RIGHT);

        horiz = (float)0.05;
        vert = (float)0.8;
        horiz2 = (float)0.9;
        if (ifClicked)
        {
            constraintSet.setVerticalBias(iv.getId(), 0.93f);
            constraintSet.setHorizontalBias(iv.getId(), 0.05f);
        }

        if(ifClicked2)
        {
            constraintSet.setVerticalBias(iv.getId(), 0.93f);
            constraintSet.setHorizontalBias(iv.getId(), 0.9f);
        }

        constraintSet.applyTo(layout);

        TranslateAnimation translateAnimation = new TranslateAnimation(xVal, xVal, 0, 900);
        translateAnimation.setDuration(1000);
        translateAnimation.setFillAfter(true);
        iv.startAnimation(translateAnimation);


        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);

        ImageView finalIv = iv;
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finalIv.startAnimation(rotate);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv.startAnimation(translateAnimation);
    }

    public void ClickAnimation(ImageView iv) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.90f, 1.0f, 0.90f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);
        iv.startAnimation(scaleAnimation);
    }

    public void increaseFunds() {
        funds = funds + 5;
        scoopFunds.setText(funds + " scoops");
    }

    public void increaseFunds2() {
        funds2 = funds2 + 10;
        shopFunds.setText(funds2 + " scoops");
    }

    public void check() {
            if (numClicks >= funds) {
                scoop.setEnabled(true);
                setEnabled(scoop);
            }
            else if (numClicks < funds){
                scoop.setEnabled(false);
                setDisabled(scoop);
            }
    }

    public void check2 ()
    {
        if (numClicks >= funds2) {
            shop.setEnabled(true);
            setEnabled(shop);

        } else {
            shop.setEnabled(false);
            setDisabled(shop);
        }
    }
}



