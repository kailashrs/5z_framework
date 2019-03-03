package android.content;

public class UndoOwner
{
  Object mData;
  final UndoManager mManager;
  int mOpCount;
  int mSavedIdx;
  int mStateSeq;
  final String mTag;
  
  UndoOwner(String paramString, UndoManager paramUndoManager)
  {
    if (paramString != null)
    {
      if (paramUndoManager != null)
      {
        mTag = paramString;
        mManager = paramUndoManager;
        return;
      }
      throw new NullPointerException("manager can't be null");
    }
    throw new NullPointerException("tag can't be null");
  }
  
  public Object getData()
  {
    return mData;
  }
  
  public String getTag()
  {
    return mTag;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("UndoOwner:[mTag=");
    localStringBuilder.append(mTag);
    localStringBuilder.append(" mManager=");
    localStringBuilder.append(mManager);
    localStringBuilder.append(" mData=");
    localStringBuilder.append(mData);
    localStringBuilder.append(" mData=");
    localStringBuilder.append(mData);
    localStringBuilder.append(" mOpCount=");
    localStringBuilder.append(mOpCount);
    localStringBuilder.append(" mStateSeq=");
    localStringBuilder.append(mStateSeq);
    localStringBuilder.append(" mSavedIdx=");
    localStringBuilder.append(mSavedIdx);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
}
