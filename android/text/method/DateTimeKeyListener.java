package android.text.method;

import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.ArrayUtils;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;

public class DateTimeKeyListener
  extends NumberKeyListener
{
  public static final char[] CHARACTERS = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 109, 112, 58, 47, 45, 32 };
  private static final String SKELETON_12HOUR = "yMdhms";
  private static final String SKELETON_24HOUR = "yMdHms";
  private static final String SYMBOLS_TO_IGNORE = "yMLdahHKkms";
  @GuardedBy("sLock")
  private static final HashMap<Locale, DateTimeKeyListener> sInstanceCache = new HashMap();
  private static final Object sLock = new Object();
  private final char[] mCharacters;
  private final boolean mNeedsAdvancedInput;
  
  @Deprecated
  public DateTimeKeyListener()
  {
    this(null);
  }
  
  public DateTimeKeyListener(Locale paramLocale)
  {
    LinkedHashSet localLinkedHashSet = new LinkedHashSet();
    int i;
    if ((NumberKeyListener.addDigits(localLinkedHashSet, paramLocale)) && (NumberKeyListener.addAmPmChars(localLinkedHashSet, paramLocale)) && (NumberKeyListener.addFormatCharsFromSkeleton(localLinkedHashSet, paramLocale, "yMdhms", "yMLdahHKkms")) && (NumberKeyListener.addFormatCharsFromSkeleton(localLinkedHashSet, paramLocale, "yMdHms", "yMLdahHKkms"))) {
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
  public static DateTimeKeyListener getInstance()
  {
    return getInstance(null);
  }
  
  public static DateTimeKeyListener getInstance(Locale paramLocale)
  {
    synchronized (sLock)
    {
      DateTimeKeyListener localDateTimeKeyListener1 = (DateTimeKeyListener)sInstanceCache.get(paramLocale);
      DateTimeKeyListener localDateTimeKeyListener2 = localDateTimeKeyListener1;
      if (localDateTimeKeyListener1 == null)
      {
        localDateTimeKeyListener2 = new android/text/method/DateTimeKeyListener;
        localDateTimeKeyListener2.<init>(paramLocale);
        sInstanceCache.put(paramLocale, localDateTimeKeyListener2);
      }
      return localDateTimeKeyListener2;
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
    return 4;
  }
}
