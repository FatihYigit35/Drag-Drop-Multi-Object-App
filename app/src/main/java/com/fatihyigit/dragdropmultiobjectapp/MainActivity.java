package com.fatihyigit.dragdropmultiobjectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnDragListener, View.OnLongClickListener {
    private TextView textView;
    private Button button;
    private ImageView imageView;
    private LinearLayout top, left, right;
    private static final String TEXT_STICK = "TEXT";
    private static final String BUTTON_STICK = "BUTTON";
    private static final String IMAGE_STICK = "IMAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        top = findViewById(R.id.top);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);

        textView.setTag(TEXT_STICK);
        button.setTag(BUTTON_STICK);
        imageView.setTag(IMAGE_STICK);

        textView.setOnLongClickListener(this);
        button.setOnLongClickListener(this);
        imageView.setOnLongClickListener(this);

        top.setOnDragListener(this);
        left.setOnDragListener(this);
        right.setOnDragListener(this);
    }

    @Override
    public boolean onLongClick(View v) {
        ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());

        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN}; //verinin uzantısı

        ClipData clipData = new ClipData(v.getTag().toString(), mimeTypes, item);

        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v); //butonun gölgesini al

        v.startDragAndDrop(clipData, shadowBuilder, v, 0);

        v.setVisibility(View.INVISIBLE);

        return true;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()){
            case DragEvent.ACTION_DRAG_STARTED:
            case DragEvent.ACTION_DRAG_LOCATION:
                return true;
            case DragEvent.ACTION_DROP:
                v.getBackground().clearColorFilter(); //değiştirilen rengi temizler
                v.invalidate();

                View viewObject = (View) event.getLocalState();

                ViewGroup oldViewGroup = (ViewGroup) viewObject.getParent();
                oldViewGroup.removeView(viewObject);

                LinearLayout targetLayout = (LinearLayout) v;
                targetLayout.addView(viewObject);
                viewObject.setVisibility(View.VISIBLE);

                return true;
            case DragEvent.ACTION_DRAG_ENDED:
            case DragEvent.ACTION_DRAG_EXITED:
                v.getBackground().clearColorFilter(); //değiştirilen rengi temizler
                v.invalidate();
                return true;
            case DragEvent.ACTION_DRAG_ENTERED:
                v.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN); //içinde olduğu layout arka plan rengini değiştirir
                v.invalidate(); //işlemi uygular
                return true;
            default:break;
        }
        return false;
    }
}