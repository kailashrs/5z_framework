package android.filterfw.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class FilterSurfaceView
  extends SurfaceView
  implements SurfaceHolder.Callback
{
  private static int STATE_ALLOCATED = 0;
  private static int STATE_CREATED = 1;
  private static int STATE_INITIALIZED = 2;
  private int mFormat;
  private GLEnvironment mGLEnv;
  private int mHeight;
  private SurfaceHolder.Callback mListener;
  private int mState = STATE_ALLOCATED;
  private int mSurfaceId = -1;
  private int mWidth;
  
  public FilterSurfaceView(Context paramContext)
  {
    super(paramContext);
    getHolder().addCallback(this);
  }
  
  public FilterSurfaceView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    getHolder().addCallback(this);
  }
  
  private void registerSurface()
  {
    mSurfaceId = mGLEnv.registerSurface(getHolder().getSurface());
    if (mSurfaceId >= 0) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Could not register Surface: ");
    localStringBuilder.append(getHolder().getSurface());
    localStringBuilder.append(" in FilterSurfaceView!");
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  private void unregisterSurface()
  {
    if ((mGLEnv != null) && (mSurfaceId > 0)) {
      mGLEnv.unregisterSurfaceId(mSurfaceId);
    }
  }
  
  public void bindToListener(SurfaceHolder.Callback paramCallback, GLEnvironment paramGLEnvironment)
  {
    if (paramCallback != null) {}
    try
    {
      if ((mListener != null) && (mListener != paramCallback))
      {
        paramGLEnvironment = new java/lang/RuntimeException;
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Attempting to bind filter ");
        localStringBuilder.append(paramCallback);
        localStringBuilder.append(" to SurfaceView with another open filter ");
        localStringBuilder.append(mListener);
        localStringBuilder.append(" attached already!");
        paramGLEnvironment.<init>(localStringBuilder.toString());
        throw paramGLEnvironment;
      }
      mListener = paramCallback;
      if ((mGLEnv != null) && (mGLEnv != paramGLEnvironment)) {
        mGLEnv.unregisterSurfaceId(mSurfaceId);
      }
      mGLEnv = paramGLEnvironment;
      if (mState >= STATE_CREATED)
      {
        registerSurface();
        mListener.surfaceCreated(getHolder());
        if (mState == STATE_INITIALIZED) {
          mListener.surfaceChanged(getHolder(), mFormat, mWidth, mHeight);
        }
      }
      return;
    }
    finally {}
    paramCallback = new java/lang/NullPointerException;
    paramCallback.<init>("Attempting to bind null filter to SurfaceView!");
    throw paramCallback;
  }
  
  public GLEnvironment getGLEnv()
  {
    try
    {
      GLEnvironment localGLEnvironment = mGLEnv;
      return localGLEnvironment;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public int getSurfaceId()
  {
    try
    {
      int i = mSurfaceId;
      return i;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3)
  {
    try
    {
      mFormat = paramInt1;
      mWidth = paramInt2;
      mHeight = paramInt3;
      mState = STATE_INITIALIZED;
      if (mListener != null) {
        mListener.surfaceChanged(paramSurfaceHolder, paramInt1, paramInt2, paramInt3);
      }
      return;
    }
    finally
    {
      paramSurfaceHolder = finally;
      throw paramSurfaceHolder;
    }
  }
  
  public void surfaceCreated(SurfaceHolder paramSurfaceHolder)
  {
    try
    {
      mState = STATE_CREATED;
      if (mGLEnv != null) {
        registerSurface();
      }
      if (mListener != null) {
        mListener.surfaceCreated(paramSurfaceHolder);
      }
      return;
    }
    finally {}
  }
  
  public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder)
  {
    try
    {
      mState = STATE_ALLOCATED;
      if (mListener != null) {
        mListener.surfaceDestroyed(paramSurfaceHolder);
      }
      unregisterSurface();
      return;
    }
    finally {}
  }
  
  public void unbind()
  {
    try
    {
      mListener = null;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
}
