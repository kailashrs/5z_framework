package android.os.storage;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.UserHandle;
import com.android.internal.util.IndentingPrintWriter;
import com.android.internal.util.Preconditions;
import java.io.CharArrayWriter;
import java.io.File;

public final class StorageVolume
  implements Parcelable
{
  private static final String ACTION_OPEN_EXTERNAL_DIRECTORY = "android.os.storage.action.OPEN_EXTERNAL_DIRECTORY";
  public static final Parcelable.Creator<StorageVolume> CREATOR = new Parcelable.Creator()
  {
    public StorageVolume createFromParcel(Parcel paramAnonymousParcel)
    {
      return new StorageVolume(paramAnonymousParcel, null);
    }
    
    public StorageVolume[] newArray(int paramAnonymousInt)
    {
      return new StorageVolume[paramAnonymousInt];
    }
  };
  public static final String EXTRA_DIRECTORY_NAME = "android.os.storage.extra.DIRECTORY_NAME";
  public static final String EXTRA_STORAGE_VOLUME = "android.os.storage.extra.STORAGE_VOLUME";
  public static final int STORAGE_ID_INVALID = 0;
  public static final int STORAGE_ID_PRIMARY = 65537;
  private final boolean mAllowMassStorage;
  private final String mDescription;
  private final boolean mEmulated;
  private final String mFsUuid;
  private final String mId;
  private final File mInternalPath;
  private final long mMaxFileSize;
  private final UserHandle mOwner;
  private final File mPath;
  private final boolean mPrimary;
  private final boolean mRemovable;
  private final String mState;
  
  private StorageVolume(Parcel paramParcel)
  {
    mId = paramParcel.readString();
    mPath = new File(paramParcel.readString());
    mInternalPath = new File(paramParcel.readString());
    mDescription = paramParcel.readString();
    int i = paramParcel.readInt();
    boolean bool1 = false;
    if (i != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mPrimary = bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mRemovable = bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mEmulated = bool2;
    boolean bool2 = bool1;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    }
    mAllowMassStorage = bool2;
    mMaxFileSize = paramParcel.readLong();
    mOwner = ((UserHandle)paramParcel.readParcelable(null));
    mFsUuid = paramParcel.readString();
    mState = paramParcel.readString();
  }
  
  public StorageVolume(String paramString1, File paramFile1, File paramFile2, String paramString2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, long paramLong, UserHandle paramUserHandle, String paramString3, String paramString4)
  {
    mId = ((String)Preconditions.checkNotNull(paramString1));
    mPath = ((File)Preconditions.checkNotNull(paramFile1));
    mInternalPath = ((File)Preconditions.checkNotNull(paramFile2));
    mDescription = ((String)Preconditions.checkNotNull(paramString2));
    mPrimary = paramBoolean1;
    mRemovable = paramBoolean2;
    mEmulated = paramBoolean3;
    mAllowMassStorage = paramBoolean4;
    mMaxFileSize = paramLong;
    mOwner = ((UserHandle)Preconditions.checkNotNull(paramUserHandle));
    mFsUuid = paramString3;
    mState = ((String)Preconditions.checkNotNull(paramString4));
  }
  
  public boolean allowMassStorage()
  {
    return mAllowMassStorage;
  }
  
  public Intent createAccessIntent(String paramString)
  {
    if (((isPrimary()) && (paramString == null)) || ((paramString != null) && (!Environment.isStandardDirectory(paramString)))) {
      return null;
    }
    Intent localIntent = new Intent("android.os.storage.action.OPEN_EXTERNAL_DIRECTORY");
    localIntent.putExtra("android.os.storage.extra.STORAGE_VOLUME", this);
    localIntent.putExtra("android.os.storage.extra.DIRECTORY_NAME", paramString);
    return localIntent;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String dump()
  {
    CharArrayWriter localCharArrayWriter = new CharArrayWriter();
    dump(new IndentingPrintWriter(localCharArrayWriter, "    ", 80));
    return localCharArrayWriter.toString();
  }
  
  public void dump(IndentingPrintWriter paramIndentingPrintWriter)
  {
    paramIndentingPrintWriter.println("StorageVolume:");
    paramIndentingPrintWriter.increaseIndent();
    paramIndentingPrintWriter.printPair("mId", mId);
    paramIndentingPrintWriter.printPair("mPath", mPath);
    paramIndentingPrintWriter.printPair("mInternalPath", mInternalPath);
    paramIndentingPrintWriter.printPair("mDescription", mDescription);
    paramIndentingPrintWriter.printPair("mPrimary", Boolean.valueOf(mPrimary));
    paramIndentingPrintWriter.printPair("mRemovable", Boolean.valueOf(mRemovable));
    paramIndentingPrintWriter.printPair("mEmulated", Boolean.valueOf(mEmulated));
    paramIndentingPrintWriter.printPair("mAllowMassStorage", Boolean.valueOf(mAllowMassStorage));
    paramIndentingPrintWriter.printPair("mMaxFileSize", Long.valueOf(mMaxFileSize));
    paramIndentingPrintWriter.printPair("mOwner", mOwner);
    paramIndentingPrintWriter.printPair("mFsUuid", mFsUuid);
    paramIndentingPrintWriter.printPair("mState", mState);
    paramIndentingPrintWriter.decreaseIndent();
  }
  
  public boolean equals(Object paramObject)
  {
    if (((paramObject instanceof StorageVolume)) && (mPath != null))
    {
      paramObject = (StorageVolume)paramObject;
      return mPath.equals(mPath);
    }
    return false;
  }
  
  public String getDescription(Context paramContext)
  {
    return mDescription;
  }
  
  public int getFatVolumeId()
  {
    if ((mFsUuid != null) && (mFsUuid.length() == 9)) {
      try
      {
        long l = Long.parseLong(mFsUuid.replace("-", ""), 16);
        return (int)l;
      }
      catch (NumberFormatException localNumberFormatException)
      {
        return -1;
      }
    }
    return -1;
  }
  
  public String getId()
  {
    return mId;
  }
  
  public String getInternalPath()
  {
    return mInternalPath.toString();
  }
  
  public long getMaxFileSize()
  {
    return mMaxFileSize;
  }
  
  public UserHandle getOwner()
  {
    return mOwner;
  }
  
  public String getPath()
  {
    return mPath.toString();
  }
  
  public File getPathFile()
  {
    return mPath;
  }
  
  public String getState()
  {
    return mState;
  }
  
  public String getUserLabel()
  {
    return mDescription;
  }
  
  public String getUuid()
  {
    return mFsUuid;
  }
  
  public int hashCode()
  {
    return mPath.hashCode();
  }
  
  public boolean isEmulated()
  {
    return mEmulated;
  }
  
  public boolean isPrimary()
  {
    return mPrimary;
  }
  
  public boolean isRemovable()
  {
    return mRemovable;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("StorageVolume: ").append(mDescription);
    if (mFsUuid != null)
    {
      localStringBuilder.append(" (");
      localStringBuilder.append(mFsUuid);
      localStringBuilder.append(")");
    }
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mId);
    paramParcel.writeString(mPath.toString());
    paramParcel.writeString(mInternalPath.toString());
    paramParcel.writeString(mDescription);
    paramParcel.writeInt(mPrimary);
    paramParcel.writeInt(mRemovable);
    paramParcel.writeInt(mEmulated);
    paramParcel.writeInt(mAllowMassStorage);
    paramParcel.writeLong(mMaxFileSize);
    paramParcel.writeParcelable(mOwner, paramInt);
    paramParcel.writeString(mFsUuid);
    paramParcel.writeString(mState);
  }
  
  public static final class ScopedAccessProviderContract
  {
    public static final String AUTHORITY = "com.android.documentsui.scopedAccess";
    public static final String COL_DIRECTORY = "directory";
    public static final String COL_GRANTED = "granted";
    public static final String COL_PACKAGE = "package_name";
    public static final String COL_VOLUME_UUID = "volume_uuid";
    public static final String TABLE_PACKAGES = "packages";
    public static final String[] TABLE_PACKAGES_COLUMNS = { "package_name" };
    public static final int TABLE_PACKAGES_COL_PACKAGE = 0;
    public static final String TABLE_PERMISSIONS = "permissions";
    public static final String[] TABLE_PERMISSIONS_COLUMNS = { "package_name", "volume_uuid", "directory", "granted" };
    public static final int TABLE_PERMISSIONS_COL_DIRECTORY = 2;
    public static final int TABLE_PERMISSIONS_COL_GRANTED = 3;
    public static final int TABLE_PERMISSIONS_COL_PACKAGE = 0;
    public static final int TABLE_PERMISSIONS_COL_VOLUME_UUID = 1;
    
    private ScopedAccessProviderContract()
    {
      throw new UnsupportedOperationException("contains constants only");
    }
  }
}
