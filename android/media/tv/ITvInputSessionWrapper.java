package android.media.tv;

import android.content.Context;
import android.graphics.Rect;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.InputChannel;
import android.view.InputEvent;
import android.view.InputEventReceiver;
import android.view.Surface;
import com.android.internal.os.HandlerCaller;
import com.android.internal.os.HandlerCaller.Callback;
import com.android.internal.os.SomeArgs;

public class ITvInputSessionWrapper
  extends ITvInputSession.Stub
  implements HandlerCaller.Callback
{
  private static final int DO_APP_PRIVATE_COMMAND = 9;
  private static final int DO_CREATE_OVERLAY_VIEW = 10;
  private static final int DO_DISPATCH_SURFACE_CHANGED = 4;
  private static final int DO_RELAYOUT_OVERLAY_VIEW = 11;
  private static final int DO_RELEASE = 1;
  private static final int DO_REMOVE_OVERLAY_VIEW = 12;
  private static final int DO_SELECT_TRACK = 8;
  private static final int DO_SET_CAPTION_ENABLED = 7;
  private static final int DO_SET_MAIN = 2;
  private static final int DO_SET_STREAM_VOLUME = 5;
  private static final int DO_SET_SURFACE = 3;
  private static final int DO_START_RECORDING = 20;
  private static final int DO_STOP_RECORDING = 21;
  private static final int DO_TIME_SHIFT_ENABLE_POSITION_TRACKING = 19;
  private static final int DO_TIME_SHIFT_PAUSE = 15;
  private static final int DO_TIME_SHIFT_PLAY = 14;
  private static final int DO_TIME_SHIFT_RESUME = 16;
  private static final int DO_TIME_SHIFT_SEEK_TO = 17;
  private static final int DO_TIME_SHIFT_SET_PLAYBACK_PARAMS = 18;
  private static final int DO_TUNE = 6;
  private static final int DO_UNBLOCK_CONTENT = 13;
  private static final int EXECUTE_MESSAGE_TIMEOUT_LONG_MILLIS = 5000;
  private static final int EXECUTE_MESSAGE_TIMEOUT_SHORT_MILLIS = 50;
  private static final int EXECUTE_MESSAGE_TUNE_TIMEOUT_MILLIS = 2000;
  private static final String TAG = "TvInputSessionWrapper";
  private final HandlerCaller mCaller;
  private InputChannel mChannel;
  private final boolean mIsRecordingSession;
  private TvInputEventReceiver mReceiver;
  private TvInputService.RecordingSession mTvInputRecordingSessionImpl;
  private TvInputService.Session mTvInputSessionImpl;
  
  public ITvInputSessionWrapper(Context paramContext, TvInputService.RecordingSession paramRecordingSession)
  {
    mIsRecordingSession = true;
    mCaller = new HandlerCaller(paramContext, null, this, true);
    mTvInputRecordingSessionImpl = paramRecordingSession;
  }
  
  public ITvInputSessionWrapper(Context paramContext, TvInputService.Session paramSession, InputChannel paramInputChannel)
  {
    mIsRecordingSession = false;
    mCaller = new HandlerCaller(paramContext, null, this, true);
    mTvInputSessionImpl = paramSession;
    mChannel = paramInputChannel;
    if (paramInputChannel != null) {
      mReceiver = new TvInputEventReceiver(paramInputChannel, paramContext.getMainLooper());
    }
  }
  
  public void appPrivateCommand(String paramString, Bundle paramBundle)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageOO(9, paramString, paramBundle));
  }
  
  public void createOverlayView(IBinder paramIBinder, Rect paramRect)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageOO(10, paramIBinder, paramRect));
  }
  
  public void dispatchSurfaceChanged(int paramInt1, int paramInt2, int paramInt3)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageIIII(4, paramInt1, paramInt2, paramInt3, 0));
  }
  
  public void executeMessage(Message paramMessage)
  {
    if (((mIsRecordingSession) && (mTvInputRecordingSessionImpl == null)) || ((!mIsRecordingSession) && (mTvInputSessionImpl == null))) {
      return;
    }
    long l = System.nanoTime();
    Object localObject;
    switch (what)
    {
    default: 
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Unhandled message code: ");
      ((StringBuilder)localObject).append(what);
      Log.w("TvInputSessionWrapper", ((StringBuilder)localObject).toString());
      break;
    case 21: 
      mTvInputRecordingSessionImpl.stopRecording();
      break;
    case 20: 
      mTvInputRecordingSessionImpl.startRecording((Uri)obj);
      break;
    case 19: 
      mTvInputSessionImpl.timeShiftEnablePositionTracking(((Boolean)obj).booleanValue());
      break;
    case 18: 
      mTvInputSessionImpl.timeShiftSetPlaybackParams((PlaybackParams)obj);
      break;
    case 17: 
      mTvInputSessionImpl.timeShiftSeekTo(((Long)obj).longValue());
      break;
    case 16: 
      mTvInputSessionImpl.timeShiftResume();
      break;
    case 15: 
      mTvInputSessionImpl.timeShiftPause();
      break;
    case 14: 
      mTvInputSessionImpl.timeShiftPlay((Uri)obj);
      break;
    case 13: 
      mTvInputSessionImpl.unblockContent((String)obj);
      break;
    case 12: 
      mTvInputSessionImpl.removeOverlayView(true);
      break;
    case 11: 
      mTvInputSessionImpl.relayoutOverlayView((Rect)obj);
      break;
    case 10: 
      localObject = (SomeArgs)obj;
      mTvInputSessionImpl.createOverlayView((IBinder)arg1, (Rect)arg2);
      ((SomeArgs)localObject).recycle();
      break;
    case 9: 
      localObject = (SomeArgs)obj;
      if (mIsRecordingSession) {
        mTvInputRecordingSessionImpl.appPrivateCommand((String)arg1, (Bundle)arg2);
      } else {
        mTvInputSessionImpl.appPrivateCommand((String)arg1, (Bundle)arg2);
      }
      ((SomeArgs)localObject).recycle();
      break;
    case 8: 
      localObject = (SomeArgs)obj;
      mTvInputSessionImpl.selectTrack(((Integer)arg1).intValue(), (String)arg2);
      ((SomeArgs)localObject).recycle();
      break;
    case 7: 
      mTvInputSessionImpl.setCaptionEnabled(((Boolean)obj).booleanValue());
      break;
    case 6: 
      localObject = (SomeArgs)obj;
      if (mIsRecordingSession) {
        mTvInputRecordingSessionImpl.tune((Uri)arg1, (Bundle)arg2);
      } else {
        mTvInputSessionImpl.tune((Uri)arg1, (Bundle)arg2);
      }
      ((SomeArgs)localObject).recycle();
      break;
    case 5: 
      mTvInputSessionImpl.setStreamVolume(((Float)obj).floatValue());
      break;
    case 4: 
      localObject = (SomeArgs)obj;
      mTvInputSessionImpl.dispatchSurfaceChanged(argi1, argi2, argi3);
      ((SomeArgs)localObject).recycle();
      break;
    case 3: 
      mTvInputSessionImpl.setSurface((Surface)obj);
      break;
    case 2: 
      mTvInputSessionImpl.setMain(((Boolean)obj).booleanValue());
      break;
    case 1: 
      if (mIsRecordingSession)
      {
        mTvInputRecordingSessionImpl.release();
        mTvInputRecordingSessionImpl = null;
      }
      else
      {
        mTvInputSessionImpl.release();
        mTvInputSessionImpl = null;
        if (mReceiver != null)
        {
          mReceiver.dispose();
          mReceiver = null;
        }
        if (mChannel != null)
        {
          mChannel.dispose();
          mChannel = null;
        }
      }
      break;
    }
    l = (System.nanoTime() - l) / 1000000L;
    if (l > 50L)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Handling message (");
      ((StringBuilder)localObject).append(what);
      ((StringBuilder)localObject).append(") took too long time (duration=");
      ((StringBuilder)localObject).append(l);
      ((StringBuilder)localObject).append("ms)");
      Log.w("TvInputSessionWrapper", ((StringBuilder)localObject).toString());
      if ((what == 6) && (l > 2000L))
      {
        paramMessage = new StringBuilder();
        paramMessage.append("Too much time to handle tune request. (");
        paramMessage.append(l);
        paramMessage.append("ms > ");
        paramMessage.append(2000);
        paramMessage.append("ms) Consider handling the tune request in a separate thread.");
        throw new RuntimeException(paramMessage.toString());
      }
      if (l > 5000L)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Too much time to handle a request. (type=");
        ((StringBuilder)localObject).append(what);
        ((StringBuilder)localObject).append(", ");
        ((StringBuilder)localObject).append(l);
        ((StringBuilder)localObject).append("ms > ");
        ((StringBuilder)localObject).append(5000);
        ((StringBuilder)localObject).append("ms).");
        throw new RuntimeException(((StringBuilder)localObject).toString());
      }
    }
  }
  
  public void relayoutOverlayView(Rect paramRect)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageO(11, paramRect));
  }
  
  public void release()
  {
    if (!mIsRecordingSession) {
      mTvInputSessionImpl.scheduleOverlayViewCleanup();
    }
    mCaller.executeOrSendMessage(mCaller.obtainMessage(1));
  }
  
  public void removeOverlayView()
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessage(12));
  }
  
  public void selectTrack(int paramInt, String paramString)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageOO(8, Integer.valueOf(paramInt), paramString));
  }
  
  public void setCaptionEnabled(boolean paramBoolean)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageO(7, Boolean.valueOf(paramBoolean)));
  }
  
  public void setMain(boolean paramBoolean)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageO(2, Boolean.valueOf(paramBoolean)));
  }
  
  public void setSurface(Surface paramSurface)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageO(3, paramSurface));
  }
  
  public final void setVolume(float paramFloat)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageO(5, Float.valueOf(paramFloat)));
  }
  
  public void startRecording(Uri paramUri)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageO(20, paramUri));
  }
  
  public void stopRecording()
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessage(21));
  }
  
  public void timeShiftEnablePositionTracking(boolean paramBoolean)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageO(19, Boolean.valueOf(paramBoolean)));
  }
  
  public void timeShiftPause()
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessage(15));
  }
  
  public void timeShiftPlay(Uri paramUri)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageO(14, paramUri));
  }
  
  public void timeShiftResume()
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessage(16));
  }
  
  public void timeShiftSeekTo(long paramLong)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageO(17, Long.valueOf(paramLong)));
  }
  
  public void timeShiftSetPlaybackParams(PlaybackParams paramPlaybackParams)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageO(18, paramPlaybackParams));
  }
  
  public void tune(Uri paramUri, Bundle paramBundle)
  {
    mCaller.removeMessages(6);
    mCaller.executeOrSendMessage(mCaller.obtainMessageOO(6, paramUri, paramBundle));
  }
  
  public void unblockContent(String paramString)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageO(13, paramString));
  }
  
  private final class TvInputEventReceiver
    extends InputEventReceiver
  {
    public TvInputEventReceiver(InputChannel paramInputChannel, Looper paramLooper)
    {
      super(paramLooper);
    }
    
    public void onInputEvent(InputEvent paramInputEvent, int paramInt)
    {
      TvInputService.Session localSession = mTvInputSessionImpl;
      boolean bool = false;
      if (localSession == null)
      {
        finishInputEvent(paramInputEvent, false);
        return;
      }
      paramInt = mTvInputSessionImpl.dispatchInputEvent(paramInputEvent, this);
      if (paramInt != -1)
      {
        if (paramInt == 1) {
          bool = true;
        }
        finishInputEvent(paramInputEvent, bool);
      }
    }
  }
}
