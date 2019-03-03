package android.os;

import android.util.EventLog;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public final class StatsLogEventWrapper
  implements Parcelable
{
  public static final Parcelable.Creator<StatsLogEventWrapper> CREATOR = new Parcelable.Creator()
  {
    public StatsLogEventWrapper createFromParcel(Parcel paramAnonymousParcel)
    {
      EventLog.writeEvent(1397638484, new Object[] { "112550251", Integer.valueOf(Binder.getCallingUid()), "" });
      throw new RuntimeException("Not implemented");
    }
    
    public StatsLogEventWrapper[] newArray(int paramAnonymousInt)
    {
      EventLog.writeEvent(1397638484, new Object[] { "112550251", Integer.valueOf(Binder.getCallingUid()), "" });
      throw new RuntimeException("Not implemented");
    }
  };
  private static final int EVENT_TYPE_FLOAT = 4;
  private static final int EVENT_TYPE_INT = 0;
  private static final int EVENT_TYPE_LIST = 3;
  private static final int EVENT_TYPE_LONG = 1;
  private static final int EVENT_TYPE_STRING = 2;
  private static final int STATS_BUFFER_TAG_ID = 1937006964;
  private ByteArrayOutputStream mStorage = new ByteArrayOutputStream();
  
  public StatsLogEventWrapper(long paramLong, int paramInt1, int paramInt2)
  {
    write4Bytes(1937006964);
    mStorage.write(3);
    mStorage.write(paramInt2 + 2);
    writeLong(paramLong);
    writeInt(paramInt1);
  }
  
  private void write4Bytes(int paramInt)
  {
    mStorage.write(paramInt);
    mStorage.write(paramInt >>> 8);
    mStorage.write(paramInt >>> 16);
    mStorage.write(paramInt >>> 24);
  }
  
  private void write8Bytes(long paramLong)
  {
    write4Bytes((int)(0xFFFFFFFFFFFFFFFF & paramLong));
    write4Bytes((int)(paramLong >>> 32));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeFloat(float paramFloat)
  {
    int i = Float.floatToIntBits(paramFloat);
    mStorage.write(4);
    write4Bytes(i);
  }
  
  public void writeInt(int paramInt)
  {
    mStorage.write(0);
    write4Bytes(paramInt);
  }
  
  public void writeLong(long paramLong)
  {
    mStorage.write(1);
    write8Bytes(paramLong);
  }
  
  public void writeString(String paramString)
  {
    mStorage.write(2);
    write4Bytes(paramString.length());
    paramString = paramString.getBytes(StandardCharsets.UTF_8);
    mStorage.write(paramString, 0, paramString.length);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    mStorage.write(10);
    paramParcel.writeByteArray(mStorage.toByteArray());
  }
}
