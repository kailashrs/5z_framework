package android.app.usage;

import java.util.ArrayList;

public class EventList
{
  private final ArrayList<UsageEvents.Event> mEvents = new ArrayList();
  
  public EventList() {}
  
  public void clear()
  {
    mEvents.clear();
  }
  
  public int firstIndexOnOrAfter(long paramLong)
  {
    int i = mEvents.size();
    int j = i;
    int k = 0;
    i--;
    while (k <= i)
    {
      int m = k + i >>> 1;
      if (mEvents.get(m)).mTimeStamp >= paramLong)
      {
        i = m - 1;
        j = m;
      }
      else
      {
        k = m + 1;
      }
    }
    return j;
  }
  
  public UsageEvents.Event get(int paramInt)
  {
    return (UsageEvents.Event)mEvents.get(paramInt);
  }
  
  public void insert(UsageEvents.Event paramEvent)
  {
    int i = mEvents.size();
    if ((i != 0) && (mTimeStamp < mEvents.get(i - 1)).mTimeStamp))
    {
      i = firstIndexOnOrAfter(mTimeStamp + 1L);
      mEvents.add(i, paramEvent);
      return;
    }
    mEvents.add(paramEvent);
  }
  
  public int size()
  {
    return mEvents.size();
  }
}
