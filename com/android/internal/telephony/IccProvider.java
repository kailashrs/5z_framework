package com.android.internal.telephony;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.telephony.Rlog;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.internal.telephony.uicc.AdnRecord;
import java.util.List;

public class IccProvider
  extends ContentProvider
{
  private static final String[] ADDRESS_BOOK_COLUMN_NAMES = { "name", "number", "emails", "_id", "secondname", "additionalnumber", "recordNumber" };
  protected static final int ADN = 1;
  protected static final int ADN_ALL = 7;
  public static final String ADN_ECODE_NAME_TOO_LONG = "1";
  public static final String ADN_ECODE_NONE = "0";
  public static final String ADN_ECODE_NUM_TOO_LONG = "2";
  protected static final int ADN_ID = 14;
  protected static final int ADN_READY = 12;
  protected static final int ADN_READY_SUB = 13;
  protected static final int ADN_SUB = 2;
  protected static final int ADN_SUB1_ID = 10;
  protected static final int ADN_SUB2_ID = 11;
  private static final boolean DBG = true;
  protected static final int FDN = 3;
  protected static final int FDN_SUB = 4;
  protected static final int PROP = 8;
  protected static final int PROP_SUB = 9;
  protected static final int SDN = 5;
  protected static final int SDN_SUB = 6;
  private static final String[] SIM_ADN_READY_NAMES;
  private static final String[] SIM_PROP_COLUMN_NAMES = { "sim_type", "total_number_sim", "total_number_email", "total_number_additionalnumber", "total_number_secondname", "length_name", "length_number", "length_email", "length_additionalnumber", "length_secondname", "editmode" };
  public static final String STR_ADDITIONALNUMBER = "additionalnumber";
  public static final String STR_ANRS = "additionalnumber";
  public static final String STR_EMAILS = "emails";
  public static final String STR_NEW_ANRS = "newAnrs";
  public static final String STR_NEW_EMAILS = "newEmails";
  public static final String STR_NEW_NUMBER = "newNumber";
  public static final String STR_NEW_TAG = "newTag";
  public static final String STR_NUMBER = "number";
  public static final String STR_PIN2 = "pin2";
  public static final String STR_SECONDNAME = "secondname";
  public static final String STR_TAG = "tag";
  private static final String TAG = "IccProvider";
  private static final UriMatcher URL_MATCHER;
  private boolean isQuerySingleId;
  private SubscriptionManager mSubscriptionManager;
  private long simID;
  
  static
  {
    SIM_ADN_READY_NAMES = new String[] { "adn_ready" };
    URL_MATCHER = new UriMatcher(-1);
    URL_MATCHER.addURI("icc", "adn", 1);
    URL_MATCHER.addURI("icc", "adn/subId/#", 2);
    URL_MATCHER.addURI("icc", "fdn", 3);
    URL_MATCHER.addURI("icc", "fdn/subId/#", 4);
    URL_MATCHER.addURI("icc", "sdn", 5);
    URL_MATCHER.addURI("icc", "sdn/subId/#", 6);
    URL_MATCHER.addURI("icc", "prop", 0);
    URL_MATCHER.addURI("icc", "prop/subId/#", 9);
    URL_MATCHER.addURI("icc", "adn/slotId/1/#", 10);
    URL_MATCHER.addURI("icc", "adn/slotId/2/#", 11);
    URL_MATCHER.addURI("icc", "adn_ready", 12);
    URL_MATCHER.addURI("icc", "adn_ready/subId/#", 13);
    URL_MATCHER.addURI("icc", "adn/#", 14);
  }
  
  public IccProvider() {}
  
  private boolean CheckSIMState(int paramInt)
  {
    paramInt = ((TelephonyManager)getContext().getSystemService("phone")).getSimState(SubscriptionManager.getSlotIndex(paramInt));
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Sim state = ");
    localStringBuilder.append(paramInt);
    log(localStringBuilder.toString());
    boolean bool;
    if (paramInt == 5) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean addIccRecordToEf(int paramInt1, String paramString1, String paramString2, String[] paramArrayOfString, String paramString3, int paramInt2)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("addIccRecordToEf: efType=0x");
    localStringBuilder.append(Integer.toHexString(paramInt1).toUpperCase());
    localStringBuilder.append(", name=");
    localStringBuilder.append(Rlog.pii("IccProvider", paramString1));
    localStringBuilder.append(", number=");
    localStringBuilder.append(Rlog.pii("IccProvider", paramString2));
    localStringBuilder.append(", emails=");
    localStringBuilder.append(Rlog.pii("IccProvider", paramArrayOfString));
    localStringBuilder.append(", subscription=");
    localStringBuilder.append(paramInt2);
    log(localStringBuilder.toString());
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3;
    try
    {
      paramArrayOfString = IIccPhoneBook.Stub.asInterface(ServiceManager.getService("simphonebook"));
      bool3 = bool2;
      if (paramArrayOfString != null) {
        bool3 = paramArrayOfString.updateAdnRecordsInEfBySearchForSubscriber(paramInt2, paramInt1, "", "", paramString1, paramString2, paramString3);
      }
    }
    catch (SecurityException paramString1)
    {
      log(paramString1.toString());
      bool3 = bool1;
    }
    catch (RemoteException paramString1)
    {
      for (;;)
      {
        bool3 = bool2;
      }
    }
    paramString1 = new StringBuilder();
    paramString1.append("addIccRecordToEf: ");
    paramString1.append(bool3);
    log(paramString1.toString());
    return bool3;
  }
  
  private boolean deleteIccRecordFromEf(int paramInt1, String paramString1, String paramString2, String[] paramArrayOfString, String paramString3, int paramInt2)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("deleteIccRecordFromEf: efType=0x");
    localStringBuilder.append(Integer.toHexString(paramInt1).toUpperCase());
    localStringBuilder.append(", name=");
    localStringBuilder.append(Rlog.pii("IccProvider", paramString1));
    localStringBuilder.append(", number=");
    localStringBuilder.append(Rlog.pii("IccProvider", paramString2));
    localStringBuilder.append(", emails=");
    localStringBuilder.append(Rlog.pii("IccProvider", paramArrayOfString));
    localStringBuilder.append(", pin2=");
    localStringBuilder.append(Rlog.pii("IccProvider", paramString3));
    localStringBuilder.append(", subscription=");
    localStringBuilder.append(paramInt2);
    log(localStringBuilder.toString());
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3;
    try
    {
      paramArrayOfString = IIccPhoneBook.Stub.asInterface(ServiceManager.getService("simphonebook"));
      bool3 = bool2;
      if (paramArrayOfString != null) {
        bool3 = paramArrayOfString.updateAdnRecordsInEfBySearchForSubscriber(paramInt2, paramInt1, paramString1, paramString2, "", "", paramString3);
      }
    }
    catch (SecurityException paramString1)
    {
      log(paramString1.toString());
      bool3 = bool1;
    }
    catch (RemoteException paramString1)
    {
      for (;;)
      {
        bool3 = bool2;
      }
    }
    paramString1 = new StringBuilder();
    paramString1.append("deleteIccRecordFromEf: ");
    paramString1.append(bool3);
    log(paramString1.toString());
    return bool3;
  }
  
  private MatrixCursor getAdnReadySub(int paramInt)
  {
    MatrixCursor localMatrixCursor = new MatrixCursor(SIM_ADN_READY_NAMES, 1);
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getAdnReadySub: subId = ");
    ((StringBuilder)localObject).append(paramInt);
    log(((StringBuilder)localObject).toString());
    int i = 0;
    int j = 0;
    int k;
    try
    {
      localObject = IIccPhoneBook.Stub.asInterface(ServiceManager.getService("simphonebook"));
      k = j;
      if (localObject != null) {
        k = ((IIccPhoneBook)localObject).getADNReadyForSubscriber(paramInt);
      }
    }
    catch (SecurityException localSecurityException)
    {
      log(localSecurityException.toString());
      k = i;
    }
    catch (RemoteException localRemoteException)
    {
      for (;;)
      {
        log(localRemoteException.toString());
        k = j;
      }
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getAdnReadySub: return adnReady: ");
    localStringBuilder.append(k);
    localStringBuilder.append(", subId: ");
    localStringBuilder.append(paramInt);
    log(localStringBuilder.toString());
    localMatrixCursor.addRow(new Object[] { String.valueOf(k) });
    return localMatrixCursor;
  }
  
  private int getRequestSubId(Uri paramUri)
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    localStringBuilder1.append("getRequestSubId url: ");
    localStringBuilder1.append(paramUri);
    log(localStringBuilder1.toString());
    try
    {
      int i = Integer.parseInt(paramUri.getLastPathSegment());
      return i;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      StringBuilder localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append("Unknown URL ");
      localStringBuilder2.append(paramUri);
      throw new IllegalArgumentException(localStringBuilder2.toString());
    }
  }
  
  private int getRowIdForNewAddedRecordForSubscriber(int paramInt)
  {
    int i = -1;
    int j;
    try
    {
      IIccPhoneBook localIIccPhoneBook = IIccPhoneBook.Stub.asInterface(ServiceManager.getService("simphonebook"));
      j = i;
      if (localIIccPhoneBook != null) {
        j = localIIccPhoneBook.getRowIdForNewAddedRecordForSubscriber(paramInt);
      }
    }
    catch (SecurityException localSecurityException)
    {
      log(localSecurityException.toString());
      j = i;
    }
    catch (RemoteException localRemoteException)
    {
      for (;;)
      {
        j = i;
      }
    }
    return j;
  }
  
  private MatrixCursor getSimProp(int paramInt)
  {
    MatrixCursor localMatrixCursor = new MatrixCursor(SIM_PROP_COLUMN_NAMES, 1);
    Object localObject1 = null;
    Object localObject2 = null;
    Object[] arrayOfObject = new Object[11];
    Object localObject3 = new StringBuilder();
    ((StringBuilder)localObject3).append("getSimProp: efType=");
    ((StringBuilder)localObject3).append(paramInt);
    log(((StringBuilder)localObject3).toString());
    Object localObject5;
    try
    {
      IIccPhoneBook localIIccPhoneBook = IIccPhoneBook.Stub.asInterface(ServiceManager.getService("simphonebook"));
      localObject3 = localObject2;
      if (localIIccPhoneBook != null) {
        localObject3 = localIIccPhoneBook.getSIMPhoneBookProperty(paramInt);
      }
    }
    catch (SecurityException localSecurityException)
    {
      log(localSecurityException.toString());
      Object localObject4 = localObject1;
    }
    catch (RemoteException localRemoteException)
    {
      for (;;)
      {
        log("getSimProp: ignore it.");
        localObject5 = localObject2;
      }
    }
    if (localObject5 != null)
    {
      if (localObject5[0] == 1) {
        arrayOfObject[0] = "USIM";
      } else {
        arrayOfObject[0] = "SIM";
      }
      if (localObject5[0] == 1)
      {
        arrayOfObject[1] = String.valueOf(localObject5[1]);
        arrayOfObject[2] = String.valueOf(localObject5[2]);
        arrayOfObject[3] = String.valueOf(localObject5[3]);
        arrayOfObject[4] = String.valueOf(localObject5[4]);
        arrayOfObject[5] = String.valueOf(localObject5[5]);
        arrayOfObject[6] = String.valueOf(localObject5[6]);
        arrayOfObject[7] = String.valueOf(localObject5[7]);
        arrayOfObject[8] = String.valueOf(localObject5[8]);
        arrayOfObject[9] = String.valueOf(localObject5[9]);
        if (localObject5[10] == 1) {
          localObject5 = "True";
        } else {
          localObject5 = "False";
        }
        arrayOfObject[10] = localObject5;
      }
      else
      {
        arrayOfObject[1] = String.valueOf(localObject5[1]);
        arrayOfObject[2] = "0";
        arrayOfObject[3] = "0";
        arrayOfObject[4] = "0";
        arrayOfObject[5] = String.valueOf(localObject5[2] - 14);
        arrayOfObject[6] = "10";
        arrayOfObject[7] = "0";
        arrayOfObject[8] = "0";
        arrayOfObject[9] = "0";
        arrayOfObject[10] = "True";
      }
      localMatrixCursor.addRow(arrayOfObject);
    }
    else
    {
      Rlog.w("IccProvider", "Cannot load ADN prop");
      arrayOfObject[0] = "SIM";
      arrayOfObject[10] = "False";
      localMatrixCursor.addRow(arrayOfObject);
    }
    log("loadSimPropFromEf: return results");
    return localMatrixCursor;
  }
  
  private MatrixCursor getSimPropSub(int paramInt1, int paramInt2)
  {
    MatrixCursor localMatrixCursor = new MatrixCursor(SIM_PROP_COLUMN_NAMES, 1);
    Object localObject1 = null;
    Object localObject2 = null;
    Object[] arrayOfObject = new Object[11];
    Object localObject3 = new StringBuilder();
    ((StringBuilder)localObject3).append("getSimPropSub: efType=");
    ((StringBuilder)localObject3).append(paramInt1);
    ((StringBuilder)localObject3).append(", subscription = ");
    ((StringBuilder)localObject3).append(paramInt2);
    log(((StringBuilder)localObject3).toString());
    Object localObject5;
    try
    {
      IIccPhoneBook localIIccPhoneBook = IIccPhoneBook.Stub.asInterface(ServiceManager.getService("simphonebook"));
      localObject3 = localObject2;
      if (localIIccPhoneBook != null) {
        if (CheckSIMState(paramInt2)) {
          localObject3 = localIIccPhoneBook.getSIMPhoneBookPropertyForSubscriber(paramInt2, paramInt1);
        } else {
          localObject3 = null;
        }
      }
    }
    catch (SecurityException localSecurityException)
    {
      log(localSecurityException.toString());
      Object localObject4 = localObject1;
    }
    catch (RemoteException localRemoteException)
    {
      for (;;)
      {
        localObject5 = localObject2;
      }
    }
    if (localObject5 != null)
    {
      if (localObject5[0] == 1) {
        arrayOfObject[0] = "USIM";
      } else {
        arrayOfObject[0] = "SIM";
      }
      if ((localObject5[0] != 1) && (!isSimPhoneBookEnabled()))
      {
        arrayOfObject[1] = String.valueOf(localObject5[1]);
        arrayOfObject[2] = "0";
        arrayOfObject[3] = "0";
        arrayOfObject[4] = "0";
        arrayOfObject[5] = String.valueOf(localObject5[2] - 14);
        arrayOfObject[6] = "20";
        arrayOfObject[7] = "0";
        arrayOfObject[8] = "0";
        arrayOfObject[9] = "0";
        arrayOfObject[10] = "True";
      }
      else
      {
        arrayOfObject[1] = String.valueOf(localObject5[1]);
        arrayOfObject[2] = String.valueOf(localObject5[2]);
        arrayOfObject[3] = String.valueOf(localObject5[3]);
        arrayOfObject[4] = String.valueOf(localObject5[4]);
        arrayOfObject[5] = String.valueOf(localObject5[5]);
        arrayOfObject[6] = String.valueOf(localObject5[6]);
        arrayOfObject[7] = String.valueOf(localObject5[7]);
        arrayOfObject[8] = String.valueOf(localObject5[8]);
        arrayOfObject[9] = String.valueOf(localObject5[9]);
        if (localObject5[10] == 1) {
          localObject5 = "True";
        } else {
          localObject5 = "False";
        }
        arrayOfObject[10] = localObject5;
      }
      localMatrixCursor.addRow(arrayOfObject);
    }
    else
    {
      Rlog.w("IccProvider", "Cannot load ADN prop");
      arrayOfObject[0] = "SIM";
      arrayOfObject[1] = "0";
      arrayOfObject[2] = "0";
      arrayOfObject[3] = "0";
      arrayOfObject[4] = "0";
      arrayOfObject[5] = "0";
      arrayOfObject[6] = "0";
      arrayOfObject[7] = "0";
      arrayOfObject[8] = "0";
      arrayOfObject[9] = "0";
      arrayOfObject[10] = "False";
      localMatrixCursor.addRow(arrayOfObject);
    }
    log("loadSimPropFromEf: return results");
    return localMatrixCursor;
  }
  
  private boolean isSimPhoneBookEnabled()
  {
    if (!SystemProperties.getBoolean("persist.vendor.radio.sim_contacts_from_iccio", false))
    {
      log("[IccProvider] isSimPhoneBookEnabled ret true. ");
      return true;
    }
    log("[IccProvider] isSimPhoneBookEnabled ret false. ");
    return false;
  }
  
  private Cursor loadAllSimContacts(int paramInt)
  {
    List localList = mSubscriptionManager.getActiveSubscriptionInfoList();
    int i = 0;
    if ((localList != null) && (localList.size() != 0))
    {
      int j = localList.size();
      Cursor[] arrayOfCursor = new Cursor[j];
      for (;;)
      {
        localObject = arrayOfCursor;
        if (i >= j) {
          break;
        }
        int k = ((SubscriptionInfo)localList.get(i)).getSubscriptionId();
        arrayOfCursor[i] = loadFromEf(paramInt, k);
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("ADN Records loaded for Subscription ::");
        ((StringBuilder)localObject).append(k);
        Rlog.i("IccProvider", ((StringBuilder)localObject).toString());
        i++;
      }
    }
    Object localObject = new Cursor[0];
    return new MergeCursor((Cursor[])localObject);
  }
  
  private MatrixCursor loadFromEf(int paramInt1, int paramInt2)
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("loadFromEf: efType=0x");
    ((StringBuilder)localObject1).append(Integer.toHexString(paramInt1).toUpperCase());
    ((StringBuilder)localObject1).append(", subscription=");
    ((StringBuilder)localObject1).append(paramInt2);
    log(((StringBuilder)localObject1).toString());
    MatrixCursor localMatrixCursor = null;
    StringBuilder localStringBuilder = null;
    Object localObject3;
    try
    {
      IIccPhoneBook localIIccPhoneBook = IIccPhoneBook.Stub.asInterface(ServiceManager.getService("simphonebook"));
      localObject1 = localStringBuilder;
      if (localIIccPhoneBook != null) {
        localObject1 = localIIccPhoneBook.getAdnRecordsInEfForSubscriber(paramInt2, paramInt1);
      }
    }
    catch (SecurityException localSecurityException)
    {
      log(localSecurityException.toString());
      Object localObject2 = localMatrixCursor;
    }
    catch (RemoteException localRemoteException)
    {
      for (;;)
      {
        localObject3 = localStringBuilder;
      }
    }
    if (localObject3 != null)
    {
      paramInt2 = localObject3.size();
      localMatrixCursor = new MatrixCursor(ADDRESS_BOOK_COLUMN_NAMES, paramInt2);
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("adnRecords.size=");
      localStringBuilder.append(paramInt2);
      log(localStringBuilder.toString());
      for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++) {
        loadRecord((AdnRecord)localObject3.get(paramInt1), localMatrixCursor, paramInt1);
      }
      return localMatrixCursor;
    }
    Rlog.w("IccProvider", "Cannot load ADN records");
    return new MatrixCursor(ADDRESS_BOOK_COLUMN_NAMES);
  }
  
  private void loadRecord(AdnRecord paramAdnRecord, MatrixCursor paramMatrixCursor, int paramInt)
  {
    if (!paramAdnRecord.isEmpty())
    {
      Object[] arrayOfObject = new Object[7];
      String str1 = paramAdnRecord.getAlphaTag();
      String str2 = paramAdnRecord.getNumber();
      Object localObject1 = paramAdnRecord.getAdditionalNumbers();
      String str3 = String.valueOf(paramAdnRecord.getRecordNumber());
      arrayOfObject[0] = str1;
      arrayOfObject[1] = str2;
      Object localObject2 = paramAdnRecord.getEmails();
      paramAdnRecord = new StringBuilder();
      int i;
      StringBuilder localStringBuilder;
      if (localObject2 != null)
      {
        i = localObject2.length;
        for (paramInt = 0; paramInt < i; paramInt++)
        {
          localObject3 = localObject2[paramInt];
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("Adding email:");
          localStringBuilder.append(Rlog.pii("IccProvider", localObject3));
          log(localStringBuilder.toString());
          paramAdnRecord.append((String)localObject3);
          paramAdnRecord.append(",");
        }
        if (paramAdnRecord.toString().isEmpty()) {
          arrayOfObject[2] = paramAdnRecord.toString();
        } else {
          arrayOfObject[2] = paramAdnRecord.toString().substring(0, paramAdnRecord.toString().length() - 1);
        }
      }
      Object localObject3 = new StringBuilder();
      if (localObject1 != null)
      {
        i = localObject1.length;
        for (paramInt = 0; paramInt < i; paramInt++)
        {
          localStringBuilder = localObject1[paramInt];
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("Adding anr:");
          ((StringBuilder)localObject2).append(Rlog.pii("IccProvider", localStringBuilder));
          log(((StringBuilder)localObject2).toString());
          ((StringBuilder)localObject3).append(localStringBuilder);
          ((StringBuilder)localObject3).append(":");
        }
        if (((StringBuilder)localObject3).toString().isEmpty()) {
          arrayOfObject[5] = ((StringBuilder)localObject3).toString();
        } else {
          arrayOfObject[5] = ((StringBuilder)localObject3).toString().substring(0, ((StringBuilder)localObject3).toString().length() - 1);
        }
      }
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("loadRecord: ");
      ((StringBuilder)localObject1).append(Rlog.pii("IccProvider", str1));
      ((StringBuilder)localObject1).append(", ");
      ((StringBuilder)localObject1).append(Rlog.pii("IccProvider", str2));
      ((StringBuilder)localObject1).append(",");
      ((StringBuilder)localObject1).append(str3);
      ((StringBuilder)localObject1).append(",");
      ((StringBuilder)localObject1).append(Rlog.pii("IccProvider", paramAdnRecord.toString()));
      ((StringBuilder)localObject1).append(",");
      ((StringBuilder)localObject1).append(Rlog.pii("IccProvider", ((StringBuilder)localObject3).toString()));
      log(((StringBuilder)localObject1).toString());
      arrayOfObject[3] = str3;
      arrayOfObject[6] = str3;
      paramMatrixCursor.addRow(arrayOfObject);
    }
  }
  
  private void log(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[IccProvider] ");
    localStringBuilder.append(paramString);
    Rlog.d("IccProvider", localStringBuilder.toString());
  }
  
  private String normalizeValue(String paramString)
  {
    int i = paramString.length();
    if (i == 0)
    {
      log("len of input String is 0");
      return paramString;
    }
    String str1 = paramString;
    String str2 = str1;
    if (paramString.charAt(0) == '\'')
    {
      str2 = str1;
      if (paramString.charAt(i - 1) == '\'') {
        str2 = paramString.substring(1, i - 1);
      }
    }
    return str2;
  }
  
  private boolean updateIccRecordInEf(int paramInt1, ContentValues paramContentValues, String paramString, int paramInt2)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("updateIccRecordInEf: efType=");
    ((StringBuilder)localObject).append(paramInt1);
    ((StringBuilder)localObject).append(", values: [ ");
    ((StringBuilder)localObject).append(Rlog.pii("IccProvider", paramContentValues));
    ((StringBuilder)localObject).append(" ], subId:");
    ((StringBuilder)localObject).append(paramInt2);
    log(((StringBuilder)localObject).toString());
    try
    {
      IIccPhoneBook localIIccPhoneBook = IIccPhoneBook.Stub.asInterface(ServiceManager.getService("simphonebook"));
      if (localIIccPhoneBook != null)
      {
        String str1 = paramContentValues.getAsString("tag");
        localObject = paramContentValues.getAsString("number");
        String str2 = paramContentValues.getAsString("emails");
        String str3 = paramContentValues.getAsString("number1");
        int i = paramContentValues.getAsInteger("index").intValue();
        if (i > 0) {
          bool3 = localIIccPhoneBook.updateAdnRecordsInEfByIndexForSubscriber(paramInt2, paramInt1, str1, (String)localObject, str2, str3, i, paramString);
        } else {
          try
          {
            bool3 = localIIccPhoneBook.updateAdnRecordsWithContentValuesInEfBySearchUsingSubId(paramInt2, paramInt1, paramContentValues, paramString);
          }
          catch (SecurityException paramContentValues) {}catch (RemoteException paramContentValues) {}
        }
      }
    }
    catch (SecurityException paramContentValues)
    {
      log(paramContentValues.toString());
      bool3 = bool2;
    }
    catch (RemoteException paramContentValues)
    {
      for (;;)
      {
        bool3 = bool1;
      }
    }
    paramContentValues = new StringBuilder();
    paramContentValues.append("updateIccRecordInEf: ");
    paramContentValues.append(bool3);
    log(paramContentValues.toString());
    return bool3;
  }
  
  public int delete(Uri paramUri, String paramString, String[] paramArrayOfString)
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("delete(): url = ");
    ((StringBuilder)localObject1).append(paramUri);
    log(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("delete(): where = ");
    ((StringBuilder)localObject1).append(Rlog.pii("IccProvider", paramString));
    log(((StringBuilder)localObject1).toString());
    int i;
    if (paramArrayOfString != null) {
      for (i = 0; i < paramArrayOfString.length; i++)
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("delete(): whereArgs - ");
        ((StringBuilder)localObject1).append(i);
        ((StringBuilder)localObject1).append(" = ");
        ((StringBuilder)localObject1).append(Rlog.pii("IccProvider", paramArrayOfString[i]));
        log(((StringBuilder)localObject1).toString());
      }
    }
    int j = -1;
    int k = URL_MATCHER.match(paramUri);
    int n;
    int i2;
    if (k != 14)
    {
      switch (k)
      {
      default: 
        int m;
        int i1;
        switch (k)
        {
        default: 
          paramString = new StringBuilder();
          paramString.append("Cannot insert into URL: ");
          paramString.append(paramUri);
          throw new UnsupportedOperationException(paramString.toString());
        case 11: 
          m = 28474;
          n = getRequestSubId(paramUri);
          i1 = SubscriptionManager.getSubId(1)[0];
          if (SubscriptionManager.isValidSubscriptionId(i1))
          {
            j = n;
            i2 = m;
            i = i1;
            if (i1 >= 0) {
              break;
            }
          }
          else
          {
            log("get invalid subId! change to default");
            i = SubscriptionManager.getDefaultSubscriptionId();
            j = n;
            i2 = m;
          }
          break;
        case 10: 
          m = 28474;
          n = getRequestSubId(paramUri);
          i1 = SubscriptionManager.getSubId(0)[0];
          if (SubscriptionManager.isValidSubscriptionId(i1))
          {
            j = n;
            i2 = m;
            i = i1;
            if (i1 >= 0) {
              break;
            }
          }
          else
          {
            log("get invalid subId! change to default");
            i = SubscriptionManager.getDefaultSubscriptionId();
            j = n;
            i2 = m;
          }
          break;
        }
        break;
      case 4: 
        i2 = 28475;
        i = getRequestSubId(paramUri);
        break;
      case 3: 
        i2 = 28475;
        i = SubscriptionManager.getDefaultSubscriptionId();
        break;
      case 2: 
        i2 = 28474;
        i = getRequestSubId(paramUri);
        break;
      case 1: 
        i2 = 28474;
        i = SubscriptionManager.getDefaultSubscriptionId();
        break;
      }
    }
    else
    {
      i2 = 28474;
      j = getRequestSubId(paramUri);
      i = SubscriptionManager.getDefaultSubscriptionId();
      paramArrayOfString = new StringBuilder();
      paramArrayOfString.append("[JERRY] delete(): ADN_ID, EF_ADN, index: ");
      paramArrayOfString.append(j);
      paramArrayOfString.append(", subId: ");
      paramArrayOfString.append(i);
      log(paramArrayOfString.toString());
    }
    log("delete");
    Object localObject2 = null;
    Object localObject3 = null;
    Object localObject4 = null;
    localObject1 = null;
    Object localObject5 = null;
    paramArrayOfString = null;
    Object localObject6 = null;
    Object localObject7 = null;
    String str1 = null;
    String str2 = null;
    if (paramString != null)
    {
      localObject2 = paramString.split(" AND ");
      n = localObject2.length;
      paramString = (String)localObject7;
      n--;
      if (n >= 0)
      {
        localObject7 = localObject2[n];
        localObject6 = new StringBuilder();
        ((StringBuilder)localObject6).append("parsing '");
        ((StringBuilder)localObject6).append(Rlog.pii("IccProvider", localObject7));
        ((StringBuilder)localObject6).append("'");
        log(((StringBuilder)localObject6).toString());
        localObject6 = ((String)localObject7).split("=", 2);
        if (localObject6.length != 2)
        {
          localObject6 = new StringBuilder();
          ((StringBuilder)localObject6).append("resolve: bad whereClause parameter: ");
          ((StringBuilder)localObject6).append(Rlog.pii("IccProvider", localObject7));
          Rlog.e("IccProvider", ((StringBuilder)localObject6).toString());
        }
        for (;;)
        {
          break;
          str1 = localObject6[0].trim();
          String str3 = localObject6[1].trim();
          if ("tag".equals(str1))
          {
            localObject6 = normalizeValue(str3);
            localObject7 = localObject1;
            localObject5 = paramArrayOfString;
            localObject4 = paramString;
          }
          else if ("number".equals(str1))
          {
            localObject7 = normalizeValue(str3);
            localObject6 = localObject3;
            localObject5 = paramArrayOfString;
            localObject4 = paramString;
          }
          else if ("emails".equals(str1))
          {
            localObject5 = normalizeValue(str3);
            localObject6 = localObject3;
            localObject7 = localObject1;
            localObject4 = paramString;
          }
          else if ("additionalnumber".equals(str1))
          {
            localObject4 = normalizeValue(str3);
            localObject6 = localObject3;
            localObject7 = localObject1;
            localObject5 = paramArrayOfString;
          }
          else
          {
            localObject6 = localObject3;
            localObject7 = localObject1;
            localObject5 = paramArrayOfString;
            localObject4 = paramString;
            if ("pin2".equals(str1))
            {
              str2 = normalizeValue(str3);
              localObject4 = paramString;
              localObject5 = paramArrayOfString;
              localObject7 = localObject1;
              localObject6 = localObject3;
            }
          }
          localObject3 = localObject6;
          localObject1 = localObject7;
          paramArrayOfString = (String[])localObject5;
          paramString = (String)localObject4;
        }
      }
      localObject2 = localObject3;
      localObject4 = localObject1;
      localObject5 = paramArrayOfString;
      localObject6 = paramString;
      localObject7 = str2;
      if (i2 == 3)
      {
        localObject2 = localObject3;
        localObject4 = localObject1;
        localObject5 = paramArrayOfString;
        localObject6 = paramString;
        localObject7 = str2;
        if (TextUtils.isEmpty(str2)) {
          return 0;
        }
      }
    }
    else
    {
      localObject7 = str1;
    }
    paramString = new ContentValues();
    paramString.put("tag", (String)localObject2);
    paramString.put("number", (String)localObject4);
    paramString.put("emails", (String)localObject5);
    paramString.put("additionalnumber", (String)localObject6);
    paramString.put("newTag", "");
    paramString.put("newNumber", "");
    paramString.put("newEmails", "");
    paramString.put("newAnrs", "");
    paramString.put("index", Integer.valueOf(j));
    paramArrayOfString = new StringBuilder();
    paramArrayOfString.append("delete mvalues= ");
    paramArrayOfString.append(Rlog.pii("IccProvider", paramString));
    log(paramArrayOfString.toString());
    if (!updateIccRecordInEf(i2, paramString, (String)localObject7, i)) {
      return 0;
    }
    getContext().getContentResolver().notifyChange(paramUri, null);
    return 1;
  }
  
  public String getType(Uri paramUri)
  {
    switch (URL_MATCHER.match(paramUri))
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown URL ");
      localStringBuilder.append(paramUri);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    return "vnd.android.cursor.dir/sim-contact";
  }
  
  public Uri insert(Uri paramUri, ContentValues paramContentValues)
  {
    Object localObject1 = null;
    Object localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("insert(): url = ");
    ((StringBuilder)localObject2).append(paramUri);
    log(((StringBuilder)localObject2).toString());
    int i = URL_MATCHER.match(paramUri);
    int j;
    int k;
    switch (i)
    {
    default: 
      paramContentValues = new StringBuilder();
      paramContentValues.append("Cannot insert into URL: ");
      paramContentValues.append(paramUri);
      throw new UnsupportedOperationException(paramContentValues.toString());
    case 4: 
      j = 28475;
      k = getRequestSubId(paramUri);
      localObject1 = paramContentValues.getAsString("pin2");
      break;
    case 3: 
      j = 28475;
      k = SubscriptionManager.getDefaultSubscriptionId();
      localObject1 = paramContentValues.getAsString("pin2");
      break;
    case 2: 
      j = 28474;
      k = getRequestSubId(paramUri);
      break;
    case 1: 
      j = 28474;
      k = SubscriptionManager.getDefaultSubscriptionId();
    }
    String str1 = paramContentValues.getAsString("tag");
    String str2 = paramContentValues.getAsString("number");
    localObject2 = paramContentValues.getAsString("emails");
    paramContentValues = paramContentValues.getAsString("number1");
    Object localObject3 = new StringBuilder();
    ((StringBuilder)localObject3).append("[ASIM] insert(): emails = (");
    ((StringBuilder)localObject3).append(Rlog.pii("IccProvider", localObject2));
    ((StringBuilder)localObject3).append("), anrs = (");
    ((StringBuilder)localObject3).append(Rlog.pii("IccProvider", paramContentValues));
    ((StringBuilder)localObject3).append(")");
    log(((StringBuilder)localObject3).toString());
    localObject3 = new ContentValues();
    ((ContentValues)localObject3).put("tag", "");
    ((ContentValues)localObject3).put("number", "");
    ((ContentValues)localObject3).put("emails", "");
    ((ContentValues)localObject3).put("additionalnumber", "");
    ((ContentValues)localObject3).put("newTag", str1);
    ((ContentValues)localObject3).put("newNumber", str2);
    ((ContentValues)localObject3).put("newEmails", (String)localObject2);
    ((ContentValues)localObject3).put("newAnrs", paramContentValues);
    ((ContentValues)localObject3).put("index", Integer.valueOf(-1));
    boolean bool = updateIccRecordInEf(j, (ContentValues)localObject3, (String)localObject1, k);
    if ((!bool) && (i != 2) && (i != 1)) {
      return null;
    }
    paramContentValues = new StringBuilder("content://icc/");
    switch (i)
    {
    default: 
      break;
    case 4: 
      paramContentValues.append("fdn/subId/");
      break;
    case 3: 
      paramContentValues.append("fdn/");
      break;
    case 2: 
      paramContentValues.append("adn/subId/");
      break;
    case 1: 
      paramContentValues.append("adn/");
    }
    if (bool)
    {
      paramContentValues.append(getRowIdForNewAddedRecordForSubscriber(k));
    }
    else
    {
      j = SubscriptionManager.getPhoneId(k);
      if (SubscriptionManager.isValidPhoneId(j))
      {
        k = j + 1;
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("gsm.sim.adn.ecode");
        ((StringBuilder)localObject1).append(k);
        localObject1 = SystemProperties.get(((StringBuilder)localObject1).toString(), "0");
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("[ABSP] insert error and err code=");
        ((StringBuilder)localObject2).append((String)localObject1);
        log(((StringBuilder)localObject2).toString());
        j = -1;
        switch (((String)localObject1).hashCode())
        {
        default: 
          break;
        case 50: 
          if (((String)localObject1).equals("2")) {
            j = 1;
          }
          break;
        case 49: 
          if (((String)localObject1).equals("1")) {
            j = 0;
          }
          break;
        }
        switch (j)
        {
        default: 
          return null;
        case 1: 
          paramContentValues.append("-2");
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("gsm.sim.adn.ecode");
          ((StringBuilder)localObject1).append(k);
          SystemProperties.set(((StringBuilder)localObject1).toString(), "0");
          log("[ABSP] append [-2]");
          break;
        case 0: 
          paramContentValues.append("-1");
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("gsm.sim.adn.ecode");
          ((StringBuilder)localObject1).append(k);
          SystemProperties.set(((StringBuilder)localObject1).toString(), "0");
          log("[ABSP] append [-1]");
          break;
        }
      }
    }
    paramContentValues = Uri.parse(paramContentValues.toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("insert result = ");
    ((StringBuilder)localObject1).append(paramContentValues);
    log(((StringBuilder)localObject1).toString());
    getContext().getContentResolver().notifyChange(paramUri, null);
    return paramContentValues;
  }
  
  public boolean onCreate()
  {
    mSubscriptionManager = SubscriptionManager.from(getContext());
    return true;
  }
  
  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    int i = SubscriptionManager.getDefaultSubscriptionId();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("query(): url = ");
    localStringBuilder.append(paramUri);
    log(localStringBuilder.toString());
    int j = 0;
    int k;
    if (paramArrayOfString1 != null) {
      for (k = 0; k < paramArrayOfString1.length; k++)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("query(): projection - ");
        localStringBuilder.append(k);
        localStringBuilder.append(" = ");
        localStringBuilder.append(paramArrayOfString1[k]);
        log(localStringBuilder.toString());
      }
    }
    paramArrayOfString1 = new StringBuilder();
    paramArrayOfString1.append("query(): selection = ");
    paramArrayOfString1.append(Rlog.pii("IccProvider", paramString1));
    log(paramArrayOfString1.toString());
    if (paramArrayOfString2 != null) {
      for (k = j; k < paramArrayOfString2.length; k++)
      {
        paramArrayOfString1 = new StringBuilder();
        paramArrayOfString1.append("query(): selectionArgs - ");
        paramArrayOfString1.append(k);
        paramArrayOfString1.append(" = ");
        paramArrayOfString1.append(Rlog.pii("IccProvider", paramArrayOfString2[k]));
        log(paramArrayOfString1.toString());
      }
    }
    paramArrayOfString1 = new StringBuilder();
    paramArrayOfString1.append("query(): sort = ");
    paramArrayOfString1.append(paramString2);
    log(paramArrayOfString1.toString());
    try
    {
      switch (URL_MATCHER.match(paramUri))
      {
      case 8: 
      case 10: 
      case 11: 
      default: 
        paramArrayOfString1 = new java/lang/IllegalArgumentException;
        break;
      case 13: 
        k = getRequestSubId(paramUri);
        paramUri = new java/lang/StringBuilder;
        paramUri.<init>();
        paramUri.append("query start ADN_READY_SUB subId=");
        paramUri.append(k);
        log(paramUri.toString());
        paramUri = getAdnReadySub(k);
        paramArrayOfString1 = new java/lang/StringBuilder;
        paramArrayOfString1.<init>();
        paramArrayOfString1.append("query end ADN_READY_SUB subId=");
        paramArrayOfString1.append(k);
        log(paramArrayOfString1.toString());
        return paramUri;
      case 12: 
        paramUri = new java/lang/StringBuilder;
        paramUri.<init>();
        paramUri.append("query start ADN_READY subId=");
        paramUri.append(i);
        log(paramUri.toString());
        paramUri = getAdnReadySub(i);
        paramArrayOfString1 = new java/lang/StringBuilder;
        paramArrayOfString1.<init>();
        paramArrayOfString1.append("query end ADN_READY subId=");
        paramArrayOfString1.append(i);
        log(paramArrayOfString1.toString());
        return paramUri;
      case 9: 
        k = getRequestSubId(paramUri);
        paramUri = new java/lang/StringBuilder;
        paramUri.<init>();
        paramUri.append("query start PROP_SUB subId=");
        paramUri.append(k);
        log(paramUri.toString());
        paramArrayOfString1 = getSimPropSub(28474, k);
        paramUri = new java/lang/StringBuilder;
        paramUri.<init>();
        paramUri.append("query end PROP_SUB subId=");
        paramUri.append(k);
        log(paramUri.toString());
        return paramArrayOfString1;
      case 7: 
        log("query start ADN_ALL ");
        paramUri = loadAllSimContacts(28474);
        log("query end ADN_ALL ");
        return paramUri;
      case 6: 
        k = getRequestSubId(paramUri);
        paramUri = new java/lang/StringBuilder;
        paramUri.<init>();
        paramUri.append("query start SDN_SUB subId=");
        paramUri.append(k);
        log(paramUri.toString());
        paramArrayOfString1 = loadFromEf(28489, k);
        paramUri = new java/lang/StringBuilder;
        paramUri.<init>();
        paramUri.append("query end SDN_SUB subId=");
        paramUri.append(k);
        log(paramUri.toString());
        return paramArrayOfString1;
      case 5: 
        paramUri = new java/lang/StringBuilder;
        paramUri.<init>();
        paramUri.append("query start SDN subId=");
        paramUri.append(i);
        log(paramUri.toString());
        paramArrayOfString1 = loadFromEf(28489, i);
        paramUri = new java/lang/StringBuilder;
        paramUri.<init>();
        paramUri.append("query end SDN subId=");
        paramUri.append(i);
        log(paramUri.toString());
        return paramArrayOfString1;
      case 4: 
        k = getRequestSubId(paramUri);
        paramUri = new java/lang/StringBuilder;
        paramUri.<init>();
        paramUri.append("query start FDN_SUB subId=");
        paramUri.append(k);
        log(paramUri.toString());
        paramUri = loadFromEf(28475, k);
        paramArrayOfString1 = new java/lang/StringBuilder;
        paramArrayOfString1.<init>();
        paramArrayOfString1.append("query end FDN_SUB subId=");
        paramArrayOfString1.append(k);
        log(paramArrayOfString1.toString());
        return paramUri;
      case 3: 
        paramUri = new java/lang/StringBuilder;
        paramUri.<init>();
        paramUri.append("query start FDN subId=");
        paramUri.append(i);
        log(paramUri.toString());
        paramUri = loadFromEf(28475, i);
        paramArrayOfString1 = new java/lang/StringBuilder;
        paramArrayOfString1.<init>();
        paramArrayOfString1.append("query end FDN subId=");
        paramArrayOfString1.append(i);
        log(paramArrayOfString1.toString());
        return paramUri;
      case 2: 
        k = getRequestSubId(paramUri);
        paramUri = new java/lang/StringBuilder;
        paramUri.<init>();
        paramUri.append("query start ADN_SUB subId=");
        paramUri.append(k);
        log(paramUri.toString());
        paramUri = loadFromEf(28474, k);
        paramArrayOfString1 = new java/lang/StringBuilder;
        paramArrayOfString1.<init>();
        paramArrayOfString1.append("query end ADN_SUB subId=");
        paramArrayOfString1.append(k);
        log(paramArrayOfString1.toString());
        return paramUri;
      case 1: 
        paramUri = new java/lang/StringBuilder;
        paramUri.<init>();
        paramUri.append("query start EF_ADN subId=");
        paramUri.append(i);
        log(paramUri.toString());
        paramArrayOfString1 = loadFromEf(28474, i);
        paramUri = new java/lang/StringBuilder;
        paramUri.<init>();
        paramUri.append("query end EF_ADN subId=");
        paramUri.append(i);
        log(paramUri.toString());
        return paramArrayOfString1;
      case 0: 
        log("query start ADN/PROP ");
        paramUri = getSimPropSub(28474, i);
        log("query end ADN/PROP ");
        return paramUri;
      }
      paramString1 = new java/lang/StringBuilder;
      paramString1.<init>();
      paramString1.append("Unknown URL ");
      paramString1.append(paramUri);
      paramArrayOfString1.<init>(paramString1.toString());
      throw paramArrayOfString1;
    }
    finally {}
  }
  
  public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    Object localObject = null;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("update(): url = ");
    localStringBuilder.append(paramUri);
    log(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("update(): initialValues = (");
    localStringBuilder.append(Rlog.pii("IccProvider", paramContentValues));
    localStringBuilder.append("), where = ");
    localStringBuilder.append(Rlog.pii("IccProvider", paramString));
    log(localStringBuilder.toString());
    if (paramArrayOfString != null) {
      for (i = 0; i < paramArrayOfString.length; i++)
      {
        paramString = new StringBuilder();
        paramString.append("update(): whereArgs - ");
        paramString.append(i);
        paramString.append(" = ");
        paramString.append(Rlog.pii("IccProvider", paramArrayOfString[i]));
        log(paramString.toString());
      }
    }
    int j = -1;
    int i = URL_MATCHER.match(paramUri);
    int i1;
    if (i != 14)
    {
      switch (i)
      {
      default: 
        int k;
        int m;
        int n;
        switch (i)
        {
        default: 
          paramContentValues = new StringBuilder();
          paramContentValues.append("Cannot insert into URL: ");
          paramContentValues.append(paramUri);
          throw new UnsupportedOperationException(paramContentValues.toString());
        case 11: 
          k = 28474;
          m = getRequestSubId(paramUri);
          n = SubscriptionManager.getSubId(1)[0];
          if (SubscriptionManager.isValidSubscriptionId(n))
          {
            paramString = localObject;
            j = m;
            i1 = k;
            i = n;
            if (n >= 0) {
              break;
            }
          }
          else
          {
            log("get invalid subId! change to default");
            i = SubscriptionManager.getDefaultSubscriptionId();
            paramString = localObject;
            j = m;
            i1 = k;
          }
          break;
        case 10: 
          k = 28474;
          m = getRequestSubId(paramUri);
          n = SubscriptionManager.getSubId(0)[0];
          if (SubscriptionManager.isValidSubscriptionId(n))
          {
            paramString = localObject;
            j = m;
            i1 = k;
            i = n;
            if (n >= 0) {
              break;
            }
          }
          else
          {
            log("get invalid subId! change to default");
            i = SubscriptionManager.getDefaultSubscriptionId();
            paramString = localObject;
            j = m;
            i1 = k;
          }
          break;
        }
        break;
      case 4: 
        i1 = 28475;
        i = getRequestSubId(paramUri);
        paramString = paramContentValues.getAsString("pin2");
        break;
      case 3: 
        i1 = 28475;
        i = SubscriptionManager.getDefaultSubscriptionId();
        paramString = paramContentValues.getAsString("pin2");
        break;
      case 2: 
        i1 = 28474;
        i = getRequestSubId(paramUri);
        paramString = localObject;
        break;
      case 1: 
        i1 = 28474;
        i = SubscriptionManager.getDefaultSubscriptionId();
        paramString = localObject;
        break;
      }
    }
    else
    {
      i1 = 28474;
      j = getRequestSubId(paramUri);
      i = SubscriptionManager.getDefaultSubscriptionId();
      paramString = new StringBuilder();
      paramString.append("[JERRY] update(): ADN_ID, EF_ADN, index: ");
      paramString.append(j);
      paramString.append(", subId: ");
      paramString.append(i);
      log(paramString.toString());
      paramString = localObject;
    }
    paramContentValues.put("index", Integer.valueOf(j));
    if (!updateIccRecordInEf(i1, paramContentValues, paramString, i))
    {
      i = SubscriptionManager.getPhoneId(i);
      if (SubscriptionManager.isValidPhoneId(i))
      {
        i1 = i + 1;
        paramUri = new StringBuilder();
        paramUri.append("gsm.sim.adn.ecode");
        paramUri.append(i1);
        paramContentValues = SystemProperties.get(paramUri.toString(), "0");
        paramUri = new StringBuilder();
        paramUri.append("[ABSP] insert error and err code=");
        paramUri.append(paramContentValues);
        log(paramUri.toString());
        switch (paramContentValues.hashCode())
        {
        default: 
          break;
        case 50: 
          if (paramContentValues.equals("2")) {
            i = 1;
          }
          break;
        case 49: 
          if (paramContentValues.equals("1")) {
            i = 0;
          }
          break;
        }
        i = -1;
        switch (i)
        {
        default: 
          break;
        case 1: 
          paramUri = new StringBuilder();
          paramUri.append("gsm.sim.adn.ecode");
          paramUri.append(i1);
          SystemProperties.set(paramUri.toString(), "0");
          log("[ABSP] insert error and return -2");
          return -2;
        case 0: 
          paramUri = new StringBuilder();
          paramUri.append("gsm.sim.adn.ecode");
          paramUri.append(i1);
          SystemProperties.set(paramUri.toString(), "0");
          log("[ABSP] insert error and return -1");
          return -1;
        }
      }
      return 0;
    }
    getContext().getContentResolver().notifyChange(paramUri, null);
    return 1;
  }
}
