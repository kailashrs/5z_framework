package android.content.pm;

import android.os.Parcel;
import android.os.Parcel.ReadWriteHelper;
import java.util.ArrayList;
import java.util.HashMap;

public class PackageParserCacheHelper
{
  private static final boolean DEBUG = false;
  private static final String TAG = "PackageParserCacheHelper";
  
  private PackageParserCacheHelper() {}
  
  public static class ReadHelper
    extends Parcel.ReadWriteHelper
  {
    private final Parcel mParcel;
    private final ArrayList<String> mStrings = new ArrayList();
    
    public ReadHelper(Parcel paramParcel)
    {
      mParcel = paramParcel;
    }
    
    public String readString(Parcel paramParcel)
    {
      return (String)mStrings.get(paramParcel.readInt());
    }
    
    public void startAndInstall()
    {
      mStrings.clear();
      int i = mParcel.readInt();
      int j = mParcel.dataPosition();
      mParcel.setDataPosition(i);
      mParcel.readStringList(mStrings);
      mParcel.setDataPosition(j);
      mParcel.setReadWriteHelper(this);
    }
  }
  
  public static class WriteHelper
    extends Parcel.ReadWriteHelper
  {
    private final HashMap<String, Integer> mIndexes = new HashMap();
    private final Parcel mParcel;
    private final int mStartPos;
    private final ArrayList<String> mStrings = new ArrayList();
    
    public WriteHelper(Parcel paramParcel)
    {
      mParcel = paramParcel;
      mStartPos = paramParcel.dataPosition();
      mParcel.writeInt(0);
      mParcel.setReadWriteHelper(this);
    }
    
    public void finishAndUninstall()
    {
      mParcel.setReadWriteHelper(null);
      int i = mParcel.dataPosition();
      mParcel.writeStringList(mStrings);
      mParcel.setDataPosition(mStartPos);
      mParcel.writeInt(i);
      mParcel.setDataPosition(mParcel.dataSize());
    }
    
    public void writeString(Parcel paramParcel, String paramString)
    {
      Integer localInteger = (Integer)mIndexes.get(paramString);
      if (localInteger != null)
      {
        paramParcel.writeInt(localInteger.intValue());
      }
      else
      {
        int i = mStrings.size();
        mIndexes.put(paramString, Integer.valueOf(i));
        mStrings.add(paramString);
        paramParcel.writeInt(i);
      }
    }
  }
}
