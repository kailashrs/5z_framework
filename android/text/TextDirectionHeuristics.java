package android.text;

import java.nio.CharBuffer;
import java.util.Locale;

public class TextDirectionHeuristics
{
  public static final TextDirectionHeuristic ANYRTL_LTR = new TextDirectionHeuristicInternal(AnyStrong.INSTANCE_RTL, false, null);
  public static final TextDirectionHeuristic FIRSTSTRONG_LTR;
  public static final TextDirectionHeuristic FIRSTSTRONG_RTL;
  public static final TextDirectionHeuristic LOCALE = TextDirectionHeuristicLocale.INSTANCE;
  public static final TextDirectionHeuristic LTR = new TextDirectionHeuristicInternal(null, false, null);
  public static final TextDirectionHeuristic RTL = new TextDirectionHeuristicInternal(null, true, null);
  private static final int STATE_FALSE = 1;
  private static final int STATE_TRUE = 0;
  private static final int STATE_UNKNOWN = 2;
  
  static
  {
    FIRSTSTRONG_LTR = new TextDirectionHeuristicInternal(FirstStrong.INSTANCE, false, null);
    FIRSTSTRONG_RTL = new TextDirectionHeuristicInternal(FirstStrong.INSTANCE, true, null);
  }
  
  public TextDirectionHeuristics() {}
  
  private static int isRtlCodePoint(int paramInt)
  {
    switch (Character.getDirectionality(paramInt))
    {
    default: 
      return 2;
    case 1: 
    case 2: 
      return 0;
    case 0: 
      return 1;
    }
    if (((1424 <= paramInt) && (paramInt <= 2303)) || ((64285 <= paramInt) && (paramInt <= 64975)) || ((65008 <= paramInt) && (paramInt <= 65023)) || ((65136 <= paramInt) && (paramInt <= 65279)) || ((67584 <= paramInt) && (paramInt <= 69631)) || ((124928 <= paramInt) && (paramInt <= 126975))) {
      return 0;
    }
    if (((8293 > paramInt) || (paramInt > 8297)) && ((65520 > paramInt) || (paramInt > 65528)) && ((917504 > paramInt) || (paramInt > 921599)) && ((64976 > paramInt) || (paramInt > 65007)) && ((paramInt & 0xFFFE) != 65534) && ((8352 > paramInt) || (paramInt > 8399)) && ((55296 > paramInt) || (paramInt > 57343))) {
      return 1;
    }
    return 2;
  }
  
  private static class AnyStrong
    implements TextDirectionHeuristics.TextDirectionAlgorithm
  {
    public static final AnyStrong INSTANCE_LTR = new AnyStrong(false);
    public static final AnyStrong INSTANCE_RTL = new AnyStrong(true);
    private final boolean mLookForRtl;
    
    private AnyStrong(boolean paramBoolean)
    {
      mLookForRtl = paramBoolean;
    }
    
    public int checkRtl(CharSequence paramCharSequence, int paramInt1, int paramInt2)
    {
      int i = 0;
      int j = 0;
      int k = paramInt1;
      while (k < paramInt1 + paramInt2)
      {
        int m = Character.codePointAt(paramCharSequence, k);
        int n;
        int i1;
        if ((8294 <= m) && (m <= 8296))
        {
          n = j + 1;
          i1 = i;
        }
        else if (m == 8297)
        {
          i1 = i;
          n = j;
          if (j > 0)
          {
            n = j - 1;
            i1 = i;
          }
        }
        else
        {
          i1 = i;
          n = j;
          if (j == 0) {
            switch (TextDirectionHeuristics.isRtlCodePoint(m))
            {
            default: 
              i1 = i;
              n = j;
              break;
            case 1: 
              if (!mLookForRtl) {
                return 1;
              }
              i1 = 1;
              n = j;
              break;
            case 0: 
              if (mLookForRtl) {
                return 0;
              }
              i1 = 1;
              n = j;
            }
          }
        }
        k += Character.charCount(m);
        i = i1;
        j = n;
      }
      if (i != 0) {
        return mLookForRtl;
      }
      return 2;
    }
  }
  
  private static class FirstStrong
    implements TextDirectionHeuristics.TextDirectionAlgorithm
  {
    public static final FirstStrong INSTANCE = new FirstStrong();
    
    private FirstStrong() {}
    
    public int checkRtl(CharSequence paramCharSequence, int paramInt1, int paramInt2)
    {
      int i = 2;
      int j = 0;
      int k = paramInt1;
      while ((k < paramInt1 + paramInt2) && (i == 2))
      {
        int m = Character.codePointAt(paramCharSequence, k);
        int n;
        int i1;
        if ((8294 <= m) && (m <= 8296))
        {
          n = j + 1;
          i1 = i;
        }
        else if (m == 8297)
        {
          i1 = i;
          n = j;
          if (j > 0)
          {
            n = j - 1;
            i1 = i;
          }
        }
        else
        {
          i1 = i;
          n = j;
          if (j == 0)
          {
            i1 = TextDirectionHeuristics.isRtlCodePoint(m);
            n = j;
          }
        }
        k += Character.charCount(m);
        i = i1;
        j = n;
      }
      return i;
    }
  }
  
  private static abstract interface TextDirectionAlgorithm
  {
    public abstract int checkRtl(CharSequence paramCharSequence, int paramInt1, int paramInt2);
  }
  
  private static abstract class TextDirectionHeuristicImpl
    implements TextDirectionHeuristic
  {
    private final TextDirectionHeuristics.TextDirectionAlgorithm mAlgorithm;
    
    public TextDirectionHeuristicImpl(TextDirectionHeuristics.TextDirectionAlgorithm paramTextDirectionAlgorithm)
    {
      mAlgorithm = paramTextDirectionAlgorithm;
    }
    
    private boolean doCheck(CharSequence paramCharSequence, int paramInt1, int paramInt2)
    {
      switch (mAlgorithm.checkRtl(paramCharSequence, paramInt1, paramInt2))
      {
      default: 
        return defaultIsRtl();
      case 1: 
        return false;
      }
      return true;
    }
    
    protected abstract boolean defaultIsRtl();
    
    public boolean isRtl(CharSequence paramCharSequence, int paramInt1, int paramInt2)
    {
      if ((paramCharSequence != null) && (paramInt1 >= 0) && (paramInt2 >= 0) && (paramCharSequence.length() - paramInt2 >= paramInt1))
      {
        if (mAlgorithm == null) {
          return defaultIsRtl();
        }
        return doCheck(paramCharSequence, paramInt1, paramInt2);
      }
      throw new IllegalArgumentException();
    }
    
    public boolean isRtl(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    {
      return isRtl(CharBuffer.wrap(paramArrayOfChar), paramInt1, paramInt2);
    }
  }
  
  private static class TextDirectionHeuristicInternal
    extends TextDirectionHeuristics.TextDirectionHeuristicImpl
  {
    private final boolean mDefaultIsRtl;
    
    private TextDirectionHeuristicInternal(TextDirectionHeuristics.TextDirectionAlgorithm paramTextDirectionAlgorithm, boolean paramBoolean)
    {
      super();
      mDefaultIsRtl = paramBoolean;
    }
    
    protected boolean defaultIsRtl()
    {
      return mDefaultIsRtl;
    }
  }
  
  private static class TextDirectionHeuristicLocale
    extends TextDirectionHeuristics.TextDirectionHeuristicImpl
  {
    public static final TextDirectionHeuristicLocale INSTANCE = new TextDirectionHeuristicLocale();
    
    public TextDirectionHeuristicLocale()
    {
      super();
    }
    
    protected boolean defaultIsRtl()
    {
      int i = TextUtils.getLayoutDirectionFromLocale(Locale.getDefault());
      boolean bool = true;
      if (i != 1) {
        bool = false;
      }
      return bool;
    }
  }
}
