package android.content.pm;

import android.annotation.SystemApi;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@SystemApi
public final class InstantAppResolveInfo
  implements Parcelable
{
  public static final Parcelable.Creator<InstantAppResolveInfo> CREATOR = new Parcelable.Creator()
  {
    public InstantAppResolveInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new InstantAppResolveInfo(paramAnonymousParcel);
    }
    
    public InstantAppResolveInfo[] newArray(int paramAnonymousInt)
    {
      return new InstantAppResolveInfo[paramAnonymousInt];
    }
  };
  private static final byte[] EMPTY_DIGEST = new byte[0];
  private static final String SHA_ALGORITHM = "SHA-256";
  private final InstantAppDigest mDigest;
  private final Bundle mExtras;
  private final List<InstantAppIntentFilter> mFilters;
  private final String mPackageName;
  private final boolean mShouldLetInstallerDecide;
  private final long mVersionCode;
  
  public InstantAppResolveInfo(InstantAppDigest paramInstantAppDigest, String paramString, List<InstantAppIntentFilter> paramList, int paramInt)
  {
    this(paramInstantAppDigest, paramString, paramList, paramInt, null);
  }
  
  public InstantAppResolveInfo(InstantAppDigest paramInstantAppDigest, String paramString, List<InstantAppIntentFilter> paramList, long paramLong, Bundle paramBundle)
  {
    this(paramInstantAppDigest, paramString, paramList, paramLong, paramBundle, false);
  }
  
  private InstantAppResolveInfo(InstantAppDigest paramInstantAppDigest, String paramString, List<InstantAppIntentFilter> paramList, long paramLong, Bundle paramBundle, boolean paramBoolean)
  {
    if (((paramString == null) && (paramList != null) && (paramList.size() != 0)) || ((paramString != null) && ((paramList == null) || (paramList.size() == 0)))) {
      throw new IllegalArgumentException();
    }
    mDigest = paramInstantAppDigest;
    if (paramList != null)
    {
      mFilters = new ArrayList(paramList.size());
      mFilters.addAll(paramList);
    }
    else
    {
      mFilters = null;
    }
    mPackageName = paramString;
    mVersionCode = paramLong;
    mExtras = paramBundle;
    mShouldLetInstallerDecide = paramBoolean;
  }
  
  public InstantAppResolveInfo(Bundle paramBundle)
  {
    this(InstantAppDigest.UNDEFINED, null, null, -1L, paramBundle, true);
  }
  
  InstantAppResolveInfo(Parcel paramParcel)
  {
    mShouldLetInstallerDecide = paramParcel.readBoolean();
    mExtras = paramParcel.readBundle();
    if (mShouldLetInstallerDecide)
    {
      mDigest = InstantAppDigest.UNDEFINED;
      mPackageName = null;
      mFilters = Collections.emptyList();
      mVersionCode = -1L;
    }
    else
    {
      mDigest = ((InstantAppDigest)paramParcel.readParcelable(null));
      mPackageName = paramParcel.readString();
      mFilters = new ArrayList();
      paramParcel.readList(mFilters, null);
      mVersionCode = paramParcel.readLong();
    }
  }
  
  public InstantAppResolveInfo(String paramString1, String paramString2, List<InstantAppIntentFilter> paramList)
  {
    this(new InstantAppDigest(paramString1), paramString2, paramList, -1L, null);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public byte[] getDigestBytes()
  {
    byte[] arrayOfByte;
    if (mDigest.mDigestBytes.length > 0) {
      arrayOfByte = mDigest.getDigestBytes()[0];
    } else {
      arrayOfByte = EMPTY_DIGEST;
    }
    return arrayOfByte;
  }
  
  public int getDigestPrefix()
  {
    return mDigest.getDigestPrefix()[0];
  }
  
  public Bundle getExtras()
  {
    return mExtras;
  }
  
  public List<InstantAppIntentFilter> getIntentFilters()
  {
    return mFilters;
  }
  
  public long getLongVersionCode()
  {
    return mVersionCode;
  }
  
  public String getPackageName()
  {
    return mPackageName;
  }
  
  @Deprecated
  public int getVersionCode()
  {
    return (int)(mVersionCode & 0xFFFFFFFFFFFFFFFF);
  }
  
  public boolean shouldLetInstallerDecide()
  {
    return mShouldLetInstallerDecide;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeBoolean(mShouldLetInstallerDecide);
    paramParcel.writeBundle(mExtras);
    if (mShouldLetInstallerDecide) {
      return;
    }
    paramParcel.writeParcelable(mDigest, paramInt);
    paramParcel.writeString(mPackageName);
    paramParcel.writeList(mFilters);
    paramParcel.writeLong(mVersionCode);
  }
  
  @SystemApi
  public static final class InstantAppDigest
    implements Parcelable
  {
    public static final Parcelable.Creator<InstantAppDigest> CREATOR = new Parcelable.Creator()
    {
      public InstantAppResolveInfo.InstantAppDigest createFromParcel(Parcel paramAnonymousParcel)
      {
        if (paramAnonymousParcel.readBoolean()) {
          return InstantAppResolveInfo.InstantAppDigest.UNDEFINED;
        }
        return new InstantAppResolveInfo.InstantAppDigest(paramAnonymousParcel);
      }
      
      public InstantAppResolveInfo.InstantAppDigest[] newArray(int paramAnonymousInt)
      {
        return new InstantAppResolveInfo.InstantAppDigest[paramAnonymousInt];
      }
    };
    static final int DIGEST_MASK = -4096;
    public static final InstantAppDigest UNDEFINED = new InstantAppDigest(new byte[0][], new int[0]);
    private static Random sRandom = null;
    private final byte[][] mDigestBytes;
    private final int[] mDigestPrefix;
    private int[] mDigestPrefixSecure;
    
    static
    {
      try
      {
        sRandom = SecureRandom.getInstance("SHA1PRNG");
      }
      catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
      {
        sRandom = new Random();
      }
    }
    
    InstantAppDigest(Parcel paramParcel)
    {
      int i = paramParcel.readInt();
      if (i == -1)
      {
        mDigestBytes = null;
      }
      else
      {
        mDigestBytes = new byte[i][];
        for (int j = 0; j < i; j++) {
          mDigestBytes[j] = paramParcel.createByteArray();
        }
      }
      mDigestPrefix = paramParcel.createIntArray();
      mDigestPrefixSecure = paramParcel.createIntArray();
    }
    
    public InstantAppDigest(String paramString)
    {
      this(paramString, -1);
    }
    
    public InstantAppDigest(String paramString, int paramInt)
    {
      if (paramString != null)
      {
        mDigestBytes = generateDigest(paramString.toLowerCase(Locale.ENGLISH), paramInt);
        mDigestPrefix = new int[mDigestBytes.length];
        for (paramInt = 0; paramInt < mDigestBytes.length; paramInt++) {
          mDigestPrefix[paramInt] = (((mDigestBytes[paramInt][0] & 0xFF) << 24 | (mDigestBytes[paramInt][1] & 0xFF) << 16 | (mDigestBytes[paramInt][2] & 0xFF) << 8 | (mDigestBytes[paramInt][3] & 0xFF) << 0) & 0xF000);
        }
        return;
      }
      throw new IllegalArgumentException();
    }
    
    private InstantAppDigest(byte[][] paramArrayOfByte, int[] paramArrayOfInt)
    {
      mDigestPrefix = paramArrayOfInt;
      mDigestBytes = paramArrayOfByte;
    }
    
    private static byte[][] generateDigest(String paramString, int paramInt)
    {
      ArrayList localArrayList = new ArrayList();
      try
      {
        MessageDigest localMessageDigest = MessageDigest.getInstance("SHA-256");
        if (paramInt <= 0)
        {
          localArrayList.add(localMessageDigest.digest(paramString.getBytes()));
        }
        else
        {
          int i = paramString.lastIndexOf('.', paramString.lastIndexOf(46) - 1);
          if (i < 0)
          {
            localArrayList.add(localMessageDigest.digest(paramString.getBytes()));
          }
          else
          {
            localArrayList.add(localMessageDigest.digest(paramString.substring(i + 1, paramString.length()).getBytes()));
            for (int j = 1; (i >= 0) && (j < paramInt); j++)
            {
              i = paramString.lastIndexOf('.', i - 1);
              localArrayList.add(localMessageDigest.digest(paramString.substring(i + 1, paramString.length()).getBytes()));
            }
          }
        }
        return (byte[][])localArrayList.toArray(new byte[localArrayList.size()][]);
      }
      catch (NoSuchAlgorithmException paramString)
      {
        throw new IllegalStateException("could not find digest algorithm");
      }
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public byte[][] getDigestBytes()
    {
      return mDigestBytes;
    }
    
    public int[] getDigestPrefix()
    {
      return mDigestPrefix;
    }
    
    public int[] getDigestPrefixSecure()
    {
      if (this == UNDEFINED) {
        return getDigestPrefix();
      }
      if (mDigestPrefixSecure == null)
      {
        int i = getDigestPrefix().length;
        int j = i + 10 + sRandom.nextInt(10);
        mDigestPrefixSecure = Arrays.copyOf(getDigestPrefix(), j);
        while (i < j)
        {
          mDigestPrefixSecure[i] = (sRandom.nextInt() & 0xF000);
          i++;
        }
        Arrays.sort(mDigestPrefixSecure);
      }
      return mDigestPrefixSecure;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      InstantAppDigest localInstantAppDigest = UNDEFINED;
      paramInt = 0;
      boolean bool;
      if (this == localInstantAppDigest) {
        bool = true;
      } else {
        bool = false;
      }
      paramParcel.writeBoolean(bool);
      if (bool) {
        return;
      }
      if (mDigestBytes == null)
      {
        paramParcel.writeInt(-1);
      }
      else
      {
        paramParcel.writeInt(mDigestBytes.length);
        while (paramInt < mDigestBytes.length)
        {
          paramParcel.writeByteArray(mDigestBytes[paramInt]);
          paramInt++;
        }
      }
      paramParcel.writeIntArray(mDigestPrefix);
      paramParcel.writeIntArray(mDigestPrefixSecure);
    }
  }
}
