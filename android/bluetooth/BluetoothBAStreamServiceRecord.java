package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class BluetoothBAStreamServiceRecord
  implements Parcelable
{
  public static final long BSSR_AFH_CHANNEL_MAP_UPDATE_METHOD_NONE = 0L;
  public static final long BSSR_AFH_CHANNEL_MAP_UPDATE_METHOD_SCM = 1L;
  public static final long BSSR_AFH_CHANNEL_MAP_UPDATE_METHOD_TRIGGERED_SYNC_TRAIN = 2L;
  public static final long BSSR_CHANNELS_MONO = 1L;
  public static final long BSSR_CHANNELS_STEREO = 4L;
  public static final long BSSR_CODEC_FREQ_11025HZ = 2L;
  public static final long BSSR_CODEC_FREQ_128KHZ = 4096L;
  public static final long BSSR_CODEC_FREQ_12KHZ = 4L;
  public static final long BSSR_CODEC_FREQ_16KHZ = 8L;
  public static final long BSSR_CODEC_FREQ_176400HZ = 8192L;
  public static final long BSSR_CODEC_FREQ_192KHZ = 16384L;
  public static final long BSSR_CODEC_FREQ_22050HZ = 16L;
  public static final long BSSR_CODEC_FREQ_24KHZ = 32L;
  public static final long BSSR_CODEC_FREQ_32KHZ = 64L;
  public static final long BSSR_CODEC_FREQ_44100HZ = 128L;
  public static final long BSSR_CODEC_FREQ_48KHZ = 256L;
  public static final long BSSR_CODEC_FREQ_64KHZ = 512L;
  public static final long BSSR_CODEC_FREQ_88200HZ = 1024L;
  public static final long BSSR_CODEC_FREQ_8KHZ = 1L;
  public static final long BSSR_CODEC_FREQ_96KHZ = 2048L;
  public static final long BSSR_CODEC_TYPE_CELT = 1L;
  public static final long BSSR_ERASURE_CODE_2_5 = 1L;
  public static final long BSSR_ERASURE_CODE_3_7 = 2L;
  public static final long BSSR_ERASURE_CODE_3_8 = 3L;
  public static final long BSSR_ERASURE_CODE_3_9 = 4L;
  public static final long BSSR_ERASURE_CODE_NONE = 0L;
  public static final long BSSR_SAMPLE_SIZE_16_BIT = 2L;
  public static final long BSSR_SAMPLE_SIZE_24_BIT = 4L;
  public static final long BSSR_SAMPLE_SIZE_8_BIT = 1L;
  public static final long BSSR_SCMST_SUPPORT_COPY = 1L;
  public static final long BSSR_SCMST_SUPPORT_FORWARD = 2L;
  public static final long BSSR_SECURITY_ENCRYPT_TYPE_AESCCM = 512L;
  public static final long BSSR_SECURITY_ENCRYPT_TYPE_NONE = 256L;
  public static final long BSSR_SECURITY_KEY_TYPE_PRIVATE = 1L;
  public static final long BSSR_SECURITY_KEY_TYPE_TEMP = 2L;
  public static final int BSSR_TYPE_AFH_UPDATE_METHOD_ID = 9;
  public static final int BSSR_TYPE_AFH_UPDATE_METHOD_ID_LEN = 1;
  public static final int BSSR_TYPE_CHANNELS_ID = 7;
  public static final int BSSR_TYPE_CHANNELS_ID_LEN = 2;
  public static final int BSSR_TYPE_CODEC_CONFIG_CELT_FRAME_SAMPLES_ID = 12;
  public static final int BSSR_TYPE_CODEC_CONFIG_CELT_FRAME_SAMPLES_ID_LEN = 2;
  public static final int BSSR_TYPE_CODEC_CONFIG_CELT_FRAME_SIZE_ID = 11;
  public static final int BSSR_TYPE_CODEC_CONFIG_CELT_FRAME_SIZE_ID_LEN = 2;
  public static final int BSSR_TYPE_CODEC_CONFIG_CELT_FREQ_ID = 10;
  public static final int BSSR_TYPE_CODEC_CONFIG_CELT_FREQ_ID_LEN = 2;
  public static final int BSSR_TYPE_CODEC_CONFIG_CELT_ID = 3;
  public static final int BSSR_TYPE_CODEC_CONFIG_CELT_ID_LEN = 6;
  public static final int BSSR_TYPE_CODEC_TYPE_ID = 2;
  public static final int BSSR_TYPE_CODEC_TYPE_ID_LEN = 1;
  public static final int BSSR_TYPE_ERASURE_CODE_ID = 6;
  public static final int BSSR_TYPE_ERASURE_CODE_ID_LEN = 1;
  public static final int BSSR_TYPE_SAMPLE_SIZE_ID = 8;
  public static final int BSSR_TYPE_SAMPLE_SIZE_ID_LEN = 1;
  public static final int BSSR_TYPE_SCMST_SUPPORT_ID = 5;
  public static final int BSSR_TYPE_SCMST_SUPPORT_ID_LEN = 1;
  public static final int BSSR_TYPE_SECURITY_ID = 1;
  public static final int BSSR_TYPE_SECURITY_ID_LEN = 2;
  public static final int BSSR_TYPE_STREAM_ID = 0;
  public static final int BSSR_TYPE_STREAM_ID_LEN = 1;
  public static final Parcelable.Creator<BluetoothBAStreamServiceRecord> CREATOR = new Parcelable.Creator()
  {
    public BluetoothBAStreamServiceRecord createFromParcel(Parcel paramAnonymousParcel)
    {
      return new BluetoothBAStreamServiceRecord(paramAnonymousParcel, null);
    }
    
    public BluetoothBAStreamServiceRecord[] newArray(int paramAnonymousInt)
    {
      return new BluetoothBAStreamServiceRecord[paramAnonymousInt];
    }
  };
  public static final String TAG = "BluetoothBAStreamServiceRecord";
  int mNumRecords;
  private List<Map<Integer, Long>> mServiceRecordList = new ArrayList();
  
  public BluetoothBAStreamServiceRecord(int paramInt)
  {
    mNumRecords = paramInt;
  }
  
  private BluetoothBAStreamServiceRecord(Parcel paramParcel)
  {
    mNumRecords = paramParcel.readInt();
    for (int i = 0; i < mNumRecords; i++)
    {
      HashMap localHashMap = new HashMap();
      int j = paramParcel.readInt();
      for (int k = 0; k < j; k++) {
        localHashMap.put(Integer.valueOf(paramParcel.readInt()), Long.valueOf(paramParcel.readLong()));
      }
      mServiceRecordList.add(localHashMap);
    }
  }
  
  public void addServiceRecord(Map<Integer, Long> paramMap)
  {
    if (mServiceRecordList.isEmpty())
    {
      mServiceRecordList.add(paramMap);
      return;
    }
    Iterator localIterator = mServiceRecordList.iterator();
    while (localIterator.hasNext())
    {
      Map localMap = (Map)localIterator.next();
      if ((localMap.containsKey(Integer.valueOf(0))) && (((Long)localMap.get(Integer.valueOf(0))).equals(paramMap.get(Integer.valueOf(0))))) {
        mServiceRecordList.remove(localMap);
      }
    }
    mServiceRecordList.add(paramMap);
  }
  
  public void addServiceRecordValue(Long paramLong1, int paramInt, Long paramLong2)
  {
    if (!mServiceRecordList.isEmpty())
    {
      Iterator localIterator = mServiceRecordList.iterator();
      while (localIterator.hasNext())
      {
        localObject = (Map)localIterator.next();
        if ((((Map)localObject).containsKey(Integer.valueOf(0))) && (((Long)((Map)localObject).get(Integer.valueOf(0))).equals(paramLong1)))
        {
          ((Map)localObject).put(Integer.valueOf(paramInt), paramLong2);
          return;
        }
      }
    }
    Object localObject = new HashMap();
    ((Map)localObject).put(Integer.valueOf(0), paramLong1);
    ((Map)localObject).put(Integer.valueOf(paramInt), paramLong2);
    mServiceRecordList.add(localObject);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getNumRecords()
  {
    return mNumRecords;
  }
  
  public Map<Integer, Long> getServiceRecord(Long paramLong)
  {
    if (mServiceRecordList.isEmpty()) {
      return null;
    }
    Iterator localIterator = mServiceRecordList.iterator();
    while (localIterator.hasNext())
    {
      Map localMap = (Map)localIterator.next();
      if ((localMap.containsKey(Integer.valueOf(0))) && (((Long)localMap.get(Integer.valueOf(0))).equals(paramLong))) {
        return localMap;
      }
    }
    return null;
  }
  
  public Long getServiceRecordValue(Long paramLong, int paramInt)
  {
    if (!mServiceRecordList.isEmpty())
    {
      Iterator localIterator = mServiceRecordList.iterator();
      while (localIterator.hasNext())
      {
        Map localMap = (Map)localIterator.next();
        if ((localMap.containsKey(Integer.valueOf(0))) && (((Long)localMap.get(Integer.valueOf(0))).equals(paramLong))) {
          return (Long)localMap.get(Integer.valueOf(paramInt));
        }
      }
    }
    return new Long(0L);
  }
  
  public Long[] getStreamIds()
  {
    if (mServiceRecordList.isEmpty()) {
      return null;
    }
    Long[] arrayOfLong = new Long[mServiceRecordList.size()];
    int i = 0;
    Iterator localIterator = mServiceRecordList.iterator();
    while (localIterator.hasNext())
    {
      Map localMap = (Map)localIterator.next();
      int j = i;
      if (localMap.containsKey(Integer.valueOf(0)))
      {
        arrayOfLong[i] = ((Long)localMap.get(Integer.valueOf(0)));
        j = i + 1;
      }
      i = j;
    }
    return arrayOfLong;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mServiceRecordList.size());
    Iterator localIterator = mServiceRecordList.iterator();
    while (localIterator.hasNext())
    {
      Object localObject = (Map)localIterator.next();
      paramParcel.writeInt(((Map)localObject).size());
      localObject = ((Map)localObject).entrySet().iterator();
      while (((Iterator)localObject).hasNext())
      {
        Map.Entry localEntry = (Map.Entry)((Iterator)localObject).next();
        paramParcel.writeInt(((Integer)localEntry.getKey()).intValue());
        paramParcel.writeLong(((Long)localEntry.getValue()).longValue());
      }
    }
  }
}
