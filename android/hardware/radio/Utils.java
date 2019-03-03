package android.hardware.radio;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

final class Utils
{
  private static final String TAG = "BroadcastRadio.utils";
  
  Utils() {}
  
  static void close(ICloseHandle paramICloseHandle)
  {
    try
    {
      paramICloseHandle.close();
    }
    catch (RemoteException paramICloseHandle)
    {
      paramICloseHandle.rethrowFromSystemServer();
    }
  }
  
  static Set<Integer> createIntSet(Parcel paramParcel)
  {
    createSet(paramParcel, new Parcelable.Creator()
    {
      public Integer createFromParcel(Parcel paramAnonymousParcel)
      {
        return Integer.valueOf(paramAnonymousParcel.readInt());
      }
      
      public Integer[] newArray(int paramAnonymousInt)
      {
        return new Integer[paramAnonymousInt];
      }
    });
  }
  
  static <T> Set<T> createSet(Parcel paramParcel, Parcelable.Creator<T> paramCreator)
  {
    int i = paramParcel.readInt();
    HashSet localHashSet = new HashSet();
    while (i > 0)
    {
      localHashSet.add(paramParcel.readTypedObject(paramCreator));
      i--;
    }
    return localHashSet;
  }
  
  static Map<String, Integer> readStringIntMap(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    HashMap localHashMap = new HashMap();
    while (i > 0)
    {
      localHashMap.put(paramParcel.readString(), Integer.valueOf(paramParcel.readInt()));
      i--;
    }
    return localHashMap;
  }
  
  static Map<String, String> readStringMap(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    HashMap localHashMap = new HashMap();
    while (i > 0)
    {
      localHashMap.put(paramParcel.readString(), paramParcel.readString());
      i--;
    }
    return localHashMap;
  }
  
  static void writeIntSet(Parcel paramParcel, Set<Integer> paramSet)
  {
    if (paramSet == null)
    {
      paramParcel.writeInt(0);
      return;
    }
    paramParcel.writeInt(paramSet.size());
    paramSet.stream().forEach(new _..Lambda.Utils.CpgxAbBJVMfl2IUCmgGvKDeq9_U(paramParcel));
  }
  
  static <T extends Parcelable> void writeSet(Parcel paramParcel, Set<T> paramSet)
  {
    if (paramSet == null)
    {
      paramParcel.writeInt(0);
      return;
    }
    paramParcel.writeInt(paramSet.size());
    paramSet.stream().forEach(new _..Lambda.Utils.Cu3trYWUZE7O75pNHuKMUbHskAY(paramParcel));
  }
  
  static void writeStringIntMap(Parcel paramParcel, Map<String, Integer> paramMap)
  {
    if (paramMap == null)
    {
      paramParcel.writeInt(0);
      return;
    }
    paramParcel.writeInt(paramMap.size());
    paramMap = paramMap.entrySet().iterator();
    while (paramMap.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)paramMap.next();
      paramParcel.writeString((String)localEntry.getKey());
      paramParcel.writeInt(((Integer)localEntry.getValue()).intValue());
    }
  }
  
  static void writeStringMap(Parcel paramParcel, Map<String, String> paramMap)
  {
    if (paramMap == null)
    {
      paramParcel.writeInt(0);
      return;
    }
    paramParcel.writeInt(paramMap.size());
    Iterator localIterator = paramMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      paramMap = (Map.Entry)localIterator.next();
      paramParcel.writeString((String)paramMap.getKey());
      paramParcel.writeString((String)paramMap.getValue());
    }
  }
  
  static <T extends Parcelable> void writeTypedCollection(Parcel paramParcel, Collection<T> paramCollection)
  {
    ArrayList localArrayList = null;
    if (paramCollection != null) {
      if ((paramCollection instanceof ArrayList)) {
        localArrayList = (ArrayList)paramCollection;
      } else {
        localArrayList = new ArrayList(paramCollection);
      }
    }
    paramParcel.writeTypedList(localArrayList);
  }
}
