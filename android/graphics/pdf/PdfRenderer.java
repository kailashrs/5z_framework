package android.graphics.pdf;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.ParcelFileDescriptor;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructStat;
import com.android.internal.util.Preconditions;
import dalvik.system.CloseGuard;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import libcore.io.IoUtils;

public final class PdfRenderer
  implements AutoCloseable
{
  static final Object sPdfiumLock = new Object();
  private final CloseGuard mCloseGuard = CloseGuard.get();
  private Page mCurrentPage;
  private ParcelFileDescriptor mInput;
  private long mNativeDocument;
  private final int mPageCount;
  private final Point mTempPoint = new Point();
  
  public PdfRenderer(ParcelFileDescriptor arg1)
    throws IOException
  {
    if (??? != null) {
      try
      {
        Os.lseek(???.getFileDescriptor(), 0L, OsConstants.SEEK_SET);
        long l = fstatgetFileDescriptorst_size;
        mInput = ???;
        synchronized (sPdfiumLock)
        {
          mNativeDocument = nativeCreate(mInput.getFd(), l);
          try
          {
            mPageCount = nativeGetPageCount(mNativeDocument);
            mCloseGuard.open("close");
            return;
          }
          catch (Throwable localThrowable)
          {
            nativeClose(mNativeDocument);
            mNativeDocument = 0L;
            throw localThrowable;
          }
        }
        throw new NullPointerException("input cannot be null");
      }
      catch (ErrnoException ???)
      {
        throw new IllegalArgumentException("file descriptor not seekable");
      }
    }
  }
  
  private void doClose()
  {
    if (mCurrentPage != null)
    {
      mCurrentPage.close();
      mCurrentPage = null;
    }
    if (mNativeDocument != 0L) {
      synchronized (sPdfiumLock)
      {
        nativeClose(mNativeDocument);
        mNativeDocument = 0L;
      }
    }
    if (mInput != null)
    {
      IoUtils.closeQuietly(mInput);
      mInput = null;
    }
    mCloseGuard.close();
  }
  
  private static native void nativeClose(long paramLong);
  
  private static native void nativeClosePage(long paramLong);
  
  private static native long nativeCreate(int paramInt, long paramLong);
  
  private static native int nativeGetPageCount(long paramLong);
  
  private static native long nativeOpenPageAndGetSize(long paramLong, int paramInt, Point paramPoint);
  
  private static native void nativeRenderPage(long paramLong1, long paramLong2, Bitmap paramBitmap, int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong3, int paramInt5);
  
  private static native boolean nativeScaleForPrinting(long paramLong);
  
  private void throwIfClosed()
  {
    if (mInput != null) {
      return;
    }
    throw new IllegalStateException("Already closed");
  }
  
  private void throwIfPageNotInDocument(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < mPageCount)) {
      return;
    }
    throw new IllegalArgumentException("Invalid page index");
  }
  
  private void throwIfPageOpened()
  {
    if (mCurrentPage == null) {
      return;
    }
    throw new IllegalStateException("Current page not closed");
  }
  
  public void close()
  {
    throwIfClosed();
    throwIfPageOpened();
    doClose();
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mCloseGuard != null) {
        mCloseGuard.warnIfOpen();
      }
      doClose();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public int getPageCount()
  {
    throwIfClosed();
    return mPageCount;
  }
  
  public Page openPage(int paramInt)
  {
    throwIfClosed();
    throwIfPageOpened();
    throwIfPageNotInDocument(paramInt);
    mCurrentPage = new Page(paramInt, null);
    return mCurrentPage;
  }
  
  public boolean shouldScaleForPrinting()
  {
    throwIfClosed();
    synchronized (sPdfiumLock)
    {
      boolean bool = nativeScaleForPrinting(mNativeDocument);
      return bool;
    }
  }
  
  public final class Page
    implements AutoCloseable
  {
    public static final int RENDER_MODE_FOR_DISPLAY = 1;
    public static final int RENDER_MODE_FOR_PRINT = 2;
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private final int mHeight;
    private final int mIndex;
    private long mNativePage;
    private final int mWidth;
    
    private Page(int paramInt)
    {
      Point localPoint = mTempPoint;
      synchronized (PdfRenderer.sPdfiumLock)
      {
        mNativePage = PdfRenderer.nativeOpenPageAndGetSize(mNativeDocument, paramInt, localPoint);
        mIndex = paramInt;
        mWidth = x;
        mHeight = y;
        mCloseGuard.open("close");
        return;
      }
    }
    
    private void doClose()
    {
      if (mNativePage != 0L) {
        synchronized (PdfRenderer.sPdfiumLock)
        {
          PdfRenderer.nativeClosePage(mNativePage);
          mNativePage = 0L;
        }
      }
      mCloseGuard.close();
      PdfRenderer.access$602(PdfRenderer.this, null);
    }
    
    private void throwIfClosed()
    {
      if (mNativePage != 0L) {
        return;
      }
      throw new IllegalStateException("Already closed");
    }
    
    public void close()
    {
      throwIfClosed();
      doClose();
    }
    
    protected void finalize()
      throws Throwable
    {
      try
      {
        if (mCloseGuard != null) {
          mCloseGuard.warnIfOpen();
        }
        doClose();
        return;
      }
      finally
      {
        super.finalize();
      }
    }
    
    public int getHeight()
    {
      return mHeight;
    }
    
    public int getIndex()
    {
      return mIndex;
    }
    
    public int getWidth()
    {
      return mWidth;
    }
    
    public void render(Bitmap paramBitmap, Rect arg2, Matrix paramMatrix, int paramInt)
    {
      if (mNativePage != 0L)
      {
        Bitmap localBitmap = (Bitmap)Preconditions.checkNotNull(paramBitmap, "bitmap null");
        if (localBitmap.getConfig() == Bitmap.Config.ARGB_8888)
        {
          if ((??? != null) && ((left < 0) || (top < 0) || (right > localBitmap.getWidth()) || (bottom > localBitmap.getHeight()))) {
            throw new IllegalArgumentException("destBounds not in destination");
          }
          if ((paramMatrix != null) && (!paramMatrix.isAffine())) {
            throw new IllegalArgumentException("transform not affine");
          }
          if ((paramInt != 2) && (paramInt != 1)) {
            throw new IllegalArgumentException("Unsupported render mode");
          }
          if ((paramInt == 2) && (paramInt == 1)) {
            throw new IllegalArgumentException("Only single render mode supported");
          }
          int i = 0;
          int j;
          if (??? != null) {
            j = left;
          } else {
            j = 0;
          }
          if (??? != null) {
            i = top;
          }
          int k;
          if (??? != null) {
            k = right;
          } else {
            k = localBitmap.getWidth();
          }
          int m;
          if (??? != null) {
            m = bottom;
          } else {
            m = localBitmap.getHeight();
          }
          if (paramMatrix == null)
          {
            paramBitmap = new Matrix();
            paramBitmap.postScale((k - j) / getWidth(), (m - i) / getHeight());
            paramBitmap.postTranslate(j, i);
          }
          else
          {
            paramBitmap = paramMatrix;
          }
          long l1 = native_instance;
          long l2;
          long l3;
          synchronized (PdfRenderer.sPdfiumLock)
          {
            l2 = mNativeDocument;
            l3 = mNativePage;
          }
          throw paramBitmap;
        }
        throw new IllegalArgumentException("Unsupported pixel format");
      }
      throw new NullPointerException();
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface RenderMode {}
}
