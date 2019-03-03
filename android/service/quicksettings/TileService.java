package android.service.quicksettings;

import android.annotation.SystemApi;
import android.app.Dialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Icon;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.Window;

public class TileService
  extends Service
{
  public static final String ACTION_QS_TILE = "android.service.quicksettings.action.QS_TILE";
  public static final String ACTION_QS_TILE_PREFERENCES = "android.service.quicksettings.action.QS_TILE_PREFERENCES";
  public static final String ACTION_REQUEST_LISTENING = "android.service.quicksettings.action.REQUEST_LISTENING";
  public static final String EXTRA_SERVICE = "service";
  public static final String EXTRA_STATE = "state";
  public static final String EXTRA_TOKEN = "token";
  public static final String META_DATA_ACTIVE_TILE = "android.service.quicksettings.ACTIVE_TILE";
  private final H mHandler = new H(Looper.getMainLooper());
  private boolean mListening = false;
  private IQSService mService;
  private Tile mTile;
  private IBinder mTileToken;
  private IBinder mToken;
  private Runnable mUnlockRunnable;
  
  public TileService() {}
  
  public static boolean isQuickSettingsSupported()
  {
    return Resources.getSystem().getBoolean(17957012);
  }
  
  public static final void requestListeningState(Context paramContext, ComponentName paramComponentName)
  {
    Intent localIntent = new Intent("android.service.quicksettings.action.REQUEST_LISTENING");
    localIntent.putExtra("android.intent.extra.COMPONENT_NAME", paramComponentName);
    localIntent.setPackage("com.android.systemui");
    paramContext.sendBroadcast(localIntent, "android.permission.BIND_QUICK_SETTINGS_TILE");
  }
  
  public final Tile getQsTile()
  {
    return mTile;
  }
  
  public final boolean isLocked()
  {
    try
    {
      boolean bool = mService.isLocked();
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return true;
  }
  
  public final boolean isSecure()
  {
    try
    {
      boolean bool = mService.isSecure();
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return true;
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    mService = IQSService.Stub.asInterface(paramIntent.getIBinderExtra("service"));
    mTileToken = paramIntent.getIBinderExtra("token");
    try
    {
      mTile = mService.getTile(mTileToken);
      if (mTile != null)
      {
        mTile.setService(mService, mTileToken);
        mHandler.sendEmptyMessage(7);
      }
      new IQSTileService.Stub()
      {
        public void onClick(IBinder paramAnonymousIBinder)
          throws RemoteException
        {
          mHandler.obtainMessage(5, paramAnonymousIBinder).sendToTarget();
        }
        
        public void onStartListening()
          throws RemoteException
        {
          mHandler.sendEmptyMessage(1);
        }
        
        public void onStopListening()
          throws RemoteException
        {
          mHandler.sendEmptyMessage(2);
        }
        
        public void onTileAdded()
          throws RemoteException
        {
          mHandler.sendEmptyMessage(3);
        }
        
        public void onTileRemoved()
          throws RemoteException
        {
          mHandler.sendEmptyMessage(4);
        }
        
        public void onUnlockComplete()
          throws RemoteException
        {
          mHandler.sendEmptyMessage(6);
        }
      };
    }
    catch (RemoteException paramIntent)
    {
      throw new RuntimeException("Unable to reach IQSService", paramIntent);
    }
  }
  
  public void onClick() {}
  
  public void onDestroy()
  {
    if (mListening)
    {
      onStopListening();
      mListening = false;
    }
    super.onDestroy();
  }
  
  public void onStartListening() {}
  
  public void onStopListening() {}
  
  public void onTileAdded() {}
  
  public void onTileRemoved() {}
  
  @SystemApi
  public final void setStatusIcon(Icon paramIcon, String paramString)
  {
    if (mService != null) {
      try
      {
        mService.updateStatusIcon(mTileToken, paramIcon, paramString);
      }
      catch (RemoteException paramIcon) {}
    }
  }
  
  public final void showDialog(Dialog paramDialog)
  {
    getWindowgetAttributestoken = mToken;
    paramDialog.getWindow().setType(2035);
    paramDialog.getWindow().getDecorView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener()
    {
      public void onViewAttachedToWindow(View paramAnonymousView) {}
      
      public void onViewDetachedFromWindow(View paramAnonymousView)
      {
        try
        {
          mService.onDialogHidden(mTileToken);
        }
        catch (RemoteException paramAnonymousView) {}
      }
    });
    paramDialog.show();
    try
    {
      mService.onShowDialog(mTileToken);
    }
    catch (RemoteException paramDialog) {}
  }
  
  public final void startActivityAndCollapse(Intent paramIntent)
  {
    startActivity(paramIntent);
    try
    {
      mService.onStartActivity(mTileToken);
    }
    catch (RemoteException paramIntent) {}
  }
  
  public final void unlockAndRun(Runnable paramRunnable)
  {
    mUnlockRunnable = paramRunnable;
    try
    {
      mService.startUnlockAndRun(mTileToken);
    }
    catch (RemoteException paramRunnable) {}
  }
  
  private class H
    extends Handler
  {
    private static final int MSG_START_LISTENING = 1;
    private static final int MSG_START_SUCCESS = 7;
    private static final int MSG_STOP_LISTENING = 2;
    private static final int MSG_TILE_ADDED = 3;
    private static final int MSG_TILE_CLICKED = 5;
    private static final int MSG_TILE_REMOVED = 4;
    private static final int MSG_UNLOCK_COMPLETE = 6;
    
    public H(Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      switch (what)
      {
      default: 
        break;
      case 7: 
        try
        {
          mService.onStartSuccessful(mTileToken);
        }
        catch (RemoteException paramMessage) {}
      case 6: 
        if (mUnlockRunnable != null) {
          mUnlockRunnable.run();
        }
        break;
      case 5: 
        TileService.access$402(TileService.this, (IBinder)obj);
        onClick();
        break;
      case 4: 
        if (mListening)
        {
          TileService.access$302(TileService.this, false);
          onStopListening();
        }
        onTileRemoved();
        break;
      case 3: 
        onTileAdded();
        break;
      case 2: 
        if (mListening)
        {
          TileService.access$302(TileService.this, false);
          onStopListening();
        }
        break;
      case 1: 
        if (!mListening)
        {
          TileService.access$302(TileService.this, true);
          onStartListening();
        }
        break;
      }
    }
  }
}
