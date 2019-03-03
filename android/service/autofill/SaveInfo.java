package android.service.autofill;

import android.content.IntentSender;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.DebugUtils;
import android.view.autofill.AutofillId;
import android.view.autofill.Helper;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

public final class SaveInfo
  implements Parcelable
{
  public static final Parcelable.Creator<SaveInfo> CREATOR = new Parcelable.Creator()
  {
    public SaveInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      Object localObject1 = (AutofillId[])paramAnonymousParcel.readParcelableArray(null, AutofillId.class);
      if (localObject1 != null) {
        localObject1 = new SaveInfo.Builder(i, (AutofillId[])localObject1);
      } else {
        localObject1 = new SaveInfo.Builder(i);
      }
      Object localObject2 = (AutofillId[])paramAnonymousParcel.readParcelableArray(null, AutofillId.class);
      if (localObject2 != null) {
        ((SaveInfo.Builder)localObject1).setOptionalIds((AutofillId[])localObject2);
      }
      ((SaveInfo.Builder)localObject1).setNegativeAction(paramAnonymousParcel.readInt(), (IntentSender)paramAnonymousParcel.readParcelable(null));
      ((SaveInfo.Builder)localObject1).setDescription(paramAnonymousParcel.readCharSequence());
      localObject2 = (CustomDescription)paramAnonymousParcel.readParcelable(null);
      if (localObject2 != null) {
        ((SaveInfo.Builder)localObject1).setCustomDescription((CustomDescription)localObject2);
      }
      localObject2 = (InternalValidator)paramAnonymousParcel.readParcelable(null);
      if (localObject2 != null) {
        ((SaveInfo.Builder)localObject1).setValidator((Validator)localObject2);
      }
      localObject2 = (InternalSanitizer[])paramAnonymousParcel.readParcelableArray(null, InternalSanitizer.class);
      if (localObject2 != null)
      {
        int j = localObject2.length;
        for (i = 0; i < j; i++)
        {
          AutofillId[] arrayOfAutofillId = (AutofillId[])paramAnonymousParcel.readParcelableArray(null, AutofillId.class);
          ((SaveInfo.Builder)localObject1).addSanitizer(localObject2[i], arrayOfAutofillId);
        }
      }
      localObject2 = (AutofillId)paramAnonymousParcel.readParcelable(null);
      if (localObject2 != null) {
        ((SaveInfo.Builder)localObject1).setTriggerId((AutofillId)localObject2);
      }
      ((SaveInfo.Builder)localObject1).setFlags(paramAnonymousParcel.readInt());
      return ((SaveInfo.Builder)localObject1).build();
    }
    
    public SaveInfo[] newArray(int paramAnonymousInt)
    {
      return new SaveInfo[paramAnonymousInt];
    }
  };
  public static final int FLAG_DONT_SAVE_ON_FINISH = 2;
  public static final int FLAG_SAVE_ON_ALL_VIEWS_INVISIBLE = 1;
  public static final int NEGATIVE_BUTTON_STYLE_CANCEL = 0;
  public static final int NEGATIVE_BUTTON_STYLE_REJECT = 1;
  public static final int SAVE_DATA_TYPE_ADDRESS = 2;
  public static final int SAVE_DATA_TYPE_CREDIT_CARD = 4;
  public static final int SAVE_DATA_TYPE_EMAIL_ADDRESS = 16;
  public static final int SAVE_DATA_TYPE_GENERIC = 0;
  public static final int SAVE_DATA_TYPE_PASSWORD = 1;
  public static final int SAVE_DATA_TYPE_USERNAME = 8;
  private final CustomDescription mCustomDescription;
  private final CharSequence mDescription;
  private final int mFlags;
  private final IntentSender mNegativeActionListener;
  private final int mNegativeButtonStyle;
  private final AutofillId[] mOptionalIds;
  private final AutofillId[] mRequiredIds;
  private final InternalSanitizer[] mSanitizerKeys;
  private final AutofillId[][] mSanitizerValues;
  private final AutofillId mTriggerId;
  private final int mType;
  private final InternalValidator mValidator;
  
  private SaveInfo(Builder paramBuilder)
  {
    mType = mType;
    mNegativeButtonStyle = mNegativeButtonStyle;
    mNegativeActionListener = mNegativeActionListener;
    mRequiredIds = mRequiredIds;
    mOptionalIds = mOptionalIds;
    mDescription = mDescription;
    mFlags = mFlags;
    mCustomDescription = mCustomDescription;
    mValidator = mValidator;
    if (mSanitizers == null)
    {
      mSanitizerKeys = null;
      mSanitizerValues = null;
    }
    else
    {
      int i = mSanitizers.size();
      mSanitizerKeys = new InternalSanitizer[i];
      mSanitizerValues = new AutofillId[i][];
      for (int j = 0; j < i; j++)
      {
        mSanitizerKeys[j] = ((InternalSanitizer)mSanitizers.keyAt(j));
        mSanitizerValues[j] = ((AutofillId[])mSanitizers.valueAt(j));
      }
    }
    mTriggerId = mTriggerId;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public CustomDescription getCustomDescription()
  {
    return mCustomDescription;
  }
  
  public CharSequence getDescription()
  {
    return mDescription;
  }
  
  public int getFlags()
  {
    return mFlags;
  }
  
  public IntentSender getNegativeActionListener()
  {
    return mNegativeActionListener;
  }
  
  public int getNegativeActionStyle()
  {
    return mNegativeButtonStyle;
  }
  
  public AutofillId[] getOptionalIds()
  {
    return mOptionalIds;
  }
  
  public AutofillId[] getRequiredIds()
  {
    return mRequiredIds;
  }
  
  public InternalSanitizer[] getSanitizerKeys()
  {
    return mSanitizerKeys;
  }
  
  public AutofillId[][] getSanitizerValues()
  {
    return mSanitizerValues;
  }
  
  public AutofillId getTriggerId()
  {
    return mTriggerId;
  }
  
  public int getType()
  {
    return mType;
  }
  
  public InternalValidator getValidator()
  {
    return mValidator;
  }
  
  public String toString()
  {
    if (!Helper.sDebug) {
      return super.toString();
    }
    StringBuilder localStringBuilder = new StringBuilder("SaveInfo: [type=");
    localStringBuilder.append(DebugUtils.flagsToString(SaveInfo.class, "SAVE_DATA_TYPE_", mType));
    localStringBuilder.append(", requiredIds=");
    localStringBuilder.append(Arrays.toString(mRequiredIds));
    localStringBuilder.append(", style=");
    localStringBuilder = localStringBuilder.append(DebugUtils.flagsToString(SaveInfo.class, "NEGATIVE_BUTTON_STYLE_", mNegativeButtonStyle));
    if (mOptionalIds != null)
    {
      localStringBuilder.append(", optionalIds=");
      localStringBuilder.append(Arrays.toString(mOptionalIds));
    }
    if (mDescription != null)
    {
      localStringBuilder.append(", description=");
      localStringBuilder.append(mDescription);
    }
    if (mFlags != 0)
    {
      localStringBuilder.append(", flags=");
      localStringBuilder.append(mFlags);
    }
    if (mCustomDescription != null)
    {
      localStringBuilder.append(", customDescription=");
      localStringBuilder.append(mCustomDescription);
    }
    if (mValidator != null)
    {
      localStringBuilder.append(", validator=");
      localStringBuilder.append(mValidator);
    }
    if (mSanitizerKeys != null)
    {
      localStringBuilder.append(", sanitizerKeys=");
      localStringBuilder.append(mSanitizerKeys.length);
    }
    if (mSanitizerValues != null)
    {
      localStringBuilder.append(", sanitizerValues=");
      localStringBuilder.append(mSanitizerValues.length);
    }
    if (mTriggerId != null)
    {
      localStringBuilder.append(", triggerId=");
      localStringBuilder.append(mTriggerId);
    }
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mType);
    paramParcel.writeParcelableArray(mRequiredIds, paramInt);
    paramParcel.writeParcelableArray(mOptionalIds, paramInt);
    paramParcel.writeInt(mNegativeButtonStyle);
    paramParcel.writeParcelable(mNegativeActionListener, paramInt);
    paramParcel.writeCharSequence(mDescription);
    paramParcel.writeParcelable(mCustomDescription, paramInt);
    paramParcel.writeParcelable(mValidator, paramInt);
    paramParcel.writeParcelableArray(mSanitizerKeys, paramInt);
    if (mSanitizerKeys != null) {
      for (int i = 0; i < mSanitizerValues.length; i++) {
        paramParcel.writeParcelableArray(mSanitizerValues[i], paramInt);
      }
    }
    paramParcel.writeParcelable(mTriggerId, paramInt);
    paramParcel.writeInt(mFlags);
  }
  
  public static final class Builder
  {
    private CustomDescription mCustomDescription;
    private CharSequence mDescription;
    private boolean mDestroyed;
    private int mFlags;
    private IntentSender mNegativeActionListener;
    private int mNegativeButtonStyle = 0;
    private AutofillId[] mOptionalIds;
    private final AutofillId[] mRequiredIds;
    private ArraySet<AutofillId> mSanitizerIds;
    private ArrayMap<InternalSanitizer, AutofillId[]> mSanitizers;
    private AutofillId mTriggerId;
    private final int mType;
    private InternalValidator mValidator;
    
    public Builder(int paramInt)
    {
      mType = paramInt;
      mRequiredIds = null;
    }
    
    public Builder(int paramInt, AutofillId[] paramArrayOfAutofillId)
    {
      mType = paramInt;
      mRequiredIds = AutofillServiceHelper.assertValid(paramArrayOfAutofillId);
    }
    
    private void throwIfDestroyed()
    {
      if (!mDestroyed) {
        return;
      }
      throw new IllegalStateException("Already called #build()");
    }
    
    public Builder addSanitizer(Sanitizer paramSanitizer, AutofillId... paramVarArgs)
    {
      throwIfDestroyed();
      Preconditions.checkArgument(ArrayUtils.isEmpty(paramVarArgs) ^ true, "ids cannot be empty or null");
      boolean bool = paramSanitizer instanceof InternalSanitizer;
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("not provided by Android System: ");
      ((StringBuilder)localObject).append(paramSanitizer);
      Preconditions.checkArgument(bool, ((StringBuilder)localObject).toString());
      if (mSanitizers == null)
      {
        mSanitizers = new ArrayMap();
        mSanitizerIds = new ArraySet(paramVarArgs.length);
      }
      int i = paramVarArgs.length;
      for (int j = 0; j < i; j++)
      {
        localObject = paramVarArgs[j];
        Preconditions.checkArgument(mSanitizerIds.contains(localObject) ^ true, "already added %s", new Object[] { localObject });
        mSanitizerIds.add(localObject);
      }
      mSanitizers.put((InternalSanitizer)paramSanitizer, paramVarArgs);
      return this;
    }
    
    public SaveInfo build()
    {
      throwIfDestroyed();
      boolean bool;
      if ((ArrayUtils.isEmpty(mRequiredIds)) && (ArrayUtils.isEmpty(mOptionalIds))) {
        bool = false;
      } else {
        bool = true;
      }
      Preconditions.checkState(bool, "must have at least one required or optional id");
      mDestroyed = true;
      return new SaveInfo(this, null);
    }
    
    public Builder setCustomDescription(CustomDescription paramCustomDescription)
    {
      throwIfDestroyed();
      boolean bool;
      if (mDescription == null) {
        bool = true;
      } else {
        bool = false;
      }
      Preconditions.checkState(bool, "Can call setDescription() or setCustomDescription(), but not both");
      mCustomDescription = paramCustomDescription;
      return this;
    }
    
    public Builder setDescription(CharSequence paramCharSequence)
    {
      throwIfDestroyed();
      boolean bool;
      if (mCustomDescription == null) {
        bool = true;
      } else {
        bool = false;
      }
      Preconditions.checkState(bool, "Can call setDescription() or setCustomDescription(), but not both");
      mDescription = paramCharSequence;
      return this;
    }
    
    public Builder setFlags(int paramInt)
    {
      throwIfDestroyed();
      mFlags = Preconditions.checkFlagsArgument(paramInt, 3);
      return this;
    }
    
    public Builder setNegativeAction(int paramInt, IntentSender paramIntentSender)
    {
      throwIfDestroyed();
      if ((paramInt != 0) && (paramInt != 1))
      {
        paramIntentSender = new StringBuilder();
        paramIntentSender.append("Invalid style: ");
        paramIntentSender.append(paramInt);
        throw new IllegalArgumentException(paramIntentSender.toString());
      }
      mNegativeButtonStyle = paramInt;
      mNegativeActionListener = paramIntentSender;
      return this;
    }
    
    public Builder setOptionalIds(AutofillId[] paramArrayOfAutofillId)
    {
      throwIfDestroyed();
      mOptionalIds = AutofillServiceHelper.assertValid(paramArrayOfAutofillId);
      return this;
    }
    
    public Builder setTriggerId(AutofillId paramAutofillId)
    {
      throwIfDestroyed();
      mTriggerId = ((AutofillId)Preconditions.checkNotNull(paramAutofillId));
      return this;
    }
    
    public Builder setValidator(Validator paramValidator)
    {
      throwIfDestroyed();
      boolean bool = paramValidator instanceof InternalValidator;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("not provided by Android System: ");
      localStringBuilder.append(paramValidator);
      Preconditions.checkArgument(bool, localStringBuilder.toString());
      mValidator = ((InternalValidator)paramValidator);
      return this;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  static @interface NegativeButtonStyle {}
  
  @Retention(RetentionPolicy.SOURCE)
  static @interface SaveDataType {}
  
  @Retention(RetentionPolicy.SOURCE)
  static @interface SaveInfoFlags {}
}
