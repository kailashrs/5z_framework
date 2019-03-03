package com.android.internal.telephony;

import android.provider.Telephony.Mms;
import android.telephony.SmsMessage;
import android.text.Emoji;
import java.text.BreakIterator;
import java.util.Arrays;

public abstract class SmsMessageBase
{
  protected String mEmailBody;
  protected String mEmailFrom;
  protected int mIndexOnIcc = -1;
  protected boolean mIsEmail;
  protected boolean mIsMwi;
  protected String mMessageBody;
  public int mMessageRef;
  protected boolean mMwiDontStore;
  protected boolean mMwiSense;
  protected SmsAddress mOriginatingAddress;
  protected byte[] mPdu;
  protected String mPseudoSubject;
  protected SmsAddress mRecipientAddress;
  protected String mScAddress;
  protected long mScTimeMillis;
  protected int mStatusOnIcc = -1;
  protected byte[] mUserData;
  protected SmsHeader mUserDataHeader;
  
  public SmsMessageBase() {}
  
  public static GsmAlphabet.TextEncodingDetails calcUnicodeEncodingDetails(CharSequence paramCharSequence)
  {
    GsmAlphabet.TextEncodingDetails localTextEncodingDetails = new GsmAlphabet.TextEncodingDetails();
    int i = paramCharSequence.length() * 2;
    codeUnitSize = 3;
    codeUnitCount = paramCharSequence.length();
    if (i > 140)
    {
      int j = 134;
      int k = j;
      if (!SmsMessage.hasEmsSupport())
      {
        k = j;
        if (i <= 9 * ('' - 2)) {
          k = '' - 2;
        }
      }
      i = 0;
      for (j = 0; i < paramCharSequence.length(); j++)
      {
        int m = findNextUnicodePosition(i, k, paramCharSequence);
        if (m == paramCharSequence.length()) {
          codeUnitsRemaining = (k / 2 + i - paramCharSequence.length());
        }
        i = m;
      }
      msgCount = j;
    }
    else
    {
      msgCount = 1;
      codeUnitsRemaining = ((140 - i) / 2);
    }
    return localTextEncodingDetails;
  }
  
  public static int findNextUnicodePosition(int paramInt1, int paramInt2, CharSequence paramCharSequence)
  {
    int i = Math.min(paramInt2 / 2 + paramInt1, paramCharSequence.length());
    paramInt2 = i;
    if (i < paramCharSequence.length())
    {
      BreakIterator localBreakIterator = BreakIterator.getCharacterInstance();
      localBreakIterator.setText(paramCharSequence.toString());
      paramInt2 = i;
      if (!localBreakIterator.isBoundary(i))
      {
        for (paramInt2 = localBreakIterator.preceding(i); (paramInt2 + 4 <= i) && (Emoji.isRegionalIndicatorSymbol(Character.codePointAt(paramCharSequence, paramInt2))) && (Emoji.isRegionalIndicatorSymbol(Character.codePointAt(paramCharSequence, paramInt2 + 2))); paramInt2 += 4) {}
        if (paramInt2 <= paramInt1)
        {
          paramInt2 = i;
          if (Character.isHighSurrogate(paramCharSequence.charAt(i - 1))) {
            paramInt2 = i - 1;
          }
        }
      }
    }
    return paramInt2;
  }
  
  protected void extractEmailAddressFromMessageBody()
  {
    String[] arrayOfString = mMessageBody.split("( /)|( )", 2);
    if (arrayOfString.length < 2) {
      return;
    }
    mEmailFrom = arrayOfString[0];
    mEmailBody = arrayOfString[1];
    mIsEmail = Telephony.Mms.isEmailAddress(mEmailFrom);
  }
  
  public String getDisplayMessageBody()
  {
    if (mIsEmail) {
      return mEmailBody;
    }
    return getMessageBody();
  }
  
  public String getDisplayOriginatingAddress()
  {
    if (mIsEmail) {
      return mEmailFrom;
    }
    return getOriginatingAddress();
  }
  
  public String getEmailBody()
  {
    return mEmailBody;
  }
  
  public String getEmailFrom()
  {
    return mEmailFrom;
  }
  
  public int getIndexOnIcc()
  {
    return mIndexOnIcc;
  }
  
  public String getMessageBody()
  {
    return mMessageBody;
  }
  
  public abstract SmsConstants.MessageClass getMessageClass();
  
  public String getOriginatingAddress()
  {
    if (mOriginatingAddress == null) {
      return null;
    }
    return mOriginatingAddress.getAddressString();
  }
  
  public byte[] getPdu()
  {
    return mPdu;
  }
  
  public abstract int getProtocolIdentifier();
  
  public String getPseudoSubject()
  {
    String str;
    if (mPseudoSubject == null) {
      str = "";
    } else {
      str = mPseudoSubject;
    }
    return str;
  }
  
  public String getRecipientAddress()
  {
    if (mRecipientAddress == null) {
      return null;
    }
    return mRecipientAddress.getAddressString();
  }
  
  public String getServiceCenterAddress()
  {
    return mScAddress;
  }
  
  public abstract int getStatus();
  
  public int getStatusOnIcc()
  {
    return mStatusOnIcc;
  }
  
  public long getTimestampMillis()
  {
    return mScTimeMillis;
  }
  
  public byte[] getUserData()
  {
    return mUserData;
  }
  
  public SmsHeader getUserDataHeader()
  {
    return mUserDataHeader;
  }
  
  public abstract boolean isCphsMwiMessage();
  
  public boolean isEmail()
  {
    return mIsEmail;
  }
  
  public abstract boolean isMWIClearMessage();
  
  public abstract boolean isMWISetMessage();
  
  public abstract boolean isMwiDontStore();
  
  public abstract boolean isReplace();
  
  public abstract boolean isReplyPathPresent();
  
  public abstract boolean isStatusReportMessage();
  
  protected void parseMessageBody()
  {
    if ((mOriginatingAddress != null) && (mOriginatingAddress.couldBeEmailGateway())) {
      extractEmailAddressFromMessageBody();
    }
  }
  
  public static abstract class SubmitPduBase
  {
    public byte[] encodedMessage;
    public byte[] encodedScAddress;
    
    public SubmitPduBase() {}
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("SubmitPdu: encodedScAddress = ");
      localStringBuilder.append(Arrays.toString(encodedScAddress));
      localStringBuilder.append(", encodedMessage = ");
      localStringBuilder.append(Arrays.toString(encodedMessage));
      return localStringBuilder.toString();
    }
  }
}
