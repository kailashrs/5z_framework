package android.telephony;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.format.DateUtils;

public class CellBroadcastMessage
  implements Parcelable
{
  public static final Parcelable.Creator<CellBroadcastMessage> CREATOR = new Parcelable.Creator()
  {
    public CellBroadcastMessage createFromParcel(Parcel paramAnonymousParcel)
    {
      return new CellBroadcastMessage(paramAnonymousParcel, null);
    }
    
    public CellBroadcastMessage[] newArray(int paramAnonymousInt)
    {
      return new CellBroadcastMessage[paramAnonymousInt];
    }
  };
  public static final String SMS_CB_MESSAGE_EXTRA = "com.android.cellbroadcastreceiver.SMS_CB_MESSAGE";
  private final long mDeliveryTime;
  private boolean mIsRead;
  private final SmsCbMessage mSmsCbMessage;
  private int mSubId;
  
  private CellBroadcastMessage(Parcel paramParcel)
  {
    boolean bool = false;
    mSubId = 0;
    mSmsCbMessage = new SmsCbMessage(paramParcel);
    mDeliveryTime = paramParcel.readLong();
    if (paramParcel.readInt() != 0) {
      bool = true;
    }
    mIsRead = bool;
    mSubId = paramParcel.readInt();
  }
  
  public CellBroadcastMessage(SmsCbMessage paramSmsCbMessage)
  {
    mSubId = 0;
    mSmsCbMessage = paramSmsCbMessage;
    mDeliveryTime = System.currentTimeMillis();
    mIsRead = false;
  }
  
  private CellBroadcastMessage(SmsCbMessage paramSmsCbMessage, long paramLong, boolean paramBoolean)
  {
    mSubId = 0;
    mSmsCbMessage = paramSmsCbMessage;
    mDeliveryTime = paramLong;
    mIsRead = paramBoolean;
  }
  
  public static CellBroadcastMessage createFromCursor(Cursor paramCursor)
  {
    int i = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("geo_scope"));
    int j = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("serial_number"));
    int k = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("service_category"));
    String str1 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("language"));
    String str2 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("body"));
    int m = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("format"));
    int n = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("priority"));
    int i1 = paramCursor.getColumnIndex("plmn");
    if ((i1 != -1) && (!paramCursor.isNull(i1))) {
      localObject = paramCursor.getString(i1);
    } else {
      localObject = null;
    }
    i1 = paramCursor.getColumnIndex("lac");
    if ((i1 != -1) && (!paramCursor.isNull(i1))) {
      i1 = paramCursor.getInt(i1);
    } else {
      i1 = -1;
    }
    int i2 = paramCursor.getColumnIndex("cid");
    if ((i2 != -1) && (!paramCursor.isNull(i2))) {
      i2 = paramCursor.getInt(i2);
    } else {
      i2 = -1;
    }
    SmsCbLocation localSmsCbLocation = new SmsCbLocation((String)localObject, i1, i2);
    i1 = paramCursor.getColumnIndex("etws_warning_type");
    if ((i1 != -1) && (!paramCursor.isNull(i1))) {
      localObject = new SmsCbEtwsInfo(paramCursor.getInt(i1), false, false, false, null);
    } else {
      localObject = null;
    }
    i1 = paramCursor.getColumnIndex("cmas_message_class");
    SmsCbCmasInfo localSmsCbCmasInfo;
    if ((i1 != -1) && (!paramCursor.isNull(i1)))
    {
      int i3 = paramCursor.getInt(i1);
      i1 = paramCursor.getColumnIndex("cmas_category");
      if ((i1 != -1) && (!paramCursor.isNull(i1))) {
        i1 = paramCursor.getInt(i1);
      } else {
        i1 = -1;
      }
      i2 = paramCursor.getColumnIndex("cmas_response_type");
      if ((i2 != -1) && (!paramCursor.isNull(i2))) {
        i2 = paramCursor.getInt(i2);
      } else {
        i2 = -1;
      }
      int i4 = paramCursor.getColumnIndex("cmas_severity");
      if ((i4 != -1) && (!paramCursor.isNull(i4))) {
        i4 = paramCursor.getInt(i4);
      } else {
        i4 = -1;
      }
      int i5 = paramCursor.getColumnIndex("cmas_urgency");
      if ((i5 != -1) && (!paramCursor.isNull(i5))) {
        i5 = paramCursor.getInt(i5);
      } else {
        i5 = -1;
      }
      int i6 = paramCursor.getColumnIndex("cmas_certainty");
      int i7 = -1;
      if ((i6 != -1) && (!paramCursor.isNull(i6))) {
        i7 = paramCursor.getInt(i6);
      }
      localSmsCbCmasInfo = new SmsCbCmasInfo(i3, i1, i2, i4, i5, i7);
    }
    else
    {
      localSmsCbCmasInfo = null;
    }
    Object localObject = new SmsCbMessage(m, i, j, localSmsCbLocation, k, str1, str2, n, (SmsCbEtwsInfo)localObject, localSmsCbCmasInfo);
    long l = paramCursor.getLong(paramCursor.getColumnIndexOrThrow("date"));
    boolean bool;
    if (paramCursor.getInt(paramCursor.getColumnIndexOrThrow("read")) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return new CellBroadcastMessage((SmsCbMessage)localObject, l, bool);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getCmasMessageClass()
  {
    if (mSmsCbMessage.isCmasMessage()) {
      return mSmsCbMessage.getCmasWarningInfo().getMessageClass();
    }
    return -1;
  }
  
  public SmsCbCmasInfo getCmasWarningInfo()
  {
    return mSmsCbMessage.getCmasWarningInfo();
  }
  
  public ContentValues getContentValues()
  {
    ContentValues localContentValues = new ContentValues(16);
    SmsCbMessage localSmsCbMessage = mSmsCbMessage;
    localContentValues.put("geo_scope", Integer.valueOf(localSmsCbMessage.getGeographicalScope()));
    Object localObject = localSmsCbMessage.getLocation();
    if (((SmsCbLocation)localObject).getPlmn() != null) {
      localContentValues.put("plmn", ((SmsCbLocation)localObject).getPlmn());
    }
    if (((SmsCbLocation)localObject).getLac() != -1) {
      localContentValues.put("lac", Integer.valueOf(((SmsCbLocation)localObject).getLac()));
    }
    if (((SmsCbLocation)localObject).getCid() != -1) {
      localContentValues.put("cid", Integer.valueOf(((SmsCbLocation)localObject).getCid()));
    }
    localContentValues.put("serial_number", Integer.valueOf(localSmsCbMessage.getSerialNumber()));
    localContentValues.put("service_category", Integer.valueOf(localSmsCbMessage.getServiceCategory()));
    localContentValues.put("language", localSmsCbMessage.getLanguageCode());
    localContentValues.put("body", localSmsCbMessage.getMessageBody());
    localContentValues.put("date", Long.valueOf(mDeliveryTime));
    localContentValues.put("read", Boolean.valueOf(mIsRead));
    localContentValues.put("format", Integer.valueOf(localSmsCbMessage.getMessageFormat()));
    localContentValues.put("priority", Integer.valueOf(localSmsCbMessage.getMessagePriority()));
    localObject = mSmsCbMessage.getEtwsWarningInfo();
    if (localObject != null) {
      localContentValues.put("etws_warning_type", Integer.valueOf(((SmsCbEtwsInfo)localObject).getWarningType()));
    }
    localObject = mSmsCbMessage.getCmasWarningInfo();
    if (localObject != null)
    {
      localContentValues.put("cmas_message_class", Integer.valueOf(((SmsCbCmasInfo)localObject).getMessageClass()));
      localContentValues.put("cmas_category", Integer.valueOf(((SmsCbCmasInfo)localObject).getCategory()));
      localContentValues.put("cmas_response_type", Integer.valueOf(((SmsCbCmasInfo)localObject).getResponseType()));
      localContentValues.put("cmas_severity", Integer.valueOf(((SmsCbCmasInfo)localObject).getSeverity()));
      localContentValues.put("cmas_urgency", Integer.valueOf(((SmsCbCmasInfo)localObject).getUrgency()));
      localContentValues.put("cmas_certainty", Integer.valueOf(((SmsCbCmasInfo)localObject).getCertainty()));
    }
    return localContentValues;
  }
  
  public String getDateString(Context paramContext)
  {
    return DateUtils.formatDateTime(paramContext, mDeliveryTime, 527121);
  }
  
  public long getDeliveryTime()
  {
    return mDeliveryTime;
  }
  
  public SmsCbEtwsInfo getEtwsWarningInfo()
  {
    return mSmsCbMessage.getEtwsWarningInfo();
  }
  
  public String getLanguageCode()
  {
    return mSmsCbMessage.getLanguageCode();
  }
  
  public String getMessageBody()
  {
    return mSmsCbMessage.getMessageBody();
  }
  
  public int getSerialNumber()
  {
    return mSmsCbMessage.getSerialNumber();
  }
  
  public int getServiceCategory()
  {
    return mSmsCbMessage.getServiceCategory();
  }
  
  public String getSpokenDateString(Context paramContext)
  {
    return DateUtils.formatDateTime(paramContext, mDeliveryTime, 17);
  }
  
  public int getSubId()
  {
    return mSubId;
  }
  
  public boolean isCmasMessage()
  {
    return mSmsCbMessage.isCmasMessage();
  }
  
  public boolean isEmergencyAlertMessage()
  {
    return mSmsCbMessage.isEmergencyMessage();
  }
  
  public boolean isEtwsEmergencyUserAlert()
  {
    SmsCbEtwsInfo localSmsCbEtwsInfo = mSmsCbMessage.getEtwsWarningInfo();
    boolean bool;
    if ((localSmsCbEtwsInfo != null) && (localSmsCbEtwsInfo.isEmergencyUserAlert())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isEtwsMessage()
  {
    return mSmsCbMessage.isEtwsMessage();
  }
  
  public boolean isEtwsPopupAlert()
  {
    SmsCbEtwsInfo localSmsCbEtwsInfo = mSmsCbMessage.getEtwsWarningInfo();
    boolean bool;
    if ((localSmsCbEtwsInfo != null) && (localSmsCbEtwsInfo.isPopupAlert())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isEtwsTestMessage()
  {
    SmsCbEtwsInfo localSmsCbEtwsInfo = mSmsCbMessage.getEtwsWarningInfo();
    boolean bool;
    if ((localSmsCbEtwsInfo != null) && (localSmsCbEtwsInfo.getWarningType() == 3)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isPublicAlertMessage()
  {
    return mSmsCbMessage.isEmergencyMessage();
  }
  
  public boolean isRead()
  {
    return mIsRead;
  }
  
  public void setIsRead(boolean paramBoolean)
  {
    mIsRead = paramBoolean;
  }
  
  public void setSubId(int paramInt)
  {
    mSubId = paramInt;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    mSmsCbMessage.writeToParcel(paramParcel, paramInt);
    paramParcel.writeLong(mDeliveryTime);
    paramParcel.writeInt(mIsRead);
    paramParcel.writeInt(mSubId);
  }
}
