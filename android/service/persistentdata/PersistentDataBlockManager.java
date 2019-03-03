package android.service.persistentdata;

import android.annotation.SuppressLint;
import android.annotation.SystemApi;
import android.os.RemoteException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SystemApi
public class PersistentDataBlockManager
{
  public static final int FLASH_LOCK_LOCKED = 1;
  public static final int FLASH_LOCK_UNKNOWN = -1;
  public static final int FLASH_LOCK_UNLOCKED = 0;
  private static final String TAG = PersistentDataBlockManager.class.getSimpleName();
  private IPersistentDataBlockService sService;
  
  public PersistentDataBlockManager(IPersistentDataBlockService paramIPersistentDataBlockService)
  {
    sService = paramIPersistentDataBlockService;
  }
  
  /* Error */
  private int getAccFlag()
  {
    // Byte code:
    //   0: ldc 44
    //   2: astore_1
    //   3: new 46	java/io/File
    //   6: dup
    //   7: ldc 44
    //   9: invokespecial 49	java/io/File:<init>	(Ljava/lang/String;)V
    //   12: invokevirtual 53	java/io/File:exists	()Z
    //   15: ifne +41 -> 56
    //   18: ldc 55
    //   20: astore_1
    //   21: new 46	java/io/File
    //   24: dup
    //   25: ldc 55
    //   27: invokespecial 49	java/io/File:<init>	(Ljava/lang/String;)V
    //   30: invokevirtual 53	java/io/File:exists	()Z
    //   33: ifne +23 -> 56
    //   36: ldc 57
    //   38: astore_1
    //   39: new 46	java/io/File
    //   42: dup
    //   43: ldc 57
    //   45: invokespecial 49	java/io/File:<init>	(Ljava/lang/String;)V
    //   48: invokevirtual 53	java/io/File:exists	()Z
    //   51: ifne +5 -> 56
    //   54: iconst_0
    //   55: ireturn
    //   56: iconst_0
    //   57: istore_2
    //   58: iconst_0
    //   59: istore_3
    //   60: iconst_0
    //   61: istore 4
    //   63: iconst_0
    //   64: istore 5
    //   66: aconst_null
    //   67: astore 6
    //   69: aconst_null
    //   70: astore 7
    //   72: aload 7
    //   74: astore 8
    //   76: aload 6
    //   78: astore 9
    //   80: new 59	java/io/RandomAccessFile
    //   83: astore 10
    //   85: aload 7
    //   87: astore 8
    //   89: aload 6
    //   91: astore 9
    //   93: aload 10
    //   95: aload_1
    //   96: ldc 61
    //   98: invokespecial 64	java/io/RandomAccessFile:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   101: aload 10
    //   103: astore_1
    //   104: aload_1
    //   105: astore 8
    //   107: aload_1
    //   108: astore 9
    //   110: aload_1
    //   111: invokevirtual 68	java/io/RandomAccessFile:length	()J
    //   114: ldc2_w 69
    //   117: lcmp
    //   118: iflt +15 -> 133
    //   121: aload_1
    //   122: astore 8
    //   124: aload_1
    //   125: astore 9
    //   127: aload_1
    //   128: invokevirtual 73	java/io/RandomAccessFile:readInt	()I
    //   131: istore 5
    //   133: iload 5
    //   135: istore_3
    //   136: aload_1
    //   137: invokevirtual 76	java/io/RandomAccessFile:close	()V
    //   140: goto +50 -> 190
    //   143: astore 8
    //   145: aload 8
    //   147: invokevirtual 79	java/io/IOException:printStackTrace	()V
    //   150: iload_3
    //   151: istore 5
    //   153: goto -13 -> 140
    //   156: astore 9
    //   158: goto +45 -> 203
    //   161: astore_1
    //   162: aload 9
    //   164: astore 8
    //   166: aload_1
    //   167: invokevirtual 80	java/lang/Exception:printStackTrace	()V
    //   170: iload 4
    //   172: istore 5
    //   174: aload 9
    //   176: ifnull +14 -> 190
    //   179: aload 9
    //   181: invokevirtual 76	java/io/RandomAccessFile:close	()V
    //   184: iload_2
    //   185: istore 5
    //   187: goto -47 -> 140
    //   190: iload 5
    //   192: istore_3
    //   193: iload 5
    //   195: iconst_2
    //   196: if_icmple +5 -> 201
    //   199: iconst_0
    //   200: istore_3
    //   201: iload_3
    //   202: ireturn
    //   203: aload 8
    //   205: ifnull +18 -> 223
    //   208: aload 8
    //   210: invokevirtual 76	java/io/RandomAccessFile:close	()V
    //   213: goto +10 -> 223
    //   216: astore 8
    //   218: aload 8
    //   220: invokevirtual 79	java/io/IOException:printStackTrace	()V
    //   223: aload 9
    //   225: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	226	0	this	PersistentDataBlockManager
    //   2	135	1	localObject1	Object
    //   161	6	1	localException	Exception
    //   57	128	2	i	int
    //   59	143	3	j	int
    //   61	110	4	k	int
    //   64	133	5	m	int
    //   67	23	6	localObject2	Object
    //   70	16	7	localObject3	Object
    //   74	49	8	localObject4	Object
    //   143	3	8	localIOException1	java.io.IOException
    //   164	45	8	localObject5	Object
    //   216	3	8	localIOException2	java.io.IOException
    //   78	48	9	localObject6	Object
    //   156	68	9	localObject7	Object
    //   83	19	10	localRandomAccessFile	java.io.RandomAccessFile
    // Exception table:
    //   from	to	target	type
    //   136	140	143	java/io/IOException
    //   179	184	143	java/io/IOException
    //   80	85	156	finally
    //   93	101	156	finally
    //   110	121	156	finally
    //   127	133	156	finally
    //   166	170	156	finally
    //   80	85	161	java/lang/Exception
    //   93	101	161	java/lang/Exception
    //   110	121	161	java/lang/Exception
    //   127	133	161	java/lang/Exception
    //   208	213	216	java/io/IOException
  }
  
  public int getDataBlockSize()
  {
    try
    {
      int i = sService.getDataBlockSize();
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getFlashLockState()
  {
    try
    {
      int i = sService.getFlashLockState();
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SuppressLint({"Doclava125"})
  public long getMaximumDataBlockSize()
  {
    try
    {
      long l = sService.getMaximumDataBlockSize();
      return l;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean getOemUnlockEnabled()
  {
    try
    {
      boolean bool = sService.getOemUnlockEnabled();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SuppressLint({"Doclava125"})
  public byte[] read()
  {
    try
    {
      byte[] arrayOfByte = sService.read();
      return arrayOfByte;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setOemUnlockEnabled(boolean paramBoolean)
  {
    try
    {
      sService.setOemUnlockEnabled(paramBoolean);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void wipe()
  {
    try
    {
      sService.wipe();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SuppressLint({"Doclava125"})
  public int write(byte[] paramArrayOfByte)
  {
    try
    {
      if (getAccFlag() >= 1) {
        return -1;
      }
      int i = sService.write(paramArrayOfByte);
      return i;
    }
    catch (RemoteException paramArrayOfByte)
    {
      throw paramArrayOfByte.rethrowFromSystemServer();
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface FlashLockState {}
}
