package android.hardware.radio;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SystemApi
public final class ProgramList
  implements AutoCloseable
{
  private boolean mIsClosed = false;
  private boolean mIsComplete = false;
  private final List<ListCallback> mListCallbacks = new ArrayList();
  private final Object mLock = new Object();
  private OnCloseListener mOnCloseListener;
  private final List<OnCompleteListener> mOnCompleteListeners = new ArrayList();
  private final Map<ProgramSelector.Identifier, RadioManager.ProgramInfo> mPrograms = new HashMap();
  
  ProgramList() {}
  
  private void putLocked(RadioManager.ProgramInfo paramProgramInfo)
  {
    ProgramSelector.Identifier localIdentifier = paramProgramInfo.getSelector().getPrimaryId();
    mPrograms.put((ProgramSelector.Identifier)Objects.requireNonNull(localIdentifier), paramProgramInfo);
    paramProgramInfo = paramProgramInfo.getSelector().getPrimaryId();
    mListCallbacks.forEach(new _..Lambda.ProgramList.fDnoTVk5UB7qTfD9S7SYPcadYn0(paramProgramInfo));
  }
  
  private void removeLocked(ProgramSelector.Identifier paramIdentifier)
  {
    paramIdentifier = (RadioManager.ProgramInfo)mPrograms.remove(Objects.requireNonNull(paramIdentifier));
    if (paramIdentifier == null) {
      return;
    }
    paramIdentifier = paramIdentifier.getSelector().getPrimaryId();
    mListCallbacks.forEach(new _..Lambda.ProgramList.fHYelmhnUsVTYl6dFj75fMqCjGs(paramIdentifier));
  }
  
  public void addOnCompleteListener(OnCompleteListener paramOnCompleteListener)
  {
    synchronized (mLock)
    {
      if (mIsClosed) {
        return;
      }
      mOnCompleteListeners.add((OnCompleteListener)Objects.requireNonNull(paramOnCompleteListener));
      if (mIsComplete) {
        paramOnCompleteListener.onComplete();
      }
      return;
    }
  }
  
  public void addOnCompleteListener(Executor paramExecutor, OnCompleteListener paramOnCompleteListener)
  {
    addOnCompleteListener(new _..Lambda.ProgramList.aDYMynqVdAUqeKXIxfNtN1u67zs(paramExecutor, paramOnCompleteListener));
  }
  
  void apply(Chunk paramChunk)
  {
    synchronized (mLock)
    {
      if (mIsClosed) {
        return;
      }
      mIsComplete = false;
      if (paramChunk.isPurge())
      {
        localObject2 = new java/util/HashSet;
        ((HashSet)localObject2).<init>(mPrograms.keySet());
        localObject3 = ((HashSet)localObject2).stream();
        localObject2 = new android/hardware/radio/_$$Lambda$ProgramList$F_JpTj3vYguKIUQbnLbTePTuqUE;
        ((_..Lambda.ProgramList.F_JpTj3vYguKIUQbnLbTePTuqUE)localObject2).<init>(this);
        ((Stream)localObject3).forEach((Consumer)localObject2);
      }
      Object localObject2 = paramChunk.getRemoved().stream();
      Object localObject3 = new android/hardware/radio/_$$Lambda$ProgramList$pKu0Zp5jwjix619hfB_Imj8Ke_g;
      ((_..Lambda.ProgramList.pKu0Zp5jwjix619hfB_Imj8Ke_g)localObject3).<init>(this);
      ((Stream)localObject2).forEach((Consumer)localObject3);
      localObject3 = paramChunk.getModified().stream();
      localObject2 = new android/hardware/radio/_$$Lambda$ProgramList$eY050tMTgAcGV9hiWR_UDxhkfhw;
      ((_..Lambda.ProgramList.eY050tMTgAcGV9hiWR_UDxhkfhw)localObject2).<init>(this);
      ((Stream)localObject3).forEach((Consumer)localObject2);
      if (paramChunk.isComplete())
      {
        mIsComplete = true;
        mOnCompleteListeners.forEach(_..Lambda.ProgramList.GfCj9jJ5znxw2TV4c2uykq35dgI.INSTANCE);
      }
      return;
    }
  }
  
  public void close()
  {
    synchronized (mLock)
    {
      if (mIsClosed) {
        return;
      }
      mIsClosed = true;
      mPrograms.clear();
      mListCallbacks.clear();
      mOnCompleteListeners.clear();
      if (mOnCloseListener != null)
      {
        mOnCloseListener.onClose();
        mOnCloseListener = null;
      }
      return;
    }
  }
  
  public RadioManager.ProgramInfo get(ProgramSelector.Identifier paramIdentifier)
  {
    synchronized (mLock)
    {
      paramIdentifier = (RadioManager.ProgramInfo)mPrograms.get(Objects.requireNonNull(paramIdentifier));
      return paramIdentifier;
    }
  }
  
  public void registerListCallback(ListCallback paramListCallback)
  {
    synchronized (mLock)
    {
      if (mIsClosed) {
        return;
      }
      mListCallbacks.add((ListCallback)Objects.requireNonNull(paramListCallback));
      return;
    }
  }
  
  public void registerListCallback(final Executor paramExecutor, final ListCallback paramListCallback)
  {
    registerListCallback(new ListCallback()
    {
      public void onItemChanged(ProgramSelector.Identifier paramAnonymousIdentifier)
      {
        paramExecutor.execute(new _..Lambda.ProgramList.1.DVvry5MfhR6n8H2EZn67rvuhllI(paramListCallback, paramAnonymousIdentifier));
      }
      
      public void onItemRemoved(ProgramSelector.Identifier paramAnonymousIdentifier)
      {
        paramExecutor.execute(new _..Lambda.ProgramList.1.a_xWqo5pESOZhcJIWvpiCd2AXmY(paramListCallback, paramAnonymousIdentifier));
      }
    });
  }
  
  public void removeOnCompleteListener(OnCompleteListener paramOnCompleteListener)
  {
    synchronized (mLock)
    {
      if (mIsClosed) {
        return;
      }
      mOnCompleteListeners.remove(Objects.requireNonNull(paramOnCompleteListener));
      return;
    }
  }
  
  void setOnCloseListener(OnCloseListener paramOnCloseListener)
  {
    synchronized (mLock)
    {
      if (mOnCloseListener == null)
      {
        mOnCloseListener = paramOnCloseListener;
        return;
      }
      paramOnCloseListener = new java/lang/IllegalStateException;
      paramOnCloseListener.<init>("Close callback is already set");
      throw paramOnCloseListener;
    }
  }
  
  public List<RadioManager.ProgramInfo> toList()
  {
    synchronized (mLock)
    {
      List localList = (List)mPrograms.values().stream().collect(Collectors.toList());
      return localList;
    }
  }
  
  public void unregisterListCallback(ListCallback paramListCallback)
  {
    synchronized (mLock)
    {
      if (mIsClosed) {
        return;
      }
      mListCallbacks.remove(Objects.requireNonNull(paramListCallback));
      return;
    }
  }
  
  public static final class Chunk
    implements Parcelable
  {
    public static final Parcelable.Creator<Chunk> CREATOR = new Parcelable.Creator()
    {
      public ProgramList.Chunk createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ProgramList.Chunk(paramAnonymousParcel, null);
      }
      
      public ProgramList.Chunk[] newArray(int paramAnonymousInt)
      {
        return new ProgramList.Chunk[paramAnonymousInt];
      }
    };
    private final boolean mComplete;
    private final Set<RadioManager.ProgramInfo> mModified;
    private final boolean mPurge;
    private final Set<ProgramSelector.Identifier> mRemoved;
    
    private Chunk(Parcel paramParcel)
    {
      int i = paramParcel.readByte();
      boolean bool1 = false;
      if (i != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      mPurge = bool2;
      boolean bool2 = bool1;
      if (paramParcel.readByte() != 0) {
        bool2 = true;
      }
      mComplete = bool2;
      mModified = Utils.createSet(paramParcel, RadioManager.ProgramInfo.CREATOR);
      mRemoved = Utils.createSet(paramParcel, ProgramSelector.Identifier.CREATOR);
    }
    
    public Chunk(boolean paramBoolean1, boolean paramBoolean2, Set<RadioManager.ProgramInfo> paramSet, Set<ProgramSelector.Identifier> paramSet1)
    {
      mPurge = paramBoolean1;
      mComplete = paramBoolean2;
      if (paramSet == null) {
        paramSet = Collections.emptySet();
      }
      mModified = paramSet;
      if (paramSet1 != null) {
        paramSet = paramSet1;
      } else {
        paramSet = Collections.emptySet();
      }
      mRemoved = paramSet;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public Set<RadioManager.ProgramInfo> getModified()
    {
      return mModified;
    }
    
    public Set<ProgramSelector.Identifier> getRemoved()
    {
      return mRemoved;
    }
    
    public boolean isComplete()
    {
      return mComplete;
    }
    
    public boolean isPurge()
    {
      return mPurge;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeByte((byte)mPurge);
      paramParcel.writeByte((byte)mComplete);
      Utils.writeSet(paramParcel, mModified);
      Utils.writeSet(paramParcel, mRemoved);
    }
  }
  
  public static final class Filter
    implements Parcelable
  {
    public static final Parcelable.Creator<Filter> CREATOR = new Parcelable.Creator()
    {
      public ProgramList.Filter createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ProgramList.Filter(paramAnonymousParcel, null);
      }
      
      public ProgramList.Filter[] newArray(int paramAnonymousInt)
      {
        return new ProgramList.Filter[paramAnonymousInt];
      }
    };
    private final boolean mExcludeModifications;
    private final Set<Integer> mIdentifierTypes;
    private final Set<ProgramSelector.Identifier> mIdentifiers;
    private final boolean mIncludeCategories;
    private final Map<String, String> mVendorFilter;
    
    public Filter()
    {
      mIdentifierTypes = Collections.emptySet();
      mIdentifiers = Collections.emptySet();
      mIncludeCategories = false;
      mExcludeModifications = false;
      mVendorFilter = null;
    }
    
    private Filter(Parcel paramParcel)
    {
      mIdentifierTypes = Utils.createIntSet(paramParcel);
      mIdentifiers = Utils.createSet(paramParcel, ProgramSelector.Identifier.CREATOR);
      int i = paramParcel.readByte();
      boolean bool1 = false;
      if (i != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      mIncludeCategories = bool2;
      boolean bool2 = bool1;
      if (paramParcel.readByte() != 0) {
        bool2 = true;
      }
      mExcludeModifications = bool2;
      mVendorFilter = Utils.readStringMap(paramParcel);
    }
    
    public Filter(Map<String, String> paramMap)
    {
      mIdentifierTypes = Collections.emptySet();
      mIdentifiers = Collections.emptySet();
      mIncludeCategories = false;
      mExcludeModifications = false;
      mVendorFilter = paramMap;
    }
    
    public Filter(Set<Integer> paramSet, Set<ProgramSelector.Identifier> paramSet1, boolean paramBoolean1, boolean paramBoolean2)
    {
      mIdentifierTypes = ((Set)Objects.requireNonNull(paramSet));
      mIdentifiers = ((Set)Objects.requireNonNull(paramSet1));
      mIncludeCategories = paramBoolean1;
      mExcludeModifications = paramBoolean2;
      mVendorFilter = null;
    }
    
    public boolean areCategoriesIncluded()
    {
      return mIncludeCategories;
    }
    
    public boolean areModificationsExcluded()
    {
      return mExcludeModifications;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public Set<Integer> getIdentifierTypes()
    {
      return mIdentifierTypes;
    }
    
    public Set<ProgramSelector.Identifier> getIdentifiers()
    {
      return mIdentifiers;
    }
    
    public Map<String, String> getVendorFilter()
    {
      return mVendorFilter;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      Utils.writeIntSet(paramParcel, mIdentifierTypes);
      Utils.writeSet(paramParcel, mIdentifiers);
      paramParcel.writeByte((byte)mIncludeCategories);
      paramParcel.writeByte((byte)mExcludeModifications);
      Utils.writeStringMap(paramParcel, mVendorFilter);
    }
  }
  
  public static abstract class ListCallback
  {
    public ListCallback() {}
    
    public void onItemChanged(ProgramSelector.Identifier paramIdentifier) {}
    
    public void onItemRemoved(ProgramSelector.Identifier paramIdentifier) {}
  }
  
  static abstract interface OnCloseListener
  {
    public abstract void onClose();
  }
  
  public static abstract interface OnCompleteListener
  {
    public abstract void onComplete();
  }
}
