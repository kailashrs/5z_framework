package android.renderscript;

import dalvik.system.CloseGuard;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;

public class BaseObj
{
  final CloseGuard guard = CloseGuard.get();
  private boolean mDestroyed;
  private long mID;
  private String mName;
  RenderScript mRS;
  
  BaseObj(long paramLong, RenderScript paramRenderScript)
  {
    paramRenderScript.validate();
    mRS = paramRenderScript;
    mID = paramLong;
    mDestroyed = false;
  }
  
  private void helpDestroy()
  {
    int i = 0;
    try
    {
      if (!mDestroyed)
      {
        i = 1;
        mDestroyed = true;
      }
      if (i != 0)
      {
        guard.close();
        ReentrantReadWriteLock.ReadLock localReadLock = mRS.mRWLock.readLock();
        localReadLock.lock();
        if ((mRS.isAlive()) && (mID != 0L)) {
          mRS.nObjDestroy(mID);
        }
        localReadLock.unlock();
        mRS = null;
        mID = 0L;
      }
      return;
    }
    finally {}
  }
  
  void checkValid()
  {
    if (mID != 0L) {
      return;
    }
    throw new RSIllegalArgumentException("Invalid object.");
  }
  
  public void destroy()
  {
    if (!mDestroyed)
    {
      helpDestroy();
      return;
    }
    throw new RSInvalidStateException("Object already destroyed.");
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if (getClass() != paramObject.getClass()) {
      return false;
    }
    paramObject = (BaseObj)paramObject;
    if (mID != mID) {
      bool = false;
    }
    return bool;
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (guard != null) {
        guard.warnIfOpen();
      }
      helpDestroy();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  long getID(RenderScript paramRenderScript)
  {
    mRS.validate();
    if (!mDestroyed)
    {
      if (mID != 0L)
      {
        if ((paramRenderScript != null) && (paramRenderScript != mRS)) {
          throw new RSInvalidStateException("using object with mismatched context.");
        }
        return mID;
      }
      throw new RSRuntimeException("Internal error: Object id 0.");
    }
    throw new RSInvalidStateException("using a destroyed object.");
  }
  
  public String getName()
  {
    return mName;
  }
  
  public int hashCode()
  {
    return (int)(mID & 0xFFFFFFF ^ mID >> 32);
  }
  
  void setID(long paramLong)
  {
    if (mID == 0L)
    {
      mID = paramLong;
      return;
    }
    throw new RSRuntimeException("Internal Error, reset of object ID.");
  }
  
  public void setName(String paramString)
  {
    if (paramString != null)
    {
      if (paramString.length() >= 1)
      {
        if (mName == null) {
          try
          {
            byte[] arrayOfByte = paramString.getBytes("UTF-8");
            mRS.nAssignName(mID, arrayOfByte);
            mName = paramString;
            return;
          }
          catch (UnsupportedEncodingException paramString)
          {
            throw new RuntimeException(paramString);
          }
        }
        throw new RSIllegalArgumentException("setName object already has a name.");
      }
      throw new RSIllegalArgumentException("setName does not accept a zero length string.");
    }
    throw new RSIllegalArgumentException("setName requires a string of non-zero length.");
  }
  
  void updateFromNative()
  {
    mRS.validate();
    mName = mRS.nGetName(getID(mRS));
  }
}
