package android.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import java.util.concurrent.Semaphore;

public class SearchRecentSuggestions
{
  private static final String LOG_TAG = "SearchSuggestions";
  private static final int MAX_HISTORY_COUNT = 250;
  public static final String[] QUERIES_PROJECTION_1LINE = { "_id", "date", "query", "display1" };
  public static final String[] QUERIES_PROJECTION_2LINE = { "_id", "date", "query", "display1", "display2" };
  public static final int QUERIES_PROJECTION_DATE_INDEX = 1;
  public static final int QUERIES_PROJECTION_DISPLAY1_INDEX = 3;
  public static final int QUERIES_PROJECTION_DISPLAY2_INDEX = 4;
  public static final int QUERIES_PROJECTION_QUERY_INDEX = 2;
  private static final Semaphore sWritesInProgress = new Semaphore(0);
  private final String mAuthority;
  private final Context mContext;
  private final Uri mSuggestionsUri;
  private final boolean mTwoLineDisplay;
  
  public SearchRecentSuggestions(Context paramContext, String paramString, int paramInt)
  {
    if ((!TextUtils.isEmpty(paramString)) && ((paramInt & 0x1) != 0))
    {
      boolean bool;
      if ((paramInt & 0x2) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      mTwoLineDisplay = bool;
      mContext = paramContext;
      mAuthority = new String(paramString);
      paramContext = new StringBuilder();
      paramContext.append("content://");
      paramContext.append(mAuthority);
      paramContext.append("/suggestions");
      mSuggestionsUri = Uri.parse(paramContext.toString());
      return;
    }
    throw new IllegalArgumentException();
  }
  
  private void saveRecentQueryBlocking(String paramString1, String paramString2)
  {
    ContentResolver localContentResolver = mContext.getContentResolver();
    long l = System.currentTimeMillis();
    try
    {
      ContentValues localContentValues = new android/content/ContentValues;
      localContentValues.<init>();
      localContentValues.put("display1", paramString1);
      if (mTwoLineDisplay) {
        localContentValues.put("display2", paramString2);
      }
      localContentValues.put("query", paramString1);
      localContentValues.put("date", Long.valueOf(l));
      localContentResolver.insert(mSuggestionsUri, localContentValues);
    }
    catch (RuntimeException paramString1)
    {
      Log.e("SearchSuggestions", "saveRecentQuery", paramString1);
    }
    truncateHistory(localContentResolver, 250);
  }
  
  public void clearHistory()
  {
    truncateHistory(mContext.getContentResolver(), 0);
  }
  
  public void saveRecentQuery(final String paramString1, final String paramString2)
  {
    if (TextUtils.isEmpty(paramString1)) {
      return;
    }
    if ((!mTwoLineDisplay) && (!TextUtils.isEmpty(paramString2))) {
      throw new IllegalArgumentException();
    }
    new Thread("saveRecentQuery")
    {
      public void run()
      {
        SearchRecentSuggestions.this.saveRecentQueryBlocking(paramString1, paramString2);
        SearchRecentSuggestions.sWritesInProgress.release();
      }
    }.start();
  }
  
  protected void truncateHistory(ContentResolver paramContentResolver, int paramInt)
  {
    if (paramInt >= 0)
    {
      Object localObject = null;
      if (paramInt > 0) {
        try
        {
          localObject = new java/lang/StringBuilder;
          ((StringBuilder)localObject).<init>();
          ((StringBuilder)localObject).append("_id IN (SELECT _id FROM suggestions ORDER BY date DESC LIMIT -1 OFFSET ");
          ((StringBuilder)localObject).append(String.valueOf(paramInt));
          ((StringBuilder)localObject).append(")");
          localObject = ((StringBuilder)localObject).toString();
        }
        catch (RuntimeException paramContentResolver)
        {
          break label67;
        }
      }
      paramContentResolver.delete(mSuggestionsUri, (String)localObject, null);
      break label76;
      label67:
      Log.e("SearchSuggestions", "truncateHistory", paramContentResolver);
      label76:
      return;
    }
    throw new IllegalArgumentException();
  }
  
  void waitForSave()
  {
    do
    {
      sWritesInProgress.acquireUninterruptibly();
    } while (sWritesInProgress.availablePermits() > 0);
  }
  
  private static class SuggestionColumns
    implements BaseColumns
  {
    public static final String DATE = "date";
    public static final String DISPLAY1 = "display1";
    public static final String DISPLAY2 = "display2";
    public static final String QUERY = "query";
    
    private SuggestionColumns() {}
  }
}
