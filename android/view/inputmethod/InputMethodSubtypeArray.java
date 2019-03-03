package android.view.inputmethod;

import android.os.Parcel;
import android.util.Slog;
import java.util.List;

public class InputMethodSubtypeArray
{
  private static final String TAG = "InputMethodSubtypeArray";
  private volatile byte[] mCompressedData;
  private final int mCount;
  private volatile int mDecompressedSize;
  private volatile InputMethodSubtype[] mInstance;
  private final Object mLockObject = new Object();
  
  public InputMethodSubtypeArray(Parcel paramParcel)
  {
    mCount = paramParcel.readInt();
    if (mCount > 0)
    {
      mDecompressedSize = paramParcel.readInt();
      mCompressedData = paramParcel.createByteArray();
    }
  }
  
  public InputMethodSubtypeArray(List<InputMethodSubtype> paramList)
  {
    if (paramList == null)
    {
      mCount = 0;
      return;
    }
    mCount = paramList.size();
    mInstance = ((InputMethodSubtype[])paramList.toArray(new InputMethodSubtype[mCount]));
  }
  
  /* Error */
  private static byte[] compress(byte[] paramArrayOfByte)
  {
    // Byte code:
    //   0: new 77	java/io/ByteArrayOutputStream
    //   3: astore_1
    //   4: aload_1
    //   5: invokespecial 78	java/io/ByteArrayOutputStream:<init>	()V
    //   8: new 80	java/util/zip/GZIPOutputStream
    //   11: astore_2
    //   12: aload_2
    //   13: aload_1
    //   14: invokespecial 83	java/util/zip/GZIPOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   17: aload_2
    //   18: aload_0
    //   19: invokevirtual 87	java/util/zip/GZIPOutputStream:write	([B)V
    //   22: aload_2
    //   23: invokevirtual 90	java/util/zip/GZIPOutputStream:finish	()V
    //   26: aload_1
    //   27: invokevirtual 93	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   30: astore_0
    //   31: aconst_null
    //   32: aload_2
    //   33: invokestatic 95	android/view/inputmethod/InputMethodSubtypeArray:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   36: aconst_null
    //   37: aload_1
    //   38: invokestatic 95	android/view/inputmethod/InputMethodSubtypeArray:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   41: aload_0
    //   42: areturn
    //   43: astore_3
    //   44: aconst_null
    //   45: astore_0
    //   46: goto +7 -> 53
    //   49: astore_0
    //   50: aload_0
    //   51: athrow
    //   52: astore_3
    //   53: aload_0
    //   54: aload_2
    //   55: invokestatic 95	android/view/inputmethod/InputMethodSubtypeArray:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   58: aload_3
    //   59: athrow
    //   60: astore_3
    //   61: aconst_null
    //   62: astore_0
    //   63: goto +7 -> 70
    //   66: astore_0
    //   67: aload_0
    //   68: athrow
    //   69: astore_3
    //   70: aload_0
    //   71: aload_1
    //   72: invokestatic 95	android/view/inputmethod/InputMethodSubtypeArray:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   75: aload_3
    //   76: athrow
    //   77: astore_0
    //   78: ldc 8
    //   80: ldc 97
    //   82: aload_0
    //   83: invokestatic 103	android/util/Slog:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   86: pop
    //   87: aconst_null
    //   88: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	89	0	paramArrayOfByte	byte[]
    //   3	69	1	localByteArrayOutputStream	java.io.ByteArrayOutputStream
    //   11	44	2	localGZIPOutputStream	java.util.zip.GZIPOutputStream
    //   43	1	3	localObject1	Object
    //   52	7	3	localObject2	Object
    //   60	1	3	localObject3	Object
    //   69	7	3	localObject4	Object
    // Exception table:
    //   from	to	target	type
    //   17	31	43	finally
    //   17	31	49	java/lang/Throwable
    //   50	52	52	finally
    //   8	17	60	finally
    //   31	36	60	finally
    //   53	60	60	finally
    //   8	17	66	java/lang/Throwable
    //   31	36	66	java/lang/Throwable
    //   53	60	66	java/lang/Throwable
    //   67	69	69	finally
    //   0	8	77	java/lang/Exception
    //   36	41	77	java/lang/Exception
    //   70	77	77	java/lang/Exception
  }
  
  /* Error */
  private static byte[] decompress(byte[] paramArrayOfByte, int paramInt)
  {
    // Byte code:
    //   0: new 107	java/io/ByteArrayInputStream
    //   3: astore_2
    //   4: aload_2
    //   5: aload_0
    //   6: invokespecial 109	java/io/ByteArrayInputStream:<init>	([B)V
    //   9: new 111	java/util/zip/GZIPInputStream
    //   12: astore_3
    //   13: aload_3
    //   14: aload_2
    //   15: invokespecial 114	java/util/zip/GZIPInputStream:<init>	(Ljava/io/InputStream;)V
    //   18: iload_1
    //   19: newarray byte
    //   21: astore_0
    //   22: iconst_0
    //   23: istore 4
    //   25: iload 4
    //   27: aload_0
    //   28: arraylength
    //   29: if_icmpge +35 -> 64
    //   32: aload_3
    //   33: aload_0
    //   34: iload 4
    //   36: aload_0
    //   37: arraylength
    //   38: iload 4
    //   40: isub
    //   41: invokevirtual 118	java/util/zip/GZIPInputStream:read	([BII)I
    //   44: istore 5
    //   46: iload 5
    //   48: ifge +6 -> 54
    //   51: goto +13 -> 64
    //   54: iload 4
    //   56: iload 5
    //   58: iadd
    //   59: istore 4
    //   61: goto -36 -> 25
    //   64: iload_1
    //   65: iload 4
    //   67: if_icmpeq +15 -> 82
    //   70: aconst_null
    //   71: aload_3
    //   72: invokestatic 95	android/view/inputmethod/InputMethodSubtypeArray:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   75: aconst_null
    //   76: aload_2
    //   77: invokestatic 95	android/view/inputmethod/InputMethodSubtypeArray:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   80: aconst_null
    //   81: areturn
    //   82: aconst_null
    //   83: aload_3
    //   84: invokestatic 95	android/view/inputmethod/InputMethodSubtypeArray:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   87: aconst_null
    //   88: aload_2
    //   89: invokestatic 95	android/view/inputmethod/InputMethodSubtypeArray:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   92: aload_0
    //   93: areturn
    //   94: astore_0
    //   95: aconst_null
    //   96: astore 6
    //   98: goto +14 -> 112
    //   101: astore_0
    //   102: aload_0
    //   103: athrow
    //   104: astore 7
    //   106: aload_0
    //   107: astore 6
    //   109: aload 7
    //   111: astore_0
    //   112: aload 6
    //   114: aload_3
    //   115: invokestatic 95	android/view/inputmethod/InputMethodSubtypeArray:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   118: aload_0
    //   119: athrow
    //   120: astore_0
    //   121: aconst_null
    //   122: astore 6
    //   124: goto +9 -> 133
    //   127: astore 6
    //   129: aload 6
    //   131: athrow
    //   132: astore_0
    //   133: aload 6
    //   135: aload_2
    //   136: invokestatic 95	android/view/inputmethod/InputMethodSubtypeArray:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   139: aload_0
    //   140: athrow
    //   141: astore_0
    //   142: ldc 8
    //   144: ldc 120
    //   146: aload_0
    //   147: invokestatic 103	android/util/Slog:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   150: pop
    //   151: aconst_null
    //   152: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	153	0	paramArrayOfByte	byte[]
    //   0	153	1	paramInt	int
    //   3	133	2	localByteArrayInputStream	java.io.ByteArrayInputStream
    //   12	103	3	localGZIPInputStream	java.util.zip.GZIPInputStream
    //   23	45	4	i	int
    //   44	15	5	j	int
    //   96	27	6	arrayOfByte	byte[]
    //   127	7	6	localThrowable	Throwable
    //   104	6	7	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   18	22	94	finally
    //   25	46	94	finally
    //   18	22	101	java/lang/Throwable
    //   25	46	101	java/lang/Throwable
    //   102	104	104	finally
    //   9	18	120	finally
    //   70	75	120	finally
    //   82	87	120	finally
    //   112	120	120	finally
    //   9	18	127	java/lang/Throwable
    //   70	75	127	java/lang/Throwable
    //   82	87	127	java/lang/Throwable
    //   112	120	127	java/lang/Throwable
    //   129	132	132	finally
    //   0	9	141	java/lang/Exception
    //   75	80	141	java/lang/Exception
    //   87	92	141	java/lang/Exception
    //   133	141	141	java/lang/Exception
  }
  
  private static byte[] marshall(InputMethodSubtype[] paramArrayOfInputMethodSubtype)
  {
    Object localObject = null;
    try
    {
      Parcel localParcel = Parcel.obtain();
      localObject = localParcel;
      localParcel.writeTypedArray(paramArrayOfInputMethodSubtype, 0);
      localObject = localParcel;
      paramArrayOfInputMethodSubtype = localParcel.marshall();
      return paramArrayOfInputMethodSubtype;
    }
    finally
    {
      if (localObject != null) {
        localObject.recycle();
      }
    }
  }
  
  private static InputMethodSubtype[] unmarshall(byte[] paramArrayOfByte)
  {
    Object localObject = null;
    try
    {
      Parcel localParcel = Parcel.obtain();
      localObject = localParcel;
      localParcel.unmarshall(paramArrayOfByte, 0, paramArrayOfByte.length);
      localObject = localParcel;
      localParcel.setDataPosition(0);
      localObject = localParcel;
      paramArrayOfByte = (InputMethodSubtype[])localParcel.createTypedArray(InputMethodSubtype.CREATOR);
      return paramArrayOfByte;
    }
    finally
    {
      if (localObject != null) {
        localObject.recycle();
      }
    }
  }
  
  public InputMethodSubtype get(int paramInt)
  {
    if ((paramInt >= 0) && (mCount > paramInt))
    {
      InputMethodSubtype[] arrayOfInputMethodSubtype = mInstance;
      Object localObject1 = arrayOfInputMethodSubtype;
      if (arrayOfInputMethodSubtype == null) {
        synchronized (mLockObject)
        {
          arrayOfInputMethodSubtype = mInstance;
          localObject1 = arrayOfInputMethodSubtype;
          if (arrayOfInputMethodSubtype == null)
          {
            localObject1 = decompress(mCompressedData, mDecompressedSize);
            mCompressedData = null;
            mDecompressedSize = 0;
            if (localObject1 != null)
            {
              localObject1 = unmarshall((byte[])localObject1);
            }
            else
            {
              Slog.e("InputMethodSubtypeArray", "Failed to decompress data. Returns null as fallback.");
              localObject1 = new InputMethodSubtype[mCount];
            }
            mInstance = ((InputMethodSubtype[])localObject1);
          }
        }
      }
      return localObject2[paramInt];
    }
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public int getCount()
  {
    return mCount;
  }
  
  public void writeToParcel(Parcel paramParcel)
  {
    if (mCount == 0)
    {
      paramParcel.writeInt(mCount);
      return;
    }
    byte[] arrayOfByte1 = mCompressedData;
    int i = mDecompressedSize;
    byte[] arrayOfByte2 = arrayOfByte1;
    int j = i;
    if (arrayOfByte1 == null)
    {
      arrayOfByte2 = arrayOfByte1;
      j = i;
      if (i == 0) {
        synchronized (mLockObject)
        {
          arrayOfByte1 = mCompressedData;
          i = mDecompressedSize;
          arrayOfByte2 = arrayOfByte1;
          j = i;
          if (arrayOfByte1 == null)
          {
            arrayOfByte2 = arrayOfByte1;
            j = i;
            if (i == 0)
            {
              arrayOfByte1 = marshall(mInstance);
              arrayOfByte2 = compress(arrayOfByte1);
              if (arrayOfByte2 == null)
              {
                j = -1;
                Slog.i("InputMethodSubtypeArray", "Failed to compress data.");
              }
              else
              {
                j = arrayOfByte1.length;
              }
              mDecompressedSize = j;
              mCompressedData = arrayOfByte2;
            }
          }
        }
      }
    }
    if ((arrayOfByte2 != null) && (j > 0))
    {
      paramParcel.writeInt(mCount);
      paramParcel.writeInt(j);
      paramParcel.writeByteArray(arrayOfByte2);
    }
    else
    {
      Slog.i("InputMethodSubtypeArray", "Unexpected state. Behaving as an empty array.");
      paramParcel.writeInt(0);
    }
  }
}
