package com.android.internal.telephony.gsm;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.telephony.Rlog;
import android.util.SparseArray;
import android.util.SparseIntArray;
import com.android.internal.telephony.uicc.AdnRecord;
import com.android.internal.telephony.uicc.AdnRecordCache;
import com.android.internal.telephony.uicc.IccConstants;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UsimPhoneBookManager
  extends Handler
  implements IccConstants
{
  private static final boolean DBG = true;
  static final int EVENT_EF_GETSIZE_DONE = 12;
  private static final int EVENT_EMAIL_LOAD_DONE = 4;
  private static final int EVENT_IAP_LOAD_DONE = 3;
  private static final int EVENT_PBR_LOAD_DONE = 1;
  private static final int EVENT_USIM_ADN_LOAD_DONE = 2;
  private static final byte INVALID_BYTE = -1;
  private static final int INVALID_SFI = -1;
  private static final String LOG_TAG = "UsimPhoneBookManager";
  private static final int TYPE_ANR = 103;
  private static final int TYPE_Email = 101;
  private static final int TYPE_IAP = 104;
  private static final int TYPE_SNE = 102;
  private static final int USIM_EFAAS_TAG = 199;
  private static final int USIM_EFADN_TAG = 192;
  private static final int USIM_EFANR_TAG = 196;
  private static final int USIM_EFCCP1_TAG = 203;
  private static final int USIM_EFEMAIL_TAG = 202;
  private static final int USIM_EFEXT1_TAG = 194;
  private static final int USIM_EFGRP_TAG = 198;
  private static final int USIM_EFGSD_TAG = 200;
  private static final int USIM_EFIAP_TAG = 193;
  private static final int USIM_EFPBC_TAG = 197;
  private static final int USIM_EFSNE_TAG = 195;
  private static final int USIM_EFUID_TAG = 201;
  private static final int USIM_TYPE1_TAG = 168;
  private static final int USIM_TYPE2_TAG = 169;
  private static final int USIM_TYPE3_TAG = 170;
  private AdnRecordCache mAdnCache;
  private ArrayList<byte[]> mEmailFileRecord;
  private SparseArray<ArrayList<String>> mEmailsForAdnRec;
  private IccFileHandler mFh;
  private ArrayList<byte[]> mIapFileRecord;
  private Boolean mIsPbrPresent;
  private Object mLock = new Object();
  private ArrayList<PbrRecord> mPbrRecords;
  private ArrayList<AdnRecord> mPhoneBookRecords;
  private Map<Integer, int[]> mRecordsInfo;
  private int[] mRecordsInfoTemp;
  private boolean mRefreshCache = false;
  private SparseIntArray mSfiEfidTable;
  private int[] recordSize;
  
  public UsimPhoneBookManager(IccFileHandler paramIccFileHandler, AdnRecordCache paramAdnRecordCache)
  {
    mFh = paramIccFileHandler;
    mPhoneBookRecords = new ArrayList();
    mPbrRecords = null;
    mRecordsInfo = new HashMap();
    mIsPbrPresent = Boolean.valueOf(true);
    mAdnCache = paramAdnRecordCache;
    mEmailsForAdnRec = new SparseArray();
    mSfiEfidTable = new SparseIntArray();
  }
  
  private void buildType1EmailList(int paramInt)
  {
    if (mPbrRecords.get(paramInt) == null) {
      return;
    }
    int i = mPbrRecords.get(paramInt)).mMasterFileRecordNum;
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("Building type 1 email list. recId = ");
    ((StringBuilder)localObject1).append(paramInt);
    ((StringBuilder)localObject1).append(", numRecs = ");
    ((StringBuilder)localObject1).append(i);
    log(((StringBuilder)localObject1).toString());
    int j = 0;
    while (j < i) {
      try
      {
        localObject1 = (byte[])mEmailFileRecord.get(j);
        int k = localObject1[(localObject1.length - 2)];
        int m = localObject1[(localObject1.length - 1)];
        String str = readEmailRecord(j);
        if ((str != null) && (!str.equals("")))
        {
          if ((k != -1) && (mSfiEfidTable.get(k) != 0))
          {
            k = mSfiEfidTable.get(k);
          }
          else
          {
            localObject1 = (File)mPbrRecords.get(paramInt)).mFileIds.get(192);
            if (localObject1 == null) {
              break label327;
            }
            k = ((File)localObject1).getEfid();
          }
          k = (0xFFFF & k) << 8 | m - 1 & 0xFF;
          Object localObject2 = (ArrayList)mEmailsForAdnRec.get(k);
          localObject1 = localObject2;
          if (localObject2 == null) {
            localObject1 = new ArrayList();
          }
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("Adding email #");
          ((StringBuilder)localObject2).append(j);
          ((StringBuilder)localObject2).append(" list to index 0x");
          ((StringBuilder)localObject2).append(Integer.toHexString(k).toUpperCase());
          log(((StringBuilder)localObject2).toString());
          ((ArrayList)localObject1).add(str);
          mEmailsForAdnRec.put(k, localObject1);
        }
        label327:
        j++;
      }
      catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
      {
        Rlog.e("UsimPhoneBookManager", "Error: Improper ICC card: No email record for ADN, continuing");
      }
    }
  }
  
  private boolean buildType2EmailList(int paramInt)
  {
    Object localObject1 = mPbrRecords.get(paramInt);
    int i = 0;
    if (localObject1 == null) {
      return false;
    }
    int j = mPbrRecords.get(paramInt)).mMasterFileRecordNum;
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("Building type 2 email list. recId = ");
    ((StringBuilder)localObject1).append(paramInt);
    ((StringBuilder)localObject1).append(", numRecs = ");
    ((StringBuilder)localObject1).append(j);
    log(((StringBuilder)localObject1).toString());
    localObject1 = (File)mPbrRecords.get(paramInt)).mFileIds.get(192);
    if (localObject1 == null)
    {
      Rlog.e("UsimPhoneBookManager", "Error: Improper ICC card: EF_ADN does not exist in PBR files");
      return false;
    }
    int k = ((File)localObject1).getEfid();
    while (i < j)
    {
      try
      {
        int m = ((byte[])mIapFileRecord.get(i))[((File)mPbrRecords.get(paramInt)).mFileIds.get(202)).getIndex()];
        String str = readEmailRecord(m - 1);
        if ((str != null) && (!str.equals("")))
        {
          m = (0xFFFF & k) << 8 | i & 0xFF;
          Object localObject2 = (ArrayList)mEmailsForAdnRec.get(m);
          localObject1 = localObject2;
          if (localObject2 == null) {
            localObject1 = new ArrayList();
          }
          ((ArrayList)localObject1).add(str);
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("Adding email list to index 0x");
          ((StringBuilder)localObject2).append(Integer.toHexString(m).toUpperCase());
          log(((StringBuilder)localObject2).toString());
          mEmailsForAdnRec.put(m, localObject1);
        }
      }
      catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
      {
        Rlog.e("UsimPhoneBookManager", "Error: Improper ICC card: Corrupted EF_IAP");
      }
      i++;
    }
    return true;
  }
  
  private void createPbrFile(ArrayList<byte[]> paramArrayList)
  {
    if (paramArrayList == null)
    {
      mPbrRecords = null;
      mIsPbrPresent = Boolean.valueOf(false);
      return;
    }
    mPbrRecords = new ArrayList();
    for (int i = 0; i < paramArrayList.size(); i++) {
      if (((byte[])paramArrayList.get(i))[0] != -1) {
        mPbrRecords.add(new PbrRecord((byte[])paramArrayList.get(i)));
      }
    }
    Iterator localIterator = mPbrRecords.iterator();
    while (localIterator.hasNext())
    {
      paramArrayList = (PbrRecord)localIterator.next();
      File localFile = (File)mFileIds.get(192);
      if (localFile != null)
      {
        i = localFile.getSfi();
        if (i != -1) {
          mSfiEfidTable.put(i, ((File)mFileIds.get(192)).getEfid());
        }
      }
    }
  }
  
  private void getRecordInfoandwait(int paramInt)
  {
    mFh.getEFLinearRecordSize(paramInt, obtainMessage(12));
    try
    {
      mLock.wait();
    }
    catch (InterruptedException localInterruptedException)
    {
      Rlog.e("UsimPhoneBookManager", "Interrupted Exception in getRecordInfoandwait");
    }
  }
  
  private void log(String paramString)
  {
    Rlog.d("UsimPhoneBookManager", paramString);
  }
  
  private void readAdnFileAndWait(int paramInt)
  {
    SparseArray localSparseArray = mPbrRecords.get(paramInt)).mFileIds;
    if ((localSparseArray != null) && (localSparseArray.size() != 0))
    {
      int i = 0;
      if (localSparseArray.get(194) != null) {
        i = ((File)localSparseArray.get(194)).getEfid();
      }
      if (localSparseArray.get(192) == null) {
        return;
      }
      int j = mPhoneBookRecords.size();
      mAdnCache.requestLoadAllAdnLike(((File)localSparseArray.get(192)).getEfid(), i, obtainMessage(2));
      try
      {
        mLock.wait();
      }
      catch (InterruptedException localInterruptedException)
      {
        Rlog.e("UsimPhoneBookManager", "Interrupted Exception in readAdnFileAndWait");
      }
      PbrRecord.access$102((PbrRecord)mPbrRecords.get(paramInt), mPhoneBookRecords.size() - j);
      return;
    }
  }
  
  private void readEmailFileAndWait(int paramInt)
  {
    Object localObject = mPbrRecords.get(paramInt)).mFileIds;
    if (localObject == null) {
      return;
    }
    File localFile = (File)((SparseArray)localObject).get(202);
    if (localFile != null)
    {
      if (localFile.getParentTag() == 169)
      {
        if (((SparseArray)localObject).get(193) == null)
        {
          Rlog.e("UsimPhoneBookManager", "Can't locate EF_IAP in EF_PBR.");
          return;
        }
        log("EF_IAP exists. Loading EF_IAP to retrieve the index.");
        readIapFileAndWait(((File)((SparseArray)localObject).get(193)).getEfid());
        if (mIapFileRecord == null)
        {
          Rlog.e("UsimPhoneBookManager", "Error: IAP file is empty");
          return;
        }
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("EF_EMAIL order in PBR record: ");
        ((StringBuilder)localObject).append(localFile.getIndex());
        log(((StringBuilder)localObject).toString());
      }
      int i = localFile.getEfid();
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("EF_EMAIL exists in PBR. efid = 0x");
      ((StringBuilder)localObject).append(Integer.toHexString(i).toUpperCase());
      log(((StringBuilder)localObject).toString());
      for (int j = 0; j < paramInt; j++) {
        if (mPbrRecords.get(j) != null)
        {
          localObject = mPbrRecords.get(j)).mFileIds;
          if (localObject != null)
          {
            localObject = (File)((SparseArray)localObject).get(202);
            if ((localObject != null) && (((File)localObject).getEfid() == i))
            {
              log("Skipped this EF_EMAIL which was loaded earlier");
              return;
            }
          }
        }
      }
      mFh.loadEFLinearFixedAll(i, obtainMessage(4));
      try
      {
        mLock.wait();
      }
      catch (InterruptedException localInterruptedException)
      {
        Rlog.e("UsimPhoneBookManager", "Interrupted Exception in readEmailFileAndWait");
      }
      if (mEmailFileRecord == null)
      {
        Rlog.e("UsimPhoneBookManager", "Error: Email file is empty");
        return;
      }
      if ((localFile.getParentTag() == 169) && (mIapFileRecord != null)) {
        buildType2EmailList(paramInt);
      } else {
        buildType1EmailList(paramInt);
      }
    }
  }
  
  private String readEmailRecord(int paramInt)
  {
    try
    {
      byte[] arrayOfByte = (byte[])mEmailFileRecord.get(paramInt);
      return IccUtils.adnStringFieldToString(arrayOfByte, 0, arrayOfByte.length - 2);
    }
    catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {}
    return null;
  }
  
  private void readIapFileAndWait(int paramInt)
  {
    mFh.loadEFLinearFixedAll(paramInt, obtainMessage(3));
    try
    {
      mLock.wait();
    }
    catch (InterruptedException localInterruptedException)
    {
      Rlog.e("UsimPhoneBookManager", "Interrupted Exception in readIapFileAndWait");
    }
  }
  
  private void readPbrFileAndWait()
  {
    mFh.loadEFLinearFixedAll(20272, obtainMessage(1));
    try
    {
      mLock.wait();
    }
    catch (InterruptedException localInterruptedException)
    {
      Rlog.e("UsimPhoneBookManager", "Interrupted Exception in readAdnFileAndWait");
    }
  }
  
  private void refreshCache()
  {
    if (mPbrRecords == null) {
      return;
    }
    mPhoneBookRecords.clear();
    int i = mPbrRecords.size();
    for (int j = 0; j < i; j++) {
      readAdnFileAndWait(j);
    }
  }
  
  private void updatePhoneAdnRecord()
  {
    int i = mPhoneBookRecords.size();
    for (int j = 0; j < i; j++)
    {
      AdnRecord localAdnRecord = (AdnRecord)mPhoneBookRecords.get(j);
      int k = localAdnRecord.getEfid();
      int m = localAdnRecord.getRecId();
      try
      {
        Object localObject = (ArrayList)mEmailsForAdnRec.get((0xFFFF & k) << 8 | m - 1 & 0xFF);
        if (localObject != null)
        {
          String[] arrayOfString = new String[((ArrayList)localObject).size()];
          System.arraycopy(((ArrayList)localObject).toArray(), 0, arrayOfString, 0, ((ArrayList)localObject).size());
          localAdnRecord.setEmails(arrayOfString);
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Adding email list to ADN (0x");
          ((StringBuilder)localObject).append(Integer.toHexString(((AdnRecord)mPhoneBookRecords.get(j)).getEfid()).toUpperCase());
          ((StringBuilder)localObject).append(") record #");
          ((StringBuilder)localObject).append(((AdnRecord)mPhoneBookRecords.get(j)).getRecId());
          log(((StringBuilder)localObject).toString());
          mPhoneBookRecords.set(j, localAdnRecord);
        }
      }
      catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {}
    }
  }
  
  public boolean CheckCoUseEFID(int paramInt1, int paramInt2, int paramInt3)
  {
    for (int i = 0; i < paramInt1; i++)
    {
      SparseArray localSparseArray = mPbrRecords.get(i)).mFileIds;
      if ((localSparseArray != null) && (localSparseArray.size() != 0) && (localSparseArray.get(paramInt2) != null) && (paramInt3 == ((File)localSparseArray.get(paramInt2)).getEfid()))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Efid ");
        localStringBuilder.append(Integer.toHexString(paramInt3));
        localStringBuilder.append(", fileId = ");
        localStringBuilder.append(Integer.toHexString(((File)localSparseArray.get(paramInt2)).getEfid()));
        log(localStringBuilder.toString());
        return true;
      }
    }
    return false;
  }
  
  public int FindEFOffsetPrePBR(int paramInt1, int paramInt2)
  {
    SparseArray localSparseArray = mPbrRecords.get(paramInt1)).mFileIds;
    if ((localSparseArray != null) && (localSparseArray.size() != 0))
    {
      if ((localSparseArray.get(paramInt2) != null) && (mRecordsInfo.containsKey(Integer.valueOf(((File)localSparseArray.get(paramInt2)).getEfid())))) {
        return ((int[])mRecordsInfo.get(Integer.valueOf(((File)localSparseArray.get(paramInt2)).getEfid())))[2] & 0xFF;
      }
      return -1;
    }
    return -1;
  }
  
  public int FindRealADNIndexbyIndex(int paramInt)
  {
    int i = 0;
    for (int j = 0; j < mPbrRecords.size(); j++)
    {
      i += FindEFOffsetPrePBR(j, 192);
      if (paramInt <= i) {
        return paramInt - (i - FindEFOffsetPrePBR(j, 192));
      }
    }
    return -1;
  }
  
  public int FindwhichPBRbyEfid(int paramInt1, int paramInt2)
  {
    for (int i = 0; i < mPbrRecords.size(); i++)
    {
      SparseArray localSparseArray = mPbrRecords.get(i)).mFileIds;
      if ((localSparseArray != null) && (localSparseArray.size() != 0) && (localSparseArray.get(paramInt2) != null) && (paramInt1 == ((File)localSparseArray.get(paramInt2)).getEfid())) {
        return i;
      }
    }
    return -1;
  }
  
  public int FindwhichPBRbyIndex(int paramInt1, int paramInt2)
  {
    int i = 0;
    for (int j = 0; j < mPbrRecords.size(); j++)
    {
      i += FindEFOffsetPrePBR(j, paramInt2);
      if (paramInt1 <= i) {
        return j;
      }
    }
    return -1;
  }
  
  public int MaptheTapType(int paramInt)
  {
    int i = -1;
    switch (paramInt)
    {
    default: 
      paramInt = i;
      break;
    case 104: 
      paramInt = 193;
      break;
    case 103: 
      paramInt = 196;
      break;
    case 102: 
      paramInt = 195;
      break;
    case 101: 
      paramInt = 202;
    }
    return paramInt;
  }
  
  public int TheOffsetbyCacheADNIndex(int paramInt1, int paramInt2)
  {
    int i = 0;
    for (int j = 0; j < FindwhichPBRbyIndex(paramInt1, 192); j++) {
      i += FindEFOffsetPrePBR(j, paramInt2);
    }
    return i;
  }
  
  public int TheOffsetbyCacheTypeIndex(int paramInt1, int paramInt2)
  {
    int i = 0;
    for (int j = 0; j < FindwhichPBRbyIndex(paramInt1, paramInt2); j++) {
      i += FindEFOffsetPrePBR(j, paramInt2);
    }
    return i;
  }
  
  public int[] getUSIMPhoneBookProperty()
  {
    recordSize = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    synchronized (mLock)
    {
      if (mPbrRecords == null)
      {
        log("[APB] read PBR and wait into getUSIMPhoneBookProperty().");
        readPbrFileAndWait();
      }
      if (mPbrRecords == null) {
        return null;
      }
      int i = mPbrRecords.size();
      ??? = new StringBuilder();
      ((StringBuilder)???).append("[APB] numRecs = ");
      ((StringBuilder)???).append(i);
      log(((StringBuilder)???).toString());
      for (int j = 0; j < i; j++) {
        readAllRecordsInfoOfPrePBR(j);
      }
      j = 0;
      try
      {
        while (j < mPbrRecords.size())
        {
          ??? = mPbrRecords.get(j)).mFileIds;
          if ((??? != null) && (((SparseArray)???).size() != 0))
          {
            int[] arrayOfInt;
            if ((((SparseArray)???).get(192) != null) && (!CheckCoUseEFID(j, 202, ((File)((SparseArray)???).get(192)).getEfid())))
            {
              arrayOfInt = recordSize;
              arrayOfInt[1] += ((int[])mRecordsInfo.get(Integer.valueOf(((File)???.get(192)).getEfid())))[2];
              recordSize[5] = (((int[])mRecordsInfo.get(Integer.valueOf(((File)???.get(192)).getEfid())))[0] - 14);
            }
            recordSize[6] = 10;
            if ((((SparseArray)???).get(202) != null) && (!CheckCoUseEFID(j, 202, ((File)((SparseArray)???).get(202)).getEfid())))
            {
              arrayOfInt = recordSize;
              arrayOfInt[2] += ((int[])mRecordsInfo.get(Integer.valueOf(((File)???.get(202)).getEfid())))[2];
              recordSize[7] = (((int[])mRecordsInfo.get(Integer.valueOf(((File)???.get(202)).getEfid())))[0] - 2);
            }
            if ((((SparseArray)???).get(196) != null) && (!CheckCoUseEFID(j, 196, ((File)((SparseArray)???).get(196)).getEfid())))
            {
              arrayOfInt = recordSize;
              arrayOfInt[3] += ((int[])mRecordsInfo.get(Integer.valueOf(((File)???.get(196)).getEfid())))[2];
              recordSize[8] = 10;
            }
            if ((((SparseArray)???).get(195) != null) && (!CheckCoUseEFID(j, 195, ((File)((SparseArray)???).get(195)).getEfid())))
            {
              arrayOfInt = recordSize;
              arrayOfInt[4] += ((int[])mRecordsInfo.get(Integer.valueOf(((File)???.get(195)).getEfid())))[2];
              recordSize[9] = (((int[])mRecordsInfo.get(Integer.valueOf(((File)???.get(195)).getEfid())))[0] - 2);
            }
          }
          else
          {
            ??? = new java/lang/StringBuilder;
            ((StringBuilder)???).<init>();
            ((StringBuilder)???).append("[APB] getUSIMPhoneBookProperty fileIds[");
            ((StringBuilder)???).append(j);
            ((StringBuilder)???).append("]=null, continue");
            log(((StringBuilder)???).toString());
          }
          j++;
        }
        recordSize[10] = 1;
      }
      catch (Exception localException)
      {
        Rlog.e("UsimPhoneBookManager", "getUSIMPhoneBookProperty error", localException);
      }
      return recordSize;
    }
  }
  
  public void handleMessage(Message arg1)
  {
    int i = what;
    if (i != 12)
    {
      switch (i)
      {
      default: 
        break;
      case 4: 
        log("Loading USIM Email records done");
        ??? = (AsyncResult)obj;
        if (exception == null) {
          mEmailFileRecord = ((ArrayList)result);
        }
        synchronized (mLock)
        {
          mLock.notify();
        }
      case 3: 
        log("Loading USIM IAP records done");
        ??? = (AsyncResult)obj;
        if (exception == null) {
          mIapFileRecord = ((ArrayList)result);
        }
        synchronized (mLock)
        {
          mLock.notify();
        }
      case 2: 
        log("Loading USIM ADN records done");
        ??? = (AsyncResult)obj;
        if (exception == null) {
          mPhoneBookRecords.addAll((ArrayList)result);
        }
        synchronized (mLock)
        {
          mLock.notify();
        }
      case 1: 
        log("Loading PBR records done");
        ??? = (AsyncResult)obj;
        if (exception == null) {
          createPbrFile((ArrayList)result);
        }
        synchronized (mLock)
        {
          mLock.notify();
        }
      }
    }
    else
    {
      log("EVENT_EF_GETSIZE_DONE");
      ??? = (AsyncResult)obj;
      if (exception == null) {
        mRecordsInfoTemp = ((int[])result);
      }
    }
    synchronized (mLock)
    {
      mLock.notify();
      return;
    }
  }
  
  public void invalidateCache()
  {
    mRefreshCache = true;
  }
  
  public ArrayList<AdnRecord> loadEfFilesFromUsim()
  {
    synchronized (mLock)
    {
      boolean bool = mPhoneBookRecords.isEmpty();
      int i = 0;
      if (!bool)
      {
        if (mRefreshCache)
        {
          mRefreshCache = false;
          refreshCache();
        }
        ArrayList localArrayList = mPhoneBookRecords;
        return localArrayList;
      }
      if (!mIsPbrPresent.booleanValue()) {
        return null;
      }
      if (mPbrRecords == null) {
        readPbrFileAndWait();
      }
      if (mPbrRecords == null) {
        return null;
      }
      int j = mPbrRecords.size();
      log("loadEfFilesFromUsim: Loading adn and emails");
      while (i < j)
      {
        readAdnFileAndWait(i);
        readEmailFileAndWait(i);
        i++;
      }
      updatePhoneAdnRecord();
      return mPhoneBookRecords;
    }
  }
  
  void readAllRecordsInfoOfPrePBR(int paramInt)
  {
    if (mPbrRecords == null) {
      return;
    }
    Object localObject1 = mPbrRecords.get(paramInt)).mFileIds;
    if ((localObject1 != null) && (((SparseArray)localObject1).size() != 0)) {
      synchronized (mLock)
      {
        int i;
        StringBuilder localStringBuilder;
        if (((SparseArray)localObject1).get(192) != null)
        {
          i = ((File)((SparseArray)localObject1).get(192)).getEfid();
          if (!mRecordsInfo.containsKey(Integer.valueOf(i)))
          {
            getRecordInfoandwait(i);
            mRecordsInfo.put(Integer.valueOf(i), mRecordsInfoTemp);
          }
          localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("ADN Efid = ");
          localStringBuilder.append(Integer.toHexString(i));
          localStringBuilder.append(":");
          localStringBuilder.append(((int[])mRecordsInfo.get(Integer.valueOf(i)))[0]);
          localStringBuilder.append(",");
          localStringBuilder.append(((int[])mRecordsInfo.get(Integer.valueOf(i)))[1]);
          localStringBuilder.append(",");
          localStringBuilder.append(((int[])mRecordsInfo.get(Integer.valueOf(i)))[2]);
          log(localStringBuilder.toString());
        }
        if (((SparseArray)localObject1).get(193) != null)
        {
          i = ((File)((SparseArray)localObject1).get(193)).getEfid();
          if (!mRecordsInfo.containsKey(Integer.valueOf(i)))
          {
            getRecordInfoandwait(i);
            mRecordsInfo.put(Integer.valueOf(i), mRecordsInfoTemp);
          }
          localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("IAP Efid = ");
          localStringBuilder.append(Integer.toHexString(i));
          localStringBuilder.append(":");
          localStringBuilder.append(((int[])mRecordsInfo.get(Integer.valueOf(i)))[0]);
          localStringBuilder.append(",");
          localStringBuilder.append(((int[])mRecordsInfo.get(Integer.valueOf(i)))[1]);
          localStringBuilder.append(",");
          localStringBuilder.append(((int[])mRecordsInfo.get(Integer.valueOf(i)))[2]);
          log(localStringBuilder.toString());
        }
        if (((SparseArray)localObject1).get(202) != null)
        {
          i = ((File)((SparseArray)localObject1).get(202)).getEfid();
          if (!mRecordsInfo.containsKey(Integer.valueOf(i)))
          {
            getRecordInfoandwait(i);
            mRecordsInfo.put(Integer.valueOf(i), mRecordsInfoTemp);
          }
          localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Email Efid = ");
          localStringBuilder.append(Integer.toHexString(i));
          localStringBuilder.append(":");
          localStringBuilder.append(((int[])mRecordsInfo.get(Integer.valueOf(i)))[0]);
          localStringBuilder.append(",");
          localStringBuilder.append(((int[])mRecordsInfo.get(Integer.valueOf(i)))[1]);
          localStringBuilder.append(",");
          localStringBuilder.append(((int[])mRecordsInfo.get(Integer.valueOf(i)))[2]);
          log(localStringBuilder.toString());
        }
        if (((SparseArray)localObject1).get(196) != null)
        {
          i = ((File)((SparseArray)localObject1).get(196)).getEfid();
          if (!mRecordsInfo.containsKey(Integer.valueOf(i)))
          {
            getRecordInfoandwait(i);
            mRecordsInfo.put(Integer.valueOf(i), mRecordsInfoTemp);
          }
          localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("ANR Efid = ");
          localStringBuilder.append(Integer.toHexString(i));
          localStringBuilder.append(":");
          localStringBuilder.append(((int[])mRecordsInfo.get(Integer.valueOf(i)))[0]);
          localStringBuilder.append(",");
          localStringBuilder.append(((int[])mRecordsInfo.get(Integer.valueOf(i)))[1]);
          localStringBuilder.append(",");
          localStringBuilder.append(((int[])mRecordsInfo.get(Integer.valueOf(i)))[2]);
          log(localStringBuilder.toString());
        }
        if (((SparseArray)localObject1).get(195) != null)
        {
          i = ((File)((SparseArray)localObject1).get(195)).getEfid();
          if (!mRecordsInfo.containsKey(Integer.valueOf(i)))
          {
            getRecordInfoandwait(i);
            mRecordsInfo.put(Integer.valueOf(i), mRecordsInfoTemp);
          }
          localObject1 = new java/lang/StringBuilder;
          ((StringBuilder)localObject1).<init>();
          ((StringBuilder)localObject1).append("SNE Efid = ");
          ((StringBuilder)localObject1).append(Integer.toHexString(i));
          ((StringBuilder)localObject1).append(":");
          ((StringBuilder)localObject1).append(((int[])mRecordsInfo.get(Integer.valueOf(i)))[0]);
          ((StringBuilder)localObject1).append(",");
          ((StringBuilder)localObject1).append(((int[])mRecordsInfo.get(Integer.valueOf(i)))[1]);
          ((StringBuilder)localObject1).append(",");
          ((StringBuilder)localObject1).append(((int[])mRecordsInfo.get(Integer.valueOf(i)))[2]);
          log(((StringBuilder)localObject1).toString());
        }
        ??? = new StringBuilder();
        ((StringBuilder)???).append("PBR:");
        ((StringBuilder)???).append(paramInt);
        ((StringBuilder)???).append(";mRecordsInfo.size = ");
        ((StringBuilder)???).append(mRecordsInfo.size());
        log(((StringBuilder)???).toString());
        return;
      }
    }
  }
  
  public void reset()
  {
    mPhoneBookRecords.clear();
    mIapFileRecord = null;
    mEmailFileRecord = null;
    mPbrRecords = null;
    mRecordsInfo.clear();
    mIsPbrPresent = Boolean.valueOf(true);
    mRefreshCache = false;
    mEmailsForAdnRec.clear();
    mSfiEfidTable.clear();
  }
  
  private class File
  {
    private final int mEfid;
    private final int mIndex;
    private final int mParentTag;
    private final int mSfi;
    
    File(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      mParentTag = paramInt1;
      mEfid = paramInt2;
      mSfi = paramInt3;
      mIndex = paramInt4;
    }
    
    public int getEfid()
    {
      return mEfid;
    }
    
    public int getIndex()
    {
      return mIndex;
    }
    
    public int getParentTag()
    {
      return mParentTag;
    }
    
    public int getSfi()
    {
      return mSfi;
    }
  }
  
  private class PbrRecord
  {
    private SparseArray<UsimPhoneBookManager.File> mFileIds = new SparseArray();
    private int mMasterFileRecordNum;
    
    PbrRecord(byte[] paramArrayOfByte)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("PBR rec: ");
      localStringBuilder.append(IccUtils.bytesToHexString(paramArrayOfByte));
      UsimPhoneBookManager.this.log(localStringBuilder.toString());
      parseTag(new SimTlv(paramArrayOfByte, 0, paramArrayOfByte.length));
    }
    
    void parseEfAndSFI(SimTlv paramSimTlv, int paramInt)
    {
      int i = 0;
      do
      {
        int j = paramSimTlv.getTag();
        switch (j)
        {
        default: 
          break;
        case 192: 
        case 193: 
        case 194: 
        case 195: 
        case 196: 
        case 197: 
        case 198: 
        case 199: 
        case 200: 
        case 201: 
        case 202: 
        case 203: 
          int k = -1;
          byte[] arrayOfByte = paramSimTlv.getData();
          if ((arrayOfByte.length >= 2) && (arrayOfByte.length <= 3))
          {
            if (arrayOfByte.length == 3) {
              k = arrayOfByte[2] & 0xFF;
            }
            int m = arrayOfByte[0];
            int n = arrayOfByte[1];
            mFileIds.put(j, new UsimPhoneBookManager.File(UsimPhoneBookManager.this, paramInt, (m & 0xFF) << 8 | n & 0xFF, k, i));
          }
          else
          {
            UsimPhoneBookManager localUsimPhoneBookManager = UsimPhoneBookManager.this;
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("Invalid TLV length: ");
            localStringBuilder.append(arrayOfByte.length);
            localUsimPhoneBookManager.log(localStringBuilder.toString());
          }
          break;
        }
        i++;
      } while (paramSimTlv.nextObject());
    }
    
    void parseTag(SimTlv paramSimTlv)
    {
      do
      {
        int i = paramSimTlv.getTag();
        switch (i)
        {
        default: 
          break;
        case 168: 
        case 169: 
        case 170: 
          byte[] arrayOfByte = paramSimTlv.getData();
          parseEfAndSFI(new SimTlv(arrayOfByte, 0, arrayOfByte.length), i);
        }
      } while (paramSimTlv.nextObject());
    }
  }
}
