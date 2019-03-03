package android.net.captiveportal;

import android.text.TextUtils;
import android.util.Log;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public abstract class CaptivePortalProbeSpec
{
  public static final String HTTP_LOCATION_HEADER_NAME = "Location";
  private static final String REGEX_SEPARATOR = "@@/@@";
  private static final String SPEC_SEPARATOR = "@@,@@";
  private static final String TAG = CaptivePortalProbeSpec.class.getSimpleName();
  private final String mEncodedSpec;
  private final URL mUrl;
  
  CaptivePortalProbeSpec(String paramString, URL paramURL)
  {
    mEncodedSpec = paramString;
    mUrl = paramURL;
  }
  
  public static CaptivePortalProbeSpec[] parseCaptivePortalProbeSpecs(String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    if (paramString != null) {
      for (String str1 : TextUtils.split(paramString, "@@,@@")) {
        try
        {
          localArrayList.add(parseSpec(str1));
        }
        catch (ParseException|MalformedURLException localParseException)
        {
          String str2 = TAG;
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Invalid probe spec: ");
          localStringBuilder.append(str1);
          Log.e(str2, localStringBuilder.toString(), localParseException);
        }
      }
    }
    if (localArrayList.isEmpty()) {
      Log.e(TAG, String.format("could not create any validation spec from %s", new Object[] { paramString }));
    }
    return (CaptivePortalProbeSpec[])localArrayList.toArray(new CaptivePortalProbeSpec[localArrayList.size()]);
  }
  
  private static Pattern parsePatternIfNonEmpty(String paramString, int paramInt)
    throws ParseException
  {
    if (TextUtils.isEmpty(paramString)) {
      return null;
    }
    try
    {
      Pattern localPattern = Pattern.compile(paramString);
      return localPattern;
    }
    catch (PatternSyntaxException localPatternSyntaxException)
    {
      throw new ParseException(String.format("Invalid status pattern [%s]: %s", new Object[] { paramString, localPatternSyntaxException }), paramInt);
    }
  }
  
  public static CaptivePortalProbeSpec parseSpec(String paramString)
    throws ParseException, MalformedURLException
  {
    if (!TextUtils.isEmpty(paramString))
    {
      String[] arrayOfString = TextUtils.split(paramString, "@@/@@");
      if (arrayOfString.length == 3)
      {
        int i = arrayOfString[0].length() + "@@/@@".length();
        int j = arrayOfString[1].length();
        int k = "@@/@@".length();
        Pattern localPattern1 = parsePatternIfNonEmpty(arrayOfString[1], i);
        Pattern localPattern2 = parsePatternIfNonEmpty(arrayOfString[2], j + i + k);
        return new RegexMatchProbeSpec(paramString, new URL(arrayOfString[0]), localPattern1, localPattern2);
      }
      throw new ParseException("Probe spec does not have 3 parts", 0);
    }
    throw new ParseException("Empty probe spec", 0);
  }
  
  public static CaptivePortalProbeSpec parseSpecOrNull(String paramString)
  {
    if (paramString != null) {
      try
      {
        CaptivePortalProbeSpec localCaptivePortalProbeSpec = parseSpec(paramString);
        return localCaptivePortalProbeSpec;
      }
      catch (ParseException|MalformedURLException localParseException)
      {
        String str = TAG;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Invalid probe spec: ");
        localStringBuilder.append(paramString);
        Log.e(str, localStringBuilder.toString(), localParseException);
      }
    }
    return null;
  }
  
  private static boolean safeMatch(String paramString, Pattern paramPattern)
  {
    boolean bool;
    if ((paramPattern != null) && (!TextUtils.isEmpty(paramString)) && (!paramPattern.matcher(paramString).matches())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public String getEncodedSpec()
  {
    return mEncodedSpec;
  }
  
  public abstract CaptivePortalProbeResult getResult(int paramInt, String paramString);
  
  public URL getUrl()
  {
    return mUrl;
  }
  
  private static class RegexMatchProbeSpec
    extends CaptivePortalProbeSpec
  {
    final Pattern mLocationHeaderRegex;
    final Pattern mStatusRegex;
    
    RegexMatchProbeSpec(String paramString, URL paramURL, Pattern paramPattern1, Pattern paramPattern2)
    {
      super(paramURL);
      mStatusRegex = paramPattern1;
      mLocationHeaderRegex = paramPattern2;
    }
    
    public CaptivePortalProbeResult getResult(int paramInt, String paramString)
    {
      boolean bool1 = CaptivePortalProbeSpec.safeMatch(String.valueOf(paramInt), mStatusRegex);
      boolean bool2 = CaptivePortalProbeSpec.safeMatch(paramString, mLocationHeaderRegex);
      if ((bool1) && (bool2)) {
        paramInt = 204;
      } else {
        paramInt = 302;
      }
      return new CaptivePortalProbeResult(paramInt, paramString, getUrl().toString(), this);
    }
  }
}
