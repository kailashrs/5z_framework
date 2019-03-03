package android.media;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class TtmlUtils
{
  public static final String ATTR_BEGIN = "begin";
  public static final String ATTR_DURATION = "dur";
  public static final String ATTR_END = "end";
  private static final Pattern CLOCK_TIME = Pattern.compile("^([0-9][0-9]+):([0-9][0-9]):([0-9][0-9])(?:(\\.[0-9]+)|:([0-9][0-9])(?:\\.([0-9]+))?)?$");
  public static final long INVALID_TIMESTAMP = Long.MAX_VALUE;
  private static final Pattern OFFSET_TIME = Pattern.compile("^([0-9]+(?:\\.[0-9]+)?)(h|m|s|ms|f|t)$");
  public static final String PCDATA = "#pcdata";
  public static final String TAG_BODY = "body";
  public static final String TAG_BR = "br";
  public static final String TAG_DIV = "div";
  public static final String TAG_HEAD = "head";
  public static final String TAG_LAYOUT = "layout";
  public static final String TAG_METADATA = "metadata";
  public static final String TAG_P = "p";
  public static final String TAG_REGION = "region";
  public static final String TAG_SMPTE_DATA = "smpte:data";
  public static final String TAG_SMPTE_IMAGE = "smpte:image";
  public static final String TAG_SMPTE_INFORMATION = "smpte:information";
  public static final String TAG_SPAN = "span";
  public static final String TAG_STYLE = "style";
  public static final String TAG_STYLING = "styling";
  public static final String TAG_TT = "tt";
  
  private TtmlUtils() {}
  
  public static String applyDefaultSpacePolicy(String paramString)
  {
    return applySpacePolicy(paramString, true);
  }
  
  public static String applySpacePolicy(String paramString, boolean paramBoolean)
  {
    paramString = paramString.replaceAll("\r\n", "\n").replaceAll(" *\n *", "\n");
    if (paramBoolean) {
      paramString = paramString.replaceAll("\n", " ");
    }
    return paramString.replaceAll("[ \t\\x0B\f\r]+", " ");
  }
  
  public static String extractText(TtmlNode paramTtmlNode, long paramLong1, long paramLong2)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    extractText(paramTtmlNode, paramLong1, paramLong2, localStringBuilder, false);
    return localStringBuilder.toString().replaceAll("\n$", "");
  }
  
  private static void extractText(TtmlNode paramTtmlNode, long paramLong1, long paramLong2, StringBuilder paramStringBuilder, boolean paramBoolean)
  {
    if ((mName.equals("#pcdata")) && (paramBoolean))
    {
      paramStringBuilder.append(mText);
    }
    else if ((mName.equals("br")) && (paramBoolean))
    {
      paramStringBuilder.append("\n");
    }
    else if ((!mName.equals("metadata")) && (paramTtmlNode.isActive(paramLong1, paramLong2)))
    {
      boolean bool1 = mName.equals("p");
      int i = paramStringBuilder.length();
      for (int j = 0; j < mChildren.size(); j++)
      {
        TtmlNode localTtmlNode = (TtmlNode)mChildren.get(j);
        boolean bool2;
        if ((!bool1) && (!paramBoolean)) {
          bool2 = false;
        } else {
          bool2 = true;
        }
        extractText(localTtmlNode, paramLong1, paramLong2, paramStringBuilder, bool2);
      }
      if ((bool1) && (i != paramStringBuilder.length())) {
        paramStringBuilder.append("\n");
      }
    }
  }
  
  public static String extractTtmlFragment(TtmlNode paramTtmlNode, long paramLong1, long paramLong2)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    extractTtmlFragment(paramTtmlNode, paramLong1, paramLong2, localStringBuilder);
    return localStringBuilder.toString();
  }
  
  private static void extractTtmlFragment(TtmlNode paramTtmlNode, long paramLong1, long paramLong2, StringBuilder paramStringBuilder)
  {
    if (mName.equals("#pcdata"))
    {
      paramStringBuilder.append(mText);
    }
    else if (mName.equals("br"))
    {
      paramStringBuilder.append("<br/>");
    }
    else if (paramTtmlNode.isActive(paramLong1, paramLong2))
    {
      paramStringBuilder.append("<");
      paramStringBuilder.append(mName);
      paramStringBuilder.append(mAttributes);
      paramStringBuilder.append(">");
      for (int i = 0; i < mChildren.size(); i++) {
        extractTtmlFragment((TtmlNode)mChildren.get(i), paramLong1, paramLong2, paramStringBuilder);
      }
      paramStringBuilder.append("</");
      paramStringBuilder.append(mName);
      paramStringBuilder.append(">");
    }
  }
  
  public static long parseTimeExpression(String paramString, int paramInt1, int paramInt2, int paramInt3)
    throws NumberFormatException
  {
    Object localObject = CLOCK_TIME.matcher(paramString);
    double d4;
    double d5;
    if (((Matcher)localObject).matches())
    {
      double d1 = Long.parseLong(((Matcher)localObject).group(1)) * 3600L;
      double d2 = Long.parseLong(((Matcher)localObject).group(2)) * 60L;
      double d3 = Long.parseLong(((Matcher)localObject).group(3));
      paramString = ((Matcher)localObject).group(4);
      if (paramString != null) {
        d4 = Double.parseDouble(paramString);
      } else {
        d4 = 0.0D;
      }
      paramString = ((Matcher)localObject).group(5);
      if (paramString != null) {
        d5 = Long.parseLong(paramString) / paramInt1;
      } else {
        d5 = 0.0D;
      }
      paramString = ((Matcher)localObject).group(6);
      double d6;
      if (paramString != null) {
        d6 = Long.parseLong(paramString) / paramInt2 / paramInt1;
      } else {
        d6 = 0.0D;
      }
      return (1000.0D * (d1 + d2 + d3 + d4 + d5 + d6));
    }
    localObject = OFFSET_TIME.matcher(paramString);
    if (((Matcher)localObject).matches())
    {
      d5 = Double.parseDouble(((Matcher)localObject).group(1));
      paramString = ((Matcher)localObject).group(2);
      if (paramString.equals("h")) {
        d4 = d5 * 3.6E9D;
      }
      for (;;)
      {
        break;
        if (paramString.equals("m"))
        {
          d4 = d5 * 6.0E7D;
        }
        else if (paramString.equals("s"))
        {
          d4 = d5 * 1000000.0D;
        }
        else if (paramString.equals("ms"))
        {
          d4 = d5 * 1000.0D;
        }
        else if (paramString.equals("f"))
        {
          d4 = d5 / paramInt1 * 1000000.0D;
        }
        else
        {
          d4 = d5;
          if (paramString.equals("t")) {
            d4 = d5 / paramInt3 * 1000000.0D;
          }
        }
      }
      return d4;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Malformed time expression : ");
    ((StringBuilder)localObject).append(paramString);
    throw new NumberFormatException(((StringBuilder)localObject).toString());
  }
}
