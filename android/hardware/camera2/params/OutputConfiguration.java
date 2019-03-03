package android.hardware.camera2.params;

import android.annotation.SystemApi;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.utils.HashCodeHelpers;
import android.hardware.camera2.utils.SurfaceUtils;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class OutputConfiguration
  implements Parcelable
{
  public static final Parcelable.Creator<OutputConfiguration> CREATOR = new Parcelable.Creator()
  {
    public OutputConfiguration createFromParcel(Parcel paramAnonymousParcel)
    {
      try
      {
        paramAnonymousParcel = new OutputConfiguration(paramAnonymousParcel, null);
        return paramAnonymousParcel;
      }
      catch (Exception paramAnonymousParcel)
      {
        Log.e("OutputConfiguration", "Exception creating OutputConfiguration from parcel", paramAnonymousParcel);
      }
      return null;
    }
    
    public OutputConfiguration[] newArray(int paramAnonymousInt)
    {
      return new OutputConfiguration[paramAnonymousInt];
    }
  };
  private static final int MAX_SURFACES_COUNT = 4;
  @SystemApi
  public static final int ROTATION_0 = 0;
  @SystemApi
  public static final int ROTATION_180 = 2;
  @SystemApi
  public static final int ROTATION_270 = 3;
  @SystemApi
  public static final int ROTATION_90 = 1;
  public static final int SURFACE_GROUP_ID_NONE = -1;
  private static final String TAG = "OutputConfiguration";
  private final int SURFACE_TYPE_SURFACE_TEXTURE;
  private final int SURFACE_TYPE_SURFACE_VIEW = 0;
  private final int SURFACE_TYPE_UNKNOWN = -1;
  private final int mConfiguredDataspace;
  private final int mConfiguredFormat;
  private final int mConfiguredGenerationId;
  private final Size mConfiguredSize;
  private final boolean mIsDeferredConfig;
  private boolean mIsShared;
  private String mPhysicalCameraId;
  private final int mRotation;
  private final int mSurfaceGroupId;
  private final int mSurfaceType;
  private ArrayList<Surface> mSurfaces;
  
  public OutputConfiguration(int paramInt, Surface paramSurface)
  {
    this(paramInt, paramSurface, 0);
  }
  
  @SystemApi
  public OutputConfiguration(int paramInt1, Surface paramSurface, int paramInt2)
  {
    SURFACE_TYPE_SURFACE_TEXTURE = 1;
    Preconditions.checkNotNull(paramSurface, "Surface must not be null");
    Preconditions.checkArgumentInRange(paramInt2, 0, 3, "Rotation constant");
    mSurfaceGroupId = paramInt1;
    mSurfaceType = -1;
    mSurfaces = new ArrayList();
    mSurfaces.add(paramSurface);
    mRotation = paramInt2;
    mConfiguredSize = SurfaceUtils.getSurfaceSize(paramSurface);
    mConfiguredFormat = SurfaceUtils.getSurfaceFormat(paramSurface);
    mConfiguredDataspace = SurfaceUtils.getSurfaceDataspace(paramSurface);
    mConfiguredGenerationId = paramSurface.getGenerationId();
    mIsDeferredConfig = false;
    mIsShared = false;
    mPhysicalCameraId = null;
  }
  
  public OutputConfiguration(OutputConfiguration paramOutputConfiguration)
  {
    SURFACE_TYPE_SURFACE_TEXTURE = 1;
    if (paramOutputConfiguration != null)
    {
      mSurfaces = mSurfaces;
      mRotation = mRotation;
      mSurfaceGroupId = mSurfaceGroupId;
      mSurfaceType = mSurfaceType;
      mConfiguredDataspace = mConfiguredDataspace;
      mConfiguredFormat = mConfiguredFormat;
      mConfiguredSize = mConfiguredSize;
      mConfiguredGenerationId = mConfiguredGenerationId;
      mIsDeferredConfig = mIsDeferredConfig;
      mIsShared = mIsShared;
      mPhysicalCameraId = mPhysicalCameraId;
      return;
    }
    throw new IllegalArgumentException("OutputConfiguration shouldn't be null");
  }
  
  private OutputConfiguration(Parcel paramParcel)
  {
    boolean bool1 = true;
    SURFACE_TYPE_SURFACE_TEXTURE = 1;
    int i = paramParcel.readInt();
    int j = paramParcel.readInt();
    int k = paramParcel.readInt();
    int m = paramParcel.readInt();
    int n = paramParcel.readInt();
    boolean bool2;
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    if (paramParcel.readInt() != 1) {
      bool1 = false;
    }
    ArrayList localArrayList = new ArrayList();
    paramParcel.readTypedList(localArrayList, Surface.CREATOR);
    paramParcel = paramParcel.readString();
    Preconditions.checkArgumentInRange(i, 0, 3, "Rotation constant");
    mSurfaceGroupId = j;
    mRotation = i;
    mSurfaces = localArrayList;
    mConfiguredSize = new Size(m, n);
    mIsDeferredConfig = bool2;
    mIsShared = bool1;
    mSurfaces = localArrayList;
    if (mSurfaces.size() > 0)
    {
      mSurfaceType = -1;
      mConfiguredFormat = SurfaceUtils.getSurfaceFormat((Surface)mSurfaces.get(0));
      mConfiguredDataspace = SurfaceUtils.getSurfaceDataspace((Surface)mSurfaces.get(0));
      mConfiguredGenerationId = ((Surface)mSurfaces.get(0)).getGenerationId();
    }
    else
    {
      mSurfaceType = k;
      mConfiguredFormat = StreamConfigurationMap.imageFormatToInternal(34);
      mConfiguredDataspace = StreamConfigurationMap.imageFormatToDataspace(34);
      mConfiguredGenerationId = 0;
    }
    mPhysicalCameraId = paramParcel;
  }
  
  public <T> OutputConfiguration(Size paramSize, Class<T> paramClass)
  {
    SURFACE_TYPE_SURFACE_TEXTURE = 1;
    Preconditions.checkNotNull(paramClass, "surfaceSize must not be null");
    Preconditions.checkNotNull(paramClass, "klass must not be null");
    if (paramClass == SurfaceHolder.class)
    {
      mSurfaceType = 0;
    }
    else
    {
      if (paramClass != SurfaceTexture.class) {
        break label147;
      }
      mSurfaceType = 1;
    }
    if ((paramSize.getWidth() != 0) && (paramSize.getHeight() != 0))
    {
      mSurfaceGroupId = -1;
      mSurfaces = new ArrayList();
      mRotation = 0;
      mConfiguredSize = paramSize;
      mConfiguredFormat = StreamConfigurationMap.imageFormatToInternal(34);
      mConfiguredDataspace = StreamConfigurationMap.imageFormatToDataspace(34);
      mConfiguredGenerationId = 0;
      mIsDeferredConfig = true;
      mIsShared = false;
      mPhysicalCameraId = null;
      return;
    }
    throw new IllegalArgumentException("Surface size needs to be non-zero");
    label147:
    mSurfaceType = -1;
    throw new IllegalArgumentException("Unknow surface source class type");
  }
  
  public OutputConfiguration(Surface paramSurface)
  {
    this(-1, paramSurface, 0);
  }
  
  @SystemApi
  public OutputConfiguration(Surface paramSurface, int paramInt)
  {
    this(-1, paramSurface, paramInt);
  }
  
  public void addSurface(Surface paramSurface)
  {
    Preconditions.checkNotNull(paramSurface, "Surface must not be null");
    if (!mSurfaces.contains(paramSurface))
    {
      if ((mSurfaces.size() == 1) && (!mIsShared)) {
        throw new IllegalStateException("Cannot have 2 surfaces for a non-sharing configuration");
      }
      if (mSurfaces.size() + 1 <= 4)
      {
        Size localSize = SurfaceUtils.getSurfaceSize(paramSurface);
        if (!localSize.equals(mConfiguredSize))
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Added surface size ");
          localStringBuilder.append(localSize);
          localStringBuilder.append(" is different than pre-configured size ");
          localStringBuilder.append(mConfiguredSize);
          localStringBuilder.append(", the pre-configured size will be used.");
          Log.w("OutputConfiguration", localStringBuilder.toString());
        }
        if (mConfiguredFormat == SurfaceUtils.getSurfaceFormat(paramSurface))
        {
          if ((mConfiguredFormat != 34) && (mConfiguredDataspace != SurfaceUtils.getSurfaceDataspace(paramSurface))) {
            throw new IllegalArgumentException("The dataspace of added surface doesn't match");
          }
          mSurfaces.add(paramSurface);
          return;
        }
        throw new IllegalArgumentException("The format of added surface format doesn't match");
      }
      throw new IllegalArgumentException("Exceeds maximum number of surfaces");
    }
    throw new IllegalStateException("Surface is already added!");
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void enableSurfaceSharing()
  {
    mIsShared = true;
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == null) {
      return false;
    }
    if (this == paramObject) {
      return true;
    }
    if ((paramObject instanceof OutputConfiguration))
    {
      paramObject = (OutputConfiguration)paramObject;
      if ((mRotation == mRotation) && (mConfiguredSize.equals(mConfiguredSize)) && (mConfiguredFormat == mConfiguredFormat) && (mSurfaceGroupId == mSurfaceGroupId) && (mSurfaceType == mSurfaceType) && (mIsDeferredConfig == mIsDeferredConfig) && (mIsShared == mIsShared) && (mConfiguredFormat == mConfiguredFormat) && (mConfiguredDataspace == mConfiguredDataspace) && (mConfiguredGenerationId == mConfiguredGenerationId))
      {
        int i = Math.min(mSurfaces.size(), mSurfaces.size());
        for (int j = 0; j < i; j++) {
          if (mSurfaces.get(j) != mSurfaces.get(j)) {
            return false;
          }
        }
        return true;
      }
      return false;
    }
    return false;
  }
  
  public int getMaxSharedSurfaceCount()
  {
    return 4;
  }
  
  @SystemApi
  public int getRotation()
  {
    return mRotation;
  }
  
  public Surface getSurface()
  {
    if (mSurfaces.size() == 0) {
      return null;
    }
    return (Surface)mSurfaces.get(0);
  }
  
  public int getSurfaceGroupId()
  {
    return mSurfaceGroupId;
  }
  
  public List<Surface> getSurfaces()
  {
    return Collections.unmodifiableList(mSurfaces);
  }
  
  public int hashCode()
  {
    boolean bool1 = mIsDeferredConfig;
    int i = 0;
    int j = 0;
    if (bool1)
    {
      k = mRotation;
      m = mConfiguredSize.hashCode();
      n = mConfiguredFormat;
      i1 = mConfiguredDataspace;
      i2 = mSurfaceGroupId;
      i3 = mSurfaceType;
      bool2 = mIsShared;
      if (mPhysicalCameraId == null) {
        i = j;
      } else {
        i = mPhysicalCameraId.hashCode();
      }
      return HashCodeHelpers.hashCode(new int[] { k, m, n, i1, i2, i3, bool2, i });
    }
    int i2 = mRotation;
    int i1 = mSurfaces.hashCode();
    int n = mConfiguredGenerationId;
    j = mConfiguredSize.hashCode();
    int k = mConfiguredFormat;
    int i3 = mConfiguredDataspace;
    int m = mSurfaceGroupId;
    boolean bool2 = mIsShared;
    if (mPhysicalCameraId != null) {
      i = mPhysicalCameraId.hashCode();
    }
    return HashCodeHelpers.hashCode(new int[] { i2, i1, n, j, k, i3, m, bool2, i });
  }
  
  public boolean isDeferredConfiguration()
  {
    return mIsDeferredConfig;
  }
  
  public boolean isForPhysicalCamera()
  {
    boolean bool;
    if (mPhysicalCameraId != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void removeSurface(Surface paramSurface)
  {
    if (getSurface() != paramSurface)
    {
      if (mSurfaces.remove(paramSurface)) {
        return;
      }
      throw new IllegalArgumentException("Surface is not part of this output configuration");
    }
    throw new IllegalArgumentException("Cannot remove surface associated with this output configuration");
  }
  
  public void setPhysicalCameraId(String paramString)
  {
    mPhysicalCameraId = paramString;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (paramParcel != null)
    {
      paramParcel.writeInt(mRotation);
      paramParcel.writeInt(mSurfaceGroupId);
      paramParcel.writeInt(mSurfaceType);
      paramParcel.writeInt(mConfiguredSize.getWidth());
      paramParcel.writeInt(mConfiguredSize.getHeight());
      paramParcel.writeInt(mIsDeferredConfig);
      paramParcel.writeInt(mIsShared);
      paramParcel.writeTypedList(mSurfaces);
      paramParcel.writeString(mPhysicalCameraId);
      return;
    }
    throw new IllegalArgumentException("dest must not be null");
  }
}
