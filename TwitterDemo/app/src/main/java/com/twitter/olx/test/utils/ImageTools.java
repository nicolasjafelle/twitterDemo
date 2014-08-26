package com.twitter.olx.test.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by iguerendiain on 23/09/13.
 */
public class ImageTools {
    public static final int CHECK_SIZE_LESS_THAN = 1;
    public static final int CHECK_SIZE_MORE_THAN = 2;
    public static final int CHECK_SIZE_EQUAL = 3;

    public static Bitmap roundCorners(Context context, Bitmap bitmap, int dimenResourceId){
        Resources r = context.getResources();
        return roundCorners(bitmap,r.getDimensionPixelSize(dimenResourceId));
    }

    public static Bitmap roundCorners(Bitmap bitmap, int pixels) {
        return roundCorners(bitmap,pixels,pixels);
    }

    public static Bitmap roundCorners(Bitmap bitmap, int pixelsX, int pixelsY) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixelsX;
        final float roundPy = pixelsY;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPy, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap cropCenter(Bitmap bitmap, float aspect){
        final int originalWidth = bitmap.getWidth();
        final int originalHeight = bitmap.getHeight();
        int sourceX = 0;
        int sourceY = 0;

        if ((float)originalWidth / (float)originalHeight == aspect){
            return bitmap;
        }else{
            int newWidth;
            int newHeight;

            // Testing for newWidth = originalWidth
            newWidth = originalWidth;
            newHeight = Math.round(newWidth * aspect);
            if (newHeight <= originalHeight){
                sourceX = 0;
                sourceY = Math.round(originalHeight*.5f - newHeight*.5f);
            }else{
                // Testing for newHeight == originalHeight
                newHeight = originalHeight;
                newWidth = Math.round(newHeight / aspect);

                if (newWidth <= originalWidth){
                    sourceX = Math.round(originalWidth*.5f - newWidth*.5f);
                    sourceY = 0;
                }else{
                    return null;
                }
            }

            return Bitmap.createBitmap(bitmap, sourceX, sourceY, newWidth, newHeight);
        }
    }

    public static boolean checkImageSize(Context context, Uri imageUri, int checkType, int width, int height){
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;

        InputStream input = null;
        try {
            input = context.getContentResolver().openInputStream(imageUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BitmapFactory.decodeStream(input, null, bitmapOptions);

        switch(checkType){
            case CHECK_SIZE_LESS_THAN:
                if (width <= bitmapOptions.outWidth && height <= bitmapOptions.outHeight){
                    return true;
                }else{
                    return false;
                }
            case CHECK_SIZE_MORE_THAN:
                if (width >= bitmapOptions.outWidth && height >= bitmapOptions.outHeight){
                    return true;
                }else{
                    return false;
                }
            case CHECK_SIZE_EQUAL:
                if (width == bitmapOptions.outWidth && height == bitmapOptions.outHeight){
                    return true;
                }else{
                    return false;
                }
        }

        return false;
    }

    public static Bitmap getThumbnail(Context context, Uri uri, int thumbnailSize) throws FileNotFoundException, IOException {
        // Img dimensions
		BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
		onlyBoundsOptions.inJustDecodeBounds = true;
		onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        InputStream input = context.getContentResolver().openInputStream(uri);
		BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
		if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
			return null;

        // Calculate image size ratio
		int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;
		double ratio = (originalSize > thumbnailSize) ? (originalSize / thumbnailSize) : 1.0;

        // Scale image
		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
		bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        input = context.getContentResolver().openInputStream(uri);
		Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        // Crop and return
		return ImageTools.cropCenter(bitmap,1);
	}

	/**
	 * Resolve the best value for inSampleSize attribute.
	 * @param ratio
	 * @return
	 */
	private static int getPowerOfTwoForSampleRatio(double ratio) {
		int k = Integer.highestOneBit((int) Math.floor(ratio));
		if (k == 0){
			return 1;
        }else{
			return k;
        }
	}

}
