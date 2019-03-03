package android.util;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public final class LocalLog
{
  private final Deque<String> mLog;
  private final int mMaxLines;
  
  public LocalLog(int paramInt)
  {
    mMaxLines = Math.max(0, paramInt);
    mLog = new ArrayDeque(mMaxLines);
  }
  
  private void append(String paramString)
  {
    try
    {
      while (mLog.size() >= mMaxLines) {
        mLog.remove();
      }
      mLog.add(paramString);
      return;
    }
    finally {}
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    try
    {
      paramFileDescriptor = mLog.iterator();
      while (paramFileDescriptor.hasNext()) {
        paramPrintWriter.println((String)paramFileDescriptor.next());
      }
      return;
    }
    finally {}
  }
  
  public void log(String paramString)
  {
    if (mMaxLines <= 0) {
      return;
    }
    append(String.format("%s - %s", new Object[] { LocalDateTime.now(), paramString }));
  }
  
  public ReadOnlyLocalLog readOnlyLocalLog()
  {
    return new ReadOnlyLocalLog(this);
  }
  
  public void reverseDump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    try
    {
      paramFileDescriptor = mLog.descendingIterator();
      while (paramFileDescriptor.hasNext()) {
        paramPrintWriter.println((String)paramFileDescriptor.next());
      }
      return;
    }
    finally {}
  }
  
  public static class ReadOnlyLocalLog
  {
    private final LocalLog mLog;
    
    ReadOnlyLocalLog(LocalLog paramLocalLog)
    {
      mLog = paramLocalLog;
    }
    
    public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
    {
      mLog.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    }
    
    public void reverseDump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
    {
      mLog.reverseDump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    }
  }
}
