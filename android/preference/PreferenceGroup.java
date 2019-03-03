package android.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.android.internal.R.styleable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class PreferenceGroup
  extends Preference
  implements GenericInflater.Parent<Preference>
{
  private boolean mAttachedToActivity = false;
  private int mCurrentPreferenceOrder = 0;
  private boolean mOrderingAsAdded = true;
  private List<Preference> mPreferenceList = new ArrayList();
  
  public PreferenceGroup(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public PreferenceGroup(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public PreferenceGroup(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.PreferenceGroup, paramInt1, paramInt2);
    mOrderingAsAdded = paramContext.getBoolean(0, mOrderingAsAdded);
    paramContext.recycle();
  }
  
  private boolean removePreferenceInt(Preference paramPreference)
  {
    try
    {
      paramPreference.onPrepareForRemoval();
      if (paramPreference.getParent() == this) {
        paramPreference.assignParent(null);
      }
      boolean bool = mPreferenceList.remove(paramPreference);
      return bool;
    }
    finally {}
  }
  
  public void addItemFromInflater(Preference paramPreference)
  {
    addPreference(paramPreference);
  }
  
  public boolean addPreference(Preference paramPreference)
  {
    if (mPreferenceList.contains(paramPreference)) {
      return true;
    }
    int i;
    if (paramPreference.getOrder() == Integer.MAX_VALUE)
    {
      if (mOrderingAsAdded)
      {
        i = mCurrentPreferenceOrder;
        mCurrentPreferenceOrder = (i + 1);
        paramPreference.setOrder(i);
      }
      if ((paramPreference instanceof PreferenceGroup)) {
        ((PreferenceGroup)paramPreference).setOrderingAsAdded(mOrderingAsAdded);
      }
    }
    if (!onPrepareAddPreference(paramPreference)) {
      return false;
    }
    try
    {
      int j = Collections.binarySearch(mPreferenceList, paramPreference);
      i = j;
      if (j < 0) {
        i = j * -1 - 1;
      }
      mPreferenceList.add(i, paramPreference);
      paramPreference.onAttachedToHierarchy(getPreferenceManager());
      paramPreference.assignParent(this);
      if (mAttachedToActivity) {
        paramPreference.onAttachedToActivity();
      }
      notifyHierarchyChanged();
      return true;
    }
    finally {}
  }
  
  protected void dispatchRestoreInstanceState(Bundle paramBundle)
  {
    super.dispatchRestoreInstanceState(paramBundle);
    int i = getPreferenceCount();
    for (int j = 0; j < i; j++) {
      getPreference(j).dispatchRestoreInstanceState(paramBundle);
    }
  }
  
  protected void dispatchSaveInstanceState(Bundle paramBundle)
  {
    super.dispatchSaveInstanceState(paramBundle);
    int i = getPreferenceCount();
    for (int j = 0; j < i; j++) {
      getPreference(j).dispatchSaveInstanceState(paramBundle);
    }
  }
  
  public Preference findPreference(CharSequence paramCharSequence)
  {
    if (TextUtils.equals(getKey(), paramCharSequence)) {
      return this;
    }
    int i = getPreferenceCount();
    for (int j = 0; j < i; j++)
    {
      Preference localPreference = getPreference(j);
      String str = localPreference.getKey();
      if ((str != null) && (str.equals(paramCharSequence))) {
        return localPreference;
      }
      if ((localPreference instanceof PreferenceGroup))
      {
        localPreference = ((PreferenceGroup)localPreference).findPreference(paramCharSequence);
        if (localPreference != null) {
          return localPreference;
        }
      }
    }
    return null;
  }
  
  public Preference getPreference(int paramInt)
  {
    return (Preference)mPreferenceList.get(paramInt);
  }
  
  public int getPreferenceCount()
  {
    return mPreferenceList.size();
  }
  
  protected boolean isOnSameScreenAsChildren()
  {
    return true;
  }
  
  public boolean isOrderingAsAdded()
  {
    return mOrderingAsAdded;
  }
  
  public void notifyDependencyChange(boolean paramBoolean)
  {
    super.notifyDependencyChange(paramBoolean);
    int i = getPreferenceCount();
    for (int j = 0; j < i; j++) {
      getPreference(j).onParentChanged(this, paramBoolean);
    }
  }
  
  protected void onAttachedToActivity()
  {
    super.onAttachedToActivity();
    mAttachedToActivity = true;
    int i = getPreferenceCount();
    for (int j = 0; j < i; j++) {
      getPreference(j).onAttachedToActivity();
    }
  }
  
  protected boolean onPrepareAddPreference(Preference paramPreference)
  {
    paramPreference.onParentChanged(this, shouldDisableDependents());
    return true;
  }
  
  protected void onPrepareForRemoval()
  {
    super.onPrepareForRemoval();
    mAttachedToActivity = false;
  }
  
  public void removeAll()
  {
    try
    {
      List localList = mPreferenceList;
      for (int i = localList.size() - 1; i >= 0; i--) {
        removePreferenceInt((Preference)localList.get(0));
      }
      notifyHierarchyChanged();
      return;
    }
    finally {}
  }
  
  public boolean removePreference(Preference paramPreference)
  {
    boolean bool = removePreferenceInt(paramPreference);
    notifyHierarchyChanged();
    return bool;
  }
  
  public void setOrderingAsAdded(boolean paramBoolean)
  {
    mOrderingAsAdded = paramBoolean;
  }
  
  void sortPreferences()
  {
    try
    {
      Collections.sort(mPreferenceList);
      return;
    }
    finally {}
  }
}
