package android.widget;

import java.util.ArrayList;

class ExpandableListPosition
{
  public static final int CHILD = 1;
  public static final int GROUP = 2;
  private static final int MAX_POOL_SIZE = 5;
  private static ArrayList<ExpandableListPosition> sPool = new ArrayList(5);
  public int childPos;
  int flatListPos;
  public int groupPos;
  public int type;
  
  private ExpandableListPosition() {}
  
  private static ExpandableListPosition getRecycledOrCreate()
  {
    synchronized (sPool)
    {
      if (sPool.size() > 0)
      {
        localExpandableListPosition = (ExpandableListPosition)sPool.remove(0);
        localExpandableListPosition.resetState();
        return localExpandableListPosition;
      }
      ExpandableListPosition localExpandableListPosition = new android/widget/ExpandableListPosition;
      localExpandableListPosition.<init>();
      return localExpandableListPosition;
    }
  }
  
  static ExpandableListPosition obtain(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    ExpandableListPosition localExpandableListPosition = getRecycledOrCreate();
    type = paramInt1;
    groupPos = paramInt2;
    childPos = paramInt3;
    flatListPos = paramInt4;
    return localExpandableListPosition;
  }
  
  static ExpandableListPosition obtainChildPosition(int paramInt1, int paramInt2)
  {
    return obtain(1, paramInt1, paramInt2, 0);
  }
  
  static ExpandableListPosition obtainGroupPosition(int paramInt)
  {
    return obtain(2, paramInt, 0, 0);
  }
  
  static ExpandableListPosition obtainPosition(long paramLong)
  {
    if (paramLong == 4294967295L) {
      return null;
    }
    ExpandableListPosition localExpandableListPosition = getRecycledOrCreate();
    groupPos = ExpandableListView.getPackedPositionGroup(paramLong);
    if (ExpandableListView.getPackedPositionType(paramLong) == 1)
    {
      type = 1;
      childPos = ExpandableListView.getPackedPositionChild(paramLong);
    }
    else
    {
      type = 2;
    }
    return localExpandableListPosition;
  }
  
  private void resetState()
  {
    groupPos = 0;
    childPos = 0;
    flatListPos = 0;
    type = 0;
  }
  
  long getPackedPosition()
  {
    if (type == 1) {
      return ExpandableListView.getPackedPositionForChild(groupPos, childPos);
    }
    return ExpandableListView.getPackedPositionForGroup(groupPos);
  }
  
  public void recycle()
  {
    synchronized (sPool)
    {
      if (sPool.size() < 5) {
        sPool.add(this);
      }
      return;
    }
  }
}
