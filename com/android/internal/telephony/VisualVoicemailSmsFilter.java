package com.android.internal.telephony;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.telecom.PhoneAccountHandle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.VisualVoicemailSms.Builder;
import android.telephony.VisualVoicemailSmsFilterSettings;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VisualVoicemailSmsFilter
{
  private static final PhoneAccountHandleConverter DEFAULT_PHONE_ACCOUNT_HANDLE_CONVERTER = new PhoneAccountHandleConverter()
  {
    public PhoneAccountHandle fromSubId(int paramAnonymousInt)
    {
      if (!SubscriptionManager.isValidSubscriptionId(paramAnonymousInt)) {
        return null;
      }
      paramAnonymousInt = SubscriptionManager.getPhoneId(paramAnonymousInt);
      if (paramAnonymousInt == -1) {
        return null;
      }
      return new PhoneAccountHandle(VisualVoicemailSmsFilter.PSTN_CONNECTION_SERVICE_COMPONENT, PhoneFactory.getPhone(paramAnonymousInt).getFullIccSerialNumber());
    }
  };
  private static final ComponentName PSTN_CONNECTION_SERVICE_COMPONENT = new ComponentName("com.android.phone", "com.android.services.telephony.TelephonyConnectionService");
  private static final String TAG = "VvmSmsFilter";
  private static final String TELEPHONY_SERVICE_PACKAGE = "com.android.phone";
  private static Map<String, List<Pattern>> sPatterns;
  private static PhoneAccountHandleConverter sPhoneAccountHandleConverter = DEFAULT_PHONE_ACCOUNT_HANDLE_CONVERTER;
  
  public VisualVoicemailSmsFilter() {}
  
  private static void buildPatternsMap(Context paramContext)
  {
    if (sPatterns != null) {
      return;
    }
    sPatterns = new ArrayMap();
    for (Object localObject1 : paramContext.getResources().getStringArray(17236056))
    {
      paramContext = localObject1.split(";")[0].split(",");
      localObject1 = Pattern.compile(localObject1.split(";")[1]);
      int k = paramContext.length;
      for (int m = 0; m < k; m++)
      {
        Object localObject2 = paramContext[m];
        if (!sPatterns.containsKey(localObject2)) {
          sPatterns.put(localObject2, new ArrayList());
        }
        ((List)sPatterns.get(localObject2)).add(localObject1);
      }
    }
  }
  
  public static boolean filter(Context paramContext, byte[][] paramArrayOfByte, String paramString, int paramInt1, int paramInt2)
  {
    Object localObject1 = (TelephonyManager)paramContext.getSystemService("phone");
    VisualVoicemailSmsFilterSettings localVisualVoicemailSmsFilterSettings = ((TelephonyManager)localObject1).getActiveVisualVoicemailSmsFilterSettings(paramInt2);
    if (localVisualVoicemailSmsFilterSettings == null) {
      return false;
    }
    PhoneAccountHandle localPhoneAccountHandle = sPhoneAccountHandleConverter.fromSubId(paramInt2);
    if (localPhoneAccountHandle == null)
    {
      paramContext = new StringBuilder();
      paramContext.append("Unable to convert subId ");
      paramContext.append(paramInt2);
      paramContext.append(" to PhoneAccountHandle");
      Log.e("VvmSmsFilter", paramContext.toString());
      return false;
    }
    paramString = getFullMessage(paramArrayOfByte, paramString);
    if (paramString == null)
    {
      Log.i("VvmSmsFilter", "Unparsable SMS received");
      paramArrayOfByte = parseAsciiPduMessage(paramArrayOfByte);
      paramArrayOfByte = VisualVoicemailSmsParser.parseAlternativeFormat(paramArrayOfByte);
      if (paramArrayOfByte != null) {
        sendVvmSmsBroadcast(paramContext, localVisualVoicemailSmsFilterSettings, localPhoneAccountHandle, paramArrayOfByte, null);
      }
      return false;
    }
    paramArrayOfByte = fullMessageBody;
    Object localObject2 = clientPrefix;
    localObject2 = VisualVoicemailSmsParser.parse((String)localObject2, paramArrayOfByte);
    if (localObject2 != null)
    {
      if (destinationPort == -2)
      {
        if (paramInt1 == -1)
        {
          Log.i("VvmSmsFilter", "SMS matching VVM format received but is not a DATA SMS");
          return false;
        }
      }
      else if ((destinationPort != -1) && (destinationPort != paramInt1))
      {
        paramContext = new StringBuilder();
        paramContext.append("SMS matching VVM format received but is not directed to port ");
        paramContext.append(destinationPort);
        Log.i("VvmSmsFilter", paramContext.toString());
        return false;
      }
      if ((!originatingNumbers.isEmpty()) && (!isSmsFromNumbers(firstMessage, originatingNumbers)))
      {
        Log.i("VvmSmsFilter", "SMS matching VVM format received but is not from originating numbers");
        return false;
      }
      sendVvmSmsBroadcast(paramContext, localVisualVoicemailSmsFilterSettings, localPhoneAccountHandle, (VisualVoicemailSmsParser.WrappedMessageData)localObject2, null);
      return true;
    }
    buildPatternsMap(paramContext);
    paramString = ((TelephonyManager)localObject1).getSimOperator(paramInt2);
    paramString = (List)sPatterns.get(paramString);
    if ((paramString != null) && (!paramString.isEmpty()))
    {
      localObject1 = paramString.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        paramString = (Pattern)((Iterator)localObject1).next();
        if (paramString.matcher(paramArrayOfByte).matches())
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Incoming SMS matches pattern ");
          ((StringBuilder)localObject1).append(paramString);
          ((StringBuilder)localObject1).append(" but has illegal format, still dropping as VVM SMS");
          Log.w("VvmSmsFilter", ((StringBuilder)localObject1).toString());
          sendVvmSmsBroadcast(paramContext, localVisualVoicemailSmsFilterSettings, localPhoneAccountHandle, null, paramArrayOfByte);
          return true;
        }
      }
      return false;
    }
    return false;
  }
  
  private static FullMessage getFullMessage(byte[][] paramArrayOfByte, String paramString)
  {
    FullMessage localFullMessage = new FullMessage(null);
    StringBuilder localStringBuilder = new StringBuilder();
    CharsetDecoder localCharsetDecoder = StandardCharsets.UTF_8.newDecoder();
    int i = paramArrayOfByte.length;
    for (int j = 0; j < i; j++)
    {
      SmsMessage localSmsMessage = SmsMessage.createFromPdu(paramArrayOfByte[j], paramString);
      if (localSmsMessage == null) {
        return null;
      }
      if (firstMessage == null) {
        firstMessage = localSmsMessage;
      }
      String str = localSmsMessage.getMessageBody();
      Object localObject = str;
      if (str == null)
      {
        localObject = str;
        if (localSmsMessage.getUserData() != null)
        {
          localObject = ByteBuffer.wrap(localSmsMessage.getUserData());
          try
          {
            localObject = localCharsetDecoder.decode((ByteBuffer)localObject).toString();
          }
          catch (CharacterCodingException paramArrayOfByte)
          {
            return null;
          }
        }
      }
      if (localObject != null) {
        localStringBuilder.append((String)localObject);
      }
    }
    fullMessageBody = localStringBuilder.toString();
    return localFullMessage;
  }
  
  private static boolean isSmsFromNumbers(SmsMessage paramSmsMessage, List<String> paramList)
  {
    if (paramSmsMessage == null)
    {
      Log.e("VvmSmsFilter", "Unable to create SmsMessage from PDU, cannot determine originating number");
      return false;
    }
    paramList = paramList.iterator();
    while (paramList.hasNext()) {
      if (PhoneNumberUtils.compare((String)paramList.next(), paramSmsMessage.getOriginatingAddress())) {
        return true;
      }
    }
    return false;
  }
  
  private static String parseAsciiPduMessage(byte[][] paramArrayOfByte)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = paramArrayOfByte.length;
    for (int j = 0; j < i; j++) {
      localStringBuilder.append(new String(paramArrayOfByte[j], StandardCharsets.US_ASCII));
    }
    return localStringBuilder.toString();
  }
  
  private static void sendVvmSmsBroadcast(Context paramContext, VisualVoicemailSmsFilterSettings paramVisualVoicemailSmsFilterSettings, PhoneAccountHandle paramPhoneAccountHandle, VisualVoicemailSmsParser.WrappedMessageData paramWrappedMessageData, String paramString)
  {
    Log.i("VvmSmsFilter", "VVM SMS received");
    Intent localIntent = new Intent("com.android.internal.provider.action.VOICEMAIL_SMS_RECEIVED");
    VisualVoicemailSms.Builder localBuilder = new VisualVoicemailSms.Builder();
    if (paramWrappedMessageData != null)
    {
      localBuilder.setPrefix(prefix);
      localBuilder.setFields(fields);
    }
    if (paramString != null) {
      localBuilder.setMessageBody(paramString);
    }
    localBuilder.setPhoneAccountHandle(paramPhoneAccountHandle);
    localIntent.putExtra("android.provider.extra.VOICEMAIL_SMS", localBuilder.build());
    localIntent.putExtra("android.provider.extra.TARGET_PACAKGE", packageName);
    localIntent.setPackage("com.android.phone");
    paramContext.sendBroadcast(localIntent);
  }
  
  @VisibleForTesting
  public static void setPhoneAccountHandleConverterForTest(PhoneAccountHandleConverter paramPhoneAccountHandleConverter)
  {
    if (paramPhoneAccountHandleConverter == null) {
      sPhoneAccountHandleConverter = DEFAULT_PHONE_ACCOUNT_HANDLE_CONVERTER;
    } else {
      sPhoneAccountHandleConverter = paramPhoneAccountHandleConverter;
    }
  }
  
  private static class FullMessage
  {
    public SmsMessage firstMessage;
    public String fullMessageBody;
    
    private FullMessage() {}
  }
  
  @VisibleForTesting
  public static abstract interface PhoneAccountHandleConverter
  {
    public abstract PhoneAccountHandle fromSubId(int paramInt);
  }
}
