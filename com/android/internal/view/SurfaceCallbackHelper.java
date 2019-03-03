package com.android.internal.view;

import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceHolder.Callback2;

public class SurfaceCallbackHelper
{
  int mFinishDrawingCollected = 0;
  int mFinishDrawingExpected = 0;
  private Runnable mFinishDrawingRunnable = new Runnable()
  {
    public void run()
    {
      synchronized (SurfaceCallbackHelper.this)
      {
        SurfaceCallbackHelper localSurfaceCallbackHelper2 = SurfaceCallbackHelper.this;
        mFinishDrawingCollected += 1;
        if (mFinishDrawingCollected < mFinishDrawingExpected) {
          return;
        }
        mRunnable.run();
        return;
      }
    }
  };
  Runnable mRunnable;
  
  public SurfaceCallbackHelper(Runnable paramRunnable)
  {
    mRunnable = paramRunnable;
  }
  
  public void dispatchSurfaceRedrawNeededAsync(SurfaceHolder paramSurfaceHolder, SurfaceHolder.Callback[] paramArrayOfCallback)
  {
    if ((paramArrayOfCallback != null) && (paramArrayOfCallback.length != 0)) {
      try
      {
        mFinishDrawingExpected = paramArrayOfCallback.length;
        int i = 0;
        mFinishDrawingCollected = 0;
        int j = paramArrayOfCallback.length;
        while (i < j)
        {
          SurfaceHolder.Callback localCallback = paramArrayOfCallback[i];
          if ((localCallback instanceof SurfaceHolder.Callback2)) {
            ((SurfaceHolder.Callback2)localCallback).surfaceRedrawNeededAsync(paramSurfaceHolder, mFinishDrawingRunnable);
          } else {
            mFinishDrawingRunnable.run();
          }
          i++;
        }
        return;
      }
      finally {}
    }
    mRunnable.run();
  }
}
