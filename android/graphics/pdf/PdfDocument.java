package android.graphics.pdf;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import dalvik.system.CloseGuard;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PdfDocument
{
  private final byte[] mChunk = new byte['က'];
  private final CloseGuard mCloseGuard = CloseGuard.get();
  private Page mCurrentPage;
  private long mNativeDocument = nativeCreateDocument();
  private final List<PageInfo> mPages = new ArrayList();
  
  public PdfDocument()
  {
    mCloseGuard.open("close");
  }
  
  private void dispose()
  {
    if (mNativeDocument != 0L)
    {
      nativeClose(mNativeDocument);
      mCloseGuard.close();
      mNativeDocument = 0L;
    }
  }
  
  private native void nativeClose(long paramLong);
  
  private native long nativeCreateDocument();
  
  private native void nativeFinishPage(long paramLong);
  
  private static native long nativeStartPage(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
  
  private native void nativeWriteTo(long paramLong, OutputStream paramOutputStream, byte[] paramArrayOfByte);
  
  private void throwIfClosed()
  {
    if (mNativeDocument != 0L) {
      return;
    }
    throw new IllegalStateException("document is closed!");
  }
  
  private void throwIfCurrentPageNotFinished()
  {
    if (mCurrentPage == null) {
      return;
    }
    throw new IllegalStateException("Current page not finished!");
  }
  
  public void close()
  {
    throwIfCurrentPageNotFinished();
    dispose();
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mCloseGuard != null) {
        mCloseGuard.warnIfOpen();
      }
      dispose();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public void finishPage(Page paramPage)
  {
    throwIfClosed();
    if (paramPage != null)
    {
      if (paramPage == mCurrentPage)
      {
        if (!paramPage.isFinished())
        {
          mPages.add(paramPage.getInfo());
          mCurrentPage = null;
          nativeFinishPage(mNativeDocument);
          paramPage.finish();
          return;
        }
        throw new IllegalStateException("page already finished");
      }
      throw new IllegalStateException("invalid page");
    }
    throw new IllegalArgumentException("page cannot be null");
  }
  
  public List<PageInfo> getPages()
  {
    return Collections.unmodifiableList(mPages);
  }
  
  public Page startPage(PageInfo paramPageInfo)
  {
    throwIfClosed();
    throwIfCurrentPageNotFinished();
    if (paramPageInfo != null)
    {
      mCurrentPage = new Page(new PdfCanvas(nativeStartPage(mNativeDocument, mPageWidth, mPageHeight, mContentRect.left, mContentRect.top, mContentRect.right, mContentRect.bottom)), paramPageInfo, null);
      return mCurrentPage;
    }
    throw new IllegalArgumentException("page cannot be null");
  }
  
  public void writeTo(OutputStream paramOutputStream)
    throws IOException
  {
    throwIfClosed();
    throwIfCurrentPageNotFinished();
    if (paramOutputStream != null)
    {
      nativeWriteTo(mNativeDocument, paramOutputStream, mChunk);
      return;
    }
    throw new IllegalArgumentException("out cannot be null!");
  }
  
  public static final class Page
  {
    private Canvas mCanvas;
    private final PdfDocument.PageInfo mPageInfo;
    
    private Page(Canvas paramCanvas, PdfDocument.PageInfo paramPageInfo)
    {
      mCanvas = paramCanvas;
      mPageInfo = paramPageInfo;
    }
    
    private void finish()
    {
      if (mCanvas != null)
      {
        mCanvas.release();
        mCanvas = null;
      }
    }
    
    public Canvas getCanvas()
    {
      return mCanvas;
    }
    
    public PdfDocument.PageInfo getInfo()
    {
      return mPageInfo;
    }
    
    boolean isFinished()
    {
      boolean bool;
      if (mCanvas == null) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  }
  
  public static final class PageInfo
  {
    private Rect mContentRect;
    private int mPageHeight;
    private int mPageNumber;
    private int mPageWidth;
    
    private PageInfo() {}
    
    public Rect getContentRect()
    {
      return mContentRect;
    }
    
    public int getPageHeight()
    {
      return mPageHeight;
    }
    
    public int getPageNumber()
    {
      return mPageNumber;
    }
    
    public int getPageWidth()
    {
      return mPageWidth;
    }
    
    public static final class Builder
    {
      private final PdfDocument.PageInfo mPageInfo = new PdfDocument.PageInfo(null);
      
      public Builder(int paramInt1, int paramInt2, int paramInt3)
      {
        if (paramInt1 > 0)
        {
          if (paramInt2 > 0)
          {
            if (paramInt3 >= 0)
            {
              PdfDocument.PageInfo.access$002(mPageInfo, paramInt1);
              PdfDocument.PageInfo.access$102(mPageInfo, paramInt2);
              PdfDocument.PageInfo.access$602(mPageInfo, paramInt3);
              return;
            }
            throw new IllegalArgumentException("pageNumber must be non negative");
          }
          throw new IllegalArgumentException("page width must be positive");
        }
        throw new IllegalArgumentException("page width must be positive");
      }
      
      public PdfDocument.PageInfo create()
      {
        if (mPageInfo.mContentRect == null) {
          PdfDocument.PageInfo.access$202(mPageInfo, new Rect(0, 0, mPageInfo.mPageWidth, mPageInfo.mPageHeight));
        }
        return mPageInfo;
      }
      
      public Builder setContentRect(Rect paramRect)
      {
        if ((paramRect != null) && ((left < 0) || (top < 0) || (right > mPageInfo.mPageWidth) || (bottom > mPageInfo.mPageHeight))) {
          throw new IllegalArgumentException("contentRect does not fit the page");
        }
        PdfDocument.PageInfo.access$202(mPageInfo, paramRect);
        return this;
      }
    }
  }
  
  private final class PdfCanvas
    extends Canvas
  {
    public PdfCanvas(long paramLong)
    {
      super();
    }
    
    public void setBitmap(Bitmap paramBitmap)
    {
      throw new UnsupportedOperationException();
    }
  }
}
