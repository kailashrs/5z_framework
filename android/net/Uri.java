package android.net;

import android.content.Intent;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.StrictMode;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Set;
import libcore.net.UriCodec;

public abstract class Uri
  implements Parcelable, Comparable<Uri>
{
  public static final Parcelable.Creator<Uri> CREATOR = new Parcelable.Creator()
  {
    public Uri createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      switch (i)
      {
      default: 
        paramAnonymousParcel = new StringBuilder();
        paramAnonymousParcel.append("Unknown URI type: ");
        paramAnonymousParcel.append(i);
        throw new IllegalArgumentException(paramAnonymousParcel.toString());
      case 3: 
        return Uri.HierarchicalUri.readFrom(paramAnonymousParcel);
      case 2: 
        return Uri.OpaqueUri.readFrom(paramAnonymousParcel);
      case 1: 
        return Uri.StringUri.readFrom(paramAnonymousParcel);
      }
      return null;
    }
    
    public Uri[] newArray(int paramAnonymousInt)
    {
      return new Uri[paramAnonymousInt];
    }
  };
  private static final String DEFAULT_ENCODING = "UTF-8";
  public static final Uri EMPTY;
  private static final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
  private static final String LOG = Uri.class.getSimpleName();
  private static final String NOT_CACHED = new String("NOT CACHED");
  private static final int NOT_CALCULATED = -2;
  private static final int NOT_FOUND = -1;
  private static final String NOT_HIERARCHICAL = "This isn't a hierarchical URI.";
  private static final int NULL_TYPE_ID = 0;
  
  static
  {
    EMPTY = new HierarchicalUri(null, Part.NULL, PathPart.EMPTY, Part.NULL, Part.NULL, null);
  }
  
  private Uri() {}
  
  public static String decode(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    return UriCodec.decode(paramString, false, StandardCharsets.UTF_8, false);
  }
  
  public static String encode(String paramString)
  {
    return encode(paramString, null);
  }
  
  public static String encode(String paramString1, String paramString2)
  {
    if (paramString1 == null) {
      return null;
    }
    int i = paramString1.length();
    Object localObject1 = null;
    int j = 0;
    while (j < i)
    {
      for (int k = j; (k < i) && (isAllowed(paramString1.charAt(k), paramString2)); k++) {}
      if (k == i)
      {
        if (j == 0) {
          return paramString1;
        }
        ((StringBuilder)localObject1).append(paramString1, j, i);
        return ((StringBuilder)localObject1).toString();
      }
      Object localObject2 = localObject1;
      if (localObject1 == null) {
        localObject2 = new StringBuilder();
      }
      if (k > j) {
        ((StringBuilder)localObject2).append(paramString1, j, k);
      }
      for (j = k + 1; (j < i) && (!isAllowed(paramString1.charAt(j), paramString2)); j++) {}
      localObject1 = paramString1.substring(k, j);
      try
      {
        localObject1 = ((String)localObject1).getBytes("UTF-8");
        int m = localObject1.length;
        for (k = 0; k < m; k++)
        {
          ((StringBuilder)localObject2).append('%');
          ((StringBuilder)localObject2).append(HEX_DIGITS[((localObject1[k] & 0xF0) >> 4)]);
          ((StringBuilder)localObject2).append(HEX_DIGITS[(localObject1[k] & 0xF)]);
        }
        localObject1 = localObject2;
      }
      catch (UnsupportedEncodingException paramString1)
      {
        throw new AssertionError(paramString1);
      }
    }
    if (localObject1 != null) {
      paramString1 = ((StringBuilder)localObject1).toString();
    }
    return paramString1;
  }
  
  public static Uri fromFile(File paramFile)
  {
    if (paramFile != null)
    {
      paramFile = PathPart.fromDecoded(paramFile.getAbsolutePath());
      return new HierarchicalUri("file", Part.EMPTY, paramFile, Part.NULL, Part.NULL, null);
    }
    throw new NullPointerException("file");
  }
  
  public static Uri fromParts(String paramString1, String paramString2, String paramString3)
  {
    if (paramString1 != null)
    {
      if (paramString2 != null) {
        return new OpaqueUri(paramString1, Part.fromDecoded(paramString2), Part.fromDecoded(paramString3), null);
      }
      throw new NullPointerException("ssp");
    }
    throw new NullPointerException("scheme");
  }
  
  private static boolean isAllowed(char paramChar, String paramString)
  {
    boolean bool;
    if (((paramChar < 'A') || (paramChar > 'Z')) && ((paramChar < 'a') || (paramChar > 'z')) && ((paramChar < '0') || (paramChar > '9')) && ("_-!.~'()*".indexOf(paramChar) == -1) && ((paramString == null) || (paramString.indexOf(paramChar) == -1))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static Uri parse(String paramString)
  {
    return new StringUri(paramString, null);
  }
  
  public static Uri withAppendedPath(Uri paramUri, String paramString)
  {
    return paramUri.buildUpon().appendEncodedPath(paramString).build();
  }
  
  public static void writeToParcel(Parcel paramParcel, Uri paramUri)
  {
    if (paramUri == null) {
      paramParcel.writeInt(0);
    } else {
      paramUri.writeToParcel(paramParcel, 0);
    }
  }
  
  public abstract Builder buildUpon();
  
  public void checkContentUriWithoutPermission(String paramString, int paramInt)
  {
    if (("content".equals(getScheme())) && (!Intent.isAccessUriMode(paramInt))) {
      StrictMode.onContentUriWithoutPermission(this, paramString);
    }
  }
  
  public void checkFileUriExposed(String paramString)
  {
    if (("file".equals(getScheme())) && (getPath() != null) && (!getPath().startsWith("/system/"))) {
      StrictMode.onFileUriExposed(this, paramString);
    }
  }
  
  public int compareTo(Uri paramUri)
  {
    return toString().compareTo(paramUri.toString());
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof Uri)) {
      return false;
    }
    paramObject = (Uri)paramObject;
    return toString().equals(paramObject.toString());
  }
  
  public abstract String getAuthority();
  
  public boolean getBooleanQueryParameter(String paramString, boolean paramBoolean)
  {
    paramString = getQueryParameter(paramString);
    if (paramString == null) {
      return paramBoolean;
    }
    paramString = paramString.toLowerCase(Locale.ROOT);
    if ((!"false".equals(paramString)) && (!"0".equals(paramString))) {
      paramBoolean = true;
    } else {
      paramBoolean = false;
    }
    return paramBoolean;
  }
  
  public Uri getCanonicalUri()
  {
    if ("file".equals(getScheme())) {
      try
      {
        Object localObject = new java/io/File;
        ((File)localObject).<init>(getPath());
        localObject = ((File)localObject).getCanonicalPath();
        if (Environment.isExternalStorageEmulated())
        {
          String str = Environment.getLegacyExternalStorageDirectory().toString();
          if (((String)localObject).startsWith(str)) {
            return fromFile(new File(Environment.getExternalStorageDirectory().toString(), ((String)localObject).substring(str.length() + 1)));
          }
        }
        return fromFile(new File((String)localObject));
      }
      catch (IOException localIOException)
      {
        return this;
      }
    }
    return this;
  }
  
  public abstract String getEncodedAuthority();
  
  public abstract String getEncodedFragment();
  
  public abstract String getEncodedPath();
  
  public abstract String getEncodedQuery();
  
  public abstract String getEncodedSchemeSpecificPart();
  
  public abstract String getEncodedUserInfo();
  
  public abstract String getFragment();
  
  public abstract String getHost();
  
  public abstract String getLastPathSegment();
  
  public abstract String getPath();
  
  public abstract List<String> getPathSegments();
  
  public abstract int getPort();
  
  public abstract String getQuery();
  
  public String getQueryParameter(String paramString)
  {
    if (!isOpaque())
    {
      if (paramString != null)
      {
        String str = getEncodedQuery();
        if (str == null) {
          return null;
        }
        paramString = encode(paramString, null);
        int i = str.length();
        int k;
        for (int j = 0;; j = k + 1)
        {
          k = str.indexOf('&', j);
          int m;
          if (k != -1) {
            m = k;
          } else {
            m = i;
          }
          int n = str.indexOf('=', j);
          int i1;
          if (n <= m)
          {
            i1 = n;
            if (n != -1) {}
          }
          else
          {
            i1 = m;
          }
          if ((i1 - j == paramString.length()) && (str.regionMatches(j, paramString, 0, paramString.length())))
          {
            if (i1 == m) {
              return "";
            }
            return UriCodec.decode(str.substring(i1 + 1, m), true, StandardCharsets.UTF_8, false);
          }
          if (k == -1) {
            break;
          }
        }
        return null;
      }
      throw new NullPointerException("key");
    }
    throw new UnsupportedOperationException("This isn't a hierarchical URI.");
  }
  
  public Set<String> getQueryParameterNames()
  {
    if (!isOpaque())
    {
      String str = getEncodedQuery();
      if (str == null) {
        return Collections.emptySet();
      }
      LinkedHashSet localLinkedHashSet = new LinkedHashSet();
      int i = 0;
      int j;
      do
      {
        j = str.indexOf('&', i);
        if (j == -1) {
          j = str.length();
        }
        int k = str.indexOf('=', i);
        int m;
        if (k <= j)
        {
          m = k;
          if (k != -1) {}
        }
        else
        {
          m = j;
        }
        localLinkedHashSet.add(decode(str.substring(i, m)));
        j++;
        i = j;
      } while (j < str.length());
      return Collections.unmodifiableSet(localLinkedHashSet);
    }
    throw new UnsupportedOperationException("This isn't a hierarchical URI.");
  }
  
  public List<String> getQueryParameters(String paramString)
  {
    if (!isOpaque())
    {
      if (paramString != null)
      {
        String str = getEncodedQuery();
        if (str == null) {
          return Collections.emptyList();
        }
        try
        {
          paramString = URLEncoder.encode(paramString, "UTF-8");
          ArrayList localArrayList = new ArrayList();
          int j;
          for (int i = 0;; i = j + 1)
          {
            j = str.indexOf('&', i);
            int k;
            if (j != -1) {
              k = j;
            } else {
              k = str.length();
            }
            int m = str.indexOf('=', i);
            int n;
            if (m <= k)
            {
              n = m;
              if (m != -1) {}
            }
            else
            {
              n = k;
            }
            if ((n - i == paramString.length()) && (str.regionMatches(i, paramString, 0, paramString.length()))) {
              if (n == k) {
                localArrayList.add("");
              } else {
                localArrayList.add(decode(str.substring(n + 1, k)));
              }
            }
            if (j == -1) {
              break;
            }
          }
          return Collections.unmodifiableList(localArrayList);
        }
        catch (UnsupportedEncodingException paramString)
        {
          throw new AssertionError(paramString);
        }
      }
      throw new NullPointerException("key");
    }
    throw new UnsupportedOperationException("This isn't a hierarchical URI.");
  }
  
  public abstract String getScheme();
  
  public abstract String getSchemeSpecificPart();
  
  public abstract String getUserInfo();
  
  public int hashCode()
  {
    return toString().hashCode();
  }
  
  public boolean isAbsolute()
  {
    return isRelative() ^ true;
  }
  
  public abstract boolean isHierarchical();
  
  public boolean isOpaque()
  {
    return isHierarchical() ^ true;
  }
  
  public boolean isPathPrefixMatch(Uri paramUri)
  {
    if (!Objects.equals(getScheme(), paramUri.getScheme())) {
      return false;
    }
    if (!Objects.equals(getAuthority(), paramUri.getAuthority())) {
      return false;
    }
    List localList = getPathSegments();
    paramUri = paramUri.getPathSegments();
    int i = paramUri.size();
    if (localList.size() < i) {
      return false;
    }
    for (int j = 0; j < i; j++) {
      if (!Objects.equals(localList.get(j), paramUri.get(j))) {
        return false;
      }
    }
    return true;
  }
  
  public abstract boolean isRelative();
  
  public Uri normalizeScheme()
  {
    String str1 = getScheme();
    if (str1 == null) {
      return this;
    }
    String str2 = str1.toLowerCase(Locale.ROOT);
    if (str1.equals(str2)) {
      return this;
    }
    return buildUpon().scheme(str2).build();
  }
  
  public String toSafeString()
  {
    String str = getScheme();
    Object localObject1 = getSchemeSpecificPart();
    Object localObject2 = localObject1;
    if (str != null) {
      if ((!str.equalsIgnoreCase("tel")) && (!str.equalsIgnoreCase("sip")) && (!str.equalsIgnoreCase("sms")) && (!str.equalsIgnoreCase("smsto")) && (!str.equalsIgnoreCase("mailto")) && (!str.equalsIgnoreCase("nfc")))
      {
        if ((!str.equalsIgnoreCase("http")) && (!str.equalsIgnoreCase("https")))
        {
          localObject2 = localObject1;
          if (!str.equalsIgnoreCase("ftp")) {}
        }
        else
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("//");
          if (getHost() != null) {
            localObject2 = getHost();
          } else {
            localObject2 = "";
          }
          ((StringBuilder)localObject1).append((String)localObject2);
          if (getPort() != -1)
          {
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append(":");
            ((StringBuilder)localObject2).append(getPort());
            localObject2 = ((StringBuilder)localObject2).toString();
          }
          else
          {
            localObject2 = "";
          }
          ((StringBuilder)localObject1).append((String)localObject2);
          ((StringBuilder)localObject1).append("/...");
          localObject2 = ((StringBuilder)localObject1).toString();
        }
      }
      else
      {
        localObject2 = new StringBuilder(64);
        ((StringBuilder)localObject2).append(str);
        ((StringBuilder)localObject2).append(':');
        if (localObject1 != null) {
          for (int i = 0; i < ((String)localObject1).length(); i++)
          {
            char c = ((String)localObject1).charAt(i);
            if ((c != '-') && (c != '@') && (c != '.')) {
              ((StringBuilder)localObject2).append('x');
            } else {
              ((StringBuilder)localObject2).append(c);
            }
          }
        }
        return ((StringBuilder)localObject2).toString();
      }
    }
    localObject1 = new StringBuilder(64);
    if (str != null)
    {
      ((StringBuilder)localObject1).append(str);
      ((StringBuilder)localObject1).append(':');
    }
    if (localObject2 != null) {
      ((StringBuilder)localObject1).append((String)localObject2);
    }
    return ((StringBuilder)localObject1).toString();
  }
  
  public abstract String toString();
  
  private static abstract class AbstractHierarchicalUri
    extends Uri
  {
    private volatile String host = Uri.NOT_CACHED;
    private volatile int port = -2;
    private Uri.Part userInfo;
    
    private AbstractHierarchicalUri()
    {
      super();
    }
    
    private Uri.Part getUserInfoPart()
    {
      Uri.Part localPart;
      if (userInfo == null)
      {
        localPart = Uri.Part.fromEncoded(parseUserInfo());
        userInfo = localPart;
      }
      else
      {
        localPart = userInfo;
      }
      return localPart;
    }
    
    private String parseHost()
    {
      String str = getEncodedAuthority();
      if (str == null) {
        return null;
      }
      int i = str.lastIndexOf('@');
      int j = str.indexOf(':', i);
      if (j == -1) {
        str = str.substring(i + 1);
      } else {
        str = str.substring(i + 1, j);
      }
      return decode(str);
    }
    
    private int parsePort()
    {
      String str = getEncodedAuthority();
      if (str == null) {
        return -1;
      }
      int i = str.indexOf(':', str.lastIndexOf(64));
      if (i == -1) {
        return -1;
      }
      str = decode(str.substring(i + 1));
      try
      {
        i = Integer.parseInt(str);
        return i;
      }
      catch (NumberFormatException localNumberFormatException)
      {
        Log.w(Uri.LOG, "Error parsing port string.", localNumberFormatException);
      }
      return -1;
    }
    
    private String parseUserInfo()
    {
      String str1 = getEncodedAuthority();
      String str2 = null;
      if (str1 == null) {
        return null;
      }
      int i = str1.lastIndexOf('@');
      if (i != -1) {
        str2 = str1.substring(0, i);
      }
      return str2;
    }
    
    public final String getEncodedUserInfo()
    {
      return getUserInfoPart().getEncoded();
    }
    
    public String getHost()
    {
      int i;
      if (host != Uri.NOT_CACHED) {
        i = 1;
      } else {
        i = 0;
      }
      String str;
      if (i != 0)
      {
        str = host;
      }
      else
      {
        str = parseHost();
        host = str;
      }
      return str;
    }
    
    public String getLastPathSegment()
    {
      List localList = getPathSegments();
      int i = localList.size();
      if (i == 0) {
        return null;
      }
      return (String)localList.get(i - 1);
    }
    
    public int getPort()
    {
      int i;
      if (port == -2)
      {
        i = parsePort();
        port = i;
      }
      else
      {
        i = port;
      }
      return i;
    }
    
    public String getUserInfo()
    {
      return getUserInfoPart().getDecoded();
    }
  }
  
  static abstract class AbstractPart
  {
    volatile String decoded;
    volatile String encoded;
    
    AbstractPart(String paramString1, String paramString2)
    {
      encoded = paramString1;
      decoded = paramString2;
    }
    
    final String getDecoded()
    {
      int i;
      if (decoded != Uri.NOT_CACHED) {
        i = 1;
      } else {
        i = 0;
      }
      String str;
      if (i != 0)
      {
        str = decoded;
      }
      else
      {
        str = Uri.decode(encoded);
        decoded = str;
      }
      return str;
    }
    
    abstract String getEncoded();
    
    final void writeTo(Parcel paramParcel)
    {
      int i;
      if (encoded != Uri.NOT_CACHED) {
        i = 1;
      } else {
        i = 0;
      }
      int j;
      if (decoded != Uri.NOT_CACHED) {
        j = 1;
      } else {
        j = 0;
      }
      if ((i != 0) && (j != 0))
      {
        paramParcel.writeInt(0);
        paramParcel.writeString(encoded);
        paramParcel.writeString(decoded);
      }
      else if (i != 0)
      {
        paramParcel.writeInt(1);
        paramParcel.writeString(encoded);
      }
      else
      {
        if (j == 0) {
          break label104;
        }
        paramParcel.writeInt(2);
        paramParcel.writeString(decoded);
      }
      return;
      label104:
      throw new IllegalArgumentException("Neither encoded nor decoded");
    }
    
    static class Representation
    {
      static final int BOTH = 0;
      static final int DECODED = 2;
      static final int ENCODED = 1;
      
      Representation() {}
    }
  }
  
  public static final class Builder
  {
    private Uri.Part authority;
    private Uri.Part fragment;
    private Uri.Part opaquePart;
    private Uri.PathPart path;
    private Uri.Part query;
    private String scheme;
    
    public Builder() {}
    
    private boolean hasSchemeOrAuthority()
    {
      boolean bool;
      if ((scheme == null) && ((authority == null) || (authority == Uri.Part.NULL))) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public Builder appendEncodedPath(String paramString)
    {
      return path(Uri.PathPart.appendEncodedSegment(path, paramString));
    }
    
    public Builder appendPath(String paramString)
    {
      return path(Uri.PathPart.appendDecodedSegment(path, paramString));
    }
    
    public Builder appendQueryParameter(String paramString1, String paramString2)
    {
      opaquePart = null;
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append(Uri.encode(paramString1, null));
      ((StringBuilder)localObject).append("=");
      ((StringBuilder)localObject).append(Uri.encode(paramString2, null));
      localObject = ((StringBuilder)localObject).toString();
      if (query == null)
      {
        query = Uri.Part.fromEncoded((String)localObject);
        return this;
      }
      paramString1 = query.getEncoded();
      if ((paramString1 != null) && (paramString1.length() != 0))
      {
        paramString2 = new StringBuilder();
        paramString2.append(paramString1);
        paramString2.append("&");
        paramString2.append((String)localObject);
        query = Uri.Part.fromEncoded(paramString2.toString());
      }
      else
      {
        query = Uri.Part.fromEncoded((String)localObject);
      }
      return this;
    }
    
    Builder authority(Uri.Part paramPart)
    {
      opaquePart = null;
      authority = paramPart;
      return this;
    }
    
    public Builder authority(String paramString)
    {
      return authority(Uri.Part.fromDecoded(paramString));
    }
    
    public Uri build()
    {
      if (opaquePart != null)
      {
        if (scheme != null) {
          return new Uri.OpaqueUri(scheme, opaquePart, fragment, null);
        }
        throw new UnsupportedOperationException("An opaque URI must have a scheme.");
      }
      Uri.PathPart localPathPart1 = path;
      Uri.PathPart localPathPart2;
      if ((localPathPart1 != null) && (localPathPart1 != Uri.PathPart.NULL))
      {
        localPathPart2 = localPathPart1;
        if (hasSchemeOrAuthority()) {
          localPathPart2 = Uri.PathPart.makeAbsolute(localPathPart1);
        }
      }
      else
      {
        localPathPart2 = Uri.PathPart.EMPTY;
      }
      return new Uri.HierarchicalUri(scheme, authority, localPathPart2, query, fragment, null);
    }
    
    public Builder clearQuery()
    {
      return query((Uri.Part)null);
    }
    
    public Builder encodedAuthority(String paramString)
    {
      return authority(Uri.Part.fromEncoded(paramString));
    }
    
    public Builder encodedFragment(String paramString)
    {
      return fragment(Uri.Part.fromEncoded(paramString));
    }
    
    public Builder encodedOpaquePart(String paramString)
    {
      return opaquePart(Uri.Part.fromEncoded(paramString));
    }
    
    public Builder encodedPath(String paramString)
    {
      return path(Uri.PathPart.fromEncoded(paramString));
    }
    
    public Builder encodedQuery(String paramString)
    {
      return query(Uri.Part.fromEncoded(paramString));
    }
    
    Builder fragment(Uri.Part paramPart)
    {
      fragment = paramPart;
      return this;
    }
    
    public Builder fragment(String paramString)
    {
      return fragment(Uri.Part.fromDecoded(paramString));
    }
    
    Builder opaquePart(Uri.Part paramPart)
    {
      opaquePart = paramPart;
      return this;
    }
    
    public Builder opaquePart(String paramString)
    {
      return opaquePart(Uri.Part.fromDecoded(paramString));
    }
    
    Builder path(Uri.PathPart paramPathPart)
    {
      opaquePart = null;
      path = paramPathPart;
      return this;
    }
    
    public Builder path(String paramString)
    {
      return path(Uri.PathPart.fromDecoded(paramString));
    }
    
    Builder query(Uri.Part paramPart)
    {
      opaquePart = null;
      query = paramPart;
      return this;
    }
    
    public Builder query(String paramString)
    {
      return query(Uri.Part.fromDecoded(paramString));
    }
    
    public Builder scheme(String paramString)
    {
      scheme = paramString;
      return this;
    }
    
    public String toString()
    {
      return build().toString();
    }
  }
  
  private static class HierarchicalUri
    extends Uri.AbstractHierarchicalUri
  {
    static final int TYPE_ID = 3;
    private final Uri.Part authority;
    private final Uri.Part fragment;
    private final Uri.PathPart path;
    private final Uri.Part query;
    private final String scheme;
    private Uri.Part ssp;
    private volatile String uriString = Uri.NOT_CACHED;
    
    private HierarchicalUri(String paramString, Uri.Part paramPart1, Uri.PathPart paramPathPart, Uri.Part paramPart2, Uri.Part paramPart3)
    {
      super();
      scheme = paramString;
      authority = Uri.Part.nonNull(paramPart1);
      if (paramPathPart == null) {
        paramString = Uri.PathPart.NULL;
      } else {
        paramString = paramPathPart;
      }
      path = paramString;
      query = Uri.Part.nonNull(paramPart2);
      fragment = Uri.Part.nonNull(paramPart3);
    }
    
    private void appendSspTo(StringBuilder paramStringBuilder)
    {
      String str = authority.getEncoded();
      if (str != null)
      {
        paramStringBuilder.append("//");
        paramStringBuilder.append(str);
      }
      str = path.getEncoded();
      if (str != null) {
        paramStringBuilder.append(str);
      }
      if (!query.isEmpty())
      {
        paramStringBuilder.append('?');
        paramStringBuilder.append(query.getEncoded());
      }
    }
    
    private Uri.Part getSsp()
    {
      Uri.Part localPart;
      if (ssp == null)
      {
        localPart = Uri.Part.fromEncoded(makeSchemeSpecificPart());
        ssp = localPart;
      }
      else
      {
        localPart = ssp;
      }
      return localPart;
    }
    
    private String makeSchemeSpecificPart()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      appendSspTo(localStringBuilder);
      return localStringBuilder.toString();
    }
    
    private String makeUriString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      if (scheme != null)
      {
        localStringBuilder.append(scheme);
        localStringBuilder.append(':');
      }
      appendSspTo(localStringBuilder);
      if (!fragment.isEmpty())
      {
        localStringBuilder.append('#');
        localStringBuilder.append(fragment.getEncoded());
      }
      return localStringBuilder.toString();
    }
    
    static Uri readFrom(Parcel paramParcel)
    {
      return new HierarchicalUri(paramParcel.readString(), Uri.Part.readFrom(paramParcel), Uri.PathPart.readFrom(paramParcel), Uri.Part.readFrom(paramParcel), Uri.Part.readFrom(paramParcel));
    }
    
    public Uri.Builder buildUpon()
    {
      return new Uri.Builder().scheme(scheme).authority(authority).path(path).query(query).fragment(fragment);
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public String getAuthority()
    {
      return authority.getDecoded();
    }
    
    public String getEncodedAuthority()
    {
      return authority.getEncoded();
    }
    
    public String getEncodedFragment()
    {
      return fragment.getEncoded();
    }
    
    public String getEncodedPath()
    {
      return path.getEncoded();
    }
    
    public String getEncodedQuery()
    {
      return query.getEncoded();
    }
    
    public String getEncodedSchemeSpecificPart()
    {
      return getSsp().getEncoded();
    }
    
    public String getFragment()
    {
      return fragment.getDecoded();
    }
    
    public String getPath()
    {
      return path.getDecoded();
    }
    
    public List<String> getPathSegments()
    {
      return path.getPathSegments();
    }
    
    public String getQuery()
    {
      return query.getDecoded();
    }
    
    public String getScheme()
    {
      return scheme;
    }
    
    public String getSchemeSpecificPart()
    {
      return getSsp().getDecoded();
    }
    
    public boolean isHierarchical()
    {
      return true;
    }
    
    public boolean isRelative()
    {
      boolean bool;
      if (scheme == null) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public String toString()
    {
      int i;
      if (uriString != Uri.NOT_CACHED) {
        i = 1;
      } else {
        i = 0;
      }
      String str;
      if (i != 0)
      {
        str = uriString;
      }
      else
      {
        str = makeUriString();
        uriString = str;
      }
      return str;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(3);
      paramParcel.writeString(scheme);
      authority.writeTo(paramParcel);
      path.writeTo(paramParcel);
      query.writeTo(paramParcel);
      fragment.writeTo(paramParcel);
    }
  }
  
  private static class OpaqueUri
    extends Uri
  {
    static final int TYPE_ID = 2;
    private volatile String cachedString = Uri.NOT_CACHED;
    private final Uri.Part fragment;
    private final String scheme;
    private final Uri.Part ssp;
    
    private OpaqueUri(String paramString, Uri.Part paramPart1, Uri.Part paramPart2)
    {
      super();
      scheme = paramString;
      ssp = paramPart1;
      if (paramPart2 == null) {
        paramPart2 = Uri.Part.NULL;
      }
      fragment = paramPart2;
    }
    
    static Uri readFrom(Parcel paramParcel)
    {
      return new OpaqueUri(paramParcel.readString(), Uri.Part.readFrom(paramParcel), Uri.Part.readFrom(paramParcel));
    }
    
    public Uri.Builder buildUpon()
    {
      return new Uri.Builder().scheme(scheme).opaquePart(ssp).fragment(fragment);
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public String getAuthority()
    {
      return null;
    }
    
    public String getEncodedAuthority()
    {
      return null;
    }
    
    public String getEncodedFragment()
    {
      return fragment.getEncoded();
    }
    
    public String getEncodedPath()
    {
      return null;
    }
    
    public String getEncodedQuery()
    {
      return null;
    }
    
    public String getEncodedSchemeSpecificPart()
    {
      return ssp.getEncoded();
    }
    
    public String getEncodedUserInfo()
    {
      return null;
    }
    
    public String getFragment()
    {
      return fragment.getDecoded();
    }
    
    public String getHost()
    {
      return null;
    }
    
    public String getLastPathSegment()
    {
      return null;
    }
    
    public String getPath()
    {
      return null;
    }
    
    public List<String> getPathSegments()
    {
      return Collections.emptyList();
    }
    
    public int getPort()
    {
      return -1;
    }
    
    public String getQuery()
    {
      return null;
    }
    
    public String getScheme()
    {
      return scheme;
    }
    
    public String getSchemeSpecificPart()
    {
      return ssp.getDecoded();
    }
    
    public String getUserInfo()
    {
      return null;
    }
    
    public boolean isHierarchical()
    {
      return false;
    }
    
    public boolean isRelative()
    {
      boolean bool;
      if (scheme == null) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public String toString()
    {
      int i;
      if (cachedString != Uri.NOT_CACHED) {
        i = 1;
      } else {
        i = 0;
      }
      if (i != 0) {
        return cachedString;
      }
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append(scheme);
      ((StringBuilder)localObject).append(':');
      ((StringBuilder)localObject).append(getEncodedSchemeSpecificPart());
      if (!fragment.isEmpty())
      {
        ((StringBuilder)localObject).append('#');
        ((StringBuilder)localObject).append(fragment.getEncoded());
      }
      localObject = ((StringBuilder)localObject).toString();
      cachedString = ((String)localObject);
      return localObject;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(2);
      paramParcel.writeString(scheme);
      ssp.writeTo(paramParcel);
      fragment.writeTo(paramParcel);
    }
  }
  
  static class Part
    extends Uri.AbstractPart
  {
    static final Part EMPTY = new EmptyPart("");
    static final Part NULL = new EmptyPart(null);
    
    private Part(String paramString1, String paramString2)
    {
      super(paramString2);
    }
    
    static Part from(String paramString1, String paramString2)
    {
      if (paramString1 == null) {
        return NULL;
      }
      if (paramString1.length() == 0) {
        return EMPTY;
      }
      if (paramString2 == null) {
        return NULL;
      }
      if (paramString2.length() == 0) {
        return EMPTY;
      }
      return new Part(paramString1, paramString2);
    }
    
    static Part fromDecoded(String paramString)
    {
      return from(Uri.NOT_CACHED, paramString);
    }
    
    static Part fromEncoded(String paramString)
    {
      return from(paramString, Uri.NOT_CACHED);
    }
    
    static Part nonNull(Part paramPart)
    {
      if (paramPart == null) {
        paramPart = NULL;
      }
      return paramPart;
    }
    
    static Part readFrom(Parcel paramParcel)
    {
      int i = paramParcel.readInt();
      switch (i)
      {
      default: 
        paramParcel = new StringBuilder();
        paramParcel.append("Unknown representation: ");
        paramParcel.append(i);
        throw new IllegalArgumentException(paramParcel.toString());
      case 2: 
        return fromDecoded(paramParcel.readString());
      case 1: 
        return fromEncoded(paramParcel.readString());
      }
      return from(paramParcel.readString(), paramParcel.readString());
    }
    
    String getEncoded()
    {
      int i;
      if (encoded != Uri.NOT_CACHED) {
        i = 1;
      } else {
        i = 0;
      }
      String str;
      if (i != 0)
      {
        str = encoded;
      }
      else
      {
        str = Uri.encode(decoded);
        encoded = str;
      }
      return str;
    }
    
    boolean isEmpty()
    {
      return false;
    }
    
    private static class EmptyPart
      extends Uri.Part
    {
      public EmptyPart(String paramString)
      {
        super(paramString, null);
      }
      
      boolean isEmpty()
      {
        return true;
      }
    }
  }
  
  static class PathPart
    extends Uri.AbstractPart
  {
    static final PathPart EMPTY = new PathPart("", "");
    static final PathPart NULL = new PathPart(null, null);
    private Uri.PathSegments pathSegments;
    
    private PathPart(String paramString1, String paramString2)
    {
      super(paramString2);
    }
    
    static PathPart appendDecodedSegment(PathPart paramPathPart, String paramString)
    {
      return appendEncodedSegment(paramPathPart, Uri.encode(paramString));
    }
    
    static PathPart appendEncodedSegment(PathPart paramPathPart, String paramString)
    {
      if (paramPathPart == null)
      {
        paramPathPart = new StringBuilder();
        paramPathPart.append("/");
        paramPathPart.append(paramString);
        return fromEncoded(paramPathPart.toString());
      }
      Object localObject = paramPathPart.getEncoded();
      paramPathPart = (PathPart)localObject;
      if (localObject == null) {
        paramPathPart = "";
      }
      int i = paramPathPart.length();
      if (i == 0)
      {
        paramPathPart = new StringBuilder();
        paramPathPart.append("/");
        paramPathPart.append(paramString);
        paramPathPart = paramPathPart.toString();
      }
      for (;;)
      {
        break;
        if (paramPathPart.charAt(i - 1) == '/')
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append(paramPathPart);
          ((StringBuilder)localObject).append(paramString);
          paramPathPart = ((StringBuilder)localObject).toString();
        }
        else
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append(paramPathPart);
          ((StringBuilder)localObject).append("/");
          ((StringBuilder)localObject).append(paramString);
          paramPathPart = ((StringBuilder)localObject).toString();
        }
      }
      return fromEncoded(paramPathPart);
    }
    
    static PathPart from(String paramString1, String paramString2)
    {
      if (paramString1 == null) {
        return NULL;
      }
      if (paramString1.length() == 0) {
        return EMPTY;
      }
      return new PathPart(paramString1, paramString2);
    }
    
    static PathPart fromDecoded(String paramString)
    {
      return from(Uri.NOT_CACHED, paramString);
    }
    
    static PathPart fromEncoded(String paramString)
    {
      return from(paramString, Uri.NOT_CACHED);
    }
    
    static PathPart makeAbsolute(PathPart paramPathPart)
    {
      Object localObject1 = encoded;
      Object localObject2 = Uri.NOT_CACHED;
      int i = 0;
      int j;
      if (localObject1 != localObject2) {
        j = 1;
      } else {
        j = 0;
      }
      if (j != 0) {
        localObject2 = encoded;
      } else {
        localObject2 = decoded;
      }
      if ((localObject2 != null) && (((String)localObject2).length() != 0) && (!((String)localObject2).startsWith("/")))
      {
        if (j != 0)
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("/");
          ((StringBuilder)localObject2).append(encoded);
          localObject2 = ((StringBuilder)localObject2).toString();
        }
        else
        {
          localObject2 = Uri.NOT_CACHED;
        }
        j = i;
        if (decoded != Uri.NOT_CACHED) {
          j = 1;
        }
        if (j != 0)
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("/");
          ((StringBuilder)localObject1).append(decoded);
          paramPathPart = ((StringBuilder)localObject1).toString();
        }
        else
        {
          paramPathPart = Uri.NOT_CACHED;
        }
        return new PathPart((String)localObject2, paramPathPart);
      }
      return paramPathPart;
    }
    
    static PathPart readFrom(Parcel paramParcel)
    {
      int i = paramParcel.readInt();
      switch (i)
      {
      default: 
        paramParcel = new StringBuilder();
        paramParcel.append("Bad representation: ");
        paramParcel.append(i);
        throw new IllegalArgumentException(paramParcel.toString());
      case 2: 
        return fromDecoded(paramParcel.readString());
      case 1: 
        return fromEncoded(paramParcel.readString());
      }
      return from(paramParcel.readString(), paramParcel.readString());
    }
    
    String getEncoded()
    {
      int i;
      if (encoded != Uri.NOT_CACHED) {
        i = 1;
      } else {
        i = 0;
      }
      String str;
      if (i != 0)
      {
        str = encoded;
      }
      else
      {
        str = Uri.encode(decoded, "/");
        encoded = str;
      }
      return str;
    }
    
    Uri.PathSegments getPathSegments()
    {
      if (pathSegments != null) {
        return pathSegments;
      }
      String str = getEncoded();
      if (str == null)
      {
        localObject = Uri.PathSegments.EMPTY;
        pathSegments = ((Uri.PathSegments)localObject);
        return localObject;
      }
      Object localObject = new Uri.PathSegmentsBuilder();
      int j;
      for (int i = 0;; i = j + 1)
      {
        j = str.indexOf('/', i);
        if (j <= -1) {
          break;
        }
        if (i < j) {
          ((Uri.PathSegmentsBuilder)localObject).add(Uri.decode(str.substring(i, j)));
        }
      }
      if (i < str.length()) {
        ((Uri.PathSegmentsBuilder)localObject).add(Uri.decode(str.substring(i)));
      }
      localObject = ((Uri.PathSegmentsBuilder)localObject).build();
      pathSegments = ((Uri.PathSegments)localObject);
      return localObject;
    }
  }
  
  static class PathSegments
    extends AbstractList<String>
    implements RandomAccess
  {
    static final PathSegments EMPTY = new PathSegments(null, 0);
    final String[] segments;
    final int size;
    
    PathSegments(String[] paramArrayOfString, int paramInt)
    {
      segments = paramArrayOfString;
      size = paramInt;
    }
    
    public String get(int paramInt)
    {
      if (paramInt < size) {
        return segments[paramInt];
      }
      throw new IndexOutOfBoundsException();
    }
    
    public int size()
    {
      return size;
    }
  }
  
  static class PathSegmentsBuilder
  {
    String[] segments;
    int size = 0;
    
    PathSegmentsBuilder() {}
    
    void add(String paramString)
    {
      if (segments == null)
      {
        segments = new String[4];
      }
      else if (size + 1 == segments.length)
      {
        arrayOfString = new String[segments.length * 2];
        System.arraycopy(segments, 0, arrayOfString, 0, segments.length);
        segments = arrayOfString;
      }
      String[] arrayOfString = segments;
      int i = size;
      size = (i + 1);
      arrayOfString[i] = paramString;
    }
    
    Uri.PathSegments build()
    {
      if (segments == null) {
        return Uri.PathSegments.EMPTY;
      }
      try
      {
        Uri.PathSegments localPathSegments = new Uri.PathSegments(segments, size);
        return localPathSegments;
      }
      finally
      {
        segments = null;
      }
    }
  }
  
  private static class StringUri
    extends Uri.AbstractHierarchicalUri
  {
    static final int TYPE_ID = 1;
    private Uri.Part authority;
    private volatile int cachedFsi = -2;
    private volatile int cachedSsi = -2;
    private Uri.Part fragment;
    private Uri.PathPart path;
    private Uri.Part query;
    private volatile String scheme = Uri.NOT_CACHED;
    private Uri.Part ssp;
    private final String uriString;
    
    private StringUri(String paramString)
    {
      super();
      if (paramString != null)
      {
        uriString = paramString;
        return;
      }
      throw new NullPointerException("uriString");
    }
    
    private int findFragmentSeparator()
    {
      int i;
      if (cachedFsi == -2)
      {
        i = uriString.indexOf('#', findSchemeSeparator());
        cachedFsi = i;
      }
      else
      {
        i = cachedFsi;
      }
      return i;
    }
    
    private int findSchemeSeparator()
    {
      int i;
      if (cachedSsi == -2)
      {
        i = uriString.indexOf(':');
        cachedSsi = i;
      }
      else
      {
        i = cachedSsi;
      }
      return i;
    }
    
    private Uri.Part getAuthorityPart()
    {
      if (authority == null)
      {
        Uri.Part localPart = Uri.Part.fromEncoded(parseAuthority(uriString, findSchemeSeparator()));
        authority = localPart;
        return localPart;
      }
      return authority;
    }
    
    private Uri.Part getFragmentPart()
    {
      Uri.Part localPart;
      if (fragment == null)
      {
        localPart = Uri.Part.fromEncoded(parseFragment());
        fragment = localPart;
      }
      else
      {
        localPart = fragment;
      }
      return localPart;
    }
    
    private Uri.PathPart getPathPart()
    {
      Uri.PathPart localPathPart;
      if (path == null)
      {
        localPathPart = Uri.PathPart.fromEncoded(parsePath());
        path = localPathPart;
      }
      else
      {
        localPathPart = path;
      }
      return localPathPart;
    }
    
    private Uri.Part getQueryPart()
    {
      Uri.Part localPart;
      if (query == null)
      {
        localPart = Uri.Part.fromEncoded(parseQuery());
        query = localPart;
      }
      else
      {
        localPart = query;
      }
      return localPart;
    }
    
    private Uri.Part getSsp()
    {
      Uri.Part localPart;
      if (ssp == null)
      {
        localPart = Uri.Part.fromEncoded(parseSsp());
        ssp = localPart;
      }
      else
      {
        localPart = ssp;
      }
      return localPart;
    }
    
    static String parseAuthority(String paramString, int paramInt)
    {
      int i = paramString.length();
      if ((i > paramInt + 2) && (paramString.charAt(paramInt + 1) == '/') && (paramString.charAt(paramInt + 2) == '/'))
      {
        for (int j = paramInt + 3; j < i; j++)
        {
          int k = paramString.charAt(j);
          if ((k == 35) || (k == 47) || (k == 63) || (k == 92)) {
            break;
          }
        }
        return paramString.substring(paramInt + 3, j);
      }
      return null;
    }
    
    private String parseFragment()
    {
      int i = findFragmentSeparator();
      String str;
      if (i == -1) {
        str = null;
      } else {
        str = uriString.substring(i + 1);
      }
      return str;
    }
    
    private String parsePath()
    {
      String str = uriString;
      int i = findSchemeSeparator();
      if (i > -1)
      {
        int j;
        if (i + 1 == str.length()) {
          j = 1;
        } else {
          j = 0;
        }
        if (j != 0) {
          return null;
        }
        if (str.charAt(i + 1) != '/') {
          return null;
        }
      }
      return parsePath(str, i);
    }
    
    static String parsePath(String paramString, int paramInt)
    {
      int i = paramString.length();
      if ((i > paramInt + 2) && (paramString.charAt(paramInt + 1) == '/') && (paramString.charAt(paramInt + 2) == '/'))
      {
        for (j = paramInt + 3;; j++)
        {
          paramInt = j;
          if (j >= i) {
            break label94;
          }
          paramInt = paramString.charAt(j);
          if (paramInt == 35) {
            break label88;
          }
          if (paramInt == 47) {
            break;
          }
          if (paramInt == 63) {
            break label88;
          }
          if (paramInt == 92) {
            break;
          }
        }
        paramInt = j;
        break label94;
        label88:
        return "";
      }
      else
      {
        paramInt++;
      }
      label94:
      for (int j = paramInt; j < i; j++)
      {
        int k = paramString.charAt(j);
        if ((k == 35) || (k == 63)) {
          break;
        }
      }
      return paramString.substring(paramInt, j);
    }
    
    private String parseQuery()
    {
      int i = uriString.indexOf('?', findSchemeSeparator());
      if (i == -1) {
        return null;
      }
      int j = findFragmentSeparator();
      if (j == -1) {
        return uriString.substring(i + 1);
      }
      if (j < i) {
        return null;
      }
      return uriString.substring(i + 1, j);
    }
    
    private String parseScheme()
    {
      int i = findSchemeSeparator();
      String str;
      if (i == -1) {
        str = null;
      } else {
        str = uriString.substring(0, i);
      }
      return str;
    }
    
    private String parseSsp()
    {
      int i = findSchemeSeparator();
      int j = findFragmentSeparator();
      String str;
      if (j == -1) {
        str = uriString.substring(i + 1);
      } else {
        str = uriString.substring(i + 1, j);
      }
      return str;
    }
    
    static Uri readFrom(Parcel paramParcel)
    {
      return new StringUri(paramParcel.readString());
    }
    
    public Uri.Builder buildUpon()
    {
      if (isHierarchical()) {
        return new Uri.Builder().scheme(getScheme()).authority(getAuthorityPart()).path(getPathPart()).query(getQueryPart()).fragment(getFragmentPart());
      }
      return new Uri.Builder().scheme(getScheme()).opaquePart(getSsp()).fragment(getFragmentPart());
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public String getAuthority()
    {
      return getAuthorityPart().getDecoded();
    }
    
    public String getEncodedAuthority()
    {
      return getAuthorityPart().getEncoded();
    }
    
    public String getEncodedFragment()
    {
      return getFragmentPart().getEncoded();
    }
    
    public String getEncodedPath()
    {
      return getPathPart().getEncoded();
    }
    
    public String getEncodedQuery()
    {
      return getQueryPart().getEncoded();
    }
    
    public String getEncodedSchemeSpecificPart()
    {
      return getSsp().getEncoded();
    }
    
    public String getFragment()
    {
      return getFragmentPart().getDecoded();
    }
    
    public String getPath()
    {
      return getPathPart().getDecoded();
    }
    
    public List<String> getPathSegments()
    {
      return getPathPart().getPathSegments();
    }
    
    public String getQuery()
    {
      return getQueryPart().getDecoded();
    }
    
    public String getScheme()
    {
      int i;
      if (scheme != Uri.NOT_CACHED) {
        i = 1;
      } else {
        i = 0;
      }
      String str;
      if (i != 0)
      {
        str = scheme;
      }
      else
      {
        str = parseScheme();
        scheme = str;
      }
      return str;
    }
    
    public String getSchemeSpecificPart()
    {
      return getSsp().getDecoded();
    }
    
    public boolean isHierarchical()
    {
      int i = findSchemeSeparator();
      boolean bool = true;
      if (i == -1) {
        return true;
      }
      if (uriString.length() == i + 1) {
        return false;
      }
      if (uriString.charAt(i + 1) != '/') {
        bool = false;
      }
      return bool;
    }
    
    public boolean isRelative()
    {
      boolean bool;
      if (findSchemeSeparator() == -1) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public String toString()
    {
      return uriString;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(1);
      paramParcel.writeString(uriString);
    }
  }
}
