package com.android.internal.util;

import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class LocalLog
{
  private final ArrayList<String> mLines = new ArrayList(20);
  private final int mMaxLines = 20;
  private final String mTag;
  
  public LocalLog(String paramString)
  {
    mTag = paramString;
  }
  
  public boolean dump(PrintWriter paramPrintWriter, String paramString1, String paramString2)
  {
    synchronized (mLines)
    {
      int i = mLines.size();
      int j = 0;
      if (i <= 0) {
        return false;
      }
      if (paramString1 != null) {
        paramPrintWriter.println(paramString1);
      }
      while (j < mLines.size())
      {
        if (paramString2 != null) {
          paramPrintWriter.print(paramString2);
        }
        paramPrintWriter.println((String)mLines.get(j));
        j++;
      }
      return true;
    }
  }
  
  public void w(String paramString)
  {
    synchronized (mLines)
    {
      Slog.w(mTag, paramString);
      if (mLines.size() >= 20) {
        mLines.remove(0);
      }
      mLines.add(paramString);
      return;
    }
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    ArrayList localArrayList = mLines;
    int i = 0;
    try
    {
      while (i < mLines.size())
      {
        paramProtoOutputStream.write(2237677961217L, (String)mLines.get(i));
        i++;
      }
      paramProtoOutputStream.end(paramLong);
      return;
    }
    finally {}
  }
}
