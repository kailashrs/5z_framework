package android.bluetooth.le;

import android.bluetooth.BluetoothUuid;
import android.os.ParcelUuid;
import android.util.SparseArray;
import java.util.List;
import java.util.Map;

public final class ScanRecord
{
  private static final int DATA_TYPE_FLAGS = 1;
  private static final int DATA_TYPE_LOCAL_NAME_COMPLETE = 9;
  private static final int DATA_TYPE_LOCAL_NAME_SHORT = 8;
  private static final int DATA_TYPE_MANUFACTURER_SPECIFIC_DATA = 255;
  private static final int DATA_TYPE_SERVICE_DATA_128_BIT = 33;
  private static final int DATA_TYPE_SERVICE_DATA_16_BIT = 22;
  private static final int DATA_TYPE_SERVICE_DATA_32_BIT = 32;
  private static final int DATA_TYPE_SERVICE_SOLICITATION_UUIDS_128_BIT = 21;
  private static final int DATA_TYPE_SERVICE_SOLICITATION_UUIDS_16_BIT = 20;
  private static final int DATA_TYPE_SERVICE_SOLICITATION_UUIDS_32_BIT = 31;
  private static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_COMPLETE = 7;
  private static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_PARTIAL = 6;
  private static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_COMPLETE = 3;
  private static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_PARTIAL = 2;
  private static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_COMPLETE = 5;
  private static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_PARTIAL = 4;
  private static final int DATA_TYPE_TX_POWER_LEVEL = 10;
  private static final String TAG = "ScanRecord";
  private final int mAdvertiseFlags;
  private final byte[] mBytes;
  private final String mDeviceName;
  private final SparseArray<byte[]> mManufacturerSpecificData;
  private final Map<ParcelUuid, byte[]> mServiceData;
  private final List<ParcelUuid> mServiceSolicitationUuids;
  private final List<ParcelUuid> mServiceUuids;
  private final int mTxPowerLevel;
  
  private ScanRecord(List<ParcelUuid> paramList1, List<ParcelUuid> paramList2, SparseArray<byte[]> paramSparseArray, Map<ParcelUuid, byte[]> paramMap, int paramInt1, int paramInt2, String paramString, byte[] paramArrayOfByte)
  {
    mServiceSolicitationUuids = paramList2;
    mServiceUuids = paramList1;
    mManufacturerSpecificData = paramSparseArray;
    mServiceData = paramMap;
    mDeviceName = paramString;
    mAdvertiseFlags = paramInt1;
    mTxPowerLevel = paramInt2;
    mBytes = paramArrayOfByte;
  }
  
  private static byte[] extractBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    byte[] arrayOfByte = new byte[paramInt2];
    System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, paramInt2);
    return arrayOfByte;
  }
  
  /* Error */
  public static ScanRecord parseFromBytes(byte[] paramArrayOfByte)
  {
    // Byte code:
    //   0: aload_0
    //   1: ifnonnull +5 -> 6
    //   4: aconst_null
    //   5: areturn
    //   6: new 96	java/util/ArrayList
    //   9: dup
    //   10: invokespecial 97	java/util/ArrayList:<init>	()V
    //   13: astore_1
    //   14: new 96	java/util/ArrayList
    //   17: dup
    //   18: invokespecial 97	java/util/ArrayList:<init>	()V
    //   21: astore_2
    //   22: new 99	android/util/SparseArray
    //   25: dup
    //   26: invokespecial 100	android/util/SparseArray:<init>	()V
    //   29: astore_3
    //   30: new 102	android/util/ArrayMap
    //   33: dup
    //   34: invokespecial 103	android/util/ArrayMap:<init>	()V
    //   37: astore 4
    //   39: iconst_m1
    //   40: istore 5
    //   42: aconst_null
    //   43: astore 6
    //   45: ldc 104
    //   47: istore 7
    //   49: iconst_0
    //   50: istore 8
    //   52: aload_0
    //   53: arraylength
    //   54: istore 9
    //   56: iload 8
    //   58: iload 9
    //   60: if_icmpge +419 -> 479
    //   63: iload 8
    //   65: iconst_1
    //   66: iadd
    //   67: istore 10
    //   69: aload_0
    //   70: iload 8
    //   72: baload
    //   73: istore 8
    //   75: iload 8
    //   77: sipush 255
    //   80: iand
    //   81: istore 8
    //   83: iload 8
    //   85: ifne +6 -> 91
    //   88: goto +391 -> 479
    //   91: iload 8
    //   93: iconst_1
    //   94: isub
    //   95: istore 11
    //   97: iload 10
    //   99: iconst_1
    //   100: iadd
    //   101: istore 9
    //   103: aload_0
    //   104: iload 10
    //   106: baload
    //   107: sipush 255
    //   110: iand
    //   111: istore 10
    //   113: iload 10
    //   115: sipush 255
    //   118: if_icmpeq +308 -> 426
    //   121: iload 10
    //   123: tableswitch	default:+53->176, 1:+290->413, 2:+276->399, 3:+276->399, 4:+262->385, 5:+262->385, 6:+247->370, 7:+247->370, 8:+227->350, 9:+227->350, 10:+218->341
    //   176: iload 10
    //   178: tableswitch	default:+26->204, 20:+149->327, 21:+134->312, 22:+71->249
    //   204: iload 10
    //   206: tableswitch	default:+26->232, 31:+29->235, 32:+43->249, 33:+43->249
    //   232: goto +232 -> 464
    //   235: aload_0
    //   236: iload 9
    //   238: iload 11
    //   240: iconst_4
    //   241: aload_2
    //   242: invokestatic 108	android/bluetooth/le/ScanRecord:parseServiceSolicitationUuid	([BIIILjava/util/List;)I
    //   245: pop
    //   246: goto -14 -> 232
    //   249: iconst_2
    //   250: istore 8
    //   252: iload 10
    //   254: bipush 32
    //   256: if_icmpne +9 -> 265
    //   259: iconst_4
    //   260: istore 8
    //   262: goto +14 -> 276
    //   265: iload 10
    //   267: bipush 33
    //   269: if_icmpne +7 -> 276
    //   272: bipush 16
    //   274: istore 8
    //   276: aload 4
    //   278: aload_0
    //   279: iload 9
    //   281: iload 8
    //   283: invokestatic 110	android/bluetooth/le/ScanRecord:extractBytes	([BII)[B
    //   286: invokestatic 116	android/bluetooth/BluetoothUuid:parseUuidFrom	([B)Landroid/os/ParcelUuid;
    //   289: aload_0
    //   290: iload 9
    //   292: iload 8
    //   294: iadd
    //   295: iload 11
    //   297: iload 8
    //   299: isub
    //   300: invokestatic 110	android/bluetooth/le/ScanRecord:extractBytes	([BII)[B
    //   303: invokeinterface 122 3 0
    //   308: pop
    //   309: goto +155 -> 464
    //   312: aload_0
    //   313: iload 9
    //   315: iload 11
    //   317: bipush 16
    //   319: aload_2
    //   320: invokestatic 108	android/bluetooth/le/ScanRecord:parseServiceSolicitationUuid	([BIIILjava/util/List;)I
    //   323: pop
    //   324: goto +140 -> 464
    //   327: aload_0
    //   328: iload 9
    //   330: iload 11
    //   332: iconst_2
    //   333: aload_2
    //   334: invokestatic 108	android/bluetooth/le/ScanRecord:parseServiceSolicitationUuid	([BIIILjava/util/List;)I
    //   337: pop
    //   338: goto +126 -> 464
    //   341: aload_0
    //   342: iload 9
    //   344: baload
    //   345: istore 7
    //   347: goto +117 -> 464
    //   350: new 124	java/lang/String
    //   353: dup
    //   354: aload_0
    //   355: iload 9
    //   357: iload 11
    //   359: invokestatic 110	android/bluetooth/le/ScanRecord:extractBytes	([BII)[B
    //   362: invokespecial 127	java/lang/String:<init>	([B)V
    //   365: astore 6
    //   367: goto +97 -> 464
    //   370: aload_0
    //   371: iload 9
    //   373: iload 11
    //   375: bipush 16
    //   377: aload_1
    //   378: invokestatic 130	android/bluetooth/le/ScanRecord:parseServiceUuid	([BIIILjava/util/List;)I
    //   381: pop
    //   382: goto +82 -> 464
    //   385: aload_0
    //   386: iload 9
    //   388: iload 11
    //   390: iconst_4
    //   391: aload_1
    //   392: invokestatic 130	android/bluetooth/le/ScanRecord:parseServiceUuid	([BIIILjava/util/List;)I
    //   395: pop
    //   396: goto +68 -> 464
    //   399: aload_0
    //   400: iload 9
    //   402: iload 11
    //   404: iconst_2
    //   405: aload_1
    //   406: invokestatic 130	android/bluetooth/le/ScanRecord:parseServiceUuid	([BIIILjava/util/List;)I
    //   409: pop
    //   410: goto +54 -> 464
    //   413: aload_0
    //   414: iload 9
    //   416: baload
    //   417: sipush 255
    //   420: iand
    //   421: istore 5
    //   423: goto +41 -> 464
    //   426: aload_3
    //   427: aload_0
    //   428: iload 9
    //   430: iconst_1
    //   431: iadd
    //   432: baload
    //   433: sipush 255
    //   436: iand
    //   437: bipush 8
    //   439: ishl
    //   440: sipush 255
    //   443: aload_0
    //   444: iload 9
    //   446: baload
    //   447: iand
    //   448: iadd
    //   449: aload_0
    //   450: iload 9
    //   452: iconst_2
    //   453: iadd
    //   454: iload 11
    //   456: iconst_2
    //   457: isub
    //   458: invokestatic 110	android/bluetooth/le/ScanRecord:extractBytes	([BII)[B
    //   461: invokevirtual 133	android/util/SparseArray:put	(ILjava/lang/Object;)V
    //   464: iload 9
    //   466: iload 11
    //   468: iadd
    //   469: istore 8
    //   471: goto -419 -> 52
    //   474: astore 4
    //   476: goto +80 -> 556
    //   479: aload_1
    //   480: invokeinterface 139 1 0
    //   485: istore 12
    //   487: iload 12
    //   489: ifeq +8 -> 497
    //   492: aconst_null
    //   493: astore_1
    //   494: goto +3 -> 497
    //   497: aload_2
    //   498: invokeinterface 139 1 0
    //   503: istore 12
    //   505: iload 12
    //   507: ifeq +8 -> 515
    //   510: aconst_null
    //   511: astore_2
    //   512: goto +3 -> 515
    //   515: new 2	android/bluetooth/le/ScanRecord
    //   518: dup
    //   519: aload_1
    //   520: aload_2
    //   521: aload_3
    //   522: aload 4
    //   524: iload 5
    //   526: iload 7
    //   528: aload 6
    //   530: aload_0
    //   531: invokespecial 141	android/bluetooth/le/ScanRecord:<init>	(Ljava/util/List;Ljava/util/List;Landroid/util/SparseArray;Ljava/util/Map;IILjava/lang/String;[B)V
    //   534: astore 4
    //   536: aload 4
    //   538: areturn
    //   539: astore 4
    //   541: goto +15 -> 556
    //   544: astore 4
    //   546: goto +10 -> 556
    //   549: astore 4
    //   551: goto +5 -> 556
    //   554: astore 4
    //   556: new 143	java/lang/StringBuilder
    //   559: dup
    //   560: invokespecial 144	java/lang/StringBuilder:<init>	()V
    //   563: astore 4
    //   565: aload 4
    //   567: ldc -110
    //   569: invokevirtual 150	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   572: pop
    //   573: aload 4
    //   575: aload_0
    //   576: invokestatic 156	java/util/Arrays:toString	([B)Ljava/lang/String;
    //   579: invokevirtual 150	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   582: pop
    //   583: ldc 43
    //   585: aload 4
    //   587: invokevirtual 159	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   590: invokestatic 165	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   593: pop
    //   594: new 2	android/bluetooth/le/ScanRecord
    //   597: dup
    //   598: aconst_null
    //   599: aconst_null
    //   600: aconst_null
    //   601: aconst_null
    //   602: iconst_m1
    //   603: ldc 104
    //   605: aconst_null
    //   606: aload_0
    //   607: invokespecial 141	android/bluetooth/le/ScanRecord:<init>	(Ljava/util/List;Ljava/util/List;Landroid/util/SparseArray;Ljava/util/Map;IILjava/lang/String;[B)V
    //   610: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	611	0	paramArrayOfByte	byte[]
    //   13	507	1	localArrayList1	java.util.ArrayList
    //   21	500	2	localArrayList2	java.util.ArrayList
    //   29	493	3	localSparseArray	SparseArray
    //   37	240	4	localArrayMap	android.util.ArrayMap
    //   474	49	4	localException1	Exception
    //   534	3	4	localScanRecord	ScanRecord
    //   539	1	4	localException2	Exception
    //   544	1	4	localException3	Exception
    //   549	1	4	localException4	Exception
    //   554	1	4	localException5	Exception
    //   563	23	4	localStringBuilder	StringBuilder
    //   40	485	5	i	int
    //   43	486	6	str	String
    //   47	480	7	j	int
    //   50	420	8	k	int
    //   54	415	9	m	int
    //   67	203	10	n	int
    //   95	374	11	i1	int
    //   485	21	12	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   235	246	474	java/lang/Exception
    //   276	309	474	java/lang/Exception
    //   312	324	474	java/lang/Exception
    //   327	338	474	java/lang/Exception
    //   350	367	474	java/lang/Exception
    //   370	382	474	java/lang/Exception
    //   385	396	474	java/lang/Exception
    //   399	410	474	java/lang/Exception
    //   426	464	474	java/lang/Exception
    //   515	536	539	java/lang/Exception
    //   497	505	544	java/lang/Exception
    //   479	487	549	java/lang/Exception
    //   52	56	554	java/lang/Exception
  }
  
  private static int parseServiceSolicitationUuid(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, List<ParcelUuid> paramList)
  {
    while (paramInt2 > 0)
    {
      paramList.add(BluetoothUuid.parseUuidFrom(extractBytes(paramArrayOfByte, paramInt1, paramInt3)));
      paramInt2 -= paramInt3;
      paramInt1 += paramInt3;
    }
    return paramInt1;
  }
  
  private static int parseServiceUuid(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, List<ParcelUuid> paramList)
  {
    while (paramInt2 > 0)
    {
      paramList.add(BluetoothUuid.parseUuidFrom(extractBytes(paramArrayOfByte, paramInt1, paramInt3)));
      paramInt2 -= paramInt3;
      paramInt1 += paramInt3;
    }
    return paramInt1;
  }
  
  public int getAdvertiseFlags()
  {
    return mAdvertiseFlags;
  }
  
  public byte[] getBytes()
  {
    return mBytes;
  }
  
  public String getDeviceName()
  {
    return mDeviceName;
  }
  
  public SparseArray<byte[]> getManufacturerSpecificData()
  {
    return mManufacturerSpecificData;
  }
  
  public byte[] getManufacturerSpecificData(int paramInt)
  {
    return (byte[])mManufacturerSpecificData.get(paramInt);
  }
  
  public Map<ParcelUuid, byte[]> getServiceData()
  {
    return mServiceData;
  }
  
  public byte[] getServiceData(ParcelUuid paramParcelUuid)
  {
    if (paramParcelUuid == null) {
      return null;
    }
    return (byte[])mServiceData.get(paramParcelUuid);
  }
  
  public List<ParcelUuid> getServiceSolicitationUuids()
  {
    return mServiceSolicitationUuids;
  }
  
  public List<ParcelUuid> getServiceUuids()
  {
    return mServiceUuids;
  }
  
  public int getTxPowerLevel()
  {
    return mTxPowerLevel;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ScanRecord [mAdvertiseFlags=");
    localStringBuilder.append(mAdvertiseFlags);
    localStringBuilder.append(", mServiceUuids=");
    localStringBuilder.append(mServiceUuids);
    localStringBuilder.append(", mServiceSolicitationUuids=");
    localStringBuilder.append(mServiceSolicitationUuids);
    localStringBuilder.append(", mManufacturerSpecificData=");
    localStringBuilder.append(BluetoothLeUtils.toString(mManufacturerSpecificData));
    localStringBuilder.append(", mServiceData=");
    localStringBuilder.append(BluetoothLeUtils.toString(mServiceData));
    localStringBuilder.append(", mTxPowerLevel=");
    localStringBuilder.append(mTxPowerLevel);
    localStringBuilder.append(", mDeviceName=");
    localStringBuilder.append(mDeviceName);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
}
