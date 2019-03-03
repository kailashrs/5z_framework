package com.android.server.backup;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.backup.BackupDataOutput;
import android.app.backup.BackupHelper;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncAdapterType;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AccountSyncSettingsBackupHelper
  implements BackupHelper
{
  private static final boolean DEBUG = false;
  private static final String JSON_FORMAT_ENCODING = "UTF-8";
  private static final String JSON_FORMAT_HEADER_KEY = "account_data";
  private static final int JSON_FORMAT_VERSION = 1;
  private static final String KEY_ACCOUNTS = "accounts";
  private static final String KEY_ACCOUNT_AUTHORITIES = "authorities";
  private static final String KEY_ACCOUNT_NAME = "name";
  private static final String KEY_ACCOUNT_TYPE = "type";
  private static final String KEY_AUTHORITY_NAME = "name";
  private static final String KEY_AUTHORITY_SYNC_ENABLED = "syncEnabled";
  private static final String KEY_AUTHORITY_SYNC_STATE = "syncState";
  private static final String KEY_MASTER_SYNC_ENABLED = "masterSyncEnabled";
  private static final String KEY_VERSION = "version";
  private static final int MD5_BYTE_SIZE = 16;
  private static final String STASH_FILE;
  private static final int STATE_VERSION = 1;
  private static final int SYNC_REQUEST_LATCH_TIMEOUT_SECONDS = 1;
  private static final String TAG = "AccountSyncSettingsBackupHelper";
  private AccountManager mAccountManager;
  private Context mContext;
  
  static
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(Environment.getDataDirectory());
    localStringBuilder.append("/backup/unadded_account_syncsettings.json");
    STASH_FILE = localStringBuilder.toString();
  }
  
  public AccountSyncSettingsBackupHelper(Context paramContext)
  {
    mContext = paramContext;
    mAccountManager = AccountManager.get(mContext);
  }
  
  public static void accountAdded(Context paramContext)
  {
    new AccountSyncSettingsBackupHelper(paramContext).accountAddedInternal();
  }
  
  /* Error */
  private void accountAddedInternal()
  {
    // Byte code:
    //   0: new 124	java/io/FileInputStream
    //   3: astore_1
    //   4: new 126	java/io/File
    //   7: astore_2
    //   8: aload_2
    //   9: getstatic 98	com/android/server/backup/AccountSyncSettingsBackupHelper:STASH_FILE	Ljava/lang/String;
    //   12: invokespecial 129	java/io/File:<init>	(Ljava/lang/String;)V
    //   15: aload_1
    //   16: aload_2
    //   17: invokespecial 132	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   20: aconst_null
    //   21: astore_3
    //   22: aload_3
    //   23: astore_2
    //   24: new 134	java/io/DataInputStream
    //   27: astore 4
    //   29: aload_3
    //   30: astore_2
    //   31: aload 4
    //   33: aload_1
    //   34: invokespecial 137	java/io/DataInputStream:<init>	(Ljava/io/InputStream;)V
    //   37: aload_3
    //   38: astore_2
    //   39: aload 4
    //   41: invokevirtual 140	java/io/DataInputStream:readUTF	()Ljava/lang/String;
    //   44: astore_3
    //   45: aconst_null
    //   46: aload_1
    //   47: invokestatic 142	com/android/server/backup/AccountSyncSettingsBackupHelper:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   50: new 144	org/json/JSONArray
    //   53: astore_2
    //   54: aload_2
    //   55: aload_3
    //   56: invokespecial 145	org/json/JSONArray:<init>	(Ljava/lang/String;)V
    //   59: aload_0
    //   60: aload_2
    //   61: invokespecial 149	com/android/server/backup/AccountSyncSettingsBackupHelper:restoreFromJsonArray	(Lorg/json/JSONArray;)V
    //   64: goto +13 -> 77
    //   67: astore_2
    //   68: ldc 52
    //   70: ldc -105
    //   72: aload_2
    //   73: invokestatic 157	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   76: pop
    //   77: return
    //   78: astore_3
    //   79: goto +8 -> 87
    //   82: astore_3
    //   83: aload_3
    //   84: astore_2
    //   85: aload_3
    //   86: athrow
    //   87: aload_2
    //   88: aload_1
    //   89: invokestatic 142	com/android/server/backup/AccountSyncSettingsBackupHelper:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   92: aload_3
    //   93: athrow
    //   94: astore_2
    //   95: return
    //   96: astore_2
    //   97: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	98	0	this	AccountSyncSettingsBackupHelper
    //   3	86	1	localFileInputStream	FileInputStream
    //   7	54	2	localObject1	Object
    //   67	6	2	localJSONException	JSONException
    //   84	4	2	localObject2	Object
    //   94	1	2	localIOException	IOException
    //   96	1	2	localFileNotFoundException	java.io.FileNotFoundException
    //   21	35	3	str	String
    //   78	1	3	localObject3	Object
    //   82	11	3	localThrowable	Throwable
    //   27	13	4	localDataInputStream	DataInputStream
    // Exception table:
    //   from	to	target	type
    //   50	64	67	org/json/JSONException
    //   24	29	78	finally
    //   31	37	78	finally
    //   39	45	78	finally
    //   85	87	78	finally
    //   24	29	82	java/lang/Throwable
    //   31	37	82	java/lang/Throwable
    //   39	45	82	java/lang/Throwable
    //   0	20	94	java/io/IOException
    //   45	50	94	java/io/IOException
    //   87	94	94	java/io/IOException
    //   0	20	96	java/io/FileNotFoundException
    //   45	50	96	java/io/FileNotFoundException
    //   87	94	96	java/io/FileNotFoundException
  }
  
  private byte[] generateMd5Checksum(byte[] paramArrayOfByte)
    throws NoSuchAlgorithmException
  {
    if (paramArrayOfByte == null) {
      return null;
    }
    return MessageDigest.getInstance("MD5").digest(paramArrayOfByte);
  }
  
  private HashSet<Account> getAccounts()
  {
    Account[] arrayOfAccount = mAccountManager.getAccounts();
    HashSet localHashSet = new HashSet();
    int i = arrayOfAccount.length;
    for (int j = 0; j < i; j++) {
      localHashSet.add(arrayOfAccount[j]);
    }
    return localHashSet;
  }
  
  private byte[] readOldMd5Checksum(ParcelFileDescriptor paramParcelFileDescriptor)
    throws IOException
  {
    Object localObject = new DataInputStream(new FileInputStream(paramParcelFileDescriptor.getFileDescriptor()));
    paramParcelFileDescriptor = new byte[16];
    try
    {
      int i = ((DataInputStream)localObject).readInt();
      if (i <= 1) {
        for (i = 0; i < 16; i++) {
          paramParcelFileDescriptor[i] = ((DataInputStream)localObject).readByte();
        }
      }
      localObject = new java/lang/StringBuilder;
      ((StringBuilder)localObject).<init>();
      ((StringBuilder)localObject).append("Backup state version is: ");
      ((StringBuilder)localObject).append(i);
      ((StringBuilder)localObject).append(" (support only up to version ");
      ((StringBuilder)localObject).append(1);
      ((StringBuilder)localObject).append(")");
      Log.i("AccountSyncSettingsBackupHelper", ((StringBuilder)localObject).toString());
    }
    catch (EOFException localEOFException) {}
    return paramParcelFileDescriptor;
  }
  
  private void restoreExistingAccountSyncSettingsFromJSON(JSONObject paramJSONObject)
    throws JSONException
  {
    JSONArray localJSONArray = paramJSONObject.getJSONArray("authorities");
    paramJSONObject = new Account(paramJSONObject.getString("name"), paramJSONObject.getString("type"));
    for (int i = 0; i < localJSONArray.length(); i++)
    {
      JSONObject localJSONObject = (JSONObject)localJSONArray.get(i);
      String str = localJSONObject.getString("name");
      boolean bool = localJSONObject.getBoolean("syncEnabled");
      int j = localJSONObject.getInt("syncState");
      ContentResolver.setSyncAutomaticallyAsUser(paramJSONObject, str, bool, 0);
      if (!bool)
      {
        if (j == 0) {
          j = 0;
        } else {
          j = 2;
        }
        ContentResolver.setIsSyncable(paramJSONObject, str, j);
      }
    }
  }
  
  /* Error */
  private void restoreFromJsonArray(JSONArray paramJSONArray)
    throws JSONException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 266	com/android/server/backup/AccountSyncSettingsBackupHelper:getAccounts	()Ljava/util/HashSet;
    //   4: astore_2
    //   5: new 144	org/json/JSONArray
    //   8: dup
    //   9: invokespecial 267	org/json/JSONArray:<init>	()V
    //   12: astore_3
    //   13: iconst_0
    //   14: istore 4
    //   16: aload_1
    //   17: invokevirtual 241	org/json/JSONArray:length	()I
    //   20: istore 5
    //   22: aconst_null
    //   23: astore 6
    //   25: iload 4
    //   27: iload 5
    //   29: if_icmpge +82 -> 111
    //   32: aload_1
    //   33: iload 4
    //   35: invokevirtual 244	org/json/JSONArray:get	(I)Ljava/lang/Object;
    //   38: checkcast 225	org/json/JSONObject
    //   41: astore 7
    //   43: aload 7
    //   45: ldc 28
    //   47: invokevirtual 235	org/json/JSONObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   50: astore 8
    //   52: aload 7
    //   54: ldc 31
    //   56: invokevirtual 235	org/json/JSONObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   59: astore 6
    //   61: new 231	android/accounts/Account
    //   64: astore 9
    //   66: aload 9
    //   68: aload 8
    //   70: aload 6
    //   72: invokespecial 238	android/accounts/Account:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   75: aload_2
    //   76: aload 9
    //   78: invokevirtual 270	java/util/HashSet:contains	(Ljava/lang/Object;)Z
    //   81: ifeq +12 -> 93
    //   84: aload_0
    //   85: aload 7
    //   87: invokespecial 272	com/android/server/backup/AccountSyncSettingsBackupHelper:restoreExistingAccountSyncSettingsFromJSON	(Lorg/json/JSONObject;)V
    //   90: goto +15 -> 105
    //   93: aload_3
    //   94: aload 7
    //   96: invokevirtual 276	org/json/JSONArray:put	(Ljava/lang/Object;)Lorg/json/JSONArray;
    //   99: pop
    //   100: goto +5 -> 105
    //   103: astore 6
    //   105: iinc 4 1
    //   108: goto -92 -> 16
    //   111: aload_3
    //   112: invokevirtual 241	org/json/JSONArray:length	()I
    //   115: ifle +91 -> 206
    //   118: new 278	java/io/FileOutputStream
    //   121: astore_2
    //   122: aload_2
    //   123: getstatic 98	com/android/server/backup/AccountSyncSettingsBackupHelper:STASH_FILE	Ljava/lang/String;
    //   126: invokespecial 279	java/io/FileOutputStream:<init>	(Ljava/lang/String;)V
    //   129: aload 6
    //   131: astore_1
    //   132: aload_3
    //   133: invokevirtual 280	org/json/JSONArray:toString	()Ljava/lang/String;
    //   136: astore_3
    //   137: aload 6
    //   139: astore_1
    //   140: new 282	java/io/DataOutputStream
    //   143: astore 8
    //   145: aload 6
    //   147: astore_1
    //   148: aload 8
    //   150: aload_2
    //   151: invokespecial 285	java/io/DataOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   154: aload 6
    //   156: astore_1
    //   157: aload 8
    //   159: aload_3
    //   160: invokevirtual 288	java/io/DataOutputStream:writeUTF	(Ljava/lang/String;)V
    //   163: aconst_null
    //   164: aload_2
    //   165: invokestatic 142	com/android/server/backup/AccountSyncSettingsBackupHelper:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   168: goto +35 -> 203
    //   171: astore 6
    //   173: goto +11 -> 184
    //   176: astore 6
    //   178: aload 6
    //   180: astore_1
    //   181: aload 6
    //   183: athrow
    //   184: aload_1
    //   185: aload_2
    //   186: invokestatic 142	com/android/server/backup/AccountSyncSettingsBackupHelper:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   189: aload 6
    //   191: athrow
    //   192: astore_1
    //   193: ldc 52
    //   195: ldc_w 290
    //   198: aload_1
    //   199: invokestatic 157	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   202: pop
    //   203: goto +26 -> 229
    //   206: new 126	java/io/File
    //   209: dup
    //   210: getstatic 98	com/android/server/backup/AccountSyncSettingsBackupHelper:STASH_FILE	Ljava/lang/String;
    //   213: invokespecial 129	java/io/File:<init>	(Ljava/lang/String;)V
    //   216: astore_1
    //   217: aload_1
    //   218: invokevirtual 294	java/io/File:exists	()Z
    //   221: ifeq +8 -> 229
    //   224: aload_1
    //   225: invokevirtual 297	java/io/File:delete	()Z
    //   228: pop
    //   229: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	230	0	this	AccountSyncSettingsBackupHelper
    //   0	230	1	paramJSONArray	JSONArray
    //   4	182	2	localObject1	Object
    //   12	148	3	localObject2	Object
    //   14	92	4	i	int
    //   20	10	5	j	int
    //   23	48	6	str	String
    //   103	52	6	localIllegalArgumentException	IllegalArgumentException
    //   171	1	6	localObject3	Object
    //   176	14	6	localThrowable	Throwable
    //   41	54	7	localJSONObject	JSONObject
    //   50	108	8	localObject4	Object
    //   64	13	9	localAccount	Account
    // Exception table:
    //   from	to	target	type
    //   61	75	103	java/lang/IllegalArgumentException
    //   132	137	171	finally
    //   140	145	171	finally
    //   148	154	171	finally
    //   157	163	171	finally
    //   181	184	171	finally
    //   132	137	176	java/lang/Throwable
    //   140	145	176	java/lang/Throwable
    //   148	154	176	java/lang/Throwable
    //   157	163	176	java/lang/Throwable
    //   118	129	192	java/io/IOException
    //   163	168	192	java/io/IOException
    //   184	192	192	java/io/IOException
  }
  
  private JSONObject serializeAccountSyncSettingsToJSON()
    throws JSONException
  {
    Account[] arrayOfAccount = mAccountManager.getAccounts();
    Object localObject1 = ContentResolver.getSyncAdapterTypesAsUser(mContext.getUserId());
    HashMap localHashMap = new HashMap();
    int i = localObject1.length;
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      localJSONObject1 = localObject1[k];
      if (localJSONObject1.isUserVisible())
      {
        if (!localHashMap.containsKey(accountType)) {
          localHashMap.put(accountType, new ArrayList());
        }
        ((List)localHashMap.get(accountType)).add(authority);
      }
    }
    JSONObject localJSONObject1 = new JSONObject();
    localJSONObject1.put("version", 1);
    localJSONObject1.put("masterSyncEnabled", ContentResolver.getMasterSyncAutomatically());
    localObject1 = new JSONArray();
    i = arrayOfAccount.length;
    for (k = j; k < i; k++)
    {
      Account localAccount = arrayOfAccount[k];
      Object localObject2 = (List)localHashMap.get(type);
      if ((localObject2 != null) && (!((List)localObject2).isEmpty()))
      {
        JSONObject localJSONObject2 = new JSONObject();
        localJSONObject2.put("name", name);
        localJSONObject2.put("type", type);
        JSONArray localJSONArray = new JSONArray();
        localObject2 = ((List)localObject2).iterator();
        while (((Iterator)localObject2).hasNext())
        {
          String str = (String)((Iterator)localObject2).next();
          j = ContentResolver.getIsSyncable(localAccount, str);
          boolean bool = ContentResolver.getSyncAutomatically(localAccount, str);
          JSONObject localJSONObject3 = new JSONObject();
          localJSONObject3.put("name", str);
          localJSONObject3.put("syncState", j);
          localJSONObject3.put("syncEnabled", bool);
          localJSONArray.put(localJSONObject3);
        }
        localJSONObject2.put("authorities", localJSONArray);
        ((JSONArray)localObject1).put(localJSONObject2);
      }
    }
    localJSONObject1.put("accounts", localObject1);
    return localJSONObject1;
  }
  
  private void writeNewMd5Checksum(ParcelFileDescriptor paramParcelFileDescriptor, byte[] paramArrayOfByte)
    throws IOException
  {
    paramParcelFileDescriptor = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(paramParcelFileDescriptor.getFileDescriptor())));
    paramParcelFileDescriptor.writeInt(1);
    paramParcelFileDescriptor.write(paramArrayOfByte);
  }
  
  public void performBackup(ParcelFileDescriptor paramParcelFileDescriptor1, BackupDataOutput paramBackupDataOutput, ParcelFileDescriptor paramParcelFileDescriptor2)
  {
    try
    {
      byte[] arrayOfByte1 = serializeAccountSyncSettingsToJSON().toString().getBytes("UTF-8");
      paramParcelFileDescriptor1 = readOldMd5Checksum(paramParcelFileDescriptor1);
      byte[] arrayOfByte2 = generateMd5Checksum(arrayOfByte1);
      if (!Arrays.equals(paramParcelFileDescriptor1, arrayOfByte2))
      {
        int i = arrayOfByte1.length;
        paramBackupDataOutput.writeEntityHeader("account_data", i);
        paramBackupDataOutput.writeEntityData(arrayOfByte1, i);
        Log.i("AccountSyncSettingsBackupHelper", "Backup successful.");
      }
      else
      {
        Log.i("AccountSyncSettingsBackupHelper", "Old and new MD5 checksums match. Skipping backup.");
      }
      writeNewMd5Checksum(paramParcelFileDescriptor2, arrayOfByte2);
    }
    catch (JSONException|IOException|NoSuchAlgorithmException paramParcelFileDescriptor1)
    {
      paramBackupDataOutput = new StringBuilder();
      paramBackupDataOutput.append("Couldn't backup account sync settings\n");
      paramBackupDataOutput.append(paramParcelFileDescriptor1);
      Log.e("AccountSyncSettingsBackupHelper", paramBackupDataOutput.toString());
    }
  }
  
  /* Error */
  public void restoreEntity(android.app.backup.BackupDataInputStream paramBackupDataInputStream)
  {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual 440	android/app/backup/BackupDataInputStream:size	()I
    //   4: newarray byte
    //   6: astore_2
    //   7: aload_1
    //   8: aload_2
    //   9: invokevirtual 444	android/app/backup/BackupDataInputStream:read	([B)I
    //   12: pop
    //   13: new 372	java/lang/String
    //   16: astore_1
    //   17: aload_1
    //   18: aload_2
    //   19: ldc 13
    //   21: invokespecial 447	java/lang/String:<init>	([BLjava/lang/String;)V
    //   24: new 225	org/json/JSONObject
    //   27: astore_2
    //   28: aload_2
    //   29: aload_1
    //   30: invokespecial 448	org/json/JSONObject:<init>	(Ljava/lang/String;)V
    //   33: aload_2
    //   34: ldc 41
    //   36: invokevirtual 248	org/json/JSONObject:getBoolean	(Ljava/lang/String;)Z
    //   39: istore_3
    //   40: aload_2
    //   41: ldc 22
    //   43: invokevirtual 229	org/json/JSONObject:getJSONArray	(Ljava/lang/String;)Lorg/json/JSONArray;
    //   46: astore_1
    //   47: invokestatic 344	android/content/ContentResolver:getMasterSyncAutomatically	()Z
    //   50: ifeq +7 -> 57
    //   53: iconst_0
    //   54: invokestatic 452	android/content/ContentResolver:setMasterSyncAutomatically	(Z)V
    //   57: aload_0
    //   58: aload_1
    //   59: invokespecial 149	com/android/server/backup/AccountSyncSettingsBackupHelper:restoreFromJsonArray	(Lorg/json/JSONArray;)V
    //   62: iload_3
    //   63: invokestatic 452	android/content/ContentResolver:setMasterSyncAutomatically	(Z)V
    //   66: ldc 52
    //   68: ldc_w 454
    //   71: invokestatic 221	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   74: pop
    //   75: goto +43 -> 118
    //   78: astore_1
    //   79: iload_3
    //   80: invokestatic 452	android/content/ContentResolver:setMasterSyncAutomatically	(Z)V
    //   83: aload_1
    //   84: athrow
    //   85: astore_1
    //   86: new 74	java/lang/StringBuilder
    //   89: dup
    //   90: invokespecial 77	java/lang/StringBuilder:<init>	()V
    //   93: astore_2
    //   94: aload_2
    //   95: ldc_w 456
    //   98: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: pop
    //   102: aload_2
    //   103: aload_1
    //   104: invokevirtual 87	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   107: pop
    //   108: ldc 52
    //   110: aload_2
    //   111: invokevirtual 96	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   114: invokestatic 433	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   117: pop
    //   118: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	119	0	this	AccountSyncSettingsBackupHelper
    //   0	119	1	paramBackupDataInputStream	android.app.backup.BackupDataInputStream
    //   6	105	2	localObject	Object
    //   39	41	3	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   57	62	78	finally
    //   7	57	85	java/io/IOException
    //   7	57	85	org/json/JSONException
    //   62	66	85	java/io/IOException
    //   62	66	85	org/json/JSONException
    //   66	75	85	java/io/IOException
    //   66	75	85	org/json/JSONException
    //   79	85	85	java/io/IOException
    //   79	85	85	org/json/JSONException
  }
  
  public void writeNewStateDescription(ParcelFileDescriptor paramParcelFileDescriptor) {}
}
