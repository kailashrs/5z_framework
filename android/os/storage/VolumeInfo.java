package android.os.storage;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.UserHandle;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.DebugUtils;
import android.util.SparseArray;
import android.util.SparseIntArray;
import com.android.internal.util.IndentingPrintWriter;
import com.android.internal.util.Preconditions;
import java.io.CharArrayWriter;
import java.io.File;
import java.util.Comparator;
import java.util.Objects;

public class VolumeInfo
  implements Parcelable
{
  public static final String ACTION_VOLUME_STATE_CHANGED = "android.os.storage.action.VOLUME_STATE_CHANGED";
  public static final Parcelable.Creator<VolumeInfo> CREATOR = new Parcelable.Creator()
  {
    public VolumeInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new VolumeInfo(paramAnonymousParcel);
    }
    
    public VolumeInfo[] newArray(int paramAnonymousInt)
    {
      return new VolumeInfo[paramAnonymousInt];
    }
  };
  private static final String DOCUMENT_AUTHORITY = "com.android.externalstorage.documents";
  private static final String DOCUMENT_ROOT_PRIMARY_EMULATED = "primary";
  public static final String EXTRA_VOLUME_ID = "android.os.storage.extra.VOLUME_ID";
  public static final String EXTRA_VOLUME_STATE = "android.os.storage.extra.VOLUME_STATE";
  public static final String ID_EMULATED_INTERNAL = "emulated";
  public static final String ID_PRIVATE_INTERNAL = "private";
  public static final int MOUNT_FLAG_PRIMARY = 1;
  public static final int MOUNT_FLAG_VISIBLE = 2;
  public static final int STATE_BAD_REMOVAL = 8;
  public static final int STATE_CHECKING = 1;
  public static final int STATE_EJECTING = 5;
  public static final int STATE_FORMATTING = 4;
  public static final int STATE_MOUNTED = 2;
  public static final int STATE_MOUNTED_READ_ONLY = 3;
  public static final int STATE_REMOVED = 7;
  public static final int STATE_UNMOUNTABLE = 6;
  public static final int STATE_UNMOUNTED = 0;
  public static final int TYPE_ASEC = 3;
  public static final int TYPE_EMULATED = 2;
  public static final int TYPE_OBB = 4;
  public static final int TYPE_PRIVATE = 1;
  public static final int TYPE_PUBLIC = 0;
  private static final Comparator<VolumeInfo> sDescriptionComparator;
  private static ArrayMap<String, String> sEnvironmentToBroadcast;
  private static SparseIntArray sStateToDescrip;
  private static SparseArray<String> sStateToEnvironment = new SparseArray();
  public final DiskInfo disk;
  public String fsLabel;
  public String fsType;
  public String fsUuid;
  public final String id;
  public String internalPath;
  public int mountFlags = 0;
  public int mountUserId = -1;
  public final String partGuid;
  public String path;
  public int state = 0;
  public final int type;
  
  static
  {
    sEnvironmentToBroadcast = new ArrayMap();
    sStateToDescrip = new SparseIntArray();
    sDescriptionComparator = new Comparator()
    {
      public int compare(VolumeInfo paramAnonymousVolumeInfo1, VolumeInfo paramAnonymousVolumeInfo2)
      {
        if ("private".equals(paramAnonymousVolumeInfo1.getId())) {
          return -1;
        }
        if (paramAnonymousVolumeInfo1.getDescription() == null) {
          return 1;
        }
        if (paramAnonymousVolumeInfo2.getDescription() == null) {
          return -1;
        }
        return paramAnonymousVolumeInfo1.getDescription().compareTo(paramAnonymousVolumeInfo2.getDescription());
      }
    };
    sStateToEnvironment.put(0, "unmounted");
    sStateToEnvironment.put(1, "checking");
    sStateToEnvironment.put(2, "mounted");
    sStateToEnvironment.put(3, "mounted_ro");
    sStateToEnvironment.put(4, "unmounted");
    sStateToEnvironment.put(5, "ejecting");
    sStateToEnvironment.put(6, "unmountable");
    sStateToEnvironment.put(7, "removed");
    sStateToEnvironment.put(8, "bad_removal");
    sEnvironmentToBroadcast.put("unmounted", "android.intent.action.MEDIA_UNMOUNTED");
    sEnvironmentToBroadcast.put("checking", "android.intent.action.MEDIA_CHECKING");
    sEnvironmentToBroadcast.put("mounted", "android.intent.action.MEDIA_MOUNTED");
    sEnvironmentToBroadcast.put("mounted_ro", "android.intent.action.MEDIA_MOUNTED");
    sEnvironmentToBroadcast.put("ejecting", "android.intent.action.MEDIA_EJECT");
    sEnvironmentToBroadcast.put("unmountable", "android.intent.action.MEDIA_UNMOUNTABLE");
    sEnvironmentToBroadcast.put("removed", "android.intent.action.MEDIA_REMOVED");
    sEnvironmentToBroadcast.put("bad_removal", "android.intent.action.MEDIA_BAD_REMOVAL");
    sStateToDescrip.put(0, 17039970);
    sStateToDescrip.put(1, 17039962);
    sStateToDescrip.put(2, 17039966);
    sStateToDescrip.put(3, 17039967);
    sStateToDescrip.put(4, 17039964);
    sStateToDescrip.put(5, 17039963);
    sStateToDescrip.put(6, 17039969);
    sStateToDescrip.put(7, 17039968);
    sStateToDescrip.put(8, 17039961);
  }
  
  public VolumeInfo(Parcel paramParcel)
  {
    id = paramParcel.readString();
    type = paramParcel.readInt();
    if (paramParcel.readInt() != 0) {
      disk = ((DiskInfo)DiskInfo.CREATOR.createFromParcel(paramParcel));
    } else {
      disk = null;
    }
    partGuid = paramParcel.readString();
    mountFlags = paramParcel.readInt();
    mountUserId = paramParcel.readInt();
    state = paramParcel.readInt();
    fsType = paramParcel.readString();
    fsUuid = paramParcel.readString();
    fsLabel = paramParcel.readString();
    path = paramParcel.readString();
    internalPath = paramParcel.readString();
  }
  
  public VolumeInfo(String paramString1, int paramInt, DiskInfo paramDiskInfo, String paramString2)
  {
    id = ((String)Preconditions.checkNotNull(paramString1));
    type = paramInt;
    disk = paramDiskInfo;
    partGuid = paramString2;
  }
  
  public static int buildStableMtpStorageId(String paramString)
  {
    boolean bool = TextUtils.isEmpty(paramString);
    int i = 0;
    if (bool) {
      return 0;
    }
    int j = 0;
    while (i < paramString.length())
    {
      j = 31 * j + paramString.charAt(i);
      i++;
    }
    i = (j << 16 ^ j) & 0xFFFF0000;
    j = i;
    if (i == 0) {
      j = 131072;
    }
    i = j;
    if (j == 65536) {
      i = 131072;
    }
    j = i;
    if (i == -65536) {
      j = -131072;
    }
    return j | 0x1;
  }
  
  public static String getBroadcastForEnvironment(String paramString)
  {
    return (String)sEnvironmentToBroadcast.get(paramString);
  }
  
  public static String getBroadcastForState(int paramInt)
  {
    return getBroadcastForEnvironment(getEnvironmentForState(paramInt));
  }
  
  public static Comparator<VolumeInfo> getDescriptionComparator()
  {
    return sDescriptionComparator;
  }
  
  public static String getEnvironmentForState(int paramInt)
  {
    String str = (String)sStateToEnvironment.get(paramInt);
    if (str != null) {
      return str;
    }
    return "unknown";
  }
  
  public Intent buildBrowseIntent()
  {
    return buildBrowseIntentForUser(UserHandle.myUserId());
  }
  
  public Intent buildBrowseIntentForUser(int paramInt)
  {
    if ((type == 0) && (mountUserId == paramInt)) {}
    for (Uri localUri = DocumentsContract.buildRootUri("com.android.externalstorage.documents", fsUuid);; localUri = DocumentsContract.buildRootUri("com.android.externalstorage.documents", "primary"))
    {
      break;
      if ((type != 2) || (!isPrimary())) {
        break label96;
      }
    }
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.addCategory("android.intent.category.DEFAULT");
    localIntent.setDataAndType(localUri, "vnd.android.document/root");
    localIntent.putExtra("android.content.extra.SHOW_ADVANCED", isPrimary());
    return localIntent;
    label96:
    return null;
  }
  
  public StorageVolume buildStorageVolume(Context paramContext, int paramInt, boolean paramBoolean)
  {
    Object localObject1 = (StorageManager)paramContext.getSystemService(StorageManager.class);
    if (paramBoolean) {}
    for (String str1 = "unmounted";; str1 = getEnvironmentForState(state)) {
      break;
    }
    Object localObject2 = getPathForUser(paramInt);
    Object localObject3 = localObject2;
    if (localObject2 == null) {
      localObject3 = new File("/dev/null");
    }
    localObject2 = getInternalPathForUser(paramInt);
    Object localObject4 = localObject2;
    if (localObject2 == null) {
      localObject4 = new File("/dev/null");
    }
    localObject2 = null;
    String str2 = fsUuid;
    long l = 0L;
    boolean bool;
    if (type == 2)
    {
      VolumeInfo localVolumeInfo = ((StorageManager)localObject1).findPrivateForEmulated(this);
      if (localVolumeInfo != null)
      {
        localObject2 = ((StorageManager)localObject1).getBestVolumeDescription(localVolumeInfo);
        str2 = fsUuid;
      }
      if ("emulated".equals(id)) {
        paramBoolean = false;
      } else {
        paramBoolean = true;
      }
      l = 0L;
      bool = true;
    }
    else
    {
      if (type != 0) {
        break label264;
      }
      localObject2 = ((StorageManager)localObject1).getBestVolumeDescription(this);
      if ("vfat".equals(fsType)) {
        l = 4294967295L;
      }
      bool = false;
      paramBoolean = true;
    }
    localObject1 = localObject2;
    if (localObject2 == null) {
      localObject1 = paramContext.getString(17039374);
    }
    return new StorageVolume(id, (File)localObject3, (File)localObject4, (String)localObject1, isPrimary(), paramBoolean, bool, false, l, new UserHandle(paramInt), str2, str1);
    label264:
    paramContext = new StringBuilder();
    paramContext.append("Unexpected volume type ");
    paramContext.append(type);
    throw new IllegalStateException(paramContext.toString());
  }
  
  public VolumeInfo clone()
  {
    Parcel localParcel = Parcel.obtain();
    try
    {
      writeToParcel(localParcel, 0);
      localParcel.setDataPosition(0);
      VolumeInfo localVolumeInfo = (VolumeInfo)CREATOR.createFromParcel(localParcel);
      return localVolumeInfo;
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
    localStringBuilder.append("VolumeInfo{");
    localStringBuilder.append(id);
    localStringBuilder.append("}:");
    paramIndentingPrintWriter.println(localStringBuilder.toString());
    paramIndentingPrintWriter.increaseIndent();
    paramIndentingPrintWriter.printPair("type", DebugUtils.valueToString(getClass(), "TYPE_", type));
    paramIndentingPrintWriter.printPair("diskId", getDiskId());
    paramIndentingPrintWriter.printPair("partGuid", partGuid);
    paramIndentingPrintWriter.printPair("mountFlags", DebugUtils.flagsToString(getClass(), "MOUNT_FLAG_", mountFlags));
    paramIndentingPrintWriter.printPair("mountUserId", Integer.valueOf(mountUserId));
    paramIndentingPrintWriter.printPair("state", DebugUtils.valueToString(getClass(), "STATE_", state));
    paramIndentingPrintWriter.println();
    paramIndentingPrintWriter.printPair("fsType", fsType);
    paramIndentingPrintWriter.printPair("fsUuid", fsUuid);
    paramIndentingPrintWriter.printPair("fsLabel", fsLabel);
    paramIndentingPrintWriter.println();
    paramIndentingPrintWriter.printPair("path", path);
    paramIndentingPrintWriter.printPair("internalPath", internalPath);
    paramIndentingPrintWriter.decreaseIndent();
    paramIndentingPrintWriter.println();
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof VolumeInfo)) {
      return Objects.equals(id, id);
    }
    return false;
  }
  
  public String getDescription()
  {
    if ((!"private".equals(id)) && (!"emulated".equals(id)))
    {
      if (!TextUtils.isEmpty(fsLabel)) {
        return fsLabel;
      }
      if ((disk != null) && (disk.isSd())) {
        return Resources.getSystem().getString(17041086);
      }
      if ((disk != null) && (disk.isUsb())) {
        return Resources.getSystem().getString(17041088);
      }
      return null;
    }
    return Resources.getSystem().getString(17041085);
  }
  
  public DiskInfo getDisk()
  {
    return disk;
  }
  
  public String getDiskId()
  {
    String str;
    if (disk != null) {
      str = disk.id;
    } else {
      str = null;
    }
    return str;
  }
  
  public String getFsUuid()
  {
    return fsUuid;
  }
  
  public String getId()
  {
    return id;
  }
  
  public File getInternalPath()
  {
    File localFile;
    if (internalPath != null) {
      localFile = new File(internalPath);
    } else {
      localFile = null;
    }
    return localFile;
  }
  
  public File getInternalPathForUser(int paramInt)
  {
    if (path == null) {
      return null;
    }
    if (type == 0) {
      return new File(path.replace("/storage/", "/mnt/media_rw/"));
    }
    return getPathForUser(paramInt);
  }
  
  public int getMountUserId()
  {
    return mountUserId;
  }
  
  public File getPath()
  {
    File localFile;
    if (path != null) {
      localFile = new File(path);
    } else {
      localFile = null;
    }
    return localFile;
  }
  
  public File getPathForUser(int paramInt)
  {
    if (path == null) {
      return null;
    }
    if (type == 0) {
      return new File(path);
    }
    if (type == 2) {
      return new File(path, Integer.toString(paramInt));
    }
    return null;
  }
  
  public int getState()
  {
    return state;
  }
  
  public int getStateDescription()
  {
    return sStateToDescrip.get(state, 0);
  }
  
  public int getType()
  {
    return type;
  }
  
  public int hashCode()
  {
    return id.hashCode();
  }
  
  public boolean isMountedReadable()
  {
    boolean bool;
    if ((state != 2) && (state != 3)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isMountedWritable()
  {
    boolean bool;
    if (state == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isPrimary()
  {
    int i = mountFlags;
    boolean bool = true;
    if ((i & 0x1) == 0) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isPrimaryPhysical()
  {
    boolean bool;
    if ((isPrimary()) && (getType() == 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isVisible()
  {
    boolean bool;
    if ((mountFlags & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isVisibleForRead(int paramInt)
  {
    return isVisibleForUser(paramInt);
  }
  
  public boolean isVisibleForUser(int paramInt)
  {
    if ((type == 0) && (mountUserId == paramInt)) {
      return isVisible();
    }
    if (type == 2) {
      return isVisible();
    }
    return false;
  }
  
  public boolean isVisibleForWrite(int paramInt)
  {
    return isVisibleForUser(paramInt);
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
    paramParcel.writeInt(type);
    if (disk != null)
    {
      paramParcel.writeInt(1);
      disk.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeString(partGuid);
    paramParcel.writeInt(mountFlags);
    paramParcel.writeInt(mountUserId);
    paramParcel.writeInt(state);
    paramParcel.writeString(fsType);
    paramParcel.writeString(fsUuid);
    paramParcel.writeString(fsLabel);
    paramParcel.writeString(path);
    paramParcel.writeString(internalPath);
  }
}
