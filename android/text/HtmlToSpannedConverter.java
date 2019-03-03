package android.text;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.style.AlignmentSpan.Standard;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
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
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

class HtmlToSpannedConverter
  implements ContentHandler
{
  private static final float[] HEADING_SIZES = { 1.5F, 1.4F, 1.3F, 1.2F, 1.1F, 1.0F };
  private static Pattern sBackgroundColorPattern;
  private static final Map<String, Integer> sColorMap = new HashMap();
  private static Pattern sForegroundColorPattern;
  private static Pattern sTextAlignPattern;
  private static Pattern sTextDecorationPattern;
  private int mFlags;
  private Html.ImageGetter mImageGetter;
  private XMLReader mReader;
  private String mSource;
  private SpannableStringBuilder mSpannableStringBuilder;
  private Html.TagHandler mTagHandler;
  
  static
  {
    sColorMap.put("darkgray", Integer.valueOf(-5658199));
    sColorMap.put("gray", Integer.valueOf(-8355712));
    sColorMap.put("lightgray", Integer.valueOf(-2894893));
    sColorMap.put("darkgrey", Integer.valueOf(-5658199));
    sColorMap.put("grey", Integer.valueOf(-8355712));
    sColorMap.put("lightgrey", Integer.valueOf(-2894893));
    sColorMap.put("green", Integer.valueOf(-16744448));
  }
  
  public HtmlToSpannedConverter(String paramString, Html.ImageGetter paramImageGetter, Html.TagHandler paramTagHandler, Parser paramParser, int paramInt)
  {
    mSource = paramString;
    mSpannableStringBuilder = new SpannableStringBuilder();
    mImageGetter = paramImageGetter;
    mTagHandler = paramTagHandler;
    mReader = paramParser;
    mFlags = paramInt;
  }
  
  private static void appendNewlines(Editable paramEditable, int paramInt)
  {
    int i = paramEditable.length();
    if (i == 0) {
      return;
    }
    int j = 0;
    i--;
    while ((i >= 0) && (paramEditable.charAt(i) == '\n'))
    {
      j++;
      i--;
    }
    while (j < paramInt)
    {
      paramEditable.append("\n");
      j++;
    }
  }
  
  private static void end(Editable paramEditable, Class paramClass, Object paramObject)
  {
    paramEditable.length();
    paramClass = getLast(paramEditable, paramClass);
    if (paramClass != null) {
      setSpanFromMark(paramEditable, paramClass, new Object[] { paramObject });
    }
  }
  
  private static void endA(Editable paramEditable)
  {
    Href localHref = (Href)getLast(paramEditable, Href.class);
    if ((localHref != null) && (mHref != null)) {
      setSpanFromMark(paramEditable, localHref, new Object[] { new URLSpan(mHref) });
    }
  }
  
  private static void endBlockElement(Editable paramEditable)
  {
    Object localObject = (Newline)getLast(paramEditable, Newline.class);
    if (localObject != null)
    {
      appendNewlines(paramEditable, mNumNewlines);
      paramEditable.removeSpan(localObject);
    }
    localObject = (Alignment)getLast(paramEditable, Alignment.class);
    if (localObject != null) {
      setSpanFromMark(paramEditable, localObject, new Object[] { new AlignmentSpan.Standard(mAlignment) });
    }
  }
  
  private static void endBlockquote(Editable paramEditable)
  {
    endBlockElement(paramEditable);
    end(paramEditable, Blockquote.class, new QuoteSpan());
  }
  
  private static void endCssStyle(Editable paramEditable)
  {
    Object localObject = (Strikethrough)getLast(paramEditable, Strikethrough.class);
    if (localObject != null) {
      setSpanFromMark(paramEditable, localObject, new Object[] { new StrikethroughSpan() });
    }
    localObject = (Background)getLast(paramEditable, Background.class);
    if (localObject != null) {
      setSpanFromMark(paramEditable, localObject, new Object[] { new BackgroundColorSpan(mBackgroundColor) });
    }
    localObject = (Foreground)getLast(paramEditable, Foreground.class);
    if (localObject != null) {
      setSpanFromMark(paramEditable, localObject, new Object[] { new ForegroundColorSpan(mForegroundColor) });
    }
  }
  
  private static void endFont(Editable paramEditable)
  {
    Object localObject = (Font)getLast(paramEditable, Font.class);
    if (localObject != null) {
      setSpanFromMark(paramEditable, localObject, new Object[] { new TypefaceSpan(mFace) });
    }
    localObject = (Foreground)getLast(paramEditable, Foreground.class);
    if (localObject != null) {
      setSpanFromMark(paramEditable, localObject, new Object[] { new ForegroundColorSpan(mForegroundColor) });
    }
  }
  
  private static void endHeading(Editable paramEditable)
  {
    Heading localHeading = (Heading)getLast(paramEditable, Heading.class);
    if (localHeading != null) {
      setSpanFromMark(paramEditable, localHeading, new Object[] { new RelativeSizeSpan(HEADING_SIZES[mLevel]), new StyleSpan(1) });
    }
    endBlockElement(paramEditable);
  }
  
  private static void endLi(Editable paramEditable)
  {
    endCssStyle(paramEditable);
    endBlockElement(paramEditable);
    end(paramEditable, Bullet.class, new BulletSpan());
  }
  
  private static Pattern getBackgroundColorPattern()
  {
    if (sBackgroundColorPattern == null) {
      sBackgroundColorPattern = Pattern.compile("(?:\\s+|\\A)background(?:-color)?\\s*:\\s*(\\S*)\\b");
    }
    return sBackgroundColorPattern;
  }
  
  private static Pattern getForegroundColorPattern()
  {
    if (sForegroundColorPattern == null) {
      sForegroundColorPattern = Pattern.compile("(?:\\s+|\\A)color\\s*:\\s*(\\S*)\\b");
    }
    return sForegroundColorPattern;
  }
  
  private int getHtmlColor(String paramString)
  {
    if ((mFlags & 0x100) == 256)
    {
      Integer localInteger = (Integer)sColorMap.get(paramString.toLowerCase(Locale.US));
      if (localInteger != null) {
        return localInteger.intValue();
      }
    }
    return Color.getHtmlColor(paramString);
  }
  
  private static <T> T getLast(Spanned paramSpanned, Class<T> paramClass)
  {
    paramSpanned = paramSpanned.getSpans(0, paramSpanned.length(), paramClass);
    if (paramSpanned.length == 0) {
      return null;
    }
    return paramSpanned[(paramSpanned.length - 1)];
  }
  
  private int getMargin(int paramInt)
  {
    if ((mFlags & paramInt) != 0) {
      return 1;
    }
    return 2;
  }
  
  private int getMarginBlockquote()
  {
    return getMargin(32);
  }
  
  private int getMarginDiv()
  {
    return getMargin(16);
  }
  
  private int getMarginHeading()
  {
    return getMargin(2);
  }
  
  private int getMarginList()
  {
    return getMargin(8);
  }
  
  private int getMarginListItem()
  {
    return getMargin(4);
  }
  
  private int getMarginParagraph()
  {
    return getMargin(1);
  }
  
  private static Pattern getTextAlignPattern()
  {
    if (sTextAlignPattern == null) {
      sTextAlignPattern = Pattern.compile("(?:\\s+|\\A)text-align\\s*:\\s*(\\S*)\\b");
    }
    return sTextAlignPattern;
  }
  
  private static Pattern getTextDecorationPattern()
  {
    if (sTextDecorationPattern == null) {
      sTextDecorationPattern = Pattern.compile("(?:\\s+|\\A)text-decoration\\s*:\\s*(\\S*)\\b");
    }
    return sTextDecorationPattern;
  }
  
  private static void handleBr(Editable paramEditable)
  {
    paramEditable.append('\n');
  }
  
  private void handleEndTag(String paramString)
  {
    if (paramString.equalsIgnoreCase("br"))
    {
      handleBr(mSpannableStringBuilder);
    }
    else if (paramString.equalsIgnoreCase("p"))
    {
      endCssStyle(mSpannableStringBuilder);
      endBlockElement(mSpannableStringBuilder);
    }
    else if (paramString.equalsIgnoreCase("ul"))
    {
      endBlockElement(mSpannableStringBuilder);
    }
    else if (paramString.equalsIgnoreCase("li"))
    {
      endLi(mSpannableStringBuilder);
    }
    else if (paramString.equalsIgnoreCase("div"))
    {
      endBlockElement(mSpannableStringBuilder);
    }
    else if (paramString.equalsIgnoreCase("span"))
    {
      endCssStyle(mSpannableStringBuilder);
    }
    else if (paramString.equalsIgnoreCase("strong"))
    {
      end(mSpannableStringBuilder, Bold.class, new StyleSpan(1));
    }
    else if (paramString.equalsIgnoreCase("b"))
    {
      end(mSpannableStringBuilder, Bold.class, new StyleSpan(1));
    }
    else if (paramString.equalsIgnoreCase("em"))
    {
      end(mSpannableStringBuilder, Italic.class, new StyleSpan(2));
    }
    else if (paramString.equalsIgnoreCase("cite"))
    {
      end(mSpannableStringBuilder, Italic.class, new StyleSpan(2));
    }
    else if (paramString.equalsIgnoreCase("dfn"))
    {
      end(mSpannableStringBuilder, Italic.class, new StyleSpan(2));
    }
    else if (paramString.equalsIgnoreCase("i"))
    {
      end(mSpannableStringBuilder, Italic.class, new StyleSpan(2));
    }
    else if (paramString.equalsIgnoreCase("big"))
    {
      end(mSpannableStringBuilder, Big.class, new RelativeSizeSpan(1.25F));
    }
    else if (paramString.equalsIgnoreCase("small"))
    {
      end(mSpannableStringBuilder, Small.class, new RelativeSizeSpan(0.8F));
    }
    else if (paramString.equalsIgnoreCase("font"))
    {
      endFont(mSpannableStringBuilder);
    }
    else if (paramString.equalsIgnoreCase("blockquote"))
    {
      endBlockquote(mSpannableStringBuilder);
    }
    else if (paramString.equalsIgnoreCase("tt"))
    {
      end(mSpannableStringBuilder, Monospace.class, new TypefaceSpan("monospace"));
    }
    else if (paramString.equalsIgnoreCase("a"))
    {
      endA(mSpannableStringBuilder);
    }
    else if (paramString.equalsIgnoreCase("u"))
    {
      end(mSpannableStringBuilder, Underline.class, new UnderlineSpan());
    }
    else if (paramString.equalsIgnoreCase("del"))
    {
      end(mSpannableStringBuilder, Strikethrough.class, new StrikethroughSpan());
    }
    else if (paramString.equalsIgnoreCase("s"))
    {
      end(mSpannableStringBuilder, Strikethrough.class, new StrikethroughSpan());
    }
    else if (paramString.equalsIgnoreCase("strike"))
    {
      end(mSpannableStringBuilder, Strikethrough.class, new StrikethroughSpan());
    }
    else if (paramString.equalsIgnoreCase("sup"))
    {
      end(mSpannableStringBuilder, Super.class, new SuperscriptSpan());
    }
    else if (paramString.equalsIgnoreCase("sub"))
    {
      end(mSpannableStringBuilder, Sub.class, new SubscriptSpan());
    }
    else if ((paramString.length() == 2) && (Character.toLowerCase(paramString.charAt(0)) == 'h') && (paramString.charAt(1) >= '1') && (paramString.charAt(1) <= '6'))
    {
      endHeading(mSpannableStringBuilder);
    }
    else if (mTagHandler != null)
    {
      mTagHandler.handleTag(false, paramString, mSpannableStringBuilder, mReader);
    }
  }
  
  private void handleStartTag(String paramString, Attributes paramAttributes)
  {
    if (!paramString.equalsIgnoreCase("br")) {
      if (paramString.equalsIgnoreCase("p"))
      {
        startBlockElement(mSpannableStringBuilder, paramAttributes, getMarginParagraph());
        startCssStyle(mSpannableStringBuilder, paramAttributes);
      }
      else if (paramString.equalsIgnoreCase("ul"))
      {
        startBlockElement(mSpannableStringBuilder, paramAttributes, getMarginList());
      }
      else if (paramString.equalsIgnoreCase("li"))
      {
        startLi(mSpannableStringBuilder, paramAttributes);
      }
      else if (paramString.equalsIgnoreCase("div"))
      {
        startBlockElement(mSpannableStringBuilder, paramAttributes, getMarginDiv());
      }
      else if (paramString.equalsIgnoreCase("span"))
      {
        startCssStyle(mSpannableStringBuilder, paramAttributes);
      }
      else if (paramString.equalsIgnoreCase("strong"))
      {
        start(mSpannableStringBuilder, new Bold(null));
      }
      else if (paramString.equalsIgnoreCase("b"))
      {
        start(mSpannableStringBuilder, new Bold(null));
      }
      else if (paramString.equalsIgnoreCase("em"))
      {
        start(mSpannableStringBuilder, new Italic(null));
      }
      else if (paramString.equalsIgnoreCase("cite"))
      {
        start(mSpannableStringBuilder, new Italic(null));
      }
      else if (paramString.equalsIgnoreCase("dfn"))
      {
        start(mSpannableStringBuilder, new Italic(null));
      }
      else if (paramString.equalsIgnoreCase("i"))
      {
        start(mSpannableStringBuilder, new Italic(null));
      }
      else if (paramString.equalsIgnoreCase("big"))
      {
        start(mSpannableStringBuilder, new Big(null));
      }
      else if (paramString.equalsIgnoreCase("small"))
      {
        start(mSpannableStringBuilder, new Small(null));
      }
      else if (paramString.equalsIgnoreCase("font"))
      {
        startFont(mSpannableStringBuilder, paramAttributes);
      }
      else if (paramString.equalsIgnoreCase("blockquote"))
      {
        startBlockquote(mSpannableStringBuilder, paramAttributes);
      }
      else if (paramString.equalsIgnoreCase("tt"))
      {
        start(mSpannableStringBuilder, new Monospace(null));
      }
      else if (paramString.equalsIgnoreCase("a"))
      {
        startA(mSpannableStringBuilder, paramAttributes);
      }
      else if (paramString.equalsIgnoreCase("u"))
      {
        start(mSpannableStringBuilder, new Underline(null));
      }
      else if (paramString.equalsIgnoreCase("del"))
      {
        start(mSpannableStringBuilder, new Strikethrough(null));
      }
      else if (paramString.equalsIgnoreCase("s"))
      {
        start(mSpannableStringBuilder, new Strikethrough(null));
      }
      else if (paramString.equalsIgnoreCase("strike"))
      {
        start(mSpannableStringBuilder, new Strikethrough(null));
      }
      else if (paramString.equalsIgnoreCase("sup"))
      {
        start(mSpannableStringBuilder, new Super(null));
      }
      else if (paramString.equalsIgnoreCase("sub"))
      {
        start(mSpannableStringBuilder, new Sub(null));
      }
      else if ((paramString.length() == 2) && (Character.toLowerCase(paramString.charAt(0)) == 'h') && (paramString.charAt(1) >= '1') && (paramString.charAt(1) <= '6'))
      {
        startHeading(mSpannableStringBuilder, paramAttributes, paramString.charAt(1) - '1');
      }
      else if (paramString.equalsIgnoreCase("img"))
      {
        startImg(mSpannableStringBuilder, paramAttributes, mImageGetter);
      }
      else if (mTagHandler != null)
      {
        mTagHandler.handleTag(true, paramString, mSpannableStringBuilder, mReader);
      }
    }
  }
  
  private static void setSpanFromMark(Spannable paramSpannable, Object paramObject, Object... paramVarArgs)
  {
    int i = paramSpannable.getSpanStart(paramObject);
    paramSpannable.removeSpan(paramObject);
    int j = paramSpannable.length();
    if (i != j)
    {
      int k = paramVarArgs.length;
      for (int m = 0; m < k; m++) {
        paramSpannable.setSpan(paramVarArgs[m], i, j, 33);
      }
    }
  }
  
  private static void start(Editable paramEditable, Object paramObject)
  {
    int i = paramEditable.length();
    paramEditable.setSpan(paramObject, i, i, 17);
  }
  
  private static void startA(Editable paramEditable, Attributes paramAttributes)
  {
    start(paramEditable, new Href(paramAttributes.getValue("", "href")));
  }
  
  private static void startBlockElement(Editable paramEditable, Attributes paramAttributes, int paramInt)
  {
    paramEditable.length();
    if (paramInt > 0)
    {
      appendNewlines(paramEditable, paramInt);
      start(paramEditable, new Newline(paramInt));
    }
    paramAttributes = paramAttributes.getValue("", "style");
    if (paramAttributes != null)
    {
      paramAttributes = getTextAlignPattern().matcher(paramAttributes);
      if (paramAttributes.find())
      {
        paramAttributes = paramAttributes.group(1);
        if (paramAttributes.equalsIgnoreCase("start")) {
          start(paramEditable, new Alignment(Layout.Alignment.ALIGN_NORMAL));
        } else if (paramAttributes.equalsIgnoreCase("center")) {
          start(paramEditable, new Alignment(Layout.Alignment.ALIGN_CENTER));
        } else if (paramAttributes.equalsIgnoreCase("end")) {
          start(paramEditable, new Alignment(Layout.Alignment.ALIGN_OPPOSITE));
        }
      }
    }
  }
  
  private void startBlockquote(Editable paramEditable, Attributes paramAttributes)
  {
    startBlockElement(paramEditable, paramAttributes, getMarginBlockquote());
    start(paramEditable, new Blockquote(null));
  }
  
  private void startCssStyle(Editable paramEditable, Attributes paramAttributes)
  {
    paramAttributes = paramAttributes.getValue("", "style");
    if (paramAttributes != null)
    {
      Matcher localMatcher = getForegroundColorPattern().matcher(paramAttributes);
      int i;
      if (localMatcher.find())
      {
        i = getHtmlColor(localMatcher.group(1));
        if (i != -1) {
          start(paramEditable, new Foreground(i | 0xFF000000));
        }
      }
      localMatcher = getBackgroundColorPattern().matcher(paramAttributes);
      if (localMatcher.find())
      {
        i = getHtmlColor(localMatcher.group(1));
        if (i != -1) {
          start(paramEditable, new Background(0xFF000000 | i));
        }
      }
      paramAttributes = getTextDecorationPattern().matcher(paramAttributes);
      if ((paramAttributes.find()) && (paramAttributes.group(1).equalsIgnoreCase("line-through"))) {
        start(paramEditable, new Strikethrough(null));
      }
    }
  }
  
  private void startFont(Editable paramEditable, Attributes paramAttributes)
  {
    String str = paramAttributes.getValue("", "color");
    paramAttributes = paramAttributes.getValue("", "face");
    if (!TextUtils.isEmpty(str))
    {
      int i = getHtmlColor(str);
      if (i != -1) {
        start(paramEditable, new Foreground(0xFF000000 | i));
      }
    }
    if (!TextUtils.isEmpty(paramAttributes)) {
      start(paramEditable, new Font(paramAttributes));
    }
  }
  
  private void startHeading(Editable paramEditable, Attributes paramAttributes, int paramInt)
  {
    startBlockElement(paramEditable, paramAttributes, getMarginHeading());
    start(paramEditable, new Heading(paramInt));
  }
  
  private static void startImg(Editable paramEditable, Attributes paramAttributes, Html.ImageGetter paramImageGetter)
  {
    String str = paramAttributes.getValue("", "src");
    paramAttributes = null;
    if (paramImageGetter != null) {
      paramAttributes = paramImageGetter.getDrawable(str);
    }
    paramImageGetter = paramAttributes;
    if (paramAttributes == null)
    {
      paramImageGetter = Resources.getSystem().getDrawable(17303968);
      paramImageGetter.setBounds(0, 0, paramImageGetter.getIntrinsicWidth(), paramImageGetter.getIntrinsicHeight());
    }
    int i = paramEditable.length();
    paramEditable.append("￼");
    paramEditable.setSpan(new ImageSpan(paramImageGetter, str), i, paramEditable.length(), 33);
  }
  
  private void startLi(Editable paramEditable, Attributes paramAttributes)
  {
    startBlockElement(paramEditable, paramAttributes, getMarginListItem());
    start(paramEditable, new Bullet(null));
    startCssStyle(paramEditable, paramAttributes);
  }
  
  public void characters(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws SAXException
  {
    StringBuilder localStringBuilder = new StringBuilder();
    for (int i = 0; i < paramInt2; i++)
    {
      char c = paramArrayOfChar[(i + paramInt1)];
      if ((c != ' ') && (c != '\n'))
      {
        localStringBuilder.append(c);
      }
      else
      {
        int j = localStringBuilder.length();
        if (j == 0)
        {
          j = mSpannableStringBuilder.length();
          if (j == 0) {}
          for (j = 10;; j = mSpannableStringBuilder.charAt(j - 1)) {
            break;
          }
        }
        j = localStringBuilder.charAt(j - 1);
        if ((j != 32) && (j != 10)) {
          localStringBuilder.append(' ');
        }
      }
    }
    mSpannableStringBuilder.append(localStringBuilder);
  }
  
  public Spanned convert()
  {
    mReader.setContentHandler(this);
    try
    {
      XMLReader localXMLReader = mReader;
      InputSource localInputSource = new org/xml/sax/InputSource;
      Object localObject = new java/io/StringReader;
      ((StringReader)localObject).<init>(mSource);
      localInputSource.<init>((Reader)localObject);
      localXMLReader.parse(localInputSource);
      localObject = mSpannableStringBuilder;
      int i = mSpannableStringBuilder.length();
      int j = 0;
      localObject = ((SpannableStringBuilder)localObject).getSpans(0, i, ParagraphStyle.class);
      while (j < localObject.length)
      {
        int k = mSpannableStringBuilder.getSpanStart(localObject[j]);
        int m = mSpannableStringBuilder.getSpanEnd(localObject[j]);
        i = m;
        if (m - 2 >= 0)
        {
          i = m;
          if (mSpannableStringBuilder.charAt(m - 1) == '\n')
          {
            i = m;
            if (mSpannableStringBuilder.charAt(m - 2) == '\n') {
              i = m - 1;
            }
          }
        }
        if (i == k) {
          mSpannableStringBuilder.removeSpan(localObject[j]);
        } else {
          mSpannableStringBuilder.setSpan(localObject[j], k, i, 51);
        }
        j++;
      }
      return mSpannableStringBuilder;
    }
    catch (SAXException localSAXException)
    {
      throw new RuntimeException(localSAXException);
    }
    catch (IOException localIOException)
    {
      throw new RuntimeException(localIOException);
    }
  }
  
  public void endDocument()
    throws SAXException
  {}
  
  public void endElement(String paramString1, String paramString2, String paramString3)
    throws SAXException
  {
    handleEndTag(paramString2);
  }
  
  public void endPrefixMapping(String paramString)
    throws SAXException
  {}
  
  public void ignorableWhitespace(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws SAXException
  {}
  
  public void processingInstruction(String paramString1, String paramString2)
    throws SAXException
  {}
  
  public void setDocumentLocator(Locator paramLocator) {}
  
  public void skippedEntity(String paramString)
    throws SAXException
  {}
  
  public void startDocument()
    throws SAXException
  {}
  
  public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    handleStartTag(paramString2, paramAttributes);
  }
  
  public void startPrefixMapping(String paramString1, String paramString2)
    throws SAXException
  {}
  
  private static class Alignment
  {
    private Layout.Alignment mAlignment;
    
    public Alignment(Layout.Alignment paramAlignment)
    {
      mAlignment = paramAlignment;
    }
  }
  
  private static class Background
  {
    private int mBackgroundColor;
    
    public Background(int paramInt)
    {
      mBackgroundColor = paramInt;
    }
  }
  
  private static class Big
  {
    private Big() {}
  }
  
  private static class Blockquote
  {
    private Blockquote() {}
  }
  
  private static class Bold
  {
    private Bold() {}
  }
  
  private static class Bullet
  {
    private Bullet() {}
  }
  
  private static class Font
  {
    public String mFace;
    
    public Font(String paramString)
    {
      mFace = paramString;
    }
  }
  
  private static class Foreground
  {
    private int mForegroundColor;
    
    public Foreground(int paramInt)
    {
      mForegroundColor = paramInt;
    }
  }
  
  private static class Heading
  {
    private int mLevel;
    
    public Heading(int paramInt)
    {
      mLevel = paramInt;
    }
  }
  
  private static class Href
  {
    public String mHref;
    
    public Href(String paramString)
    {
      mHref = paramString;
    }
  }
  
  private static class Italic
  {
    private Italic() {}
  }
  
  private static class Monospace
  {
    private Monospace() {}
  }
  
  private static class Newline
  {
    private int mNumNewlines;
    
    public Newline(int paramInt)
    {
      mNumNewlines = paramInt;
    }
  }
  
  private static class Small
  {
    private Small() {}
  }
  
  private static class Strikethrough
  {
    private Strikethrough() {}
  }
  
  private static class Sub
  {
    private Sub() {}
  }
  
  private static class Super
  {
    private Super() {}
  }
  
  private static class Underline
  {
    private Underline() {}
  }
}
