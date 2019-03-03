package android.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.UserHandle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import com.android.internal.R.styleable;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class SearchableInfo
  implements Parcelable
{
  public static final Parcelable.Creator<SearchableInfo> CREATOR = new Parcelable.Creator()
  {
    public SearchableInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SearchableInfo(paramAnonymousParcel);
    }
    
    public SearchableInfo[] newArray(int paramAnonymousInt)
    {
      return new SearchableInfo[paramAnonymousInt];
    }
  };
  private static final boolean DBG = false;
  private static final String LOG_TAG = "SearchableInfo";
  private static final String MD_LABEL_SEARCHABLE = "android.app.searchable";
  private static final String MD_XML_ELEMENT_SEARCHABLE = "searchable";
  private static final String MD_XML_ELEMENT_SEARCHABLE_ACTION_KEY = "actionkey";
  private static final int SEARCH_MODE_BADGE_ICON = 8;
  private static final int SEARCH_MODE_BADGE_LABEL = 4;
  private static final int SEARCH_MODE_QUERY_REWRITE_FROM_DATA = 16;
  private static final int SEARCH_MODE_QUERY_REWRITE_FROM_TEXT = 32;
  private static final int VOICE_SEARCH_LAUNCH_RECOGNIZER = 4;
  private static final int VOICE_SEARCH_LAUNCH_WEB_SEARCH = 2;
  private static final int VOICE_SEARCH_SHOW_BUTTON = 1;
  private HashMap<Integer, ActionKeyInfo> mActionKeys = null;
  private final boolean mAutoUrlDetect;
  private final int mHintId;
  private final int mIconId;
  private final boolean mIncludeInGlobalSearch;
  private final int mLabelId;
  private final boolean mQueryAfterZeroResults;
  private final ComponentName mSearchActivity;
  private final int mSearchButtonText;
  private final int mSearchImeOptions;
  private final int mSearchInputType;
  private final int mSearchMode;
  private final int mSettingsDescriptionId;
  private final String mSuggestAuthority;
  private final String mSuggestIntentAction;
  private final String mSuggestIntentData;
  private final String mSuggestPath;
  private final String mSuggestProviderPackage;
  private final String mSuggestSelection;
  private final int mSuggestThreshold;
  private final int mVoiceLanguageId;
  private final int mVoiceLanguageModeId;
  private final int mVoiceMaxResults;
  private final int mVoicePromptTextId;
  private final int mVoiceSearchMode;
  
  private SearchableInfo(Context paramContext, AttributeSet paramAttributeSet, ComponentName paramComponentName)
  {
    mSearchActivity = paramComponentName;
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.Searchable);
    mSearchMode = paramAttributeSet.getInt(3, 0);
    mLabelId = paramAttributeSet.getResourceId(0, 0);
    mHintId = paramAttributeSet.getResourceId(2, 0);
    mIconId = paramAttributeSet.getResourceId(1, 0);
    mSearchButtonText = paramAttributeSet.getResourceId(9, 0);
    mSearchInputType = paramAttributeSet.getInt(10, 1);
    mSearchImeOptions = paramAttributeSet.getInt(16, 2);
    mIncludeInGlobalSearch = paramAttributeSet.getBoolean(18, false);
    mQueryAfterZeroResults = paramAttributeSet.getBoolean(19, false);
    mAutoUrlDetect = paramAttributeSet.getBoolean(21, false);
    mSettingsDescriptionId = paramAttributeSet.getResourceId(20, 0);
    mSuggestAuthority = paramAttributeSet.getString(4);
    mSuggestPath = paramAttributeSet.getString(5);
    mSuggestSelection = paramAttributeSet.getString(6);
    mSuggestIntentAction = paramAttributeSet.getString(7);
    mSuggestIntentData = paramAttributeSet.getString(8);
    mSuggestThreshold = paramAttributeSet.getInt(17, 0);
    mVoiceSearchMode = paramAttributeSet.getInt(11, 0);
    mVoiceLanguageModeId = paramAttributeSet.getResourceId(12, 0);
    mVoicePromptTextId = paramAttributeSet.getResourceId(13, 0);
    mVoiceLanguageId = paramAttributeSet.getResourceId(14, 0);
    mVoiceMaxResults = paramAttributeSet.getInt(15, 0);
    paramAttributeSet.recycle();
    paramComponentName = null;
    paramAttributeSet = paramComponentName;
    if (mSuggestAuthority != null)
    {
      paramContext = paramContext.getPackageManager().resolveContentProvider(mSuggestAuthority, 268435456);
      paramAttributeSet = paramComponentName;
      if (paramContext != null) {
        paramAttributeSet = packageName;
      }
    }
    mSuggestProviderPackage = paramAttributeSet;
    if (mLabelId != 0) {
      return;
    }
    throw new IllegalArgumentException("Search label must be a resource reference.");
  }
  
  SearchableInfo(Parcel paramParcel)
  {
    mLabelId = paramParcel.readInt();
    mSearchActivity = ComponentName.readFromParcel(paramParcel);
    mHintId = paramParcel.readInt();
    mSearchMode = paramParcel.readInt();
    mIconId = paramParcel.readInt();
    mSearchButtonText = paramParcel.readInt();
    mSearchInputType = paramParcel.readInt();
    mSearchImeOptions = paramParcel.readInt();
    int i = paramParcel.readInt();
    boolean bool1 = false;
    if (i != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mIncludeInGlobalSearch = bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mQueryAfterZeroResults = bool2;
    boolean bool2 = bool1;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    }
    mAutoUrlDetect = bool2;
    mSettingsDescriptionId = paramParcel.readInt();
    mSuggestAuthority = paramParcel.readString();
    mSuggestPath = paramParcel.readString();
    mSuggestSelection = paramParcel.readString();
    mSuggestIntentAction = paramParcel.readString();
    mSuggestIntentData = paramParcel.readString();
    mSuggestThreshold = paramParcel.readInt();
    for (i = paramParcel.readInt(); i > 0; i--) {
      addActionKey(new ActionKeyInfo(paramParcel, null));
    }
    mSuggestProviderPackage = paramParcel.readString();
    mVoiceSearchMode = paramParcel.readInt();
    mVoiceLanguageModeId = paramParcel.readInt();
    mVoicePromptTextId = paramParcel.readInt();
    mVoiceLanguageId = paramParcel.readInt();
    mVoiceMaxResults = paramParcel.readInt();
  }
  
  private void addActionKey(ActionKeyInfo paramActionKeyInfo)
  {
    if (mActionKeys == null) {
      mActionKeys = new HashMap();
    }
    mActionKeys.put(Integer.valueOf(paramActionKeyInfo.getKeyCode()), paramActionKeyInfo);
  }
  
  private static Context createActivityContext(Context paramContext, ComponentName paramComponentName)
  {
    Object localObject1 = null;
    Object localObject2 = null;
    try
    {
      paramContext = paramContext.createPackageContext(paramComponentName.getPackageName(), 0);
    }
    catch (SecurityException localSecurityException)
    {
      paramContext = new StringBuilder();
      paramContext.append("Can't make context for ");
      paramContext.append(paramComponentName.getPackageName());
      Log.e("SearchableInfo", paramContext.toString(), localSecurityException);
      paramContext = localObject1;
    }
    catch (PackageManager.NameNotFoundException paramContext)
    {
      for (;;)
      {
        paramContext = new StringBuilder();
        paramContext.append("Package not found ");
        paramContext.append(paramComponentName.getPackageName());
        Log.e("SearchableInfo", paramContext.toString());
        paramContext = localSecurityException;
      }
    }
    return paramContext;
  }
  
  public static SearchableInfo getActivityMetaData(Context paramContext, ActivityInfo paramActivityInfo, int paramInt)
  {
    try
    {
      Object localObject = new android/os/UserHandle;
      ((UserHandle)localObject).<init>(paramInt);
      localObject = paramContext.createPackageContextAsUser("system", 0, (UserHandle)localObject);
      paramContext = paramActivityInfo.loadXmlMetaData(((Context)localObject).getPackageManager(), "android.app.searchable");
      if (paramContext == null) {
        return null;
      }
      paramActivityInfo = getActivityMetaData((Context)localObject, paramContext, new ComponentName(packageName, name));
      paramContext.close();
      return paramActivityInfo;
    }
    catch (PackageManager.NameNotFoundException paramContext)
    {
      paramContext = new StringBuilder();
      paramContext.append("Couldn't create package context for user ");
      paramContext.append(paramInt);
      Log.e("SearchableInfo", paramContext.toString());
    }
    return null;
  }
  
  private static SearchableInfo getActivityMetaData(Context paramContext, XmlPullParser paramXmlPullParser, ComponentName paramComponentName)
  {
    Object localObject = null;
    Context localContext = createActivityContext(paramContext, paramComponentName);
    if (localContext == null) {
      return null;
    }
    try
    {
      int i = paramXmlPullParser.next();
      for (paramContext = (Context)localObject; i != 1; paramContext = (Context)localObject)
      {
        localObject = paramContext;
        if (i == 2) {
          if (paramXmlPullParser.getName().equals("searchable"))
          {
            localObject = Xml.asAttributeSet(paramXmlPullParser);
            if (localObject != null) {
              try
              {
                paramContext = new android/app/SearchableInfo;
                paramContext.<init>(localContext, (AttributeSet)localObject, paramComponentName);
              }
              catch (IllegalArgumentException paramXmlPullParser)
              {
                paramContext = new java/lang/StringBuilder;
                paramContext.<init>();
                paramContext.append("Invalid searchable metadata for ");
                paramContext.append(paramComponentName.flattenToShortString());
                paramContext.append(": ");
                paramContext.append(paramXmlPullParser.getMessage());
                Log.w("SearchableInfo", paramContext.toString());
                return null;
              }
            }
            localObject = paramContext;
          }
          else
          {
            localObject = paramContext;
            if (paramXmlPullParser.getName().equals("actionkey"))
            {
              if (paramContext == null) {
                return null;
              }
              AttributeSet localAttributeSet = Xml.asAttributeSet(paramXmlPullParser);
              localObject = paramContext;
              if (localAttributeSet != null) {
                try
                {
                  localObject = new android/app/SearchableInfo$ActionKeyInfo;
                  ((ActionKeyInfo)localObject).<init>(localContext, localAttributeSet);
                  paramContext.addActionKey((ActionKeyInfo)localObject);
                  localObject = paramContext;
                }
                catch (IllegalArgumentException paramXmlPullParser)
                {
                  paramContext = new java/lang/StringBuilder;
                  paramContext.<init>();
                  paramContext.append("Invalid action key for ");
                  paramContext.append(paramComponentName.flattenToShortString());
                  paramContext.append(": ");
                  paramContext.append(paramXmlPullParser.getMessage());
                  Log.w("SearchableInfo", paramContext.toString());
                  return null;
                }
              }
            }
          }
        }
        i = paramXmlPullParser.next();
      }
      return paramContext;
    }
    catch (IOException paramXmlPullParser)
    {
      paramContext = new StringBuilder();
      paramContext.append("Reading searchable metadata for ");
      paramContext.append(paramComponentName.flattenToShortString());
      Log.w("SearchableInfo", paramContext.toString(), paramXmlPullParser);
      return null;
    }
    catch (XmlPullParserException paramXmlPullParser)
    {
      paramContext = new StringBuilder();
      paramContext.append("Reading searchable metadata for ");
      paramContext.append(paramComponentName.flattenToShortString());
      Log.w("SearchableInfo", paramContext.toString(), paramXmlPullParser);
    }
    return null;
  }
  
  public boolean autoUrlDetect()
  {
    return mAutoUrlDetect;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public ActionKeyInfo findActionKey(int paramInt)
  {
    if (mActionKeys == null) {
      return null;
    }
    return (ActionKeyInfo)mActionKeys.get(Integer.valueOf(paramInt));
  }
  
  public Context getActivityContext(Context paramContext)
  {
    return createActivityContext(paramContext, mSearchActivity);
  }
  
  public int getHintId()
  {
    return mHintId;
  }
  
  public int getIconId()
  {
    return mIconId;
  }
  
  public int getImeOptions()
  {
    return mSearchImeOptions;
  }
  
  public int getInputType()
  {
    return mSearchInputType;
  }
  
  public int getLabelId()
  {
    return mLabelId;
  }
  
  public Context getProviderContext(Context paramContext1, Context paramContext2)
  {
    Object localObject1 = null;
    Object localObject2 = null;
    if (mSearchActivity.getPackageName().equals(mSuggestProviderPackage)) {
      return paramContext2;
    }
    paramContext2 = localObject1;
    if (mSuggestProviderPackage != null) {
      try
      {
        paramContext1 = paramContext1.createPackageContext(mSuggestProviderPackage, 0);
        paramContext2 = paramContext1;
      }
      catch (SecurityException paramContext1)
      {
        paramContext2 = localObject1;
      }
      catch (PackageManager.NameNotFoundException paramContext1)
      {
        for (;;)
        {
          paramContext1 = localObject2;
        }
      }
    }
    return paramContext2;
  }
  
  public ComponentName getSearchActivity()
  {
    return mSearchActivity;
  }
  
  public int getSearchButtonText()
  {
    return mSearchButtonText;
  }
  
  public int getSettingsDescriptionId()
  {
    return mSettingsDescriptionId;
  }
  
  public String getSuggestAuthority()
  {
    return mSuggestAuthority;
  }
  
  public String getSuggestIntentAction()
  {
    return mSuggestIntentAction;
  }
  
  public String getSuggestIntentData()
  {
    return mSuggestIntentData;
  }
  
  public String getSuggestPackage()
  {
    return mSuggestProviderPackage;
  }
  
  public String getSuggestPath()
  {
    return mSuggestPath;
  }
  
  public String getSuggestSelection()
  {
    return mSuggestSelection;
  }
  
  public int getSuggestThreshold()
  {
    return mSuggestThreshold;
  }
  
  public int getVoiceLanguageId()
  {
    return mVoiceLanguageId;
  }
  
  public int getVoiceLanguageModeId()
  {
    return mVoiceLanguageModeId;
  }
  
  public int getVoiceMaxResults()
  {
    return mVoiceMaxResults;
  }
  
  public int getVoicePromptTextId()
  {
    return mVoicePromptTextId;
  }
  
  public boolean getVoiceSearchEnabled()
  {
    int i = mVoiceSearchMode;
    boolean bool = true;
    if ((i & 0x1) == 0) {
      bool = false;
    }
    return bool;
  }
  
  public boolean getVoiceSearchLaunchRecognizer()
  {
    boolean bool;
    if ((mVoiceSearchMode & 0x4) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean getVoiceSearchLaunchWebSearch()
  {
    boolean bool;
    if ((mVoiceSearchMode & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean queryAfterZeroResults()
  {
    return mQueryAfterZeroResults;
  }
  
  public boolean shouldIncludeInGlobalSearch()
  {
    return mIncludeInGlobalSearch;
  }
  
  public boolean shouldRewriteQueryFromData()
  {
    boolean bool;
    if ((mSearchMode & 0x10) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean shouldRewriteQueryFromText()
  {
    boolean bool;
    if ((mSearchMode & 0x20) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean useBadgeIcon()
  {
    boolean bool;
    if (((mSearchMode & 0x8) != 0) && (mIconId != 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean useBadgeLabel()
  {
    boolean bool;
    if ((mSearchMode & 0x4) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mLabelId);
    mSearchActivity.writeToParcel(paramParcel, paramInt);
    paramParcel.writeInt(mHintId);
    paramParcel.writeInt(mSearchMode);
    paramParcel.writeInt(mIconId);
    paramParcel.writeInt(mSearchButtonText);
    paramParcel.writeInt(mSearchInputType);
    paramParcel.writeInt(mSearchImeOptions);
    paramParcel.writeInt(mIncludeInGlobalSearch);
    paramParcel.writeInt(mQueryAfterZeroResults);
    paramParcel.writeInt(mAutoUrlDetect);
    paramParcel.writeInt(mSettingsDescriptionId);
    paramParcel.writeString(mSuggestAuthority);
    paramParcel.writeString(mSuggestPath);
    paramParcel.writeString(mSuggestSelection);
    paramParcel.writeString(mSuggestIntentAction);
    paramParcel.writeString(mSuggestIntentData);
    paramParcel.writeInt(mSuggestThreshold);
    if (mActionKeys == null)
    {
      paramParcel.writeInt(0);
    }
    else
    {
      paramParcel.writeInt(mActionKeys.size());
      Iterator localIterator = mActionKeys.values().iterator();
      while (localIterator.hasNext()) {
        ((ActionKeyInfo)localIterator.next()).writeToParcel(paramParcel, paramInt);
      }
    }
    paramParcel.writeString(mSuggestProviderPackage);
    paramParcel.writeInt(mVoiceSearchMode);
    paramParcel.writeInt(mVoiceLanguageModeId);
    paramParcel.writeInt(mVoicePromptTextId);
    paramParcel.writeInt(mVoiceLanguageId);
    paramParcel.writeInt(mVoiceMaxResults);
  }
  
  public static class ActionKeyInfo
    implements Parcelable
  {
    private final int mKeyCode;
    private final String mQueryActionMsg;
    private final String mSuggestActionMsg;
    private final String mSuggestActionMsgColumn;
    
    ActionKeyInfo(Context paramContext, AttributeSet paramAttributeSet)
    {
      paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.SearchableActionKey);
      mKeyCode = paramContext.getInt(0, 0);
      mQueryActionMsg = paramContext.getString(1);
      mSuggestActionMsg = paramContext.getString(2);
      mSuggestActionMsgColumn = paramContext.getString(3);
      paramContext.recycle();
      if (mKeyCode != 0)
      {
        if ((mQueryActionMsg == null) && (mSuggestActionMsg == null) && (mSuggestActionMsgColumn == null)) {
          throw new IllegalArgumentException("No message information.");
        }
        return;
      }
      throw new IllegalArgumentException("No keycode.");
    }
    
    private ActionKeyInfo(Parcel paramParcel)
    {
      mKeyCode = paramParcel.readInt();
      mQueryActionMsg = paramParcel.readString();
      mSuggestActionMsg = paramParcel.readString();
      mSuggestActionMsgColumn = paramParcel.readString();
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public int getKeyCode()
    {
      return mKeyCode;
    }
    
    public String getQueryActionMsg()
    {
      return mQueryActionMsg;
    }
    
    public String getSuggestActionMsg()
    {
      return mSuggestActionMsg;
    }
    
    public String getSuggestActionMsgColumn()
    {
      return mSuggestActionMsgColumn;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(mKeyCode);
      paramParcel.writeString(mQueryActionMsg);
      paramParcel.writeString(mSuggestActionMsg);
      paramParcel.writeString(mSuggestActionMsgColumn);
    }
  }
}
