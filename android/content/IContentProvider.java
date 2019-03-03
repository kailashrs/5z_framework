package android.content;

import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.IInterface;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public abstract interface IContentProvider
  extends IInterface
{
  public static final int APPLY_BATCH_TRANSACTION = 20;
  public static final int BULK_INSERT_TRANSACTION = 13;
  public static final int CALL_TRANSACTION = 21;
  public static final int CANONICALIZE_TRANSACTION = 25;
  public static final int CREATE_CANCELATION_SIGNAL_TRANSACTION = 24;
  public static final int DELETE_TRANSACTION = 4;
  public static final int GET_STREAM_TYPES_TRANSACTION = 22;
  public static final int GET_TYPE_TRANSACTION = 2;
  public static final int INSERT_TRANSACTION = 3;
  public static final int OPEN_ASSET_FILE_TRANSACTION = 15;
  public static final int OPEN_FILE_TRANSACTION = 14;
  public static final int OPEN_TYPED_ASSET_FILE_TRANSACTION = 23;
  public static final int QUERY_TRANSACTION = 1;
  public static final int REFRESH_TRANSACTION = 27;
  public static final int UNCANONICALIZE_TRANSACTION = 26;
  public static final int UPDATE_TRANSACTION = 10;
  public static final String descriptor = "android.content.IContentProvider";
  
  public abstract ContentProviderResult[] applyBatch(String paramString, ArrayList<ContentProviderOperation> paramArrayList)
    throws RemoteException, OperationApplicationException;
  
  public abstract int bulkInsert(String paramString, Uri paramUri, ContentValues[] paramArrayOfContentValues)
    throws RemoteException;
  
  public abstract Bundle call(String paramString1, String paramString2, String paramString3, Bundle paramBundle)
    throws RemoteException;
  
  public abstract Uri canonicalize(String paramString, Uri paramUri)
    throws RemoteException;
  
  public abstract ICancellationSignal createCancellationSignal()
    throws RemoteException;
  
  public abstract int delete(String paramString1, Uri paramUri, String paramString2, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract String[] getStreamTypes(Uri paramUri, String paramString)
    throws RemoteException;
  
  public abstract String getType(Uri paramUri)
    throws RemoteException;
  
  public abstract Uri insert(String paramString, Uri paramUri, ContentValues paramContentValues)
    throws RemoteException;
  
  public abstract AssetFileDescriptor openAssetFile(String paramString1, Uri paramUri, String paramString2, ICancellationSignal paramICancellationSignal)
    throws RemoteException, FileNotFoundException;
  
  public abstract ParcelFileDescriptor openFile(String paramString1, Uri paramUri, String paramString2, ICancellationSignal paramICancellationSignal, IBinder paramIBinder)
    throws RemoteException, FileNotFoundException;
  
  public abstract AssetFileDescriptor openTypedAssetFile(String paramString1, Uri paramUri, String paramString2, Bundle paramBundle, ICancellationSignal paramICancellationSignal)
    throws RemoteException, FileNotFoundException;
  
  public abstract Cursor query(String paramString, Uri paramUri, String[] paramArrayOfString, Bundle paramBundle, ICancellationSignal paramICancellationSignal)
    throws RemoteException;
  
  public abstract boolean refresh(String paramString, Uri paramUri, Bundle paramBundle, ICancellationSignal paramICancellationSignal)
    throws RemoteException;
  
  public abstract Uri uncanonicalize(String paramString, Uri paramUri)
    throws RemoteException;
  
  public abstract int update(String paramString1, Uri paramUri, ContentValues paramContentValues, String paramString2, String[] paramArrayOfString)
    throws RemoteException;
}
