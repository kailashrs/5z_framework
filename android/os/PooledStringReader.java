package android.os;

public class PooledStringReader
{
  private final Parcel mIn;
  private final String[] mPool;
  
  public PooledStringReader(Parcel paramParcel)
  {
    mIn = paramParcel;
    mPool = new String[paramParcel.readInt()];
  }
  
  public int getStringCount()
  {
    return mPool.length;
  }
  
  public String readString()
  {
    int i = mIn.readInt();
    if (i >= 0) {
      return mPool[i];
    }
    i = -i;
    String str = mIn.readString();
    mPool[(i - 1)] = str;
    return str;
  }
}
