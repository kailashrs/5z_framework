package android.widget;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseAdapter
  implements ListAdapter, SpinnerAdapter
{
  private CharSequence[] mAutofillOptions;
  private final DataSetObservable mDataSetObservable = new DataSetObservable();
  
  public BaseAdapter() {}
  
  public boolean areAllItemsEnabled()
  {
    return true;
  }
  
  public CharSequence[] getAutofillOptions()
  {
    return mAutofillOptions;
  }
  
  public View getDropDownView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    return getView(paramInt, paramView, paramViewGroup);
  }
  
  public int getItemViewType(int paramInt)
  {
    return 0;
  }
  
  public int getViewTypeCount()
  {
    return 1;
  }
  
  public boolean hasStableIds()
  {
    return false;
  }
  
  public boolean isEmpty()
  {
    boolean bool;
    if (getCount() == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isEnabled(int paramInt)
  {
    return true;
  }
  
  public void notifyDataSetChanged()
  {
    mDataSetObservable.notifyChanged();
  }
  
  public void notifyDataSetInvalidated()
  {
    mDataSetObservable.notifyInvalidated();
  }
  
  public void registerDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    mDataSetObservable.registerObserver(paramDataSetObserver);
  }
  
  public void setAutofillOptions(CharSequence... paramVarArgs)
  {
    mAutofillOptions = paramVarArgs;
  }
  
  public void unregisterDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    mDataSetObservable.unregisterObserver(paramDataSetObserver);
  }
}
