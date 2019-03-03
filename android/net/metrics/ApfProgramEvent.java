package android.net.metrics;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.SparseArray;
import com.android.internal.util.MessageUtils;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public final class ApfProgramEvent
  implements Parcelable
{
  public static final Parcelable.Creator<ApfProgramEvent> CREATOR = new Parcelable.Creator()
  {
    public ApfProgramEvent createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ApfProgramEvent(paramAnonymousParcel, null);
    }
    
    public ApfProgramEvent[] newArray(int paramAnonymousInt)
    {
      return new ApfProgramEvent[paramAnonymousInt];
    }
  };
  public static final int FLAG_HAS_IPV4_ADDRESS = 1;
  public static final int FLAG_MULTICAST_FILTER_ON = 0;
  public long actualLifetime;
  public int currentRas;
  public int filteredRas;
  public int flags;
  public long lifetime;
  public int programLength;
  
  public ApfProgramEvent() {}
  
  private ApfProgramEvent(Parcel paramParcel)
  {
    lifetime = paramParcel.readLong();
    actualLifetime = paramParcel.readLong();
    filteredRas = paramParcel.readInt();
    currentRas = paramParcel.readInt();
    programLength = paramParcel.readInt();
    flags = paramParcel.readInt();
  }
  
  public static int flagsFor(boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = 0;
    if (paramBoolean1) {
      i = 0x0 | 0x2;
    }
    int j = i;
    if (paramBoolean2) {
      j = i | 0x1;
    }
    return j;
  }
  
  private static String namesOf(int paramInt)
  {
    ArrayList localArrayList = new ArrayList(Integer.bitCount(paramInt));
    BitSet localBitSet = BitSet.valueOf(new long[] { 0x7FFFFFFF & paramInt });
    for (paramInt = localBitSet.nextSetBit(0); paramInt >= 0; paramInt = localBitSet.nextSetBit(paramInt + 1)) {
      localArrayList.add((String)Decoder.constants.get(paramInt));
    }
    return TextUtils.join("|", localArrayList);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    Object localObject;
    if (lifetime < Long.MAX_VALUE)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(lifetime);
      ((StringBuilder)localObject).append("s");
      localObject = ((StringBuilder)localObject).toString();
    }
    else
    {
      localObject = "forever";
    }
    return String.format("ApfProgramEvent(%d/%d RAs %dB %ds/%s %s)", new Object[] { Integer.valueOf(filteredRas), Integer.valueOf(currentRas), Integer.valueOf(programLength), Long.valueOf(actualLifetime), localObject, namesOf(flags) });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(lifetime);
    paramParcel.writeLong(actualLifetime);
    paramParcel.writeInt(filteredRas);
    paramParcel.writeInt(currentRas);
    paramParcel.writeInt(programLength);
    paramParcel.writeInt(paramInt);
  }
  
  static final class Decoder
  {
    static final SparseArray<String> constants = MessageUtils.findMessageNames(new Class[] { ApfProgramEvent.class }, new String[] { "FLAG_" });
    
    Decoder() {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Flags {}
}
