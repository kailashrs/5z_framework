package android.widget;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.DataSetObservable;
import android.os.AsyncTask;
import android.os.Process;
import android.text.TextUtils;
import com.android.internal.content.PackageMonitor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityChooserModel
  extends DataSetObservable
{
  private static final String ATTRIBUTE_ACTIVITY = "activity";
  private static final String ATTRIBUTE_TIME = "time";
  private static final String ATTRIBUTE_WEIGHT = "weight";
  private static final boolean DEBUG = false;
  private static final int DEFAULT_ACTIVITY_INFLATION = 5;
  private static final float DEFAULT_HISTORICAL_RECORD_WEIGHT = 1.0F;
  public static final String DEFAULT_HISTORY_FILE_NAME = "activity_choser_model_history.xml";
  public static final int DEFAULT_HISTORY_MAX_LENGTH = 50;
  private static final String HISTORY_FILE_EXTENSION = ".xml";
  private static final int INVALID_INDEX = -1;
  private static final String LOG_TAG = ActivityChooserModel.class.getSimpleName();
  private static final String TAG_HISTORICAL_RECORD = "historical-record";
  private static final String TAG_HISTORICAL_RECORDS = "historical-records";
  private static final Map<String, ActivityChooserModel> sDataModelRegistry = new HashMap();
  private static final Object sRegistryLock = new Object();
  private final List<ActivityResolveInfo> mActivities = new ArrayList();
  private OnChooseActivityListener mActivityChoserModelPolicy;
  private ActivitySorter mActivitySorter = new DefaultSorter(null);
  private boolean mCanReadHistoricalData = true;
  private final Context mContext;
  private final List<HistoricalRecord> mHistoricalRecords = new ArrayList();
  private boolean mHistoricalRecordsChanged = true;
  private final String mHistoryFileName;
  private int mHistoryMaxSize = 50;
  private final Object mInstanceLock = new Object();
  private Intent mIntent;
  private final PackageMonitor mPackageMonitor = new DataModelPackageMonitor(null);
  private boolean mReadShareHistoryCalled = false;
  private boolean mReloadActivities = false;
  
  private ActivityChooserModel(Context paramContext, String paramString)
  {
    mContext = paramContext.getApplicationContext();
    if ((!TextUtils.isEmpty(paramString)) && (!paramString.endsWith(".xml")))
    {
      paramContext = new StringBuilder();
      paramContext.append(paramString);
      paramContext.append(".xml");
      mHistoryFileName = paramContext.toString();
    }
    else
    {
      mHistoryFileName = paramString;
    }
    mPackageMonitor.register(mContext, null, true);
  }
  
  private boolean addHisoricalRecord(HistoricalRecord paramHistoricalRecord)
  {
    boolean bool = mHistoricalRecords.add(paramHistoricalRecord);
    if (bool)
    {
      mHistoricalRecordsChanged = true;
      pruneExcessiveHistoricalRecordsIfNeeded();
      persistHistoricalDataIfNeeded();
      sortActivitiesIfNeeded();
      notifyChanged();
    }
    return bool;
  }
  
  private void ensureConsistentState()
  {
    boolean bool1 = loadActivitiesIfNeeded();
    boolean bool2 = readHistoricalDataIfNeeded();
    pruneExcessiveHistoricalRecordsIfNeeded();
    if ((bool1 | bool2))
    {
      sortActivitiesIfNeeded();
      notifyChanged();
    }
  }
  
  public static ActivityChooserModel get(Context paramContext, String paramString)
  {
    synchronized (sRegistryLock)
    {
      ActivityChooserModel localActivityChooserModel1 = (ActivityChooserModel)sDataModelRegistry.get(paramString);
      ActivityChooserModel localActivityChooserModel2 = localActivityChooserModel1;
      if (localActivityChooserModel1 == null)
      {
        localActivityChooserModel2 = new android/widget/ActivityChooserModel;
        localActivityChooserModel2.<init>(paramContext, paramString);
        sDataModelRegistry.put(paramString, localActivityChooserModel2);
      }
      return localActivityChooserModel2;
    }
  }
  
  private boolean loadActivitiesIfNeeded()
  {
    boolean bool = mReloadActivities;
    int i = 0;
    if ((bool) && (mIntent != null))
    {
      mReloadActivities = false;
      mActivities.clear();
      List localList = mContext.getPackageManager().queryIntentActivities(mIntent, 0);
      int j = localList.size();
      while (i < j)
      {
        ResolveInfo localResolveInfo = (ResolveInfo)localList.get(i);
        ActivityInfo localActivityInfo = activityInfo;
        if (ActivityManager.checkComponentPermission(permission, Process.myUid(), applicationInfo.uid, exported) == 0) {
          mActivities.add(new ActivityResolveInfo(localResolveInfo));
        }
        i++;
      }
      return true;
    }
    return false;
  }
  
  private void persistHistoricalDataIfNeeded()
  {
    if (mReadShareHistoryCalled)
    {
      if (!mHistoricalRecordsChanged) {
        return;
      }
      mHistoricalRecordsChanged = false;
      if (!TextUtils.isEmpty(mHistoryFileName)) {
        new PersistHistoryAsyncTask(null).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new Object[] { new ArrayList(mHistoricalRecords), mHistoryFileName });
      }
      return;
    }
    throw new IllegalStateException("No preceding call to #readHistoricalData");
  }
  
  private void pruneExcessiveHistoricalRecordsIfNeeded()
  {
    int i = mHistoricalRecords.size() - mHistoryMaxSize;
    if (i <= 0) {
      return;
    }
    mHistoricalRecordsChanged = true;
    for (int j = 0; j < i; j++) {
      HistoricalRecord localHistoricalRecord = (HistoricalRecord)mHistoricalRecords.remove(0);
    }
  }
  
  private boolean readHistoricalDataIfNeeded()
  {
    if ((mCanReadHistoricalData) && (mHistoricalRecordsChanged) && (!TextUtils.isEmpty(mHistoryFileName)))
    {
      mCanReadHistoricalData = false;
      mReadShareHistoryCalled = true;
      readHistoricalDataImpl();
      return true;
    }
    return false;
  }
  
  /* Error */
  private void readHistoricalDataImpl()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 153	android/widget/ActivityChooserModel:mContext	Landroid/content/Context;
    //   4: aload_0
    //   5: getfield 177	android/widget/ActivityChooserModel:mHistoryFileName	Ljava/lang/String;
    //   8: invokevirtual 328	android/content/Context:openFileInput	(Ljava/lang/String;)Ljava/io/FileInputStream;
    //   11: astore_1
    //   12: invokestatic 334	android/util/Xml:newPullParser	()Lorg/xmlpull/v1/XmlPullParser;
    //   15: astore_2
    //   16: aload_2
    //   17: aload_1
    //   18: getstatic 340	java/nio/charset/StandardCharsets:UTF_8	Ljava/nio/charset/Charset;
    //   21: invokevirtual 345	java/nio/charset/Charset:name	()Ljava/lang/String;
    //   24: invokeinterface 351 3 0
    //   29: iconst_0
    //   30: istore_3
    //   31: iload_3
    //   32: iconst_1
    //   33: if_icmpeq +18 -> 51
    //   36: iload_3
    //   37: iconst_2
    //   38: if_icmpeq +13 -> 51
    //   41: aload_2
    //   42: invokeinterface 354 1 0
    //   47: istore_3
    //   48: goto -17 -> 31
    //   51: ldc 66
    //   53: aload_2
    //   54: invokeinterface 357 1 0
    //   59: invokevirtual 360	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   62: ifeq +147 -> 209
    //   65: aload_0
    //   66: getfield 127	android/widget/ActivityChooserModel:mHistoricalRecords	Ljava/util/List;
    //   69: astore 4
    //   71: aload 4
    //   73: invokeinterface 237 1 0
    //   78: aload_2
    //   79: invokeinterface 354 1 0
    //   84: istore_3
    //   85: iload_3
    //   86: iconst_1
    //   87: if_icmpne +14 -> 101
    //   90: aload_1
    //   91: ifnull +252 -> 343
    //   94: aload_1
    //   95: invokevirtual 365	java/io/FileInputStream:close	()V
    //   98: goto +238 -> 336
    //   101: iload_3
    //   102: iconst_3
    //   103: if_icmpeq -25 -> 78
    //   106: iload_3
    //   107: iconst_4
    //   108: if_icmpne +6 -> 114
    //   111: goto -33 -> 78
    //   114: ldc 63
    //   116: aload_2
    //   117: invokeinterface 357 1 0
    //   122: invokevirtual 360	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   125: ifeq +71 -> 196
    //   128: aload_2
    //   129: aconst_null
    //   130: ldc 34
    //   132: invokeinterface 369 3 0
    //   137: astore 5
    //   139: aload_2
    //   140: aconst_null
    //   141: ldc 37
    //   143: invokeinterface 369 3 0
    //   148: invokestatic 375	java/lang/Long:parseLong	(Ljava/lang/String;)J
    //   151: lstore 6
    //   153: aload_2
    //   154: aconst_null
    //   155: ldc 40
    //   157: invokeinterface 369 3 0
    //   162: invokestatic 381	java/lang/Float:parseFloat	(Ljava/lang/String;)F
    //   165: fstore 8
    //   167: new 23	android/widget/ActivityChooserModel$HistoricalRecord
    //   170: astore 9
    //   172: aload 9
    //   174: aload 5
    //   176: lload 6
    //   178: fload 8
    //   180: invokespecial 384	android/widget/ActivityChooserModel$HistoricalRecord:<init>	(Ljava/lang/String;JF)V
    //   183: aload 4
    //   185: aload 9
    //   187: invokeinterface 199 2 0
    //   192: pop
    //   193: goto -115 -> 78
    //   196: new 322	org/xmlpull/v1/XmlPullParserException
    //   199: astore_2
    //   200: aload_2
    //   201: ldc_w 386
    //   204: invokespecial 387	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   207: aload_2
    //   208: athrow
    //   209: new 322	org/xmlpull/v1/XmlPullParserException
    //   212: astore_2
    //   213: aload_2
    //   214: ldc_w 389
    //   217: invokespecial 387	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   220: aload_2
    //   221: athrow
    //   222: astore_2
    //   223: goto +121 -> 344
    //   226: astore 9
    //   228: getstatic 103	android/widget/ActivityChooserModel:LOG_TAG	Ljava/lang/String;
    //   231: astore 4
    //   233: new 167	java/lang/StringBuilder
    //   236: astore_2
    //   237: aload_2
    //   238: invokespecial 168	java/lang/StringBuilder:<init>	()V
    //   241: aload_2
    //   242: ldc_w 391
    //   245: invokevirtual 172	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   248: pop
    //   249: aload_2
    //   250: aload_0
    //   251: getfield 177	android/widget/ActivityChooserModel:mHistoryFileName	Ljava/lang/String;
    //   254: invokevirtual 172	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   257: pop
    //   258: aload 4
    //   260: aload_2
    //   261: invokevirtual 175	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   264: aload 9
    //   266: invokestatic 397	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   269: pop
    //   270: aload_1
    //   271: ifnull +72 -> 343
    //   274: aload_1
    //   275: invokevirtual 365	java/io/FileInputStream:close	()V
    //   278: goto +58 -> 336
    //   281: astore 9
    //   283: getstatic 103	android/widget/ActivityChooserModel:LOG_TAG	Ljava/lang/String;
    //   286: astore_2
    //   287: new 167	java/lang/StringBuilder
    //   290: astore 4
    //   292: aload 4
    //   294: invokespecial 168	java/lang/StringBuilder:<init>	()V
    //   297: aload 4
    //   299: ldc_w 391
    //   302: invokevirtual 172	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   305: pop
    //   306: aload 4
    //   308: aload_0
    //   309: getfield 177	android/widget/ActivityChooserModel:mHistoryFileName	Ljava/lang/String;
    //   312: invokevirtual 172	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   315: pop
    //   316: aload_2
    //   317: aload 4
    //   319: invokevirtual 175	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   322: aload 9
    //   324: invokestatic 397	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   327: pop
    //   328: aload_1
    //   329: ifnull +14 -> 343
    //   332: aload_1
    //   333: invokevirtual 365	java/io/FileInputStream:close	()V
    //   336: goto +7 -> 343
    //   339: astore_1
    //   340: goto -4 -> 336
    //   343: return
    //   344: aload_1
    //   345: ifnull +11 -> 356
    //   348: aload_1
    //   349: invokevirtual 365	java/io/FileInputStream:close	()V
    //   352: goto +4 -> 356
    //   355: astore_1
    //   356: aload_2
    //   357: athrow
    //   358: astore_1
    //   359: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	360	0	this	ActivityChooserModel
    //   11	322	1	localFileInputStream	java.io.FileInputStream
    //   339	10	1	localIOException1	java.io.IOException
    //   355	1	1	localIOException2	java.io.IOException
    //   358	1	1	localFileNotFoundException	java.io.FileNotFoundException
    //   15	206	2	localObject1	Object
    //   222	1	2	localObject2	Object
    //   236	121	2	localObject3	Object
    //   30	79	3	i	int
    //   69	249	4	localObject4	Object
    //   137	38	5	str	String
    //   151	26	6	l	long
    //   165	14	8	f	float
    //   170	16	9	localHistoricalRecord	HistoricalRecord
    //   226	39	9	localIOException3	java.io.IOException
    //   281	42	9	localXmlPullParserException	org.xmlpull.v1.XmlPullParserException
    // Exception table:
    //   from	to	target	type
    //   12	29	222	finally
    //   41	48	222	finally
    //   51	78	222	finally
    //   78	85	222	finally
    //   114	193	222	finally
    //   196	209	222	finally
    //   209	222	222	finally
    //   228	270	222	finally
    //   283	328	222	finally
    //   12	29	226	java/io/IOException
    //   41	48	226	java/io/IOException
    //   51	78	226	java/io/IOException
    //   78	85	226	java/io/IOException
    //   114	193	226	java/io/IOException
    //   196	209	226	java/io/IOException
    //   209	222	226	java/io/IOException
    //   12	29	281	org/xmlpull/v1/XmlPullParserException
    //   41	48	281	org/xmlpull/v1/XmlPullParserException
    //   51	78	281	org/xmlpull/v1/XmlPullParserException
    //   78	85	281	org/xmlpull/v1/XmlPullParserException
    //   114	193	281	org/xmlpull/v1/XmlPullParserException
    //   196	209	281	org/xmlpull/v1/XmlPullParserException
    //   209	222	281	org/xmlpull/v1/XmlPullParserException
    //   94	98	339	java/io/IOException
    //   274	278	339	java/io/IOException
    //   332	336	339	java/io/IOException
    //   348	352	355	java/io/IOException
    //   0	12	358	java/io/FileNotFoundException
  }
  
  private boolean sortActivitiesIfNeeded()
  {
    if ((mActivitySorter != null) && (mIntent != null) && (!mActivities.isEmpty()) && (!mHistoricalRecords.isEmpty()))
    {
      mActivitySorter.sort(mIntent, mActivities, Collections.unmodifiableList(mHistoricalRecords));
      return true;
    }
    return false;
  }
  
  public Intent chooseActivity(int paramInt)
  {
    synchronized (mInstanceLock)
    {
      if (mIntent == null) {
        return null;
      }
      ensureConsistentState();
      Object localObject2 = (ActivityResolveInfo)mActivities.get(paramInt);
      ComponentName localComponentName = new android/content/ComponentName;
      localComponentName.<init>(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
      localObject2 = new android/content/Intent;
      ((Intent)localObject2).<init>(mIntent);
      ((Intent)localObject2).setComponent(localComponentName);
      if (mActivityChoserModelPolicy != null)
      {
        localObject4 = new android/content/Intent;
        ((Intent)localObject4).<init>((Intent)localObject2);
        if (mActivityChoserModelPolicy.onChooseActivity(this, (Intent)localObject4)) {
          return null;
        }
      }
      Object localObject4 = new android/widget/ActivityChooserModel$HistoricalRecord;
      ((HistoricalRecord)localObject4).<init>(localComponentName, System.currentTimeMillis(), 1.0F);
      addHisoricalRecord((HistoricalRecord)localObject4);
      return localObject2;
    }
  }
  
  protected void finalize()
    throws Throwable
  {
    super.finalize();
    mPackageMonitor.unregister();
  }
  
  public ResolveInfo getActivity(int paramInt)
  {
    synchronized (mInstanceLock)
    {
      ensureConsistentState();
      ResolveInfo localResolveInfo = mActivities.get(paramInt)).resolveInfo;
      return localResolveInfo;
    }
  }
  
  public int getActivityCount()
  {
    synchronized (mInstanceLock)
    {
      ensureConsistentState();
      int i = mActivities.size();
      return i;
    }
  }
  
  public int getActivityIndex(ResolveInfo paramResolveInfo)
  {
    synchronized (mInstanceLock)
    {
      ensureConsistentState();
      List localList = mActivities;
      int i = localList.size();
      for (int j = 0; j < i; j++) {
        if (getresolveInfo == paramResolveInfo) {
          return j;
        }
      }
      return -1;
    }
  }
  
  public ResolveInfo getDefaultActivity()
  {
    synchronized (mInstanceLock)
    {
      ensureConsistentState();
      if (!mActivities.isEmpty())
      {
        ResolveInfo localResolveInfo = mActivities.get(0)).resolveInfo;
        return localResolveInfo;
      }
      return null;
    }
  }
  
  public int getHistoryMaxSize()
  {
    synchronized (mInstanceLock)
    {
      int i = mHistoryMaxSize;
      return i;
    }
  }
  
  public int getHistorySize()
  {
    synchronized (mInstanceLock)
    {
      ensureConsistentState();
      int i = mHistoricalRecords.size();
      return i;
    }
  }
  
  public Intent getIntent()
  {
    synchronized (mInstanceLock)
    {
      Intent localIntent = mIntent;
      return localIntent;
    }
  }
  
  public void setActivitySorter(ActivitySorter paramActivitySorter)
  {
    synchronized (mInstanceLock)
    {
      if (mActivitySorter == paramActivitySorter) {
        return;
      }
      mActivitySorter = paramActivitySorter;
      if (sortActivitiesIfNeeded()) {
        notifyChanged();
      }
      return;
    }
  }
  
  public void setDefaultActivity(int paramInt)
  {
    synchronized (mInstanceLock)
    {
      ensureConsistentState();
      Object localObject2 = (ActivityResolveInfo)mActivities.get(paramInt);
      Object localObject4 = (ActivityResolveInfo)mActivities.get(0);
      float f;
      if (localObject4 != null) {
        f = weight - weight + 5.0F;
      } else {
        f = 1.0F;
      }
      localObject4 = new android/content/ComponentName;
      ((ComponentName)localObject4).<init>(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
      localObject2 = new android/widget/ActivityChooserModel$HistoricalRecord;
      ((HistoricalRecord)localObject2).<init>((ComponentName)localObject4, System.currentTimeMillis(), f);
      addHisoricalRecord((HistoricalRecord)localObject2);
      return;
    }
  }
  
  public void setHistoryMaxSize(int paramInt)
  {
    synchronized (mInstanceLock)
    {
      if (mHistoryMaxSize == paramInt) {
        return;
      }
      mHistoryMaxSize = paramInt;
      pruneExcessiveHistoricalRecordsIfNeeded();
      if (sortActivitiesIfNeeded()) {
        notifyChanged();
      }
      return;
    }
  }
  
  public void setIntent(Intent paramIntent)
  {
    synchronized (mInstanceLock)
    {
      if (mIntent == paramIntent) {
        return;
      }
      mIntent = paramIntent;
      mReloadActivities = true;
      ensureConsistentState();
      return;
    }
  }
  
  public void setOnChooseActivityListener(OnChooseActivityListener paramOnChooseActivityListener)
  {
    synchronized (mInstanceLock)
    {
      mActivityChoserModelPolicy = paramOnChooseActivityListener;
      return;
    }
  }
  
  public static abstract interface ActivityChooserModelClient
  {
    public abstract void setActivityChooserModel(ActivityChooserModel paramActivityChooserModel);
  }
  
  public final class ActivityResolveInfo
    implements Comparable<ActivityResolveInfo>
  {
    public final ResolveInfo resolveInfo;
    public float weight;
    
    public ActivityResolveInfo(ResolveInfo paramResolveInfo)
    {
      resolveInfo = paramResolveInfo;
    }
    
    public int compareTo(ActivityResolveInfo paramActivityResolveInfo)
    {
      return Float.floatToIntBits(weight) - Float.floatToIntBits(weight);
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (paramObject == null) {
        return false;
      }
      if (getClass() != paramObject.getClass()) {
        return false;
      }
      paramObject = (ActivityResolveInfo)paramObject;
      return Float.floatToIntBits(weight) == Float.floatToIntBits(weight);
    }
    
    public int hashCode()
    {
      return 31 + Float.floatToIntBits(weight);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("[");
      localStringBuilder.append("resolveInfo:");
      localStringBuilder.append(resolveInfo.toString());
      localStringBuilder.append("; weight:");
      localStringBuilder.append(new BigDecimal(weight));
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
  }
  
  public static abstract interface ActivitySorter
  {
    public abstract void sort(Intent paramIntent, List<ActivityChooserModel.ActivityResolveInfo> paramList, List<ActivityChooserModel.HistoricalRecord> paramList1);
  }
  
  private final class DataModelPackageMonitor
    extends PackageMonitor
  {
    private DataModelPackageMonitor() {}
    
    public void onSomePackagesChanged()
    {
      ActivityChooserModel.access$702(ActivityChooserModel.this, true);
    }
  }
  
  private final class DefaultSorter
    implements ActivityChooserModel.ActivitySorter
  {
    private static final float WEIGHT_DECAY_COEFFICIENT = 0.95F;
    private final Map<ComponentName, ActivityChooserModel.ActivityResolveInfo> mPackageNameToActivityMap = new HashMap();
    
    private DefaultSorter() {}
    
    public void sort(Intent paramIntent, List<ActivityChooserModel.ActivityResolveInfo> paramList, List<ActivityChooserModel.HistoricalRecord> paramList1)
    {
      paramIntent = mPackageNameToActivityMap;
      paramIntent.clear();
      int i = paramList.size();
      ActivityChooserModel.ActivityResolveInfo localActivityResolveInfo;
      for (int j = 0; j < i; j++)
      {
        localActivityResolveInfo = (ActivityChooserModel.ActivityResolveInfo)paramList.get(j);
        weight = 0.0F;
        paramIntent.put(new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name), localActivityResolveInfo);
      }
      j = paramList1.size();
      float f1 = 1.0F;
      j--;
      while (j >= 0)
      {
        ActivityChooserModel.HistoricalRecord localHistoricalRecord = (ActivityChooserModel.HistoricalRecord)paramList1.get(j);
        localActivityResolveInfo = (ActivityChooserModel.ActivityResolveInfo)paramIntent.get(activity);
        float f2 = f1;
        if (localActivityResolveInfo != null)
        {
          weight += weight * f1;
          f2 = f1 * 0.95F;
        }
        j--;
        f1 = f2;
      }
      Collections.sort(paramList);
    }
  }
  
  public static final class HistoricalRecord
  {
    public final ComponentName activity;
    public final long time;
    public final float weight;
    
    public HistoricalRecord(ComponentName paramComponentName, long paramLong, float paramFloat)
    {
      activity = paramComponentName;
      time = paramLong;
      weight = paramFloat;
    }
    
    public HistoricalRecord(String paramString, long paramLong, float paramFloat)
    {
      this(ComponentName.unflattenFromString(paramString), paramLong, paramFloat);
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (paramObject == null) {
        return false;
      }
      if (getClass() != paramObject.getClass()) {
        return false;
      }
      paramObject = (HistoricalRecord)paramObject;
      if (activity == null)
      {
        if (activity != null) {
          return false;
        }
      }
      else if (!activity.equals(activity)) {
        return false;
      }
      if (time != time) {
        return false;
      }
      return Float.floatToIntBits(weight) == Float.floatToIntBits(weight);
    }
    
    public int hashCode()
    {
      int i;
      if (activity == null) {
        i = 0;
      } else {
        i = activity.hashCode();
      }
      return 31 * (31 * (31 * 1 + i) + (int)(time ^ time >>> 32)) + Float.floatToIntBits(weight);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("[");
      localStringBuilder.append("; activity:");
      localStringBuilder.append(activity);
      localStringBuilder.append("; time:");
      localStringBuilder.append(time);
      localStringBuilder.append("; weight:");
      localStringBuilder.append(new BigDecimal(weight));
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
  }
  
  public static abstract interface OnChooseActivityListener
  {
    public abstract boolean onChooseActivity(ActivityChooserModel paramActivityChooserModel, Intent paramIntent);
  }
  
  private final class PersistHistoryAsyncTask
    extends AsyncTask<Object, Void, Void>
  {
    private PersistHistoryAsyncTask() {}
    
    /* Error */
    public Void doInBackground(Object... paramVarArgs)
    {
      // Byte code:
      //   0: aload_1
      //   1: iconst_0
      //   2: aaload
      //   3: checkcast 36	java/util/List
      //   6: astore_2
      //   7: aload_1
      //   8: iconst_1
      //   9: aaload
      //   10: checkcast 38	java/lang/String
      //   13: astore_3
      //   14: aload_0
      //   15: getfield 14	android/widget/ActivityChooserModel$PersistHistoryAsyncTask:this$0	Landroid/widget/ActivityChooserModel;
      //   18: invokestatic 42	android/widget/ActivityChooserModel:access$300	(Landroid/widget/ActivityChooserModel;)Landroid/content/Context;
      //   21: aload_3
      //   22: iconst_0
      //   23: invokevirtual 48	android/content/Context:openFileOutput	(Ljava/lang/String;I)Ljava/io/FileOutputStream;
      //   26: astore_1
      //   27: invokestatic 54	android/util/Xml:newSerializer	()Lorg/xmlpull/v1/XmlSerializer;
      //   30: astore_3
      //   31: aload_3
      //   32: aload_1
      //   33: aconst_null
      //   34: invokeinterface 60 3 0
      //   39: aload_3
      //   40: getstatic 66	java/nio/charset/StandardCharsets:UTF_8	Ljava/nio/charset/Charset;
      //   43: invokevirtual 72	java/nio/charset/Charset:name	()Ljava/lang/String;
      //   46: iconst_1
      //   47: invokestatic 78	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
      //   50: invokeinterface 82 3 0
      //   55: aload_3
      //   56: aconst_null
      //   57: ldc 84
      //   59: invokeinterface 88 3 0
      //   64: pop
      //   65: aload_2
      //   66: invokeinterface 92 1 0
      //   71: istore 4
      //   73: iconst_0
      //   74: istore 5
      //   76: iload 5
      //   78: iload 4
      //   80: if_icmpge +95 -> 175
      //   83: aload_2
      //   84: iconst_0
      //   85: invokeinterface 96 2 0
      //   90: checkcast 98	android/widget/ActivityChooserModel$HistoricalRecord
      //   93: astore 6
      //   95: aload_3
      //   96: aconst_null
      //   97: ldc 100
      //   99: invokeinterface 88 3 0
      //   104: pop
      //   105: aload_3
      //   106: aconst_null
      //   107: ldc 102
      //   109: aload 6
      //   111: getfield 105	android/widget/ActivityChooserModel$HistoricalRecord:activity	Landroid/content/ComponentName;
      //   114: invokevirtual 110	android/content/ComponentName:flattenToString	()Ljava/lang/String;
      //   117: invokeinterface 114 4 0
      //   122: pop
      //   123: aload_3
      //   124: aconst_null
      //   125: ldc 116
      //   127: aload 6
      //   129: getfield 119	android/widget/ActivityChooserModel$HistoricalRecord:time	J
      //   132: invokestatic 122	java/lang/String:valueOf	(J)Ljava/lang/String;
      //   135: invokeinterface 114 4 0
      //   140: pop
      //   141: aload_3
      //   142: aconst_null
      //   143: ldc 124
      //   145: aload 6
      //   147: getfield 127	android/widget/ActivityChooserModel$HistoricalRecord:weight	F
      //   150: invokestatic 130	java/lang/String:valueOf	(F)Ljava/lang/String;
      //   153: invokeinterface 114 4 0
      //   158: pop
      //   159: aload_3
      //   160: aconst_null
      //   161: ldc 100
      //   163: invokeinterface 133 3 0
      //   168: pop
      //   169: iinc 5 1
      //   172: goto -96 -> 76
      //   175: aload_3
      //   176: aconst_null
      //   177: ldc 84
      //   179: invokeinterface 133 3 0
      //   184: pop
      //   185: aload_3
      //   186: invokeinterface 136 1 0
      //   191: aload_0
      //   192: getfield 14	android/widget/ActivityChooserModel$PersistHistoryAsyncTask:this$0	Landroid/widget/ActivityChooserModel;
      //   195: iconst_1
      //   196: invokestatic 140	android/widget/ActivityChooserModel:access$602	(Landroid/widget/ActivityChooserModel;Z)Z
      //   199: pop
      //   200: aload_1
      //   201: ifnull +216 -> 417
      //   204: aload_1
      //   205: invokevirtual 145	java/io/FileOutputStream:close	()V
      //   208: goto +202 -> 410
      //   211: astore_3
      //   212: goto +207 -> 419
      //   215: astore_2
      //   216: invokestatic 148	android/widget/ActivityChooserModel:access$400	()Ljava/lang/String;
      //   219: astore_3
      //   220: new 150	java/lang/StringBuilder
      //   223: astore 6
      //   225: aload 6
      //   227: invokespecial 151	java/lang/StringBuilder:<init>	()V
      //   230: aload 6
      //   232: ldc -103
      //   234: invokevirtual 157	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   237: pop
      //   238: aload 6
      //   240: aload_0
      //   241: getfield 14	android/widget/ActivityChooserModel$PersistHistoryAsyncTask:this$0	Landroid/widget/ActivityChooserModel;
      //   244: invokestatic 161	android/widget/ActivityChooserModel:access$500	(Landroid/widget/ActivityChooserModel;)Ljava/lang/String;
      //   247: invokevirtual 157	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   250: pop
      //   251: aload_3
      //   252: aload 6
      //   254: invokevirtual 164	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   257: aload_2
      //   258: invokestatic 170	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   261: pop
      //   262: aload_0
      //   263: getfield 14	android/widget/ActivityChooserModel$PersistHistoryAsyncTask:this$0	Landroid/widget/ActivityChooserModel;
      //   266: iconst_1
      //   267: invokestatic 140	android/widget/ActivityChooserModel:access$602	(Landroid/widget/ActivityChooserModel;Z)Z
      //   270: pop
      //   271: aload_1
      //   272: ifnull +145 -> 417
      //   275: aload_1
      //   276: invokevirtual 145	java/io/FileOutputStream:close	()V
      //   279: goto +131 -> 410
      //   282: astore_3
      //   283: invokestatic 148	android/widget/ActivityChooserModel:access$400	()Ljava/lang/String;
      //   286: astore_2
      //   287: new 150	java/lang/StringBuilder
      //   290: astore 6
      //   292: aload 6
      //   294: invokespecial 151	java/lang/StringBuilder:<init>	()V
      //   297: aload 6
      //   299: ldc -103
      //   301: invokevirtual 157	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   304: pop
      //   305: aload 6
      //   307: aload_0
      //   308: getfield 14	android/widget/ActivityChooserModel$PersistHistoryAsyncTask:this$0	Landroid/widget/ActivityChooserModel;
      //   311: invokestatic 161	android/widget/ActivityChooserModel:access$500	(Landroid/widget/ActivityChooserModel;)Ljava/lang/String;
      //   314: invokevirtual 157	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   317: pop
      //   318: aload_2
      //   319: aload 6
      //   321: invokevirtual 164	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   324: aload_3
      //   325: invokestatic 170	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   328: pop
      //   329: aload_0
      //   330: getfield 14	android/widget/ActivityChooserModel$PersistHistoryAsyncTask:this$0	Landroid/widget/ActivityChooserModel;
      //   333: iconst_1
      //   334: invokestatic 140	android/widget/ActivityChooserModel:access$602	(Landroid/widget/ActivityChooserModel;Z)Z
      //   337: pop
      //   338: aload_1
      //   339: ifnull +78 -> 417
      //   342: aload_1
      //   343: invokevirtual 145	java/io/FileOutputStream:close	()V
      //   346: goto +64 -> 410
      //   349: astore_2
      //   350: invokestatic 148	android/widget/ActivityChooserModel:access$400	()Ljava/lang/String;
      //   353: astore 6
      //   355: new 150	java/lang/StringBuilder
      //   358: astore_3
      //   359: aload_3
      //   360: invokespecial 151	java/lang/StringBuilder:<init>	()V
      //   363: aload_3
      //   364: ldc -103
      //   366: invokevirtual 157	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   369: pop
      //   370: aload_3
      //   371: aload_0
      //   372: getfield 14	android/widget/ActivityChooserModel$PersistHistoryAsyncTask:this$0	Landroid/widget/ActivityChooserModel;
      //   375: invokestatic 161	android/widget/ActivityChooserModel:access$500	(Landroid/widget/ActivityChooserModel;)Ljava/lang/String;
      //   378: invokevirtual 157	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   381: pop
      //   382: aload 6
      //   384: aload_3
      //   385: invokevirtual 164	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   388: aload_2
      //   389: invokestatic 170	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   392: pop
      //   393: aload_0
      //   394: getfield 14	android/widget/ActivityChooserModel$PersistHistoryAsyncTask:this$0	Landroid/widget/ActivityChooserModel;
      //   397: iconst_1
      //   398: invokestatic 140	android/widget/ActivityChooserModel:access$602	(Landroid/widget/ActivityChooserModel;Z)Z
      //   401: pop
      //   402: aload_1
      //   403: ifnull +14 -> 417
      //   406: aload_1
      //   407: invokevirtual 145	java/io/FileOutputStream:close	()V
      //   410: goto +7 -> 417
      //   413: astore_1
      //   414: goto -4 -> 410
      //   417: aconst_null
      //   418: areturn
      //   419: aload_0
      //   420: getfield 14	android/widget/ActivityChooserModel$PersistHistoryAsyncTask:this$0	Landroid/widget/ActivityChooserModel;
      //   423: iconst_1
      //   424: invokestatic 140	android/widget/ActivityChooserModel:access$602	(Landroid/widget/ActivityChooserModel;Z)Z
      //   427: pop
      //   428: aload_1
      //   429: ifnull +11 -> 440
      //   432: aload_1
      //   433: invokevirtual 145	java/io/FileOutputStream:close	()V
      //   436: goto +4 -> 440
      //   439: astore_1
      //   440: aload_3
      //   441: athrow
      //   442: astore_2
      //   443: invokestatic 148	android/widget/ActivityChooserModel:access$400	()Ljava/lang/String;
      //   446: astore 6
      //   448: new 150	java/lang/StringBuilder
      //   451: dup
      //   452: invokespecial 151	java/lang/StringBuilder:<init>	()V
      //   455: astore_1
      //   456: aload_1
      //   457: ldc -103
      //   459: invokevirtual 157	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   462: pop
      //   463: aload_1
      //   464: aload_3
      //   465: invokevirtual 157	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   468: pop
      //   469: aload 6
      //   471: aload_1
      //   472: invokevirtual 164	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   475: aload_2
      //   476: invokestatic 170	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   479: pop
      //   480: aconst_null
      //   481: areturn
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	482	0	this	PersistHistoryAsyncTask
      //   0	482	1	paramVarArgs	Object[]
      //   6	78	2	localList	List
      //   215	43	2	localIOException	java.io.IOException
      //   286	33	2	str1	String
      //   349	40	2	localIllegalArgumentException	IllegalArgumentException
      //   442	34	2	localFileNotFoundException	java.io.FileNotFoundException
      //   13	173	3	localObject1	Object
      //   211	1	3	localObject2	Object
      //   219	33	3	str2	String
      //   282	43	3	localIllegalStateException	IllegalStateException
      //   358	107	3	localStringBuilder	StringBuilder
      //   71	10	4	i	int
      //   74	96	5	j	int
      //   93	377	6	localObject3	Object
      // Exception table:
      //   from	to	target	type
      //   31	73	211	finally
      //   83	169	211	finally
      //   175	191	211	finally
      //   216	262	211	finally
      //   283	329	211	finally
      //   350	393	211	finally
      //   31	73	215	java/io/IOException
      //   83	169	215	java/io/IOException
      //   175	191	215	java/io/IOException
      //   31	73	282	java/lang/IllegalStateException
      //   83	169	282	java/lang/IllegalStateException
      //   175	191	282	java/lang/IllegalStateException
      //   31	73	349	java/lang/IllegalArgumentException
      //   83	169	349	java/lang/IllegalArgumentException
      //   175	191	349	java/lang/IllegalArgumentException
      //   204	208	413	java/io/IOException
      //   275	279	413	java/io/IOException
      //   342	346	413	java/io/IOException
      //   406	410	413	java/io/IOException
      //   432	436	439	java/io/IOException
      //   14	27	442	java/io/FileNotFoundException
    }
  }
}
