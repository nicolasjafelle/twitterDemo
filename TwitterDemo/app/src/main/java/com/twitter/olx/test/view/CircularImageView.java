package com.twitter.olx.test.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.twitter.olx.test.utils.ImageTools;

/**
 * Created by nicolas on 9/24/13.
 */
public class CircularImageView extends ImageView {

    private int borderWidth = 15;
    private int viewWidth;
    private int viewHeight;
    private Paint paint;
    private Paint paintBorder;
    private BitmapShader shader;


    protected Bitmap roundBitmap;

    public CircularImageView(Context context) {
        super(context);
        setup();
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }

    private void setup() {
        // init paint
        paint = new Paint();
        paint.setAntiAlias(true);

        paintBorder = new Paint();
        paintBorder.setColor(Color.WHITE);
        paintBorder.setAntiAlias(true);
    }

    @Override
    protected void onDraw(final Canvas canvas) {

        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        if (b != null) {
            Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

            shader = createBitmapShader(bitmap);
            paint.setShader(shader);
            int circleCenter = viewWidth / 2;

            // circleCenter is the x or y of the view's center
            // radius is the radius in pixels of the cirle to be drawn
            // paint contains the shader that will texture the shape
            canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, circleCenter + borderWidth, paintBorder);
            canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, circleCenter, paint);

            bitmap.recycle();
            bitmap = null;
        }


    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (roundBitmap != null && !roundBitmap.isRecycled()) {
            roundBitmap.recycle();
            roundBitmap = null;
        }
    }

    private BitmapShader createBitmapShader(Bitmap bitmap) {
        int w = getWidth(), h = getHeight();

        Bitmap croppedBitmap;
        if (bitmap.getWidth() != w || bitmap.getHeight() != w) {

            croppedBitmap = ImageTools.cropCenter(bitmap, 1);
            roundBitmap = Bitmap.createScaledBitmap(croppedBitmap, w, w, false);
            croppedBitmap.recycle();
            croppedBitmap = null;
        }else {
            roundBitmap = bitmap;
        }

        return new BitmapShader(roundBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec, widthMeasureSpec);

        viewWidth = width - (borderWidth * 2);
        viewHeight = height - (borderWidth * 2);

        setMeasuredDimension(width, height);
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            result = viewWidth;

        }

        return result;
    }

    private int measureHeight(int measureSpecHeight, int measureSpecWidth) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpecHeight);
        int specSize = MeasureSpec.getSize(measureSpecHeight);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = viewHeight;
        }
        return result;
    }

}
