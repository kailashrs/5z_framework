package android.bluetooth.le;

import android.annotation.SystemApi;
import android.app.ActivityThread;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.IBluetoothGatt;
import android.bluetooth.IBluetoothManager;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.os.WorkSource;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class BluetoothLeScanner
{
  private static final boolean DBG = true;
  public static final String EXTRA_CALLBACK_TYPE = "android.bluetooth.le.extra.CALLBACK_TYPE";
  public static final String EXTRA_ERROR_CODE = "android.bluetooth.le.extra.ERROR_CODE";
  public static final String EXTRA_LIST_SCAN_RESULT = "android.bluetooth.le.extra.LIST_SCAN_RESULT";
  private static final String TAG = "BluetoothLeScanner";
  private static final boolean VDBG = false;
  private BluetoothAdapter mBluetoothAdapter;
  private final IBluetoothManager mBluetoothManager;
  private final Handler mHandler;
  private final Map<ScanCallback, BleScanCallbackWrapper> mLeScanClients;
  
  public BluetoothLeScanner(IBluetoothManager paramIBluetoothManager)
  {
    mBluetoothManager = paramIBluetoothManager;
    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    mHandler = new Handler(Looper.getMainLooper());
    mLeScanClients = new HashMap();
  }
  
  private boolean isHardwareResourcesAvailableForScan(ScanSettings paramScanSettings)
  {
    int i = paramScanSettings.getCallbackType();
    boolean bool = true;
    if (((i & 0x2) == 0) && ((i & 0x4) == 0)) {
      return true;
    }
    if ((!mBluetoothAdapter.isOffloadedFilteringSupported()) || (!mBluetoothAdapter.isHardwareTrackingFiltersAvailable())) {
      bool = false;
    }
    return bool;
  }
  
  private boolean isRoutingAllowedForScan(ScanSettings paramScanSettings)
  {
    return (paramScanSettings.getCallbackType() != 8) || (paramScanSettings.getScanMode() != -1);
  }
  
  private boolean isSettingsAndFilterComboAllowed(ScanSettings paramScanSettings, List<ScanFilter> paramList)
  {
    if ((paramScanSettings.getCallbackType() & 0x6) != 0)
    {
      if (paramList == null) {
        return false;
      }
      paramScanSettings = paramList.iterator();
      while (paramScanSettings.hasNext()) {
        if (((ScanFilter)paramScanSettings.next()).isAllFieldsEmpty()) {
          return false;
        }
      }
    }
    return true;
  }
  
  private boolean isSettingsConfigAllowedForScan(ScanSettings paramScanSettings)
  {
    if (mBluetoothAdapter.isOffloadedFilteringSupported()) {
      return true;
    }
    return (paramScanSettings.getCallbackType() == 1) && (paramScanSettings.getReportDelayMillis() == 0L);
  }
  
  private void postCallbackError(final ScanCallback paramScanCallback, final int paramInt)
  {
    mHandler.post(new Runnable()
    {
      public void run()
      {
        paramScanCallback.onScanFailed(paramInt);
      }
    });
  }
  
  private int postCallbackErrorOrReturn(ScanCallback paramScanCallback, int paramInt)
  {
    if (paramScanCallback == null) {
      return paramInt;
    }
    postCallbackError(paramScanCallback, paramInt);
    return 0;
  }
  
  /* Error */
  private int startScan(List<ScanFilter> paramList, ScanSettings paramScanSettings, WorkSource paramWorkSource, ScanCallback paramScanCallback, PendingIntent paramPendingIntent, List<List<ResultStorageDescriptor>> paramList1)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 57	android/bluetooth/le/BluetoothLeScanner:mBluetoothAdapter	Landroid/bluetooth/BluetoothAdapter;
    //   4: invokestatic 150	android/bluetooth/le/BluetoothLeUtils:checkAdapterStateOn	(Landroid/bluetooth/BluetoothAdapter;)V
    //   7: new 152	java/lang/StringBuilder
    //   10: dup
    //   11: invokespecial 153	java/lang/StringBuilder:<init>	()V
    //   14: astore 7
    //   16: aload 7
    //   18: ldc -101
    //   20: invokevirtual 159	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   23: pop
    //   24: aload 7
    //   26: aload_2
    //   27: invokevirtual 103	android/bluetooth/le/ScanSettings:getScanMode	()I
    //   30: invokevirtual 162	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   33: pop
    //   34: ldc 31
    //   36: aload 7
    //   38: invokevirtual 166	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   41: invokestatic 172	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   44: pop
    //   45: aload 4
    //   47: ifnonnull +21 -> 68
    //   50: aload 5
    //   52: ifnull +6 -> 58
    //   55: goto +13 -> 68
    //   58: new 174	java/lang/IllegalArgumentException
    //   61: dup
    //   62: ldc -80
    //   64: invokespecial 179	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   67: athrow
    //   68: aload_2
    //   69: ifnull +318 -> 387
    //   72: aload_0
    //   73: getfield 75	android/bluetooth/le/BluetoothLeScanner:mLeScanClients	Ljava/util/Map;
    //   76: astore 8
    //   78: aload 8
    //   80: monitorenter
    //   81: aload 4
    //   83: ifnull +36 -> 119
    //   86: aload_0
    //   87: getfield 75	android/bluetooth/le/BluetoothLeScanner:mLeScanClients	Ljava/util/Map;
    //   90: aload 4
    //   92: invokeinterface 185 2 0
    //   97: ifeq +22 -> 119
    //   100: aload_0
    //   101: aload 4
    //   103: iconst_1
    //   104: invokespecial 187	android/bluetooth/le/BluetoothLeScanner:postCallbackErrorOrReturn	(Landroid/bluetooth/le/ScanCallback;I)I
    //   107: istore 9
    //   109: aload 8
    //   111: monitorexit
    //   112: iload 9
    //   114: ireturn
    //   115: astore_1
    //   116: goto +262 -> 378
    //   119: aload_0
    //   120: getfield 49	android/bluetooth/le/BluetoothLeScanner:mBluetoothManager	Landroid/bluetooth/IBluetoothManager;
    //   123: invokeinterface 193 1 0
    //   128: astore 7
    //   130: goto +8 -> 138
    //   133: astore 7
    //   135: aconst_null
    //   136: astore 7
    //   138: aload 7
    //   140: ifnonnull +18 -> 158
    //   143: aload_0
    //   144: aload 4
    //   146: iconst_3
    //   147: invokespecial 187	android/bluetooth/le/BluetoothLeScanner:postCallbackErrorOrReturn	(Landroid/bluetooth/le/ScanCallback;I)I
    //   150: istore 9
    //   152: aload 8
    //   154: monitorexit
    //   155: iload 9
    //   157: ireturn
    //   158: aload_2
    //   159: invokevirtual 92	android/bluetooth/le/ScanSettings:getCallbackType	()I
    //   162: bipush 8
    //   164: if_icmpne +42 -> 206
    //   167: aload_1
    //   168: ifnull +12 -> 180
    //   171: aload_1
    //   172: invokeinterface 196 1 0
    //   177: ifeq +29 -> 206
    //   180: new 198	android/bluetooth/le/ScanFilter$Builder
    //   183: astore_1
    //   184: aload_1
    //   185: invokespecial 199	android/bluetooth/le/ScanFilter$Builder:<init>	()V
    //   188: iconst_1
    //   189: anewarray 122	android/bluetooth/le/ScanFilter
    //   192: dup
    //   193: iconst_0
    //   194: aload_1
    //   195: invokevirtual 203	android/bluetooth/le/ScanFilter$Builder:build	()Landroid/bluetooth/le/ScanFilter;
    //   198: aastore
    //   199: invokestatic 209	java/util/Arrays:asList	([Ljava/lang/Object;)Ljava/util/List;
    //   202: astore_1
    //   203: goto +3 -> 206
    //   206: aload_0
    //   207: aload_2
    //   208: invokespecial 211	android/bluetooth/le/BluetoothLeScanner:isSettingsConfigAllowedForScan	(Landroid/bluetooth/le/ScanSettings;)Z
    //   211: istore 10
    //   213: iload 10
    //   215: ifne +22 -> 237
    //   218: aload_0
    //   219: aload 4
    //   221: iconst_4
    //   222: invokespecial 187	android/bluetooth/le/BluetoothLeScanner:postCallbackErrorOrReturn	(Landroid/bluetooth/le/ScanCallback;I)I
    //   225: istore 9
    //   227: aload 8
    //   229: monitorexit
    //   230: iload 9
    //   232: ireturn
    //   233: astore_1
    //   234: goto +144 -> 378
    //   237: aload_0
    //   238: aload_2
    //   239: invokespecial 213	android/bluetooth/le/BluetoothLeScanner:isHardwareResourcesAvailableForScan	(Landroid/bluetooth/le/ScanSettings;)Z
    //   242: istore 10
    //   244: iload 10
    //   246: ifne +18 -> 264
    //   249: aload_0
    //   250: aload 4
    //   252: iconst_5
    //   253: invokespecial 187	android/bluetooth/le/BluetoothLeScanner:postCallbackErrorOrReturn	(Landroid/bluetooth/le/ScanCallback;I)I
    //   256: istore 9
    //   258: aload 8
    //   260: monitorexit
    //   261: iload 9
    //   263: ireturn
    //   264: aload_0
    //   265: aload_2
    //   266: aload_1
    //   267: invokespecial 215	android/bluetooth/le/BluetoothLeScanner:isSettingsAndFilterComboAllowed	(Landroid/bluetooth/le/ScanSettings;Ljava/util/List;)Z
    //   270: istore 10
    //   272: iload 10
    //   274: ifne +18 -> 292
    //   277: aload_0
    //   278: aload 4
    //   280: iconst_4
    //   281: invokespecial 187	android/bluetooth/le/BluetoothLeScanner:postCallbackErrorOrReturn	(Landroid/bluetooth/le/ScanCallback;I)I
    //   284: istore 9
    //   286: aload 8
    //   288: monitorexit
    //   289: iload 9
    //   291: ireturn
    //   292: aload_0
    //   293: aload_2
    //   294: invokespecial 217	android/bluetooth/le/BluetoothLeScanner:isRoutingAllowedForScan	(Landroid/bluetooth/le/ScanSettings;)Z
    //   297: istore 10
    //   299: iload 10
    //   301: ifne +18 -> 319
    //   304: aload_0
    //   305: aload 4
    //   307: iconst_4
    //   308: invokespecial 187	android/bluetooth/le/BluetoothLeScanner:postCallbackErrorOrReturn	(Landroid/bluetooth/le/ScanCallback;I)I
    //   311: istore 9
    //   313: aload 8
    //   315: monitorexit
    //   316: iload 9
    //   318: ireturn
    //   319: aload 4
    //   321: ifnull +31 -> 352
    //   324: new 8	android/bluetooth/le/BluetoothLeScanner$BleScanCallbackWrapper
    //   327: astore 5
    //   329: aload 5
    //   331: aload_0
    //   332: aload 7
    //   334: aload_1
    //   335: aload_2
    //   336: aload_3
    //   337: aload 4
    //   339: aload 6
    //   341: invokespecial 220	android/bluetooth/le/BluetoothLeScanner$BleScanCallbackWrapper:<init>	(Landroid/bluetooth/le/BluetoothLeScanner;Landroid/bluetooth/IBluetoothGatt;Ljava/util/List;Landroid/bluetooth/le/ScanSettings;Landroid/os/WorkSource;Landroid/bluetooth/le/ScanCallback;Ljava/util/List;)V
    //   344: aload 5
    //   346: invokevirtual 223	android/bluetooth/le/BluetoothLeScanner$BleScanCallbackWrapper:startRegistration	()V
    //   349: goto +17 -> 366
    //   352: aload 7
    //   354: aload 5
    //   356: aload_2
    //   357: aload_1
    //   358: invokestatic 228	android/app/ActivityThread:currentOpPackageName	()Ljava/lang/String;
    //   361: invokeinterface 234 5 0
    //   366: aload 8
    //   368: monitorexit
    //   369: iconst_0
    //   370: ireturn
    //   371: astore_1
    //   372: aload 8
    //   374: monitorexit
    //   375: iconst_3
    //   376: ireturn
    //   377: astore_1
    //   378: aload 8
    //   380: monitorexit
    //   381: aload_1
    //   382: athrow
    //   383: astore_1
    //   384: goto -6 -> 378
    //   387: new 174	java/lang/IllegalArgumentException
    //   390: dup
    //   391: ldc -20
    //   393: invokespecial 179	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   396: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	397	0	this	BluetoothLeScanner
    //   0	397	1	paramList	List<ScanFilter>
    //   0	397	2	paramScanSettings	ScanSettings
    //   0	397	3	paramWorkSource	WorkSource
    //   0	397	4	paramScanCallback	ScanCallback
    //   0	397	5	paramPendingIntent	PendingIntent
    //   0	397	6	paramList1	List<List<ResultStorageDescriptor>>
    //   14	115	7	localObject	Object
    //   133	1	7	localRemoteException	RemoteException
    //   136	217	7	localIBluetoothGatt	IBluetoothGatt
    //   76	303	8	localMap	Map
    //   107	210	9	i	int
    //   211	89	10	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   86	112	115	finally
    //   119	130	115	finally
    //   143	155	115	finally
    //   158	167	115	finally
    //   171	180	115	finally
    //   180	203	115	finally
    //   119	130	133	android/os/RemoteException
    //   218	230	233	finally
    //   249	261	233	finally
    //   277	289	233	finally
    //   304	316	233	finally
    //   352	366	371	android/os/RemoteException
    //   206	213	377	finally
    //   237	244	377	finally
    //   264	272	377	finally
    //   292	299	377	finally
    //   324	329	377	finally
    //   329	349	383	finally
    //   352	366	383	finally
    //   366	369	383	finally
    //   372	375	383	finally
    //   378	381	383	finally
  }
  
  public void cleanup()
  {
    mLeScanClients.clear();
  }
  
  public void flushPendingScanResults(ScanCallback paramScanCallback)
  {
    BluetoothLeUtils.checkAdapterStateOn(mBluetoothAdapter);
    if (paramScanCallback != null) {
      synchronized (mLeScanClients)
      {
        paramScanCallback = (BleScanCallbackWrapper)mLeScanClients.get(paramScanCallback);
        if (paramScanCallback == null) {
          return;
        }
        paramScanCallback.flushPendingBatchResults();
        return;
      }
    }
    throw new IllegalArgumentException("callback cannot be null!");
  }
  
  public int startScan(List<ScanFilter> paramList, ScanSettings paramScanSettings, PendingIntent paramPendingIntent)
  {
    if (paramScanSettings == null) {
      paramScanSettings = new ScanSettings.Builder().build();
    }
    return startScan(paramList, paramScanSettings, null, null, paramPendingIntent, null);
  }
  
  public void startScan(ScanCallback paramScanCallback)
  {
    startScan(null, new ScanSettings.Builder().build(), paramScanCallback);
  }
  
  public void startScan(List<ScanFilter> paramList, ScanSettings paramScanSettings, ScanCallback paramScanCallback)
  {
    startScan(paramList, paramScanSettings, null, paramScanCallback, null, null);
  }
  
  @SystemApi
  public void startScanFromSource(WorkSource paramWorkSource, ScanCallback paramScanCallback)
  {
    startScanFromSource(null, new ScanSettings.Builder().build(), paramWorkSource, paramScanCallback);
  }
  
  @SystemApi
  public void startScanFromSource(List<ScanFilter> paramList, ScanSettings paramScanSettings, WorkSource paramWorkSource, ScanCallback paramScanCallback)
  {
    startScan(paramList, paramScanSettings, paramWorkSource, paramScanCallback, null, null);
  }
  
  @SystemApi
  public void startTruncatedScan(List<TruncatedFilter> paramList, ScanSettings paramScanSettings, ScanCallback paramScanCallback)
  {
    int i = paramList.size();
    ArrayList localArrayList1 = new ArrayList(i);
    ArrayList localArrayList2 = new ArrayList(i);
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      paramList = (TruncatedFilter)localIterator.next();
      localArrayList1.add(paramList.getFilter());
      localArrayList2.add(paramList.getStorageDescriptors());
    }
    startScan(localArrayList1, paramScanSettings, null, paramScanCallback, null, localArrayList2);
  }
  
  public void stopScan(PendingIntent paramPendingIntent)
  {
    BluetoothLeUtils.checkAdapterStateOn(mBluetoothAdapter);
    try
    {
      mBluetoothManager.getBluetoothGatt().stopScanForIntent(paramPendingIntent, ActivityThread.currentOpPackageName());
    }
    catch (RemoteException paramPendingIntent) {}
  }
  
  public void stopScan(ScanCallback paramScanCallback)
  {
    BluetoothLeUtils.checkAdapterStateOn(mBluetoothAdapter);
    synchronized (mLeScanClients)
    {
      paramScanCallback = (BleScanCallbackWrapper)mLeScanClients.remove(paramScanCallback);
      if (paramScanCallback == null)
      {
        Log.d("BluetoothLeScanner", "could not find callback wrapper");
        return;
      }
      paramScanCallback.stopLeScan();
      return;
    }
  }
  
  private class BleScanCallbackWrapper
    extends IScannerCallback.Stub
  {
    private static final int REGISTRATION_CALLBACK_TIMEOUT_MILLIS = 2000;
    private IBluetoothGatt mBluetoothGatt;
    private final List<ScanFilter> mFilters;
    private List<List<ResultStorageDescriptor>> mResultStorages;
    private final ScanCallback mScanCallback;
    private int mScannerId;
    private ScanSettings mSettings;
    private final WorkSource mWorkSource;
    
    public BleScanCallbackWrapper(List<ScanFilter> paramList, ScanSettings paramScanSettings, WorkSource paramWorkSource, ScanCallback paramScanCallback, List<List<ResultStorageDescriptor>> paramList1)
    {
      mBluetoothGatt = paramList;
      mFilters = paramScanSettings;
      mSettings = paramWorkSource;
      mWorkSource = paramScanCallback;
      mScanCallback = paramList1;
      mScannerId = 0;
      Object localObject;
      mResultStorages = localObject;
    }
    
    void flushPendingBatchResults()
    {
      try
      {
        if (mScannerId <= 0)
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Error state, mLeHandle: ");
          localStringBuilder.append(mScannerId);
          Log.e("BluetoothLeScanner", localStringBuilder.toString());
          return;
        }
        try
        {
          mBluetoothGatt.flushPendingBatchResults(mScannerId);
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("BluetoothLeScanner", "Failed to get pending scan results", localRemoteException);
        }
        return;
      }
      finally {}
    }
    
    public void onBatchScanResults(final List<ScanResult> paramList)
    {
      new Handler(Looper.getMainLooper()).post(new Runnable()
      {
        public void run()
        {
          mScanCallback.onBatchScanResults(paramList);
        }
      });
    }
    
    public void onFoundOrLost(final boolean paramBoolean, final ScanResult paramScanResult)
    {
      try
      {
        if (mScannerId <= 0) {
          return;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable()
        {
          public void run()
          {
            if (paramBoolean) {
              mScanCallback.onScanResult(2, paramScanResult);
            } else {
              mScanCallback.onScanResult(4, paramScanResult);
            }
          }
        });
        return;
      }
      finally {}
    }
    
    public void onScanManagerErrorCallback(int paramInt)
    {
      try
      {
        if (mScannerId <= 0) {
          return;
        }
        BluetoothLeScanner.this.postCallbackError(mScanCallback, paramInt);
        return;
      }
      finally {}
    }
    
    public void onScanResult(final ScanResult paramScanResult)
    {
      try
      {
        if (mScannerId <= 0) {
          return;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable()
        {
          public void run()
          {
            mScanCallback.onScanResult(1, paramScanResult);
          }
        });
        return;
      }
      finally {}
    }
    
    /* Error */
    public void onScannerRegistered(int paramInt1, int paramInt2)
    {
      // Byte code:
      //   0: new 63	java/lang/StringBuilder
      //   3: dup
      //   4: invokespecial 64	java/lang/StringBuilder:<init>	()V
      //   7: astore_3
      //   8: aload_3
      //   9: ldc -119
      //   11: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   14: pop
      //   15: aload_3
      //   16: iload_1
      //   17: invokevirtual 73	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   20: pop
      //   21: aload_3
      //   22: ldc -117
      //   24: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   27: pop
      //   28: aload_3
      //   29: iload_2
      //   30: invokevirtual 73	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   33: pop
      //   34: aload_3
      //   35: ldc -115
      //   37: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   40: pop
      //   41: aload_3
      //   42: aload_0
      //   43: getfield 51	android/bluetooth/le/BluetoothLeScanner$BleScanCallbackWrapper:mScannerId	I
      //   46: invokevirtual 73	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   49: pop
      //   50: ldc 75
      //   52: aload_3
      //   53: invokevirtual 79	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   56: invokestatic 144	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   59: pop
      //   60: aload_0
      //   61: monitorenter
      //   62: iload_1
      //   63: ifne +109 -> 172
      //   66: aload_0
      //   67: getfield 51	android/bluetooth/le/BluetoothLeScanner$BleScanCallbackWrapper:mScannerId	I
      //   70: iconst_m1
      //   71: if_icmpne +16 -> 87
      //   74: aload_0
      //   75: getfield 41	android/bluetooth/le/BluetoothLeScanner$BleScanCallbackWrapper:mBluetoothGatt	Landroid/bluetooth/IBluetoothGatt;
      //   78: iload_2
      //   79: invokeinterface 147 2 0
      //   84: goto +36 -> 120
      //   87: aload_0
      //   88: iload_2
      //   89: putfield 51	android/bluetooth/le/BluetoothLeScanner$BleScanCallbackWrapper:mScannerId	I
      //   92: aload_0
      //   93: getfield 41	android/bluetooth/le/BluetoothLeScanner$BleScanCallbackWrapper:mBluetoothGatt	Landroid/bluetooth/IBluetoothGatt;
      //   96: aload_0
      //   97: getfield 51	android/bluetooth/le/BluetoothLeScanner$BleScanCallbackWrapper:mScannerId	I
      //   100: aload_0
      //   101: getfield 45	android/bluetooth/le/BluetoothLeScanner$BleScanCallbackWrapper:mSettings	Landroid/bluetooth/le/ScanSettings;
      //   104: aload_0
      //   105: getfield 43	android/bluetooth/le/BluetoothLeScanner$BleScanCallbackWrapper:mFilters	Ljava/util/List;
      //   108: aload_0
      //   109: getfield 53	android/bluetooth/le/BluetoothLeScanner$BleScanCallbackWrapper:mResultStorages	Ljava/util/List;
      //   112: invokestatic 152	android/app/ActivityThread:currentOpPackageName	()Ljava/lang/String;
      //   115: invokeinterface 156 6 0
      //   120: goto +72 -> 192
      //   123: astore_3
      //   124: goto +75 -> 199
      //   127: astore_3
      //   128: new 63	java/lang/StringBuilder
      //   131: astore 4
      //   133: aload 4
      //   135: invokespecial 64	java/lang/StringBuilder:<init>	()V
      //   138: aload 4
      //   140: ldc -98
      //   142: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   145: pop
      //   146: aload 4
      //   148: aload_3
      //   149: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   152: pop
      //   153: ldc 75
      //   155: aload 4
      //   157: invokevirtual 79	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   160: invokestatic 85	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
      //   163: pop
      //   164: aload_0
      //   165: iconst_m1
      //   166: putfield 51	android/bluetooth/le/BluetoothLeScanner$BleScanCallbackWrapper:mScannerId	I
      //   169: goto -49 -> 120
      //   172: iload_1
      //   173: bipush 6
      //   175: if_icmpne +12 -> 187
      //   178: aload_0
      //   179: bipush -2
      //   181: putfield 51	android/bluetooth/le/BluetoothLeScanner$BleScanCallbackWrapper:mScannerId	I
      //   184: goto +8 -> 192
      //   187: aload_0
      //   188: iconst_m1
      //   189: putfield 51	android/bluetooth/le/BluetoothLeScanner$BleScanCallbackWrapper:mScannerId	I
      //   192: aload_0
      //   193: invokevirtual 166	java/lang/Object:notifyAll	()V
      //   196: aload_0
      //   197: monitorexit
      //   198: return
      //   199: aload_0
      //   200: monitorexit
      //   201: aload_3
      //   202: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	203	0	this	BleScanCallbackWrapper
      //   0	203	1	paramInt1	int
      //   0	203	2	paramInt2	int
      //   7	46	3	localStringBuilder1	StringBuilder
      //   123	1	3	localObject	Object
      //   127	75	3	localRemoteException	RemoteException
      //   131	25	4	localStringBuilder2	StringBuilder
      // Exception table:
      //   from	to	target	type
      //   66	84	123	finally
      //   87	120	123	finally
      //   128	169	123	finally
      //   178	184	123	finally
      //   187	192	123	finally
      //   192	198	123	finally
      //   199	201	123	finally
      //   66	84	127	android/os/RemoteException
      //   66	84	127	java/lang/SecurityException
      //   87	120	127	android/os/RemoteException
      //   87	120	127	java/lang/SecurityException
    }
    
    public void startRegistration()
    {
      try
      {
        if (mScannerId != -1)
        {
          int i = mScannerId;
          if (i != -2)
          {
            try
            {
              mBluetoothGatt.registerScanner(this, mWorkSource);
              wait(2000L);
            }
            catch (InterruptedException|RemoteException localInterruptedException)
            {
              Log.e("BluetoothLeScanner", "application registeration exception", localInterruptedException);
              BluetoothLeScanner.this.postCallbackError(mScanCallback, 3);
            }
            if (mScannerId > 0)
            {
              mLeScanClients.put(mScanCallback, this);
            }
            else
            {
              if (mScannerId == 0) {
                mScannerId = -1;
              }
              if (mScannerId == -2) {
                return;
              }
              BluetoothLeScanner.this.postCallbackError(mScanCallback, 2);
            }
            return;
          }
        }
        return;
      }
      finally {}
    }
    
    public void stopLeScan()
    {
      Log.d("BluetoothLeScanner", "stop le scan");
      try
      {
        if (mScannerId <= 0)
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Error state, mLeHandle: ");
          localStringBuilder.append(mScannerId);
          Log.e("BluetoothLeScanner", localStringBuilder.toString());
          return;
        }
        try
        {
          mBluetoothGatt.stopScan(mScannerId);
          mBluetoothGatt.unregisterScanner(mScannerId);
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("BluetoothLeScanner", "Failed to stop scan and unregister", localRemoteException);
        }
        mScannerId = -1;
        return;
      }
      finally {}
    }
  }
}
