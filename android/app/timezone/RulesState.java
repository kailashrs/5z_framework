package android.app.timezone;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class RulesState
  implements Parcelable
{
  private static final byte BYTE_FALSE = 0;
  private static final byte BYTE_TRUE = 1;
  public static final Parcelable.Creator<RulesState> CREATOR = new Parcelable.Creator()
  {
    public RulesState createFromParcel(Parcel paramAnonymousParcel)
    {
      return RulesState.createFromParcel(paramAnonymousParcel);
    }
    
    public RulesState[] newArray(int paramAnonymousInt)
    {
      return new RulesState[paramAnonymousInt];
    }
  };
  public static final int DISTRO_STATUS_INSTALLED = 2;
  public static final int DISTRO_STATUS_NONE = 1;
  public static final int DISTRO_STATUS_UNKNOWN = 0;
  public static final int STAGED_OPERATION_INSTALL = 3;
  public static final int STAGED_OPERATION_NONE = 1;
  public static final int STAGED_OPERATION_UNINSTALL = 2;
  public static final int STAGED_OPERATION_UNKNOWN = 0;
  private final DistroFormatVersion mDistroFormatVersionSupported;
  private final int mDistroStatus;
  private final DistroRulesVersion mInstalledDistroRulesVersion;
  private final boolean mOperationInProgress;
  private final DistroRulesVersion mStagedDistroRulesVersion;
  private final int mStagedOperationType;
  private final String mSystemRulesVersion;
  
  public RulesState(String paramString, DistroFormatVersion paramDistroFormatVersion, boolean paramBoolean, int paramInt1, DistroRulesVersion paramDistroRulesVersion1, int paramInt2, DistroRulesVersion paramDistroRulesVersion2)
  {
    mSystemRulesVersion = Utils.validateRulesVersion("systemRulesVersion", paramString);
    mDistroFormatVersionSupported = ((DistroFormatVersion)Utils.validateNotNull("distroFormatVersionSupported", paramDistroFormatVersion));
    mOperationInProgress = paramBoolean;
    if ((paramBoolean) && (paramInt1 != 0)) {
      throw new IllegalArgumentException("stagedOperationType != STAGED_OPERATION_UNKNOWN");
    }
    mStagedOperationType = validateStagedOperation(paramInt1);
    paramInt1 = mStagedOperationType;
    boolean bool = false;
    if (paramInt1 == 3) {
      paramBoolean = true;
    } else {
      paramBoolean = false;
    }
    mStagedDistroRulesVersion = ((DistroRulesVersion)Utils.validateConditionalNull(paramBoolean, "stagedDistroRulesVersion", paramDistroRulesVersion1));
    mDistroStatus = validateDistroStatus(paramInt2);
    paramBoolean = bool;
    if (mDistroStatus == 2) {
      paramBoolean = true;
    }
    mInstalledDistroRulesVersion = ((DistroRulesVersion)Utils.validateConditionalNull(paramBoolean, "installedDistroRulesVersion", paramDistroRulesVersion2));
  }
  
  private static RulesState createFromParcel(Parcel paramParcel)
  {
    String str = paramParcel.readString();
    DistroFormatVersion localDistroFormatVersion = (DistroFormatVersion)paramParcel.readParcelable(null);
    boolean bool;
    if (paramParcel.readByte() == 1) {
      bool = true;
    } else {
      bool = false;
    }
    return new RulesState(str, localDistroFormatVersion, bool, paramParcel.readByte(), (DistroRulesVersion)paramParcel.readParcelable(null), paramParcel.readByte(), (DistroRulesVersion)paramParcel.readParcelable(null));
  }
  
  private static int validateDistroStatus(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 2)) {
      return paramInt;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unknown distro status=");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private static int validateStagedOperation(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 3)) {
      return paramInt;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unknown operation type=");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (RulesState)paramObject;
      if (mOperationInProgress != mOperationInProgress) {
        return false;
      }
      if (mStagedOperationType != mStagedOperationType) {
        return false;
      }
      if (mDistroStatus != mDistroStatus) {
        return false;
      }
      if (!mSystemRulesVersion.equals(mSystemRulesVersion)) {
        return false;
      }
      if (!mDistroFormatVersionSupported.equals(mDistroFormatVersionSupported)) {
        return false;
      }
      if (mStagedDistroRulesVersion != null ? !mStagedDistroRulesVersion.equals(mStagedDistroRulesVersion) : mStagedDistroRulesVersion != null) {
        return false;
      }
      if (mInstalledDistroRulesVersion != null) {
        bool = mInstalledDistroRulesVersion.equals(mInstalledDistroRulesVersion);
      } else if (mInstalledDistroRulesVersion != null) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public int getDistroStatus()
  {
    return mDistroStatus;
  }
  
  public DistroRulesVersion getInstalledDistroRulesVersion()
  {
    return mInstalledDistroRulesVersion;
  }
  
  public DistroRulesVersion getStagedDistroRulesVersion()
  {
    return mStagedDistroRulesVersion;
  }
  
  public int getStagedOperationType()
  {
    return mStagedOperationType;
  }
  
  public String getSystemRulesVersion()
  {
    return mSystemRulesVersion;
  }
  
  public int hashCode()
  {
    int i = mSystemRulesVersion.hashCode();
    int j = mDistroFormatVersionSupported.hashCode();
    int k = mOperationInProgress;
    int m = mStagedOperationType;
    DistroRulesVersion localDistroRulesVersion = mStagedDistroRulesVersion;
    int n = 0;
    int i1;
    if (localDistroRulesVersion != null) {
      i1 = mStagedDistroRulesVersion.hashCode();
    } else {
      i1 = 0;
    }
    int i2 = mDistroStatus;
    if (mInstalledDistroRulesVersion != null) {
      n = mInstalledDistroRulesVersion.hashCode();
    }
    return 31 * (31 * (31 * (31 * (31 * (31 * i + j) + k) + m) + i1) + i2) + n;
  }
  
  public boolean isDistroFormatVersionSupported(DistroFormatVersion paramDistroFormatVersion)
  {
    return mDistroFormatVersionSupported.supports(paramDistroFormatVersion);
  }
  
  public boolean isOperationInProgress()
  {
    return mOperationInProgress;
  }
  
  public boolean isSystemVersionNewerThan(DistroRulesVersion paramDistroRulesVersion)
  {
    boolean bool;
    if (mSystemRulesVersion.compareTo(paramDistroRulesVersion.getRulesVersion()) > 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("RulesState{mSystemRulesVersion='");
    localStringBuilder.append(mSystemRulesVersion);
    localStringBuilder.append('\'');
    localStringBuilder.append(", mDistroFormatVersionSupported=");
    localStringBuilder.append(mDistroFormatVersionSupported);
    localStringBuilder.append(", mOperationInProgress=");
    localStringBuilder.append(mOperationInProgress);
    localStringBuilder.append(", mStagedOperationType=");
    localStringBuilder.append(mStagedOperationType);
    localStringBuilder.append(", mStagedDistroRulesVersion=");
    localStringBuilder.append(mStagedDistroRulesVersion);
    localStringBuilder.append(", mDistroStatus=");
    localStringBuilder.append(mDistroStatus);
    localStringBuilder.append(", mInstalledDistroRulesVersion=");
    localStringBuilder.append(mInstalledDistroRulesVersion);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mSystemRulesVersion);
    paramParcel.writeParcelable(mDistroFormatVersionSupported, 0);
    paramParcel.writeByte(mOperationInProgress);
    paramParcel.writeByte((byte)mStagedOperationType);
    paramParcel.writeParcelable(mStagedDistroRulesVersion, 0);
    paramParcel.writeByte((byte)mDistroStatus);
    paramParcel.writeParcelable(mInstalledDistroRulesVersion, 0);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  private static @interface DistroStatus {}
  
  @Retention(RetentionPolicy.SOURCE)
  private static @interface StagedOperationType {}
}
