package android.widget;

public abstract interface HeterogeneousExpandableList
{
  public abstract int getChildType(int paramInt1, int paramInt2);
  
  public abstract int getChildTypeCount();
  
  public abstract int getGroupType(int paramInt);
  
  public abstract int getGroupTypeCount();
}
