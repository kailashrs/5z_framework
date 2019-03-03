package com.android.internal.alsa;

import android.util.Slog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;

public class AlsaCardsParser
{
  protected static final boolean DEBUG = false;
  public static final int SCANSTATUS_EMPTY = 2;
  public static final int SCANSTATUS_FAIL = 1;
  public static final int SCANSTATUS_NOTSCANNED = -1;
  public static final int SCANSTATUS_SUCCESS = 0;
  private static final String TAG = "AlsaCardsParser";
  private static final String kAlsaFolderPath = "/proc/asound";
  private static final String kCardsFilePath = "/proc/asound/cards";
  private static final String kDeviceAddressPrefix = "/dev/bus/usb/";
  private static LineTokenizer mTokenizer = new LineTokenizer(" :[]");
  private ArrayList<AlsaCardRecord> mCardRecords = new ArrayList();
  private int mScanStatus = -1;
  
  public AlsaCardsParser() {}
  
  private void Log(String paramString) {}
  
  public AlsaCardRecord findCardNumFor(String paramString)
  {
    Iterator localIterator = mCardRecords.iterator();
    while (localIterator.hasNext())
    {
      AlsaCardRecord localAlsaCardRecord = (AlsaCardRecord)localIterator.next();
      if ((localAlsaCardRecord.isUsb()) && (mUsbDeviceAddress.equals(paramString))) {
        return localAlsaCardRecord;
      }
    }
    return null;
  }
  
  public int getScanStatus()
  {
    return mScanStatus;
  }
  
  public int scan()
  {
    mCardRecords = new ArrayList();
    Object localObject1 = new File("/proc/asound/cards");
    try
    {
      FileReader localFileReader = new java/io/FileReader;
      localFileReader.<init>((File)localObject1);
      localObject1 = new java/io/BufferedReader;
      ((BufferedReader)localObject1).<init>(localFileReader);
      for (;;)
      {
        Object localObject2 = ((BufferedReader)localObject1).readLine();
        if (localObject2 == null) {
          break;
        }
        AlsaCardRecord localAlsaCardRecord = new com/android/internal/alsa/AlsaCardsParser$AlsaCardRecord;
        localAlsaCardRecord.<init>(this);
        localAlsaCardRecord.parse((String)localObject2, 0);
        localObject2 = ((BufferedReader)localObject1).readLine();
        if (localObject2 == null) {
          break;
        }
        localAlsaCardRecord.parse((String)localObject2, 1);
        int i = mCardNum;
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("/proc/asound/card");
        ((StringBuilder)localObject2).append(i);
        Object localObject3 = ((StringBuilder)localObject2).toString();
        Object localObject4 = new java/io/File;
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append((String)localObject3);
        ((StringBuilder)localObject2).append("/usbbus");
        ((File)localObject4).<init>(((StringBuilder)localObject2).toString());
        if (((File)localObject4).exists())
        {
          localObject2 = new java/io/FileReader;
          ((FileReader)localObject2).<init>((File)localObject4);
          localObject4 = new java/io/BufferedReader;
          ((BufferedReader)localObject4).<init>((Reader)localObject2);
          localObject4 = ((BufferedReader)localObject4).readLine();
          if (localObject4 != null)
          {
            localObject3 = new java/lang/StringBuilder;
            ((StringBuilder)localObject3).<init>();
            ((StringBuilder)localObject3).append("/dev/bus/usb/");
            ((StringBuilder)localObject3).append((String)localObject4);
            localAlsaCardRecord.setDeviceAddress(((StringBuilder)localObject3).toString());
          }
          ((FileReader)localObject2).close();
        }
        mCardRecords.add(localAlsaCardRecord);
      }
      localFileReader.close();
      if (mCardRecords.size() > 0) {
        mScanStatus = 0;
      } else {
        mScanStatus = 2;
      }
    }
    catch (IOException localIOException)
    {
      mScanStatus = 1;
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      mScanStatus = 1;
    }
    return mScanStatus;
  }
  
  public class AlsaCardRecord
  {
    private static final String TAG = "AlsaCardRecord";
    private static final String kUsbCardKeyStr = "at usb-";
    String mCardDescription = "";
    String mCardName = "";
    int mCardNum = -1;
    String mField1 = "";
    private String mUsbDeviceAddress = null;
    
    public AlsaCardRecord() {}
    
    private boolean parse(String paramString, int paramInt)
    {
      int i = 0;
      int j;
      if (paramInt == 0)
      {
        j = AlsaCardsParser.mTokenizer.nextToken(paramString, 0);
        i = AlsaCardsParser.mTokenizer.nextDelimiter(paramString, j);
        try
        {
          mCardNum = Integer.parseInt(paramString.substring(j, i));
          paramInt = AlsaCardsParser.mTokenizer.nextToken(paramString, i);
          i = AlsaCardsParser.mTokenizer.nextDelimiter(paramString, paramInt);
          mField1 = paramString.substring(paramInt, i);
          mCardName = paramString.substring(AlsaCardsParser.mTokenizer.nextToken(paramString, i));
        }
        catch (NumberFormatException localNumberFormatException)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Failed to parse line ");
          localStringBuilder.append(paramInt);
          localStringBuilder.append(" of ");
          localStringBuilder.append("/proc/asound/cards");
          localStringBuilder.append(": ");
          localStringBuilder.append(paramString.substring(j, i));
          Slog.e("AlsaCardRecord", localStringBuilder.toString());
          return false;
        }
      }
      else if (paramInt == 1)
      {
        int k = AlsaCardsParser.mTokenizer.nextToken(paramString, 0);
        if (k != -1)
        {
          j = paramString.indexOf("at usb-");
          paramInt = i;
          if (j != -1) {
            paramInt = 1;
          }
          if (paramInt != 0) {
            mCardDescription = paramString.substring(k, j - 1);
          }
        }
      }
      return true;
    }
    
    public String getCardDescription()
    {
      return mCardDescription;
    }
    
    public String getCardName()
    {
      return mCardName;
    }
    
    public int getCardNum()
    {
      return mCardNum;
    }
    
    boolean isUsb()
    {
      boolean bool;
      if (mUsbDeviceAddress != null) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void log(int paramInt)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(" [");
      localStringBuilder.append(mCardNum);
      localStringBuilder.append(" ");
      localStringBuilder.append(mCardName);
      localStringBuilder.append(" : ");
      localStringBuilder.append(mCardDescription);
      localStringBuilder.append(" usb:");
      localStringBuilder.append(isUsb());
      Slog.d("AlsaCardRecord", localStringBuilder.toString());
    }
    
    public void setDeviceAddress(String paramString)
    {
      mUsbDeviceAddress = paramString;
    }
    
    public String textFormat()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(mCardName);
      localStringBuilder.append(" : ");
      localStringBuilder.append(mCardDescription);
      localStringBuilder.append(" [addr:");
      localStringBuilder.append(mUsbDeviceAddress);
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
  }
}
