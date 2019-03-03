package android.widget;

public abstract interface WrapperListAdapter
  extends ListAdapter
{
  public abstract ListAdapter getWrappedAdapter();
}
