package android.text.style;

public abstract interface TabStopSpan
  extends ParagraphStyle
{
  public abstract int getTabStop();
  
  public static class Standard
    implements TabStopSpan
  {
    private int mTabOffset;
    
    public Standard(int paramInt)
    {
      mTabOffset = paramInt;
    }
    
    public int getTabStop()
    {
      return mTabOffset;
    }
  }
}
