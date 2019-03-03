package android.content.pm;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

@SystemApi
public final class IntentFilterVerificationInfo
  implements Parcelable
{
  private static final String ATTR_DOMAIN_NAME = "name";
  private static final String ATTR_PACKAGE_NAME = "packageName";
  private static final String ATTR_STATUS = "status";
  public static final Parcelable.Creator<IntentFilterVerificationInfo> CREATOR = new Parcelable.Creator()
  {
    public IntentFilterVerificationInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new IntentFilterVerificationInfo(paramAnonymousParcel);
    }
    
    public IntentFilterVerificationInfo[] newArray(int paramAnonymousInt)
    {
      return new IntentFilterVerificationInfo[paramAnonymousInt];
    }
  };
  private static final String TAG = IntentFilterVerificationInfo.class.getName();
  private static final String TAG_DOMAIN = "domain";
  private ArraySet<String> mDomains = new ArraySet();
  private int mMainStatus;
  private String mPackageName;
  
  public IntentFilterVerificationInfo()
  {
    mPackageName = null;
    mMainStatus = 0;
  }
  
  public IntentFilterVerificationInfo(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public IntentFilterVerificationInfo(String paramString, ArraySet<String> paramArraySet)
  {
    mPackageName = paramString;
    mDomains = paramArraySet;
    mMainStatus = 0;
  }
  
  public IntentFilterVerificationInfo(XmlPullParser paramXmlPullParser)
    throws IOException, XmlPullParserException
  {
    readFromXml(paramXmlPullParser);
  }
  
  public static String getStatusStringFromValue(long paramLong)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    switch ((int)(paramLong >> 32))
    {
    default: 
      localStringBuilder.append("undefined");
      break;
    case 4: 
      localStringBuilder.append("always-ask");
      break;
    case 3: 
      localStringBuilder.append("never");
      break;
    case 2: 
      localStringBuilder.append("always : ");
      localStringBuilder.append(Long.toHexString(0xFFFFFFFFFFFFFFFF & paramLong));
      break;
    case 1: 
      localStringBuilder.append("ask");
    }
    return localStringBuilder.toString();
  }
  
  private void readFromParcel(Parcel paramParcel)
  {
    mPackageName = paramParcel.readString();
    mMainStatus = paramParcel.readInt();
    ArrayList localArrayList = new ArrayList();
    paramParcel.readStringList(localArrayList);
    mDomains.addAll(localArrayList);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Set<String> getDomains()
  {
    return mDomains;
  }
  
  public String getDomainsString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Iterator localIterator = mDomains.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      if (localStringBuilder.length() > 0) {
        localStringBuilder.append(" ");
      }
      localStringBuilder.append(str);
    }
    return localStringBuilder.toString();
  }
  
  int getIntFromXml(XmlPullParser paramXmlPullParser, String paramString, int paramInt)
  {
    Object localObject = paramXmlPullParser.getAttributeValue(null, paramString);
    if (TextUtils.isEmpty((CharSequence)localObject))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Missing element under ");
      ((StringBuilder)localObject).append(TAG);
      ((StringBuilder)localObject).append(": ");
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append(" at ");
      ((StringBuilder)localObject).append(paramXmlPullParser.getPositionDescription());
      paramXmlPullParser = ((StringBuilder)localObject).toString();
      Log.w(TAG, paramXmlPullParser);
      return paramInt;
    }
    return Integer.parseInt((String)localObject);
  }
  
  public String getPackageName()
  {
    return mPackageName;
  }
  
  public int getStatus()
  {
    return mMainStatus;
  }
  
  public String getStatusString()
  {
    return getStatusStringFromValue(mMainStatus << 32);
  }
  
  String getStringFromXml(XmlPullParser paramXmlPullParser, String paramString1, String paramString2)
  {
    Object localObject = paramXmlPullParser.getAttributeValue(null, paramString1);
    if (localObject == null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Missing element under ");
      ((StringBuilder)localObject).append(TAG);
      ((StringBuilder)localObject).append(": ");
      ((StringBuilder)localObject).append(paramString1);
      ((StringBuilder)localObject).append(" at ");
      ((StringBuilder)localObject).append(paramXmlPullParser.getPositionDescription());
      paramXmlPullParser = ((StringBuilder)localObject).toString();
      Log.w(TAG, paramXmlPullParser);
      return paramString2;
    }
    return localObject;
  }
  
  public void readFromXml(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    mPackageName = getStringFromXml(paramXmlPullParser, "packageName", null);
    if (mPackageName == null) {
      Log.e(TAG, "Package name cannot be null!");
    }
    int i = getIntFromXml(paramXmlPullParser, "status", -1);
    String str1;
    StringBuilder localStringBuilder;
    if (i == -1)
    {
      str1 = TAG;
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown status value: ");
      localStringBuilder.append(i);
      Log.e(str1, localStringBuilder.toString());
    }
    mMainStatus = i;
    i = paramXmlPullParser.getDepth();
    for (;;)
    {
      int j = paramXmlPullParser.next();
      if ((j == 1) || ((j == 3) && (paramXmlPullParser.getDepth() <= i))) {
        break;
      }
      if ((j != 3) && (j != 4))
      {
        str1 = paramXmlPullParser.getName();
        if (str1.equals("domain"))
        {
          str1 = getStringFromXml(paramXmlPullParser, "name", null);
          if (!TextUtils.isEmpty(str1)) {
            mDomains.add(str1);
          }
        }
        else
        {
          String str2 = TAG;
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("Unknown tag parsing IntentFilter: ");
          localStringBuilder.append(str1);
          Log.w(str2, localStringBuilder.toString());
        }
        XmlUtils.skipCurrentTag(paramXmlPullParser);
      }
    }
  }
  
  public void setDomains(ArraySet<String> paramArraySet)
  {
    mDomains = paramArraySet;
  }
  
  public void setStatus(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 3))
    {
      mMainStatus = paramInt;
    }
    else
    {
      String str = TAG;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Trying to set a non supported status: ");
      localStringBuilder.append(paramInt);
      Log.w(str, localStringBuilder.toString());
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mPackageName);
    paramParcel.writeInt(mMainStatus);
    paramParcel.writeStringList(new ArrayList(mDomains));
  }
  
  public void writeToXml(XmlSerializer paramXmlSerializer)
    throws IOException
  {
    paramXmlSerializer.attribute(null, "packageName", mPackageName);
    paramXmlSerializer.attribute(null, "status", String.valueOf(mMainStatus));
    Iterator localIterator = mDomains.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      paramXmlSerializer.startTag(null, "domain");
      paramXmlSerializer.attribute(null, "name", str);
      paramXmlSerializer.endTag(null, "domain");
    }
  }
}
