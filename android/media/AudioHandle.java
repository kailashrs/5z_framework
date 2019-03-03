package android.media;

class AudioHandle
{
  private final int mId;
  
  AudioHandle(int paramInt)
  {
    mId = paramInt;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = false;
    if ((paramObject != null) && ((paramObject instanceof AudioHandle)))
    {
      paramObject = (AudioHandle)paramObject;
      if (mId == paramObject.id()) {
        bool = true;
      }
      return bool;
    }
    return false;
  }
  
  public int hashCode()
  {
    return mId;
  }
  
  int id()
  {
    return mId;
  }
  
  public String toString()
  {
    return Integer.toString(mId);
  }
}
