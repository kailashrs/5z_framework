package com.android.internal.telephony.cat;

public class ImageDescriptor
{
  static final int CODING_SCHEME_BASIC = 17;
  static final int CODING_SCHEME_COLOUR = 33;
  int mCodingScheme = 0;
  int mHeight = 0;
  int mHighOffset = 0;
  int mImageId = 0;
  int mLength = 0;
  int mLowOffset = 0;
  int mWidth = 0;
  
  ImageDescriptor() {}
  
  /* Error */
  static ImageDescriptor parse(byte[] paramArrayOfByte, int paramInt)
  {
    // Byte code:
    //   0: new 2	com/android/internal/telephony/cat/ImageDescriptor
    //   3: dup
    //   4: invokespecial 40	com/android/internal/telephony/cat/ImageDescriptor:<init>	()V
    //   7: astore_2
    //   8: iload_1
    //   9: iconst_1
    //   10: iadd
    //   11: istore_3
    //   12: iload_3
    //   13: istore 4
    //   15: aload_2
    //   16: aload_0
    //   17: iload_1
    //   18: baload
    //   19: sipush 255
    //   22: iand
    //   23: putfield 22	com/android/internal/telephony/cat/ImageDescriptor:mWidth	I
    //   26: iload_3
    //   27: iconst_1
    //   28: iadd
    //   29: istore 5
    //   31: iload 5
    //   33: istore_1
    //   34: aload_2
    //   35: aload_0
    //   36: iload_3
    //   37: baload
    //   38: sipush 255
    //   41: iand
    //   42: putfield 24	com/android/internal/telephony/cat/ImageDescriptor:mHeight	I
    //   45: iload 5
    //   47: iconst_1
    //   48: iadd
    //   49: istore_3
    //   50: iload_3
    //   51: istore 4
    //   53: aload_2
    //   54: aload_0
    //   55: iload 5
    //   57: baload
    //   58: sipush 255
    //   61: iand
    //   62: putfield 26	com/android/internal/telephony/cat/ImageDescriptor:mCodingScheme	I
    //   65: iload_3
    //   66: iconst_1
    //   67: iadd
    //   68: istore 4
    //   70: iload 4
    //   72: istore_1
    //   73: aload_2
    //   74: aload_0
    //   75: iload_3
    //   76: baload
    //   77: sipush 255
    //   80: iand
    //   81: bipush 8
    //   83: ishl
    //   84: putfield 28	com/android/internal/telephony/cat/ImageDescriptor:mImageId	I
    //   87: iload 4
    //   89: istore_1
    //   90: aload_2
    //   91: getfield 28	com/android/internal/telephony/cat/ImageDescriptor:mImageId	I
    //   94: istore 5
    //   96: iload 4
    //   98: iconst_1
    //   99: iadd
    //   100: istore_1
    //   101: aload_2
    //   102: aload_0
    //   103: iload 4
    //   105: baload
    //   106: sipush 255
    //   109: iand
    //   110: iload 5
    //   112: ior
    //   113: putfield 28	com/android/internal/telephony/cat/ImageDescriptor:mImageId	I
    //   116: iload_1
    //   117: iconst_1
    //   118: iadd
    //   119: istore 5
    //   121: iload 5
    //   123: istore 4
    //   125: aload_2
    //   126: aload_0
    //   127: iload_1
    //   128: baload
    //   129: sipush 255
    //   132: iand
    //   133: putfield 30	com/android/internal/telephony/cat/ImageDescriptor:mHighOffset	I
    //   136: iload 5
    //   138: iconst_1
    //   139: iadd
    //   140: istore 4
    //   142: iload 4
    //   144: istore_1
    //   145: aload_2
    //   146: aload_0
    //   147: iload 5
    //   149: baload
    //   150: sipush 255
    //   153: iand
    //   154: putfield 32	com/android/internal/telephony/cat/ImageDescriptor:mLowOffset	I
    //   157: iload 4
    //   159: iconst_1
    //   160: iadd
    //   161: istore_1
    //   162: aload_0
    //   163: iload 4
    //   165: baload
    //   166: istore 4
    //   168: aload_2
    //   169: iload 4
    //   171: sipush 255
    //   174: iand
    //   175: bipush 8
    //   177: ishl
    //   178: aload_0
    //   179: iload_1
    //   180: baload
    //   181: sipush 255
    //   184: iand
    //   185: ior
    //   186: putfield 34	com/android/internal/telephony/cat/ImageDescriptor:mLength	I
    //   189: new 42	java/lang/StringBuilder
    //   192: astore_0
    //   193: aload_0
    //   194: invokespecial 43	java/lang/StringBuilder:<init>	()V
    //   197: aload_0
    //   198: ldc 45
    //   200: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   203: pop
    //   204: aload_0
    //   205: aload_2
    //   206: getfield 22	com/android/internal/telephony/cat/ImageDescriptor:mWidth	I
    //   209: invokevirtual 52	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   212: pop
    //   213: aload_0
    //   214: ldc 54
    //   216: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   219: pop
    //   220: aload_0
    //   221: aload_2
    //   222: getfield 24	com/android/internal/telephony/cat/ImageDescriptor:mHeight	I
    //   225: invokevirtual 52	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   228: pop
    //   229: aload_0
    //   230: ldc 54
    //   232: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   235: pop
    //   236: aload_0
    //   237: aload_2
    //   238: getfield 26	com/android/internal/telephony/cat/ImageDescriptor:mCodingScheme	I
    //   241: invokevirtual 52	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   244: pop
    //   245: aload_0
    //   246: ldc 56
    //   248: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   251: pop
    //   252: aload_0
    //   253: aload_2
    //   254: getfield 28	com/android/internal/telephony/cat/ImageDescriptor:mImageId	I
    //   257: invokestatic 62	java/lang/Integer:toHexString	(I)Ljava/lang/String;
    //   260: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   263: pop
    //   264: aload_0
    //   265: ldc 54
    //   267: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   270: pop
    //   271: aload_0
    //   272: aload_2
    //   273: getfield 30	com/android/internal/telephony/cat/ImageDescriptor:mHighOffset	I
    //   276: invokevirtual 52	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   279: pop
    //   280: aload_0
    //   281: ldc 54
    //   283: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   286: pop
    //   287: aload_0
    //   288: aload_2
    //   289: getfield 32	com/android/internal/telephony/cat/ImageDescriptor:mLowOffset	I
    //   292: invokevirtual 52	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   295: pop
    //   296: aload_0
    //   297: ldc 54
    //   299: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   302: pop
    //   303: aload_0
    //   304: aload_2
    //   305: getfield 34	com/android/internal/telephony/cat/ImageDescriptor:mLength	I
    //   308: invokevirtual 52	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   311: pop
    //   312: ldc 64
    //   314: aload_0
    //   315: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   318: invokestatic 74	com/android/internal/telephony/cat/CatLog:d	(Ljava/lang/String;Ljava/lang/String;)V
    //   321: aload_2
    //   322: astore_0
    //   323: goto +21 -> 344
    //   326: astore_0
    //   327: goto +8 -> 335
    //   330: astore_0
    //   331: goto +4 -> 335
    //   334: astore_0
    //   335: ldc 64
    //   337: ldc 76
    //   339: invokestatic 74	com/android/internal/telephony/cat/CatLog:d	(Ljava/lang/String;Ljava/lang/String;)V
    //   342: aconst_null
    //   343: astore_0
    //   344: aload_0
    //   345: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	346	0	paramArrayOfByte	byte[]
    //   0	346	1	paramInt	int
    //   7	315	2	localImageDescriptor	ImageDescriptor
    //   11	65	3	i	int
    //   13	162	4	j	int
    //   29	119	5	k	int
    // Exception table:
    //   from	to	target	type
    //   101	116	326	java/lang/IndexOutOfBoundsException
    //   168	321	326	java/lang/IndexOutOfBoundsException
    //   34	45	330	java/lang/IndexOutOfBoundsException
    //   73	87	330	java/lang/IndexOutOfBoundsException
    //   90	96	330	java/lang/IndexOutOfBoundsException
    //   145	157	330	java/lang/IndexOutOfBoundsException
    //   15	26	334	java/lang/IndexOutOfBoundsException
    //   53	65	334	java/lang/IndexOutOfBoundsException
    //   125	136	334	java/lang/IndexOutOfBoundsException
  }
}
