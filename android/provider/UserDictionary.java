package android.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import java.util.Locale;

public class UserDictionary
{
  public static final String AUTHORITY = "user_dictionary";
  public static final Uri CONTENT_URI = Uri.parse("content://user_dictionary");
  private static final int FREQUENCY_MAX = 255;
  private static final int FREQUENCY_MIN = 0;
  
  public UserDictionary() {}
  
  public static class Words
    implements BaseColumns
  {
    public static final String APP_ID = "appid";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.userword";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.userword";
    public static final Uri CONTENT_URI = Uri.parse("content://user_dictionary/words");
    public static final String DEFAULT_SORT_ORDER = "frequency DESC";
    public static final String FREQUENCY = "frequency";
    public static final String LOCALE = "locale";
    @Deprecated
    public static final int LOCALE_TYPE_ALL = 0;
    @Deprecated
    public static final int LOCALE_TYPE_CURRENT = 1;
    public static final String SHORTCUT = "shortcut";
    public static final String WORD = "word";
    public static final String _ID = "_id";
    
    public Words() {}
    
    @Deprecated
    public static void addWord(Context paramContext, String paramString, int paramInt1, int paramInt2)
    {
      if ((paramInt2 != 0) && (paramInt2 != 1)) {
        return;
      }
      Locale localLocale;
      if (paramInt2 == 1) {
        localLocale = Locale.getDefault();
      } else {
        localLocale = null;
      }
      addWord(paramContext, paramString, paramInt1, null, localLocale);
    }
    
    public static void addWord(Context paramContext, String paramString1, int paramInt, String paramString2, Locale paramLocale)
    {
      ContentResolver localContentResolver = paramContext.getContentResolver();
      if (TextUtils.isEmpty(paramString1)) {
        return;
      }
      int i = paramInt;
      if (paramInt < 0) {
        i = 0;
      }
      paramInt = i;
      if (i > 255) {
        paramInt = 255;
      }
      ContentValues localContentValues = new ContentValues(5);
      localContentValues.put("word", paramString1);
      localContentValues.put("frequency", Integer.valueOf(paramInt));
      if (paramLocale == null) {
        paramContext = null;
      } else {
        paramContext = paramLocale.toString();
      }
      localContentValues.put("locale", paramContext);
      localContentValues.put("appid", Integer.valueOf(0));
      localContentValues.put("shortcut", paramString2);
      localContentResolver.insert(CONTENT_URI, localContentValues);
    }
  }
}
