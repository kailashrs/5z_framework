package android.printservice.recommendation;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SystemApi
public final class RecommendationInfo
  implements Parcelable
{
  public static final Parcelable.Creator<RecommendationInfo> CREATOR = new Parcelable.Creator()
  {
    public RecommendationInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new RecommendationInfo(paramAnonymousParcel, null);
    }
    
    public RecommendationInfo[] newArray(int paramAnonymousInt)
    {
      return new RecommendationInfo[paramAnonymousInt];
    }
  };
  private final List<InetAddress> mDiscoveredPrinters;
  private final CharSequence mName;
  private final CharSequence mPackageName;
  private final boolean mRecommendsMultiVendorService;
  
  private RecommendationInfo(Parcel paramParcel)
  {
    this(localCharSequence1, localCharSequence2, localArrayList, bool);
  }
  
  @Deprecated
  public RecommendationInfo(CharSequence paramCharSequence1, CharSequence paramCharSequence2, int paramInt, boolean paramBoolean)
  {
    throw new IllegalArgumentException("This constructor has been deprecated");
  }
  
  public RecommendationInfo(CharSequence paramCharSequence1, CharSequence paramCharSequence2, List<InetAddress> paramList, boolean paramBoolean)
  {
    mPackageName = Preconditions.checkStringNotEmpty(paramCharSequence1);
    mName = Preconditions.checkStringNotEmpty(paramCharSequence2);
    mDiscoveredPrinters = ((List)Preconditions.checkCollectionElementsNotNull(paramList, "discoveredPrinters"));
    mRecommendsMultiVendorService = paramBoolean;
  }
  
  private static ArrayList<InetAddress> readDiscoveredPrinters(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    ArrayList localArrayList = new ArrayList(i);
    int j = 0;
    while (j < i) {
      try
      {
        localArrayList.add(InetAddress.getByAddress(paramParcel.readBlob()));
        j++;
      }
      catch (UnknownHostException paramParcel)
      {
        throw new IllegalArgumentException(paramParcel);
      }
    }
    return localArrayList;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public List<InetAddress> getDiscoveredPrinters()
  {
    return mDiscoveredPrinters;
  }
  
  public CharSequence getName()
  {
    return mName;
  }
  
  public int getNumDiscoveredPrinters()
  {
    return mDiscoveredPrinters.size();
  }
  
  public CharSequence getPackageName()
  {
    return mPackageName;
  }
  
  public boolean recommendsMultiVendorService()
  {
    return mRecommendsMultiVendorService;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeCharSequence(mPackageName);
    paramParcel.writeCharSequence(mName);
    paramParcel.writeInt(mDiscoveredPrinters.size());
    Iterator localIterator = mDiscoveredPrinters.iterator();
    while (localIterator.hasNext()) {
      paramParcel.writeBlob(((InetAddress)localIterator.next()).getAddress());
    }
    paramParcel.writeByte((byte)mRecommendsMultiVendorService);
  }
}
