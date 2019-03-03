package android.app;

import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.PrintWriter;

public class WaitResult
  implements Parcelable
{
  public static final Parcelable.Creator<WaitResult> CREATOR = new Parcelable.Creator()
  {
    public WaitResult createFromParcel(Parcel paramAnonymousParcel)
    {
      return new WaitResult(paramAnonymousParcel, null);
    }
    
    public WaitResult[] newArray(int paramAnonymousInt)
    {
      return new WaitResult[paramAnonymousInt];
    }
  };
  public int result;
  public long thisTime;
  public boolean timeout;
  public long totalTime;
  public ComponentName who;
  
  public WaitResult() {}
  
  private WaitResult(Parcel paramParcel)
  {
    result = paramParcel.readInt();
    boolean bool;
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    timeout = bool;
    who = ComponentName.readFromParcel(paramParcel);
    thisTime = paramParcel.readLong();
    totalTime = paramParcel.readLong();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void dump(PrintWriter paramPrintWriter, String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("WaitResult:");
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("  result=");
    localStringBuilder.append(result);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("  timeout=");
    localStringBuilder.append(timeout);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("  who=");
    localStringBuilder.append(who);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("  thisTime=");
    localStringBuilder.append(thisTime);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("  totalTime=");
    localStringBuilder.append(totalTime);
    paramPrintWriter.println(localStringBuilder.toString());
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(result);
    paramParcel.writeInt(timeout);
    ComponentName.writeToParcel(who, paramParcel);
    paramParcel.writeLong(thisTime);
    paramParcel.writeLong(totalTime);
  }
}
