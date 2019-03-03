package android.text.method;

import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.ArrayUtils;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;

public class TimeKeyListener
  extends NumberKeyListener
{
  public static final char[] CHARACTERS = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 109, 112, 58 };
  private static final String SKELETON_12HOUR = "hms";
  private static final String SKELETON_24HOUR = "Hms";
  private static final String SYMBOLS_TO_IGNORE = "ahHKkms";
  @GuardedBy("sLock")
  private static final HashMap<Locale, TimeKeyListener> sInstanceCache = new HashMap();
  private static final Object sLock = new Object();
  private final char[] mCharacters;
  private final boolean mNeedsAdvancedInput;
  
  @Deprecated
  public TimeKeyListener()
  {
    this(null);
  }
  
  public TimeKeyListener(Locale paramLocale)
  {
    LinkedHashSet localLinkedHashSet = new LinkedHashSet();
    int i;
    if ((NumberKeyListener.addDigits(localLinkedHashSet, paramLocale)) && (NumberKeyListener.addAmPmChars(localLinkedHashSet, paramLocale)) && (NumberKeyListener.addFormatCharsFromSkeleton(localLinkedHashSet, paramLocale, "hms", "ahHKkms")) && (NumberKeyListener.addFormatCharsFromSkeleton(localLinkedHashSet, paramLocale, "Hms", "ahHKkms"))) {
      i = 1;
    } else {
      i = 0;
    }
    if (i != 0)
    {
      mCharacters = NumberKeyListener.collectionToArray(localLinkedHashSet);
      if ((paramLocale != null) && ("en".equals(paramLocale.getLanguage()))) {
        mNeedsAdvancedInput = false;
      } else {
        mNeedsAdvancedInput = (true ^ ArrayUtils.containsAll(CHARACTERS, mCharacters));
      }
    }
    else
    {
      mCharacters = CHARACTERS;
      mNeedsAdvancedInput = false;
    }
  }
  
  @Deprecated
  public static TimeKeyListener getInstance()
  {
    return getInstance(null);
  }
  
  public static TimeKeyListener getInstance(Locale paramLocale)
  {
    synchronized (sLock)
    {
      TimeKeyListener localTimeKeyListener1 = (TimeKeyListener)sInstanceCache.get(paramLocale);
      TimeKeyListener localTimeKeyListener2 = localTimeKeyListener1;
      if (localTimeKeyListener1 == null)
      {
        localTimeKeyListener2 = new android/text/method/TimeKeyListener;
        localTimeKeyListener2.<init>(paramLocale);
        sInstanceCache.put(paramLocale, localTimeKeyListener2);
      }
      return localTimeKeyListener2;
    }
  }
  
  protected char[] getAcceptedChars()
  {
    return mCharacters;
  }
  
  public int getInputType()
  {
    if (mNeedsAdvancedInput) {
      return 1;
    }
    return 36;
  }
}
