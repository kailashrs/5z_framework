package android.media;

import android.content.Context;
import android.net.Uri;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.util.Log;
import java.util.LinkedList;

public class AsyncPlayer
{
  private static final int PLAY = 1;
  private static final int STOP = 2;
  private static final boolean mDebug = false;
  private final LinkedList<Command> mCmdQueue = new LinkedList();
  private MediaPlayer mPlayer;
  private int mState = 2;
  private String mTag;
  private Thread mThread;
  private PowerManager.WakeLock mWakeLock;
  
  public AsyncPlayer(String paramString)
  {
    if (paramString != null) {
      mTag = paramString;
    } else {
      mTag = "AsyncPlayer";
    }
  }
  
  private void acquireWakeLock()
  {
    if (mWakeLock != null) {
      mWakeLock.acquire();
    }
  }
  
  private void enqueueLocked(Command paramCommand)
  {
    mCmdQueue.add(paramCommand);
    if (mThread == null)
    {
      acquireWakeLock();
      mThread = new Thread();
      mThread.start();
    }
  }
  
  private void releaseWakeLock()
  {
    if (mWakeLock != null) {
      mWakeLock.release();
    }
  }
  
  private void startSound(Command paramCommand)
  {
    try
    {
      localObject = new android/media/MediaPlayer;
      ((MediaPlayer)localObject).<init>();
      ((MediaPlayer)localObject).setAudioAttributes(attributes);
      ((MediaPlayer)localObject).setDataSource(context, uri);
      ((MediaPlayer)localObject).setLooping(looping);
      ((MediaPlayer)localObject).prepare();
      ((MediaPlayer)localObject).start();
      if (mPlayer != null) {
        mPlayer.release();
      }
      mPlayer = ((MediaPlayer)localObject);
      long l = SystemClock.uptimeMillis() - requestTime;
      if (l > 1000L)
      {
        localObject = mTag;
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Notification sound delayed by ");
        localStringBuilder.append(l);
        localStringBuilder.append("msecs");
        Log.w((String)localObject, localStringBuilder.toString());
      }
    }
    catch (Exception localException)
    {
      Object localObject = mTag;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("error loading sound for ");
      localStringBuilder.append(uri);
      Log.w((String)localObject, localStringBuilder.toString(), localException);
    }
  }
  
  public void play(Context paramContext, Uri paramUri, boolean paramBoolean, int paramInt)
  {
    PlayerBase.deprecateStreamTypeForPlayback(paramInt, "AsyncPlayer", "play()");
    if ((paramContext != null) && (paramUri != null))
    {
      try
      {
        AudioAttributes.Builder localBuilder = new android/media/AudioAttributes$Builder;
        localBuilder.<init>();
        play(paramContext, paramUri, paramBoolean, localBuilder.setInternalLegacyStreamType(paramInt).build());
      }
      catch (IllegalArgumentException paramContext)
      {
        Log.e(mTag, "Call to deprecated AsyncPlayer.play() method caused:", paramContext);
      }
      return;
    }
  }
  
  public void play(Context arg1, Uri paramUri, boolean paramBoolean, AudioAttributes paramAudioAttributes)
    throws IllegalArgumentException
  {
    if ((??? != null) && (paramUri != null) && (paramAudioAttributes != null))
    {
      Command localCommand = new Command(null);
      requestTime = SystemClock.uptimeMillis();
      code = 1;
      context = ???;
      uri = paramUri;
      looping = paramBoolean;
      attributes = paramAudioAttributes;
      synchronized (mCmdQueue)
      {
        enqueueLocked(localCommand);
        mState = 1;
        return;
      }
    }
    throw new IllegalArgumentException("Illegal null AsyncPlayer.play() argument");
  }
  
  public void setUsesWakeLock(Context paramContext)
  {
    if ((mWakeLock == null) && (mThread == null))
    {
      mWakeLock = ((PowerManager)paramContext.getSystemService("power")).newWakeLock(1, mTag);
      return;
    }
    paramContext = new StringBuilder();
    paramContext.append("assertion failed mWakeLock=");
    paramContext.append(mWakeLock);
    paramContext.append(" mThread=");
    paramContext.append(mThread);
    throw new RuntimeException(paramContext.toString());
  }
  
  public void stop()
  {
    synchronized (mCmdQueue)
    {
      if (mState != 2)
      {
        Command localCommand = new android/media/AsyncPlayer$Command;
        localCommand.<init>(null);
        requestTime = SystemClock.uptimeMillis();
        code = 2;
        enqueueLocked(localCommand);
        mState = 2;
      }
      return;
    }
  }
  
  private static final class Command
  {
    AudioAttributes attributes;
    int code;
    Context context;
    boolean looping;
    long requestTime;
    Uri uri;
    
    private Command() {}
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("{ code=");
      localStringBuilder.append(code);
      localStringBuilder.append(" looping=");
      localStringBuilder.append(looping);
      localStringBuilder.append(" attr=");
      localStringBuilder.append(attributes);
      localStringBuilder.append(" uri=");
      localStringBuilder.append(uri);
      localStringBuilder.append(" }");
      return localStringBuilder.toString();
    }
  }
  
  private final class Thread
    extends Thread
  {
    Thread()
    {
      super();
    }
    
    public void run()
    {
      synchronized (mCmdQueue)
      {
        for (;;)
        {
          ??? = (AsyncPlayer.Command)mCmdQueue.removeFirst();
          switch (code)
          {
          default: 
            break;
          case 2: 
            if (mPlayer != null)
            {
              long l = SystemClock.uptimeMillis() - requestTime;
              if (l > 1000L)
              {
                ??? = mTag;
                ??? = new StringBuilder();
                ((StringBuilder)???).append("Notification stop delayed by ");
                ((StringBuilder)???).append(l);
                ((StringBuilder)???).append("msecs");
                Log.w((String)???, ((StringBuilder)???).toString());
              }
              mPlayer.stop();
              mPlayer.release();
              AsyncPlayer.access$302(AsyncPlayer.this, null);
            }
            else
            {
              Log.w(mTag, "STOP command without a player");
            }
            break;
          case 1: 
            AsyncPlayer.this.startSound((AsyncPlayer.Command)???);
          }
          synchronized (mCmdQueue)
          {
            if (mCmdQueue.size() == 0)
            {
              AsyncPlayer.access$402(AsyncPlayer.this, null);
              AsyncPlayer.this.releaseWakeLock();
              return;
            }
          }
        }
      }
    }
  }
}
