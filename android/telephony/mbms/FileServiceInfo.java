package android.telephony.mbms;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class FileServiceInfo
  extends ServiceInfo
  implements Parcelable
{
  public static final Parcelable.Creator<FileServiceInfo> CREATOR = new Parcelable.Creator()
  {
    public FileServiceInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new FileServiceInfo(paramAnonymousParcel);
    }
    
    public FileServiceInfo[] newArray(int paramAnonymousInt)
    {
      return new FileServiceInfo[paramAnonymousInt];
    }
  };
  private final List<FileInfo> files;
  
  FileServiceInfo(Parcel paramParcel)
  {
    super(paramParcel);
    files = new ArrayList();
    paramParcel.readList(files, FileInfo.class.getClassLoader());
  }
  
  @SystemApi
  public FileServiceInfo(Map<Locale, String> paramMap, String paramString1, List<Locale> paramList, String paramString2, Date paramDate1, Date paramDate2, List<FileInfo> paramList1)
  {
    super(paramMap, paramString1, paramList, paramString2, paramDate1, paramDate2);
    files = new ArrayList(paramList1);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public List<FileInfo> getFiles()
  {
    return files;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, paramInt);
    paramParcel.writeList(files);
  }
}
