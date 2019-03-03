package com.android.internal.widget;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;

public class LocalImageResolver
{
  private static final int MAX_SAFE_ICON_SIZE_PX = 480;
  
  public LocalImageResolver() {}
  
  private static BitmapFactory.Options getBoundsOptionsForImage(Uri paramUri, Context paramContext)
    throws IOException
  {
    paramUri = paramContext.getContentResolver().openInputStream(paramUri);
    paramContext = new BitmapFactory.Options();
    inJustDecodeBounds = true;
    BitmapFactory.decodeStream(paramUri, null, paramContext);
    paramUri.close();
    return paramContext;
  }
  
  private static int getPowerOfTwoForSampleRatio(double paramDouble)
  {
    return Math.max(1, Integer.highestOneBit((int)Math.floor(paramDouble)));
  }
  
  public static Drawable resolveImage(Uri paramUri, Context paramContext)
    throws IOException
  {
    Object localObject = getBoundsOptionsForImage(paramUri, paramContext);
    if ((outWidth != -1) && (outHeight != -1))
    {
      int i;
      if (outHeight > outWidth) {
        i = outHeight;
      } else {
        i = outWidth;
      }
      double d;
      if (i > 480) {
        d = i / 480;
      } else {
        d = 1.0D;
      }
      localObject = new BitmapFactory.Options();
      inSampleSize = getPowerOfTwoForSampleRatio(d);
      paramUri = paramContext.getContentResolver().openInputStream(paramUri);
      localObject = BitmapFactory.decodeStream(paramUri, null, (BitmapFactory.Options)localObject);
      paramUri.close();
      return new BitmapDrawable(paramContext.getResources(), (Bitmap)localObject);
    }
    return null;
  }
}
