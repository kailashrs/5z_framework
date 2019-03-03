package android.provider;

import android.annotation.SystemApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;

@SystemApi
public abstract class SearchIndexablesProvider
  extends ContentProvider
{
  private static final int MATCH_NON_INDEXABLE_KEYS_CODE = 3;
  private static final int MATCH_RAW_CODE = 2;
  private static final int MATCH_RES_CODE = 1;
  private static final int MATCH_SITE_MAP_PAIRS_CODE = 4;
  private static final String TAG = "IndexablesProvider";
  private String mAuthority;
  private UriMatcher mMatcher;
  
  public SearchIndexablesProvider() {}
  
  public void attachInfo(Context paramContext, ProviderInfo paramProviderInfo)
  {
    mAuthority = authority;
    mMatcher = new UriMatcher(-1);
    mMatcher.addURI(mAuthority, "settings/indexables_xml_res", 1);
    mMatcher.addURI(mAuthority, "settings/indexables_raw", 2);
    mMatcher.addURI(mAuthority, "settings/non_indexables_key", 3);
    mMatcher.addURI(mAuthority, "settings/site_map_pairs", 4);
    if (exported)
    {
      if (grantUriPermissions)
      {
        if ("android.permission.READ_SEARCH_INDEXABLES".equals(readPermission))
        {
          super.attachInfo(paramContext, paramProviderInfo);
          return;
        }
        throw new SecurityException("Provider must be protected by READ_SEARCH_INDEXABLES");
      }
      throw new SecurityException("Provider must grantUriPermissions");
    }
    throw new SecurityException("Provider must be exported");
  }
  
  public final int delete(Uri paramUri, String paramString, String[] paramArrayOfString)
  {
    throw new UnsupportedOperationException("Delete not supported");
  }
  
  public String getType(Uri paramUri)
  {
    switch (mMatcher.match(paramUri))
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown URI ");
      localStringBuilder.append(paramUri);
      throw new IllegalArgumentException(localStringBuilder.toString());
    case 3: 
      return "vnd.android.cursor.dir/non_indexables_key";
    case 2: 
      return "vnd.android.cursor.dir/indexables_raw";
    }
    return "vnd.android.cursor.dir/indexables_xml_res";
  }
  
  public final Uri insert(Uri paramUri, ContentValues paramContentValues)
  {
    throw new UnsupportedOperationException("Insert not supported");
  }
  
  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    switch (mMatcher.match(paramUri))
    {
    default: 
      paramArrayOfString1 = new StringBuilder();
      paramArrayOfString1.append("Unknown Uri ");
      paramArrayOfString1.append(paramUri);
      throw new UnsupportedOperationException(paramArrayOfString1.toString());
    case 4: 
      return querySiteMapPairs();
    case 3: 
      return queryNonIndexableKeys(null);
    case 2: 
      return queryRawData(null);
    }
    return queryXmlResources(null);
  }
  
  public abstract Cursor queryNonIndexableKeys(String[] paramArrayOfString);
  
  public abstract Cursor queryRawData(String[] paramArrayOfString);
  
  public Cursor querySiteMapPairs()
  {
    return null;
  }
  
  public abstract Cursor queryXmlResources(String[] paramArrayOfString);
  
  public final int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    throw new UnsupportedOperationException("Update not supported");
  }
}
