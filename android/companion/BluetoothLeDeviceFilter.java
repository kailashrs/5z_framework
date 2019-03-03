package android.companion;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.provider.OneTimeUseBuilder;
import android.text.TextUtils;
import com.android.internal.util.BitUtils;
import com.android.internal.util.ObjectUtils;
import com.android.internal.util.Preconditions;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

public final class BluetoothLeDeviceFilter
  implements DeviceFilter<ScanResult>
{
  public static final Parcelable.Creator<BluetoothLeDeviceFilter> CREATOR = new Parcelable.Creator()
  {
    public BluetoothLeDeviceFilter createFromParcel(Parcel paramAnonymousParcel)
    {
      BluetoothLeDeviceFilter.Builder localBuilder = new BluetoothLeDeviceFilter.Builder().setNamePattern(BluetoothDeviceFilterUtils.patternFromString(paramAnonymousParcel.readString())).setScanFilter((ScanFilter)paramAnonymousParcel.readParcelable(null));
      Object localObject1 = paramAnonymousParcel.createByteArray();
      Object localObject2 = paramAnonymousParcel.createByteArray();
      if (localObject1 != null) {
        localBuilder.setRawDataFilter((byte[])localObject1, (byte[])localObject2);
      }
      localObject1 = paramAnonymousParcel.readString();
      localObject2 = paramAnonymousParcel.readString();
      int i = paramAnonymousParcel.readInt();
      int j = paramAnonymousParcel.readInt();
      int k = paramAnonymousParcel.readInt();
      int m = paramAnonymousParcel.readInt();
      boolean bool = paramAnonymousParcel.readBoolean();
      if (localObject1 != null) {
        if (i >= 0)
        {
          if (bool) {}
          for (paramAnonymousParcel = ByteOrder.LITTLE_ENDIAN;; paramAnonymousParcel = ByteOrder.BIG_ENDIAN) {
            break;
          }
          localBuilder.setRenameFromBytes((String)localObject1, (String)localObject2, i, j, paramAnonymousParcel);
        }
        else
        {
          localBuilder.setRenameFromName((String)localObject1, (String)localObject2, k, m);
        }
      }
      return localBuilder.build();
    }
    
    public BluetoothLeDeviceFilter[] newArray(int paramAnonymousInt)
    {
      return new BluetoothLeDeviceFilter[paramAnonymousInt];
    }
  };
  private static final boolean DEBUG = false;
  private static final String LOG_TAG = "BluetoothLeDeviceFilter";
  private static final int RENAME_PREFIX_LENGTH_LIMIT = 10;
  private final Pattern mNamePattern;
  private final byte[] mRawDataFilter;
  private final byte[] mRawDataFilterMask;
  private final int mRenameBytesFrom;
  private final int mRenameBytesLength;
  private final boolean mRenameBytesReverseOrder;
  private final int mRenameNameFrom;
  private final int mRenameNameLength;
  private final String mRenamePrefix;
  private final String mRenameSuffix;
  private final ScanFilter mScanFilter;
  
  private BluetoothLeDeviceFilter(Pattern paramPattern, ScanFilter paramScanFilter, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
  {
    mNamePattern = paramPattern;
    mScanFilter = ((ScanFilter)ObjectUtils.firstNotNull(paramScanFilter, ScanFilter.EMPTY));
    mRawDataFilter = paramArrayOfByte1;
    mRawDataFilterMask = paramArrayOfByte2;
    mRenamePrefix = paramString1;
    mRenameSuffix = paramString2;
    mRenameBytesFrom = paramInt1;
    mRenameBytesLength = paramInt2;
    mRenameNameFrom = paramInt3;
    mRenameNameLength = paramInt4;
    mRenameBytesReverseOrder = paramBoolean;
  }
  
  public static int getRenamePrefixLengthLimit()
  {
    return 10;
  }
  
  private boolean matches(BluetoothDevice paramBluetoothDevice)
  {
    boolean bool;
    if ((BluetoothDeviceFilterUtils.matches(getScanFilter(), paramBluetoothDevice)) && (BluetoothDeviceFilterUtils.matchesName(getNamePattern(), paramBluetoothDevice))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
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
      paramObject = (BluetoothLeDeviceFilter)paramObject;
      if ((mRenameBytesFrom != mRenameBytesFrom) || (mRenameBytesLength != mRenameBytesLength) || (mRenameNameFrom != mRenameNameFrom) || (mRenameNameLength != mRenameNameLength) || (mRenameBytesReverseOrder != mRenameBytesReverseOrder) || (!Objects.equals(mNamePattern, mNamePattern)) || (!Objects.equals(mScanFilter, mScanFilter)) || (!Arrays.equals(mRawDataFilter, mRawDataFilter)) || (!Arrays.equals(mRawDataFilterMask, mRawDataFilterMask)) || (!Objects.equals(mRenamePrefix, mRenamePrefix)) || (!Objects.equals(mRenameSuffix, mRenameSuffix))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public String getDeviceDisplayName(ScanResult paramScanResult)
  {
    if ((mRenameBytesFrom < 0) && (mRenameNameFrom < 0)) {
      return BluetoothDeviceFilterUtils.getDeviceDisplayNameInternal(paramScanResult.getDevice());
    }
    StringBuilder localStringBuilder = new StringBuilder(TextUtils.emptyIfNull(mRenamePrefix));
    if (mRenameBytesFrom >= 0)
    {
      paramScanResult = paramScanResult.getScanRecord().getBytes();
      int i = mRenameBytesFrom;
      int j = mRenameBytesFrom + mRenameBytesLength - 1;
      int k;
      if (mRenameBytesReverseOrder) {
        k = j;
      } else {
        k = i;
      }
      int m;
      if (mRenameBytesReverseOrder) {
        m = -1;
      } else {
        m = 1;
      }
      while ((i <= k) && (k <= j))
      {
        localStringBuilder.append(Byte.toHexString(paramScanResult[k], true));
        k += m;
      }
    }
    else
    {
      localStringBuilder.append(BluetoothDeviceFilterUtils.getDeviceDisplayNameInternal(paramScanResult.getDevice()).substring(mRenameNameFrom, mRenameNameFrom + mRenameNameLength));
    }
    localStringBuilder.append(TextUtils.emptyIfNull(mRenameSuffix));
    return localStringBuilder.toString();
  }
  
  public int getMediumType()
  {
    return 1;
  }
  
  public Pattern getNamePattern()
  {
    return mNamePattern;
  }
  
  public byte[] getRawDataFilter()
  {
    return mRawDataFilter;
  }
  
  public byte[] getRawDataFilterMask()
  {
    return mRawDataFilterMask;
  }
  
  public int getRenameBytesFrom()
  {
    return mRenameBytesFrom;
  }
  
  public int getRenameBytesLength()
  {
    return mRenameBytesLength;
  }
  
  public String getRenamePrefix()
  {
    return mRenamePrefix;
  }
  
  public String getRenameSuffix()
  {
    return mRenameSuffix;
  }
  
  public ScanFilter getScanFilter()
  {
    return mScanFilter;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { mNamePattern, mScanFilter, mRawDataFilter, mRawDataFilterMask, mRenamePrefix, mRenameSuffix, Integer.valueOf(mRenameBytesFrom), Integer.valueOf(mRenameBytesLength), Integer.valueOf(mRenameNameFrom), Integer.valueOf(mRenameNameLength), Boolean.valueOf(mRenameBytesReverseOrder) });
  }
  
  public boolean isRenameBytesReverseOrder()
  {
    return mRenameBytesReverseOrder;
  }
  
  public boolean matches(ScanResult paramScanResult)
  {
    boolean bool;
    if ((matches(paramScanResult.getDevice())) && ((mRawDataFilter == null) || (BitUtils.maskedEquals(paramScanResult.getScanRecord().getBytes(), mRawDataFilter, mRawDataFilterMask)))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("BluetoothLEDeviceFilter{mNamePattern=");
    localStringBuilder.append(mNamePattern);
    localStringBuilder.append(", mScanFilter=");
    localStringBuilder.append(mScanFilter);
    localStringBuilder.append(", mRawDataFilter=");
    localStringBuilder.append(Arrays.toString(mRawDataFilter));
    localStringBuilder.append(", mRawDataFilterMask=");
    localStringBuilder.append(Arrays.toString(mRawDataFilterMask));
    localStringBuilder.append(", mRenamePrefix='");
    localStringBuilder.append(mRenamePrefix);
    localStringBuilder.append('\'');
    localStringBuilder.append(", mRenameSuffix='");
    localStringBuilder.append(mRenameSuffix);
    localStringBuilder.append('\'');
    localStringBuilder.append(", mRenameBytesFrom=");
    localStringBuilder.append(mRenameBytesFrom);
    localStringBuilder.append(", mRenameBytesLength=");
    localStringBuilder.append(mRenameBytesLength);
    localStringBuilder.append(", mRenameNameFrom=");
    localStringBuilder.append(mRenameNameFrom);
    localStringBuilder.append(", mRenameNameLength=");
    localStringBuilder.append(mRenameNameLength);
    localStringBuilder.append(", mRenameBytesReverseOrder=");
    localStringBuilder.append(mRenameBytesReverseOrder);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(BluetoothDeviceFilterUtils.patternToString(getNamePattern()));
    paramParcel.writeParcelable(mScanFilter, paramInt);
    paramParcel.writeByteArray(mRawDataFilter);
    paramParcel.writeByteArray(mRawDataFilterMask);
    paramParcel.writeString(mRenamePrefix);
    paramParcel.writeString(mRenameSuffix);
    paramParcel.writeInt(mRenameBytesFrom);
    paramParcel.writeInt(mRenameBytesLength);
    paramParcel.writeInt(mRenameNameFrom);
    paramParcel.writeInt(mRenameNameLength);
    paramParcel.writeBoolean(mRenameBytesReverseOrder);
  }
  
  public static final class Builder
    extends OneTimeUseBuilder<BluetoothLeDeviceFilter>
  {
    private Pattern mNamePattern;
    private byte[] mRawDataFilter;
    private byte[] mRawDataFilterMask;
    private int mRenameBytesFrom = -1;
    private int mRenameBytesLength;
    private boolean mRenameBytesReverseOrder = false;
    private int mRenameNameFrom = -1;
    private int mRenameNameLength;
    private String mRenamePrefix;
    private String mRenameSuffix;
    private ScanFilter mScanFilter;
    
    public Builder() {}
    
    private void checkRangeNotEmpty(int paramInt)
    {
      boolean bool;
      if (paramInt > 0) {
        bool = true;
      } else {
        bool = false;
      }
      Preconditions.checkArgument(bool, "Range must be non-empty");
    }
    
    private void checkRenameNotSet()
    {
      boolean bool;
      if (mRenamePrefix == null) {
        bool = true;
      } else {
        bool = false;
      }
      Preconditions.checkState(bool, "Renaming rule can only be set once");
    }
    
    private Builder setRename(String paramString1, String paramString2)
    {
      checkNotUsed();
      boolean bool;
      if (TextUtils.length(paramString1) <= BluetoothLeDeviceFilter.getRenamePrefixLengthLimit()) {
        bool = true;
      } else {
        bool = false;
      }
      Preconditions.checkArgument(bool, "Prefix is too long");
      mRenamePrefix = paramString1;
      mRenameSuffix = paramString2;
      return this;
    }
    
    public BluetoothLeDeviceFilter build()
    {
      markUsed();
      return new BluetoothLeDeviceFilter(mNamePattern, mScanFilter, mRawDataFilter, mRawDataFilterMask, mRenamePrefix, mRenameSuffix, mRenameBytesFrom, mRenameBytesLength, mRenameNameFrom, mRenameNameLength, mRenameBytesReverseOrder, null);
    }
    
    public Builder setNamePattern(Pattern paramPattern)
    {
      checkNotUsed();
      mNamePattern = paramPattern;
      return this;
    }
    
    public Builder setRawDataFilter(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    {
      checkNotUsed();
      Preconditions.checkNotNull(paramArrayOfByte1);
      boolean bool;
      if ((paramArrayOfByte2 != null) && (paramArrayOfByte1.length != paramArrayOfByte2.length)) {
        bool = false;
      } else {
        bool = true;
      }
      Preconditions.checkArgument(bool, "Mask and filter should be the same length");
      mRawDataFilter = paramArrayOfByte1;
      mRawDataFilterMask = paramArrayOfByte2;
      return this;
    }
    
    public Builder setRenameFromBytes(String paramString1, String paramString2, int paramInt1, int paramInt2, ByteOrder paramByteOrder)
    {
      checkRenameNotSet();
      checkRangeNotEmpty(paramInt2);
      mRenameBytesFrom = paramInt1;
      mRenameBytesLength = paramInt2;
      boolean bool;
      if (paramByteOrder == ByteOrder.LITTLE_ENDIAN) {
        bool = true;
      } else {
        bool = false;
      }
      mRenameBytesReverseOrder = bool;
      return setRename(paramString1, paramString2);
    }
    
    public Builder setRenameFromName(String paramString1, String paramString2, int paramInt1, int paramInt2)
    {
      checkRenameNotSet();
      checkRangeNotEmpty(paramInt2);
      mRenameNameFrom = paramInt1;
      mRenameNameLength = paramInt2;
      mRenameBytesReverseOrder = false;
      return setRename(paramString1, paramString2);
    }
    
    public Builder setScanFilter(ScanFilter paramScanFilter)
    {
      checkNotUsed();
      mScanFilter = paramScanFilter;
      return this;
    }
  }
}
