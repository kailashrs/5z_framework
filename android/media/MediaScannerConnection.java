package android.media;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;

public class MediaScannerConnection
  implements ServiceConnection
{
  private static final String TAG = "MediaScannerConnection";
  private MediaScannerConnectionClient mClient;
  private boolean mConnected;
  private Context mContext;
  private final IMediaScannerListener.Stub mListener = new IMediaScannerListener.Stub()
  {
    public void scanCompleted(String paramAnonymousString, Uri paramAnonymousUri)
    {
      MediaScannerConnection.MediaScannerConnectionClient localMediaScannerConnectionClient = mClient;
      if (localMediaScannerConnectionClient != null) {
        localMediaScannerConnectionClient.onScanCompleted(paramAnonymousString, paramAnonymousUri);
      }
    }
  };
  private IMediaScannerService mService;
  
  public MediaScannerConnection(Context paramContext, MediaScannerConnectionClient paramMediaScannerConnectionClient)
  {
    mContext = paramContext;
    mClient = paramMediaScannerConnectionClient;
  }
  
  public static void scanFile(Context paramContext, String[] paramArrayOfString1, String[] paramArrayOfString2, OnScanCompletedListener paramOnScanCompletedListener)
  {
    paramArrayOfString1 = new ClientProxy(paramArrayOfString1, paramArrayOfString2, paramOnScanCompletedListener);
    paramContext = new MediaScannerConnection(paramContext, paramArrayOfString1);
    mConnection = paramContext;
    paramContext.connect();
  }
  
  public void connect()
  {
    try
    {
      if (!mConnected)
      {
        Intent localIntent = new android/content/Intent;
        localIntent.<init>(IMediaScannerService.class.getName());
        ComponentName localComponentName = new android/content/ComponentName;
        localComponentName.<init>("com.android.providers.media", "com.android.providers.media.MediaScannerService");
        localIntent.setComponent(localComponentName);
        mContext.bindService(localIntent, this, 1);
        mConnected = true;
      }
      return;
    }
    finally {}
  }
  
  public void disconnect()
  {
    try
    {
      boolean bool = mConnected;
      if (bool)
      {
        try
        {
          mContext.unbindService(this);
          if ((mClient instanceof ClientProxy)) {
            mClient = null;
          }
          mService = null;
        }
        catch (IllegalArgumentException localIllegalArgumentException) {}
        mConnected = false;
      }
      return;
    }
    finally {}
  }
  
  public boolean isConnected()
  {
    try
    {
      if (mService != null)
      {
        bool = mConnected;
        if (bool)
        {
          bool = true;
          break label25;
        }
      }
      boolean bool = false;
      label25:
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
  {
    try
    {
      mService = IMediaScannerService.Stub.asInterface(paramIBinder);
      if ((mService != null) && (mClient != null)) {
        mClient.onMediaScannerConnected();
      }
      return;
    }
    finally {}
  }
  
  public void onServiceDisconnected(ComponentName paramComponentName)
  {
    try
    {
      mService = null;
      return;
    }
    finally {}
  }
  
  public void scanFile(String paramString1, String paramString2)
  {
    try
    {
      if (mService != null)
      {
        boolean bool = mConnected;
        if (bool)
        {
          try
          {
            mService.requestScanFile(paramString1, paramString2, mListener);
          }
          catch (RemoteException paramString1) {}
          return;
        }
      }
      paramString1 = new java/lang/IllegalStateException;
      paramString1.<init>("not connected to MediaScannerService");
      throw paramString1;
    }
    finally {}
  }
  
  static class ClientProxy
    implements MediaScannerConnection.MediaScannerConnectionClient
  {
    final MediaScannerConnection.OnScanCompletedListener mClient;
    MediaScannerConnection mConnection;
    final String[] mMimeTypes;
    int mNextPath;
    final String[] mPaths;
    
    ClientProxy(String[] paramArrayOfString1, String[] paramArrayOfString2, MediaScannerConnection.OnScanCompletedListener paramOnScanCompletedListener)
    {
      mPaths = paramArrayOfString1;
      mMimeTypes = paramArrayOfString2;
      mClient = paramOnScanCompletedListener;
    }
    
    public void onMediaScannerConnected()
    {
      scanNextPath();
    }
    
    public void onScanCompleted(String paramString, Uri paramUri)
    {
      if (mClient != null) {
        mClient.onScanCompleted(paramString, paramUri);
      }
      scanNextPath();
    }
    
    void scanNextPath()
    {
      int i = mNextPath;
      int j = mPaths.length;
      String str = null;
      if (i >= j)
      {
        mConnection.disconnect();
        mConnection = null;
        return;
      }
      if (mMimeTypes != null) {
        str = mMimeTypes[mNextPath];
      }
      mConnection.scanFile(mPaths[mNextPath], str);
      mNextPath += 1;
    }
  }
  
  public static abstract interface MediaScannerConnectionClient
    extends MediaScannerConnection.OnScanCompletedListener
  {
    public abstract void onMediaScannerConnected();
    
    public abstract void onScanCompleted(String paramString, Uri paramUri);
  }
  
  public static abstract interface OnScanCompletedListener
  {
    public abstract void onScanCompleted(String paramString, Uri paramUri);
  }
}
