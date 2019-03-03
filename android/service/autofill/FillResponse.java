package android.service.autofill;

import android.content.IntentSender;
import android.content.pm.ParceledListSlice;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.view.autofill.AutofillId;
import android.view.autofill.Helper;
import android.widget.RemoteViews;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FillResponse
  implements Parcelable
{
  public static final Parcelable.Creator<FillResponse> CREATOR = new Parcelable.Creator()
  {
    public FillResponse createFromParcel(Parcel paramAnonymousParcel)
    {
      FillResponse.Builder localBuilder = new FillResponse.Builder();
      Object localObject = (ParceledListSlice)paramAnonymousParcel.readParcelable(null);
      if (localObject != null) {
        localObject = ((ParceledListSlice)localObject).getList();
      } else {
        localObject = null;
      }
      int i = 0;
      int j;
      if (localObject != null) {
        j = ((List)localObject).size();
      } else {
        j = 0;
      }
      while (i < j)
      {
        localBuilder.addDataset((Dataset)((List)localObject).get(i));
        i++;
      }
      localBuilder.setSaveInfo((SaveInfo)paramAnonymousParcel.readParcelable(null));
      localBuilder.setClientState((Bundle)paramAnonymousParcel.readParcelable(null));
      localObject = (AutofillId[])paramAnonymousParcel.readParcelableArray(null, AutofillId.class);
      IntentSender localIntentSender = (IntentSender)paramAnonymousParcel.readParcelable(null);
      RemoteViews localRemoteViews = (RemoteViews)paramAnonymousParcel.readParcelable(null);
      if (localObject != null) {
        localBuilder.setAuthentication((AutofillId[])localObject, localIntentSender, localRemoteViews);
      }
      localObject = (RemoteViews)paramAnonymousParcel.readParcelable(null);
      if (localObject != null) {
        localBuilder.setHeader((RemoteViews)localObject);
      }
      localObject = (RemoteViews)paramAnonymousParcel.readParcelable(null);
      if (localObject != null) {
        localBuilder.setFooter((RemoteViews)localObject);
      }
      localBuilder.setIgnoredIds((AutofillId[])paramAnonymousParcel.readParcelableArray(null, AutofillId.class));
      long l = paramAnonymousParcel.readLong();
      if (l > 0L) {
        localBuilder.disableAutofill(l);
      }
      localObject = (AutofillId[])paramAnonymousParcel.readParcelableArray(null, AutofillId.class);
      if (localObject != null) {
        localBuilder.setFieldClassificationIds((AutofillId[])localObject);
      }
      localBuilder.setFlags(paramAnonymousParcel.readInt());
      localObject = localBuilder.build();
      ((FillResponse)localObject).setRequestId(paramAnonymousParcel.readInt());
      return localObject;
    }
    
    public FillResponse[] newArray(int paramAnonymousInt)
    {
      return new FillResponse[paramAnonymousInt];
    }
  };
  public static final int FLAG_DISABLE_ACTIVITY_ONLY = 2;
  public static final int FLAG_TRACK_CONTEXT_COMMITED = 1;
  private final IntentSender mAuthentication;
  private final AutofillId[] mAuthenticationIds;
  private final Bundle mClientState;
  private final ParceledListSlice<Dataset> mDatasets;
  private final long mDisableDuration;
  private final AutofillId[] mFieldClassificationIds;
  private final int mFlags;
  private final RemoteViews mFooter;
  private final RemoteViews mHeader;
  private final AutofillId[] mIgnoredIds;
  private final RemoteViews mPresentation;
  private int mRequestId;
  private final SaveInfo mSaveInfo;
  
  private FillResponse(Builder paramBuilder)
  {
    ParceledListSlice localParceledListSlice;
    if (mDatasets != null) {
      localParceledListSlice = new ParceledListSlice(mDatasets);
    } else {
      localParceledListSlice = null;
    }
    mDatasets = localParceledListSlice;
    mSaveInfo = mSaveInfo;
    mClientState = mClientState;
    mPresentation = mPresentation;
    mHeader = mHeader;
    mFooter = mFooter;
    mAuthentication = mAuthentication;
    mAuthenticationIds = mAuthenticationIds;
    mIgnoredIds = mIgnoredIds;
    mDisableDuration = mDisableDuration;
    mFieldClassificationIds = mFieldClassificationIds;
    mFlags = mFlags;
    mRequestId = Integer.MIN_VALUE;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public IntentSender getAuthentication()
  {
    return mAuthentication;
  }
  
  public AutofillId[] getAuthenticationIds()
  {
    return mAuthenticationIds;
  }
  
  public Bundle getClientState()
  {
    return mClientState;
  }
  
  public List<Dataset> getDatasets()
  {
    List localList;
    if (mDatasets != null) {
      localList = mDatasets.getList();
    } else {
      localList = null;
    }
    return localList;
  }
  
  public long getDisableDuration()
  {
    return mDisableDuration;
  }
  
  public AutofillId[] getFieldClassificationIds()
  {
    return mFieldClassificationIds;
  }
  
  public int getFlags()
  {
    return mFlags;
  }
  
  public RemoteViews getFooter()
  {
    return mFooter;
  }
  
  public RemoteViews getHeader()
  {
    return mHeader;
  }
  
  public AutofillId[] getIgnoredIds()
  {
    return mIgnoredIds;
  }
  
  public RemoteViews getPresentation()
  {
    return mPresentation;
  }
  
  public int getRequestId()
  {
    return mRequestId;
  }
  
  public SaveInfo getSaveInfo()
  {
    return mSaveInfo;
  }
  
  public void setRequestId(int paramInt)
  {
    mRequestId = paramInt;
  }
  
  public String toString()
  {
    if (!Helper.sDebug) {
      return super.toString();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("FillResponse : [mRequestId=");
    localStringBuilder.append(mRequestId);
    localStringBuilder = new StringBuilder(localStringBuilder.toString());
    if (mDatasets != null)
    {
      localStringBuilder.append(", datasets=");
      localStringBuilder.append(mDatasets.getList());
    }
    if (mSaveInfo != null)
    {
      localStringBuilder.append(", saveInfo=");
      localStringBuilder.append(mSaveInfo);
    }
    if (mClientState != null) {
      localStringBuilder.append(", hasClientState");
    }
    if (mPresentation != null) {
      localStringBuilder.append(", hasPresentation");
    }
    if (mHeader != null) {
      localStringBuilder.append(", hasHeader");
    }
    if (mFooter != null) {
      localStringBuilder.append(", hasFooter");
    }
    if (mAuthentication != null) {
      localStringBuilder.append(", hasAuthentication");
    }
    if (mAuthenticationIds != null)
    {
      localStringBuilder.append(", authenticationIds=");
      localStringBuilder.append(Arrays.toString(mAuthenticationIds));
    }
    localStringBuilder.append(", disableDuration=");
    localStringBuilder.append(mDisableDuration);
    if (mFlags != 0)
    {
      localStringBuilder.append(", flags=");
      localStringBuilder.append(mFlags);
    }
    if (mFieldClassificationIds != null) {
      localStringBuilder.append(Arrays.toString(mFieldClassificationIds));
    }
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mDatasets, paramInt);
    paramParcel.writeParcelable(mSaveInfo, paramInt);
    paramParcel.writeParcelable(mClientState, paramInt);
    paramParcel.writeParcelableArray(mAuthenticationIds, paramInt);
    paramParcel.writeParcelable(mAuthentication, paramInt);
    paramParcel.writeParcelable(mPresentation, paramInt);
    paramParcel.writeParcelable(mHeader, paramInt);
    paramParcel.writeParcelable(mFooter, paramInt);
    paramParcel.writeParcelableArray(mIgnoredIds, paramInt);
    paramParcel.writeLong(mDisableDuration);
    paramParcel.writeParcelableArray(mFieldClassificationIds, paramInt);
    paramParcel.writeInt(mFlags);
    paramParcel.writeInt(mRequestId);
  }
  
  public static final class Builder
  {
    private IntentSender mAuthentication;
    private AutofillId[] mAuthenticationIds;
    private Bundle mClientState;
    private ArrayList<Dataset> mDatasets;
    private boolean mDestroyed;
    private long mDisableDuration;
    private AutofillId[] mFieldClassificationIds;
    private int mFlags;
    private RemoteViews mFooter;
    private RemoteViews mHeader;
    private AutofillId[] mIgnoredIds;
    private RemoteViews mPresentation;
    private SaveInfo mSaveInfo;
    
    public Builder() {}
    
    private void throwIfAuthenticationCalled()
    {
      if (mAuthentication == null) {
        return;
      }
      throw new IllegalStateException("Already called #setAuthentication()");
    }
    
    private void throwIfDestroyed()
    {
      if (!mDestroyed) {
        return;
      }
      throw new IllegalStateException("Already called #build()");
    }
    
    private void throwIfDisableAutofillCalled()
    {
      if (mDisableDuration <= 0L) {
        return;
      }
      throw new IllegalStateException("Already called #disableAutofill()");
    }
    
    public Builder addDataset(Dataset paramDataset)
    {
      throwIfDestroyed();
      throwIfDisableAutofillCalled();
      if (paramDataset == null) {
        return this;
      }
      if (mDatasets == null) {
        mDatasets = new ArrayList();
      }
      if (!mDatasets.add(paramDataset)) {
        return this;
      }
      return this;
    }
    
    public FillResponse build()
    {
      throwIfDestroyed();
      if ((mAuthentication == null) && (mDatasets == null) && (mSaveInfo == null) && (mDisableDuration == 0L) && (mFieldClassificationIds == null) && (mClientState == null)) {
        throw new IllegalStateException("need to provide: at least one DataSet, or a SaveInfo, or an authentication with a presentation, or a FieldsDetection, or a client state, or disable autofill");
      }
      if ((mDatasets == null) && ((mHeader != null) || (mFooter != null))) {
        throw new IllegalStateException("must add at least 1 dataset when using header or footer");
      }
      mDestroyed = true;
      return new FillResponse(this, null);
    }
    
    public Builder disableAutofill(long paramLong)
    {
      throwIfDestroyed();
      if (paramLong > 0L)
      {
        if ((mAuthentication == null) && (mDatasets == null) && (mSaveInfo == null) && (mFieldClassificationIds == null) && (mClientState == null))
        {
          mDisableDuration = paramLong;
          return this;
        }
        throw new IllegalStateException("disableAutofill() must be the only method called");
      }
      throw new IllegalArgumentException("duration must be greater than 0");
    }
    
    public Builder setAuthentication(AutofillId[] paramArrayOfAutofillId, IntentSender paramIntentSender, RemoteViews paramRemoteViews)
    {
      throwIfDestroyed();
      throwIfDisableAutofillCalled();
      if ((mHeader == null) && (mFooter == null))
      {
        int i = 0;
        int j;
        if (paramIntentSender == null) {
          j = 1;
        } else {
          j = 0;
        }
        if (paramRemoteViews == null) {
          i = 1;
        }
        if ((i ^ j) == 0)
        {
          mAuthentication = paramIntentSender;
          mPresentation = paramRemoteViews;
          mAuthenticationIds = AutofillServiceHelper.assertValid(paramArrayOfAutofillId);
          return this;
        }
        throw new IllegalArgumentException("authentication and presentation must be both non-null or null");
      }
      throw new IllegalStateException("Already called #setHeader() or #setFooter()");
    }
    
    public Builder setClientState(Bundle paramBundle)
    {
      throwIfDestroyed();
      throwIfDisableAutofillCalled();
      mClientState = paramBundle;
      return this;
    }
    
    public Builder setFieldClassificationIds(AutofillId... paramVarArgs)
    {
      throwIfDestroyed();
      throwIfDisableAutofillCalled();
      Preconditions.checkArrayElementsNotNull(paramVarArgs, "ids");
      Preconditions.checkArgumentInRange(paramVarArgs.length, 1, UserData.getMaxFieldClassificationIdsSize(), "ids length");
      mFieldClassificationIds = paramVarArgs;
      mFlags |= 0x1;
      return this;
    }
    
    public Builder setFlags(int paramInt)
    {
      throwIfDestroyed();
      mFlags = Preconditions.checkFlagsArgument(paramInt, 3);
      return this;
    }
    
    public Builder setFooter(RemoteViews paramRemoteViews)
    {
      throwIfDestroyed();
      throwIfAuthenticationCalled();
      mFooter = ((RemoteViews)Preconditions.checkNotNull(paramRemoteViews));
      return this;
    }
    
    public Builder setHeader(RemoteViews paramRemoteViews)
    {
      throwIfDestroyed();
      throwIfAuthenticationCalled();
      mHeader = ((RemoteViews)Preconditions.checkNotNull(paramRemoteViews));
      return this;
    }
    
    public Builder setIgnoredIds(AutofillId... paramVarArgs)
    {
      throwIfDestroyed();
      mIgnoredIds = paramVarArgs;
      return this;
    }
    
    public Builder setSaveInfo(SaveInfo paramSaveInfo)
    {
      throwIfDestroyed();
      throwIfDisableAutofillCalled();
      mSaveInfo = paramSaveInfo;
      return this;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  static @interface FillResponseFlags {}
}
