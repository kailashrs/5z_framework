package android.database.sqlite;

import java.io.Closeable;

public abstract class SQLiteClosable
  implements Closeable
{
  private int mReferenceCount = 1;
  
  public SQLiteClosable() {}
  
  public void acquireReference()
  {
    try
    {
      if (mReferenceCount > 0)
      {
        mReferenceCount += 1;
        return;
      }
      IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("attempt to re-open an already-closed object: ");
      localStringBuilder.append(this);
      localIllegalStateException.<init>(localStringBuilder.toString());
      throw localIllegalStateException;
    }
    finally {}
  }
  
  public void close()
  {
    releaseReference();
  }
  
  protected abstract void onAllReferencesReleased();
  
  @Deprecated
  protected void onAllReferencesReleasedFromContainer()
  {
    onAllReferencesReleased();
  }
  
  public void releaseReference()
  {
    try
    {
      int i = mReferenceCount;
      int j = 1;
      i--;
      mReferenceCount = i;
      if (i != 0) {
        j = 0;
      }
      if (j != 0) {
        onAllReferencesReleased();
      }
      return;
    }
    finally {}
  }
  
  @Deprecated
  public void releaseReferenceFromContainer()
  {
    try
    {
      int i = mReferenceCount;
      int j = 1;
      i--;
      mReferenceCount = i;
      if (i != 0) {
        j = 0;
      }
      if (j != 0) {
        onAllReferencesReleasedFromContainer();
      }
      return;
    }
    finally {}
  }
}
