package android.content.res;

import android.util.AttributeSet;
import org.xmlpull.v1.XmlPullParser;

public abstract interface XmlResourceParser
  extends XmlPullParser, AttributeSet, AutoCloseable
{
  public abstract void close();
  
  public abstract String getAttributeNamespace(int paramInt);
}
