package android.speech.tts;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.media.AudioAttributes;
import android.media.AudioAttributes.Builder;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.os.MessageQueue.IdleHandler;
import android.os.ParcelFileDescriptor;
import android.os.ParcelFileDescriptor.AutoCloseOutputStream;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Log;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Set;

public abstract class TextToSpeechService
  extends Service
{
  private static final boolean DBG = false;
  private static final String SYNTH_THREAD_NAME = "SynthThread";
  private static final String TAG = "TextToSpeechService";
  private AudioPlaybackHandler mAudioPlaybackHandler;
  private final ITextToSpeechService.Stub mBinder = new ITextToSpeechService.Stub()
  {
    private boolean checkNonNull(Object... paramAnonymousVarArgs)
    {
      int i = paramAnonymousVarArgs.length;
      for (int j = 0; j < i; j++) {
        if (paramAnonymousVarArgs[j] == null) {
          return false;
        }
      }
      return true;
    }
    
    private String intern(String paramAnonymousString)
    {
      return paramAnonymousString.intern();
    }
    
    public String[] getClientDefaultLanguage()
    {
      return TextToSpeechService.this.getSettingsLocale();
    }
    
    public String getDefaultVoiceNameFor(String paramAnonymousString1, String paramAnonymousString2, String paramAnonymousString3)
    {
      if (!checkNonNull(new Object[] { paramAnonymousString1 })) {
        return null;
      }
      int i = onIsLanguageAvailable(paramAnonymousString1, paramAnonymousString2, paramAnonymousString3);
      if ((i != 0) && (i != 1) && (i != 2)) {
        return null;
      }
      return onGetDefaultVoiceNameFor(paramAnonymousString1, paramAnonymousString2, paramAnonymousString3);
    }
    
    public String[] getFeaturesForLanguage(String paramAnonymousString1, String paramAnonymousString2, String paramAnonymousString3)
    {
      paramAnonymousString2 = onGetFeaturesForLanguage(paramAnonymousString1, paramAnonymousString2, paramAnonymousString3);
      if (paramAnonymousString2 != null)
      {
        paramAnonymousString1 = new String[paramAnonymousString2.size()];
        paramAnonymousString2.toArray(paramAnonymousString1);
      }
      else
      {
        paramAnonymousString1 = new String[0];
      }
      return paramAnonymousString1;
    }
    
    public String[] getLanguage()
    {
      return onGetLanguage();
    }
    
    public List<Voice> getVoices()
    {
      return onGetVoices();
    }
    
    public int isLanguageAvailable(String paramAnonymousString1, String paramAnonymousString2, String paramAnonymousString3)
    {
      if (!checkNonNull(new Object[] { paramAnonymousString1 })) {
        return -1;
      }
      return onIsLanguageAvailable(paramAnonymousString1, paramAnonymousString2, paramAnonymousString3);
    }
    
    public boolean isSpeaking()
    {
      boolean bool;
      if ((!mSynthHandler.isSpeaking()) && (!mAudioPlaybackHandler.isSpeaking())) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public int loadLanguage(IBinder paramAnonymousIBinder, String paramAnonymousString1, String paramAnonymousString2, String paramAnonymousString3)
    {
      if (!checkNonNull(new Object[] { paramAnonymousString1 })) {
        return -1;
      }
      int i = onIsLanguageAvailable(paramAnonymousString1, paramAnonymousString2, paramAnonymousString3);
      if ((i == 0) || (i == 1) || (i == 2))
      {
        paramAnonymousIBinder = new TextToSpeechService.LoadLanguageItem(TextToSpeechService.this, paramAnonymousIBinder, Binder.getCallingUid(), Binder.getCallingPid(), paramAnonymousString1, paramAnonymousString2, paramAnonymousString3);
        if (mSynthHandler.enqueueSpeechItem(1, paramAnonymousIBinder) != 0) {
          return -1;
        }
      }
      return i;
    }
    
    public int loadVoice(IBinder paramAnonymousIBinder, String paramAnonymousString)
    {
      if (!checkNonNull(new Object[] { paramAnonymousString })) {
        return -1;
      }
      int i = onIsValidVoiceName(paramAnonymousString);
      if (i == 0)
      {
        paramAnonymousIBinder = new TextToSpeechService.LoadVoiceItem(TextToSpeechService.this, paramAnonymousIBinder, Binder.getCallingUid(), Binder.getCallingPid(), paramAnonymousString);
        if (mSynthHandler.enqueueSpeechItem(1, paramAnonymousIBinder) != 0) {
          return -1;
        }
      }
      return i;
    }
    
    public int playAudio(IBinder paramAnonymousIBinder, Uri paramAnonymousUri, int paramAnonymousInt, Bundle paramAnonymousBundle, String paramAnonymousString)
    {
      if (!checkNonNull(new Object[] { paramAnonymousIBinder, paramAnonymousUri, paramAnonymousBundle })) {
        return -1;
      }
      paramAnonymousIBinder = new TextToSpeechService.AudioSpeechItem(TextToSpeechService.this, paramAnonymousIBinder, Binder.getCallingUid(), Binder.getCallingPid(), paramAnonymousBundle, paramAnonymousString, paramAnonymousUri);
      return mSynthHandler.enqueueSpeechItem(paramAnonymousInt, paramAnonymousIBinder);
    }
    
    public int playSilence(IBinder paramAnonymousIBinder, long paramAnonymousLong, int paramAnonymousInt, String paramAnonymousString)
    {
      if (!checkNonNull(new Object[] { paramAnonymousIBinder })) {
        return -1;
      }
      paramAnonymousIBinder = new TextToSpeechService.SilenceSpeechItem(TextToSpeechService.this, paramAnonymousIBinder, Binder.getCallingUid(), Binder.getCallingPid(), paramAnonymousString, paramAnonymousLong);
      return mSynthHandler.enqueueSpeechItem(paramAnonymousInt, paramAnonymousIBinder);
    }
    
    public void setCallback(IBinder paramAnonymousIBinder, ITextToSpeechCallback paramAnonymousITextToSpeechCallback)
    {
      if (!checkNonNull(new Object[] { paramAnonymousIBinder })) {
        return;
      }
      mCallbacks.setCallback(paramAnonymousIBinder, paramAnonymousITextToSpeechCallback);
    }
    
    public int speak(IBinder paramAnonymousIBinder, CharSequence paramAnonymousCharSequence, int paramAnonymousInt, Bundle paramAnonymousBundle, String paramAnonymousString)
    {
      if (!checkNonNull(new Object[] { paramAnonymousIBinder, paramAnonymousCharSequence, paramAnonymousBundle })) {
        return -1;
      }
      paramAnonymousIBinder = new TextToSpeechService.SynthesisSpeechItem(TextToSpeechService.this, paramAnonymousIBinder, Binder.getCallingUid(), Binder.getCallingPid(), paramAnonymousBundle, paramAnonymousString, paramAnonymousCharSequence);
      return mSynthHandler.enqueueSpeechItem(paramAnonymousInt, paramAnonymousIBinder);
    }
    
    public int stop(IBinder paramAnonymousIBinder)
    {
      if (!checkNonNull(new Object[] { paramAnonymousIBinder })) {
        return -1;
      }
      return mSynthHandler.stopForApp(paramAnonymousIBinder);
    }
    
    public int synthesizeToFileDescriptor(IBinder paramAnonymousIBinder, CharSequence paramAnonymousCharSequence, ParcelFileDescriptor paramAnonymousParcelFileDescriptor, Bundle paramAnonymousBundle, String paramAnonymousString)
    {
      if (!checkNonNull(new Object[] { paramAnonymousIBinder, paramAnonymousCharSequence, paramAnonymousParcelFileDescriptor, paramAnonymousBundle })) {
        return -1;
      }
      paramAnonymousParcelFileDescriptor = ParcelFileDescriptor.adoptFd(paramAnonymousParcelFileDescriptor.detachFd());
      paramAnonymousIBinder = new TextToSpeechService.SynthesisToFileOutputStreamSpeechItem(TextToSpeechService.this, paramAnonymousIBinder, Binder.getCallingUid(), Binder.getCallingPid(), paramAnonymousBundle, paramAnonymousString, paramAnonymousCharSequence, new ParcelFileDescriptor.AutoCloseOutputStream(paramAnonymousParcelFileDescriptor));
      return mSynthHandler.enqueueSpeechItem(1, paramAnonymousIBinder);
    }
  };
  private CallbackMap mCallbacks;
  private TtsEngines mEngineHelper;
  private String mPackageName;
  private SynthHandler mSynthHandler;
  private final Object mVoicesInfoLock = new Object();
  
  public TextToSpeechService() {}
  
  private int getDefaultPitch()
  {
    return getSecureSettingInt("tts_default_pitch", 100);
  }
  
  private int getDefaultSpeechRate()
  {
    return getSecureSettingInt("tts_default_rate", 100);
  }
  
  private int getExpectedLanguageAvailableStatus(Locale paramLocale)
  {
    int i = 2;
    if (paramLocale.getVariant().isEmpty()) {
      if (paramLocale.getCountry().isEmpty()) {
        i = 0;
      } else {
        i = 1;
      }
    }
    return i;
  }
  
  private int getSecureSettingInt(String paramString, int paramInt)
  {
    return Settings.Secure.getInt(getContentResolver(), paramString, paramInt);
  }
  
  private String[] getSettingsLocale()
  {
    return TtsEngines.toOldLocaleStringFormat(mEngineHelper.getLocalePrefForEngine(mPackageName));
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    if ("android.intent.action.TTS_SERVICE".equals(paramIntent.getAction())) {
      return mBinder;
    }
    return null;
  }
  
  public void onCreate()
  {
    super.onCreate();
    Object localObject = new SynthThread();
    ((SynthThread)localObject).start();
    mSynthHandler = new SynthHandler(((SynthThread)localObject).getLooper());
    mAudioPlaybackHandler = new AudioPlaybackHandler();
    mAudioPlaybackHandler.start();
    mEngineHelper = new TtsEngines(this);
    mCallbacks = new CallbackMap(null);
    mPackageName = getApplicationInfopackageName;
    localObject = getSettingsLocale();
    onLoadLanguage(localObject[0], localObject[1], localObject[2]);
  }
  
  public void onDestroy()
  {
    mSynthHandler.quit();
    mAudioPlaybackHandler.quit();
    mCallbacks.kill();
    super.onDestroy();
  }
  
  public String onGetDefaultVoiceNameFor(String paramString1, String paramString2, String paramString3)
  {
    switch (onIsLanguageAvailable(paramString1, paramString2, paramString3))
    {
    default: 
      return null;
    case 2: 
      paramString1 = new Locale(paramString1, paramString2, paramString3);
      break;
    case 1: 
      paramString1 = new Locale(paramString1, paramString2);
      break;
    case 0: 
      paramString1 = new Locale(paramString1);
    }
    paramString1 = TtsEngines.normalizeTTSLocale(paramString1).toLanguageTag();
    if (onIsValidVoiceName(paramString1) == 0) {
      return paramString1;
    }
    return null;
  }
  
  protected Set<String> onGetFeaturesForLanguage(String paramString1, String paramString2, String paramString3)
  {
    return new HashSet();
  }
  
  protected abstract String[] onGetLanguage();
  
  public List<Voice> onGetVoices()
  {
    ArrayList localArrayList = new ArrayList();
    Locale[] arrayOfLocale = Locale.getAvailableLocales();
    int i = arrayOfLocale.length;
    for (int j = 0;; j++)
    {
      TextToSpeechService localTextToSpeechService = this;
      if (j >= i) {
        break;
      }
      Locale localLocale = arrayOfLocale[j];
      int k = localTextToSpeechService.getExpectedLanguageAvailableStatus(localLocale);
      try
      {
        int m = localTextToSpeechService.onIsLanguageAvailable(localLocale.getISO3Language(), localLocale.getISO3Country(), localLocale.getVariant());
        if (m == k)
        {
          Set localSet = localTextToSpeechService.onGetFeaturesForLanguage(localLocale.getISO3Language(), localLocale.getISO3Country(), localLocale.getVariant());
          localArrayList.add(new Voice(localTextToSpeechService.onGetDefaultVoiceNameFor(localLocale.getISO3Language(), localLocale.getISO3Country(), localLocale.getVariant()), localLocale, 300, 300, false, localSet));
        }
      }
      catch (MissingResourceException localMissingResourceException) {}
    }
    return localArrayList;
  }
  
  protected abstract int onIsLanguageAvailable(String paramString1, String paramString2, String paramString3);
  
  public int onIsValidVoiceName(String paramString)
  {
    paramString = Locale.forLanguageTag(paramString);
    if (paramString == null) {
      return -1;
    }
    int i = getExpectedLanguageAvailableStatus(paramString);
    try
    {
      int j = onIsLanguageAvailable(paramString.getISO3Language(), paramString.getISO3Country(), paramString.getVariant());
      if (j != i) {
        return -1;
      }
      return 0;
    }
    catch (MissingResourceException paramString) {}
    return -1;
  }
  
  protected abstract int onLoadLanguage(String paramString1, String paramString2, String paramString3);
  
  public int onLoadVoice(String paramString)
  {
    paramString = Locale.forLanguageTag(paramString);
    if (paramString == null) {
      return -1;
    }
    int i = getExpectedLanguageAvailableStatus(paramString);
    try
    {
      if (onIsLanguageAvailable(paramString.getISO3Language(), paramString.getISO3Country(), paramString.getVariant()) != i) {
        return -1;
      }
      onLoadLanguage(paramString.getISO3Language(), paramString.getISO3Country(), paramString.getVariant());
      return 0;
    }
    catch (MissingResourceException paramString) {}
    return -1;
  }
  
  protected abstract void onStop();
  
  protected abstract void onSynthesizeText(SynthesisRequest paramSynthesisRequest, SynthesisCallback paramSynthesisCallback);
  
  static class AudioOutputParams
  {
    public final AudioAttributes mAudioAttributes;
    public final float mPan;
    public final int mSessionId;
    public final float mVolume;
    
    AudioOutputParams()
    {
      mSessionId = 0;
      mVolume = 1.0F;
      mPan = 0.0F;
      mAudioAttributes = null;
    }
    
    AudioOutputParams(int paramInt, float paramFloat1, float paramFloat2, AudioAttributes paramAudioAttributes)
    {
      mSessionId = paramInt;
      mVolume = paramFloat1;
      mPan = paramFloat2;
      mAudioAttributes = paramAudioAttributes;
    }
    
    static AudioOutputParams createFromParamsBundle(Bundle paramBundle, boolean paramBoolean)
    {
      if (paramBundle == null) {
        return new AudioOutputParams();
      }
      AudioAttributes localAudioAttributes = (AudioAttributes)paramBundle.getParcelable("audioAttributes");
      Object localObject = localAudioAttributes;
      if (localAudioAttributes == null)
      {
        int i = paramBundle.getInt("streamType", 3);
        localObject = new AudioAttributes.Builder().setLegacyStreamType(i);
        if (paramBoolean) {
          i = 1;
        } else {
          i = 4;
        }
        localObject = ((AudioAttributes.Builder)localObject).setContentType(i).build();
      }
      return new AudioOutputParams(paramBundle.getInt("sessionId", 0), paramBundle.getFloat("volume", 1.0F), paramBundle.getFloat("pan", 0.0F), (AudioAttributes)localObject);
    }
  }
  
  private class AudioSpeechItem
    extends TextToSpeechService.UtteranceSpeechItemWithParams
  {
    private final AudioPlaybackQueueItem mItem = new AudioPlaybackQueueItem(this, getCallerIdentity(), TextToSpeechService.this, paramUri, getAudioParams());
    
    public AudioSpeechItem(Object paramObject, int paramInt1, int paramInt2, Bundle paramBundle, String paramString, Uri paramUri)
    {
      super(paramObject, paramInt1, paramInt2, paramBundle, paramString);
    }
    
    TextToSpeechService.AudioOutputParams getAudioParams()
    {
      return TextToSpeechService.AudioOutputParams.createFromParamsBundle(mParams, false);
    }
    
    public String getUtteranceId()
    {
      return getStringParam(mParams, "utteranceId", null);
    }
    
    public boolean isValid()
    {
      return true;
    }
    
    protected void playImpl()
    {
      mAudioPlaybackHandler.enqueue(mItem);
    }
    
    protected void stopImpl() {}
  }
  
  private class CallbackMap
    extends RemoteCallbackList<ITextToSpeechCallback>
  {
    private final HashMap<IBinder, ITextToSpeechCallback> mCallerToCallback = new HashMap();
    
    private CallbackMap() {}
    
    private ITextToSpeechCallback getCallbackFor(Object arg1)
    {
      Object localObject1 = (IBinder)???;
      synchronized (mCallerToCallback)
      {
        localObject1 = (ITextToSpeechCallback)mCallerToCallback.get(localObject1);
        return localObject1;
      }
    }
    
    public void dispatchOnAudioAvailable(Object paramObject, String paramString, byte[] paramArrayOfByte)
    {
      paramObject = getCallbackFor(paramObject);
      if (paramObject == null) {
        return;
      }
      try
      {
        paramObject.onAudioAvailable(paramString, paramArrayOfByte);
      }
      catch (RemoteException paramObject)
      {
        paramString = new StringBuilder();
        paramString.append("Callback dispatchOnAudioAvailable(String, byte[]) failed: ");
        paramString.append(paramObject);
        Log.e("TextToSpeechService", paramString.toString());
      }
    }
    
    public void dispatchOnBeginSynthesis(Object paramObject, String paramString, int paramInt1, int paramInt2, int paramInt3)
    {
      paramObject = getCallbackFor(paramObject);
      if (paramObject == null) {
        return;
      }
      try
      {
        paramObject.onBeginSynthesis(paramString, paramInt1, paramInt2, paramInt3);
      }
      catch (RemoteException paramObject)
      {
        paramString = new StringBuilder();
        paramString.append("Callback dispatchOnBeginSynthesis(String, int, int, int) failed: ");
        paramString.append(paramObject);
        Log.e("TextToSpeechService", paramString.toString());
      }
    }
    
    public void dispatchOnError(Object paramObject, String paramString, int paramInt)
    {
      paramObject = getCallbackFor(paramObject);
      if (paramObject == null) {
        return;
      }
      try
      {
        paramObject.onError(paramString, paramInt);
      }
      catch (RemoteException paramString)
      {
        paramObject = new StringBuilder();
        paramObject.append("Callback onError failed: ");
        paramObject.append(paramString);
        Log.e("TextToSpeechService", paramObject.toString());
      }
    }
    
    public void dispatchOnRangeStart(Object paramObject, String paramString, int paramInt1, int paramInt2, int paramInt3)
    {
      paramObject = getCallbackFor(paramObject);
      if (paramObject == null) {
        return;
      }
      try
      {
        paramObject.onRangeStart(paramString, paramInt1, paramInt2, paramInt3);
      }
      catch (RemoteException paramString)
      {
        paramObject = new StringBuilder();
        paramObject.append("Callback dispatchOnRangeStart(String, int, int, int) failed: ");
        paramObject.append(paramString);
        Log.e("TextToSpeechService", paramObject.toString());
      }
    }
    
    public void dispatchOnStart(Object paramObject, String paramString)
    {
      paramObject = getCallbackFor(paramObject);
      if (paramObject == null) {
        return;
      }
      try
      {
        paramObject.onStart(paramString);
      }
      catch (RemoteException paramObject)
      {
        paramString = new StringBuilder();
        paramString.append("Callback onStart failed: ");
        paramString.append(paramObject);
        Log.e("TextToSpeechService", paramString.toString());
      }
    }
    
    public void dispatchOnStop(Object paramObject, String paramString, boolean paramBoolean)
    {
      paramObject = getCallbackFor(paramObject);
      if (paramObject == null) {
        return;
      }
      try
      {
        paramObject.onStop(paramString, paramBoolean);
      }
      catch (RemoteException paramString)
      {
        paramObject = new StringBuilder();
        paramObject.append("Callback onStop failed: ");
        paramObject.append(paramString);
        Log.e("TextToSpeechService", paramObject.toString());
      }
    }
    
    public void dispatchOnSuccess(Object paramObject, String paramString)
    {
      paramObject = getCallbackFor(paramObject);
      if (paramObject == null) {
        return;
      }
      try
      {
        paramObject.onSuccess(paramString);
      }
      catch (RemoteException paramString)
      {
        paramObject = new StringBuilder();
        paramObject.append("Callback onDone failed: ");
        paramObject.append(paramString);
        Log.e("TextToSpeechService", paramObject.toString());
      }
    }
    
    public void kill()
    {
      synchronized (mCallerToCallback)
      {
        mCallerToCallback.clear();
        super.kill();
        return;
      }
    }
    
    public void onCallbackDied(ITextToSpeechCallback arg1, Object paramObject)
    {
      paramObject = (IBinder)paramObject;
      synchronized (mCallerToCallback)
      {
        mCallerToCallback.remove(paramObject);
        return;
      }
    }
    
    public void setCallback(IBinder paramIBinder, ITextToSpeechCallback paramITextToSpeechCallback)
    {
      HashMap localHashMap = mCallerToCallback;
      if (paramITextToSpeechCallback != null) {
        try
        {
          register(paramITextToSpeechCallback, paramIBinder);
          paramIBinder = (ITextToSpeechCallback)mCallerToCallback.put(paramIBinder, paramITextToSpeechCallback);
        }
        finally
        {
          break label68;
        }
      } else {
        paramIBinder = (ITextToSpeechCallback)mCallerToCallback.remove(paramIBinder);
      }
      if ((paramIBinder != null) && (paramIBinder != paramITextToSpeechCallback)) {
        unregister(paramIBinder);
      }
      return;
      label68:
      throw paramIBinder;
    }
  }
  
  private class LoadLanguageItem
    extends TextToSpeechService.SpeechItem
  {
    private final String mCountry;
    private final String mLanguage;
    private final String mVariant;
    
    public LoadLanguageItem(Object paramObject, int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3)
    {
      super(paramObject, paramInt1, paramInt2);
      mLanguage = paramString1;
      mCountry = paramString2;
      mVariant = paramString3;
    }
    
    public boolean isValid()
    {
      return true;
    }
    
    protected void playImpl()
    {
      onLoadLanguage(mLanguage, mCountry, mVariant);
    }
    
    protected void stopImpl() {}
  }
  
  private class LoadVoiceItem
    extends TextToSpeechService.SpeechItem
  {
    private final String mVoiceName;
    
    public LoadVoiceItem(Object paramObject, int paramInt1, int paramInt2, String paramString)
    {
      super(paramObject, paramInt1, paramInt2);
      mVoiceName = paramString;
    }
    
    public boolean isValid()
    {
      return true;
    }
    
    protected void playImpl()
    {
      onLoadVoice(mVoiceName);
    }
    
    protected void stopImpl() {}
  }
  
  private class SilenceSpeechItem
    extends TextToSpeechService.UtteranceSpeechItem
  {
    private final long mDuration;
    private final String mUtteranceId;
    
    public SilenceSpeechItem(Object paramObject, int paramInt1, int paramInt2, String paramString, long paramLong)
    {
      super(paramObject, paramInt1, paramInt2);
      mUtteranceId = paramString;
      mDuration = paramLong;
    }
    
    public String getUtteranceId()
    {
      return mUtteranceId;
    }
    
    public boolean isValid()
    {
      return true;
    }
    
    protected void playImpl()
    {
      mAudioPlaybackHandler.enqueue(new SilencePlaybackQueueItem(this, getCallerIdentity(), mDuration));
    }
    
    protected void stopImpl() {}
  }
  
  private abstract class SpeechItem
  {
    private final Object mCallerIdentity;
    private final int mCallerPid;
    private final int mCallerUid;
    private boolean mStarted = false;
    private boolean mStopped = false;
    
    public SpeechItem(Object paramObject, int paramInt1, int paramInt2)
    {
      mCallerIdentity = paramObject;
      mCallerUid = paramInt1;
      mCallerPid = paramInt2;
    }
    
    public Object getCallerIdentity()
    {
      return mCallerIdentity;
    }
    
    public int getCallerPid()
    {
      return mCallerPid;
    }
    
    public int getCallerUid()
    {
      return mCallerUid;
    }
    
    protected boolean isStarted()
    {
      try
      {
        boolean bool = mStarted;
        return bool;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
    
    protected boolean isStopped()
    {
      try
      {
        boolean bool = mStopped;
        return bool;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
    
    public abstract boolean isValid();
    
    public void play()
    {
      try
      {
        if (!mStarted)
        {
          mStarted = true;
          playImpl();
          return;
        }
        IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
        localIllegalStateException.<init>("play() called twice");
        throw localIllegalStateException;
      }
      finally {}
    }
    
    protected abstract void playImpl();
    
    public void stop()
    {
      try
      {
        if (!mStopped)
        {
          mStopped = true;
          stopImpl();
          return;
        }
        IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
        localIllegalStateException.<init>("stop() called twice");
        throw localIllegalStateException;
      }
      finally {}
    }
    
    protected abstract void stopImpl();
  }
  
  private class SynthHandler
    extends Handler
  {
    private TextToSpeechService.SpeechItem mCurrentSpeechItem = null;
    private int mFlushAll = 0;
    private List<Object> mFlushedObjects = new ArrayList();
    
    public SynthHandler(Looper paramLooper)
    {
      super();
    }
    
    private void endFlushingSpeechItems(Object paramObject)
    {
      List localList = mFlushedObjects;
      if (paramObject == null) {
        try
        {
          mFlushAll -= 1;
        }
        finally
        {
          break label42;
        }
      } else {
        mFlushedObjects.remove(paramObject);
      }
      return;
      label42:
      throw paramObject;
    }
    
    private TextToSpeechService.SpeechItem getCurrentSpeechItem()
    {
      try
      {
        TextToSpeechService.SpeechItem localSpeechItem = mCurrentSpeechItem;
        return localSpeechItem;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
    
    private boolean isFlushed(TextToSpeechService.SpeechItem paramSpeechItem)
    {
      synchronized (mFlushedObjects)
      {
        boolean bool;
        if ((mFlushAll <= 0) && (!mFlushedObjects.contains(paramSpeechItem.getCallerIdentity()))) {
          bool = false;
        } else {
          bool = true;
        }
        return bool;
      }
    }
    
    private TextToSpeechService.SpeechItem maybeRemoveCurrentSpeechItem(Object paramObject)
    {
      try
      {
        if ((mCurrentSpeechItem != null) && (mCurrentSpeechItem.getCallerIdentity() == paramObject))
        {
          paramObject = mCurrentSpeechItem;
          mCurrentSpeechItem = null;
          return paramObject;
        }
        return null;
      }
      finally
      {
        paramObject = finally;
        throw paramObject;
      }
    }
    
    private TextToSpeechService.SpeechItem removeCurrentSpeechItem()
    {
      try
      {
        TextToSpeechService.SpeechItem localSpeechItem = mCurrentSpeechItem;
        mCurrentSpeechItem = null;
        return localSpeechItem;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
    
    private boolean setCurrentSpeechItem(TextToSpeechService.SpeechItem paramSpeechItem)
    {
      if (paramSpeechItem != null) {
        try
        {
          boolean bool = isFlushed(paramSpeechItem);
          if (bool) {
            return false;
          }
        }
        finally
        {
          break label33;
        }
      }
      mCurrentSpeechItem = paramSpeechItem;
      return true;
      label33:
      throw paramSpeechItem;
    }
    
    private void startFlushingSpeechItems(Object paramObject)
    {
      List localList = mFlushedObjects;
      if (paramObject == null) {
        try
        {
          mFlushAll += 1;
        }
        finally
        {
          break label42;
        }
      } else {
        mFlushedObjects.add(paramObject);
      }
      return;
      label42:
      throw paramObject;
    }
    
    public int enqueueSpeechItem(int paramInt, final TextToSpeechService.SpeechItem paramSpeechItem)
    {
      TextToSpeechService.UtteranceProgressDispatcher localUtteranceProgressDispatcher = null;
      if ((paramSpeechItem instanceof TextToSpeechService.UtteranceProgressDispatcher)) {
        localUtteranceProgressDispatcher = (TextToSpeechService.UtteranceProgressDispatcher)paramSpeechItem;
      }
      if (!paramSpeechItem.isValid())
      {
        if (localUtteranceProgressDispatcher != null) {
          localUtteranceProgressDispatcher.dispatchOnError(-8);
        }
        return -1;
      }
      if (paramInt == 0) {
        stopForApp(paramSpeechItem.getCallerIdentity());
      } else if (paramInt == 2) {
        stopAll();
      }
      Message localMessage = Message.obtain(this, new Runnable()
      {
        public void run()
        {
          if (TextToSpeechService.SynthHandler.this.setCurrentSpeechItem(paramSpeechItem))
          {
            paramSpeechItem.play();
            TextToSpeechService.SynthHandler.this.removeCurrentSpeechItem();
          }
          else
          {
            paramSpeechItem.stop();
          }
        }
      });
      obj = paramSpeechItem.getCallerIdentity();
      if (sendMessage(localMessage)) {
        return 0;
      }
      Log.w("TextToSpeechService", "SynthThread has quit");
      if (localUtteranceProgressDispatcher != null) {
        localUtteranceProgressDispatcher.dispatchOnError(-4);
      }
      return -1;
    }
    
    public boolean isSpeaking()
    {
      boolean bool;
      if (getCurrentSpeechItem() != null) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void quit()
    {
      getLooper().quit();
      TextToSpeechService.SpeechItem localSpeechItem = removeCurrentSpeechItem();
      if (localSpeechItem != null) {
        localSpeechItem.stop();
      }
    }
    
    public int stopAll()
    {
      startFlushingSpeechItems(null);
      TextToSpeechService.SpeechItem localSpeechItem = removeCurrentSpeechItem();
      if (localSpeechItem != null) {
        localSpeechItem.stop();
      }
      mAudioPlaybackHandler.stop();
      sendMessage(Message.obtain(this, new Runnable()
      {
        public void run()
        {
          TextToSpeechService.SynthHandler.this.endFlushingSpeechItems(null);
        }
      }));
      return 0;
    }
    
    public int stopForApp(final Object paramObject)
    {
      if (paramObject == null) {
        return -1;
      }
      startFlushingSpeechItems(paramObject);
      TextToSpeechService.SpeechItem localSpeechItem = maybeRemoveCurrentSpeechItem(paramObject);
      if (localSpeechItem != null) {
        localSpeechItem.stop();
      }
      mAudioPlaybackHandler.stopForApp(paramObject);
      sendMessage(Message.obtain(this, new Runnable()
      {
        public void run()
        {
          TextToSpeechService.SynthHandler.this.endFlushingSpeechItems(paramObject);
        }
      }));
      return 0;
    }
  }
  
  private class SynthThread
    extends HandlerThread
    implements MessageQueue.IdleHandler
  {
    private boolean mFirstIdle = true;
    
    public SynthThread()
    {
      super(0);
    }
    
    private void broadcastTtsQueueProcessingCompleted()
    {
      Intent localIntent = new Intent("android.speech.tts.TTS_QUEUE_PROCESSING_COMPLETED");
      sendBroadcast(localIntent);
    }
    
    protected void onLooperPrepared()
    {
      getLooper().getQueue().addIdleHandler(this);
    }
    
    public boolean queueIdle()
    {
      if (mFirstIdle) {
        mFirstIdle = false;
      } else {
        broadcastTtsQueueProcessingCompleted();
      }
      return true;
    }
  }
  
  class SynthesisSpeechItem
    extends TextToSpeechService.UtteranceSpeechItemWithParams
  {
    private final int mCallerUid;
    private final String[] mDefaultLocale;
    private final EventLogger mEventLogger;
    private AbstractSynthesisCallback mSynthesisCallback;
    private final SynthesisRequest mSynthesisRequest;
    private final CharSequence mText;
    
    public SynthesisSpeechItem(Object paramObject, int paramInt1, int paramInt2, Bundle paramBundle, String paramString, CharSequence paramCharSequence)
    {
      super(paramObject, paramInt1, paramInt2, paramBundle, paramString);
      mText = paramCharSequence;
      mCallerUid = paramInt1;
      mSynthesisRequest = new SynthesisRequest(mText, mParams);
      mDefaultLocale = TextToSpeechService.this.getSettingsLocale();
      setRequestParams(mSynthesisRequest);
      mEventLogger = new EventLogger(mSynthesisRequest, paramInt1, paramInt2, mPackageName);
    }
    
    private String getCountry()
    {
      if (!hasLanguage()) {
        return mDefaultLocale[1];
      }
      return getStringParam(mParams, "country", "");
    }
    
    private String getVariant()
    {
      if (!hasLanguage()) {
        return mDefaultLocale[2];
      }
      return getStringParam(mParams, "variant", "");
    }
    
    private void setRequestParams(SynthesisRequest paramSynthesisRequest)
    {
      String str = getVoiceName();
      paramSynthesisRequest.setLanguage(getLanguage(), getCountry(), getVariant());
      if (!TextUtils.isEmpty(str)) {
        paramSynthesisRequest.setVoiceName(getVoiceName());
      }
      paramSynthesisRequest.setSpeechRate(getSpeechRate());
      paramSynthesisRequest.setCallerUid(mCallerUid);
      paramSynthesisRequest.setPitch(getPitch());
    }
    
    protected AbstractSynthesisCallback createSynthesisCallback()
    {
      return new PlaybackSynthesisCallback(getAudioParams(), mAudioPlaybackHandler, this, getCallerIdentity(), mEventLogger, false);
    }
    
    public String getLanguage()
    {
      return getStringParam(mParams, "language", mDefaultLocale[0]);
    }
    
    public CharSequence getText()
    {
      return mText;
    }
    
    public String getVoiceName()
    {
      return getStringParam(mParams, "voiceName", "");
    }
    
    public boolean isValid()
    {
      if (mText == null)
      {
        Log.e("TextToSpeechService", "null synthesis text");
        return false;
      }
      if (mText.length() >= TextToSpeech.getMaxSpeechInputLength())
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Text too long: ");
        localStringBuilder.append(mText.length());
        localStringBuilder.append(" chars");
        Log.w("TextToSpeechService", localStringBuilder.toString());
        return false;
      }
      return true;
    }
    
    protected void playImpl()
    {
      mEventLogger.onRequestProcessingStart();
      try
      {
        if (isStopped()) {
          return;
        }
        mSynthesisCallback = createSynthesisCallback();
        AbstractSynthesisCallback localAbstractSynthesisCallback = mSynthesisCallback;
        onSynthesizeText(mSynthesisRequest, localAbstractSynthesisCallback);
        if ((localAbstractSynthesisCallback.hasStarted()) && (!localAbstractSynthesisCallback.hasFinished())) {
          localAbstractSynthesisCallback.done();
        }
        return;
      }
      finally {}
    }
    
    protected void stopImpl()
    {
      try
      {
        AbstractSynthesisCallback localAbstractSynthesisCallback = mSynthesisCallback;
        if (localAbstractSynthesisCallback != null)
        {
          localAbstractSynthesisCallback.stop();
          onStop();
        }
        else
        {
          dispatchOnStop();
        }
        return;
      }
      finally {}
    }
  }
  
  private class SynthesisToFileOutputStreamSpeechItem
    extends TextToSpeechService.SynthesisSpeechItem
  {
    private final FileOutputStream mFileOutputStream;
    
    public SynthesisToFileOutputStreamSpeechItem(Object paramObject, int paramInt1, int paramInt2, Bundle paramBundle, String paramString, CharSequence paramCharSequence, FileOutputStream paramFileOutputStream)
    {
      super(paramObject, paramInt1, paramInt2, paramBundle, paramString, paramCharSequence);
      mFileOutputStream = paramFileOutputStream;
    }
    
    protected AbstractSynthesisCallback createSynthesisCallback()
    {
      return new FileSynthesisCallback(mFileOutputStream.getChannel(), this, false);
    }
    
    protected void playImpl()
    {
      dispatchOnStart();
      super.playImpl();
      try
      {
        mFileOutputStream.close();
      }
      catch (IOException localIOException)
      {
        Log.w("TextToSpeechService", "Failed to close output file", localIOException);
      }
    }
  }
  
  static abstract interface UtteranceProgressDispatcher
  {
    public abstract void dispatchOnAudioAvailable(byte[] paramArrayOfByte);
    
    public abstract void dispatchOnBeginSynthesis(int paramInt1, int paramInt2, int paramInt3);
    
    public abstract void dispatchOnError(int paramInt);
    
    public abstract void dispatchOnRangeStart(int paramInt1, int paramInt2, int paramInt3);
    
    public abstract void dispatchOnStart();
    
    public abstract void dispatchOnStop();
    
    public abstract void dispatchOnSuccess();
  }
  
  private abstract class UtteranceSpeechItem
    extends TextToSpeechService.SpeechItem
    implements TextToSpeechService.UtteranceProgressDispatcher
  {
    public UtteranceSpeechItem(Object paramObject, int paramInt1, int paramInt2)
    {
      super(paramObject, paramInt1, paramInt2);
    }
    
    public void dispatchOnAudioAvailable(byte[] paramArrayOfByte)
    {
      String str = getUtteranceId();
      if (str != null) {
        mCallbacks.dispatchOnAudioAvailable(getCallerIdentity(), str, paramArrayOfByte);
      }
    }
    
    public void dispatchOnBeginSynthesis(int paramInt1, int paramInt2, int paramInt3)
    {
      String str = getUtteranceId();
      if (str != null) {
        mCallbacks.dispatchOnBeginSynthesis(getCallerIdentity(), str, paramInt1, paramInt2, paramInt3);
      }
    }
    
    public void dispatchOnError(int paramInt)
    {
      String str = getUtteranceId();
      if (str != null) {
        mCallbacks.dispatchOnError(getCallerIdentity(), str, paramInt);
      }
    }
    
    public void dispatchOnRangeStart(int paramInt1, int paramInt2, int paramInt3)
    {
      String str = getUtteranceId();
      if (str != null) {
        mCallbacks.dispatchOnRangeStart(getCallerIdentity(), str, paramInt1, paramInt2, paramInt3);
      }
    }
    
    public void dispatchOnStart()
    {
      String str = getUtteranceId();
      if (str != null) {
        mCallbacks.dispatchOnStart(getCallerIdentity(), str);
      }
    }
    
    public void dispatchOnStop()
    {
      String str = getUtteranceId();
      if (str != null) {
        mCallbacks.dispatchOnStop(getCallerIdentity(), str, isStarted());
      }
    }
    
    public void dispatchOnSuccess()
    {
      String str = getUtteranceId();
      if (str != null) {
        mCallbacks.dispatchOnSuccess(getCallerIdentity(), str);
      }
    }
    
    float getFloatParam(Bundle paramBundle, String paramString, float paramFloat)
    {
      if (paramBundle != null) {
        paramFloat = paramBundle.getFloat(paramString, paramFloat);
      }
      return paramFloat;
    }
    
    int getIntParam(Bundle paramBundle, String paramString, int paramInt)
    {
      if (paramBundle != null) {
        paramInt = paramBundle.getInt(paramString, paramInt);
      }
      return paramInt;
    }
    
    String getStringParam(Bundle paramBundle, String paramString1, String paramString2)
    {
      if (paramBundle == null) {
        paramBundle = paramString2;
      } else {
        paramBundle = paramBundle.getString(paramString1, paramString2);
      }
      return paramBundle;
    }
    
    public abstract String getUtteranceId();
  }
  
  private abstract class UtteranceSpeechItemWithParams
    extends TextToSpeechService.UtteranceSpeechItem
  {
    protected final Bundle mParams;
    protected final String mUtteranceId;
    
    UtteranceSpeechItemWithParams(Object paramObject, int paramInt1, int paramInt2, Bundle paramBundle, String paramString)
    {
      super(paramObject, paramInt1, paramInt2);
      mParams = paramBundle;
      mUtteranceId = paramString;
    }
    
    TextToSpeechService.AudioOutputParams getAudioParams()
    {
      return TextToSpeechService.AudioOutputParams.createFromParamsBundle(mParams, true);
    }
    
    int getPitch()
    {
      return getIntParam(mParams, "pitch", TextToSpeechService.this.getDefaultPitch());
    }
    
    int getSpeechRate()
    {
      return getIntParam(mParams, "rate", TextToSpeechService.this.getDefaultSpeechRate());
    }
    
    public String getUtteranceId()
    {
      return mUtteranceId;
    }
    
    boolean hasLanguage()
    {
      return TextUtils.isEmpty(getStringParam(mParams, "language", null)) ^ true;
    }
  }
}
