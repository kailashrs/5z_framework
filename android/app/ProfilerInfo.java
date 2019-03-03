package android.app;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import java.io.IOException;
import java.util.Objects;

public class ProfilerInfo
  implements Parcelable
{
  public static final Parcelable.Creator<ProfilerInfo> CREATOR = new Parcelable.Creator()
  {
    public ProfilerInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ProfilerInfo(paramAnonymousParcel, null);
    }
    
    public ProfilerInfo[] newArray(int paramAnonymousInt)
    {
      return new ProfilerInfo[paramAnonymousInt];
    }
  };
  private static final String TAG = "ProfilerInfo";
  public final String agent;
  public final boolean attachAgentDuringBind;
  public final boolean autoStopProfiler;
  public ParcelFileDescriptor profileFd;
  public final String profileFile;
  public final int samplingInterval;
  public final boolean streamingOutput;
  
  public ProfilerInfo(ProfilerInfo paramProfilerInfo)
  {
    profileFile = profileFile;
    profileFd = profileFd;
    samplingInterval = samplingInterval;
    autoStopProfiler = autoStopProfiler;
    streamingOutput = streamingOutput;
    agent = agent;
    attachAgentDuringBind = attachAgentDuringBind;
  }
  
  private ProfilerInfo(Parcel paramParcel)
  {
    profileFile = paramParcel.readString();
    ParcelFileDescriptor localParcelFileDescriptor;
    if (paramParcel.readInt() != 0) {
      localParcelFileDescriptor = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel);
    } else {
      localParcelFileDescriptor = null;
    }
    profileFd = localParcelFileDescriptor;
    samplingInterval = paramParcel.readInt();
    int i = paramParcel.readInt();
    boolean bool1 = false;
    if (i != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    autoStopProfiler = bool2;
    boolean bool2 = bool1;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    }
    streamingOutput = bool2;
    agent = paramParcel.readString();
    attachAgentDuringBind = paramParcel.readBoolean();
  }
  
  public ProfilerInfo(String paramString1, ParcelFileDescriptor paramParcelFileDescriptor, int paramInt, boolean paramBoolean1, boolean paramBoolean2, String paramString2, boolean paramBoolean3)
  {
    profileFile = paramString1;
    profileFd = paramParcelFileDescriptor;
    samplingInterval = paramInt;
    autoStopProfiler = paramBoolean1;
    streamingOutput = paramBoolean2;
    agent = paramString2;
    attachAgentDuringBind = paramBoolean3;
  }
  
  public void closeFd()
  {
    if (profileFd != null)
    {
      try
      {
        profileFd.close();
      }
      catch (IOException localIOException)
      {
        Slog.w("ProfilerInfo", "Failure closing profile fd", localIOException);
      }
      profileFd = null;
    }
  }
  
  public int describeContents()
  {
    if (profileFd != null) {
      return profileFd.describeContents();
    }
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
      paramObject = (ProfilerInfo)paramObject;
      if ((!Objects.equals(profileFile, profileFile)) || (autoStopProfiler != autoStopProfiler) || (samplingInterval != samplingInterval) || (streamingOutput != streamingOutput) || (!Objects.equals(agent, agent))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public int hashCode()
  {
    return 31 * (31 * (31 * (31 * (31 * 17 + Objects.hashCode(profileFile)) + samplingInterval) + autoStopProfiler) + streamingOutput) + Objects.hashCode(agent);
  }
  
  public ProfilerInfo setAgent(String paramString, boolean paramBoolean)
  {
    return new ProfilerInfo(profileFile, profileFd, samplingInterval, autoStopProfiler, streamingOutput, paramString, paramBoolean);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(profileFile);
    if (profileFd != null)
    {
      paramParcel.writeInt(1);
      profileFd.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeInt(samplingInterval);
    paramParcel.writeInt(autoStopProfiler);
    paramParcel.writeInt(streamingOutput);
    paramParcel.writeString(agent);
    paramParcel.writeBoolean(attachAgentDuringBind);
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    paramProtoOutputStream.write(1138166333441L, profileFile);
    if (profileFd != null) {
      paramProtoOutputStream.write(1120986464258L, profileFd.getFd());
    }
    paramProtoOutputStream.write(1120986464259L, samplingInterval);
    paramProtoOutputStream.write(1133871366148L, autoStopProfiler);
    paramProtoOutputStream.write(1133871366149L, streamingOutput);
    paramProtoOutputStream.write(1138166333446L, agent);
    paramProtoOutputStream.end(paramLong);
  }
}
