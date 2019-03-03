package android.media;

import android.annotation.SystemApi;
import android.os.Binder;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.util.Log;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

public final class AudioPlaybackConfiguration
  implements Parcelable
{
  public static final Parcelable.Creator<AudioPlaybackConfiguration> CREATOR = new Parcelable.Creator()
  {
    public AudioPlaybackConfiguration createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AudioPlaybackConfiguration(paramAnonymousParcel, null);
    }
    
    public AudioPlaybackConfiguration[] newArray(int paramAnonymousInt)
    {
      return new AudioPlaybackConfiguration[paramAnonymousInt];
    }
  };
  private static final boolean DEBUG = false;
  public static final int PLAYER_PIID_INVALID = -1;
  public static final int PLAYER_PIID_UNASSIGNED = 0;
  @SystemApi
  public static final int PLAYER_STATE_IDLE = 1;
  @SystemApi
  public static final int PLAYER_STATE_PAUSED = 3;
  @SystemApi
  public static final int PLAYER_STATE_RELEASED = 0;
  @SystemApi
  public static final int PLAYER_STATE_STARTED = 2;
  @SystemApi
  public static final int PLAYER_STATE_STOPPED = 4;
  @SystemApi
  public static final int PLAYER_STATE_UNKNOWN = -1;
  public static final int PLAYER_TYPE_AAUDIO = 13;
  public static final int PLAYER_TYPE_EXTERNAL_PROXY = 15;
  public static final int PLAYER_TYPE_HW_SOURCE = 14;
  @SystemApi
  public static final int PLAYER_TYPE_JAM_AUDIOTRACK = 1;
  @SystemApi
  public static final int PLAYER_TYPE_JAM_MEDIAPLAYER = 2;
  @SystemApi
  public static final int PLAYER_TYPE_JAM_SOUNDPOOL = 3;
  @SystemApi
  public static final int PLAYER_TYPE_SLES_AUDIOPLAYER_BUFFERQUEUE = 11;
  @SystemApi
  public static final int PLAYER_TYPE_SLES_AUDIOPLAYER_URI_FD = 12;
  @SystemApi
  public static final int PLAYER_TYPE_UNKNOWN = -1;
  public static final int PLAYER_UPID_INVALID = -1;
  private static final String TAG = new String("AudioPlaybackConfiguration");
  public static PlayerDeathMonitor sPlayerDeathMonitor;
  private int mClientPid;
  private int mClientUid;
  private IPlayerShell mIPlayerShell;
  private AudioAttributes mPlayerAttr;
  private final int mPlayerIId;
  private int mPlayerState;
  private int mPlayerType;
  
  private AudioPlaybackConfiguration(int paramInt)
  {
    mPlayerIId = paramInt;
    mIPlayerShell = null;
  }
  
  public AudioPlaybackConfiguration(PlayerBase.PlayerIdCard paramPlayerIdCard, int paramInt1, int paramInt2, int paramInt3)
  {
    mPlayerIId = paramInt1;
    mPlayerType = mPlayerType;
    mClientUid = paramInt2;
    mClientPid = paramInt3;
    mPlayerState = 1;
    mPlayerAttr = mAttributes;
    if ((sPlayerDeathMonitor != null) && (mIPlayer != null)) {
      mIPlayerShell = new IPlayerShell(this, mIPlayer);
    } else {
      mIPlayerShell = null;
    }
  }
  
  private AudioPlaybackConfiguration(Parcel paramParcel)
  {
    mPlayerIId = paramParcel.readInt();
    mPlayerType = paramParcel.readInt();
    mClientUid = paramParcel.readInt();
    mClientPid = paramParcel.readInt();
    mPlayerState = paramParcel.readInt();
    mPlayerAttr = ((AudioAttributes)AudioAttributes.CREATOR.createFromParcel(paramParcel));
    IPlayer localIPlayer = IPlayer.Stub.asInterface(paramParcel.readStrongBinder());
    paramParcel = null;
    if (localIPlayer != null) {
      paramParcel = new IPlayerShell(null, localIPlayer);
    }
    mIPlayerShell = paramParcel;
  }
  
  public static AudioPlaybackConfiguration anonymizedCopy(AudioPlaybackConfiguration paramAudioPlaybackConfiguration)
  {
    AudioPlaybackConfiguration localAudioPlaybackConfiguration = new AudioPlaybackConfiguration(mPlayerIId);
    mPlayerState = mPlayerState;
    mPlayerAttr = new AudioAttributes.Builder().setUsage(mPlayerAttr.getUsage()).setContentType(mPlayerAttr.getContentType()).setFlags(mPlayerAttr.getFlags()).build();
    mPlayerType = -1;
    mClientUid = -1;
    mClientPid = -1;
    mIPlayerShell = null;
    return localAudioPlaybackConfiguration;
  }
  
  private void playerDied()
  {
    if (sPlayerDeathMonitor != null) {
      sPlayerDeathMonitor.playerDeath(mPlayerIId);
    }
  }
  
  public static String toLogFriendlyPlayerState(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "unknown player state - FIXME";
    case 4: 
      return "stopped";
    case 3: 
      return "paused";
    case 2: 
      return "started";
    case 1: 
      return "idle";
    case 0: 
      return "released";
    }
    return "unknown";
  }
  
  public static String toLogFriendlyPlayerType(int paramInt)
  {
    if (paramInt != -1)
    {
      switch (paramInt)
      {
      default: 
        switch (paramInt)
        {
        default: 
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("unknown player type ");
          localStringBuilder.append(paramInt);
          localStringBuilder.append(" - FIXME");
          return localStringBuilder.toString();
        case 15: 
          return "external proxy";
        case 14: 
          return "hardware source";
        case 13: 
          return "AAudio";
        case 12: 
          return "OpenSL ES AudioPlayer (URI/FD)";
        }
        return "OpenSL ES AudioPlayer (Buffer Queue)";
      case 3: 
        return "android.media.SoundPool";
      case 2: 
        return "android.media.MediaPlayer";
      }
      return "android.media.AudioTrack";
    }
    return "unknown";
  }
  
  public static String toLogFriendlyString(AudioPlaybackConfiguration paramAudioPlaybackConfiguration)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ID:");
    localStringBuilder.append(mPlayerIId);
    localStringBuilder.append(" -- type:");
    localStringBuilder.append(toLogFriendlyPlayerType(mPlayerType));
    localStringBuilder.append(" -- u/pid:");
    localStringBuilder.append(mClientUid);
    localStringBuilder.append("/");
    localStringBuilder.append(mClientPid);
    localStringBuilder.append(" -- state:");
    localStringBuilder.append(toLogFriendlyPlayerState(mPlayerState));
    localStringBuilder.append(" -- attr:");
    localStringBuilder.append(mPlayerAttr);
    return new String(localStringBuilder.toString());
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void dump(PrintWriter paramPrintWriter)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("  ");
    localStringBuilder.append(toLogFriendlyString(this));
    paramPrintWriter.println(localStringBuilder.toString());
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && ((paramObject instanceof AudioPlaybackConfiguration)))
    {
      paramObject = (AudioPlaybackConfiguration)paramObject;
      if ((mPlayerIId != mPlayerIId) || (mPlayerType != mPlayerType) || (mClientUid != mClientUid) || (mClientPid != mClientPid)) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public AudioAttributes getAudioAttributes()
  {
    return mPlayerAttr;
  }
  
  @SystemApi
  public int getClientPid()
  {
    return mClientPid;
  }
  
  @SystemApi
  public int getClientUid()
  {
    return mClientUid;
  }
  
  IPlayer getIPlayer()
  {
    try
    {
      Object localObject1 = mIPlayerShell;
      if (localObject1 == null) {
        localObject1 = null;
      } else {
        localObject1 = ((IPlayerShell)localObject1).getIPlayer();
      }
      return localObject1;
    }
    finally {}
  }
  
  @SystemApi
  public int getPlayerInterfaceId()
  {
    return mPlayerIId;
  }
  
  @SystemApi
  public PlayerProxy getPlayerProxy()
  {
    try
    {
      Object localObject1 = mIPlayerShell;
      if (localObject1 == null) {
        localObject1 = null;
      } else {
        localObject1 = new PlayerProxy(this);
      }
      return localObject1;
    }
    finally {}
  }
  
  @SystemApi
  public int getPlayerState()
  {
    return mPlayerState;
  }
  
  @SystemApi
  public int getPlayerType()
  {
    switch (mPlayerType)
    {
    default: 
      return mPlayerType;
    }
    return -1;
  }
  
  public boolean handleAudioAttributesEvent(AudioAttributes paramAudioAttributes)
  {
    boolean bool = paramAudioAttributes.equals(mPlayerAttr);
    mPlayerAttr = paramAudioAttributes;
    return bool ^ true;
  }
  
  public boolean handleStateEvent(int paramInt)
  {
    try
    {
      boolean bool;
      if (mPlayerState != paramInt) {
        bool = true;
      } else {
        bool = false;
      }
      mPlayerState = paramInt;
      if ((bool) && (paramInt == 0) && (mIPlayerShell != null))
      {
        mIPlayerShell.release();
        mIPlayerShell = null;
      }
      return bool;
    }
    finally {}
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(mPlayerIId), Integer.valueOf(mPlayerType), Integer.valueOf(mClientUid), Integer.valueOf(mClientPid) });
  }
  
  public void init()
  {
    try
    {
      if (mIPlayerShell != null) {
        mIPlayerShell.monitorDeath();
      }
      return;
    }
    finally {}
  }
  
  public boolean isActive()
  {
    return mPlayerState == 2;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mPlayerIId);
    paramParcel.writeInt(mPlayerType);
    paramParcel.writeInt(mClientUid);
    paramParcel.writeInt(mClientPid);
    paramParcel.writeInt(mPlayerState);
    mPlayerAttr.writeToParcel(paramParcel, 0);
    try
    {
      Object localObject = mIPlayerShell;
      if (localObject == null) {
        localObject = null;
      } else {
        localObject = ((IPlayerShell)localObject).getIPlayer();
      }
      paramParcel.writeStrongInterface((IInterface)localObject);
      return;
    }
    finally {}
  }
  
  static final class IPlayerShell
    implements IBinder.DeathRecipient
  {
    private volatile IPlayer mIPlayer;
    final AudioPlaybackConfiguration mMonitor;
    
    IPlayerShell(AudioPlaybackConfiguration paramAudioPlaybackConfiguration, IPlayer paramIPlayer)
    {
      mMonitor = paramAudioPlaybackConfiguration;
      mIPlayer = paramIPlayer;
    }
    
    public void binderDied()
    {
      if (mMonitor != null) {
        mMonitor.playerDied();
      }
    }
    
    IPlayer getIPlayer()
    {
      return mIPlayer;
    }
    
    void monitorDeath()
    {
      try
      {
        Object localObject1 = mIPlayer;
        if (localObject1 == null) {
          return;
        }
        try
        {
          mIPlayer.asBinder().linkToDeath(this, 0);
        }
        catch (RemoteException localRemoteException)
        {
          if (mMonitor != null)
          {
            localObject1 = AudioPlaybackConfiguration.TAG;
            StringBuilder localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append("Could not link to client death for piid=");
            localStringBuilder.append(mMonitor.mPlayerIId);
            Log.w((String)localObject1, localStringBuilder.toString(), localRemoteException);
          }
          else
          {
            Log.w(AudioPlaybackConfiguration.TAG, "Could not link to client death", localRemoteException);
          }
        }
        return;
      }
      finally {}
    }
    
    void release()
    {
      try
      {
        IPlayer localIPlayer = mIPlayer;
        if (localIPlayer == null) {
          return;
        }
        mIPlayer.asBinder().unlinkToDeath(this, 0);
        mIPlayer = null;
        Binder.flushPendingCommands();
        return;
      }
      finally {}
    }
  }
  
  public static abstract interface PlayerDeathMonitor
  {
    public abstract void playerDeath(int paramInt);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface PlayerState {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface PlayerType {}
}
