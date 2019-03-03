package android.content.pm;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Environment;
import android.os.Handler;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.AtomicFile;
import android.util.AttributeSet;
import android.util.IntArray;
import android.util.Log;
import android.util.SparseArray;
import android.util.Xml;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FastXmlSerializer;
import com.google.android.collect.Lists;
import com.google.android.collect.Maps;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public abstract class RegisteredServicesCache<V>
{
  private static final boolean DEBUG = false;
  protected static final String REGISTERED_SERVICES_DIR = "registered_services";
  private static final String TAG = "PackageManager";
  private final String mAttributesName;
  public final Context mContext;
  private final BroadcastReceiver mExternalReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      RegisteredServicesCache.this.handlePackageEvent(paramAnonymousIntent, 0);
    }
  };
  private Handler mHandler;
  private final String mInterfaceName;
  private RegisteredServicesCacheListener<V> mListener;
  private final String mMetaDataName;
  private final BroadcastReceiver mPackageReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      int i = paramAnonymousIntent.getIntExtra("android.intent.extra.UID", -1);
      if (i != -1) {
        RegisteredServicesCache.this.handlePackageEvent(paramAnonymousIntent, UserHandle.getUserId(i));
      }
    }
  };
  private final XmlSerializerAndParser<V> mSerializerAndParser;
  protected final Object mServicesLock = new Object();
  private final BroadcastReceiver mUserRemovedReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      int i = paramAnonymousIntent.getIntExtra("android.intent.extra.user_handle", -1);
      onUserRemoved(i);
    }
  };
  @GuardedBy("mServicesLock")
  private final SparseArray<UserServices<V>> mUserServices = new SparseArray(2);
  
  public RegisteredServicesCache(Context paramContext, String paramString1, String paramString2, String paramString3, XmlSerializerAndParser<V> paramXmlSerializerAndParser)
  {
    mContext = paramContext;
    mInterfaceName = paramString1;
    mMetaDataName = paramString2;
    mAttributesName = paramString3;
    mSerializerAndParser = paramXmlSerializerAndParser;
    migrateIfNecessaryLocked();
    paramContext = new IntentFilter();
    paramContext.addAction("android.intent.action.PACKAGE_ADDED");
    paramContext.addAction("android.intent.action.PACKAGE_CHANGED");
    paramContext.addAction("android.intent.action.PACKAGE_REMOVED");
    paramContext.addDataScheme("package");
    mContext.registerReceiverAsUser(mPackageReceiver, UserHandle.ALL, paramContext, null, null);
    paramString1 = new IntentFilter();
    paramString1.addAction("android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE");
    paramString1.addAction("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE");
    mContext.registerReceiver(mExternalReceiver, paramString1);
    paramContext = new IntentFilter();
    paramString1.addAction("android.intent.action.USER_REMOVED");
    mContext.registerReceiver(mUserRemovedReceiver, paramContext);
  }
  
  private boolean containsType(ArrayList<ServiceInfo<V>> paramArrayList, V paramV)
  {
    int i = 0;
    int j = paramArrayList.size();
    while (i < j)
    {
      if (gettype.equals(paramV)) {
        return true;
      }
      i++;
    }
    return false;
  }
  
  private boolean containsTypeAndUid(ArrayList<ServiceInfo<V>> paramArrayList, V paramV, int paramInt)
  {
    int i = 0;
    int j = paramArrayList.size();
    while (i < j)
    {
      ServiceInfo localServiceInfo = (ServiceInfo)paramArrayList.get(i);
      if ((type.equals(paramV)) && (uid == paramInt)) {
        return true;
      }
      i++;
    }
    return false;
  }
  
  private boolean containsUid(int[] paramArrayOfInt, int paramInt)
  {
    boolean bool;
    if ((paramArrayOfInt != null) && (!ArrayUtils.contains(paramArrayOfInt, paramInt))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private AtomicFile createFileForUser(int paramInt)
  {
    File localFile = getUserSystemDirectory(paramInt);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("registered_services/");
    localStringBuilder.append(mInterfaceName);
    localStringBuilder.append(".xml");
    return new AtomicFile(new File(localFile, localStringBuilder.toString()));
  }
  
  @GuardedBy("mServicesLock")
  private UserServices<V> findOrCreateUserLocked(int paramInt)
  {
    return findOrCreateUserLocked(paramInt, true);
  }
  
  /* Error */
  @GuardedBy("mServicesLock")
  private UserServices<V> findOrCreateUserLocked(int paramInt, boolean paramBoolean)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 67	android/content/pm/RegisteredServicesCache:mUserServices	Landroid/util/SparseArray;
    //   4: iload_1
    //   5: invokevirtual 215	android/util/SparseArray:get	(I)Ljava/lang/Object;
    //   8: checkcast 18	android/content/pm/RegisteredServicesCache$UserServices
    //   11: astore_3
    //   12: aload_3
    //   13: astore 4
    //   15: aload_3
    //   16: ifnonnull +204 -> 220
    //   19: aconst_null
    //   20: astore 5
    //   22: aconst_null
    //   23: astore_3
    //   24: new 18	android/content/pm/RegisteredServicesCache$UserServices
    //   27: dup
    //   28: aconst_null
    //   29: invokespecial 218	android/content/pm/RegisteredServicesCache$UserServices:<init>	(Landroid/content/pm/RegisteredServicesCache$1;)V
    //   32: astore 6
    //   34: aload_0
    //   35: getfield 67	android/content/pm/RegisteredServicesCache:mUserServices	Landroid/util/SparseArray;
    //   38: iload_1
    //   39: aload 6
    //   41: invokevirtual 222	android/util/SparseArray:put	(ILjava/lang/Object;)V
    //   44: aload 6
    //   46: astore 4
    //   48: iload_2
    //   49: ifeq +171 -> 220
    //   52: aload 6
    //   54: astore 4
    //   56: aload_0
    //   57: getfield 88	android/content/pm/RegisteredServicesCache:mSerializerAndParser	Landroid/content/pm/XmlSerializerAndParser;
    //   60: ifnull +160 -> 220
    //   63: aload_0
    //   64: iload_1
    //   65: invokevirtual 226	android/content/pm/RegisteredServicesCache:getUser	(I)Landroid/content/pm/UserInfo;
    //   68: astore 7
    //   70: aload 6
    //   72: astore 4
    //   74: aload 7
    //   76: ifnull +144 -> 220
    //   79: aload_0
    //   80: aload 7
    //   82: getfield 231	android/content/pm/UserInfo:id	I
    //   85: invokespecial 233	android/content/pm/RegisteredServicesCache:createFileForUser	(I)Landroid/util/AtomicFile;
    //   88: astore 8
    //   90: aload 6
    //   92: astore 4
    //   94: aload 8
    //   96: invokevirtual 237	android/util/AtomicFile:getBaseFile	()Ljava/io/File;
    //   99: invokevirtual 241	java/io/File:exists	()Z
    //   102: ifeq +118 -> 220
    //   105: aload 5
    //   107: astore 4
    //   109: aload 8
    //   111: invokevirtual 245	android/util/AtomicFile:openRead	()Ljava/io/FileInputStream;
    //   114: astore 5
    //   116: aload 5
    //   118: astore_3
    //   119: aload 5
    //   121: astore 4
    //   123: aload_0
    //   124: aload 5
    //   126: invokespecial 249	android/content/pm/RegisteredServicesCache:readPersistentServicesLocked	(Ljava/io/InputStream;)V
    //   129: aload 5
    //   131: astore 4
    //   133: aload 4
    //   135: invokestatic 255	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   138: aload 6
    //   140: astore 4
    //   142: goto +78 -> 220
    //   145: astore 4
    //   147: goto +66 -> 213
    //   150: astore 8
    //   152: aload 4
    //   154: astore_3
    //   155: new 182	java/lang/StringBuilder
    //   158: astore 5
    //   160: aload 4
    //   162: astore_3
    //   163: aload 5
    //   165: invokespecial 183	java/lang/StringBuilder:<init>	()V
    //   168: aload 4
    //   170: astore_3
    //   171: aload 5
    //   173: ldc_w 257
    //   176: invokevirtual 189	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   179: pop
    //   180: aload 4
    //   182: astore_3
    //   183: aload 5
    //   185: aload 7
    //   187: getfield 231	android/content/pm/UserInfo:id	I
    //   190: invokevirtual 260	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   193: pop
    //   194: aload 4
    //   196: astore_3
    //   197: ldc 29
    //   199: aload 5
    //   201: invokevirtual 199	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   204: aload 8
    //   206: invokestatic 266	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   209: pop
    //   210: goto -77 -> 133
    //   213: aload_3
    //   214: invokestatic 255	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   217: aload 4
    //   219: athrow
    //   220: aload 4
    //   222: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	223	0	this	RegisteredServicesCache
    //   0	223	1	paramInt	int
    //   0	223	2	paramBoolean	boolean
    //   11	203	3	localObject1	Object
    //   13	128	4	localObject2	Object
    //   145	76	4	localUserServices	UserServices<V>
    //   20	180	5	localObject3	Object
    //   32	107	6	localUserServices1	UserServices
    //   68	118	7	localUserInfo	UserInfo
    //   88	22	8	localAtomicFile	AtomicFile
    //   150	55	8	localException	Exception
    // Exception table:
    //   from	to	target	type
    //   109	116	145	finally
    //   123	129	145	finally
    //   155	160	145	finally
    //   163	168	145	finally
    //   171	180	145	finally
    //   183	194	145	finally
    //   197	210	145	finally
    //   109	116	150	java/lang/Exception
    //   123	129	150	java/lang/Exception
  }
  
  private void generateServicesMap(int[] paramArrayOfInt, int paramInt)
  {
    Object localObject1 = new ArrayList();
    Object localObject2 = queryIntentServices(paramInt).iterator();
    Object localObject4;
    while (((Iterator)localObject2).hasNext())
    {
      ??? = (ResolveInfo)((Iterator)localObject2).next();
      try
      {
        localObject4 = parseServiceInfo((ResolveInfo)???);
        if (localObject4 == null)
        {
          localObject4 = new java/lang/StringBuilder;
          ((StringBuilder)localObject4).<init>();
          ((StringBuilder)localObject4).append("Unable to load service info ");
          ((StringBuilder)localObject4).append(((ResolveInfo)???).toString());
          Log.w("PackageManager", ((StringBuilder)localObject4).toString());
        }
        else
        {
          ((ArrayList)localObject1).add(localObject4);
        }
      }
      catch (XmlPullParserException|IOException localXmlPullParserException)
      {
        localObject4 = new StringBuilder();
        ((StringBuilder)localObject4).append("Unable to load service info ");
        ((StringBuilder)localObject4).append(((ResolveInfo)???).toString());
        Log.w("PackageManager", ((StringBuilder)localObject4).toString(), localXmlPullParserException);
      }
    }
    synchronized (mServicesLock)
    {
      localObject2 = findOrCreateUserLocked(paramInt);
      int i;
      if (services == null) {
        i = 1;
      } else {
        i = 0;
      }
      if (i != 0) {
        services = Maps.newHashMap();
      }
      new StringBuilder();
      int j = 0;
      Object localObject5 = ((ArrayList)localObject1).iterator();
      while (((Iterator)localObject5).hasNext())
      {
        localObject4 = (ServiceInfo)((Iterator)localObject5).next();
        localObject6 = (Integer)persistentServices.get(type);
        if (localObject6 == null)
        {
          int k = 1;
          services.put(type, localObject4);
          persistentServices.put(type, Integer.valueOf(uid));
          if (mPersistentServicesFileDidNotExist)
          {
            j = k;
            if (i != 0) {}
          }
          else
          {
            notifyListener(type, paramInt, false);
            j = k;
          }
        }
        else if (((Integer)localObject6).intValue() == uid)
        {
          services.put(type, localObject4);
        }
        else if ((inSystemImage(uid)) || (!containsTypeAndUid((ArrayList)localObject1, type, ((Integer)localObject6).intValue())))
        {
          services.put(type, localObject4);
          persistentServices.put(type, Integer.valueOf(uid));
          notifyListener(type, paramInt, false);
          j = 1;
        }
      }
      Object localObject6 = Lists.newArrayList();
      localObject4 = persistentServices.keySet().iterator();
      for (;;)
      {
        if (((Iterator)localObject4).hasNext())
        {
          localObject5 = ((Iterator)localObject4).next();
          if (!containsType((ArrayList)localObject1, localObject5)) {
            i = ((Integer)persistentServices.get(localObject5)).intValue();
          }
        }
        try
        {
          if (containsUid(paramArrayOfInt, i)) {
            ((ArrayList)localObject6).add(localObject5);
          }
        }
        finally
        {
          for (;;) {}
        }
      }
      paramArrayOfInt = ((ArrayList)localObject6).iterator();
      while (paramArrayOfInt.hasNext())
      {
        localObject1 = paramArrayOfInt.next();
        j = 1;
        persistentServices.remove(localObject1);
        services.remove(localObject1);
        notifyListener(localObject1, paramInt, true);
      }
      if (j != 0)
      {
        onServicesChangedLocked(paramInt);
        writePersistentServicesLocked((UserServices)localObject2, paramInt);
      }
      return;
    }
  }
  
  private final void handlePackageEvent(Intent paramIntent, int paramInt)
  {
    String str = paramIntent.getAction();
    int i;
    if ((!"android.intent.action.PACKAGE_REMOVED".equals(str)) && (!"android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE".equals(str))) {
      i = 0;
    } else {
      i = 1;
    }
    boolean bool = paramIntent.getBooleanExtra("android.intent.extra.REPLACING", false);
    if ((i == 0) || (!bool))
    {
      Object localObject = null;
      if ((!"android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE".equals(str)) && (!"android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE".equals(str)))
      {
        i = paramIntent.getIntExtra("android.intent.extra.UID", -1);
        paramIntent = localObject;
        if (i > 0) {
          paramIntent = new int[] { i };
        }
      }
      else
      {
        paramIntent = paramIntent.getIntArrayExtra("android.intent.extra.changed_uid_list");
      }
      generateServicesMap(paramIntent, paramInt);
    }
  }
  
  /* Error */
  private void migrateIfNecessaryLocked()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 88	android/content/pm/RegisteredServicesCache:mSerializerAndParser	Landroid/content/pm/XmlSerializerAndParser;
    //   4: ifnonnull +4 -> 8
    //   7: return
    //   8: new 195	java/io/File
    //   11: dup
    //   12: new 195	java/io/File
    //   15: dup
    //   16: aload_0
    //   17: invokevirtual 412	android/content/pm/RegisteredServicesCache:getDataDirectory	()Ljava/io/File;
    //   20: ldc_w 414
    //   23: invokespecial 202	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   26: ldc 26
    //   28: invokespecial 202	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   31: astore_1
    //   32: new 182	java/lang/StringBuilder
    //   35: dup
    //   36: invokespecial 183	java/lang/StringBuilder:<init>	()V
    //   39: astore_2
    //   40: aload_2
    //   41: aload_0
    //   42: getfield 82	android/content/pm/RegisteredServicesCache:mInterfaceName	Ljava/lang/String;
    //   45: invokevirtual 189	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   48: pop
    //   49: aload_2
    //   50: ldc -65
    //   52: invokevirtual 189	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   55: pop
    //   56: new 193	android/util/AtomicFile
    //   59: dup
    //   60: new 195	java/io/File
    //   63: dup
    //   64: aload_1
    //   65: aload_2
    //   66: invokevirtual 199	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   69: invokespecial 202	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   72: invokespecial 205	android/util/AtomicFile:<init>	(Ljava/io/File;)V
    //   75: astore_3
    //   76: aload_3
    //   77: invokevirtual 237	android/util/AtomicFile:getBaseFile	()Ljava/io/File;
    //   80: invokevirtual 241	java/io/File:exists	()Z
    //   83: ifeq +204 -> 287
    //   86: new 182	java/lang/StringBuilder
    //   89: dup
    //   90: invokespecial 183	java/lang/StringBuilder:<init>	()V
    //   93: astore_2
    //   94: aload_2
    //   95: aload_0
    //   96: getfield 82	android/content/pm/RegisteredServicesCache:mInterfaceName	Ljava/lang/String;
    //   99: invokevirtual 189	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   102: pop
    //   103: aload_2
    //   104: ldc_w 416
    //   107: invokevirtual 189	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   110: pop
    //   111: new 195	java/io/File
    //   114: dup
    //   115: aload_1
    //   116: aload_2
    //   117: invokevirtual 199	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   120: invokespecial 202	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   123: astore 4
    //   125: aload 4
    //   127: invokevirtual 241	java/io/File:exists	()Z
    //   130: ifne +157 -> 287
    //   133: aconst_null
    //   134: astore_1
    //   135: aconst_null
    //   136: astore_2
    //   137: aload_3
    //   138: invokevirtual 245	android/util/AtomicFile:openRead	()Ljava/io/FileInputStream;
    //   141: astore_3
    //   142: aload_3
    //   143: astore_2
    //   144: aload_3
    //   145: astore_1
    //   146: aload_0
    //   147: getfield 67	android/content/pm/RegisteredServicesCache:mUserServices	Landroid/util/SparseArray;
    //   150: invokevirtual 419	android/util/SparseArray:clear	()V
    //   153: aload_3
    //   154: astore_2
    //   155: aload_3
    //   156: astore_1
    //   157: aload_0
    //   158: aload_3
    //   159: invokespecial 249	android/content/pm/RegisteredServicesCache:readPersistentServicesLocked	(Ljava/io/InputStream;)V
    //   162: aload_3
    //   163: astore_1
    //   164: aload_1
    //   165: invokestatic 255	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   168: goto +23 -> 191
    //   171: astore_1
    //   172: goto +109 -> 281
    //   175: astore_3
    //   176: aload_1
    //   177: astore_2
    //   178: ldc 29
    //   180: ldc_w 421
    //   183: aload_3
    //   184: invokestatic 266	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   187: pop
    //   188: goto -24 -> 164
    //   191: aload_0
    //   192: invokevirtual 425	android/content/pm/RegisteredServicesCache:getUsers	()Ljava/util/List;
    //   195: invokeinterface 284 1 0
    //   200: astore_1
    //   201: aload_1
    //   202: invokeinterface 289 1 0
    //   207: ifeq +44 -> 251
    //   210: aload_1
    //   211: invokeinterface 293 1 0
    //   216: checkcast 228	android/content/pm/UserInfo
    //   219: astore_3
    //   220: aload_0
    //   221: getfield 67	android/content/pm/RegisteredServicesCache:mUserServices	Landroid/util/SparseArray;
    //   224: aload_3
    //   225: getfield 231	android/content/pm/UserInfo:id	I
    //   228: invokevirtual 215	android/util/SparseArray:get	(I)Ljava/lang/Object;
    //   231: checkcast 18	android/content/pm/RegisteredServicesCache$UserServices
    //   234: astore_2
    //   235: aload_2
    //   236: ifnull +12 -> 248
    //   239: aload_0
    //   240: aload_2
    //   241: aload_3
    //   242: getfield 231	android/content/pm/UserInfo:id	I
    //   245: invokespecial 381	android/content/pm/RegisteredServicesCache:writePersistentServicesLocked	(Landroid/content/pm/RegisteredServicesCache$UserServices;I)V
    //   248: goto -47 -> 201
    //   251: aload 4
    //   253: invokevirtual 428	java/io/File:createNewFile	()Z
    //   256: pop
    //   257: goto +14 -> 271
    //   260: astore_1
    //   261: ldc 29
    //   263: ldc_w 430
    //   266: aload_1
    //   267: invokestatic 266	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   270: pop
    //   271: aload_0
    //   272: getfield 67	android/content/pm/RegisteredServicesCache:mUserServices	Landroid/util/SparseArray;
    //   275: invokevirtual 419	android/util/SparseArray:clear	()V
    //   278: goto +9 -> 287
    //   281: aload_2
    //   282: invokestatic 255	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   285: aload_1
    //   286: athrow
    //   287: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	288	0	this	RegisteredServicesCache
    //   31	134	1	localObject1	Object
    //   171	6	1	localObject2	Object
    //   200	11	1	localIterator	Iterator
    //   260	26	1	localException1	Exception
    //   39	243	2	localObject3	Object
    //   75	88	3	localObject4	Object
    //   175	9	3	localException2	Exception
    //   219	23	3	localUserInfo	UserInfo
    //   123	129	4	localFile	File
    // Exception table:
    //   from	to	target	type
    //   137	142	171	finally
    //   146	153	171	finally
    //   157	162	171	finally
    //   178	188	171	finally
    //   137	142	175	java/lang/Exception
    //   146	153	175	java/lang/Exception
    //   157	162	175	java/lang/Exception
    //   191	201	260	java/lang/Exception
    //   201	235	260	java/lang/Exception
    //   239	248	260	java/lang/Exception
    //   251	257	260	java/lang/Exception
  }
  
  private void notifyListener(final V paramV, final int paramInt, final boolean paramBoolean)
  {
    try
    {
      final RegisteredServicesCacheListener localRegisteredServicesCacheListener = mListener;
      Handler localHandler = mHandler;
      if (localRegisteredServicesCacheListener == null) {
        return;
      }
      localHandler.post(new Runnable()
      {
        public void run()
        {
          localRegisteredServicesCacheListener.onServiceChanged(paramV, paramInt, paramBoolean);
        }
      });
      return;
    }
    finally {}
  }
  
  private void readPersistentServicesLocked(InputStream paramInputStream)
    throws XmlPullParserException, IOException
  {
    XmlPullParser localXmlPullParser = Xml.newPullParser();
    localXmlPullParser.setInput(paramInputStream, StandardCharsets.UTF_8.name());
    for (int i = localXmlPullParser.getEventType(); (i != 2) && (i != 1); i = localXmlPullParser.next()) {}
    if ("services".equals(localXmlPullParser.getName()))
    {
      i = localXmlPullParser.next();
      int j;
      do
      {
        if ((i == 2) && (localXmlPullParser.getDepth() == 2) && ("service".equals(localXmlPullParser.getName())))
        {
          paramInputStream = mSerializerAndParser.createFromXml(localXmlPullParser);
          if (paramInputStream == null) {
            break;
          }
          i = Integer.parseInt(localXmlPullParser.getAttributeValue(null, "uid"));
          findOrCreateUserLockedgetUserIdpersistentServices.put(paramInputStream, Integer.valueOf(i));
        }
        j = localXmlPullParser.next();
        i = j;
      } while (j != 1);
    }
  }
  
  private void writePersistentServicesLocked(UserServices<V> paramUserServices, int paramInt)
  {
    if (mSerializerAndParser == null) {
      return;
    }
    AtomicFile localAtomicFile = createFileForUser(paramInt);
    Object localObject = null;
    try
    {
      FileOutputStream localFileOutputStream = localAtomicFile.startWrite();
      localObject = localFileOutputStream;
      FastXmlSerializer localFastXmlSerializer = new com/android/internal/util/FastXmlSerializer;
      localObject = localFileOutputStream;
      localFastXmlSerializer.<init>();
      localObject = localFileOutputStream;
      localFastXmlSerializer.setOutput(localFileOutputStream, StandardCharsets.UTF_8.name());
      localObject = localFileOutputStream;
      localFastXmlSerializer.startDocument(null, Boolean.valueOf(true));
      localObject = localFileOutputStream;
      localFastXmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
      localObject = localFileOutputStream;
      localFastXmlSerializer.startTag(null, "services");
      localObject = localFileOutputStream;
      Iterator localIterator = persistentServices.entrySet().iterator();
      for (;;)
      {
        localObject = localFileOutputStream;
        if (!localIterator.hasNext()) {
          break;
        }
        localObject = localFileOutputStream;
        paramUserServices = (Map.Entry)localIterator.next();
        localObject = localFileOutputStream;
        localFastXmlSerializer.startTag(null, "service");
        localObject = localFileOutputStream;
        localFastXmlSerializer.attribute(null, "uid", Integer.toString(((Integer)paramUserServices.getValue()).intValue()));
        localObject = localFileOutputStream;
        mSerializerAndParser.writeAsXml(paramUserServices.getKey(), localFastXmlSerializer);
        localObject = localFileOutputStream;
        localFastXmlSerializer.endTag(null, "service");
      }
      localObject = localFileOutputStream;
      localFastXmlSerializer.endTag(null, "services");
      localObject = localFileOutputStream;
      localFastXmlSerializer.endDocument();
      localObject = localFileOutputStream;
      localAtomicFile.finishWrite(localFileOutputStream);
    }
    catch (IOException paramUserServices)
    {
      Log.w("PackageManager", "Error writing accounts", paramUserServices);
      if (localObject != null) {
        localAtomicFile.failWrite(localObject);
      }
    }
  }
  
  public void dump(FileDescriptor arg1, PrintWriter paramPrintWriter, String[] paramArrayOfString, int paramInt)
  {
    synchronized (mServicesLock)
    {
      paramArrayOfString = findOrCreateUserLocked(paramInt);
      if (services != null)
      {
        Object localObject = new java/lang/StringBuilder;
        ((StringBuilder)localObject).<init>();
        ((StringBuilder)localObject).append("RegisteredServicesCache: ");
        ((StringBuilder)localObject).append(services.size());
        ((StringBuilder)localObject).append(" services");
        paramPrintWriter.println(((StringBuilder)localObject).toString());
        localObject = services.values().iterator();
        while (((Iterator)localObject).hasNext())
        {
          paramArrayOfString = (ServiceInfo)((Iterator)localObject).next();
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("  ");
          localStringBuilder.append(paramArrayOfString);
          paramPrintWriter.println(localStringBuilder.toString());
        }
      }
      paramPrintWriter.println("RegisteredServicesCache: services not loaded");
      return;
    }
  }
  
  public Collection<ServiceInfo<V>> getAllServices(int paramInt)
  {
    synchronized (mServicesLock)
    {
      Object localObject2 = findOrCreateUserLocked(paramInt);
      if (services == null) {
        generateServicesMap(null, paramInt);
      }
      ArrayList localArrayList = new java/util/ArrayList;
      localArrayList.<init>(services.values());
      localObject2 = Collections.unmodifiableCollection(localArrayList);
      return localObject2;
    }
  }
  
  public boolean getBindInstantServiceAllowed(int paramInt)
  {
    mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_BIND_INSTANT_SERVICE", "getBindInstantServiceAllowed");
    synchronized (mServicesLock)
    {
      boolean bool = findOrCreateUserLockedmBindInstantServiceAllowed;
      return bool;
    }
  }
  
  @VisibleForTesting
  protected File getDataDirectory()
  {
    return Environment.getDataDirectory();
  }
  
  public RegisteredServicesCacheListener<V> getListener()
  {
    try
    {
      RegisteredServicesCacheListener localRegisteredServicesCacheListener = mListener;
      return localRegisteredServicesCacheListener;
    }
    finally {}
  }
  
  @VisibleForTesting
  protected Map<V, Integer> getPersistentServices(int paramInt)
  {
    return findOrCreateUserLockedpersistentServices;
  }
  
  public ServiceInfo<V> getServiceInfo(V paramV, int paramInt)
  {
    synchronized (mServicesLock)
    {
      UserServices localUserServices = findOrCreateUserLocked(paramInt);
      if (services == null) {
        generateServicesMap(null, paramInt);
      }
      paramV = (ServiceInfo)services.get(paramV);
      return paramV;
    }
  }
  
  @VisibleForTesting
  protected UserInfo getUser(int paramInt)
  {
    return UserManager.get(mContext).getUserInfo(paramInt);
  }
  
  @VisibleForTesting
  protected File getUserSystemDirectory(int paramInt)
  {
    return Environment.getUserSystemDirectory(paramInt);
  }
  
  @VisibleForTesting
  protected List<UserInfo> getUsers()
  {
    return UserManager.get(mContext).getUsers(true);
  }
  
  @VisibleForTesting
  protected boolean inSystemImage(int paramInt)
  {
    String[] arrayOfString = mContext.getPackageManager().getPackagesForUid(paramInt);
    if (arrayOfString != null)
    {
      int i = arrayOfString.length;
      paramInt = 0;
      while (paramInt < i)
      {
        String str = arrayOfString[paramInt];
        try
        {
          int j = mContext.getPackageManager().getPackageInfo(str, 0).applicationInfo.flags;
          if ((j & 0x1) != 0) {
            return true;
          }
          paramInt++;
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
          return false;
        }
      }
    }
    return false;
  }
  
  public void invalidateCache(int paramInt)
  {
    synchronized (mServicesLock)
    {
      findOrCreateUserLockedservices = null;
      onServicesChangedLocked(paramInt);
      return;
    }
  }
  
  protected void onServicesChangedLocked(int paramInt) {}
  
  @VisibleForTesting
  protected void onUserRemoved(int paramInt)
  {
    synchronized (mServicesLock)
    {
      mUserServices.remove(paramInt);
      return;
    }
  }
  
  public abstract V parseServiceAttributes(Resources paramResources, String paramString, AttributeSet paramAttributeSet);
  
  /* Error */
  @VisibleForTesting
  protected ServiceInfo<V> parseServiceInfo(ResolveInfo paramResolveInfo)
    throws XmlPullParserException, IOException
  {
    // Byte code:
    //   0: aload_1
    //   1: getfield 683	android/content/pm/ResolveInfo:serviceInfo	Landroid/content/pm/ServiceInfo;
    //   4: astore_2
    //   5: new 685	android/content/ComponentName
    //   8: dup
    //   9: aload_2
    //   10: getfield 690	android/content/pm/ServiceInfo:packageName	Ljava/lang/String;
    //   13: aload_2
    //   14: getfield 692	android/content/pm/ServiceInfo:name	Ljava/lang/String;
    //   17: invokespecial 694	android/content/ComponentName:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   20: astore_3
    //   21: aload_0
    //   22: getfield 80	android/content/pm/RegisteredServicesCache:mContext	Landroid/content/Context;
    //   25: invokevirtual 652	android/content/Context:getPackageManager	()Landroid/content/pm/PackageManager;
    //   28: astore 4
    //   30: aconst_null
    //   31: astore 5
    //   33: aconst_null
    //   34: astore 6
    //   36: aload_2
    //   37: aload 4
    //   39: aload_0
    //   40: getfield 84	android/content/pm/RegisteredServicesCache:mMetaDataName	Ljava/lang/String;
    //   43: invokevirtual 698	android/content/pm/ServiceInfo:loadXmlMetaData	(Landroid/content/pm/PackageManager;Ljava/lang/String;)Landroid/content/res/XmlResourceParser;
    //   46: astore 7
    //   48: aload 7
    //   50: ifnull +283 -> 333
    //   53: aload 7
    //   55: astore 6
    //   57: aload 7
    //   59: astore 5
    //   61: aload 7
    //   63: invokestatic 702	android/util/Xml:asAttributeSet	(Lorg/xmlpull/v1/XmlPullParser;)Landroid/util/AttributeSet;
    //   66: astore 8
    //   68: aload 7
    //   70: astore 6
    //   72: aload 7
    //   74: astore 5
    //   76: aload 7
    //   78: invokeinterface 705 1 0
    //   83: istore 9
    //   85: iload 9
    //   87: iconst_1
    //   88: if_icmpeq +12 -> 100
    //   91: iload 9
    //   93: iconst_2
    //   94: if_icmpeq +6 -> 100
    //   97: goto -29 -> 68
    //   100: aload 7
    //   102: astore 6
    //   104: aload 7
    //   106: astore 5
    //   108: aload 7
    //   110: invokeinterface 706 1 0
    //   115: astore 10
    //   117: aload 7
    //   119: astore 6
    //   121: aload 7
    //   123: astore 5
    //   125: aload_0
    //   126: getfield 86	android/content/pm/RegisteredServicesCache:mAttributesName	Ljava/lang/String;
    //   129: aload 10
    //   131: invokevirtual 389	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   134: ifeq +88 -> 222
    //   137: aload 7
    //   139: astore 6
    //   141: aload 7
    //   143: astore 5
    //   145: aload_0
    //   146: aload 4
    //   148: aload_2
    //   149: getfield 707	android/content/pm/ServiceInfo:applicationInfo	Landroid/content/pm/ApplicationInfo;
    //   152: invokevirtual 711	android/content/pm/PackageManager:getResourcesForApplication	(Landroid/content/pm/ApplicationInfo;)Landroid/content/res/Resources;
    //   155: aload_2
    //   156: getfield 690	android/content/pm/ServiceInfo:packageName	Ljava/lang/String;
    //   159: aload 8
    //   161: invokevirtual 713	android/content/pm/RegisteredServicesCache:parseServiceAttributes	(Landroid/content/res/Resources;Ljava/lang/String;Landroid/util/AttributeSet;)Ljava/lang/Object;
    //   164: astore 4
    //   166: aload 4
    //   168: ifnonnull +17 -> 185
    //   171: aload 7
    //   173: ifnull +10 -> 183
    //   176: aload 7
    //   178: invokeinterface 716 1 0
    //   183: aconst_null
    //   184: areturn
    //   185: aload 7
    //   187: astore 6
    //   189: aload 7
    //   191: astore 5
    //   193: new 15	android/content/pm/RegisteredServicesCache$ServiceInfo
    //   196: dup
    //   197: aload 4
    //   199: aload_1
    //   200: getfield 683	android/content/pm/ResolveInfo:serviceInfo	Landroid/content/pm/ServiceInfo;
    //   203: aload_3
    //   204: invokespecial 719	android/content/pm/RegisteredServicesCache$ServiceInfo:<init>	(Ljava/lang/Object;Landroid/content/pm/ComponentInfo;Landroid/content/ComponentName;)V
    //   207: astore_1
    //   208: aload 7
    //   210: ifnull +10 -> 220
    //   213: aload 7
    //   215: invokeinterface 716 1 0
    //   220: aload_1
    //   221: areturn
    //   222: aload 7
    //   224: astore 6
    //   226: aload 7
    //   228: astore 5
    //   230: new 271	org/xmlpull/v1/XmlPullParserException
    //   233: astore_1
    //   234: aload 7
    //   236: astore 6
    //   238: aload 7
    //   240: astore 5
    //   242: new 182	java/lang/StringBuilder
    //   245: astore_3
    //   246: aload 7
    //   248: astore 6
    //   250: aload 7
    //   252: astore 5
    //   254: aload_3
    //   255: invokespecial 183	java/lang/StringBuilder:<init>	()V
    //   258: aload 7
    //   260: astore 6
    //   262: aload 7
    //   264: astore 5
    //   266: aload_3
    //   267: ldc_w 721
    //   270: invokevirtual 189	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   273: pop
    //   274: aload 7
    //   276: astore 6
    //   278: aload 7
    //   280: astore 5
    //   282: aload_3
    //   283: aload_0
    //   284: getfield 86	android/content/pm/RegisteredServicesCache:mAttributesName	Ljava/lang/String;
    //   287: invokevirtual 189	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   290: pop
    //   291: aload 7
    //   293: astore 6
    //   295: aload 7
    //   297: astore 5
    //   299: aload_3
    //   300: ldc_w 723
    //   303: invokevirtual 189	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   306: pop
    //   307: aload 7
    //   309: astore 6
    //   311: aload 7
    //   313: astore 5
    //   315: aload_1
    //   316: aload_3
    //   317: invokevirtual 199	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   320: invokespecial 725	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   323: aload 7
    //   325: astore 6
    //   327: aload 7
    //   329: astore 5
    //   331: aload_1
    //   332: athrow
    //   333: aload 7
    //   335: astore 6
    //   337: aload 7
    //   339: astore 5
    //   341: new 271	org/xmlpull/v1/XmlPullParserException
    //   344: astore_3
    //   345: aload 7
    //   347: astore 6
    //   349: aload 7
    //   351: astore 5
    //   353: new 182	java/lang/StringBuilder
    //   356: astore_1
    //   357: aload 7
    //   359: astore 6
    //   361: aload 7
    //   363: astore 5
    //   365: aload_1
    //   366: invokespecial 183	java/lang/StringBuilder:<init>	()V
    //   369: aload 7
    //   371: astore 6
    //   373: aload 7
    //   375: astore 5
    //   377: aload_1
    //   378: ldc_w 727
    //   381: invokevirtual 189	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   384: pop
    //   385: aload 7
    //   387: astore 6
    //   389: aload 7
    //   391: astore 5
    //   393: aload_1
    //   394: aload_0
    //   395: getfield 84	android/content/pm/RegisteredServicesCache:mMetaDataName	Ljava/lang/String;
    //   398: invokevirtual 189	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   401: pop
    //   402: aload 7
    //   404: astore 6
    //   406: aload 7
    //   408: astore 5
    //   410: aload_1
    //   411: ldc_w 729
    //   414: invokevirtual 189	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   417: pop
    //   418: aload 7
    //   420: astore 6
    //   422: aload 7
    //   424: astore 5
    //   426: aload_3
    //   427: aload_1
    //   428: invokevirtual 199	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   431: invokespecial 725	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   434: aload 7
    //   436: astore 6
    //   438: aload 7
    //   440: astore 5
    //   442: aload_3
    //   443: athrow
    //   444: astore_1
    //   445: goto +74 -> 519
    //   448: astore_1
    //   449: aload 5
    //   451: astore 6
    //   453: new 271	org/xmlpull/v1/XmlPullParserException
    //   456: astore 7
    //   458: aload 5
    //   460: astore 6
    //   462: new 182	java/lang/StringBuilder
    //   465: astore_1
    //   466: aload 5
    //   468: astore 6
    //   470: aload_1
    //   471: invokespecial 183	java/lang/StringBuilder:<init>	()V
    //   474: aload 5
    //   476: astore 6
    //   478: aload_1
    //   479: ldc_w 731
    //   482: invokevirtual 189	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   485: pop
    //   486: aload 5
    //   488: astore 6
    //   490: aload_1
    //   491: aload_2
    //   492: getfield 690	android/content/pm/ServiceInfo:packageName	Ljava/lang/String;
    //   495: invokevirtual 189	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   498: pop
    //   499: aload 5
    //   501: astore 6
    //   503: aload 7
    //   505: aload_1
    //   506: invokevirtual 199	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   509: invokespecial 725	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   512: aload 5
    //   514: astore 6
    //   516: aload 7
    //   518: athrow
    //   519: aload 6
    //   521: ifnull +10 -> 531
    //   524: aload 6
    //   526: invokeinterface 716 1 0
    //   531: aload_1
    //   532: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	533	0	this	RegisteredServicesCache
    //   0	533	1	paramResolveInfo	ResolveInfo
    //   4	488	2	localServiceInfo	ServiceInfo
    //   20	423	3	localObject1	Object
    //   28	170	4	localObject2	Object
    //   31	482	5	localObject3	Object
    //   34	491	6	localObject4	Object
    //   46	471	7	localObject5	Object
    //   66	94	8	localAttributeSet	AttributeSet
    //   83	12	9	i	int
    //   115	15	10	str	String
    // Exception table:
    //   from	to	target	type
    //   36	48	444	finally
    //   61	68	444	finally
    //   76	85	444	finally
    //   108	117	444	finally
    //   125	137	444	finally
    //   145	166	444	finally
    //   193	208	444	finally
    //   230	234	444	finally
    //   242	246	444	finally
    //   254	258	444	finally
    //   266	274	444	finally
    //   282	291	444	finally
    //   299	307	444	finally
    //   315	323	444	finally
    //   331	333	444	finally
    //   341	345	444	finally
    //   353	357	444	finally
    //   365	369	444	finally
    //   377	385	444	finally
    //   393	402	444	finally
    //   410	418	444	finally
    //   426	434	444	finally
    //   442	444	444	finally
    //   453	458	444	finally
    //   462	466	444	finally
    //   470	474	444	finally
    //   478	486	444	finally
    //   490	499	444	finally
    //   503	512	444	finally
    //   516	519	444	finally
    //   36	48	448	android/content/pm/PackageManager$NameNotFoundException
    //   61	68	448	android/content/pm/PackageManager$NameNotFoundException
    //   76	85	448	android/content/pm/PackageManager$NameNotFoundException
    //   108	117	448	android/content/pm/PackageManager$NameNotFoundException
    //   125	137	448	android/content/pm/PackageManager$NameNotFoundException
    //   145	166	448	android/content/pm/PackageManager$NameNotFoundException
    //   193	208	448	android/content/pm/PackageManager$NameNotFoundException
    //   230	234	448	android/content/pm/PackageManager$NameNotFoundException
    //   242	246	448	android/content/pm/PackageManager$NameNotFoundException
    //   254	258	448	android/content/pm/PackageManager$NameNotFoundException
    //   266	274	448	android/content/pm/PackageManager$NameNotFoundException
    //   282	291	448	android/content/pm/PackageManager$NameNotFoundException
    //   299	307	448	android/content/pm/PackageManager$NameNotFoundException
    //   315	323	448	android/content/pm/PackageManager$NameNotFoundException
    //   331	333	448	android/content/pm/PackageManager$NameNotFoundException
    //   341	345	448	android/content/pm/PackageManager$NameNotFoundException
    //   353	357	448	android/content/pm/PackageManager$NameNotFoundException
    //   365	369	448	android/content/pm/PackageManager$NameNotFoundException
    //   377	385	448	android/content/pm/PackageManager$NameNotFoundException
    //   393	402	448	android/content/pm/PackageManager$NameNotFoundException
    //   410	418	448	android/content/pm/PackageManager$NameNotFoundException
    //   426	434	448	android/content/pm/PackageManager$NameNotFoundException
    //   442	444	448	android/content/pm/PackageManager$NameNotFoundException
  }
  
  @VisibleForTesting
  protected List<ResolveInfo> queryIntentServices(int paramInt)
  {
    PackageManager localPackageManager = mContext.getPackageManager();
    int i = 786560;
    synchronized (mServicesLock)
    {
      if (findOrCreateUserLockedmBindInstantServiceAllowed) {
        i = 0xC0080 | 0x800000;
      }
      return localPackageManager.queryIntentServicesAsUser(new Intent(mInterfaceName), i, paramInt);
    }
  }
  
  public void setBindInstantServiceAllowed(int paramInt, boolean paramBoolean)
  {
    mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_BIND_INSTANT_SERVICE", "setBindInstantServiceAllowed");
    synchronized (mServicesLock)
    {
      findOrCreateUserLockedmBindInstantServiceAllowed = paramBoolean;
      return;
    }
  }
  
  public void setListener(RegisteredServicesCacheListener<V> paramRegisteredServicesCacheListener, Handler paramHandler)
  {
    Handler localHandler = paramHandler;
    if (paramHandler == null) {
      localHandler = new Handler(mContext.getMainLooper());
    }
    try
    {
      mHandler = localHandler;
      mListener = paramRegisteredServicesCacheListener;
      return;
    }
    finally {}
  }
  
  public void updateServices(int paramInt)
  {
    synchronized (mServicesLock)
    {
      Object localObject2 = findOrCreateUserLocked(paramInt);
      if (services == null) {
        return;
      }
      Object localObject4 = new java/util/ArrayList;
      ((ArrayList)localObject4).<init>(services.values());
      ??? = null;
      Iterator localIterator = ((List)localObject4).iterator();
      while (localIterator.hasNext())
      {
        ServiceInfo localServiceInfo = (ServiceInfo)localIterator.next();
        long l = componentInfo.applicationInfo.versionCode;
        localObject2 = componentInfo.packageName;
        localObject4 = null;
        try
        {
          localObject2 = mContext.getPackageManager().getApplicationInfoAsUser((String)localObject2, 0, paramInt);
          localObject4 = localObject2;
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException) {}
        Object localObject3;
        if (localObject4 != null)
        {
          localObject3 = ???;
          if (versionCode == l) {}
        }
        else
        {
          localObject4 = ???;
          if (??? == null) {
            localObject4 = new IntArray();
          }
          ((IntArray)localObject4).add(uid);
          localObject3 = localObject4;
        }
        ??? = localObject3;
      }
      if ((??? != null) && (???.size() > 0)) {
        generateServicesMap(???.toArray(), paramInt);
      }
      return;
    }
  }
  
  public static class ServiceInfo<V>
  {
    public final ComponentInfo componentInfo;
    public final ComponentName componentName;
    public final V type;
    public final int uid;
    
    public ServiceInfo(V paramV, ComponentInfo paramComponentInfo, ComponentName paramComponentName)
    {
      type = paramV;
      componentInfo = paramComponentInfo;
      componentName = paramComponentName;
      int i;
      if (paramComponentInfo != null) {
        i = applicationInfo.uid;
      } else {
        i = -1;
      }
      uid = i;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("ServiceInfo: ");
      localStringBuilder.append(type);
      localStringBuilder.append(", ");
      localStringBuilder.append(componentName);
      localStringBuilder.append(", uid ");
      localStringBuilder.append(uid);
      return localStringBuilder.toString();
    }
  }
  
  private static class UserServices<V>
  {
    @GuardedBy("mServicesLock")
    boolean mBindInstantServiceAllowed = false;
    @GuardedBy("mServicesLock")
    boolean mPersistentServicesFileDidNotExist = true;
    @GuardedBy("mServicesLock")
    final Map<V, Integer> persistentServices = Maps.newHashMap();
    @GuardedBy("mServicesLock")
    Map<V, RegisteredServicesCache.ServiceInfo<V>> services = null;
    
    private UserServices() {}
  }
}
