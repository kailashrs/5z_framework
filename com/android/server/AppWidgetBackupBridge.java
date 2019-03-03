package com.android.server;

import java.util.List;

public class AppWidgetBackupBridge
{
  private static WidgetBackupProvider sAppWidgetService;
  
  public AppWidgetBackupBridge() {}
  
  public static List<String> getWidgetParticipants(int paramInt)
  {
    List localList;
    if (sAppWidgetService != null) {
      localList = sAppWidgetService.getWidgetParticipants(paramInt);
    } else {
      localList = null;
    }
    return localList;
  }
  
  public static byte[] getWidgetState(String paramString, int paramInt)
  {
    if (sAppWidgetService != null) {
      paramString = sAppWidgetService.getWidgetState(paramString, paramInt);
    } else {
      paramString = null;
    }
    return paramString;
  }
  
  public static void register(WidgetBackupProvider paramWidgetBackupProvider)
  {
    sAppWidgetService = paramWidgetBackupProvider;
  }
  
  public static void restoreFinished(int paramInt)
  {
    if (sAppWidgetService != null) {
      sAppWidgetService.restoreFinished(paramInt);
    }
  }
  
  public static void restoreStarting(int paramInt)
  {
    if (sAppWidgetService != null) {
      sAppWidgetService.restoreStarting(paramInt);
    }
  }
  
  public static void restoreWidgetState(String paramString, byte[] paramArrayOfByte, int paramInt)
  {
    if (sAppWidgetService != null) {
      sAppWidgetService.restoreWidgetState(paramString, paramArrayOfByte, paramInt);
    }
  }
}
