package com.bignerdranch.android.criminalintent2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
 * Created by My on 1/26/2016.
 */
public class PictureUtils {
   // this method scans a photo file to see how big it is, figures out how much to scale it by to
   // fit it in a given area, then re-reads the photo file to create a scaled-down Bitmap object
   public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
      // read in the dimensions of the image on disk
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      BitmapFactory.decodeFile(path, options);
      float srcWidth = options.outWidth;
      float srcHeight = options.outHeight;

      // figure out how much to scale by
      int inSampleSize = 1;
      if (srcHeight > destHeight || srcWidth > destWidth)
         inSampleSize = Math.round(srcWidth > srcHeight ? (srcHeight / destHeight) : (srcWidth / destWidth));
      options = new BitmapFactory.Options();
      options.inSampleSize = inSampleSize;

      // read in and create final Bitmap
      return BitmapFactory.decodeFile(path, options);
   }

   // conservative scale method
   public static Bitmap getScaledBitmap(String path, Activity activity) {
      Point size = new Point();
      // check to see how big the screen is
      activity.getWindowManager().getDefaultDisplay().getSize(size);
      // scale the image down to that size
      return getScaledBitmap(path, size.x, size.y);
   }
}
