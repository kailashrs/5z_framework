package android.net.metrics;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.SparseArray;
import com.android.internal.util.MessageUtils;

public final class DhcpErrorEvent
  implements Parcelable
{
  public static final int BOOTP_TOO_SHORT;
  public static final int BUFFER_UNDERFLOW;
  public static final Parcelable.Creator<DhcpErrorEvent> CREATOR = new Parcelable.Creator()
  {
    public DhcpErrorEvent createFromParcel(Parcel paramAnonymousParcel)
    {
      return new DhcpErrorEvent(paramAnonymousParcel, null);
    }
    
    public DhcpErrorEvent[] newArray(int paramAnonymousInt)
    {
      return new DhcpErrorEvent[paramAnonymousInt];
    }
  };
  public static final int DHCP_BAD_MAGIC_COOKIE;
  public static final int DHCP_ERROR = 4;
  public static final int DHCP_INVALID_OPTION_LENGTH;
  public static final int DHCP_NO_COOKIE;
  public static final int DHCP_NO_MSG_TYPE;
  public static final int DHCP_UNKNOWN_MSG_TYPE;
  public static final int L2_ERROR = 1;
  public static final int L2_TOO_SHORT = makeErrorCode(1, 1);
  public static final int L2_WRONG_ETH_TYPE = makeErrorCode(1, 2);
  public static final int L3_ERROR = 2;
  public static final int L3_INVALID_IP;
  public static final int L3_NOT_IPV4;
  public static final int L3_TOO_SHORT = makeErrorCode(2, 1);
  public static final int L4_ERROR = 3;
  public static final int L4_NOT_UDP;
  public static final int L4_WRONG_PORT;
  public static final int MISC_ERROR = 5;
  public static final int PARSING_ERROR;
  public static final int RECEIVE_ERROR;
  public final int errorCode;
  
  static
  {
    L3_NOT_IPV4 = makeErrorCode(2, 2);
    L3_INVALID_IP = makeErrorCode(2, 3);
    L4_NOT_UDP = makeErrorCode(3, 1);
    L4_WRONG_PORT = makeErrorCode(3, 2);
    BOOTP_TOO_SHORT = makeErrorCode(4, 1);
    DHCP_BAD_MAGIC_COOKIE = makeErrorCode(4, 2);
    DHCP_INVALID_OPTION_LENGTH = makeErrorCode(4, 3);
    DHCP_NO_MSG_TYPE = makeErrorCode(4, 4);
    DHCP_UNKNOWN_MSG_TYPE = makeErrorCode(4, 5);
    DHCP_NO_COOKIE = makeErrorCode(4, 6);
    BUFFER_UNDERFLOW = makeErrorCode(5, 1);
    RECEIVE_ERROR = makeErrorCode(5, 2);
    PARSING_ERROR = makeErrorCode(5, 3);
  }
  
  public DhcpErrorEvent(int paramInt)
  {
    errorCode = paramInt;
  }
  
  private DhcpErrorEvent(Parcel paramParcel)
  {
    errorCode = paramParcel.readInt();
  }
  
  public static int errorCodeWithOption(int paramInt1, int paramInt2)
  {
    return 0xFFFF0000 & paramInt1 | 0xFF & paramInt2;
  }
  
  private static int makeErrorCode(int paramInt1, int paramInt2)
  {
    return paramInt1 << 24 | (0xFF & paramInt2) << 16;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    return String.format("DhcpErrorEvent(%s)", new Object[] { Decoder.constants.get(errorCode) });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(errorCode);
  }
  
  static final class Decoder
  {
    static final SparseArray<String> constants = MessageUtils.findMessageNames(new Class[] { DhcpErrorEvent.class }, new String[] { "L2_", "L3_", "L4_", "BOOTP_", "DHCP_", "BUFFER_", "RECEIVE_", "PARSING_" });
    
    Decoder() {}
  }
}
