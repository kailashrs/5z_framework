package android.view;

import android.graphics.Bitmap;
import android.graphics.CanvasProperty;
import android.graphics.Paint;
import android.util.Pools.SynchronizedPool;
import dalvik.annotation.optimization.CriticalNative;
import dalvik.annotation.optimization.FastNative;

public final class DisplayListCanvas
  extends RecordingCanvas
{
  private static final int MAX_BITMAP_SIZE = 104857600;
  private static final int POOL_LIMIT = 25;
  private static final Pools.SynchronizedPool<DisplayListCanvas> sPool = new Pools.SynchronizedPool(25);
  private int mHeight;
  RenderNode mNode;
  private int mWidth;
  
  private DisplayListCanvas(RenderNode paramRenderNode, int paramInt1, int paramInt2)
  {
    super(nCreateDisplayListCanvas(mNativeRenderNode, paramInt1, paramInt2));
    mDensity = 0;
  }
  
  @FastNative
  private static native void nCallDrawGLFunction(long paramLong1, long paramLong2, Runnable paramRunnable);
  
  @CriticalNative
  private static native long nCreateDisplayListCanvas(long paramLong, int paramInt1, int paramInt2);
  
  @CriticalNative
  private static native void nDrawCircle(long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5);
  
  @CriticalNative
  private static native void nDrawRenderNode(long paramLong1, long paramLong2);
  
  @CriticalNative
  private static native void nDrawRoundRect(long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5, long paramLong6, long paramLong7, long paramLong8);
  
  @CriticalNative
  private static native void nDrawTextureLayer(long paramLong1, long paramLong2);
  
  @CriticalNative
  private static native long nFinishRecording(long paramLong);
  
  @CriticalNative
  private static native int nGetMaximumTextureHeight();
  
  @CriticalNative
  private static native int nGetMaximumTextureWidth();
  
  @CriticalNative
  private static native void nInsertReorderBarrier(long paramLong, boolean paramBoolean);
  
  @CriticalNative
  private static native void nResetDisplayListCanvas(long paramLong1, long paramLong2, int paramInt1, int paramInt2);
  
  static DisplayListCanvas obtain(RenderNode paramRenderNode, int paramInt1, int paramInt2)
  {
    if (paramRenderNode != null)
    {
      DisplayListCanvas localDisplayListCanvas = (DisplayListCanvas)sPool.acquire();
      if (localDisplayListCanvas == null) {
        localDisplayListCanvas = new DisplayListCanvas(paramRenderNode, paramInt1, paramInt2);
      } else {
        nResetDisplayListCanvas(mNativeCanvasWrapper, mNativeRenderNode, paramInt1, paramInt2);
      }
      mNode = paramRenderNode;
      mWidth = paramInt1;
      mHeight = paramInt2;
      return localDisplayListCanvas;
    }
    throw new IllegalArgumentException("node cannot be null");
  }
  
  public void callDrawGLFunction2(long paramLong)
  {
    nCallDrawGLFunction(mNativeCanvasWrapper, paramLong, null);
  }
  
  public void drawCircle(CanvasProperty<Float> paramCanvasProperty1, CanvasProperty<Float> paramCanvasProperty2, CanvasProperty<Float> paramCanvasProperty3, CanvasProperty<Paint> paramCanvasProperty)
  {
    nDrawCircle(mNativeCanvasWrapper, paramCanvasProperty1.getNativeContainer(), paramCanvasProperty2.getNativeContainer(), paramCanvasProperty3.getNativeContainer(), paramCanvasProperty.getNativeContainer());
  }
  
  public void drawGLFunctor2(long paramLong, Runnable paramRunnable)
  {
    nCallDrawGLFunction(mNativeCanvasWrapper, paramLong, paramRunnable);
  }
  
  public void drawRenderNode(RenderNode paramRenderNode)
  {
    nDrawRenderNode(mNativeCanvasWrapper, paramRenderNode.getNativeDisplayList());
  }
  
  public void drawRoundRect(CanvasProperty<Float> paramCanvasProperty1, CanvasProperty<Float> paramCanvasProperty2, CanvasProperty<Float> paramCanvasProperty3, CanvasProperty<Float> paramCanvasProperty4, CanvasProperty<Float> paramCanvasProperty5, CanvasProperty<Float> paramCanvasProperty6, CanvasProperty<Paint> paramCanvasProperty)
  {
    nDrawRoundRect(mNativeCanvasWrapper, paramCanvasProperty1.getNativeContainer(), paramCanvasProperty2.getNativeContainer(), paramCanvasProperty3.getNativeContainer(), paramCanvasProperty4.getNativeContainer(), paramCanvasProperty5.getNativeContainer(), paramCanvasProperty6.getNativeContainer(), paramCanvasProperty.getNativeContainer());
  }
  
  void drawTextureLayer(TextureLayer paramTextureLayer)
  {
    nDrawTextureLayer(mNativeCanvasWrapper, paramTextureLayer.getLayerHandle());
  }
  
  long finishRecording()
  {
    return nFinishRecording(mNativeCanvasWrapper);
  }
  
  public int getHeight()
  {
    return mHeight;
  }
  
  public int getMaximumBitmapHeight()
  {
    return nGetMaximumTextureHeight();
  }
  
  public int getMaximumBitmapWidth()
  {
    return nGetMaximumTextureWidth();
  }
  
  public int getWidth()
  {
    return mWidth;
  }
  
  public void insertInorderBarrier()
  {
    nInsertReorderBarrier(mNativeCanvasWrapper, false);
  }
  
  public void insertReorderBarrier()
  {
    nInsertReorderBarrier(mNativeCanvasWrapper, true);
  }
  
  public boolean isHardwareAccelerated()
  {
    return true;
  }
  
  public boolean isOpaque()
  {
    return false;
  }
  
  public boolean isRecordingFor(Object paramObject)
  {
    boolean bool;
    if (paramObject == mNode) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  void recycle()
  {
    mNode = null;
    sPool.release(this);
  }
  
  public void setBitmap(Bitmap paramBitmap)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setDensity(int paramInt) {}
  
  protected void throwIfCannotDraw(Bitmap paramBitmap)
  {
    super.throwIfCannotDraw(paramBitmap);
    int i = paramBitmap.getByteCount();
    if (i <= 104857600) {
      return;
    }
    paramBitmap = new StringBuilder();
    paramBitmap.append("Canvas: trying to draw too large(");
    paramBitmap.append(i);
    paramBitmap.append("bytes) bitmap.");
    throw new RuntimeException(paramBitmap.toString());
  }
}
