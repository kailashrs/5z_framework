package android.os;

import android.util.ArrayMap;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.VisibleForTesting;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class Bundle
  extends BaseBundle
  implements Cloneable, Parcelable
{
  public static final Parcelable.Creator<Bundle> CREATOR = new Parcelable.Creator()
  {
    public Bundle createFromParcel(Parcel paramAnonymousParcel)
    {
      return paramAnonymousParcel.readBundle();
    }
    
    public Bundle[] newArray(int paramAnonymousInt)
    {
      return new Bundle[paramAnonymousInt];
    }
  };
  public static final Bundle EMPTY = new Bundle();
  @VisibleForTesting
  static final int FLAG_ALLOW_FDS = 1024;
  @VisibleForTesting
  static final int FLAG_HAS_FDS = 256;
  @VisibleForTesting
  static final int FLAG_HAS_FDS_KNOWN = 512;
  public static final Bundle STRIPPED;
  
  static
  {
    EMPTYmMap = ArrayMap.EMPTY;
    STRIPPED = new Bundle();
    STRIPPED.putInt("STRIPPED", 1);
  }
  
  public Bundle()
  {
    mFlags = 1536;
  }
  
  public Bundle(int paramInt)
  {
    super(paramInt);
    mFlags = 1536;
  }
  
  public Bundle(Bundle paramBundle)
  {
    super(paramBundle);
    mFlags = mFlags;
  }
  
  @VisibleForTesting
  public Bundle(Parcel paramParcel)
  {
    super(paramParcel);
    mFlags = 1024;
    maybePrefillHasFds();
  }
  
  @VisibleForTesting
  public Bundle(Parcel paramParcel, int paramInt)
  {
    super(paramParcel, paramInt);
    mFlags = 1024;
    maybePrefillHasFds();
  }
  
  public Bundle(PersistableBundle paramPersistableBundle)
  {
    super(paramPersistableBundle);
    mFlags = 1536;
  }
  
  public Bundle(ClassLoader paramClassLoader)
  {
    super(paramClassLoader);
    mFlags = 1536;
  }
  
  Bundle(boolean paramBoolean)
  {
    super(paramBoolean);
  }
  
  public static Bundle forPair(String paramString1, String paramString2)
  {
    Bundle localBundle = new Bundle(1);
    localBundle.putString(paramString1, paramString2);
    return localBundle;
  }
  
  private void maybePrefillHasFds()
  {
    if (mParcelledData != null) {
      if (mParcelledData.hasFileDescriptors()) {
        mFlags |= 0x300;
      } else {
        mFlags |= 0x200;
      }
    }
  }
  
  public static Bundle setDefusable(Bundle paramBundle, boolean paramBoolean)
  {
    if (paramBundle != null) {
      paramBundle.setDefusable(paramBoolean);
    }
    return paramBundle;
  }
  
  public void clear()
  {
    super.clear();
    mFlags = 1536;
  }
  
  public Object clone()
  {
    return new Bundle(this);
  }
  
  public Bundle deepCopy()
  {
    Bundle localBundle = new Bundle(false);
    localBundle.copyInternal(this, true);
    return localBundle;
  }
  
  public int describeContents()
  {
    int i = 0;
    if (hasFileDescriptors()) {
      i = 0x0 | 0x1;
    }
    return i;
  }
  
  public Bundle filterValues()
  {
    unparcel();
    Object localObject1 = this;
    Object localObject2 = localObject1;
    if (mMap != null)
    {
      Object localObject3 = mMap;
      int i = ((ArrayMap)localObject3).size() - 1;
      for (;;)
      {
        localObject2 = localObject1;
        if (i < 0) {
          break;
        }
        Object localObject4 = ((ArrayMap)localObject3).valueAt(i);
        Object localObject5;
        if (PersistableBundle.isValidType(localObject4))
        {
          localObject5 = localObject1;
          localObject2 = localObject3;
        }
        else if ((localObject4 instanceof Bundle))
        {
          Bundle localBundle = ((Bundle)localObject4).filterValues();
          localObject5 = localObject1;
          localObject2 = localObject3;
          if (localBundle != localObject4)
          {
            localObject2 = localObject3;
            if (localObject3 == mMap)
            {
              localObject1 = new Bundle(this);
              localObject2 = mMap;
            }
            ((ArrayMap)localObject2).setValueAt(i, localBundle);
            localObject5 = localObject1;
          }
        }
        else if (localObject4.getClass().getName().startsWith("android."))
        {
          localObject5 = localObject1;
          localObject2 = localObject3;
        }
        else
        {
          localObject2 = localObject3;
          if (localObject3 == mMap)
          {
            localObject1 = new Bundle(this);
            localObject2 = mMap;
          }
          ((ArrayMap)localObject2).removeAt(i);
          localObject5 = localObject1;
        }
        i--;
        localObject1 = localObject5;
        localObject3 = localObject2;
      }
    }
    mFlags |= 0x200;
    mFlags &= 0xFEFF;
    return localObject2;
  }
  
  public IBinder getBinder(String paramString)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return null;
    }
    try
    {
      IBinder localIBinder = (IBinder)localObject;
      return localIBinder;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "IBinder", localClassCastException);
    }
    return null;
  }
  
  public Bundle getBundle(String paramString)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return null;
    }
    try
    {
      Bundle localBundle = (Bundle)localObject;
      return localBundle;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "Bundle", localClassCastException);
    }
    return null;
  }
  
  public byte getByte(String paramString)
  {
    return super.getByte(paramString);
  }
  
  public Byte getByte(String paramString, byte paramByte)
  {
    return super.getByte(paramString, paramByte);
  }
  
  public byte[] getByteArray(String paramString)
  {
    return super.getByteArray(paramString);
  }
  
  public char getChar(String paramString)
  {
    return super.getChar(paramString);
  }
  
  public char getChar(String paramString, char paramChar)
  {
    return super.getChar(paramString, paramChar);
  }
  
  public char[] getCharArray(String paramString)
  {
    return super.getCharArray(paramString);
  }
  
  public CharSequence getCharSequence(String paramString)
  {
    return super.getCharSequence(paramString);
  }
  
  public CharSequence getCharSequence(String paramString, CharSequence paramCharSequence)
  {
    return super.getCharSequence(paramString, paramCharSequence);
  }
  
  public CharSequence[] getCharSequenceArray(String paramString)
  {
    return super.getCharSequenceArray(paramString);
  }
  
  public ArrayList<CharSequence> getCharSequenceArrayList(String paramString)
  {
    return super.getCharSequenceArrayList(paramString);
  }
  
  public ClassLoader getClassLoader()
  {
    return super.getClassLoader();
  }
  
  public float getFloat(String paramString)
  {
    return super.getFloat(paramString);
  }
  
  public float getFloat(String paramString, float paramFloat)
  {
    return super.getFloat(paramString, paramFloat);
  }
  
  public float[] getFloatArray(String paramString)
  {
    return super.getFloatArray(paramString);
  }
  
  @Deprecated
  public IBinder getIBinder(String paramString)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return null;
    }
    try
    {
      IBinder localIBinder = (IBinder)localObject;
      return localIBinder;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "IBinder", localClassCastException);
    }
    return null;
  }
  
  public ArrayList<Integer> getIntegerArrayList(String paramString)
  {
    return super.getIntegerArrayList(paramString);
  }
  
  public <T extends Parcelable> T getParcelable(String paramString)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return null;
    }
    try
    {
      Parcelable localParcelable = (Parcelable)localObject;
      return localParcelable;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "Parcelable", localClassCastException);
    }
    return null;
  }
  
  public Parcelable[] getParcelableArray(String paramString)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return null;
    }
    try
    {
      Parcelable[] arrayOfParcelable = (Parcelable[])localObject;
      return arrayOfParcelable;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "Parcelable[]", localClassCastException);
    }
    return null;
  }
  
  public <T extends Parcelable> ArrayList<T> getParcelableArrayList(String paramString)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return null;
    }
    try
    {
      ArrayList localArrayList = (ArrayList)localObject;
      return localArrayList;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "ArrayList", localClassCastException);
    }
    return null;
  }
  
  public Serializable getSerializable(String paramString)
  {
    return super.getSerializable(paramString);
  }
  
  public short getShort(String paramString)
  {
    return super.getShort(paramString);
  }
  
  public short getShort(String paramString, short paramShort)
  {
    return super.getShort(paramString, paramShort);
  }
  
  public short[] getShortArray(String paramString)
  {
    return super.getShortArray(paramString);
  }
  
  public int getSize()
  {
    if (mParcelledData != null) {
      return mParcelledData.dataSize();
    }
    return 0;
  }
  
  public Size getSize(String paramString)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    try
    {
      Size localSize = (Size)localObject;
      return localSize;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "Size", localClassCastException);
    }
    return null;
  }
  
  public SizeF getSizeF(String paramString)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    try
    {
      SizeF localSizeF = (SizeF)localObject;
      return localSizeF;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "SizeF", localClassCastException);
    }
    return null;
  }
  
  public <T extends Parcelable> SparseArray<T> getSparseParcelableArray(String paramString)
  {
    unparcel();
    Object localObject = mMap.get(paramString);
    if (localObject == null) {
      return null;
    }
    try
    {
      SparseArray localSparseArray = (SparseArray)localObject;
      return localSparseArray;
    }
    catch (ClassCastException localClassCastException)
    {
      typeWarning(paramString, localObject, "SparseArray", localClassCastException);
    }
    return null;
  }
  
  public ArrayList<String> getStringArrayList(String paramString)
  {
    return super.getStringArrayList(paramString);
  }
  
  public boolean hasFileDescriptors()
  {
    int i = mFlags;
    boolean bool = false;
    if ((i & 0x200) == 0)
    {
      int j = 0;
      i = 0;
      if (mParcelledData != null)
      {
        if (mParcelledData.hasFileDescriptors()) {
          j = 1;
        }
      }
      else
      {
        int k = mMap.size() - 1;
        for (;;)
        {
          j = i;
          if (k < 0) {
            break;
          }
          Object localObject1 = mMap.valueAt(k);
          if ((localObject1 instanceof Parcelable))
          {
            j = i;
            if ((((Parcelable)localObject1).describeContents() & 0x1) != 0)
            {
              j = 1;
              break;
            }
          }
          else
          {
            Object localObject2;
            int m;
            if ((localObject1 instanceof Parcelable[]))
            {
              localObject2 = (Parcelable[])localObject1;
              for (m = localObject2.length - 1;; m--)
              {
                j = i;
                if (m < 0) {
                  break;
                }
                localObject1 = localObject2[m];
                if ((localObject1 != null) && ((((Parcelable)localObject1).describeContents() & 0x1) != 0))
                {
                  j = 1;
                  break;
                }
              }
            }
            else if ((localObject1 instanceof SparseArray))
            {
              localObject2 = (SparseArray)localObject1;
              for (m = ((SparseArray)localObject2).size() - 1;; m--)
              {
                j = i;
                if (m < 0) {
                  break;
                }
                localObject1 = (Parcelable)((SparseArray)localObject2).valueAt(m);
                if ((localObject1 != null) && ((((Parcelable)localObject1).describeContents() & 0x1) != 0))
                {
                  j = 1;
                  break;
                }
              }
            }
            else
            {
              j = i;
              if ((localObject1 instanceof ArrayList))
              {
                localObject2 = (ArrayList)localObject1;
                j = i;
                if (!((ArrayList)localObject2).isEmpty())
                {
                  j = i;
                  if ((((ArrayList)localObject2).get(0) instanceof Parcelable)) {
                    for (m = ((ArrayList)localObject2).size() - 1;; m--)
                    {
                      j = i;
                      if (m < 0) {
                        break;
                      }
                      localObject1 = (Parcelable)((ArrayList)localObject2).get(m);
                      if ((localObject1 != null) && ((((Parcelable)localObject1).describeContents() & 0x1) != 0))
                      {
                        j = 1;
                        break;
                      }
                    }
                  }
                }
              }
            }
          }
          k--;
          i = j;
        }
      }
      if (j != 0) {
        mFlags |= 0x100;
      } else {
        mFlags &= 0xFEFF;
      }
      mFlags |= 0x200;
    }
    if ((mFlags & 0x100) != 0) {
      bool = true;
    }
    return bool;
  }
  
  public void putAll(Bundle paramBundle)
  {
    unparcel();
    paramBundle.unparcel();
    mMap.putAll(mMap);
    if ((mFlags & 0x100) != 0) {
      mFlags |= 0x100;
    }
    if ((mFlags & 0x200) == 0) {
      mFlags &= 0xFDFF;
    }
  }
  
  public void putBinder(String paramString, IBinder paramIBinder)
  {
    unparcel();
    mMap.put(paramString, paramIBinder);
  }
  
  public void putBundle(String paramString, Bundle paramBundle)
  {
    unparcel();
    mMap.put(paramString, paramBundle);
  }
  
  public void putByte(String paramString, byte paramByte)
  {
    super.putByte(paramString, paramByte);
  }
  
  public void putByteArray(String paramString, byte[] paramArrayOfByte)
  {
    super.putByteArray(paramString, paramArrayOfByte);
  }
  
  public void putChar(String paramString, char paramChar)
  {
    super.putChar(paramString, paramChar);
  }
  
  public void putCharArray(String paramString, char[] paramArrayOfChar)
  {
    super.putCharArray(paramString, paramArrayOfChar);
  }
  
  public void putCharSequence(String paramString, CharSequence paramCharSequence)
  {
    super.putCharSequence(paramString, paramCharSequence);
  }
  
  public void putCharSequenceArray(String paramString, CharSequence[] paramArrayOfCharSequence)
  {
    super.putCharSequenceArray(paramString, paramArrayOfCharSequence);
  }
  
  public void putCharSequenceArrayList(String paramString, ArrayList<CharSequence> paramArrayList)
  {
    super.putCharSequenceArrayList(paramString, paramArrayList);
  }
  
  public void putFloat(String paramString, float paramFloat)
  {
    super.putFloat(paramString, paramFloat);
  }
  
  public void putFloatArray(String paramString, float[] paramArrayOfFloat)
  {
    super.putFloatArray(paramString, paramArrayOfFloat);
  }
  
  @Deprecated
  public void putIBinder(String paramString, IBinder paramIBinder)
  {
    unparcel();
    mMap.put(paramString, paramIBinder);
  }
  
  public void putIntegerArrayList(String paramString, ArrayList<Integer> paramArrayList)
  {
    super.putIntegerArrayList(paramString, paramArrayList);
  }
  
  public void putParcelable(String paramString, Parcelable paramParcelable)
  {
    unparcel();
    mMap.put(paramString, paramParcelable);
    mFlags &= 0xFDFF;
  }
  
  public void putParcelableArray(String paramString, Parcelable[] paramArrayOfParcelable)
  {
    unparcel();
    mMap.put(paramString, paramArrayOfParcelable);
    mFlags &= 0xFDFF;
  }
  
  public void putParcelableArrayList(String paramString, ArrayList<? extends Parcelable> paramArrayList)
  {
    unparcel();
    mMap.put(paramString, paramArrayList);
    mFlags &= 0xFDFF;
  }
  
  public void putParcelableList(String paramString, List<? extends Parcelable> paramList)
  {
    unparcel();
    mMap.put(paramString, paramList);
    mFlags &= 0xFDFF;
  }
  
  public void putSerializable(String paramString, Serializable paramSerializable)
  {
    super.putSerializable(paramString, paramSerializable);
  }
  
  public void putShort(String paramString, short paramShort)
  {
    super.putShort(paramString, paramShort);
  }
  
  public void putShortArray(String paramString, short[] paramArrayOfShort)
  {
    super.putShortArray(paramString, paramArrayOfShort);
  }
  
  public void putSize(String paramString, Size paramSize)
  {
    unparcel();
    mMap.put(paramString, paramSize);
  }
  
  public void putSizeF(String paramString, SizeF paramSizeF)
  {
    unparcel();
    mMap.put(paramString, paramSizeF);
  }
  
  public void putSparseParcelableArray(String paramString, SparseArray<? extends Parcelable> paramSparseArray)
  {
    unparcel();
    mMap.put(paramString, paramSparseArray);
    mFlags &= 0xFDFF;
  }
  
  public void putStringArrayList(String paramString, ArrayList<String> paramArrayList)
  {
    super.putStringArrayList(paramString, paramArrayList);
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    super.readFromParcelInner(paramParcel);
    mFlags = 1024;
    maybePrefillHasFds();
  }
  
  public void remove(String paramString)
  {
    super.remove(paramString);
    if ((mFlags & 0x100) != 0) {
      mFlags &= 0xFDFF;
    }
  }
  
  public boolean setAllowFds(boolean paramBoolean)
  {
    boolean bool;
    if ((mFlags & 0x400) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    if (paramBoolean) {
      mFlags |= 0x400;
    } else {
      mFlags &= 0xFBFF;
    }
    return bool;
  }
  
  public void setClassLoader(ClassLoader paramClassLoader)
  {
    super.setClassLoader(paramClassLoader);
  }
  
  public void setDefusable(boolean paramBoolean)
  {
    if (paramBoolean) {
      mFlags |= 0x1;
    } else {
      mFlags &= 0xFFFFFFFE;
    }
  }
  
  public String toShortString()
  {
    try
    {
      if (mParcelledData != null)
      {
        if (isEmptyParcel()) {
          return "EMPTY_PARCEL";
        }
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("mParcelledData.dataSize=");
        ((StringBuilder)localObject1).append(mParcelledData.dataSize());
        localObject1 = ((StringBuilder)localObject1).toString();
        return localObject1;
      }
      Object localObject1 = mMap.toString();
      return localObject1;
    }
    finally {}
  }
  
  public String toString()
  {
    try
    {
      if (mParcelledData != null)
      {
        if (isEmptyParcel()) {
          return "Bundle[EMPTY_PARCEL]";
        }
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("Bundle[mParcelledData.dataSize=");
        ((StringBuilder)localObject1).append(mParcelledData.dataSize());
        ((StringBuilder)localObject1).append("]");
        localObject1 = ((StringBuilder)localObject1).toString();
        return localObject1;
      }
      Object localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>();
      ((StringBuilder)localObject1).append("Bundle[");
      ((StringBuilder)localObject1).append(mMap.toString());
      ((StringBuilder)localObject1).append("]");
      localObject1 = ((StringBuilder)localObject1).toString();
      return localObject1;
    }
    finally {}
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if ((mFlags & 0x400) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    boolean bool = paramParcel.pushAllowFds(bool);
    try
    {
      super.writeToParcelInner(paramParcel, paramInt);
      return;
    }
    finally
    {
      paramParcel.restoreAllowFds(bool);
    }
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    if (mParcelledData != null)
    {
      if (isEmptyParcel()) {
        paramProtoOutputStream.write(1120986464257L, 0);
      } else {
        paramProtoOutputStream.write(1120986464257L, mParcelledData.dataSize());
      }
    }
    else {
      paramProtoOutputStream.write(1138166333442L, mMap.toString());
    }
    paramProtoOutputStream.end(paramLong);
  }
}
