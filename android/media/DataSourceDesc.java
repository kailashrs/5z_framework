package android.media;

import android.content.Context;
import android.net.Uri;
import com.android.internal.util.Preconditions;
import java.io.FileDescriptor;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DataSourceDesc
{
  public static final long LONG_MAX = 576460752303423487L;
  public static final int TYPE_CALLBACK = 1;
  public static final int TYPE_FD = 2;
  public static final int TYPE_NONE = 0;
  public static final int TYPE_URI = 3;
  private long mEndPositionMs = 576460752303423487L;
  private FileDescriptor mFD;
  private long mFDLength = 576460752303423487L;
  private long mFDOffset = 0L;
  private Media2DataSource mMedia2DataSource;
  private String mMediaId;
  private long mStartPositionMs = 0L;
  private int mType = 0;
  private Uri mUri;
  private Context mUriContext;
  private List<HttpCookie> mUriCookies;
  private Map<String, String> mUriHeader;
  
  private DataSourceDesc() {}
  
  public long getEndPosition()
  {
    return mEndPositionMs;
  }
  
  public FileDescriptor getFileDescriptor()
  {
    return mFD;
  }
  
  public long getFileDescriptorLength()
  {
    return mFDLength;
  }
  
  public long getFileDescriptorOffset()
  {
    return mFDOffset;
  }
  
  public Media2DataSource getMedia2DataSource()
  {
    return mMedia2DataSource;
  }
  
  public String getMediaId()
  {
    return mMediaId;
  }
  
  public long getStartPosition()
  {
    return mStartPositionMs;
  }
  
  public int getType()
  {
    return mType;
  }
  
  public Uri getUri()
  {
    return mUri;
  }
  
  public Context getUriContext()
  {
    return mUriContext;
  }
  
  public List<HttpCookie> getUriCookies()
  {
    if (mUriCookies == null) {
      return null;
    }
    return new ArrayList(mUriCookies);
  }
  
  public Map<String, String> getUriHeaders()
  {
    if (mUriHeader == null) {
      return null;
    }
    return new HashMap(mUriHeader);
  }
  
  public static class Builder
  {
    private long mEndPositionMs = 576460752303423487L;
    private FileDescriptor mFD;
    private long mFDLength = 576460752303423487L;
    private long mFDOffset = 0L;
    private Media2DataSource mMedia2DataSource;
    private String mMediaId;
    private long mStartPositionMs = 0L;
    private int mType = 0;
    private Uri mUri;
    private Context mUriContext;
    private List<HttpCookie> mUriCookies;
    private Map<String, String> mUriHeader;
    
    public Builder() {}
    
    public Builder(DataSourceDesc paramDataSourceDesc)
    {
      mType = mType;
      mMedia2DataSource = mMedia2DataSource;
      mFD = mFD;
      mFDOffset = mFDOffset;
      mFDLength = mFDLength;
      mUri = mUri;
      mUriHeader = mUriHeader;
      mUriCookies = mUriCookies;
      mUriContext = mUriContext;
      mMediaId = mMediaId;
      mStartPositionMs = mStartPositionMs;
      mEndPositionMs = mEndPositionMs;
    }
    
    private void resetDataSource()
    {
      mType = 0;
      mMedia2DataSource = null;
      mFD = null;
      mFDOffset = 0L;
      mFDLength = 576460752303423487L;
      mUri = null;
      mUriHeader = null;
      mUriCookies = null;
      mUriContext = null;
    }
    
    public DataSourceDesc build()
    {
      if ((mType != 1) && (mType != 2) && (mType != 3))
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Illegal type: ");
        ((StringBuilder)localObject).append(mType);
        throw new IllegalStateException(((StringBuilder)localObject).toString());
      }
      if (mStartPositionMs <= mEndPositionMs)
      {
        localObject = new DataSourceDesc(null);
        DataSourceDesc.access$002((DataSourceDesc)localObject, mType);
        DataSourceDesc.access$102((DataSourceDesc)localObject, mMedia2DataSource);
        DataSourceDesc.access$202((DataSourceDesc)localObject, mFD);
        DataSourceDesc.access$302((DataSourceDesc)localObject, mFDOffset);
        DataSourceDesc.access$402((DataSourceDesc)localObject, mFDLength);
        DataSourceDesc.access$502((DataSourceDesc)localObject, mUri);
        DataSourceDesc.access$602((DataSourceDesc)localObject, mUriHeader);
        DataSourceDesc.access$702((DataSourceDesc)localObject, mUriCookies);
        DataSourceDesc.access$802((DataSourceDesc)localObject, mUriContext);
        DataSourceDesc.access$902((DataSourceDesc)localObject, mMediaId);
        DataSourceDesc.access$1002((DataSourceDesc)localObject, mStartPositionMs);
        DataSourceDesc.access$1102((DataSourceDesc)localObject, mEndPositionMs);
        return localObject;
      }
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Illegal start/end position: ");
      ((StringBuilder)localObject).append(mStartPositionMs);
      ((StringBuilder)localObject).append(" : ");
      ((StringBuilder)localObject).append(mEndPositionMs);
      throw new IllegalStateException(((StringBuilder)localObject).toString());
    }
    
    public Builder setDataSource(Context paramContext, Uri paramUri)
    {
      Preconditions.checkNotNull(paramContext, "context cannot be null");
      Preconditions.checkNotNull(paramUri, "uri cannot be null");
      resetDataSource();
      mType = 3;
      mUri = paramUri;
      mUriContext = paramContext;
      return this;
    }
    
    public Builder setDataSource(Context paramContext, Uri paramUri, Map<String, String> paramMap, List<HttpCookie> paramList)
    {
      Preconditions.checkNotNull(paramContext, "context cannot be null");
      Preconditions.checkNotNull(paramUri);
      if (paramList != null)
      {
        CookieHandler localCookieHandler = CookieHandler.getDefault();
        if ((localCookieHandler != null) && (!(localCookieHandler instanceof CookieManager))) {
          throw new IllegalArgumentException("The cookie handler has to be of CookieManager type when cookies are provided.");
        }
      }
      resetDataSource();
      mType = 3;
      mUri = paramUri;
      if (paramMap != null) {
        mUriHeader = new HashMap(paramMap);
      }
      if (paramList != null) {
        mUriCookies = new ArrayList(paramList);
      }
      mUriContext = paramContext;
      return this;
    }
    
    public Builder setDataSource(Media2DataSource paramMedia2DataSource)
    {
      Preconditions.checkNotNull(paramMedia2DataSource);
      resetDataSource();
      mType = 1;
      mMedia2DataSource = paramMedia2DataSource;
      return this;
    }
    
    public Builder setDataSource(FileDescriptor paramFileDescriptor)
    {
      Preconditions.checkNotNull(paramFileDescriptor);
      resetDataSource();
      mType = 2;
      mFD = paramFileDescriptor;
      return this;
    }
    
    public Builder setDataSource(FileDescriptor paramFileDescriptor, long paramLong1, long paramLong2)
    {
      Preconditions.checkNotNull(paramFileDescriptor);
      long l = paramLong1;
      if (paramLong1 < 0L) {
        l = 0L;
      }
      paramLong1 = paramLong2;
      if (paramLong2 < 0L) {
        paramLong1 = 576460752303423487L;
      }
      resetDataSource();
      mType = 2;
      mFD = paramFileDescriptor;
      mFDOffset = l;
      mFDLength = paramLong1;
      return this;
    }
    
    public Builder setEndPosition(long paramLong)
    {
      long l = paramLong;
      if (paramLong < 0L) {
        l = 576460752303423487L;
      }
      mEndPositionMs = l;
      return this;
    }
    
    public Builder setMediaId(String paramString)
    {
      mMediaId = paramString;
      return this;
    }
    
    public Builder setStartPosition(long paramLong)
    {
      long l = paramLong;
      if (paramLong < 0L) {
        l = 0L;
      }
      mStartPositionMs = l;
      return this;
    }
  }
}
