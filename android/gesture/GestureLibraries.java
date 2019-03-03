package android.gesture;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public final class GestureLibraries
{
  private GestureLibraries() {}
  
  public static GestureLibrary fromFile(File paramFile)
  {
    return new FileGestureLibrary(paramFile);
  }
  
  public static GestureLibrary fromFile(String paramString)
  {
    return fromFile(new File(paramString));
  }
  
  public static GestureLibrary fromPrivateFile(Context paramContext, String paramString)
  {
    return fromFile(paramContext.getFileStreamPath(paramString));
  }
  
  public static GestureLibrary fromRawResource(Context paramContext, int paramInt)
  {
    return new ResourceGestureLibrary(paramContext, paramInt);
  }
  
  private static class FileGestureLibrary
    extends GestureLibrary
  {
    private final File mPath;
    
    public FileGestureLibrary(File paramFile)
    {
      mPath = paramFile;
    }
    
    public boolean isReadOnly()
    {
      return mPath.canWrite() ^ true;
    }
    
    public boolean load()
    {
      boolean bool1 = false;
      boolean bool2 = false;
      File localFile = mPath;
      boolean bool3 = bool1;
      if (localFile.exists())
      {
        bool3 = bool1;
        if (localFile.canRead()) {
          try
          {
            localObject = mStore;
            FileInputStream localFileInputStream = new java/io/FileInputStream;
            localFileInputStream.<init>(localFile);
            ((GestureStore)localObject).load(localFileInputStream, true);
            bool3 = true;
          }
          catch (IOException localIOException)
          {
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append("Could not load the gesture library from ");
            ((StringBuilder)localObject).append(mPath);
            Log.d("Gestures", ((StringBuilder)localObject).toString(), localIOException);
            bool3 = bool1;
          }
          catch (FileNotFoundException localFileNotFoundException)
          {
            for (;;)
            {
              Object localObject = new StringBuilder();
              ((StringBuilder)localObject).append("Could not load the gesture library from ");
              ((StringBuilder)localObject).append(mPath);
              Log.d("Gestures", ((StringBuilder)localObject).toString(), localFileNotFoundException);
              bool3 = bool2;
            }
          }
        }
      }
      return bool3;
    }
    
    public boolean save()
    {
      if (!mStore.hasChanged()) {
        return true;
      }
      Object localObject1 = mPath;
      Object localObject2 = ((File)localObject1).getParentFile();
      if ((!((File)localObject2).exists()) && (!((File)localObject2).mkdirs())) {
        return false;
      }
      boolean bool1 = false;
      boolean bool2 = false;
      try
      {
        ((File)localObject1).createNewFile();
        localObject2 = mStore;
        FileOutputStream localFileOutputStream = new java/io/FileOutputStream;
        localFileOutputStream.<init>((File)localObject1);
        ((GestureStore)localObject2).save(localFileOutputStream, true);
        bool2 = true;
      }
      catch (IOException localIOException)
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("Could not save the gesture library in ");
        ((StringBuilder)localObject1).append(mPath);
        Log.d("Gestures", ((StringBuilder)localObject1).toString(), localIOException);
        bool2 = bool1;
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        for (;;)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Could not save the gesture library in ");
          localStringBuilder.append(mPath);
          Log.d("Gestures", localStringBuilder.toString(), localFileNotFoundException);
        }
      }
      return bool2;
    }
  }
  
  private static class ResourceGestureLibrary
    extends GestureLibrary
  {
    private final WeakReference<Context> mContext;
    private final int mResourceId;
    
    public ResourceGestureLibrary(Context paramContext, int paramInt)
    {
      mContext = new WeakReference(paramContext);
      mResourceId = paramInt;
    }
    
    public boolean isReadOnly()
    {
      return true;
    }
    
    public boolean load()
    {
      boolean bool1 = false;
      Context localContext = (Context)mContext.get();
      boolean bool2 = bool1;
      if (localContext != null)
      {
        InputStream localInputStream = localContext.getResources().openRawResource(mResourceId);
        try
        {
          mStore.load(localInputStream, true);
          bool2 = true;
        }
        catch (IOException localIOException)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Could not load the gesture library from raw resource ");
          localStringBuilder.append(localContext.getResources().getResourceName(mResourceId));
          Log.d("Gestures", localStringBuilder.toString(), localIOException);
          bool2 = bool1;
        }
      }
      return bool2;
    }
    
    public boolean save()
    {
      return false;
    }
  }
}
