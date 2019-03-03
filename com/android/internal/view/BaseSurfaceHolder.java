package com.android.internal.view;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.BadSurfaceTypeException;
import android.view.SurfaceHolder.Callback;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public abstract class BaseSurfaceHolder
  implements SurfaceHolder
{
  static final boolean DEBUG = false;
  private static final String TAG = "BaseSurfaceHolder";
  public final ArrayList<SurfaceHolder.Callback> mCallbacks = new ArrayList();
  SurfaceHolder.Callback[] mGottenCallbacks;
  boolean mHaveGottenCallbacks;
  long mLastLockTime = 0L;
  protected int mRequestedFormat = -1;
  int mRequestedHeight = -1;
  int mRequestedType = -1;
  int mRequestedWidth = -1;
  public Surface mSurface = new Surface();
  final Rect mSurfaceFrame = new Rect();
  public final ReentrantLock mSurfaceLock = new ReentrantLock();
  Rect mTmpDirty;
  int mType = -1;
  
  public BaseSurfaceHolder() {}
  
  private final Canvas internalLockCanvas(Rect paramRect, boolean paramBoolean)
  {
    if (mType != 3)
    {
      mSurfaceLock.lock();
      Object localObject1 = null;
      Object localObject2 = localObject1;
      if (onAllowLockCanvas())
      {
        localObject2 = paramRect;
        if (paramRect == null)
        {
          if (mTmpDirty == null) {
            mTmpDirty = new Rect();
          }
          mTmpDirty.set(mSurfaceFrame);
          localObject2 = mTmpDirty;
        }
        if (paramBoolean) {
          try
          {
            paramRect = mSurface.lockHardwareCanvas();
          }
          catch (Exception paramRect)
          {
            break label104;
          }
        } else {
          paramRect = mSurface.lockCanvas((Rect)localObject2);
        }
        localObject2 = paramRect;
        break label116;
        label104:
        Log.e("BaseSurfaceHolder", "Exception locking surface", paramRect);
        localObject2 = localObject1;
      }
      label116:
      if (localObject2 != null)
      {
        mLastLockTime = SystemClock.uptimeMillis();
        return localObject2;
      }
      long l1 = SystemClock.uptimeMillis();
      long l2 = mLastLockTime + 100L;
      long l3 = l1;
      if (l2 > l1)
      {
        try
        {
          Thread.sleep(l2 - l1);
        }
        catch (InterruptedException paramRect) {}
        l3 = SystemClock.uptimeMillis();
      }
      mLastLockTime = l3;
      mSurfaceLock.unlock();
      return null;
    }
    throw new SurfaceHolder.BadSurfaceTypeException("Surface type is SURFACE_TYPE_PUSH_BUFFERS");
  }
  
  public void addCallback(SurfaceHolder.Callback paramCallback)
  {
    synchronized (mCallbacks)
    {
      if (!mCallbacks.contains(paramCallback)) {
        mCallbacks.add(paramCallback);
      }
      return;
    }
  }
  
  public SurfaceHolder.Callback[] getCallbacks()
  {
    if (mHaveGottenCallbacks) {
      return mGottenCallbacks;
    }
    synchronized (mCallbacks)
    {
      int i = mCallbacks.size();
      if (i > 0)
      {
        if ((mGottenCallbacks == null) || (mGottenCallbacks.length != i)) {
          mGottenCallbacks = new SurfaceHolder.Callback[i];
        }
        mCallbacks.toArray(mGottenCallbacks);
      }
      else
      {
        mGottenCallbacks = null;
      }
      mHaveGottenCallbacks = true;
      return mGottenCallbacks;
    }
  }
  
  public int getRequestedFormat()
  {
    return mRequestedFormat;
  }
  
  public int getRequestedHeight()
  {
    return mRequestedHeight;
  }
  
  public int getRequestedType()
  {
    return mRequestedType;
  }
  
  public int getRequestedWidth()
  {
    return mRequestedWidth;
  }
  
  public Surface getSurface()
  {
    return mSurface;
  }
  
  public Rect getSurfaceFrame()
  {
    return mSurfaceFrame;
  }
  
  public Canvas lockCanvas()
  {
    return internalLockCanvas(null, false);
  }
  
  public Canvas lockCanvas(Rect paramRect)
  {
    return internalLockCanvas(paramRect, false);
  }
  
  public Canvas lockHardwareCanvas()
  {
    return internalLockCanvas(null, true);
  }
  
  public abstract boolean onAllowLockCanvas();
  
  public abstract void onRelayoutContainer();
  
  public abstract void onUpdateSurface();
  
  public void removeCallback(SurfaceHolder.Callback paramCallback)
  {
    synchronized (mCallbacks)
    {
      mCallbacks.remove(paramCallback);
      return;
    }
  }
  
  public void setFixedSize(int paramInt1, int paramInt2)
  {
    if ((mRequestedWidth != paramInt1) || (mRequestedHeight != paramInt2))
    {
      mRequestedWidth = paramInt1;
      mRequestedHeight = paramInt2;
      onRelayoutContainer();
    }
  }
  
  public void setFormat(int paramInt)
  {
    if (mRequestedFormat != paramInt)
    {
      mRequestedFormat = paramInt;
      onUpdateSurface();
    }
  }
  
  public void setSizeFromLayout()
  {
    if ((mRequestedWidth != -1) || (mRequestedHeight != -1))
    {
      mRequestedHeight = -1;
      mRequestedWidth = -1;
      onRelayoutContainer();
    }
  }
  
  public void setSurfaceFrameSize(int paramInt1, int paramInt2)
  {
    mSurfaceFrame.top = 0;
    mSurfaceFrame.left = 0;
    mSurfaceFrame.right = paramInt1;
    mSurfaceFrame.bottom = paramInt2;
  }
  
  public void setType(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      break;
    case 1: 
    case 2: 
      paramInt = 0;
    }
    if (((paramInt == 0) || (paramInt == 3)) && (mRequestedType != paramInt))
    {
      mRequestedType = paramInt;
      onUpdateSurface();
    }
  }
  
  public void ungetCallbacks()
  {
    mHaveGottenCallbacks = false;
  }
  
  public void unlockCanvasAndPost(Canvas paramCanvas)
  {
    mSurface.unlockCanvasAndPost(paramCanvas);
    mSurfaceLock.unlock();
  }
}
