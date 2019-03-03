package android.widget;

public abstract interface SectionIndexer
{
  public abstract int getPositionForSection(int paramInt);
  
  public abstract int getSectionForPosition(int paramInt);
  
  public abstract Object[] getSections();
}
