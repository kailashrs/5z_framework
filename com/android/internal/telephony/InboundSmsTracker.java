package com.android.internal.telephony;

import android.content.ContentValues;
import android.database.Cursor;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.HexDump;
import java.util.Arrays;
import java.util.Date;

public class InboundSmsTracker
{
  private static final int DEST_PORT_FLAG_3GPP = 131072;
  @VisibleForTesting
  public static final int DEST_PORT_FLAG_3GPP2 = 262144;
  @VisibleForTesting
  public static final int DEST_PORT_FLAG_3GPP2_WAP_PDU = 524288;
  @VisibleForTesting
  public static final int DEST_PORT_FLAG_NO_PORT = 65536;
  private static final int DEST_PORT_MASK = 65535;
  @VisibleForTesting
  public static final String SELECT_BY_DUPLICATE_REFERENCE = "address=? AND reference_number=? AND count=? AND sequence=? AND ((date=? AND message_body=?) OR deleted=0) AND (destination_port & 524288=0)";
  @VisibleForTesting
  public static final String SELECT_BY_DUPLICATE_REFERENCE_3GPP2WAP = "address=? AND reference_number=? AND count=? AND sequence=? AND ((date=? AND message_body=?) OR deleted=0) AND (destination_port & 524288=524288)";
  @VisibleForTesting
  public static final String SELECT_BY_REFERENCE = "address=? AND reference_number=? AND count=? AND (destination_port & 524288=0) AND deleted=0";
  @VisibleForTesting
  public static final String SELECT_BY_REFERENCE_3GPP2WAP = "address=? AND reference_number=? AND count=? AND (destination_port & 524288=524288) AND deleted=0";
  private final String mAddress;
  private String mDeleteWhere;
  private String[] mDeleteWhereArgs;
  private final int mDestPort;
  private final String mDisplayAddress;
  private final boolean mIs3gpp2;
  private final boolean mIs3gpp2WapPdu;
  private final String mMessageBody;
  private final int mMessageCount;
  private final byte[] mPdu;
  private final int mReferenceNumber;
  private final int mSequenceNumber;
  private final long mTimestamp;
  
  public InboundSmsTracker(Cursor paramCursor, boolean paramBoolean)
  {
    mPdu = HexDump.hexStringToByteArray(paramCursor.getString(0));
    int i;
    if (paramCursor.isNull(2))
    {
      mDestPort = -1;
      mIs3gpp2 = paramBoolean;
      mIs3gpp2WapPdu = false;
    }
    else
    {
      i = paramCursor.getInt(2);
      if ((0x20000 & i) != 0) {
        mIs3gpp2 = false;
      } else if ((0x40000 & i) != 0) {
        mIs3gpp2 = true;
      } else {
        mIs3gpp2 = paramBoolean;
      }
      if ((0x80000 & i) != 0) {
        paramBoolean = true;
      } else {
        paramBoolean = false;
      }
      mIs3gpp2WapPdu = paramBoolean;
      mDestPort = getRealDestPort(i);
    }
    mTimestamp = paramCursor.getLong(3);
    mAddress = paramCursor.getString(6);
    mDisplayAddress = paramCursor.getString(9);
    if (paramCursor.getInt(5) == 1)
    {
      long l = paramCursor.getLong(7);
      mReferenceNumber = -1;
      mSequenceNumber = getIndexOffset();
      mMessageCount = 1;
      mDeleteWhere = "_id=?";
      mDeleteWhereArgs = new String[] { Long.toString(l) };
    }
    else
    {
      mReferenceNumber = paramCursor.getInt(4);
      mMessageCount = paramCursor.getInt(5);
      mSequenceNumber = paramCursor.getInt(1);
      i = mSequenceNumber - getIndexOffset();
      if ((i < 0) || (i >= mMessageCount)) {
        break label326;
      }
      mDeleteWhere = getQueryForSegments();
      mDeleteWhereArgs = new String[] { mAddress, Integer.toString(mReferenceNumber), Integer.toString(mMessageCount) };
    }
    mMessageBody = paramCursor.getString(8);
    return;
    label326:
    paramCursor = new StringBuilder();
    paramCursor.append("invalid PDU sequence ");
    paramCursor.append(mSequenceNumber);
    paramCursor.append(" of ");
    paramCursor.append(mMessageCount);
    throw new IllegalArgumentException(paramCursor.toString());
  }
  
  public InboundSmsTracker(byte[] paramArrayOfByte, long paramLong, int paramInt1, boolean paramBoolean1, String paramString1, String paramString2, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean2, String paramString3)
  {
    mPdu = paramArrayOfByte;
    mTimestamp = paramLong;
    mDestPort = paramInt1;
    mIs3gpp2 = paramBoolean1;
    mIs3gpp2WapPdu = paramBoolean2;
    mMessageBody = paramString3;
    mDisplayAddress = paramString2;
    mAddress = paramString1;
    mReferenceNumber = paramInt2;
    mSequenceNumber = paramInt3;
    mMessageCount = paramInt4;
  }
  
  public InboundSmsTracker(byte[] paramArrayOfByte, long paramLong, int paramInt, boolean paramBoolean1, boolean paramBoolean2, String paramString1, String paramString2, String paramString3)
  {
    mPdu = paramArrayOfByte;
    mTimestamp = paramLong;
    mDestPort = paramInt;
    mIs3gpp2 = paramBoolean1;
    mIs3gpp2WapPdu = paramBoolean2;
    mMessageBody = paramString3;
    mAddress = paramString1;
    mDisplayAddress = paramString2;
    mReferenceNumber = -1;
    mSequenceNumber = getIndexOffset();
    mMessageCount = 1;
  }
  
  public static int getRealDestPort(int paramInt)
  {
    if ((0x10000 & paramInt) != 0) {
      return -1;
    }
    return 0xFFFF & paramInt;
  }
  
  public String getAddress()
  {
    return mAddress;
  }
  
  public ContentValues getContentValues()
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("pdu", HexDump.toHexString(mPdu));
    localContentValues.put("date", Long.valueOf(mTimestamp));
    int i;
    if (mDestPort == -1) {
      i = 65536;
    } else {
      i = mDestPort & 0xFFFF;
    }
    if (mIs3gpp2) {
      i |= 0x40000;
    } else {
      i |= 0x20000;
    }
    int j = i;
    if (mIs3gpp2WapPdu) {
      j = i | 0x80000;
    }
    localContentValues.put("destination_port", Integer.valueOf(j));
    if (mAddress != null)
    {
      localContentValues.put("address", mAddress);
      localContentValues.put("display_originating_addr", mDisplayAddress);
      localContentValues.put("reference_number", Integer.valueOf(mReferenceNumber));
      localContentValues.put("sequence", Integer.valueOf(mSequenceNumber));
    }
    localContentValues.put("count", Integer.valueOf(mMessageCount));
    localContentValues.put("message_body", mMessageBody);
    return localContentValues;
  }
  
  public String getDeleteWhere()
  {
    return mDeleteWhere;
  }
  
  public String[] getDeleteWhereArgs()
  {
    return mDeleteWhereArgs;
  }
  
  public int getDestPort()
  {
    return mDestPort;
  }
  
  public String getDisplayAddress()
  {
    return mDisplayAddress;
  }
  
  public String getFormat()
  {
    String str;
    if (mIs3gpp2) {
      str = "3gpp2";
    } else {
      str = "3gpp";
    }
    return str;
  }
  
  public int getIndexOffset()
  {
    int i;
    if ((mIs3gpp2) && (mIs3gpp2WapPdu)) {
      i = 0;
    } else {
      i = 1;
    }
    return i;
  }
  
  public String getMessageBody()
  {
    return mMessageBody;
  }
  
  public int getMessageCount()
  {
    return mMessageCount;
  }
  
  public byte[] getPdu()
  {
    return mPdu;
  }
  
  public String getQueryForMultiPartDuplicates()
  {
    String str;
    if (mIs3gpp2WapPdu) {
      str = "address=? AND reference_number=? AND count=? AND sequence=? AND ((date=? AND message_body=?) OR deleted=0) AND (destination_port & 524288=524288)";
    } else {
      str = "address=? AND reference_number=? AND count=? AND sequence=? AND ((date=? AND message_body=?) OR deleted=0) AND (destination_port & 524288=0)";
    }
    return str;
  }
  
  public String getQueryForSegments()
  {
    String str;
    if (mIs3gpp2WapPdu) {
      str = "address=? AND reference_number=? AND count=? AND (destination_port & 524288=524288) AND deleted=0";
    } else {
      str = "address=? AND reference_number=? AND count=? AND (destination_port & 524288=0) AND deleted=0";
    }
    return str;
  }
  
  public int getReferenceNumber()
  {
    return mReferenceNumber;
  }
  
  public int getSequenceNumber()
  {
    return mSequenceNumber;
  }
  
  public long getTimestamp()
  {
    return mTimestamp;
  }
  
  public boolean is3gpp2()
  {
    return mIs3gpp2;
  }
  
  public void setDeleteWhere(String paramString, String[] paramArrayOfString)
  {
    mDeleteWhere = paramString;
    mDeleteWhereArgs = paramArrayOfString;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("SmsTracker{timestamp=");
    localStringBuilder.append(new Date(mTimestamp));
    localStringBuilder.append(" destPort=");
    localStringBuilder.append(mDestPort);
    localStringBuilder.append(" is3gpp2=");
    localStringBuilder.append(mIs3gpp2);
    if (mAddress != null)
    {
      localStringBuilder.append(" address=");
      localStringBuilder.append(mAddress);
      localStringBuilder.append(" display_originating_addr=");
      localStringBuilder.append(mDisplayAddress);
      localStringBuilder.append(" refNumber=");
      localStringBuilder.append(mReferenceNumber);
      localStringBuilder.append(" seqNumber=");
      localStringBuilder.append(mSequenceNumber);
      localStringBuilder.append(" msgCount=");
      localStringBuilder.append(mMessageCount);
    }
    if (mDeleteWhere != null)
    {
      localStringBuilder.append(" deleteWhere(");
      localStringBuilder.append(mDeleteWhere);
      localStringBuilder.append(") deleteArgs=(");
      localStringBuilder.append(Arrays.toString(mDeleteWhereArgs));
      localStringBuilder.append(')');
    }
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
}
