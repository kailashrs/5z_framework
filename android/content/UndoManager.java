package android.content;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.ArrayMap;
import java.util.ArrayList;

public class UndoManager
{
  public static final int MERGE_MODE_ANY = 2;
  public static final int MERGE_MODE_NONE = 0;
  public static final int MERGE_MODE_UNIQUE = 1;
  private int mCommitId = 1;
  private int mHistorySize = 20;
  private boolean mInUndo;
  private boolean mMerged;
  private int mNextSavedIdx;
  private final ArrayMap<String, UndoOwner> mOwners = new ArrayMap(1);
  private final ArrayList<UndoState> mRedos = new ArrayList();
  private UndoOwner[] mStateOwners;
  private int mStateSeq;
  private final ArrayList<UndoState> mUndos = new ArrayList();
  private int mUpdateCount;
  private UndoState mWorking;
  
  public UndoManager() {}
  
  private void createWorkingState()
  {
    int i = mCommitId;
    mCommitId = (i + 1);
    mWorking = new UndoState(this, i);
    if (mCommitId < 0) {
      mCommitId = 1;
    }
  }
  
  private void pushWorkingState()
  {
    int i = mUndos.size() + 1;
    if (mWorking.hasData())
    {
      mUndos.add(mWorking);
      forgetRedos(null, -1);
      mWorking.commit();
      if (i >= 2) {
        ((UndoState)mUndos.get(i - 2)).makeExecuted();
      }
    }
    else
    {
      mWorking.destroy();
    }
    mWorking = null;
    if ((mHistorySize >= 0) && (i > mHistorySize)) {
      forgetUndos(null, i - mHistorySize);
    }
  }
  
  public void addOperation(UndoOperation<?> paramUndoOperation, int paramInt)
  {
    if (mWorking != null)
    {
      if (getOwnermManager == this)
      {
        if ((paramInt != 0) && (!mMerged) && (!mWorking.hasData()))
        {
          UndoState localUndoState = getTopUndo(null);
          if ((localUndoState != null) && ((paramInt == 2) || (!localUndoState.hasMultipleOwners())) && (localUndoState.canMerge()) && (localUndoState.hasOperation(paramUndoOperation.getOwner())))
          {
            mWorking.destroy();
            mWorking = localUndoState;
            mUndos.remove(localUndoState);
            mMerged = true;
          }
        }
        mWorking.addOperation(paramUndoOperation);
        return;
      }
      throw new IllegalArgumentException("Given operation's owner is not in this undo manager.");
    }
    throw new IllegalStateException("Must be called during an update");
  }
  
  public void beginUpdate(CharSequence paramCharSequence)
  {
    if (!mInUndo)
    {
      if (mUpdateCount <= 0)
      {
        createWorkingState();
        mMerged = false;
        mUpdateCount = 0;
      }
      mWorking.updateLabel(paramCharSequence);
      mUpdateCount += 1;
      return;
    }
    throw new IllegalStateException("Can't being update while performing undo/redo");
  }
  
  public int commitState(UndoOwner paramUndoOwner)
  {
    if ((mWorking != null) && (mWorking.hasData()))
    {
      if ((paramUndoOwner == null) || (mWorking.hasOperation(paramUndoOwner)))
      {
        mWorking.setCanMerge(false);
        int i = mWorking.getCommitId();
        pushWorkingState();
        createWorkingState();
        mMerged = true;
        return i;
      }
    }
    else
    {
      UndoState localUndoState = getTopUndo(null);
      if ((localUndoState != null) && ((paramUndoOwner == null) || (localUndoState.hasOperation(paramUndoOwner))))
      {
        localUndoState.setCanMerge(false);
        return localUndoState.getCommitId();
      }
    }
    return -1;
  }
  
  public int countRedos(UndoOwner[] paramArrayOfUndoOwner)
  {
    if (paramArrayOfUndoOwner == null) {
      return mRedos.size();
    }
    int i = 0;
    for (int j = 0;; j++)
    {
      j = findNextState(mRedos, paramArrayOfUndoOwner, j);
      if (j < 0) {
        break;
      }
      i++;
    }
    return i;
  }
  
  public int countUndos(UndoOwner[] paramArrayOfUndoOwner)
  {
    if (paramArrayOfUndoOwner == null) {
      return mUndos.size();
    }
    int i = 0;
    for (int j = 0;; j++)
    {
      j = findNextState(mUndos, paramArrayOfUndoOwner, j);
      if (j < 0) {
        break;
      }
      i++;
    }
    return i;
  }
  
  public void endUpdate()
  {
    if (mWorking != null)
    {
      mUpdateCount -= 1;
      if (mUpdateCount == 0) {
        pushWorkingState();
      }
      return;
    }
    throw new IllegalStateException("Must be called during an update");
  }
  
  int findNextState(ArrayList<UndoState> paramArrayList, UndoOwner[] paramArrayOfUndoOwner, int paramInt)
  {
    int i = paramArrayList.size();
    int j = paramInt;
    if (paramInt < 0) {
      j = 0;
    }
    if (j >= i) {
      return -1;
    }
    paramInt = j;
    if (paramArrayOfUndoOwner == null) {
      return j;
    }
    while (paramInt < i)
    {
      if (matchOwners((UndoState)paramArrayList.get(paramInt), paramArrayOfUndoOwner)) {
        return paramInt;
      }
      paramInt++;
    }
    return -1;
  }
  
  int findPrevState(ArrayList<UndoState> paramArrayList, UndoOwner[] paramArrayOfUndoOwner, int paramInt)
  {
    int i = paramArrayList.size();
    int j = paramInt;
    if (paramInt == -1) {
      j = i - 1;
    }
    if (j >= i) {
      return -1;
    }
    paramInt = j;
    if (paramArrayOfUndoOwner == null) {
      return j;
    }
    while (paramInt >= 0)
    {
      if (matchOwners((UndoState)paramArrayList.get(paramInt), paramArrayOfUndoOwner)) {
        return paramInt;
      }
      paramInt--;
    }
    return -1;
  }
  
  public int forgetRedos(UndoOwner[] paramArrayOfUndoOwner, int paramInt)
  {
    int i = paramInt;
    if (paramInt < 0) {
      i = mRedos.size();
    }
    int j = 0;
    paramInt = 0;
    while ((paramInt < mRedos.size()) && (j < i))
    {
      UndoState localUndoState = (UndoState)mRedos.get(paramInt);
      if ((i > 0) && (matchOwners(localUndoState, paramArrayOfUndoOwner)))
      {
        localUndoState.destroy();
        mRedos.remove(paramInt);
        j++;
      }
      else
      {
        paramInt++;
      }
    }
    return j;
  }
  
  public int forgetUndos(UndoOwner[] paramArrayOfUndoOwner, int paramInt)
  {
    int i = paramInt;
    if (paramInt < 0) {
      i = mUndos.size();
    }
    int j = 0;
    paramInt = 0;
    while ((paramInt < mUndos.size()) && (j < i))
    {
      UndoState localUndoState = (UndoState)mUndos.get(paramInt);
      if ((i > 0) && (matchOwners(localUndoState, paramArrayOfUndoOwner)))
      {
        localUndoState.destroy();
        mUndos.remove(paramInt);
        j++;
      }
      else
      {
        paramInt++;
      }
    }
    return j;
  }
  
  public int getHistorySize()
  {
    return mHistorySize;
  }
  
  public UndoOperation<?> getLastOperation(int paramInt)
  {
    return getLastOperation(null, null, paramInt);
  }
  
  public UndoOperation<?> getLastOperation(UndoOwner paramUndoOwner, int paramInt)
  {
    return getLastOperation(null, paramUndoOwner, paramInt);
  }
  
  public <T extends UndoOperation> T getLastOperation(Class<T> paramClass, UndoOwner paramUndoOwner, int paramInt)
  {
    if (mWorking != null)
    {
      if ((paramInt != 0) && (!mMerged) && (!mWorking.hasData()))
      {
        UndoState localUndoState = getTopUndo(null);
        if ((localUndoState != null) && ((paramInt == 2) || (!localUndoState.hasMultipleOwners())) && (localUndoState.canMerge()))
        {
          UndoOperation localUndoOperation = localUndoState.getLastOperation(paramClass, paramUndoOwner);
          if ((localUndoOperation != null) && (localUndoOperation.allowMerge()))
          {
            mWorking.destroy();
            mWorking = localUndoState;
            mUndos.remove(localUndoState);
            mMerged = true;
            return localUndoOperation;
          }
        }
      }
      return mWorking.getLastOperation(paramClass, paramUndoOwner);
    }
    throw new IllegalStateException("Must be called during an update");
  }
  
  public UndoOwner getOwner(String paramString, Object paramObject)
  {
    if (paramString != null)
    {
      if (paramObject != null)
      {
        UndoOwner localUndoOwner = (UndoOwner)mOwners.get(paramString);
        if (localUndoOwner != null)
        {
          if (mData != paramObject) {
            if (mData == null)
            {
              mData = paramObject;
            }
            else
            {
              paramString = new StringBuilder();
              paramString.append("Owner ");
              paramString.append(localUndoOwner);
              paramString.append(" already exists with data ");
              paramString.append(mData);
              paramString.append(" but giving different data ");
              paramString.append(paramObject);
              throw new IllegalStateException(paramString.toString());
            }
          }
          return localUndoOwner;
        }
        localUndoOwner = new UndoOwner(paramString, this);
        mData = paramObject;
        mOwners.put(paramString, localUndoOwner);
        return localUndoOwner;
      }
      throw new NullPointerException("data can't be null");
    }
    throw new NullPointerException("tag can't be null");
  }
  
  public CharSequence getRedoLabel(UndoOwner[] paramArrayOfUndoOwner)
  {
    paramArrayOfUndoOwner = getTopRedo(paramArrayOfUndoOwner);
    if (paramArrayOfUndoOwner != null) {
      paramArrayOfUndoOwner = paramArrayOfUndoOwner.getLabel();
    } else {
      paramArrayOfUndoOwner = null;
    }
    return paramArrayOfUndoOwner;
  }
  
  UndoState getTopRedo(UndoOwner[] paramArrayOfUndoOwner)
  {
    int i = mRedos.size();
    Object localObject = null;
    if (i <= 0) {
      return null;
    }
    i = findPrevState(mRedos, paramArrayOfUndoOwner, -1);
    paramArrayOfUndoOwner = localObject;
    if (i >= 0) {
      paramArrayOfUndoOwner = (UndoState)mRedos.get(i);
    }
    return paramArrayOfUndoOwner;
  }
  
  UndoState getTopUndo(UndoOwner[] paramArrayOfUndoOwner)
  {
    int i = mUndos.size();
    Object localObject = null;
    if (i <= 0) {
      return null;
    }
    i = findPrevState(mUndos, paramArrayOfUndoOwner, -1);
    paramArrayOfUndoOwner = localObject;
    if (i >= 0) {
      paramArrayOfUndoOwner = (UndoState)mUndos.get(i);
    }
    return paramArrayOfUndoOwner;
  }
  
  public CharSequence getUndoLabel(UndoOwner[] paramArrayOfUndoOwner)
  {
    paramArrayOfUndoOwner = getTopUndo(paramArrayOfUndoOwner);
    if (paramArrayOfUndoOwner != null) {
      paramArrayOfUndoOwner = paramArrayOfUndoOwner.getLabel();
    } else {
      paramArrayOfUndoOwner = null;
    }
    return paramArrayOfUndoOwner;
  }
  
  public int getUpdateNestingLevel()
  {
    return mUpdateCount;
  }
  
  public boolean hasOperation(UndoOwner paramUndoOwner)
  {
    if (mWorking != null) {
      return mWorking.hasOperation(paramUndoOwner);
    }
    throw new IllegalStateException("Must be called during an update");
  }
  
  public boolean isInUndo()
  {
    return mInUndo;
  }
  
  public boolean isInUpdate()
  {
    boolean bool;
    if (mUpdateCount > 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  boolean matchOwners(UndoState paramUndoState, UndoOwner[] paramArrayOfUndoOwner)
  {
    if (paramArrayOfUndoOwner == null) {
      return true;
    }
    for (int i = 0; i < paramArrayOfUndoOwner.length; i++) {
      if (paramUndoState.matchOwner(paramArrayOfUndoOwner[i])) {
        return true;
      }
    }
    return false;
  }
  
  public int redo(UndoOwner[] paramArrayOfUndoOwner, int paramInt)
  {
    if (mWorking == null)
    {
      int i = 0;
      int j = -1;
      mInUndo = true;
      while (paramInt > 0)
      {
        int k = findPrevState(mRedos, paramArrayOfUndoOwner, j);
        j = k;
        if (k < 0) {
          break;
        }
        UndoState localUndoState = (UndoState)mRedos.remove(j);
        localUndoState.redo();
        mUndos.add(localUndoState);
        paramInt--;
        i++;
      }
      mInUndo = false;
      return i;
    }
    throw new IllegalStateException("Can't be called during an update");
  }
  
  void removeOwner(UndoOwner paramUndoOwner) {}
  
  public void restoreInstanceState(Parcel paramParcel, ClassLoader paramClassLoader)
  {
    if (mUpdateCount <= 0)
    {
      forgetUndos(null, -1);
      forgetRedos(null, -1);
      mHistorySize = paramParcel.readInt();
      mStateOwners = new UndoOwner[paramParcel.readInt()];
      for (;;)
      {
        int i = paramParcel.readInt();
        if (i == 0) {
          break;
        }
        UndoState localUndoState = new UndoState(this, paramParcel, paramClassLoader);
        if (i == 1) {
          mUndos.add(0, localUndoState);
        } else {
          mRedos.add(0, localUndoState);
        }
      }
      return;
    }
    throw new IllegalStateException("Can't save state while updating");
  }
  
  UndoOwner restoreOwner(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    Object localObject1 = mStateOwners[i];
    Object localObject2 = localObject1;
    if (localObject1 == null)
    {
      localObject1 = paramParcel.readString();
      int j = paramParcel.readInt();
      localObject2 = new UndoOwner((String)localObject1, this);
      mOpCount = j;
      mStateOwners[i] = localObject2;
      mOwners.put(localObject1, localObject2);
    }
    return localObject2;
  }
  
  public void saveInstanceState(Parcel paramParcel)
  {
    if (mUpdateCount <= 0)
    {
      mStateSeq += 1;
      if (mStateSeq <= 0) {
        mStateSeq = 0;
      }
      mNextSavedIdx = 0;
      paramParcel.writeInt(mHistorySize);
      paramParcel.writeInt(mOwners.size());
      int i = mUndos.size();
      while (i > 0)
      {
        paramParcel.writeInt(1);
        i--;
        ((UndoState)mUndos.get(i)).writeToParcel(paramParcel);
      }
      i = mRedos.size();
      while (i > 0)
      {
        paramParcel.writeInt(2);
        i--;
        ((UndoState)mRedos.get(i)).writeToParcel(paramParcel);
      }
      paramParcel.writeInt(0);
      return;
    }
    throw new IllegalStateException("Can't save state while updating");
  }
  
  void saveOwner(UndoOwner paramUndoOwner, Parcel paramParcel)
  {
    if (mStateSeq == mStateSeq)
    {
      paramParcel.writeInt(mSavedIdx);
    }
    else
    {
      mStateSeq = mStateSeq;
      mSavedIdx = mNextSavedIdx;
      paramParcel.writeInt(mSavedIdx);
      paramParcel.writeString(mTag);
      paramParcel.writeInt(mOpCount);
      mNextSavedIdx += 1;
    }
  }
  
  public void setHistorySize(int paramInt)
  {
    mHistorySize = paramInt;
    if ((mHistorySize >= 0) && (countUndos(null) > mHistorySize)) {
      forgetUndos(null, countUndos(null) - mHistorySize);
    }
  }
  
  public void setUndoLabel(CharSequence paramCharSequence)
  {
    if (mWorking != null)
    {
      mWorking.setLabel(paramCharSequence);
      return;
    }
    throw new IllegalStateException("Must be called during an update");
  }
  
  public void suggestUndoLabel(CharSequence paramCharSequence)
  {
    if (mWorking != null)
    {
      mWorking.updateLabel(paramCharSequence);
      return;
    }
    throw new IllegalStateException("Must be called during an update");
  }
  
  public boolean uncommitState(int paramInt, UndoOwner paramUndoOwner)
  {
    if ((mWorking != null) && (mWorking.getCommitId() == paramInt))
    {
      if ((paramUndoOwner == null) || (mWorking.hasOperation(paramUndoOwner))) {
        return mWorking.setCanMerge(true);
      }
    }
    else
    {
      UndoState localUndoState = getTopUndo(null);
      if ((localUndoState != null) && ((paramUndoOwner == null) || (localUndoState.hasOperation(paramUndoOwner))) && (localUndoState.getCommitId() == paramInt)) {
        return localUndoState.setCanMerge(true);
      }
    }
    return false;
  }
  
  public int undo(UndoOwner[] paramArrayOfUndoOwner, int paramInt)
  {
    if (mWorking == null)
    {
      int i = 0;
      int j = -1;
      mInUndo = true;
      UndoState localUndoState = getTopUndo(null);
      int k = i;
      int m = j;
      int n = paramInt;
      if (localUndoState != null)
      {
        localUndoState.makeExecuted();
        n = paramInt;
        m = j;
      }
      for (k = i; n > 0; k++)
      {
        paramInt = findPrevState(mUndos, paramArrayOfUndoOwner, m);
        m = paramInt;
        if (paramInt < 0) {
          break;
        }
        localUndoState = (UndoState)mUndos.remove(m);
        localUndoState.undo();
        mRedos.add(localUndoState);
        n--;
      }
      mInUndo = false;
      return k;
    }
    throw new IllegalStateException("Can't be called during an update");
  }
  
  static final class UndoState
  {
    private boolean mCanMerge;
    private final int mCommitId;
    private boolean mExecuted;
    private CharSequence mLabel;
    private final UndoManager mManager;
    private final ArrayList<UndoOperation<?>> mOperations = new ArrayList();
    private ArrayList<UndoOperation<?>> mRecent;
    
    UndoState(UndoManager paramUndoManager, int paramInt)
    {
      mCanMerge = true;
      mManager = paramUndoManager;
      mCommitId = paramInt;
    }
    
    UndoState(UndoManager paramUndoManager, Parcel paramParcel, ClassLoader paramClassLoader)
    {
      boolean bool1 = true;
      mCanMerge = true;
      mManager = paramUndoManager;
      mCommitId = paramParcel.readInt();
      int i = paramParcel.readInt();
      int j = 0;
      boolean bool2;
      if (i != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      mCanMerge = bool2;
      if (paramParcel.readInt() != 0) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
      mExecuted = bool2;
      mLabel = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
      i = paramParcel.readInt();
      while (j < i)
      {
        paramUndoManager = mManager.restoreOwner(paramParcel);
        UndoOperation localUndoOperation = (UndoOperation)paramParcel.readParcelable(paramClassLoader);
        mOwner = paramUndoManager;
        mOperations.add(localUndoOperation);
        j++;
      }
    }
    
    void addOperation(UndoOperation<?> paramUndoOperation)
    {
      if (!mOperations.contains(paramUndoOperation))
      {
        mOperations.add(paramUndoOperation);
        if (mRecent == null)
        {
          mRecent = new ArrayList();
          mRecent.add(paramUndoOperation);
        }
        paramUndoOperation = mOwner;
        mOpCount += 1;
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Already holds ");
      localStringBuilder.append(paramUndoOperation);
      throw new IllegalStateException(localStringBuilder.toString());
    }
    
    boolean canMerge()
    {
      boolean bool;
      if ((mCanMerge) && (!mExecuted)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    void commit()
    {
      ArrayList localArrayList = mRecent;
      int i = 0;
      int j;
      if (localArrayList != null) {
        j = mRecent.size();
      } else {
        j = 0;
      }
      while (i < j)
      {
        ((UndoOperation)mRecent.get(i)).commit();
        i++;
      }
      mRecent = null;
    }
    
    int countOperations()
    {
      return mOperations.size();
    }
    
    void destroy()
    {
      for (int i = mOperations.size() - 1; i >= 0; i--)
      {
        UndoOwner localUndoOwner = mOperations.get(i)).mOwner;
        mOpCount -= 1;
        if (mOpCount <= 0) {
          if (mOpCount >= 0)
          {
            mManager.removeOwner(localUndoOwner);
          }
          else
          {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("Underflow of op count on owner ");
            localStringBuilder.append(localUndoOwner);
            localStringBuilder.append(" in op ");
            localStringBuilder.append(mOperations.get(i));
            throw new IllegalStateException(localStringBuilder.toString());
          }
        }
      }
    }
    
    int getCommitId()
    {
      return mCommitId;
    }
    
    CharSequence getLabel()
    {
      return mLabel;
    }
    
    <T extends UndoOperation> T getLastOperation(Class<T> paramClass, UndoOwner paramUndoOwner)
    {
      int i = mOperations.size();
      UndoOperation localUndoOperation = null;
      if ((paramClass == null) && (paramUndoOwner == null))
      {
        paramClass = localUndoOperation;
        if (i > 0) {
          paramClass = (UndoOperation)mOperations.get(i - 1);
        }
        return paramClass;
      }
      i--;
      while (i >= 0)
      {
        localUndoOperation = (UndoOperation)mOperations.get(i);
        if ((paramUndoOwner != null) && (localUndoOperation.getOwner() != paramUndoOwner))
        {
          i--;
        }
        else
        {
          if ((paramClass != null) && (localUndoOperation.getClass() != paramClass)) {
            return null;
          }
          return localUndoOperation;
        }
      }
      return null;
    }
    
    boolean hasData()
    {
      for (int i = mOperations.size() - 1; i >= 0; i--) {
        if (((UndoOperation)mOperations.get(i)).hasData()) {
          return true;
        }
      }
      return false;
    }
    
    boolean hasMultipleOwners()
    {
      int i = mOperations.size();
      if (i <= 1) {
        return false;
      }
      UndoOwner localUndoOwner = ((UndoOperation)mOperations.get(0)).getOwner();
      for (int j = 1; j < i; j++) {
        if (((UndoOperation)mOperations.get(j)).getOwner() != localUndoOwner) {
          return true;
        }
      }
      return false;
    }
    
    boolean hasOperation(UndoOwner paramUndoOwner)
    {
      int i = mOperations.size();
      boolean bool = false;
      if (paramUndoOwner == null)
      {
        if (i != 0) {
          bool = true;
        }
        return bool;
      }
      for (int j = 0; j < i; j++) {
        if (((UndoOperation)mOperations.get(j)).getOwner() == paramUndoOwner) {
          return true;
        }
      }
      return false;
    }
    
    void makeExecuted()
    {
      mExecuted = true;
    }
    
    boolean matchOwner(UndoOwner paramUndoOwner)
    {
      for (int i = mOperations.size() - 1; i >= 0; i--) {
        if (((UndoOperation)mOperations.get(i)).matchOwner(paramUndoOwner)) {
          return true;
        }
      }
      return false;
    }
    
    void redo()
    {
      int i = mOperations.size();
      for (int j = 0; j < i; j++) {
        ((UndoOperation)mOperations.get(j)).redo();
      }
    }
    
    boolean setCanMerge(boolean paramBoolean)
    {
      if ((paramBoolean) && (mExecuted)) {
        return false;
      }
      mCanMerge = paramBoolean;
      return true;
    }
    
    void setLabel(CharSequence paramCharSequence)
    {
      mLabel = paramCharSequence;
    }
    
    void undo()
    {
      for (int i = mOperations.size() - 1; i >= 0; i--) {
        ((UndoOperation)mOperations.get(i)).undo();
      }
    }
    
    void updateLabel(CharSequence paramCharSequence)
    {
      if (mLabel != null) {
        mLabel = paramCharSequence;
      }
    }
    
    void writeToParcel(Parcel paramParcel)
    {
      if (mRecent == null)
      {
        paramParcel.writeInt(mCommitId);
        paramParcel.writeInt(mCanMerge);
        paramParcel.writeInt(mExecuted);
        TextUtils.writeToParcel(mLabel, paramParcel, 0);
        int i = mOperations.size();
        paramParcel.writeInt(i);
        for (int j = 0; j < i; j++)
        {
          UndoOperation localUndoOperation = (UndoOperation)mOperations.get(j);
          mManager.saveOwner(mOwner, paramParcel);
          paramParcel.writeParcelable(localUndoOperation, 0);
        }
        return;
      }
      throw new IllegalStateException("Can't save state before committing");
    }
  }
}
