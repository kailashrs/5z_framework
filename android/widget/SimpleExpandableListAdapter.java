package android.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import java.util.Map;

public class SimpleExpandableListAdapter
  extends BaseExpandableListAdapter
{
  private List<? extends List<? extends Map<String, ?>>> mChildData;
  private String[] mChildFrom;
  private int mChildLayout;
  private int[] mChildTo;
  private int mCollapsedGroupLayout;
  private int mExpandedGroupLayout;
  private List<? extends Map<String, ?>> mGroupData;
  private String[] mGroupFrom;
  private int[] mGroupTo;
  private LayoutInflater mInflater;
  private int mLastChildLayout;
  
  public SimpleExpandableListAdapter(Context paramContext, List<? extends Map<String, ?>> paramList, int paramInt1, int paramInt2, String[] paramArrayOfString1, int[] paramArrayOfInt1, List<? extends List<? extends Map<String, ?>>> paramList1, int paramInt3, int paramInt4, String[] paramArrayOfString2, int[] paramArrayOfInt2)
  {
    mGroupData = paramList;
    mExpandedGroupLayout = paramInt1;
    mCollapsedGroupLayout = paramInt2;
    mGroupFrom = paramArrayOfString1;
    mGroupTo = paramArrayOfInt1;
    mChildData = paramList1;
    mChildLayout = paramInt3;
    mLastChildLayout = paramInt4;
    mChildFrom = paramArrayOfString2;
    mChildTo = paramArrayOfInt2;
    mInflater = ((LayoutInflater)paramContext.getSystemService("layout_inflater"));
  }
  
  public SimpleExpandableListAdapter(Context paramContext, List<? extends Map<String, ?>> paramList, int paramInt1, int paramInt2, String[] paramArrayOfString1, int[] paramArrayOfInt1, List<? extends List<? extends Map<String, ?>>> paramList1, int paramInt3, String[] paramArrayOfString2, int[] paramArrayOfInt2)
  {
    this(paramContext, paramList, paramInt1, paramInt2, paramArrayOfString1, paramArrayOfInt1, paramList1, paramInt3, paramInt3, paramArrayOfString2, paramArrayOfInt2);
  }
  
  public SimpleExpandableListAdapter(Context paramContext, List<? extends Map<String, ?>> paramList, int paramInt1, String[] paramArrayOfString1, int[] paramArrayOfInt1, List<? extends List<? extends Map<String, ?>>> paramList1, int paramInt2, String[] paramArrayOfString2, int[] paramArrayOfInt2)
  {
    this(paramContext, paramList, paramInt1, paramInt1, paramArrayOfString1, paramArrayOfInt1, paramList1, paramInt2, paramInt2, paramArrayOfString2, paramArrayOfInt2);
  }
  
  private void bindView(View paramView, Map<String, ?> paramMap, String[] paramArrayOfString, int[] paramArrayOfInt)
  {
    int i = paramArrayOfInt.length;
    for (int j = 0; j < i; j++)
    {
      TextView localTextView = (TextView)paramView.findViewById(paramArrayOfInt[j]);
      if (localTextView != null) {
        localTextView.setText((String)paramMap.get(paramArrayOfString[j]));
      }
    }
  }
  
  public Object getChild(int paramInt1, int paramInt2)
  {
    return ((List)mChildData.get(paramInt1)).get(paramInt2);
  }
  
  public long getChildId(int paramInt1, int paramInt2)
  {
    return paramInt2;
  }
  
  public View getChildView(int paramInt1, int paramInt2, boolean paramBoolean, View paramView, ViewGroup paramViewGroup)
  {
    if (paramView == null) {
      paramView = newChildView(paramBoolean, paramViewGroup);
    }
    bindView(paramView, (Map)((List)mChildData.get(paramInt1)).get(paramInt2), mChildFrom, mChildTo);
    return paramView;
  }
  
  public int getChildrenCount(int paramInt)
  {
    return ((List)mChildData.get(paramInt)).size();
  }
  
  public Object getGroup(int paramInt)
  {
    return mGroupData.get(paramInt);
  }
  
  public int getGroupCount()
  {
    return mGroupData.size();
  }
  
  public long getGroupId(int paramInt)
  {
    return paramInt;
  }
  
  public View getGroupView(int paramInt, boolean paramBoolean, View paramView, ViewGroup paramViewGroup)
  {
    if (paramView == null) {
      paramView = newGroupView(paramBoolean, paramViewGroup);
    }
    bindView(paramView, (Map)mGroupData.get(paramInt), mGroupFrom, mGroupTo);
    return paramView;
  }
  
  public boolean hasStableIds()
  {
    return true;
  }
  
  public boolean isChildSelectable(int paramInt1, int paramInt2)
  {
    return true;
  }
  
  public View newChildView(boolean paramBoolean, ViewGroup paramViewGroup)
  {
    LayoutInflater localLayoutInflater = mInflater;
    int i;
    if (paramBoolean) {
      i = mLastChildLayout;
    } else {
      i = mChildLayout;
    }
    return localLayoutInflater.inflate(i, paramViewGroup, false);
  }
  
  public View newGroupView(boolean paramBoolean, ViewGroup paramViewGroup)
  {
    LayoutInflater localLayoutInflater = mInflater;
    int i;
    if (paramBoolean) {
      i = mExpandedGroupLayout;
    } else {
      i = mCollapsedGroupLayout;
    }
    return localLayoutInflater.inflate(i, paramViewGroup, false);
  }
}
