package android.media.session;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.text.TextUtils;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public final class PlaybackState
  implements Parcelable
{
  public static final long ACTION_FAST_FORWARD = 64L;
  public static final long ACTION_PAUSE = 2L;
  public static final long ACTION_PLAY = 4L;
  public static final long ACTION_PLAY_FROM_MEDIA_ID = 1024L;
  public static final long ACTION_PLAY_FROM_SEARCH = 2048L;
  public static final long ACTION_PLAY_FROM_URI = 8192L;
  public static final long ACTION_PLAY_PAUSE = 512L;
  public static final long ACTION_PREPARE = 16384L;
  public static final long ACTION_PREPARE_FROM_MEDIA_ID = 32768L;
  public static final long ACTION_PREPARE_FROM_SEARCH = 65536L;
  public static final long ACTION_PREPARE_FROM_URI = 131072L;
  public static final long ACTION_REWIND = 8L;
  public static final long ACTION_SEEK_TO = 256L;
  public static final long ACTION_SET_RATING = 128L;
  public static final long ACTION_SKIP_TO_NEXT = 32L;
  public static final long ACTION_SKIP_TO_PREVIOUS = 16L;
  public static final long ACTION_SKIP_TO_QUEUE_ITEM = 4096L;
  public static final long ACTION_STOP = 1L;
  public static final Parcelable.Creator<PlaybackState> CREATOR = new Parcelable.Creator()
  {
    public PlaybackState createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PlaybackState(paramAnonymousParcel, null);
    }
    
    public PlaybackState[] newArray(int paramAnonymousInt)
    {
      return new PlaybackState[paramAnonymousInt];
    }
  };
  public static final long PLAYBACK_POSITION_UNKNOWN = -1L;
  public static final int STATE_BUFFERING = 6;
  public static final int STATE_CONNECTING = 8;
  public static final int STATE_ERROR = 7;
  public static final int STATE_FAST_FORWARDING = 4;
  public static final int STATE_NONE = 0;
  public static final int STATE_PAUSED = 2;
  public static final int STATE_PLAYING = 3;
  public static final int STATE_REWINDING = 5;
  public static final int STATE_SKIPPING_TO_NEXT = 10;
  public static final int STATE_SKIPPING_TO_PREVIOUS = 9;
  public static final int STATE_SKIPPING_TO_QUEUE_ITEM = 11;
  public static final int STATE_STOPPED = 1;
  private static final String TAG = "PlaybackState";
  private final long mActions;
  private final long mActiveItemId;
  private final long mBufferedPosition;
  private List<CustomAction> mCustomActions;
  private final CharSequence mErrorMessage;
  private final Bundle mExtras;
  private final long mPosition;
  private final float mSpeed;
  private final int mState;
  private final long mUpdateTime;
  
  private PlaybackState(int paramInt, long paramLong1, long paramLong2, float paramFloat, long paramLong3, long paramLong4, List<CustomAction> paramList, long paramLong5, CharSequence paramCharSequence, Bundle paramBundle)
  {
    mState = paramInt;
    mPosition = paramLong1;
    mSpeed = paramFloat;
    mUpdateTime = paramLong2;
    mBufferedPosition = paramLong3;
    mActions = paramLong4;
    mCustomActions = new ArrayList(paramList);
    mActiveItemId = paramLong5;
    mErrorMessage = paramCharSequence;
    mExtras = paramBundle;
  }
  
  private PlaybackState(Parcel paramParcel)
  {
    mState = paramParcel.readInt();
    mPosition = paramParcel.readLong();
    mSpeed = paramParcel.readFloat();
    mUpdateTime = paramParcel.readLong();
    mBufferedPosition = paramParcel.readLong();
    mActions = paramParcel.readLong();
    mCustomActions = paramParcel.createTypedArrayList(CustomAction.CREATOR);
    mActiveItemId = paramParcel.readLong();
    mErrorMessage = paramParcel.readCharSequence();
    mExtras = paramParcel.readBundle();
  }
  
  private static long getActionForRccFlag(int paramInt)
  {
    if (paramInt != 4)
    {
      if (paramInt != 8)
      {
        if (paramInt != 16)
        {
          if (paramInt != 32)
          {
            if (paramInt != 64)
            {
              if (paramInt != 128)
              {
                if (paramInt != 256)
                {
                  if (paramInt != 512)
                  {
                    switch (paramInt)
                    {
                    default: 
                      return 0L;
                    case 2: 
                      return 8L;
                    }
                    return 16L;
                  }
                  return 128L;
                }
                return 256L;
              }
              return 32L;
            }
            return 64L;
          }
          return 1L;
        }
        return 2L;
      }
      return 512L;
    }
    return 4L;
  }
  
  public static long getActionsFromRccControlFlags(int paramInt)
  {
    long l1 = 0L;
    long l2 = 1L;
    while (l2 <= paramInt)
    {
      long l3 = l1;
      if ((paramInt & l2) != 0L) {
        l3 = l1 | getActionForRccFlag((int)l2);
      }
      l2 <<= 1;
      l1 = l3;
    }
    return l1;
  }
  
  public static int getRccControlFlagsFromActions(long paramLong)
  {
    int i = 0;
    long l = 1L;
    while ((l <= paramLong) && (l < 2147483647L))
    {
      int j = i;
      if ((l & paramLong) != 0L) {
        j = i | getRccFlagForAction(l);
      }
      l <<= 1;
      i = j;
    }
    return i;
  }
  
  private static int getRccFlagForAction(long paramLong)
  {
    int i;
    if (paramLong < 2147483647L) {
      i = (int)paramLong;
    } else {
      i = 0;
    }
    if (i != 4)
    {
      if (i != 8)
      {
        if (i != 16)
        {
          if (i != 32)
          {
            if (i != 64)
            {
              if (i != 128)
              {
                if (i != 256)
                {
                  if (i != 512)
                  {
                    switch (i)
                    {
                    default: 
                      return 0;
                    case 2: 
                      return 16;
                    }
                    return 32;
                  }
                  return 8;
                }
                return 256;
              }
              return 512;
            }
            return 64;
          }
          return 128;
        }
        return 1;
      }
      return 2;
    }
    return 4;
  }
  
  public static int getRccStateFromState(int paramInt)
  {
    switch (paramInt)
    {
    case 8: 
    default: 
      return -1;
    case 10: 
      return 6;
    case 9: 
      return 7;
    case 7: 
      return 9;
    case 6: 
      return 8;
    case 5: 
      return 5;
    case 4: 
      return 4;
    case 3: 
      return 3;
    case 2: 
      return 2;
    case 1: 
      return 1;
    }
    return 0;
  }
  
  public static int getStateFromRccState(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return -1;
    case 9: 
      return 7;
    case 8: 
      return 6;
    case 7: 
      return 9;
    case 6: 
      return 10;
    case 5: 
      return 5;
    case 4: 
      return 4;
    case 3: 
      return 3;
    case 2: 
      return 2;
    case 1: 
      return 1;
    }
    return 0;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getActions()
  {
    return mActions;
  }
  
  public long getActiveQueueItemId()
  {
    return mActiveItemId;
  }
  
  public long getBufferedPosition()
  {
    return mBufferedPosition;
  }
  
  public List<CustomAction> getCustomActions()
  {
    return mCustomActions;
  }
  
  public CharSequence getErrorMessage()
  {
    return mErrorMessage;
  }
  
  public Bundle getExtras()
  {
    return mExtras;
  }
  
  public long getLastPositionUpdateTime()
  {
    return mUpdateTime;
  }
  
  public float getPlaybackSpeed()
  {
    return mSpeed;
  }
  
  public long getPosition()
  {
    return mPosition;
  }
  
  public int getState()
  {
    return mState;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("PlaybackState {");
    localStringBuilder.append("state=");
    localStringBuilder.append(mState);
    localStringBuilder.append(", position=");
    localStringBuilder.append(mPosition);
    localStringBuilder.append(", buffered position=");
    localStringBuilder.append(mBufferedPosition);
    localStringBuilder.append(", speed=");
    localStringBuilder.append(mSpeed);
    localStringBuilder.append(", updated=");
    localStringBuilder.append(mUpdateTime);
    localStringBuilder.append(", actions=");
    localStringBuilder.append(mActions);
    localStringBuilder.append(", custom actions=");
    localStringBuilder.append(mCustomActions);
    localStringBuilder.append(", active item id=");
    localStringBuilder.append(mActiveItemId);
    localStringBuilder.append(", error=");
    localStringBuilder.append(mErrorMessage);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mState);
    paramParcel.writeLong(mPosition);
    paramParcel.writeFloat(mSpeed);
    paramParcel.writeLong(mUpdateTime);
    paramParcel.writeLong(mBufferedPosition);
    paramParcel.writeLong(mActions);
    paramParcel.writeTypedList(mCustomActions);
    paramParcel.writeLong(mActiveItemId);
    paramParcel.writeCharSequence(mErrorMessage);
    paramParcel.writeBundle(mExtras);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Actions {}
  
  public static final class Builder
  {
    private long mActions;
    private long mActiveItemId = -1L;
    private long mBufferedPosition;
    private final List<PlaybackState.CustomAction> mCustomActions = new ArrayList();
    private CharSequence mErrorMessage;
    private Bundle mExtras;
    private long mPosition;
    private float mSpeed;
    private int mState;
    private long mUpdateTime;
    
    public Builder() {}
    
    public Builder(PlaybackState paramPlaybackState)
    {
      if (paramPlaybackState == null) {
        return;
      }
      mState = mState;
      mPosition = mPosition;
      mBufferedPosition = mBufferedPosition;
      mSpeed = mSpeed;
      mActions = mActions;
      if (mCustomActions != null) {
        mCustomActions.addAll(mCustomActions);
      }
      mErrorMessage = mErrorMessage;
      mUpdateTime = mUpdateTime;
      mActiveItemId = mActiveItemId;
      mExtras = mExtras;
    }
    
    public Builder addCustomAction(PlaybackState.CustomAction paramCustomAction)
    {
      if (paramCustomAction != null)
      {
        mCustomActions.add(paramCustomAction);
        return this;
      }
      throw new IllegalArgumentException("You may not add a null CustomAction to PlaybackState.");
    }
    
    public Builder addCustomAction(String paramString1, String paramString2, int paramInt)
    {
      return addCustomAction(new PlaybackState.CustomAction(paramString1, paramString2, paramInt, null, null));
    }
    
    public PlaybackState build()
    {
      return new PlaybackState(mState, mPosition, mUpdateTime, mSpeed, mBufferedPosition, mActions, mCustomActions, mActiveItemId, mErrorMessage, mExtras, null);
    }
    
    public Builder setActions(long paramLong)
    {
      mActions = paramLong;
      return this;
    }
    
    public Builder setActiveQueueItemId(long paramLong)
    {
      mActiveItemId = paramLong;
      return this;
    }
    
    public Builder setBufferedPosition(long paramLong)
    {
      mBufferedPosition = paramLong;
      return this;
    }
    
    public Builder setErrorMessage(CharSequence paramCharSequence)
    {
      mErrorMessage = paramCharSequence;
      return this;
    }
    
    public Builder setExtras(Bundle paramBundle)
    {
      mExtras = paramBundle;
      return this;
    }
    
    public Builder setState(int paramInt, long paramLong, float paramFloat)
    {
      return setState(paramInt, paramLong, paramFloat, SystemClock.elapsedRealtime());
    }
    
    public Builder setState(int paramInt, long paramLong1, float paramFloat, long paramLong2)
    {
      mState = paramInt;
      mPosition = paramLong1;
      mUpdateTime = paramLong2;
      mSpeed = paramFloat;
      return this;
    }
  }
  
  public static final class CustomAction
    implements Parcelable
  {
    public static final Parcelable.Creator<CustomAction> CREATOR = new Parcelable.Creator()
    {
      public PlaybackState.CustomAction createFromParcel(Parcel paramAnonymousParcel)
      {
        return new PlaybackState.CustomAction(paramAnonymousParcel, null);
      }
      
      public PlaybackState.CustomAction[] newArray(int paramAnonymousInt)
      {
        return new PlaybackState.CustomAction[paramAnonymousInt];
      }
    };
    private final String mAction;
    private final Bundle mExtras;
    private final int mIcon;
    private final CharSequence mName;
    
    private CustomAction(Parcel paramParcel)
    {
      mAction = paramParcel.readString();
      mName = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
      mIcon = paramParcel.readInt();
      mExtras = paramParcel.readBundle();
    }
    
    private CustomAction(String paramString, CharSequence paramCharSequence, int paramInt, Bundle paramBundle)
    {
      mAction = paramString;
      mName = paramCharSequence;
      mIcon = paramInt;
      mExtras = paramBundle;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public String getAction()
    {
      return mAction;
    }
    
    public Bundle getExtras()
    {
      return mExtras;
    }
    
    public int getIcon()
    {
      return mIcon;
    }
    
    public CharSequence getName()
    {
      return mName;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Action:mName='");
      localStringBuilder.append(mName);
      localStringBuilder.append(", mIcon=");
      localStringBuilder.append(mIcon);
      localStringBuilder.append(", mExtras=");
      localStringBuilder.append(mExtras);
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeString(mAction);
      TextUtils.writeToParcel(mName, paramParcel, paramInt);
      paramParcel.writeInt(mIcon);
      paramParcel.writeBundle(mExtras);
    }
    
    public static final class Builder
    {
      private final String mAction;
      private Bundle mExtras;
      private final int mIcon;
      private final CharSequence mName;
      
      public Builder(String paramString, CharSequence paramCharSequence, int paramInt)
      {
        if (!TextUtils.isEmpty(paramString))
        {
          if (!TextUtils.isEmpty(paramCharSequence))
          {
            if (paramInt != 0)
            {
              mAction = paramString;
              mName = paramCharSequence;
              mIcon = paramInt;
              return;
            }
            throw new IllegalArgumentException("You must specify an icon resource id to build a CustomAction.");
          }
          throw new IllegalArgumentException("You must specify a name to build a CustomAction.");
        }
        throw new IllegalArgumentException("You must specify an action to build a CustomAction.");
      }
      
      public PlaybackState.CustomAction build()
      {
        return new PlaybackState.CustomAction(mAction, mName, mIcon, mExtras, null);
      }
      
      public Builder setExtras(Bundle paramBundle)
      {
        mExtras = paramBundle;
        return this;
      }
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface State {}
}
