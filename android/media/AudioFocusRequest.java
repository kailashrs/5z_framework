package android.media;

import android.annotation.SystemApi;
import android.os.Bundle;
import android.os.Handler;

public final class AudioFocusRequest
{
  private static final AudioAttributes FOCUS_DEFAULT_ATTR = new AudioAttributes.Builder().setUsage(1).build();
  public static final String KEY_ACCESSIBILITY_FORCE_FOCUS_DUCKING = "a11y_force_ducking";
  private final AudioAttributes mAttr;
  private final int mFlags;
  private final int mFocusGain;
  private final AudioManager.OnAudioFocusChangeListener mFocusListener;
  private final Handler mListenerHandler;
  
  private AudioFocusRequest(AudioManager.OnAudioFocusChangeListener paramOnAudioFocusChangeListener, Handler paramHandler, AudioAttributes paramAudioAttributes, int paramInt1, int paramInt2)
  {
    mFocusListener = paramOnAudioFocusChangeListener;
    mListenerHandler = paramHandler;
    mFocusGain = paramInt1;
    mAttr = paramAudioAttributes;
    mFlags = paramInt2;
  }
  
  static final boolean isValidFocusGain(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return false;
    }
    return true;
  }
  
  public boolean acceptsDelayedFocusGain()
  {
    int i = mFlags;
    boolean bool = true;
    if ((i & 0x1) != 1) {
      bool = false;
    }
    return bool;
  }
  
  public AudioAttributes getAudioAttributes()
  {
    return mAttr;
  }
  
  int getFlags()
  {
    return mFlags;
  }
  
  public int getFocusGain()
  {
    return mFocusGain;
  }
  
  public AudioManager.OnAudioFocusChangeListener getOnAudioFocusChangeListener()
  {
    return mFocusListener;
  }
  
  public Handler getOnAudioFocusChangeListenerHandler()
  {
    return mListenerHandler;
  }
  
  @SystemApi
  public boolean locksFocus()
  {
    boolean bool;
    if ((mFlags & 0x4) == 4) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean willPauseWhenDucked()
  {
    boolean bool;
    if ((mFlags & 0x2) == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static final class Builder
  {
    private boolean mA11yForceDucking = false;
    private AudioAttributes mAttr = AudioFocusRequest.FOCUS_DEFAULT_ATTR;
    private boolean mDelayedFocus = false;
    private int mFocusGain;
    private AudioManager.OnAudioFocusChangeListener mFocusListener;
    private boolean mFocusLocked = false;
    private Handler mListenerHandler;
    private boolean mPausesOnDuck = false;
    
    public Builder(int paramInt)
    {
      setFocusGain(paramInt);
    }
    
    public Builder(AudioFocusRequest paramAudioFocusRequest)
    {
      if (paramAudioFocusRequest != null)
      {
        mAttr = mAttr;
        mFocusListener = mFocusListener;
        mListenerHandler = mListenerHandler;
        mFocusGain = mFocusGain;
        mPausesOnDuck = paramAudioFocusRequest.willPauseWhenDucked();
        mDelayedFocus = paramAudioFocusRequest.acceptsDelayedFocusGain();
        return;
      }
      throw new IllegalArgumentException("Illegal null AudioFocusRequest");
    }
    
    public AudioFocusRequest build()
    {
      if (((!mDelayedFocus) && (!mPausesOnDuck)) || (mFocusListener != null))
      {
        if (mA11yForceDucking)
        {
          Bundle localBundle;
          if (mAttr.getBundle() == null) {
            localBundle = new Bundle();
          } else {
            localBundle = mAttr.getBundle();
          }
          localBundle.putBoolean("a11y_force_ducking", true);
          mAttr = new AudioAttributes.Builder(mAttr).addBundle(localBundle).build();
        }
        boolean bool = mDelayedFocus;
        int i = 0;
        int j;
        if (mPausesOnDuck) {
          j = 2;
        } else {
          j = 0;
        }
        if (mFocusLocked) {
          i = 4;
        }
        return new AudioFocusRequest(mFocusListener, mListenerHandler, mAttr, mFocusGain, bool | false | j | i, null);
      }
      throw new IllegalStateException("Can't use delayed focus or pause on duck without a listener");
    }
    
    public Builder setAcceptsDelayedFocusGain(boolean paramBoolean)
    {
      mDelayedFocus = paramBoolean;
      return this;
    }
    
    public Builder setAudioAttributes(AudioAttributes paramAudioAttributes)
    {
      if (paramAudioAttributes != null)
      {
        mAttr = paramAudioAttributes;
        return this;
      }
      throw new NullPointerException("Illegal null AudioAttributes");
    }
    
    public Builder setFocusGain(int paramInt)
    {
      if (AudioFocusRequest.isValidFocusGain(paramInt))
      {
        mFocusGain = paramInt;
        return this;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Illegal audio focus gain type ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public Builder setForceDucking(boolean paramBoolean)
    {
      mA11yForceDucking = paramBoolean;
      return this;
    }
    
    @SystemApi
    public Builder setLocksFocus(boolean paramBoolean)
    {
      mFocusLocked = paramBoolean;
      return this;
    }
    
    public Builder setOnAudioFocusChangeListener(AudioManager.OnAudioFocusChangeListener paramOnAudioFocusChangeListener)
    {
      if (paramOnAudioFocusChangeListener != null)
      {
        mFocusListener = paramOnAudioFocusChangeListener;
        mListenerHandler = null;
        return this;
      }
      throw new NullPointerException("Illegal null focus listener");
    }
    
    public Builder setOnAudioFocusChangeListener(AudioManager.OnAudioFocusChangeListener paramOnAudioFocusChangeListener, Handler paramHandler)
    {
      if ((paramOnAudioFocusChangeListener != null) && (paramHandler != null))
      {
        mFocusListener = paramOnAudioFocusChangeListener;
        mListenerHandler = paramHandler;
        return this;
      }
      throw new NullPointerException("Illegal null focus listener or handler");
    }
    
    Builder setOnAudioFocusChangeListenerInt(AudioManager.OnAudioFocusChangeListener paramOnAudioFocusChangeListener, Handler paramHandler)
    {
      mFocusListener = paramOnAudioFocusChangeListener;
      mListenerHandler = paramHandler;
      return this;
    }
    
    public Builder setWillPauseWhenDucked(boolean paramBoolean)
    {
      mPausesOnDuck = paramBoolean;
      return this;
    }
  }
}
