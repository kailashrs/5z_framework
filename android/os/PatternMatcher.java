package android.os;

import android.util.proto.ProtoOutputStream;
import java.util.Arrays;

public class PatternMatcher
  implements Parcelable
{
  public static final Parcelable.Creator<PatternMatcher> CREATOR = new Parcelable.Creator()
  {
    public PatternMatcher createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PatternMatcher(paramAnonymousParcel);
    }
    
    public PatternMatcher[] newArray(int paramAnonymousInt)
    {
      return new PatternMatcher[paramAnonymousInt];
    }
  };
  private static final int MAX_PATTERN_STORAGE = 2048;
  private static final int NO_MATCH = -1;
  private static final int PARSED_MODIFIER_ONE_OR_MORE = -8;
  private static final int PARSED_MODIFIER_RANGE_START = -5;
  private static final int PARSED_MODIFIER_RANGE_STOP = -6;
  private static final int PARSED_MODIFIER_ZERO_OR_MORE = -7;
  private static final int PARSED_TOKEN_CHAR_ANY = -4;
  private static final int PARSED_TOKEN_CHAR_SET_INVERSE_START = -2;
  private static final int PARSED_TOKEN_CHAR_SET_START = -1;
  private static final int PARSED_TOKEN_CHAR_SET_STOP = -3;
  public static final int PATTERN_ADVANCED_GLOB = 3;
  public static final int PATTERN_LITERAL = 0;
  public static final int PATTERN_PREFIX = 1;
  public static final int PATTERN_SIMPLE_GLOB = 2;
  private static final String TAG = "PatternMatcher";
  private static final int TOKEN_TYPE_ANY = 1;
  private static final int TOKEN_TYPE_INVERSE_SET = 3;
  private static final int TOKEN_TYPE_LITERAL = 0;
  private static final int TOKEN_TYPE_SET = 2;
  private static final int[] sParsedPatternScratch = new int['ࠀ'];
  private final int[] mParsedPattern;
  private final String mPattern;
  private final int mType;
  
  public PatternMatcher(Parcel paramParcel)
  {
    mPattern = paramParcel.readString();
    mType = paramParcel.readInt();
    mParsedPattern = paramParcel.createIntArray();
  }
  
  public PatternMatcher(String paramString, int paramInt)
  {
    mPattern = paramString;
    mType = paramInt;
    if (mType == 3) {
      mParsedPattern = parseAndVerifyAdvancedPattern(paramString);
    } else {
      mParsedPattern = null;
    }
  }
  
  private static boolean isParsedModifier(int paramInt)
  {
    boolean bool;
    if ((paramInt != -8) && (paramInt != -7) && (paramInt != -6) && (paramInt != -5)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  static boolean matchAdvancedPattern(int[] paramArrayOfInt, String paramString)
  {
    int i = paramArrayOfInt.length;
    int j = paramString.length();
    int k = 0;
    boolean bool1 = false;
    int m = 0;
    int n = 0;
    int i1 = 0;
    for (;;)
    {
      int i2 = 1;
      if (n >= i) {
        break;
      }
      int i3 = paramArrayOfInt[n];
      int i4;
      if (i3 != -4) {
        switch (i3)
        {
        default: 
          k = n;
          i4 = 0;
          i5 = n + 1;
        }
      }
      for (;;)
      {
        break;
        if (i3 == -1) {
          i4 = 2;
        } else {
          i4 = 3;
        }
        k = n + 1;
        do
        {
          n++;
        } while ((n < i) && (paramArrayOfInt[n] != -3));
        i1 = n - 1;
        i5 = n + 1;
        continue;
        i4 = 1;
        i5 = n + 1;
      }
      if (i5 >= i)
      {
        n = 1;
        int i6 = n;
        n = i5;
        i5 = i6;
      }
      for (;;)
      {
        break label275;
        i3 = paramArrayOfInt[i5];
        if (i3 != -5) {
          switch (i3)
          {
          default: 
            n = 1;
            break;
          case -7: 
            i2 = 0;
            n = Integer.MAX_VALUE;
            i5++;
            break;
          case -8: 
            i2 = 1;
            n = Integer.MAX_VALUE;
            i5++;
            break;
          }
        }
        n = i5 + 1;
        i2 = paramArrayOfInt[n];
        n++;
        i5 = paramArrayOfInt[n];
        n += 2;
      }
      label275:
      if (i2 > i5) {
        return false;
      }
      int i5 = matchChars(paramString, m, j, i4, i2, i5, paramArrayOfInt, k, i1);
      if (i5 == -1) {
        return false;
      }
      m += i5;
    }
    boolean bool2 = bool1;
    if (n >= i)
    {
      bool2 = bool1;
      if (m >= j) {
        bool2 = true;
      }
    }
    return bool2;
  }
  
  private static boolean matchChar(String paramString, int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt, int paramInt4, int paramInt5)
  {
    boolean bool = false;
    if (paramInt1 >= paramInt2) {
      return false;
    }
    switch (paramInt3)
    {
    default: 
      return false;
    case 3: 
      for (paramInt2 = paramInt4; paramInt2 < paramInt5; paramInt2 += 2)
      {
        paramInt3 = paramString.charAt(paramInt1);
        if ((paramInt3 >= paramArrayOfInt[paramInt2]) && (paramInt3 <= paramArrayOfInt[(paramInt2 + 1)])) {
          return false;
        }
      }
      return true;
    case 2: 
      for (paramInt2 = paramInt4; paramInt2 < paramInt5; paramInt2 += 2)
      {
        paramInt3 = paramString.charAt(paramInt1);
        if ((paramInt3 >= paramArrayOfInt[paramInt2]) && (paramInt3 <= paramArrayOfInt[(paramInt2 + 1)])) {
          return true;
        }
      }
      return false;
    case 1: 
      return true;
    }
    if (paramString.charAt(paramInt1) == paramArrayOfInt[paramInt4]) {
      bool = true;
    }
    return bool;
  }
  
  private static int matchChars(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfInt, int paramInt6, int paramInt7)
  {
    for (int i = 0; (i < paramInt5) && (matchChar(paramString, paramInt1 + i, paramInt2, paramInt3, paramArrayOfInt, paramInt6, paramInt7)); i++) {}
    if (i < paramInt4) {
      i = -1;
    }
    return i;
  }
  
  static boolean matchGlobPattern(String paramString1, String paramString2)
  {
    int i = paramString1.length();
    boolean bool = false;
    if (i <= 0)
    {
      if (paramString2.length() <= 0) {
        bool = true;
      }
      return bool;
    }
    int j = paramString2.length();
    int k = 0;
    int m = 0;
    int n = paramString1.charAt(0);
    label423:
    while ((k < i) && (m < j))
    {
      int i1 = n;
      int i2 = k + 1;
      if (i2 < i) {
        n = paramString1.charAt(i2);
      } else {
        n = 0;
      }
      int i3;
      if (i1 == 92) {
        i3 = 1;
      } else {
        i3 = 0;
      }
      k = i2;
      int i4 = n;
      if (i3 != 0)
      {
        i1 = n;
        k = i2 + 1;
        if (k < i) {
          n = paramString1.charAt(k);
        } else {
          n = 0;
        }
        i4 = n;
      }
      if (i4 == 42)
      {
        n = m;
        if (i3 == 0)
        {
          n = m;
          if (i1 == 46)
          {
            if (k >= i - 1) {
              return true;
            }
            i1 = k + 1;
            i3 = paramString1.charAt(i1);
            k = i1;
            n = m;
            i4 = i3;
            if (i3 == 92)
            {
              k = i1 + 1;
              if (k < i) {
                n = paramString1.charAt(k);
              } else {
                n = 0;
              }
              i4 = n;
              n = m;
            }
            while (paramString2.charAt(n) != i4)
            {
              m = n + 1;
              n = m;
              if (m >= j) {
                n = m;
              }
            }
            if (n == j) {
              return false;
            }
            k++;
            if (k < i) {
              m = paramString1.charAt(k);
            } else {
              m = 0;
            }
            i4 = m;
            m = n + 1;
            n = i4;
            break label423;
          }
        }
        do
        {
          if (paramString2.charAt(n) != i1)
          {
            m = n;
            break;
          }
          m = n + 1;
          n = m;
        } while (m < j);
        k++;
        if (k < i) {
          n = paramString1.charAt(k);
        } else {
          n = 0;
        }
      }
      else
      {
        if ((i1 != 46) && (paramString2.charAt(m) != i1)) {
          return false;
        }
        m++;
        n = i4;
      }
    }
    if ((k >= i) && (m >= j)) {
      return true;
    }
    return (k == i - 2) && (paramString1.charAt(k) == '.') && (paramString1.charAt(k + 1) == '*');
  }
  
  static boolean matchPattern(String paramString1, String paramString2, int[] paramArrayOfInt, int paramInt)
  {
    if (paramString1 == null) {
      return false;
    }
    if (paramInt == 0) {
      return paramString2.equals(paramString1);
    }
    if (paramInt == 1) {
      return paramString1.startsWith(paramString2);
    }
    if (paramInt == 2) {
      return matchGlobPattern(paramString2, paramString1);
    }
    if (paramInt == 3) {
      return matchAdvancedPattern(paramArrayOfInt, paramString1);
    }
    return false;
  }
  
  static int[] parseAndVerifyAdvancedPattern(String paramString)
  {
    try
    {
      int i = paramString.length();
      int j = 0;
      int k = 0;
      int m = 0;
      int n = 0;
      int i1 = 0;
      while (n < i) {
        if (m <= 2045)
        {
          int i2 = paramString.charAt(n);
          int i3 = 0;
          int i4 = 0;
          int i5;
          int i6;
          if (i2 != 46) {
            if (i2 != 123) {
              if (i2 != 125) {
                switch (i2)
                {
                default: 
                  switch (i2)
                  {
                  default: 
                    i4 = 1;
                    i5 = i2;
                    i6 = n;
                    label135:
                    n = m;
                    i3 = i4;
                    i2 = i5;
                    i5 = j;
                    m = i6;
                    i4 = i1;
                  }
                  break;
                }
              }
            }
          }
          label158:
          Object localObject;
          for (;;)
          {
            break label685;
            if (k == 0)
            {
              i4 = 1;
              i6 = n;
              i5 = i2;
              break label135;
            }
            i4 = sParsedPatternScratch[(m - 1)];
            if ((i4 != -1) && (i4 != -2))
            {
              localObject = sParsedPatternScratch;
              i1 = m + 1;
              localObject[m] = -3;
              i4 = 0;
              k = 0;
              m = n;
              i5 = j;
              n = i1;
            }
            else
            {
              paramString = new java/lang/IllegalArgumentException;
              paramString.<init>("You must define characters in a set.");
              throw paramString;
              if (n + 1 < i)
              {
                i6 = n + 1;
                i5 = paramString.charAt(i6);
                i4 = 1;
                break label135;
              }
              paramString = new java/lang/IllegalArgumentException;
              paramString.<init>("Escape found at end of pattern!");
              throw paramString;
              if (k != 0)
              {
                i4 = 1;
                i6 = n;
                i5 = i2;
                break label135;
              }
              if (paramString.charAt(n + 1) == '^')
              {
                sParsedPatternScratch[m] = -2;
                n++;
                m++;
              }
              else
              {
                sParsedPatternScratch[m] = -1;
                m++;
              }
              n++;
              k = 1;
              break;
              i6 = n;
              i5 = i2;
              if (k != 0) {
                break label135;
              }
              if ((m != 0) && (!isParsedModifier(sParsedPatternScratch[(m - 1)])))
              {
                localObject = sParsedPatternScratch;
                i6 = m + 1;
                localObject[m] = -8;
                i4 = i1;
                m = n;
                i5 = j;
                n = i6;
              }
              else
              {
                paramString = new java/lang/IllegalArgumentException;
                paramString.<init>("Modifier must follow a token.");
                throw paramString;
                i6 = n;
                i5 = i2;
                if (k != 0) {
                  break label135;
                }
                if ((m != 0) && (!isParsedModifier(sParsedPatternScratch[(m - 1)])))
                {
                  localObject = sParsedPatternScratch;
                  i6 = m + 1;
                  localObject[m] = -7;
                  i4 = i1;
                  m = n;
                  i5 = j;
                  n = i6;
                }
                else
                {
                  paramString = new java/lang/IllegalArgumentException;
                  paramString.<init>("Modifier must follow a token.");
                  throw paramString;
                  i6 = n;
                  i5 = i2;
                  if (j == 0) {
                    break label135;
                  }
                  localObject = sParsedPatternScratch;
                  i6 = m + 1;
                  localObject[m] = -6;
                  for (i4 = 0;; i4 = 1)
                  {
                    i5 = i4;
                    i4 = i1;
                    m = n;
                    n = i6;
                    break label158;
                    i6 = n;
                    i5 = i2;
                    if (k != 0) {
                      break;
                    }
                    if ((m == 0) || (isParsedModifier(sParsedPatternScratch[(m - 1)]))) {
                      break label625;
                    }
                    localObject = sParsedPatternScratch;
                    i6 = m + 1;
                    localObject[m] = -5;
                    n++;
                  }
                  label625:
                  paramString = new java/lang/IllegalArgumentException;
                  paramString.<init>("Modifier must follow a token.");
                  throw paramString;
                  i6 = n;
                  i5 = i2;
                  if (k != 0) {
                    break label135;
                  }
                  localObject = sParsedPatternScratch;
                  i6 = m + 1;
                  localObject[m] = -4;
                  i4 = i1;
                  m = n;
                  i5 = j;
                  n = i6;
                }
              }
            }
          }
          label685:
          if (k != 0)
          {
            if (i4 != 0)
            {
              localObject = sParsedPatternScratch;
              i4 = n + 1;
              localObject[n] = i2;
              n = 0;
            }
            else if ((m + 2 < i) && (paramString.charAt(m + 1) == '-') && (paramString.charAt(m + 2) != ']'))
            {
              i1 = 1;
              localObject = sParsedPatternScratch;
              i4 = n + 1;
              localObject[n] = i2;
              m++;
              n = i1;
            }
            else
            {
              localObject = sParsedPatternScratch;
              i1 = n + 1;
              localObject[n] = i2;
              sParsedPatternScratch[i1] = i2;
              n = i4;
              i4 = i1 + 1;
            }
          }
          else
          {
            if (i5 != 0)
            {
              i6 = paramString.indexOf('}', m);
              if (i6 >= 0)
              {
                localObject = paramString.substring(m, i6);
                m = ((String)localObject).indexOf(',');
                if (m < 0)
                {
                  try
                  {
                    i1 = Integer.parseInt((String)localObject);
                    m = i1;
                  }
                  catch (NumberFormatException paramString)
                  {
                    break label1000;
                  }
                }
                else
                {
                  i1 = Integer.parseInt(((String)localObject).substring(0, m));
                  if (m == ((String)localObject).length() - 1) {
                    m = Integer.MAX_VALUE;
                  } else {
                    m = Integer.parseInt(((String)localObject).substring(m + 1));
                  }
                }
                if (i1 <= m)
                {
                  localObject = sParsedPatternScratch;
                  j = n + 1;
                  localObject[n] = i1;
                  try
                  {
                    localObject = sParsedPatternScratch;
                    localObject[j] = m;
                    m = j + 1;
                    n = i6;
                    i1 = i4;
                    j = i5;
                  }
                  catch (NumberFormatException paramString)
                  {
                    break label1000;
                  }
                }
                paramString = new java/lang/IllegalArgumentException;
                paramString.<init>("Range quantifier minimum is greater than maximum");
                throw paramString;
                label1000:
                localObject = new java/lang/IllegalArgumentException;
                ((IllegalArgumentException)localObject).<init>("Range number format incorrect", paramString);
                throw ((Throwable)localObject);
              }
              paramString = new java/lang/IllegalArgumentException;
              paramString.<init>("Range not ended with '}'");
              throw paramString;
            }
            if (i3 != 0)
            {
              localObject = sParsedPatternScratch;
              i1 = n + 1;
              localObject[n] = i2;
              n = i4;
              i4 = i1;
            }
            else
            {
              i1 = i4;
              i4 = n;
              n = i1;
            }
          }
          m++;
          i1 = n;
          n = m;
          m = i4;
          j = i5;
        }
        else
        {
          paramString = new java/lang/IllegalArgumentException;
          paramString.<init>("Pattern is too large!");
          throw paramString;
        }
      }
      if (k == 0)
      {
        paramString = Arrays.copyOf(sParsedPatternScratch, m);
        return paramString;
      }
      paramString = new java/lang/IllegalArgumentException;
      paramString.<init>("Set was not terminated!");
      throw paramString;
    }
    finally {}
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public final String getPath()
  {
    return mPattern;
  }
  
  public final int getType()
  {
    return mType;
  }
  
  public boolean match(String paramString)
  {
    return matchPattern(paramString, mPattern, mParsedPattern, mType);
  }
  
  public String toString()
  {
    String str = "? ";
    switch (mType)
    {
    default: 
      break;
    case 3: 
      str = "ADVANCED: ";
      break;
    case 2: 
      str = "GLOB: ";
      break;
    case 1: 
      str = "PREFIX: ";
      break;
    case 0: 
      str = "LITERAL: ";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("PatternMatcher{");
    localStringBuilder.append(str);
    localStringBuilder.append(mPattern);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mPattern);
    paramParcel.writeInt(mType);
    paramParcel.writeIntArray(mParsedPattern);
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    paramProtoOutputStream.write(1138166333441L, mPattern);
    paramProtoOutputStream.write(1159641169922L, mType);
    paramProtoOutputStream.end(paramLong);
  }
}
