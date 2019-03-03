package android.media;

import android.content.ContentProvider;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;

public class ExternalRingtonesCursorWrapper
  extends CursorWrapper
{
  private int mUserId;
  
  public ExternalRingtonesCursorWrapper(Cursor paramCursor, int paramInt)
  {
    super(paramCursor);
    mUserId = paramInt;
  }
  
  public String getString(int paramInt)
  {
    String str1 = super.getString(paramInt);
    String str2 = str1;
    if (paramInt == 2) {
      str2 = ContentProvider.maybeAddUserId(Uri.parse(str1), mUserId).toString();
    }
    return str2;
  }
}
