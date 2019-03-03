package android.media.projection;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.hardware.display.VirtualDisplay.Callback;
import android.media.AudioRecord;
import android.os.Handler;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Surface;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public final class MediaProjection
{
  private static final String TAG = "MediaProjection";
  private final Map<Callback, CallbackRecord> mCallbacks = new ArrayMap();
  private final Context mContext;
  private final IMediaProjection mImpl;
  
  public MediaProjection(Context paramContext, IMediaProjection paramIMediaProjection)
  {
    mContext = paramContext;
    mImpl = paramIMediaProjection;
    try
    {
      paramContext = mImpl;
      paramIMediaProjection = new android/media/projection/MediaProjection$MediaProjectionCallback;
      paramIMediaProjection.<init>(this, null);
      paramContext.start(paramIMediaProjection);
      return;
    }
    catch (RemoteException paramContext)
    {
      throw new RuntimeException("Failed to start media projection", paramContext);
    }
  }
  
  public AudioRecord createAudioRecord(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    return null;
  }
  
  public VirtualDisplay createVirtualDisplay(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Surface paramSurface, VirtualDisplay.Callback paramCallback, Handler paramHandler)
  {
    return ((DisplayManager)mContext.getSystemService("display")).createVirtualDisplay(this, paramString, paramInt1, paramInt2, paramInt3, paramSurface, paramInt4, paramCallback, paramHandler, null);
  }
  
  public VirtualDisplay createVirtualDisplay(String paramString, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, Surface paramSurface, VirtualDisplay.Callback paramCallback, Handler paramHandler)
  {
    DisplayManager localDisplayManager = (DisplayManager)mContext.getSystemService("display");
    int i;
    if (paramBoolean) {
      i = 4;
    } else {
      i = 0;
    }
    return localDisplayManager.createVirtualDisplay(this, paramString, paramInt1, paramInt2, paramInt3, paramSurface, i | 0x10 | 0x2, paramCallback, paramHandler, null);
  }
  
  public IMediaProjection getProjection()
  {
    return mImpl;
  }
  
  public void registerCallback(Callback paramCallback, Handler paramHandler)
  {
    if (paramCallback != null)
    {
      Handler localHandler = paramHandler;
      if (paramHandler == null) {
        localHandler = new Handler();
      }
      mCallbacks.put(paramCallback, new CallbackRecord(paramCallback, localHandler));
      return;
    }
    throw new IllegalArgumentException("callback should not be null");
  }
  
  public void stop()
  {
    try
    {
      mImpl.stop();
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("MediaProjection", "Unable to stop projection", localRemoteException);
    }
  }
  
  public void unregisterCallback(Callback paramCallback)
  {
    if (paramCallback != null)
    {
      mCallbacks.remove(paramCallback);
      return;
    }
    throw new IllegalArgumentException("callback should not be null");
  }
  
  public static abstract class Callback
  {
    public Callback() {}
    
    public void onStop() {}
  }
  
  private static final class CallbackRecord
  {
    private final MediaProjection.Callback mCallback;
    private final Handler mHandler;
    
    public CallbackRecord(MediaProjection.Callback paramCallback, Handler paramHandler)
    {
      mCallback = paramCallback;
      mHandler = paramHandler;
    }
    
    public void onStop()
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mCallback.onStop();
        }
      });
    }
  }
  
  private final class MediaProjectionCallback
    extends IMediaProjectionCallback.Stub
  {
    private MediaProjectionCallback() {}
    
    public void onStop()
    {
      Iterator localIterator = mCallbacks.values().iterator();
      while (localIterator.hasNext()) {
        ((MediaProjection.CallbackRecord)localIterator.next()).onStop();
      }
    }
  }
}
