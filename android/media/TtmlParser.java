package android.media;

import android.text.TextUtils;
import android.util.Log;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

class TtmlParser
{
  private static final int DEFAULT_FRAMERATE = 30;
  private static final int DEFAULT_SUBFRAMERATE = 1;
  private static final int DEFAULT_TICKRATE = 1;
  static final String TAG = "TtmlParser";
  private long mCurrentRunId;
  private final TtmlNodeListener mListener;
  private XmlPullParser mParser;
  
  public TtmlParser(TtmlNodeListener paramTtmlNodeListener)
  {
    mListener = paramTtmlNodeListener;
  }
  
  private void extractAttribute(XmlPullParser paramXmlPullParser, int paramInt, StringBuilder paramStringBuilder)
  {
    paramStringBuilder.append(" ");
    paramStringBuilder.append(paramXmlPullParser.getAttributeName(paramInt));
    paramStringBuilder.append("=\"");
    paramStringBuilder.append(paramXmlPullParser.getAttributeValue(paramInt));
    paramStringBuilder.append("\"");
  }
  
  private boolean isEndOfDoc()
    throws XmlPullParserException
  {
    int i = mParser.getEventType();
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    return bool;
  }
  
  private static boolean isSupportedTag(String paramString)
  {
    return (paramString.equals("tt")) || (paramString.equals("head")) || (paramString.equals("body")) || (paramString.equals("div")) || (paramString.equals("p")) || (paramString.equals("span")) || (paramString.equals("br")) || (paramString.equals("style")) || (paramString.equals("styling")) || (paramString.equals("layout")) || (paramString.equals("region")) || (paramString.equals("metadata")) || (paramString.equals("smpte:image")) || (paramString.equals("smpte:data")) || (paramString.equals("smpte:information"));
  }
  
  private void loadParser(String paramString)
    throws XmlPullParserException
  {
    XmlPullParserFactory localXmlPullParserFactory = XmlPullParserFactory.newInstance();
    localXmlPullParserFactory.setNamespaceAware(false);
    mParser = localXmlPullParserFactory.newPullParser();
    paramString = new StringReader(paramString);
    mParser.setInput(paramString);
  }
  
  private TtmlNode parseNode(TtmlNode paramTtmlNode)
    throws XmlPullParserException, IOException
  {
    if (mParser.getEventType() != 2) {
      return null;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    long l1 = 0L;
    long l2 = Long.MAX_VALUE;
    int i = 0;
    long l3 = 0L;
    while (i < mParser.getAttributeCount())
    {
      String str1 = mParser.getAttributeName(i);
      String str2 = mParser.getAttributeValue(i);
      str1 = str1.replaceFirst("^.*:", "");
      if (str1.equals("begin")) {
        l1 = TtmlUtils.parseTimeExpression(str2, 30, 1, 1);
      } else if (str1.equals("end")) {
        l2 = TtmlUtils.parseTimeExpression(str2, 30, 1, 1);
      } else if (str1.equals("dur")) {
        l3 = TtmlUtils.parseTimeExpression(str2, 30, 1, 1);
      } else {
        extractAttribute(mParser, i, localStringBuilder);
      }
      i++;
    }
    long l4 = l1;
    long l5 = l2;
    if (paramTtmlNode != null)
    {
      l1 += mStartTimeMs;
      l4 = l1;
      l5 = l2;
      if (l2 != Long.MAX_VALUE)
      {
        l5 = l2 + mStartTimeMs;
        l4 = l1;
      }
    }
    l2 = l5;
    if (l3 > 0L)
    {
      if (l5 != Long.MAX_VALUE) {
        Log.e("TtmlParser", "'dur' and 'end' attributes are defined at the same time.'end' value is ignored.");
      }
      l2 = l4 + l3;
    }
    if ((paramTtmlNode != null) && (l2 == Long.MAX_VALUE) && (mEndTimeMs != Long.MAX_VALUE) && (l2 > mEndTimeMs)) {
      l2 = mEndTimeMs;
    }
    return new TtmlNode(mParser.getName(), localStringBuilder.toString(), null, l4, l2, paramTtmlNode, mCurrentRunId);
  }
  
  private void parseTtml()
    throws XmlPullParserException, IOException
  {
    LinkedList localLinkedList = new LinkedList();
    int i = 0;
    int n;
    for (int j = 1; !isEndOfDoc(); j = n)
    {
      int k = mParser.getEventType();
      Object localObject1 = (TtmlNode)localLinkedList.peekLast();
      int m;
      if (j != 0)
      {
        Object localObject2;
        if (k == 2)
        {
          if (!isSupportedTag(mParser.getName()))
          {
            localObject1 = new StringBuilder();
            ((StringBuilder)localObject1).append("Unsupported tag ");
            ((StringBuilder)localObject1).append(mParser.getName());
            ((StringBuilder)localObject1).append(" is ignored.");
            Log.w("TtmlParser", ((StringBuilder)localObject1).toString());
            m = i + 1;
            n = 0;
          }
          else
          {
            localObject2 = parseNode((TtmlNode)localObject1);
            localLinkedList.addLast(localObject2);
            if (localObject1 != null) {
              mChildren.add(localObject2);
            }
            m = i;
            n = j;
          }
        }
        else if (k == 4)
        {
          localObject2 = TtmlUtils.applyDefaultSpacePolicy(mParser.getText());
          if (!TextUtils.isEmpty((CharSequence)localObject2)) {
            mChildren.add(new TtmlNode("#pcdata", "", (String)localObject2, 0L, Long.MAX_VALUE, (TtmlNode)localObject1, mCurrentRunId));
          }
          m = i;
          n = j;
        }
        else
        {
          m = i;
          n = j;
          if (k == 3)
          {
            if (mParser.getName().equals("p")) {
              mListener.onTtmlNodeParsed((TtmlNode)localLinkedList.getLast());
            } else if (mParser.getName().equals("tt")) {
              mListener.onRootNodeParsed((TtmlNode)localLinkedList.getLast());
            }
            localLinkedList.removeLast();
            m = i;
            n = j;
          }
        }
      }
      else if (k == 2)
      {
        m = i + 1;
        n = j;
      }
      else
      {
        m = i;
        n = j;
        if (k == 3)
        {
          i--;
          m = i;
          n = j;
          if (i == 0)
          {
            n = 1;
            m = i;
          }
        }
      }
      mParser.next();
      i = m;
    }
  }
  
  public void parse(String paramString, long paramLong)
    throws XmlPullParserException, IOException
  {
    mParser = null;
    mCurrentRunId = paramLong;
    loadParser(paramString);
    parseTtml();
  }
}
