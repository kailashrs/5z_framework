package android.text;

import com.android.internal.annotations.VisibleForTesting;
import java.util.Locale;

public final class BidiFormatter
{
  private static final int DEFAULT_FLAGS = 2;
  private static final BidiFormatter DEFAULT_LTR_INSTANCE = new BidiFormatter(false, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
  private static final BidiFormatter DEFAULT_RTL_INSTANCE = new BidiFormatter(true, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
  private static TextDirectionHeuristic DEFAULT_TEXT_DIRECTION_HEURISTIC = TextDirectionHeuristics.FIRSTSTRONG_LTR;
  private static final int DIR_LTR = -1;
  private static final int DIR_RTL = 1;
  private static final int DIR_UNKNOWN = 0;
  private static final String EMPTY_STRING = "";
  private static final int FLAG_STEREO_RESET = 2;
  private static final char LRE = '‪';
  private static final char LRM = '‎';
  private static final String LRM_STRING = Character.toString('‎');
  private static final char PDF = '‬';
  private static final char RLE = '‫';
  private static final char RLM = '‏';
  private static final String RLM_STRING = Character.toString('‏');
  private final TextDirectionHeuristic mDefaultTextDirectionHeuristic;
  private final int mFlags;
  private final boolean mIsRtlContext;
  
  private BidiFormatter(boolean paramBoolean, int paramInt, TextDirectionHeuristic paramTextDirectionHeuristic)
  {
    mIsRtlContext = paramBoolean;
    mFlags = paramInt;
    mDefaultTextDirectionHeuristic = paramTextDirectionHeuristic;
  }
  
  private static BidiFormatter getDefaultInstanceFromContext(boolean paramBoolean)
  {
    BidiFormatter localBidiFormatter;
    if (paramBoolean) {
      localBidiFormatter = DEFAULT_RTL_INSTANCE;
    } else {
      localBidiFormatter = DEFAULT_LTR_INSTANCE;
    }
    return localBidiFormatter;
  }
  
  private static int getEntryDir(CharSequence paramCharSequence)
  {
    return new DirectionalityEstimator(paramCharSequence, false).getEntryDir();
  }
  
  private static int getExitDir(CharSequence paramCharSequence)
  {
    return new DirectionalityEstimator(paramCharSequence, false).getExitDir();
  }
  
  public static BidiFormatter getInstance()
  {
    return getDefaultInstanceFromContext(isRtlLocale(Locale.getDefault()));
  }
  
  public static BidiFormatter getInstance(Locale paramLocale)
  {
    return getDefaultInstanceFromContext(isRtlLocale(paramLocale));
  }
  
  public static BidiFormatter getInstance(boolean paramBoolean)
  {
    return getDefaultInstanceFromContext(paramBoolean);
  }
  
  private static boolean isRtlLocale(Locale paramLocale)
  {
    int i = TextUtils.getLayoutDirectionFromLocale(paramLocale);
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    return bool;
  }
  
  public boolean getStereoReset()
  {
    boolean bool;
    if ((mFlags & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isRtl(CharSequence paramCharSequence)
  {
    return mDefaultTextDirectionHeuristic.isRtl(paramCharSequence, 0, paramCharSequence.length());
  }
  
  public boolean isRtl(String paramString)
  {
    return isRtl(paramString);
  }
  
  public boolean isRtlContext()
  {
    return mIsRtlContext;
  }
  
  public String markAfter(CharSequence paramCharSequence, TextDirectionHeuristic paramTextDirectionHeuristic)
  {
    boolean bool = paramTextDirectionHeuristic.isRtl(paramCharSequence, 0, paramCharSequence.length());
    if ((!mIsRtlContext) && ((bool) || (getExitDir(paramCharSequence) == 1))) {
      return LRM_STRING;
    }
    if ((mIsRtlContext) && ((!bool) || (getExitDir(paramCharSequence) == -1))) {
      return RLM_STRING;
    }
    return "";
  }
  
  public String markBefore(CharSequence paramCharSequence, TextDirectionHeuristic paramTextDirectionHeuristic)
  {
    boolean bool = paramTextDirectionHeuristic.isRtl(paramCharSequence, 0, paramCharSequence.length());
    if ((!mIsRtlContext) && ((bool) || (getEntryDir(paramCharSequence) == 1))) {
      return LRM_STRING;
    }
    if ((mIsRtlContext) && ((!bool) || (getEntryDir(paramCharSequence) == -1))) {
      return RLM_STRING;
    }
    return "";
  }
  
  public CharSequence unicodeWrap(CharSequence paramCharSequence)
  {
    return unicodeWrap(paramCharSequence, mDefaultTextDirectionHeuristic, true);
  }
  
  public CharSequence unicodeWrap(CharSequence paramCharSequence, TextDirectionHeuristic paramTextDirectionHeuristic)
  {
    return unicodeWrap(paramCharSequence, paramTextDirectionHeuristic, true);
  }
  
  public CharSequence unicodeWrap(CharSequence paramCharSequence, TextDirectionHeuristic paramTextDirectionHeuristic, boolean paramBoolean)
  {
    if (paramCharSequence == null) {
      return null;
    }
    boolean bool = paramTextDirectionHeuristic.isRtl(paramCharSequence, 0, paramCharSequence.length());
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder();
    if ((getStereoReset()) && (paramBoolean))
    {
      if (bool) {
        paramTextDirectionHeuristic = TextDirectionHeuristics.RTL;
      } else {
        paramTextDirectionHeuristic = TextDirectionHeuristics.LTR;
      }
      localSpannableStringBuilder.append(markBefore(paramCharSequence, paramTextDirectionHeuristic));
    }
    if (bool != mIsRtlContext)
    {
      char c1;
      char c2;
      if (bool)
      {
        c1 = '‫';
        c2 = c1;
      }
      else
      {
        c1 = '‪';
        c2 = c1;
      }
      localSpannableStringBuilder.append(c2);
      localSpannableStringBuilder.append(paramCharSequence);
      localSpannableStringBuilder.append('‬');
    }
    else
    {
      localSpannableStringBuilder.append(paramCharSequence);
    }
    if (paramBoolean)
    {
      if (bool) {
        paramTextDirectionHeuristic = TextDirectionHeuristics.RTL;
      } else {
        paramTextDirectionHeuristic = TextDirectionHeuristics.LTR;
      }
      localSpannableStringBuilder.append(markAfter(paramCharSequence, paramTextDirectionHeuristic));
    }
    return localSpannableStringBuilder;
  }
  
  public CharSequence unicodeWrap(CharSequence paramCharSequence, boolean paramBoolean)
  {
    return unicodeWrap(paramCharSequence, mDefaultTextDirectionHeuristic, paramBoolean);
  }
  
  public String unicodeWrap(String paramString)
  {
    return unicodeWrap(paramString, mDefaultTextDirectionHeuristic, true);
  }
  
  public String unicodeWrap(String paramString, TextDirectionHeuristic paramTextDirectionHeuristic)
  {
    return unicodeWrap(paramString, paramTextDirectionHeuristic, true);
  }
  
  public String unicodeWrap(String paramString, TextDirectionHeuristic paramTextDirectionHeuristic, boolean paramBoolean)
  {
    if (paramString == null) {
      return null;
    }
    return unicodeWrap(paramString, paramTextDirectionHeuristic, paramBoolean).toString();
  }
  
  public String unicodeWrap(String paramString, boolean paramBoolean)
  {
    return unicodeWrap(paramString, mDefaultTextDirectionHeuristic, paramBoolean);
  }
  
  public static final class Builder
  {
    private int mFlags;
    private boolean mIsRtlContext;
    private TextDirectionHeuristic mTextDirectionHeuristic;
    
    public Builder()
    {
      initialize(BidiFormatter.isRtlLocale(Locale.getDefault()));
    }
    
    public Builder(Locale paramLocale)
    {
      initialize(BidiFormatter.isRtlLocale(paramLocale));
    }
    
    public Builder(boolean paramBoolean)
    {
      initialize(paramBoolean);
    }
    
    private void initialize(boolean paramBoolean)
    {
      mIsRtlContext = paramBoolean;
      mTextDirectionHeuristic = BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC;
      mFlags = 2;
    }
    
    public BidiFormatter build()
    {
      if ((mFlags == 2) && (mTextDirectionHeuristic == BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC)) {
        return BidiFormatter.getDefaultInstanceFromContext(mIsRtlContext);
      }
      return new BidiFormatter(mIsRtlContext, mFlags, mTextDirectionHeuristic, null);
    }
    
    public Builder setTextDirectionHeuristic(TextDirectionHeuristic paramTextDirectionHeuristic)
    {
      mTextDirectionHeuristic = paramTextDirectionHeuristic;
      return this;
    }
    
    public Builder stereoReset(boolean paramBoolean)
    {
      if (paramBoolean) {
        mFlags |= 0x2;
      } else {
        mFlags &= 0xFFFFFFFD;
      }
      return this;
    }
  }
  
  @VisibleForTesting
  public static class DirectionalityEstimator
  {
    private static final byte[] DIR_TYPE_CACHE = new byte['܀'];
    private static final int DIR_TYPE_CACHE_SIZE = 1792;
    private int charIndex;
    private final boolean isHtml;
    private char lastChar;
    private final int length;
    private final CharSequence text;
    
    static
    {
      for (int i = 0; i < 1792; i++) {
        DIR_TYPE_CACHE[i] = Character.getDirectionality(i);
      }
    }
    
    DirectionalityEstimator(CharSequence paramCharSequence, boolean paramBoolean)
    {
      text = paramCharSequence;
      isHtml = paramBoolean;
      length = paramCharSequence.length();
    }
    
    private static byte getCachedDirectionality(char paramChar)
    {
      int i;
      if (paramChar < '܀')
      {
        paramChar = DIR_TYPE_CACHE[paramChar];
        i = paramChar;
      }
      else
      {
        paramChar = getDirectionality(paramChar);
        i = paramChar;
      }
      return i;
    }
    
    public static byte getDirectionality(int paramInt)
    {
      if (Emoji.isNewEmoji(paramInt)) {
        return 13;
      }
      return Character.getDirectionality(paramInt);
    }
    
    private byte skipEntityBackward()
    {
      int i = charIndex;
      do
      {
        if (charIndex <= 0) {
          break;
        }
        CharSequence localCharSequence = text;
        int j = charIndex - 1;
        charIndex = j;
        lastChar = localCharSequence.charAt(j);
        if (lastChar == '&') {
          return 12;
        }
      } while (lastChar != ';');
      charIndex = i;
      lastChar = ((char)59);
      return 13;
    }
    
    private byte skipEntityForward()
    {
      int i;
      do
      {
        if (charIndex >= length) {
          break;
        }
        CharSequence localCharSequence = text;
        i = charIndex;
        charIndex = (i + 1);
        i = localCharSequence.charAt(i);
        lastChar = ((char)i);
      } while (i != 59);
      return 12;
    }
    
    private byte skipTagBackward()
    {
      int i = charIndex;
      while (charIndex > 0)
      {
        CharSequence localCharSequence = text;
        int j = charIndex - 1;
        charIndex = j;
        lastChar = localCharSequence.charAt(j);
        if (lastChar == '<') {
          return 12;
        }
        if (lastChar == '>') {
          break;
        }
        if ((lastChar == '"') || (lastChar == '\''))
        {
          j = lastChar;
          int k;
          do
          {
            if (charIndex <= 0) {
              break;
            }
            localCharSequence = text;
            k = charIndex - 1;
            charIndex = k;
            k = localCharSequence.charAt(k);
            lastChar = ((char)k);
          } while (k != j);
        }
      }
      charIndex = i;
      lastChar = ((char)62);
      return 13;
    }
    
    private byte skipTagForward()
    {
      int i = charIndex;
      while (charIndex < length)
      {
        CharSequence localCharSequence = text;
        int j = charIndex;
        charIndex = (j + 1);
        lastChar = localCharSequence.charAt(j);
        if (lastChar == '>') {
          return 12;
        }
        if ((lastChar == '"') || (lastChar == '\''))
        {
          j = lastChar;
          int k;
          do
          {
            if (charIndex >= length) {
              break;
            }
            localCharSequence = text;
            k = charIndex;
            charIndex = (k + 1);
            k = localCharSequence.charAt(k);
            lastChar = ((char)k);
          } while (k != j);
        }
      }
      charIndex = i;
      lastChar = ((char)60);
      return 13;
    }
    
    byte dirTypeBackward()
    {
      lastChar = text.charAt(charIndex - 1);
      if (Character.isLowSurrogate(lastChar))
      {
        i = Character.codePointBefore(text, charIndex);
        charIndex -= Character.charCount(i);
        return getDirectionality(i);
      }
      charIndex -= 1;
      int i = getCachedDirectionality(lastChar);
      int j = i;
      if (isHtml) {
        if (lastChar == '>')
        {
          i = skipTagBackward();
          j = i;
        }
        else
        {
          j = i;
          if (lastChar == ';')
          {
            i = skipEntityBackward();
            j = i;
          }
        }
      }
      return j;
    }
    
    byte dirTypeForward()
    {
      lastChar = text.charAt(charIndex);
      if (Character.isHighSurrogate(lastChar))
      {
        i = Character.codePointAt(text, charIndex);
        charIndex += Character.charCount(i);
        return getDirectionality(i);
      }
      charIndex += 1;
      int i = getCachedDirectionality(lastChar);
      int j = i;
      if (isHtml) {
        if (lastChar == '<')
        {
          i = skipTagForward();
          j = i;
        }
        else
        {
          j = i;
          if (lastChar == '&')
          {
            i = skipEntityForward();
            j = i;
          }
        }
      }
      return j;
    }
    
    int getEntryDir()
    {
      charIndex = 0;
      int i = 0;
      int j = 0;
      int k = 0;
      while ((charIndex < length) && (k == 0))
      {
        int m = dirTypeForward();
        if (m != 9) {
          switch (m)
          {
          default: 
            switch (m)
            {
            default: 
              k = j;
              break;
            case 18: 
              j--;
              i = 0;
              break;
            case 16: 
            case 17: 
              j++;
              i = 1;
              break;
            case 14: 
            case 15: 
              j++;
              i = -1;
            }
            break;
          case 1: 
          case 2: 
            if (j == 0) {
              return 1;
            }
            k = j;
            break;
          case 0: 
            if (j == 0) {
              return -1;
            }
            k = j;
          }
        }
      }
      if (k == 0) {
        return 0;
      }
      if (i != 0) {
        return i;
      }
      while (charIndex > 0) {
        switch (dirTypeBackward())
        {
        default: 
          break;
        case 18: 
          j++;
          break;
        case 16: 
        case 17: 
          if (k == j) {
            return 1;
          }
          j--;
          break;
        case 14: 
        case 15: 
          if (k == j) {
            return -1;
          }
          j--;
        }
      }
      return 0;
    }
    
    int getExitDir()
    {
      charIndex = length;
      int i = 0;
      int j = 0;
      while (charIndex > 0)
      {
        int k = dirTypeBackward();
        if (k != 9) {
          switch (k)
          {
          default: 
            switch (k)
            {
            default: 
              if (j == 0) {
                j = i;
              }
              break;
            case 18: 
              i++;
              break;
            case 16: 
            case 17: 
              if (j == i) {
                return 1;
              }
              i--;
              break;
            case 14: 
            case 15: 
              if (j == i) {
                return -1;
              }
              i--;
            }
            break;
          case 1: 
          case 2: 
            if (i == 0) {
              return 1;
            }
            if (j == 0) {
              j = i;
            }
            break;
          case 0: 
            if (i == 0) {
              return -1;
            }
            if (j == 0) {
              j = i;
            }
            break;
          }
        }
      }
      return 0;
    }
  }
}
