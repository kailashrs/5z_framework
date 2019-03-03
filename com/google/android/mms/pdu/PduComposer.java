package com.google.android.mms.pdu;

import android.content.ContentResolver;
import android.content.Context;
import android.text.TextUtils;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;

public class PduComposer
{
  private static final int END_STRING_FLAG = 0;
  private static final int LENGTH_QUOTE = 31;
  private static final int LONG_INTEGER_LENGTH_MAX = 8;
  private static final int PDU_COMPOSER_BLOCK_SIZE = 1024;
  private static final int PDU_COMPOSE_CONTENT_ERROR = 1;
  private static final int PDU_COMPOSE_FIELD_NOT_SET = 2;
  private static final int PDU_COMPOSE_FIELD_NOT_SUPPORTED = 3;
  private static final int PDU_COMPOSE_SUCCESS = 0;
  private static final int PDU_EMAIL_ADDRESS_TYPE = 2;
  private static final int PDU_IPV4_ADDRESS_TYPE = 3;
  private static final int PDU_IPV6_ADDRESS_TYPE = 4;
  private static final int PDU_PHONE_NUMBER_ADDRESS_TYPE = 1;
  private static final int PDU_UNKNOWN_ADDRESS_TYPE = 5;
  private static final int QUOTED_STRING_FLAG = 34;
  static final String REGEXP_EMAIL_ADDRESS_TYPE = "[a-zA-Z| ]*\\<{0,1}[a-zA-Z| ]+@{1}[a-zA-Z| ]+\\.{1}[a-zA-Z| ]+\\>{0,1}";
  static final String REGEXP_IPV4_ADDRESS_TYPE = "[0-9]{1,3}\\.{1}[0-9]{1,3}\\.{1}[0-9]{1,3}\\.{1}[0-9]{1,3}";
  static final String REGEXP_IPV6_ADDRESS_TYPE = "[a-fA-F]{4}\\:{1}[a-fA-F0-9]{4}\\:{1}[a-fA-F0-9]{4}\\:{1}[a-fA-F0-9]{4}\\:{1}[a-fA-F0-9]{4}\\:{1}[a-fA-F0-9]{4}\\:{1}[a-fA-F0-9]{4}\\:{1}[a-fA-F0-9]{4}";
  static final String REGEXP_PHONE_NUMBER_ADDRESS_TYPE = "\\+?[0-9|\\.|\\-]+";
  private static final int SHORT_INTEGER_MAX = 127;
  static final String STRING_IPV4_ADDRESS_TYPE = "/TYPE=IPV4";
  static final String STRING_IPV6_ADDRESS_TYPE = "/TYPE=IPV6";
  static final String STRING_PHONE_NUMBER_ADDRESS_TYPE = "/TYPE=PLMN";
  private static final int TEXT_MAX = 127;
  private static HashMap<String, Integer> mContentTypeMap = null;
  protected ByteArrayOutputStream mMessage = null;
  private GenericPdu mPdu = null;
  private PduHeaders mPduHeader = null;
  protected int mPosition = 0;
  private final ContentResolver mResolver;
  private BufferStack mStack = null;
  
  static
  {
    mContentTypeMap = new HashMap();
    for (int i = 0; i < PduContentTypes.contentTypes.length; i++) {
      mContentTypeMap.put(PduContentTypes.contentTypes[i], Integer.valueOf(i));
    }
  }
  
  public PduComposer(Context paramContext, GenericPdu paramGenericPdu)
  {
    mPdu = paramGenericPdu;
    mResolver = paramContext.getContentResolver();
    mPduHeader = paramGenericPdu.getPduHeaders();
    mStack = new BufferStack(null);
    mMessage = new ByteArrayOutputStream();
    mPosition = 0;
  }
  
  private EncodedStringValue appendAddressType(EncodedStringValue paramEncodedStringValue)
  {
    try
    {
      int i = checkAddressType(paramEncodedStringValue.getString());
      paramEncodedStringValue = EncodedStringValue.copy(paramEncodedStringValue);
      if (1 == i) {
        paramEncodedStringValue.appendTextString("/TYPE=PLMN".getBytes());
      } else if (3 == i) {
        paramEncodedStringValue.appendTextString("/TYPE=IPV4".getBytes());
      } else if (4 == i) {
        paramEncodedStringValue.appendTextString("/TYPE=IPV6".getBytes());
      }
      return paramEncodedStringValue;
    }
    catch (NullPointerException paramEncodedStringValue) {}
    return null;
  }
  
  private int appendHeader(int paramInt)
  {
    Object localObject;
    EncodedStringValue localEncodedStringValue;
    long l;
    int i;
    switch (paramInt)
    {
    case 131: 
    case 132: 
    case 135: 
    case 140: 
    case 142: 
    case 146: 
    case 147: 
    case 148: 
    default: 
      return 3;
    case 150: 
    case 154: 
      localObject = mPduHeader.getEncodedStringValue(paramInt);
      if (localObject == null) {
        return 2;
      }
      appendOctet(paramInt);
      appendEncodedString((EncodedStringValue)localObject);
      break;
    case 141: 
      appendOctet(paramInt);
      paramInt = mPduHeader.getOctet(paramInt);
      if (paramInt == 0) {
        appendShortInteger(18);
      } else {
        appendShortInteger(paramInt);
      }
      break;
    case 139: 
    case 152: 
      localObject = mPduHeader.getTextString(paramInt);
      if (localObject == null) {
        return 2;
      }
      appendOctet(paramInt);
      appendTextString((byte[])localObject);
      break;
    case 138: 
      localObject = mPduHeader.getTextString(paramInt);
      if (localObject == null) {
        return 2;
      }
      appendOctet(paramInt);
      if (Arrays.equals((byte[])localObject, "advertisement".getBytes())) {
        appendOctet(129);
      } else if (Arrays.equals((byte[])localObject, "auto".getBytes())) {
        appendOctet(131);
      } else if (Arrays.equals((byte[])localObject, "personal".getBytes())) {
        appendOctet(128);
      } else if (Arrays.equals((byte[])localObject, "informational".getBytes())) {
        appendOctet(130);
      } else {
        appendTextString((byte[])localObject);
      }
      break;
    case 137: 
      appendOctet(paramInt);
      localEncodedStringValue = mPduHeader.getEncodedStringValue(paramInt);
      if ((localEncodedStringValue != null) && (!TextUtils.isEmpty(localEncodedStringValue.getString())) && (!new String(localEncodedStringValue.getTextString()).equals("insert-address-token")))
      {
        mStack.newbuf();
        localObject = mStack.mark();
        append(128);
        localEncodedStringValue = appendAddressType(localEncodedStringValue);
        if (localEncodedStringValue == null) {
          return 1;
        }
        appendEncodedString(localEncodedStringValue);
        paramInt = ((PositionMarker)localObject).getLength();
        mStack.pop();
        appendValueLength(paramInt);
        mStack.copy();
      }
      else
      {
        append(1);
        append(129);
      }
      break;
    case 136: 
      l = mPduHeader.getLongInteger(paramInt);
      if (-1L == l) {
        return 2;
      }
      appendOctet(paramInt);
      mStack.newbuf();
      localObject = mStack.mark();
      append(129);
      appendLongInteger(l);
      paramInt = ((PositionMarker)localObject).getLength();
      mStack.pop();
      appendValueLength(paramInt);
      mStack.copy();
      break;
    case 134: 
    case 143: 
    case 144: 
    case 145: 
    case 149: 
    case 153: 
    case 155: 
      i = mPduHeader.getOctet(paramInt);
      if (i == 0) {
        return 2;
      }
      appendOctet(paramInt);
      appendOctet(i);
      break;
    case 133: 
      l = mPduHeader.getLongInteger(paramInt);
      if (-1L == l) {
        return 2;
      }
      appendOctet(paramInt);
      appendDateValue(l);
      break;
    case 129: 
    case 130: 
    case 151: 
      localObject = mPduHeader.getEncodedStringValues(paramInt);
      if (localObject == null) {
        return 2;
      }
      for (i = 0; i < localObject.length; i++)
      {
        localEncodedStringValue = appendAddressType(localObject[i]);
        if (localEncodedStringValue == null) {
          return 1;
        }
        appendOctet(paramInt);
        appendEncodedString(localEncodedStringValue);
      }
    }
    return 0;
  }
  
  protected static int checkAddressType(String paramString)
  {
    if (paramString == null) {
      return 5;
    }
    if (paramString.matches("[0-9]{1,3}\\.{1}[0-9]{1,3}\\.{1}[0-9]{1,3}\\.{1}[0-9]{1,3}")) {
      return 3;
    }
    if (paramString.matches("\\+?[0-9|\\.|\\-]+")) {
      return 1;
    }
    if (paramString.matches("[a-zA-Z| ]*\\<{0,1}[a-zA-Z| ]+@{1}[a-zA-Z| ]+\\.{1}[a-zA-Z| ]+\\>{0,1}")) {
      return 2;
    }
    if (paramString.matches("[a-fA-F]{4}\\:{1}[a-fA-F0-9]{4}\\:{1}[a-fA-F0-9]{4}\\:{1}[a-fA-F0-9]{4}\\:{1}[a-fA-F0-9]{4}\\:{1}[a-fA-F0-9]{4}\\:{1}[a-fA-F0-9]{4}\\:{1}[a-fA-F0-9]{4}")) {
      return 4;
    }
    return 5;
  }
  
  private int makeAckInd()
  {
    if (mMessage == null)
    {
      mMessage = new ByteArrayOutputStream();
      mPosition = 0;
    }
    appendOctet(140);
    appendOctet(133);
    if (appendHeader(152) != 0) {
      return 1;
    }
    if (appendHeader(141) != 0) {
      return 1;
    }
    appendHeader(145);
    return 0;
  }
  
  /* Error */
  private int makeMessageBody(int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 117	com/google/android/mms/pdu/PduComposer:mStack	Lcom/google/android/mms/pdu/PduComposer$BufferStack;
    //   4: invokevirtual 227	com/google/android/mms/pdu/PduComposer$BufferStack:newbuf	()V
    //   7: aload_0
    //   8: getfield 117	com/google/android/mms/pdu/PduComposer:mStack	Lcom/google/android/mms/pdu/PduComposer$BufferStack;
    //   11: invokevirtual 231	com/google/android/mms/pdu/PduComposer$BufferStack:mark	()Lcom/google/android/mms/pdu/PduComposer$PositionMarker;
    //   14: astore_2
    //   15: new 160	java/lang/String
    //   18: dup
    //   19: aload_0
    //   20: getfield 119	com/google/android/mms/pdu/PduComposer:mPduHeader	Lcom/google/android/mms/pdu/PduHeaders;
    //   23: sipush 132
    //   26: invokevirtual 194	com/google/android/mms/pdu/PduHeaders:getTextString	(I)[B
    //   29: invokespecial 219	java/lang/String:<init>	([B)V
    //   32: astore_3
    //   33: getstatic 85	com/google/android/mms/pdu/PduComposer:mContentTypeMap	Ljava/util/HashMap;
    //   36: aload_3
    //   37: invokevirtual 285	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   40: checkcast 98	java/lang/Integer
    //   43: astore 4
    //   45: aload 4
    //   47: ifnonnull +5 -> 52
    //   50: iconst_1
    //   51: ireturn
    //   52: aload_0
    //   53: aload 4
    //   55: invokevirtual 288	java/lang/Integer:intValue	()I
    //   58: invokevirtual 190	com/google/android/mms/pdu/PduComposer:appendShortInteger	(I)V
    //   61: iload_1
    //   62: sipush 132
    //   65: if_icmpne +18 -> 83
    //   68: aload_0
    //   69: getfield 113	com/google/android/mms/pdu/PduComposer:mPdu	Lcom/google/android/mms/pdu/GenericPdu;
    //   72: checkcast 290	com/google/android/mms/pdu/RetrieveConf
    //   75: invokevirtual 294	com/google/android/mms/pdu/RetrieveConf:getBody	()Lcom/google/android/mms/pdu/PduBody;
    //   78: astore 5
    //   80: goto +15 -> 95
    //   83: aload_0
    //   84: getfield 113	com/google/android/mms/pdu/PduComposer:mPdu	Lcom/google/android/mms/pdu/GenericPdu;
    //   87: checkcast 296	com/google/android/mms/pdu/SendReq
    //   90: invokevirtual 297	com/google/android/mms/pdu/SendReq:getBody	()Lcom/google/android/mms/pdu/PduBody;
    //   93: astore 5
    //   95: aload 5
    //   97: ifnull +956 -> 1053
    //   100: aload 5
    //   102: invokevirtual 302	com/google/android/mms/pdu/PduBody:getPartsNum	()I
    //   105: ifne +6 -> 111
    //   108: goto +945 -> 1053
    //   111: aload 5
    //   113: iconst_0
    //   114: invokevirtual 306	com/google/android/mms/pdu/PduBody:getPart	(I)Lcom/google/android/mms/pdu/PduPart;
    //   117: astore 6
    //   119: aload 6
    //   121: invokevirtual 311	com/google/android/mms/pdu/PduPart:getContentId	()[B
    //   124: astore 7
    //   126: aload 7
    //   128: ifnull +98 -> 226
    //   131: aload_0
    //   132: sipush 138
    //   135: invokevirtual 180	com/google/android/mms/pdu/PduComposer:appendOctet	(I)V
    //   138: bipush 60
    //   140: aload 7
    //   142: iconst_0
    //   143: baload
    //   144: if_icmpne +25 -> 169
    //   147: bipush 62
    //   149: aload 7
    //   151: aload 7
    //   153: arraylength
    //   154: iconst_1
    //   155: isub
    //   156: baload
    //   157: if_icmpne +12 -> 169
    //   160: aload_0
    //   161: aload 7
    //   163: invokevirtual 195	com/google/android/mms/pdu/PduComposer:appendTextString	([B)V
    //   166: goto +60 -> 226
    //   169: new 313	java/lang/StringBuilder
    //   172: astore 8
    //   174: aload 8
    //   176: invokespecial 314	java/lang/StringBuilder:<init>	()V
    //   179: aload 8
    //   181: ldc_w 316
    //   184: invokevirtual 319	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   187: pop
    //   188: new 160	java/lang/String
    //   191: astore 9
    //   193: aload 9
    //   195: aload 7
    //   197: invokespecial 219	java/lang/String:<init>	([B)V
    //   200: aload 8
    //   202: aload 9
    //   204: invokevirtual 319	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   207: pop
    //   208: aload 8
    //   210: ldc_w 321
    //   213: invokevirtual 319	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   216: pop
    //   217: aload_0
    //   218: aload 8
    //   220: invokevirtual 324	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   223: invokevirtual 327	com/google/android/mms/pdu/PduComposer:appendTextString	(Ljava/lang/String;)V
    //   226: aload_0
    //   227: sipush 137
    //   230: invokevirtual 180	com/google/android/mms/pdu/PduComposer:appendOctet	(I)V
    //   233: aload_0
    //   234: aload 6
    //   236: invokevirtual 330	com/google/android/mms/pdu/PduPart:getContentType	()[B
    //   239: invokevirtual 195	com/google/android/mms/pdu/PduComposer:appendTextString	([B)V
    //   242: goto +10 -> 252
    //   245: astore 7
    //   247: aload 7
    //   249: invokevirtual 333	java/lang/ArrayIndexOutOfBoundsException:printStackTrace	()V
    //   252: aload_2
    //   253: invokevirtual 240	com/google/android/mms/pdu/PduComposer$PositionMarker:getLength	()I
    //   256: istore_1
    //   257: aload_0
    //   258: getfield 117	com/google/android/mms/pdu/PduComposer:mStack	Lcom/google/android/mms/pdu/PduComposer$BufferStack;
    //   261: invokevirtual 243	com/google/android/mms/pdu/PduComposer$BufferStack:pop	()V
    //   264: aload_0
    //   265: iload_1
    //   266: i2l
    //   267: invokevirtual 247	com/google/android/mms/pdu/PduComposer:appendValueLength	(J)V
    //   270: aload_0
    //   271: getfield 117	com/google/android/mms/pdu/PduComposer:mStack	Lcom/google/android/mms/pdu/PduComposer$BufferStack;
    //   274: invokevirtual 249	com/google/android/mms/pdu/PduComposer$BufferStack:copy	()V
    //   277: aload 5
    //   279: invokevirtual 302	com/google/android/mms/pdu/PduBody:getPartsNum	()I
    //   282: istore 10
    //   284: aload_0
    //   285: iload 10
    //   287: i2l
    //   288: invokevirtual 336	com/google/android/mms/pdu/PduComposer:appendUintvarInteger	(J)V
    //   291: iconst_0
    //   292: istore 11
    //   294: aload 5
    //   296: astore 7
    //   298: iload 11
    //   300: iload 10
    //   302: if_icmpge +749 -> 1051
    //   305: aload 7
    //   307: iload 11
    //   309: invokevirtual 306	com/google/android/mms/pdu/PduBody:getPart	(I)Lcom/google/android/mms/pdu/PduPart;
    //   312: astore 12
    //   314: aload_0
    //   315: getfield 117	com/google/android/mms/pdu/PduComposer:mStack	Lcom/google/android/mms/pdu/PduComposer$BufferStack;
    //   318: invokevirtual 227	com/google/android/mms/pdu/PduComposer$BufferStack:newbuf	()V
    //   321: aload_0
    //   322: getfield 117	com/google/android/mms/pdu/PduComposer:mStack	Lcom/google/android/mms/pdu/PduComposer$BufferStack;
    //   325: invokevirtual 231	com/google/android/mms/pdu/PduComposer$BufferStack:mark	()Lcom/google/android/mms/pdu/PduComposer$PositionMarker;
    //   328: astore 13
    //   330: aload_0
    //   331: getfield 117	com/google/android/mms/pdu/PduComposer:mStack	Lcom/google/android/mms/pdu/PduComposer$BufferStack;
    //   334: invokevirtual 227	com/google/android/mms/pdu/PduComposer$BufferStack:newbuf	()V
    //   337: aload_0
    //   338: getfield 117	com/google/android/mms/pdu/PduComposer:mStack	Lcom/google/android/mms/pdu/PduComposer$BufferStack;
    //   341: invokevirtual 231	com/google/android/mms/pdu/PduComposer$BufferStack:mark	()Lcom/google/android/mms/pdu/PduComposer$PositionMarker;
    //   344: astore 6
    //   346: aload 12
    //   348: invokevirtual 330	com/google/android/mms/pdu/PduPart:getContentType	()[B
    //   351: astore 8
    //   353: aload 8
    //   355: ifnonnull +5 -> 360
    //   358: iconst_1
    //   359: ireturn
    //   360: getstatic 85	com/google/android/mms/pdu/PduComposer:mContentTypeMap	Ljava/util/HashMap;
    //   363: new 160	java/lang/String
    //   366: dup
    //   367: aload 8
    //   369: invokespecial 219	java/lang/String:<init>	([B)V
    //   372: invokevirtual 285	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   375: checkcast 98	java/lang/Integer
    //   378: astore 5
    //   380: aload 5
    //   382: ifnonnull +12 -> 394
    //   385: aload_0
    //   386: aload 8
    //   388: invokevirtual 195	com/google/android/mms/pdu/PduComposer:appendTextString	([B)V
    //   391: goto +12 -> 403
    //   394: aload_0
    //   395: aload 5
    //   397: invokevirtual 288	java/lang/Integer:intValue	()I
    //   400: invokevirtual 190	com/google/android/mms/pdu/PduComposer:appendShortInteger	(I)V
    //   403: aload 12
    //   405: invokevirtual 339	com/google/android/mms/pdu/PduPart:getName	()[B
    //   408: astore 8
    //   410: aload 8
    //   412: astore 5
    //   414: aload 8
    //   416: ifnonnull +37 -> 453
    //   419: aload 12
    //   421: invokevirtual 342	com/google/android/mms/pdu/PduPart:getFilename	()[B
    //   424: astore 8
    //   426: aload 8
    //   428: astore 5
    //   430: aload 8
    //   432: ifnonnull +21 -> 453
    //   435: aload 12
    //   437: invokevirtual 345	com/google/android/mms/pdu/PduPart:getContentLocation	()[B
    //   440: astore 8
    //   442: aload 8
    //   444: astore 5
    //   446: aload 8
    //   448: ifnonnull +5 -> 453
    //   451: iconst_1
    //   452: ireturn
    //   453: aload 5
    //   455: astore 8
    //   457: aload_0
    //   458: sipush 133
    //   461: invokevirtual 180	com/google/android/mms/pdu/PduComposer:appendOctet	(I)V
    //   464: aload_0
    //   465: aload 8
    //   467: invokevirtual 195	com/google/android/mms/pdu/PduComposer:appendTextString	([B)V
    //   470: aload 12
    //   472: invokevirtual 348	com/google/android/mms/pdu/PduPart:getCharset	()I
    //   475: istore_1
    //   476: iload_1
    //   477: ifeq +15 -> 492
    //   480: aload_0
    //   481: sipush 129
    //   484: invokevirtual 180	com/google/android/mms/pdu/PduComposer:appendOctet	(I)V
    //   487: aload_0
    //   488: iload_1
    //   489: invokevirtual 190	com/google/android/mms/pdu/PduComposer:appendShortInteger	(I)V
    //   492: aload 6
    //   494: invokevirtual 240	com/google/android/mms/pdu/PduComposer$PositionMarker:getLength	()I
    //   497: istore_1
    //   498: aload_0
    //   499: getfield 117	com/google/android/mms/pdu/PduComposer:mStack	Lcom/google/android/mms/pdu/PduComposer$BufferStack;
    //   502: invokevirtual 243	com/google/android/mms/pdu/PduComposer$BufferStack:pop	()V
    //   505: aload_0
    //   506: iload_1
    //   507: i2l
    //   508: invokevirtual 247	com/google/android/mms/pdu/PduComposer:appendValueLength	(J)V
    //   511: aload_0
    //   512: getfield 117	com/google/android/mms/pdu/PduComposer:mStack	Lcom/google/android/mms/pdu/PduComposer$BufferStack;
    //   515: invokevirtual 249	com/google/android/mms/pdu/PduComposer$BufferStack:copy	()V
    //   518: aload 12
    //   520: invokevirtual 311	com/google/android/mms/pdu/PduPart:getContentId	()[B
    //   523: astore 6
    //   525: aload 6
    //   527: ifnull +92 -> 619
    //   530: aload_0
    //   531: sipush 192
    //   534: invokevirtual 180	com/google/android/mms/pdu/PduComposer:appendOctet	(I)V
    //   537: bipush 60
    //   539: aload 6
    //   541: iconst_0
    //   542: baload
    //   543: if_icmpne +25 -> 568
    //   546: bipush 62
    //   548: aload 6
    //   550: aload 6
    //   552: arraylength
    //   553: iconst_1
    //   554: isub
    //   555: baload
    //   556: if_icmpne +12 -> 568
    //   559: aload_0
    //   560: aload 6
    //   562: invokevirtual 351	com/google/android/mms/pdu/PduComposer:appendQuotedString	([B)V
    //   565: goto +54 -> 619
    //   568: new 313	java/lang/StringBuilder
    //   571: dup
    //   572: invokespecial 314	java/lang/StringBuilder:<init>	()V
    //   575: astore 5
    //   577: aload 5
    //   579: ldc_w 316
    //   582: invokevirtual 319	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   585: pop
    //   586: aload 5
    //   588: new 160	java/lang/String
    //   591: dup
    //   592: aload 6
    //   594: invokespecial 219	java/lang/String:<init>	([B)V
    //   597: invokevirtual 319	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   600: pop
    //   601: aload 5
    //   603: ldc_w 321
    //   606: invokevirtual 319	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   609: pop
    //   610: aload_0
    //   611: aload 5
    //   613: invokevirtual 324	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   616: invokevirtual 353	com/google/android/mms/pdu/PduComposer:appendQuotedString	(Ljava/lang/String;)V
    //   619: aload 12
    //   621: invokevirtual 345	com/google/android/mms/pdu/PduPart:getContentLocation	()[B
    //   624: astore 5
    //   626: aload 5
    //   628: ifnull +16 -> 644
    //   631: aload_0
    //   632: sipush 142
    //   635: invokevirtual 180	com/google/android/mms/pdu/PduComposer:appendOctet	(I)V
    //   638: aload_0
    //   639: aload 5
    //   641: invokevirtual 195	com/google/android/mms/pdu/PduComposer:appendTextString	([B)V
    //   644: aload 13
    //   646: invokevirtual 240	com/google/android/mms/pdu/PduComposer$PositionMarker:getLength	()I
    //   649: istore 14
    //   651: iconst_0
    //   652: istore_1
    //   653: aload 12
    //   655: invokevirtual 356	com/google/android/mms/pdu/PduPart:getData	()[B
    //   658: astore 5
    //   660: aload 5
    //   662: ifnull +20 -> 682
    //   665: aload_0
    //   666: aload 5
    //   668: iconst_0
    //   669: aload 5
    //   671: arraylength
    //   672: invokevirtual 360	com/google/android/mms/pdu/PduComposer:arraycopy	([BII)V
    //   675: aload 5
    //   677: arraylength
    //   678: istore_1
    //   679: goto +155 -> 834
    //   682: aconst_null
    //   683: astore 6
    //   685: aconst_null
    //   686: astore 9
    //   688: aconst_null
    //   689: astore 15
    //   691: aconst_null
    //   692: astore 5
    //   694: sipush 1024
    //   697: newarray byte
    //   699: astore 16
    //   701: aload_0
    //   702: getfield 127	com/google/android/mms/pdu/PduComposer:mResolver	Landroid/content/ContentResolver;
    //   705: astore 17
    //   707: aload 17
    //   709: aload 12
    //   711: invokevirtual 364	com/google/android/mms/pdu/PduPart:getDataUri	()Landroid/net/Uri;
    //   714: invokevirtual 370	android/content/ContentResolver:openInputStream	(Landroid/net/Uri;)Ljava/io/InputStream;
    //   717: astore 6
    //   719: aload 6
    //   721: astore 5
    //   723: iconst_0
    //   724: istore 18
    //   726: aload 5
    //   728: aload 16
    //   730: invokevirtual 376	java/io/InputStream:read	([B)I
    //   733: istore 18
    //   735: iload 18
    //   737: iconst_m1
    //   738: if_icmpeq +78 -> 816
    //   741: aload_0
    //   742: getfield 111	com/google/android/mms/pdu/PduComposer:mMessage	Ljava/io/ByteArrayOutputStream;
    //   745: astore 6
    //   747: aload 6
    //   749: aload 16
    //   751: iconst_0
    //   752: iload 18
    //   754: invokevirtual 379	java/io/ByteArrayOutputStream:write	([BII)V
    //   757: aload_0
    //   758: aload_0
    //   759: getfield 115	com/google/android/mms/pdu/PduComposer:mPosition	I
    //   762: iload 18
    //   764: iadd
    //   765: putfield 115	com/google/android/mms/pdu/PduComposer:mPosition	I
    //   768: iload_1
    //   769: iload 18
    //   771: iadd
    //   772: istore_1
    //   773: goto -47 -> 726
    //   776: astore 4
    //   778: goto +186 -> 964
    //   781: astore 4
    //   783: goto +205 -> 988
    //   786: astore 4
    //   788: goto +223 -> 1011
    //   791: astore 4
    //   793: goto +241 -> 1034
    //   796: astore 4
    //   798: goto +166 -> 964
    //   801: astore 4
    //   803: goto +185 -> 988
    //   806: astore 4
    //   808: goto +203 -> 1011
    //   811: astore 4
    //   813: goto +221 -> 1034
    //   816: aload 5
    //   818: ifnull +16 -> 834
    //   821: aload 5
    //   823: invokevirtual 382	java/io/InputStream:close	()V
    //   826: goto +8 -> 834
    //   829: astore 5
    //   831: goto -5 -> 826
    //   834: iload_1
    //   835: aload 13
    //   837: invokevirtual 240	com/google/android/mms/pdu/PduComposer$PositionMarker:getLength	()I
    //   840: iload 14
    //   842: isub
    //   843: if_icmpne +36 -> 879
    //   846: aload_0
    //   847: getfield 117	com/google/android/mms/pdu/PduComposer:mStack	Lcom/google/android/mms/pdu/PduComposer$BufferStack;
    //   850: invokevirtual 243	com/google/android/mms/pdu/PduComposer$BufferStack:pop	()V
    //   853: aload_0
    //   854: iload 14
    //   856: i2l
    //   857: invokevirtual 336	com/google/android/mms/pdu/PduComposer:appendUintvarInteger	(J)V
    //   860: aload_0
    //   861: iload_1
    //   862: i2l
    //   863: invokevirtual 336	com/google/android/mms/pdu/PduComposer:appendUintvarInteger	(J)V
    //   866: aload_0
    //   867: getfield 117	com/google/android/mms/pdu/PduComposer:mStack	Lcom/google/android/mms/pdu/PduComposer$BufferStack;
    //   870: invokevirtual 249	com/google/android/mms/pdu/PduComposer$BufferStack:copy	()V
    //   873: iinc 11 1
    //   876: goto -578 -> 298
    //   879: new 281	java/lang/RuntimeException
    //   882: dup
    //   883: ldc_w 384
    //   886: invokespecial 386	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
    //   889: athrow
    //   890: astore 4
    //   892: goto +20 -> 912
    //   895: astore 4
    //   897: goto +20 -> 917
    //   900: astore 4
    //   902: goto +20 -> 922
    //   905: astore 4
    //   907: goto +20 -> 927
    //   910: astore 4
    //   912: goto +52 -> 964
    //   915: astore 4
    //   917: goto +71 -> 988
    //   920: astore 4
    //   922: goto +89 -> 1011
    //   925: astore 4
    //   927: goto +107 -> 1034
    //   930: astore 4
    //   932: goto +32 -> 964
    //   935: astore 4
    //   937: aload 6
    //   939: astore 5
    //   941: goto +47 -> 988
    //   944: astore 4
    //   946: aload 9
    //   948: astore 5
    //   950: goto +61 -> 1011
    //   953: astore 4
    //   955: aload 15
    //   957: astore 5
    //   959: goto +75 -> 1034
    //   962: astore 4
    //   964: aload 5
    //   966: ifnull +13 -> 979
    //   969: aload 5
    //   971: invokevirtual 382	java/io/InputStream:close	()V
    //   974: goto +5 -> 979
    //   977: astore 5
    //   979: aload 4
    //   981: athrow
    //   982: astore 4
    //   984: aload 6
    //   986: astore 5
    //   988: aload 5
    //   990: ifnull +13 -> 1003
    //   993: aload 5
    //   995: invokevirtual 382	java/io/InputStream:close	()V
    //   998: goto +5 -> 1003
    //   1001: astore 5
    //   1003: iconst_1
    //   1004: ireturn
    //   1005: astore 4
    //   1007: aload 9
    //   1009: astore 5
    //   1011: aload 5
    //   1013: ifnull +13 -> 1026
    //   1016: aload 5
    //   1018: invokevirtual 382	java/io/InputStream:close	()V
    //   1021: goto +5 -> 1026
    //   1024: astore 5
    //   1026: iconst_1
    //   1027: ireturn
    //   1028: astore 4
    //   1030: aload 15
    //   1032: astore 5
    //   1034: aload 5
    //   1036: ifnull +13 -> 1049
    //   1039: aload 5
    //   1041: invokevirtual 382	java/io/InputStream:close	()V
    //   1044: goto +5 -> 1049
    //   1047: astore 5
    //   1049: iconst_1
    //   1050: ireturn
    //   1051: iconst_0
    //   1052: ireturn
    //   1053: aload_0
    //   1054: lconst_0
    //   1055: invokevirtual 336	com/google/android/mms/pdu/PduComposer:appendUintvarInteger	(J)V
    //   1058: aload_0
    //   1059: getfield 117	com/google/android/mms/pdu/PduComposer:mStack	Lcom/google/android/mms/pdu/PduComposer$BufferStack;
    //   1062: invokevirtual 243	com/google/android/mms/pdu/PduComposer$BufferStack:pop	()V
    //   1065: aload_0
    //   1066: getfield 117	com/google/android/mms/pdu/PduComposer:mStack	Lcom/google/android/mms/pdu/PduComposer$BufferStack;
    //   1069: invokevirtual 249	com/google/android/mms/pdu/PduComposer$BufferStack:copy	()V
    //   1072: iconst_0
    //   1073: ireturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	1074	0	this	PduComposer
    //   0	1074	1	paramInt	int
    //   14	239	2	localPositionMarker1	PositionMarker
    //   32	5	3	str1	String
    //   43	11	4	localInteger	Integer
    //   776	1	4	localObject1	Object
    //   781	1	4	localRuntimeException1	RuntimeException
    //   786	1	4	localIOException1	java.io.IOException
    //   791	1	4	localFileNotFoundException1	java.io.FileNotFoundException
    //   796	1	4	localObject2	Object
    //   801	1	4	localRuntimeException2	RuntimeException
    //   806	1	4	localIOException2	java.io.IOException
    //   811	1	4	localFileNotFoundException2	java.io.FileNotFoundException
    //   890	1	4	localObject3	Object
    //   895	1	4	localRuntimeException3	RuntimeException
    //   900	1	4	localIOException3	java.io.IOException
    //   905	1	4	localFileNotFoundException3	java.io.FileNotFoundException
    //   910	1	4	localObject4	Object
    //   915	1	4	localRuntimeException4	RuntimeException
    //   920	1	4	localIOException4	java.io.IOException
    //   925	1	4	localFileNotFoundException4	java.io.FileNotFoundException
    //   930	1	4	localObject5	Object
    //   935	1	4	localRuntimeException5	RuntimeException
    //   944	1	4	localIOException5	java.io.IOException
    //   953	1	4	localFileNotFoundException5	java.io.FileNotFoundException
    //   962	18	4	localObject6	Object
    //   982	1	4	localRuntimeException6	RuntimeException
    //   1005	1	4	localIOException6	java.io.IOException
    //   1028	1	4	localFileNotFoundException6	java.io.FileNotFoundException
    //   78	744	5	localObject7	Object
    //   829	1	5	localIOException7	java.io.IOException
    //   939	31	5	localObject8	Object
    //   977	1	5	localIOException8	java.io.IOException
    //   986	8	5	localObject9	Object
    //   1001	1	5	localIOException9	java.io.IOException
    //   1009	8	5	localObject10	Object
    //   1024	1	5	localIOException10	java.io.IOException
    //   1032	8	5	localObject11	Object
    //   1047	1	5	localIOException11	java.io.IOException
    //   117	868	6	localObject12	Object
    //   124	72	7	arrayOfByte1	byte[]
    //   245	3	7	localArrayIndexOutOfBoundsException	ArrayIndexOutOfBoundsException
    //   296	10	7	localObject13	Object
    //   172	294	8	localObject14	Object
    //   191	817	9	str2	String
    //   282	21	10	i	int
    //   292	582	11	j	int
    //   312	398	12	localPduPart	PduPart
    //   328	508	13	localPositionMarker2	PositionMarker
    //   649	206	14	k	int
    //   689	342	15	localObject15	Object
    //   699	51	16	arrayOfByte2	byte[]
    //   705	3	17	localContentResolver	ContentResolver
    //   724	48	18	m	int
    // Exception table:
    //   from	to	target	type
    //   111	126	245	java/lang/ArrayIndexOutOfBoundsException
    //   131	138	245	java/lang/ArrayIndexOutOfBoundsException
    //   147	166	245	java/lang/ArrayIndexOutOfBoundsException
    //   169	226	245	java/lang/ArrayIndexOutOfBoundsException
    //   226	242	245	java/lang/ArrayIndexOutOfBoundsException
    //   747	768	776	finally
    //   747	768	781	java/lang/RuntimeException
    //   747	768	786	java/io/IOException
    //   747	768	791	java/io/FileNotFoundException
    //   741	747	796	finally
    //   741	747	801	java/lang/RuntimeException
    //   741	747	806	java/io/IOException
    //   741	747	811	java/io/FileNotFoundException
    //   821	826	829	java/io/IOException
    //   726	735	890	finally
    //   726	735	895	java/lang/RuntimeException
    //   726	735	900	java/io/IOException
    //   726	735	905	java/io/FileNotFoundException
    //   707	719	910	finally
    //   707	719	915	java/lang/RuntimeException
    //   707	719	920	java/io/IOException
    //   707	719	925	java/io/FileNotFoundException
    //   701	707	930	finally
    //   701	707	935	java/lang/RuntimeException
    //   701	707	944	java/io/IOException
    //   701	707	953	java/io/FileNotFoundException
    //   694	701	962	finally
    //   969	974	977	java/io/IOException
    //   694	701	982	java/lang/RuntimeException
    //   993	998	1001	java/io/IOException
    //   694	701	1005	java/io/IOException
    //   1016	1021	1024	java/io/IOException
    //   694	701	1028	java/io/FileNotFoundException
    //   1039	1044	1047	java/io/IOException
  }
  
  private int makeNotifyResp()
  {
    if (mMessage == null)
    {
      mMessage = new ByteArrayOutputStream();
      mPosition = 0;
    }
    appendOctet(140);
    appendOctet(131);
    if (appendHeader(152) != 0) {
      return 1;
    }
    if (appendHeader(141) != 0) {
      return 1;
    }
    if (appendHeader(149) != 0) {
      return 1;
    }
    return 0;
  }
  
  private int makeReadRecInd()
  {
    if (mMessage == null)
    {
      mMessage = new ByteArrayOutputStream();
      mPosition = 0;
    }
    appendOctet(140);
    appendOctet(135);
    if (appendHeader(141) != 0) {
      return 1;
    }
    if (appendHeader(139) != 0) {
      return 1;
    }
    if (appendHeader(151) != 0) {
      return 1;
    }
    if (appendHeader(137) != 0) {
      return 1;
    }
    appendHeader(133);
    if (appendHeader(155) != 0) {
      return 1;
    }
    return 0;
  }
  
  private int makeSendRetrievePdu(int paramInt)
  {
    if (mMessage == null)
    {
      mMessage = new ByteArrayOutputStream();
      mPosition = 0;
    }
    appendOctet(140);
    appendOctet(paramInt);
    appendOctet(152);
    byte[] arrayOfByte = mPduHeader.getTextString(152);
    if (arrayOfByte != null)
    {
      appendTextString(arrayOfByte);
      if (appendHeader(141) != 0) {
        return 1;
      }
      appendHeader(133);
      if (appendHeader(137) != 0) {
        return 1;
      }
      int i = 0;
      if (appendHeader(151) != 1) {
        i = 1;
      }
      if (appendHeader(130) != 1) {
        i = 1;
      }
      if (appendHeader(129) != 1) {
        i = 1;
      }
      if (i == 0) {
        return 1;
      }
      appendHeader(150);
      appendHeader(138);
      appendHeader(136);
      appendHeader(143);
      appendHeader(134);
      appendHeader(144);
      if (paramInt == 132)
      {
        appendHeader(153);
        appendHeader(154);
      }
      appendOctet(132);
      return makeMessageBody(paramInt);
    }
    throw new IllegalArgumentException("Transaction-ID is null.");
  }
  
  protected void append(int paramInt)
  {
    mMessage.write(paramInt);
    mPosition += 1;
  }
  
  protected void appendDateValue(long paramLong)
  {
    appendLongInteger(paramLong);
  }
  
  protected void appendEncodedString(EncodedStringValue paramEncodedStringValue)
  {
    int i = paramEncodedStringValue.getCharacterSet();
    paramEncodedStringValue = paramEncodedStringValue.getTextString();
    if (paramEncodedStringValue == null) {
      return;
    }
    mStack.newbuf();
    PositionMarker localPositionMarker = mStack.mark();
    appendShortInteger(i);
    appendTextString(paramEncodedStringValue);
    i = localPositionMarker.getLength();
    mStack.pop();
    appendValueLength(i);
    mStack.copy();
  }
  
  protected void appendLongInteger(long paramLong)
  {
    int i = 0;
    long l = paramLong;
    for (int j = 0; (l != 0L) && (j < 8); j++) {
      l >>>= 8;
    }
    appendShortLength(j);
    int k = (j - 1) * 8;
    while (i < j)
    {
      append((int)(paramLong >>> k & 0xFF));
      k -= 8;
      i++;
    }
  }
  
  protected void appendOctet(int paramInt)
  {
    append(paramInt);
  }
  
  protected void appendQuotedString(String paramString)
  {
    appendQuotedString(paramString.getBytes());
  }
  
  protected void appendQuotedString(byte[] paramArrayOfByte)
  {
    append(34);
    arraycopy(paramArrayOfByte, 0, paramArrayOfByte.length);
    append(0);
  }
  
  protected void appendShortInteger(int paramInt)
  {
    append((paramInt | 0x80) & 0xFF);
  }
  
  protected void appendShortLength(int paramInt)
  {
    append(paramInt);
  }
  
  protected void appendTextString(String paramString)
  {
    appendTextString(paramString.getBytes());
  }
  
  protected void appendTextString(byte[] paramArrayOfByte)
  {
    if ((paramArrayOfByte[0] & 0xFF) > Byte.MAX_VALUE) {
      append(127);
    }
    arraycopy(paramArrayOfByte, 0, paramArrayOfByte.length);
    append(0);
  }
  
  protected void appendUintvarInteger(long paramLong)
  {
    long l = 127L;
    int j;
    for (int i = 0;; i++)
    {
      j = i;
      if (i >= 5) {
        break;
      }
      if (paramLong < l)
      {
        j = i;
        break;
      }
      l = l << 7 | 0x7F;
    }
    while (j > 0)
    {
      append((int)((0x80 | paramLong >>> j * 7 & 0x7F) & 0xFF));
      j--;
    }
    append((int)(paramLong & 0x7F));
  }
  
  protected void appendValueLength(long paramLong)
  {
    if (paramLong < 31L)
    {
      appendShortLength((int)paramLong);
      return;
    }
    append(31);
    appendUintvarInteger(paramLong);
  }
  
  protected void arraycopy(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    mMessage.write(paramArrayOfByte, paramInt1, paramInt2);
    mPosition += paramInt2;
  }
  
  public byte[] make()
  {
    int i = mPdu.getMessageType();
    if (i != 128)
    {
      if (i != 135) {}
      switch (i)
      {
      default: 
        return null;
      case 133: 
        if (makeAckInd() == 0) {
          break;
        }
        return null;
      case 131: 
        if (makeNotifyResp() == 0) {
          break;
        }
        return null;
        if (makeReadRecInd() == 0) {
          break;
        }
        return null;
      }
    }
    else if (makeSendRetrievePdu(i) != 0)
    {
      return null;
    }
    return mMessage.toByteArray();
  }
  
  private class BufferStack
  {
    private PduComposer.LengthRecordNode stack = null;
    int stackSize = 0;
    private PduComposer.LengthRecordNode toCopy = null;
    
    private BufferStack() {}
    
    void copy()
    {
      arraycopy(toCopy.currentMessage.toByteArray(), 0, toCopy.currentPosition);
      toCopy = null;
    }
    
    PduComposer.PositionMarker mark()
    {
      PduComposer.PositionMarker localPositionMarker = new PduComposer.PositionMarker(PduComposer.this, null);
      PduComposer.PositionMarker.access$402(localPositionMarker, mPosition);
      PduComposer.PositionMarker.access$502(localPositionMarker, stackSize);
      return localPositionMarker;
    }
    
    void newbuf()
    {
      if (toCopy == null)
      {
        PduComposer.LengthRecordNode localLengthRecordNode = new PduComposer.LengthRecordNode(null);
        currentMessage = mMessage;
        currentPosition = mPosition;
        next = stack;
        stack = localLengthRecordNode;
        stackSize += 1;
        mMessage = new ByteArrayOutputStream();
        mPosition = 0;
        return;
      }
      throw new RuntimeException("BUG: Invalid newbuf() before copy()");
    }
    
    void pop()
    {
      ByteArrayOutputStream localByteArrayOutputStream = mMessage;
      int i = mPosition;
      mMessage = stack.currentMessage;
      mPosition = stack.currentPosition;
      toCopy = stack;
      stack = stack.next;
      stackSize -= 1;
      toCopy.currentMessage = localByteArrayOutputStream;
      toCopy.currentPosition = i;
    }
  }
  
  private static class LengthRecordNode
  {
    ByteArrayOutputStream currentMessage = null;
    public int currentPosition = 0;
    public LengthRecordNode next = null;
    
    private LengthRecordNode() {}
  }
  
  private class PositionMarker
  {
    private int c_pos;
    private int currentStackSize;
    
    private PositionMarker() {}
    
    int getLength()
    {
      if (currentStackSize == mStack.stackSize) {
        return mPosition - c_pos;
      }
      throw new RuntimeException("BUG: Invalid call to getLength()");
    }
  }
}
