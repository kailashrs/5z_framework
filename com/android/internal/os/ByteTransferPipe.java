package com.android.internal.os;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ByteTransferPipe
  extends TransferPipe
{
  static final String TAG = "ByteTransferPipe";
  private ByteArrayOutputStream mOutputStream;
  
  public ByteTransferPipe()
    throws IOException
  {}
  
  public ByteTransferPipe(String paramString)
    throws IOException
  {
    super(paramString, "ByteTransferPipe");
  }
  
  public byte[] get()
    throws IOException
  {
    go(null);
    return mOutputStream.toByteArray();
  }
  
  protected OutputStream getNewOutputStream()
  {
    mOutputStream = new ByteArrayOutputStream();
    return mOutputStream;
  }
}
