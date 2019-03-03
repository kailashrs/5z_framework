package android.os.storage;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.DebugUtils;
import com.android.internal.util.IndentingPrintWriter;
import com.android.internal.util.Preconditions;
import java.io.CharArrayWriter;
import java.util.Objects;

public class DiskInfo
  implements Parcelable
{
  public static final String ACTION_DISK_SCANNED = "android.os.storage.action.DISK_SCANNED";
  public static final Parcelable.Creator<DiskInfo> CREATOR = new Parcelable.Creator()
  {
    public DiskInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new DiskInfo(paramAnonymousParcel);
    }
    
    public DiskInfo[] newArray(int paramAnonymousInt)
    {
      return new DiskInfo[paramAnonymousInt];
    }
  };
  public static final String EXTRA_DISK_ID = "android.os.storage.extra.DISK_ID";
  public static final String EXTRA_VOLUME_COUNT = "android.os.storage.extra.VOLUME_COUNT";
  public static final int FLAG_ADOPTABLE = 1;
  public static final int FLAG_DEFAULT_PRIMARY = 2;
  public static final int FLAG_SD = 4;
  public static final int FLAG_USB = 8;
  public final int flags;
  public final String id;
  public String label;
  public long size;
  public String sysPath;
  public int volumeCount;
  
  public DiskInfo(Parcel paramParcel)
  {
    id = paramParcel.readString();
    flags = paramParcel.readInt();
    size = paramParcel.readLong();
    label = paramParcel.readString();
    volumeCount = paramParcel.readInt();
    sysPath = paramParcel.readString();
  }
  
  public DiskInfo(String paramString, int paramInt)
  {
    id = ((String)Preconditions.checkNotNull(paramString));
    flags = paramInt;
  }
  
  private boolean isInteresting(String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      return false;
    }
    if (paramString.equalsIgnoreCase("ata")) {
      return false;
    }
    if (paramString.toLowerCase().contains("generic")) {
      return false;
    }
    if (paramString.toLowerCase().startsWith("usb")) {
      return false;
    }
    return !paramString.toLowerCase().startsWith("multiple");
  }
  
  public DiskInfo clone()
  {
    Parcel localParcel = Parcel.obtain();
    try
    {
      writeToParcel(localParcel, 0);
      localParcel.setDataPosition(0);
      DiskInfo localDiskInfo = (DiskInfo)CREATOR.createFromParcel(localParcel);
      return localDiskInfo;
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
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("DiskInfo{");
    localStringBuilder.append(id);
    localStringBuilder.append("}:");
    paramIndentingPrintWriter.println(localStringBuilder.toString());
    paramIndentingPrintWriter.increaseIndent();
    paramIndentingPrintWriter.printPair("flags", DebugUtils.flagsToString(getClass(), "FLAG_", flags));
    paramIndentingPrintWriter.printPair("size", Long.valueOf(size));
    paramIndentingPrintWriter.printPair("label", label);
    paramIndentingPrintWriter.println();
    paramIndentingPrintWriter.printPair("sysPath", sysPath);
    paramIndentingPrintWriter.decreaseIndent();
    paramIndentingPrintWriter.println();
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof DiskInfo)) {
      return Objects.equals(id, id);
    }
    return false;
  }
  
  public String getDescription()
  {
    Resources localResources = Resources.getSystem();
    if ((flags & 0x4) != 0)
    {
      if (isInteresting(label)) {
        return localResources.getString(17041087, new Object[] { label });
      }
      return localResources.getString(17041086);
    }
    if ((flags & 0x8) != 0)
    {
      if (isInteresting(label)) {
        return localResources.getString(17041090, new Object[] { label });
      }
      return localResources.getString(17041089);
    }
    return null;
  }
  
  public String getId()
  {
    return id;
  }
  
  public String getShortDescription()
  {
    Resources localResources = Resources.getSystem();
    if (isSd()) {
      return localResources.getString(17041086);
    }
    if (isUsb()) {
      return localResources.getString(17041089);
    }
    return null;
  }
  
  public int hashCode()
  {
    return id.hashCode();
  }
  
  public boolean isAdoptable()
  {
    int i = flags;
    boolean bool = true;
    if ((i & 0x1) == 0) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isDefaultPrimary()
  {
    boolean bool;
    if ((flags & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isSd()
  {
    boolean bool;
    if ((flags & 0x4) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isUsb()
  {
    boolean bool;
    if ((flags & 0x8) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    CharArrayWriter localCharArrayWriter = new CharArrayWriter();
    dump(new IndentingPrintWriter(localCharArrayWriter, "    ", 80));
    return localCharArrayWriter.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(id);
    paramParcel.writeInt(flags);
    paramParcel.writeLong(size);
    paramParcel.writeString(label);
    paramParcel.writeInt(volumeCount);
    paramParcel.writeString(sysPath);
  }
}
