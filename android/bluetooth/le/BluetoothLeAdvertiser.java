package android.bluetooth.le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothUuid;
import android.bluetooth.IBluetoothGatt;
import android.bluetooth.IBluetoothManager;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class BluetoothLeAdvertiser
{
  private static final int FLAGS_FIELD_BYTES = 3;
  private static final int MANUFACTURER_SPECIFIC_DATA_LENGTH = 2;
  private static final int MAX_ADVERTISING_DATA_BYTES = 1650;
  private static final int MAX_LEGACY_ADVERTISING_DATA_BYTES = 31;
  private static final int OVERHEAD_BYTES_PER_FIELD = 2;
  private static final String TAG = "BluetoothLeAdvertiser";
  private final Map<Integer, AdvertisingSet> mAdvertisingSets = Collections.synchronizedMap(new HashMap());
  private BluetoothAdapter mBluetoothAdapter;
  private final IBluetoothManager mBluetoothManager;
  private final Map<AdvertisingSetCallback, IAdvertisingSetCallback> mCallbackWrappers = Collections.synchronizedMap(new HashMap());
  private final Handler mHandler;
  private final Map<AdvertiseCallback, AdvertisingSetCallback> mLegacyAdvertisers = new HashMap();
  
  public BluetoothLeAdvertiser(IBluetoothManager paramIBluetoothManager)
  {
    mBluetoothManager = paramIBluetoothManager;
    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    mHandler = new Handler(Looper.getMainLooper());
  }
  
  private int byteLength(byte[] paramArrayOfByte)
  {
    int i;
    if (paramArrayOfByte == null) {
      i = 0;
    } else {
      i = paramArrayOfByte.length;
    }
    return i;
  }
  
  private void postStartFailure(final AdvertiseCallback paramAdvertiseCallback, final int paramInt)
  {
    mHandler.post(new Runnable()
    {
      public void run()
      {
        paramAdvertiseCallback.onStartFailure(paramInt);
      }
    });
  }
  
  private void postStartSetFailure(Handler paramHandler, final AdvertisingSetCallback paramAdvertisingSetCallback, final int paramInt)
  {
    paramHandler.post(new Runnable()
    {
      public void run()
      {
        paramAdvertisingSetCallback.onAdvertisingSetStarted(null, 0, paramInt);
      }
    });
  }
  
  private void postStartSuccess(final AdvertiseCallback paramAdvertiseCallback, final AdvertiseSettings paramAdvertiseSettings)
  {
    mHandler.post(new Runnable()
    {
      public void run()
      {
        paramAdvertiseCallback.onStartSuccess(paramAdvertiseSettings);
      }
    });
  }
  
  private int totalBytes(AdvertiseData paramAdvertiseData, boolean paramBoolean)
  {
    int i = 0;
    if (paramAdvertiseData == null) {
      return 0;
    }
    if (paramBoolean) {
      j = 3;
    } else {
      j = 0;
    }
    int k = j;
    ParcelUuid localParcelUuid;
    if (paramAdvertiseData.getServiceUuids() != null)
    {
      int m = 0;
      int n = 0;
      int i1 = 0;
      localIterator = paramAdvertiseData.getServiceUuids().iterator();
      while (localIterator.hasNext())
      {
        localParcelUuid = (ParcelUuid)localIterator.next();
        if (BluetoothUuid.is16BitUuid(localParcelUuid)) {
          m++;
        } else if (BluetoothUuid.is32BitUuid(localParcelUuid)) {
          n++;
        } else {
          i1++;
        }
      }
      k = j;
      if (m != 0) {
        k = j + (m * 2 + 2);
      }
      j = k;
      if (n != 0) {
        j = k + (n * 4 + 2);
      }
      k = j;
      if (i1 != 0) {
        k = j + (i1 * 16 + 2);
      }
    }
    Iterator localIterator = paramAdvertiseData.getServiceData().keySet().iterator();
    int j = k;
    while (localIterator.hasNext())
    {
      localParcelUuid = (ParcelUuid)localIterator.next();
      j += 2 + BluetoothUuid.uuidToBytes(localParcelUuid).length + byteLength((byte[])paramAdvertiseData.getServiceData().get(localParcelUuid));
    }
    for (k = i; k < paramAdvertiseData.getManufacturerSpecificData().size(); k++) {
      j += 4 + byteLength((byte[])paramAdvertiseData.getManufacturerSpecificData().valueAt(k));
    }
    k = j;
    if (paramAdvertiseData.getIncludeTxPowerLevel()) {
      k = j + 3;
    }
    j = k;
    if (paramAdvertiseData.getIncludeDeviceName())
    {
      j = k;
      if (mBluetoothAdapter.getName() != null) {
        j = k + (2 + mBluetoothAdapter.getName().length());
      }
    }
    return j;
  }
  
  public void cleanup()
  {
    mLegacyAdvertisers.clear();
    mCallbackWrappers.clear();
    mAdvertisingSets.clear();
  }
  
  public void startAdvertising(AdvertiseSettings paramAdvertiseSettings, AdvertiseData paramAdvertiseData, AdvertiseCallback paramAdvertiseCallback)
  {
    startAdvertising(paramAdvertiseSettings, paramAdvertiseData, null, paramAdvertiseCallback);
  }
  
  /* Error */
  public void startAdvertising(AdvertiseSettings paramAdvertiseSettings, AdvertiseData paramAdvertiseData1, AdvertiseData paramAdvertiseData2, AdvertiseCallback paramAdvertiseCallback)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 71	android/bluetooth/le/BluetoothLeAdvertiser:mLegacyAdvertisers	Ljava/util/Map;
    //   4: astore 5
    //   6: aload 5
    //   8: monitorenter
    //   9: aload_0
    //   10: getfield 91	android/bluetooth/le/BluetoothLeAdvertiser:mBluetoothAdapter	Landroid/bluetooth/BluetoothAdapter;
    //   13: invokestatic 241	android/bluetooth/le/BluetoothLeUtils:checkAdapterStateOn	(Landroid/bluetooth/BluetoothAdapter;)V
    //   16: aload 4
    //   18: ifnull +318 -> 336
    //   21: aload_1
    //   22: invokevirtual 246	android/bluetooth/le/AdvertiseSettings:isConnectable	()Z
    //   25: istore 6
    //   27: aload_0
    //   28: aload_2
    //   29: iload 6
    //   31: invokespecial 248	android/bluetooth/le/BluetoothLeAdvertiser:totalBytes	(Landroid/bluetooth/le/AdvertiseData;Z)I
    //   34: istore 7
    //   36: iconst_1
    //   37: istore 8
    //   39: iload 7
    //   41: bipush 31
    //   43: if_icmpgt +278 -> 321
    //   46: aload_0
    //   47: aload_3
    //   48: iconst_0
    //   49: invokespecial 248	android/bluetooth/le/BluetoothLeAdvertiser:totalBytes	(Landroid/bluetooth/le/AdvertiseData;Z)I
    //   52: bipush 31
    //   54: if_icmple +6 -> 60
    //   57: goto +264 -> 321
    //   60: aload_0
    //   61: getfield 71	android/bluetooth/le/BluetoothLeAdvertiser:mLegacyAdvertisers	Ljava/util/Map;
    //   64: aload 4
    //   66: invokeinterface 252 2 0
    //   71: ifeq +14 -> 85
    //   74: aload_0
    //   75: aload 4
    //   77: iconst_3
    //   78: invokespecial 111	android/bluetooth/le/BluetoothLeAdvertiser:postStartFailure	(Landroid/bluetooth/le/AdvertiseCallback;I)V
    //   81: aload 5
    //   83: monitorexit
    //   84: return
    //   85: new 254	android/bluetooth/le/AdvertisingSetParameters$Builder
    //   88: astore 9
    //   90: aload 9
    //   92: invokespecial 255	android/bluetooth/le/AdvertisingSetParameters$Builder:<init>	()V
    //   95: aload 9
    //   97: iconst_1
    //   98: invokevirtual 259	android/bluetooth/le/AdvertisingSetParameters$Builder:setLegacyMode	(Z)Landroid/bluetooth/le/AdvertisingSetParameters$Builder;
    //   101: pop
    //   102: aload 9
    //   104: iload 6
    //   106: invokevirtual 262	android/bluetooth/le/AdvertisingSetParameters$Builder:setConnectable	(Z)Landroid/bluetooth/le/AdvertisingSetParameters$Builder;
    //   109: pop
    //   110: aload 9
    //   112: iconst_1
    //   113: invokevirtual 265	android/bluetooth/le/AdvertisingSetParameters$Builder:setScannable	(Z)Landroid/bluetooth/le/AdvertisingSetParameters$Builder;
    //   116: pop
    //   117: aload_1
    //   118: invokevirtual 268	android/bluetooth/le/AdvertiseSettings:getMode	()I
    //   121: ifne +15 -> 136
    //   124: aload 9
    //   126: sipush 1600
    //   129: invokevirtual 272	android/bluetooth/le/AdvertisingSetParameters$Builder:setInterval	(I)Landroid/bluetooth/le/AdvertisingSetParameters$Builder;
    //   132: pop
    //   133: goto +40 -> 173
    //   136: aload_1
    //   137: invokevirtual 268	android/bluetooth/le/AdvertiseSettings:getMode	()I
    //   140: iconst_1
    //   141: if_icmpne +15 -> 156
    //   144: aload 9
    //   146: sipush 400
    //   149: invokevirtual 272	android/bluetooth/le/AdvertisingSetParameters$Builder:setInterval	(I)Landroid/bluetooth/le/AdvertisingSetParameters$Builder;
    //   152: pop
    //   153: goto +20 -> 173
    //   156: aload_1
    //   157: invokevirtual 268	android/bluetooth/le/AdvertiseSettings:getMode	()I
    //   160: iconst_2
    //   161: if_icmpne +12 -> 173
    //   164: aload 9
    //   166: sipush 160
    //   169: invokevirtual 272	android/bluetooth/le/AdvertisingSetParameters$Builder:setInterval	(I)Landroid/bluetooth/le/AdvertisingSetParameters$Builder;
    //   172: pop
    //   173: aload_1
    //   174: invokevirtual 275	android/bluetooth/le/AdvertiseSettings:getTxPowerLevel	()I
    //   177: ifne +14 -> 191
    //   180: aload 9
    //   182: bipush -21
    //   184: invokevirtual 278	android/bluetooth/le/AdvertisingSetParameters$Builder:setTxPowerLevel	(I)Landroid/bluetooth/le/AdvertisingSetParameters$Builder;
    //   187: pop
    //   188: goto +56 -> 244
    //   191: aload_1
    //   192: invokevirtual 275	android/bluetooth/le/AdvertiseSettings:getTxPowerLevel	()I
    //   195: iconst_1
    //   196: if_icmpne +14 -> 210
    //   199: aload 9
    //   201: bipush -15
    //   203: invokevirtual 278	android/bluetooth/le/AdvertisingSetParameters$Builder:setTxPowerLevel	(I)Landroid/bluetooth/le/AdvertisingSetParameters$Builder;
    //   206: pop
    //   207: goto +37 -> 244
    //   210: aload_1
    //   211: invokevirtual 275	android/bluetooth/le/AdvertiseSettings:getTxPowerLevel	()I
    //   214: iconst_2
    //   215: if_icmpne +14 -> 229
    //   218: aload 9
    //   220: bipush -7
    //   222: invokevirtual 278	android/bluetooth/le/AdvertisingSetParameters$Builder:setTxPowerLevel	(I)Landroid/bluetooth/le/AdvertisingSetParameters$Builder;
    //   225: pop
    //   226: goto +18 -> 244
    //   229: aload_1
    //   230: invokevirtual 275	android/bluetooth/le/AdvertiseSettings:getTxPowerLevel	()I
    //   233: iconst_3
    //   234: if_icmpne +10 -> 244
    //   237: aload 9
    //   239: iconst_1
    //   240: invokevirtual 278	android/bluetooth/le/AdvertisingSetParameters$Builder:setTxPowerLevel	(I)Landroid/bluetooth/le/AdvertisingSetParameters$Builder;
    //   243: pop
    //   244: iconst_0
    //   245: istore 7
    //   247: aload_1
    //   248: invokevirtual 281	android/bluetooth/le/AdvertiseSettings:getTimeout	()I
    //   251: istore 10
    //   253: iload 10
    //   255: ifle +24 -> 279
    //   258: iload 10
    //   260: bipush 10
    //   262: if_icmpge +10 -> 272
    //   265: iload 8
    //   267: istore 7
    //   269: goto +10 -> 279
    //   272: iload 10
    //   274: bipush 10
    //   276: idiv
    //   277: istore 7
    //   279: aload_0
    //   280: aload 4
    //   282: aload_1
    //   283: invokevirtual 285	android/bluetooth/le/BluetoothLeAdvertiser:wrapOldCallback	(Landroid/bluetooth/le/AdvertiseCallback;Landroid/bluetooth/le/AdvertiseSettings;)Landroid/bluetooth/le/AdvertisingSetCallback;
    //   286: astore_1
    //   287: aload_0
    //   288: getfield 71	android/bluetooth/le/BluetoothLeAdvertiser:mLegacyAdvertisers	Ljava/util/Map;
    //   291: aload 4
    //   293: aload_1
    //   294: invokeinterface 289 3 0
    //   299: pop
    //   300: aload_0
    //   301: aload 9
    //   303: invokevirtual 293	android/bluetooth/le/AdvertisingSetParameters$Builder:build	()Landroid/bluetooth/le/AdvertisingSetParameters;
    //   306: aload_2
    //   307: aload_3
    //   308: aconst_null
    //   309: aconst_null
    //   310: iload 7
    //   312: iconst_0
    //   313: aload_1
    //   314: invokevirtual 297	android/bluetooth/le/BluetoothLeAdvertiser:startAdvertisingSet	(Landroid/bluetooth/le/AdvertisingSetParameters;Landroid/bluetooth/le/AdvertiseData;Landroid/bluetooth/le/AdvertiseData;Landroid/bluetooth/le/PeriodicAdvertisingParameters;Landroid/bluetooth/le/AdvertiseData;IILandroid/bluetooth/le/AdvertisingSetCallback;)V
    //   317: aload 5
    //   319: monitorexit
    //   320: return
    //   321: aload_0
    //   322: aload 4
    //   324: iconst_1
    //   325: invokespecial 111	android/bluetooth/le/BluetoothLeAdvertiser:postStartFailure	(Landroid/bluetooth/le/AdvertiseCallback;I)V
    //   328: aload 5
    //   330: monitorexit
    //   331: return
    //   332: astore_1
    //   333: goto +17 -> 350
    //   336: new 299	java/lang/IllegalArgumentException
    //   339: astore_1
    //   340: aload_1
    //   341: ldc_w 301
    //   344: invokespecial 304	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   347: aload_1
    //   348: athrow
    //   349: astore_1
    //   350: aload 5
    //   352: monitorexit
    //   353: aload_1
    //   354: athrow
    //   355: astore_1
    //   356: goto -6 -> 350
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	359	0	this	BluetoothLeAdvertiser
    //   0	359	1	paramAdvertiseSettings	AdvertiseSettings
    //   0	359	2	paramAdvertiseData1	AdvertiseData
    //   0	359	3	paramAdvertiseData2	AdvertiseData
    //   0	359	4	paramAdvertiseCallback	AdvertiseCallback
    //   4	347	5	localMap	Map
    //   25	80	6	bool	boolean
    //   34	277	7	i	int
    //   37	229	8	j	int
    //   88	214	9	localBuilder	AdvertisingSetParameters.Builder
    //   251	26	10	k	int
    // Exception table:
    //   from	to	target	type
    //   27	36	332	finally
    //   9	16	349	finally
    //   21	27	349	finally
    //   46	57	355	finally
    //   60	84	355	finally
    //   85	133	355	finally
    //   136	153	355	finally
    //   156	173	355	finally
    //   173	188	355	finally
    //   191	207	355	finally
    //   210	226	355	finally
    //   229	244	355	finally
    //   247	253	355	finally
    //   272	279	355	finally
    //   279	320	355	finally
    //   321	331	355	finally
    //   336	349	355	finally
    //   350	353	355	finally
  }
  
  public void startAdvertisingSet(AdvertisingSetParameters paramAdvertisingSetParameters, AdvertiseData paramAdvertiseData1, AdvertiseData paramAdvertiseData2, PeriodicAdvertisingParameters paramPeriodicAdvertisingParameters, AdvertiseData paramAdvertiseData3, int paramInt1, int paramInt2, AdvertisingSetCallback paramAdvertisingSetCallback)
  {
    startAdvertisingSet(paramAdvertisingSetParameters, paramAdvertiseData1, paramAdvertiseData2, paramPeriodicAdvertisingParameters, paramAdvertiseData3, paramInt1, paramInt2, paramAdvertisingSetCallback, new Handler(Looper.getMainLooper()));
  }
  
  public void startAdvertisingSet(AdvertisingSetParameters paramAdvertisingSetParameters, AdvertiseData paramAdvertiseData1, AdvertiseData paramAdvertiseData2, PeriodicAdvertisingParameters paramPeriodicAdvertisingParameters, AdvertiseData paramAdvertiseData3, int paramInt1, int paramInt2, AdvertisingSetCallback paramAdvertisingSetCallback, Handler paramHandler)
  {
    BluetoothLeUtils.checkAdapterStateOn(mBluetoothAdapter);
    if (paramAdvertisingSetCallback != null)
    {
      boolean bool1 = paramAdvertisingSetParameters.isConnectable();
      if (paramAdvertisingSetParameters.isLegacy())
      {
        if (totalBytes(paramAdvertiseData1, bool1) <= 31)
        {
          if (totalBytes(paramAdvertiseData2, false) > 31) {
            throw new IllegalArgumentException("Legacy scan response data too big");
          }
        }
        else {
          throw new IllegalArgumentException("Legacy advertising data too big");
        }
      }
      else
      {
        boolean bool2 = mBluetoothAdapter.isLeCodedPhySupported();
        boolean bool3 = mBluetoothAdapter.isLe2MPhySupported();
        int i = paramAdvertisingSetParameters.getPrimaryPhy();
        int j = paramAdvertisingSetParameters.getSecondaryPhy();
        if ((i == 3) && (!bool2)) {
          throw new IllegalArgumentException("Unsupported primary PHY selected");
        }
        if (((j == 3) && (!bool2)) || ((j == 2) && (!bool3))) {
          throw new IllegalArgumentException("Unsupported secondary PHY selected");
        }
        i = mBluetoothAdapter.getLeMaximumAdvertisingDataLength();
        if (totalBytes(paramAdvertiseData1, bool1) > i) {
          break label499;
        }
        if (totalBytes(paramAdvertiseData2, false) > i) {
          break label488;
        }
        if (totalBytes(paramAdvertiseData3, false) > i) {
          break label477;
        }
        bool1 = mBluetoothAdapter.isLePeriodicAdvertisingSupported();
        if ((paramPeriodicAdvertisingParameters != null) && (!bool1)) {
          throw new IllegalArgumentException("Controller does not support LE Periodic Advertising");
        }
      }
      if ((paramInt2 >= 0) && (paramInt2 <= 255))
      {
        if ((paramInt2 != 0) && (!mBluetoothAdapter.isLePeriodicAdvertisingSupported())) {
          throw new IllegalArgumentException("Can't use maxExtendedAdvertisingEvents with controller that don't support LE Extended Advertising");
        }
        if ((paramInt1 >= 0) && (paramInt1 <= 65535)) {
          try
          {
            IBluetoothGatt localIBluetoothGatt = mBluetoothManager.getBluetoothGatt();
            IAdvertisingSetCallback localIAdvertisingSetCallback = wrap(paramAdvertisingSetCallback, paramHandler);
            if (mCallbackWrappers.putIfAbsent(paramAdvertisingSetCallback, localIAdvertisingSetCallback) == null) {
              try
              {
                localIBluetoothGatt.startAdvertisingSet(paramAdvertisingSetParameters, paramAdvertiseData1, paramAdvertiseData2, paramPeriodicAdvertisingParameters, paramAdvertiseData3, paramInt1, paramInt2, localIAdvertisingSetCallback);
                return;
              }
              catch (RemoteException paramAdvertisingSetParameters)
              {
                Log.e("BluetoothLeAdvertiser", "Failed to start advertising set - ", paramAdvertisingSetParameters);
                postStartSetFailure(paramHandler, paramAdvertisingSetCallback, 4);
                return;
              }
            }
            throw new IllegalArgumentException("callback instance already associated with advertising");
          }
          catch (RemoteException paramAdvertisingSetParameters)
          {
            Log.e("BluetoothLeAdvertiser", "Failed to get Bluetooth gatt - ", paramAdvertisingSetParameters);
            postStartSetFailure(paramHandler, paramAdvertisingSetCallback, 4);
            return;
          }
        }
        paramAdvertisingSetParameters = new StringBuilder();
        paramAdvertisingSetParameters.append("duration out of range: ");
        paramAdvertisingSetParameters.append(paramInt1);
        throw new IllegalArgumentException(paramAdvertisingSetParameters.toString());
      }
      paramAdvertisingSetParameters = new StringBuilder();
      paramAdvertisingSetParameters.append("maxExtendedAdvertisingEvents out of range: ");
      paramAdvertisingSetParameters.append(paramInt2);
      throw new IllegalArgumentException(paramAdvertisingSetParameters.toString());
      label477:
      throw new IllegalArgumentException("Periodic advertising data too big");
      label488:
      throw new IllegalArgumentException("Scan response data too big");
      label499:
      throw new IllegalArgumentException("Advertising data too big");
    }
    throw new IllegalArgumentException("callback cannot be null");
  }
  
  public void startAdvertisingSet(AdvertisingSetParameters paramAdvertisingSetParameters, AdvertiseData paramAdvertiseData1, AdvertiseData paramAdvertiseData2, PeriodicAdvertisingParameters paramPeriodicAdvertisingParameters, AdvertiseData paramAdvertiseData3, AdvertisingSetCallback paramAdvertisingSetCallback)
  {
    startAdvertisingSet(paramAdvertisingSetParameters, paramAdvertiseData1, paramAdvertiseData2, paramPeriodicAdvertisingParameters, paramAdvertiseData3, 0, 0, paramAdvertisingSetCallback, new Handler(Looper.getMainLooper()));
  }
  
  public void startAdvertisingSet(AdvertisingSetParameters paramAdvertisingSetParameters, AdvertiseData paramAdvertiseData1, AdvertiseData paramAdvertiseData2, PeriodicAdvertisingParameters paramPeriodicAdvertisingParameters, AdvertiseData paramAdvertiseData3, AdvertisingSetCallback paramAdvertisingSetCallback, Handler paramHandler)
  {
    startAdvertisingSet(paramAdvertisingSetParameters, paramAdvertiseData1, paramAdvertiseData2, paramPeriodicAdvertisingParameters, paramAdvertiseData3, 0, 0, paramAdvertisingSetCallback, paramHandler);
  }
  
  public void stopAdvertising(AdvertiseCallback paramAdvertiseCallback)
  {
    localMap = mLegacyAdvertisers;
    if (paramAdvertiseCallback != null) {}
    try
    {
      AdvertisingSetCallback localAdvertisingSetCallback = (AdvertisingSetCallback)mLegacyAdvertisers.get(paramAdvertiseCallback);
      if (localAdvertisingSetCallback == null) {
        return;
      }
      stopAdvertisingSet(localAdvertisingSetCallback);
      mLegacyAdvertisers.remove(paramAdvertiseCallback);
      return;
    }
    finally {}
    paramAdvertiseCallback = new java/lang/IllegalArgumentException;
    paramAdvertiseCallback.<init>("callback cannot be null");
    throw paramAdvertiseCallback;
  }
  
  public void stopAdvertisingSet(AdvertisingSetCallback paramAdvertisingSetCallback)
  {
    if (paramAdvertisingSetCallback != null)
    {
      paramAdvertisingSetCallback = (IAdvertisingSetCallback)mCallbackWrappers.remove(paramAdvertisingSetCallback);
      if (paramAdvertisingSetCallback == null) {
        return;
      }
      try
      {
        mBluetoothManager.getBluetoothGatt().stopAdvertisingSet(paramAdvertisingSetCallback);
      }
      catch (RemoteException paramAdvertisingSetCallback)
      {
        Log.e("BluetoothLeAdvertiser", "Failed to stop advertising - ", paramAdvertisingSetCallback);
      }
      return;
    }
    throw new IllegalArgumentException("callback cannot be null");
  }
  
  IAdvertisingSetCallback wrap(final AdvertisingSetCallback paramAdvertisingSetCallback, final Handler paramHandler)
  {
    new IAdvertisingSetCallback.Stub()
    {
      public void onAdvertisingDataSet(final int paramAnonymousInt1, final int paramAnonymousInt2)
      {
        paramHandler.post(new Runnable()
        {
          public void run()
          {
            AdvertisingSet localAdvertisingSet = (AdvertisingSet)mAdvertisingSets.get(Integer.valueOf(paramAnonymousInt1));
            val$callback.onAdvertisingDataSet(localAdvertisingSet, paramAnonymousInt2);
          }
        });
      }
      
      public void onAdvertisingEnabled(final int paramAnonymousInt1, final boolean paramAnonymousBoolean, final int paramAnonymousInt2)
      {
        paramHandler.post(new Runnable()
        {
          public void run()
          {
            AdvertisingSet localAdvertisingSet = (AdvertisingSet)mAdvertisingSets.get(Integer.valueOf(paramAnonymousInt1));
            val$callback.onAdvertisingEnabled(localAdvertisingSet, paramAnonymousBoolean, paramAnonymousInt2);
          }
        });
      }
      
      public void onAdvertisingParametersUpdated(final int paramAnonymousInt1, final int paramAnonymousInt2, final int paramAnonymousInt3)
      {
        paramHandler.post(new Runnable()
        {
          public void run()
          {
            AdvertisingSet localAdvertisingSet = (AdvertisingSet)mAdvertisingSets.get(Integer.valueOf(paramAnonymousInt1));
            val$callback.onAdvertisingParametersUpdated(localAdvertisingSet, paramAnonymousInt2, paramAnonymousInt3);
          }
        });
      }
      
      public void onAdvertisingSetStarted(final int paramAnonymousInt1, final int paramAnonymousInt2, final int paramAnonymousInt3)
      {
        paramHandler.post(new Runnable()
        {
          public void run()
          {
            if (paramAnonymousInt3 != 0)
            {
              val$callback.onAdvertisingSetStarted(null, 0, paramAnonymousInt3);
              mCallbackWrappers.remove(val$callback);
              return;
            }
            AdvertisingSet localAdvertisingSet = new AdvertisingSet(paramAnonymousInt1, mBluetoothManager);
            mAdvertisingSets.put(Integer.valueOf(paramAnonymousInt1), localAdvertisingSet);
            val$callback.onAdvertisingSetStarted(localAdvertisingSet, paramAnonymousInt2, paramAnonymousInt3);
          }
        });
      }
      
      public void onAdvertisingSetStopped(final int paramAnonymousInt)
      {
        paramHandler.post(new Runnable()
        {
          public void run()
          {
            AdvertisingSet localAdvertisingSet = (AdvertisingSet)mAdvertisingSets.get(Integer.valueOf(paramAnonymousInt));
            val$callback.onAdvertisingSetStopped(localAdvertisingSet);
            mAdvertisingSets.remove(Integer.valueOf(paramAnonymousInt));
            mCallbackWrappers.remove(val$callback);
          }
        });
      }
      
      public void onOwnAddressRead(final int paramAnonymousInt1, final int paramAnonymousInt2, final String paramAnonymousString)
      {
        paramHandler.post(new Runnable()
        {
          public void run()
          {
            AdvertisingSet localAdvertisingSet = (AdvertisingSet)mAdvertisingSets.get(Integer.valueOf(paramAnonymousInt1));
            val$callback.onOwnAddressRead(localAdvertisingSet, paramAnonymousInt2, paramAnonymousString);
          }
        });
      }
      
      public void onPeriodicAdvertisingDataSet(final int paramAnonymousInt1, final int paramAnonymousInt2)
      {
        paramHandler.post(new Runnable()
        {
          public void run()
          {
            AdvertisingSet localAdvertisingSet = (AdvertisingSet)mAdvertisingSets.get(Integer.valueOf(paramAnonymousInt1));
            val$callback.onPeriodicAdvertisingDataSet(localAdvertisingSet, paramAnonymousInt2);
          }
        });
      }
      
      public void onPeriodicAdvertisingEnabled(final int paramAnonymousInt1, final boolean paramAnonymousBoolean, final int paramAnonymousInt2)
      {
        paramHandler.post(new Runnable()
        {
          public void run()
          {
            AdvertisingSet localAdvertisingSet = (AdvertisingSet)mAdvertisingSets.get(Integer.valueOf(paramAnonymousInt1));
            val$callback.onPeriodicAdvertisingEnabled(localAdvertisingSet, paramAnonymousBoolean, paramAnonymousInt2);
          }
        });
      }
      
      public void onPeriodicAdvertisingParametersUpdated(final int paramAnonymousInt1, final int paramAnonymousInt2)
      {
        paramHandler.post(new Runnable()
        {
          public void run()
          {
            AdvertisingSet localAdvertisingSet = (AdvertisingSet)mAdvertisingSets.get(Integer.valueOf(paramAnonymousInt1));
            val$callback.onPeriodicAdvertisingParametersUpdated(localAdvertisingSet, paramAnonymousInt2);
          }
        });
      }
      
      public void onScanResponseDataSet(final int paramAnonymousInt1, final int paramAnonymousInt2)
      {
        paramHandler.post(new Runnable()
        {
          public void run()
          {
            AdvertisingSet localAdvertisingSet = (AdvertisingSet)mAdvertisingSets.get(Integer.valueOf(paramAnonymousInt1));
            val$callback.onScanResponseDataSet(localAdvertisingSet, paramAnonymousInt2);
          }
        });
      }
    };
  }
  
  AdvertisingSetCallback wrapOldCallback(final AdvertiseCallback paramAdvertiseCallback, final AdvertiseSettings paramAdvertiseSettings)
  {
    new AdvertisingSetCallback()
    {
      public void onAdvertisingEnabled(AdvertisingSet paramAnonymousAdvertisingSet, boolean paramAnonymousBoolean, int paramAnonymousInt)
      {
        if (paramAnonymousBoolean)
        {
          Log.e("BluetoothLeAdvertiser", "Legacy advertiser should be only disabled on timeout, but was enabled!");
          return;
        }
        stopAdvertising(paramAdvertiseCallback);
      }
      
      public void onAdvertisingSetStarted(AdvertisingSet paramAnonymousAdvertisingSet, int paramAnonymousInt1, int paramAnonymousInt2)
      {
        if (paramAnonymousInt2 != 0)
        {
          BluetoothLeAdvertiser.this.postStartFailure(paramAdvertiseCallback, paramAnonymousInt2);
          return;
        }
        BluetoothLeAdvertiser.this.postStartSuccess(paramAdvertiseCallback, paramAdvertiseSettings);
      }
    };
  }
}
