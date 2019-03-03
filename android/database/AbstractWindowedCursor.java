package android.database;

public abstract class AbstractWindowedCursor
  extends AbstractCursor
{
  protected CursorWindow mWindow;
  
  public AbstractWindowedCursor() {}
  
  protected void checkPosition()
  {
    super.checkPosition();
    if (mWindow != null) {
      return;
    }
    throw new StaleDataException("Attempting to access a closed CursorWindow.Most probable cause: cursor is deactivated prior to calling this method.");
  }
  
  protected void clearOrCreateWindow(String paramString)
  {
    if (mWindow == null) {
      mWindow = new CursorWindow(paramString);
    } else {
      mWindow.clear();
    }
  }
  
  protected void closeWindow()
  {
    if (mWindow != null)
    {
      mWindow.close();
      mWindow = null;
    }
  }
  
  public void copyStringToBuffer(int paramInt, CharArrayBuffer paramCharArrayBuffer)
  {
    checkPosition();
    mWindow.copyStringToBuffer(mPos, paramInt, paramCharArrayBuffer);
  }
  
  public byte[] getBlob(int paramInt)
  {
    checkPosition();
    return mWindow.getBlob(mPos, paramInt);
  }
  
  public double getDouble(int paramInt)
  {
    checkPosition();
    return mWindow.getDouble(mPos, paramInt);
  }
  
  public float getFloat(int paramInt)
  {
    checkPosition();
    return mWindow.getFloat(mPos, paramInt);
  }
  
  public int getInt(int paramInt)
  {
    checkPosition();
    return mWindow.getInt(mPos, paramInt);
  }
  
  public long getLong(int paramInt)
  {
    checkPosition();
    return mWindow.getLong(mPos, paramInt);
  }
  
  public short getShort(int paramInt)
  {
    checkPosition();
    return mWindow.getShort(mPos, paramInt);
  }
  
  public String getString(int paramInt)
  {
    checkPosition();
    return mWindow.getString(mPos, paramInt);
  }
  
  public int getType(int paramInt)
  {
    checkPosition();
    return mWindow.getType(mPos, paramInt);
  }
  
  public CursorWindow getWindow()
  {
    return mWindow;
  }
  
  public boolean hasWindow()
  {
    boolean bool;
    if (mWindow != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @Deprecated
  public boolean isBlob(int paramInt)
  {
    boolean bool;
    if (getType(paramInt) == 4) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @Deprecated
  public boolean isFloat(int paramInt)
  {
    boolean bool;
    if (getType(paramInt) == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @Deprecated
  public boolean isLong(int paramInt)
  {
    paramInt = getType(paramInt);
    boolean bool = true;
    if (paramInt != 1) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isNull(int paramInt)
  {
    checkPosition();
    boolean bool;
    if (mWindow.getType(mPos, paramInt) == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @Deprecated
  public boolean isString(int paramInt)
  {
    boolean bool;
    if (getType(paramInt) == 3) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected void onDeactivateOrClose()
  {
    super.onDeactivateOrClose();
    closeWindow();
  }
  
  public void setWindow(CursorWindow paramCursorWindow)
  {
    if (paramCursorWindow != mWindow)
    {
      closeWindow();
      mWindow = paramCursorWindow;
    }
  }
}
