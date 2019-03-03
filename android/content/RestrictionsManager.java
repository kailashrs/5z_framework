package android.content;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import com.android.internal.R.styleable;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;

public class RestrictionsManager
{
  public static final String ACTION_PERMISSION_RESPONSE_RECEIVED = "android.content.action.PERMISSION_RESPONSE_RECEIVED";
  public static final String ACTION_REQUEST_LOCAL_APPROVAL = "android.content.action.REQUEST_LOCAL_APPROVAL";
  public static final String ACTION_REQUEST_PERMISSION = "android.content.action.REQUEST_PERMISSION";
  public static final String EXTRA_PACKAGE_NAME = "android.content.extra.PACKAGE_NAME";
  public static final String EXTRA_REQUEST_BUNDLE = "android.content.extra.REQUEST_BUNDLE";
  public static final String EXTRA_REQUEST_ID = "android.content.extra.REQUEST_ID";
  public static final String EXTRA_REQUEST_TYPE = "android.content.extra.REQUEST_TYPE";
  public static final String EXTRA_RESPONSE_BUNDLE = "android.content.extra.RESPONSE_BUNDLE";
  public static final String META_DATA_APP_RESTRICTIONS = "android.content.APP_RESTRICTIONS";
  public static final String REQUEST_KEY_APPROVE_LABEL = "android.request.approve_label";
  public static final String REQUEST_KEY_DATA = "android.request.data";
  public static final String REQUEST_KEY_DENY_LABEL = "android.request.deny_label";
  public static final String REQUEST_KEY_ICON = "android.request.icon";
  public static final String REQUEST_KEY_ID = "android.request.id";
  public static final String REQUEST_KEY_MESSAGE = "android.request.mesg";
  public static final String REQUEST_KEY_NEW_REQUEST = "android.request.new_request";
  public static final String REQUEST_KEY_TITLE = "android.request.title";
  public static final String REQUEST_TYPE_APPROVAL = "android.request.type.approval";
  public static final String RESPONSE_KEY_ERROR_CODE = "android.response.errorcode";
  public static final String RESPONSE_KEY_MESSAGE = "android.response.msg";
  public static final String RESPONSE_KEY_RESPONSE_TIMESTAMP = "android.response.timestamp";
  public static final String RESPONSE_KEY_RESULT = "android.response.result";
  public static final int RESULT_APPROVED = 1;
  public static final int RESULT_DENIED = 2;
  public static final int RESULT_ERROR = 5;
  public static final int RESULT_ERROR_BAD_REQUEST = 1;
  public static final int RESULT_ERROR_INTERNAL = 3;
  public static final int RESULT_ERROR_NETWORK = 2;
  public static final int RESULT_NO_RESPONSE = 3;
  public static final int RESULT_UNKNOWN_REQUEST = 4;
  private static final String TAG = "RestrictionsManager";
  private static final String TAG_RESTRICTION = "restriction";
  private final Context mContext;
  private final IRestrictionsManager mService;
  
  public RestrictionsManager(Context paramContext, IRestrictionsManager paramIRestrictionsManager)
  {
    mContext = paramContext;
    mService = paramIRestrictionsManager;
  }
  
  private static Bundle addRestrictionToBundle(Bundle paramBundle, RestrictionEntry paramRestrictionEntry)
  {
    Object localObject;
    switch (paramRestrictionEntry.getType())
    {
    default: 
      paramBundle = new StringBuilder();
      paramBundle.append("Unsupported restrictionEntry type: ");
      paramBundle.append(paramRestrictionEntry.getType());
      throw new IllegalArgumentException(paramBundle.toString());
    case 8: 
      localObject = paramRestrictionEntry.getRestrictions();
      Bundle[] arrayOfBundle = new Bundle[localObject.length];
      for (int i = 0; i < localObject.length; i++)
      {
        RestrictionEntry[] arrayOfRestrictionEntry = localObject[i].getRestrictions();
        if (arrayOfRestrictionEntry == null)
        {
          Log.w("RestrictionsManager", "addRestrictionToBundle: Non-bundle entry found in bundle array");
          arrayOfBundle[i] = new Bundle();
        }
        else
        {
          arrayOfBundle[i] = convertRestrictionsToBundle(Arrays.asList(arrayOfRestrictionEntry));
        }
      }
      paramBundle.putParcelableArray(paramRestrictionEntry.getKey(), arrayOfBundle);
      break;
    case 7: 
      localObject = convertRestrictionsToBundle(Arrays.asList(paramRestrictionEntry.getRestrictions()));
      paramBundle.putBundle(paramRestrictionEntry.getKey(), (Bundle)localObject);
      break;
    case 5: 
      paramBundle.putInt(paramRestrictionEntry.getKey(), paramRestrictionEntry.getIntValue());
      break;
    case 2: 
    case 3: 
    case 4: 
      paramBundle.putStringArray(paramRestrictionEntry.getKey(), paramRestrictionEntry.getAllSelectedStrings());
      break;
    case 1: 
      paramBundle.putBoolean(paramRestrictionEntry.getKey(), paramRestrictionEntry.getSelectedState());
      break;
    case 0: 
    case 6: 
      paramBundle.putString(paramRestrictionEntry.getKey(), paramRestrictionEntry.getSelectedString());
    }
    return paramBundle;
  }
  
  public static Bundle convertRestrictionsToBundle(List<RestrictionEntry> paramList)
  {
    Bundle localBundle = new Bundle();
    paramList = paramList.iterator();
    while (paramList.hasNext()) {
      addRestrictionToBundle(localBundle, (RestrictionEntry)paramList.next());
    }
    return localBundle;
  }
  
  private List<RestrictionEntry> loadManifestRestrictions(String paramString, XmlResourceParser paramXmlResourceParser)
  {
    try
    {
      Context localContext = mContext.createPackageContext(paramString, 0);
      ArrayList localArrayList = new ArrayList();
      try
      {
        for (int i = paramXmlResourceParser.next(); i != 1; i = paramXmlResourceParser.next()) {
          if (i == 2)
          {
            RestrictionEntry localRestrictionEntry = loadRestrictionElement(localContext, paramXmlResourceParser);
            if (localRestrictionEntry != null) {
              localArrayList.add(localRestrictionEntry);
            }
          }
        }
        return localArrayList;
      }
      catch (IOException localIOException)
      {
        paramXmlResourceParser = new StringBuilder();
        paramXmlResourceParser.append("Reading restriction metadata for ");
        paramXmlResourceParser.append(paramString);
        Log.w("RestrictionsManager", paramXmlResourceParser.toString(), localIOException);
        return null;
      }
      catch (XmlPullParserException localXmlPullParserException)
      {
        paramXmlResourceParser = new StringBuilder();
        paramXmlResourceParser.append("Reading restriction metadata for ");
        paramXmlResourceParser.append(paramString);
        Log.w("RestrictionsManager", paramXmlResourceParser.toString(), localXmlPullParserException);
        return null;
      }
      return null;
    }
    catch (PackageManager.NameNotFoundException paramString) {}
  }
  
  private RestrictionEntry loadRestriction(Context paramContext, TypedArray paramTypedArray, XmlResourceParser paramXmlResourceParser)
    throws IOException, XmlPullParserException
  {
    String str1 = paramTypedArray.getString(3);
    int i = paramTypedArray.getInt(6, -1);
    Object localObject = paramTypedArray.getString(2);
    String str2 = paramTypedArray.getString(0);
    int j = paramTypedArray.getResourceId(1, 0);
    int k = paramTypedArray.getResourceId(5, 0);
    if (i == -1)
    {
      Log.w("RestrictionsManager", "restrictionType cannot be omitted");
      return null;
    }
    if (str1 == null)
    {
      Log.w("RestrictionsManager", "key cannot be omitted");
      return null;
    }
    RestrictionEntry localRestrictionEntry = new RestrictionEntry(i, str1);
    localRestrictionEntry.setTitle((String)localObject);
    localRestrictionEntry.setDescription(str2);
    if (j != 0) {
      localRestrictionEntry.setChoiceEntries(paramContext, j);
    }
    if (k != 0) {
      localRestrictionEntry.setChoiceValues(paramContext, k);
    }
    switch (i)
    {
    case 3: 
    default: 
      paramContext = new StringBuilder();
      paramContext.append("Unknown restriction type ");
      paramContext.append(i);
      Log.w("RestrictionsManager", paramContext.toString());
      break;
    case 7: 
    case 8: 
      k = paramXmlResourceParser.getDepth();
      localObject = new ArrayList();
      for (;;)
      {
        paramTypedArray = paramXmlResourceParser;
        if (!XmlUtils.nextElementWithin(paramTypedArray, k)) {
          break;
        }
        paramTypedArray = loadRestrictionElement(paramContext, paramTypedArray);
        if (paramTypedArray == null)
        {
          paramTypedArray = new StringBuilder();
          paramTypedArray.append("Child entry cannot be loaded for bundle restriction ");
          paramTypedArray.append(str1);
          Log.w("RestrictionsManager", paramTypedArray.toString());
        }
        else
        {
          ((List)localObject).add(paramTypedArray);
          if ((i == 8) && (paramTypedArray.getType() != 7))
          {
            paramTypedArray = new StringBuilder();
            paramTypedArray.append("bundle_array ");
            paramTypedArray.append(str1);
            paramTypedArray.append(" can only contain entries of type bundle");
            Log.w("RestrictionsManager", paramTypedArray.toString());
          }
        }
      }
      localRestrictionEntry.setRestrictions((RestrictionEntry[])((List)localObject).toArray(new RestrictionEntry[((List)localObject).size()]));
      break;
    case 5: 
      localRestrictionEntry.setIntValue(paramTypedArray.getInt(4, 0));
      break;
    case 4: 
      i = paramTypedArray.getResourceId(4, 0);
      if (i != 0) {
        localRestrictionEntry.setAllSelectedStrings(paramContext.getResources().getStringArray(i));
      }
      break;
    case 1: 
      localRestrictionEntry.setSelectedState(paramTypedArray.getBoolean(4, false));
      break;
    case 0: 
    case 2: 
    case 6: 
      localRestrictionEntry.setSelectedString(paramTypedArray.getString(4));
    }
    return localRestrictionEntry;
  }
  
  private RestrictionEntry loadRestrictionElement(Context paramContext, XmlResourceParser paramXmlResourceParser)
    throws IOException, XmlPullParserException
  {
    if (paramXmlResourceParser.getName().equals("restriction"))
    {
      AttributeSet localAttributeSet = Xml.asAttributeSet(paramXmlResourceParser);
      if (localAttributeSet != null) {
        return loadRestriction(paramContext, paramContext.obtainStyledAttributes(localAttributeSet, R.styleable.RestrictionEntry), paramXmlResourceParser);
      }
    }
    return null;
  }
  
  public Intent createLocalApprovalIntent()
  {
    try
    {
      if (mService != null)
      {
        Intent localIntent = mService.createLocalApprovalIntent();
        return localIntent;
      }
      return null;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public Bundle getApplicationRestrictions()
  {
    try
    {
      if (mService != null)
      {
        Bundle localBundle = mService.getApplicationRestrictions(mContext.getPackageName());
        return localBundle;
      }
      return null;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<RestrictionEntry> getManifestRestrictions(String paramString)
  {
    try
    {
      ApplicationInfo localApplicationInfo = mContext.getPackageManager().getApplicationInfo(paramString, 128);
      if ((localApplicationInfo != null) && (metaData.containsKey("android.content.APP_RESTRICTIONS"))) {
        return loadManifestRestrictions(paramString, localApplicationInfo.loadXmlMetaData(mContext.getPackageManager(), "android.content.APP_RESTRICTIONS"));
      }
      return null;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("No such package ");
      localStringBuilder.append(paramString);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
  }
  
  public boolean hasRestrictionsProvider()
  {
    try
    {
      if (mService != null)
      {
        boolean bool = mService.hasRestrictionsProvider();
        return bool;
      }
      return false;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void notifyPermissionResponse(String paramString, PersistableBundle paramPersistableBundle)
  {
    if (paramString != null)
    {
      if (paramPersistableBundle != null)
      {
        if (paramPersistableBundle.containsKey("android.request.id"))
        {
          if (paramPersistableBundle.containsKey("android.response.result")) {
            try
            {
              if (mService != null) {
                mService.notifyPermissionResponse(paramString, paramPersistableBundle);
              }
              return;
            }
            catch (RemoteException paramString)
            {
              throw paramString.rethrowFromSystemServer();
            }
          }
          throw new IllegalArgumentException("RESPONSE_KEY_RESULT must be specified");
        }
        throw new IllegalArgumentException("REQUEST_KEY_ID must be specified");
      }
      throw new NullPointerException("request cannot be null");
    }
    throw new NullPointerException("packageName cannot be null");
  }
  
  public void requestPermission(String paramString1, String paramString2, PersistableBundle paramPersistableBundle)
  {
    if (paramString1 != null)
    {
      if (paramString2 != null)
      {
        if (paramPersistableBundle != null) {
          try
          {
            if (mService != null) {
              mService.requestPermission(mContext.getPackageName(), paramString1, paramString2, paramPersistableBundle);
            }
            return;
          }
          catch (RemoteException paramString1)
          {
            throw paramString1.rethrowFromSystemServer();
          }
        }
        throw new NullPointerException("request cannot be null");
      }
      throw new NullPointerException("requestId cannot be null");
    }
    throw new NullPointerException("requestType cannot be null");
  }
}
