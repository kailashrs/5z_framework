package android.text.util;

import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.URLSpan;
import android.util.Patterns;
import android.view.textclassifier.TextClassifier;
import android.view.textclassifier.TextClassifier.EntityConfig;
import android.view.textclassifier.TextLinks.Request.Builder;
import android.view.textclassifier.TextLinksParams;
import android.webkit.WebView;
import android.widget.TextView;
import com.android.i18n.phonenumbers.PhoneNumberMatch;
import com.android.i18n.phonenumbers.PhoneNumberUtil;
import com.android.i18n.phonenumbers.PhoneNumberUtil.Leniency;
import com.android.internal.util.Preconditions;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import libcore.util.EmptyArray;

public class Linkify
{
  public static final int ALL = 15;
  public static final int EMAIL_ADDRESSES = 2;
  @Deprecated
  public static final int MAP_ADDRESSES = 8;
  public static final int PHONE_NUMBERS = 4;
  private static final int PHONE_NUMBER_MINIMUM_DIGITS = 5;
  public static final int WEB_URLS = 1;
  public static final MatchFilter sPhoneNumberMatchFilter = new MatchFilter()
  {
    public final boolean acceptMatch(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2)
    {
      int i = 0;
      int j = paramAnonymousInt1;
      while (j < paramAnonymousInt2)
      {
        paramAnonymousInt1 = i;
        if (Character.isDigit(paramAnonymousCharSequence.charAt(j)))
        {
          i++;
          paramAnonymousInt1 = i;
          if (i >= 5) {
            return true;
          }
        }
        j++;
        i = paramAnonymousInt1;
      }
      return false;
    }
  };
  public static final TransformFilter sPhoneNumberTransformFilter = new TransformFilter()
  {
    public final String transformUrl(Matcher paramAnonymousMatcher, String paramAnonymousString)
    {
      return Patterns.digitsAndPlusOnly(paramAnonymousMatcher);
    }
  };
  public static final MatchFilter sUrlMatchFilter = new MatchFilter()
  {
    public final boolean acceptMatch(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2)
    {
      if (paramAnonymousInt1 == 0) {
        return true;
      }
      return paramAnonymousCharSequence.charAt(paramAnonymousInt1 - 1) != '@';
    }
  };
  
  public Linkify() {}
  
  private static final void addLinkMovementMethod(TextView paramTextView)
  {
    MovementMethod localMovementMethod = paramTextView.getMovementMethod();
    if (((localMovementMethod == null) || (!(localMovementMethod instanceof LinkMovementMethod))) && (paramTextView.getLinksClickable())) {
      paramTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }
  }
  
  public static final void addLinks(TextView paramTextView, Pattern paramPattern, String paramString)
  {
    addLinks(paramTextView, paramPattern, paramString, null, null, null);
  }
  
  public static final void addLinks(TextView paramTextView, Pattern paramPattern, String paramString, MatchFilter paramMatchFilter, TransformFilter paramTransformFilter)
  {
    addLinks(paramTextView, paramPattern, paramString, null, paramMatchFilter, paramTransformFilter);
  }
  
  public static final void addLinks(TextView paramTextView, Pattern paramPattern, String paramString, String[] paramArrayOfString, MatchFilter paramMatchFilter, TransformFilter paramTransformFilter)
  {
    SpannableString localSpannableString = SpannableString.valueOf(paramTextView.getText());
    if (addLinks(localSpannableString, paramPattern, paramString, paramArrayOfString, paramMatchFilter, paramTransformFilter))
    {
      paramTextView.setText(localSpannableString);
      addLinkMovementMethod(paramTextView);
    }
  }
  
  public static final boolean addLinks(Spannable paramSpannable, int paramInt)
  {
    return addLinks(paramSpannable, paramInt, null);
  }
  
  private static boolean addLinks(Spannable paramSpannable, int paramInt, Context paramContext)
  {
    if (paramInt == 0) {
      return false;
    }
    Object localObject = (URLSpan[])paramSpannable.getSpans(0, paramSpannable.length(), URLSpan.class);
    for (int i = localObject.length - 1; i >= 0; i--) {
      paramSpannable.removeSpan(localObject[i]);
    }
    ArrayList localArrayList = new ArrayList();
    if ((paramInt & 0x1) != 0)
    {
      localObject = Patterns.AUTOLINK_WEB_URL;
      MatchFilter localMatchFilter = sUrlMatchFilter;
      gatherLinks(localArrayList, paramSpannable, (Pattern)localObject, new String[] { "http://", "https://", "rtsp://" }, localMatchFilter, null);
    }
    if ((paramInt & 0x2) != 0) {
      gatherLinks(localArrayList, paramSpannable, Patterns.AUTOLINK_EMAIL_ADDRESS, new String[] { "mailto:" }, null, null);
    }
    if ((paramInt & 0x4) != 0) {
      gatherTelLinks(localArrayList, paramSpannable, paramContext);
    }
    if ((paramInt & 0x8) != 0) {
      gatherMapLinks(localArrayList, paramSpannable);
    }
    pruneOverlaps(localArrayList);
    if (localArrayList.size() == 0) {
      return false;
    }
    localObject = localArrayList.iterator();
    while (((Iterator)localObject).hasNext())
    {
      paramContext = (LinkSpec)((Iterator)localObject).next();
      applyLink(url, start, end, paramSpannable);
    }
    return true;
  }
  
  public static final boolean addLinks(Spannable paramSpannable, Pattern paramPattern, String paramString)
  {
    return addLinks(paramSpannable, paramPattern, paramString, null, null, null);
  }
  
  public static final boolean addLinks(Spannable paramSpannable, Pattern paramPattern, String paramString, MatchFilter paramMatchFilter, TransformFilter paramTransformFilter)
  {
    return addLinks(paramSpannable, paramPattern, paramString, null, paramMatchFilter, paramTransformFilter);
  }
  
  public static final boolean addLinks(Spannable paramSpannable, Pattern paramPattern, String paramString, String[] paramArrayOfString, MatchFilter paramMatchFilter, TransformFilter paramTransformFilter)
  {
    String str = paramString;
    if (paramString == null) {
      str = "";
    }
    if (paramArrayOfString != null)
    {
      paramString = paramArrayOfString;
      if (paramArrayOfString.length >= 1) {}
    }
    else
    {
      paramString = EmptyArray.STRING;
    }
    String[] arrayOfString = new String[paramString.length + 1];
    arrayOfString[0] = str.toLowerCase(Locale.ROOT);
    for (int i = 0; i < paramString.length; i++)
    {
      paramArrayOfString = paramString[i];
      if (paramArrayOfString == null) {
        paramArrayOfString = "";
      } else {
        paramArrayOfString = paramArrayOfString.toLowerCase(Locale.ROOT);
      }
      arrayOfString[(i + 1)] = paramArrayOfString;
    }
    boolean bool1 = false;
    paramPattern = paramPattern.matcher(paramSpannable);
    while (paramPattern.find())
    {
      i = paramPattern.start();
      int j = paramPattern.end();
      boolean bool2 = true;
      if (paramMatchFilter != null) {
        bool2 = paramMatchFilter.acceptMatch(paramSpannable, i, j);
      }
      if (bool2)
      {
        applyLink(makeUrl(paramPattern.group(0), arrayOfString, paramPattern, paramTransformFilter), i, j, paramSpannable);
        bool1 = true;
      }
    }
    return bool1;
  }
  
  public static final boolean addLinks(TextView paramTextView, int paramInt)
  {
    if (paramInt == 0) {
      return false;
    }
    Context localContext = paramTextView.getContext();
    Object localObject = paramTextView.getText();
    if ((localObject instanceof Spannable))
    {
      if (addLinks((Spannable)localObject, paramInt, localContext))
      {
        addLinkMovementMethod(paramTextView);
        return true;
      }
      return false;
    }
    localObject = SpannableString.valueOf((CharSequence)localObject);
    if (addLinks((Spannable)localObject, paramInt, localContext))
    {
      addLinkMovementMethod(paramTextView);
      paramTextView.setText((CharSequence)localObject);
      return true;
    }
    return false;
  }
  
  public static Future<Void> addLinksAsync(Spannable paramSpannable, TextClassifier paramTextClassifier, int paramInt)
  {
    return addLinksAsync(paramSpannable, paramTextClassifier, TextLinksParams.fromLinkMask(paramInt), null, null);
  }
  
  public static Future<Void> addLinksAsync(Spannable paramSpannable, TextClassifier paramTextClassifier, TextLinksParams paramTextLinksParams)
  {
    return addLinksAsync(paramSpannable, paramTextClassifier, paramTextLinksParams, null, null);
  }
  
  public static Future<Void> addLinksAsync(Spannable paramSpannable, TextClassifier paramTextClassifier, TextLinksParams paramTextLinksParams, Executor paramExecutor, Consumer<Integer> paramConsumer)
  {
    return addLinksAsync(paramSpannable, paramTextClassifier, paramTextLinksParams, paramExecutor, paramConsumer, null);
  }
  
  private static Future<Void> addLinksAsync(Spannable paramSpannable, TextClassifier paramTextClassifier, TextLinksParams paramTextLinksParams, Executor paramExecutor, Consumer<Integer> paramConsumer, Runnable paramRunnable)
  {
    Preconditions.checkNotNull(paramSpannable);
    Preconditions.checkNotNull(paramTextClassifier);
    CharSequence localCharSequence = paramSpannable.subSequence(0, Math.min(paramSpannable.length(), paramTextClassifier.getMaxGenerateLinksTextLength()));
    TextClassifier.EntityConfig localEntityConfig;
    if (paramTextLinksParams == null) {
      localEntityConfig = null;
    } else {
      localEntityConfig = paramTextLinksParams.getEntityConfig();
    }
    paramTextClassifier = new _..Lambda.Linkify.hPjCKfcU4vqhADicCa9bWKrOoog(paramTextClassifier, new TextLinks.Request.Builder(localCharSequence).setLegacyFallback(true).setEntityConfig(localEntityConfig).build());
    paramSpannable = new _..Lambda.Linkify.ZGgxzuK_YqBkZXo_7HE4xwOLsh0(paramConsumer, paramSpannable, localCharSequence, paramTextLinksParams, paramRunnable);
    if (paramExecutor == null) {
      return CompletableFuture.supplyAsync(paramTextClassifier).thenAccept(paramSpannable);
    }
    return CompletableFuture.supplyAsync(paramTextClassifier, paramExecutor).thenAccept(paramSpannable);
  }
  
  public static Future<Void> addLinksAsync(TextView paramTextView, int paramInt)
  {
    return addLinksAsync(paramTextView, TextLinksParams.fromLinkMask(paramInt), null, null);
  }
  
  public static Future<Void> addLinksAsync(TextView paramTextView, TextLinksParams paramTextLinksParams)
  {
    return addLinksAsync(paramTextView, paramTextLinksParams, null, null);
  }
  
  public static Future<Void> addLinksAsync(TextView paramTextView, TextLinksParams paramTextLinksParams, Executor paramExecutor, Consumer<Integer> paramConsumer)
  {
    Preconditions.checkNotNull(paramTextView);
    Object localObject1 = paramTextView.getText();
    Object localObject2;
    if ((localObject1 instanceof Spannable)) {
      localObject2 = (Spannable)localObject1;
    } else {
      localObject2 = SpannableString.valueOf((CharSequence)localObject1);
    }
    localObject1 = new _..Lambda.Linkify.wWMJCtMwD1HLtUFna4kOfNQK1Z0(paramTextView, (Spannable)localObject2, (CharSequence)localObject1);
    return addLinksAsync((Spannable)localObject2, paramTextView.getTextClassifier(), paramTextLinksParams, paramExecutor, paramConsumer, (Runnable)localObject1);
  }
  
  private static final void applyLink(String paramString, int paramInt1, int paramInt2, Spannable paramSpannable)
  {
    paramSpannable.setSpan(new URLSpan(paramString), paramInt1, paramInt2, 33);
  }
  
  private static final void gatherLinks(ArrayList<LinkSpec> paramArrayList, Spannable paramSpannable, Pattern paramPattern, String[] paramArrayOfString, MatchFilter paramMatchFilter, TransformFilter paramTransformFilter)
  {
    Matcher localMatcher = paramPattern.matcher(paramSpannable);
    while (localMatcher.find())
    {
      int i = localMatcher.start();
      int j = localMatcher.end();
      if ((paramMatchFilter == null) || (paramMatchFilter.acceptMatch(paramSpannable, i, j)))
      {
        paramPattern = new LinkSpec();
        url = makeUrl(localMatcher.group(0), paramArrayOfString, localMatcher, paramTransformFilter);
        start = i;
        end = j;
        paramArrayList.add(paramPattern);
      }
    }
  }
  
  private static final void gatherMapLinks(ArrayList<LinkSpec> paramArrayList, Spannable paramSpannable)
  {
    paramSpannable = paramSpannable.toString();
    int i = 0;
    try
    {
      for (;;)
      {
        String str = WebView.findAddress(paramSpannable);
        if (str == null) {
          break;
        }
        int j = paramSpannable.indexOf(str);
        if (j < 0) {
          break;
        }
        LinkSpec localLinkSpec = new android/text/util/LinkSpec;
        localLinkSpec.<init>();
        int k = j + str.length();
        start = (i + j);
        end = (i + k);
        paramSpannable = paramSpannable.substring(k);
        i += k;
        try
        {
          str = URLEncoder.encode(str, "UTF-8");
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("geo:0,0?q=");
          localStringBuilder.append(str);
          url = localStringBuilder.toString();
          paramArrayList.add(localLinkSpec);
        }
        catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
      }
      return;
    }
    catch (UnsupportedOperationException paramArrayList) {}
  }
  
  private static void gatherTelLinks(ArrayList<LinkSpec> paramArrayList, Spannable paramSpannable, Context paramContext)
  {
    Object localObject = PhoneNumberUtil.getInstance();
    if (paramContext == null) {
      paramContext = TelephonyManager.getDefault();
    } else {
      paramContext = TelephonyManager.from(paramContext);
    }
    Iterator localIterator = ((PhoneNumberUtil)localObject).findNumbers(paramSpannable.toString(), paramContext.getSimCountryIso().toUpperCase(Locale.US), PhoneNumberUtil.Leniency.POSSIBLE, Long.MAX_VALUE).iterator();
    while (localIterator.hasNext())
    {
      paramSpannable = (PhoneNumberMatch)localIterator.next();
      localObject = new LinkSpec();
      paramContext = new StringBuilder();
      paramContext.append("tel:");
      paramContext.append(PhoneNumberUtils.normalizeNumber(paramSpannable.rawString()));
      url = paramContext.toString();
      start = paramSpannable.start();
      end = paramSpannable.end();
      paramArrayList.add(localObject);
    }
  }
  
  private static final String makeUrl(String paramString, String[] paramArrayOfString, Matcher paramMatcher, TransformFilter paramTransformFilter)
  {
    String str = paramString;
    if (paramTransformFilter != null) {
      str = paramTransformFilter.transformUrl(paramMatcher, paramString);
    }
    int i = 0;
    int k;
    for (int j = 0;; j++)
    {
      k = i;
      paramString = str;
      if (j >= paramArrayOfString.length) {
        break;
      }
      if (str.regionMatches(true, 0, paramArrayOfString[j], 0, paramArrayOfString[j].length()))
      {
        i = 1;
        k = i;
        paramString = str;
        if (str.regionMatches(false, 0, paramArrayOfString[j], 0, paramArrayOfString[j].length())) {
          break;
        }
        paramString = new StringBuilder();
        paramString.append(paramArrayOfString[j]);
        paramString.append(str.substring(paramArrayOfString[j].length()));
        paramString = paramString.toString();
        k = i;
        break;
      }
    }
    paramMatcher = paramString;
    if (k == 0)
    {
      paramMatcher = paramString;
      if (paramArrayOfString.length > 0)
      {
        paramMatcher = new StringBuilder();
        paramMatcher.append(paramArrayOfString[0]);
        paramMatcher.append(paramString);
        paramMatcher = paramMatcher.toString();
      }
    }
    return paramMatcher;
  }
  
  private static final void pruneOverlaps(ArrayList<LinkSpec> paramArrayList)
  {
    Collections.sort(paramArrayList, new Comparator()
    {
      public final int compare(LinkSpec paramAnonymousLinkSpec1, LinkSpec paramAnonymousLinkSpec2)
      {
        if (start < start) {
          return -1;
        }
        if (start > start) {
          return 1;
        }
        if (end < end) {
          return 1;
        }
        if (end > end) {
          return -1;
        }
        return 0;
      }
    });
    int i = paramArrayList.size();
    int j = 0;
    while (j < i - 1)
    {
      LinkSpec localLinkSpec1 = (LinkSpec)paramArrayList.get(j);
      LinkSpec localLinkSpec2 = (LinkSpec)paramArrayList.get(j + 1);
      int k = -1;
      if ((start <= start) && (end > start))
      {
        if (end <= end) {
          k = j + 1;
        } else if (end - start > end - start) {
          k = j + 1;
        } else if (end - start < end - start) {
          k = j;
        }
        if (k != -1)
        {
          paramArrayList.remove(k);
          i--;
          continue;
        }
      }
      j++;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface LinkifyMask {}
  
  public static abstract interface MatchFilter
  {
    public abstract boolean acceptMatch(CharSequence paramCharSequence, int paramInt1, int paramInt2);
  }
  
  public static abstract interface TransformFilter
  {
    public abstract String transformUrl(Matcher paramMatcher, String paramString);
  }
}
