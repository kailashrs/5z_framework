package android.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.util.AttributeSet;
import android.view.InputQueue;
import android.view.InputQueue.Callback;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback2;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import dalvik.system.BaseDexClassLoader;
import java.io.File;

public class NativeActivity
  extends Activity
  implements SurfaceHolder.Callback2, InputQueue.Callback, ViewTreeObserver.OnGlobalLayoutListener
{
  private static final String KEY_NATIVE_SAVED_STATE = "android:native_state";
  public static final String META_DATA_FUNC_NAME = "android.app.func_name";
  public static final String META_DATA_LIB_NAME = "android.app.lib_name";
  private InputQueue mCurInputQueue;
  private SurfaceHolder mCurSurfaceHolder;
  private boolean mDestroyed;
  private boolean mDispatchingUnhandledKey;
  private InputMethodManager mIMM;
  int mLastContentHeight;
  int mLastContentWidth;
  int mLastContentX;
  int mLastContentY;
  final int[] mLocation = new int[2];
  private NativeContentView mNativeContentView;
  private long mNativeHandle;
  
  public NativeActivity() {}
  
  private static String getAbsolutePath(File paramFile)
  {
    if (paramFile != null) {
      paramFile = paramFile.getAbsolutePath();
    } else {
      paramFile = null;
    }
    return paramFile;
  }
  
  private native String getDlError();
  
  private native long loadNativeCode(String paramString1, String paramString2, MessageQueue paramMessageQueue, String paramString3, String paramString4, String paramString5, int paramInt, AssetManager paramAssetManager, byte[] paramArrayOfByte, ClassLoader paramClassLoader, String paramString6);
  
  private native void onConfigurationChangedNative(long paramLong);
  
  private native void onContentRectChangedNative(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  private native void onInputQueueCreatedNative(long paramLong1, long paramLong2);
  
  private native void onInputQueueDestroyedNative(long paramLong1, long paramLong2);
  
  private native void onLowMemoryNative(long paramLong);
  
  private native void onPauseNative(long paramLong);
  
  private native void onResumeNative(long paramLong);
  
  private native byte[] onSaveInstanceStateNative(long paramLong);
  
  private native void onStartNative(long paramLong);
  
  private native void onStopNative(long paramLong);
  
  private native void onSurfaceChangedNative(long paramLong, Surface paramSurface, int paramInt1, int paramInt2, int paramInt3);
  
  private native void onSurfaceCreatedNative(long paramLong, Surface paramSurface);
  
  private native void onSurfaceDestroyedNative(long paramLong);
  
  private native void onSurfaceRedrawNeededNative(long paramLong, Surface paramSurface);
  
  private native void onWindowFocusChangedNative(long paramLong, boolean paramBoolean);
  
  private native void unloadNativeCode(long paramLong);
  
  void hideIme(int paramInt)
  {
    mIMM.hideSoftInputFromWindow(mNativeContentView.getWindowToken(), paramInt);
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    if (!mDestroyed) {
      onConfigurationChangedNative(mNativeHandle);
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    Object localObject1 = "main";
    Object localObject2 = "ANativeActivity_onCreate";
    mIMM = ((InputMethodManager)getSystemService(InputMethodManager.class));
    getWindow().takeSurface(this);
    getWindow().takeInputQueue(this);
    getWindow().setFormat(4);
    getWindow().setSoftInputMode(16);
    mNativeContentView = new NativeContentView(this);
    mNativeContentView.mActivity = this;
    setContentView(mNativeContentView);
    mNativeContentView.requestFocus();
    mNativeContentView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    try
    {
      Object localObject3 = getPackageManager().getActivityInfo(getIntent().getComponent(), 128);
      Object localObject4 = localObject1;
      Object localObject5 = localObject2;
      if (metaData != null)
      {
        localObject5 = metaData.getString("android.app.lib_name");
        if (localObject5 != null) {
          localObject1 = localObject5;
        }
        localObject3 = metaData.getString("android.app.func_name");
        localObject4 = localObject1;
        localObject5 = localObject2;
        if (localObject3 != null)
        {
          localObject5 = localObject3;
          localObject4 = localObject1;
        }
      }
      localObject2 = (BaseDexClassLoader)getClassLoader();
      localObject3 = ((BaseDexClassLoader)localObject2).findLibrary(localObject4);
      if (localObject3 != null)
      {
        if (paramBundle != null) {
          localObject1 = paramBundle.getByteArray("android:native_state");
        } else {
          localObject1 = null;
        }
        mNativeHandle = loadNativeCode((String)localObject3, (String)localObject5, Looper.myQueue(), getAbsolutePath(getFilesDir()), getAbsolutePath(getObbDir()), getAbsolutePath(getExternalFilesDir(null)), Build.VERSION.SDK_INT, getAssets(), (byte[])localObject1, (ClassLoader)localObject2, ((BaseDexClassLoader)localObject2).getLdLibraryPath());
        if (mNativeHandle != 0L)
        {
          super.onCreate(paramBundle);
          return;
        }
        paramBundle = new StringBuilder();
        paramBundle.append("Unable to load native library \"");
        paramBundle.append((String)localObject3);
        paramBundle.append("\": ");
        paramBundle.append(getDlError());
        throw new UnsatisfiedLinkError(paramBundle.toString());
      }
      paramBundle = new StringBuilder();
      paramBundle.append("Unable to find native library ");
      paramBundle.append(localObject4);
      paramBundle.append(" using classloader: ");
      paramBundle.append(((BaseDexClassLoader)localObject2).toString());
      throw new IllegalArgumentException(paramBundle.toString());
    }
    catch (PackageManager.NameNotFoundException paramBundle)
    {
      throw new RuntimeException("Error getting activity info", paramBundle);
    }
  }
  
  protected void onDestroy()
  {
    mDestroyed = true;
    if (mCurSurfaceHolder != null)
    {
      onSurfaceDestroyedNative(mNativeHandle);
      mCurSurfaceHolder = null;
    }
    if (mCurInputQueue != null)
    {
      onInputQueueDestroyedNative(mNativeHandle, mCurInputQueue.getNativePtr());
      mCurInputQueue = null;
    }
    unloadNativeCode(mNativeHandle);
    super.onDestroy();
  }
  
  public void onGlobalLayout()
  {
    mNativeContentView.getLocationInWindow(mLocation);
    int i = mNativeContentView.getWidth();
    int j = mNativeContentView.getHeight();
    if ((mLocation[0] != mLastContentX) || (mLocation[1] != mLastContentY) || (i != mLastContentWidth) || (j != mLastContentHeight))
    {
      mLastContentX = mLocation[0];
      mLastContentY = mLocation[1];
      mLastContentWidth = i;
      mLastContentHeight = j;
      if (!mDestroyed) {
        onContentRectChangedNative(mNativeHandle, mLastContentX, mLastContentY, mLastContentWidth, mLastContentHeight);
      }
    }
  }
  
  public void onInputQueueCreated(InputQueue paramInputQueue)
  {
    if (!mDestroyed)
    {
      mCurInputQueue = paramInputQueue;
      onInputQueueCreatedNative(mNativeHandle, paramInputQueue.getNativePtr());
    }
  }
  
  public void onInputQueueDestroyed(InputQueue paramInputQueue)
  {
    if (!mDestroyed)
    {
      onInputQueueDestroyedNative(mNativeHandle, paramInputQueue.getNativePtr());
      mCurInputQueue = null;
    }
  }
  
  public void onLowMemory()
  {
    super.onLowMemory();
    if (!mDestroyed) {
      onLowMemoryNative(mNativeHandle);
    }
  }
  
  protected void onPause()
  {
    super.onPause();
    onPauseNative(mNativeHandle);
  }
  
  protected void onResume()
  {
    super.onResume();
    onResumeNative(mNativeHandle);
  }
  
  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    byte[] arrayOfByte = onSaveInstanceStateNative(mNativeHandle);
    if (arrayOfByte != null) {
      paramBundle.putByteArray("android:native_state", arrayOfByte);
    }
  }
  
  protected void onStart()
  {
    super.onStart();
    onStartNative(mNativeHandle);
  }
  
  protected void onStop()
  {
    super.onStop();
    onStopNative(mNativeHandle);
  }
  
  public void onWindowFocusChanged(boolean paramBoolean)
  {
    super.onWindowFocusChanged(paramBoolean);
    if (!mDestroyed) {
      onWindowFocusChangedNative(mNativeHandle, paramBoolean);
    }
  }
  
  void setWindowFlags(int paramInt1, int paramInt2)
  {
    getWindow().setFlags(paramInt1, paramInt2);
  }
  
  void setWindowFormat(int paramInt)
  {
    getWindow().setFormat(paramInt);
  }
  
  void showIme(int paramInt)
  {
    mIMM.showSoftInput(mNativeContentView, paramInt);
  }
  
  public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3)
  {
    if (!mDestroyed)
    {
      mCurSurfaceHolder = paramSurfaceHolder;
      onSurfaceChangedNative(mNativeHandle, paramSurfaceHolder.getSurface(), paramInt1, paramInt2, paramInt3);
    }
  }
  
  public void surfaceCreated(SurfaceHolder paramSurfaceHolder)
  {
    if (!mDestroyed)
    {
      mCurSurfaceHolder = paramSurfaceHolder;
      onSurfaceCreatedNative(mNativeHandle, paramSurfaceHolder.getSurface());
    }
  }
  
  public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder)
  {
    mCurSurfaceHolder = null;
    if (!mDestroyed) {
      onSurfaceDestroyedNative(mNativeHandle);
    }
  }
  
  public void surfaceRedrawNeeded(SurfaceHolder paramSurfaceHolder)
  {
    if (!mDestroyed)
    {
      mCurSurfaceHolder = paramSurfaceHolder;
      onSurfaceRedrawNeededNative(mNativeHandle, paramSurfaceHolder.getSurface());
    }
  }
  
  static class NativeContentView
    extends View
  {
    NativeActivity mActivity;
    
    public NativeContentView(Context paramContext)
    {
      super();
    }
    
    public NativeContentView(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
    }
  }
}
