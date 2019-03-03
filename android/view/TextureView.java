package android.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

public class TextureView
  extends View
{
  private static final String LOG_TAG = "TextureView";
  private Canvas mCanvas;
  private boolean mHadSurface;
  private TextureLayer mLayer;
  private SurfaceTextureListener mListener;
  private final Object[] mLock = new Object[0];
  private final Matrix mMatrix = new Matrix();
  private boolean mMatrixChanged;
  private long mNativeWindow;
  private final Object[] mNativeWindowLock = new Object[0];
  private boolean mOpaque = true;
  private int mSaveCount;
  private SurfaceTexture mSurface;
  private boolean mUpdateLayer;
  private final SurfaceTexture.OnFrameAvailableListener mUpdateListener = new SurfaceTexture.OnFrameAvailableListener()
  {
    public void onFrameAvailable(SurfaceTexture paramAnonymousSurfaceTexture)
    {
      TextureView.this.updateLayer();
      invalidate();
    }
  };
  private boolean mUpdateSurface;
  
  public TextureView(Context paramContext)
  {
    super(paramContext);
  }
  
  public TextureView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TextureView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TextureView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  private void applyTransformMatrix()
  {
    if ((mMatrixChanged) && (mLayer != null))
    {
      mLayer.setTransform(mMatrix);
      mMatrixChanged = false;
    }
  }
  
  private void applyUpdate()
  {
    if (mLayer == null) {
      return;
    }
    synchronized (mLock)
    {
      if (mUpdateLayer)
      {
        mUpdateLayer = false;
        mLayer.prepare(getWidth(), getHeight(), mOpaque);
        mLayer.updateSurfaceTexture();
        if (mListener != null) {
          mListener.onSurfaceTextureUpdated(mSurface);
        }
        return;
      }
      return;
    }
  }
  
  private void destroyHardwareLayer()
  {
    if (mLayer != null)
    {
      mLayer.detachSurfaceTexture();
      mLayer.destroy();
      mLayer = null;
      mMatrixChanged = true;
    }
  }
  
  private native void nCreateNativeWindow(SurfaceTexture paramSurfaceTexture);
  
  private native void nDestroyNativeWindow();
  
  private static native boolean nLockCanvas(long paramLong, Canvas paramCanvas, Rect paramRect);
  
  private static native void nUnlockCanvasAndPost(long paramLong, Canvas paramCanvas);
  
  private void releaseSurfaceTexture()
  {
    if (mSurface != null)
    {
      boolean bool = true;
      if (mListener != null) {
        bool = mListener.onSurfaceTextureDestroyed(mSurface);
      }
      synchronized (mNativeWindowLock)
      {
        nDestroyNativeWindow();
        if (bool) {
          mSurface.release();
        }
        mSurface = null;
        mHadSurface = true;
      }
    }
  }
  
  private void updateLayer()
  {
    synchronized (mLock)
    {
      mUpdateLayer = true;
      return;
    }
  }
  
  private void updateLayerAndInvalidate()
  {
    synchronized (mLock)
    {
      mUpdateLayer = true;
      invalidate();
      return;
    }
  }
  
  public void buildLayer() {}
  
  protected void destroyHardwareResources()
  {
    super.destroyHardwareResources();
    destroyHardwareLayer();
  }
  
  public final void draw(Canvas paramCanvas)
  {
    mPrivateFlags = (mPrivateFlags & 0xFF9FFFFF | 0x20);
    if (paramCanvas.isHardwareAccelerated())
    {
      DisplayListCanvas localDisplayListCanvas = (DisplayListCanvas)paramCanvas;
      paramCanvas = getTextureLayer();
      if (paramCanvas != null)
      {
        applyUpdate();
        applyTransformMatrix();
        mLayer.setLayerPaint(mLayerPaint);
        localDisplayListCanvas.drawTextureLayer(paramCanvas);
      }
    }
  }
  
  public Bitmap getBitmap()
  {
    return getBitmap(getWidth(), getHeight());
  }
  
  public Bitmap getBitmap(int paramInt1, int paramInt2)
  {
    if ((isAvailable()) && (paramInt1 > 0) && (paramInt2 > 0)) {
      return getBitmap(Bitmap.createBitmap(getResources().getDisplayMetrics(), paramInt1, paramInt2, Bitmap.Config.ARGB_8888));
    }
    return null;
  }
  
  public Bitmap getBitmap(Bitmap paramBitmap)
  {
    if ((paramBitmap != null) && (isAvailable()))
    {
      applyUpdate();
      applyTransformMatrix();
      if ((mLayer == null) && (mUpdateSurface)) {
        getTextureLayer();
      }
      if (mLayer != null) {
        mLayer.copyInto(paramBitmap);
      }
    }
    return paramBitmap;
  }
  
  public int getLayerType()
  {
    return 2;
  }
  
  public SurfaceTexture getSurfaceTexture()
  {
    return mSurface;
  }
  
  public SurfaceTextureListener getSurfaceTextureListener()
  {
    return mListener;
  }
  
  TextureLayer getTextureLayer()
  {
    if (mLayer == null) {
      if ((mAttachInfo != null) && (mAttachInfo.mThreadedRenderer != null))
      {
        mLayer = mAttachInfo.mThreadedRenderer.createTextureLayer();
        int i;
        if (mSurface == null) {
          i = 1;
        } else {
          i = 0;
        }
        if (i != 0)
        {
          mSurface = new SurfaceTexture(false);
          nCreateNativeWindow(mSurface);
        }
        mLayer.setSurfaceTexture(mSurface);
        mSurface.setDefaultBufferSize(getWidth(), getHeight());
        mSurface.setOnFrameAvailableListener(mUpdateListener, mAttachInfo.mHandler);
        if ((mListener != null) && (i != 0)) {
          mListener.onSurfaceTextureAvailable(mSurface, getWidth(), getHeight());
        }
        mLayer.setLayerPaint(mLayerPaint);
      }
      else
      {
        return null;
      }
    }
    if (mUpdateSurface)
    {
      mUpdateSurface = false;
      updateLayer();
      mMatrixChanged = true;
      mLayer.setSurfaceTexture(mSurface);
      mSurface.setDefaultBufferSize(getWidth(), getHeight());
    }
    return mLayer;
  }
  
  public Matrix getTransform(Matrix paramMatrix)
  {
    Matrix localMatrix = paramMatrix;
    if (paramMatrix == null) {
      localMatrix = new Matrix();
    }
    localMatrix.set(mMatrix);
    return localMatrix;
  }
  
  public boolean isAvailable()
  {
    boolean bool;
    if (mSurface != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isOpaque()
  {
    return mOpaque;
  }
  
  public Canvas lockCanvas()
  {
    return lockCanvas(null);
  }
  
  public Canvas lockCanvas(Rect paramRect)
  {
    if (!isAvailable()) {
      return null;
    }
    if (mCanvas == null) {
      mCanvas = new Canvas();
    }
    synchronized (mNativeWindowLock)
    {
      if (!nLockCanvas(mNativeWindow, mCanvas, paramRect)) {
        return null;
      }
      mSaveCount = mCanvas.save();
      return mCanvas;
    }
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    if (!isHardwareAccelerated()) {
      Log.w("TextureView", "A TextureView or a subclass can only be used with hardware acceleration enabled.");
    }
    if (mHadSurface)
    {
      invalidate(true);
      mHadSurface = false;
    }
  }
  
  protected void onDetachedFromWindowInternal()
  {
    destroyHardwareLayer();
    releaseSurfaceTexture();
    super.onDetachedFromWindowInternal();
  }
  
  protected final void onDraw(Canvas paramCanvas) {}
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (mSurface != null)
    {
      mSurface.setDefaultBufferSize(getWidth(), getHeight());
      updateLayer();
      if (mListener != null) {
        mListener.onSurfaceTextureSizeChanged(mSurface, getWidth(), getHeight());
      }
    }
  }
  
  protected void onVisibilityChanged(View paramView, int paramInt)
  {
    super.onVisibilityChanged(paramView, paramInt);
    if (mSurface != null) {
      if (paramInt == 0)
      {
        if (mLayer != null) {
          mSurface.setOnFrameAvailableListener(mUpdateListener, mAttachInfo.mHandler);
        }
        updateLayerAndInvalidate();
      }
      else
      {
        mSurface.setOnFrameAvailableListener(null);
      }
    }
  }
  
  public void setBackgroundDrawable(Drawable paramDrawable)
  {
    if ((paramDrawable != null) && (!sTextureViewIgnoresDrawableSetters)) {
      throw new UnsupportedOperationException("TextureView doesn't support displaying a background drawable");
    }
  }
  
  public void setForeground(Drawable paramDrawable)
  {
    if ((paramDrawable != null) && (!sTextureViewIgnoresDrawableSetters)) {
      throw new UnsupportedOperationException("TextureView doesn't support displaying a foreground drawable");
    }
  }
  
  public void setLayerPaint(Paint paramPaint)
  {
    if (paramPaint != mLayerPaint)
    {
      mLayerPaint = paramPaint;
      invalidate();
    }
  }
  
  public void setLayerType(int paramInt, Paint paramPaint)
  {
    setLayerPaint(paramPaint);
  }
  
  public void setOpaque(boolean paramBoolean)
  {
    if (paramBoolean != mOpaque)
    {
      mOpaque = paramBoolean;
      if (mLayer != null) {
        updateLayerAndInvalidate();
      }
    }
  }
  
  public void setSurfaceTexture(SurfaceTexture paramSurfaceTexture)
  {
    if (paramSurfaceTexture != null)
    {
      if (paramSurfaceTexture != mSurface)
      {
        if (!paramSurfaceTexture.isReleased())
        {
          if (mSurface != null)
          {
            nDestroyNativeWindow();
            mSurface.release();
          }
          mSurface = paramSurfaceTexture;
          nCreateNativeWindow(mSurface);
          if (((mViewFlags & 0xC) == 0) && (mLayer != null)) {
            mSurface.setOnFrameAvailableListener(mUpdateListener, mAttachInfo.mHandler);
          }
          mUpdateSurface = true;
          invalidateParentIfNeeded();
          return;
        }
        throw new IllegalArgumentException("Cannot setSurfaceTexture to a released SurfaceTexture");
      }
      throw new IllegalArgumentException("Trying to setSurfaceTexture to the same SurfaceTexture that's already set.");
    }
    throw new NullPointerException("surfaceTexture must not be null");
  }
  
  public void setSurfaceTextureListener(SurfaceTextureListener paramSurfaceTextureListener)
  {
    mListener = paramSurfaceTextureListener;
  }
  
  public void setTransform(Matrix paramMatrix)
  {
    mMatrix.set(paramMatrix);
    mMatrixChanged = true;
    invalidateParentIfNeeded();
  }
  
  public void unlockCanvasAndPost(Canvas paramCanvas)
  {
    if ((mCanvas != null) && (paramCanvas == mCanvas))
    {
      paramCanvas.restoreToCount(mSaveCount);
      mSaveCount = 0;
      synchronized (mNativeWindowLock)
      {
        nUnlockCanvasAndPost(mNativeWindow, mCanvas);
      }
    }
  }
  
  public static abstract interface SurfaceTextureListener
  {
    public abstract void onSurfaceTextureAvailable(SurfaceTexture paramSurfaceTexture, int paramInt1, int paramInt2);
    
    public abstract boolean onSurfaceTextureDestroyed(SurfaceTexture paramSurfaceTexture);
    
    public abstract void onSurfaceTextureSizeChanged(SurfaceTexture paramSurfaceTexture, int paramInt1, int paramInt2);
    
    public abstract void onSurfaceTextureUpdated(SurfaceTexture paramSurfaceTexture);
  }
}
