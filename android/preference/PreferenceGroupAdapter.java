package android.preference;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PreferenceGroupAdapter
  extends BaseAdapter
  implements Preference.OnPreferenceChangeInternalListener
{
  private static final String TAG = "PreferenceGroupAdapter";
  private static ViewGroup.LayoutParams sWrapperLayoutParams = new ViewGroup.LayoutParams(-1, -2);
  private Handler mHandler = new Handler();
  private boolean mHasReturnedViewTypeCount = false;
  private Drawable mHighlightedDrawable;
  private int mHighlightedPosition = -1;
  private volatile boolean mIsSyncing = false;
  private PreferenceGroup mPreferenceGroup;
  private ArrayList<PreferenceLayout> mPreferenceLayouts;
  private List<Preference> mPreferenceList;
  private Runnable mSyncRunnable = new Runnable()
  {
    public void run()
    {
      PreferenceGroupAdapter.this.syncMyPreferences();
    }
  };
  private PreferenceLayout mTempPreferenceLayout = new PreferenceLayout(null);
  
  public PreferenceGroupAdapter(PreferenceGroup paramPreferenceGroup)
  {
    mPreferenceGroup = paramPreferenceGroup;
    mPreferenceGroup.setOnPreferenceChangeInternalListener(this);
    mPreferenceList = new ArrayList();
    mPreferenceLayouts = new ArrayList();
    syncMyPreferences();
  }
  
  private void addPreferenceClassName(Preference paramPreference)
  {
    paramPreference = createPreferenceLayout(paramPreference, null);
    int i = Collections.binarySearch(mPreferenceLayouts, paramPreference);
    if (i < 0) {
      mPreferenceLayouts.add(i * -1 - 1, paramPreference);
    }
  }
  
  private PreferenceLayout createPreferenceLayout(Preference paramPreference, PreferenceLayout paramPreferenceLayout)
  {
    if (paramPreferenceLayout == null) {
      paramPreferenceLayout = new PreferenceLayout(null);
    }
    PreferenceLayout.access$202(paramPreferenceLayout, paramPreference.getClass().getName());
    PreferenceLayout.access$302(paramPreferenceLayout, paramPreference.getLayoutResource());
    PreferenceLayout.access$402(paramPreferenceLayout, paramPreference.getWidgetLayoutResource());
    return paramPreferenceLayout;
  }
  
  private void flattenPreferenceGroup(List<Preference> paramList, PreferenceGroup paramPreferenceGroup)
  {
    paramPreferenceGroup.sortPreferences();
    int i = paramPreferenceGroup.getPreferenceCount();
    for (int j = 0; j < i; j++)
    {
      Preference localPreference = paramPreferenceGroup.getPreference(j);
      paramList.add(localPreference);
      if ((!mHasReturnedViewTypeCount) && (localPreference.isRecycleEnabled())) {
        addPreferenceClassName(localPreference);
      }
      if ((localPreference instanceof PreferenceGroup))
      {
        PreferenceGroup localPreferenceGroup = (PreferenceGroup)localPreference;
        if (localPreferenceGroup.isOnSameScreenAsChildren()) {
          flattenPreferenceGroup(paramList, localPreferenceGroup);
        }
      }
      localPreference.setOnPreferenceChangeInternalListener(this);
    }
  }
  
  private int getHighlightItemViewType()
  {
    return getViewTypeCount() - 1;
  }
  
  /* Error */
  private void syncMyPreferences()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 61	android/preference/PreferenceGroupAdapter:mIsSyncing	Z
    //   6: ifeq +6 -> 12
    //   9: aload_0
    //   10: monitorexit
    //   11: return
    //   12: aload_0
    //   13: iconst_1
    //   14: putfield 61	android/preference/PreferenceGroupAdapter:mIsSyncing	Z
    //   17: aload_0
    //   18: monitorexit
    //   19: new 83	java/util/ArrayList
    //   22: dup
    //   23: aload_0
    //   24: getfield 86	android/preference/PreferenceGroupAdapter:mPreferenceList	Ljava/util/List;
    //   27: invokeinterface 178 1 0
    //   32: invokespecial 181	java/util/ArrayList:<init>	(I)V
    //   35: astore_1
    //   36: aload_0
    //   37: aload_1
    //   38: aload_0
    //   39: getfield 75	android/preference/PreferenceGroupAdapter:mPreferenceGroup	Landroid/preference/PreferenceGroup;
    //   42: invokespecial 168	android/preference/PreferenceGroupAdapter:flattenPreferenceGroup	(Ljava/util/List;Landroid/preference/PreferenceGroup;)V
    //   45: aload_0
    //   46: aload_1
    //   47: putfield 86	android/preference/PreferenceGroupAdapter:mPreferenceList	Ljava/util/List;
    //   50: aload_0
    //   51: invokevirtual 184	android/preference/PreferenceGroupAdapter:notifyDataSetChanged	()V
    //   54: aload_0
    //   55: monitorenter
    //   56: aload_0
    //   57: iconst_0
    //   58: putfield 61	android/preference/PreferenceGroupAdapter:mIsSyncing	Z
    //   61: aload_0
    //   62: invokevirtual 187	java/lang/Object:notifyAll	()V
    //   65: aload_0
    //   66: monitorexit
    //   67: return
    //   68: astore_1
    //   69: aload_0
    //   70: monitorexit
    //   71: aload_1
    //   72: athrow
    //   73: astore_1
    //   74: aload_0
    //   75: monitorexit
    //   76: aload_1
    //   77: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	78	0	this	PreferenceGroupAdapter
    //   35	12	1	localArrayList	ArrayList
    //   68	4	1	localObject1	Object
    //   73	4	1	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   56	67	68	finally
    //   69	71	68	finally
    //   2	11	73	finally
    //   12	19	73	finally
    //   74	76	73	finally
  }
  
  public boolean areAllItemsEnabled()
  {
    return false;
  }
  
  public int getCount()
  {
    return mPreferenceList.size();
  }
  
  public Preference getItem(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < getCount())) {
      return (Preference)mPreferenceList.get(paramInt);
    }
    return null;
  }
  
  public long getItemId(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < getCount())) {
      return getItem(paramInt).getId();
    }
    return Long.MIN_VALUE;
  }
  
  public int getItemViewType(int paramInt)
  {
    if (paramInt == mHighlightedPosition) {
      return getHighlightItemViewType();
    }
    if (!mHasReturnedViewTypeCount) {
      mHasReturnedViewTypeCount = true;
    }
    Preference localPreference = getItem(paramInt);
    if (!localPreference.isRecycleEnabled()) {
      return -1;
    }
    mTempPreferenceLayout = createPreferenceLayout(localPreference, mTempPreferenceLayout);
    paramInt = Collections.binarySearch(mPreferenceLayouts, mTempPreferenceLayout);
    if (paramInt < 0) {
      return -1;
    }
    return paramInt;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    Object localObject = getItem(paramInt);
    mTempPreferenceLayout = createPreferenceLayout((Preference)localObject, mTempPreferenceLayout);
    if ((Collections.binarySearch(mPreferenceLayouts, mTempPreferenceLayout) < 0) || (getItemViewType(paramInt) == getHighlightItemViewType())) {
      paramView = null;
    }
    localObject = ((Preference)localObject).getView(paramView, paramViewGroup);
    paramView = (View)localObject;
    if (paramInt == mHighlightedPosition)
    {
      paramView = (View)localObject;
      if (mHighlightedDrawable != null)
      {
        paramView = new FrameLayout(paramViewGroup.getContext());
        paramView.setLayoutParams(sWrapperLayoutParams);
        paramView.setBackgroundDrawable(mHighlightedDrawable);
        paramView.addView((View)localObject);
      }
    }
    return paramView;
  }
  
  public int getViewTypeCount()
  {
    if (!mHasReturnedViewTypeCount) {
      mHasReturnedViewTypeCount = true;
    }
    return Math.max(1, mPreferenceLayouts.size()) + 1;
  }
  
  public boolean hasStableIds()
  {
    return true;
  }
  
  public boolean isEnabled(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < getCount())) {
      return getItem(paramInt).isSelectable();
    }
    return true;
  }
  
  public void onPreferenceChange(Preference paramPreference)
  {
    notifyDataSetChanged();
  }
  
  public void onPreferenceHierarchyChange(Preference paramPreference)
  {
    mHandler.removeCallbacks(mSyncRunnable);
    mHandler.post(mSyncRunnable);
  }
  
  public void setHighlighted(int paramInt)
  {
    mHighlightedPosition = paramInt;
  }
  
  public void setHighlightedDrawable(Drawable paramDrawable)
  {
    mHighlightedDrawable = paramDrawable;
  }
  
  private static class PreferenceLayout
    implements Comparable<PreferenceLayout>
  {
    private String name;
    private int resId;
    private int widgetResId;
    
    private PreferenceLayout() {}
    
    public int compareTo(PreferenceLayout paramPreferenceLayout)
    {
      int i = name.compareTo(name);
      if (i == 0)
      {
        if (resId == resId)
        {
          if (widgetResId == widgetResId) {
            return 0;
          }
          return widgetResId - widgetResId;
        }
        return resId - resId;
      }
      return i;
    }
  }
}
