package android.content;

import android.net.Uri;
import android.net.Uri.Builder;

public class ContentUris
{
  public ContentUris() {}
  
  public static Uri.Builder appendId(Uri.Builder paramBuilder, long paramLong)
  {
    return paramBuilder.appendEncodedPath(String.valueOf(paramLong));
  }
  
  public static long parseId(Uri paramUri)
  {
    paramUri = paramUri.getLastPathSegment();
    long l;
    if (paramUri == null) {
      l = -1L;
    } else {
      l = Long.parseLong(paramUri);
    }
    return l;
  }
  
  public static Uri withAppendedId(Uri paramUri, long paramLong)
  {
    return appendId(paramUri.buildUpon(), paramLong).build();
  }
}
