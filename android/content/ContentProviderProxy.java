package android.content;

import android.content.res.AssetFileDescriptor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.ICancellationSignal.Stub;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

final class ContentProviderProxy
  implements IContentProvider
{
  private IBinder mRemote;
  
  public ContentProviderProxy(IBinder paramIBinder)
  {
    mRemote = paramIBinder;
  }
  
  public ContentProviderResult[] applyBatch(String paramString, ArrayList<ContentProviderOperation> paramArrayList)
    throws RemoteException, OperationApplicationException
  {
    Parcel localParcel1 = Parcel.obtain();
    Parcel localParcel2 = Parcel.obtain();
    try
    {
      localParcel1.writeInterfaceToken("android.content.IContentProvider");
      localParcel1.writeString(paramString);
      localParcel1.writeInt(paramArrayList.size());
      paramString = paramArrayList.iterator();
      while (paramString.hasNext()) {
        ((ContentProviderOperation)paramString.next()).writeToParcel(localParcel1, 0);
      }
      mRemote.transact(20, localParcel1, localParcel2, 0);
      DatabaseUtils.readExceptionWithOperationApplicationExceptionFromParcel(localParcel2);
      paramString = (ContentProviderResult[])localParcel2.createTypedArray(ContentProviderResult.CREATOR);
      return paramString;
    }
    finally
    {
      localParcel1.recycle();
      localParcel2.recycle();
    }
  }
  
  public IBinder asBinder()
  {
    return mRemote;
  }
  
  public int bulkInsert(String paramString, Uri paramUri, ContentValues[] paramArrayOfContentValues)
    throws RemoteException
  {
    Parcel localParcel1 = Parcel.obtain();
    Parcel localParcel2 = Parcel.obtain();
    try
    {
      localParcel1.writeInterfaceToken("android.content.IContentProvider");
      localParcel1.writeString(paramString);
      paramUri.writeToParcel(localParcel1, 0);
      localParcel1.writeTypedArray(paramArrayOfContentValues, 0);
      mRemote.transact(13, localParcel1, localParcel2, 0);
      DatabaseUtils.readExceptionFromParcel(localParcel2);
      int i = localParcel2.readInt();
      return i;
    }
    finally
    {
      localParcel1.recycle();
      localParcel2.recycle();
    }
  }
  
  public Bundle call(String paramString1, String paramString2, String paramString3, Bundle paramBundle)
    throws RemoteException
  {
    Parcel localParcel1 = Parcel.obtain();
    Parcel localParcel2 = Parcel.obtain();
    try
    {
      localParcel1.writeInterfaceToken("android.content.IContentProvider");
      localParcel1.writeString(paramString1);
      localParcel1.writeString(paramString2);
      localParcel1.writeString(paramString3);
      localParcel1.writeBundle(paramBundle);
      mRemote.transact(21, localParcel1, localParcel2, 0);
      DatabaseUtils.readExceptionFromParcel(localParcel2);
      paramString1 = localParcel2.readBundle();
      return paramString1;
    }
    finally
    {
      localParcel1.recycle();
      localParcel2.recycle();
    }
  }
  
  public Uri canonicalize(String paramString, Uri paramUri)
    throws RemoteException
  {
    Parcel localParcel1 = Parcel.obtain();
    Parcel localParcel2 = Parcel.obtain();
    try
    {
      localParcel1.writeInterfaceToken("android.content.IContentProvider");
      localParcel1.writeString(paramString);
      paramUri.writeToParcel(localParcel1, 0);
      mRemote.transact(25, localParcel1, localParcel2, 0);
      DatabaseUtils.readExceptionFromParcel(localParcel2);
      paramString = (Uri)Uri.CREATOR.createFromParcel(localParcel2);
      return paramString;
    }
    finally
    {
      localParcel1.recycle();
      localParcel2.recycle();
    }
  }
  
  public ICancellationSignal createCancellationSignal()
    throws RemoteException
  {
    Parcel localParcel1 = Parcel.obtain();
    Parcel localParcel2 = Parcel.obtain();
    try
    {
      localParcel1.writeInterfaceToken("android.content.IContentProvider");
      mRemote.transact(24, localParcel1, localParcel2, 0);
      DatabaseUtils.readExceptionFromParcel(localParcel2);
      ICancellationSignal localICancellationSignal = ICancellationSignal.Stub.asInterface(localParcel2.readStrongBinder());
      return localICancellationSignal;
    }
    finally
    {
      localParcel1.recycle();
      localParcel2.recycle();
    }
  }
  
  public int delete(String paramString1, Uri paramUri, String paramString2, String[] paramArrayOfString)
    throws RemoteException
  {
    Parcel localParcel1 = Parcel.obtain();
    Parcel localParcel2 = Parcel.obtain();
    try
    {
      localParcel1.writeInterfaceToken("android.content.IContentProvider");
      localParcel1.writeString(paramString1);
      paramUri.writeToParcel(localParcel1, 0);
      localParcel1.writeString(paramString2);
      localParcel1.writeStringArray(paramArrayOfString);
      mRemote.transact(4, localParcel1, localParcel2, 0);
      DatabaseUtils.readExceptionFromParcel(localParcel2);
      int i = localParcel2.readInt();
      return i;
    }
    finally
    {
      localParcel1.recycle();
      localParcel2.recycle();
    }
  }
  
  public String[] getStreamTypes(Uri paramUri, String paramString)
    throws RemoteException
  {
    Parcel localParcel1 = Parcel.obtain();
    Parcel localParcel2 = Parcel.obtain();
    try
    {
      localParcel1.writeInterfaceToken("android.content.IContentProvider");
      paramUri.writeToParcel(localParcel1, 0);
      localParcel1.writeString(paramString);
      mRemote.transact(22, localParcel1, localParcel2, 0);
      DatabaseUtils.readExceptionFromParcel(localParcel2);
      paramUri = localParcel2.createStringArray();
      return paramUri;
    }
    finally
    {
      localParcel1.recycle();
      localParcel2.recycle();
    }
  }
  
  public String getType(Uri paramUri)
    throws RemoteException
  {
    Parcel localParcel1 = Parcel.obtain();
    Parcel localParcel2 = Parcel.obtain();
    try
    {
      localParcel1.writeInterfaceToken("android.content.IContentProvider");
      paramUri.writeToParcel(localParcel1, 0);
      mRemote.transact(2, localParcel1, localParcel2, 0);
      DatabaseUtils.readExceptionFromParcel(localParcel2);
      paramUri = localParcel2.readString();
      return paramUri;
    }
    finally
    {
      localParcel1.recycle();
      localParcel2.recycle();
    }
  }
  
  public Uri insert(String paramString, Uri paramUri, ContentValues paramContentValues)
    throws RemoteException
  {
    Parcel localParcel1 = Parcel.obtain();
    Parcel localParcel2 = Parcel.obtain();
    try
    {
      localParcel1.writeInterfaceToken("android.content.IContentProvider");
      localParcel1.writeString(paramString);
      paramUri.writeToParcel(localParcel1, 0);
      paramContentValues.writeToParcel(localParcel1, 0);
      mRemote.transact(3, localParcel1, localParcel2, 0);
      DatabaseUtils.readExceptionFromParcel(localParcel2);
      paramString = (Uri)Uri.CREATOR.createFromParcel(localParcel2);
      return paramString;
    }
    finally
    {
      localParcel1.recycle();
      localParcel2.recycle();
    }
  }
  
  public AssetFileDescriptor openAssetFile(String paramString1, Uri paramUri, String paramString2, ICancellationSignal paramICancellationSignal)
    throws RemoteException, FileNotFoundException
  {
    Parcel localParcel1 = Parcel.obtain();
    Parcel localParcel2 = Parcel.obtain();
    try
    {
      localParcel1.writeInterfaceToken("android.content.IContentProvider");
      localParcel1.writeString(paramString1);
      paramUri.writeToParcel(localParcel1, 0);
      localParcel1.writeString(paramString2);
      paramUri = null;
      if (paramICancellationSignal != null) {
        paramString1 = paramICancellationSignal.asBinder();
      } else {
        paramString1 = null;
      }
      localParcel1.writeStrongBinder(paramString1);
      mRemote.transact(15, localParcel1, localParcel2, 0);
      DatabaseUtils.readExceptionWithFileNotFoundExceptionFromParcel(localParcel2);
      paramString1 = paramUri;
      if (localParcel2.readInt() != 0) {
        paramString1 = (AssetFileDescriptor)AssetFileDescriptor.CREATOR.createFromParcel(localParcel2);
      }
      return paramString1;
    }
    finally
    {
      localParcel1.recycle();
      localParcel2.recycle();
    }
  }
  
  public ParcelFileDescriptor openFile(String paramString1, Uri paramUri, String paramString2, ICancellationSignal paramICancellationSignal, IBinder paramIBinder)
    throws RemoteException, FileNotFoundException
  {
    Parcel localParcel1 = Parcel.obtain();
    Parcel localParcel2 = Parcel.obtain();
    try
    {
      localParcel1.writeInterfaceToken("android.content.IContentProvider");
      localParcel1.writeString(paramString1);
      paramUri.writeToParcel(localParcel1, 0);
      localParcel1.writeString(paramString2);
      paramUri = null;
      if (paramICancellationSignal != null) {
        paramString1 = paramICancellationSignal.asBinder();
      } else {
        paramString1 = null;
      }
      localParcel1.writeStrongBinder(paramString1);
      localParcel1.writeStrongBinder(paramIBinder);
      mRemote.transact(14, localParcel1, localParcel2, 0);
      DatabaseUtils.readExceptionWithFileNotFoundExceptionFromParcel(localParcel2);
      paramString1 = paramUri;
      if (localParcel2.readInt() != 0) {
        paramString1 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(localParcel2);
      }
      return paramString1;
    }
    finally
    {
      localParcel1.recycle();
      localParcel2.recycle();
    }
  }
  
  public AssetFileDescriptor openTypedAssetFile(String paramString1, Uri paramUri, String paramString2, Bundle paramBundle, ICancellationSignal paramICancellationSignal)
    throws RemoteException, FileNotFoundException
  {
    Parcel localParcel1 = Parcel.obtain();
    Parcel localParcel2 = Parcel.obtain();
    try
    {
      localParcel1.writeInterfaceToken("android.content.IContentProvider");
      localParcel1.writeString(paramString1);
      paramUri.writeToParcel(localParcel1, 0);
      localParcel1.writeString(paramString2);
      localParcel1.writeBundle(paramBundle);
      paramUri = null;
      if (paramICancellationSignal != null) {
        paramString1 = paramICancellationSignal.asBinder();
      } else {
        paramString1 = null;
      }
      localParcel1.writeStrongBinder(paramString1);
      mRemote.transact(23, localParcel1, localParcel2, 0);
      DatabaseUtils.readExceptionWithFileNotFoundExceptionFromParcel(localParcel2);
      paramString1 = paramUri;
      if (localParcel2.readInt() != 0) {
        paramString1 = (AssetFileDescriptor)AssetFileDescriptor.CREATOR.createFromParcel(localParcel2);
      }
      return paramString1;
    }
    finally
    {
      localParcel1.recycle();
      localParcel2.recycle();
    }
  }
  
  /* Error */
  public android.database.Cursor query(String paramString, Uri paramUri, String[] paramArrayOfString, Bundle paramBundle, ICancellationSignal paramICancellationSignal)
    throws RemoteException
  {
    // Byte code:
    //   0: new 197	android/database/BulkCursorToCursorAdaptor
    //   3: dup
    //   4: invokespecial 198	android/database/BulkCursorToCursorAdaptor:<init>	()V
    //   7: astore 6
    //   9: invokestatic 28	android/os/Parcel:obtain	()Landroid/os/Parcel;
    //   12: astore 7
    //   14: invokestatic 28	android/os/Parcel:obtain	()Landroid/os/Parcel;
    //   17: astore 8
    //   19: aload 7
    //   21: ldc 30
    //   23: invokevirtual 34	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
    //   26: aload 7
    //   28: aload_1
    //   29: invokevirtual 37	android/os/Parcel:writeString	(Ljava/lang/String;)V
    //   32: aload_2
    //   33: aload 7
    //   35: iconst_0
    //   36: invokevirtual 104	android/net/Uri:writeToParcel	(Landroid/os/Parcel;I)V
    //   39: iconst_0
    //   40: istore 9
    //   42: aload_3
    //   43: ifnull +7 -> 50
    //   46: aload_3
    //   47: arraylength
    //   48: istore 9
    //   50: aload 7
    //   52: iload 9
    //   54: invokevirtual 47	android/os/Parcel:writeInt	(I)V
    //   57: iconst_0
    //   58: istore 10
    //   60: iload 10
    //   62: iload 9
    //   64: if_icmpge +18 -> 82
    //   67: aload 7
    //   69: aload_3
    //   70: iload 10
    //   72: aaload
    //   73: invokevirtual 37	android/os/Parcel:writeString	(Ljava/lang/String;)V
    //   76: iinc 10 1
    //   79: goto -19 -> 60
    //   82: aload 7
    //   84: aload 4
    //   86: invokevirtual 120	android/os/Parcel:writeBundle	(Landroid/os/Bundle;)V
    //   89: aload 7
    //   91: aload 6
    //   93: invokevirtual 202	android/database/BulkCursorToCursorAdaptor:getObserver	()Landroid/database/IContentObserver;
    //   96: invokeinterface 205 1 0
    //   101: invokevirtual 178	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
    //   104: aconst_null
    //   105: astore_2
    //   106: aload 5
    //   108: ifnull +14 -> 122
    //   111: aload 5
    //   113: invokeinterface 175 1 0
    //   118: astore_1
    //   119: goto +5 -> 124
    //   122: aconst_null
    //   123: astore_1
    //   124: aload 7
    //   126: aload_1
    //   127: invokevirtual 178	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
    //   130: aload_0
    //   131: getfield 15	android/content/ContentProviderProxy:mRemote	Landroid/os/IBinder;
    //   134: iconst_1
    //   135: aload 7
    //   137: aload 8
    //   139: iconst_0
    //   140: invokeinterface 73 5 0
    //   145: pop
    //   146: aload 8
    //   148: invokestatic 111	android/database/DatabaseUtils:readExceptionFromParcel	(Landroid/os/Parcel;)V
    //   151: aload 8
    //   153: invokevirtual 114	android/os/Parcel:readInt	()I
    //   156: ifeq +62 -> 218
    //   159: getstatic 208	android/database/BulkCursorDescriptor:CREATOR	Landroid/os/Parcelable$Creator;
    //   162: aload 8
    //   164: invokeinterface 133 2 0
    //   169: checkcast 207	android/database/BulkCursorDescriptor
    //   172: astore 4
    //   174: aload_0
    //   175: getfield 15	android/content/ContentProviderProxy:mRemote	Landroid/os/IBinder;
    //   178: astore_3
    //   179: aload_2
    //   180: astore_1
    //   181: aload 4
    //   183: getfield 212	android/database/BulkCursorDescriptor:cursor	Landroid/database/IBulkCursor;
    //   186: ifnull +14 -> 200
    //   189: aload 4
    //   191: getfield 212	android/database/BulkCursorDescriptor:cursor	Landroid/database/IBulkCursor;
    //   194: invokeinterface 215 1 0
    //   199: astore_1
    //   200: aload_3
    //   201: aload_1
    //   202: invokestatic 221	android/os/Binder:copyAllowBlocking	(Landroid/os/IBinder;Landroid/os/IBinder;)V
    //   205: aload 6
    //   207: aload 4
    //   209: invokevirtual 225	android/database/BulkCursorToCursorAdaptor:initialize	(Landroid/database/BulkCursorDescriptor;)V
    //   212: aload 6
    //   214: astore_1
    //   215: goto +10 -> 225
    //   218: aload 6
    //   220: invokevirtual 228	android/database/BulkCursorToCursorAdaptor:close	()V
    //   223: aconst_null
    //   224: astore_1
    //   225: aload 7
    //   227: invokevirtual 94	android/os/Parcel:recycle	()V
    //   230: aload 8
    //   232: invokevirtual 94	android/os/Parcel:recycle	()V
    //   235: aload_1
    //   236: areturn
    //   237: astore_1
    //   238: goto +19 -> 257
    //   241: astore_1
    //   242: aload 6
    //   244: invokevirtual 228	android/database/BulkCursorToCursorAdaptor:close	()V
    //   247: aload_1
    //   248: athrow
    //   249: astore_1
    //   250: aload 6
    //   252: invokevirtual 228	android/database/BulkCursorToCursorAdaptor:close	()V
    //   255: aload_1
    //   256: athrow
    //   257: aload 7
    //   259: invokevirtual 94	android/os/Parcel:recycle	()V
    //   262: aload 8
    //   264: invokevirtual 94	android/os/Parcel:recycle	()V
    //   267: aload_1
    //   268: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	269	0	this	ContentProviderProxy
    //   0	269	1	paramString	String
    //   0	269	2	paramUri	Uri
    //   0	269	3	paramArrayOfString	String[]
    //   0	269	4	paramBundle	Bundle
    //   0	269	5	paramICancellationSignal	ICancellationSignal
    //   7	244	6	localBulkCursorToCursorAdaptor	android.database.BulkCursorToCursorAdaptor
    //   12	246	7	localParcel1	Parcel
    //   17	246	8	localParcel2	Parcel
    //   40	25	9	i	int
    //   58	19	10	j	int
    // Exception table:
    //   from	to	target	type
    //   19	39	237	finally
    //   46	50	237	finally
    //   50	57	237	finally
    //   67	76	237	finally
    //   82	104	237	finally
    //   111	119	237	finally
    //   124	179	237	finally
    //   181	200	237	finally
    //   200	212	237	finally
    //   218	223	237	finally
    //   242	249	237	finally
    //   250	257	237	finally
    //   19	39	241	java/lang/RuntimeException
    //   46	50	241	java/lang/RuntimeException
    //   50	57	241	java/lang/RuntimeException
    //   67	76	241	java/lang/RuntimeException
    //   82	104	241	java/lang/RuntimeException
    //   111	119	241	java/lang/RuntimeException
    //   124	179	241	java/lang/RuntimeException
    //   181	200	241	java/lang/RuntimeException
    //   200	212	241	java/lang/RuntimeException
    //   218	223	241	java/lang/RuntimeException
    //   19	39	249	android/os/RemoteException
    //   46	50	249	android/os/RemoteException
    //   50	57	249	android/os/RemoteException
    //   67	76	249	android/os/RemoteException
    //   82	104	249	android/os/RemoteException
    //   111	119	249	android/os/RemoteException
    //   124	179	249	android/os/RemoteException
    //   181	200	249	android/os/RemoteException
    //   200	212	249	android/os/RemoteException
    //   218	223	249	android/os/RemoteException
  }
  
  public boolean refresh(String paramString, Uri paramUri, Bundle paramBundle, ICancellationSignal paramICancellationSignal)
    throws RemoteException
  {
    Parcel localParcel1 = Parcel.obtain();
    Parcel localParcel2 = Parcel.obtain();
    try
    {
      localParcel1.writeInterfaceToken("android.content.IContentProvider");
      localParcel1.writeString(paramString);
      boolean bool = false;
      paramUri.writeToParcel(localParcel1, 0);
      localParcel1.writeBundle(paramBundle);
      if (paramICancellationSignal != null) {
        paramString = paramICancellationSignal.asBinder();
      } else {
        paramString = null;
      }
      localParcel1.writeStrongBinder(paramString);
      mRemote.transact(27, localParcel1, localParcel2, 0);
      DatabaseUtils.readExceptionFromParcel(localParcel2);
      int i = localParcel2.readInt();
      if (i == 0) {
        bool = true;
      }
      return bool;
    }
    finally
    {
      localParcel1.recycle();
      localParcel2.recycle();
    }
  }
  
  public Uri uncanonicalize(String paramString, Uri paramUri)
    throws RemoteException
  {
    Parcel localParcel1 = Parcel.obtain();
    Parcel localParcel2 = Parcel.obtain();
    try
    {
      localParcel1.writeInterfaceToken("android.content.IContentProvider");
      localParcel1.writeString(paramString);
      paramUri.writeToParcel(localParcel1, 0);
      mRemote.transact(26, localParcel1, localParcel2, 0);
      DatabaseUtils.readExceptionFromParcel(localParcel2);
      paramString = (Uri)Uri.CREATOR.createFromParcel(localParcel2);
      return paramString;
    }
    finally
    {
      localParcel1.recycle();
      localParcel2.recycle();
    }
  }
  
  public int update(String paramString1, Uri paramUri, ContentValues paramContentValues, String paramString2, String[] paramArrayOfString)
    throws RemoteException
  {
    Parcel localParcel1 = Parcel.obtain();
    Parcel localParcel2 = Parcel.obtain();
    try
    {
      localParcel1.writeInterfaceToken("android.content.IContentProvider");
      localParcel1.writeString(paramString1);
      paramUri.writeToParcel(localParcel1, 0);
      paramContentValues.writeToParcel(localParcel1, 0);
      localParcel1.writeString(paramString2);
      localParcel1.writeStringArray(paramArrayOfString);
      mRemote.transact(10, localParcel1, localParcel2, 0);
      DatabaseUtils.readExceptionFromParcel(localParcel2);
      int i = localParcel2.readInt();
      return i;
    }
    finally
    {
      localParcel1.recycle();
      localParcel2.recycle();
    }
  }
}
