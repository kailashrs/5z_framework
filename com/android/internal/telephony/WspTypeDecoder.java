package com.android.internal.telephony;

import java.util.HashMap;

public class WspTypeDecoder
{
  public static final String CONTENT_TYPE_B_MMS = "application/vnd.wap.mms-message";
  public static final String CONTENT_TYPE_B_PUSH_CO = "application/vnd.wap.coc";
  public static final String CONTENT_TYPE_B_PUSH_SYNCML_NOTI = "application/vnd.syncml.notification";
  public static final int PARAMETER_ID_X_WAP_APPLICATION_ID = 47;
  public static final int PDU_TYPE_CONFIRMED_PUSH = 7;
  public static final int PDU_TYPE_PUSH = 6;
  private static final int Q_VALUE = 0;
  private static final int WAP_PDU_LENGTH_QUOTE = 31;
  private static final int WAP_PDU_SHORT_LENGTH_MAX = 30;
  private static final HashMap<Integer, String> WELL_KNOWN_MIME_TYPES = new HashMap();
  private static final HashMap<Integer, String> WELL_KNOWN_PARAMETERS = new HashMap();
  HashMap<String, String> mContentParameters;
  int mDataLength;
  String mStringValue;
  long mUnsigned32bit;
  byte[] mWspData;
  
  static
  {
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(0), "*/*");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(1), "text/*");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(2), "text/html");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(3), "text/plain");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(4), "text/x-hdml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(5), "text/x-ttml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(6), "text/x-vCalendar");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(7), "text/x-vCard");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(8), "text/vnd.wap.wml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(9), "text/vnd.wap.wmlscript");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(10), "text/vnd.wap.wta-event");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(11), "multipart/*");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(12), "multipart/mixed");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(13), "multipart/form-data");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(14), "multipart/byterantes");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(15), "multipart/alternative");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(16), "application/*");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(17), "application/java-vm");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(18), "application/x-www-form-urlencoded");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(19), "application/x-hdmlc");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(20), "application/vnd.wap.wmlc");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(21), "application/vnd.wap.wmlscriptc");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(22), "application/vnd.wap.wta-eventc");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(23), "application/vnd.wap.uaprof");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(24), "application/vnd.wap.wtls-ca-certificate");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(25), "application/vnd.wap.wtls-user-certificate");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(26), "application/x-x509-ca-cert");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(27), "application/x-x509-user-cert");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(28), "image/*");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(29), "image/gif");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(30), "image/jpeg");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(31), "image/tiff");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(32), "image/png");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(33), "image/vnd.wap.wbmp");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(34), "application/vnd.wap.multipart.*");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(35), "application/vnd.wap.multipart.mixed");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(36), "application/vnd.wap.multipart.form-data");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(37), "application/vnd.wap.multipart.byteranges");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(38), "application/vnd.wap.multipart.alternative");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(39), "application/xml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(40), "text/xml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(41), "application/vnd.wap.wbxml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(42), "application/x-x968-cross-cert");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(43), "application/x-x968-ca-cert");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(44), "application/x-x968-user-cert");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(45), "text/vnd.wap.si");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(46), "application/vnd.wap.sic");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(47), "text/vnd.wap.sl");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(48), "application/vnd.wap.slc");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(49), "text/vnd.wap.co");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(50), "application/vnd.wap.coc");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(51), "application/vnd.wap.multipart.related");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(52), "application/vnd.wap.sia");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(53), "text/vnd.wap.connectivity-xml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(54), "application/vnd.wap.connectivity-wbxml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(55), "application/pkcs7-mime");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(56), "application/vnd.wap.hashed-certificate");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(57), "application/vnd.wap.signed-certificate");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(58), "application/vnd.wap.cert-response");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(59), "application/xhtml+xml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(60), "application/wml+xml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(61), "text/css");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(62), "application/vnd.wap.mms-message");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(63), "application/vnd.wap.rollover-certificate");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(64), "application/vnd.wap.locc+wbxml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(65), "application/vnd.wap.loc+xml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(66), "application/vnd.syncml.dm+wbxml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(67), "application/vnd.syncml.dm+xml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(68), "application/vnd.syncml.notification");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(69), "application/vnd.wap.xhtml+xml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(70), "application/vnd.wv.csp.cir");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(71), "application/vnd.oma.dd+xml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(72), "application/vnd.oma.drm.message");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(73), "application/vnd.oma.drm.content");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(74), "application/vnd.oma.drm.rights+xml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(75), "application/vnd.oma.drm.rights+wbxml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(76), "application/vnd.wv.csp+xml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(77), "application/vnd.wv.csp+wbxml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(78), "application/vnd.syncml.ds.notification");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(79), "audio/*");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(80), "video/*");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(81), "application/vnd.oma.dd2+xml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(82), "application/mikey");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(83), "application/vnd.oma.dcd");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(84), "application/vnd.oma.dcdc");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(513), "application/vnd.uplanet.cacheop-wbxml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(514), "application/vnd.uplanet.signal");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(515), "application/vnd.uplanet.alert-wbxml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(516), "application/vnd.uplanet.list-wbxml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(517), "application/vnd.uplanet.listcmd-wbxml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(518), "application/vnd.uplanet.channel-wbxml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(519), "application/vnd.uplanet.provisioning-status-uri");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(520), "x-wap.multipart/vnd.uplanet.header-set");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(521), "application/vnd.uplanet.bearer-choice-wbxml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(522), "application/vnd.phonecom.mmc-wbxml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(523), "application/vnd.nokia.syncset+wbxml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(524), "image/x-up-wpng");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(768), "application/iota.mmc-wbxml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(769), "application/iota.mmc-xml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(770), "application/vnd.syncml+xml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(771), "application/vnd.syncml+wbxml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(772), "text/vnd.wap.emn+xml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(773), "text/calendar");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(774), "application/vnd.omads-email+xml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(775), "application/vnd.omads-file+xml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(776), "application/vnd.omads-folder+xml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(777), "text/directory;profile=vCard");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(778), "application/vnd.wap.emn+wbxml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(779), "application/vnd.nokia.ipdc-purchase-response");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(780), "application/vnd.motorola.screen3+xml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(781), "application/vnd.motorola.screen3+gzip");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(782), "application/vnd.cmcc.setting+wbxml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(783), "application/vnd.cmcc.bombing+wbxml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(784), "application/vnd.docomo.pf");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(785), "application/vnd.docomo.ub");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(786), "application/vnd.omaloc-supl-init");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(787), "application/vnd.oma.group-usage-list+xml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(788), "application/oma-directory+xml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(789), "application/vnd.docomo.pf2");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(790), "application/vnd.oma.drm.roap-trigger+wbxml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(791), "application/vnd.sbm.mid2");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(792), "application/vnd.wmf.bootstrap");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(793), "application/vnc.cmcc.dcd+xml");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(794), "application/vnd.sbm.cid");
    WELL_KNOWN_MIME_TYPES.put(Integer.valueOf(795), "application/vnd.oma.bcast.provisioningtrigger");
    WELL_KNOWN_PARAMETERS.put(Integer.valueOf(0), "Q");
    WELL_KNOWN_PARAMETERS.put(Integer.valueOf(1), "Charset");
    WELL_KNOWN_PARAMETERS.put(Integer.valueOf(2), "Level");
    WELL_KNOWN_PARAMETERS.put(Integer.valueOf(3), "Type");
    WELL_KNOWN_PARAMETERS.put(Integer.valueOf(7), "Differences");
    WELL_KNOWN_PARAMETERS.put(Integer.valueOf(8), "Padding");
    WELL_KNOWN_PARAMETERS.put(Integer.valueOf(9), "Type");
    WELL_KNOWN_PARAMETERS.put(Integer.valueOf(14), "Max-Age");
    WELL_KNOWN_PARAMETERS.put(Integer.valueOf(16), "Secure");
    WELL_KNOWN_PARAMETERS.put(Integer.valueOf(17), "SEC");
    WELL_KNOWN_PARAMETERS.put(Integer.valueOf(18), "MAC");
    WELL_KNOWN_PARAMETERS.put(Integer.valueOf(19), "Creation-date");
    WELL_KNOWN_PARAMETERS.put(Integer.valueOf(20), "Modification-date");
    WELL_KNOWN_PARAMETERS.put(Integer.valueOf(21), "Read-date");
    WELL_KNOWN_PARAMETERS.put(Integer.valueOf(22), "Size");
    WELL_KNOWN_PARAMETERS.put(Integer.valueOf(23), "Name");
    WELL_KNOWN_PARAMETERS.put(Integer.valueOf(24), "Filename");
    WELL_KNOWN_PARAMETERS.put(Integer.valueOf(25), "Start");
    WELL_KNOWN_PARAMETERS.put(Integer.valueOf(26), "Start-info");
    WELL_KNOWN_PARAMETERS.put(Integer.valueOf(27), "Comment");
    WELL_KNOWN_PARAMETERS.put(Integer.valueOf(28), "Domain");
    WELL_KNOWN_PARAMETERS.put(Integer.valueOf(29), "Path");
  }
  
  public WspTypeDecoder(byte[] paramArrayOfByte)
  {
    mWspData = paramArrayOfByte;
  }
  
  private boolean decodeNoValue(int paramInt)
  {
    if (mWspData[paramInt] == 0)
    {
      mDataLength = 1;
      return true;
    }
    return false;
  }
  
  private void expandWellKnownMimeType()
  {
    if (mStringValue == null)
    {
      int i = (int)mUnsigned32bit;
      mStringValue = ((String)WELL_KNOWN_MIME_TYPES.get(Integer.valueOf(i)));
    }
    else
    {
      mUnsigned32bit = -1L;
    }
  }
  
  private boolean readContentParameters(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt2 > 0)
    {
      int i = mWspData[paramInt1];
      Object localObject1;
      if (((i & 0x80) == 0) && (i > 31))
      {
        decodeTokenText(paramInt1);
        localObject1 = mStringValue;
        i = 0 + mDataLength;
      }
      else
      {
        if (!decodeIntegerValue(paramInt1)) {
          break label339;
        }
        i = 0 + mDataLength;
        int j = (int)mUnsigned32bit;
        localObject1 = (String)WELL_KNOWN_PARAMETERS.get(Integer.valueOf(j));
        localObject2 = localObject1;
        if (localObject1 == null)
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("unassigned/0x");
          ((StringBuilder)localObject2).append(Long.toHexString(j));
          localObject2 = ((StringBuilder)localObject2).toString();
        }
        if (j == 0)
        {
          if (decodeUintvarInteger(paramInt1 + i))
          {
            i += mDataLength;
            long l = mUnsigned32bit;
            mContentParameters.put(localObject2, String.valueOf(l));
            return readContentParameters(paramInt1 + i, paramInt2 - i, paramInt3 + i);
          }
          return false;
        }
        localObject1 = localObject2;
      }
      if (decodeNoValue(paramInt1 + i)) {
        i += mDataLength;
      }
      for (Object localObject2 = null;; localObject2 = ((String)localObject2).substring(1))
      {
        break;
        if (decodeIntegerValue(paramInt1 + i))
        {
          i += mDataLength;
          localObject2 = String.valueOf((int)mUnsigned32bit);
          break;
        }
        decodeTokenText(paramInt1 + i);
        i += mDataLength;
        localObject2 = mStringValue;
        if (!((String)localObject2).startsWith("\"")) {
          break;
        }
      }
      mContentParameters.put(localObject1, localObject2);
      return readContentParameters(paramInt1 + i, paramInt2 - i, paramInt3 + i);
      label339:
      return false;
    }
    mDataLength = paramInt3;
    return true;
  }
  
  public boolean decodeConstrainedEncoding(int paramInt)
  {
    if (decodeShortInteger(paramInt) == true)
    {
      mStringValue = null;
      return true;
    }
    return decodeExtensionMedia(paramInt);
  }
  
  public boolean decodeContentLength(int paramInt)
  {
    return decodeIntegerValue(paramInt);
  }
  
  public boolean decodeContentLocation(int paramInt)
  {
    return decodeTextString(paramInt);
  }
  
  public boolean decodeContentType(int paramInt)
  {
    mContentParameters = new HashMap();
    try
    {
      if (!decodeValueLength(paramInt))
      {
        boolean bool = decodeConstrainedEncoding(paramInt);
        if (bool) {
          expandWellKnownMimeType();
        }
        return bool;
      }
      int i = (int)mUnsigned32bit;
      int j = getDecodedDataLength();
      int k;
      long l;
      String str;
      if (decodeIntegerValue(paramInt + j) == true)
      {
        mDataLength += j;
        k = mDataLength;
        mStringValue = null;
        expandWellKnownMimeType();
        l = mUnsigned32bit;
        str = mStringValue;
        if (readContentParameters(mDataLength + paramInt, i - (mDataLength - j), 0))
        {
          mDataLength += k;
          mUnsigned32bit = l;
          mStringValue = str;
          return true;
        }
        return false;
      }
      if (decodeExtensionMedia(paramInt + j) == true)
      {
        mDataLength += j;
        k = mDataLength;
        expandWellKnownMimeType();
        l = mUnsigned32bit;
        str = mStringValue;
        if (readContentParameters(mDataLength + paramInt, i - (mDataLength - j), 0))
        {
          mDataLength += k;
          mUnsigned32bit = l;
          mStringValue = str;
          return true;
        }
      }
      return false;
    }
    catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {}
    return false;
  }
  
  public boolean decodeExtensionMedia(int paramInt)
  {
    int i = paramInt;
    boolean bool = false;
    mDataLength = 0;
    mStringValue = null;
    int j = mWspData.length;
    int k = i;
    if (i < j) {
      bool = true;
    }
    for (k = i; (k < j) && (mWspData[k] != 0); k++) {}
    mDataLength = (k - paramInt + 1);
    mStringValue = new String(mWspData, paramInt, mDataLength - 1);
    return bool;
  }
  
  public boolean decodeIntegerValue(int paramInt)
  {
    if (decodeShortInteger(paramInt) == true) {
      return true;
    }
    return decodeLongInteger(paramInt);
  }
  
  public boolean decodeLongInteger(int paramInt)
  {
    int i = mWspData[paramInt] & 0xFF;
    if (i > 30) {
      return false;
    }
    mUnsigned32bit = 0L;
    for (int j = 1; j <= i; j++) {
      mUnsigned32bit = (mUnsigned32bit << 8 | mWspData[(paramInt + j)] & 0xFF);
    }
    mDataLength = (1 + i);
    return true;
  }
  
  public boolean decodeShortInteger(int paramInt)
  {
    if ((mWspData[paramInt] & 0x80) == 0) {
      return false;
    }
    mUnsigned32bit = (mWspData[paramInt] & 0x7F);
    mDataLength = 1;
    return true;
  }
  
  public boolean decodeTextString(int paramInt)
  {
    for (int i = paramInt; mWspData[i] != 0; i++) {}
    mDataLength = (i - paramInt + 1);
    if (mWspData[paramInt] == Byte.MAX_VALUE) {
      mStringValue = new String(mWspData, paramInt + 1, mDataLength - 2);
    } else {
      mStringValue = new String(mWspData, paramInt, mDataLength - 1);
    }
    return true;
  }
  
  public boolean decodeTokenText(int paramInt)
  {
    for (int i = paramInt; mWspData[i] != 0; i++) {}
    mDataLength = (i - paramInt + 1);
    mStringValue = new String(mWspData, paramInt, mDataLength - 1);
    return true;
  }
  
  public boolean decodeUintvarInteger(int paramInt)
  {
    int i = paramInt;
    mUnsigned32bit = 0L;
    while ((mWspData[i] & 0x80) != 0)
    {
      if (i - paramInt >= 4) {
        return false;
      }
      mUnsigned32bit = (mUnsigned32bit << 7 | mWspData[i] & 0x7F);
      i++;
    }
    mUnsigned32bit = (mUnsigned32bit << 7 | mWspData[i] & 0x7F);
    mDataLength = (i - paramInt + 1);
    return true;
  }
  
  public boolean decodeValueLength(int paramInt)
  {
    if ((mWspData[paramInt] & 0xFF) > 31) {
      return false;
    }
    if (mWspData[paramInt] < 31)
    {
      mUnsigned32bit = mWspData[paramInt];
      mDataLength = 1;
    }
    else
    {
      decodeUintvarInteger(paramInt + 1);
      mDataLength += 1;
    }
    return true;
  }
  
  public boolean decodeXWapApplicationId(int paramInt)
  {
    if (decodeIntegerValue(paramInt) == true)
    {
      mStringValue = null;
      return true;
    }
    return decodeTextString(paramInt);
  }
  
  public boolean decodeXWapContentURI(int paramInt)
  {
    return decodeTextString(paramInt);
  }
  
  public boolean decodeXWapInitiatorURI(int paramInt)
  {
    return decodeTextString(paramInt);
  }
  
  public HashMap<String, String> getContentParameters()
  {
    return mContentParameters;
  }
  
  public int getDecodedDataLength()
  {
    return mDataLength;
  }
  
  public long getValue32()
  {
    return mUnsigned32bit;
  }
  
  public String getValueString()
  {
    return mStringValue;
  }
  
  public boolean seekXWapApplicationId(int paramInt1, int paramInt2)
  {
    while (paramInt1 <= paramInt2) {
      try
      {
        if (decodeIntegerValue(paramInt1))
        {
          if ((int)getValue32() == 47)
          {
            mUnsigned32bit = (paramInt1 + 1);
            return true;
          }
        }
        else if (!decodeTextString(paramInt1)) {
          return false;
        }
        paramInt1 += getDecodedDataLength();
        if (paramInt1 > paramInt2) {
          return false;
        }
        int i = mWspData[paramInt1];
        if ((i >= 0) && (i <= 30))
        {
          paramInt1 += mWspData[paramInt1] + 1;
        }
        else if (i == 31)
        {
          if (paramInt1 + 1 >= paramInt2) {
            return false;
          }
          paramInt1++;
          if (!decodeUintvarInteger(paramInt1)) {
            return false;
          }
          paramInt1 += getDecodedDataLength();
        }
        else if ((31 < i) && (i <= 127))
        {
          if (!decodeTextString(paramInt1)) {
            return false;
          }
          i = getDecodedDataLength();
          paramInt1 += i;
        }
        else
        {
          paramInt1++;
        }
      }
      catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
      {
        return false;
      }
    }
    return false;
  }
}
