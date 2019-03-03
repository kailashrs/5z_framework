package com.android.internal.util;

import android.util.Log;
import android.util.Printer;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

public class FastPrintWriter
  extends PrintWriter
{
  private final boolean mAutoFlush;
  private final int mBufferLen;
  private final ByteBuffer mBytes;
  private CharsetEncoder mCharset;
  private boolean mIoError;
  private final OutputStream mOutputStream;
  private int mPos;
  private final Printer mPrinter;
  private final String mSeparator;
  private final char[] mText;
  private final Writer mWriter;
  
  public FastPrintWriter(Printer paramPrinter)
  {
    this(paramPrinter, 512);
  }
  
  public FastPrintWriter(Printer paramPrinter, int paramInt)
  {
    super(new DummyWriter(null), true);
    if (paramPrinter != null)
    {
      mBufferLen = paramInt;
      mText = new char[paramInt];
      mBytes = null;
      mOutputStream = null;
      mWriter = null;
      mPrinter = paramPrinter;
      mAutoFlush = true;
      mSeparator = System.lineSeparator();
      initDefaultEncoder();
      return;
    }
    throw new NullPointerException("pr is null");
  }
  
  public FastPrintWriter(OutputStream paramOutputStream)
  {
    this(paramOutputStream, false, 8192);
  }
  
  public FastPrintWriter(OutputStream paramOutputStream, boolean paramBoolean)
  {
    this(paramOutputStream, paramBoolean, 8192);
  }
  
  public FastPrintWriter(OutputStream paramOutputStream, boolean paramBoolean, int paramInt)
  {
    super(new DummyWriter(null), paramBoolean);
    if (paramOutputStream != null)
    {
      mBufferLen = paramInt;
      mText = new char[paramInt];
      mBytes = ByteBuffer.allocate(mBufferLen);
      mOutputStream = paramOutputStream;
      mWriter = null;
      mPrinter = null;
      mAutoFlush = paramBoolean;
      mSeparator = System.lineSeparator();
      initDefaultEncoder();
      return;
    }
    throw new NullPointerException("out is null");
  }
  
  public FastPrintWriter(Writer paramWriter)
  {
    this(paramWriter, false, 8192);
  }
  
  public FastPrintWriter(Writer paramWriter, boolean paramBoolean)
  {
    this(paramWriter, paramBoolean, 8192);
  }
  
  public FastPrintWriter(Writer paramWriter, boolean paramBoolean, int paramInt)
  {
    super(new DummyWriter(null), paramBoolean);
    if (paramWriter != null)
    {
      mBufferLen = paramInt;
      mText = new char[paramInt];
      mBytes = null;
      mOutputStream = null;
      mWriter = paramWriter;
      mPrinter = null;
      mAutoFlush = paramBoolean;
      mSeparator = System.lineSeparator();
      initDefaultEncoder();
      return;
    }
    throw new NullPointerException("wr is null");
  }
  
  private void appendLocked(char paramChar)
    throws IOException
  {
    int i = mPos;
    int j = i;
    if (i >= mBufferLen - 1)
    {
      flushLocked();
      j = mPos;
    }
    mText[j] = ((char)paramChar);
    mPos = (j + 1);
  }
  
  private void appendLocked(String paramString, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = mBufferLen;
    if (paramInt2 > i)
    {
      j = paramInt1 + paramInt2;
      while (paramInt1 < j)
      {
        k = paramInt1 + i;
        if (k < j) {
          paramInt2 = i;
        } else {
          paramInt2 = j - paramInt1;
        }
        appendLocked(paramString, paramInt1, paramInt2);
        paramInt1 = k;
      }
      return;
    }
    int j = mPos;
    int k = j;
    if (j + paramInt2 > i)
    {
      flushLocked();
      k = mPos;
    }
    paramString.getChars(paramInt1, paramInt1 + paramInt2, mText, k);
    mPos = (k + paramInt2);
  }
  
  private void appendLocked(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = mBufferLen;
    if (paramInt2 > i)
    {
      j = paramInt1 + paramInt2;
      while (paramInt1 < j)
      {
        k = paramInt1 + i;
        if (k < j) {
          paramInt2 = i;
        } else {
          paramInt2 = j - paramInt1;
        }
        appendLocked(paramArrayOfChar, paramInt1, paramInt2);
        paramInt1 = k;
      }
      return;
    }
    int j = mPos;
    int k = j;
    if (j + paramInt2 > i)
    {
      flushLocked();
      k = mPos;
    }
    System.arraycopy(paramArrayOfChar, paramInt1, mText, k, paramInt2);
    mPos = (k + paramInt2);
  }
  
  private void flushBytesLocked()
    throws IOException
  {
    if (!mIoError)
    {
      int i = mBytes.position();
      if (i > 0)
      {
        mBytes.flip();
        mOutputStream.write(mBytes.array(), 0, i);
        mBytes.clear();
      }
    }
  }
  
  private void flushLocked()
    throws IOException
  {
    if (mPos > 0)
    {
      if (mOutputStream != null)
      {
        CharBuffer localCharBuffer = CharBuffer.wrap(mText, 0, mPos);
        CoderResult localCoderResult = mCharset.encode(localCharBuffer, mBytes, true);
        while (!mIoError) {
          if (!localCoderResult.isError())
          {
            if (localCoderResult.isOverflow())
            {
              flushBytesLocked();
              localCoderResult = mCharset.encode(localCharBuffer, mBytes, true);
            }
          }
          else {
            throw new IOException(localCoderResult.toString());
          }
        }
        if (!mIoError)
        {
          flushBytesLocked();
          mOutputStream.flush();
        }
      }
      else if (mWriter != null)
      {
        if (!mIoError)
        {
          mWriter.write(mText, 0, mPos);
          mWriter.flush();
        }
      }
      else
      {
        int i = 0;
        int j = mSeparator.length();
        if (j >= mPos) {
          j = mPos;
        }
        while ((i < j) && (mText[(mPos - 1 - i)] == mSeparator.charAt(mSeparator.length() - 1 - i))) {
          i++;
        }
        if (i >= mPos) {
          mPrinter.println("");
        } else {
          mPrinter.println(new String(mText, 0, mPos - i));
        }
      }
      mPos = 0;
    }
  }
  
  private final void initDefaultEncoder()
  {
    mCharset = Charset.defaultCharset().newEncoder();
    mCharset.onMalformedInput(CodingErrorAction.REPLACE);
    mCharset.onUnmappableCharacter(CodingErrorAction.REPLACE);
  }
  
  private final void initEncoder(String paramString)
    throws UnsupportedEncodingException
  {
    try
    {
      mCharset = Charset.forName(paramString).newEncoder();
      mCharset.onMalformedInput(CodingErrorAction.REPLACE);
      mCharset.onUnmappableCharacter(CodingErrorAction.REPLACE);
      return;
    }
    catch (Exception localException)
    {
      throw new UnsupportedEncodingException(paramString);
    }
  }
  
  public PrintWriter append(CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    Object localObject = paramCharSequence;
    if (paramCharSequence == null) {
      localObject = "null";
    }
    paramCharSequence = ((CharSequence)localObject).subSequence(paramInt1, paramInt2).toString();
    write(paramCharSequence, 0, paramCharSequence.length());
    return this;
  }
  
  public boolean checkError()
  {
    flush();
    synchronized (lock)
    {
      boolean bool = mIoError;
      return bool;
    }
  }
  
  protected void clearError()
  {
    synchronized (lock)
    {
      mIoError = false;
      return;
    }
  }
  
  public void close()
  {
    try
    {
      synchronized (lock)
      {
        flushLocked();
        if (mOutputStream != null) {
          mOutputStream.close();
        } else if (mWriter != null) {
          mWriter.close();
        }
      }
    }
    catch (IOException localIOException)
    {
      Log.w("FastPrintWriter", "Write failure", localIOException);
      setError();
      return;
    }
  }
  
  public void flush()
  {
    try
    {
      synchronized (lock)
      {
        flushLocked();
        if (!mIoError) {
          if (mOutputStream != null) {
            mOutputStream.flush();
          } else if (mWriter != null) {
            mWriter.flush();
          }
        }
      }
    }
    catch (IOException localIOException)
    {
      Log.w("FastPrintWriter", "Write failure", localIOException);
      setError();
      return;
    }
  }
  
  public void print(char paramChar)
  {
    try
    {
      synchronized (lock)
      {
        appendLocked(paramChar);
      }
    }
    catch (IOException localIOException)
    {
      Log.w("FastPrintWriter", "Write failure", localIOException);
      setError();
      return;
    }
  }
  
  public void print(int paramInt)
  {
    if (paramInt == 0) {
      print("0");
    } else {
      super.print(paramInt);
    }
  }
  
  public void print(long paramLong)
  {
    if (paramLong == 0L) {
      print("0");
    } else {
      super.print(paramLong);
    }
  }
  
  public void print(String arg1)
  {
    String str = ???;
    if (??? == null) {
      str = String.valueOf(null);
    }
    try
    {
      synchronized (lock)
      {
        appendLocked(str, 0, str.length());
      }
    }
    catch (IOException localIOException)
    {
      Log.w("FastPrintWriter", "Write failure", localIOException);
      setError();
      return;
    }
  }
  
  public void print(char[] paramArrayOfChar)
  {
    try
    {
      synchronized (lock)
      {
        appendLocked(paramArrayOfChar, 0, paramArrayOfChar.length);
      }
    }
    catch (IOException paramArrayOfChar)
    {
      Log.w("FastPrintWriter", "Write failure", paramArrayOfChar);
      setError();
      return;
    }
  }
  
  public void println()
  {
    try
    {
      synchronized (lock)
      {
        appendLocked(mSeparator, 0, mSeparator.length());
        if (mAutoFlush) {
          flushLocked();
        }
      }
    }
    catch (IOException localIOException)
    {
      Log.w("FastPrintWriter", "Write failure", localIOException);
      setError();
      return;
    }
  }
  
  public void println(char paramChar)
  {
    print(paramChar);
    println();
  }
  
  public void println(int paramInt)
  {
    if (paramInt == 0) {
      println("0");
    } else {
      super.println(paramInt);
    }
  }
  
  public void println(long paramLong)
  {
    if (paramLong == 0L) {
      println("0");
    } else {
      super.println(paramLong);
    }
  }
  
  public void println(char[] paramArrayOfChar)
  {
    print(paramArrayOfChar);
    println();
  }
  
  protected void setError()
  {
    synchronized (lock)
    {
      mIoError = true;
      return;
    }
  }
  
  /* Error */
  public void write(int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 252	com/android/internal/util/FastPrintWriter:lock	Ljava/lang/Object;
    //   4: astore_2
    //   5: aload_2
    //   6: monitorenter
    //   7: iload_1
    //   8: i2c
    //   9: istore_3
    //   10: aload_0
    //   11: iload_3
    //   12: invokespecial 273	com/android/internal/util/FastPrintWriter:appendLocked	(C)V
    //   15: goto +26 -> 41
    //   18: astore 4
    //   20: goto +24 -> 44
    //   23: astore 4
    //   25: ldc_w 259
    //   28: ldc_w 261
    //   31: aload 4
    //   33: invokestatic 267	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   36: pop
    //   37: aload_0
    //   38: invokevirtual 270	com/android/internal/util/FastPrintWriter:setError	()V
    //   41: aload_2
    //   42: monitorexit
    //   43: return
    //   44: aload_2
    //   45: monitorexit
    //   46: aload 4
    //   48: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	49	0	this	FastPrintWriter
    //   0	49	1	paramInt	int
    //   4	41	2	localObject1	Object
    //   9	3	3	c	char
    //   18	1	4	localObject2	Object
    //   23	24	4	localIOException	IOException
    // Exception table:
    //   from	to	target	type
    //   10	15	18	finally
    //   25	41	18	finally
    //   41	43	18	finally
    //   44	46	18	finally
    //   10	15	23	java/io/IOException
  }
  
  public void write(String paramString)
  {
    try
    {
      synchronized (lock)
      {
        appendLocked(paramString, 0, paramString.length());
      }
    }
    catch (IOException paramString)
    {
      Log.w("FastPrintWriter", "Write failure", paramString);
      setError();
      return;
    }
  }
  
  public void write(String paramString, int paramInt1, int paramInt2)
  {
    try
    {
      synchronized (lock)
      {
        appendLocked(paramString, paramInt1, paramInt2);
      }
    }
    catch (IOException paramString)
    {
      Log.w("FastPrintWriter", "Write failure", paramString);
      setError();
      return;
    }
  }
  
  public void write(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    try
    {
      synchronized (lock)
      {
        appendLocked(paramArrayOfChar, paramInt1, paramInt2);
      }
    }
    catch (IOException paramArrayOfChar)
    {
      Log.w("FastPrintWriter", "Write failure", paramArrayOfChar);
      setError();
      return;
    }
  }
  
  private static class DummyWriter
    extends Writer
  {
    private DummyWriter() {}
    
    public void close()
      throws IOException
    {
      throw new UnsupportedOperationException("Shouldn't be here");
    }
    
    public void flush()
      throws IOException
    {
      close();
    }
    
    public void write(char[] paramArrayOfChar, int paramInt1, int paramInt2)
      throws IOException
    {
      close();
    }
  }
}
