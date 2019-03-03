package android.content.res;

import android.util.Log;
import android.util.Xml;
import com.android.internal.R.styleable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class FontResourcesParser
{
  private static final String TAG = "FontResourcesParser";
  
  public FontResourcesParser() {}
  
  public static FamilyResourceEntry parse(XmlPullParser paramXmlPullParser, Resources paramResources)
    throws XmlPullParserException, IOException
  {
    int i;
    do
    {
      i = paramXmlPullParser.next();
    } while ((i != 2) && (i != 1));
    if (i == 2) {
      return readFamilies(paramXmlPullParser, paramResources);
    }
    throw new XmlPullParserException("No start tag found");
  }
  
  private static FamilyResourceEntry readFamilies(XmlPullParser paramXmlPullParser, Resources paramResources)
    throws XmlPullParserException, IOException
  {
    paramXmlPullParser.require(2, null, "font-family");
    if (paramXmlPullParser.getName().equals("font-family")) {
      return readFamily(paramXmlPullParser, paramResources);
    }
    skip(paramXmlPullParser);
    Log.e("FontResourcesParser", "Failed to find font-family tag");
    return null;
  }
  
  private static FamilyResourceEntry readFamily(XmlPullParser paramXmlPullParser, Resources paramResources)
    throws XmlPullParserException, IOException
  {
    Object localObject1 = paramResources.obtainAttributes(Xml.asAttributeSet(paramXmlPullParser), R.styleable.FontFamily);
    Object localObject2 = ((TypedArray)localObject1).getString(0);
    String str1 = ((TypedArray)localObject1).getString(2);
    int i = 1;
    String str2 = ((TypedArray)localObject1).getString(1);
    int j = ((TypedArray)localObject1).getResourceId(3, 0);
    ((TypedArray)localObject1).recycle();
    if ((localObject2 != null) && (str1 != null) && (str2 != null))
    {
      while (paramXmlPullParser.next() != 3) {
        skip(paramXmlPullParser);
      }
      localObject1 = null;
      paramXmlPullParser = (XmlPullParser)localObject1;
      if (j != 0)
      {
        TypedArray localTypedArray = paramResources.obtainTypedArray(j);
        paramXmlPullParser = (XmlPullParser)localObject1;
        if (localTypedArray.length() > 0)
        {
          localObject1 = new ArrayList();
          if (localTypedArray.getResourceId(0, 0) == 0) {
            i = 0;
          }
          if (i != 0) {
            for (i = 0;; i++)
            {
              paramXmlPullParser = (XmlPullParser)localObject1;
              if (i >= localTypedArray.length()) {
                break;
              }
              ((List)localObject1).add(Arrays.asList(paramResources.getStringArray(localTypedArray.getResourceId(i, 0))));
            }
          }
          ((List)localObject1).add(Arrays.asList(paramResources.getStringArray(j)));
          paramXmlPullParser = (XmlPullParser)localObject1;
        }
      }
      return new ProviderResourceEntry((String)localObject2, str1, str2, paramXmlPullParser);
    }
    localObject2 = new ArrayList();
    while (paramXmlPullParser.next() != 3) {
      if (paramXmlPullParser.getEventType() == 2) {
        if (paramXmlPullParser.getName().equals("font"))
        {
          localObject1 = readFont(paramXmlPullParser, paramResources);
          if (localObject1 != null) {
            ((List)localObject2).add(localObject1);
          }
        }
        else
        {
          skip(paramXmlPullParser);
        }
      }
    }
    if (((List)localObject2).isEmpty()) {
      return null;
    }
    return new FontFamilyFilesResourceEntry((FontFileResourceEntry[])((List)localObject2).toArray(new FontFileResourceEntry[((List)localObject2).size()]));
  }
  
  private static FontFileResourceEntry readFont(XmlPullParser paramXmlPullParser, Resources paramResources)
    throws XmlPullParserException, IOException
  {
    paramResources = paramResources.obtainAttributes(Xml.asAttributeSet(paramXmlPullParser), R.styleable.FontFamilyFont);
    int i = paramResources.getInt(1, -1);
    int j = paramResources.getInt(2, -1);
    String str1 = paramResources.getString(4);
    int k = paramResources.getInt(3, 0);
    String str2 = paramResources.getString(0);
    paramResources.recycle();
    while (paramXmlPullParser.next() != 3) {
      skip(paramXmlPullParser);
    }
    if (str2 == null) {
      return null;
    }
    return new FontFileResourceEntry(str2, i, j, str1, k);
  }
  
  private static void skip(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    int i = 1;
    while (i > 0) {
      switch (paramXmlPullParser.next())
      {
      default: 
        break;
      case 3: 
        i--;
        break;
      case 2: 
        i++;
      }
    }
  }
  
  public static abstract interface FamilyResourceEntry {}
  
  public static final class FontFamilyFilesResourceEntry
    implements FontResourcesParser.FamilyResourceEntry
  {
    private final FontResourcesParser.FontFileResourceEntry[] mEntries;
    
    public FontFamilyFilesResourceEntry(FontResourcesParser.FontFileResourceEntry[] paramArrayOfFontFileResourceEntry)
    {
      mEntries = paramArrayOfFontFileResourceEntry;
    }
    
    public FontResourcesParser.FontFileResourceEntry[] getEntries()
    {
      return mEntries;
    }
  }
  
  public static final class FontFileResourceEntry
  {
    private final String mFileName;
    private int mItalic;
    private int mResourceId;
    private int mTtcIndex;
    private String mVariationSettings;
    private int mWeight;
    
    public FontFileResourceEntry(String paramString1, int paramInt1, int paramInt2, String paramString2, int paramInt3)
    {
      mFileName = paramString1;
      mWeight = paramInt1;
      mItalic = paramInt2;
      mVariationSettings = paramString2;
      mTtcIndex = paramInt3;
    }
    
    public String getFileName()
    {
      return mFileName;
    }
    
    public int getItalic()
    {
      return mItalic;
    }
    
    public int getTtcIndex()
    {
      return mTtcIndex;
    }
    
    public String getVariationSettings()
    {
      return mVariationSettings;
    }
    
    public int getWeight()
    {
      return mWeight;
    }
  }
  
  public static final class ProviderResourceEntry
    implements FontResourcesParser.FamilyResourceEntry
  {
    private final List<List<String>> mCerts;
    private final String mProviderAuthority;
    private final String mProviderPackage;
    private final String mQuery;
    
    public ProviderResourceEntry(String paramString1, String paramString2, String paramString3, List<List<String>> paramList)
    {
      mProviderAuthority = paramString1;
      mProviderPackage = paramString2;
      mQuery = paramString3;
      mCerts = paramList;
    }
    
    public String getAuthority()
    {
      return mProviderAuthority;
    }
    
    public List<List<String>> getCerts()
    {
      return mCerts;
    }
    
    public String getPackage()
    {
      return mProviderPackage;
    }
    
    public String getQuery()
    {
      return mQuery;
    }
  }
}
