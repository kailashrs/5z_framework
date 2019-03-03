package android.content;

import android.content.pm.RegisteredServicesCache;
import android.content.pm.RegisteredServicesCache.ServiceInfo;
import android.content.pm.XmlSerializerAndParser;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.SparseArray;
import com.android.internal.R.styleable;
import com.android.internal.annotations.GuardedBy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class SyncAdaptersCache
  extends RegisteredServicesCache<SyncAdapterType>
{
  private static final String ATTRIBUTES_NAME = "sync-adapter";
  private static final String SERVICE_INTERFACE = "android.content.SyncAdapter";
  private static final String SERVICE_META_DATA = "android.content.SyncAdapter";
  private static final String TAG = "Account";
  private static final MySerializer sSerializer = new MySerializer();
  @GuardedBy("mServicesLock")
  private SparseArray<ArrayMap<String, String[]>> mAuthorityToSyncAdapters = new SparseArray();
  
  public SyncAdaptersCache(Context paramContext)
  {
    super(paramContext, "android.content.SyncAdapter", "android.content.SyncAdapter", "sync-adapter", sSerializer);
  }
  
  public String[] getSyncAdapterPackagesForAuthority(String paramString, int paramInt)
  {
    synchronized (mServicesLock)
    {
      Object localObject2 = (ArrayMap)mAuthorityToSyncAdapters.get(paramInt);
      Object localObject3 = localObject2;
      if (localObject2 == null)
      {
        localObject3 = new android/util/ArrayMap;
        ((ArrayMap)localObject3).<init>();
        mAuthorityToSyncAdapters.put(paramInt, localObject3);
      }
      if (((ArrayMap)localObject3).containsKey(paramString))
      {
        paramString = (String[])((ArrayMap)localObject3).get(paramString);
        return paramString;
      }
      Object localObject4 = getAllServices(paramInt);
      localObject2 = new java/util/ArrayList;
      ((ArrayList)localObject2).<init>();
      Iterator localIterator = ((Collection)localObject4).iterator();
      while (localIterator.hasNext())
      {
        localObject4 = (RegisteredServicesCache.ServiceInfo)localIterator.next();
        if ((paramString.equals(type).authority)) && (componentName != null)) {
          ((ArrayList)localObject2).add(componentName.getPackageName());
        }
      }
      localObject4 = new String[((ArrayList)localObject2).size()];
      ((ArrayList)localObject2).toArray((Object[])localObject4);
      ((ArrayMap)localObject3).put(paramString, localObject4);
      return localObject4;
    }
  }
  
  protected void onServicesChangedLocked(int paramInt)
  {
    synchronized (mServicesLock)
    {
      ArrayMap localArrayMap = (ArrayMap)mAuthorityToSyncAdapters.get(paramInt);
      if (localArrayMap != null) {
        localArrayMap.clear();
      }
      super.onServicesChangedLocked(paramInt);
      return;
    }
  }
  
  protected void onUserRemoved(int paramInt)
  {
    synchronized (mServicesLock)
    {
      mAuthorityToSyncAdapters.remove(paramInt);
      super.onUserRemoved(paramInt);
      return;
    }
  }
  
  public SyncAdapterType parseServiceAttributes(Resources paramResources, String paramString, AttributeSet paramAttributeSet)
  {
    paramResources = paramResources.obtainAttributes(paramAttributeSet, R.styleable.SyncAdapter);
    try
    {
      String str = paramResources.getString(2);
      paramAttributeSet = paramResources.getString(1);
      if ((!TextUtils.isEmpty(str)) && (!TextUtils.isEmpty(paramAttributeSet)))
      {
        boolean bool1 = paramResources.getBoolean(3, true);
        boolean bool2 = paramResources.getBoolean(4, true);
        boolean bool3 = paramResources.getBoolean(6, false);
        boolean bool4 = paramResources.getBoolean(5, false);
        paramString = new SyncAdapterType(str, paramAttributeSet, bool1, bool2, bool3, bool4, paramResources.getString(0), paramString);
        return paramString;
      }
      return null;
    }
    finally
    {
      paramResources.recycle();
    }
  }
  
  static class MySerializer
    implements XmlSerializerAndParser<SyncAdapterType>
  {
    MySerializer() {}
    
    public SyncAdapterType createFromXml(XmlPullParser paramXmlPullParser)
      throws IOException, XmlPullParserException
    {
      return SyncAdapterType.newKey(paramXmlPullParser.getAttributeValue(null, "authority"), paramXmlPullParser.getAttributeValue(null, "accountType"));
    }
    
    public void writeAsXml(SyncAdapterType paramSyncAdapterType, XmlSerializer paramXmlSerializer)
      throws IOException
    {
      paramXmlSerializer.attribute(null, "authority", authority);
      paramXmlSerializer.attribute(null, "accountType", accountType);
    }
  }
}
