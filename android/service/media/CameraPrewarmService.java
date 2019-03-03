package android.service.media;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

public abstract class CameraPrewarmService
  extends Service
{
  public static final String ACTION_PREWARM = "android.service.media.CameraPrewarmService.ACTION_PREWARM";
  public static final int MSG_CAMERA_FIRED = 1;
  private boolean mCameraIntentFired;
  private final Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      if (what != 1) {
        super.handleMessage(paramAnonymousMessage);
      } else {
        CameraPrewarmService.access$002(CameraPrewarmService.this, true);
      }
    }
  };
  
  public CameraPrewarmService() {}
  
  public IBinder onBind(Intent paramIntent)
  {
    if ("android.service.media.CameraPrewarmService.ACTION_PREWARM".equals(paramIntent.getAction()))
    {
      onPrewarm();
      return new Messenger(mHandler).getBinder();
    }
    return null;
  }
  
  public abstract void onCooldown(boolean paramBoolean);
  
  public abstract void onPrewarm();
  
  public boolean onUnbind(Intent paramIntent)
  {
    if ("android.service.media.CameraPrewarmService.ACTION_PREWARM".equals(paramIntent.getAction())) {
      onCooldown(mCameraIntentFired);
    }
    return false;
  }
}
