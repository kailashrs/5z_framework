package android.view;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import com.android.internal.util.VirtualRefBasePtr;

final class TextureLayer
{
  private VirtualRefBasePtr mFinalizer;
  private ThreadedRenderer mRenderer;
  
  private TextureLayer(ThreadedRenderer paramThreadedRenderer, long paramLong)
  {
    if ((paramThreadedRenderer != null) && (paramLong != 0L))
    {
      mRenderer = paramThreadedRenderer;
      mFinalizer = new VirtualRefBasePtr(paramLong);
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Either hardware renderer: ");
    localStringBuilder.append(paramThreadedRenderer);
    localStringBuilder.append(" or deferredUpdater: ");
    localStringBuilder.append(paramLong);
    localStringBuilder.append(" is invalid");
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  static TextureLayer adoptTextureLayer(ThreadedRenderer paramThreadedRenderer, long paramLong)
  {
    return new TextureLayer(paramThreadedRenderer, paramLong);
  }
  
  private static native boolean nPrepare(long paramLong, int paramInt1, int paramInt2, boolean paramBoolean);
  
  private static native void nSetLayerPaint(long paramLong1, long paramLong2);
  
  private static native void nSetSurfaceTexture(long paramLong, SurfaceTexture paramSurfaceTexture);
  
  private static native void nSetTransform(long paramLong1, long paramLong2);
  
  private static native void nUpdateSurfaceTexture(long paramLong);
  
  public boolean copyInto(Bitmap paramBitmap)
  {
    return mRenderer.copyLayerInto(this, paramBitmap);
  }
  
  public void destroy()
  {
    if (!isValid()) {
      return;
    }
    mRenderer.onLayerDestroyed(this);
    mRenderer = null;
    mFinalizer.release();
    mFinalizer = null;
  }
  
  public void detachSurfaceTexture()
  {
    mRenderer.detachSurfaceTexture(mFinalizer.get());
  }
  
  public long getDeferredLayerUpdater()
  {
    return mFinalizer.get();
  }
  
  public long getLayerHandle()
  {
    return mFinalizer.get();
  }
  
  public boolean isValid()
  {
    boolean bool;
    if ((mFinalizer != null) && (mFinalizer.get() != 0L)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean prepare(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    return nPrepare(mFinalizer.get(), paramInt1, paramInt2, paramBoolean);
  }
  
  public void setLayerPaint(Paint paramPaint)
  {
    long l1 = mFinalizer.get();
    long l2;
    if (paramPaint != null) {
      l2 = paramPaint.getNativeInstance();
    } else {
      l2 = 0L;
    }
    nSetLayerPaint(l1, l2);
    mRenderer.pushLayerUpdate(this);
  }
  
  public void setSurfaceTexture(SurfaceTexture paramSurfaceTexture)
  {
    nSetSurfaceTexture(mFinalizer.get(), paramSurfaceTexture);
    mRenderer.pushLayerUpdate(this);
  }
  
  public void setTransform(Matrix paramMatrix)
  {
    nSetTransform(mFinalizer.get(), native_instance);
    mRenderer.pushLayerUpdate(this);
  }
  
  public void updateSurfaceTexture()
  {
    nUpdateSurfaceTexture(mFinalizer.get());
    mRenderer.pushLayerUpdate(this);
  }
}
