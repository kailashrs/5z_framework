package com.android.internal.util.dump;

import android.util.Log;
import android.util.proto.ProtoOutputStream;
import com.android.internal.util.IndentingPrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class DualDumpOutputStream
{
  private static final String LOG_TAG = DualDumpOutputStream.class.getSimpleName();
  private final LinkedList<DumpObject> mDumpObjects = new LinkedList();
  private final IndentingPrintWriter mIpw;
  private final ProtoOutputStream mProtoStream;
  
  public DualDumpOutputStream(ProtoOutputStream paramProtoOutputStream)
  {
    mProtoStream = paramProtoOutputStream;
    mIpw = null;
  }
  
  public DualDumpOutputStream(IndentingPrintWriter paramIndentingPrintWriter)
  {
    mProtoStream = null;
    mIpw = paramIndentingPrintWriter;
    mDumpObjects.add(new DumpObject(null, null));
  }
  
  public void end(long paramLong)
  {
    if (mProtoStream != null)
    {
      mProtoStream.end(paramLong);
    }
    else
    {
      if (System.identityHashCode(mDumpObjects.getLast()) != paramLong)
      {
        String str = LOG_TAG;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unexpected token for ending ");
        localStringBuilder.append(mDumpObjects.getLast()).name);
        localStringBuilder.append(" at ");
        localStringBuilder.append(Arrays.toString(Thread.currentThread().getStackTrace()));
        Log.w(str, localStringBuilder.toString());
      }
      mDumpObjects.removeLast();
    }
  }
  
  public void flush()
  {
    if (mProtoStream != null)
    {
      mProtoStream.flush();
    }
    else
    {
      if (mDumpObjects.size() == 1)
      {
        ((DumpObject)mDumpObjects.getFirst()).print(mIpw, false);
        mDumpObjects.clear();
        mDumpObjects.add(new DumpObject(null, null));
      }
      mIpw.flush();
    }
  }
  
  public boolean isProto()
  {
    boolean bool;
    if (mProtoStream != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public long start(String paramString, long paramLong)
  {
    if (mProtoStream != null) {
      return mProtoStream.start(paramLong);
    }
    DumpObject localDumpObject = new DumpObject(paramString, null);
    ((DumpObject)mDumpObjects.getLast()).add(paramString, localDumpObject);
    mDumpObjects.addLast(localDumpObject);
    return System.identityHashCode(localDumpObject);
  }
  
  public void write(String paramString, long paramLong, double paramDouble)
  {
    if (mProtoStream != null) {
      mProtoStream.write(paramLong, paramDouble);
    } else {
      ((DumpObject)mDumpObjects.getLast()).add(paramString, new DumpField(paramString, String.valueOf(paramDouble), null));
    }
  }
  
  public void write(String paramString, long paramLong, float paramFloat)
  {
    if (mProtoStream != null) {
      mProtoStream.write(paramLong, paramFloat);
    } else {
      ((DumpObject)mDumpObjects.getLast()).add(paramString, new DumpField(paramString, String.valueOf(paramFloat), null));
    }
  }
  
  public void write(String paramString, long paramLong, int paramInt)
  {
    if (mProtoStream != null) {
      mProtoStream.write(paramLong, paramInt);
    } else {
      ((DumpObject)mDumpObjects.getLast()).add(paramString, new DumpField(paramString, String.valueOf(paramInt), null));
    }
  }
  
  public void write(String paramString, long paramLong1, long paramLong2)
  {
    if (mProtoStream != null) {
      mProtoStream.write(paramLong1, paramLong2);
    } else {
      ((DumpObject)mDumpObjects.getLast()).add(paramString, new DumpField(paramString, String.valueOf(paramLong2), null));
    }
  }
  
  public void write(String paramString1, long paramLong, String paramString2)
  {
    if (mProtoStream != null) {
      mProtoStream.write(paramLong, paramString2);
    } else {
      ((DumpObject)mDumpObjects.getLast()).add(paramString1, new DumpField(paramString1, String.valueOf(paramString2), null));
    }
  }
  
  public void write(String paramString, long paramLong, boolean paramBoolean)
  {
    if (mProtoStream != null) {
      mProtoStream.write(paramLong, paramBoolean);
    } else {
      ((DumpObject)mDumpObjects.getLast()).add(paramString, new DumpField(paramString, String.valueOf(paramBoolean), null));
    }
  }
  
  public void write(String paramString, long paramLong, byte[] paramArrayOfByte)
  {
    if (mProtoStream != null) {
      mProtoStream.write(paramLong, paramArrayOfByte);
    } else {
      ((DumpObject)mDumpObjects.getLast()).add(paramString, new DumpField(paramString, Arrays.toString(paramArrayOfByte), null));
    }
  }
  
  public void writeNested(String paramString, byte[] paramArrayOfByte)
  {
    if (mIpw == null)
    {
      Log.w(LOG_TAG, "writeNested does not work for proto logging");
      return;
    }
    ((DumpObject)mDumpObjects.getLast()).add(paramString, new DumpField(paramString, new String(paramArrayOfByte, StandardCharsets.UTF_8).trim(), null));
  }
  
  private static class DumpField
    extends DualDumpOutputStream.Dumpable
  {
    private final String mValue;
    
    private DumpField(String paramString1, String paramString2)
    {
      super(null);
      mValue = paramString2;
    }
    
    void print(IndentingPrintWriter paramIndentingPrintWriter, boolean paramBoolean)
    {
      if (paramBoolean)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(name);
        localStringBuilder.append("=");
        localStringBuilder.append(mValue);
        paramIndentingPrintWriter.println(localStringBuilder.toString());
      }
      else
      {
        paramIndentingPrintWriter.println(mValue);
      }
    }
  }
  
  private static class DumpObject
    extends DualDumpOutputStream.Dumpable
  {
    private final LinkedHashMap<String, ArrayList<DualDumpOutputStream.Dumpable>> mSubObjects = new LinkedHashMap();
    
    private DumpObject(String paramString)
    {
      super(null);
    }
    
    public void add(String paramString, DualDumpOutputStream.Dumpable paramDumpable)
    {
      ArrayList localArrayList1 = (ArrayList)mSubObjects.get(paramString);
      ArrayList localArrayList2 = localArrayList1;
      if (localArrayList1 == null)
      {
        localArrayList2 = new ArrayList(1);
        mSubObjects.put(paramString, localArrayList2);
      }
      localArrayList2.add(paramDumpable);
    }
    
    void print(IndentingPrintWriter paramIndentingPrintWriter, boolean paramBoolean)
    {
      if (paramBoolean)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(name);
        ((StringBuilder)localObject).append("={");
        paramIndentingPrintWriter.println(((StringBuilder)localObject).toString());
      }
      else
      {
        paramIndentingPrintWriter.println("{");
      }
      paramIndentingPrintWriter.increaseIndent();
      Object localObject = mSubObjects.values().iterator();
      while (((Iterator)localObject).hasNext())
      {
        ArrayList localArrayList = (ArrayList)((Iterator)localObject).next();
        int i = localArrayList.size();
        if (i == 1)
        {
          ((DualDumpOutputStream.Dumpable)localArrayList.get(0)).print(paramIndentingPrintWriter, true);
        }
        else
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append(get0name);
          localStringBuilder.append("=[");
          paramIndentingPrintWriter.println(localStringBuilder.toString());
          paramIndentingPrintWriter.increaseIndent();
          for (int j = 0; j < i; j++) {
            ((DualDumpOutputStream.Dumpable)localArrayList.get(j)).print(paramIndentingPrintWriter, false);
          }
          paramIndentingPrintWriter.decreaseIndent();
          paramIndentingPrintWriter.println("]");
        }
      }
      paramIndentingPrintWriter.decreaseIndent();
      paramIndentingPrintWriter.println("}");
    }
  }
  
  private static abstract class Dumpable
  {
    final String name;
    
    private Dumpable(String paramString)
    {
      name = paramString;
    }
    
    abstract void print(IndentingPrintWriter paramIndentingPrintWriter, boolean paramBoolean);
  }
}
