package android.database;

public abstract interface CrossProcessCursor
  extends Cursor
{
  public abstract void fillWindow(int paramInt, CursorWindow paramCursorWindow);
  
  public abstract CursorWindow getWindow();
  
  public abstract boolean onMove(int paramInt1, int paramInt2);
}
