package android.provider;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.graphics.Typeface.Builder;
import android.graphics.fonts.FontVariationAxis;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.LruCache;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FontsContract
{
  private static final long SYNC_FONT_FETCH_TIMEOUT_MS = 500L;
  private static final String TAG = "FontsContract";
  private static final int THREAD_RENEWAL_THRESHOLD_MS = 10000;
  private static final Comparator<byte[]> sByteArrayComparator = _..Lambda.FontsContract.3FDNQd_WsglsyDhif_aHVbzkfrA.INSTANCE;
  private static volatile Context sContext;
  @GuardedBy("sLock")
  private static Handler sHandler;
  @GuardedBy("sLock")
  private static Set<String> sInQueueSet;
  private static final Object sLock = new Object();
  private static final Runnable sReplaceDispatcherThreadRunnable;
  @GuardedBy("sLock")
  private static HandlerThread sThread;
  private static final LruCache<String, Typeface> sTypefaceCache = new LruCache(16);
  
  static
  {
    sReplaceDispatcherThreadRunnable = new Runnable()
    {
      public void run()
      {
        synchronized (FontsContract.sLock)
        {
          if (FontsContract.sThread != null)
          {
            FontsContract.sThread.quitSafely();
            FontsContract.access$102(null);
            FontsContract.access$202(null);
          }
          return;
        }
      }
    };
  }
  
  private FontsContract() {}
  
  public static Typeface buildTypeface(Context paramContext, CancellationSignal paramCancellationSignal, FontInfo[] paramArrayOfFontInfo)
  {
    if (paramContext.isRestricted()) {
      return null;
    }
    paramContext = prepareFontData(paramContext, paramArrayOfFontInfo, paramCancellationSignal);
    if (paramContext.isEmpty()) {
      return null;
    }
    return new Typeface.Builder(paramArrayOfFontInfo, paramContext).build();
  }
  
  private static List<byte[]> convertToByteArrayList(Signature[] paramArrayOfSignature)
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i < paramArrayOfSignature.length; i++) {
      localArrayList.add(paramArrayOfSignature[i].toByteArray());
    }
    return localArrayList;
  }
  
  private static boolean equalsByteArrayList(List<byte[]> paramList1, List<byte[]> paramList2)
  {
    if (paramList1.size() != paramList2.size()) {
      return false;
    }
    for (int i = 0; i < paramList1.size(); i++) {
      if (!Arrays.equals((byte[])paramList1.get(i), (byte[])paramList2.get(i))) {
        return false;
      }
    }
    return true;
  }
  
  public static FontFamilyResult fetchFonts(Context paramContext, CancellationSignal paramCancellationSignal, FontRequest paramFontRequest)
    throws PackageManager.NameNotFoundException
  {
    if (paramContext.isRestricted()) {
      return new FontFamilyResult(3, null);
    }
    ProviderInfo localProviderInfo = getProvider(paramContext.getPackageManager(), paramFontRequest);
    if (localProviderInfo == null) {
      return new FontFamilyResult(1, null);
    }
    try
    {
      paramContext = new FontFamilyResult(0, getFontFromProvider(paramContext, paramFontRequest, authority, paramCancellationSignal));
      return paramContext;
    }
    catch (IllegalArgumentException paramContext) {}
    return new FontFamilyResult(2, null);
  }
  
  /* Error */
  @VisibleForTesting
  public static FontInfo[] getFontFromProvider(Context paramContext, FontRequest paramFontRequest, String paramString, CancellationSignal paramCancellationSignal)
  {
    // Byte code:
    //   0: new 137	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 138	java/util/ArrayList:<init>	()V
    //   7: astore 4
    //   9: new 201	android/net/Uri$Builder
    //   12: dup
    //   13: invokespecial 202	android/net/Uri$Builder:<init>	()V
    //   16: ldc -52
    //   18: invokevirtual 208	android/net/Uri$Builder:scheme	(Ljava/lang/String;)Landroid/net/Uri$Builder;
    //   21: aload_2
    //   22: invokevirtual 210	android/net/Uri$Builder:authority	(Ljava/lang/String;)Landroid/net/Uri$Builder;
    //   25: invokevirtual 213	android/net/Uri$Builder:build	()Landroid/net/Uri;
    //   28: astore 5
    //   30: new 201	android/net/Uri$Builder
    //   33: dup
    //   34: invokespecial 202	android/net/Uri$Builder:<init>	()V
    //   37: ldc -52
    //   39: invokevirtual 208	android/net/Uri$Builder:scheme	(Ljava/lang/String;)Landroid/net/Uri$Builder;
    //   42: aload_2
    //   43: invokevirtual 210	android/net/Uri$Builder:authority	(Ljava/lang/String;)Landroid/net/Uri$Builder;
    //   46: ldc -41
    //   48: invokevirtual 218	android/net/Uri$Builder:appendPath	(Ljava/lang/String;)Landroid/net/Uri$Builder;
    //   51: invokevirtual 213	android/net/Uri$Builder:build	()Landroid/net/Uri;
    //   54: astore 6
    //   56: aload_0
    //   57: invokevirtual 222	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   60: astore_0
    //   61: aload_1
    //   62: invokevirtual 228	android/provider/FontRequest:getQuery	()Ljava/lang/String;
    //   65: astore_1
    //   66: aload_0
    //   67: aload 5
    //   69: bipush 7
    //   71: anewarray 230	java/lang/String
    //   74: dup
    //   75: iconst_0
    //   76: ldc -24
    //   78: aastore
    //   79: dup
    //   80: iconst_1
    //   81: ldc -22
    //   83: aastore
    //   84: dup
    //   85: iconst_2
    //   86: ldc -20
    //   88: aastore
    //   89: dup
    //   90: iconst_3
    //   91: ldc -18
    //   93: aastore
    //   94: dup
    //   95: iconst_4
    //   96: ldc -16
    //   98: aastore
    //   99: dup
    //   100: iconst_5
    //   101: ldc -14
    //   103: aastore
    //   104: dup
    //   105: bipush 6
    //   107: ldc -12
    //   109: aastore
    //   110: ldc -10
    //   112: iconst_1
    //   113: anewarray 230	java/lang/String
    //   116: dup
    //   117: iconst_0
    //   118: aload_1
    //   119: aastore
    //   120: aconst_null
    //   121: aload_3
    //   122: invokevirtual 252	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/database/Cursor;
    //   125: astore_3
    //   126: aload 4
    //   128: astore_1
    //   129: aload_3
    //   130: ifnull +314 -> 444
    //   133: aload 4
    //   135: astore_1
    //   136: aload_3
    //   137: invokeinterface 257 1 0
    //   142: ifle +302 -> 444
    //   145: aload_3
    //   146: ldc -12
    //   148: invokeinterface 261 2 0
    //   153: istore 7
    //   155: new 137	java/util/ArrayList
    //   158: astore_0
    //   159: aload_0
    //   160: invokespecial 138	java/util/ArrayList:<init>	()V
    //   163: aload_3
    //   164: ldc -24
    //   166: invokeinterface 264 2 0
    //   171: istore 8
    //   173: aload_3
    //   174: ldc -22
    //   176: invokeinterface 261 2 0
    //   181: istore 9
    //   183: aload_3
    //   184: ldc -20
    //   186: invokeinterface 261 2 0
    //   191: istore 10
    //   193: aload_3
    //   194: ldc -18
    //   196: invokeinterface 261 2 0
    //   201: istore 11
    //   203: aload_3
    //   204: ldc -16
    //   206: invokeinterface 261 2 0
    //   211: istore 12
    //   213: aload_3
    //   214: ldc -14
    //   216: invokeinterface 261 2 0
    //   221: istore 13
    //   223: aload_0
    //   224: astore_1
    //   225: aload_3
    //   226: invokeinterface 267 1 0
    //   231: ifeq +213 -> 444
    //   234: iload 7
    //   236: iconst_m1
    //   237: if_icmpeq +16 -> 253
    //   240: aload_3
    //   241: iload 7
    //   243: invokeinterface 271 2 0
    //   248: istore 14
    //   250: goto +6 -> 256
    //   253: iconst_0
    //   254: istore 14
    //   256: iload 10
    //   258: iconst_m1
    //   259: if_icmpeq +16 -> 275
    //   262: aload_3
    //   263: iload 10
    //   265: invokeinterface 271 2 0
    //   270: istore 15
    //   272: goto +6 -> 278
    //   275: iconst_0
    //   276: istore 15
    //   278: iload 11
    //   280: iconst_m1
    //   281: if_icmpeq +15 -> 296
    //   284: aload_3
    //   285: iload 11
    //   287: invokeinterface 275 2 0
    //   292: astore_1
    //   293: goto +5 -> 298
    //   296: aconst_null
    //   297: astore_1
    //   298: iload 9
    //   300: iconst_m1
    //   301: if_icmpne +20 -> 321
    //   304: aload 5
    //   306: aload_3
    //   307: iload 8
    //   309: invokeinterface 279 2 0
    //   314: invokestatic 285	android/content/ContentUris:withAppendedId	(Landroid/net/Uri;J)Landroid/net/Uri;
    //   317: astore_2
    //   318: goto +17 -> 335
    //   321: aload 6
    //   323: aload_3
    //   324: iload 9
    //   326: invokeinterface 279 2 0
    //   331: invokestatic 285	android/content/ContentUris:withAppendedId	(Landroid/net/Uri;J)Landroid/net/Uri;
    //   334: astore_2
    //   335: iload 12
    //   337: iconst_m1
    //   338: if_icmpeq +43 -> 381
    //   341: iload 13
    //   343: iconst_m1
    //   344: if_icmpeq +37 -> 381
    //   347: aload_3
    //   348: iload 12
    //   350: invokeinterface 271 2 0
    //   355: istore 16
    //   357: aload_3
    //   358: iload 13
    //   360: invokeinterface 271 2 0
    //   365: iconst_1
    //   366: if_icmpne +9 -> 375
    //   369: iconst_1
    //   370: istore 17
    //   372: goto +6 -> 378
    //   375: iconst_0
    //   376: istore 17
    //   378: goto +11 -> 389
    //   381: sipush 400
    //   384: istore 16
    //   386: iconst_0
    //   387: istore 17
    //   389: aload_1
    //   390: invokestatic 291	android/graphics/fonts/FontVariationAxis:fromFontVariationSettings	(Ljava/lang/String;)[Landroid/graphics/fonts/FontVariationAxis;
    //   393: astore 4
    //   395: new 17	android/provider/FontsContract$FontInfo
    //   398: astore_1
    //   399: aload_1
    //   400: aload_2
    //   401: iload 15
    //   403: aload 4
    //   405: iload 16
    //   407: iload 17
    //   409: iload 14
    //   411: invokespecial 294	android/provider/FontsContract$FontInfo:<init>	(Landroid/net/Uri;I[Landroid/graphics/fonts/FontVariationAxis;IZI)V
    //   414: aload_0
    //   415: aload_1
    //   416: invokevirtual 295	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   419: pop
    //   420: goto -197 -> 223
    //   423: astore_1
    //   424: aconst_null
    //   425: astore_0
    //   426: goto +7 -> 433
    //   429: astore_0
    //   430: aload_0
    //   431: athrow
    //   432: astore_1
    //   433: aload_3
    //   434: ifnull +8 -> 442
    //   437: aload_0
    //   438: aload_3
    //   439: invokestatic 297	android/provider/FontsContract:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   442: aload_1
    //   443: athrow
    //   444: aload_3
    //   445: ifnull +8 -> 453
    //   448: aconst_null
    //   449: aload_3
    //   450: invokestatic 297	android/provider/FontsContract:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   453: aload_1
    //   454: iconst_0
    //   455: anewarray 17	android/provider/FontsContract$FontInfo
    //   458: invokevirtual 301	java/util/ArrayList:toArray	([Ljava/lang/Object;)[Ljava/lang/Object;
    //   461: checkcast 303	[Landroid/provider/FontsContract$FontInfo;
    //   464: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	465	0	paramContext	Context
    //   0	465	1	paramFontRequest	FontRequest
    //   0	465	2	paramString	String
    //   0	465	3	paramCancellationSignal	CancellationSignal
    //   7	397	4	localObject	Object
    //   28	277	5	localUri1	Uri
    //   54	268	6	localUri2	Uri
    //   153	89	7	i	int
    //   171	137	8	j	int
    //   181	144	9	k	int
    //   191	73	10	m	int
    //   201	85	11	n	int
    //   211	138	12	i1	int
    //   221	138	13	i2	int
    //   248	162	14	i3	int
    //   270	132	15	i4	int
    //   355	51	16	i5	int
    //   370	38	17	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   136	163	423	finally
    //   163	223	423	finally
    //   225	234	423	finally
    //   240	250	423	finally
    //   262	272	423	finally
    //   284	293	423	finally
    //   304	318	423	finally
    //   321	335	423	finally
    //   347	369	423	finally
    //   389	420	423	finally
    //   136	163	429	java/lang/Throwable
    //   163	223	429	java/lang/Throwable
    //   225	234	429	java/lang/Throwable
    //   240	250	429	java/lang/Throwable
    //   262	272	429	java/lang/Throwable
    //   284	293	429	java/lang/Throwable
    //   304	318	429	java/lang/Throwable
    //   321	335	429	java/lang/Throwable
    //   347	369	429	java/lang/Throwable
    //   389	420	429	java/lang/Throwable
    //   430	432	432	finally
  }
  
  /* Error */
  public static Typeface getFontSync(FontRequest paramFontRequest)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 311	android/provider/FontRequest:getIdentifier	()Ljava/lang/String;
    //   4: astore_1
    //   5: getstatic 84	android/provider/FontsContract:sTypefaceCache	Landroid/util/LruCache;
    //   8: aload_1
    //   9: invokevirtual 314	android/util/LruCache:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   12: checkcast 316	android/graphics/Typeface
    //   15: astore_2
    //   16: aload_2
    //   17: ifnull +5 -> 22
    //   20: aload_2
    //   21: areturn
    //   22: getstatic 77	android/provider/FontsContract:sLock	Ljava/lang/Object;
    //   25: astore_2
    //   26: aload_2
    //   27: monitorenter
    //   28: getstatic 107	android/provider/FontsContract:sHandler	Landroid/os/Handler;
    //   31: astore_3
    //   32: aload_3
    //   33: ifnonnull +51 -> 84
    //   36: new 318	android/os/HandlerThread
    //   39: astore_3
    //   40: aload_3
    //   41: ldc_w 320
    //   44: bipush 10
    //   46: invokespecial 323	android/os/HandlerThread:<init>	(Ljava/lang/String;I)V
    //   49: aload_3
    //   50: putstatic 101	android/provider/FontsContract:sThread	Landroid/os/HandlerThread;
    //   53: getstatic 101	android/provider/FontsContract:sThread	Landroid/os/HandlerThread;
    //   56: invokevirtual 326	android/os/HandlerThread:start	()V
    //   59: new 328	android/os/Handler
    //   62: astore_3
    //   63: aload_3
    //   64: getstatic 101	android/provider/FontsContract:sThread	Landroid/os/HandlerThread;
    //   67: invokevirtual 332	android/os/HandlerThread:getLooper	()Landroid/os/Looper;
    //   70: invokespecial 335	android/os/Handler:<init>	(Landroid/os/Looper;)V
    //   73: aload_3
    //   74: putstatic 107	android/provider/FontsContract:sHandler	Landroid/os/Handler;
    //   77: goto +7 -> 84
    //   80: astore_0
    //   81: goto +286 -> 367
    //   84: new 337	java/util/concurrent/locks/ReentrantLock
    //   87: astore_3
    //   88: aload_3
    //   89: invokespecial 338	java/util/concurrent/locks/ReentrantLock:<init>	()V
    //   92: aload_3
    //   93: invokeinterface 344 1 0
    //   98: astore 4
    //   100: new 346	java/util/concurrent/atomic/AtomicReference
    //   103: astore 5
    //   105: aload 5
    //   107: invokespecial 347	java/util/concurrent/atomic/AtomicReference:<init>	()V
    //   110: new 349	java/util/concurrent/atomic/AtomicBoolean
    //   113: astore 6
    //   115: aload 6
    //   117: iconst_1
    //   118: invokespecial 352	java/util/concurrent/atomic/AtomicBoolean:<init>	(Z)V
    //   121: new 349	java/util/concurrent/atomic/AtomicBoolean
    //   124: astore 7
    //   126: aload 7
    //   128: iconst_0
    //   129: invokespecial 352	java/util/concurrent/atomic/AtomicBoolean:<init>	(Z)V
    //   132: getstatic 107	android/provider/FontsContract:sHandler	Landroid/os/Handler;
    //   135: astore 8
    //   137: new 354	android/provider/_$$Lambda$FontsContract$rqfIZKvP1frnI9vP1hVA8jQN_RE
    //   140: astore 9
    //   142: aload 9
    //   144: aload_0
    //   145: aload_1
    //   146: aload 5
    //   148: aload_3
    //   149: aload 7
    //   151: aload 6
    //   153: aload 4
    //   155: invokespecial 357	android/provider/_$$Lambda$FontsContract$rqfIZKvP1frnI9vP1hVA8jQN_RE:<init>	(Landroid/provider/FontRequest;Ljava/lang/String;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/locks/Lock;Ljava/util/concurrent/atomic/AtomicBoolean;Ljava/util/concurrent/atomic/AtomicBoolean;Ljava/util/concurrent/locks/Condition;)V
    //   158: aload 8
    //   160: aload 9
    //   162: invokevirtual 361	android/os/Handler:post	(Ljava/lang/Runnable;)Z
    //   165: pop
    //   166: getstatic 107	android/provider/FontsContract:sHandler	Landroid/os/Handler;
    //   169: getstatic 87	android/provider/FontsContract:sReplaceDispatcherThreadRunnable	Ljava/lang/Runnable;
    //   172: invokevirtual 365	android/os/Handler:removeCallbacks	(Ljava/lang/Runnable;)V
    //   175: getstatic 107	android/provider/FontsContract:sHandler	Landroid/os/Handler;
    //   178: getstatic 87	android/provider/FontsContract:sReplaceDispatcherThreadRunnable	Ljava/lang/Runnable;
    //   181: ldc2_w 366
    //   184: invokevirtual 371	android/os/Handler:postDelayed	(Ljava/lang/Runnable;J)Z
    //   187: pop
    //   188: getstatic 377	java/util/concurrent/TimeUnit:MILLISECONDS	Ljava/util/concurrent/TimeUnit;
    //   191: ldc2_w 27
    //   194: invokevirtual 381	java/util/concurrent/TimeUnit:toNanos	(J)J
    //   197: lstore 10
    //   199: aload_3
    //   200: invokeinterface 384 1 0
    //   205: aload 6
    //   207: invokevirtual 386	java/util/concurrent/atomic/AtomicBoolean:get	()Z
    //   210: ifne +22 -> 232
    //   213: aload 5
    //   215: invokevirtual 388	java/util/concurrent/atomic/AtomicReference:get	()Ljava/lang/Object;
    //   218: checkcast 316	android/graphics/Typeface
    //   221: astore_0
    //   222: aload_3
    //   223: invokeinterface 391 1 0
    //   228: aload_2
    //   229: monitorexit
    //   230: aload_0
    //   231: areturn
    //   232: aload 4
    //   234: lload 10
    //   236: invokeinterface 396 3 0
    //   241: lstore 12
    //   243: goto +8 -> 251
    //   246: astore_1
    //   247: lload 10
    //   249: lstore 12
    //   251: aload 6
    //   253: invokevirtual 386	java/util/concurrent/atomic/AtomicBoolean:get	()Z
    //   256: ifne +22 -> 278
    //   259: aload 5
    //   261: invokevirtual 388	java/util/concurrent/atomic/AtomicReference:get	()Ljava/lang/Object;
    //   264: checkcast 316	android/graphics/Typeface
    //   267: astore_0
    //   268: aload_3
    //   269: invokeinterface 391 1 0
    //   274: aload_2
    //   275: monitorexit
    //   276: aload_0
    //   277: areturn
    //   278: lload 12
    //   280: lstore 10
    //   282: lload 12
    //   284: lconst_0
    //   285: lcmp
    //   286: ifgt -54 -> 232
    //   289: aload 7
    //   291: iconst_1
    //   292: invokevirtual 399	java/util/concurrent/atomic/AtomicBoolean:set	(Z)V
    //   295: new 401	java/lang/StringBuilder
    //   298: astore_1
    //   299: aload_1
    //   300: invokespecial 402	java/lang/StringBuilder:<init>	()V
    //   303: aload_1
    //   304: ldc_w 404
    //   307: invokevirtual 408	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   310: pop
    //   311: aload_1
    //   312: aload_0
    //   313: invokevirtual 411	android/provider/FontRequest:getProviderAuthority	()Ljava/lang/String;
    //   316: invokevirtual 408	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   319: pop
    //   320: aload_1
    //   321: ldc_w 413
    //   324: invokevirtual 408	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   327: pop
    //   328: aload_1
    //   329: aload_0
    //   330: invokevirtual 228	android/provider/FontRequest:getQuery	()Ljava/lang/String;
    //   333: invokevirtual 408	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   336: pop
    //   337: ldc 32
    //   339: aload_1
    //   340: invokevirtual 416	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   343: invokestatic 422	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   346: pop
    //   347: aload_3
    //   348: invokeinterface 391 1 0
    //   353: aload_2
    //   354: monitorexit
    //   355: aconst_null
    //   356: areturn
    //   357: astore_0
    //   358: aload_3
    //   359: invokeinterface 391 1 0
    //   364: aload_0
    //   365: athrow
    //   366: astore_0
    //   367: aload_2
    //   368: monitorexit
    //   369: aload_0
    //   370: athrow
    //   371: astore_0
    //   372: goto -5 -> 367
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	375	0	paramFontRequest	FontRequest
    //   4	142	1	str	String
    //   246	1	1	localInterruptedException	InterruptedException
    //   298	42	1	localStringBuilder	StringBuilder
    //   15	353	2	localObject1	Object
    //   31	328	3	localObject2	Object
    //   98	135	4	localCondition	java.util.concurrent.locks.Condition
    //   103	157	5	localAtomicReference	java.util.concurrent.atomic.AtomicReference
    //   113	139	6	localAtomicBoolean1	java.util.concurrent.atomic.AtomicBoolean
    //   124	166	7	localAtomicBoolean2	java.util.concurrent.atomic.AtomicBoolean
    //   135	24	8	localHandler	Handler
    //   140	21	9	localRqfIZKvP1frnI9vP1hVA8jQN_RE	_..Lambda.FontsContract.rqfIZKvP1frnI9vP1hVA8jQN_RE
    //   197	84	10	l1	long
    //   241	42	12	l2	long
    // Exception table:
    //   from	to	target	type
    //   36	77	80	finally
    //   232	243	246	java/lang/InterruptedException
    //   205	222	357	finally
    //   232	243	357	finally
    //   251	268	357	finally
    //   289	347	357	finally
    //   28	32	366	finally
    //   84	142	366	finally
    //   142	205	371	finally
    //   222	230	371	finally
    //   268	276	371	finally
    //   347	355	371	finally
    //   358	366	371	finally
    //   367	369	371	finally
  }
  
  @VisibleForTesting
  public static ProviderInfo getProvider(PackageManager paramPackageManager, FontRequest paramFontRequest)
    throws PackageManager.NameNotFoundException
  {
    Object localObject = paramFontRequest.getProviderAuthority();
    int i = 0;
    ProviderInfo localProviderInfo = paramPackageManager.resolveContentProvider((String)localObject, 0);
    if (localProviderInfo != null)
    {
      if (packageName.equals(paramFontRequest.getProviderPackage()))
      {
        if (applicationInfo.isSystemApp()) {
          return localProviderInfo;
        }
        paramPackageManager = convertToByteArrayList(getPackageInfopackageName, 64).signatures);
        Collections.sort(paramPackageManager, sByteArrayComparator);
        localObject = paramFontRequest.getCertificates();
        while (i < ((List)localObject).size())
        {
          paramFontRequest = new ArrayList((Collection)((List)localObject).get(i));
          Collections.sort(paramFontRequest, sByteArrayComparator);
          if (equalsByteArrayList(paramPackageManager, paramFontRequest)) {
            return localProviderInfo;
          }
          i++;
        }
        return null;
      }
      paramPackageManager = new StringBuilder();
      paramPackageManager.append("Found content provider ");
      paramPackageManager.append((String)localObject);
      paramPackageManager.append(", but package was not ");
      paramPackageManager.append(paramFontRequest.getProviderPackage());
      throw new PackageManager.NameNotFoundException(paramPackageManager.toString());
    }
    paramPackageManager = new StringBuilder();
    paramPackageManager.append("No package found for authority: ");
    paramPackageManager.append((String)localObject);
    throw new PackageManager.NameNotFoundException(paramPackageManager.toString());
  }
  
  /* Error */
  private static Map<Uri, java.nio.ByteBuffer> prepareFontData(Context paramContext, FontInfo[] paramArrayOfFontInfo, CancellationSignal paramCancellationSignal)
  {
    // Byte code:
    //   0: new 572	java/util/HashMap
    //   3: dup
    //   4: invokespecial 573	java/util/HashMap:<init>	()V
    //   7: astore_3
    //   8: aload_0
    //   9: invokevirtual 222	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   12: astore 4
    //   14: aload_1
    //   15: arraylength
    //   16: istore 5
    //   18: iconst_0
    //   19: istore 6
    //   21: iload 6
    //   23: iload 5
    //   25: if_icmpge +297 -> 322
    //   28: aload_1
    //   29: iload 6
    //   31: aaload
    //   32: astore_0
    //   33: aload_0
    //   34: invokevirtual 538	android/provider/FontsContract$FontInfo:getResultCode	()I
    //   37: ifeq +6 -> 43
    //   40: goto +276 -> 316
    //   43: aload_0
    //   44: invokevirtual 576	android/provider/FontsContract$FontInfo:getUri	()Landroid/net/Uri;
    //   47: astore 7
    //   49: aload_3
    //   50: aload 7
    //   52: invokevirtual 579	java/util/HashMap:containsKey	(Ljava/lang/Object;)Z
    //   55: ifeq +6 -> 61
    //   58: goto -18 -> 40
    //   61: aconst_null
    //   62: astore 8
    //   64: aconst_null
    //   65: astore 9
    //   67: aconst_null
    //   68: astore 10
    //   70: aconst_null
    //   71: astore_0
    //   72: aconst_null
    //   73: astore 11
    //   75: aconst_null
    //   76: astore 12
    //   78: aconst_null
    //   79: astore 13
    //   81: aload 4
    //   83: aload 7
    //   85: ldc_w 581
    //   88: aload_2
    //   89: invokevirtual 585	android/content/ContentResolver:openFileDescriptor	(Landroid/net/Uri;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/os/ParcelFileDescriptor;
    //   92: astore 14
    //   94: aload 14
    //   96: ifnull +188 -> 284
    //   99: aload 8
    //   101: astore 15
    //   103: aload 13
    //   105: astore 12
    //   107: aload 9
    //   109: astore 11
    //   111: aload 10
    //   113: astore 16
    //   115: new 587	java/io/FileInputStream
    //   118: astore 17
    //   120: aload 8
    //   122: astore 15
    //   124: aload 13
    //   126: astore 12
    //   128: aload 9
    //   130: astore 11
    //   132: aload 10
    //   134: astore 16
    //   136: aload 17
    //   138: aload 14
    //   140: invokevirtual 593	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   143: invokespecial 596	java/io/FileInputStream:<init>	(Ljava/io/FileDescriptor;)V
    //   146: aload 17
    //   148: invokevirtual 600	java/io/FileInputStream:getChannel	()Ljava/nio/channels/FileChannel;
    //   151: astore_0
    //   152: aload_0
    //   153: invokevirtual 605	java/nio/channels/FileChannel:size	()J
    //   156: lstore 18
    //   158: aload_0
    //   159: getstatic 611	java/nio/channels/FileChannel$MapMode:READ_ONLY	Ljava/nio/channels/FileChannel$MapMode;
    //   162: lconst_0
    //   163: lload 18
    //   165: invokevirtual 615	java/nio/channels/FileChannel:map	(Ljava/nio/channels/FileChannel$MapMode;JJ)Ljava/nio/MappedByteBuffer;
    //   168: astore_0
    //   169: aload 8
    //   171: astore 15
    //   173: aload_0
    //   174: astore 12
    //   176: aload_0
    //   177: astore 11
    //   179: aload_0
    //   180: astore 16
    //   182: aconst_null
    //   183: aload 17
    //   185: invokestatic 297	android/provider/FontsContract:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   188: goto +96 -> 284
    //   191: astore 20
    //   193: aconst_null
    //   194: astore_0
    //   195: goto +8 -> 203
    //   198: astore_0
    //   199: aload_0
    //   200: athrow
    //   201: astore 20
    //   203: aload 8
    //   205: astore 15
    //   207: aload 13
    //   209: astore 12
    //   211: aload 9
    //   213: astore 11
    //   215: aload 10
    //   217: astore 16
    //   219: aload_0
    //   220: aload 17
    //   222: invokestatic 297	android/provider/FontsContract:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   225: aload 8
    //   227: astore 15
    //   229: aload 13
    //   231: astore 12
    //   233: aload 9
    //   235: astore 11
    //   237: aload 10
    //   239: astore 16
    //   241: aload 20
    //   243: athrow
    //   244: astore_0
    //   245: goto +13 -> 258
    //   248: astore_0
    //   249: aload_0
    //   250: astore 15
    //   252: aload 11
    //   254: astore 12
    //   256: aload_0
    //   257: athrow
    //   258: aload 14
    //   260: ifnull +14 -> 274
    //   263: aload 12
    //   265: astore 11
    //   267: aload 15
    //   269: aload 14
    //   271: invokestatic 297	android/provider/FontsContract:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   274: aload 12
    //   276: astore 11
    //   278: aload_0
    //   279: athrow
    //   280: astore_0
    //   281: aload 16
    //   283: astore_0
    //   284: aload 14
    //   286: ifnull +12 -> 298
    //   289: aload_0
    //   290: astore 11
    //   292: aconst_null
    //   293: aload 14
    //   295: invokestatic 297	android/provider/FontsContract:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   298: goto +10 -> 308
    //   301: astore_0
    //   302: aload 11
    //   304: astore_0
    //   305: goto +3 -> 308
    //   308: aload_3
    //   309: aload 7
    //   311: aload_0
    //   312: invokevirtual 616	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   315: pop
    //   316: iinc 6 1
    //   319: goto -298 -> 21
    //   322: aload_3
    //   323: invokestatic 620	java/util/Collections:unmodifiableMap	(Ljava/util/Map;)Ljava/util/Map;
    //   326: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	327	0	paramContext	Context
    //   0	327	1	paramArrayOfFontInfo	FontInfo[]
    //   0	327	2	paramCancellationSignal	CancellationSignal
    //   7	316	3	localHashMap	java.util.HashMap
    //   12	70	4	localContentResolver	android.content.ContentResolver
    //   16	10	5	i	int
    //   19	298	6	j	int
    //   47	263	7	localUri	Uri
    //   62	164	8	localObject1	Object
    //   65	169	9	localObject2	Object
    //   68	170	10	localObject3	Object
    //   73	230	11	localObject4	Object
    //   76	199	12	localObject5	Object
    //   79	151	13	localObject6	Object
    //   92	202	14	localParcelFileDescriptor	android.os.ParcelFileDescriptor
    //   101	167	15	localObject7	Object
    //   113	169	16	localObject8	Object
    //   118	103	17	localFileInputStream	java.io.FileInputStream
    //   156	8	18	l	long
    //   191	1	20	localObject9	Object
    //   201	41	20	localObject10	Object
    // Exception table:
    //   from	to	target	type
    //   146	169	191	finally
    //   146	169	198	java/lang/Throwable
    //   199	201	201	finally
    //   115	120	244	finally
    //   136	146	244	finally
    //   182	188	244	finally
    //   219	225	244	finally
    //   241	244	244	finally
    //   256	258	244	finally
    //   115	120	248	java/lang/Throwable
    //   136	146	248	java/lang/Throwable
    //   182	188	248	java/lang/Throwable
    //   219	225	248	java/lang/Throwable
    //   241	244	248	java/lang/Throwable
    //   115	120	280	java/io/IOException
    //   136	146	280	java/io/IOException
    //   182	188	280	java/io/IOException
    //   219	225	280	java/io/IOException
    //   241	244	280	java/io/IOException
    //   81	94	301	java/io/IOException
    //   267	274	301	java/io/IOException
    //   278	280	301	java/io/IOException
    //   292	298	301	java/io/IOException
  }
  
  public static void requestFonts(Context paramContext, FontRequest paramFontRequest, Handler paramHandler, CancellationSignal paramCancellationSignal, FontRequestCallback paramFontRequestCallback)
  {
    Handler localHandler = new Handler();
    Typeface localTypeface = (Typeface)sTypefaceCache.get(paramFontRequest.getIdentifier());
    if (localTypeface != null)
    {
      localHandler.post(new _..Lambda.FontsContract.p_tsXYYYpEH0_EJSp2uPrJ33dkU(paramFontRequestCallback, localTypeface));
      return;
    }
    paramHandler.post(new _..Lambda.FontsContract.dFs2m4XF5xdir4W3T_ncUQAVX8k(paramContext, paramCancellationSignal, paramFontRequest, localHandler, paramFontRequestCallback));
  }
  
  public static void setApplicationContextForResources(Context paramContext)
  {
    sContext = paramContext.getApplicationContext();
  }
  
  public static final class Columns
    implements BaseColumns
  {
    public static final String FILE_ID = "file_id";
    public static final String ITALIC = "font_italic";
    public static final String RESULT_CODE = "result_code";
    public static final int RESULT_CODE_FONT_NOT_FOUND = 1;
    public static final int RESULT_CODE_FONT_UNAVAILABLE = 2;
    public static final int RESULT_CODE_MALFORMED_QUERY = 3;
    public static final int RESULT_CODE_OK = 0;
    public static final String TTC_INDEX = "font_ttc_index";
    public static final String VARIATION_SETTINGS = "font_variation_settings";
    public static final String WEIGHT = "font_weight";
    
    private Columns() {}
  }
  
  public static class FontFamilyResult
  {
    public static final int STATUS_OK = 0;
    public static final int STATUS_REJECTED = 3;
    public static final int STATUS_UNEXPECTED_DATA_PROVIDED = 2;
    public static final int STATUS_WRONG_CERTIFICATES = 1;
    private final FontsContract.FontInfo[] mFonts;
    private final int mStatusCode;
    
    public FontFamilyResult(int paramInt, FontsContract.FontInfo[] paramArrayOfFontInfo)
    {
      mStatusCode = paramInt;
      mFonts = paramArrayOfFontInfo;
    }
    
    public FontsContract.FontInfo[] getFonts()
    {
      return mFonts;
    }
    
    public int getStatusCode()
    {
      return mStatusCode;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    static @interface FontResultStatus {}
  }
  
  public static class FontInfo
  {
    private final FontVariationAxis[] mAxes;
    private final boolean mItalic;
    private final int mResultCode;
    private final int mTtcIndex;
    private final Uri mUri;
    private final int mWeight;
    
    public FontInfo(Uri paramUri, int paramInt1, FontVariationAxis[] paramArrayOfFontVariationAxis, int paramInt2, boolean paramBoolean, int paramInt3)
    {
      mUri = ((Uri)Preconditions.checkNotNull(paramUri));
      mTtcIndex = paramInt1;
      mAxes = paramArrayOfFontVariationAxis;
      mWeight = paramInt2;
      mItalic = paramBoolean;
      mResultCode = paramInt3;
    }
    
    public FontVariationAxis[] getAxes()
    {
      return mAxes;
    }
    
    public int getResultCode()
    {
      return mResultCode;
    }
    
    public int getTtcIndex()
    {
      return mTtcIndex;
    }
    
    public Uri getUri()
    {
      return mUri;
    }
    
    public int getWeight()
    {
      return mWeight;
    }
    
    public boolean isItalic()
    {
      return mItalic;
    }
  }
  
  public static class FontRequestCallback
  {
    public static final int FAIL_REASON_FONT_LOAD_ERROR = -3;
    public static final int FAIL_REASON_FONT_NOT_FOUND = 1;
    public static final int FAIL_REASON_FONT_UNAVAILABLE = 2;
    public static final int FAIL_REASON_MALFORMED_QUERY = 3;
    public static final int FAIL_REASON_PROVIDER_NOT_FOUND = -1;
    public static final int FAIL_REASON_WRONG_CERTIFICATES = -2;
    
    public FontRequestCallback() {}
    
    public void onTypefaceRequestFailed(int paramInt) {}
    
    public void onTypefaceRetrieved(Typeface paramTypeface) {}
    
    @Retention(RetentionPolicy.SOURCE)
    static @interface FontRequestFailReason {}
  }
}
