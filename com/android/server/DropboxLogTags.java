package com.android.server;

import android.util.EventLog;

public class DropboxLogTags
{
  public static final int DROPBOX_FILE_COPY = 81002;
  
  private DropboxLogTags() {}
  
  public static void writeDropboxFileCopy(String paramString1, int paramInt, String paramString2)
  {
    EventLog.writeEvent(81002, new Object[] { paramString1, Integer.valueOf(paramInt), paramString2 });
  }
}
