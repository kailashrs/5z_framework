package android.service.autofill;

import android.app.AppGlobals;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.metrics.LogMaker;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Xml;
import com.android.internal.R.styleable;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.io.PrintWriter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class AutofillServiceInfo
{
  private static final String TAG = "AutofillServiceInfo";
  private static final String TAG_AUTOFILL_SERVICE = "autofill-service";
  private static final String TAG_COMPATIBILITY_PACKAGE = "compatibility-package";
  private final ArrayMap<String, Long> mCompatibilityPackages;
  private final ServiceInfo mServiceInfo;
  private final String mSettingsActivity;
  
  public AutofillServiceInfo(Context paramContext, ComponentName paramComponentName, int paramInt)
    throws PackageManager.NameNotFoundException
  {
    this(paramContext, getServiceInfoOrThrow(paramComponentName, paramInt));
  }
  
  public AutofillServiceInfo(Context paramContext, ServiceInfo paramServiceInfo)
  {
    if (!"android.permission.BIND_AUTOFILL_SERVICE".equals(permission)) {
      if ("android.permission.BIND_AUTOFILL".equals(permission))
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("AutofillService from '");
        ((StringBuilder)localObject1).append(packageName);
        ((StringBuilder)localObject1).append("' uses unsupported permission ");
        ((StringBuilder)localObject1).append("android.permission.BIND_AUTOFILL");
        ((StringBuilder)localObject1).append(". It works for now, but might not be supported on future releases");
        Log.w("AutofillServiceInfo", ((StringBuilder)localObject1).toString());
        new MetricsLogger().write(new LogMaker(1289).setPackageName(packageName));
      }
      else
      {
        paramContext = new StringBuilder();
        paramContext.append("AutofillService from '");
        paramContext.append(packageName);
        paramContext.append("' does not require permission ");
        paramContext.append("android.permission.BIND_AUTOFILL_SERVICE");
        Log.w("AutofillServiceInfo", paramContext.toString());
        throw new SecurityException("Service does not require permission android.permission.BIND_AUTOFILL_SERVICE");
      }
    }
    mServiceInfo = paramServiceInfo;
    XmlResourceParser localXmlResourceParser = paramServiceInfo.loadXmlMetaData(paramContext.getPackageManager(), "android.autofill");
    Object localObject2 = null;
    if (localXmlResourceParser == null)
    {
      mSettingsActivity = null;
      mCompatibilityPackages = null;
      return;
    }
    Object localObject3 = null;
    Object localObject4 = null;
    Object localObject5 = null;
    Object localObject6 = null;
    Object localObject1 = localObject3;
    try
    {
      Resources localResources = paramContext.getPackageManager().getResourcesForApplication(applicationInfo);
      for (int i = 0; (i != 1) && (i != 2); i = localXmlResourceParser.next()) {
        localObject1 = localObject3;
      }
      localObject1 = localObject3;
      if ("autofill-service".equals(localXmlResourceParser.getName()))
      {
        localObject1 = localObject3;
        paramServiceInfo = Xml.asAttributeSet(localXmlResourceParser);
        paramContext = localObject2;
        try
        {
          paramServiceInfo = localResources.obtainAttributes(paramServiceInfo, R.styleable.AutofillService);
          paramContext = paramServiceInfo;
          localObject1 = paramServiceInfo.getString(0);
          paramContext = (Context)localObject1;
          if (paramServiceInfo != null)
          {
            localObject1 = paramContext;
            paramServiceInfo.recycle();
          }
          localObject1 = paramContext;
          paramServiceInfo = parseCompatibilityPackages(localXmlResourceParser, localResources);
          localObject1 = paramServiceInfo;
          paramServiceInfo = paramContext;
          paramContext = (Context)localObject1;
        }
        finally
        {
          if (paramContext != null)
          {
            localObject1 = localObject3;
            paramContext.recycle();
          }
          localObject1 = localObject3;
        }
      }
      localObject1 = localObject3;
      Log.e("AutofillServiceInfo", "Meta-data does not start with autofill-service tag");
      paramContext = localObject6;
      paramServiceInfo = localObject4;
    }
    catch (PackageManager.NameNotFoundException|IOException|XmlPullParserException paramContext)
    {
      Log.e("AutofillServiceInfo", "Error parsing auto fill service meta-data", paramContext);
      paramContext = localObject5;
      paramServiceInfo = (ServiceInfo)localObject1;
    }
    mSettingsActivity = paramServiceInfo;
    mCompatibilityPackages = paramContext;
  }
  
  private static ServiceInfo getServiceInfoOrThrow(ComponentName paramComponentName, int paramInt)
    throws PackageManager.NameNotFoundException
  {
    try
    {
      ServiceInfo localServiceInfo = AppGlobals.getPackageManager().getServiceInfo(paramComponentName, 128, paramInt);
      if (localServiceInfo != null) {
        return localServiceInfo;
      }
    }
    catch (RemoteException localRemoteException) {}
    throw new PackageManager.NameNotFoundException(paramComponentName.toString());
  }
  
  private ArrayMap<String, Long> parseCompatibilityPackages(XmlPullParser paramXmlPullParser, Resources paramResources)
    throws IOException, XmlPullParserException
  {
    Object localObject1 = null;
    int i = paramXmlPullParser.getDepth();
    for (;;)
    {
      int j = paramXmlPullParser.next();
      if ((j != 1) && ((j != 3) || (paramXmlPullParser.getDepth() > i)))
      {
        if ((j == 3) || (j == 4) || (!"compatibility-package".equals(paramXmlPullParser.getName()))) {
          continue;
        }
        Object localObject2 = null;
        try
        {
          TypedArray localTypedArray = paramResources.obtainAttributes(Xml.asAttributeSet(paramXmlPullParser), R.styleable.AutofillService_CompatibilityPackage);
          localObject2 = localTypedArray;
          String str = localTypedArray.getString(0);
          localObject2 = localTypedArray;
          if (TextUtils.isEmpty(str))
          {
            localObject2 = localTypedArray;
            paramResources = new java/lang/StringBuilder;
            localObject2 = localTypedArray;
            paramResources.<init>();
            localObject2 = localTypedArray;
            paramResources.append("Invalid compatibility package:");
            localObject2 = localTypedArray;
            paramResources.append(str);
            localObject2 = localTypedArray;
            Log.e("AutofillServiceInfo", paramResources.toString());
            XmlUtils.skipCurrentTag(paramXmlPullParser);
            if (localTypedArray == null) {
              break;
            }
          }
          else
          {
            for (;;)
            {
              localTypedArray.recycle();
              return localObject1;
              localObject2 = localTypedArray;
              localObject3 = localTypedArray.getString(1);
              if (localObject3 == null) {
                break;
              }
              localObject2 = localTypedArray;
              try
              {
                localObject4 = Long.valueOf(Long.parseLong((String)localObject3));
                localObject3 = localObject4;
                localObject2 = localTypedArray;
                if (((Long)localObject4).longValue() >= 0L) {
                  break label369;
                }
                localObject2 = localTypedArray;
                paramResources = new java/lang/StringBuilder;
                localObject2 = localTypedArray;
                paramResources.<init>();
                localObject2 = localTypedArray;
                paramResources.append("Invalid compatibility max version code:");
                localObject2 = localTypedArray;
                paramResources.append(localObject4);
                localObject2 = localTypedArray;
                Log.e("AutofillServiceInfo", paramResources.toString());
                XmlUtils.skipCurrentTag(paramXmlPullParser);
                if (localTypedArray == null) {
                  return localObject1;
                }
              }
              catch (NumberFormatException paramResources)
              {
                localObject2 = localTypedArray;
                paramResources = new java/lang/StringBuilder;
                localObject2 = localTypedArray;
                paramResources.<init>();
                localObject2 = localTypedArray;
                paramResources.append("Invalid compatibility max version code:");
                localObject2 = localTypedArray;
                paramResources.append((String)localObject3);
                localObject2 = localTypedArray;
                Log.e("AutofillServiceInfo", paramResources.toString());
                XmlUtils.skipCurrentTag(paramXmlPullParser);
                if (localTypedArray == null) {
                  return localObject1;
                }
              }
            }
            localObject2 = localTypedArray;
            Object localObject3 = Long.valueOf(Long.MAX_VALUE);
            label369:
            Object localObject4 = localObject1;
            if (localObject1 == null)
            {
              localObject2 = localTypedArray;
              localObject4 = new android/util/ArrayMap;
              localObject2 = localTypedArray;
              ((ArrayMap)localObject4).<init>();
            }
            localObject2 = localTypedArray;
            ((ArrayMap)localObject4).put(str, localObject3);
            XmlUtils.skipCurrentTag(paramXmlPullParser);
            if (localTypedArray != null) {
              localTypedArray.recycle();
            }
            localObject1 = localObject4;
            continue;
          }
        }
        finally
        {
          XmlUtils.skipCurrentTag(paramXmlPullParser);
          if (localObject2 != null) {
            localObject2.recycle();
          }
        }
      }
    }
    return localObject1;
  }
  
  public void dump(String paramString, PrintWriter paramPrintWriter)
  {
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("Component: ");
    paramPrintWriter.println(getServiceInfo().getComponentName());
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("Settings: ");
    paramPrintWriter.println(mSettingsActivity);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("Compat packages: ");
    paramPrintWriter.println(mCompatibilityPackages);
  }
  
  public ArrayMap<String, Long> getCompatibilityPackages()
  {
    return mCompatibilityPackages;
  }
  
  public ServiceInfo getServiceInfo()
  {
    return mServiceInfo;
  }
  
  public String getSettingsActivity()
  {
    return mSettingsActivity;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(getClass().getSimpleName());
    localStringBuilder.append("[");
    localStringBuilder.append(mServiceInfo);
    localStringBuilder.append(", settings:");
    localStringBuilder.append(mSettingsActivity);
    localStringBuilder.append(", hasCompatPckgs:");
    boolean bool;
    if ((mCompatibilityPackages != null) && (!mCompatibilityPackages.isEmpty())) {
      bool = true;
    } else {
      bool = false;
    }
    localStringBuilder.append(bool);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
}
