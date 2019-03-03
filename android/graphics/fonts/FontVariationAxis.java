package android.graphics.fonts;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FontVariationAxis
{
  private static final Pattern STYLE_VALUE_PATTERN = Pattern.compile("-?(([0-9]+(\\.[0-9]+)?)|(\\.[0-9]+))");
  private static final Pattern TAG_PATTERN = Pattern.compile("[ -~]{4}");
  private final float mStyleValue;
  private final int mTag;
  private final String mTagString;
  
  public FontVariationAxis(String paramString, float paramFloat)
  {
    if (isValidTag(paramString))
    {
      mTag = makeTag(paramString);
      mTagString = paramString;
      mStyleValue = paramFloat;
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Illegal tag pattern: ");
    localStringBuilder.append(paramString);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public static FontVariationAxis[] fromFontVariationSettings(String paramString)
  {
    if ((paramString != null) && (!paramString.isEmpty()))
    {
      Object localObject = new ArrayList();
      int i = paramString.length();
      int j = 0;
      for (;;)
      {
        if (j >= i) {
          break label229;
        }
        char c = paramString.charAt(j);
        String str;
        int k;
        if (!Character.isWhitespace(c))
        {
          if (((c != '\'') && (c != '"')) || (i < j + 6) || (paramString.charAt(j + 5) != c)) {
            break;
          }
          str = paramString.substring(j + 1, j + 5);
          k = j + 6;
          int m = paramString.indexOf(',', k);
          j = m;
          if (m == -1) {
            j = i;
          }
        }
        try
        {
          float f = Float.parseFloat(paramString.substring(k, j));
          ((ArrayList)localObject).add(new FontVariationAxis(str, f));
          j++;
        }
        catch (NumberFormatException paramString)
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Failed to parse float string: ");
          ((StringBuilder)localObject).append(paramString.getMessage());
          throw new IllegalArgumentException(((StringBuilder)localObject).toString());
        }
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Tag should be wrapped with double or single quote: ");
      ((StringBuilder)localObject).append(paramString);
      throw new IllegalArgumentException(((StringBuilder)localObject).toString());
      label229:
      if (((ArrayList)localObject).isEmpty()) {
        return null;
      }
      return (FontVariationAxis[])((ArrayList)localObject).toArray(new FontVariationAxis[0]);
    }
    return null;
  }
  
  private static boolean isValidTag(String paramString)
  {
    boolean bool;
    if ((paramString != null) && (TAG_PATTERN.matcher(paramString).matches())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static boolean isValidValueFormat(String paramString)
  {
    boolean bool;
    if ((paramString != null) && (STYLE_VALUE_PATTERN.matcher(paramString).matches())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static int makeTag(String paramString)
  {
    return paramString.charAt(0) << '\030' | paramString.charAt(1) << '\020' | paramString.charAt(2) << '\b' | paramString.charAt(3);
  }
  
  public static String toFontVariationSettings(FontVariationAxis[] paramArrayOfFontVariationAxis)
  {
    if (paramArrayOfFontVariationAxis == null) {
      return "";
    }
    return TextUtils.join(",", paramArrayOfFontVariationAxis);
  }
  
  public int getOpenTypeTagValue()
  {
    return mTag;
  }
  
  public float getStyleValue()
  {
    return mStyleValue;
  }
  
  public String getTag()
  {
    return mTagString;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("'");
    localStringBuilder.append(mTagString);
    localStringBuilder.append("' ");
    localStringBuilder.append(Float.toString(mStyleValue));
    return localStringBuilder.toString();
  }
}
