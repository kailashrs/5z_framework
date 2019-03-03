package com.android.internal.telephony.uicc.euicc;

import android.telephony.Rlog;
import com.android.internal.telephony.uicc.asn1.Asn1Decoder;
import com.android.internal.telephony.uicc.asn1.Asn1Node;
import com.android.internal.telephony.uicc.asn1.InvalidAsn1DataException;
import com.android.internal.telephony.uicc.asn1.TagNotFoundException;
import java.util.Arrays;

public final class EuiccSpecVersion
  implements Comparable<EuiccSpecVersion>
{
  private static final String LOG_TAG = "EuiccSpecVer";
  private static final int TAG_ISD_R_APP_TEMPLATE = 224;
  private static final int TAG_VERSION = 130;
  private final int[] mVersionValues = new int[3];
  
  public EuiccSpecVersion(int paramInt1, int paramInt2, int paramInt3)
  {
    mVersionValues[0] = paramInt1;
    mVersionValues[1] = paramInt2;
    mVersionValues[2] = paramInt3;
  }
  
  public EuiccSpecVersion(byte[] paramArrayOfByte)
  {
    mVersionValues[0] = (paramArrayOfByte[0] & 0xFF);
    mVersionValues[1] = (paramArrayOfByte[1] & 0xFF);
    mVersionValues[2] = (paramArrayOfByte[2] & 0xFF);
  }
  
  public static EuiccSpecVersion fromOpenChannelResponse(byte[] paramArrayOfByte)
  {
    try
    {
      Object localObject = new com/android/internal/telephony/uicc/asn1/Asn1Decoder;
      ((Asn1Decoder)localObject).<init>(paramArrayOfByte);
      if (!((Asn1Decoder)localObject).hasNextNode()) {
        return null;
      }
      localObject = ((Asn1Decoder)localObject).nextNode();
      try
      {
        if (((Asn1Node)localObject).getTag() == 224) {
          paramArrayOfByte = ((Asn1Node)localObject).getChild(130, new int[0]).asBytes();
        } else {
          paramArrayOfByte = ((Asn1Node)localObject).getChild(224, new int[] { 130 }).asBytes();
        }
        if (paramArrayOfByte.length == 3) {
          return new EuiccSpecVersion(paramArrayOfByte);
        }
        paramArrayOfByte = new java/lang/StringBuilder;
        paramArrayOfByte.<init>();
        paramArrayOfByte.append("Cannot parse select response of ISD-R: ");
        paramArrayOfByte.append(((Asn1Node)localObject).toHex());
        Rlog.e("EuiccSpecVer", paramArrayOfByte.toString());
      }
      catch (InvalidAsn1DataException|TagNotFoundException paramArrayOfByte)
      {
        paramArrayOfByte = new StringBuilder();
        paramArrayOfByte.append("Cannot parse select response of ISD-R: ");
        paramArrayOfByte.append(((Asn1Node)localObject).toHex());
        Rlog.e("EuiccSpecVer", paramArrayOfByte.toString());
      }
      return null;
    }
    catch (InvalidAsn1DataException paramArrayOfByte)
    {
      Rlog.e("EuiccSpecVer", "Cannot parse the select response of ISD-R.", paramArrayOfByte);
    }
    return null;
  }
  
  public int compareTo(EuiccSpecVersion paramEuiccSpecVersion)
  {
    if (getMajor() > paramEuiccSpecVersion.getMajor()) {
      return 1;
    }
    if (getMajor() < paramEuiccSpecVersion.getMajor()) {
      return -1;
    }
    if (getMinor() > paramEuiccSpecVersion.getMinor()) {
      return 1;
    }
    if (getMinor() < paramEuiccSpecVersion.getMinor()) {
      return -1;
    }
    if (getRevision() > paramEuiccSpecVersion.getRevision()) {
      return 1;
    }
    if (getRevision() < paramEuiccSpecVersion.getRevision()) {
      return -1;
    }
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass())) {
      return Arrays.equals(mVersionValues, mVersionValues);
    }
    return false;
  }
  
  public int getMajor()
  {
    return mVersionValues[0];
  }
  
  public int getMinor()
  {
    return mVersionValues[1];
  }
  
  public int getRevision()
  {
    return mVersionValues[2];
  }
  
  public int hashCode()
  {
    return Arrays.hashCode(mVersionValues);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(mVersionValues[0]);
    localStringBuilder.append(".");
    localStringBuilder.append(mVersionValues[1]);
    localStringBuilder.append(".");
    localStringBuilder.append(mVersionValues[2]);
    return localStringBuilder.toString();
  }
}
