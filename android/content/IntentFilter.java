package android.content;

import android.annotation.SystemApi;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PatternMatcher;
import android.text.TextUtils;
import android.util.AndroidException;
import android.util.Log;
import android.util.Printer;
import android.util.proto.ProtoOutputStream;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class IntentFilter
  implements Parcelable
{
  private static final String ACTION_STR = "action";
  private static final String AGLOB_STR = "aglob";
  private static final String AUTH_STR = "auth";
  private static final String AUTO_VERIFY_STR = "autoVerify";
  private static final String CAT_STR = "cat";
  public static final Parcelable.Creator<IntentFilter> CREATOR = new Parcelable.Creator()
  {
    public IntentFilter createFromParcel(Parcel paramAnonymousParcel)
    {
      return new IntentFilter(paramAnonymousParcel);
    }
    
    public IntentFilter[] newArray(int paramAnonymousInt)
    {
      return new IntentFilter[paramAnonymousInt];
    }
  };
  private static final String HOST_STR = "host";
  private static final String LITERAL_STR = "literal";
  public static final int MATCH_ADJUSTMENT_MASK = 65535;
  public static final int MATCH_ADJUSTMENT_NORMAL = 32768;
  public static final int MATCH_CATEGORY_EMPTY = 1048576;
  public static final int MATCH_CATEGORY_HOST = 3145728;
  public static final int MATCH_CATEGORY_MASK = 268369920;
  public static final int MATCH_CATEGORY_PATH = 5242880;
  public static final int MATCH_CATEGORY_PORT = 4194304;
  public static final int MATCH_CATEGORY_SCHEME = 2097152;
  public static final int MATCH_CATEGORY_SCHEME_SPECIFIC_PART = 5767168;
  public static final int MATCH_CATEGORY_TYPE = 6291456;
  private static final String NAME_STR = "name";
  public static final int NO_MATCH_ACTION = -3;
  public static final int NO_MATCH_CATEGORY = -4;
  public static final int NO_MATCH_DATA = -2;
  public static final int NO_MATCH_TYPE = -1;
  private static final String PATH_STR = "path";
  private static final String PORT_STR = "port";
  private static final String PREFIX_STR = "prefix";
  public static final String SCHEME_HTTP = "http";
  public static final String SCHEME_HTTPS = "https";
  private static final String SCHEME_STR = "scheme";
  private static final String SGLOB_STR = "sglob";
  private static final String SSP_STR = "ssp";
  private static final int STATE_NEED_VERIFY = 16;
  private static final int STATE_NEED_VERIFY_CHECKED = 256;
  private static final int STATE_VERIFIED = 4096;
  private static final int STATE_VERIFY_AUTO = 1;
  public static final int SYSTEM_HIGH_PRIORITY = 1000;
  public static final int SYSTEM_LOW_PRIORITY = -1000;
  private static final String TYPE_STR = "type";
  public static final int VISIBILITY_EXPLICIT = 1;
  public static final int VISIBILITY_IMPLICIT = 2;
  public static final int VISIBILITY_NONE = 0;
  private final ArrayList<String> mActions;
  private ArrayList<String> mCategories = null;
  private ArrayList<AuthorityEntry> mDataAuthorities = null;
  private ArrayList<PatternMatcher> mDataPaths = null;
  private ArrayList<PatternMatcher> mDataSchemeSpecificParts = null;
  private ArrayList<String> mDataSchemes = null;
  private ArrayList<String> mDataTypes = null;
  private boolean mHasPartialTypes;
  private int mInstantAppVisibility;
  private int mOrder;
  private int mPriority;
  private int mVerifyState;
  
  public IntentFilter()
  {
    mHasPartialTypes = false;
    mPriority = 0;
    mActions = new ArrayList();
  }
  
  public IntentFilter(IntentFilter paramIntentFilter)
  {
    mHasPartialTypes = false;
    mPriority = mPriority;
    mOrder = mOrder;
    mActions = new ArrayList(mActions);
    if (mCategories != null) {
      mCategories = new ArrayList(mCategories);
    }
    if (mDataTypes != null) {
      mDataTypes = new ArrayList(mDataTypes);
    }
    if (mDataSchemes != null) {
      mDataSchemes = new ArrayList(mDataSchemes);
    }
    if (mDataSchemeSpecificParts != null) {
      mDataSchemeSpecificParts = new ArrayList(mDataSchemeSpecificParts);
    }
    if (mDataAuthorities != null) {
      mDataAuthorities = new ArrayList(mDataAuthorities);
    }
    if (mDataPaths != null) {
      mDataPaths = new ArrayList(mDataPaths);
    }
    mHasPartialTypes = mHasPartialTypes;
    mVerifyState = mVerifyState;
    mInstantAppVisibility = mInstantAppVisibility;
  }
  
  public IntentFilter(Parcel paramParcel)
  {
    boolean bool1 = false;
    mHasPartialTypes = false;
    mActions = new ArrayList();
    paramParcel.readStringList(mActions);
    if (paramParcel.readInt() != 0)
    {
      mCategories = new ArrayList();
      paramParcel.readStringList(mCategories);
    }
    if (paramParcel.readInt() != 0)
    {
      mDataSchemes = new ArrayList();
      paramParcel.readStringList(mDataSchemes);
    }
    if (paramParcel.readInt() != 0)
    {
      mDataTypes = new ArrayList();
      paramParcel.readStringList(mDataTypes);
    }
    int i = paramParcel.readInt();
    int j;
    if (i > 0)
    {
      mDataSchemeSpecificParts = new ArrayList(i);
      for (j = 0; j < i; j++) {
        mDataSchemeSpecificParts.add(new PatternMatcher(paramParcel));
      }
    }
    i = paramParcel.readInt();
    if (i > 0)
    {
      mDataAuthorities = new ArrayList(i);
      for (j = 0; j < i; j++) {
        mDataAuthorities.add(new AuthorityEntry(paramParcel));
      }
    }
    i = paramParcel.readInt();
    if (i > 0)
    {
      mDataPaths = new ArrayList(i);
      for (j = 0; j < i; j++) {
        mDataPaths.add(new PatternMatcher(paramParcel));
      }
    }
    mPriority = paramParcel.readInt();
    if (paramParcel.readInt() > 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mHasPartialTypes = bool2;
    boolean bool2 = bool1;
    if (paramParcel.readInt() > 0) {
      bool2 = true;
    }
    setAutoVerify(bool2);
    setVisibilityToInstantApp(paramParcel.readInt());
    mOrder = paramParcel.readInt();
  }
  
  public IntentFilter(String paramString)
  {
    mHasPartialTypes = false;
    mPriority = 0;
    mActions = new ArrayList();
    addAction(paramString);
  }
  
  public IntentFilter(String paramString1, String paramString2)
    throws IntentFilter.MalformedMimeTypeException
  {
    mHasPartialTypes = false;
    mPriority = 0;
    mActions = new ArrayList();
    addAction(paramString1);
    addDataType(paramString2);
  }
  
  private static String[] addStringToSet(String[] paramArrayOfString, String paramString, int[] paramArrayOfInt, int paramInt)
  {
    if (findStringInSet(paramArrayOfString, paramString, paramArrayOfInt, paramInt) >= 0) {
      return paramArrayOfString;
    }
    if (paramArrayOfString == null)
    {
      paramArrayOfString = new String[2];
      paramArrayOfString[0] = paramString;
      paramArrayOfInt[paramInt] = 1;
      return paramArrayOfString;
    }
    int i = paramArrayOfInt[paramInt];
    if (i < paramArrayOfString.length)
    {
      paramArrayOfString[i] = paramString;
      paramArrayOfInt[paramInt] = (i + 1);
      return paramArrayOfString;
    }
    String[] arrayOfString = new String[i * 3 / 2 + 2];
    System.arraycopy(paramArrayOfString, 0, arrayOfString, 0, i);
    arrayOfString[i] = paramString;
    paramArrayOfInt[paramInt] = (i + 1);
    return arrayOfString;
  }
  
  public static IntentFilter create(String paramString1, String paramString2)
  {
    try
    {
      paramString1 = new IntentFilter(paramString1, paramString2);
      return paramString1;
    }
    catch (MalformedMimeTypeException paramString1)
    {
      throw new RuntimeException("Bad MIME type", paramString1);
    }
  }
  
  private final boolean findMimeType(String paramString)
  {
    ArrayList localArrayList = mDataTypes;
    if (paramString == null) {
      return false;
    }
    if (localArrayList.contains(paramString)) {
      return true;
    }
    int i = paramString.length();
    if ((i == 3) && (paramString.equals("*/*"))) {
      return localArrayList.isEmpty() ^ true;
    }
    if ((mHasPartialTypes) && (localArrayList.contains("*"))) {
      return true;
    }
    int j = paramString.indexOf('/');
    if (j > 0)
    {
      if ((mHasPartialTypes) && (localArrayList.contains(paramString.substring(0, j)))) {
        return true;
      }
      if ((i == j + 2) && (paramString.charAt(j + 1) == '*'))
      {
        int k = localArrayList.size();
        for (i = 0; i < k; i++) {
          if (paramString.regionMatches(0, (String)localArrayList.get(i), 0, j + 1)) {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  private static int findStringInSet(String[] paramArrayOfString, String paramString, int[] paramArrayOfInt, int paramInt)
  {
    if (paramArrayOfString == null) {
      return -1;
    }
    int i = paramArrayOfInt[paramInt];
    for (paramInt = 0; paramInt < i; paramInt++) {
      if (paramArrayOfString[paramInt].equals(paramString)) {
        return paramInt;
      }
    }
    return -1;
  }
  
  private static String[] removeStringFromSet(String[] paramArrayOfString, String paramString, int[] paramArrayOfInt, int paramInt)
  {
    int i = findStringInSet(paramArrayOfString, paramString, paramArrayOfInt, paramInt);
    if (i < 0) {
      return paramArrayOfString;
    }
    int j = paramArrayOfInt[paramInt];
    if (j > paramArrayOfString.length / 4)
    {
      int k = j - (i + 1);
      if (k > 0) {
        System.arraycopy(paramArrayOfString, i + 1, paramArrayOfString, i, k);
      }
      paramArrayOfString[(j - 1)] = null;
      paramArrayOfInt[paramInt] = (j - 1);
      return paramArrayOfString;
    }
    paramString = new String[paramArrayOfString.length / 3];
    if (i > 0) {
      System.arraycopy(paramArrayOfString, 0, paramString, 0, i);
    }
    if (i + 1 < j) {
      System.arraycopy(paramArrayOfString, i + 1, paramString, i, j - (i + 1));
    }
    return paramString;
  }
  
  public final Iterator<String> actionsIterator()
  {
    Iterator localIterator;
    if (mActions != null) {
      localIterator = mActions.iterator();
    } else {
      localIterator = null;
    }
    return localIterator;
  }
  
  public final void addAction(String paramString)
  {
    if (!mActions.contains(paramString)) {
      mActions.add(paramString.intern());
    }
  }
  
  public final void addCategory(String paramString)
  {
    if (mCategories == null) {
      mCategories = new ArrayList();
    }
    if (!mCategories.contains(paramString)) {
      mCategories.add(paramString.intern());
    }
  }
  
  public final void addDataAuthority(AuthorityEntry paramAuthorityEntry)
  {
    if (mDataAuthorities == null) {
      mDataAuthorities = new ArrayList();
    }
    mDataAuthorities.add(paramAuthorityEntry);
  }
  
  public final void addDataAuthority(String paramString1, String paramString2)
  {
    String str = paramString2;
    if (paramString2 != null) {
      str = paramString2.intern();
    }
    addDataAuthority(new AuthorityEntry(paramString1.intern(), str));
  }
  
  public final void addDataPath(PatternMatcher paramPatternMatcher)
  {
    if (mDataPaths == null) {
      mDataPaths = new ArrayList();
    }
    mDataPaths.add(paramPatternMatcher);
  }
  
  public final void addDataPath(String paramString, int paramInt)
  {
    addDataPath(new PatternMatcher(paramString.intern(), paramInt));
  }
  
  public final void addDataScheme(String paramString)
  {
    if (mDataSchemes == null) {
      mDataSchemes = new ArrayList();
    }
    if (!mDataSchemes.contains(paramString)) {
      mDataSchemes.add(paramString.intern());
    }
  }
  
  public final void addDataSchemeSpecificPart(PatternMatcher paramPatternMatcher)
  {
    if (mDataSchemeSpecificParts == null) {
      mDataSchemeSpecificParts = new ArrayList();
    }
    mDataSchemeSpecificParts.add(paramPatternMatcher);
  }
  
  public final void addDataSchemeSpecificPart(String paramString, int paramInt)
  {
    addDataSchemeSpecificPart(new PatternMatcher(paramString, paramInt));
  }
  
  public final void addDataType(String paramString)
    throws IntentFilter.MalformedMimeTypeException
  {
    int i = paramString.indexOf('/');
    int j = paramString.length();
    if ((i > 0) && (j >= i + 2))
    {
      if (mDataTypes == null) {
        mDataTypes = new ArrayList();
      }
      if ((j == i + 2) && (paramString.charAt(i + 1) == '*'))
      {
        paramString = paramString.substring(0, i);
        if (!mDataTypes.contains(paramString)) {
          mDataTypes.add(paramString.intern());
        }
        mHasPartialTypes = true;
      }
      else if (!mDataTypes.contains(paramString))
      {
        mDataTypes.add(paramString.intern());
      }
      return;
    }
    throw new MalformedMimeTypeException(paramString);
  }
  
  public final Iterator<AuthorityEntry> authoritiesIterator()
  {
    Iterator localIterator;
    if (mDataAuthorities != null) {
      localIterator = mDataAuthorities.iterator();
    } else {
      localIterator = null;
    }
    return localIterator;
  }
  
  public final Iterator<String> categoriesIterator()
  {
    Iterator localIterator;
    if (mCategories != null) {
      localIterator = mCategories.iterator();
    } else {
      localIterator = null;
    }
    return localIterator;
  }
  
  public final int countActions()
  {
    return mActions.size();
  }
  
  public final int countCategories()
  {
    int i;
    if (mCategories != null) {
      i = mCategories.size();
    } else {
      i = 0;
    }
    return i;
  }
  
  public final int countDataAuthorities()
  {
    int i;
    if (mDataAuthorities != null) {
      i = mDataAuthorities.size();
    } else {
      i = 0;
    }
    return i;
  }
  
  public final int countDataPaths()
  {
    int i;
    if (mDataPaths != null) {
      i = mDataPaths.size();
    } else {
      i = 0;
    }
    return i;
  }
  
  public final int countDataSchemeSpecificParts()
  {
    int i;
    if (mDataSchemeSpecificParts != null) {
      i = mDataSchemeSpecificParts.size();
    } else {
      i = 0;
    }
    return i;
  }
  
  public final int countDataSchemes()
  {
    int i;
    if (mDataSchemes != null) {
      i = mDataSchemes.size();
    } else {
      i = 0;
    }
    return i;
  }
  
  public final int countDataTypes()
  {
    int i;
    if (mDataTypes != null) {
      i = mDataTypes.size();
    } else {
      i = 0;
    }
    return i;
  }
  
  public boolean debugCheck()
  {
    return true;
  }
  
  public final int describeContents()
  {
    return 0;
  }
  
  public void dump(Printer paramPrinter, String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder(256);
    Object localObject1;
    if (mActions.size() > 0)
    {
      localObject1 = mActions.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localStringBuilder.setLength(0);
        localStringBuilder.append(paramString);
        localStringBuilder.append("Action: \"");
        localStringBuilder.append((String)((Iterator)localObject1).next());
        localStringBuilder.append("\"");
        paramPrinter.println(localStringBuilder.toString());
      }
    }
    if (mCategories != null)
    {
      localObject1 = mCategories.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localStringBuilder.setLength(0);
        localStringBuilder.append(paramString);
        localStringBuilder.append("Category: \"");
        localStringBuilder.append((String)((Iterator)localObject1).next());
        localStringBuilder.append("\"");
        paramPrinter.println(localStringBuilder.toString());
      }
    }
    if (mDataSchemes != null)
    {
      localObject1 = mDataSchemes.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localStringBuilder.setLength(0);
        localStringBuilder.append(paramString);
        localStringBuilder.append("Scheme: \"");
        localStringBuilder.append((String)((Iterator)localObject1).next());
        localStringBuilder.append("\"");
        paramPrinter.println(localStringBuilder.toString());
      }
    }
    Object localObject2;
    if (mDataSchemeSpecificParts != null)
    {
      localObject1 = mDataSchemeSpecificParts.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (PatternMatcher)((Iterator)localObject1).next();
        localStringBuilder.setLength(0);
        localStringBuilder.append(paramString);
        localStringBuilder.append("Ssp: \"");
        localStringBuilder.append(localObject2);
        localStringBuilder.append("\"");
        paramPrinter.println(localStringBuilder.toString());
      }
    }
    if (mDataAuthorities != null)
    {
      localObject2 = mDataAuthorities.iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject1 = (AuthorityEntry)((Iterator)localObject2).next();
        localStringBuilder.setLength(0);
        localStringBuilder.append(paramString);
        localStringBuilder.append("Authority: \"");
        localStringBuilder.append(mHost);
        localStringBuilder.append("\": ");
        localStringBuilder.append(mPort);
        if (mWild) {
          localStringBuilder.append(" WILD");
        }
        paramPrinter.println(localStringBuilder.toString());
      }
    }
    if (mDataPaths != null)
    {
      localObject1 = mDataPaths.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (PatternMatcher)((Iterator)localObject1).next();
        localStringBuilder.setLength(0);
        localStringBuilder.append(paramString);
        localStringBuilder.append("Path: \"");
        localStringBuilder.append(localObject2);
        localStringBuilder.append("\"");
        paramPrinter.println(localStringBuilder.toString());
      }
    }
    if (mDataTypes != null)
    {
      localObject1 = mDataTypes.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localStringBuilder.setLength(0);
        localStringBuilder.append(paramString);
        localStringBuilder.append("Type: \"");
        localStringBuilder.append((String)((Iterator)localObject1).next());
        localStringBuilder.append("\"");
        paramPrinter.println(localStringBuilder.toString());
      }
    }
    if ((mPriority != 0) || (mOrder != 0) || (mHasPartialTypes))
    {
      localStringBuilder.setLength(0);
      localStringBuilder.append(paramString);
      localStringBuilder.append("mPriority=");
      localStringBuilder.append(mPriority);
      localStringBuilder.append(", mOrder=");
      localStringBuilder.append(mOrder);
      localStringBuilder.append(", mHasPartialTypes=");
      localStringBuilder.append(mHasPartialTypes);
      paramPrinter.println(localStringBuilder.toString());
    }
    if (getAutoVerify())
    {
      localStringBuilder.setLength(0);
      localStringBuilder.append(paramString);
      localStringBuilder.append("AutoVerify=");
      localStringBuilder.append(getAutoVerify());
      paramPrinter.println(localStringBuilder.toString());
    }
  }
  
  public final String getAction(int paramInt)
  {
    return (String)mActions.get(paramInt);
  }
  
  public final boolean getAutoVerify()
  {
    int i = mVerifyState;
    boolean bool = true;
    if ((i & 0x1) != 1) {
      bool = false;
    }
    return bool;
  }
  
  public final String getCategory(int paramInt)
  {
    return (String)mCategories.get(paramInt);
  }
  
  public final AuthorityEntry getDataAuthority(int paramInt)
  {
    return (AuthorityEntry)mDataAuthorities.get(paramInt);
  }
  
  public final PatternMatcher getDataPath(int paramInt)
  {
    return (PatternMatcher)mDataPaths.get(paramInt);
  }
  
  public final String getDataScheme(int paramInt)
  {
    return (String)mDataSchemes.get(paramInt);
  }
  
  public final PatternMatcher getDataSchemeSpecificPart(int paramInt)
  {
    return (PatternMatcher)mDataSchemeSpecificParts.get(paramInt);
  }
  
  public final String getDataType(int paramInt)
  {
    return (String)mDataTypes.get(paramInt);
  }
  
  public String[] getHosts()
  {
    ArrayList localArrayList = getHostsList();
    return (String[])localArrayList.toArray(new String[localArrayList.size()]);
  }
  
  public ArrayList<String> getHostsList()
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = authoritiesIterator();
    if (localIterator != null) {
      while (localIterator.hasNext()) {
        localArrayList.add(((AuthorityEntry)localIterator.next()).getHost());
      }
    }
    return localArrayList;
  }
  
  @SystemApi
  public final int getOrder()
  {
    return mOrder;
  }
  
  public final int getPriority()
  {
    return mPriority;
  }
  
  public int getVisibilityToInstantApp()
  {
    return mInstantAppVisibility;
  }
  
  public final boolean handleAllWebDataURI()
  {
    boolean bool1 = hasCategory("android.intent.category.APP_BROWSER");
    boolean bool2 = false;
    if ((!bool1) && ((!handlesWebUris(false)) || (countDataAuthorities() != 0))) {
      return bool2;
    }
    bool2 = true;
    return bool2;
  }
  
  public final boolean handlesWebUris(boolean paramBoolean)
  {
    if ((hasAction("android.intent.action.VIEW")) && (hasCategory("android.intent.category.BROWSABLE")) && (mDataSchemes != null) && (mDataSchemes.size() != 0))
    {
      int i = mDataSchemes.size();
      for (int j = 0; j < i; j++)
      {
        String str = (String)mDataSchemes.get(j);
        int k;
        if ((!"http".equals(str)) && (!"https".equals(str))) {
          k = 0;
        } else {
          k = 1;
        }
        if (paramBoolean)
        {
          if (k == 0) {
            return false;
          }
        }
        else if (k != 0) {
          return true;
        }
      }
      return paramBoolean;
    }
    return false;
  }
  
  public final boolean hasAction(String paramString)
  {
    boolean bool;
    if ((paramString != null) && (mActions.contains(paramString))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean hasCategory(String paramString)
  {
    boolean bool;
    if ((mCategories != null) && (mCategories.contains(paramString))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean hasDataAuthority(AuthorityEntry paramAuthorityEntry)
  {
    if (mDataAuthorities == null) {
      return false;
    }
    int i = mDataAuthorities.size();
    for (int j = 0; j < i; j++) {
      if (((AuthorityEntry)mDataAuthorities.get(j)).match(paramAuthorityEntry)) {
        return true;
      }
    }
    return false;
  }
  
  public final boolean hasDataAuthority(Uri paramUri)
  {
    boolean bool;
    if (matchDataAuthority(paramUri) >= 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean hasDataPath(PatternMatcher paramPatternMatcher)
  {
    if (mDataPaths == null) {
      return false;
    }
    int i = mDataPaths.size();
    for (int j = 0; j < i; j++)
    {
      PatternMatcher localPatternMatcher = (PatternMatcher)mDataPaths.get(j);
      if ((localPatternMatcher.getType() == paramPatternMatcher.getType()) && (localPatternMatcher.getPath().equals(paramPatternMatcher.getPath()))) {
        return true;
      }
    }
    return false;
  }
  
  public final boolean hasDataPath(String paramString)
  {
    if (mDataPaths == null) {
      return false;
    }
    int i = mDataPaths.size();
    for (int j = 0; j < i; j++) {
      if (((PatternMatcher)mDataPaths.get(j)).match(paramString)) {
        return true;
      }
    }
    return false;
  }
  
  public final boolean hasDataScheme(String paramString)
  {
    boolean bool;
    if ((mDataSchemes != null) && (mDataSchemes.contains(paramString))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean hasDataSchemeSpecificPart(PatternMatcher paramPatternMatcher)
  {
    if (mDataSchemeSpecificParts == null) {
      return false;
    }
    int i = mDataSchemeSpecificParts.size();
    for (int j = 0; j < i; j++)
    {
      PatternMatcher localPatternMatcher = (PatternMatcher)mDataSchemeSpecificParts.get(j);
      if ((localPatternMatcher.getType() == paramPatternMatcher.getType()) && (localPatternMatcher.getPath().equals(paramPatternMatcher.getPath()))) {
        return true;
      }
    }
    return false;
  }
  
  public final boolean hasDataSchemeSpecificPart(String paramString)
  {
    if (mDataSchemeSpecificParts == null) {
      return false;
    }
    int i = mDataSchemeSpecificParts.size();
    for (int j = 0; j < i; j++) {
      if (((PatternMatcher)mDataSchemeSpecificParts.get(j)).match(paramString)) {
        return true;
      }
    }
    return false;
  }
  
  public final boolean hasDataType(String paramString)
  {
    boolean bool;
    if ((mDataTypes != null) && (findMimeType(paramString))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean hasExactDataType(String paramString)
  {
    boolean bool;
    if ((mDataTypes != null) && (mDataTypes.contains(paramString))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isExplicitlyVisibleToInstantApp()
  {
    int i = mInstantAppVisibility;
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isImplicitlyVisibleToInstantApp()
  {
    boolean bool;
    if (mInstantAppVisibility == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean isVerified()
  {
    int i = mVerifyState;
    boolean bool = false;
    if ((i & 0x100) == 256)
    {
      if ((mVerifyState & 0x10) == 16) {
        bool = true;
      }
      return bool;
    }
    return false;
  }
  
  public boolean isVisibleToInstantApp()
  {
    boolean bool;
    if (mInstantAppVisibility != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final int match(ContentResolver paramContentResolver, Intent paramIntent, boolean paramBoolean, String paramString)
  {
    if (paramBoolean) {}
    for (paramContentResolver = paramIntent.resolveType(paramContentResolver);; paramContentResolver = paramIntent.getType()) {
      break;
    }
    return match(paramIntent.getAction(), paramContentResolver, paramIntent.getScheme(), paramIntent.getData(), paramIntent.getCategories(), paramString);
  }
  
  public final int match(String paramString1, String paramString2, String paramString3, Uri paramUri, Set<String> paramSet, String paramString4)
  {
    if ((paramString1 != null) && (!matchAction(paramString1))) {
      return -3;
    }
    int i = matchData(paramString2, paramString3, paramUri);
    if (i < 0) {
      return i;
    }
    if (matchCategories(paramSet) != null) {
      return -4;
    }
    return i;
  }
  
  public final boolean matchAction(String paramString)
  {
    return hasAction(paramString);
  }
  
  public final String matchCategories(Set<String> paramSet)
  {
    Object localObject = null;
    if (paramSet == null) {
      return null;
    }
    Iterator localIterator = paramSet.iterator();
    if (mCategories == null)
    {
      paramSet = localObject;
      if (localIterator.hasNext()) {
        paramSet = (String)localIterator.next();
      }
      return paramSet;
    }
    while (localIterator.hasNext())
    {
      paramSet = (String)localIterator.next();
      if (!mCategories.contains(paramSet)) {
        return paramSet;
      }
    }
    return null;
  }
  
  public final int matchData(String paramString1, String paramString2, Uri paramUri)
  {
    ArrayList localArrayList1 = mDataTypes;
    ArrayList localArrayList2 = mDataSchemes;
    int i = 1048576;
    int j = -2;
    int k;
    if ((localArrayList1 == null) && (localArrayList2 == null))
    {
      k = j;
      if (paramString1 == null)
      {
        k = j;
        if (paramUri == null) {
          k = 1081344;
        }
      }
      return k;
    }
    if (localArrayList2 != null)
    {
      if (paramString2 == null) {
        paramString2 = "";
      }
      if (localArrayList2.contains(paramString2))
      {
        j = 2097152;
        k = j;
        if (mDataSchemeSpecificParts != null)
        {
          k = j;
          if (paramUri != null) {
            if (hasDataSchemeSpecificPart(paramUri.getSchemeSpecificPart())) {
              k = 5767168;
            } else {
              k = -2;
            }
          }
        }
        j = k;
        if (k != 5767168)
        {
          j = k;
          if (mDataAuthorities != null)
          {
            j = matchDataAuthority(paramUri);
            if (j >= 0)
            {
              if (mDataPaths != null)
              {
                if (hasDataPath(paramUri.getPath())) {
                  j = 5242880;
                }
              }
              else {
                break label192;
              }
              return -2;
            }
            else
            {
              return -2;
            }
          }
        }
        label192:
        if (j == -2) {
          return -2;
        }
        k = j;
      }
      else
      {
        return -2;
      }
    }
    else
    {
      k = i;
      if (paramString2 != null)
      {
        k = i;
        if (!"".equals(paramString2))
        {
          k = i;
          if (!"content".equals(paramString2))
          {
            k = i;
            if (!"file".equals(paramString2)) {
              return -2;
            }
          }
        }
      }
    }
    if (localArrayList1 != null)
    {
      if (findMimeType(paramString1)) {
        k = 6291456;
      } else {
        return -1;
      }
    }
    else if (paramString1 != null) {
      return -1;
    }
    return 32768 + k;
  }
  
  public final int matchDataAuthority(Uri paramUri)
  {
    if ((mDataAuthorities != null) && (paramUri != null))
    {
      int i = mDataAuthorities.size();
      for (int j = 0; j < i; j++)
      {
        int k = ((AuthorityEntry)mDataAuthorities.get(j)).match(paramUri);
        if (k >= 0) {
          return k;
        }
      }
      return -2;
    }
    return -2;
  }
  
  public final boolean needsVerification()
  {
    boolean bool1 = getAutoVerify();
    boolean bool2 = true;
    if ((!bool1) || (!handlesWebUris(true))) {
      bool2 = false;
    }
    return bool2;
  }
  
  public final Iterator<PatternMatcher> pathsIterator()
  {
    Iterator localIterator;
    if (mDataPaths != null) {
      localIterator = mDataPaths.iterator();
    } else {
      localIterator = null;
    }
    return localIterator;
  }
  
  public void readFromXml(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    String str1 = paramXmlPullParser.getAttributeValue(null, "autoVerify");
    boolean bool;
    if (TextUtils.isEmpty(str1)) {
      bool = false;
    } else {
      bool = Boolean.getBoolean(str1);
    }
    setAutoVerify(bool);
    int i = paramXmlPullParser.getDepth();
    for (;;)
    {
      int j = paramXmlPullParser.next();
      if ((j == 1) || ((j == 3) && (paramXmlPullParser.getDepth() <= i))) {
        break;
      }
      if ((j != 3) && (j != 4))
      {
        str1 = paramXmlPullParser.getName();
        if (str1.equals("action"))
        {
          str1 = paramXmlPullParser.getAttributeValue(null, "name");
          if (str1 != null) {
            addAction(str1);
          }
        }
        else if (str1.equals("cat"))
        {
          str1 = paramXmlPullParser.getAttributeValue(null, "name");
          if (str1 != null) {
            addCategory(str1);
          }
        }
        else if (str1.equals("type"))
        {
          str1 = paramXmlPullParser.getAttributeValue(null, "name");
          if (str1 != null) {
            try
            {
              addDataType(str1);
            }
            catch (MalformedMimeTypeException localMalformedMimeTypeException) {}
          }
        }
        else
        {
          String str2;
          if (localMalformedMimeTypeException.equals("scheme"))
          {
            str2 = paramXmlPullParser.getAttributeValue(null, "name");
            if (str2 != null) {
              addDataScheme(str2);
            }
          }
          else if (str2.equals("ssp"))
          {
            str2 = paramXmlPullParser.getAttributeValue(null, "literal");
            if (str2 != null)
            {
              addDataSchemeSpecificPart(str2, 0);
            }
            else
            {
              str2 = paramXmlPullParser.getAttributeValue(null, "prefix");
              if (str2 != null)
              {
                addDataSchemeSpecificPart(str2, 1);
              }
              else
              {
                str2 = paramXmlPullParser.getAttributeValue(null, "sglob");
                if (str2 != null)
                {
                  addDataSchemeSpecificPart(str2, 2);
                }
                else
                {
                  str2 = paramXmlPullParser.getAttributeValue(null, "aglob");
                  if (str2 != null) {
                    addDataSchemeSpecificPart(str2, 3);
                  }
                }
              }
            }
          }
          else
          {
            Object localObject;
            if (str2.equals("auth"))
            {
              localObject = paramXmlPullParser.getAttributeValue(null, "host");
              str2 = paramXmlPullParser.getAttributeValue(null, "port");
              if (localObject != null) {
                addDataAuthority((String)localObject, str2);
              }
            }
            else if (str2.equals("path"))
            {
              str2 = paramXmlPullParser.getAttributeValue(null, "literal");
              if (str2 != null)
              {
                addDataPath(str2, 0);
              }
              else
              {
                str2 = paramXmlPullParser.getAttributeValue(null, "prefix");
                if (str2 != null)
                {
                  addDataPath(str2, 1);
                }
                else
                {
                  str2 = paramXmlPullParser.getAttributeValue(null, "sglob");
                  if (str2 != null)
                  {
                    addDataPath(str2, 2);
                  }
                  else
                  {
                    str2 = paramXmlPullParser.getAttributeValue(null, "aglob");
                    if (str2 != null) {
                      addDataPath(str2, 3);
                    }
                  }
                }
              }
            }
            else
            {
              localObject = new StringBuilder();
              ((StringBuilder)localObject).append("Unknown tag parsing IntentFilter: ");
              ((StringBuilder)localObject).append(str2);
              Log.w("IntentFilter", ((StringBuilder)localObject).toString());
            }
          }
        }
        XmlUtils.skipCurrentTag(paramXmlPullParser);
      }
    }
  }
  
  public final Iterator<PatternMatcher> schemeSpecificPartsIterator()
  {
    Iterator localIterator;
    if (mDataSchemeSpecificParts != null) {
      localIterator = mDataSchemeSpecificParts.iterator();
    } else {
      localIterator = null;
    }
    return localIterator;
  }
  
  public final Iterator<String> schemesIterator()
  {
    Iterator localIterator;
    if (mDataSchemes != null) {
      localIterator = mDataSchemes.iterator();
    } else {
      localIterator = null;
    }
    return localIterator;
  }
  
  public final void setAutoVerify(boolean paramBoolean)
  {
    mVerifyState &= 0xFFFFFFFE;
    if (paramBoolean) {
      mVerifyState |= 0x1;
    }
  }
  
  @SystemApi
  public final void setOrder(int paramInt)
  {
    mOrder = paramInt;
  }
  
  public final void setPriority(int paramInt)
  {
    mPriority = paramInt;
  }
  
  public void setVerified(boolean paramBoolean)
  {
    mVerifyState |= 0x100;
    mVerifyState &= 0xEFFF;
    if (paramBoolean) {
      mVerifyState |= 0x1000;
    }
  }
  
  public void setVisibilityToInstantApp(int paramInt)
  {
    mInstantAppVisibility = paramInt;
  }
  
  public final Iterator<String> typesIterator()
  {
    Iterator localIterator;
    if (mDataTypes != null) {
      localIterator = mDataTypes.iterator();
    } else {
      localIterator = null;
    }
    return localIterator;
  }
  
  public final void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeStringList(mActions);
    ArrayList localArrayList = mCategories;
    int i = 0;
    if (localArrayList != null)
    {
      paramParcel.writeInt(1);
      paramParcel.writeStringList(mCategories);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (mDataSchemes != null)
    {
      paramParcel.writeInt(1);
      paramParcel.writeStringList(mDataSchemes);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (mDataTypes != null)
    {
      paramParcel.writeInt(1);
      paramParcel.writeStringList(mDataTypes);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    int j;
    int k;
    if (mDataSchemeSpecificParts != null)
    {
      j = mDataSchemeSpecificParts.size();
      paramParcel.writeInt(j);
      for (k = 0; k < j; k++) {
        ((PatternMatcher)mDataSchemeSpecificParts.get(k)).writeToParcel(paramParcel, paramInt);
      }
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (mDataAuthorities != null)
    {
      j = mDataAuthorities.size();
      paramParcel.writeInt(j);
      for (k = 0; k < j; k++) {
        ((AuthorityEntry)mDataAuthorities.get(k)).writeToParcel(paramParcel);
      }
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (mDataPaths != null)
    {
      j = mDataPaths.size();
      paramParcel.writeInt(j);
      for (k = i; k < j; k++) {
        ((PatternMatcher)mDataPaths.get(k)).writeToParcel(paramParcel, paramInt);
      }
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeInt(mPriority);
    paramParcel.writeInt(mHasPartialTypes);
    paramParcel.writeInt(getAutoVerify());
    paramParcel.writeInt(mInstantAppVisibility);
    paramParcel.writeInt(mOrder);
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    Iterator localIterator;
    if (mActions.size() > 0)
    {
      localIterator = mActions.iterator();
      while (localIterator.hasNext()) {
        paramProtoOutputStream.write(2237677961217L, (String)localIterator.next());
      }
    }
    if (mCategories != null)
    {
      localIterator = mCategories.iterator();
      while (localIterator.hasNext()) {
        paramProtoOutputStream.write(2237677961218L, (String)localIterator.next());
      }
    }
    if (mDataSchemes != null)
    {
      localIterator = mDataSchemes.iterator();
      while (localIterator.hasNext()) {
        paramProtoOutputStream.write(2237677961219L, (String)localIterator.next());
      }
    }
    if (mDataSchemeSpecificParts != null)
    {
      localIterator = mDataSchemeSpecificParts.iterator();
      while (localIterator.hasNext()) {
        ((PatternMatcher)localIterator.next()).writeToProto(paramProtoOutputStream, 2246267895812L);
      }
    }
    if (mDataAuthorities != null)
    {
      localIterator = mDataAuthorities.iterator();
      while (localIterator.hasNext()) {
        ((AuthorityEntry)localIterator.next()).writeToProto(paramProtoOutputStream, 2246267895813L);
      }
    }
    if (mDataPaths != null)
    {
      localIterator = mDataPaths.iterator();
      while (localIterator.hasNext()) {
        ((PatternMatcher)localIterator.next()).writeToProto(paramProtoOutputStream, 2246267895814L);
      }
    }
    if (mDataTypes != null)
    {
      localIterator = mDataTypes.iterator();
      while (localIterator.hasNext()) {
        paramProtoOutputStream.write(2237677961223L, (String)localIterator.next());
      }
    }
    if ((mPriority != 0) || (mHasPartialTypes))
    {
      paramProtoOutputStream.write(1120986464264L, mPriority);
      paramProtoOutputStream.write(1133871366153L, mHasPartialTypes);
    }
    paramProtoOutputStream.write(1133871366154L, getAutoVerify());
    paramProtoOutputStream.end(paramLong);
  }
  
  public void writeToXml(XmlSerializer paramXmlSerializer)
    throws IOException
  {
    if (getAutoVerify()) {
      paramXmlSerializer.attribute(null, "autoVerify", Boolean.toString(true));
    }
    int i = countActions();
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      paramXmlSerializer.startTag(null, "action");
      paramXmlSerializer.attribute(null, "name", (String)mActions.get(k));
      paramXmlSerializer.endTag(null, "action");
    }
    i = countCategories();
    for (k = 0; k < i; k++)
    {
      paramXmlSerializer.startTag(null, "cat");
      paramXmlSerializer.attribute(null, "name", (String)mCategories.get(k));
      paramXmlSerializer.endTag(null, "cat");
    }
    i = countDataTypes();
    Object localObject;
    for (k = 0; k < i; k++)
    {
      paramXmlSerializer.startTag(null, "type");
      String str = (String)mDataTypes.get(k);
      localObject = str;
      if (str.indexOf('/') < 0)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(str);
        ((StringBuilder)localObject).append("/*");
        localObject = ((StringBuilder)localObject).toString();
      }
      paramXmlSerializer.attribute(null, "name", (String)localObject);
      paramXmlSerializer.endTag(null, "type");
    }
    i = countDataSchemes();
    for (k = 0; k < i; k++)
    {
      paramXmlSerializer.startTag(null, "scheme");
      paramXmlSerializer.attribute(null, "name", (String)mDataSchemes.get(k));
      paramXmlSerializer.endTag(null, "scheme");
    }
    i = countDataSchemeSpecificParts();
    for (k = 0; k < i; k++)
    {
      paramXmlSerializer.startTag(null, "ssp");
      localObject = (PatternMatcher)mDataSchemeSpecificParts.get(k);
      switch (((PatternMatcher)localObject).getType())
      {
      default: 
        break;
      case 3: 
        paramXmlSerializer.attribute(null, "aglob", ((PatternMatcher)localObject).getPath());
        break;
      case 2: 
        paramXmlSerializer.attribute(null, "sglob", ((PatternMatcher)localObject).getPath());
        break;
      case 1: 
        paramXmlSerializer.attribute(null, "prefix", ((PatternMatcher)localObject).getPath());
        break;
      case 0: 
        paramXmlSerializer.attribute(null, "literal", ((PatternMatcher)localObject).getPath());
      }
      paramXmlSerializer.endTag(null, "ssp");
    }
    i = countDataAuthorities();
    for (k = 0; k < i; k++)
    {
      paramXmlSerializer.startTag(null, "auth");
      localObject = (AuthorityEntry)mDataAuthorities.get(k);
      paramXmlSerializer.attribute(null, "host", ((AuthorityEntry)localObject).getHost());
      if (((AuthorityEntry)localObject).getPort() >= 0) {
        paramXmlSerializer.attribute(null, "port", Integer.toString(((AuthorityEntry)localObject).getPort()));
      }
      paramXmlSerializer.endTag(null, "auth");
    }
    i = countDataPaths();
    for (k = j; k < i; k++)
    {
      paramXmlSerializer.startTag(null, "path");
      localObject = (PatternMatcher)mDataPaths.get(k);
      switch (((PatternMatcher)localObject).getType())
      {
      default: 
        break;
      case 3: 
        paramXmlSerializer.attribute(null, "aglob", ((PatternMatcher)localObject).getPath());
        break;
      case 2: 
        paramXmlSerializer.attribute(null, "sglob", ((PatternMatcher)localObject).getPath());
        break;
      case 1: 
        paramXmlSerializer.attribute(null, "prefix", ((PatternMatcher)localObject).getPath());
        break;
      case 0: 
        paramXmlSerializer.attribute(null, "literal", ((PatternMatcher)localObject).getPath());
      }
      paramXmlSerializer.endTag(null, "path");
    }
  }
  
  public static final class AuthorityEntry
  {
    private final String mHost;
    private final String mOrigHost;
    private final int mPort;
    private final boolean mWild;
    
    AuthorityEntry(Parcel paramParcel)
    {
      mOrigHost = paramParcel.readString();
      mHost = paramParcel.readString();
      boolean bool;
      if (paramParcel.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      mWild = bool;
      mPort = paramParcel.readInt();
    }
    
    public AuthorityEntry(String paramString1, String paramString2)
    {
      mOrigHost = paramString1;
      int i = paramString1.length();
      boolean bool1 = false;
      boolean bool2 = bool1;
      if (i > 0)
      {
        bool2 = bool1;
        if (paramString1.charAt(0) == '*') {
          bool2 = true;
        }
      }
      mWild = bool2;
      if (mWild) {
        paramString1 = paramString1.substring(1).intern();
      }
      mHost = paramString1;
      if (paramString2 != null) {
        i = Integer.parseInt(paramString2);
      } else {
        i = -1;
      }
      mPort = i;
    }
    
    public boolean equals(Object paramObject)
    {
      if ((paramObject instanceof AuthorityEntry)) {
        return match((AuthorityEntry)paramObject);
      }
      return false;
    }
    
    public String getHost()
    {
      return mOrigHost;
    }
    
    public int getPort()
    {
      return mPort;
    }
    
    public int match(Uri paramUri)
    {
      String str1 = paramUri.getHost();
      if (str1 == null) {
        return -2;
      }
      String str2 = str1;
      if (mWild)
      {
        if (str1.length() < mHost.length()) {
          return -2;
        }
        str2 = str1.substring(str1.length() - mHost.length());
      }
      if (str2.compareToIgnoreCase(mHost) != 0) {
        return -2;
      }
      if (mPort >= 0)
      {
        if (mPort != paramUri.getPort()) {
          return -2;
        }
        return 4194304;
      }
      return 3145728;
    }
    
    public boolean match(AuthorityEntry paramAuthorityEntry)
    {
      if (mWild != mWild) {
        return false;
      }
      if (!mHost.equals(mHost)) {
        return false;
      }
      return mPort == mPort;
    }
    
    void writeToParcel(Parcel paramParcel)
    {
      paramParcel.writeString(mOrigHost);
      paramParcel.writeString(mHost);
      paramParcel.writeInt(mWild);
      paramParcel.writeInt(mPort);
    }
    
    void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
    {
      paramLong = paramProtoOutputStream.start(paramLong);
      paramProtoOutputStream.write(1138166333441L, mHost);
      paramProtoOutputStream.write(1133871366146L, mWild);
      paramProtoOutputStream.write(1120986464259L, mPort);
      paramProtoOutputStream.end(paramLong);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface InstantAppVisibility {}
  
  public static class MalformedMimeTypeException
    extends AndroidException
  {
    public MalformedMimeTypeException() {}
    
    public MalformedMimeTypeException(String paramString)
    {
      super();
    }
  }
}
