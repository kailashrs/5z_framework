package android.util;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import dalvik.system.CloseGuard;
import java.io.Closeable;
import java.io.IOException;
import java.util.UUID;
import libcore.io.IoUtils;

public final class MemoryIntArray
  implements Parcelable, Closeable
{
  public static final Parcelable.Creator<MemoryIntArray> CREATOR = new Parcelable.Creator()
  {
    public MemoryIntArray createFromParcel(Parcel paramAnonymousParcel)
    {
      try
      {
        paramAnonymousParcel = new MemoryIntArray(paramAnonymousParcel, null);
        return paramAnonymousParcel;
      }
      catch (IOException paramAnonymousParcel)
      {
        throw new IllegalArgumentException("Error unparceling MemoryIntArray");
      }
    }
    
    public MemoryIntArray[] newArray(int paramAnonymousInt)
    {
      return new MemoryIntArray[paramAnonymousInt];
    }
  };
  private static final int MAX_SIZE = 1024;
  private static final String TAG = "MemoryIntArray";
  private final CloseGuard mCloseGuard = CloseGuard.get();
  private int mFd = -1;
  private final boolean mIsOwner;
  private final long mMemoryAddr;
  
  public MemoryIntArray(int paramInt)
    throws IOException
  {
    if (paramInt <= 1024)
    {
      mIsOwner = true;
      mFd = nativeCreate(UUID.randomUUID().toString(), paramInt);
      mMemoryAddr = nativeOpen(mFd, mIsOwner);
      mCloseGuard.open("close");
      return;
    }
    throw new IllegalArgumentException("Max size is 1024");
  }
  
  private MemoryIntArray(Parcel paramParcel)
    throws IOException
  {
    mIsOwner = false;
    paramParcel = (ParcelFileDescriptor)paramParcel.readParcelable(null);
    if (paramParcel != null)
    {
      mFd = paramParcel.detachFd();
      mMemoryAddr = nativeOpen(mFd, mIsOwner);
      mCloseGuard.open("close");
      return;
    }
    throw new IOException("No backing file descriptor");
  }
  
  private void enforceNotClosed()
  {
    if (!isClosed()) {
      return;
    }
    throw new IllegalStateException("cannot interact with a closed instance");
  }
  
  private void enforceValidIndex(int paramInt)
    throws IOException
  {
    int i = size();
    if ((paramInt >= 0) && (paramInt <= i - 1)) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramInt);
    localStringBuilder.append(" not between 0 and ");
    localStringBuilder.append(i - 1);
    throw new IndexOutOfBoundsException(localStringBuilder.toString());
  }
  
  private void enforceWritable()
  {
    if (isWritable()) {
      return;
    }
    throw new UnsupportedOperationException("array is not writable");
  }
  
  public static int getMaxSize()
  {
    return 1024;
  }
  
  private native void nativeClose(int paramInt, long paramLong, boolean paramBoolean);
  
  private native int nativeCreate(String paramString, int paramInt);
  
  private native int nativeGet(int paramInt1, long paramLong, int paramInt2);
  
  private native long nativeOpen(int paramInt, boolean paramBoolean);
  
  private native void nativeSet(int paramInt1, long paramLong, int paramInt2, int paramInt3);
  
  private native int nativeSize(int paramInt);
  
  public void close()
    throws IOException
  {
    if (!isClosed())
    {
      nativeClose(mFd, mMemoryAddr, mIsOwner);
      mFd = -1;
      mCloseGuard.close();
    }
  }
  
  public int describeContents()
  {
    return 1;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = false;
    if (paramObject == null) {
      return false;
    }
    if (this == paramObject) {
      return true;
    }
    if (getClass() != paramObject.getClass()) {
      return false;
    }
    paramObject = (MemoryIntArray)paramObject;
    if (mFd == mFd) {
      bool = true;
    }
    return bool;
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mCloseGuard != null) {
        mCloseGuard.warnIfOpen();
      }
      IoUtils.closeQuietly(this);
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public int get(int paramInt)
    throws IOException
  {
    enforceNotClosed();
    enforceValidIndex(paramInt);
    return nativeGet(mFd, mMemoryAddr, paramInt);
  }
  
  public int hashCode()
  {
    return mFd;
  }
  
  public boolean isClosed()
  {
    boolean bool;
    if (mFd == -1) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isWritable()
  {
    enforceNotClosed();
    return mIsOwner;
  }
  
  public void set(int paramInt1, int paramInt2)
    throws IOException
  {
    enforceNotClosed();
    enforceWritable();
    enforceValidIndex(paramInt1);
    nativeSet(mFd, mMemoryAddr, paramInt1, paramInt2);
  }
  
  public int size()
    throws IOException
  {
    enforceNotClosed();
    return nativeSize(mFd);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    ParcelFileDescriptor localParcelFileDescriptor = ParcelFileDescriptor.adoptFd(mFd);
    try
    {
      paramParcel.writeParcelable(localParcelFileDescriptor, paramInt & 0xFFFFFFFE);
      return;
    }
    finally
    {
      localParcelFileDescriptor.detachFd();
    }
  }
}
