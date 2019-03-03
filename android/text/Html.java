package android.text;

import android.app.ActivityThread;
import android.app.Application;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.ParagraphStyle;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import org.ccil.cowan.tagsoup.HTMLSchema;
import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

public class Html
{
  public static final int FROM_HTML_MODE_COMPACT = 63;
  public static final int FROM_HTML_MODE_LEGACY = 0;
  public static final int FROM_HTML_OPTION_USE_CSS_COLORS = 256;
  public static final int FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE = 32;
  public static final int FROM_HTML_SEPARATOR_LINE_BREAK_DIV = 16;
  public static final int FROM_HTML_SEPARATOR_LINE_BREAK_HEADING = 2;
  public static final int FROM_HTML_SEPARATOR_LINE_BREAK_LIST = 8;
  public static final int FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM = 4;
  public static final int FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH = 1;
  private static final int TO_HTML_PARAGRAPH_FLAG = 1;
  public static final int TO_HTML_PARAGRAPH_LINES_CONSECUTIVE = 0;
  public static final int TO_HTML_PARAGRAPH_LINES_INDIVIDUAL = 1;
  
  private Html() {}
  
  private static void encodeTextAlignmentByDiv(StringBuilder paramStringBuilder, Spanned paramSpanned, int paramInt)
  {
    int i = paramSpanned.length();
    int k;
    for (int j = 0; j < i; j = k)
    {
      k = paramSpanned.nextSpanTransition(j, i, ParagraphStyle.class);
      ParagraphStyle[] arrayOfParagraphStyle = (ParagraphStyle[])paramSpanned.getSpans(j, k, ParagraphStyle.class);
      int m = 0;
      Object localObject1 = " ";
      int n = 0;
      while (n < arrayOfParagraphStyle.length)
      {
        Object localObject2 = localObject1;
        if ((arrayOfParagraphStyle[n] instanceof AlignmentSpan))
        {
          localObject2 = ((AlignmentSpan)arrayOfParagraphStyle[n]).getAlignment();
          m = 1;
          if (localObject2 == Layout.Alignment.ALIGN_CENTER)
          {
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("align=\"center\" ");
            ((StringBuilder)localObject2).append((String)localObject1);
            localObject2 = ((StringBuilder)localObject2).toString();
          }
          else if (localObject2 == Layout.Alignment.ALIGN_OPPOSITE)
          {
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("align=\"right\" ");
            ((StringBuilder)localObject2).append((String)localObject1);
            localObject2 = ((StringBuilder)localObject2).toString();
          }
          else
          {
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("align=\"left\" ");
            ((StringBuilder)localObject2).append((String)localObject1);
            localObject2 = ((StringBuilder)localObject2).toString();
          }
        }
        n++;
        localObject1 = localObject2;
      }
      if (m != 0)
      {
        paramStringBuilder.append("<div ");
        paramStringBuilder.append((String)localObject1);
        paramStringBuilder.append(">");
      }
      withinDiv(paramStringBuilder, paramSpanned, j, k, paramInt);
      if (m != 0) {
        paramStringBuilder.append("</div>");
      }
    }
  }
  
  public static String escapeHtml(CharSequence paramCharSequence)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    withinStyle(localStringBuilder, paramCharSequence, 0, paramCharSequence.length());
    return localStringBuilder.toString();
  }
  
  @Deprecated
  public static Spanned fromHtml(String paramString)
  {
    return fromHtml(paramString, 0, null, null);
  }
  
  public static Spanned fromHtml(String paramString, int paramInt)
  {
    return fromHtml(paramString, paramInt, null, null);
  }
  
  public static Spanned fromHtml(String paramString, int paramInt, ImageGetter paramImageGetter, TagHandler paramTagHandler)
  {
    Parser localParser = new Parser();
    try
    {
      localParser.setProperty("http://www.ccil.org/~cowan/tagsoup/properties/schema", HtmlParser.schema);
      return new HtmlToSpannedConverter(paramString, paramImageGetter, paramTagHandler, localParser, paramInt).convert();
    }
    catch (SAXNotSupportedException paramString)
    {
      throw new RuntimeException(paramString);
    }
    catch (SAXNotRecognizedException paramString)
    {
      throw new RuntimeException(paramString);
    }
  }
  
  @Deprecated
  public static Spanned fromHtml(String paramString, ImageGetter paramImageGetter, TagHandler paramTagHandler)
  {
    return fromHtml(paramString, 0, paramImageGetter, paramTagHandler);
  }
  
  private static String getTextDirection(Spanned paramSpanned, int paramInt1, int paramInt2)
  {
    if (TextDirectionHeuristics.FIRSTSTRONG_LTR.isRtl(paramSpanned, paramInt1, paramInt2 - paramInt1)) {
      return " dir=\"rtl\"";
    }
    return " dir=\"ltr\"";
  }
  
  private static String getTextStyles(Spanned paramSpanned, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
  {
    String str = null;
    Object localObject1 = null;
    if (paramBoolean1) {
      str = "margin-top:0; margin-bottom:0;";
    }
    Object localObject2 = localObject1;
    if (paramBoolean2)
    {
      AlignmentSpan[] arrayOfAlignmentSpan = (AlignmentSpan[])paramSpanned.getSpans(paramInt1, paramInt2, AlignmentSpan.class);
      for (paramInt1 = arrayOfAlignmentSpan.length - 1;; paramInt1--)
      {
        localObject2 = localObject1;
        if (paramInt1 < 0) {
          break;
        }
        localObject2 = arrayOfAlignmentSpan[paramInt1];
        if ((paramSpanned.getSpanFlags(localObject2) & 0x33) == 51)
        {
          paramSpanned = ((AlignmentSpan)localObject2).getAlignment();
          if (paramSpanned == Layout.Alignment.ALIGN_NORMAL)
          {
            localObject2 = "text-align:start;";
            break;
          }
          if (paramSpanned == Layout.Alignment.ALIGN_CENTER)
          {
            localObject2 = "text-align:center;";
            break;
          }
          localObject2 = localObject1;
          if (paramSpanned != Layout.Alignment.ALIGN_OPPOSITE) {
            break;
          }
          localObject2 = "text-align:end;";
          break;
        }
      }
    }
    if ((str == null) && (localObject2 == null)) {
      return "";
    }
    paramSpanned = new StringBuilder(" style=\"");
    if ((str != null) && (localObject2 != null))
    {
      paramSpanned.append(str);
      paramSpanned.append(" ");
      paramSpanned.append((String)localObject2);
    }
    else if (str != null)
    {
      paramSpanned.append(str);
    }
    else if (localObject2 != null)
    {
      paramSpanned.append((String)localObject2);
    }
    paramSpanned.append("\"");
    return paramSpanned.toString();
  }
  
  @Deprecated
  public static String toHtml(Spanned paramSpanned)
  {
    return toHtml(paramSpanned, 0);
  }
  
  public static String toHtml(Spanned paramSpanned, int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    withinHtml(localStringBuilder, paramSpanned, paramInt);
    return localStringBuilder.toString();
  }
  
  private static void withinBlockquote(StringBuilder paramStringBuilder, Spanned paramSpanned, int paramInt1, int paramInt2, int paramInt3)
  {
    if ((paramInt3 & 0x1) == 0) {
      withinBlockquoteConsecutive(paramStringBuilder, paramSpanned, paramInt1, paramInt2);
    } else {
      withinBlockquoteIndividual(paramStringBuilder, paramSpanned, paramInt1, paramInt2);
    }
  }
  
  private static void withinBlockquoteConsecutive(StringBuilder paramStringBuilder, Spanned paramSpanned, int paramInt1, int paramInt2)
  {
    paramStringBuilder.append("<p");
    paramStringBuilder.append(getTextDirection(paramSpanned, paramInt1, paramInt2));
    paramStringBuilder.append(">");
    int k;
    for (int i = paramInt1; i < paramInt2; i = k)
    {
      int j = TextUtils.indexOf(paramSpanned, '\n', i, paramInt2);
      k = j;
      if (j < 0) {
        k = paramInt2;
      }
      j = 0;
      while ((k < paramInt2) && (paramSpanned.charAt(k) == '\n'))
      {
        j++;
        k++;
      }
      withinParagraph(paramStringBuilder, paramSpanned, i, k - j);
      if (j == 1)
      {
        paramStringBuilder.append("<br>\n");
      }
      else
      {
        for (i = 2; i < j; i++) {
          paramStringBuilder.append("<br>");
        }
        if (k != paramInt2)
        {
          paramStringBuilder.append("</p>\n");
          paramStringBuilder.append("<p");
          paramStringBuilder.append(getTextDirection(paramSpanned, paramInt1, paramInt2));
          paramStringBuilder.append(">");
        }
      }
    }
    paramStringBuilder.append("</p>\n");
  }
  
  private static void withinBlockquoteIndividual(StringBuilder paramStringBuilder, Spanned paramSpanned, int paramInt1, int paramInt2)
  {
    int i = 0;
    int j = paramInt1;
    paramInt1 = i;
    while (j <= paramInt2)
    {
      i = TextUtils.indexOf(paramSpanned, '\n', j, paramInt2);
      int k = i;
      if (i < 0) {
        k = paramInt2;
      }
      if (k == j)
      {
        i = paramInt1;
        if (paramInt1 != 0)
        {
          i = 0;
          paramStringBuilder.append("</ul>\n");
        }
        paramStringBuilder.append("<br>\n");
        paramInt1 = i;
      }
      else
      {
        int m = 0;
        ParagraphStyle[] arrayOfParagraphStyle = (ParagraphStyle[])paramSpanned.getSpans(j, k, ParagraphStyle.class);
        int n = arrayOfParagraphStyle.length;
        boolean bool = false;
        int i1;
        Object localObject;
        for (i = 0;; i++)
        {
          i1 = m;
          if (i >= n) {
            break;
          }
          localObject = arrayOfParagraphStyle[i];
          if (((paramSpanned.getSpanFlags(localObject) & 0x33) == 51) && ((localObject instanceof BulletSpan)))
          {
            i1 = 1;
            break;
          }
        }
        i = paramInt1;
        if (i1 != 0)
        {
          i = paramInt1;
          if (paramInt1 == 0)
          {
            i = 1;
            paramStringBuilder.append("<ul");
            paramStringBuilder.append(getTextStyles(paramSpanned, j, k, true, false));
            paramStringBuilder.append(">\n");
          }
        }
        m = i;
        if (i != 0)
        {
          m = i;
          if (i1 == 0)
          {
            m = 0;
            paramStringBuilder.append("</ul>\n");
          }
        }
        if (i1 != 0) {
          localObject = "li";
        } else {
          localObject = "p";
        }
        paramStringBuilder.append("<");
        paramStringBuilder.append((String)localObject);
        paramStringBuilder.append(getTextDirection(paramSpanned, j, k));
        if (i1 == 0) {
          bool = true;
        }
        paramStringBuilder.append(getTextStyles(paramSpanned, j, k, bool, true));
        paramStringBuilder.append(">");
        withinParagraph(paramStringBuilder, paramSpanned, j, k);
        paramStringBuilder.append("</");
        paramStringBuilder.append((String)localObject);
        paramStringBuilder.append(">\n");
        paramInt1 = m;
        if (k == paramInt2)
        {
          paramInt1 = m;
          if (m != 0)
          {
            paramInt1 = 0;
            paramStringBuilder.append("</ul>\n");
          }
        }
      }
      j = k + 1;
    }
  }
  
  private static void withinDiv(StringBuilder paramStringBuilder, Spanned paramSpanned, int paramInt1, int paramInt2, int paramInt3)
  {
    while (paramInt1 < paramInt2)
    {
      int i = paramSpanned.nextSpanTransition(paramInt1, paramInt2, QuoteSpan.class);
      QuoteSpan[] arrayOfQuoteSpan = (QuoteSpan[])paramSpanned.getSpans(paramInt1, i, QuoteSpan.class);
      int j = arrayOfQuoteSpan.length;
      int k = 0;
      QuoteSpan localQuoteSpan;
      for (int m = 0; m < j; m++)
      {
        localQuoteSpan = arrayOfQuoteSpan[m];
        paramStringBuilder.append("<blockquote>");
      }
      withinBlockquote(paramStringBuilder, paramSpanned, paramInt1, i, paramInt3);
      m = arrayOfQuoteSpan.length;
      for (paramInt1 = k; paramInt1 < m; paramInt1++)
      {
        localQuoteSpan = arrayOfQuoteSpan[paramInt1];
        paramStringBuilder.append("</blockquote>\n");
      }
      paramInt1 = i;
    }
  }
  
  private static void withinHtml(StringBuilder paramStringBuilder, Spanned paramSpanned, int paramInt)
  {
    if ((paramInt & 0x1) == 0)
    {
      encodeTextAlignmentByDiv(paramStringBuilder, paramSpanned, paramInt);
      return;
    }
    withinDiv(paramStringBuilder, paramSpanned, 0, paramSpanned.length(), paramInt);
  }
  
  private static void withinParagraph(StringBuilder paramStringBuilder, Spanned paramSpanned, int paramInt1, int paramInt2)
  {
    while (paramInt1 < paramInt2)
    {
      int i = paramSpanned.nextSpanTransition(paramInt1, paramInt2, CharacterStyle.class);
      CharacterStyle[] arrayOfCharacterStyle = (CharacterStyle[])paramSpanned.getSpans(paramInt1, i, CharacterStyle.class);
      int j = 0;
      int k = paramInt1;
      for (paramInt1 = j; paramInt1 < arrayOfCharacterStyle.length; paramInt1++)
      {
        if ((arrayOfCharacterStyle[paramInt1] instanceof StyleSpan))
        {
          j = ((StyleSpan)arrayOfCharacterStyle[paramInt1]).getStyle();
          if ((j & 0x1) != 0) {
            paramStringBuilder.append("<b>");
          }
          if ((j & 0x2) != 0) {
            paramStringBuilder.append("<i>");
          }
        }
        if (((arrayOfCharacterStyle[paramInt1] instanceof TypefaceSpan)) && ("monospace".equals(((TypefaceSpan)arrayOfCharacterStyle[paramInt1]).getFamily()))) {
          paramStringBuilder.append("<tt>");
        }
        if ((arrayOfCharacterStyle[paramInt1] instanceof SuperscriptSpan)) {
          paramStringBuilder.append("<sup>");
        }
        if ((arrayOfCharacterStyle[paramInt1] instanceof SubscriptSpan)) {
          paramStringBuilder.append("<sub>");
        }
        if ((arrayOfCharacterStyle[paramInt1] instanceof UnderlineSpan)) {
          paramStringBuilder.append("<u>");
        }
        if ((arrayOfCharacterStyle[paramInt1] instanceof StrikethroughSpan)) {
          paramStringBuilder.append("<span style=\"text-decoration:line-through;\">");
        }
        if ((arrayOfCharacterStyle[paramInt1] instanceof URLSpan))
        {
          paramStringBuilder.append("<a href=\"");
          paramStringBuilder.append(((URLSpan)arrayOfCharacterStyle[paramInt1]).getURL());
          paramStringBuilder.append("\">");
        }
        if ((arrayOfCharacterStyle[paramInt1] instanceof ImageSpan))
        {
          paramStringBuilder.append("<img src=\"");
          paramStringBuilder.append(((ImageSpan)arrayOfCharacterStyle[paramInt1]).getSource());
          paramStringBuilder.append("\">");
          k = i;
        }
        if ((arrayOfCharacterStyle[paramInt1] instanceof AbsoluteSizeSpan))
        {
          AbsoluteSizeSpan localAbsoluteSizeSpan = (AbsoluteSizeSpan)arrayOfCharacterStyle[paramInt1];
          float f1 = localAbsoluteSizeSpan.getSize();
          float f2 = f1;
          if (!localAbsoluteSizeSpan.getDip()) {
            f2 = f1 / currentApplicationgetResourcesgetDisplayMetricsdensity;
          }
          paramStringBuilder.append(String.format("<span style=\"font-size:%.0fpx\";>", new Object[] { Float.valueOf(f2) }));
        }
        if ((arrayOfCharacterStyle[paramInt1] instanceof RelativeSizeSpan)) {
          paramStringBuilder.append(String.format("<span style=\"font-size:%.2fem;\">", new Object[] { Float.valueOf(((RelativeSizeSpan)arrayOfCharacterStyle[paramInt1]).getSizeChange()) }));
        }
        if ((arrayOfCharacterStyle[paramInt1] instanceof ForegroundColorSpan)) {
          paramStringBuilder.append(String.format("<span style=\"color:#%06X;\">", new Object[] { Integer.valueOf(0xFFFFFF & ((ForegroundColorSpan)arrayOfCharacterStyle[paramInt1]).getForegroundColor()) }));
        }
        if ((arrayOfCharacterStyle[paramInt1] instanceof BackgroundColorSpan)) {
          paramStringBuilder.append(String.format("<span style=\"background-color:#%06X;\">", new Object[] { Integer.valueOf(0xFFFFFF & ((BackgroundColorSpan)arrayOfCharacterStyle[paramInt1]).getBackgroundColor()) }));
        }
      }
      withinStyle(paramStringBuilder, paramSpanned, k, i);
      for (paramInt1 = arrayOfCharacterStyle.length - 1; paramInt1 >= 0; paramInt1--)
      {
        if ((arrayOfCharacterStyle[paramInt1] instanceof BackgroundColorSpan)) {
          paramStringBuilder.append("</span>");
        }
        if ((arrayOfCharacterStyle[paramInt1] instanceof ForegroundColorSpan)) {
          paramStringBuilder.append("</span>");
        }
        if ((arrayOfCharacterStyle[paramInt1] instanceof RelativeSizeSpan)) {
          paramStringBuilder.append("</span>");
        }
        if ((arrayOfCharacterStyle[paramInt1] instanceof AbsoluteSizeSpan)) {
          paramStringBuilder.append("</span>");
        }
        if ((arrayOfCharacterStyle[paramInt1] instanceof URLSpan)) {
          paramStringBuilder.append("</a>");
        }
        if ((arrayOfCharacterStyle[paramInt1] instanceof StrikethroughSpan)) {
          paramStringBuilder.append("</span>");
        }
        if ((arrayOfCharacterStyle[paramInt1] instanceof UnderlineSpan)) {
          paramStringBuilder.append("</u>");
        }
        if ((arrayOfCharacterStyle[paramInt1] instanceof SubscriptSpan)) {
          paramStringBuilder.append("</sub>");
        }
        if ((arrayOfCharacterStyle[paramInt1] instanceof SuperscriptSpan)) {
          paramStringBuilder.append("</sup>");
        }
        if (((arrayOfCharacterStyle[paramInt1] instanceof TypefaceSpan)) && (((TypefaceSpan)arrayOfCharacterStyle[paramInt1]).getFamily().equals("monospace"))) {
          paramStringBuilder.append("</tt>");
        }
        if ((arrayOfCharacterStyle[paramInt1] instanceof StyleSpan))
        {
          k = ((StyleSpan)arrayOfCharacterStyle[paramInt1]).getStyle();
          if ((k & 0x1) != 0) {
            paramStringBuilder.append("</b>");
          }
          if ((k & 0x2) != 0) {
            paramStringBuilder.append("</i>");
          }
        }
      }
      paramInt1 = i;
    }
  }
  
  private static void withinStyle(StringBuilder paramStringBuilder, CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    while (paramInt1 < paramInt2)
    {
      int i = paramCharSequence.charAt(paramInt1);
      int j;
      if (i == 60)
      {
        paramStringBuilder.append("&lt;");
        j = paramInt1;
      }
      else if (i == 62)
      {
        paramStringBuilder.append("&gt;");
        j = paramInt1;
      }
      else if (i == 38)
      {
        paramStringBuilder.append("&amp;");
        j = paramInt1;
      }
      else if ((i >= 55296) && (i <= 57343))
      {
        j = paramInt1;
        if (i < 56320)
        {
          j = paramInt1;
          if (paramInt1 + 1 < paramInt2)
          {
            int k = paramCharSequence.charAt(paramInt1 + 1);
            j = paramInt1;
            if (k >= 56320)
            {
              j = paramInt1;
              if (k <= 57343)
              {
                j = paramInt1 + 1;
                paramStringBuilder.append("&#");
                paramStringBuilder.append(i - 55296 << 10 | 0x10000 | k - 56320);
                paramStringBuilder.append(";");
              }
            }
          }
        }
      }
      else if ((i <= 126) && (i >= 32))
      {
        if (i == 32)
        {
          while ((paramInt1 + 1 < paramInt2) && (paramCharSequence.charAt(paramInt1 + 1) == ' '))
          {
            paramStringBuilder.append("&nbsp;");
            paramInt1++;
          }
          paramStringBuilder.append(' ');
          j = paramInt1;
        }
        else
        {
          paramStringBuilder.append(i);
          j = paramInt1;
        }
      }
      else
      {
        paramStringBuilder.append("&#");
        paramStringBuilder.append(i);
        paramStringBuilder.append(";");
        j = paramInt1;
      }
      paramInt1 = j + 1;
    }
  }
  
  private static class HtmlParser
  {
    private static final HTMLSchema schema = new HTMLSchema();
    
    private HtmlParser() {}
  }
  
  public static abstract interface ImageGetter
  {
    public abstract Drawable getDrawable(String paramString);
  }
  
  public static abstract interface TagHandler
  {
    public abstract void handleTag(boolean paramBoolean, String paramString, Editable paramEditable, XMLReader paramXMLReader);
  }
}
