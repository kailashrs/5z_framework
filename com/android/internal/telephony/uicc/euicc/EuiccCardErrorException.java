package com.android.internal.telephony.uicc.euicc;

import com.android.internal.telephony.uicc.asn1.Asn1Node;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class EuiccCardErrorException
  extends EuiccCardException
{
  public static final int OPERATION_AUTHENTICATE_SERVER = 3;
  public static final int OPERATION_CANCEL_SESSION = 4;
  public static final int OPERATION_DELETE_PROFILE = 12;
  public static final int OPERATION_DISABLE_PROFILE = 11;
  public static final int OPERATION_GET_PROFILE = 1;
  public static final int OPERATION_LIST_NOTIFICATIONS = 6;
  public static final int OPERATION_LOAD_BOUND_PROFILE_PACKAGE = 5;
  public static final int OPERATION_PREPARE_DOWNLOAD = 2;
  public static final int OPERATION_REMOVE_NOTIFICATION_FROM_LIST = 9;
  public static final int OPERATION_RESET_MEMORY = 13;
  public static final int OPERATION_RETRIEVE_NOTIFICATION = 8;
  public static final int OPERATION_SET_DEFAULT_SMDP_ADDRESS = 14;
  public static final int OPERATION_SET_NICKNAME = 7;
  public static final int OPERATION_SWITCH_TO_PROFILE = 10;
  public static final int OPERATION_UNKNOWN = 0;
  private final int mErrorCode;
  private final Asn1Node mErrorDetails;
  private final int mOperationCode;
  
  public EuiccCardErrorException(int paramInt1, int paramInt2)
  {
    mOperationCode = paramInt1;
    mErrorCode = paramInt2;
    mErrorDetails = null;
  }
  
  public EuiccCardErrorException(int paramInt1, int paramInt2, Asn1Node paramAsn1Node)
  {
    mOperationCode = paramInt1;
    mErrorCode = paramInt2;
    mErrorDetails = paramAsn1Node;
  }
  
  public int getErrorCode()
  {
    return mErrorCode;
  }
  
  public Asn1Node getErrorDetails()
  {
    return mErrorDetails;
  }
  
  public String getMessage()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("EuiccCardError: mOperatorCode=");
    localStringBuilder.append(mOperationCode);
    localStringBuilder.append(", mErrorCode=");
    localStringBuilder.append(mErrorCode);
    localStringBuilder.append(", errorDetails=");
    String str;
    if (mErrorDetails == null) {
      str = "null";
    } else {
      str = mErrorDetails.toHex();
    }
    localStringBuilder.append(str);
    return localStringBuilder.toString();
  }
  
  public int getOperationCode()
  {
    return mOperationCode;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface OperationCode {}
}
