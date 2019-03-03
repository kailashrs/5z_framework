package android.media;

import android.content.Context;
import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;
import android.util.Pair;
import android.util.Range;
import android.util.Rational;
import android.util.Size;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

class Utils
{
  private static final String TAG = "Utils";
  
  Utils() {}
  
  static Range<Integer> alignRange(Range<Integer> paramRange, int paramInt)
  {
    return paramRange.intersect(Integer.valueOf(divUp(((Integer)paramRange.getLower()).intValue(), paramInt) * paramInt), Integer.valueOf(((Integer)paramRange.getUpper()).intValue() / paramInt * paramInt));
  }
  
  public static <T extends Comparable<? super T>> int binarySearchDistinctRanges(Range<T>[] paramArrayOfRange, T paramT)
  {
    Arrays.binarySearch(paramArrayOfRange, Range.create(paramT, paramT), new Comparator()
    {
      public int compare(Range<T> paramAnonymousRange1, Range<T> paramAnonymousRange2)
      {
        if (paramAnonymousRange1.getUpper().compareTo(paramAnonymousRange2.getLower()) < 0) {
          return -1;
        }
        if (paramAnonymousRange1.getLower().compareTo(paramAnonymousRange2.getUpper()) > 0) {
          return 1;
        }
        return 0;
      }
    });
  }
  
  static int divUp(int paramInt1, int paramInt2)
  {
    return (paramInt1 + paramInt2 - 1) / paramInt2;
  }
  
  static long divUp(long paramLong1, long paramLong2)
  {
    return (paramLong1 + paramLong2 - 1L) / paramLong2;
  }
  
  static Range<Integer> factorRange(Range<Integer> paramRange, int paramInt)
  {
    if (paramInt == 1) {
      return paramRange;
    }
    return Range.create(Integer.valueOf(divUp(((Integer)paramRange.getLower()).intValue(), paramInt)), Integer.valueOf(((Integer)paramRange.getUpper()).intValue() / paramInt));
  }
  
  static Range<Long> factorRange(Range<Long> paramRange, long paramLong)
  {
    if (paramLong == 1L) {
      return paramRange;
    }
    return Range.create(Long.valueOf(divUp(((Long)paramRange.getLower()).longValue(), paramLong)), Long.valueOf(((Long)paramRange.getUpper()).longValue() / paramLong));
  }
  
  static int gcd(int paramInt1, int paramInt2)
  {
    if ((paramInt1 == 0) && (paramInt2 == 0)) {
      return 1;
    }
    int i = paramInt2;
    if (paramInt2 < 0) {
      i = -paramInt2;
    }
    paramInt2 = paramInt1;
    int j = i;
    if (paramInt1 < 0)
    {
      paramInt2 = -paramInt1;
      j = i;
    }
    for (;;)
    {
      paramInt1 = j;
      if (paramInt2 == 0) {
        break;
      }
      j = paramInt2;
      paramInt2 = paramInt1 % paramInt2;
    }
    return paramInt1;
  }
  
  /* Error */
  static String getFileDisplayNameFromUri(Context paramContext, android.net.Uri paramUri)
  {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual 87	android/net/Uri:getScheme	()Ljava/lang/String;
    //   4: astore_2
    //   5: ldc 89
    //   7: aload_2
    //   8: invokevirtual 95	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   11: ifeq +8 -> 19
    //   14: aload_1
    //   15: invokevirtual 98	android/net/Uri:getLastPathSegment	()Ljava/lang/String;
    //   18: areturn
    //   19: ldc 100
    //   21: aload_2
    //   22: invokevirtual 95	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   25: ifeq +132 -> 157
    //   28: aload_0
    //   29: invokevirtual 106	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   32: aload_1
    //   33: iconst_1
    //   34: anewarray 91	java/lang/String
    //   37: dup
    //   38: iconst_0
    //   39: ldc 108
    //   41: aastore
    //   42: aconst_null
    //   43: aconst_null
    //   44: aconst_null
    //   45: invokevirtual 114	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   48: astore_3
    //   49: aconst_null
    //   50: astore_2
    //   51: aload_3
    //   52: ifnull +95 -> 147
    //   55: aload_2
    //   56: astore_0
    //   57: aload_3
    //   58: invokeinterface 119 1 0
    //   63: ifeq +84 -> 147
    //   66: aload_2
    //   67: astore_0
    //   68: aload_3
    //   69: invokeinterface 123 1 0
    //   74: pop
    //   75: aload_2
    //   76: astore_0
    //   77: aload_3
    //   78: aload_3
    //   79: ldc 108
    //   81: invokeinterface 127 2 0
    //   86: invokeinterface 131 2 0
    //   91: astore_1
    //   92: aload_3
    //   93: ifnull +9 -> 102
    //   96: aload_3
    //   97: invokeinterface 134 1 0
    //   102: aload_1
    //   103: areturn
    //   104: astore_1
    //   105: goto +8 -> 113
    //   108: astore_1
    //   109: aload_1
    //   110: astore_0
    //   111: aload_1
    //   112: athrow
    //   113: aload_3
    //   114: ifnull +31 -> 145
    //   117: aload_0
    //   118: ifnull +21 -> 139
    //   121: aload_3
    //   122: invokeinterface 134 1 0
    //   127: goto +18 -> 145
    //   130: astore_2
    //   131: aload_0
    //   132: aload_2
    //   133: invokevirtual 138	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   136: goto +9 -> 145
    //   139: aload_3
    //   140: invokeinterface 134 1 0
    //   145: aload_1
    //   146: athrow
    //   147: aload_3
    //   148: ifnull +9 -> 157
    //   151: aload_3
    //   152: invokeinterface 134 1 0
    //   157: aload_1
    //   158: invokevirtual 141	android/net/Uri:toString	()Ljava/lang/String;
    //   161: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	162	0	paramContext	Context
    //   0	162	1	paramUri	android.net.Uri
    //   4	72	2	str	String
    //   130	3	2	localThrowable	Throwable
    //   48	104	3	localCursor	android.database.Cursor
    // Exception table:
    //   from	to	target	type
    //   57	66	104	finally
    //   68	75	104	finally
    //   77	92	104	finally
    //   111	113	104	finally
    //   57	66	108	java/lang/Throwable
    //   68	75	108	java/lang/Throwable
    //   77	92	108	java/lang/Throwable
    //   121	127	130	java/lang/Throwable
  }
  
  public static File getUniqueExternalFile(Context paramContext, String paramString1, String paramString2, String paramString3)
  {
    paramContext = Environment.getExternalStoragePublicDirectory(paramString1);
    paramContext.mkdirs();
    try
    {
      paramContext = FileUtils.buildUniqueFile(paramContext, paramString3, paramString2);
      return paramContext;
    }
    catch (FileNotFoundException paramContext)
    {
      paramString1 = new StringBuilder();
      paramString1.append("Unable to get a unique file name: ");
      paramString1.append(paramContext);
      Log.e("Utils", paramString1.toString());
    }
    return null;
  }
  
  static Range<Integer> intRangeFor(double paramDouble)
  {
    return Range.create(Integer.valueOf((int)paramDouble), Integer.valueOf((int)Math.ceil(paramDouble)));
  }
  
  public static <T extends Comparable<? super T>> Range<T>[] intersectSortedDistinctRanges(Range<T>[] paramArrayOfRange1, Range<T>[] paramArrayOfRange2)
  {
    int i = 0;
    Vector localVector = new Vector();
    int j = paramArrayOfRange2.length;
    for (int k = 0; k < j; k++)
    {
      Range<T> localRange = paramArrayOfRange2[k];
      for (int m = i;; m++)
      {
        i = m;
        if (m >= paramArrayOfRange1.length) {
          break;
        }
        i = m;
        if (paramArrayOfRange1[m].getUpper().compareTo(localRange.getLower()) >= 0) {
          break;
        }
      }
      while ((i < paramArrayOfRange1.length) && (paramArrayOfRange1[i].getUpper().compareTo(localRange.getUpper()) < 0))
      {
        localVector.add(localRange.intersect(paramArrayOfRange1[i]));
        i++;
      }
      if (i == paramArrayOfRange1.length) {
        break;
      }
      if (paramArrayOfRange1[i].getLower().compareTo(localRange.getUpper()) <= 0) {
        localVector.add(localRange.intersect(paramArrayOfRange1[i]));
      }
    }
    return (Range[])localVector.toArray(new Range[localVector.size()]);
  }
  
  private static long lcm(int paramInt1, int paramInt2)
  {
    if ((paramInt1 != 0) && (paramInt2 != 0)) {
      return paramInt1 * paramInt2 / gcd(paramInt1, paramInt2);
    }
    throw new IllegalArgumentException("lce is not defined for zero arguments");
  }
  
  static Range<Long> longRangeFor(double paramDouble)
  {
    return Range.create(Long.valueOf(paramDouble), Long.valueOf(Math.ceil(paramDouble)));
  }
  
  static Range<Integer> parseIntRange(Object paramObject, Range<Integer> paramRange)
  {
    try
    {
      Object localObject = (String)paramObject;
      int i = ((String)localObject).indexOf('-');
      if (i >= 0) {
        return Range.create(Integer.valueOf(Integer.parseInt(((String)localObject).substring(0, i), 10)), Integer.valueOf(Integer.parseInt(((String)localObject).substring(i + 1), 10)));
      }
      i = Integer.parseInt((String)localObject);
      localObject = Range.create(Integer.valueOf(i), Integer.valueOf(i));
      return localObject;
    }
    catch (IllegalArgumentException localIllegalArgumentException) {}catch (NullPointerException paramObject)
    {
      return paramRange;
    }
    catch (NumberFormatException localNumberFormatException) {}catch (ClassCastException localClassCastException) {}
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("could not parse integer range '");
    localStringBuilder.append(paramObject);
    localStringBuilder.append("'");
    Log.w("Utils", localStringBuilder.toString());
    return paramRange;
  }
  
  static int parseIntSafely(Object paramObject, int paramInt)
  {
    if (paramObject == null) {
      return paramInt;
    }
    try
    {
      int i = Integer.parseInt((String)paramObject);
      return i;
    }
    catch (NullPointerException paramObject)
    {
      return paramInt;
    }
    catch (NumberFormatException localNumberFormatException) {}catch (ClassCastException localClassCastException) {}
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("could not parse integer '");
    localStringBuilder.append(paramObject);
    localStringBuilder.append("'");
    Log.w("Utils", localStringBuilder.toString());
    return paramInt;
  }
  
  static Range<Long> parseLongRange(Object paramObject, Range<Long> paramRange)
  {
    try
    {
      Object localObject = (String)paramObject;
      int i = ((String)localObject).indexOf('-');
      if (i >= 0) {
        return Range.create(Long.valueOf(Long.parseLong(((String)localObject).substring(0, i), 10)), Long.valueOf(Long.parseLong(((String)localObject).substring(i + 1), 10)));
      }
      long l = Long.parseLong((String)localObject);
      localObject = Range.create(Long.valueOf(l), Long.valueOf(l));
      return localObject;
    }
    catch (IllegalArgumentException localIllegalArgumentException) {}catch (NullPointerException paramObject)
    {
      return paramRange;
    }
    catch (NumberFormatException localNumberFormatException) {}catch (ClassCastException localClassCastException) {}
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("could not parse long range '");
    localStringBuilder.append(paramObject);
    localStringBuilder.append("'");
    Log.w("Utils", localStringBuilder.toString());
    return paramRange;
  }
  
  static Range<Rational> parseRationalRange(Object paramObject, Range<Rational> paramRange)
  {
    try
    {
      Object localObject = (String)paramObject;
      int i = ((String)localObject).indexOf('-');
      if (i >= 0) {
        return Range.create(Rational.parseRational(((String)localObject).substring(0, i)), Rational.parseRational(((String)localObject).substring(i + 1)));
      }
      localObject = Rational.parseRational((String)localObject);
      localObject = Range.create((Comparable)localObject, (Comparable)localObject);
      return localObject;
    }
    catch (IllegalArgumentException localIllegalArgumentException) {}catch (NullPointerException paramObject)
    {
      return paramRange;
    }
    catch (NumberFormatException localNumberFormatException) {}catch (ClassCastException localClassCastException) {}
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("could not parse rational range '");
    localStringBuilder.append(paramObject);
    localStringBuilder.append("'");
    Log.w("Utils", localStringBuilder.toString());
    return paramRange;
  }
  
  static Size parseSize(Object paramObject, Size paramSize)
  {
    try
    {
      Size localSize = Size.parseSize((String)paramObject);
      return localSize;
    }
    catch (NullPointerException paramObject)
    {
      return paramSize;
    }
    catch (NumberFormatException localNumberFormatException) {}catch (ClassCastException localClassCastException) {}
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("could not parse size '");
    localStringBuilder.append(paramObject);
    localStringBuilder.append("'");
    Log.w("Utils", localStringBuilder.toString());
    return paramSize;
  }
  
  static Pair<Size, Size> parseSizeRange(Object paramObject)
  {
    try
    {
      Object localObject = (String)paramObject;
      int i = ((String)localObject).indexOf('-');
      if (i >= 0) {
        return Pair.create(Size.parseSize(((String)localObject).substring(0, i)), Size.parseSize(((String)localObject).substring(i + 1)));
      }
      localObject = Size.parseSize((String)localObject);
      localObject = Pair.create(localObject, localObject);
      return localObject;
    }
    catch (IllegalArgumentException localIllegalArgumentException) {}catch (NullPointerException paramObject)
    {
      return null;
    }
    catch (NumberFormatException localNumberFormatException) {}catch (ClassCastException localClassCastException) {}
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("could not parse size range '");
    localStringBuilder.append(paramObject);
    localStringBuilder.append("'");
    Log.w("Utils", localStringBuilder.toString());
    return null;
  }
  
  static Range<Rational> scaleRange(Range<Rational> paramRange, int paramInt1, int paramInt2)
  {
    if (paramInt1 == paramInt2) {
      return paramRange;
    }
    return Range.create(scaleRatio((Rational)paramRange.getLower(), paramInt1, paramInt2), scaleRatio((Rational)paramRange.getUpper(), paramInt1, paramInt2));
  }
  
  private static Rational scaleRatio(Rational paramRational, int paramInt1, int paramInt2)
  {
    int i = gcd(paramInt1, paramInt2);
    paramInt1 /= i;
    paramInt2 /= i;
    return new Rational((int)(paramRational.getNumerator() * paramInt1), (int)(paramRational.getDenominator() * paramInt2));
  }
  
  public static <T extends Comparable<? super T>> void sortDistinctRanges(Range<T>[] paramArrayOfRange)
  {
    Arrays.sort(paramArrayOfRange, new Comparator()
    {
      public int compare(Range<T> paramAnonymousRange1, Range<T> paramAnonymousRange2)
      {
        if (paramAnonymousRange1.getUpper().compareTo(paramAnonymousRange2.getLower()) < 0) {
          return -1;
        }
        if (paramAnonymousRange1.getLower().compareTo(paramAnonymousRange2.getUpper()) > 0) {
          return 1;
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("sample rate ranges must be distinct (");
        localStringBuilder.append(paramAnonymousRange1);
        localStringBuilder.append(" and ");
        localStringBuilder.append(paramAnonymousRange2);
        localStringBuilder.append(")");
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
    });
  }
}
