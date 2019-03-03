package android.app;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.proto.ProtoOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

public final class NotificationChannelGroup
  implements Parcelable
{
  private static final String ATT_BLOCKED = "blocked";
  private static final String ATT_DESC = "desc";
  private static final String ATT_ID = "id";
  private static final String ATT_NAME = "name";
  public static final Parcelable.Creator<NotificationChannelGroup> CREATOR = new Parcelable.Creator()
  {
    public NotificationChannelGroup createFromParcel(Parcel paramAnonymousParcel)
    {
      return new NotificationChannelGroup(paramAnonymousParcel);
    }
    
    public NotificationChannelGroup[] newArray(int paramAnonymousInt)
    {
      return new NotificationChannelGroup[paramAnonymousInt];
    }
  };
  private static final int MAX_TEXT_LENGTH = 1000;
  private static final String TAG_GROUP = "channelGroup";
  private boolean mBlocked;
  private List<NotificationChannel> mChannels = new ArrayList();
  private String mDescription;
  private final String mId;
  private CharSequence mName;
  
  protected NotificationChannelGroup(Parcel paramParcel)
  {
    if (paramParcel.readByte() != 0) {
      mId = paramParcel.readString();
    } else {
      mId = null;
    }
    mName = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    if (paramParcel.readByte() != 0) {
      mDescription = paramParcel.readString();
    } else {
      mDescription = null;
    }
    paramParcel.readParcelableList(mChannels, NotificationChannel.class.getClassLoader());
    mBlocked = paramParcel.readBoolean();
  }
  
  public NotificationChannelGroup(String paramString, CharSequence paramCharSequence)
  {
    mId = getTrimmedString(paramString);
    if (paramCharSequence != null) {
      paramString = getTrimmedString(paramCharSequence.toString());
    } else {
      paramString = null;
    }
    mName = paramString;
  }
  
  private String getTrimmedString(String paramString)
  {
    if ((paramString != null) && (paramString.length() > 1000)) {
      return paramString.substring(0, 1000);
    }
    return paramString;
  }
  
  private static boolean safeBool(XmlPullParser paramXmlPullParser, String paramString, boolean paramBoolean)
  {
    paramXmlPullParser = paramXmlPullParser.getAttributeValue(null, paramString);
    if (TextUtils.isEmpty(paramXmlPullParser)) {
      return paramBoolean;
    }
    return Boolean.parseBoolean(paramXmlPullParser);
  }
  
  public void addChannel(NotificationChannel paramNotificationChannel)
  {
    mChannels.add(paramNotificationChannel);
  }
  
  public NotificationChannelGroup clone()
  {
    NotificationChannelGroup localNotificationChannelGroup = new NotificationChannelGroup(getId(), getName());
    localNotificationChannelGroup.setDescription(getDescription());
    localNotificationChannelGroup.setBlocked(isBlocked());
    localNotificationChannelGroup.setChannels(getChannels());
    return localNotificationChannelGroup;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (NotificationChannelGroup)paramObject;
      if (isBlocked() != paramObject.isBlocked()) {
        return false;
      }
      if (getId() != null ? !getId().equals(paramObject.getId()) : paramObject.getId() != null) {
        return false;
      }
      if (getName() != null ? !getName().equals(paramObject.getName()) : paramObject.getName() != null) {
        return false;
      }
      if (getDescription() != null ? !getDescription().equals(paramObject.getDescription()) : paramObject.getDescription() != null) {
        return false;
      }
      if (getChannels() != null) {
        bool = getChannels().equals(paramObject.getChannels());
      } else if (paramObject.getChannels() != null) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public List<NotificationChannel> getChannels()
  {
    return mChannels;
  }
  
  public String getDescription()
  {
    return mDescription;
  }
  
  public String getId()
  {
    return mId;
  }
  
  public CharSequence getName()
  {
    return mName;
  }
  
  public int hashCode()
  {
    String str = getId();
    int i = 0;
    int j;
    if (str != null) {
      j = getId().hashCode();
    } else {
      j = 0;
    }
    int k;
    if (getName() != null) {
      k = getName().hashCode();
    } else {
      k = 0;
    }
    int m;
    if (getDescription() != null) {
      m = getDescription().hashCode();
    } else {
      m = 0;
    }
    int n = isBlocked();
    if (getChannels() != null) {
      i = getChannels().hashCode();
    }
    return 31 * (31 * (31 * (31 * j + k) + m) + n) + i;
  }
  
  public boolean isBlocked()
  {
    return mBlocked;
  }
  
  public void populateFromXml(XmlPullParser paramXmlPullParser)
  {
    setDescription(paramXmlPullParser.getAttributeValue(null, "desc"));
    setBlocked(safeBool(paramXmlPullParser, "blocked", false));
  }
  
  public void setBlocked(boolean paramBoolean)
  {
    mBlocked = paramBoolean;
  }
  
  public void setChannels(List<NotificationChannel> paramList)
  {
    mChannels = paramList;
  }
  
  public void setDescription(String paramString)
  {
    mDescription = getTrimmedString(paramString);
  }
  
  @SystemApi
  public JSONObject toJson()
    throws JSONException
  {
    JSONObject localJSONObject = new JSONObject();
    localJSONObject.put("id", getId());
    localJSONObject.put("name", getName());
    localJSONObject.put("desc", getDescription());
    localJSONObject.put("blocked", isBlocked());
    return localJSONObject;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("NotificationChannelGroup{mId='");
    localStringBuilder.append(mId);
    localStringBuilder.append('\'');
    localStringBuilder.append(", mName=");
    localStringBuilder.append(mName);
    localStringBuilder.append(", mDescription=");
    String str;
    if (!TextUtils.isEmpty(mDescription)) {
      str = "hasDescription ";
    } else {
      str = "";
    }
    localStringBuilder.append(str);
    localStringBuilder.append(", mBlocked=");
    localStringBuilder.append(mBlocked);
    localStringBuilder.append(", mChannels=");
    localStringBuilder.append(mChannels);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (mId != null)
    {
      paramParcel.writeByte((byte)1);
      paramParcel.writeString(mId);
    }
    else
    {
      paramParcel.writeByte((byte)0);
    }
    TextUtils.writeToParcel(mName, paramParcel, paramInt);
    if (mDescription != null)
    {
      paramParcel.writeByte((byte)1);
      paramParcel.writeString(mDescription);
    }
    else
    {
      paramParcel.writeByte((byte)0);
    }
    paramParcel.writeParcelableList(mChannels, paramInt);
    paramParcel.writeBoolean(mBlocked);
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    paramProtoOutputStream.write(1138166333441L, mId);
    paramProtoOutputStream.write(1138166333442L, mName.toString());
    paramProtoOutputStream.write(1138166333443L, mDescription);
    paramProtoOutputStream.write(1133871366148L, mBlocked);
    Iterator localIterator = mChannels.iterator();
    while (localIterator.hasNext()) {
      ((NotificationChannel)localIterator.next()).writeToProto(paramProtoOutputStream, 2246267895813L);
    }
    paramProtoOutputStream.end(paramLong);
  }
  
  public void writeXml(XmlSerializer paramXmlSerializer)
    throws IOException
  {
    paramXmlSerializer.startTag(null, "channelGroup");
    paramXmlSerializer.attribute(null, "id", getId());
    if (getName() != null) {
      paramXmlSerializer.attribute(null, "name", getName().toString());
    }
    if (getDescription() != null) {
      paramXmlSerializer.attribute(null, "desc", getDescription().toString());
    }
    paramXmlSerializer.attribute(null, "blocked", Boolean.toString(isBlocked()));
    paramXmlSerializer.endTag(null, "channelGroup");
  }
}
