package com.android.internal.os;

import android.os.RemoteException;
import android.os.ServiceManager;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RegionalizationEnvironment
{
  private static final boolean DEBUG = true;
  public static final String ISREGIONAL_APP = "app";
  public static final String ISREGIONAL_FRAMEWORK = "Framework";
  private static final String SPEC_FILE_PATH = "/persist/speccfg/spec";
  private static final boolean SUPPORTED = false;
  private static final String TAG = "RegionalizationEnvironment";
  private static boolean isLoaded = false;
  private static ArrayList<String> mExcludedApps;
  private static ArrayList<Package> mPackages;
  private static IRegionalizationService mRegionalizationService = null;
  
  static
  {
    mPackages = new ArrayList();
    mExcludedApps = new ArrayList();
  }
  
  public RegionalizationEnvironment() {}
  
  public static List<File> getAllPackageDirectories()
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = mPackages.iterator();
    while (localIterator.hasNext())
    {
      Package localPackage = (Package)localIterator.next();
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Package Directoriy(");
      localStringBuilder.append(localPackage.getPriority());
      localStringBuilder.append("):");
      localStringBuilder.append(localPackage.getDirectory());
      Log.v("RegionalizationEnvironment", localStringBuilder.toString());
      localArrayList.add(localPackage.getDirectory());
    }
    return localArrayList;
  }
  
  public static List<String> getAllPackageNames()
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = mPackages.iterator();
    while (localIterator.hasNext()) {
      localArrayList.add(((Package)localIterator.next()).getName());
    }
    return localArrayList;
  }
  
  public static int getPackagesCount()
  {
    return mPackages.size();
  }
  
  public static IRegionalizationService getRegionalizationService()
  {
    return mRegionalizationService;
  }
  
  public static String getStoragePos()
  {
    Object localObject1 = mPackages.iterator();
    Object localObject2;
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = ((Package)((Iterator)localObject1).next()).getStoragePos();
      if (!TextUtils.isEmpty((CharSequence)localObject2)) {
        return localObject2;
      }
    }
    try
    {
      mPackages.clear();
      localObject1 = new java/io/IOException;
      ((IOException)localObject1).<init>("Read wrong package for Carrier!");
      throw ((Throwable)localObject1);
    }
    catch (IOException localIOException)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("Get storage pos error, caused by: ");
      ((StringBuilder)localObject2).append(localIOException.getMessage());
      Log.e("RegionalizationEnvironment", ((StringBuilder)localObject2).toString());
    }
    return "";
  }
  
  private static void init()
  {
    mRegionalizationService = IRegionalizationService.Stub.asInterface(ServiceManager.getService("regionalization"));
    if (mRegionalizationService != null)
    {
      loadSwitchedPackages();
      loadExcludedApplist();
      isLoaded = true;
    }
  }
  
  public static boolean isExcludedApp(String paramString)
  {
    if (getPackagesCount() == 0) {
      return false;
    }
    if (!paramString.endsWith(".apk"))
    {
      ArrayList localArrayList = mExcludedApps;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append(".apk");
      return localArrayList.contains(localStringBuilder.toString());
    }
    return mExcludedApps.contains(paramString);
  }
  
  public static boolean isRegionalizationCarrierOverlayPackage(String paramString1, String paramString2)
  {
    if ((isSupported()) && (paramString1 != null))
    {
      Object localObject = getAllPackageNames();
      int i = 0;
      localObject = ((List)localObject).iterator();
      while (((Iterator)localObject).hasNext())
      {
        if (paramString1.indexOf((String)((Iterator)localObject).next()) != -1) {
          i = 1;
        }
        if ((i != 0) && (((paramString2.equals("Framework")) && (paramString1.indexOf("Framework") != -1)) || (paramString2.equals("app")))) {
          return true;
        }
      }
      return false;
    }
    return false;
  }
  
  public static boolean isSupported()
  {
    return false;
  }
  
  private static void loadExcludedApplist()
  {
    Log.d("RegionalizationEnvironment", "loadExcludedApps!");
    if (getPackagesCount() == 0) {
      return;
    }
    Iterator localIterator = mPackages.iterator();
    while (localIterator.hasNext())
    {
      Object localObject1 = (Package)localIterator.next();
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("load excluded apps for ");
      ((StringBuilder)localObject2).append(((Package)localObject1).getDirectory());
      Log.d("RegionalizationEnvironment", ((StringBuilder)localObject2).toString());
      localObject2 = ((Package)localObject1).getExcludedListFilePath();
      localObject1 = null;
      try
      {
        localObject2 = (ArrayList)mRegionalizationService.readFile((String)localObject2, null);
        localObject1 = localObject2;
      }
      catch (RemoteException localRemoteException)
      {
        localRemoteException.printStackTrace();
      }
      if ((localObject1 != null) && (((ArrayList)localObject1).size() > 0))
      {
        localObject1 = ((ArrayList)localObject1).iterator();
        while (((Iterator)localObject1).hasNext())
        {
          String str = (String)((Iterator)localObject1).next();
          if (!TextUtils.isEmpty(str))
          {
            int i = str.lastIndexOf("/");
            if (i != -1)
            {
              str = str.substring(i + 1);
              if ((!TextUtils.isEmpty(str)) && (!mExcludedApps.contains(str))) {
                mExcludedApps.add(str);
              }
            }
          }
        }
      }
    }
  }
  
  private static void loadSwitchedPackages()
  {
    Log.d("RegionalizationEnvironment", "load packages for Carrier!");
    Object localObject1 = null;
    try
    {
      ArrayList localArrayList1 = (ArrayList)mRegionalizationService.readFile("/persist/speccfg/spec", null);
      localObject1 = localArrayList1;
    }
    catch (IOException localIOException)
    {
      break label400;
    }
    catch (RemoteException localRemoteException1)
    {
      localRemoteException1.printStackTrace();
    }
    String str1;
    if ((localObject1 != null) && (((ArrayList)localObject1).size() > 0))
    {
      if (((String)((ArrayList)localObject1).get(0)).startsWith("packStorage="))
      {
        str1 = ((String)((ArrayList)localObject1).get(0)).substring("packStorage=".length());
        if (!TextUtils.isEmpty(str1))
        {
          if (((String)((ArrayList)localObject1).get(1)).matches("^packCount=[0-9]$"))
          {
            int i = Integer.parseInt(((String)((ArrayList)localObject1).get(1)).substring("packCount=".length()));
            if ((i > 0) && (((ArrayList)localObject1).size() > i))
            {
              for (int j = 2;; j++)
              {
                if (j >= i + 2) {
                  return;
                }
                if (!((String)((ArrayList)localObject1).get(j)).matches("^strSpec[0-9]=\\w+$")) {
                  break;
                }
                String str2 = ((String)((ArrayList)localObject1).get(j)).substring("strSpec".length() + 2);
                boolean bool1 = TextUtils.isEmpty(str2);
                if (!bool1)
                {
                  boolean bool2 = false;
                  Object localObject2;
                  try
                  {
                    localObject2 = mRegionalizationService;
                    StringBuilder localStringBuilder = new java/lang/StringBuilder;
                    localStringBuilder.<init>();
                    localStringBuilder.append(str1);
                    localStringBuilder.append("/");
                    localStringBuilder.append(str2);
                    bool1 = ((IRegionalizationService)localObject2).checkFileExists(localStringBuilder.toString());
                  }
                  catch (RemoteException localRemoteException2)
                  {
                    localRemoteException2.printStackTrace();
                    bool1 = bool2;
                  }
                  if (bool1)
                  {
                    ArrayList localArrayList2 = mPackages;
                    localObject2 = new com/android/internal/os/RegionalizationEnvironment$Package;
                    ((Package)localObject2).<init>(str2, j, str1);
                    localArrayList2.add(localObject2);
                  }
                  else
                  {
                    mPackages.clear();
                    localObject1 = new java/io/IOException;
                    ((IOException)localObject1).<init>("Read wrong packages for Carrier!");
                    throw ((Throwable)localObject1);
                  }
                }
              }
              mPackages.clear();
              localObject1 = new java/io/IOException;
              ((IOException)localObject1).<init>("Read wrong packages for Carrier!");
              throw ((Throwable)localObject1);
            }
            localObject1 = new java/io/IOException;
            ((IOException)localObject1).<init>("Package count of Carrier is wrong!");
            throw ((Throwable)localObject1);
          }
          localObject1 = new java/io/IOException;
          ((IOException)localObject1).<init>("Can't read package count of Carrier!");
          throw ((Throwable)localObject1);
        }
        localObject1 = new java/io/IOException;
        ((IOException)localObject1).<init>("Storage pos for Carrier package is wrong!");
        throw ((Throwable)localObject1);
      }
      localObject1 = new java/io/IOException;
      ((IOException)localObject1).<init>("Can't read storage pos for Carrier package!");
      throw ((Throwable)localObject1);
    }
    return;
    label400:
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("Load package for carrier error, caused by: ");
    ((StringBuilder)localObject1).append(str1.getMessage());
    Log.e("RegionalizationEnvironment", ((StringBuilder)localObject1).toString());
  }
  
  private static class Package
  {
    private final String mName;
    private final int mPriority;
    private final String mStorage;
    
    public Package(String paramString1, int paramInt, String paramString2)
    {
      mName = paramString1;
      mPriority = paramInt;
      mStorage = paramString2;
    }
    
    public File getDirectory()
    {
      return new File(mStorage, mName);
    }
    
    public String getExcludedListFilePath()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(getDirectory().getAbsolutePath());
      localStringBuilder.append("/exclude.list");
      return localStringBuilder.toString();
    }
    
    public String getName()
    {
      return mName;
    }
    
    public int getPriority()
    {
      return mPriority;
    }
    
    public String getStoragePos()
    {
      return mStorage;
    }
  }
}
