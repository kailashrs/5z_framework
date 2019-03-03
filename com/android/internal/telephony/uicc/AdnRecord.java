package com.android.internal.telephony.uicc;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.text.TextUtils;
import java.util.Arrays;
import java.util.List;

public class AdnRecord
  implements Parcelable
{
  static final int ADN_BCD_NUMBER_LENGTH = 0;
  static final int ADN_CAPABILITY_ID = 12;
  static final int ADN_DIALING_NUMBER_END = 11;
  static final int ADN_DIALING_NUMBER_START = 2;
  static final int ADN_EXTENSION_ID = 13;
  static final int ADN_TON_AND_NPI = 1;
  public static final Parcelable.Creator<AdnRecord> CREATOR = new Parcelable.Creator()
  {
    public AdnRecord createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AdnRecord(paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readString(), paramAnonymousParcel.readString(), paramAnonymousParcel.readStringArray(), paramAnonymousParcel.readStringArray());
    }
    
    public AdnRecord[] newArray(int paramAnonymousInt)
    {
      return new AdnRecord[paramAnonymousInt];
    }
  };
  static final int EXT_RECORD_LENGTH_BYTES = 13;
  static final int EXT_RECORD_TYPE_ADDITIONAL_DATA = 2;
  static final int EXT_RECORD_TYPE_MASK = 3;
  static final int FOOTER_SIZE_BYTES = 14;
  static final String LOG_TAG = "AdnRecord";
  static final int MAX_EXT_CALLED_PARTY_LENGTH = 10;
  static final int MAX_NUMBER_SIZE_BYTES = 11;
  String[] mAdditionalNumbers = null;
  String mAlphaTag = null;
  int mEfid;
  String[] mEmails;
  int mExtRecord = 255;
  String mNumber = null;
  int mRecordNumber;
  
  public AdnRecord(int paramInt1, int paramInt2, String paramString1, String paramString2)
  {
    mEfid = paramInt1;
    mRecordNumber = paramInt2;
    mAlphaTag = paramString1;
    mNumber = paramString2;
    mEmails = null;
    mAdditionalNumbers = null;
  }
  
  public AdnRecord(int paramInt1, int paramInt2, String paramString1, String paramString2, String[] paramArrayOfString)
  {
    mEfid = paramInt1;
    mRecordNumber = paramInt2;
    mAlphaTag = paramString1;
    mNumber = paramString2;
    mEmails = paramArrayOfString;
    mAdditionalNumbers = null;
  }
  
  public AdnRecord(int paramInt1, int paramInt2, String paramString1, String paramString2, String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    mEfid = paramInt1;
    mRecordNumber = paramInt2;
    mAlphaTag = paramString1;
    mNumber = paramString2;
    mEmails = paramArrayOfString1;
    mAdditionalNumbers = paramArrayOfString2;
  }
  
  public AdnRecord(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    mEfid = paramInt1;
    mRecordNumber = paramInt2;
    parseRecord(paramArrayOfByte);
  }
  
  public AdnRecord(String paramString1, String paramString2)
  {
    this(0, 0, paramString1, paramString2);
  }
  
  public AdnRecord(String paramString1, String paramString2, String[] paramArrayOfString)
  {
    this(0, 0, paramString1, paramString2, paramArrayOfString);
  }
  
  public AdnRecord(String paramString1, String paramString2, String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    this(0, 0, paramString1, paramString2, paramArrayOfString1, paramArrayOfString2);
  }
  
  public AdnRecord(byte[] paramArrayOfByte)
  {
    this(0, 0, paramArrayOfByte);
  }
  
  private static boolean arrayCompareNullEqualsEmpty(String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    if (paramArrayOfString1 == paramArrayOfString2) {
      return true;
    }
    String[] arrayOfString = paramArrayOfString1;
    if (paramArrayOfString1 == null)
    {
      arrayOfString = new String[1];
      arrayOfString[0] = "";
    }
    paramArrayOfString1 = paramArrayOfString2;
    if (paramArrayOfString2 == null)
    {
      paramArrayOfString1 = new String[1];
      paramArrayOfString1[0] = "";
    }
    int i = arrayOfString.length;
    int j = 0;
    while (j < i)
    {
      paramArrayOfString2 = arrayOfString[j];
      if ((TextUtils.isEmpty(paramArrayOfString2)) || (Arrays.asList(paramArrayOfString1).contains(paramArrayOfString2))) {
        j++;
      } else {
        return false;
      }
    }
    i = paramArrayOfString1.length;
    j = 0;
    while (j < i)
    {
      paramArrayOfString2 = paramArrayOfString1[j];
      if ((TextUtils.isEmpty(paramArrayOfString2)) || (Arrays.asList(arrayOfString).contains(paramArrayOfString2))) {
        j++;
      } else {
        return false;
      }
    }
    return true;
  }
  
  private void parseRecord(byte[] paramArrayOfByte)
  {
    try
    {
      mAlphaTag = IccUtils.adnStringFieldToString(paramArrayOfByte, 0, paramArrayOfByte.length - 14);
      int i = paramArrayOfByte.length - 14;
      int j = paramArrayOfByte[i] & 0xFF;
      if (j > 11)
      {
        mNumber = "";
        return;
      }
      mNumber = PhoneNumberUtils.calledPartyBCDToString(paramArrayOfByte, i + 1, j, 1);
      mExtRecord = (0xFF & paramArrayOfByte[(paramArrayOfByte.length - 1)]);
      mEmails = null;
      mAdditionalNumbers = null;
    }
    catch (RuntimeException paramArrayOfByte)
    {
      Rlog.w("AdnRecord", "Error parsing AdnRecord", paramArrayOfByte);
      mNumber = "";
      mAlphaTag = "";
      mEmails = null;
      mAdditionalNumbers = null;
    }
  }
  
  private static boolean stringCompareNullEqualsEmpty(String paramString1, String paramString2)
  {
    if (paramString1 == paramString2) {
      return true;
    }
    String str = paramString1;
    if (paramString1 == null) {
      str = "";
    }
    paramString1 = paramString2;
    if (paramString2 == null) {
      paramString1 = "";
    }
    return str.equals(paramString1);
  }
  
  public void appendExtRecord(byte[] paramArrayOfByte)
  {
    try
    {
      if (paramArrayOfByte.length != 13) {
        return;
      }
      if ((paramArrayOfByte[0] & 0x3) != 2) {
        return;
      }
      if ((paramArrayOfByte[1] & 0xFF) > 10) {
        return;
      }
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append(mNumber);
      localStringBuilder.append(PhoneNumberUtils.calledPartyBCDFragmentToString(paramArrayOfByte, 2, 0xFF & paramArrayOfByte[1], 1));
      mNumber = localStringBuilder.toString();
    }
    catch (RuntimeException paramArrayOfByte)
    {
      Rlog.w("AdnRecord", "Error parsing AdnRecord ext record", paramArrayOfByte);
    }
  }
  
  public byte[] buildAdnString(int paramInt)
  {
    int i = paramInt - 14;
    byte[] arrayOfByte1 = new byte[paramInt];
    for (int j = 0; j < paramInt; j++) {
      arrayOfByte1[j] = ((byte)-1);
    }
    if ((TextUtils.isEmpty(mNumber)) && (TextUtils.isEmpty(mAlphaTag)))
    {
      Rlog.w("AdnRecord", "[buildAdnString] Empty dialing number");
      return arrayOfByte1;
    }
    if ((mNumber != null) && (mNumber.length() > 20))
    {
      Rlog.w("AdnRecord", "[buildAdnString] Max length of dialing number is 20");
      return null;
    }
    Object localObject;
    if (!TextUtils.isEmpty(mAlphaTag)) {
      localObject = IccUtils.stringToAdnStringField(mAlphaTag);
    } else {
      localObject = new byte[0];
    }
    if (localObject.length > i)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("[buildAdnString] Max length of tag is ");
      ((StringBuilder)localObject).append(i);
      Rlog.w("AdnRecord", ((StringBuilder)localObject).toString());
      return null;
    }
    if (!TextUtils.isEmpty(mNumber))
    {
      byte[] arrayOfByte2 = PhoneNumberUtils.numberToCalledPartyBCD(mNumber, 1);
      System.arraycopy(arrayOfByte2, 0, arrayOfByte1, i + 1, arrayOfByte2.length);
      arrayOfByte1[(i + 0)] = ((byte)(byte)arrayOfByte2.length);
    }
    arrayOfByte1[(i + 12)] = ((byte)-1);
    arrayOfByte1[(i + 13)] = ((byte)-1);
    if (localObject.length > 0) {
      System.arraycopy((byte[])localObject, 0, arrayOfByte1, 0, localObject.length);
    }
    return arrayOfByte1;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String[] getAdditionalNumbers()
  {
    return mAdditionalNumbers;
  }
  
  public String getAlphaTag()
  {
    return mAlphaTag;
  }
  
  public int getEfid()
  {
    return mEfid;
  }
  
  public String[] getEmails()
  {
    return mEmails;
  }
  
  public String getNumber()
  {
    return mNumber;
  }
  
  public int getRecId()
  {
    return mRecordNumber;
  }
  
  public int getRecordNumber()
  {
    return mRecordNumber;
  }
  
  public boolean hasExtendedRecord()
  {
    boolean bool;
    if ((mExtRecord != 0) && (mExtRecord != 255)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isEmpty()
  {
    boolean bool;
    if ((TextUtils.isEmpty(mAlphaTag)) && (TextUtils.isEmpty(mNumber)) && (mEmails == null) && (mAdditionalNumbers == null)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isEqual(AdnRecord paramAdnRecord)
  {
    boolean bool;
    if ((stringCompareNullEqualsEmpty(mAlphaTag, mAlphaTag)) && (stringCompareNullEqualsEmpty(mNumber, mNumber)) && (arrayCompareNullEqualsEmpty(mEmails, mEmails)) && (arrayCompareNullEqualsEmpty(mAdditionalNumbers, mAdditionalNumbers))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void setAdditionalNumbers(String[] paramArrayOfString)
  {
    mAdditionalNumbers = paramArrayOfString;
  }
  
  public void setEmails(String[] paramArrayOfString)
  {
    mEmails = paramArrayOfString;
  }
  
  public void setNumber(String paramString)
  {
    mNumber = paramString;
  }
  
  public void setRecordNumber(int paramInt)
  {
    mRecordNumber = paramInt;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ADN Record '");
    localStringBuilder.append(mEfid);
    localStringBuilder.append(", ");
    localStringBuilder.append(mRecordNumber);
    localStringBuilder.append(", ");
    localStringBuilder.append(Rlog.pii("AdnRecord", mAlphaTag));
    localStringBuilder.append("' '");
    localStringBuilder.append(Rlog.pii("AdnRecord", mNumber));
    localStringBuilder.append(" ");
    localStringBuilder.append(Rlog.pii("AdnRecord", mEmails));
    localStringBuilder.append(" ");
    localStringBuilder.append(Rlog.pii("AdnRecord", mAdditionalNumbers));
    localStringBuilder.append("'");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mEfid);
    paramParcel.writeInt(mRecordNumber);
    paramParcel.writeString(mAlphaTag);
    paramParcel.writeString(mNumber);
    paramParcel.writeStringArray(mEmails);
    paramParcel.writeStringArray(mAdditionalNumbers);
  }
}
