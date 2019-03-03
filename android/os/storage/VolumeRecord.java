package android.os.storage;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.DebugUtils;
import android.util.TimeUtils;
import com.android.internal.util.IndentingPrintWriter;
import com.android.internal.util.Preconditions;
import java.util.Objects;

public class VolumeRecord
  implements Parcelable
{
  public static final Parcelable.Creator<VolumeRecord> CREATOR = new Parcelable.Creator()
  {
    public VolumeRecord createFromParcel(Parcel paramAnonymousParcel)
    {
      return new VolumeRecord(paramAnonymousParcel);
    }
    
    public VolumeRecord[] newArray(int paramAnonymousInt)
    {
      return new VolumeRecord[paramAnonymousInt];
    }
  };
  public static final String EXTRA_FS_UUID = "android.os.storage.extra.FS_UUID";
  public static final int USER_FLAG_INITED = 1;
  public static final int USER_FLAG_SNOOZED = 2;
  public long createdMillis;
  public final String fsUuid;
  public long lastBenchMillis;
  public long lastTrimMillis;
  public String nickname;
  public String partGuid;
  public final int type;
  public int userFlags;
  
  public VolumeRecord(int paramInt, String paramString)
  {
    type = paramInt;
    fsUuid = ((String)Preconditions.checkNotNull(paramString));
  }
  
  public VolumeRecord(Parcel paramParcel)
  {
    type = paramParcel.readInt();
    fsUuid = paramParcel.readString();
    partGuid = paramParcel.readString();
    nickname = paramParcel.readString();
    userFlags = paramParcel.readInt();
    createdMillis = paramParcel.readLong();
    lastTrimMillis = paramParcel.readLong();
    lastBenchMillis = paramParcel.readLong();
  }
  
  public VolumeRecord clone()
  {
    Parcel localParcel = Parcel.obtain();
    try
    {
      writeToParcel(localParcel, 0);
      localParcel.setDataPosition(0);
      VolumeRecord localVolumeRecord = (VolumeRecord)CREATOR.createFromParcel(localParcel);
      return localVolumeRecord;
    }
    finally
    {
      localParcel.recycle();
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void dump(IndentingPrintWriter paramIndentingPrintWriter)
  {
    paramIndentingPrintWriter.println("VolumeRecord:");
    paramIndentingPrintWriter.increaseIndent();
    paramIndentingPrintWriter.printPair("type", DebugUtils.valueToString(VolumeInfo.class, "TYPE_", type));
    paramIndentingPrintWriter.printPair("fsUuid", fsUuid);
    paramIndentingPrintWriter.printPair("partGuid", partGuid);
    paramIndentingPrintWriter.println();
    paramIndentingPrintWriter.printPair("nickname", nickname);
    paramIndentingPrintWriter.printPair("userFlags", DebugUtils.flagsToString(VolumeRecord.class, "USER_FLAG_", userFlags));
    paramIndentingPrintWriter.println();
    paramIndentingPrintWriter.printPair("createdMillis", TimeUtils.formatForLogging(createdMillis));
    paramIndentingPrintWriter.printPair("lastTrimMillis", TimeUtils.formatForLogging(lastTrimMillis));
    paramIndentingPrintWriter.printPair("lastBenchMillis", TimeUtils.formatForLogging(lastBenchMillis));
    paramIndentingPrintWriter.decreaseIndent();
    paramIndentingPrintWriter.println();
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof VolumeRecord)) {
      return Objects.equals(fsUuid, fsUuid);
    }
    return false;
  }
  
  public String getFsUuid()
  {
    return fsUuid;
  }
  
  public String getNickname()
  {
    return nickname;
  }
  
  public int getType()
  {
    return type;
  }
  
  public int hashCode()
  {
    return fsUuid.hashCode();
  }
  
  public boolean isInited()
  {
    int i = userFlags;
    boolean bool = true;
    if ((i & 0x1) == 0) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isSnoozed()
  {
    boolean bool;
    if ((userFlags & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(type);
    paramParcel.writeString(fsUuid);
    paramParcel.writeString(partGuid);
    paramParcel.writeString(nickname);
    paramParcel.writeInt(userFlags);
    paramParcel.writeLong(createdMillis);
    paramParcel.writeLong(lastTrimMillis);
    paramParcel.writeLong(lastBenchMillis);
  }
}
