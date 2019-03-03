package android.widget;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentResolver.OpenResourceIdResult;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.WeakHashMap;

class SuggestionsAdapter
  extends ResourceCursorAdapter
  implements View.OnClickListener
{
  private static final boolean DBG = false;
  private static final long DELETE_KEY_POST_DELAY = 500L;
  static final int INVALID_INDEX = -1;
  private static final String LOG_TAG = "SuggestionsAdapter";
  private static final int QUERY_LIMIT = 50;
  static final int REFINE_ALL = 2;
  static final int REFINE_BY_ENTRY = 1;
  static final int REFINE_NONE = 0;
  private boolean mClosed = false;
  private final int mCommitIconResId;
  private int mFlagsCol = -1;
  private int mIconName1Col = -1;
  private int mIconName2Col = -1;
  private final WeakHashMap<String, Drawable.ConstantState> mOutsideDrawablesCache;
  private final Context mProviderContext;
  private int mQueryRefinement = 1;
  private final SearchManager mSearchManager = (SearchManager)mContext.getSystemService("search");
  private final SearchView mSearchView;
  private final SearchableInfo mSearchable;
  private int mText1Col = -1;
  private int mText2Col = -1;
  private int mText2UrlCol = -1;
  private ColorStateList mUrlColor;
  
  public SuggestionsAdapter(Context paramContext, SearchView paramSearchView, SearchableInfo paramSearchableInfo, WeakHashMap<String, Drawable.ConstantState> paramWeakHashMap)
  {
    super(paramContext, paramSearchView.getSuggestionRowLayout(), null, true);
    mSearchView = paramSearchView;
    mSearchable = paramSearchableInfo;
    mCommitIconResId = paramSearchView.getSuggestionCommitIconResId();
    paramContext = mSearchable.getActivityContext(mContext);
    mProviderContext = mSearchable.getProviderContext(mContext, paramContext);
    mOutsideDrawablesCache = paramWeakHashMap;
    getFilter().setDelayer(new Filter.Delayer()
    {
      private int mPreviousLength = 0;
      
      public long getPostingDelay(CharSequence paramAnonymousCharSequence)
      {
        long l = 0L;
        if (paramAnonymousCharSequence == null) {
          return 0L;
        }
        if (paramAnonymousCharSequence.length() < mPreviousLength) {
          l = 500L;
        }
        mPreviousLength = paramAnonymousCharSequence.length();
        return l;
      }
    });
  }
  
  private Drawable checkIconCache(String paramString)
  {
    paramString = (Drawable.ConstantState)mOutsideDrawablesCache.get(paramString);
    if (paramString == null) {
      return null;
    }
    return paramString.newDrawable();
  }
  
  private CharSequence formatUrl(Context paramContext, CharSequence paramCharSequence)
  {
    if (mUrlColor == null)
    {
      TypedValue localTypedValue = new TypedValue();
      paramContext.getTheme().resolveAttribute(17891546, localTypedValue, true);
      mUrlColor = paramContext.getColorStateList(resourceId);
    }
    paramContext = new SpannableString(paramCharSequence);
    paramContext.setSpan(new TextAppearanceSpan(null, 0, 0, mUrlColor, null), 0, paramCharSequence.length(), 33);
    return paramContext;
  }
  
  private Drawable getActivityIcon(ComponentName paramComponentName)
  {
    PackageManager localPackageManager = mContext.getPackageManager();
    try
    {
      Object localObject = localPackageManager.getActivityInfo(paramComponentName, 128);
      int i = ((ActivityInfo)localObject).getIconResource();
      if (i == 0) {
        return null;
      }
      localObject = localPackageManager.getDrawable(paramComponentName.getPackageName(), i, applicationInfo);
      if (localObject == null)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Invalid icon resource ");
        ((StringBuilder)localObject).append(i);
        ((StringBuilder)localObject).append(" for ");
        ((StringBuilder)localObject).append(paramComponentName.flattenToShortString());
        Log.w("SuggestionsAdapter", ((StringBuilder)localObject).toString());
        return null;
      }
      return localObject;
    }
    catch (PackageManager.NameNotFoundException paramComponentName)
    {
      Log.w("SuggestionsAdapter", paramComponentName.toString());
    }
    return null;
  }
  
  private Drawable getActivityIconWithCache(ComponentName paramComponentName)
  {
    String str = paramComponentName.flattenToShortString();
    boolean bool = mOutsideDrawablesCache.containsKey(str);
    Object localObject = null;
    Drawable localDrawable = null;
    if (bool)
    {
      paramComponentName = (Drawable.ConstantState)mOutsideDrawablesCache.get(str);
      if (paramComponentName == null) {
        paramComponentName = localDrawable;
      } else {
        paramComponentName = paramComponentName.newDrawable(mProviderContext.getResources());
      }
      return paramComponentName;
    }
    localDrawable = getActivityIcon(paramComponentName);
    if (localDrawable == null) {
      paramComponentName = localObject;
    } else {
      paramComponentName = localDrawable.getConstantState();
    }
    mOutsideDrawablesCache.put(str, paramComponentName);
    return localDrawable;
  }
  
  public static String getColumnString(Cursor paramCursor, String paramString)
  {
    return getStringOrNull(paramCursor, paramCursor.getColumnIndex(paramString));
  }
  
  private Drawable getDefaultIcon1(Cursor paramCursor)
  {
    paramCursor = getActivityIconWithCache(mSearchable.getSearchActivity());
    if (paramCursor != null) {
      return paramCursor;
    }
    return mContext.getPackageManager().getDefaultActivityIcon();
  }
  
  private Drawable getDrawable(Uri paramUri)
  {
    try
    {
      Object localObject2;
      if ("android.resource".equals(paramUri.getScheme()))
      {
        Object localObject1 = mProviderContext.getContentResolver().getResourceId(paramUri);
        try
        {
          localObject1 = r.getDrawable(id, mProviderContext.getTheme());
          return localObject1;
        }
        catch (Resources.NotFoundException localNotFoundException)
        {
          localObject2 = new java/io/FileNotFoundException;
          localObject4 = new java/lang/StringBuilder;
          ((StringBuilder)localObject4).<init>();
          ((StringBuilder)localObject4).append("Resource does not exist: ");
          ((StringBuilder)localObject4).append(paramUri);
          ((FileNotFoundException)localObject2).<init>(((StringBuilder)localObject4).toString());
          throw ((Throwable)localObject2);
        }
      }
      Object localObject4 = mProviderContext.getContentResolver().openInputStream(paramUri);
      if (localObject4 != null) {
        try
        {
          localObject2 = Drawable.createFromStream((InputStream)localObject4, null);
          try
          {
            ((InputStream)localObject4).close();
          }
          catch (IOException localIOException1)
          {
            localStringBuilder2 = new java/lang/StringBuilder;
            localStringBuilder2.<init>();
            localStringBuilder2.append("Error closing icon stream for ");
            localStringBuilder2.append(paramUri);
            Log.e("SuggestionsAdapter", localStringBuilder2.toString(), localIOException1);
          }
          return localObject2;
        }
        finally
        {
          try
          {
            localIOException1.close();
          }
          catch (IOException localIOException2)
          {
            StringBuilder localStringBuilder2 = new java/lang/StringBuilder;
            localStringBuilder2.<init>();
            localStringBuilder2.append("Error closing icon stream for ");
            localStringBuilder2.append(paramUri);
            Log.e("SuggestionsAdapter", localStringBuilder2.toString(), localIOException2);
          }
        }
      }
      FileNotFoundException localFileNotFoundException1 = new java/io/FileNotFoundException;
      localStringBuilder1 = new java/lang/StringBuilder;
      localStringBuilder1.<init>();
      localStringBuilder1.append("Failed to open ");
      localStringBuilder1.append(paramUri);
      localFileNotFoundException1.<init>(localStringBuilder1.toString());
      throw localFileNotFoundException1;
    }
    catch (FileNotFoundException localFileNotFoundException2)
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      localStringBuilder1.append("Icon not found: ");
      localStringBuilder1.append(paramUri);
      localStringBuilder1.append(", ");
      localStringBuilder1.append(localFileNotFoundException2.getMessage());
      Log.w("SuggestionsAdapter", localStringBuilder1.toString());
    }
    return null;
  }
  
  private Drawable getDrawableFromResourceValue(String paramString)
  {
    if ((paramString != null) && (paramString.length() != 0) && (!"0".equals(paramString))) {
      try
      {
        int i = Integer.parseInt(paramString);
        Object localObject = new java/lang/StringBuilder;
        ((StringBuilder)localObject).<init>();
        ((StringBuilder)localObject).append("android.resource://");
        ((StringBuilder)localObject).append(mProviderContext.getPackageName());
        ((StringBuilder)localObject).append("/");
        ((StringBuilder)localObject).append(i);
        localObject = ((StringBuilder)localObject).toString();
        Drawable localDrawable2 = checkIconCache((String)localObject);
        if (localDrawable2 != null) {
          return localDrawable2;
        }
        localDrawable2 = mProviderContext.getDrawable(i);
        storeInIconCache((String)localObject, localDrawable2);
        return localDrawable2;
      }
      catch (Resources.NotFoundException localNotFoundException)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Icon resource not found: ");
        localStringBuilder.append(paramString);
        Log.w("SuggestionsAdapter", localStringBuilder.toString());
        return null;
      }
      catch (NumberFormatException localNumberFormatException)
      {
        Drawable localDrawable1 = checkIconCache(paramString);
        if (localDrawable1 != null) {
          return localDrawable1;
        }
        localDrawable1 = getDrawable(Uri.parse(paramString));
        storeInIconCache(paramString, localDrawable1);
        return localDrawable1;
      }
    }
    return null;
  }
  
  private Drawable getIcon1(Cursor paramCursor)
  {
    if (mIconName1Col == -1) {
      return null;
    }
    Drawable localDrawable = getDrawableFromResourceValue(paramCursor.getString(mIconName1Col));
    if (localDrawable != null) {
      return localDrawable;
    }
    return getDefaultIcon1(paramCursor);
  }
  
  private Drawable getIcon2(Cursor paramCursor)
  {
    if (mIconName2Col == -1) {
      return null;
    }
    return getDrawableFromResourceValue(paramCursor.getString(mIconName2Col));
  }
  
  private static String getStringOrNull(Cursor paramCursor, int paramInt)
  {
    if (paramInt == -1) {
      return null;
    }
    try
    {
      paramCursor = paramCursor.getString(paramInt);
      return paramCursor;
    }
    catch (Exception paramCursor)
    {
      Log.e("SuggestionsAdapter", "unexpected error retrieving valid column from cursor, did the remote process die?", paramCursor);
    }
    return null;
  }
  
  private void setViewDrawable(ImageView paramImageView, Drawable paramDrawable, int paramInt)
  {
    paramImageView.setImageDrawable(paramDrawable);
    if (paramDrawable == null)
    {
      paramImageView.setVisibility(paramInt);
    }
    else
    {
      paramImageView.setVisibility(0);
      paramDrawable.setVisible(false, false);
      paramDrawable.setVisible(true, false);
    }
  }
  
  private void setViewText(TextView paramTextView, CharSequence paramCharSequence)
  {
    paramTextView.setText(paramCharSequence);
    if (TextUtils.isEmpty(paramCharSequence)) {
      paramTextView.setVisibility(8);
    } else {
      paramTextView.setVisibility(0);
    }
  }
  
  private void storeInIconCache(String paramString, Drawable paramDrawable)
  {
    if (paramDrawable != null) {
      mOutsideDrawablesCache.put(paramString, paramDrawable.getConstantState());
    }
  }
  
  private void updateSpinnerState(Cursor paramCursor)
  {
    if (paramCursor != null) {
      paramCursor = paramCursor.getExtras();
    } else {
      paramCursor = null;
    }
    if ((paramCursor != null) && (paramCursor.getBoolean("in_progress"))) {}
  }
  
  public void bindView(View paramView, Context paramContext, Cursor paramCursor)
  {
    ChildViewCache localChildViewCache = (ChildViewCache)paramView.getTag();
    int i = 0;
    if (mFlagsCol != -1) {
      i = paramCursor.getInt(mFlagsCol);
    }
    if (mText1 != null)
    {
      paramView = getStringOrNull(paramCursor, mText1Col);
      setViewText(mText1, paramView);
    }
    if (mText2 != null)
    {
      paramView = getStringOrNull(paramCursor, mText2UrlCol);
      if (paramView != null) {
        paramView = formatUrl(paramContext, paramView);
      } else {
        paramView = getStringOrNull(paramCursor, mText2Col);
      }
      if (TextUtils.isEmpty(paramView))
      {
        if (mText1 != null)
        {
          mText1.setSingleLine(false);
          mText1.setMaxLines(2);
        }
      }
      else if (mText1 != null)
      {
        mText1.setSingleLine(true);
        mText1.setMaxLines(1);
      }
      setViewText(mText2, paramView);
    }
    if (mIcon1 != null) {
      setViewDrawable(mIcon1, getIcon1(paramCursor), 4);
    }
    if (mIcon2 != null) {
      setViewDrawable(mIcon2, getIcon2(paramCursor), 8);
    }
    if ((mQueryRefinement != 2) && ((mQueryRefinement != 1) || ((i & 0x1) == 0)))
    {
      mIconRefine.setVisibility(8);
    }
    else
    {
      mIconRefine.setVisibility(0);
      mIconRefine.setTag(mText1.getText());
      mIconRefine.setOnClickListener(this);
    }
  }
  
  public void changeCursor(Cursor paramCursor)
  {
    if (mClosed)
    {
      Log.w("SuggestionsAdapter", "Tried to change cursor after adapter was closed.");
      if (paramCursor != null) {
        paramCursor.close();
      }
      return;
    }
    try
    {
      super.changeCursor(paramCursor);
      if (paramCursor != null)
      {
        mText1Col = paramCursor.getColumnIndex("suggest_text_1");
        mText2Col = paramCursor.getColumnIndex("suggest_text_2");
        mText2UrlCol = paramCursor.getColumnIndex("suggest_text_2_url");
        mIconName1Col = paramCursor.getColumnIndex("suggest_icon_1");
        mIconName2Col = paramCursor.getColumnIndex("suggest_icon_2");
        mFlagsCol = paramCursor.getColumnIndex("suggest_flags");
      }
    }
    catch (Exception paramCursor)
    {
      Log.e("SuggestionsAdapter", "error changing cursor and caching columns", paramCursor);
    }
  }
  
  public void close()
  {
    changeCursor(null);
    mClosed = true;
  }
  
  public CharSequence convertToString(Cursor paramCursor)
  {
    if (paramCursor == null) {
      return null;
    }
    String str = getColumnString(paramCursor, "suggest_intent_query");
    if (str != null) {
      return str;
    }
    if (mSearchable.shouldRewriteQueryFromData())
    {
      str = getColumnString(paramCursor, "suggest_intent_data");
      if (str != null) {
        return str;
      }
    }
    if (mSearchable.shouldRewriteQueryFromText())
    {
      paramCursor = getColumnString(paramCursor, "suggest_text_1");
      if (paramCursor != null) {
        return paramCursor;
      }
    }
    return null;
  }
  
  public View getDropDownView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    try
    {
      paramView = super.getDropDownView(paramInt, paramView, paramViewGroup);
      return paramView;
    }
    catch (RuntimeException localRuntimeException)
    {
      Log.w("SuggestionsAdapter", "Search suggestions cursor threw exception.", localRuntimeException);
      if (mDropDownContext == null) {
        paramView = mContext;
      } else {
        paramView = mDropDownContext;
      }
      paramView = newDropDownView(paramView, mCursor, paramViewGroup);
      if (paramView != null) {
        getTagmText1.setText(localRuntimeException.toString());
      }
    }
    return paramView;
  }
  
  public int getQueryRefinement()
  {
    return mQueryRefinement;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    try
    {
      paramView = super.getView(paramInt, paramView, paramViewGroup);
      return paramView;
    }
    catch (RuntimeException paramView)
    {
      Log.w("SuggestionsAdapter", "Search suggestions cursor threw exception.", paramView);
      paramViewGroup = newView(mContext, mCursor, paramViewGroup);
      if (paramViewGroup != null) {
        getTagmText1.setText(paramView.toString());
      }
    }
    return paramViewGroup;
  }
  
  public boolean hasStableIds()
  {
    return false;
  }
  
  public View newView(Context paramContext, Cursor paramCursor, ViewGroup paramViewGroup)
  {
    paramContext = super.newView(paramContext, paramCursor, paramViewGroup);
    paramContext.setTag(new ChildViewCache(paramContext));
    ((ImageView)paramContext.findViewById(16908912)).setImageResource(mCommitIconResId);
    return paramContext;
  }
  
  public void notifyDataSetChanged()
  {
    super.notifyDataSetChanged();
    updateSpinnerState(getCursor());
  }
  
  public void notifyDataSetInvalidated()
  {
    super.notifyDataSetInvalidated();
    updateSpinnerState(getCursor());
  }
  
  public void onClick(View paramView)
  {
    paramView = paramView.getTag();
    if ((paramView instanceof CharSequence)) {
      mSearchView.onQueryRefine((CharSequence)paramView);
    }
  }
  
  public Cursor runQueryOnBackgroundThread(CharSequence paramCharSequence)
  {
    if (paramCharSequence == null) {
      paramCharSequence = "";
    } else {
      paramCharSequence = paramCharSequence.toString();
    }
    if ((mSearchView.getVisibility() == 0) && (mSearchView.getWindowVisibility() == 0))
    {
      try
      {
        paramCharSequence = mSearchManager.getSuggestions(mSearchable, paramCharSequence, 50);
        if (paramCharSequence != null)
        {
          paramCharSequence.getCount();
          return paramCharSequence;
        }
      }
      catch (RuntimeException paramCharSequence)
      {
        Log.w("SuggestionsAdapter", "Search suggestions query threw an exception.", paramCharSequence);
      }
      return null;
    }
    return null;
  }
  
  public void setQueryRefinement(int paramInt)
  {
    mQueryRefinement = paramInt;
  }
  
  private static final class ChildViewCache
  {
    public final ImageView mIcon1;
    public final ImageView mIcon2;
    public final ImageView mIconRefine;
    public final TextView mText1;
    public final TextView mText2;
    
    public ChildViewCache(View paramView)
    {
      mText1 = ((TextView)paramView.findViewById(16908308));
      mText2 = ((TextView)paramView.findViewById(16908309));
      mIcon1 = ((ImageView)paramView.findViewById(16908295));
      mIcon2 = ((ImageView)paramView.findViewById(16908296));
      mIconRefine = ((ImageView)paramView.findViewById(16908912));
    }
  }
}
