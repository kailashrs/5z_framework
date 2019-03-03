package android.util;

import com.android.internal.util.XmlUtils;
import org.xmlpull.v1.XmlPullParser;

class XmlPullAttributes
  implements AttributeSet
{
  XmlPullParser mParser;
  
  public XmlPullAttributes(XmlPullParser paramXmlPullParser)
  {
    mParser = paramXmlPullParser;
  }
  
  public boolean getAttributeBooleanValue(int paramInt, boolean paramBoolean)
  {
    return XmlUtils.convertValueToBoolean(getAttributeValue(paramInt), paramBoolean);
  }
  
  public boolean getAttributeBooleanValue(String paramString1, String paramString2, boolean paramBoolean)
  {
    return XmlUtils.convertValueToBoolean(getAttributeValue(paramString1, paramString2), paramBoolean);
  }
  
  public int getAttributeCount()
  {
    return mParser.getAttributeCount();
  }
  
  public float getAttributeFloatValue(int paramInt, float paramFloat)
  {
    String str = getAttributeValue(paramInt);
    if (str != null) {
      return Float.parseFloat(str);
    }
    return paramFloat;
  }
  
  public float getAttributeFloatValue(String paramString1, String paramString2, float paramFloat)
  {
    paramString1 = getAttributeValue(paramString1, paramString2);
    if (paramString1 != null) {
      return Float.parseFloat(paramString1);
    }
    return paramFloat;
  }
  
  public int getAttributeIntValue(int paramInt1, int paramInt2)
  {
    return XmlUtils.convertValueToInt(getAttributeValue(paramInt1), paramInt2);
  }
  
  public int getAttributeIntValue(String paramString1, String paramString2, int paramInt)
  {
    return XmlUtils.convertValueToInt(getAttributeValue(paramString1, paramString2), paramInt);
  }
  
  public int getAttributeListValue(int paramInt1, String[] paramArrayOfString, int paramInt2)
  {
    return XmlUtils.convertValueToList(getAttributeValue(paramInt1), paramArrayOfString, paramInt2);
  }
  
  public int getAttributeListValue(String paramString1, String paramString2, String[] paramArrayOfString, int paramInt)
  {
    return XmlUtils.convertValueToList(getAttributeValue(paramString1, paramString2), paramArrayOfString, paramInt);
  }
  
  public String getAttributeName(int paramInt)
  {
    return mParser.getAttributeName(paramInt);
  }
  
  public int getAttributeNameResource(int paramInt)
  {
    return 0;
  }
  
  public String getAttributeNamespace(int paramInt)
  {
    return mParser.getAttributeNamespace(paramInt);
  }
  
  public int getAttributeResourceValue(int paramInt1, int paramInt2)
  {
    return XmlUtils.convertValueToInt(getAttributeValue(paramInt1), paramInt2);
  }
  
  public int getAttributeResourceValue(String paramString1, String paramString2, int paramInt)
  {
    return XmlUtils.convertValueToInt(getAttributeValue(paramString1, paramString2), paramInt);
  }
  
  public int getAttributeUnsignedIntValue(int paramInt1, int paramInt2)
  {
    return XmlUtils.convertValueToUnsignedInt(getAttributeValue(paramInt1), paramInt2);
  }
  
  public int getAttributeUnsignedIntValue(String paramString1, String paramString2, int paramInt)
  {
    return XmlUtils.convertValueToUnsignedInt(getAttributeValue(paramString1, paramString2), paramInt);
  }
  
  public String getAttributeValue(int paramInt)
  {
    return mParser.getAttributeValue(paramInt);
  }
  
  public String getAttributeValue(String paramString1, String paramString2)
  {
    return mParser.getAttributeValue(paramString1, paramString2);
  }
  
  public String getClassAttribute()
  {
    return getAttributeValue(null, "class");
  }
  
  public String getIdAttribute()
  {
    return getAttributeValue(null, "id");
  }
  
  public int getIdAttributeResourceValue(int paramInt)
  {
    return getAttributeResourceValue(null, "id", paramInt);
  }
  
  public String getPositionDescription()
  {
    return mParser.getPositionDescription();
  }
  
  public int getStyleAttribute()
  {
    return getAttributeResourceValue(null, "style", 0);
  }
}
