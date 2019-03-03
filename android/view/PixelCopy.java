package android.view;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class PixelCopy
{
  public static final int ERROR_DESTINATION_INVALID = 5;
  public static final int ERROR_SOURCE_INVALID = 4;
  public static final int ERROR_SOURCE_NO_DATA = 3;
  public static final int ERROR_TIMEOUT = 2;
  public static final int ERROR_UNKNOWN = 1;
  public static final int SUCCESS = 0;
  
  private PixelCopy() {}
  
  public static void request(Surface paramSurface, Bitmap paramBitmap, OnPixelCopyFinishedListener paramOnPixelCopyFinishedListener, Handler paramHandler)
  {
    request(paramSurface, null, paramBitmap, paramOnPixelCopyFinishedListener, paramHandler);
  }
  
  public static void request(Surface paramSurface, Rect paramRect, Bitmap paramBitmap, OnPixelCopyFinishedListener paramOnPixelCopyFinishedListener, Handler paramHandler)
  {
    validateBitmapDest(paramBitmap);
    if (paramSurface.isValid())
    {
      if ((paramRect != null) && (paramRect.isEmpty())) {
        throw new IllegalArgumentException("sourceRect is empty");
      }
      paramHandler.post(new Runnable()
      {
        public void run()
        {
          onPixelCopyFinished(val$result);
        }
      });
      return;
    }
    throw new IllegalArgumentException("Surface isn't valid, source.isValid() == false");
  }
  
  public static void request(SurfaceView paramSurfaceView, Bitmap paramBitmap, OnPixelCopyFinishedListener paramOnPixelCopyFinishedListener, Handler paramHandler)
  {
    request(paramSurfaceView.getHolder().getSurface(), paramBitmap, paramOnPixelCopyFinishedListener, paramHandler);
  }
  
  public static void request(SurfaceView paramSurfaceView, Rect paramRect, Bitmap paramBitmap, OnPixelCopyFinishedListener paramOnPixelCopyFinishedListener, Handler paramHandler)
  {
    request(paramSurfaceView.getHolder().getSurface(), paramRect, paramBitmap, paramOnPixelCopyFinishedListener, paramHandler);
  }
  
  public static void request(Window paramWindow, Bitmap paramBitmap, OnPixelCopyFinishedListener paramOnPixelCopyFinishedListener, Handler paramHandler)
  {
    request(paramWindow, null, paramBitmap, paramOnPixelCopyFinishedListener, paramHandler);
  }
  
  public static void request(Window paramWindow, Rect paramRect, Bitmap paramBitmap, OnPixelCopyFinishedListener paramOnPixelCopyFinishedListener, Handler paramHandler)
  {
    validateBitmapDest(paramBitmap);
    if (paramWindow != null)
    {
      if (paramWindow.peekDecorView() != null)
      {
        Rect localRect = null;
        ViewRootImpl localViewRootImpl = paramWindow.peekDecorView().getViewRootImpl();
        paramWindow = localRect;
        localRect = paramRect;
        if (localViewRootImpl != null)
        {
          paramWindow = mSurface;
          localRect = mWindowAttributes.surfaceInsets;
          if (paramRect == null)
          {
            localRect = new Rect(left, top, mWidth + left, mHeight + top);
          }
          else
          {
            paramRect.offset(left, top);
            localRect = paramRect;
          }
        }
        if ((paramWindow != null) && (paramWindow.isValid()))
        {
          request(paramWindow, localRect, paramBitmap, paramOnPixelCopyFinishedListener, paramHandler);
          return;
        }
        throw new IllegalArgumentException("Window doesn't have a backing surface!");
      }
      throw new IllegalArgumentException("Only able to copy windows with decor views");
    }
    throw new IllegalArgumentException("source is null");
  }
  
  private static void validateBitmapDest(Bitmap paramBitmap)
  {
    if (paramBitmap != null)
    {
      if (!paramBitmap.isRecycled())
      {
        if (paramBitmap.isMutable()) {
          return;
        }
        throw new IllegalArgumentException("Bitmap is immutable");
      }
      throw new IllegalArgumentException("Bitmap is recycled");
    }
    throw new IllegalArgumentException("Bitmap cannot be null");
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface CopyResultStatus {}
  
  public static abstract interface OnPixelCopyFinishedListener
  {
    public abstract void onPixelCopyFinished(int paramInt);
  }
}
