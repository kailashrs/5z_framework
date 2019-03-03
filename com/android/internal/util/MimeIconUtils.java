package com.android.internal.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import java.util.HashMap;

public class MimeIconUtils
{
  private static HashMap<String, Integer> sMimeIcons = new HashMap();
  
  static
  {
    add("application/vnd.android.package-archive", 17302616);
    add("application/ogg", 17302617);
    add("application/x-flac", 17302617);
    add("application/pgp-keys", 17302618);
    add("application/pgp-signature", 17302618);
    add("application/x-pkcs12", 17302618);
    add("application/x-pkcs7-certreqresp", 17302618);
    add("application/x-pkcs7-crl", 17302618);
    add("application/x-x509-ca-cert", 17302618);
    add("application/x-x509-user-cert", 17302618);
    add("application/x-pkcs7-certificates", 17302618);
    add("application/x-pkcs7-mime", 17302618);
    add("application/x-pkcs7-signature", 17302618);
    add("application/rdf+xml", 17302619);
    add("application/rss+xml", 17302619);
    add("application/x-object", 17302619);
    add("application/xhtml+xml", 17302619);
    add("text/css", 17302619);
    add("text/html", 17302619);
    add("text/xml", 17302619);
    add("text/x-c++hdr", 17302619);
    add("text/x-c++src", 17302619);
    add("text/x-chdr", 17302619);
    add("text/x-csrc", 17302619);
    add("text/x-dsrc", 17302619);
    add("text/x-csh", 17302619);
    add("text/x-haskell", 17302619);
    add("text/x-java", 17302619);
    add("text/x-literate-haskell", 17302619);
    add("text/x-pascal", 17302619);
    add("text/x-tcl", 17302619);
    add("text/x-tex", 17302619);
    add("application/x-latex", 17302619);
    add("application/x-texinfo", 17302619);
    add("application/atom+xml", 17302619);
    add("application/ecmascript", 17302619);
    add("application/json", 17302619);
    add("application/javascript", 17302619);
    add("application/xml", 17302619);
    add("text/javascript", 17302619);
    add("application/x-javascript", 17302619);
    add("application/mac-binhex40", 17302620);
    add("application/rar", 17302620);
    add("application/zip", 17302620);
    add("application/x-apple-diskimage", 17302620);
    add("application/x-debian-package", 17302620);
    add("application/x-gtar", 17302620);
    add("application/x-iso9660-image", 17302620);
    add("application/x-lha", 17302620);
    add("application/x-lzh", 17302620);
    add("application/x-lzx", 17302620);
    add("application/x-stuffit", 17302620);
    add("application/x-tar", 17302620);
    add("application/x-webarchive", 17302620);
    add("application/x-webarchive-xml", 17302620);
    add("application/gzip", 17302620);
    add("application/x-7z-compressed", 17302620);
    add("application/x-deb", 17302620);
    add("application/x-rar-compressed", 17302620);
    add("text/x-vcard", 17302621);
    add("text/vcard", 17302621);
    add("text/calendar", 17302623);
    add("text/x-vcalendar", 17302623);
    add("application/x-font", 17302626);
    add("application/font-woff", 17302626);
    add("application/x-font-woff", 17302626);
    add("application/x-font-ttf", 17302626);
    add("application/vnd.oasis.opendocument.graphics", 17302628);
    add("application/vnd.oasis.opendocument.graphics-template", 17302628);
    add("application/vnd.oasis.opendocument.image", 17302628);
    add("application/vnd.stardivision.draw", 17302628);
    add("application/vnd.sun.xml.draw", 17302628);
    add("application/vnd.sun.xml.draw.template", 17302628);
    add("application/pdf", 17302629);
    add("application/vnd.stardivision.impress", 17302631);
    add("application/vnd.sun.xml.impress", 17302631);
    add("application/vnd.sun.xml.impress.template", 17302631);
    add("application/x-kpresenter", 17302631);
    add("application/vnd.oasis.opendocument.presentation", 17302631);
    add("application/vnd.oasis.opendocument.spreadsheet", 17302632);
    add("application/vnd.oasis.opendocument.spreadsheet-template", 17302632);
    add("application/vnd.stardivision.calc", 17302632);
    add("application/vnd.sun.xml.calc", 17302632);
    add("application/vnd.sun.xml.calc.template", 17302632);
    add("application/x-kspread", 17302632);
    add("application/vnd.oasis.opendocument.text", 17302622);
    add("application/vnd.oasis.opendocument.text-master", 17302622);
    add("application/vnd.oasis.opendocument.text-template", 17302622);
    add("application/vnd.oasis.opendocument.text-web", 17302622);
    add("application/vnd.stardivision.writer", 17302622);
    add("application/vnd.stardivision.writer-global", 17302622);
    add("application/vnd.sun.xml.writer", 17302622);
    add("application/vnd.sun.xml.writer.global", 17302622);
    add("application/vnd.sun.xml.writer.template", 17302622);
    add("application/x-abiword", 17302622);
    add("application/x-kword", 17302622);
    add("application/x-quicktimeplayer", 17302634);
    add("application/x-shockwave-flash", 17302634);
    add("application/msword", 17302635);
    add("application/vnd.openxmlformats-officedocument.wordprocessingml.document", 17302635);
    add("application/vnd.openxmlformats-officedocument.wordprocessingml.template", 17302635);
    add("application/vnd.ms-excel", 17302624);
    add("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 17302624);
    add("application/vnd.openxmlformats-officedocument.spreadsheetml.template", 17302624);
    add("application/vnd.ms-powerpoint", 17302630);
    add("application/vnd.openxmlformats-officedocument.presentationml.presentation", 17302630);
    add("application/vnd.openxmlformats-officedocument.presentationml.template", 17302630);
    add("application/vnd.openxmlformats-officedocument.presentationml.slideshow", 17302630);
  }
  
  public MimeIconUtils() {}
  
  private static void add(String paramString, int paramInt)
  {
    if (sMimeIcons.put(paramString, Integer.valueOf(paramInt)) == null) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append(" already registered!");
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public static Drawable loadMimeIcon(Context paramContext, String paramString)
  {
    if ("vnd.android.document/directory".equals(paramString)) {
      return paramContext.getDrawable(17302625);
    }
    Integer localInteger = (Integer)sMimeIcons.get(paramString);
    if (localInteger != null) {
      return paramContext.getDrawable(localInteger.intValue());
    }
    if (paramString == null) {
      return null;
    }
    paramString = paramString.split("/")[0];
    if ("audio".equals(paramString)) {
      return paramContext.getDrawable(17302617);
    }
    if ("image".equals(paramString)) {
      return paramContext.getDrawable(17302628);
    }
    if ("text".equals(paramString)) {
      return paramContext.getDrawable(17302633);
    }
    if ("video".equals(paramString)) {
      return paramContext.getDrawable(17302634);
    }
    return paramContext.getDrawable(17302627);
  }
}
