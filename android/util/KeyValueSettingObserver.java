package android.util;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

public abstract class KeyValueSettingObserver
{
  private static final String TAG = "KeyValueSettingObserver";
  private final ContentObserver mObserver;
  private final KeyValueListParser mParser = new KeyValueListParser(',');
  private final ContentResolver mResolver;
  private final Uri mSettingUri;
  
  public KeyValueSettingObserver(Handler paramHandler, ContentResolver paramContentResolver, Uri paramUri)
  {
    mObserver = new SettingObserver(paramHandler, null);
    mResolver = paramContentResolver;
    mSettingUri = paramUri;
  }
  
  private void setParserValue()
  {
    String str = getSettingValue(mResolver);
    try
    {
      mParser.setString(str);
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Malformed setting: ");
      localStringBuilder.append(str);
      Slog.e("KeyValueSettingObserver", localStringBuilder.toString());
    }
  }
  
  public abstract String getSettingValue(ContentResolver paramContentResolver);
  
  public void start()
  {
    mResolver.registerContentObserver(mSettingUri, false, mObserver);
    setParserValue();
    update(mParser);
  }
  
  public void stop()
  {
    mResolver.unregisterContentObserver(mObserver);
  }
  
  public abstract void update(KeyValueListParser paramKeyValueListParser);
  
  private class SettingObserver
    extends ContentObserver
  {
    private SettingObserver(Handler paramHandler)
    {
      super();
    }
    
    public void onChange(boolean paramBoolean)
    {
      KeyValueSettingObserver.this.setParserValue();
      update(mParser);
    }
  }
}
