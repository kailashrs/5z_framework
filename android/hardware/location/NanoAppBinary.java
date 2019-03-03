package android.hardware.location;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

@SystemApi
public final class NanoAppBinary
  implements Parcelable
{
  public static final Parcelable.Creator<NanoAppBinary> CREATOR = new Parcelable.Creator()
  {
    public NanoAppBinary createFromParcel(Parcel paramAnonymousParcel)
    {
      return new NanoAppBinary(paramAnonymousParcel, null);
    }
    
    public NanoAppBinary[] newArray(int paramAnonymousInt)
    {
      return new NanoAppBinary[paramAnonymousInt];
    }
  };
  private static final int EXPECTED_HEADER_VERSION = 1;
  private static final int EXPECTED_MAGIC_VALUE = 1330528590;
  private static final ByteOrder HEADER_ORDER = ByteOrder.LITTLE_ENDIAN;
  private static final int HEADER_SIZE_BYTES = 40;
  private static final int NANOAPP_ENCRYPTED_FLAG_BIT = 2;
  private static final int NANOAPP_SIGNED_FLAG_BIT = 1;
  private static final String TAG = "NanoAppBinary";
  private int mFlags;
  private boolean mHasValidHeader = false;
  private int mHeaderVersion;
  private long mHwHubType;
  private int mMagic;
  private byte[] mNanoAppBinary;
  private long mNanoAppId;
  private int mNanoAppVersion;
  private byte mTargetChreApiMajorVersion;
  private byte mTargetChreApiMinorVersion;
  
  private NanoAppBinary(Parcel paramParcel)
  {
    mNanoAppBinary = new byte[paramParcel.readInt()];
    paramParcel.readByteArray(mNanoAppBinary);
    parseBinaryHeader();
  }
  
  public NanoAppBinary(byte[] paramArrayOfByte)
  {
    mNanoAppBinary = paramArrayOfByte;
    parseBinaryHeader();
  }
  
  private void parseBinaryHeader()
  {
    Object localObject = ByteBuffer.wrap(mNanoAppBinary).order(HEADER_ORDER);
    mHasValidHeader = false;
    try
    {
      mHeaderVersion = ((ByteBuffer)localObject).getInt();
      if (mHeaderVersion != 1)
      {
        localObject = new java/lang/StringBuilder;
        ((StringBuilder)localObject).<init>();
        ((StringBuilder)localObject).append("Unexpected header version ");
        ((StringBuilder)localObject).append(mHeaderVersion);
        ((StringBuilder)localObject).append(" while parsing header (expected ");
        ((StringBuilder)localObject).append(1);
        ((StringBuilder)localObject).append(")");
        Log.e("NanoAppBinary", ((StringBuilder)localObject).toString());
        return;
      }
      mMagic = ((ByteBuffer)localObject).getInt();
      mNanoAppId = ((ByteBuffer)localObject).getLong();
      mNanoAppVersion = ((ByteBuffer)localObject).getInt();
      mFlags = ((ByteBuffer)localObject).getInt();
      mHwHubType = ((ByteBuffer)localObject).getLong();
      mTargetChreApiMajorVersion = ((ByteBuffer)localObject).get();
      mTargetChreApiMinorVersion = ((ByteBuffer)localObject).get();
      if (mMagic != 1330528590)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Unexpected magic value ");
        ((StringBuilder)localObject).append(String.format("0x%08X", new Object[] { Integer.valueOf(mMagic) }));
        ((StringBuilder)localObject).append("while parsing header (expected ");
        ((StringBuilder)localObject).append(String.format("0x%08X", new Object[] { Integer.valueOf(1330528590) }));
        ((StringBuilder)localObject).append(")");
        Log.e("NanoAppBinary", ((StringBuilder)localObject).toString());
      }
      else
      {
        mHasValidHeader = true;
      }
      return;
    }
    catch (BufferUnderflowException localBufferUnderflowException)
    {
      Log.e("NanoAppBinary", "Not enough contents in nanoapp header");
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public byte[] getBinary()
  {
    return mNanoAppBinary;
  }
  
  public byte[] getBinaryNoHeader()
  {
    if (mNanoAppBinary.length >= 40) {
      return Arrays.copyOfRange(mNanoAppBinary, 40, mNanoAppBinary.length);
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("NanoAppBinary binary byte size (");
    localStringBuilder.append(mNanoAppBinary.length);
    localStringBuilder.append(") is less than header size (");
    localStringBuilder.append(40);
    localStringBuilder.append(")");
    throw new IndexOutOfBoundsException(localStringBuilder.toString());
  }
  
  public int getFlags()
  {
    return mFlags;
  }
  
  public int getHeaderVersion()
  {
    return mHeaderVersion;
  }
  
  public long getHwHubType()
  {
    return mHwHubType;
  }
  
  public long getNanoAppId()
  {
    return mNanoAppId;
  }
  
  public int getNanoAppVersion()
  {
    return mNanoAppVersion;
  }
  
  public byte getTargetChreApiMajorVersion()
  {
    return mTargetChreApiMajorVersion;
  }
  
  public byte getTargetChreApiMinorVersion()
  {
    return mTargetChreApiMinorVersion;
  }
  
  public boolean hasValidHeader()
  {
    return mHasValidHeader;
  }
  
  public boolean isEncrypted()
  {
    boolean bool;
    if ((mFlags & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isSigned()
  {
    int i = mFlags;
    boolean bool = true;
    if ((i & 0x1) == 0) {
      bool = false;
    }
    return bool;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mNanoAppBinary.length);
    paramParcel.writeByteArray(mNanoAppBinary);
  }
}
