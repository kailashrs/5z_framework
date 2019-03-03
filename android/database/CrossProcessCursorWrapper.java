package android.database;

public class CrossProcessCursorWrapper
  extends CursorWrapper
  implements CrossProcessCursor
{
  public CrossProcessCursorWrapper(Cursor paramCursor)
  {
    super(paramCursor);
  }
  
  public void fillWindow(int paramInt, CursorWindow paramCursorWindow)
  {
    if ((mCursor instanceof CrossProcessCursor))
    {
      ((CrossProcessCursor)mCursor).fillWindow(paramInt, paramCursorWindow);
      return;
    }
    DatabaseUtils.cursorFillWindow(mCursor, paramInt, paramCursorWindow);
  }
  
  public CursorWindow getWindow()
  {
    if ((mCursor instanceof CrossProcessCursor)) {
      return ((CrossProcessCursor)mCursor).getWindow();
    }
    return null;
  }
  
  public boolean onMove(int paramInt1, int paramInt2)
  {
    if ((mCursor instanceof CrossProcessCursor)) {
      return ((CrossProcessCursor)mCursor).onMove(paramInt1, paramInt2);
    }
    return true;
  }
}
