package android.media;

public abstract interface MediaScannerClient
{
  public abstract void handleStringTag(String paramString1, String paramString2);
  
  public abstract void scanFile(String paramString, long paramLong1, long paramLong2, boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract void setMimeType(String paramString);
}
