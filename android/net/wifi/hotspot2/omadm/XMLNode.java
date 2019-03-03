package android.net.wifi.hotspot2.omadm;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class XMLNode
{
  private final List<XMLNode> mChildren;
  private final XMLNode mParent;
  private final String mTag;
  private String mText;
  private StringBuilder mTextBuilder;
  
  public XMLNode(XMLNode paramXMLNode, String paramString)
  {
    mTag = paramString;
    mParent = paramXMLNode;
    mChildren = new ArrayList();
    mTextBuilder = new StringBuilder();
    mText = null;
  }
  
  public void addChild(XMLNode paramXMLNode)
  {
    mChildren.add(paramXMLNode);
  }
  
  public void addText(String paramString)
  {
    mTextBuilder.append(paramString);
  }
  
  public void close()
  {
    mText = mTextBuilder.toString().trim();
    mTextBuilder = null;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof XMLNode)) {
      return false;
    }
    paramObject = (XMLNode)paramObject;
    if ((!TextUtils.equals(mTag, mTag)) || (!TextUtils.equals(mText, mText)) || (!mChildren.equals(mChildren))) {
      bool = false;
    }
    return bool;
  }
  
  public List<XMLNode> getChildren()
  {
    return mChildren;
  }
  
  public XMLNode getParent()
  {
    return mParent;
  }
  
  public String getTag()
  {
    return mTag;
  }
  
  public String getText()
  {
    return mText;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { mTag, mText, mChildren });
  }
}
