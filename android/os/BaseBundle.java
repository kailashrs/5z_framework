package android.os;

import android.util.ArrayMap;
import android.util.Log;
import android.util.MathUtils;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.IndentingPrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

public class BaseBundle
{
  private static final int BUNDLE_MAGIC = 1279544898;
  private static final int BUNDLE_MAGIC_NATIVE = 1279544900;
  static final boolean DEBUG = false;
  static final int FLAG_DEFUSABLE = 1;
  private static final boolean LOG_DEFUSABLE = false;
  private static final String TAG = "Bundle";
  private static volatile boolean sShouldDefuse = false;
  private ClassLoader mClassLoader;
  @VisibleForTesting
  public int mFlags;
  ArrayMap<String, Object> mMap = null;
  private boolean mParcelledByNative;
  Parcel mParcelledData = null;
  
  BaseBundle()
  {
    this((ClassLoader)null, 0);
  }
  
  BaseBundle(int paramInt)
  {
    this((ClassLoader)null, paramInt);
  }
  
  BaseBundle(BaseBundle paramBaseBundle)
  {
    copyInternal(paramBaseBundle, false);
  }
  
  BaseBundle(Parcel paramParcel)
  {
    readFromParcelInner(paramParcel);
  }
  
  BaseBundle(Parcel paramParcel, int paramInt)
  {
    readFromParcelInner(paramParcel, paramInt);
  }
  
  BaseBundle(ClassLoader paramClassLoader)
  {
    this(paramClassLoader, 0);
  }
  
  BaseBundle(ClassLoader paramClassLoader, int paramInt)
  {
    ArrayMap localArrayMap;
    if (paramInt > 0) {
      localArrayMap = new ArrayMap(paramInt);
    } else {
      localArrayMap = new ArrayMap();
    }
    mMap = localArrayMap;
    if (paramClassLoader == null) {
      paramClassLoader = getClass().getClassLoader();
    }
    mClassLoader = paramClassLoader;
  }
  
  BaseBundle(boolean paramBoolean) {}
  
  public static void dumpStats(IndentingPrintWriter paramIndentingPrintWriter, BaseBundle paramBaseBundle)
  {
    paramIndentingPrintWriter.increaseIndent();
    if (paramBaseBundle == null)
    {
      paramIndentingPrintWriter.println("[null]");
      return;
    }
    paramBaseBundle = paramBaseBundle.getMap();
    for (int i = 0; i < paramBaseBundle.size(); i++) {
      dumpStats(paramIndentingPrintWriter, (String)paramBaseBundle.keyAt(i), paramBaseBundle.valueAt(i));
    }
    paramIndentingPrintWriter.decreaseIndent();
  }
  
  public static void dumpStats(IndentingPrintWriter paramIndentingPrintWriter, SparseArray paramSparseArray)
  {
    paramIndentingPrintWriter.increaseIndent();
    if (paramSparseArray == null)
    {
      paramIndentingPrintWriter.println("[null]");
      return;
    }
    for (int i = 0; i < paramSparseArray.size(); i++)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("0x");
      localStringBuilder.append(Integer.toHexString(paramSparseArray.keyAt(i)));
      dumpStats(paramIndentingPrintWriter, localStringBuilder.toString(), paramSparseArray.valueAt(i));
    }
    paramIndentingPrintWriter.decreaseIndent();
  }
  
  public static void dumpStats(IndentingPrintWriter paramIndentingPrintWriter, String paramString, Object paramObject)
  {
    Object localObject = Parcel.obtain();
    ((Parcel)localObject).writeValue(paramObject);
    int i = ((Parcel)localObject).dataPosition();
    ((Parcel)localObject).recycle();
    if (i > 1024)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append(" [size=");
      ((StringBuilder)localObject).append(i);
      ((StringBuilder)localObject).append("]");
      paramIndentingPrintWriter.println(((StringBuilder)localObject).toString());
      if ((paramObject instanceof BaseBundle)) {
        dumpStats(paramIndentingPrintWriter, (BaseBundle)paramObject);
      } else if ((paramObject instanceof SparseArray)) {
        dumpStats(paramIndentingPrintWriter, (SparseArray)paramObject);
      }
    }
  }
  
  /* Error */
  private void initializeFromParcelLocked(Parcel paramParcel, boolean paramBoolean1, boolean paramBoolean2)
  {
    // Byte code:
    //   0: aload_1
    //   1: invokestatic 181	android/os/BaseBundle:isEmptyParcel	(Landroid/os/Parcel;)Z
    //   4: ifeq +43 -> 47
    //   7: aload_0
    //   8: getfield 50	android/os/BaseBundle:mMap	Landroid/util/ArrayMap;
    //   11: ifnonnull +18 -> 29
    //   14: aload_0
    //   15: new 66	android/util/ArrayMap
    //   18: dup
    //   19: iconst_1
    //   20: invokespecial 68	android/util/ArrayMap:<init>	(I)V
    //   23: putfield 50	android/os/BaseBundle:mMap	Landroid/util/ArrayMap;
    //   26: goto +10 -> 36
    //   29: aload_0
    //   30: getfield 50	android/os/BaseBundle:mMap	Landroid/util/ArrayMap;
    //   33: invokevirtual 184	android/util/ArrayMap:erase	()V
    //   36: aload_0
    //   37: aconst_null
    //   38: putfield 52	android/os/BaseBundle:mParcelledData	Landroid/os/Parcel;
    //   41: aload_0
    //   42: iconst_0
    //   43: putfield 186	android/os/BaseBundle:mParcelledByNative	Z
    //   46: return
    //   47: aload_1
    //   48: invokevirtual 189	android/os/Parcel:readInt	()I
    //   51: istore 4
    //   53: iload 4
    //   55: ifge +4 -> 59
    //   58: return
    //   59: aload_0
    //   60: getfield 50	android/os/BaseBundle:mMap	Landroid/util/ArrayMap;
    //   63: astore 5
    //   65: aload 5
    //   67: ifnonnull +17 -> 84
    //   70: new 66	android/util/ArrayMap
    //   73: dup
    //   74: iload 4
    //   76: invokespecial 68	android/util/ArrayMap:<init>	(I)V
    //   79: astore 5
    //   81: goto +15 -> 96
    //   84: aload 5
    //   86: invokevirtual 184	android/util/ArrayMap:erase	()V
    //   89: aload 5
    //   91: iload 4
    //   93: invokevirtual 192	android/util/ArrayMap:ensureCapacity	(I)V
    //   96: iload_3
    //   97: ifeq +28 -> 125
    //   100: aload_1
    //   101: aload 5
    //   103: iload 4
    //   105: aload_0
    //   106: getfield 81	android/os/BaseBundle:mClassLoader	Ljava/lang/ClassLoader;
    //   109: invokevirtual 196	android/os/Parcel:readArrayMapSafelyInternal	(Landroid/util/ArrayMap;ILjava/lang/ClassLoader;)V
    //   112: goto +25 -> 137
    //   115: astore 6
    //   117: goto +85 -> 202
    //   120: astore 6
    //   122: goto +42 -> 164
    //   125: aload_1
    //   126: aload 5
    //   128: iload 4
    //   130: aload_0
    //   131: getfield 81	android/os/BaseBundle:mClassLoader	Ljava/lang/ClassLoader;
    //   134: invokevirtual 199	android/os/Parcel:readArrayMapInternal	(Landroid/util/ArrayMap;ILjava/lang/ClassLoader;)V
    //   137: aload_0
    //   138: aload 5
    //   140: putfield 50	android/os/BaseBundle:mMap	Landroid/util/ArrayMap;
    //   143: iload_2
    //   144: ifeq +7 -> 151
    //   147: aload_1
    //   148: invokestatic 202	android/os/BaseBundle:recycleParcel	(Landroid/os/Parcel;)V
    //   151: aload_0
    //   152: aconst_null
    //   153: putfield 52	android/os/BaseBundle:mParcelledData	Landroid/os/Parcel;
    //   156: aload_0
    //   157: iconst_0
    //   158: putfield 186	android/os/BaseBundle:mParcelledByNative	Z
    //   161: goto +37 -> 198
    //   164: getstatic 37	android/os/BaseBundle:sShouldDefuse	Z
    //   167: ifeq +32 -> 199
    //   170: ldc 22
    //   172: ldc -52
    //   174: aload 6
    //   176: invokestatic 210	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   179: pop
    //   180: aload 5
    //   182: invokevirtual 184	android/util/ArrayMap:erase	()V
    //   185: aload_0
    //   186: aload 5
    //   188: putfield 50	android/os/BaseBundle:mMap	Landroid/util/ArrayMap;
    //   191: iload_2
    //   192: ifeq -41 -> 151
    //   195: goto -48 -> 147
    //   198: return
    //   199: aload 6
    //   201: athrow
    //   202: aload_0
    //   203: aload 5
    //   205: putfield 50	android/os/BaseBundle:mMap	Landroid/util/ArrayMap;
    //   208: iload_2
    //   209: ifeq +7 -> 216
    //   212: aload_1
    //   213: invokestatic 202	android/os/BaseBundle:recycleParcel	(Landroid/os/Parcel;)V
    //   216: aload_0
    //   217: aconst_null
    //   218: putfield 52	android/os/BaseBundle:mParcelledData	Landroid/os/Parcel;
    //   221: aload_0
    //   222: iconst_0
    //   223: putfield 186	android/os/BaseBundle:mParcelledByNative	Z
    //   226: aload 6
    //   228: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	229	0	this	BaseBundle
    //   0	229	1	paramParcel	Parcel
    //   0	229	2	paramBoolean1	boolean
    //   0	229	3	paramBoolean2	boolean
    //   51	78	4	i	int
    //   63	141	5	localArrayMap	ArrayMap
    //   115	1	6	localObject	Object
    //   120	107	6	localBadParcelableException	BadParcelableException
    // Exception table:
    //   from	to	target	type
    //   100	112	115	finally
    //   125	137	115	finally
    //   164	185	115	finally
    //   199	202	115	finally
    //   100	112	120	android/os/BadParcelableException
    //   125	137	120	android/os/BadParcelableException
  }
  
  private static boolean isEmptyParcel(Parcel paramParcel)
  {
    boolean bool;
    if (paramParcel == NoImagePreloadHolder.EMPTY_PARCEL) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean kindofEquals(BaseBundle paramBaseBundle1, BaseBundle paramBaseBundle2)
  {
    boolean bool;
    if ((paramBaseBundle1 != paramBaseBundle2) && ((paramBaseBundle1 == null) || (!paramBaseBundle1.kindofEquals(paramBaseBundle2)))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private void readFromParcelInner(Parcel paramParcel, int paramInt)
  {
    if (paramInt >= 0)
    {
      if (paramInt == 0)
      {
        mParcelledData = NoImagePreloadHolder.EMPTY_PARCEL;
        mParcelledByNative = false;
        return;
      }
      int i = paramParcel.readInt();
      boolean bool = true;
      if (i == 1279544898) {
        j = 1;
      } else {
        j = 0;
      }
      if (i != 1279544900) {
        bool = false;
      }
      if ((j == 0) && (!bool))
      {
        paramParcel = new StringBuilder();
        paramParcel.append("Bad magic number for Bundle: 0x");
        paramParcel.append(Integer.toHexString(i));
        throw new IllegalStateException(paramParcel.toString());
      }
      if (paramParcel.hasReadWriteHelper()) {
        try
        {
          initializeFromParcelLocked(paramParcel, false, bool);
          return;
        }
        finally {}
      }
      int j = paramParcel.dataPosition();
      paramParcel.setDataPosition(MathUtils.addOrThrow(j, paramInt));
      Parcel localParcel = Parcel.obtain();
      localParcel.setDataPosition(0);
      localParcel.appendFrom(paramParcel, j, paramInt);
      localParcel.adoptClassCookies(paramParcel);
      localParcel.setDataPosition(0);
      mParcelledData = localParcel;
      mParcelledByNative = bool;
      return;
    }
    paramParcel = new StringBuilder();
    paramParcel.append("Bad length in parcel: ");
    paramParcel.append(paramInt);
    throw new RuntimeException(paramParcel.toString());
  }
  
  private static void recycleParcel(Parcel paramParcel)
  {
    if ((paramParcel != null) && (!isEmptyParcel(paramParcel))) {
      paramParcel.recycle();
    }
  }
  
  public static void setShouldDefuse(boolean paramBoolean)
  {
    sShouldDefuse = paramBoolean;
  }
  
  public void clear()
  {
    unparcel();
    mMap.clear();
  }
  
  public boolean containsKey(String paramString)
  {
    unparcel();
    return mMap.containsKey(paramString);
  }
  
  void copyInternal(BaseBundle paramBaseBundle, boolean paramBoolean)
  {
    try
    {
      Object localObject1 = mParcelledData;
      int i = 0;
      if (localObject1 != null)
      {
        if (paramBaseBundle.isEmptyParcel())
        {
          mParcelledData = NoImagePreloadHolder.EMPTY_PARCEL;
          mParcelledByNative = false;
        }
        else
        {
          mParcelledData = Parcel.obtain();
          mParcelledData.appendFrom(mParcelledData, 0, mParcelledData.dataSize());
          mParcelledData.setDataPosition(0);
          mParcelledByNative = mParcelledByNative;
        }
      }
      else
      {
        mParcelledData = null;
        mParcelledByNative = false;
      }
      if (mMap != null)
      {
        if (!paramBoolean)
        {
          localObject1 = new android/util/ArrayMap;
          ((ArrayMap)localObject1).<init>(mMap);
          mMap = ((ArrayMap)localObject1);
        }
        else
        {
          ArrayMap localArrayMap = mMap;
          int j = localArrayMap.size();
          localObject1 = new android/util/ArrayMap;
          ((ArrayMap)localObject1).<init>(j);
          mMap = ((ArrayMap)localObject1);
          while (i < j)
          {
            mMap.append((String)localArrayMap.keyAt(i), deepCopyValue(localArrayMap.valueAt(i)));
            i++;
          }
        }
      }
      else {
        mMap = null;
      }
      mClassLoader = mClassLoader;
      return;
    }
    finally {}
  }
  
  Object deepCopyValue(Object paramObject)
  {
    if (paramObject == null) {
      return null;
    }
    if ((paramObject instanceof Bundle)) {
      return ((Bundle)paramObject).deepCopy();
    }
    if ((paramObject instanceof PersistableBundle)) {
      return ((PersistableBundle)paramObject).deepCopy();
    }
    if ((paramObject instanceof ArrayList)) {
      return deepcopyArrayList((ArrayList)paramObject);
    }
    if (paramObject.getClass().isArray())
    {
      if ((paramObject instanceof int[])) {
        return ((int[])paramObject).clone();
      }
      if ((paramObject instanceof long[])) {
        return ((long[])paramObject).clone();
      }
      if ((paramObject instanceof float[])) {
        return ((float[])paramObject).clone();
      }
      if ((paramObject instanceof double[])) {
        return ((double[])paramObject).clone();
      }
      if ((paramObject instanceof Object[])) {
        return ((Object[])paramObject).clone();
      }
      if ((paramObject instanceof byte[])) {
        return ((byte[])paramObject).clone();
      }
      if ((paramObject instanceof short[])) {
        return ((short[])paramObject).clone();
      }
      if ((paramObject instanceof char[])) {
        return ((char[])paramObject).clone();
      }
    }
    return paramObject;
  }
  
  ArrayList deepcopyArrayList(ArrayList paramArrayList)
  {
    int i = paramArrayList.size();
    ArrayList localArrayList = new ArrayList(i);
    for (int j = 0; j < i; j++) {
      localArrayList.add(deepCopyValue(paramArrayList.get(j)));
    }
    return localArrayList;
  }
  
  public Object get(String paramString)
  {
    unparcel();
    return mMap.get(paramString);
  }
  
  public boolean getBoolean(String paramString)
  {
    unparcel();
    return getBoolean(paramString, false);
  }
  
  public boolean getBoolean(String paramString, boolean paramBoolean)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return paramBoolean;
    }
    try
    {
      boolean bool = ((Boolean)localObject).booleanValue();
      return bool;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "Boolean", Boolean.valueOf(paramBoolean), localClassCastException);
    }
    return paramBoolean;
  }
  
  public boolean[] getBooleanArray(String paramString)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return null;
    }
    try
    {
      boolean[] arrayOfBoolean = (boolean[])localObject;
      return arrayOfBoolean;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "byte[]", localClassCastException);
    }
    return null;
  }
  
  byte getByte(String paramString)
  {
    unparcel();
    return getByte(paramString, (byte)0).byteValue();
  }
  
  Byte getByte(String paramString, byte paramByte)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return Byte.valueOf(paramByte);
    }
    try
    {
      Byte localByte = (Byte)localObject;
      return localByte;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "Byte", Byte.valueOf(paramByte), localClassCastException);
    }
    return Byte.valueOf(paramByte);
  }
  
  byte[] getByteArray(String paramString)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return null;
    }
    try
    {
      byte[] arrayOfByte = (byte[])localObject;
      return arrayOfByte;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "byte[]", localClassCastException);
    }
    return null;
  }
  
  char getChar(String paramString)
  {
    unparcel();
    return getChar(paramString, '\000');
  }
  
  char getChar(String paramString, char paramChar)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return paramChar;
    }
    try
    {
      char c = ((Character)localObject).charValue();
      return c;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "Character", Character.valueOf(paramChar), localClassCastException);
    }
    return paramChar;
  }
  
  char[] getCharArray(String paramString)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return null;
    }
    try
    {
      char[] arrayOfChar = (char[])localObject;
      return arrayOfChar;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "char[]", localClassCastException);
    }
    return null;
  }
  
  CharSequence getCharSequence(String paramString)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    try
    {
      CharSequence localCharSequence = (CharSequence)localObject;
      return localCharSequence;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "CharSequence", localClassCastException);
    }
    return null;
  }
  
  CharSequence getCharSequence(String paramString, CharSequence paramCharSequence)
  {
    paramString = getCharSequence(paramString);
    if (paramString == null) {
      paramString = paramCharSequence;
    }
    return paramString;
  }
  
  CharSequence[] getCharSequenceArray(String paramString)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return null;
    }
    try
    {
      CharSequence[] arrayOfCharSequence = (CharSequence[])localObject;
      return arrayOfCharSequence;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "CharSequence[]", localClassCastException);
    }
    return null;
  }
  
  ArrayList<CharSequence> getCharSequenceArrayList(String paramString)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return null;
    }
    try
    {
      ArrayList localArrayList = (ArrayList)localObject;
      return localArrayList;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "ArrayList<CharSequence>", localClassCastException);
    }
    return null;
  }
  
  ClassLoader getClassLoader()
  {
    return mClassLoader;
  }
  
  public double getDouble(String paramString)
  {
    unparcel();
    return getDouble(paramString, 0.0D);
  }
  
  public double getDouble(String paramString, double paramDouble)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return paramDouble;
    }
    try
    {
      double d = ((Double)localObject).doubleValue();
      return d;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "Double", Double.valueOf(paramDouble), localClassCastException);
    }
    return paramDouble;
  }
  
  public double[] getDoubleArray(String paramString)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return null;
    }
    try
    {
      double[] arrayOfDouble = (double[])localObject;
      return arrayOfDouble;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "double[]", localClassCastException);
    }
    return null;
  }
  
  float getFloat(String paramString)
  {
    unparcel();
    return getFloat(paramString, 0.0F);
  }
  
  float getFloat(String paramString, float paramFloat)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return paramFloat;
    }
    try
    {
      float f = ((Float)localObject).floatValue();
      return f;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "Float", Float.valueOf(paramFloat), localClassCastException);
    }
    return paramFloat;
  }
  
  float[] getFloatArray(String paramString)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return null;
    }
    try
    {
      float[] arrayOfFloat = (float[])localObject;
      return arrayOfFloat;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "float[]", localClassCastException);
    }
    return null;
  }
  
  public int getInt(String paramString)
  {
    unparcel();
    return getInt(paramString, 0);
  }
  
  public int getInt(String paramString, int paramInt)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return paramInt;
    }
    try
    {
      int i = ((Integer)localObject).intValue();
      return i;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "Integer", Integer.valueOf(paramInt), localClassCastException);
    }
    return paramInt;
  }
  
  public int[] getIntArray(String paramString)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return null;
    }
    try
    {
      int[] arrayOfInt = (int[])localObject;
      return arrayOfInt;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "int[]", localClassCastException);
    }
    return null;
  }
  
  ArrayList<Integer> getIntegerArrayList(String paramString)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return null;
    }
    try
    {
      ArrayList localArrayList = (ArrayList)localObject;
      return localArrayList;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "ArrayList<Integer>", localClassCastException);
    }
    return null;
  }
  
  public long getLong(String paramString)
  {
    unparcel();
    return getLong(paramString, 0L);
  }
  
  public long getLong(String paramString, long paramLong)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return paramLong;
    }
    try
    {
      long l = ((Long)localObject).longValue();
      return l;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "Long", Long.valueOf(paramLong), localClassCastException);
    }
    return paramLong;
  }
  
  public long[] getLongArray(String paramString)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return null;
    }
    try
    {
      long[] arrayOfLong = (long[])localObject;
      return arrayOfLong;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "long[]", localClassCastException);
    }
    return null;
  }
  
  ArrayMap<String, Object> getMap()
  {
    unparcel();
    return mMap;
  }
  
  public String getPairValue()
  {
    unparcel();
    int i = mMap.size();
    if (i > 1) {
      Log.w("Bundle", "getPairValue() used on Bundle with multiple pairs.");
    }
    if (i == 0) {
      return null;
    }
    Object localObject = mMap.valueAt(0);
    try
    {
      String str = (String)localObject;
      return str;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning("getPairValue()", localObject, "String", localClassCastException);
    }
    return null;
  }
  
  Serializable getSerializable(String paramString)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return null;
    }
    try
    {
      Serializable localSerializable = (Serializable)localObject;
      return localSerializable;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "Serializable", localClassCastException);
    }
    return null;
  }
  
  short getShort(String paramString)
  {
    unparcel();
    return getShort(paramString, (short)0);
  }
  
  short getShort(String paramString, short paramShort)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return paramShort;
    }
    try
    {
      short s = ((Short)localObject).shortValue();
      return s;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "Short", Short.valueOf(paramShort), localClassCastException);
    }
    return paramShort;
  }
  
  short[] getShortArray(String paramString)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return null;
    }
    try
    {
      short[] arrayOfShort = (short[])localObject;
      return arrayOfShort;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "short[]", localClassCastException);
    }
    return null;
  }
  
  public String getString(String paramString)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    try
    {
      String str = (String)localObject;
      return str;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "String", localClassCastException);
    }
    return null;
  }
  
  public String getString(String paramString1, String paramString2)
  {
    paramString1 = getString(paramString1);
    if (paramString1 == null) {
      paramString1 = paramString2;
    }
    return paramString1;
  }
  
  public String[] getStringArray(String paramString)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return null;
    }
    try
    {
      String[] arrayOfString = (String[])localObject;
      return arrayOfString;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "String[]", localClassCastException);
    }
    return null;
  }
  
  ArrayList<String> getStringArrayList(String paramString)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return null;
    }
    try
    {
      ArrayList localArrayList = (ArrayList)localObject;
      return localArrayList;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "ArrayList<String>", localClassCastException);
    }
    return null;
  }
  
  public boolean isEmpty()
  {
    unparcel();
    return mMap.isEmpty();
  }
  
  public boolean isEmptyParcel()
  {
    return isEmptyParcel(mParcelledData);
  }
  
  public boolean isParcelled()
  {
    boolean bool;
    if (mParcelledData != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public Set<String> keySet()
  {
    unparcel();
    return mMap.keySet();
  }
  
  public boolean kindofEquals(BaseBundle paramBaseBundle)
  {
    boolean bool = false;
    if (paramBaseBundle == null) {
      return false;
    }
    if (isParcelled() != paramBaseBundle.isParcelled()) {
      return false;
    }
    if (isParcelled())
    {
      if (mParcelledData.compareData(mParcelledData) == 0) {
        bool = true;
      }
      return bool;
    }
    return mMap.equals(mMap);
  }
  
  public boolean maybeIsEmpty()
  {
    if (isParcelled()) {
      return isEmptyParcel();
    }
    return isEmpty();
  }
  
  public void putAll(PersistableBundle paramPersistableBundle)
  {
    unparcel();
    paramPersistableBundle.unparcel();
    mMap.putAll(mMap);
  }
  
  void putAll(ArrayMap paramArrayMap)
  {
    unparcel();
    mMap.putAll(paramArrayMap);
  }
  
  public void putBoolean(String paramString, boolean paramBoolean)
  {
    unparcel();
    mMap.put(paramString, Boolean.valueOf(paramBoolean));
  }
  
  public void putBooleanArray(String paramString, boolean[] paramArrayOfBoolean)
  {
    unparcel();
    mMap.put(paramString, paramArrayOfBoolean);
  }
  
  void putByte(String paramString, byte paramByte)
  {
    unparcel();
    mMap.put(paramString, Byte.valueOf(paramByte));
  }
  
  void putByteArray(String paramString, byte[] paramArrayOfByte)
  {
    unparcel();
    mMap.put(paramString, paramArrayOfByte);
  }
  
  void putChar(String paramString, char paramChar)
  {
    unparcel();
    mMap.put(paramString, Character.valueOf(paramChar));
  }
  
  void putCharArray(String paramString, char[] paramArrayOfChar)
  {
    unparcel();
    mMap.put(paramString, paramArrayOfChar);
  }
  
  void putCharSequence(String paramString, CharSequence paramCharSequence)
  {
    unparcel();
    mMap.put(paramString, paramCharSequence);
  }
  
  void putCharSequenceArray(String paramString, CharSequence[] paramArrayOfCharSequence)
  {
    unparcel();
    mMap.put(paramString, paramArrayOfCharSequence);
  }
  
  void putCharSequenceArrayList(String paramString, ArrayList<CharSequence> paramArrayList)
  {
    unparcel();
    mMap.put(paramString, paramArrayList);
  }
  
  public void putDouble(String paramString, double paramDouble)
  {
    unparcel();
    mMap.put(paramString, Double.valueOf(paramDouble));
  }
  
  public void putDoubleArray(String paramString, double[] paramArrayOfDouble)
  {
    unparcel();
    mMap.put(paramString, paramArrayOfDouble);
  }
  
  void putFloat(String paramString, float paramFloat)
  {
    unparcel();
    mMap.put(paramString, Float.valueOf(paramFloat));
  }
  
  void putFloatArray(String paramString, float[] paramArrayOfFloat)
  {
    unparcel();
    mMap.put(paramString, paramArrayOfFloat);
  }
  
  public void putInt(String paramString, int paramInt)
  {
    unparcel();
    mMap.put(paramString, Integer.valueOf(paramInt));
  }
  
  public void putIntArray(String paramString, int[] paramArrayOfInt)
  {
    unparcel();
    mMap.put(paramString, paramArrayOfInt);
  }
  
  void putIntegerArrayList(String paramString, ArrayList<Integer> paramArrayList)
  {
    unparcel();
    mMap.put(paramString, paramArrayList);
  }
  
  public void putLong(String paramString, long paramLong)
  {
    unparcel();
    mMap.put(paramString, Long.valueOf(paramLong));
  }
  
  public void putLongArray(String paramString, long[] paramArrayOfLong)
  {
    unparcel();
    mMap.put(paramString, paramArrayOfLong);
  }
  
  void putSerializable(String paramString, Serializable paramSerializable)
  {
    unparcel();
    mMap.put(paramString, paramSerializable);
  }
  
  void putShort(String paramString, short paramShort)
  {
    unparcel();
    mMap.put(paramString, Short.valueOf(paramShort));
  }
  
  void putShortArray(String paramString, short[] paramArrayOfShort)
  {
    unparcel();
    mMap.put(paramString, paramArrayOfShort);
  }
  
  public void putString(String paramString1, String paramString2)
  {
    unparcel();
    mMap.put(paramString1, paramString2);
  }
  
  public void putStringArray(String paramString, String[] paramArrayOfString)
  {
    unparcel();
    mMap.put(paramString, paramArrayOfString);
  }
  
  void putStringArrayList(String paramString, ArrayList<String> paramArrayList)
  {
    unparcel();
    mMap.put(paramString, paramArrayList);
  }
  
  void readFromParcelInner(Parcel paramParcel)
  {
    readFromParcelInner(paramParcel, paramParcel.readInt());
  }
  
  public void remove(String paramString)
  {
    unparcel();
    mMap.remove(paramString);
  }
  
  void setClassLoader(ClassLoader paramClassLoader)
  {
    mClassLoader = paramClassLoader;
  }
  
  public int size()
  {
    unparcel();
    return mMap.size();
  }
  
  void typeWarning(String paramString1, Object paramObject, String paramString2, ClassCastException paramClassCastException)
  {
    typeWarning(paramString1, paramObject, paramString2, "<null>", paramClassCastException);
  }
  
  void typeWarning(String paramString1, Object paramObject1, String paramString2, Object paramObject2, ClassCastException paramClassCastException)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Key ");
    localStringBuilder.append(paramString1);
    localStringBuilder.append(" expected ");
    localStringBuilder.append(paramString2);
    localStringBuilder.append(" but value was a ");
    localStringBuilder.append(paramObject1.getClass().getName());
    localStringBuilder.append(".  The default value ");
    localStringBuilder.append(paramObject2);
    localStringBuilder.append(" was returned.");
    Log.w("Bundle", localStringBuilder.toString());
    Log.w("Bundle", "Attempt to cast generated internal exception:", paramClassCastException);
  }
  
  void unparcel()
  {
    try
    {
      Parcel localParcel = mParcelledData;
      if (localParcel != null) {
        initializeFromParcelLocked(localParcel, true, mParcelledByNative);
      }
      return;
    }
    finally {}
  }
  
  void writeToParcelInner(Parcel paramParcel, int paramInt)
  {
    if (paramParcel.hasReadWriteHelper()) {
      unparcel();
    }
    try
    {
      Object localObject = mParcelledData;
      paramInt = 1279544898;
      int i;
      if (localObject != null)
      {
        if (mParcelledData == NoImagePreloadHolder.EMPTY_PARCEL)
        {
          paramParcel.writeInt(0);
        }
        else
        {
          i = mParcelledData.dataSize();
          paramParcel.writeInt(i);
          if (mParcelledByNative) {
            paramInt = 1279544900;
          }
          paramParcel.writeInt(paramInt);
          paramParcel.appendFrom(mParcelledData, 0, i);
        }
        return;
      }
      localObject = mMap;
      if ((localObject != null) && (((ArrayMap)localObject).size() > 0))
      {
        paramInt = paramParcel.dataPosition();
        paramParcel.writeInt(-1);
        paramParcel.writeInt(1279544898);
        int j = paramParcel.dataPosition();
        paramParcel.writeArrayMapInternal((ArrayMap)localObject);
        i = paramParcel.dataPosition();
        paramParcel.setDataPosition(paramInt);
        paramParcel.writeInt(i - j);
        paramParcel.setDataPosition(i);
        return;
      }
      paramParcel.writeInt(0);
      return;
    }
    finally {}
  }
  
  static final class NoImagePreloadHolder
  {
    public static final Parcel EMPTY_PARCEL = ;
    
    NoImagePreloadHolder() {}
  }
}
