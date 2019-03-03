package android.filterfw.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class CachedFrameManager
  extends SimpleFrameManager
{
  private SortedMap<Integer, Frame> mAvailableFrames = new TreeMap();
  private int mStorageCapacity = 25165824;
  private int mStorageSize = 0;
  private int mTimeStamp = 0;
  
  public CachedFrameManager() {}
  
  private void dropOldestFrame()
  {
    int i = ((Integer)mAvailableFrames.firstKey()).intValue();
    Frame localFrame = (Frame)mAvailableFrames.get(Integer.valueOf(i));
    mStorageSize -= localFrame.getFormat().getSize();
    localFrame.releaseNativeAllocation();
    mAvailableFrames.remove(Integer.valueOf(i));
  }
  
  private Frame findAvailableFrame(FrameFormat paramFrameFormat, int paramInt, long paramLong)
  {
    synchronized (mAvailableFrames)
    {
      Iterator localIterator = mAvailableFrames.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        Frame localFrame = (Frame)localEntry.getValue();
        if ((localFrame.getFormat().isReplaceableBy(paramFrameFormat)) && (paramInt == localFrame.getBindingType()) && ((paramInt == 0) || (paramLong == localFrame.getBindingId())))
        {
          super.retainFrame(localFrame);
          mAvailableFrames.remove(localEntry.getKey());
          localFrame.onFrameFetch();
          localFrame.reset(paramFrameFormat);
          mStorageSize -= paramFrameFormat.getSize();
          return localFrame;
        }
      }
      return null;
    }
  }
  
  private boolean storeFrame(Frame paramFrame)
  {
    synchronized (mAvailableFrames)
    {
      int i = paramFrame.getFormat().getSize();
      if (i > mStorageCapacity) {
        return false;
      }
      for (int j = mStorageSize + i; j > mStorageCapacity; j = mStorageSize + i) {
        dropOldestFrame();
      }
      paramFrame.onFrameStore();
      mStorageSize = j;
      mAvailableFrames.put(Integer.valueOf(mTimeStamp), paramFrame);
      mTimeStamp += 1;
      return true;
    }
  }
  
  public void clearCache()
  {
    Iterator localIterator = mAvailableFrames.values().iterator();
    while (localIterator.hasNext()) {
      ((Frame)localIterator.next()).releaseNativeAllocation();
    }
    mAvailableFrames.clear();
  }
  
  public Frame newBoundFrame(FrameFormat paramFrameFormat, int paramInt, long paramLong)
  {
    Frame localFrame1 = findAvailableFrame(paramFrameFormat, paramInt, paramLong);
    Frame localFrame2 = localFrame1;
    if (localFrame1 == null) {
      localFrame2 = super.newBoundFrame(paramFrameFormat, paramInt, paramLong);
    }
    localFrame2.setTimestamp(-2L);
    return localFrame2;
  }
  
  public Frame newFrame(FrameFormat paramFrameFormat)
  {
    Frame localFrame1 = findAvailableFrame(paramFrameFormat, 0, 0L);
    Frame localFrame2 = localFrame1;
    if (localFrame1 == null) {
      localFrame2 = super.newFrame(paramFrameFormat);
    }
    localFrame2.setTimestamp(-2L);
    return localFrame2;
  }
  
  public Frame releaseFrame(Frame paramFrame)
  {
    if (paramFrame.isReusable())
    {
      int i = paramFrame.decRefCount();
      if ((i == 0) && (paramFrame.hasNativeAllocation()))
      {
        if (!storeFrame(paramFrame)) {
          paramFrame.releaseNativeAllocation();
        }
        return null;
      }
      if (i < 0) {
        throw new RuntimeException("Frame reference count dropped below 0!");
      }
    }
    else
    {
      super.releaseFrame(paramFrame);
    }
    return paramFrame;
  }
  
  public Frame retainFrame(Frame paramFrame)
  {
    return super.retainFrame(paramFrame);
  }
  
  public void tearDown()
  {
    clearCache();
  }
}
