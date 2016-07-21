package com.jc.gamerocker.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.jc.gamerocker.MyRocker;


public class RockerView extends ImageView {

    private MyRocker rocker;// define custom rocker
    private float current_X, current_Y;
    private boolean isRockerCreated = false;// when rocker is created, set true,
                                            // else set false
    private onDirTouchListener listener;
    private String lastDirection = "";

    public RockerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RockerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDirListener(onDirTouchListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isRockerCreated) {
            rocker = new MyRocker(getResources(), getWidth(), getHeight());
            isRockerCreated = true;
        }
        rocker.drawSelf(canvas);
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        current_X = event.getX();
        current_Y = event.getY();

        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            rocker.begin(current_X, current_Y);
            postInvalidate();// refresh
            break;
        case MotionEvent.ACTION_MOVE:
            rocker.update(current_X, current_Y);
            postInvalidate();// refresh
            movingMethod();
            break;
        case MotionEvent.ACTION_UP:
            rocker.reset();
            postInvalidate();// refresh
            listener.stop();
            lastDirection = "";
            break;
        default:
            break;
        }
        return true;
    }

    /**
     * when ACTION_MOVE,use method to UP/DOWN/LEFT/RIGHT
     */
    private void movingMethod() {
        String dir = rocker.getRockerDirection();
        boolean state = getDirState(dir);
        if(state){
            if(dir.equals(MyRocker.DIRECTION_UP)){
                listener.up();
                
            }else if (dir.equals(MyRocker.DIRECTION_DOWN)) {
                listener.down();
                
            }else if (dir.equals(MyRocker.DIRECTION_LEFT)) {
                listener.left();
                
            }else if (dir.equals(MyRocker.DIRECTION_RIGHT)) {
                listener.right();
                
            }else {
                listener.stop();
            }
        }
    }
    /**
     * when ACTION_MOVE,use method to UP/DOWN/LEFT/RIGHT
     */
    /*private void movingMethod() {
        String dir = rocker.getRockerDirection();
        boolean state = getDirState(dir);
        switch (dir) {
        case MyRocker.DIRECTION_UP:
            if (state) {
                listener.up();
            }
            break;
        case MyRocker.DIRECTION_DOWN:
            if (state) {
                listener.down();
            }
            break;
        case MyRocker.DIRECTION_LEFT:
            if (state) {
                listener.left();
            }
            break;
        case MyRocker.DIRECTION_RIGHT:
            if (state) {
                listener.right();
            }
            break;
            
        default:
            break;
        }
    }*/

    /**
     * when your method is on use;please make sure "rocker.isAction()" is true
     * and at the same time newDirection is not same as lastDirection -->First
     * isAction = true Then isSame = false;
     * 
     * @param newDir
     * @return
     */
    private boolean getDirState(String newDir) {
        boolean isSame = lastDirection.equals(newDir);
        if (rocker.isAction()) {
            lastDirection = newDir;
            if (isSame) {
                return false;
            } else {
                listener.stop();
                return true;
            }
        } else {
            listener.stop();
            lastDirection = "";
            return false;
        }
    }

    public interface onDirTouchListener {
        public void up();

        public void down();

        public void left();

        public void right();

        public void stop();
    }
}
