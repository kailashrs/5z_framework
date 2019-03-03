package android.content.res;

import android.os.Bundle;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.ParcelFileDescriptor.AutoCloseInputStream;
import android.os.ParcelFileDescriptor.AutoCloseOutputStream;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class AssetFileDescriptor
  implements Parcelable, Closeable
{
  public static final Parcelable.Creator<AssetFileDescriptor> CREATOR = new Parcelable.Creator()
  {
    public AssetFileDescriptor createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AssetFileDescriptor(paramAnonymousParcel);
    }
    
    public AssetFileDescriptor[] newArray(int paramAnonymousInt)
    {
      return new AssetFileDescriptor[paramAnonymousInt];
    }
  };
  public static final long UNKNOWN_LENGTH = -1L;
  private final Bundle mExtras;
  private final ParcelFileDescriptor mFd;
  private final long mLength;
  private final long mStartOffset;
  
  AssetFileDescriptor(Parcel paramParcel)
  {
    mFd = ((ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel));
    mStartOffset = paramParcel.readLong();
    mLength = paramParcel.readLong();
    if (paramParcel.readInt() != 0) {
      mExtras = paramParcel.readBundle();
    } else {
      mExtras = null;
    }
  }
  
  public AssetFileDescriptor(ParcelFileDescriptor paramParcelFileDescriptor, long paramLong1, long paramLong2)
  {
    this(paramParcelFileDescriptor, paramLong1, paramLong2, null);
  }
  
  public AssetFileDescriptor(ParcelFileDescriptor paramParcelFileDescriptor, long paramLong1, long paramLong2, Bundle paramBundle)
  {
    if (paramParcelFileDescriptor != null)
    {
      if ((paramLong2 < 0L) && (paramLong1 != 0L)) {
        throw new IllegalArgumentException("startOffset must be 0 when using UNKNOWN_LENGTH");
      }
      mFd = paramParcelFileDescriptor;
      mStartOffset = paramLong1;
      mLength = paramLong2;
      mExtras = paramBundle;
      return;
    }
    throw new IllegalArgumentException("fd must not be null");
  }
  
  public void close()
    throws IOException
  {
    mFd.close();
  }
  
  public FileInputStream createInputStream()
    throws IOException
  {
    if (mLength < 0L) {
      return new ParcelFileDescriptor.AutoCloseInputStream(mFd);
    }
    return new AutoCloseInputStream(this);
  }
  
  public FileOutputStream createOutputStream()
    throws IOException
  {
    if (mLength < 0L) {
      return new ParcelFileDescriptor.AutoCloseOutputStream(mFd);
    }
    return new AutoCloseOutputStream(this);
  }
  
  public int describeContents()
  {
    return mFd.describeContents();
  }
  
  public long getDeclaredLength()
  {
    return mLength;
  }
  
  public Bundle getExtras()
  {
    return mExtras;
  }
  
  public FileDescriptor getFileDescriptor()
  {
    return mFd.getFileDescriptor();
  }
  
  public long getLength()
  {
    if (mLength >= 0L) {
      return mLength;
    }
    long l = mFd.getStatSize();
    if (l < 0L) {
      l = -1L;
    }
    return l;
  }
  
  public ParcelFileDescriptor getParcelFileDescriptor()
  {
    return mFd;
  }
  
  public long getStartOffset()
  {
    return mStartOffset;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{AssetFileDescriptor: ");
    localStringBuilder.append(mFd);
    localStringBuilder.append(" start=");
    localStringBuilder.append(mStartOffset);
    localStringBuilder.append(" len=");
    localStringBuilder.append(mLength);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    mFd.writeToParcel(paramParcel, paramInt);
    paramParcel.writeLong(mStartOffset);
    paramParcel.writeLong(mLength);
    if (mExtras != null)
    {
      paramParcel.writeInt(1);
      paramParcel.writeBundle(mExtras);
    }
    else
    {
      paramParcel.writeInt(0);
    }
  }
  
  public static class AutoCloseInputStream
    extends ParcelFileDescriptor.AutoCloseInputStream
  {
    private long mRemaining;
    
    public AutoCloseInputStream(AssetFileDescriptor paramAssetFileDescriptor)
      throws IOException
    {
      super();
      super.skip(paramAssetFileDescriptor.getStartOffset());
      mRemaining = ((int)paramAssetFileDescriptor.getLength());
    }
    
    public int available()
      throws IOException
    {
      int i;
      if (mRemaining >= 0L)
      {
        if (mRemaining < 2147483647L) {
          i = (int)mRemaining;
        } else {
          i = Integer.MAX_VALUE;
        }
      }
      else {
        i = super.available();
      }
      return i;
    }
    
    public void mark(int paramInt)
    {
      if (mRemaining >= 0L) {
        return;
      }
      super.mark(paramInt);
    }
    
    public boolean markSupported()
    {
      if (mRemaining >= 0L) {
        return false;
      }
      return super.markSupported();
    }
    
    public int read()
      throws IOException
    {
      byte[] arrayOfByte = new byte[1];
      int i = read(arrayOfByte, 0, 1);
      int j = -1;
      if (i != -1) {
        j = arrayOfByte[0] & 0xFF;
      }
      return j;
    }
    
    public int read(byte[] paramArrayOfByte)
      throws IOException
    {
      return read(paramArrayOfByte, 0, paramArrayOfByte.length);
    }
    
    public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      if (mRemaining >= 0L)
      {
        if (mRemaining == 0L) {
          return -1;
        }
        int i = paramInt2;
        if (paramInt2 > mRemaining) {
          i = (int)mRemaining;
        }
        paramInt1 = super.read(paramArrayOfByte, paramInt1, i);
        if (paramInt1 >= 0) {
          mRemaining -= paramInt1;
        }
        return paramInt1;
      }
      return super.read(paramArrayOfByte, paramInt1, paramInt2);
    }
    
    public void reset()
      throws IOException
    {
      try
      {
        long l = mRemaining;
        if (l >= 0L) {
          return;
        }
        super.reset();
        return;
      }
      finally {}
    }
    
    public long skip(long paramLong)
      throws IOException
    {
      if (mRemaining >= 0L)
      {
        if (mRemaining == 0L) {
          return -1L;
        }
        long l = paramLong;
        if (paramLong > mRemaining) {
          l = mRemaining;
        }
        paramLong = super.skip(l);
        if (paramLong >= 0L) {
          mRemaining -= paramLong;
        }
        return paramLong;
      }
      return super.skip(paramLong);
    }
  }
  
  public static class AutoCloseOutputStream
    extends ParcelFileDescriptor.AutoCloseOutputStream
  {
    private long mRemaining;
    
    public AutoCloseOutputStream(AssetFileDescriptor paramAssetFileDescriptor)
      throws IOException
    {
      super();
      if (paramAssetFileDescriptor.getParcelFileDescriptor().seekTo(paramAssetFileDescriptor.getStartOffset()) >= 0L)
      {
        mRemaining = ((int)paramAssetFileDescriptor.getLength());
        return;
      }
      throw new IOException("Unable to seek");
    }
    
    public void write(int paramInt)
      throws IOException
    {
      if (mRemaining >= 0L)
      {
        if (mRemaining == 0L) {
          return;
        }
        super.write(paramInt);
        mRemaining -= 1L;
        return;
      }
      super.write(paramInt);
    }
    
    public void write(byte[] paramArrayOfByte)
      throws IOException
    {
      if (mRemaining >= 0L)
      {
        if (mRemaining == 0L) {
          return;
        }
        int i = paramArrayOfByte.length;
        int j = i;
        if (i > mRemaining) {
          j = (int)mRemaining;
        }
        super.write(paramArrayOfByte);
        mRemaining -= j;
        return;
      }
      super.write(paramArrayOfByte);
    }
    
    public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      if (mRemaining >= 0L)
      {
        if (mRemaining == 0L) {
          return;
        }
        int i = paramInt2;
        if (paramInt2 > mRemaining) {
          i = (int)mRemaining;
        }
        super.write(paramArrayOfByte, paramInt1, i);
        mRemaining -= i;
        return;
      }
      super.write(paramArrayOfByte, paramInt1, paramInt2);
    }
  }
}
