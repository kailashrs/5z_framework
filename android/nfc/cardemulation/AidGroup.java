package android.nfc.cardemulation;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class AidGroup
  implements Parcelable
{
  public static final Parcelable.Creator<AidGroup> CREATOR = new Parcelable.Creator()
  {
    public AidGroup createFromParcel(Parcel paramAnonymousParcel)
    {
      String str = paramAnonymousParcel.readString();
      int i = paramAnonymousParcel.readInt();
      ArrayList localArrayList = new ArrayList();
      if (i > 0) {
        paramAnonymousParcel.readStringList(localArrayList);
      }
      return new AidGroup(localArrayList, str);
    }
    
    public AidGroup[] newArray(int paramAnonymousInt)
    {
      return new AidGroup[paramAnonymousInt];
    }
  };
  public static final int MAX_NUM_AIDS = 256;
  static final String TAG = "AidGroup";
  protected List<String> aids;
  protected String category;
  protected String description;
  
  AidGroup(String paramString1, String paramString2)
  {
    aids = new ArrayList();
    category = paramString1;
    description = paramString2;
  }
  
  public AidGroup(List<String> paramList, String paramString)
  {
    if ((paramList != null) && (paramList.size() != 0))
    {
      if (paramList.size() <= 256)
      {
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext())
        {
          String str = (String)localIterator.next();
          if (!CardEmulation.isValidAid(str))
          {
            paramList = new StringBuilder();
            paramList.append("AID ");
            paramList.append(str);
            paramList.append(" is not a valid AID.");
            throw new IllegalArgumentException(paramList.toString());
          }
        }
        if (isValidCategory(paramString)) {
          category = paramString;
        } else {
          category = "other";
        }
        aids = new ArrayList(paramList.size());
        paramString = paramList.iterator();
        while (paramString.hasNext())
        {
          paramList = (String)paramString.next();
          aids.add(paramList.toUpperCase());
        }
        description = null;
        return;
      }
      throw new IllegalArgumentException("Too many AIDs in AID group.");
    }
    throw new IllegalArgumentException("No AIDS in AID group.");
  }
  
  public static AidGroup createFromXml(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    Object localObject1 = null;
    ArrayList localArrayList = new ArrayList();
    Object localObject2 = null;
    int i = 0;
    int j = paramXmlPullParser.getEventType();
    int k = paramXmlPullParser.getDepth();
    Object localObject3;
    for (;;)
    {
      localObject3 = localObject2;
      if (j == 1) {
        break;
      }
      localObject3 = localObject2;
      if (paramXmlPullParser.getDepth() < k) {
        break;
      }
      String str = paramXmlPullParser.getName();
      int m;
      if (j == 2)
      {
        if (str.equals("aid"))
        {
          if (i != 0)
          {
            localObject3 = paramXmlPullParser.getAttributeValue(null, "value");
            if (localObject3 != null) {
              localArrayList.add(((String)localObject3).toUpperCase());
            }
            localObject3 = localObject1;
            m = i;
          }
          else
          {
            Log.d("AidGroup", "Ignoring <aid> tag while not in group");
            localObject3 = localObject1;
            m = i;
          }
        }
        else if (str.equals("aid-group"))
        {
          localObject3 = paramXmlPullParser.getAttributeValue(null, "category");
          if (localObject3 == null)
          {
            Log.e("AidGroup", "<aid-group> tag without valid category");
            return null;
          }
          m = 1;
        }
        else
        {
          localObject3 = new StringBuilder();
          ((StringBuilder)localObject3).append("Ignoring unexpected tag: ");
          ((StringBuilder)localObject3).append(str);
          Log.d("AidGroup", ((StringBuilder)localObject3).toString());
          localObject3 = localObject1;
          m = i;
        }
      }
      else
      {
        localObject3 = localObject1;
        m = i;
        if (j == 3)
        {
          localObject3 = localObject1;
          m = i;
          if (str.equals("aid-group"))
          {
            localObject3 = localObject1;
            m = i;
            if (i != 0)
            {
              localObject3 = localObject1;
              m = i;
              if (localArrayList.size() > 0)
              {
                localObject3 = new AidGroup(localArrayList, (String)localObject1);
                break;
              }
            }
          }
        }
      }
      j = paramXmlPullParser.next();
      localObject1 = localObject3;
      i = m;
    }
    return localObject3;
  }
  
  static boolean isValidCategory(String paramString)
  {
    boolean bool;
    if ((!"payment".equals(paramString)) && (!"other".equals(paramString))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public List<String> getAids()
  {
    return aids;
  }
  
  public String getCategory()
  {
    return category;
  }
  
  public String toString()
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Category: ");
    ((StringBuilder)localObject).append(category);
    ((StringBuilder)localObject).append(", AIDs:");
    StringBuilder localStringBuilder = new StringBuilder(((StringBuilder)localObject).toString());
    localObject = aids.iterator();
    while (((Iterator)localObject).hasNext())
    {
      localStringBuilder.append((String)((Iterator)localObject).next());
      localStringBuilder.append(", ");
    }
    return localStringBuilder.toString();
  }
  
  public void writeAsXml(XmlSerializer paramXmlSerializer)
    throws IOException
  {
    paramXmlSerializer.startTag(null, "aid-group");
    paramXmlSerializer.attribute(null, "category", category);
    Iterator localIterator = aids.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      paramXmlSerializer.startTag(null, "aid");
      paramXmlSerializer.attribute(null, "value", str);
      paramXmlSerializer.endTag(null, "aid");
    }
    paramXmlSerializer.endTag(null, "aid-group");
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(category);
    paramParcel.writeInt(aids.size());
    if (aids.size() > 0) {
      paramParcel.writeStringList(aids);
    }
  }
}
