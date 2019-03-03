package android.mtp;

import android.content.ContentProviderClient;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.MediaStore.Files;
import android.util.Log;
import java.nio.file.Path;
import java.util.ArrayList;

class MtpPropertyGroup
{
  private static final String PATH_WHERE = "_data=?";
  private static final String TAG = MtpPropertyGroup.class.getSimpleName();
  private String[] mColumns;
  private final Property[] mProperties;
  private final ContentProviderClient mProvider;
  private final Uri mUri;
  private final String mVolumeName;
  
  public MtpPropertyGroup(ContentProviderClient paramContentProviderClient, String paramString, int[] paramArrayOfInt)
  {
    mProvider = paramContentProviderClient;
    mVolumeName = paramString;
    mUri = MediaStore.Files.getMtpObjectsUri(paramString);
    int i = paramArrayOfInt.length;
    paramContentProviderClient = new ArrayList(i);
    paramContentProviderClient.add("_id");
    mProperties = new Property[i];
    int j = 0;
    for (int k = 0; k < i; k++) {
      mProperties[k] = createProperty(paramArrayOfInt[k], paramContentProviderClient);
    }
    i = paramContentProviderClient.size();
    mColumns = new String[i];
    for (k = j; k < i; k++) {
      mColumns[k] = ((String)paramContentProviderClient.get(k));
    }
  }
  
  private Property createProperty(int paramInt, ArrayList<String> paramArrayList)
  {
    String str1 = null;
    int i;
    switch (paramInt)
    {
    default: 
      i = 0;
      String str2 = TAG;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("unsupported property ");
      localStringBuilder.append(paramInt);
      Log.e(str2, localStringBuilder.toString());
      break;
    case 56979: 
    case 56985: 
    case 56986: 
      i = 6;
      break;
    case 56978: 
    case 56980: 
      i = 4;
      break;
    case 56544: 
      i = 65535;
      break;
    case 56475: 
      str1 = "album_artist";
      i = 65535;
      break;
    case 56474: 
      i = 65535;
      break;
    case 56473: 
      str1 = "year";
      i = 65535;
      break;
    case 56470: 
      str1 = "composer";
      i = 65535;
      break;
    case 56460: 
      i = 65535;
      break;
    case 56459: 
      str1 = "track";
      i = 4;
      break;
    case 56457: 
      str1 = "duration";
      i = 6;
      break;
    case 56398: 
      i = 65535;
      break;
    case 56392: 
      str1 = "description";
      i = 65535;
      break;
    case 56390: 
      i = 65535;
      break;
    case 56388: 
      i = 65535;
      break;
    case 56385: 
      i = 10;
      break;
    case 56331: 
      i = 6;
      break;
    case 56329: 
      i = 65535;
      break;
    case 56327: 
      i = 65535;
      break;
    case 56324: 
      i = 8;
      break;
    case 56323: 
      i = 4;
      break;
    case 56322: 
      i = 4;
      break;
    case 56321: 
      i = 6;
    }
    if (str1 != null)
    {
      paramArrayList.add(str1);
      return new Property(paramInt, i, paramArrayList.size() - 1);
    }
    return new Property(paramInt, i, -1);
  }
  
  private native String format_date_time(long paramLong);
  
  /* Error */
  private String queryAudio(String paramString1, String paramString2)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: aconst_null
    //   3: astore 4
    //   5: aload_0
    //   6: getfield 38	android/mtp/MtpPropertyGroup:mProvider	Landroid/content/ContentProviderClient;
    //   9: aload_0
    //   10: getfield 40	android/mtp/MtpPropertyGroup:mVolumeName	Ljava/lang/String;
    //   13: invokestatic 127	android/provider/MediaStore$Audio$Media:getContentUri	(Ljava/lang/String;)Landroid/net/Uri;
    //   16: iconst_1
    //   17: anewarray 71	java/lang/String
    //   20: dup
    //   21: iconst_0
    //   22: aload_2
    //   23: aastore
    //   24: ldc 11
    //   26: iconst_1
    //   27: anewarray 71	java/lang/String
    //   30: dup
    //   31: iconst_0
    //   32: aload_1
    //   33: aastore
    //   34: aconst_null
    //   35: aconst_null
    //   36: invokevirtual 133	android/content/ContentProviderClient:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/database/Cursor;
    //   39: astore_1
    //   40: aload_1
    //   41: ifnull +42 -> 83
    //   44: aload_1
    //   45: astore 4
    //   47: aload_1
    //   48: astore_3
    //   49: aload_1
    //   50: invokeinterface 139 1 0
    //   55: ifeq +28 -> 83
    //   58: aload_1
    //   59: astore 4
    //   61: aload_1
    //   62: astore_3
    //   63: aload_1
    //   64: iconst_0
    //   65: invokeinterface 143 2 0
    //   70: astore_2
    //   71: aload_1
    //   72: ifnull +9 -> 81
    //   75: aload_1
    //   76: invokeinterface 146 1 0
    //   81: aload_2
    //   82: areturn
    //   83: aload_1
    //   84: ifnull +9 -> 93
    //   87: aload_1
    //   88: invokeinterface 146 1 0
    //   93: ldc -108
    //   95: areturn
    //   96: astore_1
    //   97: goto +17 -> 114
    //   100: astore_1
    //   101: aload_3
    //   102: ifnull +9 -> 111
    //   105: aload_3
    //   106: invokeinterface 146 1 0
    //   111: ldc -108
    //   113: areturn
    //   114: aload 4
    //   116: ifnull +10 -> 126
    //   119: aload 4
    //   121: invokeinterface 146 1 0
    //   126: aload_1
    //   127: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	128	0	this	MtpPropertyGroup
    //   0	128	1	paramString1	String
    //   0	128	2	paramString2	String
    //   1	105	3	str1	String
    //   3	117	4	str2	String
    // Exception table:
    //   from	to	target	type
    //   5	40	96	finally
    //   49	58	96	finally
    //   63	71	96	finally
    //   5	40	100	java/lang/Exception
    //   49	58	100	java/lang/Exception
    //   63	71	100	java/lang/Exception
  }
  
  /* Error */
  private String queryGenre(String paramString)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: aload_0
    //   5: getfield 38	android/mtp/MtpPropertyGroup:mProvider	Landroid/content/ContentProviderClient;
    //   8: aload_0
    //   9: getfield 40	android/mtp/MtpPropertyGroup:mVolumeName	Ljava/lang/String;
    //   12: invokestatic 153	android/provider/MediaStore$Audio$Genres:getContentUri	(Ljava/lang/String;)Landroid/net/Uri;
    //   15: iconst_1
    //   16: anewarray 71	java/lang/String
    //   19: dup
    //   20: iconst_0
    //   21: ldc -101
    //   23: aastore
    //   24: ldc 11
    //   26: iconst_1
    //   27: anewarray 71	java/lang/String
    //   30: dup
    //   31: iconst_0
    //   32: aload_1
    //   33: aastore
    //   34: aconst_null
    //   35: aconst_null
    //   36: invokevirtual 133	android/content/ContentProviderClient:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/database/Cursor;
    //   39: astore_1
    //   40: aload_1
    //   41: ifnull +42 -> 83
    //   44: aload_1
    //   45: astore_3
    //   46: aload_1
    //   47: astore_2
    //   48: aload_1
    //   49: invokeinterface 139 1 0
    //   54: ifeq +29 -> 83
    //   57: aload_1
    //   58: astore_3
    //   59: aload_1
    //   60: astore_2
    //   61: aload_1
    //   62: iconst_0
    //   63: invokeinterface 143 2 0
    //   68: astore 4
    //   70: aload_1
    //   71: ifnull +9 -> 80
    //   74: aload_1
    //   75: invokeinterface 146 1 0
    //   80: aload 4
    //   82: areturn
    //   83: aload_1
    //   84: ifnull +9 -> 93
    //   87: aload_1
    //   88: invokeinterface 146 1 0
    //   93: ldc -108
    //   95: areturn
    //   96: astore_1
    //   97: goto +17 -> 114
    //   100: astore_1
    //   101: aload_2
    //   102: ifnull +9 -> 111
    //   105: aload_2
    //   106: invokeinterface 146 1 0
    //   111: ldc -108
    //   113: areturn
    //   114: aload_3
    //   115: ifnull +9 -> 124
    //   118: aload_3
    //   119: invokeinterface 146 1 0
    //   124: aload_1
    //   125: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	126	0	this	MtpPropertyGroup
    //   0	126	1	paramString	String
    //   1	105	2	str1	String
    //   3	116	3	str2	String
    //   68	13	4	str3	String
    // Exception table:
    //   from	to	target	type
    //   4	40	96	finally
    //   48	57	96	finally
    //   61	70	96	finally
    //   4	40	100	java/lang/Exception
    //   48	57	100	java/lang/Exception
    //   61	70	100	java/lang/Exception
  }
  
  public int getPropertyList(MtpStorageManager.MtpObject paramMtpObject, MtpPropertyList paramMtpPropertyList)
  {
    int i = paramMtpObject.getId();
    String str = paramMtpObject.getPath().toString();
    Property[] arrayOfProperty = mProperties;
    int j = arrayOfProperty.length;
    Object localObject1 = null;
    int k = 0;
    Object localObject2;
    while (k < j)
    {
      Property localProperty = arrayOfProperty[k];
      Object localObject3 = localObject1;
      if (column != -1)
      {
        localObject3 = localObject1;
        if (localObject1 == null)
        {
          localObject3 = localObject1;
          try
          {
            Cursor localCursor = mProvider.query(mUri, mColumns, "_data=?", new String[] { str }, null, null);
            localObject1 = localCursor;
            if (localCursor != null)
            {
              localObject1 = localCursor;
              localObject3 = localCursor;
              if (!localCursor.moveToNext())
              {
                localObject3 = localCursor;
                localCursor.close();
                localObject1 = null;
              }
            }
            localObject3 = localObject1;
          }
          catch (RemoteException localRemoteException)
          {
            Log.e(TAG, "Mediaprovider lookup failed");
          }
        }
      }
      int m;
      long l1;
      switch (code)
      {
      default: 
        m = type;
        break;
      case 56979: 
      case 56985: 
      case 56986: 
        paramMtpPropertyList.append(i, code, 6, 0L);
        break;
      case 56978: 
      case 56980: 
        paramMtpPropertyList.append(i, code, 4, 0L);
        break;
      case 56474: 
        paramMtpPropertyList.append(i, code, queryAudio(str, "album"));
        break;
      case 56473: 
        m = 0;
        if (localObject3 != null) {
          m = localObject3.getInt(column);
        }
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append(Integer.toString(m));
        ((StringBuilder)localObject2).append("0101T000000");
        localObject2 = ((StringBuilder)localObject2).toString();
        paramMtpPropertyList.append(i, code, (String)localObject2);
        break;
      case 56460: 
        localObject2 = queryGenre(str);
        if (localObject2 == null) {
          break label907;
        }
        paramMtpPropertyList.append(i, code, (String)localObject2);
        break;
      case 56459: 
        m = 0;
        if (localObject3 != null) {
          m = localObject3.getInt(column);
        }
        paramMtpPropertyList.append(i, code, 4, m % 1000);
        break;
      case 56390: 
        paramMtpPropertyList.append(i, code, queryAudio(str, "artist"));
        break;
      case 56385: 
        l1 = paramMtpObject.getPath().toString().hashCode() << 32;
        long l2 = paramMtpObject.getModifiedTime();
        paramMtpPropertyList.append(i, code, type, l1 + l2);
        break;
      case 56331: 
        m = code;
        int n = type;
        if (paramMtpObject.getParent().isRoot()) {}
        for (l1 = 0L;; l1 = paramMtpObject.getParent().getId()) {
          break;
        }
        paramMtpPropertyList.append(i, m, n, l1);
        break;
      case 56329: 
      case 56398: 
        paramMtpPropertyList.append(i, code, format_date_time(paramMtpObject.getModifiedTime()));
        break;
      case 56327: 
      case 56388: 
      case 56544: 
        paramMtpPropertyList.append(i, code, paramMtpObject.getName());
        break;
      case 56324: 
        paramMtpPropertyList.append(i, code, type, paramMtpObject.getSize());
        break;
      case 56323: 
        paramMtpPropertyList.append(i, code, type, 0L);
        break;
      case 56322: 
        paramMtpPropertyList.append(i, code, type, paramMtpObject.getFormat());
        break;
      case 56321: 
        paramMtpPropertyList.append(i, code, type, paramMtpObject.getStorageId());
        break;
      }
      if (m != 0)
      {
        if (m != 65535)
        {
          l1 = 0L;
          if (localObject3 != null) {
            l1 = localObject3.getLong(column);
          }
          paramMtpPropertyList.append(i, code, type, l1);
        }
        else
        {
          localObject2 = "";
          if (localObject3 != null) {
            localObject2 = localObject3.getString(column);
          }
          paramMtpPropertyList.append(i, code, (String)localObject2);
        }
      }
      else {
        paramMtpPropertyList.append(i, code, type, 0L);
      }
      label907:
      k++;
      localObject2 = localObject3;
    }
    if (localObject2 != null) {
      ((Cursor)localObject2).close();
    }
    return 8193;
  }
  
  private class Property
  {
    int code;
    int column;
    int type;
    
    Property(int paramInt1, int paramInt2, int paramInt3)
    {
      code = paramInt1;
      type = paramInt2;
      column = paramInt3;
    }
  }
}
