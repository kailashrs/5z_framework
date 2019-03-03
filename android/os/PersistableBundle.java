package android.os;

import android.util.ArrayMap;
import android.util.proto.ProtoOutputStream;
import com.android.internal.util.XmlUtils;
import com.android.internal.util.XmlUtils.ReadMapCallback;
import com.android.internal.util.XmlUtils.WriteMapCallback;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public final class PersistableBundle
  extends BaseBundle
  implements Cloneable, Parcelable, XmlUtils.WriteMapCallback
{
  public static final Parcelable.Creator<PersistableBundle> CREATOR = new Parcelable.Creator()
  {
    public PersistableBundle createFromParcel(Parcel paramAnonymousParcel)
    {
      return paramAnonymousParcel.readPersistableBundle();
    }
    
    public PersistableBundle[] newArray(int paramAnonymousInt)
    {
      return new PersistableBundle[paramAnonymousInt];
    }
  };
  public static final PersistableBundle EMPTY = new PersistableBundle();
  private static final String TAG_PERSISTABLEMAP = "pbundle_as_map";
  
  static
  {
    EMPTYmMap = ArrayMap.EMPTY;
  }
  
  public PersistableBundle()
  {
    mFlags = 1;
  }
  
  public PersistableBundle(int paramInt)
  {
    super(paramInt);
    mFlags = 1;
  }
  
  public PersistableBundle(Bundle paramBundle)
  {
    this(paramBundle.getMap());
  }
  
  PersistableBundle(Parcel paramParcel, int paramInt)
  {
    super(paramParcel, paramInt);
    mFlags = 1;
  }
  
  public PersistableBundle(PersistableBundle paramPersistableBundle)
  {
    super(paramPersistableBundle);
    mFlags = mFlags;
  }
  
  private PersistableBundle(ArrayMap<String, Object> paramArrayMap)
  {
    mFlags = 1;
    putAll(paramArrayMap);
    int i = mMap.size();
    int j = 0;
    while (j < i)
    {
      paramArrayMap = mMap.valueAt(j);
      if ((paramArrayMap instanceof ArrayMap)) {
        mMap.setValueAt(j, new PersistableBundle((ArrayMap)paramArrayMap));
      } else if ((paramArrayMap instanceof Bundle)) {
        mMap.setValueAt(j, new PersistableBundle((Bundle)paramArrayMap));
      } else {
        if (!isValidType(paramArrayMap)) {
          break label111;
        }
      }
      j++;
      continue;
      label111:
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Bad value in PersistableBundle key=");
      localStringBuilder.append((String)mMap.keyAt(j));
      localStringBuilder.append(" value=");
      localStringBuilder.append(paramArrayMap);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
  }
  
  PersistableBundle(boolean paramBoolean)
  {
    super(paramBoolean);
  }
  
  public static PersistableBundle forPair(String paramString1, String paramString2)
  {
    PersistableBundle localPersistableBundle = new PersistableBundle(1);
    localPersistableBundle.putString(paramString1, paramString2);
    return localPersistableBundle;
  }
  
  public static boolean isValidType(Object paramObject)
  {
    boolean bool;
    if ((!(paramObject instanceof Integer)) && (!(paramObject instanceof Long)) && (!(paramObject instanceof Double)) && (!(paramObject instanceof String)) && (!(paramObject instanceof int[])) && (!(paramObject instanceof long[])) && (!(paramObject instanceof double[])) && (!(paramObject instanceof String[])) && (!(paramObject instanceof PersistableBundle)) && (paramObject != null) && (!(paramObject instanceof Boolean)) && (!(paramObject instanceof boolean[]))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static PersistableBundle restoreFromXml(XmlPullParser paramXmlPullParser)
    throws IOException, XmlPullParserException
  {
    int i = paramXmlPullParser.getDepth();
    String str = paramXmlPullParser.getName();
    String[] arrayOfString = new String[1];
    int j;
    do
    {
      j = paramXmlPullParser.next();
      if ((j == 1) || ((j == 3) && (paramXmlPullParser.getDepth() >= i))) {
        break;
      }
    } while (j != 2);
    return new PersistableBundle(XmlUtils.readThisArrayMapXml(paramXmlPullParser, str, arrayOfString, new MyReadMapCallback()));
    return EMPTY;
  }
  
  public Object clone()
  {
    return new PersistableBundle(this);
  }
  
  public PersistableBundle deepCopy()
  {
    PersistableBundle localPersistableBundle = new PersistableBundle(false);
    localPersistableBundle.copyInternal(this, true);
    return localPersistableBundle;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public PersistableBundle getPersistableBundle(String paramString)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return null;
    }
    try
    {
      PersistableBundle localPersistableBundle = (PersistableBundle)localObject;
      return localPersistableBundle;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "Bundle", localClassCastException);
    }
    return null;
  }
  
  public void putPersistableBundle(String paramString, PersistableBundle paramPersistableBundle)
  {
    unparcel();
    mMap.put(paramString, paramPersistableBundle);
  }
  
  public void saveToXml(XmlSerializer paramXmlSerializer)
    throws IOException, XmlPullParserException
  {
    unparcel();
    XmlUtils.writeMapXml(mMap, paramXmlSerializer, this);
  }
  
  public String toShortString()
  {
    try
    {
      if (mParcelledData != null)
      {
        if (isEmptyParcel()) {
          return "EMPTY_PARCEL";
        }
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("mParcelledData.dataSize=");
        ((StringBuilder)localObject1).append(mParcelledData.dataSize());
        localObject1 = ((StringBuilder)localObject1).toString();
        return localObject1;
      }
      Object localObject1 = mMap.toString();
      return localObject1;
    }
    finally {}
  }
  
  public String toString()
  {
    try
    {
      if (mParcelledData != null)
      {
        if (isEmptyParcel()) {
          return "PersistableBundle[EMPTY_PARCEL]";
        }
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("PersistableBundle[mParcelledData.dataSize=");
        ((StringBuilder)localObject1).append(mParcelledData.dataSize());
        ((StringBuilder)localObject1).append("]");
        localObject1 = ((StringBuilder)localObject1).toString();
        return localObject1;
      }
      Object localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>();
      ((StringBuilder)localObject1).append("PersistableBundle[");
      ((StringBuilder)localObject1).append(mMap.toString());
      ((StringBuilder)localObject1).append("]");
      localObject1 = ((StringBuilder)localObject1).toString();
      return localObject1;
    }
    finally {}
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    boolean bool = paramParcel.pushAllowFds(false);
    try
    {
      writeToParcelInner(paramParcel, paramInt);
      return;
    }
    finally
    {
      paramParcel.restoreAllowFds(bool);
    }
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    if (mParcelledData != null)
    {
      if (isEmptyParcel()) {
        paramProtoOutputStream.write(1120986464257L, 0);
      } else {
        paramProtoOutputStream.write(1120986464257L, mParcelledData.dataSize());
      }
    }
    else {
      paramProtoOutputStream.write(1138166333442L, mMap.toString());
    }
    paramProtoOutputStream.end(paramLong);
  }
  
  public void writeUnknownObject(Object paramObject, String paramString, XmlSerializer paramXmlSerializer)
    throws XmlPullParserException, IOException
  {
    if ((paramObject instanceof PersistableBundle))
    {
      paramXmlSerializer.startTag(null, "pbundle_as_map");
      paramXmlSerializer.attribute(null, "name", paramString);
      ((PersistableBundle)paramObject).saveToXml(paramXmlSerializer);
      paramXmlSerializer.endTag(null, "pbundle_as_map");
      return;
    }
    paramString = new StringBuilder();
    paramString.append("Unknown Object o=");
    paramString.append(paramObject);
    throw new XmlPullParserException(paramString.toString());
  }
  
  static class MyReadMapCallback
    implements XmlUtils.ReadMapCallback
  {
    MyReadMapCallback() {}
    
    public Object readThisUnknownObjectXml(XmlPullParser paramXmlPullParser, String paramString)
      throws XmlPullParserException, IOException
    {
      if ("pbundle_as_map".equals(paramString)) {
        return PersistableBundle.restoreFromXml(paramXmlPullParser);
      }
      paramXmlPullParser = new StringBuilder();
      paramXmlPullParser.append("Unknown tag=");
      paramXmlPullParser.append(paramString);
      throw new XmlPullParserException(paramXmlPullParser.toString());
    }
  }
}
