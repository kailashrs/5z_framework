package android.text;

import android.graphics.fonts.FontVariationAxis;
import android.net.Uri;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class FontConfig
{
  private final Alias[] mAliases;
  private final Family[] mFamilies;
  
  public FontConfig(Family[] paramArrayOfFamily, Alias[] paramArrayOfAlias)
  {
    mFamilies = paramArrayOfFamily;
    mAliases = paramArrayOfAlias;
  }
  
  public Alias[] getAliases()
  {
    return mAliases;
  }
  
  public Family[] getFamilies()
  {
    return mFamilies;
  }
  
  public static final class Alias
  {
    private final String mName;
    private final String mToName;
    private final int mWeight;
    
    public Alias(String paramString1, String paramString2, int paramInt)
    {
      mName = paramString1;
      mToName = paramString2;
      mWeight = paramInt;
    }
    
    public String getName()
    {
      return mName;
    }
    
    public String getToName()
    {
      return mToName;
    }
    
    public int getWeight()
    {
      return mWeight;
    }
  }
  
  public static final class Family
  {
    public static final int VARIANT_COMPACT = 1;
    public static final int VARIANT_DEFAULT = 0;
    public static final int VARIANT_ELEGANT = 2;
    private final FontConfig.Font[] mFonts;
    private final String[] mLanguages;
    private final String mName;
    private final int mVariant;
    
    public Family(String paramString, FontConfig.Font[] paramArrayOfFont, String[] paramArrayOfString, int paramInt)
    {
      mName = paramString;
      mFonts = paramArrayOfFont;
      mLanguages = paramArrayOfString;
      mVariant = paramInt;
    }
    
    public FontConfig.Font[] getFonts()
    {
      return mFonts;
    }
    
    public String[] getLanguages()
    {
      return mLanguages;
    }
    
    public String getName()
    {
      return mName;
    }
    
    public int getVariant()
    {
      return mVariant;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface Variant {}
  }
  
  public static final class Font
  {
    private final FontVariationAxis[] mAxes;
    private final String mFallbackFor;
    private final String mFontName;
    private final boolean mIsItalic;
    private final int mTtcIndex;
    private Uri mUri;
    private final int mWeight;
    
    public Font(String paramString1, int paramInt1, FontVariationAxis[] paramArrayOfFontVariationAxis, int paramInt2, boolean paramBoolean, String paramString2)
    {
      mFontName = paramString1;
      mTtcIndex = paramInt1;
      mAxes = paramArrayOfFontVariationAxis;
      mWeight = paramInt2;
      mIsItalic = paramBoolean;
      mFallbackFor = paramString2;
    }
    
    public FontVariationAxis[] getAxes()
    {
      return mAxes;
    }
    
    public String getFallbackFor()
    {
      return mFallbackFor;
    }
    
    public String getFontName()
    {
      return mFontName;
    }
    
    public int getTtcIndex()
    {
      return mTtcIndex;
    }
    
    public Uri getUri()
    {
      return mUri;
    }
    
    public int getWeight()
    {
      return mWeight;
    }
    
    public boolean isItalic()
    {
      return mIsItalic;
    }
    
    public void setUri(Uri paramUri)
    {
      mUri = paramUri;
    }
  }
}
