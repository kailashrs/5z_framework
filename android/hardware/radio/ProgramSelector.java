package android.hardware.radio;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@SystemApi
public final class ProgramSelector
  implements Parcelable
{
  public static final Parcelable.Creator<ProgramSelector> CREATOR = new Parcelable.Creator()
  {
    public ProgramSelector createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ProgramSelector(paramAnonymousParcel, null);
    }
    
    public ProgramSelector[] newArray(int paramAnonymousInt)
    {
      return new ProgramSelector[paramAnonymousInt];
    }
  };
  public static final int IDENTIFIER_TYPE_AMFM_FREQUENCY = 1;
  public static final int IDENTIFIER_TYPE_DAB_ENSEMBLE = 6;
  public static final int IDENTIFIER_TYPE_DAB_FREQUENCY = 8;
  public static final int IDENTIFIER_TYPE_DAB_SCID = 7;
  public static final int IDENTIFIER_TYPE_DAB_SIDECC = 5;
  public static final int IDENTIFIER_TYPE_DAB_SID_EXT = 5;
  public static final int IDENTIFIER_TYPE_DRMO_FREQUENCY = 10;
  @Deprecated
  public static final int IDENTIFIER_TYPE_DRMO_MODULATION = 11;
  public static final int IDENTIFIER_TYPE_DRMO_SERVICE_ID = 9;
  public static final int IDENTIFIER_TYPE_HD_STATION_ID_EXT = 3;
  public static final int IDENTIFIER_TYPE_HD_STATION_NAME = 10004;
  @Deprecated
  public static final int IDENTIFIER_TYPE_HD_SUBCHANNEL = 4;
  public static final int IDENTIFIER_TYPE_INVALID = 0;
  public static final int IDENTIFIER_TYPE_RDS_PI = 2;
  public static final int IDENTIFIER_TYPE_SXM_CHANNEL = 13;
  public static final int IDENTIFIER_TYPE_SXM_SERVICE_ID = 12;
  public static final int IDENTIFIER_TYPE_VENDOR_END = 1999;
  @Deprecated
  public static final int IDENTIFIER_TYPE_VENDOR_PRIMARY_END = 1999;
  @Deprecated
  public static final int IDENTIFIER_TYPE_VENDOR_PRIMARY_START = 1000;
  public static final int IDENTIFIER_TYPE_VENDOR_START = 1000;
  @Deprecated
  public static final int PROGRAM_TYPE_AM = 1;
  @Deprecated
  public static final int PROGRAM_TYPE_AM_HD = 3;
  @Deprecated
  public static final int PROGRAM_TYPE_DAB = 5;
  @Deprecated
  public static final int PROGRAM_TYPE_DRMO = 6;
  @Deprecated
  public static final int PROGRAM_TYPE_FM = 2;
  @Deprecated
  public static final int PROGRAM_TYPE_FM_HD = 4;
  @Deprecated
  public static final int PROGRAM_TYPE_INVALID = 0;
  @Deprecated
  public static final int PROGRAM_TYPE_SXM = 7;
  @Deprecated
  public static final int PROGRAM_TYPE_VENDOR_END = 1999;
  @Deprecated
  public static final int PROGRAM_TYPE_VENDOR_START = 1000;
  private final Identifier mPrimaryId;
  private final int mProgramType;
  private final Identifier[] mSecondaryIds;
  private final long[] mVendorIds;
  
  public ProgramSelector(int paramInt, Identifier paramIdentifier, Identifier[] paramArrayOfIdentifier, long[] paramArrayOfLong)
  {
    Identifier[] arrayOfIdentifier = paramArrayOfIdentifier;
    if (paramArrayOfIdentifier == null) {
      arrayOfIdentifier = new Identifier[0];
    }
    paramArrayOfIdentifier = paramArrayOfLong;
    if (paramArrayOfLong == null) {
      paramArrayOfIdentifier = new long[0];
    }
    if (!Stream.of(arrayOfIdentifier).anyMatch(_..Lambda.ProgramSelector.pP_Cu6h7_REdNveY60TFDS4pIKk.INSTANCE))
    {
      mProgramType = paramInt;
      mPrimaryId = ((Identifier)Objects.requireNonNull(paramIdentifier));
      mSecondaryIds = arrayOfIdentifier;
      mVendorIds = paramArrayOfIdentifier;
      return;
    }
    throw new IllegalArgumentException("secondaryIds list must not contain nulls");
  }
  
  private ProgramSelector(Parcel paramParcel)
  {
    mProgramType = paramParcel.readInt();
    mPrimaryId = ((Identifier)paramParcel.readTypedObject(Identifier.CREATOR));
    mSecondaryIds = ((Identifier[])paramParcel.createTypedArray(Identifier.CREATOR));
    if (!Stream.of(mSecondaryIds).anyMatch(_..Lambda.ProgramSelector.nFx6NE_itx7YUkyrPxAq5zDeJdQ.INSTANCE))
    {
      mVendorIds = paramParcel.createLongArray();
      return;
    }
    throw new IllegalArgumentException("secondaryIds list must not contain nulls");
  }
  
  public static ProgramSelector createAmFmSelector(int paramInt1, int paramInt2)
  {
    return createAmFmSelector(paramInt1, paramInt2, 0);
  }
  
  public static ProgramSelector createAmFmSelector(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = 2;
    int j = paramInt1;
    if (paramInt1 == -1) {
      if (paramInt2 < 50000)
      {
        if (paramInt3 <= 0) {
          paramInt1 = 0;
        } else {
          paramInt1 = 3;
        }
        j = paramInt1;
      }
      else
      {
        if (paramInt3 <= 0) {
          paramInt1 = 1;
        } else {
          paramInt1 = 2;
        }
        j = paramInt1;
      }
    }
    boolean bool;
    if ((j != 0) && (j != 3)) {
      bool = false;
    } else {
      bool = true;
    }
    if ((j != 3) && (j != 2)) {
      paramInt1 = 0;
    } else {
      paramInt1 = 1;
    }
    if ((!bool) && (paramInt1 == 0) && (j != 1))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Unknown band: ");
      ((StringBuilder)localObject).append(j);
      throw new IllegalArgumentException(((StringBuilder)localObject).toString());
    }
    if ((paramInt3 >= 0) && (paramInt3 <= 8))
    {
      if ((paramInt3 > 0) && (paramInt1 == 0)) {
        throw new IllegalArgumentException("Subchannels are not supported for non-HD radio");
      }
      if (isValidAmFmFrequency(bool, paramInt2))
      {
        paramInt1 = i;
        if (bool) {
          paramInt1 = 1;
        }
        Identifier localIdentifier = new Identifier(1, paramInt2);
        localObject = null;
        if (paramInt3 > 0) {
          localObject = new Identifier[] { new Identifier(4, paramInt3 - 1) };
        }
        return new ProgramSelector(paramInt1, localIdentifier, (Identifier[])localObject, null);
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Provided value is not a valid AM/FM frequency: ");
      ((StringBuilder)localObject).append(paramInt2);
      throw new IllegalArgumentException(((StringBuilder)localObject).toString());
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Invalid subchannel: ");
    ((StringBuilder)localObject).append(paramInt3);
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  private static boolean isValidAmFmFrequency(boolean paramBoolean, int paramInt)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    if (paramBoolean)
    {
      paramBoolean = bool2;
      if (paramInt > 150)
      {
        paramBoolean = bool2;
        if (paramInt <= 30000) {
          paramBoolean = true;
        }
      }
      return paramBoolean;
    }
    paramBoolean = bool1;
    if (paramInt > 60000)
    {
      paramBoolean = bool1;
      if (paramInt < 110000) {
        paramBoolean = true;
      }
    }
    return paramBoolean;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof ProgramSelector)) {
      return false;
    }
    paramObject = (ProgramSelector)paramObject;
    return mPrimaryId.equals(paramObject.getPrimaryId());
  }
  
  public Identifier[] getAllIds(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    if (mPrimaryId.getType() == paramInt) {
      localArrayList.add(mPrimaryId);
    }
    for (Identifier localIdentifier : mSecondaryIds) {
      if (localIdentifier.getType() == paramInt) {
        localArrayList.add(localIdentifier);
      }
    }
    return (Identifier[])localArrayList.toArray(new Identifier[localArrayList.size()]);
  }
  
  public long getFirstId(int paramInt)
  {
    if (mPrimaryId.getType() == paramInt) {
      return mPrimaryId.getValue();
    }
    for (localObject : mSecondaryIds) {
      if (((Identifier)localObject).getType() == paramInt) {
        return ((Identifier)localObject).getValue();
      }
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Identifier ");
    ((StringBuilder)localObject).append(paramInt);
    ((StringBuilder)localObject).append(" not found");
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  public Identifier getPrimaryId()
  {
    return mPrimaryId;
  }
  
  @Deprecated
  public int getProgramType()
  {
    return mProgramType;
  }
  
  public Identifier[] getSecondaryIds()
  {
    return mSecondaryIds;
  }
  
  @Deprecated
  public long[] getVendorIds()
  {
    return mVendorIds;
  }
  
  public int hashCode()
  {
    return mPrimaryId.hashCode();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("ProgramSelector(type=");
    localStringBuilder.append(mProgramType);
    localStringBuilder.append(", primary=");
    localStringBuilder = localStringBuilder.append(mPrimaryId);
    if (mSecondaryIds.length > 0)
    {
      localStringBuilder.append(", secondary=");
      localStringBuilder.append(mSecondaryIds);
    }
    if (mVendorIds.length > 0)
    {
      localStringBuilder.append(", vendor=");
      localStringBuilder.append(mVendorIds);
    }
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
  
  public ProgramSelector withSecondaryPreferred(Identifier paramIdentifier)
  {
    int i = paramIdentifier.getType();
    paramIdentifier = (Identifier[])Stream.concat(Arrays.stream(mSecondaryIds).filter(new _..Lambda.ProgramSelector.TWK8H6GGx8Rt5rbA87tKag_pCqw(i)), Stream.of(paramIdentifier)).toArray(_..Lambda.ProgramSelector.kEsOH_p_eN5KvKLjoDTGZXYtuP4.INSTANCE);
    return new ProgramSelector(mProgramType, mPrimaryId, paramIdentifier, mVendorIds);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mProgramType);
    paramParcel.writeTypedObject(mPrimaryId, 0);
    paramParcel.writeTypedArray(mSecondaryIds, 0);
    paramParcel.writeLongArray(mVendorIds);
  }
  
  public static final class Identifier
    implements Parcelable
  {
    public static final Parcelable.Creator<Identifier> CREATOR = new Parcelable.Creator()
    {
      public ProgramSelector.Identifier createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ProgramSelector.Identifier(paramAnonymousParcel, null);
      }
      
      public ProgramSelector.Identifier[] newArray(int paramAnonymousInt)
      {
        return new ProgramSelector.Identifier[paramAnonymousInt];
      }
    };
    private final int mType;
    private final long mValue;
    
    public Identifier(int paramInt, long paramLong)
    {
      int i = paramInt;
      if (paramInt == 10004) {
        i = 4;
      }
      mType = i;
      mValue = paramLong;
    }
    
    private Identifier(Parcel paramParcel)
    {
      mType = paramParcel.readInt();
      mValue = paramParcel.readLong();
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
      if (!(paramObject instanceof Identifier)) {
        return false;
      }
      paramObject = (Identifier)paramObject;
      if ((paramObject.getType() != mType) || (paramObject.getValue() != mValue)) {
        bool = false;
      }
      return bool;
    }
    
    public int getType()
    {
      if ((mType == 4) && (mValue > 10L)) {
        return 10004;
      }
      return mType;
    }
    
    public long getValue()
    {
      return mValue;
    }
    
    public int hashCode()
    {
      return Objects.hash(new Object[] { Integer.valueOf(mType), Long.valueOf(mValue) });
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Identifier(");
      localStringBuilder.append(mType);
      localStringBuilder.append(", ");
      localStringBuilder.append(mValue);
      localStringBuilder.append(")");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(mType);
      paramParcel.writeLong(mValue);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface IdentifierType {}
  
  @Deprecated
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ProgramType {}
}
