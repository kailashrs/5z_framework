package com.android.internal.alsa;

import android.util.Slog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class AlsaDevicesParser
{
  protected static final boolean DEBUG = false;
  public static final int SCANSTATUS_EMPTY = 2;
  public static final int SCANSTATUS_FAIL = 1;
  public static final int SCANSTATUS_NOTSCANNED = -1;
  public static final int SCANSTATUS_SUCCESS = 0;
  private static final String TAG = "AlsaDevicesParser";
  private static final String kDevicesFilePath = "/proc/asound/devices";
  private static final int kEndIndex_CardNum = 8;
  private static final int kEndIndex_DeviceNum = 11;
  private static final int kIndex_CardDeviceField = 5;
  private static final int kStartIndex_CardNum = 6;
  private static final int kStartIndex_DeviceNum = 9;
  private static final int kStartIndex_Type = 14;
  private static LineTokenizer mTokenizer = new LineTokenizer(" :[]-");
  private final ArrayList<AlsaDeviceRecord> mDeviceRecords = new ArrayList();
  private boolean mHasCaptureDevices = false;
  private boolean mHasMIDIDevices = false;
  private boolean mHasPlaybackDevices = false;
  private int mScanStatus = -1;
  
  public AlsaDevicesParser() {}
  
  private void Log(String paramString) {}
  
  private boolean isLineDeviceRecord(String paramString)
  {
    boolean bool;
    if (paramString.charAt(5) == '[') {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public int getDefaultDeviceNum(int paramInt)
  {
    return 0;
  }
  
  public int getScanStatus()
  {
    return mScanStatus;
  }
  
  public boolean hasCaptureDevices(int paramInt)
  {
    Iterator localIterator = mDeviceRecords.iterator();
    while (localIterator.hasNext())
    {
      AlsaDeviceRecord localAlsaDeviceRecord = (AlsaDeviceRecord)localIterator.next();
      if ((mCardNum == paramInt) && (mDeviceType == 0) && (mDeviceDir == 0)) {
        return true;
      }
    }
    return false;
  }
  
  public boolean hasMIDIDevices(int paramInt)
  {
    Iterator localIterator = mDeviceRecords.iterator();
    while (localIterator.hasNext())
    {
      AlsaDeviceRecord localAlsaDeviceRecord = (AlsaDeviceRecord)localIterator.next();
      if ((mCardNum == paramInt) && (mDeviceType == 2)) {
        return true;
      }
    }
    return false;
  }
  
  public boolean hasPlaybackDevices(int paramInt)
  {
    Iterator localIterator = mDeviceRecords.iterator();
    while (localIterator.hasNext())
    {
      AlsaDeviceRecord localAlsaDeviceRecord = (AlsaDeviceRecord)localIterator.next();
      if ((mCardNum == paramInt) && (mDeviceType == 0) && (mDeviceDir == 1)) {
        return true;
      }
    }
    return false;
  }
  
  public int scan()
  {
    mDeviceRecords.clear();
    Object localObject = new File("/proc/asound/devices");
    try
    {
      FileReader localFileReader = new java/io/FileReader;
      localFileReader.<init>((File)localObject);
      BufferedReader localBufferedReader = new java/io/BufferedReader;
      localBufferedReader.<init>(localFileReader);
      for (;;)
      {
        localObject = localBufferedReader.readLine();
        if (localObject == null) {
          break;
        }
        if (isLineDeviceRecord((String)localObject))
        {
          AlsaDeviceRecord localAlsaDeviceRecord = new com/android/internal/alsa/AlsaDevicesParser$AlsaDeviceRecord;
          localAlsaDeviceRecord.<init>(this);
          localAlsaDeviceRecord.parse((String)localObject);
          Slog.i("AlsaDevicesParser", localAlsaDeviceRecord.textFormat());
          mDeviceRecords.add(localAlsaDeviceRecord);
        }
      }
      localFileReader.close();
      if (mDeviceRecords.size() > 0) {
        mScanStatus = 0;
      } else {
        mScanStatus = 2;
      }
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
      mScanStatus = 1;
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      localFileNotFoundException.printStackTrace();
      mScanStatus = 1;
    }
    return mScanStatus;
  }
  
  public class AlsaDeviceRecord
  {
    public static final int kDeviceDir_Capture = 0;
    public static final int kDeviceDir_Playback = 1;
    public static final int kDeviceDir_Unknown = -1;
    public static final int kDeviceType_Audio = 0;
    public static final int kDeviceType_Control = 1;
    public static final int kDeviceType_MIDI = 2;
    public static final int kDeviceType_Unknown = -1;
    int mCardNum = -1;
    int mDeviceDir = -1;
    int mDeviceNum = -1;
    int mDeviceType = -1;
    
    public AlsaDeviceRecord() {}
    
    public boolean parse(String paramString)
    {
      int i = 0;
      int m;
      for (int j = 0;; j = m + 1)
      {
        int k = AlsaDevicesParser.mTokenizer.nextToken(paramString, i);
        if (k == -1) {
          return true;
        }
        m = AlsaDevicesParser.mTokenizer.nextDelimiter(paramString, k);
        i = m;
        if (m == -1) {
          i = paramString.length();
        }
        String str = paramString.substring(k, i);
        switch (j)
        {
        default: 
          m = j;
          break;
        case 5: 
        case 4: 
        case 3: 
        case 2: 
        case 1: 
          try
          {
            if (str.equals("capture"))
            {
              mDeviceDir = 0;
              AlsaDevicesParser.access$202(AlsaDevicesParser.this, true);
              m = j;
            }
            else
            {
              m = j;
              if (str.equals("playback"))
              {
                mDeviceDir = 1;
                AlsaDevicesParser.access$302(AlsaDevicesParser.this, true);
                m = j;
              }
            }
          }
          catch (NumberFormatException paramString)
          {
            paramString = new StringBuilder();
            paramString.append("Failed to parse token ");
            paramString.append(j);
            paramString.append(" of ");
            paramString.append("/proc/asound/devices");
            paramString.append(" token: ");
            paramString.append(str);
            Slog.e("AlsaDevicesParser", paramString.toString());
            return false;
          }
          if (str.equals("audio"))
          {
            mDeviceType = 0;
            m = j;
          }
          else
          {
            m = j;
            if (str.equals("midi"))
            {
              mDeviceType = 2;
              AlsaDevicesParser.access$102(AlsaDevicesParser.this, true);
              m = j;
              continue;
              if (str.equals("digital"))
              {
                m = j;
              }
              else if (str.equals("control"))
              {
                mDeviceType = 1;
                m = j;
              }
              else
              {
                str.equals("raw");
                m = j;
                continue;
                mDeviceNum = Integer.parseInt(str);
                m = j;
                continue;
                mCardNum = Integer.parseInt(str);
                k = paramString.charAt(i);
                m = j;
                if (k != 45) {
                  m = j + 1;
                }
              }
            }
          }
          break;
        case 0: 
          m = j;
        }
      }
    }
    
    public String textFormat()
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      StringBuilder localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append("[");
      localStringBuilder2.append(mCardNum);
      localStringBuilder2.append(":");
      localStringBuilder2.append(mDeviceNum);
      localStringBuilder2.append("]");
      localStringBuilder1.append(localStringBuilder2.toString());
      switch (mDeviceType)
      {
      default: 
        localStringBuilder1.append(" N/A");
        break;
      case 2: 
        localStringBuilder1.append(" MIDI");
        break;
      case 1: 
        localStringBuilder1.append(" Control");
        break;
      case 0: 
        localStringBuilder1.append(" Audio");
      }
      switch (mDeviceDir)
      {
      default: 
        localStringBuilder1.append(" N/A");
        break;
      case 1: 
        localStringBuilder1.append(" Playback");
        break;
      case 0: 
        localStringBuilder1.append(" Capture");
      }
      return localStringBuilder1.toString();
    }
  }
}
