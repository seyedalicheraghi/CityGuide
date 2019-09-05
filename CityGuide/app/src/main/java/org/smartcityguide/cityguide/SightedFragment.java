package org.smartcityguide.cityguide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Locale;

@SuppressLint("ValidFragment")
public class SightedFragment extends Fragment {

    int pointToDrawX = -10;int pointToDrawY = -10;
    int difX = 0,difY = 0;
    boolean moveFlag=false;
    int x=0,y=0;
    String[] path;
    @SuppressLint("StaticFieldLeak")
    private static View view;
    private TextToSpeech textToSpeech=null;
    private int xy[][];
    private String extraInformationToSay;
    int sweepAngle = 0;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = new Layout(getContext());
        }

        final GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    textToSpeech.speak(extraInformationToSay, TextToSpeech.QUEUE_FLUSH, null, null);
                } else {
                    textToSpeech.speak(extraInformationToSay, TextToSpeech.QUEUE_FLUSH, null);
                }
                return true;
            }
        };

        final GestureDetector detector = new GestureDetector(listener);

        detector.setOnDoubleTapListener(listener);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return detector.onTouchEvent(motionEvent);
            }
        });

        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(getContext(), "Text to Speech not Supported!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return view;
    }

    class Layout extends View {

        Paint blue_paintbrush_fill, red_paintbrush_stroke, beam_paintbrush_fill;
        RectF oval;
        Shader gradient;
        ScaleGestureDetector SGD;
        Bitmap scaledBitmap;
        Path LineToDraw;
        Float scale = 1f;
        int imgHeight;
        int imgWidth;
        float xMy = 0f,yMy = 0f;
        float strX = 0f,strY = 0f;
        float endX = 0f, endY= 0f;


        public Layout(Context context) {
            super(context);
            setBackgroundResource(R.color.colorWhite);
            scaledBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.flrrr);
            SGD = new ScaleGestureDetector(getContext(), new Layout.ScaleListener());
            setWillNotDraw(false);
        }

        private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scale = scale * detector.getScaleFactor();
                scale = Math.max(0.1f,Math.min(scale,5f));
                if(scale<1)
                    scale=1f;
                return true;
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            SGD.onTouchEvent(event);

            xMy = event.getRawX();
            yMy = (event.getRawY()-getStatusBarHeight());

            int action = MotionEventCompat.getActionMasked(event);

            switch(action) {
                case (MotionEvent.ACTION_DOWN) :
                    strX = event.getRawX();
                    strY = event.getRawY();
                    break;
                case (MotionEvent.ACTION_UP) :
                    endX = event.getRawX();
                    endY = event.getRawY();
                    break;
            }
            if(strX!=0f && strY != 0f && endX != 0f && endY != 0f){
                difX = Math.round((strX-endX)/6);
                difY = Math.round((strY-endY)/6);
                moveFlag = true;
                strX = 0f;
                strY = 0f;
                endX = 0f;
                endY= 0f;

            }

            return true;
        }

        @SuppressLint("DrawAllocation")
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            getImageDimention();
            if(moveFlag){
                x = x - difX;
                y = y - difY;
                moveFlag = false;
            }

            canvas.drawBitmap(Bitmap.createScaledBitmap(scaledBitmap, Math.round(imgWidth*scale),Math.round((imgHeight-getStatusBarHeight())*scale), false),x*scale,y*scale,null);

            drawingPath(canvas,x,y);
            invalidate();
        }

        private void drawingPath(Canvas canvas,int x,int y) {
            red_paintbrush_stroke = new Paint();
            red_paintbrush_stroke.setColor(Color.RED);
            red_paintbrush_stroke.setStrokeWidth(15);
            red_paintbrush_stroke.setStyle(Paint.Style.STROKE);
            blue_paintbrush_fill = new Paint();
            blue_paintbrush_fill.setColor(Color.BLUE);
            blue_paintbrush_fill.setStyle(Paint.Style.FILL);

            beam_paintbrush_fill = new Paint();
            beam_paintbrush_fill.setAntiAlias(true);
            beam_paintbrush_fill.setStyle(Paint.Style.STROKE);
            beam_paintbrush_fill.setStrokeWidth(80*scale);
            beam_paintbrush_fill.setColor(0xFF3264CC);

            oval = new RectF();
            gradient = new SweepGradient(0,getMeasuredHeight()/2, Color.BLUE, Color.WHITE);

            LineToDraw = new Path();
            if(path.length>1){
                for(int i=1;i<path.length;i++){
                    LineToDraw.moveTo((x + xy[Integer.valueOf(path[i-1])][0]) * scale,(y + xy[Integer.valueOf(path[i-1])][1]) * scale);
                    LineToDraw.lineTo((x + xy[Integer.valueOf(path[i])][0]) * scale,(y + xy[Integer.valueOf(path[i])][1]) * scale);
                }
                canvas.drawPath(LineToDraw,red_paintbrush_stroke);
                for(int i=0;i<path.length-1;i++){
                    canvas.drawCircle((xy[Integer.valueOf(path[0])][0] + x) * scale, (xy[Integer.valueOf(path[0])][1] + y) * scale, 10 * scale, blue_paintbrush_fill);
                    canvas.drawCircle((xy[Integer.valueOf(path[0])][0] + x) * scale, (xy[Integer.valueOf(path[0])][1] + y) * scale, 10 * scale, blue_paintbrush_fill);

                }
            }else {

                canvas.drawCircle((pointToDrawX + x) * scale, (pointToDrawY + y) * scale, 10 * scale, blue_paintbrush_fill);
                canvas.drawCircle((pointToDrawX + x) * scale, (pointToDrawY + y) * scale, 10 * scale, blue_paintbrush_fill);
            }
            oval.set((((pointToDrawX + x))) * scale - (50 * scale), (((pointToDrawY + y))) * scale - (50 * scale),
                    (((pointToDrawX + x))) * scale + (50 * scale),(((pointToDrawY + y))) * scale + (50 * scale));
            beam_paintbrush_fill.setShader(gradient);
            canvas.drawArc(oval, sweepAngle, 45, false, beam_paintbrush_fill);

        }

        public int getStatusBarHeight() {
            int result = 0;
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = getResources().getDimensionPixelSize(resourceId);
            }
            return result;
        }

        public void getImageDimention(){
            BitmapFactory.Options dimensions = new BitmapFactory.Options();
            dimensions.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(getResources(),R.drawable.flrrr,dimensions);
            imgHeight = dimensions.outHeight;
            imgWidth =  dimensions.outWidth;
        }

    }

    public void xyToDraw(int nodeNumber, int xy[][], String extraInformationToSay, String getRouteSrcDst){
        this.xy = xy;
        this.extraInformationToSay = extraInformationToSay;
        pointToDrawX = xy[nodeNumber][0];
        pointToDrawY = xy[nodeNumber][1];
        path = getRouteSrcDst.split(",");
    }
    public void beamAngle(String where[]){
        sweepAngle = Integer.valueOf(where[1])+165;
        if(sweepAngle>360)
            sweepAngle = sweepAngle-360;
    }
}
