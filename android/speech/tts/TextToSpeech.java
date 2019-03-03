package android.speech.tts;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioAttributes;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.MissingResourceException;
import java.util.Set;

public class TextToSpeech
{
  public static final String ACTION_TTS_QUEUE_PROCESSING_COMPLETED = "android.speech.tts.TTS_QUEUE_PROCESSING_COMPLETED";
  public static final int ERROR = -1;
  public static final int ERROR_INVALID_REQUEST = -8;
  public static final int ERROR_NETWORK = -6;
  public static final int ERROR_NETWORK_TIMEOUT = -7;
  public static final int ERROR_NOT_INSTALLED_YET = -9;
  public static final int ERROR_OUTPUT = -5;
  public static final int ERROR_SERVICE = -4;
  public static final int ERROR_SYNTHESIS = -3;
  public static final int LANG_AVAILABLE = 0;
  public static final int LANG_COUNTRY_AVAILABLE = 1;
  public static final int LANG_COUNTRY_VAR_AVAILABLE = 2;
  public static final int LANG_MISSING_DATA = -1;
  public static final int LANG_NOT_SUPPORTED = -2;
  public static final int QUEUE_ADD = 1;
  static final int QUEUE_DESTROY = 2;
  public static final int QUEUE_FLUSH = 0;
  public static final int STOPPED = -2;
  public static final int SUCCESS = 0;
  private static final String TAG = "TextToSpeech";
  private Connection mConnectingServiceConnection;
  private final Context mContext;
  private volatile String mCurrentEngine = null;
  private final Map<String, Uri> mEarcons;
  private final TtsEngines mEnginesHelper;
  private OnInitListener mInitListener;
  private final Bundle mParams = new Bundle();
  private String mRequestedEngine;
  private Connection mServiceConnection;
  private final Object mStartLock = new Object();
  private final boolean mUseFallback;
  private volatile UtteranceProgressListener mUtteranceProgressListener;
  private final Map<CharSequence, Uri> mUtterances;
  
  public TextToSpeech(Context paramContext, OnInitListener paramOnInitListener)
  {
    this(paramContext, paramOnInitListener, null);
  }
  
  public TextToSpeech(Context paramContext, OnInitListener paramOnInitListener, String paramString)
  {
    this(paramContext, paramOnInitListener, paramString, null, true);
  }
  
  public TextToSpeech(Context paramContext, OnInitListener paramOnInitListener, String paramString1, String paramString2, boolean paramBoolean)
  {
    mContext = paramContext;
    mInitListener = paramOnInitListener;
    mRequestedEngine = paramString1;
    mUseFallback = paramBoolean;
    mEarcons = new HashMap();
    mUtterances = new HashMap();
    mUtteranceProgressListener = null;
    mEnginesHelper = new TtsEngines(mContext);
    initTts();
  }
  
  private boolean connectToEngine(String paramString)
  {
    Object localObject1 = new Connection(null);
    Object localObject2 = new Intent("android.intent.action.TTS_SERVICE");
    ((Intent)localObject2).setPackage(paramString);
    if (!mContext.bindService((Intent)localObject2, (ServiceConnection)localObject1, 1))
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Failed to bind to ");
      ((StringBuilder)localObject1).append(paramString);
      Log.e("TextToSpeech", ((StringBuilder)localObject1).toString());
      return false;
    }
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("Sucessfully bound to ");
    ((StringBuilder)localObject2).append(paramString);
    Log.i("TextToSpeech", ((StringBuilder)localObject2).toString());
    mConnectingServiceConnection = ((Connection)localObject1);
    return true;
  }
  
  private Bundle convertParamsHashMaptoBundle(HashMap<String, String> paramHashMap)
  {
    if ((paramHashMap != null) && (!paramHashMap.isEmpty()))
    {
      Bundle localBundle = new Bundle();
      copyIntParam(localBundle, paramHashMap, "streamType");
      copyIntParam(localBundle, paramHashMap, "sessionId");
      copyStringParam(localBundle, paramHashMap, "utteranceId");
      copyFloatParam(localBundle, paramHashMap, "volume");
      copyFloatParam(localBundle, paramHashMap, "pan");
      copyStringParam(localBundle, paramHashMap, "networkTts");
      copyStringParam(localBundle, paramHashMap, "embeddedTts");
      copyIntParam(localBundle, paramHashMap, "networkTimeoutMs");
      copyIntParam(localBundle, paramHashMap, "networkRetriesCount");
      if (!TextUtils.isEmpty(mCurrentEngine))
      {
        Iterator localIterator = paramHashMap.entrySet().iterator();
        while (localIterator.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator.next();
          paramHashMap = (String)localEntry.getKey();
          if ((paramHashMap != null) && (paramHashMap.startsWith(mCurrentEngine))) {
            localBundle.putString(paramHashMap, (String)localEntry.getValue());
          }
        }
      }
      return localBundle;
    }
    return null;
  }
  
  private void copyFloatParam(Bundle paramBundle, HashMap<String, String> paramHashMap, String paramString)
  {
    paramHashMap = (String)paramHashMap.get(paramString);
    if (!TextUtils.isEmpty(paramHashMap)) {
      try
      {
        paramBundle.putFloat(paramString, Float.parseFloat(paramHashMap));
      }
      catch (NumberFormatException paramBundle) {}
    }
  }
  
  private void copyIntParam(Bundle paramBundle, HashMap<String, String> paramHashMap, String paramString)
  {
    paramHashMap = (String)paramHashMap.get(paramString);
    if (!TextUtils.isEmpty(paramHashMap)) {
      try
      {
        paramBundle.putInt(paramString, Integer.parseInt(paramHashMap));
      }
      catch (NumberFormatException paramBundle) {}
    }
  }
  
  private void copyStringParam(Bundle paramBundle, HashMap<String, String> paramHashMap, String paramString)
  {
    paramHashMap = (String)paramHashMap.get(paramString);
    if (paramHashMap != null) {
      paramBundle.putString(paramString, paramHashMap);
    }
  }
  
  private void dispatchOnInit(int paramInt)
  {
    synchronized (mStartLock)
    {
      if (mInitListener != null)
      {
        mInitListener.onInit(paramInt);
        mInitListener = null;
      }
      return;
    }
  }
  
  private IBinder getCallerIdentity()
  {
    return mServiceConnection.getCallerIdentity();
  }
  
  public static int getMaxSpeechInputLength()
  {
    return 4000;
  }
  
  private Bundle getParams(Bundle paramBundle)
  {
    if ((paramBundle != null) && (!paramBundle.isEmpty()))
    {
      Bundle localBundle = new Bundle(mParams);
      localBundle.putAll(paramBundle);
      verifyIntegerBundleParam(localBundle, "streamType");
      verifyIntegerBundleParam(localBundle, "sessionId");
      verifyStringBundleParam(localBundle, "utteranceId");
      verifyFloatBundleParam(localBundle, "volume");
      verifyFloatBundleParam(localBundle, "pan");
      verifyBooleanBundleParam(localBundle, "networkTts");
      verifyBooleanBundleParam(localBundle, "embeddedTts");
      verifyIntegerBundleParam(localBundle, "networkTimeoutMs");
      verifyIntegerBundleParam(localBundle, "networkRetriesCount");
      return localBundle;
    }
    return mParams;
  }
  
  private Voice getVoice(ITextToSpeechService paramITextToSpeechService, String paramString)
    throws RemoteException
  {
    paramITextToSpeechService = paramITextToSpeechService.getVoices();
    if (paramITextToSpeechService == null)
    {
      Log.w("TextToSpeech", "getVoices returned null");
      return null;
    }
    paramITextToSpeechService = paramITextToSpeechService.iterator();
    while (paramITextToSpeechService.hasNext())
    {
      Voice localVoice = (Voice)paramITextToSpeechService.next();
      if (localVoice.getName().equals(paramString)) {
        return localVoice;
      }
    }
    paramITextToSpeechService = new StringBuilder();
    paramITextToSpeechService.append("Could not find voice ");
    paramITextToSpeechService.append(paramString);
    paramITextToSpeechService.append(" in voice list");
    Log.w("TextToSpeech", paramITextToSpeechService.toString());
    return null;
  }
  
  private int initTts()
  {
    if (mRequestedEngine != null) {
      if (mEnginesHelper.isEngineInstalled(mRequestedEngine))
      {
        if (connectToEngine(mRequestedEngine))
        {
          mCurrentEngine = mRequestedEngine;
          return 0;
        }
        if (!mUseFallback)
        {
          mCurrentEngine = null;
          dispatchOnInit(-1);
          return -1;
        }
      }
      else if (!mUseFallback)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Requested engine not installed: ");
        ((StringBuilder)localObject).append(mRequestedEngine);
        Log.i("TextToSpeech", ((StringBuilder)localObject).toString());
        mCurrentEngine = null;
        dispatchOnInit(-1);
        return -1;
      }
    }
    String str = getDefaultEngine();
    if ((str != null) && (!str.equals(mRequestedEngine)) && (connectToEngine(str)))
    {
      mCurrentEngine = str;
      return 0;
    }
    Object localObject = mEnginesHelper.getHighestRankedEngineName();
    if ((localObject != null) && (!((String)localObject).equals(mRequestedEngine)) && (!((String)localObject).equals(str)) && (connectToEngine((String)localObject)))
    {
      mCurrentEngine = ((String)localObject);
      return 0;
    }
    mCurrentEngine = null;
    dispatchOnInit(-1);
    return -1;
  }
  
  private Uri makeResourceUri(String paramString, int paramInt)
  {
    return new Uri.Builder().scheme("android.resource").encodedAuthority(paramString).appendEncodedPath(String.valueOf(paramInt)).build();
  }
  
  private <R> R runAction(Action<R> paramAction, R paramR, String paramString)
  {
    return runAction(paramAction, paramR, paramString, true, true);
  }
  
  private <R> R runAction(Action<R> paramAction, R paramR, String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    synchronized (mStartLock)
    {
      if (mServiceConnection == null)
      {
        paramAction = new java/lang/StringBuilder;
        paramAction.<init>();
        paramAction.append(paramString);
        paramAction.append(" failed: not bound to TTS engine");
        Log.w("TextToSpeech", paramAction.toString());
        return paramR;
      }
      paramAction = mServiceConnection.runAction(paramAction, paramR, paramString, paramBoolean1, paramBoolean2);
      return paramAction;
    }
  }
  
  private <R> R runActionNoReconnect(Action<R> paramAction, R paramR, String paramString, boolean paramBoolean)
  {
    return runAction(paramAction, paramR, paramString, false, paramBoolean);
  }
  
  private static boolean verifyBooleanBundleParam(Bundle paramBundle, String paramString)
  {
    if ((paramBundle.containsKey(paramString)) && (!(paramBundle.get(paramString) instanceof Boolean)) && (!(paramBundle.get(paramString) instanceof String)))
    {
      paramBundle.remove(paramString);
      paramBundle = new StringBuilder();
      paramBundle.append("Synthesis request paramter ");
      paramBundle.append(paramString);
      paramBundle.append(" containst value  with invalid type. Should be a Boolean or String");
      Log.w("TextToSpeech", paramBundle.toString());
      return false;
    }
    return true;
  }
  
  private static boolean verifyFloatBundleParam(Bundle paramBundle, String paramString)
  {
    if ((paramBundle.containsKey(paramString)) && (!(paramBundle.get(paramString) instanceof Float)) && (!(paramBundle.get(paramString) instanceof Double)))
    {
      paramBundle.remove(paramString);
      paramBundle = new StringBuilder();
      paramBundle.append("Synthesis request paramter ");
      paramBundle.append(paramString);
      paramBundle.append(" containst value  with invalid type. Should be a Float or a Double");
      Log.w("TextToSpeech", paramBundle.toString());
      return false;
    }
    return true;
  }
  
  private static boolean verifyIntegerBundleParam(Bundle paramBundle, String paramString)
  {
    if ((paramBundle.containsKey(paramString)) && (!(paramBundle.get(paramString) instanceof Integer)) && (!(paramBundle.get(paramString) instanceof Long)))
    {
      paramBundle.remove(paramString);
      paramBundle = new StringBuilder();
      paramBundle.append("Synthesis request paramter ");
      paramBundle.append(paramString);
      paramBundle.append(" containst value  with invalid type. Should be an Integer or a Long");
      Log.w("TextToSpeech", paramBundle.toString());
      return false;
    }
    return true;
  }
  
  private static boolean verifyStringBundleParam(Bundle paramBundle, String paramString)
  {
    if ((paramBundle.containsKey(paramString)) && (!(paramBundle.get(paramString) instanceof String)))
    {
      paramBundle.remove(paramString);
      paramBundle = new StringBuilder();
      paramBundle.append("Synthesis request paramter ");
      paramBundle.append(paramString);
      paramBundle.append(" containst value  with invalid type. Should be a String");
      Log.w("TextToSpeech", paramBundle.toString());
      return false;
    }
    return true;
  }
  
  public int addEarcon(String paramString, File paramFile)
  {
    synchronized (mStartLock)
    {
      mEarcons.put(paramString, Uri.fromFile(paramFile));
      return 0;
    }
  }
  
  @Deprecated
  public int addEarcon(String paramString1, String paramString2)
  {
    synchronized (mStartLock)
    {
      mEarcons.put(paramString1, Uri.parse(paramString2));
      return 0;
    }
  }
  
  public int addEarcon(String paramString1, String paramString2, int paramInt)
  {
    synchronized (mStartLock)
    {
      mEarcons.put(paramString1, makeResourceUri(paramString2, paramInt));
      return 0;
    }
  }
  
  public int addSpeech(CharSequence paramCharSequence, File paramFile)
  {
    synchronized (mStartLock)
    {
      mUtterances.put(paramCharSequence, Uri.fromFile(paramFile));
      return 0;
    }
  }
  
  public int addSpeech(CharSequence paramCharSequence, String paramString, int paramInt)
  {
    synchronized (mStartLock)
    {
      mUtterances.put(paramCharSequence, makeResourceUri(paramString, paramInt));
      return 0;
    }
  }
  
  public int addSpeech(String paramString1, String paramString2)
  {
    synchronized (mStartLock)
    {
      mUtterances.put(paramString1, Uri.parse(paramString2));
      return 0;
    }
  }
  
  public int addSpeech(String paramString1, String paramString2, int paramInt)
  {
    synchronized (mStartLock)
    {
      mUtterances.put(paramString1, makeResourceUri(paramString2, paramInt));
      return 0;
    }
  }
  
  @Deprecated
  public boolean areDefaultsEnforced()
  {
    return false;
  }
  
  public Set<Locale> getAvailableLanguages()
  {
    (Set)runAction(new Action()
    {
      public Set<Locale> run(ITextToSpeechService paramAnonymousITextToSpeechService)
        throws RemoteException
      {
        Object localObject = paramAnonymousITextToSpeechService.getVoices();
        if (localObject == null) {
          return new HashSet();
        }
        paramAnonymousITextToSpeechService = new HashSet();
        localObject = ((List)localObject).iterator();
        while (((Iterator)localObject).hasNext()) {
          paramAnonymousITextToSpeechService.add(((Voice)((Iterator)localObject).next()).getLocale());
        }
        return paramAnonymousITextToSpeechService;
      }
    }, null, "getAvailableLanguages");
  }
  
  public String getCurrentEngine()
  {
    return mCurrentEngine;
  }
  
  public String getDefaultEngine()
  {
    return mEnginesHelper.getDefaultEngine();
  }
  
  @Deprecated
  public Locale getDefaultLanguage()
  {
    (Locale)runAction(new Action()
    {
      public Locale run(ITextToSpeechService paramAnonymousITextToSpeechService)
        throws RemoteException
      {
        paramAnonymousITextToSpeechService = paramAnonymousITextToSpeechService.getClientDefaultLanguage();
        return new Locale(paramAnonymousITextToSpeechService[0], paramAnonymousITextToSpeechService[1], paramAnonymousITextToSpeechService[2]);
      }
    }, null, "getDefaultLanguage");
  }
  
  public Voice getDefaultVoice()
  {
    (Voice)runAction(new Action()
    {
      public Voice run(ITextToSpeechService paramAnonymousITextToSpeechService)
        throws RemoteException
      {
        Object localObject = paramAnonymousITextToSpeechService.getClientDefaultLanguage();
        if ((localObject != null) && (localObject.length != 0))
        {
          String str1 = localObject[0];
          if (localObject.length > 1) {
            str2 = localObject[1];
          } else {
            str2 = "";
          }
          if (localObject.length > 2) {
            localObject = localObject[2];
          } else {
            localObject = "";
          }
          if (paramAnonymousITextToSpeechService.isLanguageAvailable(str1, str2, (String)localObject) < 0) {
            return null;
          }
          String str2 = paramAnonymousITextToSpeechService.getDefaultVoiceNameFor(str1, str2, (String)localObject);
          if (TextUtils.isEmpty(str2)) {
            return null;
          }
          paramAnonymousITextToSpeechService = paramAnonymousITextToSpeechService.getVoices();
          if (paramAnonymousITextToSpeechService == null) {
            return null;
          }
          paramAnonymousITextToSpeechService = paramAnonymousITextToSpeechService.iterator();
          while (paramAnonymousITextToSpeechService.hasNext())
          {
            localObject = (Voice)paramAnonymousITextToSpeechService.next();
            if (((Voice)localObject).getName().equals(str2)) {
              return localObject;
            }
          }
          return null;
        }
        Log.e("TextToSpeech", "service.getClientDefaultLanguage() returned empty array");
        return null;
      }
    }, null, "getDefaultVoice");
  }
  
  public List<EngineInfo> getEngines()
  {
    return mEnginesHelper.getEngines();
  }
  
  @Deprecated
  public Set<String> getFeatures(final Locale paramLocale)
  {
    (Set)runAction(new Action()
    {
      public Set<String> run(ITextToSpeechService paramAnonymousITextToSpeechService)
        throws RemoteException
      {
        try
        {
          paramAnonymousITextToSpeechService = paramAnonymousITextToSpeechService.getFeaturesForLanguage(paramLocale.getISO3Language(), paramLocale.getISO3Country(), paramLocale.getVariant());
          if (paramAnonymousITextToSpeechService != null)
          {
            HashSet localHashSet = new HashSet();
            Collections.addAll(localHashSet, paramAnonymousITextToSpeechService);
            return localHashSet;
          }
          return null;
        }
        catch (MissingResourceException localMissingResourceException)
        {
          paramAnonymousITextToSpeechService = new StringBuilder();
          paramAnonymousITextToSpeechService.append("Couldn't retrieve 3 letter ISO 639-2/T language and/or ISO 3166 country code for locale: ");
          paramAnonymousITextToSpeechService.append(paramLocale);
          Log.w("TextToSpeech", paramAnonymousITextToSpeechService.toString(), localMissingResourceException);
        }
        return null;
      }
    }, null, "getFeatures");
  }
  
  @Deprecated
  public Locale getLanguage()
  {
    (Locale)runAction(new Action()
    {
      public Locale run(ITextToSpeechService paramAnonymousITextToSpeechService)
      {
        return new Locale(mParams.getString("language", ""), mParams.getString("country", ""), mParams.getString("variant", ""));
      }
    }, null, "getLanguage");
  }
  
  public Voice getVoice()
  {
    (Voice)runAction(new Action()
    {
      public Voice run(ITextToSpeechService paramAnonymousITextToSpeechService)
        throws RemoteException
      {
        String str = mParams.getString("voiceName", "");
        if (TextUtils.isEmpty(str)) {
          return null;
        }
        return TextToSpeech.this.getVoice(paramAnonymousITextToSpeechService, str);
      }
    }, null, "getVoice");
  }
  
  public Set<Voice> getVoices()
  {
    (Set)runAction(new Action()
    {
      public Set<Voice> run(ITextToSpeechService paramAnonymousITextToSpeechService)
        throws RemoteException
      {
        paramAnonymousITextToSpeechService = paramAnonymousITextToSpeechService.getVoices();
        if (paramAnonymousITextToSpeechService != null) {
          paramAnonymousITextToSpeechService = new HashSet(paramAnonymousITextToSpeechService);
        } else {
          paramAnonymousITextToSpeechService = new HashSet();
        }
        return paramAnonymousITextToSpeechService;
      }
    }, null, "getVoices");
  }
  
  public int isLanguageAvailable(final Locale paramLocale)
  {
    ((Integer)runAction(new Action()
    {
      public Integer run(ITextToSpeechService paramAnonymousITextToSpeechService)
        throws RemoteException
      {
        try
        {
          String str1 = paramLocale.getISO3Language();
          try
          {
            String str2 = paramLocale.getISO3Country();
            return Integer.valueOf(paramAnonymousITextToSpeechService.isLanguageAvailable(str1, str2, paramLocale.getVariant()));
          }
          catch (MissingResourceException localMissingResourceException)
          {
            paramAnonymousITextToSpeechService = new StringBuilder();
            paramAnonymousITextToSpeechService.append("Couldn't retrieve ISO 3166 country code for locale: ");
            paramAnonymousITextToSpeechService.append(paramLocale);
            Log.w("TextToSpeech", paramAnonymousITextToSpeechService.toString(), localMissingResourceException);
            return Integer.valueOf(-2);
          }
          StringBuilder localStringBuilder;
          return Integer.valueOf(-2);
        }
        catch (MissingResourceException paramAnonymousITextToSpeechService)
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("Couldn't retrieve ISO 639-2/T language code for locale: ");
          localStringBuilder.append(paramLocale);
          Log.w("TextToSpeech", localStringBuilder.toString(), paramAnonymousITextToSpeechService);
        }
      }
    }, Integer.valueOf(-2), "isLanguageAvailable")).intValue();
  }
  
  public boolean isSpeaking()
  {
    ((Boolean)runAction(new Action()
    {
      public Boolean run(ITextToSpeechService paramAnonymousITextToSpeechService)
        throws RemoteException
      {
        return Boolean.valueOf(paramAnonymousITextToSpeechService.isSpeaking());
      }
    }, Boolean.valueOf(false), "isSpeaking")).booleanValue();
  }
  
  public int playEarcon(final String paramString1, final int paramInt, final Bundle paramBundle, final String paramString2)
  {
    ((Integer)runAction(new Action()
    {
      public Integer run(ITextToSpeechService paramAnonymousITextToSpeechService)
        throws RemoteException
      {
        Uri localUri = (Uri)mEarcons.get(paramString1);
        if (localUri == null) {
          return Integer.valueOf(-1);
        }
        return Integer.valueOf(paramAnonymousITextToSpeechService.playAudio(TextToSpeech.this.getCallerIdentity(), localUri, paramInt, TextToSpeech.this.getParams(paramBundle), paramString2));
      }
    }, Integer.valueOf(-1), "playEarcon")).intValue();
  }
  
  @Deprecated
  public int playEarcon(String paramString, int paramInt, HashMap<String, String> paramHashMap)
  {
    Bundle localBundle = convertParamsHashMaptoBundle(paramHashMap);
    if (paramHashMap == null) {
      paramHashMap = null;
    } else {
      paramHashMap = (String)paramHashMap.get("utteranceId");
    }
    return playEarcon(paramString, paramInt, localBundle, paramHashMap);
  }
  
  @Deprecated
  public int playSilence(long paramLong, int paramInt, HashMap<String, String> paramHashMap)
  {
    if (paramHashMap == null) {
      paramHashMap = null;
    } else {
      paramHashMap = (String)paramHashMap.get("utteranceId");
    }
    return playSilentUtterance(paramLong, paramInt, paramHashMap);
  }
  
  public int playSilentUtterance(final long paramLong, int paramInt, final String paramString)
  {
    ((Integer)runAction(new Action()
    {
      public Integer run(ITextToSpeechService paramAnonymousITextToSpeechService)
        throws RemoteException
      {
        return Integer.valueOf(paramAnonymousITextToSpeechService.playSilence(TextToSpeech.this.getCallerIdentity(), paramLong, paramString, val$utteranceId));
      }
    }, Integer.valueOf(-1), "playSilentUtterance")).intValue();
  }
  
  public int setAudioAttributes(AudioAttributes paramAudioAttributes)
  {
    if (paramAudioAttributes != null) {
      synchronized (mStartLock)
      {
        mParams.putParcelable("audioAttributes", paramAudioAttributes);
        return 0;
      }
    }
    return -1;
  }
  
  @Deprecated
  public int setEngineByPackageName(String paramString)
  {
    mRequestedEngine = paramString;
    return initTts();
  }
  
  public int setLanguage(final Locale paramLocale)
  {
    ((Integer)runAction(new Action()
    {
      public Integer run(ITextToSpeechService paramAnonymousITextToSpeechService)
        throws RemoteException
      {
        if (paramLocale == null) {
          return Integer.valueOf(-2);
        }
        try
        {
          Object localObject1 = paramLocale.getISO3Language();
          try
          {
            String str1 = paramLocale.getISO3Country();
            Object localObject3 = paramLocale.getVariant();
            int i = paramAnonymousITextToSpeechService.isLanguageAvailable((String)localObject1, str1, (String)localObject3);
            if (i >= 0)
            {
              String str2 = paramAnonymousITextToSpeechService.getDefaultVoiceNameFor((String)localObject1, str1, (String)localObject3);
              if (TextUtils.isEmpty(str2))
              {
                paramAnonymousITextToSpeechService = new StringBuilder();
                paramAnonymousITextToSpeechService.append("Couldn't find the default voice for ");
                paramAnonymousITextToSpeechService.append((String)localObject1);
                paramAnonymousITextToSpeechService.append("-");
                paramAnonymousITextToSpeechService.append(str1);
                paramAnonymousITextToSpeechService.append("-");
                paramAnonymousITextToSpeechService.append((String)localObject3);
                Log.w("TextToSpeech", paramAnonymousITextToSpeechService.toString());
                return Integer.valueOf(-2);
              }
              if (paramAnonymousITextToSpeechService.loadVoice(TextToSpeech.this.getCallerIdentity(), str2) == -1)
              {
                paramAnonymousITextToSpeechService = new StringBuilder();
                paramAnonymousITextToSpeechService.append("The service claimed ");
                paramAnonymousITextToSpeechService.append((String)localObject1);
                paramAnonymousITextToSpeechService.append("-");
                paramAnonymousITextToSpeechService.append(str1);
                paramAnonymousITextToSpeechService.append("-");
                paramAnonymousITextToSpeechService.append((String)localObject3);
                paramAnonymousITextToSpeechService.append(" was available with voice name ");
                paramAnonymousITextToSpeechService.append(str2);
                paramAnonymousITextToSpeechService.append(" but loadVoice returned ERROR");
                Log.w("TextToSpeech", paramAnonymousITextToSpeechService.toString());
                return Integer.valueOf(-2);
              }
              Voice localVoice = TextToSpeech.this.getVoice(paramAnonymousITextToSpeechService, str2);
              if (localVoice == null)
              {
                paramAnonymousITextToSpeechService = new StringBuilder();
                paramAnonymousITextToSpeechService.append("getDefaultVoiceNameFor returned ");
                paramAnonymousITextToSpeechService.append(str2);
                paramAnonymousITextToSpeechService.append(" for locale ");
                paramAnonymousITextToSpeechService.append((String)localObject1);
                paramAnonymousITextToSpeechService.append("-");
                paramAnonymousITextToSpeechService.append(str1);
                paramAnonymousITextToSpeechService.append("-");
                paramAnonymousITextToSpeechService.append((String)localObject3);
                paramAnonymousITextToSpeechService.append(" but getVoice returns null");
                Log.w("TextToSpeech", paramAnonymousITextToSpeechService.toString());
                return Integer.valueOf(-2);
              }
              paramAnonymousITextToSpeechService = "";
              try
              {
                str1 = localVoice.getLocale().getISO3Language();
                paramAnonymousITextToSpeechService = str1;
              }
              catch (MissingResourceException localMissingResourceException2)
              {
                localObject1 = new StringBuilder();
                ((StringBuilder)localObject1).append("Couldn't retrieve ISO 639-2/T language code for locale: ");
                ((StringBuilder)localObject1).append(localVoice.getLocale());
                Log.w("TextToSpeech", ((StringBuilder)localObject1).toString(), localMissingResourceException2);
              }
              Object localObject2 = "";
              try
              {
                localObject1 = localVoice.getLocale().getISO3Country();
                localObject2 = localObject1;
              }
              catch (MissingResourceException localMissingResourceException1)
              {
                localObject3 = new StringBuilder();
                ((StringBuilder)localObject3).append("Couldn't retrieve ISO 3166 country code for locale: ");
                ((StringBuilder)localObject3).append(localVoice.getLocale());
                Log.w("TextToSpeech", ((StringBuilder)localObject3).toString(), localMissingResourceException1);
              }
              mParams.putString("voiceName", str2);
              mParams.putString("language", paramAnonymousITextToSpeechService);
              mParams.putString("country", (String)localObject2);
              mParams.putString("variant", localVoice.getLocale().getVariant());
            }
            return Integer.valueOf(i);
          }
          catch (MissingResourceException localMissingResourceException3)
          {
            paramAnonymousITextToSpeechService = new StringBuilder();
            paramAnonymousITextToSpeechService.append("Couldn't retrieve ISO 3166 country code for locale: ");
            paramAnonymousITextToSpeechService.append(paramLocale);
            Log.w("TextToSpeech", paramAnonymousITextToSpeechService.toString(), localMissingResourceException3);
            return Integer.valueOf(-2);
          }
          return Integer.valueOf(-2);
        }
        catch (MissingResourceException localMissingResourceException4)
        {
          paramAnonymousITextToSpeechService = new StringBuilder();
          paramAnonymousITextToSpeechService.append("Couldn't retrieve ISO 639-2/T language code for locale: ");
          paramAnonymousITextToSpeechService.append(paramLocale);
          Log.w("TextToSpeech", paramAnonymousITextToSpeechService.toString(), localMissingResourceException4);
        }
      }
    }, Integer.valueOf(-2), "setLanguage")).intValue();
  }
  
  @Deprecated
  public int setOnUtteranceCompletedListener(OnUtteranceCompletedListener paramOnUtteranceCompletedListener)
  {
    mUtteranceProgressListener = UtteranceProgressListener.from(paramOnUtteranceCompletedListener);
    return 0;
  }
  
  public int setOnUtteranceProgressListener(UtteranceProgressListener paramUtteranceProgressListener)
  {
    mUtteranceProgressListener = paramUtteranceProgressListener;
    return 0;
  }
  
  public int setPitch(float paramFloat)
  {
    if (paramFloat > 0.0F)
    {
      int i = (int)(100.0F * paramFloat);
      if (i > 0) {
        synchronized (mStartLock)
        {
          mParams.putInt("pitch", i);
          return 0;
        }
      }
    }
    return -1;
  }
  
  public int setSpeechRate(float paramFloat)
  {
    if (paramFloat > 0.0F)
    {
      int i = (int)(100.0F * paramFloat);
      if (i > 0) {
        synchronized (mStartLock)
        {
          mParams.putInt("rate", i);
          return 0;
        }
      }
    }
    return -1;
  }
  
  public int setVoice(final Voice paramVoice)
  {
    ((Integer)runAction(new Action()
    {
      public Integer run(ITextToSpeechService paramAnonymousITextToSpeechService)
        throws RemoteException
      {
        int i = paramAnonymousITextToSpeechService.loadVoice(TextToSpeech.this.getCallerIdentity(), paramVoice.getName());
        if (i == 0)
        {
          mParams.putString("voiceName", paramVoice.getName());
          paramAnonymousITextToSpeechService = "";
          Object localObject2;
          try
          {
            String str = paramVoice.getLocale().getISO3Language();
            paramAnonymousITextToSpeechService = str;
          }
          catch (MissingResourceException localMissingResourceException1)
          {
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("Couldn't retrieve ISO 639-2/T language code for locale: ");
            ((StringBuilder)localObject2).append(paramVoice.getLocale());
            Log.w("TextToSpeech", ((StringBuilder)localObject2).toString(), localMissingResourceException1);
          }
          Object localObject1 = "";
          try
          {
            localObject2 = paramVoice.getLocale().getISO3Country();
            localObject1 = localObject2;
          }
          catch (MissingResourceException localMissingResourceException2)
          {
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("Couldn't retrieve ISO 3166 country code for locale: ");
            ((StringBuilder)localObject2).append(paramVoice.getLocale());
            Log.w("TextToSpeech", ((StringBuilder)localObject2).toString(), localMissingResourceException2);
          }
          mParams.putString("language", paramAnonymousITextToSpeechService);
          mParams.putString("country", (String)localObject1);
          mParams.putString("variant", paramVoice.getLocale().getVariant());
        }
        return Integer.valueOf(i);
      }
    }, Integer.valueOf(-2), "setVoice")).intValue();
  }
  
  public void shutdown()
  {
    synchronized (mStartLock)
    {
      if (mConnectingServiceConnection != null)
      {
        mContext.unbindService(mConnectingServiceConnection);
        mConnectingServiceConnection = null;
        return;
      }
      runActionNoReconnect(new Action()
      {
        public Void run(ITextToSpeechService paramAnonymousITextToSpeechService)
          throws RemoteException
        {
          paramAnonymousITextToSpeechService.setCallback(TextToSpeech.this.getCallerIdentity(), null);
          paramAnonymousITextToSpeechService.stop(TextToSpeech.this.getCallerIdentity());
          mServiceConnection.disconnect();
          TextToSpeech.access$202(TextToSpeech.this, null);
          TextToSpeech.access$302(TextToSpeech.this, null);
          return null;
        }
      }, null, "shutdown", false);
      return;
    }
  }
  
  public int speak(final CharSequence paramCharSequence, final int paramInt, final Bundle paramBundle, final String paramString)
  {
    ((Integer)runAction(new Action()
    {
      public Integer run(ITextToSpeechService paramAnonymousITextToSpeechService)
        throws RemoteException
      {
        Uri localUri = (Uri)mUtterances.get(paramCharSequence);
        if (localUri != null) {
          return Integer.valueOf(paramAnonymousITextToSpeechService.playAudio(TextToSpeech.this.getCallerIdentity(), localUri, paramInt, TextToSpeech.this.getParams(paramBundle), paramString));
        }
        return Integer.valueOf(paramAnonymousITextToSpeechService.speak(TextToSpeech.this.getCallerIdentity(), paramCharSequence, paramInt, TextToSpeech.this.getParams(paramBundle), paramString));
      }
    }, Integer.valueOf(-1), "speak")).intValue();
  }
  
  @Deprecated
  public int speak(String paramString, int paramInt, HashMap<String, String> paramHashMap)
  {
    Bundle localBundle = convertParamsHashMaptoBundle(paramHashMap);
    if (paramHashMap == null) {
      paramHashMap = null;
    } else {
      paramHashMap = (String)paramHashMap.get("utteranceId");
    }
    return speak(paramString, paramInt, localBundle, paramHashMap);
  }
  
  public int stop()
  {
    ((Integer)runAction(new Action()
    {
      public Integer run(ITextToSpeechService paramAnonymousITextToSpeechService)
        throws RemoteException
      {
        return Integer.valueOf(paramAnonymousITextToSpeechService.stop(TextToSpeech.this.getCallerIdentity()));
      }
    }, Integer.valueOf(-1), "stop")).intValue();
  }
  
  public int synthesizeToFile(final CharSequence paramCharSequence, final Bundle paramBundle, final File paramFile, final String paramString)
  {
    ((Integer)runAction(new Action()
    {
      public Integer run(ITextToSpeechService paramAnonymousITextToSpeechService)
        throws RemoteException
      {
        try
        {
          if ((paramFile.exists()) && (!paramFile.canWrite()))
          {
            paramAnonymousITextToSpeechService = new java/lang/StringBuilder;
            paramAnonymousITextToSpeechService.<init>();
            paramAnonymousITextToSpeechService.append("Can't write to ");
            paramAnonymousITextToSpeechService.append(paramFile);
            Log.e("TextToSpeech", paramAnonymousITextToSpeechService.toString());
            return Integer.valueOf(-1);
          }
          localObject = ParcelFileDescriptor.open(paramFile, 738197504);
          int i = paramAnonymousITextToSpeechService.synthesizeToFileDescriptor(TextToSpeech.this.getCallerIdentity(), paramCharSequence, (ParcelFileDescriptor)localObject, TextToSpeech.this.getParams(paramBundle), paramString);
          ((ParcelFileDescriptor)localObject).close();
          return Integer.valueOf(i);
        }
        catch (IOException paramAnonymousITextToSpeechService)
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Closing file ");
          ((StringBuilder)localObject).append(paramFile);
          ((StringBuilder)localObject).append(" failed");
          Log.e("TextToSpeech", ((StringBuilder)localObject).toString(), paramAnonymousITextToSpeechService);
          return Integer.valueOf(-1);
        }
        catch (FileNotFoundException paramAnonymousITextToSpeechService)
        {
          Object localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Opening file ");
          ((StringBuilder)localObject).append(paramFile);
          ((StringBuilder)localObject).append(" failed");
          Log.e("TextToSpeech", ((StringBuilder)localObject).toString(), paramAnonymousITextToSpeechService);
        }
        return Integer.valueOf(-1);
      }
    }, Integer.valueOf(-1), "synthesizeToFile")).intValue();
  }
  
  @Deprecated
  public int synthesizeToFile(String paramString1, HashMap<String, String> paramHashMap, String paramString2)
  {
    return synthesizeToFile(paramString1, convertParamsHashMaptoBundle(paramHashMap), new File(paramString2), (String)paramHashMap.get("utteranceId"));
  }
  
  private static abstract interface Action<R>
  {
    public abstract R run(ITextToSpeechService paramITextToSpeechService)
      throws RemoteException;
  }
  
  private class Connection
    implements ServiceConnection
  {
    private final ITextToSpeechCallback.Stub mCallback = new ITextToSpeechCallback.Stub()
    {
      public void onAudioAvailable(String paramAnonymousString, byte[] paramAnonymousArrayOfByte)
      {
        UtteranceProgressListener localUtteranceProgressListener = mUtteranceProgressListener;
        if (localUtteranceProgressListener != null) {
          localUtteranceProgressListener.onAudioAvailable(paramAnonymousString, paramAnonymousArrayOfByte);
        }
      }
      
      public void onBeginSynthesis(String paramAnonymousString, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
      {
        UtteranceProgressListener localUtteranceProgressListener = mUtteranceProgressListener;
        if (localUtteranceProgressListener != null) {
          localUtteranceProgressListener.onBeginSynthesis(paramAnonymousString, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3);
        }
      }
      
      public void onError(String paramAnonymousString, int paramAnonymousInt)
      {
        UtteranceProgressListener localUtteranceProgressListener = mUtteranceProgressListener;
        if (localUtteranceProgressListener != null) {
          localUtteranceProgressListener.onError(paramAnonymousString);
        }
      }
      
      public void onRangeStart(String paramAnonymousString, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
      {
        UtteranceProgressListener localUtteranceProgressListener = mUtteranceProgressListener;
        if (localUtteranceProgressListener != null) {
          localUtteranceProgressListener.onRangeStart(paramAnonymousString, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3);
        }
      }
      
      public void onStart(String paramAnonymousString)
      {
        UtteranceProgressListener localUtteranceProgressListener = mUtteranceProgressListener;
        if (localUtteranceProgressListener != null) {
          localUtteranceProgressListener.onStart(paramAnonymousString);
        }
      }
      
      public void onStop(String paramAnonymousString, boolean paramAnonymousBoolean)
        throws RemoteException
      {
        UtteranceProgressListener localUtteranceProgressListener = mUtteranceProgressListener;
        if (localUtteranceProgressListener != null) {
          localUtteranceProgressListener.onStop(paramAnonymousString, paramAnonymousBoolean);
        }
      }
      
      public void onSuccess(String paramAnonymousString)
      {
        UtteranceProgressListener localUtteranceProgressListener = mUtteranceProgressListener;
        if (localUtteranceProgressListener != null) {
          localUtteranceProgressListener.onDone(paramAnonymousString);
        }
      }
    };
    private boolean mEstablished;
    private SetupConnectionAsyncTask mOnSetupConnectionAsyncTask;
    private ITextToSpeechService mService;
    
    private Connection() {}
    
    private boolean clearServiceConnection()
    {
      Object localObject1 = mStartLock;
      boolean bool = false;
      try
      {
        if (mOnSetupConnectionAsyncTask != null)
        {
          bool = mOnSetupConnectionAsyncTask.cancel(false);
          mOnSetupConnectionAsyncTask = null;
        }
        mService = null;
        if (mServiceConnection == this) {
          TextToSpeech.access$202(TextToSpeech.this, null);
        }
        return bool;
      }
      finally {}
    }
    
    public void disconnect()
    {
      mContext.unbindService(this);
      clearServiceConnection();
    }
    
    public IBinder getCallerIdentity()
    {
      return mCallback;
    }
    
    public boolean isEstablished()
    {
      boolean bool;
      if ((mService != null) && (mEstablished)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
    {
      synchronized (mStartLock)
      {
        TextToSpeech.access$1602(TextToSpeech.this, null);
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Connected to ");
        localStringBuilder.append(paramComponentName);
        Log.i("TextToSpeech", localStringBuilder.toString());
        if (mOnSetupConnectionAsyncTask != null) {
          mOnSetupConnectionAsyncTask.cancel(false);
        }
        mService = ITextToSpeechService.Stub.asInterface(paramIBinder);
        TextToSpeech.access$202(TextToSpeech.this, this);
        mEstablished = false;
        paramIBinder = new android/speech/tts/TextToSpeech$Connection$SetupConnectionAsyncTask;
        paramIBinder.<init>(this, paramComponentName);
        mOnSetupConnectionAsyncTask = paramIBinder;
        mOnSetupConnectionAsyncTask.execute(new Void[0]);
        return;
      }
    }
    
    public void onServiceDisconnected(ComponentName paramComponentName)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Asked to disconnect from ");
      localStringBuilder.append(paramComponentName);
      Log.i("TextToSpeech", localStringBuilder.toString());
      if (clearServiceConnection()) {
        TextToSpeech.this.dispatchOnInit(-1);
      }
    }
    
    public <R> R runAction(TextToSpeech.Action<R> paramAction, R paramR, String paramString, boolean paramBoolean1, boolean paramBoolean2)
    {
      try
      {
        synchronized (mStartLock)
        {
          if (mService == null)
          {
            paramAction = new java/lang/StringBuilder;
            paramAction.<init>();
            paramAction.append(paramString);
            paramAction.append(" failed: not connected to TTS engine");
            Log.w("TextToSpeech", paramAction.toString());
            return paramR;
          }
          if ((paramBoolean2) && (!isEstablished()))
          {
            paramAction = new java/lang/StringBuilder;
            paramAction.<init>();
            paramAction.append(paramString);
            paramAction.append(" failed: TTS engine connection not fully set up");
            Log.w("TextToSpeech", paramAction.toString());
            return paramR;
          }
          paramAction = paramAction.run(mService);
          return paramAction;
        }
      }
      catch (RemoteException localRemoteException)
      {
        paramAction = new java/lang/StringBuilder;
        paramAction.<init>();
        paramAction.append(paramString);
        paramAction.append(" failed");
        Log.e("TextToSpeech", paramAction.toString(), localRemoteException);
        if (paramBoolean1)
        {
          disconnect();
          TextToSpeech.this.initTts();
        }
        return paramR;
      }
    }
    
    private class SetupConnectionAsyncTask
      extends AsyncTask<Void, Void, Integer>
    {
      private final ComponentName mName;
      
      public SetupConnectionAsyncTask(ComponentName paramComponentName)
      {
        mName = paramComponentName;
      }
      
      protected Integer doInBackground(Void... arg1)
      {
        synchronized (mStartLock)
        {
          if (isCancelled()) {
            return null;
          }
          try
          {
            mService.setCallback(getCallerIdentity(), mCallback);
            if (mParams.getString("language") == null)
            {
              localObject1 = mService.getClientDefaultLanguage();
              mParams.putString("language", localObject1[0]);
              mParams.putString("country", localObject1[1]);
              mParams.putString("variant", localObject1[2]);
              localObject1 = mService.getDefaultVoiceNameFor(localObject1[0], localObject1[1], localObject1[2]);
              mParams.putString("voiceName", (String)localObject1);
            }
            Object localObject1 = new java/lang/StringBuilder;
            ((StringBuilder)localObject1).<init>();
            ((StringBuilder)localObject1).append("Set up connection to ");
            ((StringBuilder)localObject1).append(mName);
            Log.i("TextToSpeech", ((StringBuilder)localObject1).toString());
            return Integer.valueOf(0);
          }
          catch (RemoteException localRemoteException)
          {
            Log.e("TextToSpeech", "Error connecting to service, setCallback() failed");
            return Integer.valueOf(-1);
          }
        }
      }
      
      protected void onPostExecute(Integer paramInteger)
      {
        synchronized (mStartLock)
        {
          if (mOnSetupConnectionAsyncTask == this) {
            TextToSpeech.Connection.access$1302(TextToSpeech.Connection.this, null);
          }
          TextToSpeech.Connection.access$1402(TextToSpeech.Connection.this, true);
          TextToSpeech.this.dispatchOnInit(paramInteger.intValue());
          return;
        }
      }
    }
  }
  
  public class Engine
  {
    public static final String ACTION_CHECK_TTS_DATA = "android.speech.tts.engine.CHECK_TTS_DATA";
    public static final String ACTION_GET_SAMPLE_TEXT = "android.speech.tts.engine.GET_SAMPLE_TEXT";
    public static final String ACTION_INSTALL_TTS_DATA = "android.speech.tts.engine.INSTALL_TTS_DATA";
    public static final String ACTION_TTS_DATA_INSTALLED = "android.speech.tts.engine.TTS_DATA_INSTALLED";
    @Deprecated
    public static final int CHECK_VOICE_DATA_BAD_DATA = -1;
    public static final int CHECK_VOICE_DATA_FAIL = 0;
    @Deprecated
    public static final int CHECK_VOICE_DATA_MISSING_DATA = -2;
    @Deprecated
    public static final int CHECK_VOICE_DATA_MISSING_VOLUME = -3;
    public static final int CHECK_VOICE_DATA_PASS = 1;
    @Deprecated
    public static final String DEFAULT_ENGINE = "com.svox.pico";
    public static final float DEFAULT_PAN = 0.0F;
    public static final int DEFAULT_PITCH = 100;
    public static final int DEFAULT_RATE = 100;
    public static final int DEFAULT_STREAM = 3;
    public static final float DEFAULT_VOLUME = 1.0F;
    public static final String EXTRA_AVAILABLE_VOICES = "availableVoices";
    @Deprecated
    public static final String EXTRA_CHECK_VOICE_DATA_FOR = "checkVoiceDataFor";
    public static final String EXTRA_SAMPLE_TEXT = "sampleText";
    @Deprecated
    public static final String EXTRA_TTS_DATA_INSTALLED = "dataInstalled";
    public static final String EXTRA_UNAVAILABLE_VOICES = "unavailableVoices";
    @Deprecated
    public static final String EXTRA_VOICE_DATA_FILES = "dataFiles";
    @Deprecated
    public static final String EXTRA_VOICE_DATA_FILES_INFO = "dataFilesInfo";
    @Deprecated
    public static final String EXTRA_VOICE_DATA_ROOT_DIRECTORY = "dataRoot";
    public static final String INTENT_ACTION_TTS_SERVICE = "android.intent.action.TTS_SERVICE";
    @Deprecated
    public static final String KEY_FEATURE_EMBEDDED_SYNTHESIS = "embeddedTts";
    public static final String KEY_FEATURE_NETWORK_RETRIES_COUNT = "networkRetriesCount";
    @Deprecated
    public static final String KEY_FEATURE_NETWORK_SYNTHESIS = "networkTts";
    public static final String KEY_FEATURE_NETWORK_TIMEOUT_MS = "networkTimeoutMs";
    public static final String KEY_FEATURE_NOT_INSTALLED = "notInstalled";
    public static final String KEY_PARAM_AUDIO_ATTRIBUTES = "audioAttributes";
    public static final String KEY_PARAM_COUNTRY = "country";
    public static final String KEY_PARAM_ENGINE = "engine";
    public static final String KEY_PARAM_LANGUAGE = "language";
    public static final String KEY_PARAM_PAN = "pan";
    public static final String KEY_PARAM_PITCH = "pitch";
    public static final String KEY_PARAM_RATE = "rate";
    public static final String KEY_PARAM_SESSION_ID = "sessionId";
    public static final String KEY_PARAM_STREAM = "streamType";
    public static final String KEY_PARAM_UTTERANCE_ID = "utteranceId";
    public static final String KEY_PARAM_VARIANT = "variant";
    public static final String KEY_PARAM_VOICE_NAME = "voiceName";
    public static final String KEY_PARAM_VOLUME = "volume";
    public static final String SERVICE_META_DATA = "android.speech.tts";
    public static final int USE_DEFAULTS = 0;
    
    public Engine() {}
  }
  
  public static class EngineInfo
  {
    public int icon;
    public String label;
    public String name;
    public int priority;
    public boolean system;
    
    public EngineInfo() {}
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("EngineInfo{name=");
      localStringBuilder.append(name);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Error {}
  
  public static abstract interface OnInitListener
  {
    public abstract void onInit(int paramInt);
  }
  
  @Deprecated
  public static abstract interface OnUtteranceCompletedListener
  {
    public abstract void onUtteranceCompleted(String paramString);
  }
}
