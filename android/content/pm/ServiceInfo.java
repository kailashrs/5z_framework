package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Printer;

public class ServiceInfo
  extends ComponentInfo
  implements Parcelable
{
  public static final Parcelable.Creator<ServiceInfo> CREATOR = new Parcelable.Creator()
  {
    public ServiceInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ServiceInfo(paramAnonymousParcel, null);
    }
    
    public ServiceInfo[] newArray(int paramAnonymousInt)
    {
      return new ServiceInfo[paramAnonymousInt];
    }
  };
  public static final int FLAG_EXTERNAL_SERVICE = 4;
  public static final int FLAG_ISOLATED_PROCESS = 2;
  public static final int FLAG_SINGLE_USER = 1073741824;
  public static final int FLAG_STOP_WITH_TASK = 1;
  public static final int FLAG_VISIBLE_TO_INSTANT_APP = 1048576;
  public int flags;
  public String permission;
  
  public ServiceInfo() {}
  
  public ServiceInfo(ServiceInfo paramServiceInfo)
  {
    super(paramServiceInfo);
    permission = permission;
    flags = flags;
  }
  
  private ServiceInfo(Parcel paramParcel)
  {
    super(paramParcel);
    permission = paramParcel.readString();
    flags = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void dump(Printer paramPrinter, String paramString)
  {
    dump(paramPrinter, paramString, 3);
  }
  
  void dump(Printer paramPrinter, String paramString, int paramInt)
  {
    super.dumpFront(paramPrinter, paramString);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("permission=");
    localStringBuilder.append(permission);
    paramPrinter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("flags=0x");
    localStringBuilder.append(Integer.toHexString(flags));
    paramPrinter.println(localStringBuilder.toString());
    super.dumpBack(paramPrinter, paramString, paramInt);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ServiceInfo{");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    localStringBuilder.append(" ");
    localStringBuilder.append(name);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, paramInt);
    paramParcel.writeString(permission);
    paramParcel.writeInt(flags);
  }
}
