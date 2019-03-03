package android.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

public class UrlQuerySanitizer
{
  private static final ValueSanitizer sAllButNulAndAngleBracketsLegal = new IllegalCharacterValueSanitizer(1439);
  private static final ValueSanitizer sAllButNulLegal;
  private static final ValueSanitizer sAllButWhitespaceLegal;
  private static final ValueSanitizer sAllIllegal = new IllegalCharacterValueSanitizer(0);
  private static final ValueSanitizer sAmpAndSpaceLegal;
  private static final ValueSanitizer sAmpLegal;
  private static final ValueSanitizer sSpaceLegal;
  private static final ValueSanitizer sURLLegal;
  private static final ValueSanitizer sUrlAndSpaceLegal;
  private boolean mAllowUnregisteredParamaters;
  private final HashMap<String, String> mEntries = new HashMap();
  private final ArrayList<ParameterValuePair> mEntriesList = new ArrayList();
  private boolean mPreferFirstRepeatedParameter;
  private final HashMap<String, ValueSanitizer> mSanitizers = new HashMap();
  private ValueSanitizer mUnregisteredParameterValueSanitizer = getAllIllegal();
  
  static
  {
    sAllButNulLegal = new IllegalCharacterValueSanitizer(1535);
    sAllButWhitespaceLegal = new IllegalCharacterValueSanitizer(1532);
    sURLLegal = new IllegalCharacterValueSanitizer(404);
    sUrlAndSpaceLegal = new IllegalCharacterValueSanitizer(405);
    sAmpLegal = new IllegalCharacterValueSanitizer(128);
    sAmpAndSpaceLegal = new IllegalCharacterValueSanitizer(129);
    sSpaceLegal = new IllegalCharacterValueSanitizer(1);
  }
  
  public UrlQuerySanitizer() {}
  
  public UrlQuerySanitizer(String paramString)
  {
    setAllowUnregisteredParamaters(true);
    parseUrl(paramString);
  }
  
  public static final ValueSanitizer getAllButNulAndAngleBracketsLegal()
  {
    return sAllButNulAndAngleBracketsLegal;
  }
  
  public static final ValueSanitizer getAllButNulLegal()
  {
    return sAllButNulLegal;
  }
  
  public static final ValueSanitizer getAllButWhitespaceLegal()
  {
    return sAllButWhitespaceLegal;
  }
  
  public static final ValueSanitizer getAllIllegal()
  {
    return sAllIllegal;
  }
  
  public static final ValueSanitizer getAmpAndSpaceLegal()
  {
    return sAmpAndSpaceLegal;
  }
  
  public static final ValueSanitizer getAmpLegal()
  {
    return sAmpLegal;
  }
  
  public static final ValueSanitizer getSpaceLegal()
  {
    return sSpaceLegal;
  }
  
  public static final ValueSanitizer getUrlAndSpaceLegal()
  {
    return sUrlAndSpaceLegal;
  }
  
  public static final ValueSanitizer getUrlLegal()
  {
    return sURLLegal;
  }
  
  protected void addSanitizedEntry(String paramString1, String paramString2)
  {
    mEntriesList.add(new ParameterValuePair(paramString1, paramString2));
    if ((mPreferFirstRepeatedParameter) && (mEntries.containsKey(paramString1))) {
      return;
    }
    mEntries.put(paramString1, paramString2);
  }
  
  protected void clear()
  {
    mEntries.clear();
    mEntriesList.clear();
  }
  
  protected int decodeHexDigit(char paramChar)
  {
    if ((paramChar >= '0') && (paramChar <= '9')) {
      return paramChar - '0';
    }
    if ((paramChar >= 'A') && (paramChar <= 'F')) {
      return paramChar - 'A' + 10;
    }
    if ((paramChar >= 'a') && (paramChar <= 'f')) {
      return paramChar - 'a' + 10;
    }
    return -1;
  }
  
  public boolean getAllowUnregisteredParamaters()
  {
    return mAllowUnregisteredParamaters;
  }
  
  public ValueSanitizer getEffectiveValueSanitizer(String paramString)
  {
    ValueSanitizer localValueSanitizer = getValueSanitizer(paramString);
    paramString = localValueSanitizer;
    if (localValueSanitizer == null)
    {
      paramString = localValueSanitizer;
      if (mAllowUnregisteredParamaters) {
        paramString = getUnregisteredParameterValueSanitizer();
      }
    }
    return paramString;
  }
  
  public List<ParameterValuePair> getParameterList()
  {
    return mEntriesList;
  }
  
  public Set<String> getParameterSet()
  {
    return mEntries.keySet();
  }
  
  public boolean getPreferFirstRepeatedParameter()
  {
    return mPreferFirstRepeatedParameter;
  }
  
  public ValueSanitizer getUnregisteredParameterValueSanitizer()
  {
    return mUnregisteredParameterValueSanitizer;
  }
  
  public String getValue(String paramString)
  {
    return (String)mEntries.get(paramString);
  }
  
  public ValueSanitizer getValueSanitizer(String paramString)
  {
    return (ValueSanitizer)mSanitizers.get(paramString);
  }
  
  public boolean hasParameter(String paramString)
  {
    return mEntries.containsKey(paramString);
  }
  
  protected boolean isHexDigit(char paramChar)
  {
    boolean bool;
    if (decodeHexDigit(paramChar) >= 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected void parseEntry(String paramString1, String paramString2)
  {
    paramString1 = unescape(paramString1);
    ValueSanitizer localValueSanitizer = getEffectiveValueSanitizer(paramString1);
    if (localValueSanitizer == null) {
      return;
    }
    addSanitizedEntry(paramString1, localValueSanitizer.sanitize(unescape(paramString2)));
  }
  
  public void parseQuery(String paramString)
  {
    clear();
    paramString = new StringTokenizer(paramString, "&");
    while (paramString.hasMoreElements())
    {
      String str = paramString.nextToken();
      if (str.length() > 0)
      {
        int i = str.indexOf('=');
        if (i < 0) {
          parseEntry(str, "");
        } else {
          parseEntry(str.substring(0, i), str.substring(i + 1));
        }
      }
    }
  }
  
  public void parseUrl(String paramString)
  {
    int i = paramString.indexOf('?');
    if (i >= 0) {
      paramString = paramString.substring(i + 1);
    } else {
      paramString = "";
    }
    parseQuery(paramString);
  }
  
  public void registerParameter(String paramString, ValueSanitizer paramValueSanitizer)
  {
    if (paramValueSanitizer == null) {
      mSanitizers.remove(paramString);
    }
    mSanitizers.put(paramString, paramValueSanitizer);
  }
  
  public void registerParameters(String[] paramArrayOfString, ValueSanitizer paramValueSanitizer)
  {
    int i = paramArrayOfString.length;
    for (int j = 0; j < i; j++) {
      mSanitizers.put(paramArrayOfString[j], paramValueSanitizer);
    }
  }
  
  public void setAllowUnregisteredParamaters(boolean paramBoolean)
  {
    mAllowUnregisteredParamaters = paramBoolean;
  }
  
  public void setPreferFirstRepeatedParameter(boolean paramBoolean)
  {
    mPreferFirstRepeatedParameter = paramBoolean;
  }
  
  public void setUnregisteredParameterValueSanitizer(ValueSanitizer paramValueSanitizer)
  {
    mUnregisteredParameterValueSanitizer = paramValueSanitizer;
  }
  
  public String unescape(String paramString)
  {
    int i = paramString.indexOf('%');
    int j = i;
    if (i < 0)
    {
      i = paramString.indexOf('+');
      j = i;
      if (i < 0) {
        return paramString;
      }
    }
    int k = paramString.length();
    StringBuilder localStringBuilder = new StringBuilder(k);
    localStringBuilder.append(paramString.substring(0, j));
    while (j < k)
    {
      char c1 = paramString.charAt(j);
      char c2;
      if (c1 == '+')
      {
        c1 = ' ';
        i = j;
        c2 = c1;
      }
      else
      {
        i = j;
        c2 = c1;
        if (c1 == '%')
        {
          i = j;
          c2 = c1;
          if (j + 2 < k)
          {
            char c3 = paramString.charAt(j + 1);
            char c4 = paramString.charAt(j + 2);
            i = j;
            c2 = c1;
            if (isHexDigit(c3))
            {
              i = j;
              c2 = c1;
              if (isHexDigit(c4))
              {
                c1 = (char)(decodeHexDigit(c3) * 16 + decodeHexDigit(c4));
                i = j + 2;
                c2 = c1;
              }
            }
          }
        }
      }
      localStringBuilder.append(c2);
      j = i + 1;
    }
    return localStringBuilder.toString();
  }
  
  public static class IllegalCharacterValueSanitizer
    implements UrlQuerySanitizer.ValueSanitizer
  {
    public static final int ALL_BUT_NUL_AND_ANGLE_BRACKETS_LEGAL = 1439;
    public static final int ALL_BUT_NUL_LEGAL = 1535;
    public static final int ALL_BUT_WHITESPACE_LEGAL = 1532;
    public static final int ALL_ILLEGAL = 0;
    public static final int ALL_OK = 2047;
    public static final int ALL_WHITESPACE_OK = 3;
    public static final int AMP_AND_SPACE_LEGAL = 129;
    public static final int AMP_LEGAL = 128;
    public static final int AMP_OK = 128;
    public static final int DQUOTE_OK = 8;
    public static final int GT_OK = 64;
    private static final String JAVASCRIPT_PREFIX = "javascript:";
    public static final int LT_OK = 32;
    private static final int MIN_SCRIPT_PREFIX_LENGTH = Math.min("javascript:".length(), "vbscript:".length());
    public static final int NON_7_BIT_ASCII_OK = 4;
    public static final int NUL_OK = 512;
    public static final int OTHER_WHITESPACE_OK = 2;
    public static final int PCT_OK = 256;
    public static final int SCRIPT_URL_OK = 1024;
    public static final int SPACE_LEGAL = 1;
    public static final int SPACE_OK = 1;
    public static final int SQUOTE_OK = 16;
    public static final int URL_AND_SPACE_LEGAL = 405;
    public static final int URL_LEGAL = 404;
    private static final String VBSCRIPT_PREFIX = "vbscript:";
    private int mFlags;
    
    public IllegalCharacterValueSanitizer(int paramInt)
    {
      mFlags = paramInt;
    }
    
    private boolean characterIsLegal(char paramChar)
    {
      boolean bool1 = false;
      boolean bool2 = false;
      boolean bool3 = false;
      boolean bool4 = false;
      boolean bool5 = false;
      boolean bool6 = false;
      boolean bool7 = false;
      boolean bool8 = false;
      boolean bool9 = false;
      boolean bool10 = false;
      if (paramChar != 0)
      {
        if (paramChar != ' ')
        {
          if (paramChar != '"')
          {
            if (paramChar != '<')
            {
              if (paramChar != '>')
              {
                switch (paramChar)
                {
                default: 
                  switch (paramChar)
                  {
                  default: 
                    if ((paramChar < ' ') || (paramChar >= ''))
                    {
                      bool1 = bool10;
                      if (paramChar >= '')
                      {
                        bool1 = bool10;
                        if ((mFlags & 0x4) == 0) {}
                      }
                    }
                    else
                    {
                      bool1 = true;
                    }
                    return bool1;
                  case '\'': 
                    if ((mFlags & 0x10) != 0) {
                      bool1 = true;
                    }
                    return bool1;
                  case '&': 
                    bool1 = bool2;
                    if ((mFlags & 0x80) != 0) {
                      bool1 = true;
                    }
                    return bool1;
                  }
                  bool1 = bool3;
                  if ((mFlags & 0x100) != 0) {
                    bool1 = true;
                  }
                  return bool1;
                }
                bool1 = bool4;
                if ((mFlags & 0x2) != 0) {
                  bool1 = true;
                }
                return bool1;
              }
              bool1 = bool5;
              if ((mFlags & 0x40) != 0) {
                bool1 = true;
              }
              return bool1;
            }
            bool1 = bool6;
            if ((0x20 & mFlags) != 0) {
              bool1 = true;
            }
            return bool1;
          }
          bool1 = bool7;
          if ((mFlags & 0x8) != 0) {
            bool1 = true;
          }
          return bool1;
        }
        bool1 = bool8;
        if ((mFlags & 0x1) != 0) {
          bool1 = true;
        }
        return bool1;
      }
      bool1 = bool9;
      if ((mFlags & 0x200) != 0) {
        bool1 = true;
      }
      return bool1;
    }
    
    private boolean isWhitespace(char paramChar)
    {
      if (paramChar != ' ') {
        switch (paramChar)
        {
        default: 
          return false;
        }
      }
      return true;
    }
    
    private String trimWhitespace(String paramString)
    {
      int i = paramString.length() - 1;
      int j = 0;
      int k = i;
      int m;
      for (;;)
      {
        m = k;
        if (j > k) {
          break;
        }
        m = k;
        if (!isWhitespace(paramString.charAt(j))) {
          break;
        }
        j++;
      }
      while ((m >= j) && (isWhitespace(paramString.charAt(m)))) {
        m--;
      }
      if ((j == 0) && (m == i)) {
        return paramString;
      }
      return paramString.substring(j, m + 1);
    }
    
    public String sanitize(String paramString)
    {
      if (paramString == null) {
        return null;
      }
      int i = paramString.length();
      if (((mFlags & 0x400) != 0) && (i >= MIN_SCRIPT_PREFIX_LENGTH))
      {
        str = paramString.toLowerCase(Locale.ROOT);
        if ((str.startsWith("javascript:")) || (str.startsWith("vbscript:"))) {
          return "";
        }
      }
      String str = paramString;
      if ((mFlags & 0x3) == 0)
      {
        str = trimWhitespace(paramString);
        i = str.length();
      }
      paramString = new StringBuilder(i);
      for (int j = 0; j < i; j++)
      {
        char c1 = str.charAt(j);
        char c2 = c1;
        if (!characterIsLegal(c1))
        {
          char c3;
          if ((mFlags & 0x1) != 0)
          {
            c3 = ' ';
            c2 = c3;
          }
          else
          {
            c3 = '_';
            c2 = c3;
          }
        }
        paramString.append(c2);
      }
      return paramString.toString();
    }
  }
  
  public class ParameterValuePair
  {
    public String mParameter;
    public String mValue;
    
    public ParameterValuePair(String paramString1, String paramString2)
    {
      mParameter = paramString1;
      mValue = paramString2;
    }
  }
  
  public static abstract interface ValueSanitizer
  {
    public abstract String sanitize(String paramString);
  }
}
