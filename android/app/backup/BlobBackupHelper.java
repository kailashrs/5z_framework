package android.app.backup;

import android.os.ParcelFileDescriptor;
import android.util.ArrayMap;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public abstract class BlobBackupHelper
  implements BackupHelper
{
  private static final boolean DEBUG = false;
  private static final String TAG = "BlobBackupHelper";
  private final int mCurrentBlobVersion;
  private final String[] mKeys;
  
  public BlobBackupHelper(int paramInt, String... paramVarArgs)
  {
    mCurrentBlobVersion = paramInt;
    mKeys = paramVarArgs;
  }
  
  private long checksum(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte != null) {
      try
      {
        CRC32 localCRC32 = new java/util/zip/CRC32;
        localCRC32.<init>();
        ByteArrayInputStream localByteArrayInputStream = new java/io/ByteArrayInputStream;
        localByteArrayInputStream.<init>(paramArrayOfByte);
        paramArrayOfByte = new byte['က'];
        for (;;)
        {
          int i = localByteArrayInputStream.read(paramArrayOfByte);
          if (i < 0) {
            break;
          }
          localCRC32.update(paramArrayOfByte, 0, i);
        }
        long l = localCRC32.getValue();
        return l;
      }
      catch (Exception paramArrayOfByte) {}
    }
    return -1L;
  }
  
  private byte[] deflate(byte[] paramArrayOfByte)
  {
    Object localObject1 = null;
    Object localObject2 = localObject1;
    if (paramArrayOfByte != null) {
      try
      {
        localObject2 = new java/io/ByteArrayOutputStream;
        ((ByteArrayOutputStream)localObject2).<init>();
        Object localObject3 = new java/io/DataOutputStream;
        ((DataOutputStream)localObject3).<init>((OutputStream)localObject2);
        ((DataOutputStream)localObject3).writeInt(mCurrentBlobVersion);
        localObject3 = new java/util/zip/DeflaterOutputStream;
        ((DeflaterOutputStream)localObject3).<init>((OutputStream)localObject2);
        ((DeflaterOutputStream)localObject3).write(paramArrayOfByte);
        ((DeflaterOutputStream)localObject3).close();
        localObject2 = ((ByteArrayOutputStream)localObject2).toByteArray();
      }
      catch (IOException paramArrayOfByte)
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Unable to process payload: ");
        ((StringBuilder)localObject2).append(paramArrayOfByte.getMessage());
        Log.w("BlobBackupHelper", ((StringBuilder)localObject2).toString());
        localObject2 = localObject1;
      }
    }
    return localObject2;
  }
  
  private byte[] inflate(byte[] paramArrayOfByte)
  {
    Object localObject1 = null;
    Object localObject2 = localObject1;
    Object localObject3;
    if (paramArrayOfByte != null) {
      try
      {
        localObject2 = new java/io/ByteArrayInputStream;
        ((ByteArrayInputStream)localObject2).<init>(paramArrayOfByte);
        paramArrayOfByte = new java/io/DataInputStream;
        paramArrayOfByte.<init>((InputStream)localObject2);
        int i = paramArrayOfByte.readInt();
        if (i > mCurrentBlobVersion)
        {
          paramArrayOfByte = new java/lang/StringBuilder;
          paramArrayOfByte.<init>();
          paramArrayOfByte.append("Saved payload from unrecognized version ");
          paramArrayOfByte.append(i);
          Log.w("BlobBackupHelper", paramArrayOfByte.toString());
          return null;
        }
        paramArrayOfByte = new java/util/zip/InflaterInputStream;
        paramArrayOfByte.<init>((InputStream)localObject2);
        localObject2 = new java/io/ByteArrayOutputStream;
        ((ByteArrayOutputStream)localObject2).<init>();
        byte[] arrayOfByte = new byte['က'];
        for (;;)
        {
          i = paramArrayOfByte.read(arrayOfByte);
          if (i <= 0) {
            break;
          }
          ((ByteArrayOutputStream)localObject2).write(arrayOfByte, 0, i);
        }
        paramArrayOfByte.close();
        ((ByteArrayOutputStream)localObject2).flush();
        localObject2 = ((ByteArrayOutputStream)localObject2).toByteArray();
      }
      catch (IOException localIOException)
      {
        paramArrayOfByte = new StringBuilder();
        paramArrayOfByte.append("Unable to process restored payload: ");
        paramArrayOfByte.append(localIOException.getMessage());
        Log.w("BlobBackupHelper", paramArrayOfByte.toString());
        localObject3 = localObject1;
      }
    }
    return localObject3;
  }
  
  private ArrayMap<String, Long> readOldState(ParcelFileDescriptor paramParcelFileDescriptor)
  {
    ArrayMap localArrayMap = new ArrayMap();
    paramParcelFileDescriptor = new DataInputStream(new FileInputStream(paramParcelFileDescriptor.getFileDescriptor()));
    try
    {
      int i = paramParcelFileDescriptor.readInt();
      if (i <= mCurrentBlobVersion)
      {
        int j = paramParcelFileDescriptor.readInt();
        for (i = 0; i < j; i++) {
          localArrayMap.put(paramParcelFileDescriptor.readUTF(), Long.valueOf(paramParcelFileDescriptor.readLong()));
        }
      }
      else
      {
        paramParcelFileDescriptor = new java/lang/StringBuilder;
        paramParcelFileDescriptor.<init>();
        paramParcelFileDescriptor.append("Prior state from unrecognized version ");
        paramParcelFileDescriptor.append(i);
        Log.w("BlobBackupHelper", paramParcelFileDescriptor.toString());
      }
    }
    catch (Exception paramParcelFileDescriptor)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Error examining prior backup state ");
      localStringBuilder.append(paramParcelFileDescriptor.getMessage());
      Log.e("BlobBackupHelper", localStringBuilder.toString());
      localArrayMap.clear();
    }
    catch (EOFException paramParcelFileDescriptor)
    {
      localArrayMap.clear();
    }
    return localArrayMap;
  }
  
  private void writeBackupState(ArrayMap<String, Long> paramArrayMap, ParcelFileDescriptor paramParcelFileDescriptor)
  {
    try
    {
      Object localObject = new java/io/FileOutputStream;
      ((FileOutputStream)localObject).<init>(paramParcelFileDescriptor.getFileDescriptor());
      paramParcelFileDescriptor = new java/io/DataOutputStream;
      paramParcelFileDescriptor.<init>((OutputStream)localObject);
      paramParcelFileDescriptor.writeInt(mCurrentBlobVersion);
      int i = 0;
      int j;
      if (paramArrayMap != null) {
        j = paramArrayMap.size();
      } else {
        j = 0;
      }
      paramParcelFileDescriptor.writeInt(j);
      while (i < j)
      {
        localObject = (String)paramArrayMap.keyAt(i);
        long l = ((Long)paramArrayMap.valueAt(i)).longValue();
        paramParcelFileDescriptor.writeUTF((String)localObject);
        paramParcelFileDescriptor.writeLong(l);
        i++;
      }
    }
    catch (IOException paramArrayMap)
    {
      Log.e("BlobBackupHelper", "Unable to write updated state", paramArrayMap);
    }
  }
  
  protected abstract void applyRestoredPayload(String paramString, byte[] paramArrayOfByte);
  
  protected abstract byte[] getBackupPayload(String paramString);
  
  /* Error */
  public void performBackup(ParcelFileDescriptor paramParcelFileDescriptor1, BackupDataOutput paramBackupDataOutput, ParcelFileDescriptor paramParcelFileDescriptor2)
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial 220	android/app/backup/BlobBackupHelper:readOldState	(Landroid/os/ParcelFileDescriptor;)Landroid/util/ArrayMap;
    //   5: astore 4
    //   7: new 137	android/util/ArrayMap
    //   10: dup
    //   11: invokespecial 138	android/util/ArrayMap:<init>	()V
    //   14: astore_1
    //   15: aload_0
    //   16: getfield 26	android/app/backup/BlobBackupHelper:mKeys	[Ljava/lang/String;
    //   19: astore 5
    //   21: aload 5
    //   23: arraylength
    //   24: istore 6
    //   26: iconst_0
    //   27: istore 7
    //   29: iload 7
    //   31: iload 6
    //   33: if_icmpge +112 -> 145
    //   36: aload 5
    //   38: iload 7
    //   40: aaload
    //   41: astore 8
    //   43: aload_0
    //   44: aload_0
    //   45: aload 8
    //   47: invokevirtual 222	android/app/backup/BlobBackupHelper:getBackupPayload	(Ljava/lang/String;)[B
    //   50: invokespecial 224	android/app/backup/BlobBackupHelper:deflate	([B)[B
    //   53: astore 9
    //   55: aload_0
    //   56: aload 9
    //   58: invokespecial 226	android/app/backup/BlobBackupHelper:checksum	([B)J
    //   61: lstore 10
    //   63: aload_1
    //   64: aload 8
    //   66: lload 10
    //   68: invokestatic 161	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   71: invokevirtual 165	android/util/ArrayMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   74: pop
    //   75: aload 4
    //   77: aload 8
    //   79: invokevirtual 230	android/util/ArrayMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   82: checkcast 157	java/lang/Long
    //   85: astore 12
    //   87: aload 12
    //   89: ifnull +14 -> 103
    //   92: lload 10
    //   94: aload 12
    //   96: invokevirtual 198	java/lang/Long:longValue	()J
    //   99: lcmp
    //   100: ifeq +39 -> 139
    //   103: aload 9
    //   105: ifnull +26 -> 131
    //   108: aload_2
    //   109: aload 8
    //   111: aload 9
    //   113: arraylength
    //   114: invokevirtual 236	android/app/backup/BackupDataOutput:writeEntityHeader	(Ljava/lang/String;I)I
    //   117: pop
    //   118: aload_2
    //   119: aload 9
    //   121: aload 9
    //   123: arraylength
    //   124: invokevirtual 240	android/app/backup/BackupDataOutput:writeEntityData	([BI)I
    //   127: pop
    //   128: goto +11 -> 139
    //   131: aload_2
    //   132: aload 8
    //   134: iconst_m1
    //   135: invokevirtual 236	android/app/backup/BackupDataOutput:writeEntityHeader	(Ljava/lang/String;I)I
    //   138: pop
    //   139: iinc 7 1
    //   142: goto -113 -> 29
    //   145: aload_0
    //   146: aload_1
    //   147: aload_3
    //   148: invokespecial 242	android/app/backup/BlobBackupHelper:writeBackupState	(Landroid/util/ArrayMap;Landroid/os/ParcelFileDescriptor;)V
    //   151: goto +51 -> 202
    //   154: astore_2
    //   155: goto +48 -> 203
    //   158: astore 8
    //   160: new 84	java/lang/StringBuilder
    //   163: astore_2
    //   164: aload_2
    //   165: invokespecial 85	java/lang/StringBuilder:<init>	()V
    //   168: aload_2
    //   169: ldc -12
    //   171: invokevirtual 91	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   174: pop
    //   175: aload_2
    //   176: aload 8
    //   178: invokevirtual 170	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   181: invokevirtual 91	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   184: pop
    //   185: ldc 13
    //   187: aload_2
    //   188: invokevirtual 98	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   191: invokestatic 104	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   194: pop
    //   195: aload_1
    //   196: invokevirtual 176	android/util/ArrayMap:clear	()V
    //   199: goto -54 -> 145
    //   202: return
    //   203: aload_0
    //   204: aload_1
    //   205: aload_3
    //   206: invokespecial 242	android/app/backup/BlobBackupHelper:writeBackupState	(Landroid/util/ArrayMap;Landroid/os/ParcelFileDescriptor;)V
    //   209: aload_2
    //   210: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	211	0	this	BlobBackupHelper
    //   0	211	1	paramParcelFileDescriptor1	ParcelFileDescriptor
    //   0	211	2	paramBackupDataOutput	BackupDataOutput
    //   0	211	3	paramParcelFileDescriptor2	ParcelFileDescriptor
    //   5	71	4	localArrayMap	ArrayMap
    //   19	18	5	arrayOfString	String[]
    //   24	10	6	i	int
    //   27	113	7	j	int
    //   41	92	8	str	String
    //   158	19	8	localException	Exception
    //   53	69	9	arrayOfByte	byte[]
    //   61	32	10	l	long
    //   85	10	12	localLong	Long
    // Exception table:
    //   from	to	target	type
    //   15	26	154	finally
    //   43	87	154	finally
    //   92	103	154	finally
    //   108	128	154	finally
    //   131	139	154	finally
    //   160	199	154	finally
    //   15	26	158	java/lang/Exception
    //   43	87	158	java/lang/Exception
    //   92	103	158	java/lang/Exception
    //   108	128	158	java/lang/Exception
    //   131	139	158	java/lang/Exception
  }
  
  public void restoreEntity(BackupDataInputStream paramBackupDataInputStream)
  {
    String str = paramBackupDataInputStream.getKey();
    int i = 0;
    try
    {
      while ((i < mKeys.length) && (!str.equals(mKeys[i]))) {
        i++;
      }
      if (i >= mKeys.length)
      {
        paramBackupDataInputStream = new java/lang/StringBuilder;
        paramBackupDataInputStream.<init>();
        paramBackupDataInputStream.append("Unrecognized key ");
        paramBackupDataInputStream.append(str);
        paramBackupDataInputStream.append(", ignoring");
        Log.e("BlobBackupHelper", paramBackupDataInputStream.toString());
        return;
      }
      localObject = new byte[paramBackupDataInputStream.size()];
      paramBackupDataInputStream.read((byte[])localObject);
      applyRestoredPayload(str, inflate((byte[])localObject));
    }
    catch (Exception paramBackupDataInputStream)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Exception restoring entity ");
      ((StringBuilder)localObject).append(str);
      ((StringBuilder)localObject).append(" : ");
      ((StringBuilder)localObject).append(paramBackupDataInputStream.getMessage());
      Log.e("BlobBackupHelper", ((StringBuilder)localObject).toString());
    }
  }
  
  public void writeNewStateDescription(ParcelFileDescriptor paramParcelFileDescriptor)
  {
    writeBackupState(null, paramParcelFileDescriptor);
  }
}
