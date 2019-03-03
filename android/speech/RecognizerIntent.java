package android.speech;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

public class RecognizerIntent
{
  public static final String ACTION_GET_LANGUAGE_DETAILS = "android.speech.action.GET_LANGUAGE_DETAILS";
  public static final String ACTION_RECOGNIZE_SPEECH = "android.speech.action.RECOGNIZE_SPEECH";
  public static final String ACTION_VOICE_SEARCH_HANDS_FREE = "android.speech.action.VOICE_SEARCH_HANDS_FREE";
  public static final String ACTION_WEB_SEARCH = "android.speech.action.WEB_SEARCH";
  public static final String DETAILS_META_DATA = "android.speech.DETAILS";
  public static final String EXTRA_CALLING_PACKAGE = "calling_package";
  public static final String EXTRA_CONFIDENCE_SCORES = "android.speech.extra.CONFIDENCE_SCORES";
  public static final String EXTRA_LANGUAGE = "android.speech.extra.LANGUAGE";
  public static final String EXTRA_LANGUAGE_MODEL = "android.speech.extra.LANGUAGE_MODEL";
  public static final String EXTRA_LANGUAGE_PREFERENCE = "android.speech.extra.LANGUAGE_PREFERENCE";
  public static final String EXTRA_MAX_RESULTS = "android.speech.extra.MAX_RESULTS";
  public static final String EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE = "android.speech.extra.ONLY_RETURN_LANGUAGE_PREFERENCE";
  public static final String EXTRA_ORIGIN = "android.speech.extra.ORIGIN";
  public static final String EXTRA_PARTIAL_RESULTS = "android.speech.extra.PARTIAL_RESULTS";
  public static final String EXTRA_PREFER_OFFLINE = "android.speech.extra.PREFER_OFFLINE";
  public static final String EXTRA_PROMPT = "android.speech.extra.PROMPT";
  public static final String EXTRA_RESULTS = "android.speech.extra.RESULTS";
  public static final String EXTRA_RESULTS_PENDINGINTENT = "android.speech.extra.RESULTS_PENDINGINTENT";
  public static final String EXTRA_RESULTS_PENDINGINTENT_BUNDLE = "android.speech.extra.RESULTS_PENDINGINTENT_BUNDLE";
  public static final String EXTRA_SECURE = "android.speech.extras.EXTRA_SECURE";
  public static final String EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS = "android.speech.extras.SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS";
  public static final String EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS = "android.speech.extras.SPEECH_INPUT_MINIMUM_LENGTH_MILLIS";
  public static final String EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS = "android.speech.extras.SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS";
  public static final String EXTRA_SUPPORTED_LANGUAGES = "android.speech.extra.SUPPORTED_LANGUAGES";
  public static final String EXTRA_WEB_SEARCH_ONLY = "android.speech.extra.WEB_SEARCH_ONLY";
  public static final String LANGUAGE_MODEL_FREE_FORM = "free_form";
  public static final String LANGUAGE_MODEL_WEB_SEARCH = "web_search";
  public static final int RESULT_AUDIO_ERROR = 5;
  public static final int RESULT_CLIENT_ERROR = 2;
  public static final int RESULT_NETWORK_ERROR = 4;
  public static final int RESULT_NO_MATCH = 1;
  public static final int RESULT_SERVER_ERROR = 3;
  
  private RecognizerIntent() {}
  
  public static final Intent getVoiceDetailsIntent(Context paramContext)
  {
    Intent localIntent = new Intent("android.speech.action.WEB_SEARCH");
    ResolveInfo localResolveInfo = paramContext.getPackageManager().resolveActivity(localIntent, 128);
    if ((localResolveInfo != null) && (activityInfo != null) && (activityInfo.metaData != null))
    {
      paramContext = activityInfo.metaData.getString("android.speech.DETAILS");
      if (paramContext == null) {
        return null;
      }
      localIntent = new Intent("android.speech.action.GET_LANGUAGE_DETAILS");
      localIntent.setComponent(new ComponentName(activityInfo.packageName, paramContext));
      return localIntent;
    }
    return null;
  }
}
