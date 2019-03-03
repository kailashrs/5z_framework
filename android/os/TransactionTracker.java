package android.os;

import android.util.Log;
import com.android.internal.util.FastPrintWriter;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TransactionTracker
{
  private Map<String, Long> mTraces;
  
  TransactionTracker()
  {
    resetTraces();
  }
  
  private void resetTraces()
  {
    try
    {
      HashMap localHashMap = new java/util/HashMap;
      localHashMap.<init>();
      mTraces = localHashMap;
      return;
    }
    finally {}
  }
  
  public void addTrace(Throwable paramThrowable)
  {
    paramThrowable = Log.getStackTraceString(paramThrowable);
    try
    {
      if (mTraces.containsKey(paramThrowable)) {
        mTraces.put(paramThrowable, Long.valueOf(((Long)mTraces.get(paramThrowable)).longValue() + 1L));
      } else {
        mTraces.put(paramThrowable, Long.valueOf(1L));
      }
      return;
    }
    finally {}
  }
  
  public void clearTraces()
  {
    resetTraces();
  }
  
  public void writeTracesToFile(ParcelFileDescriptor paramParcelFileDescriptor)
  {
    if (mTraces.isEmpty()) {
      return;
    }
    FastPrintWriter localFastPrintWriter = new FastPrintWriter(new FileOutputStream(paramParcelFileDescriptor.getFileDescriptor()));
    try
    {
      Iterator localIterator = mTraces.keySet().iterator();
      while (localIterator.hasNext())
      {
        paramParcelFileDescriptor = (String)localIterator.next();
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Count: ");
        localStringBuilder.append(mTraces.get(paramParcelFileDescriptor));
        localFastPrintWriter.println(localStringBuilder.toString());
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Trace: ");
        localStringBuilder.append(paramParcelFileDescriptor);
        localFastPrintWriter.println(localStringBuilder.toString());
        localFastPrintWriter.println();
      }
      localFastPrintWriter.flush();
      return;
    }
    finally {}
  }
}
