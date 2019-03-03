package com.android.internal.util;

import java.io.File;
import java.io.IOException;

@Deprecated
public class JournaledFile
{
  File mReal;
  File mTemp;
  boolean mWriting;
  
  public JournaledFile(File paramFile1, File paramFile2)
  {
    mReal = paramFile1;
    mTemp = paramFile2;
  }
  
  public File chooseForRead()
  {
    File localFile2;
    if (mReal.exists())
    {
      File localFile1 = mReal;
      localFile2 = localFile1;
      if (mTemp.exists())
      {
        mTemp.delete();
        localFile2 = localFile1;
      }
    }
    else
    {
      if (!mTemp.exists()) {
        break label69;
      }
      localFile2 = mTemp;
      mTemp.renameTo(mReal);
    }
    return localFile2;
    label69:
    return mReal;
  }
  
  public File chooseForWrite()
  {
    if (!mWriting)
    {
      if (!mReal.exists()) {
        try
        {
          mReal.createNewFile();
        }
        catch (IOException localIOException) {}
      }
      if (mTemp.exists()) {
        mTemp.delete();
      }
      mWriting = true;
      return mTemp;
    }
    throw new IllegalStateException("uncommitted write already in progress");
  }
  
  public void commit()
  {
    if (mWriting)
    {
      mWriting = false;
      mTemp.renameTo(mReal);
      return;
    }
    throw new IllegalStateException("no file to commit");
  }
  
  public void rollback()
  {
    if (mWriting)
    {
      mWriting = false;
      mTemp.delete();
      return;
    }
    throw new IllegalStateException("no file to roll back");
  }
}
