package android.os;

class CommonTimeUtils
{
  public static final int ERROR = -1;
  public static final int ERROR_BAD_VALUE = -4;
  public static final int ERROR_DEAD_OBJECT = -7;
  public static final int SUCCESS = 0;
  private String mInterfaceDesc;
  private IBinder mRemote;
  
  public CommonTimeUtils(IBinder paramIBinder, String paramString)
  {
    mRemote = paramIBinder;
    mInterfaceDesc = paramString;
  }
  
  public int transactGetInt(int paramInt1, int paramInt2)
    throws RemoteException
  {
    Parcel localParcel1 = Parcel.obtain();
    Parcel localParcel2 = Parcel.obtain();
    try
    {
      localParcel1.writeInterfaceToken(mInterfaceDesc);
      mRemote.transact(paramInt1, localParcel1, localParcel2, 0);
      if (localParcel2.readInt() == 0) {
        paramInt1 = localParcel2.readInt();
      } else {
        paramInt1 = paramInt2;
      }
      return paramInt1;
    }
    finally
    {
      localParcel2.recycle();
      localParcel1.recycle();
    }
  }
  
  public long transactGetLong(int paramInt, long paramLong)
    throws RemoteException
  {
    Parcel localParcel1 = Parcel.obtain();
    Parcel localParcel2 = Parcel.obtain();
    try
    {
      localParcel1.writeInterfaceToken(mInterfaceDesc);
      mRemote.transact(paramInt, localParcel1, localParcel2, 0);
      if (localParcel2.readInt() == 0) {
        paramLong = localParcel2.readLong();
      }
      return paramLong;
    }
    finally
    {
      localParcel2.recycle();
      localParcel1.recycle();
    }
  }
  
  /* Error */
  public java.net.InetSocketAddress transactGetSockaddr(int paramInt)
    throws RemoteException
  {
    // Byte code:
    //   0: invokestatic 37	android/os/Parcel:obtain	()Landroid/os/Parcel;
    //   3: astore_2
    //   4: invokestatic 37	android/os/Parcel:obtain	()Landroid/os/Parcel;
    //   7: astore_3
    //   8: aconst_null
    //   9: astore 4
    //   11: aload_2
    //   12: aload_0
    //   13: getfield 26	android/os/CommonTimeUtils:mInterfaceDesc	Ljava/lang/String;
    //   16: invokevirtual 41	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
    //   19: aload_0
    //   20: getfield 24	android/os/CommonTimeUtils:mRemote	Landroid/os/IBinder;
    //   23: astore 5
    //   25: aload 5
    //   27: iload_1
    //   28: aload_2
    //   29: aload_3
    //   30: iconst_0
    //   31: invokeinterface 47 5 0
    //   36: pop
    //   37: aload 4
    //   39: astore 6
    //   41: aload_3
    //   42: invokevirtual 51	android/os/Parcel:readInt	()I
    //   45: ifne +295 -> 340
    //   48: iconst_0
    //   49: istore_1
    //   50: aconst_null
    //   51: astore 5
    //   53: aload_3
    //   54: invokevirtual 51	android/os/Parcel:readInt	()I
    //   57: istore 7
    //   59: getstatic 68	android/system/OsConstants:AF_INET	I
    //   62: iload 7
    //   64: if_icmpne +88 -> 152
    //   67: aload_3
    //   68: invokevirtual 51	android/os/Parcel:readInt	()I
    //   71: istore 7
    //   73: aload_3
    //   74: invokevirtual 51	android/os/Parcel:readInt	()I
    //   77: istore_1
    //   78: getstatic 74	java/util/Locale:US	Ljava/util/Locale;
    //   81: ldc 76
    //   83: iconst_4
    //   84: anewarray 4	java/lang/Object
    //   87: dup
    //   88: iconst_0
    //   89: iload 7
    //   91: bipush 24
    //   93: ishr
    //   94: sipush 255
    //   97: iand
    //   98: invokestatic 82	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   101: aastore
    //   102: dup
    //   103: iconst_1
    //   104: iload 7
    //   106: bipush 16
    //   108: ishr
    //   109: sipush 255
    //   112: iand
    //   113: invokestatic 82	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   116: aastore
    //   117: dup
    //   118: iconst_2
    //   119: iload 7
    //   121: bipush 8
    //   123: ishr
    //   124: sipush 255
    //   127: iand
    //   128: invokestatic 82	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   131: aastore
    //   132: dup
    //   133: iconst_3
    //   134: iload 7
    //   136: sipush 255
    //   139: iand
    //   140: invokestatic 82	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   143: aastore
    //   144: invokestatic 88	java/lang/String:format	(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   147: astore 5
    //   149: goto +170 -> 319
    //   152: getstatic 91	android/system/OsConstants:AF_INET6	I
    //   155: iload 7
    //   157: if_icmpne +162 -> 319
    //   160: aload_3
    //   161: invokevirtual 51	android/os/Parcel:readInt	()I
    //   164: istore 8
    //   166: aload_3
    //   167: invokevirtual 51	android/os/Parcel:readInt	()I
    //   170: istore 9
    //   172: aload_3
    //   173: invokevirtual 51	android/os/Parcel:readInt	()I
    //   176: istore 7
    //   178: aload_3
    //   179: invokevirtual 51	android/os/Parcel:readInt	()I
    //   182: istore 10
    //   184: aload_3
    //   185: invokevirtual 51	android/os/Parcel:readInt	()I
    //   188: istore_1
    //   189: aload_3
    //   190: invokevirtual 51	android/os/Parcel:readInt	()I
    //   193: pop
    //   194: aload_3
    //   195: invokevirtual 51	android/os/Parcel:readInt	()I
    //   198: pop
    //   199: getstatic 74	java/util/Locale:US	Ljava/util/Locale;
    //   202: ldc 93
    //   204: bipush 8
    //   206: anewarray 4	java/lang/Object
    //   209: dup
    //   210: iconst_0
    //   211: iload 8
    //   213: bipush 16
    //   215: ishr
    //   216: ldc 94
    //   218: iand
    //   219: invokestatic 82	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   222: aastore
    //   223: dup
    //   224: iconst_1
    //   225: iload 8
    //   227: ldc 94
    //   229: iand
    //   230: invokestatic 82	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   233: aastore
    //   234: dup
    //   235: iconst_2
    //   236: iload 9
    //   238: bipush 16
    //   240: ishr
    //   241: ldc 94
    //   243: iand
    //   244: invokestatic 82	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   247: aastore
    //   248: dup
    //   249: iconst_3
    //   250: iload 9
    //   252: ldc 94
    //   254: iand
    //   255: invokestatic 82	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   258: aastore
    //   259: dup
    //   260: iconst_4
    //   261: iload 7
    //   263: bipush 16
    //   265: ishr
    //   266: ldc 94
    //   268: iand
    //   269: invokestatic 82	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   272: aastore
    //   273: dup
    //   274: iconst_5
    //   275: iload 7
    //   277: ldc 94
    //   279: iand
    //   280: invokestatic 82	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   283: aastore
    //   284: dup
    //   285: bipush 6
    //   287: iload 10
    //   289: bipush 16
    //   291: ishr
    //   292: ldc 94
    //   294: iand
    //   295: invokestatic 82	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   298: aastore
    //   299: dup
    //   300: bipush 7
    //   302: iload 10
    //   304: ldc 94
    //   306: iand
    //   307: invokestatic 82	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   310: aastore
    //   311: invokestatic 88	java/lang/String:format	(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   314: astore 5
    //   316: goto +3 -> 319
    //   319: aload 4
    //   321: astore 6
    //   323: aload 5
    //   325: ifnull +15 -> 340
    //   328: new 96	java/net/InetSocketAddress
    //   331: dup
    //   332: aload 5
    //   334: iload_1
    //   335: invokespecial 99	java/net/InetSocketAddress:<init>	(Ljava/lang/String;I)V
    //   338: astore 6
    //   340: aload_3
    //   341: invokevirtual 54	android/os/Parcel:recycle	()V
    //   344: aload_2
    //   345: invokevirtual 54	android/os/Parcel:recycle	()V
    //   348: aload 6
    //   350: areturn
    //   351: astore 5
    //   353: goto +5 -> 358
    //   356: astore 5
    //   358: aload_3
    //   359: invokevirtual 54	android/os/Parcel:recycle	()V
    //   362: aload_2
    //   363: invokevirtual 54	android/os/Parcel:recycle	()V
    //   366: aload 5
    //   368: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	369	0	this	CommonTimeUtils
    //   0	369	1	paramInt	int
    //   3	360	2	localParcel1	Parcel
    //   7	352	3	localParcel2	Parcel
    //   9	311	4	localObject1	Object
    //   23	310	5	localObject2	Object
    //   351	1	5	localObject3	Object
    //   356	11	5	localObject4	Object
    //   39	310	6	localObject5	Object
    //   57	223	7	i	int
    //   164	66	8	j	int
    //   170	85	9	k	int
    //   182	125	10	m	int
    // Exception table:
    //   from	to	target	type
    //   25	37	351	finally
    //   41	48	351	finally
    //   53	149	351	finally
    //   152	316	351	finally
    //   328	340	351	finally
    //   11	25	356	finally
  }
  
  public String transactGetString(int paramInt, String paramString)
    throws RemoteException
  {
    Parcel localParcel1 = Parcel.obtain();
    Parcel localParcel2 = Parcel.obtain();
    try
    {
      localParcel1.writeInterfaceToken(mInterfaceDesc);
      mRemote.transact(paramInt, localParcel1, localParcel2, 0);
      if (localParcel2.readInt() == 0) {
        paramString = localParcel2.readString();
      }
      return paramString;
    }
    finally
    {
      localParcel2.recycle();
      localParcel1.recycle();
    }
  }
  
  /* Error */
  public int transactSetInt(int paramInt1, int paramInt2)
  {
    // Byte code:
    //   0: invokestatic 37	android/os/Parcel:obtain	()Landroid/os/Parcel;
    //   3: astore_3
    //   4: invokestatic 37	android/os/Parcel:obtain	()Landroid/os/Parcel;
    //   7: astore 4
    //   9: aload_3
    //   10: aload_0
    //   11: getfield 26	android/os/CommonTimeUtils:mInterfaceDesc	Ljava/lang/String;
    //   14: invokevirtual 41	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
    //   17: aload_3
    //   18: iload_2
    //   19: invokevirtual 110	android/os/Parcel:writeInt	(I)V
    //   22: aload_0
    //   23: getfield 24	android/os/CommonTimeUtils:mRemote	Landroid/os/IBinder;
    //   26: iload_1
    //   27: aload_3
    //   28: aload 4
    //   30: iconst_0
    //   31: invokeinterface 47 5 0
    //   36: pop
    //   37: aload 4
    //   39: invokevirtual 51	android/os/Parcel:readInt	()I
    //   42: istore_1
    //   43: aload 4
    //   45: invokevirtual 54	android/os/Parcel:recycle	()V
    //   48: aload_3
    //   49: invokevirtual 54	android/os/Parcel:recycle	()V
    //   52: iload_1
    //   53: ireturn
    //   54: astore 5
    //   56: aload 4
    //   58: invokevirtual 54	android/os/Parcel:recycle	()V
    //   61: aload_3
    //   62: invokevirtual 54	android/os/Parcel:recycle	()V
    //   65: aload 5
    //   67: athrow
    //   68: astore 5
    //   70: aload 4
    //   72: invokevirtual 54	android/os/Parcel:recycle	()V
    //   75: aload_3
    //   76: invokevirtual 54	android/os/Parcel:recycle	()V
    //   79: bipush -7
    //   81: ireturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	82	0	this	CommonTimeUtils
    //   0	82	1	paramInt1	int
    //   0	82	2	paramInt2	int
    //   3	73	3	localParcel1	Parcel
    //   7	64	4	localParcel2	Parcel
    //   54	12	5	localObject	Object
    //   68	1	5	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   9	43	54	finally
    //   9	43	68	android/os/RemoteException
  }
  
  /* Error */
  public int transactSetLong(int paramInt, long paramLong)
  {
    // Byte code:
    //   0: invokestatic 37	android/os/Parcel:obtain	()Landroid/os/Parcel;
    //   3: astore 4
    //   5: invokestatic 37	android/os/Parcel:obtain	()Landroid/os/Parcel;
    //   8: astore 5
    //   10: aload 4
    //   12: aload_0
    //   13: getfield 26	android/os/CommonTimeUtils:mInterfaceDesc	Ljava/lang/String;
    //   16: invokevirtual 41	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
    //   19: aload 4
    //   21: lload_2
    //   22: invokevirtual 116	android/os/Parcel:writeLong	(J)V
    //   25: aload_0
    //   26: getfield 24	android/os/CommonTimeUtils:mRemote	Landroid/os/IBinder;
    //   29: iload_1
    //   30: aload 4
    //   32: aload 5
    //   34: iconst_0
    //   35: invokeinterface 47 5 0
    //   40: pop
    //   41: aload 5
    //   43: invokevirtual 51	android/os/Parcel:readInt	()I
    //   46: istore_1
    //   47: aload 5
    //   49: invokevirtual 54	android/os/Parcel:recycle	()V
    //   52: aload 4
    //   54: invokevirtual 54	android/os/Parcel:recycle	()V
    //   57: iload_1
    //   58: ireturn
    //   59: astore 6
    //   61: aload 5
    //   63: invokevirtual 54	android/os/Parcel:recycle	()V
    //   66: aload 4
    //   68: invokevirtual 54	android/os/Parcel:recycle	()V
    //   71: aload 6
    //   73: athrow
    //   74: astore 6
    //   76: aload 5
    //   78: invokevirtual 54	android/os/Parcel:recycle	()V
    //   81: aload 4
    //   83: invokevirtual 54	android/os/Parcel:recycle	()V
    //   86: bipush -7
    //   88: ireturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	89	0	this	CommonTimeUtils
    //   0	89	1	paramInt	int
    //   0	89	2	paramLong	long
    //   3	79	4	localParcel1	Parcel
    //   8	69	5	localParcel2	Parcel
    //   59	13	6	localObject	Object
    //   74	1	6	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   10	47	59	finally
    //   10	47	74	android/os/RemoteException
  }
  
  /* Error */
  public int transactSetSockaddr(int paramInt, java.net.InetSocketAddress paramInetSocketAddress)
  {
    // Byte code:
    //   0: invokestatic 37	android/os/Parcel:obtain	()Landroid/os/Parcel;
    //   3: astore_3
    //   4: invokestatic 37	android/os/Parcel:obtain	()Landroid/os/Parcel;
    //   7: astore 4
    //   9: aload_3
    //   10: aload_0
    //   11: getfield 26	android/os/CommonTimeUtils:mInterfaceDesc	Ljava/lang/String;
    //   14: invokevirtual 41	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
    //   17: aload_2
    //   18: ifnonnull +11 -> 29
    //   21: aload_3
    //   22: iconst_0
    //   23: invokevirtual 110	android/os/Parcel:writeInt	(I)V
    //   26: goto +238 -> 264
    //   29: aload_3
    //   30: iconst_1
    //   31: invokevirtual 110	android/os/Parcel:writeInt	(I)V
    //   34: aload_2
    //   35: invokevirtual 122	java/net/InetSocketAddress:getAddress	()Ljava/net/InetAddress;
    //   38: astore 5
    //   40: aload 5
    //   42: invokevirtual 127	java/net/InetAddress:getAddress	()[B
    //   45: astore 6
    //   47: aload_2
    //   48: invokevirtual 130	java/net/InetSocketAddress:getPort	()I
    //   51: istore 7
    //   53: aload 5
    //   55: instanceof 132
    //   58: ifeq +83 -> 141
    //   61: aload 6
    //   63: iconst_0
    //   64: baload
    //   65: istore 8
    //   67: aload 6
    //   69: iconst_1
    //   70: baload
    //   71: istore 9
    //   73: aload 6
    //   75: iconst_2
    //   76: baload
    //   77: istore 10
    //   79: aload 6
    //   81: iconst_3
    //   82: baload
    //   83: istore 11
    //   85: aload_3
    //   86: getstatic 68	android/system/OsConstants:AF_INET	I
    //   89: invokevirtual 110	android/os/Parcel:writeInt	(I)V
    //   92: aload_3
    //   93: iload 9
    //   95: sipush 255
    //   98: iand
    //   99: bipush 16
    //   101: ishl
    //   102: iload 8
    //   104: sipush 255
    //   107: iand
    //   108: bipush 24
    //   110: ishl
    //   111: ior
    //   112: iload 10
    //   114: sipush 255
    //   117: iand
    //   118: bipush 8
    //   120: ishl
    //   121: ior
    //   122: iload 11
    //   124: sipush 255
    //   127: iand
    //   128: ior
    //   129: invokevirtual 110	android/os/Parcel:writeInt	(I)V
    //   132: aload_3
    //   133: iload 7
    //   135: invokevirtual 110	android/os/Parcel:writeInt	(I)V
    //   138: goto +126 -> 264
    //   141: aload 5
    //   143: instanceof 134
    //   146: ifeq +152 -> 298
    //   149: aload 5
    //   151: checkcast 134	java/net/Inet6Address
    //   154: astore_2
    //   155: aload_3
    //   156: getstatic 91	android/system/OsConstants:AF_INET6	I
    //   159: invokevirtual 110	android/os/Parcel:writeInt	(I)V
    //   162: iconst_0
    //   163: istore 9
    //   165: iload 9
    //   167: iconst_4
    //   168: if_icmpge +77 -> 245
    //   171: aload_3
    //   172: aload 6
    //   174: iload 9
    //   176: iconst_4
    //   177: imul
    //   178: iconst_0
    //   179: iadd
    //   180: baload
    //   181: sipush 255
    //   184: iand
    //   185: bipush 24
    //   187: ishl
    //   188: aload 6
    //   190: iload 9
    //   192: iconst_4
    //   193: imul
    //   194: iconst_1
    //   195: iadd
    //   196: baload
    //   197: sipush 255
    //   200: iand
    //   201: bipush 16
    //   203: ishl
    //   204: ior
    //   205: aload 6
    //   207: iload 9
    //   209: iconst_4
    //   210: imul
    //   211: iconst_2
    //   212: iadd
    //   213: baload
    //   214: sipush 255
    //   217: iand
    //   218: bipush 8
    //   220: ishl
    //   221: ior
    //   222: aload 6
    //   224: iload 9
    //   226: iconst_4
    //   227: imul
    //   228: iconst_3
    //   229: iadd
    //   230: baload
    //   231: sipush 255
    //   234: iand
    //   235: ior
    //   236: invokevirtual 110	android/os/Parcel:writeInt	(I)V
    //   239: iinc 9 1
    //   242: goto -77 -> 165
    //   245: aload_3
    //   246: iload 7
    //   248: invokevirtual 110	android/os/Parcel:writeInt	(I)V
    //   251: aload_3
    //   252: iconst_0
    //   253: invokevirtual 110	android/os/Parcel:writeInt	(I)V
    //   256: aload_3
    //   257: aload_2
    //   258: invokevirtual 137	java/net/Inet6Address:getScopeId	()I
    //   261: invokevirtual 110	android/os/Parcel:writeInt	(I)V
    //   264: aload_0
    //   265: getfield 24	android/os/CommonTimeUtils:mRemote	Landroid/os/IBinder;
    //   268: astore_2
    //   269: aload_2
    //   270: iload_1
    //   271: aload_3
    //   272: aload 4
    //   274: iconst_0
    //   275: invokeinterface 47 5 0
    //   280: pop
    //   281: aload 4
    //   283: invokevirtual 51	android/os/Parcel:readInt	()I
    //   286: istore_1
    //   287: goto +39 -> 326
    //   290: astore_2
    //   291: goto +20 -> 311
    //   294: astore_2
    //   295: goto +28 -> 323
    //   298: aload 4
    //   300: invokevirtual 54	android/os/Parcel:recycle	()V
    //   303: aload_3
    //   304: invokevirtual 54	android/os/Parcel:recycle	()V
    //   307: bipush -4
    //   309: ireturn
    //   310: astore_2
    //   311: aload 4
    //   313: invokevirtual 54	android/os/Parcel:recycle	()V
    //   316: aload_3
    //   317: invokevirtual 54	android/os/Parcel:recycle	()V
    //   320: aload_2
    //   321: athrow
    //   322: astore_2
    //   323: bipush -7
    //   325: istore_1
    //   326: aload 4
    //   328: invokevirtual 54	android/os/Parcel:recycle	()V
    //   331: aload_3
    //   332: invokevirtual 54	android/os/Parcel:recycle	()V
    //   335: iload_1
    //   336: ireturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	337	0	this	CommonTimeUtils
    //   0	337	1	paramInt	int
    //   0	337	2	paramInetSocketAddress	java.net.InetSocketAddress
    //   3	329	3	localParcel1	Parcel
    //   7	320	4	localParcel2	Parcel
    //   38	112	5	localInetAddress	java.net.InetAddress
    //   45	178	6	arrayOfByte	byte[]
    //   51	196	7	i	int
    //   65	43	8	j	int
    //   71	169	9	k	int
    //   77	41	10	m	int
    //   83	45	11	n	int
    // Exception table:
    //   from	to	target	type
    //   269	287	290	finally
    //   269	287	294	android/os/RemoteException
    //   9	17	310	finally
    //   21	26	310	finally
    //   29	61	310	finally
    //   85	138	310	finally
    //   141	162	310	finally
    //   171	239	310	finally
    //   245	264	310	finally
    //   264	269	310	finally
    //   9	17	322	android/os/RemoteException
    //   21	26	322	android/os/RemoteException
    //   29	61	322	android/os/RemoteException
    //   85	138	322	android/os/RemoteException
    //   141	162	322	android/os/RemoteException
    //   171	239	322	android/os/RemoteException
    //   245	264	322	android/os/RemoteException
    //   264	269	322	android/os/RemoteException
  }
  
  /* Error */
  public int transactSetString(int paramInt, String paramString)
  {
    // Byte code:
    //   0: invokestatic 37	android/os/Parcel:obtain	()Landroid/os/Parcel;
    //   3: astore_3
    //   4: invokestatic 37	android/os/Parcel:obtain	()Landroid/os/Parcel;
    //   7: astore 4
    //   9: aload_3
    //   10: aload_0
    //   11: getfield 26	android/os/CommonTimeUtils:mInterfaceDesc	Ljava/lang/String;
    //   14: invokevirtual 41	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
    //   17: aload_3
    //   18: aload_2
    //   19: invokevirtual 142	android/os/Parcel:writeString	(Ljava/lang/String;)V
    //   22: aload_0
    //   23: getfield 24	android/os/CommonTimeUtils:mRemote	Landroid/os/IBinder;
    //   26: iload_1
    //   27: aload_3
    //   28: aload 4
    //   30: iconst_0
    //   31: invokeinterface 47 5 0
    //   36: pop
    //   37: aload 4
    //   39: invokevirtual 51	android/os/Parcel:readInt	()I
    //   42: istore_1
    //   43: aload 4
    //   45: invokevirtual 54	android/os/Parcel:recycle	()V
    //   48: aload_3
    //   49: invokevirtual 54	android/os/Parcel:recycle	()V
    //   52: iload_1
    //   53: ireturn
    //   54: astore_2
    //   55: aload 4
    //   57: invokevirtual 54	android/os/Parcel:recycle	()V
    //   60: aload_3
    //   61: invokevirtual 54	android/os/Parcel:recycle	()V
    //   64: aload_2
    //   65: athrow
    //   66: astore_2
    //   67: aload 4
    //   69: invokevirtual 54	android/os/Parcel:recycle	()V
    //   72: aload_3
    //   73: invokevirtual 54	android/os/Parcel:recycle	()V
    //   76: bipush -7
    //   78: ireturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	79	0	this	CommonTimeUtils
    //   0	79	1	paramInt	int
    //   0	79	2	paramString	String
    //   3	70	3	localParcel1	Parcel
    //   7	61	4	localParcel2	Parcel
    // Exception table:
    //   from	to	target	type
    //   9	43	54	finally
    //   9	43	66	android/os/RemoteException
  }
}
