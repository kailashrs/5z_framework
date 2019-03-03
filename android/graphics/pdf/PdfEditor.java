package android.graphics.pdf;

import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.ParcelFileDescriptor;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructStat;
import dalvik.system.CloseGuard;
import java.io.IOException;
import libcore.io.IoUtils;

public final class PdfEditor
{
  private final CloseGuard mCloseGuard = CloseGuard.get();
  private ParcelFileDescriptor mInput;
  private long mNativeDocument;
  private int mPageCount;
  
  public PdfEditor(ParcelFileDescriptor arg1)
    throws IOException
  {
    if (??? != null) {
      try
      {
        Os.lseek(???.getFileDescriptor(), 0L, OsConstants.SEEK_SET);
        long l = fstatgetFileDescriptorst_size;
        mInput = ???;
        synchronized (PdfRenderer.sPdfiumLock)
        {
          mNativeDocument = nativeOpen(mInput.getFd(), l);
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
    if (mNativeDocument != 0L) {
      synchronized (PdfRenderer.sPdfiumLock)
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
  
  private static native int nativeGetPageCount(long paramLong);
  
  private static native boolean nativeGetPageCropBox(long paramLong, int paramInt, Rect paramRect);
  
  private static native boolean nativeGetPageMediaBox(long paramLong, int paramInt, Rect paramRect);
  
  private static native void nativeGetPageSize(long paramLong, int paramInt, Point paramPoint);
  
  private static native long nativeOpen(int paramInt, long paramLong);
  
  private static native int nativeRemovePage(long paramLong, int paramInt);
  
  private static native boolean nativeScaleForPrinting(long paramLong);
  
  private static native void nativeSetPageCropBox(long paramLong, int paramInt, Rect paramRect);
  
  private static native void nativeSetPageMediaBox(long paramLong, int paramInt, Rect paramRect);
  
  private static native void nativeSetTransformAndClip(long paramLong1, int paramInt1, long paramLong2, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  
  private static native void nativeWrite(long paramLong, int paramInt);
  
  private void throwIfClosed()
  {
    if (mInput != null) {
      return;
    }
    throw new IllegalStateException("Already closed");
  }
  
  private void throwIfCropBoxNull(Rect paramRect)
  {
    if (paramRect != null) {
      return;
    }
    throw new NullPointerException("cropBox cannot be null");
  }
  
  private void throwIfMediaBoxNull(Rect paramRect)
  {
    if (paramRect != null) {
      return;
    }
    throw new NullPointerException("mediaBox cannot be null");
  }
  
  private void throwIfNotNullAndNotAfine(Matrix paramMatrix)
  {
    if ((paramMatrix != null) && (!paramMatrix.isAffine())) {
      throw new IllegalStateException("Matrix must be afine");
    }
  }
  
  private void throwIfOutCropBoxNull(Rect paramRect)
  {
    if (paramRect != null) {
      return;
    }
    throw new NullPointerException("outCropBox cannot be null");
  }
  
  private void throwIfOutMediaBoxNull(Rect paramRect)
  {
    if (paramRect != null) {
      return;
    }
    throw new NullPointerException("outMediaBox cannot be null");
  }
  
  private void throwIfOutSizeNull(Point paramPoint)
  {
    if (paramPoint != null) {
      return;
    }
    throw new NullPointerException("outSize cannot be null");
  }
  
  private void throwIfPageNotInDocument(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < mPageCount)) {
      return;
    }
    throw new IllegalArgumentException("Invalid page index");
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
  
  public int getPageCount()
  {
    throwIfClosed();
    return mPageCount;
  }
  
  public boolean getPageCropBox(int paramInt, Rect paramRect)
  {
    throwIfClosed();
    throwIfOutCropBoxNull(paramRect);
    throwIfPageNotInDocument(paramInt);
    synchronized (PdfRenderer.sPdfiumLock)
    {
      boolean bool = nativeGetPageCropBox(mNativeDocument, paramInt, paramRect);
      return bool;
    }
  }
  
  public boolean getPageMediaBox(int paramInt, Rect paramRect)
  {
    throwIfClosed();
    throwIfOutMediaBoxNull(paramRect);
    throwIfPageNotInDocument(paramInt);
    synchronized (PdfRenderer.sPdfiumLock)
    {
      boolean bool = nativeGetPageMediaBox(mNativeDocument, paramInt, paramRect);
      return bool;
    }
  }
  
  public void getPageSize(int paramInt, Point paramPoint)
  {
    throwIfClosed();
    throwIfOutSizeNull(paramPoint);
    throwIfPageNotInDocument(paramInt);
    synchronized (PdfRenderer.sPdfiumLock)
    {
      nativeGetPageSize(mNativeDocument, paramInt, paramPoint);
      return;
    }
  }
  
  public void removePage(int paramInt)
  {
    throwIfClosed();
    throwIfPageNotInDocument(paramInt);
    synchronized (PdfRenderer.sPdfiumLock)
    {
      mPageCount = nativeRemovePage(mNativeDocument, paramInt);
      return;
    }
  }
  
  public void setPageCropBox(int paramInt, Rect paramRect)
  {
    throwIfClosed();
    throwIfCropBoxNull(paramRect);
    throwIfPageNotInDocument(paramInt);
    synchronized (PdfRenderer.sPdfiumLock)
    {
      nativeSetPageCropBox(mNativeDocument, paramInt, paramRect);
      return;
    }
  }
  
  public void setPageMediaBox(int paramInt, Rect paramRect)
  {
    throwIfClosed();
    throwIfMediaBoxNull(paramRect);
    throwIfPageNotInDocument(paramInt);
    synchronized (PdfRenderer.sPdfiumLock)
    {
      nativeSetPageMediaBox(mNativeDocument, paramInt, paramRect);
      return;
    }
  }
  
  public void setTransformAndClip(int paramInt, Matrix arg2, Rect paramRect)
  {
    throwIfClosed();
    throwIfPageNotInDocument(paramInt);
    throwIfNotNullAndNotAfine(???);
    Matrix localMatrix = ???;
    if (??? == null) {
      localMatrix = Matrix.IDENTITY_MATRIX;
    }
    if (paramRect == null)
    {
      paramRect = new Point();
      getPageSize(paramInt, paramRect);
      synchronized (PdfRenderer.sPdfiumLock)
      {
        nativeSetTransformAndClip(mNativeDocument, paramInt, native_instance, 0, 0, x, y);
      }
    }
    synchronized (PdfRenderer.sPdfiumLock)
    {
      nativeSetTransformAndClip(mNativeDocument, paramInt, native_instance, left, top, right, bottom);
      return;
    }
  }
  
  public boolean shouldScaleForPrinting()
  {
    throwIfClosed();
    synchronized (PdfRenderer.sPdfiumLock)
    {
      boolean bool = nativeScaleForPrinting(mNativeDocument);
      return bool;
    }
  }
  
  /* Error */
  public void write(ParcelFileDescriptor paramParcelFileDescriptor)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 168	android/graphics/pdf/PdfEditor:throwIfClosed	()V
    //   4: getstatic 65	android/graphics/pdf/PdfRenderer:sPdfiumLock	Ljava/lang/Object;
    //   7: astore_2
    //   8: aload_2
    //   9: monitorenter
    //   10: aload_0
    //   11: getfield 75	android/graphics/pdf/PdfEditor:mNativeDocument	J
    //   14: aload_1
    //   15: invokevirtual 69	android/os/ParcelFileDescriptor:getFd	()I
    //   18: invokestatic 254	android/graphics/pdf/PdfEditor:nativeWrite	(JI)V
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_1
    //   24: invokestatic 111	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   27: return
    //   28: astore_3
    //   29: aload_2
    //   30: monitorexit
    //   31: aload_3
    //   32: athrow
    //   33: astore_2
    //   34: aload_1
    //   35: invokestatic 111	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   38: aload_2
    //   39: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	40	0	this	PdfEditor
    //   0	40	1	paramParcelFileDescriptor	ParcelFileDescriptor
    //   33	6	2	localObject2	Object
    //   28	4	3	localObject3	Object
    // Exception table:
    //   from	to	target	type
    //   10	23	28	finally
    //   29	31	28	finally
    //   0	10	33	finally
    //   31	33	33	finally
  }
}
