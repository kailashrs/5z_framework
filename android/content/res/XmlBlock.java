package android.content.res;

import android.util.TypedValue;
import com.android.internal.util.XmlUtils;
import dalvik.annotation.optimization.FastNative;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import org.xmlpull.v1.XmlPullParserException;

final class XmlBlock
  implements AutoCloseable
{
  private static final boolean DEBUG = false;
  private final AssetManager mAssets;
  private final long mNative;
  private boolean mOpen = true;
  private int mOpenCount = 1;
  final StringBlock mStrings;
  
  XmlBlock(AssetManager paramAssetManager, long paramLong)
  {
    mAssets = paramAssetManager;
    mNative = paramLong;
    mStrings = new StringBlock(nativeGetStringBlock(paramLong), false);
  }
  
  public XmlBlock(byte[] paramArrayOfByte)
  {
    mAssets = null;
    mNative = nativeCreate(paramArrayOfByte, 0, paramArrayOfByte.length);
    mStrings = new StringBlock(nativeGetStringBlock(mNative), false);
  }
  
  public XmlBlock(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    mAssets = null;
    mNative = nativeCreate(paramArrayOfByte, paramInt1, paramInt2);
    mStrings = new StringBlock(nativeGetStringBlock(mNative), false);
  }
  
  private void decOpenCountLocked()
  {
    mOpenCount -= 1;
    if (mOpenCount == 0)
    {
      nativeDestroy(mNative);
      if (mAssets != null) {
        mAssets.xmlBlockGone(hashCode());
      }
    }
  }
  
  private static final native long nativeCreate(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  private static final native long nativeCreateParseState(long paramLong);
  
  private static final native void nativeDestroy(long paramLong);
  
  private static final native void nativeDestroyParseState(long paramLong);
  
  @FastNative
  private static final native int nativeGetAttributeCount(long paramLong);
  
  @FastNative
  private static final native int nativeGetAttributeData(long paramLong, int paramInt);
  
  @FastNative
  private static final native int nativeGetAttributeDataType(long paramLong, int paramInt);
  
  @FastNative
  private static final native int nativeGetAttributeIndex(long paramLong, String paramString1, String paramString2);
  
  @FastNative
  private static final native int nativeGetAttributeName(long paramLong, int paramInt);
  
  @FastNative
  private static final native int nativeGetAttributeNamespace(long paramLong, int paramInt);
  
  @FastNative
  private static final native int nativeGetAttributeResource(long paramLong, int paramInt);
  
  @FastNative
  private static final native int nativeGetAttributeStringValue(long paramLong, int paramInt);
  
  @FastNative
  private static final native int nativeGetClassAttribute(long paramLong);
  
  @FastNative
  private static final native int nativeGetIdAttribute(long paramLong);
  
  @FastNative
  private static final native int nativeGetLineNumber(long paramLong);
  
  @FastNative
  static final native int nativeGetName(long paramLong);
  
  @FastNative
  private static final native int nativeGetNamespace(long paramLong);
  
  private static final native long nativeGetStringBlock(long paramLong);
  
  @FastNative
  private static final native int nativeGetStyleAttribute(long paramLong);
  
  @FastNative
  private static final native int nativeGetText(long paramLong);
  
  @FastNative
  static final native int nativeNext(long paramLong);
  
  public void close()
  {
    try
    {
      if (mOpen)
      {
        mOpen = false;
        decOpenCountLocked();
      }
      return;
    }
    finally {}
  }
  
  protected void finalize()
    throws Throwable
  {
    close();
  }
  
  public XmlResourceParser newParser()
  {
    try
    {
      if (mNative != 0L)
      {
        Parser localParser = new android/content/res/XmlBlock$Parser;
        localParser.<init>(this, nativeCreateParseState(mNative), this);
        return localParser;
      }
      return null;
    }
    finally {}
  }
  
  final class Parser
    implements XmlResourceParser
  {
    private final XmlBlock mBlock;
    private boolean mDecNextDepth = false;
    private int mDepth = 0;
    private int mEventType = 0;
    long mParseState;
    private boolean mStarted = false;
    
    Parser(long paramLong, XmlBlock paramXmlBlock)
    {
      mParseState = paramLong;
      mBlock = paramXmlBlock;
      XmlBlock.access$008(paramXmlBlock);
    }
    
    public void close()
    {
      synchronized (mBlock)
      {
        if (mParseState != 0L)
        {
          XmlBlock.nativeDestroyParseState(mParseState);
          mParseState = 0L;
          mBlock.decOpenCountLocked();
        }
        return;
      }
    }
    
    public void defineEntityReplacementText(String paramString1, String paramString2)
      throws XmlPullParserException
    {
      throw new XmlPullParserException("defineEntityReplacementText() not supported");
    }
    
    protected void finalize()
      throws Throwable
    {
      close();
    }
    
    public boolean getAttributeBooleanValue(int paramInt, boolean paramBoolean)
    {
      int i = XmlBlock.nativeGetAttributeDataType(mParseState, paramInt);
      if ((i >= 16) && (i <= 31))
      {
        if (XmlBlock.nativeGetAttributeData(mParseState, paramInt) != 0) {
          paramBoolean = true;
        } else {
          paramBoolean = false;
        }
        return paramBoolean;
      }
      return paramBoolean;
    }
    
    public boolean getAttributeBooleanValue(String paramString1, String paramString2, boolean paramBoolean)
    {
      int i = XmlBlock.nativeGetAttributeIndex(mParseState, paramString1, paramString2);
      if (i >= 0) {
        return getAttributeBooleanValue(i, paramBoolean);
      }
      return paramBoolean;
    }
    
    public int getAttributeCount()
    {
      int i;
      if (mEventType == 2) {
        i = XmlBlock.nativeGetAttributeCount(mParseState);
      } else {
        i = -1;
      }
      return i;
    }
    
    public float getAttributeFloatValue(int paramInt, float paramFloat)
    {
      if (XmlBlock.nativeGetAttributeDataType(mParseState, paramInt) == 4) {
        return Float.intBitsToFloat(XmlBlock.nativeGetAttributeData(mParseState, paramInt));
      }
      throw new RuntimeException("not a float!");
    }
    
    public float getAttributeFloatValue(String paramString1, String paramString2, float paramFloat)
    {
      int i = XmlBlock.nativeGetAttributeIndex(mParseState, paramString1, paramString2);
      if (i >= 0) {
        return getAttributeFloatValue(i, paramFloat);
      }
      return paramFloat;
    }
    
    public int getAttributeIntValue(int paramInt1, int paramInt2)
    {
      int i = XmlBlock.nativeGetAttributeDataType(mParseState, paramInt1);
      if ((i >= 16) && (i <= 31)) {
        return XmlBlock.nativeGetAttributeData(mParseState, paramInt1);
      }
      return paramInt2;
    }
    
    public int getAttributeIntValue(String paramString1, String paramString2, int paramInt)
    {
      int i = XmlBlock.nativeGetAttributeIndex(mParseState, paramString1, paramString2);
      if (i >= 0) {
        return getAttributeIntValue(i, paramInt);
      }
      return paramInt;
    }
    
    public int getAttributeListValue(int paramInt1, String[] paramArrayOfString, int paramInt2)
    {
      int i = XmlBlock.nativeGetAttributeDataType(mParseState, paramInt1);
      paramInt1 = XmlBlock.nativeGetAttributeData(mParseState, paramInt1);
      if (i == 3) {
        return XmlUtils.convertValueToList(mStrings.get(paramInt1), paramArrayOfString, paramInt2);
      }
      return paramInt1;
    }
    
    public int getAttributeListValue(String paramString1, String paramString2, String[] paramArrayOfString, int paramInt)
    {
      int i = XmlBlock.nativeGetAttributeIndex(mParseState, paramString1, paramString2);
      if (i >= 0) {
        return getAttributeListValue(i, paramArrayOfString, paramInt);
      }
      return paramInt;
    }
    
    public String getAttributeName(int paramInt)
    {
      int i = XmlBlock.nativeGetAttributeName(mParseState, paramInt);
      if (i >= 0) {
        return mStrings.get(i).toString();
      }
      throw new IndexOutOfBoundsException(String.valueOf(paramInt));
    }
    
    public int getAttributeNameResource(int paramInt)
    {
      return XmlBlock.nativeGetAttributeResource(mParseState, paramInt);
    }
    
    public String getAttributeNamespace(int paramInt)
    {
      int i = XmlBlock.nativeGetAttributeNamespace(mParseState, paramInt);
      if (i >= 0) {
        return mStrings.get(i).toString();
      }
      if (i == -1) {
        return "";
      }
      throw new IndexOutOfBoundsException(String.valueOf(paramInt));
    }
    
    public String getAttributePrefix(int paramInt)
    {
      throw new RuntimeException("getAttributePrefix not supported");
    }
    
    public int getAttributeResourceValue(int paramInt1, int paramInt2)
    {
      if (XmlBlock.nativeGetAttributeDataType(mParseState, paramInt1) == 1) {
        return XmlBlock.nativeGetAttributeData(mParseState, paramInt1);
      }
      return paramInt2;
    }
    
    public int getAttributeResourceValue(String paramString1, String paramString2, int paramInt)
    {
      int i = XmlBlock.nativeGetAttributeIndex(mParseState, paramString1, paramString2);
      if (i >= 0) {
        return getAttributeResourceValue(i, paramInt);
      }
      return paramInt;
    }
    
    public String getAttributeType(int paramInt)
    {
      return "CDATA";
    }
    
    public int getAttributeUnsignedIntValue(int paramInt1, int paramInt2)
    {
      int i = XmlBlock.nativeGetAttributeDataType(mParseState, paramInt1);
      if ((i >= 16) && (i <= 31)) {
        return XmlBlock.nativeGetAttributeData(mParseState, paramInt1);
      }
      return paramInt2;
    }
    
    public int getAttributeUnsignedIntValue(String paramString1, String paramString2, int paramInt)
    {
      int i = XmlBlock.nativeGetAttributeIndex(mParseState, paramString1, paramString2);
      if (i >= 0) {
        return getAttributeUnsignedIntValue(i, paramInt);
      }
      return paramInt;
    }
    
    public String getAttributeValue(int paramInt)
    {
      int i = XmlBlock.nativeGetAttributeStringValue(mParseState, paramInt);
      if (i >= 0) {
        return mStrings.get(i).toString();
      }
      i = XmlBlock.nativeGetAttributeDataType(mParseState, paramInt);
      if (i != 0) {
        return TypedValue.coerceToString(i, XmlBlock.nativeGetAttributeData(mParseState, paramInt));
      }
      throw new IndexOutOfBoundsException(String.valueOf(paramInt));
    }
    
    public String getAttributeValue(String paramString1, String paramString2)
    {
      int i = XmlBlock.nativeGetAttributeIndex(mParseState, paramString1, paramString2);
      if (i >= 0) {
        return getAttributeValue(i);
      }
      return null;
    }
    
    public String getClassAttribute()
    {
      int i = XmlBlock.nativeGetClassAttribute(mParseState);
      String str;
      if (i >= 0) {
        str = mStrings.get(i).toString();
      } else {
        str = null;
      }
      return str;
    }
    
    public int getColumnNumber()
    {
      return -1;
    }
    
    public int getDepth()
    {
      return mDepth;
    }
    
    public int getEventType()
      throws XmlPullParserException
    {
      return mEventType;
    }
    
    public boolean getFeature(String paramString)
    {
      if ("http://xmlpull.org/v1/doc/features.html#process-namespaces".equals(paramString)) {
        return true;
      }
      return "http://xmlpull.org/v1/doc/features.html#report-namespace-prefixes".equals(paramString);
    }
    
    public String getIdAttribute()
    {
      int i = XmlBlock.nativeGetIdAttribute(mParseState);
      String str;
      if (i >= 0) {
        str = mStrings.get(i).toString();
      } else {
        str = null;
      }
      return str;
    }
    
    public int getIdAttributeResourceValue(int paramInt)
    {
      return getAttributeResourceValue(null, "id", paramInt);
    }
    
    public String getInputEncoding()
    {
      return null;
    }
    
    public int getLineNumber()
    {
      return XmlBlock.nativeGetLineNumber(mParseState);
    }
    
    public String getName()
    {
      int i = XmlBlock.nativeGetName(mParseState);
      String str;
      if (i >= 0) {
        str = mStrings.get(i).toString();
      } else {
        str = null;
      }
      return str;
    }
    
    public String getNamespace()
    {
      int i = XmlBlock.nativeGetNamespace(mParseState);
      String str;
      if (i >= 0) {
        str = mStrings.get(i).toString();
      } else {
        str = "";
      }
      return str;
    }
    
    public String getNamespace(String paramString)
    {
      throw new RuntimeException("getNamespace() not supported");
    }
    
    public int getNamespaceCount(int paramInt)
      throws XmlPullParserException
    {
      throw new XmlPullParserException("getNamespaceCount() not supported");
    }
    
    public String getNamespacePrefix(int paramInt)
      throws XmlPullParserException
    {
      throw new XmlPullParserException("getNamespacePrefix() not supported");
    }
    
    public String getNamespaceUri(int paramInt)
      throws XmlPullParserException
    {
      throw new XmlPullParserException("getNamespaceUri() not supported");
    }
    
    final CharSequence getPooledString(int paramInt)
    {
      return mStrings.get(paramInt);
    }
    
    public String getPositionDescription()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Binary XML file line #");
      localStringBuilder.append(getLineNumber());
      return localStringBuilder.toString();
    }
    
    public String getPrefix()
    {
      throw new RuntimeException("getPrefix not supported");
    }
    
    public Object getProperty(String paramString)
    {
      return null;
    }
    
    public int getStyleAttribute()
    {
      return XmlBlock.nativeGetStyleAttribute(mParseState);
    }
    
    public String getText()
    {
      int i = XmlBlock.nativeGetText(mParseState);
      String str;
      if (i >= 0) {
        str = mStrings.get(i).toString();
      } else {
        str = null;
      }
      return str;
    }
    
    public char[] getTextCharacters(int[] paramArrayOfInt)
    {
      String str = getText();
      char[] arrayOfChar = null;
      if (str != null)
      {
        paramArrayOfInt[0] = 0;
        paramArrayOfInt[1] = str.length();
        arrayOfChar = new char[str.length()];
        str.getChars(0, str.length(), arrayOfChar, 0);
      }
      return arrayOfChar;
    }
    
    public boolean isAttributeDefault(int paramInt)
    {
      return false;
    }
    
    public boolean isEmptyElementTag()
      throws XmlPullParserException
    {
      return false;
    }
    
    public boolean isWhitespace()
      throws XmlPullParserException
    {
      return false;
    }
    
    public int next()
      throws XmlPullParserException, IOException
    {
      if (!mStarted)
      {
        mStarted = true;
        return 0;
      }
      if (mParseState == 0L) {
        return 1;
      }
      int i = XmlBlock.nativeNext(mParseState);
      if (mDecNextDepth)
      {
        mDepth -= 1;
        mDecNextDepth = false;
      }
      switch (i)
      {
      default: 
        break;
      case 3: 
        mDecNextDepth = true;
        break;
      case 2: 
        mDepth += 1;
      }
      mEventType = i;
      if (i == 1) {
        close();
      }
      return i;
    }
    
    public int nextTag()
      throws XmlPullParserException, IOException
    {
      int i = next();
      int j = i;
      if (i == 4)
      {
        j = i;
        if (isWhitespace()) {
          j = next();
        }
      }
      if ((j != 2) && (j != 3))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(getPositionDescription());
        localStringBuilder.append(": expected start or end tag");
        throw new XmlPullParserException(localStringBuilder.toString(), this, null);
      }
      return j;
    }
    
    public String nextText()
      throws XmlPullParserException, IOException
    {
      if (getEventType() == 2)
      {
        int i = next();
        if (i == 4)
        {
          localObject = getText();
          if (next() == 3) {
            return localObject;
          }
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append(getPositionDescription());
          ((StringBuilder)localObject).append(": event TEXT it must be immediately followed by END_TAG");
          throw new XmlPullParserException(((StringBuilder)localObject).toString(), this, null);
        }
        if (i == 3) {
          return "";
        }
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(getPositionDescription());
        ((StringBuilder)localObject).append(": parser must be on START_TAG or TEXT to read text");
        throw new XmlPullParserException(((StringBuilder)localObject).toString(), this, null);
      }
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append(getPositionDescription());
      ((StringBuilder)localObject).append(": parser must be on START_TAG to read next text");
      throw new XmlPullParserException(((StringBuilder)localObject).toString(), this, null);
    }
    
    public int nextToken()
      throws XmlPullParserException, IOException
    {
      return next();
    }
    
    public void require(int paramInt, String paramString1, String paramString2)
      throws XmlPullParserException, IOException
    {
      if ((paramInt == getEventType()) && ((paramString1 == null) || (paramString1.equals(getNamespace()))) && ((paramString2 == null) || (paramString2.equals(getName())))) {
        return;
      }
      paramString1 = new StringBuilder();
      paramString1.append("expected ");
      paramString1.append(TYPES[paramInt]);
      paramString1.append(getPositionDescription());
      throw new XmlPullParserException(paramString1.toString());
    }
    
    public void setFeature(String paramString, boolean paramBoolean)
      throws XmlPullParserException
    {
      if (("http://xmlpull.org/v1/doc/features.html#process-namespaces".equals(paramString)) && (paramBoolean)) {
        return;
      }
      if (("http://xmlpull.org/v1/doc/features.html#report-namespace-prefixes".equals(paramString)) && (paramBoolean)) {
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unsupported feature: ");
      localStringBuilder.append(paramString);
      throw new XmlPullParserException(localStringBuilder.toString());
    }
    
    public void setInput(InputStream paramInputStream, String paramString)
      throws XmlPullParserException
    {
      throw new XmlPullParserException("setInput() not supported");
    }
    
    public void setInput(Reader paramReader)
      throws XmlPullParserException
    {
      throw new XmlPullParserException("setInput() not supported");
    }
    
    public void setProperty(String paramString, Object paramObject)
      throws XmlPullParserException
    {
      throw new XmlPullParserException("setProperty() not supported");
    }
  }
}
