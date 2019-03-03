package android.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import java.util.ArrayList;

final class BackStackState
  implements Parcelable
{
  public static final Parcelable.Creator<BackStackState> CREATOR = new Parcelable.Creator()
  {
    public BackStackState createFromParcel(Parcel paramAnonymousParcel)
    {
      return new BackStackState(paramAnonymousParcel);
    }
    
    public BackStackState[] newArray(int paramAnonymousInt)
    {
      return new BackStackState[paramAnonymousInt];
    }
  };
  final int mBreadCrumbShortTitleRes;
  final CharSequence mBreadCrumbShortTitleText;
  final int mBreadCrumbTitleRes;
  final CharSequence mBreadCrumbTitleText;
  final int mIndex;
  final String mName;
  final int[] mOps;
  final boolean mReorderingAllowed;
  final ArrayList<String> mSharedElementSourceNames;
  final ArrayList<String> mSharedElementTargetNames;
  final int mTransition;
  final int mTransitionStyle;
  
  public BackStackState(FragmentManagerImpl paramFragmentManagerImpl, BackStackRecord paramBackStackRecord)
  {
    int i = mOps.size();
    mOps = new int[i * 6];
    if (mAddToBackStack)
    {
      int j = 0;
      int k = 0;
      while (k < i)
      {
        paramFragmentManagerImpl = (BackStackRecord.Op)mOps.get(k);
        int[] arrayOfInt = mOps;
        int m = j + 1;
        arrayOfInt[j] = cmd;
        arrayOfInt = mOps;
        int n = m + 1;
        if (fragment != null) {
          j = fragment.mIndex;
        } else {
          j = -1;
        }
        arrayOfInt[m] = j;
        arrayOfInt = mOps;
        j = n + 1;
        arrayOfInt[n] = enterAnim;
        arrayOfInt = mOps;
        m = j + 1;
        arrayOfInt[j] = exitAnim;
        arrayOfInt = mOps;
        j = m + 1;
        arrayOfInt[m] = popEnterAnim;
        mOps[j] = popExitAnim;
        k++;
        j++;
      }
      mTransition = mTransition;
      mTransitionStyle = mTransitionStyle;
      mName = mName;
      mIndex = mIndex;
      mBreadCrumbTitleRes = mBreadCrumbTitleRes;
      mBreadCrumbTitleText = mBreadCrumbTitleText;
      mBreadCrumbShortTitleRes = mBreadCrumbShortTitleRes;
      mBreadCrumbShortTitleText = mBreadCrumbShortTitleText;
      mSharedElementSourceNames = mSharedElementSourceNames;
      mSharedElementTargetNames = mSharedElementTargetNames;
      mReorderingAllowed = mReorderingAllowed;
      return;
    }
    throw new IllegalStateException("Not on back stack");
  }
  
  public BackStackState(Parcel paramParcel)
  {
    mOps = paramParcel.createIntArray();
    mTransition = paramParcel.readInt();
    mTransitionStyle = paramParcel.readInt();
    mName = paramParcel.readString();
    mIndex = paramParcel.readInt();
    mBreadCrumbTitleRes = paramParcel.readInt();
    mBreadCrumbTitleText = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    mBreadCrumbShortTitleRes = paramParcel.readInt();
    mBreadCrumbShortTitleText = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    mSharedElementSourceNames = paramParcel.createStringArrayList();
    mSharedElementTargetNames = paramParcel.createStringArrayList();
    boolean bool;
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    mReorderingAllowed = bool;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public BackStackRecord instantiate(FragmentManagerImpl paramFragmentManagerImpl)
  {
    BackStackRecord localBackStackRecord = new BackStackRecord(paramFragmentManagerImpl);
    int i = 0;
    int j = 0;
    while (i < mOps.length)
    {
      BackStackRecord.Op localOp = new BackStackRecord.Op();
      Object localObject = mOps;
      int k = i + 1;
      cmd = localObject[i];
      if (FragmentManagerImpl.DEBUG)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Instantiate ");
        ((StringBuilder)localObject).append(localBackStackRecord);
        ((StringBuilder)localObject).append(" op #");
        ((StringBuilder)localObject).append(j);
        ((StringBuilder)localObject).append(" base fragment #");
        ((StringBuilder)localObject).append(mOps[k]);
        Log.v("FragmentManager", ((StringBuilder)localObject).toString());
      }
      localObject = mOps;
      i = k + 1;
      k = localObject[k];
      if (k >= 0) {
        fragment = ((Fragment)mActive.get(k));
      } else {
        fragment = null;
      }
      localObject = mOps;
      k = i + 1;
      enterAnim = localObject[i];
      localObject = mOps;
      i = k + 1;
      exitAnim = localObject[k];
      localObject = mOps;
      k = i + 1;
      popEnterAnim = localObject[i];
      popExitAnim = mOps[k];
      mEnterAnim = enterAnim;
      mExitAnim = exitAnim;
      mPopEnterAnim = popEnterAnim;
      mPopExitAnim = popExitAnim;
      localBackStackRecord.addOp(localOp);
      j++;
      i = k + 1;
    }
    mTransition = mTransition;
    mTransitionStyle = mTransitionStyle;
    mName = mName;
    mIndex = mIndex;
    mAddToBackStack = true;
    mBreadCrumbTitleRes = mBreadCrumbTitleRes;
    mBreadCrumbTitleText = mBreadCrumbTitleText;
    mBreadCrumbShortTitleRes = mBreadCrumbShortTitleRes;
    mBreadCrumbShortTitleText = mBreadCrumbShortTitleText;
    mSharedElementSourceNames = mSharedElementSourceNames;
    mSharedElementTargetNames = mSharedElementTargetNames;
    mReorderingAllowed = mReorderingAllowed;
    localBackStackRecord.bumpBackStackNesting(1);
    return localBackStackRecord;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeIntArray(mOps);
    paramParcel.writeInt(mTransition);
    paramParcel.writeInt(mTransitionStyle);
    paramParcel.writeString(mName);
    paramParcel.writeInt(mIndex);
    paramParcel.writeInt(mBreadCrumbTitleRes);
    TextUtils.writeToParcel(mBreadCrumbTitleText, paramParcel, 0);
    paramParcel.writeInt(mBreadCrumbShortTitleRes);
    TextUtils.writeToParcel(mBreadCrumbShortTitleText, paramParcel, 0);
    paramParcel.writeStringList(mSharedElementSourceNames);
    paramParcel.writeStringList(mSharedElementTargetNames);
    paramParcel.writeInt(mReorderingAllowed);
  }
}
