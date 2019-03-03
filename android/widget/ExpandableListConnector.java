package android.widget;

import android.database.DataSetObserver;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Collections;

class ExpandableListConnector
  extends BaseAdapter
  implements Filterable
{
  private final DataSetObserver mDataSetObserver = new MyDataSetObserver();
  private ArrayList<GroupMetadata> mExpGroupMetadataList = new ArrayList();
  private ExpandableListAdapter mExpandableListAdapter;
  private int mMaxExpGroupCount = Integer.MAX_VALUE;
  private int mTotalExpChildrenCount;
  
  public ExpandableListConnector(ExpandableListAdapter paramExpandableListAdapter)
  {
    setExpandableListAdapter(paramExpandableListAdapter);
  }
  
  private void refreshExpGroupMetadataList(boolean paramBoolean1, boolean paramBoolean2)
  {
    ArrayList localArrayList = mExpGroupMetadataList;
    int i = localArrayList.size();
    int j = 0;
    int k = 0;
    mTotalExpChildrenCount = 0;
    int m = i;
    GroupMetadata localGroupMetadata;
    int i4;
    if (paramBoolean2)
    {
      n = 0;
      i1 = i - 1;
      while (i1 >= 0)
      {
        localGroupMetadata = (GroupMetadata)localArrayList.get(i1);
        int i2 = findGroupPosition(gId, gPos);
        int i3 = i;
        i4 = n;
        if (i2 != gPos)
        {
          m = i;
          if (i2 == -1)
          {
            localArrayList.remove(i1);
            m = i - 1;
          }
          gPos = i2;
          i3 = m;
          i4 = n;
          if (n == 0)
          {
            i4 = 1;
            i3 = m;
          }
        }
        i1--;
        i = i3;
        n = i4;
      }
      m = i;
      if (n != 0)
      {
        Collections.sort(localArrayList);
        m = i;
      }
    }
    int n = 0;
    i = k;
    int i1 = j;
    while (i < m)
    {
      localGroupMetadata = (GroupMetadata)localArrayList.get(i);
      if ((lastChildFlPos != -1) && (!paramBoolean1)) {
        i4 = lastChildFlPos - flPos;
      } else {
        i4 = mExpandableListAdapter.getChildrenCount(gPos);
      }
      mTotalExpChildrenCount += i4;
      i1 += gPos - n;
      n = gPos;
      flPos = i1;
      i1 += i4;
      lastChildFlPos = i1;
      i++;
    }
  }
  
  public boolean areAllItemsEnabled()
  {
    return mExpandableListAdapter.areAllItemsEnabled();
  }
  
  boolean collapseGroup(int paramInt)
  {
    ExpandableListPosition localExpandableListPosition = ExpandableListPosition.obtain(2, paramInt, -1, -1);
    PositionMetadata localPositionMetadata = getFlattenedPos(localExpandableListPosition);
    localExpandableListPosition.recycle();
    if (localPositionMetadata == null) {
      return false;
    }
    boolean bool = collapseGroup(localPositionMetadata);
    localPositionMetadata.recycle();
    return bool;
  }
  
  boolean collapseGroup(PositionMetadata paramPositionMetadata)
  {
    if (groupMetadata == null) {
      return false;
    }
    mExpGroupMetadataList.remove(groupMetadata);
    refreshExpGroupMetadataList(false, false);
    notifyDataSetChanged();
    mExpandableListAdapter.onGroupCollapsed(groupMetadata.gPos);
    return true;
  }
  
  boolean expandGroup(int paramInt)
  {
    ExpandableListPosition localExpandableListPosition = ExpandableListPosition.obtain(2, paramInt, -1, -1);
    PositionMetadata localPositionMetadata = getFlattenedPos(localExpandableListPosition);
    localExpandableListPosition.recycle();
    boolean bool = expandGroup(localPositionMetadata);
    localPositionMetadata.recycle();
    return bool;
  }
  
  boolean expandGroup(PositionMetadata paramPositionMetadata)
  {
    if (position.groupPos >= 0)
    {
      if (mMaxExpGroupCount == 0) {
        return false;
      }
      if (groupMetadata != null) {
        return false;
      }
      if (mExpGroupMetadataList.size() >= mMaxExpGroupCount)
      {
        localGroupMetadata = (GroupMetadata)mExpGroupMetadataList.get(0);
        int i = mExpGroupMetadataList.indexOf(localGroupMetadata);
        collapseGroup(gPos);
        if (groupInsertIndex > i) {
          groupInsertIndex -= 1;
        }
      }
      GroupMetadata localGroupMetadata = GroupMetadata.obtain(-1, -1, position.groupPos, mExpandableListAdapter.getGroupId(position.groupPos));
      mExpGroupMetadataList.add(groupInsertIndex, localGroupMetadata);
      refreshExpGroupMetadataList(false, false);
      notifyDataSetChanged();
      mExpandableListAdapter.onGroupExpanded(gPos);
      return true;
    }
    throw new RuntimeException("Need group");
  }
  
  int findGroupPosition(long paramLong, int paramInt)
  {
    int i = mExpandableListAdapter.getGroupCount();
    if (i == 0) {
      return -1;
    }
    if (paramLong == Long.MIN_VALUE) {
      return -1;
    }
    paramInt = Math.min(i - 1, Math.max(0, paramInt));
    long l = SystemClock.uptimeMillis();
    int j = paramInt;
    int k = paramInt;
    int m = 0;
    ExpandableListAdapter localExpandableListAdapter = getAdapter();
    if (localExpandableListAdapter == null) {
      return -1;
    }
    while (SystemClock.uptimeMillis() <= l + 100L)
    {
      if (localExpandableListAdapter.getGroupId(paramInt) == paramLong) {
        return paramInt;
      }
      int n = 1;
      int i1;
      if (k == i - 1) {
        i1 = 1;
      } else {
        i1 = 0;
      }
      if (j != 0) {
        n = 0;
      }
      if ((i1 != 0) && (n != 0)) {
        break;
      }
      if ((n == 0) && ((m == 0) || (i1 != 0)))
      {
        if ((i1 != 0) || ((m == 0) && (n == 0)))
        {
          j--;
          paramInt = j;
          m = 1;
        }
      }
      else
      {
        k++;
        paramInt = k;
        m = 0;
      }
    }
    return -1;
  }
  
  ExpandableListAdapter getAdapter()
  {
    return mExpandableListAdapter;
  }
  
  public int getCount()
  {
    return mExpandableListAdapter.getGroupCount() + mTotalExpChildrenCount;
  }
  
  ArrayList<GroupMetadata> getExpandedGroupMetadataList()
  {
    return mExpGroupMetadataList;
  }
  
  public Filter getFilter()
  {
    ExpandableListAdapter localExpandableListAdapter = getAdapter();
    if ((localExpandableListAdapter instanceof Filterable)) {
      return ((Filterable)localExpandableListAdapter).getFilter();
    }
    return null;
  }
  
  PositionMetadata getFlattenedPos(ExpandableListPosition paramExpandableListPosition)
  {
    Object localObject = mExpGroupMetadataList;
    int i = ((ArrayList)localObject).size();
    int j = 0;
    int k = i - 1;
    int m = 0;
    if (i == 0) {
      return PositionMetadata.obtain(groupPos, type, groupPos, childPos, null, 0);
    }
    while (j <= k)
    {
      i = (k - j) / 2 + j;
      GroupMetadata localGroupMetadata = (GroupMetadata)((ArrayList)localObject).get(i);
      if (groupPos > gPos)
      {
        j = i + 1;
        m = i;
      }
      else if (groupPos < gPos)
      {
        k = i - 1;
        m = i;
      }
      else
      {
        m = i;
        if (groupPos == gPos)
        {
          if (type == 2) {
            return PositionMetadata.obtain(flPos, type, groupPos, childPos, localGroupMetadata, i);
          }
          if (type == 1) {
            return PositionMetadata.obtain(flPos + childPos + 1, type, groupPos, childPos, localGroupMetadata, i);
          }
          return null;
        }
      }
    }
    if (type != 2) {
      return null;
    }
    if (j > m)
    {
      localObject = (GroupMetadata)((ArrayList)localObject).get(j - 1);
      return PositionMetadata.obtain(lastChildFlPos + (groupPos - gPos), type, groupPos, childPos, null, j);
    }
    if (k < m)
    {
      j = k + 1;
      localObject = (GroupMetadata)((ArrayList)localObject).get(j);
      return PositionMetadata.obtain(flPos - (gPos - groupPos), type, groupPos, childPos, null, j);
    }
    return null;
  }
  
  public Object getItem(int paramInt)
  {
    PositionMetadata localPositionMetadata = getUnflattenedPos(paramInt);
    if (position.type == 2) {}
    for (Object localObject = mExpandableListAdapter.getGroup(position.groupPos);; localObject = mExpandableListAdapter.getChild(position.groupPos, position.childPos))
    {
      break;
      if (position.type != 1) {
        break label81;
      }
    }
    localPositionMetadata.recycle();
    return localObject;
    label81:
    throw new RuntimeException("Flat list position is of unknown type");
  }
  
  public long getItemId(int paramInt)
  {
    PositionMetadata localPositionMetadata = getUnflattenedPos(paramInt);
    long l1 = mExpandableListAdapter.getGroupId(position.groupPos);
    long l2;
    if (position.type == 2)
    {
      l2 = mExpandableListAdapter.getCombinedGroupId(l1);
    }
    else
    {
      if (position.type != 1) {
        break label106;
      }
      l2 = mExpandableListAdapter.getChildId(position.groupPos, position.childPos);
      l2 = mExpandableListAdapter.getCombinedChildId(l1, l2);
    }
    localPositionMetadata.recycle();
    return l2;
    label106:
    throw new RuntimeException("Flat list position is of unknown type");
  }
  
  public int getItemViewType(int paramInt)
  {
    PositionMetadata localPositionMetadata = getUnflattenedPos(paramInt);
    ExpandableListPosition localExpandableListPosition = position;
    if ((mExpandableListAdapter instanceof HeterogeneousExpandableList))
    {
      HeterogeneousExpandableList localHeterogeneousExpandableList = (HeterogeneousExpandableList)mExpandableListAdapter;
      if (type == 2) {
        paramInt = localHeterogeneousExpandableList.getGroupType(groupPos);
      } else {
        paramInt = localHeterogeneousExpandableList.getChildType(groupPos, childPos) + localHeterogeneousExpandableList.getGroupTypeCount();
      }
    }
    else if (type == 2)
    {
      paramInt = 0;
    }
    else
    {
      paramInt = 1;
    }
    localPositionMetadata.recycle();
    return paramInt;
  }
  
  PositionMetadata getUnflattenedPos(int paramInt)
  {
    Object localObject = mExpGroupMetadataList;
    int i = ((ArrayList)localObject).size();
    int j = 0;
    int k = i - 1;
    int m = 0;
    if (i == 0) {
      return PositionMetadata.obtain(paramInt, 2, paramInt, -1, null, 0);
    }
    while (j <= k)
    {
      i = (k - j) / 2 + j;
      GroupMetadata localGroupMetadata = (GroupMetadata)((ArrayList)localObject).get(i);
      if (paramInt > lastChildFlPos)
      {
        j = i + 1;
        m = i;
      }
      else if (paramInt < flPos)
      {
        k = i - 1;
        m = i;
      }
      else
      {
        if (paramInt == flPos) {
          return PositionMetadata.obtain(paramInt, 2, gPos, -1, localGroupMetadata, i);
        }
        m = i;
        if (paramInt <= lastChildFlPos)
        {
          k = flPos;
          return PositionMetadata.obtain(paramInt, 1, gPos, paramInt - (k + 1), localGroupMetadata, i);
        }
      }
    }
    if (j > m) {
      localObject = (GroupMetadata)((ArrayList)localObject).get(j - 1);
    }
    for (m = paramInt - lastChildFlPos + gPos;; m = gPos - (flPos - paramInt))
    {
      break;
      if (k >= m) {
        break label253;
      }
      k++;
      localObject = (GroupMetadata)((ArrayList)localObject).get(k);
      j = k;
    }
    return PositionMetadata.obtain(paramInt, 2, m, -1, null, j);
    label253:
    throw new RuntimeException("Unknown state");
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    PositionMetadata localPositionMetadata = getUnflattenedPos(paramInt);
    if (position.type == 2)
    {
      paramView = mExpandableListAdapter.getGroupView(position.groupPos, localPositionMetadata.isExpanded(), paramView, paramViewGroup);
    }
    else
    {
      int i = position.type;
      boolean bool = true;
      if (i != 1) {
        break label124;
      }
      if (groupMetadata.lastChildFlPos != paramInt) {
        for (;;)
        {
          bool = false;
        }
      }
      paramView = mExpandableListAdapter.getChildView(position.groupPos, position.childPos, bool, paramView, paramViewGroup);
    }
    localPositionMetadata.recycle();
    return paramView;
    label124:
    throw new RuntimeException("Flat list position is of unknown type");
  }
  
  public int getViewTypeCount()
  {
    if ((mExpandableListAdapter instanceof HeterogeneousExpandableList))
    {
      HeterogeneousExpandableList localHeterogeneousExpandableList = (HeterogeneousExpandableList)mExpandableListAdapter;
      return localHeterogeneousExpandableList.getGroupTypeCount() + localHeterogeneousExpandableList.getChildTypeCount();
    }
    return 2;
  }
  
  public boolean hasStableIds()
  {
    return mExpandableListAdapter.hasStableIds();
  }
  
  public boolean isEmpty()
  {
    ExpandableListAdapter localExpandableListAdapter = getAdapter();
    boolean bool;
    if (localExpandableListAdapter != null) {
      bool = localExpandableListAdapter.isEmpty();
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isEnabled(int paramInt)
  {
    PositionMetadata localPositionMetadata = getUnflattenedPos(paramInt);
    ExpandableListPosition localExpandableListPosition = position;
    paramInt = type;
    boolean bool = true;
    if (paramInt == 1) {
      bool = mExpandableListAdapter.isChildSelectable(groupPos, childPos);
    }
    localPositionMetadata.recycle();
    return bool;
  }
  
  public boolean isGroupExpanded(int paramInt)
  {
    for (int i = mExpGroupMetadataList.size() - 1; i >= 0; i--) {
      if (mExpGroupMetadataList.get(i)).gPos == paramInt) {
        return true;
      }
    }
    return false;
  }
  
  public void setExpandableListAdapter(ExpandableListAdapter paramExpandableListAdapter)
  {
    if (mExpandableListAdapter != null) {
      mExpandableListAdapter.unregisterDataSetObserver(mDataSetObserver);
    }
    mExpandableListAdapter = paramExpandableListAdapter;
    paramExpandableListAdapter.registerDataSetObserver(mDataSetObserver);
  }
  
  void setExpandedGroupMetadataList(ArrayList<GroupMetadata> paramArrayList)
  {
    if ((paramArrayList != null) && (mExpandableListAdapter != null))
    {
      int i = mExpandableListAdapter.getGroupCount();
      for (int j = paramArrayList.size() - 1; j >= 0; j--) {
        if (getgPos >= i) {
          return;
        }
      }
      mExpGroupMetadataList = paramArrayList;
      refreshExpGroupMetadataList(true, false);
      return;
    }
  }
  
  public void setMaxExpGroupCount(int paramInt)
  {
    mMaxExpGroupCount = paramInt;
  }
  
  static class GroupMetadata
    implements Parcelable, Comparable<GroupMetadata>
  {
    public static final Parcelable.Creator<GroupMetadata> CREATOR = new Parcelable.Creator()
    {
      public ExpandableListConnector.GroupMetadata createFromParcel(Parcel paramAnonymousParcel)
      {
        return ExpandableListConnector.GroupMetadata.obtain(paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readLong());
      }
      
      public ExpandableListConnector.GroupMetadata[] newArray(int paramAnonymousInt)
      {
        return new ExpandableListConnector.GroupMetadata[paramAnonymousInt];
      }
    };
    static final int REFRESH = -1;
    int flPos;
    long gId;
    int gPos;
    int lastChildFlPos;
    
    private GroupMetadata() {}
    
    static GroupMetadata obtain(int paramInt1, int paramInt2, int paramInt3, long paramLong)
    {
      GroupMetadata localGroupMetadata = new GroupMetadata();
      flPos = paramInt1;
      lastChildFlPos = paramInt2;
      gPos = paramInt3;
      gId = paramLong;
      return localGroupMetadata;
    }
    
    public int compareTo(GroupMetadata paramGroupMetadata)
    {
      if (paramGroupMetadata != null) {
        return gPos - gPos;
      }
      throw new IllegalArgumentException();
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(flPos);
      paramParcel.writeInt(lastChildFlPos);
      paramParcel.writeInt(gPos);
      paramParcel.writeLong(gId);
    }
  }
  
  protected class MyDataSetObserver
    extends DataSetObserver
  {
    protected MyDataSetObserver() {}
    
    public void onChanged()
    {
      ExpandableListConnector.this.refreshExpGroupMetadataList(true, true);
      notifyDataSetChanged();
    }
    
    public void onInvalidated()
    {
      ExpandableListConnector.this.refreshExpGroupMetadataList(true, true);
      notifyDataSetInvalidated();
    }
  }
  
  public static class PositionMetadata
  {
    private static final int MAX_POOL_SIZE = 5;
    private static ArrayList<PositionMetadata> sPool = new ArrayList(5);
    public int groupInsertIndex;
    public ExpandableListConnector.GroupMetadata groupMetadata;
    public ExpandableListPosition position;
    
    private PositionMetadata() {}
    
    private static PositionMetadata getRecycledOrCreate()
    {
      synchronized (sPool)
      {
        if (sPool.size() > 0)
        {
          localPositionMetadata = (PositionMetadata)sPool.remove(0);
          localPositionMetadata.resetState();
          return localPositionMetadata;
        }
        PositionMetadata localPositionMetadata = new android/widget/ExpandableListConnector$PositionMetadata;
        localPositionMetadata.<init>();
        return localPositionMetadata;
      }
    }
    
    static PositionMetadata obtain(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ExpandableListConnector.GroupMetadata paramGroupMetadata, int paramInt5)
    {
      PositionMetadata localPositionMetadata = getRecycledOrCreate();
      position = ExpandableListPosition.obtain(paramInt2, paramInt3, paramInt4, paramInt1);
      groupMetadata = paramGroupMetadata;
      groupInsertIndex = paramInt5;
      return localPositionMetadata;
    }
    
    private void resetState()
    {
      if (position != null)
      {
        position.recycle();
        position = null;
      }
      groupMetadata = null;
      groupInsertIndex = 0;
    }
    
    public boolean isExpanded()
    {
      boolean bool;
      if (groupMetadata != null) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void recycle()
    {
      resetState();
      synchronized (sPool)
      {
        if (sPool.size() < 5) {
          sPool.add(this);
        }
        return;
      }
    }
  }
}
