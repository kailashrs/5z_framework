package com.android.internal.policy;

import android.app.KeyguardManager;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.session.MediaSessionManager;
import android.os.UserHandle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.FallbackEventHandler;
import android.view.KeyEvent;
import android.view.KeyEvent.DispatcherState;
import android.view.View;

public class PhoneFallbackEventHandler
  implements FallbackEventHandler
{
  private static final boolean DEBUG = false;
  private static String TAG = "PhoneFallbackEventHandler";
  AudioManager mAudioManager;
  Context mContext;
  KeyguardManager mKeyguardManager;
  MediaSessionManager mMediaSessionManager;
  SearchManager mSearchManager;
  TelephonyManager mTelephonyManager;
  View mView;
  
  public PhoneFallbackEventHandler(Context paramContext)
  {
    mContext = paramContext;
  }
  
  private void handleMediaKeyEvent(KeyEvent paramKeyEvent)
  {
    getMediaSessionManager().dispatchMediaKeyEventAsSystemService(paramKeyEvent);
  }
  
  private void handleVolumeKeyEvent(KeyEvent paramKeyEvent)
  {
    getMediaSessionManager().dispatchVolumeKeyEventAsSystemService(paramKeyEvent, Integer.MIN_VALUE);
  }
  
  private boolean isNotInstantAppAndKeyguardRestricted(KeyEvent.DispatcherState paramDispatcherState)
  {
    boolean bool;
    if ((!mContext.getPackageManager().isInstantApp()) && ((getKeyguardManager().inKeyguardRestrictedInputMode()) || (paramDispatcherState == null))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isUserSetupComplete()
  {
    ContentResolver localContentResolver = mContext.getContentResolver();
    boolean bool = false;
    if (Settings.Secure.getInt(localContentResolver, "user_setup_complete", 0) != 0) {
      bool = true;
    }
    return bool;
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    int i = paramKeyEvent.getAction();
    int j = paramKeyEvent.getKeyCode();
    if (i == 0) {
      return onKeyDown(j, paramKeyEvent);
    }
    return onKeyUp(j, paramKeyEvent);
  }
  
  AudioManager getAudioManager()
  {
    if (mAudioManager == null) {
      mAudioManager = ((AudioManager)mContext.getSystemService("audio"));
    }
    return mAudioManager;
  }
  
  KeyguardManager getKeyguardManager()
  {
    if (mKeyguardManager == null) {
      mKeyguardManager = ((KeyguardManager)mContext.getSystemService("keyguard"));
    }
    return mKeyguardManager;
  }
  
  MediaSessionManager getMediaSessionManager()
  {
    if (mMediaSessionManager == null) {
      mMediaSessionManager = ((MediaSessionManager)mContext.getSystemService("media_session"));
    }
    return mMediaSessionManager;
  }
  
  SearchManager getSearchManager()
  {
    if (mSearchManager == null) {
      mSearchManager = ((SearchManager)mContext.getSystemService("search"));
    }
    return mSearchManager;
  }
  
  TelephonyManager getTelephonyManager()
  {
    if (mTelephonyManager == null) {
      mTelephonyManager = ((TelephonyManager)mContext.getSystemService("phone"));
    }
    return mTelephonyManager;
  }
  
  boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    Object localObject1 = mView.getKeyDispatcherState();
    if (paramInt != 5)
    {
      if (paramInt != 27)
      {
        if ((paramInt != 79) && (paramInt != 130)) {
          if (paramInt != 164)
          {
            if (paramInt != 222) {
              switch (paramInt)
              {
              default: 
                switch (paramInt)
                {
                default: 
                  switch (paramInt)
                  {
                  }
                  break;
                case 85: 
                  if (getTelephonyManager().getCallState() == 0) {
                    break;
                  }
                  return true;
                case 84: 
                  if (isNotInstantAppAndKeyguardRestricted((KeyEvent.DispatcherState)localObject1)) {
                    break label452;
                  }
                  if (paramKeyEvent.getRepeatCount() == 0)
                  {
                    ((KeyEvent.DispatcherState)localObject1).startTracking(paramKeyEvent, this);
                  }
                  else if ((paramKeyEvent.isLongPress()) && (((KeyEvent.DispatcherState)localObject1).isTracking(paramKeyEvent)))
                  {
                    Object localObject2 = mContext.getResources().getConfiguration();
                    if ((keyboard == 1) || (hardKeyboardHidden == 2)) {
                      if (isUserSetupComplete())
                      {
                        localObject2 = new Intent("android.intent.action.SEARCH_LONG_PRESS");
                        ((Intent)localObject2).setFlags(268435456);
                        try
                        {
                          mView.performHapticFeedback(0);
                          sendCloseSystemWindows();
                          getSearchManager().stopSearch();
                          mContext.startActivity((Intent)localObject2);
                          ((KeyEvent.DispatcherState)localObject1).performedLongPress(paramKeyEvent);
                          return true;
                        }
                        catch (ActivityNotFoundException paramKeyEvent) {}
                      }
                      else
                      {
                        Log.i(TAG, "Not dispatching SEARCH long press because user setup is in progress.");
                      }
                    }
                  }
                  break;
                }
                break;
              }
            }
          }
          else
          {
            handleVolumeKeyEvent(paramKeyEvent);
            return true;
          }
        }
        handleMediaKeyEvent(paramKeyEvent);
        return true;
      }
      if (!isNotInstantAppAndKeyguardRestricted((KeyEvent.DispatcherState)localObject1))
      {
        if (paramKeyEvent.getRepeatCount() == 0)
        {
          ((KeyEvent.DispatcherState)localObject1).startTracking(paramKeyEvent, this);
        }
        else if ((paramKeyEvent.isLongPress()) && (((KeyEvent.DispatcherState)localObject1).isTracking(paramKeyEvent)))
        {
          ((KeyEvent.DispatcherState)localObject1).performedLongPress(paramKeyEvent);
          if (isUserSetupComplete())
          {
            mView.performHapticFeedback(0);
            sendCloseSystemWindows();
            localObject1 = new Intent("android.intent.action.CAMERA_BUTTON", null);
            ((Intent)localObject1).addFlags(268435456);
            ((Intent)localObject1).putExtra("android.intent.extra.KEY_EVENT", paramKeyEvent);
            mContext.sendOrderedBroadcastAsUser((Intent)localObject1, UserHandle.CURRENT_OR_SELF, null, null, null, 0, null, null);
          }
          else
          {
            Log.i(TAG, "Not dispatching CAMERA long press because user setup is in progress.");
          }
        }
        return true;
      }
    }
    else
    {
      if (!isNotInstantAppAndKeyguardRestricted((KeyEvent.DispatcherState)localObject1)) {
        break label454;
      }
    }
    label452:
    return false;
    label454:
    if (paramKeyEvent.getRepeatCount() == 0)
    {
      ((KeyEvent.DispatcherState)localObject1).startTracking(paramKeyEvent, this);
    }
    else if ((paramKeyEvent.isLongPress()) && (((KeyEvent.DispatcherState)localObject1).isTracking(paramKeyEvent)))
    {
      ((KeyEvent.DispatcherState)localObject1).performedLongPress(paramKeyEvent);
      if (isUserSetupComplete())
      {
        mView.performHapticFeedback(0);
        paramKeyEvent = new Intent("android.intent.action.VOICE_COMMAND");
        paramKeyEvent.setFlags(268435456);
        try
        {
          sendCloseSystemWindows();
          mContext.startActivity(paramKeyEvent);
        }
        catch (ActivityNotFoundException paramKeyEvent)
        {
          startCallActivity();
        }
      }
      else
      {
        Log.i(TAG, "Not starting call activity because user setup is in progress.");
      }
    }
    return true;
  }
  
  boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    KeyEvent.DispatcherState localDispatcherState = mView.getKeyDispatcherState();
    if (localDispatcherState != null) {
      localDispatcherState.handleUpEvent(paramKeyEvent);
    }
    if (paramInt != 5)
    {
      if (paramInt != 27)
      {
        if ((paramInt != 79) && (paramInt != 130)) {
          if (paramInt != 164)
          {
            if (paramInt != 222) {
              switch (paramInt)
              {
              default: 
                switch (paramInt)
                {
                default: 
                  switch (paramInt)
                  {
                  }
                  break;
                }
                break;
              }
            }
          }
          else
          {
            if (!paramKeyEvent.isCanceled()) {
              handleVolumeKeyEvent(paramKeyEvent);
            }
            return true;
          }
        }
        handleMediaKeyEvent(paramKeyEvent);
        return true;
      }
      else if (!isNotInstantAppAndKeyguardRestricted(localDispatcherState))
      {
        if (paramKeyEvent.isTracking()) {
          paramKeyEvent.isCanceled();
        }
        return true;
      }
    }
    else {
      if (!isNotInstantAppAndKeyguardRestricted(localDispatcherState)) {
        break label207;
      }
    }
    return false;
    label207:
    if ((paramKeyEvent.isTracking()) && (!paramKeyEvent.isCanceled())) {
      if (isUserSetupComplete()) {
        startCallActivity();
      } else {
        Log.i(TAG, "Not starting call activity because user setup is in progress.");
      }
    }
    return true;
  }
  
  public void preDispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    getAudioManager().preDispatchKeyEvent(paramKeyEvent, Integer.MIN_VALUE);
  }
  
  void sendCloseSystemWindows()
  {
    PhoneWindow.sendCloseSystemWindows(mContext, null);
  }
  
  public void setView(View paramView)
  {
    mView = paramView;
  }
  
  void startCallActivity()
  {
    sendCloseSystemWindows();
    Intent localIntent = new Intent("android.intent.action.CALL_BUTTON");
    localIntent.setFlags(268435456);
    try
    {
      mContext.startActivity(localIntent);
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      Log.w(TAG, "No activity found for android.intent.action.CALL_BUTTON.");
    }
  }
}
