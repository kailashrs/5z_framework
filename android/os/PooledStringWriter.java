package android.os;

import java.util.HashMap;

public class PooledStringWriter
{
  private int mNext;
  private final Parcel mOut;
  private final HashMap<String, Integer> mPool;
  private int mStart;
  
  public PooledStringWriter(Parcel paramParcel)
  {
    mOut = paramParcel;
    mPool = new HashMap();
    mStart = paramParcel.dataPosition();
    paramParcel.writeInt(0);
  }
  
  public void finish()
  {
    int i = mOut.dataPosition();
    mOut.setDataPosition(mStart);
    mOut.writeInt(mNext);
    mOut.setDataPosition(i);
  }
  
  public int getStringCount()
  {
    return mPool.size();
  }
  
  public void writeString(String paramString)
  {
    Integer localInteger = (Integer)mPool.get(paramString);
    if (localInteger != null)
    {
      mOut.writeInt(localInteger.intValue());
    }
    else
    {
      mPool.put(paramString, Integer.valueOf(mNext));
      mOut.writeInt(-(mNext + 1));
      mOut.writeString(paramString);
      mNext += 1;
    }
  }
}
