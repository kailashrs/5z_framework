package android.os;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import com.android.internal.os.IDropBoxManagerService;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class DropBoxManager
{
  public static final String ACTION_DROPBOX_ENTRY_ADDED = "android.intent.action.DROPBOX_ENTRY_ADDED";
  public static final String EXTRA_TAG = "tag";
  public static final String EXTRA_TIME = "time";
  private static final int HAS_BYTE_ARRAY = 8;
  public static final int IS_EMPTY = 1;
  public static final int IS_GZIPPED = 4;
  public static final int IS_TEXT = 2;
  private static final String TAG = "DropBoxManager";
  private final Context mContext;
  private final IDropBoxManagerService mService;
  
  protected DropBoxManager()
  {
    mContext = null;
    mService = null;
  }
  
  public DropBoxManager(Context paramContext, IDropBoxManagerService paramIDropBoxManagerService)
  {
    mContext = paramContext;
    mService = paramIDropBoxManagerService;
  }
  
  public void addData(String paramString, byte[] paramArrayOfByte, int paramInt)
  {
    if (paramArrayOfByte != null) {
      try
      {
        IDropBoxManagerService localIDropBoxManagerService = mService;
        Entry localEntry = new android/os/DropBoxManager$Entry;
        localEntry.<init>(paramString, 0L, paramArrayOfByte, paramInt);
        localIDropBoxManagerService.add(localEntry);
        return;
      }
      catch (RemoteException paramString)
      {
        if (((paramString instanceof TransactionTooLargeException)) && (mContext.getApplicationInfo().targetSdkVersion < 24))
        {
          Log.e("DropBoxManager", "App sent too much data, so it was ignored", paramString);
          return;
        }
        throw paramString.rethrowFromSystemServer();
      }
    }
    throw new NullPointerException("data == null");
  }
  
  /* Error */
  public void addFile(String paramString, File paramFile, int paramInt)
    throws IOException
  {
    // Byte code:
    //   0: aload_2
    //   1: ifnull +46 -> 47
    //   4: new 6	android/os/DropBoxManager$Entry
    //   7: dup
    //   8: aload_1
    //   9: lconst_0
    //   10: aload_2
    //   11: iload_3
    //   12: invokespecial 97	android/os/DropBoxManager$Entry:<init>	(Ljava/lang/String;JLjava/io/File;I)V
    //   15: astore_1
    //   16: aload_0
    //   17: getfield 43	android/os/DropBoxManager:mService	Lcom/android/internal/os/IDropBoxManagerService;
    //   20: aload_1
    //   21: invokeinterface 58 2 0
    //   26: aload_1
    //   27: invokevirtual 100	android/os/DropBoxManager$Entry:close	()V
    //   30: return
    //   31: astore_2
    //   32: goto +9 -> 41
    //   35: astore_2
    //   36: aload_2
    //   37: invokevirtual 83	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   40: athrow
    //   41: aload_1
    //   42: invokevirtual 100	android/os/DropBoxManager$Entry:close	()V
    //   45: aload_2
    //   46: athrow
    //   47: new 85	java/lang/NullPointerException
    //   50: dup
    //   51: ldc 102
    //   53: invokespecial 90	java/lang/NullPointerException:<init>	(Ljava/lang/String;)V
    //   56: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	57	0	this	DropBoxManager
    //   0	57	1	paramString	String
    //   0	57	2	paramFile	File
    //   0	57	3	paramInt	int
    // Exception table:
    //   from	to	target	type
    //   16	26	31	finally
    //   36	41	31	finally
    //   16	26	35	android/os/RemoteException
  }
  
  public void addText(String paramString1, String paramString2)
  {
    try
    {
      IDropBoxManagerService localIDropBoxManagerService = mService;
      Entry localEntry = new android/os/DropBoxManager$Entry;
      localEntry.<init>(paramString1, 0L, paramString2);
      localIDropBoxManagerService.add(localEntry);
      return;
    }
    catch (RemoteException paramString1)
    {
      if (((paramString1 instanceof TransactionTooLargeException)) && (mContext.getApplicationInfo().targetSdkVersion < 24))
      {
        Log.e("DropBoxManager", "App sent too much data, so it was ignored", paramString1);
        return;
      }
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public Entry getNextEntry(String paramString, long paramLong)
  {
    try
    {
      paramString = mService.getNextEntry(paramString, paramLong);
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean isTagEnabled(String paramString)
  {
    try
    {
      boolean bool = mService.isTagEnabled(paramString);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public static class Entry
    implements Parcelable, Closeable
  {
    public static final Parcelable.Creator<Entry> CREATOR = new Parcelable.Creator()
    {
      public DropBoxManager.Entry createFromParcel(Parcel paramAnonymousParcel)
      {
        String str = paramAnonymousParcel.readString();
        long l = paramAnonymousParcel.readLong();
        int i = paramAnonymousParcel.readInt();
        if ((i & 0x8) != 0) {
          return new DropBoxManager.Entry(str, l, paramAnonymousParcel.createByteArray(), i & 0xFFFFFFF7);
        }
        return new DropBoxManager.Entry(str, l, (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramAnonymousParcel), i);
      }
      
      public DropBoxManager.Entry[] newArray(int paramAnonymousInt)
      {
        return new DropBoxManager.Entry[paramAnonymousInt];
      }
    };
    private final byte[] mData;
    private final ParcelFileDescriptor mFileDescriptor;
    private final int mFlags;
    private final String mTag;
    private final long mTimeMillis;
    
    public Entry(String paramString, long paramLong)
    {
      if (paramString != null)
      {
        mTag = paramString;
        mTimeMillis = paramLong;
        mData = null;
        mFileDescriptor = null;
        mFlags = 1;
        return;
      }
      throw new NullPointerException("tag == null");
    }
    
    public Entry(String paramString, long paramLong, ParcelFileDescriptor paramParcelFileDescriptor, int paramInt)
    {
      if (paramString != null)
      {
        int i = 0;
        int j;
        if ((paramInt & 0x1) != 0) {
          j = 1;
        } else {
          j = 0;
        }
        if (paramParcelFileDescriptor == null) {
          i = 1;
        }
        if (j == i)
        {
          mTag = paramString;
          mTimeMillis = paramLong;
          mData = null;
          mFileDescriptor = paramParcelFileDescriptor;
          mFlags = paramInt;
          return;
        }
        paramString = new StringBuilder();
        paramString.append("Bad flags: ");
        paramString.append(paramInt);
        throw new IllegalArgumentException(paramString.toString());
      }
      throw new NullPointerException("tag == null");
    }
    
    public Entry(String paramString, long paramLong, File paramFile, int paramInt)
      throws IOException
    {
      if (paramString != null)
      {
        if ((paramInt & 0x1) == 0)
        {
          mTag = paramString;
          mTimeMillis = paramLong;
          mData = null;
          mFileDescriptor = ParcelFileDescriptor.open(paramFile, 268435456);
          mFlags = paramInt;
          return;
        }
        paramString = new StringBuilder();
        paramString.append("Bad flags: ");
        paramString.append(paramInt);
        throw new IllegalArgumentException(paramString.toString());
      }
      throw new NullPointerException("tag == null");
    }
    
    public Entry(String paramString1, long paramLong, String paramString2)
    {
      if (paramString1 != null)
      {
        if (paramString2 != null)
        {
          mTag = paramString1;
          mTimeMillis = paramLong;
          mData = paramString2.getBytes();
          mFileDescriptor = null;
          mFlags = 2;
          return;
        }
        throw new NullPointerException("text == null");
      }
      throw new NullPointerException("tag == null");
    }
    
    public Entry(String paramString, long paramLong, byte[] paramArrayOfByte, int paramInt)
    {
      if (paramString != null)
      {
        int i = 0;
        int j;
        if ((paramInt & 0x1) != 0) {
          j = 1;
        } else {
          j = 0;
        }
        if (paramArrayOfByte == null) {
          i = 1;
        }
        if (j == i)
        {
          mTag = paramString;
          mTimeMillis = paramLong;
          mData = paramArrayOfByte;
          mFileDescriptor = null;
          mFlags = paramInt;
          return;
        }
        paramString = new StringBuilder();
        paramString.append("Bad flags: ");
        paramString.append(paramInt);
        throw new IllegalArgumentException(paramString.toString());
      }
      throw new NullPointerException("tag == null");
    }
    
    public void close()
    {
      try
      {
        if (mFileDescriptor != null) {
          mFileDescriptor.close();
        }
      }
      catch (IOException localIOException) {}
    }
    
    public int describeContents()
    {
      int i;
      if (mFileDescriptor != null) {
        i = 1;
      } else {
        i = 0;
      }
      return i;
    }
    
    public int getFlags()
    {
      return mFlags & 0xFFFFFFFB;
    }
    
    public InputStream getInputStream()
      throws IOException
    {
      if (mData != null) {}
      for (Object localObject = new ByteArrayInputStream(mData);; localObject = new ParcelFileDescriptor.AutoCloseInputStream(mFileDescriptor))
      {
        break;
        if (mFileDescriptor == null) {
          break label67;
        }
      }
      if ((mFlags & 0x4) != 0) {
        localObject = new GZIPInputStream((InputStream)localObject);
      }
      return localObject;
      label67:
      return null;
    }
    
    public String getTag()
    {
      return mTag;
    }
    
    /* Error */
    public String getText(int paramInt)
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 46	android/os/DropBoxManager$Entry:mFlags	I
      //   4: iconst_2
      //   5: iand
      //   6: ifne +5 -> 11
      //   9: aconst_null
      //   10: areturn
      //   11: aload_0
      //   12: getfield 42	android/os/DropBoxManager$Entry:mData	[B
      //   15: ifnull +25 -> 40
      //   18: new 87	java/lang/String
      //   21: dup
      //   22: aload_0
      //   23: getfield 42	android/os/DropBoxManager$Entry:mData	[B
      //   26: iconst_0
      //   27: iload_1
      //   28: aload_0
      //   29: getfield 42	android/os/DropBoxManager$Entry:mData	[B
      //   32: arraylength
      //   33: invokestatic 126	java/lang/Math:min	(II)I
      //   36: invokespecial 129	java/lang/String:<init>	([BII)V
      //   39: areturn
      //   40: aconst_null
      //   41: astore_2
      //   42: aconst_null
      //   43: astore_3
      //   44: aload_0
      //   45: invokevirtual 131	android/os/DropBoxManager$Entry:getInputStream	()Ljava/io/InputStream;
      //   48: astore 4
      //   50: aload 4
      //   52: ifnonnull +20 -> 72
      //   55: aload 4
      //   57: ifnull +13 -> 70
      //   60: aload 4
      //   62: invokevirtual 134	java/io/InputStream:close	()V
      //   65: goto +5 -> 70
      //   68: astore 4
      //   70: aconst_null
      //   71: areturn
      //   72: aload 4
      //   74: astore_3
      //   75: aload 4
      //   77: astore_2
      //   78: iload_1
      //   79: newarray byte
      //   81: astore 5
      //   83: iconst_0
      //   84: istore 6
      //   86: iconst_0
      //   87: istore 7
      //   89: iload 6
      //   91: istore 8
      //   93: iload 7
      //   95: iflt +48 -> 143
      //   98: iload 6
      //   100: iload 7
      //   102: iadd
      //   103: istore 7
      //   105: iload 7
      //   107: istore 6
      //   109: iload 6
      //   111: istore 8
      //   113: iload 7
      //   115: iload_1
      //   116: if_icmpge +27 -> 143
      //   119: aload 4
      //   121: astore_3
      //   122: aload 4
      //   124: astore_2
      //   125: aload 4
      //   127: aload 5
      //   129: iload 6
      //   131: iload_1
      //   132: iload 6
      //   134: isub
      //   135: invokevirtual 138	java/io/InputStream:read	([BII)I
      //   138: istore 7
      //   140: goto -51 -> 89
      //   143: aload 4
      //   145: astore_3
      //   146: aload 4
      //   148: astore_2
      //   149: new 87	java/lang/String
      //   152: dup
      //   153: aload 5
      //   155: iconst_0
      //   156: iload 8
      //   158: invokespecial 129	java/lang/String:<init>	([BII)V
      //   161: astore 5
      //   163: aload 4
      //   165: ifnull +13 -> 178
      //   168: aload 4
      //   170: invokevirtual 134	java/io/InputStream:close	()V
      //   173: goto +5 -> 178
      //   176: astore 4
      //   178: aload 5
      //   180: areturn
      //   181: astore 4
      //   183: aload_3
      //   184: ifnull +11 -> 195
      //   187: aload_3
      //   188: invokevirtual 134	java/io/InputStream:close	()V
      //   191: goto +4 -> 195
      //   194: astore_3
      //   195: aload 4
      //   197: athrow
      //   198: astore 4
      //   200: aload_2
      //   201: ifnull +12 -> 213
      //   204: aload_2
      //   205: invokevirtual 134	java/io/InputStream:close	()V
      //   208: goto +5 -> 213
      //   211: astore 4
      //   213: aconst_null
      //   214: areturn
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	215	0	this	Entry
      //   0	215	1	paramInt	int
      //   41	164	2	localObject1	Object
      //   43	145	3	localObject2	Object
      //   194	1	3	localIOException1	IOException
      //   48	13	4	localInputStream	InputStream
      //   68	101	4	localIOException2	IOException
      //   176	1	4	localIOException3	IOException
      //   181	15	4	localObject3	Object
      //   198	1	4	localIOException4	IOException
      //   211	1	4	localIOException5	IOException
      //   81	98	5	localObject4	Object
      //   84	51	6	i	int
      //   87	52	7	j	int
      //   91	66	8	k	int
      // Exception table:
      //   from	to	target	type
      //   60	65	68	java/io/IOException
      //   168	173	176	java/io/IOException
      //   44	50	181	finally
      //   78	83	181	finally
      //   125	140	181	finally
      //   149	163	181	finally
      //   187	191	194	java/io/IOException
      //   44	50	198	java/io/IOException
      //   78	83	198	java/io/IOException
      //   125	140	198	java/io/IOException
      //   149	163	198	java/io/IOException
      //   204	208	211	java/io/IOException
    }
    
    public long getTimeMillis()
    {
      return mTimeMillis;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeString(mTag);
      paramParcel.writeLong(mTimeMillis);
      if (mFileDescriptor != null)
      {
        paramParcel.writeInt(mFlags & 0xFFFFFFF7);
        mFileDescriptor.writeToParcel(paramParcel, paramInt);
      }
      else
      {
        paramParcel.writeInt(mFlags | 0x8);
        paramParcel.writeByteArray(mData);
      }
    }
  }
}
