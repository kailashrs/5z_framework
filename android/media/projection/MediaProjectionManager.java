package android.media.projection;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.ArrayMap;
import android.util.Log;
import java.util.Map;

public final class MediaProjectionManager
{
  public static final String EXTRA_APP_TOKEN = "android.media.projection.extra.EXTRA_APP_TOKEN";
  public static final String EXTRA_MEDIA_PROJECTION = "android.media.projection.extra.EXTRA_MEDIA_PROJECTION";
  private static final String TAG = "MediaProjectionManager";
  public static final int TYPE_MIRRORING = 1;
  public static final int TYPE_PRESENTATION = 2;
  public static final int TYPE_SCREEN_CAPTURE = 0;
  private Map<Callback, CallbackDelegate> mCallbacks;
  private Context mContext;
  private IMediaProjectionManager mService;
  
  public MediaProjectionManager(Context paramContext)
  {
    mContext = paramContext;
    mService = IMediaProjectionManager.Stub.asInterface(ServiceManager.getService("media_projection"));
    mCallbacks = new ArrayMap();
  }
  
  public void addCallback(Callback paramCallback, Handler paramHandler)
  {
    if (paramCallback != null)
    {
      paramHandler = new CallbackDelegate(paramCallback, paramHandler);
      mCallbacks.put(paramCallback, paramHandler);
      try
      {
        mService.addCallback(paramHandler);
      }
      catch (RemoteException paramCallback)
      {
        Log.e("MediaProjectionManager", "Unable to add callbacks to MediaProjection service", paramCallback);
      }
      return;
    }
    throw new IllegalArgumentException("callback must not be null");
  }
  
  public Intent createScreenCaptureIntent()
  {
    Intent localIntent = new Intent();
    localIntent.setComponent(ComponentName.unflattenFromString(mContext.getResources().getString(17039731)));
    return localIntent;
  }
  
  public MediaProjectionInfo getActiveProjectionInfo()
  {
    try
    {
      MediaProjectionInfo localMediaProjectionInfo = mService.getActiveProjectionInfo();
      return localMediaProjectionInfo;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("MediaProjectionManager", "Unable to get the active projection info", localRemoteException);
    }
    return null;
  }
  
  public MediaProjection getMediaProjection(int paramInt, Intent paramIntent)
  {
    if ((paramInt == -1) && (paramIntent != null))
    {
      paramIntent = paramIntent.getIBinderExtra("android.media.projection.extra.EXTRA_MEDIA_PROJECTION");
      if (paramIntent == null) {
        return null;
      }
      return new MediaProjection(mContext, IMediaProjection.Stub.asInterface(paramIntent));
    }
    return null;
  }
  
  public void removeCallback(Callback paramCallback)
  {
    if (paramCallback != null)
    {
      paramCallback = (CallbackDelegate)mCallbacks.remove(paramCallback);
      if (paramCallback != null) {
        try
        {
          mService.removeCallback(paramCallback);
        }
        catch (RemoteException paramCallback)
        {
          Log.e("MediaProjectionManager", "Unable to add callbacks to MediaProjection service", paramCallback);
        }
      }
      return;
    }
    throw new IllegalArgumentException("callback must not be null");
  }
  
  public void stopActiveProjection()
  {
    try
    {
      mService.stopActiveProjection();
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("MediaProjectionManager", "Unable to stop the currently active media projection", localRemoteException);
    }
  }
  
  public static abstract class Callback
  {
    public Callback() {}
    
    public abstract void onStart(MediaProjectionInfo paramMediaProjectionInfo);
    
    public abstract void onStop(MediaProjectionInfo paramMediaProjectionInfo);
  }
  
  private static final class CallbackDelegate
    extends IMediaProjectionWatcherCallback.Stub
  {
    private MediaProjectionManager.Callback mCallback;
    private Handler mHandler;
    
    public CallbackDelegate(MediaProjectionManager.Callback paramCallback, Handler paramHandler)
    {
      mCallback = paramCallback;
      paramCallback = paramHandler;
      if (paramHandler == null) {
        paramCallback = new Handler();
      }
      mHandler = paramCallback;
    }
    
    public void onStart(final MediaProjectionInfo paramMediaProjectionInfo)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mCallback.onStart(paramMediaProjectionInfo);
        }
      });
    }
    
    public void onStop(final MediaProjectionInfo paramMediaProjectionInfo)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mCallback.onStop(paramMediaProjectionInfo);
        }
      });
    }
  }
}
