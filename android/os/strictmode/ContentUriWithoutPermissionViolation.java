package android.os.strictmode;

import android.net.Uri;

public final class ContentUriWithoutPermissionViolation
  extends Violation
{
  public ContentUriWithoutPermissionViolation(Uri paramUri, String paramString)
  {
    super(localStringBuilder.toString());
  }
}
