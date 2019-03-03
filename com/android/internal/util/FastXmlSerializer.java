package com.android.internal.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import org.xmlpull.v1.XmlSerializer;

public class FastXmlSerializer
  implements XmlSerializer
{
  private static final int DEFAULT_BUFFER_LEN = 32768;
  private static final String[] ESCAPE_TABLE = { "&#0;", "&#1;", "&#2;", "&#3;", "&#4;", "&#5;", "&#6;", "&#7;", "&#8;", "&#9;", "&#10;", "&#11;", "&#12;", "&#13;", "&#14;", "&#15;", "&#16;", "&#17;", "&#18;", "&#19;", "&#20;", "&#21;", "&#22;", "&#23;", "&#24;", "&#25;", "&#26;", "&#27;", "&#28;", "&#29;", "&#30;", "&#31;", null, null, "&quot;", null, null, null, "&amp;", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "&lt;", null, "&gt;", null };
  private static String sSpace = "                                                              ";
  private final int mBufferLen;
  private ByteBuffer mBytes;
  private CharsetEncoder mCharset;
  private boolean mInTag;
  private boolean mIndent = false;
  private boolean mLineStart = true;
  private int mNesting = 0;
  private OutputStream mOutputStream;
  private int mPos;
  private final char[] mText;
  private Writer mWriter;
  
  public FastXmlSerializer()
  {
    this(32768);
  }
  
  public FastXmlSerializer(int paramInt)
  {
    if (paramInt <= 0) {
      paramInt = 32768;
    }
    mBufferLen = paramInt;
    mText = new char[mBufferLen];
    mBytes = ByteBuffer.allocate(mBufferLen);
  }
  
  private void append(char paramChar)
    throws IOException
  {
    int i = mPos;
    int j = i;
    if (i >= mBufferLen - 1)
    {
      flush();
      j = mPos;
    }
    mText[j] = ((char)paramChar);
    mPos = (j + 1);
  }
  
  private void append(String paramString)
    throws IOException
  {
    append(paramString, 0, paramString.length());
  }
  
  private void append(String paramString, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramInt2 > mBufferLen)
    {
      i = paramInt1 + paramInt2;
      while (paramInt1 < i)
      {
        j = mBufferLen + paramInt1;
        if (j < i) {
          paramInt2 = mBufferLen;
        } else {
          paramInt2 = i - paramInt1;
        }
        append(paramString, paramInt1, paramInt2);
        paramInt1 = j;
      }
      return;
    }
    int i = mPos;
    int j = i;
    if (i + paramInt2 > mBufferLen)
    {
      flush();
      j = mPos;
    }
    paramString.getChars(paramInt1, paramInt1 + paramInt2, mText, j);
    mPos = (j + paramInt2);
  }
  
  private void append(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramInt2 > mBufferLen)
    {
      i = paramInt1 + paramInt2;
      while (paramInt1 < i)
      {
        j = mBufferLen + paramInt1;
        if (j < i) {
          paramInt2 = mBufferLen;
        } else {
          paramInt2 = i - paramInt1;
        }
        append(paramArrayOfChar, paramInt1, paramInt2);
        paramInt1 = j;
      }
      return;
    }
    int i = mPos;
    int j = i;
    if (i + paramInt2 > mBufferLen)
    {
      flush();
      j = mPos;
    }
    System.arraycopy(paramArrayOfChar, paramInt1, mText, j, paramInt2);
    mPos = (j + paramInt2);
  }
  
  private void appendIndent(int paramInt)
    throws IOException
  {
    int i = paramInt * 4;
    paramInt = i;
    if (i > sSpace.length()) {
      paramInt = sSpace.length();
    }
    append(sSpace, 0, paramInt);
  }
  
  private void escapeAndAppendString(String paramString)
    throws IOException
  {
    int i = paramString.length();
    int j = (char)ESCAPE_TABLE.length;
    String[] arrayOfString = ESCAPE_TABLE;
    int k = 0;
    for (int m = 0; m < i; m++)
    {
      int n = paramString.charAt(m);
      if (n < j)
      {
        String str = arrayOfString[n];
        if (str != null)
        {
          if (k < m) {
            append(paramString, k, m - k);
          }
          k = m + 1;
          append(str);
        }
      }
    }
    if (k < m) {
      append(paramString, k, m - k);
    }
  }
  
  private void escapeAndAppendString(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = (char)ESCAPE_TABLE.length;
    String[] arrayOfString = ESCAPE_TABLE;
    int j = paramInt1;
    int k = j;
    while (j < paramInt1 + paramInt2)
    {
      int m = paramArrayOfChar[j];
      if (m < i)
      {
        String str = arrayOfString[m];
        if (str != null)
        {
          if (k < j) {
            append(paramArrayOfChar, k, j - k);
          }
          k = j + 1;
          append(str);
        }
      }
      j++;
    }
    if (k < j) {
      append(paramArrayOfChar, k, j - k);
    }
  }
  
  private void flushBytes()
    throws IOException
  {
    int i = mBytes.position();
    if (i > 0)
    {
      mBytes.flip();
      mOutputStream.write(mBytes.array(), 0, i);
      mBytes.clear();
    }
  }
  
  public XmlSerializer attribute(String paramString1, String paramString2, String paramString3)
    throws IOException, IllegalArgumentException, IllegalStateException
  {
    append(' ');
    if (paramString1 != null)
    {
      append(paramString1);
      append(':');
    }
    append(paramString2);
    append("=\"");
    escapeAndAppendString(paramString3);
    append('"');
    mLineStart = false;
    return this;
  }
  
  public void cdsect(String paramString)
    throws IOException, IllegalArgumentException, IllegalStateException
  {
    throw new UnsupportedOperationException();
  }
  
  public void comment(String paramString)
    throws IOException, IllegalArgumentException, IllegalStateException
  {
    throw new UnsupportedOperationException();
  }
  
  public void docdecl(String paramString)
    throws IOException, IllegalArgumentException, IllegalStateException
  {
    throw new UnsupportedOperationException();
  }
  
  public void endDocument()
    throws IOException, IllegalArgumentException, IllegalStateException
  {
    flush();
  }
  
  public XmlSerializer endTag(String paramString1, String paramString2)
    throws IOException, IllegalArgumentException, IllegalStateException
  {
    mNesting -= 1;
    if (mInTag)
    {
      append(" />\n");
    }
    else
    {
      if ((mIndent) && (mLineStart)) {
        appendIndent(mNesting);
      }
      append("</");
      if (paramString1 != null)
      {
        append(paramString1);
        append(':');
      }
      append(paramString2);
      append(">\n");
    }
    mLineStart = true;
    mInTag = false;
    return this;
  }
  
  public void entityRef(String paramString)
    throws IOException, IllegalArgumentException, IllegalStateException
  {
    throw new UnsupportedOperationException();
  }
  
  public void flush()
    throws IOException
  {
    if (mPos > 0)
    {
      if (mOutputStream != null)
      {
        CharBuffer localCharBuffer = CharBuffer.wrap(mText, 0, mPos);
        CoderResult localCoderResult = mCharset.encode(localCharBuffer, mBytes, true);
        while (!localCoderResult.isError()) {
          if (localCoderResult.isOverflow())
          {
            flushBytes();
            localCoderResult = mCharset.encode(localCharBuffer, mBytes, true);
          }
          else
          {
            flushBytes();
            mOutputStream.flush();
            break label125;
          }
        }
        throw new IOException(localCoderResult.toString());
      }
      mWriter.write(mText, 0, mPos);
      mWriter.flush();
      label125:
      mPos = 0;
    }
  }
  
  public int getDepth()
  {
    throw new UnsupportedOperationException();
  }
  
  public boolean getFeature(String paramString)
  {
    throw new UnsupportedOperationException();
  }
  
  public String getName()
  {
    throw new UnsupportedOperationException();
  }
  
  public String getNamespace()
  {
    throw new UnsupportedOperationException();
  }
  
  public String getPrefix(String paramString, boolean paramBoolean)
    throws IllegalArgumentException
  {
    throw new UnsupportedOperationException();
  }
  
  public Object getProperty(String paramString)
  {
    throw new UnsupportedOperationException();
  }
  
  public void ignorableWhitespace(String paramString)
    throws IOException, IllegalArgumentException, IllegalStateException
  {
    throw new UnsupportedOperationException();
  }
  
  public void processingInstruction(String paramString)
    throws IOException, IllegalArgumentException, IllegalStateException
  {
    throw new UnsupportedOperationException();
  }
  
  public void setFeature(String paramString, boolean paramBoolean)
    throws IllegalArgumentException, IllegalStateException
  {
    if (paramString.equals("http://xmlpull.org/v1/doc/features.html#indent-output"))
    {
      mIndent = true;
      return;
    }
    throw new UnsupportedOperationException();
  }
  
  public void setOutput(OutputStream paramOutputStream, String paramString)
    throws IOException, IllegalArgumentException, IllegalStateException
  {
    if (paramOutputStream != null) {
      try
      {
        mCharset = Charset.forName(paramString).newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
        mOutputStream = paramOutputStream;
        return;
      }
      catch (UnsupportedCharsetException paramOutputStream)
      {
        throw ((UnsupportedEncodingException)new UnsupportedEncodingException(paramString).initCause(paramOutputStream));
      }
      catch (IllegalCharsetNameException paramOutputStream)
      {
        throw ((UnsupportedEncodingException)new UnsupportedEncodingException(paramString).initCause(paramOutputStream));
      }
    }
    throw new IllegalArgumentException();
  }
  
  public void setOutput(Writer paramWriter)
    throws IOException, IllegalArgumentException, IllegalStateException
  {
    mWriter = paramWriter;
  }
  
  public void setPrefix(String paramString1, String paramString2)
    throws IOException, IllegalArgumentException, IllegalStateException
  {
    throw new UnsupportedOperationException();
  }
  
  public void setProperty(String paramString, Object paramObject)
    throws IllegalArgumentException, IllegalStateException
  {
    throw new UnsupportedOperationException();
  }
  
  public void startDocument(String paramString, Boolean paramBoolean)
    throws IOException, IllegalArgumentException, IllegalStateException
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<?xml version='1.0' encoding='utf-8' standalone='");
    if (paramBoolean.booleanValue()) {
      paramString = "yes";
    } else {
      paramString = "no";
    }
    localStringBuilder.append(paramString);
    localStringBuilder.append("' ?>\n");
    append(localStringBuilder.toString());
    mLineStart = true;
  }
  
  public XmlSerializer startTag(String paramString1, String paramString2)
    throws IOException, IllegalArgumentException, IllegalStateException
  {
    if (mInTag) {
      append(">\n");
    }
    if (mIndent) {
      appendIndent(mNesting);
    }
    mNesting += 1;
    append('<');
    if (paramString1 != null)
    {
      append(paramString1);
      append(':');
    }
    append(paramString2);
    mInTag = true;
    mLineStart = false;
    return this;
  }
  
  public XmlSerializer text(String paramString)
    throws IOException, IllegalArgumentException, IllegalStateException
  {
    boolean bool1 = mInTag;
    boolean bool2 = false;
    if (bool1)
    {
      append(">");
      mInTag = false;
    }
    escapeAndAppendString(paramString);
    if (mIndent)
    {
      bool1 = bool2;
      if (paramString.length() > 0)
      {
        bool1 = bool2;
        if (paramString.charAt(paramString.length() - 1) == '\n') {
          bool1 = true;
        }
      }
      mLineStart = bool1;
    }
    return this;
  }
  
  public XmlSerializer text(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException, IllegalArgumentException, IllegalStateException
  {
    boolean bool1 = mInTag;
    boolean bool2 = false;
    if (bool1)
    {
      append(">");
      mInTag = false;
    }
    escapeAndAppendString(paramArrayOfChar, paramInt1, paramInt2);
    if (mIndent)
    {
      if (paramArrayOfChar[(paramInt1 + paramInt2 - 1)] == '\n') {
        bool2 = true;
      }
      mLineStart = bool2;
    }
    return this;
  }
}
