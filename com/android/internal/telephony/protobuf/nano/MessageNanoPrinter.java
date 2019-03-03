package com.android.internal.telephony.protobuf.nano;

import java.lang.reflect.InvocationTargetException;

public final class MessageNanoPrinter
{
  private static final String INDENT = "  ";
  private static final int MAX_STRING_LEN = 200;
  
  private MessageNanoPrinter() {}
  
  private static void appendQuotedBytes(byte[] paramArrayOfByte, StringBuffer paramStringBuffer)
  {
    if (paramArrayOfByte == null)
    {
      paramStringBuffer.append("\"\"");
      return;
    }
    paramStringBuffer.append('"');
    for (int i = 0; i < paramArrayOfByte.length; i++)
    {
      int j = paramArrayOfByte[i] & 0xFF;
      if ((j != 92) && (j != 34))
      {
        if ((j >= 32) && (j < 127)) {
          paramStringBuffer.append((char)j);
        } else {
          paramStringBuffer.append(String.format("\\%03o", new Object[] { Integer.valueOf(j) }));
        }
      }
      else
      {
        paramStringBuffer.append('\\');
        paramStringBuffer.append((char)j);
      }
    }
    paramStringBuffer.append('"');
  }
  
  private static String deCamelCaseify(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < paramString.length(); i++)
    {
      char c = paramString.charAt(i);
      if (i == 0)
      {
        localStringBuffer.append(Character.toLowerCase(c));
      }
      else if (Character.isUpperCase(c))
      {
        localStringBuffer.append('_');
        localStringBuffer.append(Character.toLowerCase(c));
      }
      else
      {
        localStringBuffer.append(c);
      }
    }
    return localStringBuffer.toString();
  }
  
  private static String escapeString(String paramString)
  {
    int i = paramString.length();
    StringBuilder localStringBuilder = new StringBuilder(i);
    for (int j = 0; j < i; j++)
    {
      char c = paramString.charAt(j);
      if ((c >= ' ') && (c <= '~') && (c != '"') && (c != '\'')) {
        localStringBuilder.append(c);
      } else {
        localStringBuilder.append(String.format("\\u%04x", new Object[] { Integer.valueOf(c) }));
      }
    }
    return localStringBuilder.toString();
  }
  
  public static <T extends MessageNano> String print(T paramT)
  {
    if (paramT == null) {
      return "";
    }
    StringBuffer localStringBuffer1 = new StringBuffer();
    try
    {
      StringBuffer localStringBuffer2 = new java/lang/StringBuffer;
      localStringBuffer2.<init>();
      print(null, paramT, localStringBuffer2, localStringBuffer1);
      return localStringBuffer1.toString();
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      paramT = new StringBuilder();
      paramT.append("Error printing proto: ");
      paramT.append(localInvocationTargetException.getMessage());
      return paramT.toString();
    }
    catch (IllegalAccessException paramT)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Error printing proto: ");
      localStringBuilder.append(paramT.getMessage());
      return localStringBuilder.toString();
    }
  }
  
  /* Error */
  private static void print(String paramString, Object paramObject, StringBuffer paramStringBuffer1, StringBuffer paramStringBuffer2)
    throws IllegalAccessException, InvocationTargetException
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnonnull +6 -> 7
    //   4: goto +518 -> 522
    //   7: aload_1
    //   8: instanceof 107
    //   11: ifeq +514 -> 525
    //   14: aload_2
    //   15: invokevirtual 108	java/lang/StringBuffer:length	()I
    //   18: istore 4
    //   20: aload_0
    //   21: ifnull +32 -> 53
    //   24: aload_3
    //   25: aload_2
    //   26: invokevirtual 111	java/lang/StringBuffer:append	(Ljava/lang/StringBuffer;)Ljava/lang/StringBuffer;
    //   29: pop
    //   30: aload_3
    //   31: aload_0
    //   32: invokestatic 113	com/android/internal/telephony/protobuf/nano/MessageNanoPrinter:deCamelCaseify	(Ljava/lang/String;)Ljava/lang/String;
    //   35: invokevirtual 26	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   38: pop
    //   39: aload_3
    //   40: ldc 115
    //   42: invokevirtual 26	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   45: pop
    //   46: aload_2
    //   47: ldc 8
    //   49: invokevirtual 26	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   52: pop
    //   53: aload_1
    //   54: invokevirtual 119	java/lang/Object:getClass	()Ljava/lang/Class;
    //   57: astore 5
    //   59: aload 5
    //   61: invokevirtual 125	java/lang/Class:getFields	()[Ljava/lang/reflect/Field;
    //   64: astore 6
    //   66: aload 6
    //   68: arraylength
    //   69: istore 7
    //   71: iconst_0
    //   72: istore 8
    //   74: iload 8
    //   76: iload 7
    //   78: if_icmpge +211 -> 289
    //   81: aload 6
    //   83: iload 8
    //   85: aaload
    //   86: astore 9
    //   88: aload 9
    //   90: invokevirtual 130	java/lang/reflect/Field:getModifiers	()I
    //   93: istore 10
    //   95: aload 9
    //   97: invokevirtual 133	java/lang/reflect/Field:getName	()Ljava/lang/String;
    //   100: astore 11
    //   102: ldc -121
    //   104: aload 11
    //   106: invokevirtual 139	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   109: ifeq +6 -> 115
    //   112: goto +171 -> 283
    //   115: iload 10
    //   117: iconst_1
    //   118: iand
    //   119: iconst_1
    //   120: if_icmpne -8 -> 112
    //   123: iload 10
    //   125: bipush 8
    //   127: iand
    //   128: bipush 8
    //   130: if_icmpeq -18 -> 112
    //   133: aload 11
    //   135: ldc -115
    //   137: invokevirtual 145	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   140: ifne -28 -> 112
    //   143: aload 11
    //   145: ldc -115
    //   147: invokevirtual 148	java/lang/String:endsWith	(Ljava/lang/String;)Z
    //   150: ifne -38 -> 112
    //   153: aload 9
    //   155: invokevirtual 151	java/lang/reflect/Field:getType	()Ljava/lang/Class;
    //   158: astore 12
    //   160: aload 9
    //   162: aload_1
    //   163: invokevirtual 155	java/lang/reflect/Field:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   166: astore 9
    //   168: aload 12
    //   170: invokevirtual 159	java/lang/Class:isArray	()Z
    //   173: ifeq +101 -> 274
    //   176: aload 12
    //   178: invokevirtual 162	java/lang/Class:getComponentType	()Ljava/lang/Class;
    //   181: getstatic 168	java/lang/Byte:TYPE	Ljava/lang/Class;
    //   184: if_acmpne +27 -> 211
    //   187: aload 11
    //   189: aload 9
    //   191: aload_2
    //   192: aload_3
    //   193: invokestatic 94	com/android/internal/telephony/protobuf/nano/MessageNanoPrinter:print	(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/StringBuffer;Ljava/lang/StringBuffer;)V
    //   196: aload 6
    //   198: astore 12
    //   200: iload 7
    //   202: istore 10
    //   204: iload 10
    //   206: istore 7
    //   208: goto +59 -> 267
    //   211: aload 9
    //   213: ifnonnull +9 -> 222
    //   216: iconst_0
    //   217: istore 13
    //   219: goto +10 -> 229
    //   222: aload 9
    //   224: invokestatic 174	java/lang/reflect/Array:getLength	(Ljava/lang/Object;)I
    //   227: istore 13
    //   229: iconst_0
    //   230: istore 14
    //   232: iload 7
    //   234: istore 10
    //   236: aload 6
    //   238: astore 12
    //   240: iload 14
    //   242: iload 13
    //   244: if_icmpge -40 -> 204
    //   247: aload 11
    //   249: aload 9
    //   251: iload 14
    //   253: invokestatic 177	java/lang/reflect/Array:get	(Ljava/lang/Object;I)Ljava/lang/Object;
    //   256: aload_2
    //   257: aload_3
    //   258: invokestatic 94	com/android/internal/telephony/protobuf/nano/MessageNanoPrinter:print	(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/StringBuffer;Ljava/lang/StringBuffer;)V
    //   261: iinc 14 1
    //   264: goto -32 -> 232
    //   267: aload 12
    //   269: astore 6
    //   271: goto +12 -> 283
    //   274: aload 11
    //   276: aload 9
    //   278: aload_2
    //   279: aload_3
    //   280: invokestatic 94	com/android/internal/telephony/protobuf/nano/MessageNanoPrinter:print	(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/StringBuffer;Ljava/lang/StringBuffer;)V
    //   283: iinc 8 1
    //   286: goto -212 -> 74
    //   289: aload 5
    //   291: invokevirtual 181	java/lang/Class:getMethods	()[Ljava/lang/reflect/Method;
    //   294: astore 6
    //   296: aload 6
    //   298: arraylength
    //   299: istore 10
    //   301: iconst_0
    //   302: istore 7
    //   304: iload 7
    //   306: iload 10
    //   308: if_icmpge +191 -> 499
    //   311: aload 6
    //   313: iload 7
    //   315: aaload
    //   316: invokevirtual 184	java/lang/reflect/Method:getName	()Ljava/lang/String;
    //   319: astore 12
    //   321: aload 12
    //   323: ldc -70
    //   325: invokevirtual 145	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   328: ifeq +165 -> 493
    //   331: aload 12
    //   333: iconst_3
    //   334: invokevirtual 190	java/lang/String:substring	(I)Ljava/lang/String;
    //   337: astore 12
    //   339: new 71	java/lang/StringBuilder
    //   342: astore 11
    //   344: aload 11
    //   346: invokespecial 95	java/lang/StringBuilder:<init>	()V
    //   349: aload 11
    //   351: ldc -64
    //   353: invokevirtual 82	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   356: pop
    //   357: aload 11
    //   359: aload 12
    //   361: invokevirtual 82	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   364: pop
    //   365: aload 11
    //   367: invokevirtual 83	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   370: astore 11
    //   372: aload 5
    //   374: aload 11
    //   376: iconst_0
    //   377: anewarray 121	java/lang/Class
    //   380: invokevirtual 196	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   383: astore 11
    //   385: aload 11
    //   387: aload_1
    //   388: iconst_0
    //   389: anewarray 4	java/lang/Object
    //   392: invokevirtual 200	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   395: checkcast 202	java/lang/Boolean
    //   398: invokevirtual 205	java/lang/Boolean:booleanValue	()Z
    //   401: ifne +6 -> 407
    //   404: goto +89 -> 493
    //   407: new 71	java/lang/StringBuilder
    //   410: astore 11
    //   412: aload 11
    //   414: invokespecial 95	java/lang/StringBuilder:<init>	()V
    //   417: aload 11
    //   419: ldc -50
    //   421: invokevirtual 82	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   424: pop
    //   425: aload 11
    //   427: aload 12
    //   429: invokevirtual 82	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   432: pop
    //   433: aload 11
    //   435: invokevirtual 83	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   438: astore 11
    //   440: aload 5
    //   442: aload 11
    //   444: iconst_0
    //   445: anewarray 121	java/lang/Class
    //   448: invokevirtual 196	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   451: astore 11
    //   453: aload 12
    //   455: aload 11
    //   457: aload_1
    //   458: iconst_0
    //   459: anewarray 4	java/lang/Object
    //   462: invokevirtual 200	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   465: aload_2
    //   466: aload_3
    //   467: invokestatic 94	com/android/internal/telephony/protobuf/nano/MessageNanoPrinter:print	(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/StringBuffer;Ljava/lang/StringBuffer;)V
    //   470: goto +23 -> 493
    //   473: astore 12
    //   475: goto +5 -> 480
    //   478: astore 12
    //   480: goto +13 -> 493
    //   483: astore 12
    //   485: goto +5 -> 490
    //   488: astore 12
    //   490: goto +3 -> 493
    //   493: iinc 7 1
    //   496: goto -192 -> 304
    //   499: aload_0
    //   500: ifnull +22 -> 522
    //   503: aload_2
    //   504: iload 4
    //   506: invokevirtual 209	java/lang/StringBuffer:setLength	(I)V
    //   509: aload_3
    //   510: aload_2
    //   511: invokevirtual 111	java/lang/StringBuffer:append	(Ljava/lang/StringBuffer;)Ljava/lang/StringBuffer;
    //   514: pop
    //   515: aload_3
    //   516: ldc -45
    //   518: invokevirtual 26	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   521: pop
    //   522: goto +231 -> 753
    //   525: aload_1
    //   526: instanceof 213
    //   529: ifeq +130 -> 659
    //   532: aload_1
    //   533: checkcast 213	java/util/Map
    //   536: astore_1
    //   537: aload_0
    //   538: invokestatic 113	com/android/internal/telephony/protobuf/nano/MessageNanoPrinter:deCamelCaseify	(Ljava/lang/String;)Ljava/lang/String;
    //   541: astore_0
    //   542: aload_1
    //   543: invokeinterface 217 1 0
    //   548: invokeinterface 223 1 0
    //   553: astore_1
    //   554: aload_1
    //   555: invokeinterface 228 1 0
    //   560: ifeq +96 -> 656
    //   563: aload_1
    //   564: invokeinterface 232 1 0
    //   569: checkcast 234	java/util/Map$Entry
    //   572: astore 6
    //   574: aload_3
    //   575: aload_2
    //   576: invokevirtual 111	java/lang/StringBuffer:append	(Ljava/lang/StringBuffer;)Ljava/lang/StringBuffer;
    //   579: pop
    //   580: aload_3
    //   581: aload_0
    //   582: invokevirtual 26	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   585: pop
    //   586: aload_3
    //   587: ldc 115
    //   589: invokevirtual 26	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   592: pop
    //   593: aload_2
    //   594: invokevirtual 108	java/lang/StringBuffer:length	()I
    //   597: istore 7
    //   599: aload_2
    //   600: ldc 8
    //   602: invokevirtual 26	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   605: pop
    //   606: ldc -20
    //   608: aload 6
    //   610: invokeinterface 239 1 0
    //   615: aload_2
    //   616: aload_3
    //   617: invokestatic 94	com/android/internal/telephony/protobuf/nano/MessageNanoPrinter:print	(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/StringBuffer;Ljava/lang/StringBuffer;)V
    //   620: ldc -15
    //   622: aload 6
    //   624: invokeinterface 244 1 0
    //   629: aload_2
    //   630: aload_3
    //   631: invokestatic 94	com/android/internal/telephony/protobuf/nano/MessageNanoPrinter:print	(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/StringBuffer;Ljava/lang/StringBuffer;)V
    //   634: aload_2
    //   635: iload 7
    //   637: invokevirtual 209	java/lang/StringBuffer:setLength	(I)V
    //   640: aload_3
    //   641: aload_2
    //   642: invokevirtual 111	java/lang/StringBuffer:append	(Ljava/lang/StringBuffer;)Ljava/lang/StringBuffer;
    //   645: pop
    //   646: aload_3
    //   647: ldc -45
    //   649: invokevirtual 26	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   652: pop
    //   653: goto -99 -> 554
    //   656: goto +97 -> 753
    //   659: aload_0
    //   660: invokestatic 113	com/android/internal/telephony/protobuf/nano/MessageNanoPrinter:deCamelCaseify	(Ljava/lang/String;)Ljava/lang/String;
    //   663: astore_0
    //   664: aload_3
    //   665: aload_2
    //   666: invokevirtual 111	java/lang/StringBuffer:append	(Ljava/lang/StringBuffer;)Ljava/lang/StringBuffer;
    //   669: pop
    //   670: aload_3
    //   671: aload_0
    //   672: invokevirtual 26	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   675: pop
    //   676: aload_3
    //   677: ldc -10
    //   679: invokevirtual 26	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   682: pop
    //   683: aload_1
    //   684: instanceof 39
    //   687: ifeq +34 -> 721
    //   690: aload_1
    //   691: checkcast 39	java/lang/String
    //   694: invokestatic 249	com/android/internal/telephony/protobuf/nano/MessageNanoPrinter:sanitizeString	(Ljava/lang/String;)Ljava/lang/String;
    //   697: astore_0
    //   698: aload_3
    //   699: ldc -5
    //   701: invokevirtual 26	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   704: pop
    //   705: aload_3
    //   706: aload_0
    //   707: invokevirtual 26	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   710: pop
    //   711: aload_3
    //   712: ldc -5
    //   714: invokevirtual 26	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   717: pop
    //   718: goto +27 -> 745
    //   721: aload_1
    //   722: instanceof 253
    //   725: ifeq +14 -> 739
    //   728: aload_1
    //   729: checkcast 253	[B
    //   732: aload_3
    //   733: invokestatic 255	com/android/internal/telephony/protobuf/nano/MessageNanoPrinter:appendQuotedBytes	([BLjava/lang/StringBuffer;)V
    //   736: goto +9 -> 745
    //   739: aload_3
    //   740: aload_1
    //   741: invokevirtual 258	java/lang/StringBuffer:append	(Ljava/lang/Object;)Ljava/lang/StringBuffer;
    //   744: pop
    //   745: aload_3
    //   746: ldc_w 260
    //   749: invokevirtual 26	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   752: pop
    //   753: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	754	0	paramString	String
    //   0	754	1	paramObject	Object
    //   0	754	2	paramStringBuffer1	StringBuffer
    //   0	754	3	paramStringBuffer2	StringBuffer
    //   18	487	4	i	int
    //   57	384	5	localClass	Class
    //   64	559	6	localObject1	Object
    //   69	567	7	j	int
    //   72	212	8	k	int
    //   86	191	9	localObject2	Object
    //   93	216	10	m	int
    //   100	356	11	localObject3	Object
    //   158	296	12	localObject4	Object
    //   473	1	12	localNoSuchMethodException1	NoSuchMethodException
    //   478	1	12	localNoSuchMethodException2	NoSuchMethodException
    //   483	1	12	localNoSuchMethodException3	NoSuchMethodException
    //   488	1	12	localNoSuchMethodException4	NoSuchMethodException
    //   217	28	13	n	int
    //   230	32	14	i1	int
    // Exception table:
    //   from	to	target	type
    //   440	453	473	java/lang/NoSuchMethodException
    //   407	440	478	java/lang/NoSuchMethodException
    //   372	385	483	java/lang/NoSuchMethodException
    //   339	372	488	java/lang/NoSuchMethodException
  }
  
  private static String sanitizeString(String paramString)
  {
    Object localObject = paramString;
    if (!paramString.startsWith("http"))
    {
      localObject = paramString;
      if (paramString.length() > 200)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(paramString.substring(0, 200));
        ((StringBuilder)localObject).append("[...]");
        localObject = ((StringBuilder)localObject).toString();
      }
    }
    return escapeString((String)localObject);
  }
}
