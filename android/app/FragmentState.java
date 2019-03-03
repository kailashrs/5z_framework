package android.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

final class FragmentState
  implements Parcelable
{
  public static final Parcelable.Creator<FragmentState> CREATOR = new Parcelable.Creator()
  {
    public FragmentState createFromParcel(Parcel paramAnonymousParcel)
    {
      return new FragmentState(paramAnonymousParcel);
    }
    
    public FragmentState[] newArray(int paramAnonymousInt)
    {
      return new FragmentState[paramAnonymousInt];
    }
  };
  final Bundle mArguments;
  final String mClassName;
  final int mContainerId;
  final boolean mDetached;
  final int mFragmentId;
  final boolean mFromLayout;
  final boolean mHidden;
  final int mIndex;
  Fragment mInstance;
  final boolean mRetainInstance;
  Bundle mSavedFragmentState;
  final String mTag;
  
  FragmentState(Fragment paramFragment)
  {
    mClassName = paramFragment.getClass().getName();
    mIndex = mIndex;
    mFromLayout = mFromLayout;
    mFragmentId = mFragmentId;
    mContainerId = mContainerId;
    mTag = mTag;
    mRetainInstance = mRetainInstance;
    mDetached = mDetached;
    mArguments = mArguments;
    mHidden = mHidden;
  }
  
  FragmentState(Parcel paramParcel)
  {
    mClassName = paramParcel.readString();
    mIndex = paramParcel.readInt();
    int i = paramParcel.readInt();
    boolean bool1 = false;
    if (i != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mFromLayout = bool2;
    mFragmentId = paramParcel.readInt();
    mContainerId = paramParcel.readInt();
    mTag = paramParcel.readString();
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mRetainInstance = bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mDetached = bool2;
    mArguments = paramParcel.readBundle();
    boolean bool2 = bool1;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    }
    mHidden = bool2;
    mSavedFragmentState = paramParcel.readBundle();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Fragment instantiate(FragmentHostCallback paramFragmentHostCallback, FragmentContainer paramFragmentContainer, Fragment paramFragment, FragmentManagerNonConfig paramFragmentManagerNonConfig)
  {
    if (mInstance == null)
    {
      Context localContext = paramFragmentHostCallback.getContext();
      if (mArguments != null) {
        mArguments.setClassLoader(localContext.getClassLoader());
      }
      if (paramFragmentContainer != null) {
        mInstance = paramFragmentContainer.instantiate(localContext, mClassName, mArguments);
      } else {
        mInstance = Fragment.instantiate(localContext, mClassName, mArguments);
      }
      if (mSavedFragmentState != null)
      {
        mSavedFragmentState.setClassLoader(localContext.getClassLoader());
        mInstance.mSavedFragmentState = mSavedFragmentState;
      }
      mInstance.setIndex(mIndex, paramFragment);
      mInstance.mFromLayout = mFromLayout;
      mInstance.mRestored = true;
      mInstance.mFragmentId = mFragmentId;
      mInstance.mContainerId = mContainerId;
      mInstance.mTag = mTag;
      mInstance.mRetainInstance = mRetainInstance;
      mInstance.mDetached = mDetached;
      mInstance.mHidden = mHidden;
      mInstance.mFragmentManager = mFragmentManager;
      if (FragmentManagerImpl.DEBUG)
      {
        paramFragmentHostCallback = new StringBuilder();
        paramFragmentHostCallback.append("Instantiated fragment ");
        paramFragmentHostCallback.append(mInstance);
        Log.v("FragmentManager", paramFragmentHostCallback.toString());
      }
    }
    mInstance.mChildNonConfig = paramFragmentManagerNonConfig;
    return mInstance;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mClassName);
    paramParcel.writeInt(mIndex);
    paramParcel.writeInt(mFromLayout);
    paramParcel.writeInt(mFragmentId);
    paramParcel.writeInt(mContainerId);
    paramParcel.writeString(mTag);
    paramParcel.writeInt(mRetainInstance);
    paramParcel.writeInt(mDetached);
    paramParcel.writeBundle(mArguments);
    paramParcel.writeInt(mHidden);
    paramParcel.writeBundle(mSavedFragmentState);
  }
}
