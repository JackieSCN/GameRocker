package com.jc.gamerocker;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jc.gamerocker.R;

public class MyRocker {
    private static final float MID_VALUE = 0.75f;
    public static final String DIRECTION_LEFT = "left";
    public static final String DIRECTION_RIGHT = "right";
    public static final String DIRECTION_UP = "up";
    public static final String DIRECTION_DOWN = "down";

    float width;// screen width
    float height;// screen height
    private Paint paint;
    private float x_position, y_position;// position percentage of the screen
                                         // value:0.0~1.0
    private final float OuterCenter_X, OuterCenter_Y;// position coordinates of
                                                     // outer circle center
    private float InnerCenter_X, InnerCenter_Y;// real position coordinates of
                                               // inner circle center
    private float InnerPosition_X, InnerPosition_Y;// shown position coordinates
                                                   // of inner circle center
    private float Outer_R, Inner_R;// radius of outer and inner circle
    private boolean isRockerTouched = false;// when the rocker is touched at
                                            // ACTION_DOWN, set true
    private float degree = 0;// the degree between current position and center
                             // position
    private Bitmap bmp1;
    private boolean isAction = false;

    /**
     * <b>This constructor is for SufaceView!</b>
     * 
     * @param ScreenWidth
     *            screen width
     * @param ScreenHeight
     *            screen height
     * @param x_position
     *            position percentage of the screen value:0.0~1.0f defualt:0.5f
     * @param y_position
     *            position percentage of the screen value:0.0~1.0f default:0.5f
     * @param R
     *            radius
     */
    public MyRocker(int ScreenWidth, int ScreenHeight, float x_position,
            float y_position, float R) {
        this.width = ScreenWidth;
        this.height = ScreenHeight;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setAlpha(0x88);
        // if the x_position and y_position is between 0.0~1.0f
        if (x_position > 0 && x_position < 1 && y_position > 0
                && y_position < 1) {
            this.x_position = x_position;
            this.y_position = y_position;
        } else {// default 0.5f
            this.x_position = 0.5f;
            this.y_position = 0.5f;
        }
        // radius
        this.Outer_R = R;
        this.Inner_R = R * 0.618f;
        // calculate the positions of outer and inner circle center
        this.OuterCenter_X = this.width * this.x_position;
        this.OuterCenter_Y = this.height * this.y_position;
        this.InnerCenter_X = OuterCenter_X;
        this.InnerCenter_Y = OuterCenter_Y;
        this.InnerPosition_X = InnerCenter_X;
        this.InnerPosition_Y = InnerCenter_Y;
    }

    /**
     * This constructor is for ImageView!<br/>
     * When this View is added in the xml, the width and height <b>CANNOT</b> be
     * set as <b>"WARP_CONTENT"</b> <b>Caution： width == height</b>
     * 
     * @param width
     *            the width of ImageView
     * @param height
     *            the height of ImageView
     */
    public MyRocker(Resources res, int width, int height) {
        super();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setAlpha(0x88);
        // set width and height
        this.width = width;
        this.height = height;
        // set radius
        this.Outer_R = width / (2 * (1 + 0.7f));
        this.Inner_R = this.Outer_R * 0.7f;
        initBmp(res, width, height);
        // calculate the positions of outer and inner circle center
        this.OuterCenter_X = this.width / 2;
        this.OuterCenter_Y = this.height / 2;
        this.InnerCenter_X = OuterCenter_X;
        this.InnerCenter_Y = OuterCenter_Y;
        this.InnerPosition_X = InnerCenter_X;
        this.InnerPosition_Y = InnerCenter_Y;
    }

    private void initBmp(Resources res, int width, int height) {
        bmp1 = BitmapFactory.decodeResource(res, R.drawable.directional_control);
        bmp1 = Bitmap.createScaledBitmap(bmp1, width, height, false);
    }

    public void drawSelf(Canvas canvas) {
        canvas.drawBitmap(bmp1, 0, 0, null);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(InnerPosition_X, InnerPosition_Y, Inner_R, paint);
    }

    /**
     * Call this method when ACTION_DOWN
     * 
     * @param x
     * @param y
     */
    public void begin(float x, float y) {
        // if touch position is inside the outer circle area
        float distance = distance(x, y, OuterCenter_X, OuterCenter_Y);
        if (distance < Outer_R) {
            InnerCenter_X = x;
            InnerCenter_Y = y;
            isRockerTouched = true;
        }
        // calculate the degree
        this.degree = (float) Math.atan((x - OuterCenter_X)
                / (y - OuterCenter_Y));
    }

    /**
     * Call this method when ACTION_MOVE
     * 
     * @param x
     * @param y
     */
    public void update(float x, float y) {
        if (isRockerTouched) {// the rocker can be moved only when it is touched
            float distance = distance(x, y, OuterCenter_X, OuterCenter_Y);
            isAction = getActionState(distance);
            // if the touch position is inside the outer circle area
            if (distance < Outer_R) {
                InnerCenter_X = x;
                InnerCenter_Y = y;
                isRockerTouched = true;
            } else {// outside the outer circle area
                    // calculate the degree
                this.degree = (float) Math.atan((x - OuterCenter_X)
                        / (y - OuterCenter_Y));
                if (y < OuterCenter_Y) {
                    InnerCenter_X = OuterCenter_X
                            + (float) (Outer_R * -Math.sin(this.degree));
                    InnerCenter_Y = OuterCenter_Y
                            + (float) (Outer_R * -Math.cos(this.degree));
                } else {
                    InnerCenter_X = OuterCenter_X
                            + (float) (Outer_R * Math.sin(this.degree));
                    InnerCenter_Y = OuterCenter_Y
                            + (float) (Outer_R * Math.cos(this.degree));
                }
            }
            getNewPosition(getRockerDirection());
        }
    }

    private boolean getActionState(float distance){
        float d = Outer_R;
        if(distance>d){
            return true;
        }else {
            return false;
        }
    }
    public String getRockerDirection() {
        String dir = "";
        if (InnerCenter_X > OuterCenter_X) {
            if (Math.abs(degree) > MID_VALUE) {
                dir = DIRECTION_RIGHT;
            } else{
                if (degree > 0) {
                    dir = DIRECTION_DOWN;
                } else {
                    dir = DIRECTION_UP;
                }
            }
        } else {
            if (Math.abs(degree) > MID_VALUE) {
                dir = DIRECTION_LEFT;
            } else{
                if (degree > 0) {
                    dir = DIRECTION_UP;
                } else {
                    dir = DIRECTION_DOWN;
                }
            }
        }
        return dir;
    }

    private void getNewPosition(String dir) {
        if (dir.equals(DIRECTION_UP) || dir.equals(DIRECTION_DOWN)) {
            InnerPosition_X = OuterCenter_X;
            InnerPosition_Y = InnerCenter_Y;
        } else if (dir.equals(DIRECTION_LEFT) || dir.equals(DIRECTION_RIGHT)) {
            InnerPosition_X = InnerCenter_X;
            InnerPosition_Y = OuterCenter_Y;
        }else {
            InnerPosition_X = OuterCenter_X;
            InnerPosition_Y = OuterCenter_Y;
        }
    }
    /*private void getNewPosition(String dir) {
        switch (dir) {
        case DIRECTION_UP:
        case DIRECTION_DOWN:
            InnerPosition_X = OuterCenter_X;
            InnerPosition_Y = InnerCenter_Y;
            break;
        case DIRECTION_LEFT:
        case DIRECTION_RIGHT:
            InnerPosition_X = InnerCenter_X;
            InnerPosition_Y = OuterCenter_Y;
            break;

        default:
            break;
        }
    }*/

    /**
     * Call this method when ACTION_UP
     */
    public void reset() {
        InnerCenter_X = OuterCenter_X;
        InnerCenter_Y = OuterCenter_Y;
        InnerPosition_X = OuterCenter_X;
        InnerPosition_Y = OuterCenter_Y;
        isRockerTouched = false;
        isAction = false;
        this.degree = 0;
    }

    /**
     * Calculate the distance between two dots
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return distance
     */
    private float distance(float x1, float y1, float x2, float y2) {
        float distance = (float) Math.sqrt((float) Math.pow(x1 - x2, 2)
                + (float) Math.pow(y1 - y2, 2));
        return distance;
    }

    /**
     * get the degree between current position and center position
     * 
     * @return
     */
    public float getDegree() {
        return degree;
    }
    public boolean isAction() {
        return isAction;
    }

}
