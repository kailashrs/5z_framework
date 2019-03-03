package android.media;

import android.os.SystemProperties;
import android.util.Log;

public class MediaActionSound
{
  public static final int FOCUS_COMPLETE = 1;
  private static final String[] JP_SOUND_DIRS = { "/product/media/audio/ui_jp/", "/system/media/audio/ui_jp/" };
  private static final int NUM_MEDIA_SOUND_STREAMS = 1;
  public static final int SHUTTER_CLICK = 0;
  private static final String[] SOUND_DIRS = { "/product/media/audio/ui/", "/system/media/audio/ui/" };
  private static final String[] SOUND_FILES = { "camera_click.ogg", "camera_focus.ogg", "VideoRecord.ogg", "VideoStop.ogg" };
  public static final int START_VIDEO_RECORDING = 2;
  private static final int STATE_LOADED = 3;
  private static final int STATE_LOADING = 1;
  private static final int STATE_LOADING_PLAY_REQUESTED = 2;
  private static final int STATE_NOT_LOADED = 0;
  public static final int STOP_VIDEO_RECORDING = 3;
  private static final String TAG = "MediaActionSound";
  private String SOUND_FORCED = SystemProperties.get("ro.camera.sound.forced", "0");
  private SoundPool.OnLoadCompleteListener mLoadCompleteListener = new SoundPool.OnLoadCompleteListener()
  {
    public void onLoadComplete(SoundPool paramAnonymousSoundPool, int paramAnonymousInt1, int paramAnonymousInt2)
    {
      Object localObject1 = mSounds;
      int i = localObject1.length;
      int j = 0;
      while (j < i)
      {
        Object localObject2 = localObject1[j];
        if (id != paramAnonymousInt1)
        {
          j++;
        }
        else
        {
          paramAnonymousInt1 = 0;
          if (paramAnonymousInt2 != 0) {
            try
            {
              state = 0;
              id = 0;
              paramAnonymousSoundPool = new java/lang/StringBuilder;
              paramAnonymousSoundPool.<init>();
              paramAnonymousSoundPool.append("OnLoadCompleteListener() error: ");
              paramAnonymousSoundPool.append(paramAnonymousInt2);
              paramAnonymousSoundPool.append(" loading sound: ");
              paramAnonymousSoundPool.append(name);
              Log.e("MediaActionSound", paramAnonymousSoundPool.toString());
              return;
            }
            finally
            {
              break label259;
            }
          }
          switch (state)
          {
          default: 
            break;
          case 2: 
            paramAnonymousInt1 = id;
            state = 3;
            break;
          case 1: 
            state = 3;
            break;
          }
          localObject1 = new java/lang/StringBuilder;
          ((StringBuilder)localObject1).<init>();
          ((StringBuilder)localObject1).append("OnLoadCompleteListener() called in wrong state: ");
          ((StringBuilder)localObject1).append(state);
          ((StringBuilder)localObject1).append(" for sound: ");
          ((StringBuilder)localObject1).append(name);
          Log.e("MediaActionSound", ((StringBuilder)localObject1).toString());
          if (paramAnonymousInt1 != 0)
          {
            paramAnonymousSoundPool.play(paramAnonymousInt1, 1.0F, 1.0F, 0, 0, 1.0F);
            break;
            label259:
            throw paramAnonymousSoundPool;
          }
        }
      }
    }
  };
  private SoundPool mSoundPool = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(new AudioAttributes.Builder().setUsage(13).setFlags(1).setContentType(4).build()).build();
  private SoundState[] mSounds;
  
  public MediaActionSound()
  {
    mSoundPool.setOnLoadCompleteListener(mLoadCompleteListener);
    mSounds = new SoundState[SOUND_FILES.length];
    for (int i = 0; i < mSounds.length; i++) {
      mSounds[i] = new SoundState(i);
    }
  }
  
  private int loadSound(SoundState paramSoundState)
  {
    String str = SOUND_FILES[name];
    String[] arrayOfString;
    int i;
    int j;
    if ((SOUND_FORCED != null) && (!SOUND_FORCED.equals("0")))
    {
      arrayOfString = JP_SOUND_DIRS;
      i = arrayOfString.length;
      j = 0;
    }
    while (j < i)
    {
      Object localObject1 = arrayOfString[j];
      Log.d("MediaActionSound", "load JP camera sound");
      Object localObject2 = mSoundPool;
      Object localObject3 = new StringBuilder();
      ((StringBuilder)localObject3).append((String)localObject1);
      ((StringBuilder)localObject3).append(str);
      int k = ((SoundPool)localObject2).load(((StringBuilder)localObject3).toString(), 1);
      if (k > 0)
      {
        state = 1;
        id = k;
        return k;
      }
      j++;
      continue;
      for (localObject3 : SOUND_DIRS)
      {
        Log.d("MediaActionSound", "load WW camera sound");
        localObject1 = mSoundPool;
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append((String)localObject3);
        ((StringBuilder)localObject2).append(str);
        k = ((SoundPool)localObject1).load(((StringBuilder)localObject2).toString(), 1);
        if (k > 0)
        {
          state = 1;
          id = k;
          return k;
        }
      }
    }
    return 0;
  }
  
  public void load(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < SOUND_FILES.length)) {
      synchronized (mSounds[paramInt])
      {
        StringBuilder localStringBuilder;
        if (state != 0)
        {
          localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("load() called in wrong state: ");
          localStringBuilder.append(???);
          localStringBuilder.append(" for sound: ");
          localStringBuilder.append(paramInt);
          Log.e("MediaActionSound", localStringBuilder.toString());
        }
        else if (loadSound((SoundState)???) <= 0)
        {
          localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("load() error loading sound: ");
          localStringBuilder.append(paramInt);
          Log.e("MediaActionSound", localStringBuilder.toString());
        }
        return;
      }
    }
    ??? = new StringBuilder();
    ((StringBuilder)???).append("Unknown sound requested: ");
    ((StringBuilder)???).append(paramInt);
    throw new RuntimeException(((StringBuilder)???).toString());
  }
  
  public void play(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < SOUND_FILES.length)) {
      synchronized (mSounds[paramInt])
      {
        int i = state;
        if (i != 3)
        {
          StringBuilder localStringBuilder;
          switch (i)
          {
          default: 
            localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append("play() called in wrong state: ");
            localStringBuilder.append(state);
            localStringBuilder.append(" for sound: ");
            localStringBuilder.append(paramInt);
            Log.e("MediaActionSound", localStringBuilder.toString());
            break;
          case 0: 
            loadSound((SoundState)???);
            if (loadSound((SoundState)???) <= 0)
            {
              localStringBuilder = new java/lang/StringBuilder;
              localStringBuilder.<init>();
              localStringBuilder.append("play() error loading sound: ");
              localStringBuilder.append(paramInt);
              Log.e("MediaActionSound", localStringBuilder.toString());
            }
            break;
          }
          state = 2;
        }
        else
        {
          mSoundPool.play(id, 1.0F, 1.0F, 0, 0, 1.0F);
        }
        return;
      }
    }
    ??? = new StringBuilder();
    ((StringBuilder)???).append("Unknown sound requested: ");
    ((StringBuilder)???).append(paramInt);
    throw new RuntimeException(((StringBuilder)???).toString());
  }
  
  public void release()
  {
    if (mSoundPool != null)
    {
      SoundState[] arrayOfSoundState = mSounds;
      int i = arrayOfSoundState.length;
      int j = 0;
      while (j < i) {
        synchronized (arrayOfSoundState[j])
        {
          state = 0;
          id = 0;
          j++;
        }
      }
      mSoundPool.release();
      mSoundPool = null;
    }
  }
  
  private class SoundState
  {
    public int id;
    public final int name;
    public int state;
    
    public SoundState(int paramInt)
    {
      name = paramInt;
      id = 0;
      state = 0;
    }
  }
}
