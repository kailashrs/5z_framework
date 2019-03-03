package android.media.audiopolicy;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioFormat.Builder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class AudioPolicyConfig
  implements Parcelable
{
  public static final Parcelable.Creator<AudioPolicyConfig> CREATOR = new Parcelable.Creator()
  {
    public AudioPolicyConfig createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AudioPolicyConfig(paramAnonymousParcel, null);
    }
    
    public AudioPolicyConfig[] newArray(int paramAnonymousInt)
    {
      return new AudioPolicyConfig[paramAnonymousInt];
    }
  };
  private static final String TAG = "AudioPolicyConfig";
  protected int mDuckingPolicy = 0;
  private int mMixCounter = 0;
  protected final ArrayList<AudioMix> mMixes;
  private String mRegistrationId = null;
  
  protected AudioPolicyConfig(AudioPolicyConfig paramAudioPolicyConfig)
  {
    mMixes = mMixes;
  }
  
  private AudioPolicyConfig(Parcel paramParcel)
  {
    mMixes = new ArrayList();
    int i = paramParcel.readInt();
    for (int j = 0; j < i; j++)
    {
      AudioMix.Builder localBuilder = new AudioMix.Builder();
      localBuilder.setRouteFlags(paramParcel.readInt());
      localBuilder.setCallbackFlags(paramParcel.readInt());
      localBuilder.setDevice(paramParcel.readInt(), paramParcel.readString());
      int k = paramParcel.readInt();
      int m = paramParcel.readInt();
      int n = paramParcel.readInt();
      localBuilder.setFormat(new AudioFormat.Builder().setSampleRate(k).setChannelMask(n).setEncoding(m).build());
      n = paramParcel.readInt();
      AudioMixingRule.Builder localBuilder1 = new AudioMixingRule.Builder();
      for (k = 0; k < n; k++) {
        localBuilder1.addRuleFromParcel(paramParcel);
      }
      localBuilder.setMixingRule(localBuilder1.build());
      mMixes.add(localBuilder.build());
    }
  }
  
  AudioPolicyConfig(ArrayList<AudioMix> paramArrayList)
  {
    mMixes = paramArrayList;
  }
  
  private static String mixTypeId(int paramInt)
  {
    if (paramInt == 0) {
      return "p";
    }
    if (paramInt == 1) {
      return "r";
    }
    return "i";
  }
  
  private void setMixRegistration(AudioMix paramAudioMix)
  {
    if (!mRegistrationId.isEmpty())
    {
      if ((paramAudioMix.getRouteFlags() & 0x2) == 2)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(mRegistrationId);
        localStringBuilder.append("mix");
        localStringBuilder.append(mixTypeId(paramAudioMix.getMixType()));
        localStringBuilder.append(":");
        localStringBuilder.append(mMixCounter);
        paramAudioMix.setRegistration(localStringBuilder.toString());
      }
      else if ((paramAudioMix.getRouteFlags() & 0x1) == 1)
      {
        paramAudioMix.setRegistration(mDeviceAddress);
      }
    }
    else {
      paramAudioMix.setRegistration("");
    }
    mMixCounter += 1;
  }
  
  @GuardedBy("mMixes")
  protected void add(ArrayList<AudioMix> paramArrayList)
  {
    paramArrayList = paramArrayList.iterator();
    while (paramArrayList.hasNext())
    {
      AudioMix localAudioMix = (AudioMix)paramArrayList.next();
      setMixRegistration(localAudioMix);
      mMixes.add(localAudioMix);
    }
  }
  
  public void addMix(AudioMix paramAudioMix)
    throws IllegalArgumentException
  {
    if (paramAudioMix != null)
    {
      mMixes.add(paramAudioMix);
      return;
    }
    throw new IllegalArgumentException("Illegal null AudioMix argument");
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public ArrayList<AudioMix> getMixes()
  {
    return mMixes;
  }
  
  protected String getRegistration()
  {
    return mRegistrationId;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { mMixes });
  }
  
  @GuardedBy("mMixes")
  protected void remove(ArrayList<AudioMix> paramArrayList)
  {
    paramArrayList = paramArrayList.iterator();
    while (paramArrayList.hasNext())
    {
      AudioMix localAudioMix = (AudioMix)paramArrayList.next();
      mMixes.remove(localAudioMix);
    }
  }
  
  protected void setRegistration(String paramString)
  {
    Object localObject = mRegistrationId;
    int i = 1;
    int j;
    if ((localObject != null) && (!mRegistrationId.isEmpty())) {
      j = 0;
    } else {
      j = 1;
    }
    int k = i;
    if (paramString != null) {
      if (paramString.isEmpty()) {
        k = i;
      } else {
        k = 0;
      }
    }
    if ((j == 0) && (k == 0) && (!mRegistrationId.equals(paramString)))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Invalid registration transition from ");
      ((StringBuilder)localObject).append(mRegistrationId);
      ((StringBuilder)localObject).append(" to ");
      ((StringBuilder)localObject).append(paramString);
      Log.e("AudioPolicyConfig", ((StringBuilder)localObject).toString());
      return;
    }
    if (paramString == null) {
      paramString = "";
    }
    mRegistrationId = paramString;
    paramString = mMixes.iterator();
    while (paramString.hasNext()) {
      setMixRegistration((AudioMix)paramString.next());
    }
  }
  
  public String toLogFriendlyString()
  {
    Object localObject1 = new String("android.media.audiopolicy.AudioPolicyConfig:\n");
    Object localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append((String)localObject1);
    ((StringBuilder)localObject2).append(mMixes.size());
    ((StringBuilder)localObject2).append(" AudioMix: ");
    ((StringBuilder)localObject2).append(mRegistrationId);
    ((StringBuilder)localObject2).append("\n");
    localObject1 = ((StringBuilder)localObject2).toString();
    localObject2 = mMixes.iterator();
    while (((Iterator)localObject2).hasNext())
    {
      Object localObject3 = (AudioMix)((Iterator)localObject2).next();
      Object localObject4 = new StringBuilder();
      ((StringBuilder)localObject4).append((String)localObject1);
      ((StringBuilder)localObject4).append("* route flags=0x");
      ((StringBuilder)localObject4).append(Integer.toHexString(((AudioMix)localObject3).getRouteFlags()));
      ((StringBuilder)localObject4).append("\n");
      localObject1 = ((StringBuilder)localObject4).toString();
      localObject4 = new StringBuilder();
      ((StringBuilder)localObject4).append((String)localObject1);
      ((StringBuilder)localObject4).append("  rate=");
      ((StringBuilder)localObject4).append(((AudioMix)localObject3).getFormat().getSampleRate());
      ((StringBuilder)localObject4).append("Hz\n");
      localObject1 = ((StringBuilder)localObject4).toString();
      localObject4 = new StringBuilder();
      ((StringBuilder)localObject4).append((String)localObject1);
      ((StringBuilder)localObject4).append("  encoding=");
      ((StringBuilder)localObject4).append(((AudioMix)localObject3).getFormat().getEncoding());
      ((StringBuilder)localObject4).append("\n");
      localObject1 = ((StringBuilder)localObject4).toString();
      localObject4 = new StringBuilder();
      ((StringBuilder)localObject4).append((String)localObject1);
      ((StringBuilder)localObject4).append("  channels=0x");
      localObject1 = ((StringBuilder)localObject4).toString();
      localObject4 = new StringBuilder();
      ((StringBuilder)localObject4).append((String)localObject1);
      ((StringBuilder)localObject4).append(Integer.toHexString(((AudioMix)localObject3).getFormat().getChannelMask()).toUpperCase());
      ((StringBuilder)localObject4).append("\n");
      localObject1 = ((StringBuilder)localObject4).toString();
      localObject3 = ((AudioMix)localObject3).getRule().getCriteria().iterator();
      while (((Iterator)localObject3).hasNext())
      {
        localObject4 = (AudioMixingRule.AudioMixMatchCriterion)((Iterator)localObject3).next();
        Object localObject5;
        switch (mRule)
        {
        default: 
          localObject4 = new StringBuilder();
          ((StringBuilder)localObject4).append((String)localObject1);
          ((StringBuilder)localObject4).append("invalid rule!");
          localObject1 = ((StringBuilder)localObject4).toString();
          break;
        case 32772: 
          localObject5 = new StringBuilder();
          ((StringBuilder)localObject5).append((String)localObject1);
          ((StringBuilder)localObject5).append("  exclude UID ");
          localObject5 = ((StringBuilder)localObject5).toString();
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append((String)localObject5);
          ((StringBuilder)localObject1).append(mIntProp);
          localObject1 = ((StringBuilder)localObject1).toString();
          break;
        case 32770: 
          localObject5 = new StringBuilder();
          ((StringBuilder)localObject5).append((String)localObject1);
          ((StringBuilder)localObject5).append("  exclude capture preset ");
          localObject5 = ((StringBuilder)localObject5).toString();
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append((String)localObject5);
          ((StringBuilder)localObject1).append(mAttr.getCapturePreset());
          localObject1 = ((StringBuilder)localObject1).toString();
          break;
        case 32769: 
          localObject5 = new StringBuilder();
          ((StringBuilder)localObject5).append((String)localObject1);
          ((StringBuilder)localObject5).append("  exclude usage ");
          localObject1 = ((StringBuilder)localObject5).toString();
          localObject5 = new StringBuilder();
          ((StringBuilder)localObject5).append((String)localObject1);
          ((StringBuilder)localObject5).append(mAttr.usageToString());
          localObject1 = ((StringBuilder)localObject5).toString();
          break;
        case 4: 
          localObject5 = new StringBuilder();
          ((StringBuilder)localObject5).append((String)localObject1);
          ((StringBuilder)localObject5).append("  match UID ");
          localObject1 = ((StringBuilder)localObject5).toString();
          localObject5 = new StringBuilder();
          ((StringBuilder)localObject5).append((String)localObject1);
          ((StringBuilder)localObject5).append(mIntProp);
          localObject1 = ((StringBuilder)localObject5).toString();
          break;
        case 2: 
          localObject5 = new StringBuilder();
          ((StringBuilder)localObject5).append((String)localObject1);
          ((StringBuilder)localObject5).append("  match capture preset ");
          localObject5 = ((StringBuilder)localObject5).toString();
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append((String)localObject5);
          ((StringBuilder)localObject1).append(mAttr.getCapturePreset());
          localObject1 = ((StringBuilder)localObject1).toString();
          break;
        case 1: 
          localObject5 = new StringBuilder();
          ((StringBuilder)localObject5).append((String)localObject1);
          ((StringBuilder)localObject5).append("  match usage ");
          localObject1 = ((StringBuilder)localObject5).toString();
          localObject5 = new StringBuilder();
          ((StringBuilder)localObject5).append((String)localObject1);
          ((StringBuilder)localObject5).append(mAttr.usageToString());
          localObject1 = ((StringBuilder)localObject5).toString();
        }
        localObject4 = new StringBuilder();
        ((StringBuilder)localObject4).append((String)localObject1);
        ((StringBuilder)localObject4).append("\n");
        localObject1 = ((StringBuilder)localObject4).toString();
      }
    }
    return localObject1;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mMixes.size());
    Iterator localIterator = mMixes.iterator();
    while (localIterator.hasNext())
    {
      Object localObject = (AudioMix)localIterator.next();
      paramParcel.writeInt(((AudioMix)localObject).getRouteFlags());
      paramParcel.writeInt(mCallbackFlags);
      paramParcel.writeInt(mDeviceSystemType);
      paramParcel.writeString(mDeviceAddress);
      paramParcel.writeInt(((AudioMix)localObject).getFormat().getSampleRate());
      paramParcel.writeInt(((AudioMix)localObject).getFormat().getEncoding());
      paramParcel.writeInt(((AudioMix)localObject).getFormat().getChannelMask());
      localObject = ((AudioMix)localObject).getRule().getCriteria();
      paramParcel.writeInt(((ArrayList)localObject).size());
      localObject = ((ArrayList)localObject).iterator();
      while (((Iterator)localObject).hasNext()) {
        ((AudioMixingRule.AudioMixMatchCriterion)((Iterator)localObject).next()).writeToParcel(paramParcel);
      }
    }
  }
}
