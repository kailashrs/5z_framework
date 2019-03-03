package android.os;

import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import dalvik.system.VMRuntime;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.nio.ByteBuffer;
import java.nio.DirectByteBuffer;
import java.nio.NioUtils;
import sun.misc.Cleaner;

public final class SharedMemory
  implements Parcelable, Closeable
{
  public static final Parcelable.Creator<SharedMemory> CREATOR = new Parcelable.Creator()
  {
    public SharedMemory createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SharedMemory(paramAnonymousParcel.readRawFileDescriptor(), null);
    }
    
    public SharedMemory[] newArray(int paramAnonymousInt)
    {
      return new SharedMemory[paramAnonymousInt];
    }
  };
  private static final int PROT_MASK = OsConstants.PROT_READ | OsConstants.PROT_WRITE | OsConstants.PROT_EXEC | OsConstants.PROT_NONE;
  private Cleaner mCleaner;
  private final FileDescriptor mFileDescriptor;
  private final MemoryRegistration mMemoryRegistration;
  private final int mSize;
  
  private SharedMemory(FileDescriptor paramFileDescriptor)
  {
    if (paramFileDescriptor != null)
    {
      if (paramFileDescriptor.valid())
      {
        mFileDescriptor = paramFileDescriptor;
        mSize = nGetSize(mFileDescriptor);
        if (mSize > 0)
        {
          mMemoryRegistration = new MemoryRegistration(mSize, null);
          mCleaner = Cleaner.create(mFileDescriptor, new Closer(mFileDescriptor, mMemoryRegistration, null));
          return;
        }
        throw new IllegalArgumentException("FileDescriptor is not a valid ashmem fd");
      }
      throw new IllegalArgumentException("Unable to create SharedMemory from closed FileDescriptor");
    }
    throw new IllegalArgumentException("Unable to create SharedMemory from a null FileDescriptor");
  }
  
  private void checkOpen()
  {
    if (mFileDescriptor.valid()) {
      return;
    }
    throw new IllegalStateException("SharedMemory is closed");
  }
  
  public static SharedMemory create(String paramString, int paramInt)
    throws ErrnoException
  {
    if (paramInt > 0) {
      return new SharedMemory(nCreate(paramString, paramInt));
    }
    throw new IllegalArgumentException("Size must be greater than zero");
  }
  
  private static native FileDescriptor nCreate(String paramString, int paramInt)
    throws ErrnoException;
  
  private static native int nGetSize(FileDescriptor paramFileDescriptor);
  
  private static native int nSetProt(FileDescriptor paramFileDescriptor, int paramInt);
  
  public static void unmap(ByteBuffer paramByteBuffer)
  {
    if ((paramByteBuffer instanceof DirectByteBuffer))
    {
      NioUtils.freeDirectBuffer(paramByteBuffer);
      return;
    }
    throw new IllegalArgumentException("ByteBuffer wasn't created by #map(int, int, int); can't unmap");
  }
  
  private static void validateProt(int paramInt)
  {
    if ((PROT_MASK & paramInt) == 0) {
      return;
    }
    throw new IllegalArgumentException("Invalid prot value");
  }
  
  public void close()
  {
    if (mCleaner != null)
    {
      mCleaner.clean();
      mCleaner = null;
    }
  }
  
  public int describeContents()
  {
    return 1;
  }
  
  public int getFd()
  {
    return mFileDescriptor.getInt$();
  }
  
  public FileDescriptor getFileDescriptor()
  {
    return mFileDescriptor;
  }
  
  public int getSize()
  {
    checkOpen();
    return mSize;
  }
  
  public ByteBuffer map(int paramInt1, int paramInt2, int paramInt3)
    throws ErrnoException
  {
    checkOpen();
    validateProt(paramInt1);
    if (paramInt2 >= 0)
    {
      if (paramInt3 > 0)
      {
        if (paramInt2 + paramInt3 <= mSize)
        {
          long l = Os.mmap(0L, paramInt3, paramInt1, OsConstants.MAP_SHARED, mFileDescriptor, paramInt2);
          if ((paramInt1 & OsConstants.PROT_WRITE) == 0) {}
          for (boolean bool = true;; bool = false) {
            break;
          }
          Unmapper localUnmapper = new Unmapper(l, paramInt3, mMemoryRegistration.acquire(), null);
          return new DirectByteBuffer(paramInt3, l, mFileDescriptor, localUnmapper, bool);
        }
        throw new IllegalArgumentException("offset + length must not exceed getSize()");
      }
      throw new IllegalArgumentException("Length must be > 0");
    }
    throw new IllegalArgumentException("Offset must be >= 0");
  }
  
  public ByteBuffer mapReadOnly()
    throws ErrnoException
  {
    return map(OsConstants.PROT_READ, 0, mSize);
  }
  
  public ByteBuffer mapReadWrite()
    throws ErrnoException
  {
    return map(OsConstants.PROT_READ | OsConstants.PROT_WRITE, 0, mSize);
  }
  
  public boolean setProtect(int paramInt)
  {
    checkOpen();
    validateProt(paramInt);
    boolean bool;
    if (nSetProt(mFileDescriptor, paramInt) == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    checkOpen();
    paramParcel.writeFileDescriptor(mFileDescriptor);
  }
  
  private static final class Closer
    implements Runnable
  {
    private FileDescriptor mFd;
    private SharedMemory.MemoryRegistration mMemoryReference;
    
    private Closer(FileDescriptor paramFileDescriptor, SharedMemory.MemoryRegistration paramMemoryRegistration)
    {
      mFd = paramFileDescriptor;
      mMemoryReference = paramMemoryRegistration;
    }
    
    public void run()
    {
      try
      {
        Os.close(mFd);
      }
      catch (ErrnoException localErrnoException) {}
      mMemoryReference.release();
      mMemoryReference = null;
    }
  }
  
  private static final class MemoryRegistration
  {
    private int mReferenceCount;
    private int mSize;
    
    private MemoryRegistration(int paramInt)
    {
      mSize = paramInt;
      mReferenceCount = 1;
      VMRuntime.getRuntime().registerNativeAllocation(mSize);
    }
    
    public MemoryRegistration acquire()
    {
      try
      {
        mReferenceCount += 1;
        return this;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
    
    public void release()
    {
      try
      {
        mReferenceCount -= 1;
        if (mReferenceCount == 0) {
          VMRuntime.getRuntime().registerNativeFree(mSize);
        }
        return;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
  }
  
  private static final class Unmapper
    implements Runnable
  {
    private long mAddress;
    private SharedMemory.MemoryRegistration mMemoryReference;
    private int mSize;
    
    private Unmapper(long paramLong, int paramInt, SharedMemory.MemoryRegistration paramMemoryRegistration)
    {
      mAddress = paramLong;
      mSize = paramInt;
      mMemoryReference = paramMemoryRegistration;
    }
    
    public void run()
    {
      try
      {
        Os.munmap(mAddress, mSize);
      }
      catch (ErrnoException localErrnoException) {}
      mMemoryReference.release();
      mMemoryReference = null;
    }
  }
}
