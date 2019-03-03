package com.android.server;

import java.util.List;

public abstract interface WidgetBackupProvider
{
  public abstract List<String> getWidgetParticipants(int paramInt);
  
  public abstract byte[] getWidgetState(String paramString, int paramInt);
  
  public abstract void restoreFinished(int paramInt);
  
  public abstract void restoreStarting(int paramInt);
  
  public abstract void restoreWidgetState(String paramString, byte[] paramArrayOfByte, int paramInt);
}
