package com.android.internal.print;

import android.content.Context;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintAttributes.Margins;
import android.print.PrintAttributes.MediaSize;
import android.print.PrintAttributes.Resolution;
import android.print.PrintDocumentInfo;
import android.print.PrintJobId;
import android.print.PrintJobInfo;
import android.print.PrinterCapabilitiesInfo;
import android.print.PrinterId;
import android.print.PrinterInfo;
import com.android.internal.util.dump.DualDumpOutputStream;
import java.util.List;

public class DumpUtils
{
  public DumpUtils() {}
  
  public static void writeMargins(DualDumpOutputStream paramDualDumpOutputStream, String paramString, long paramLong, PrintAttributes.Margins paramMargins)
  {
    paramLong = paramDualDumpOutputStream.start(paramString, paramLong);
    paramDualDumpOutputStream.write("top_mils", 1120986464257L, paramMargins.getTopMils());
    paramDualDumpOutputStream.write("left_mils", 1120986464258L, paramMargins.getLeftMils());
    paramDualDumpOutputStream.write("right_mils", 1120986464259L, paramMargins.getRightMils());
    paramDualDumpOutputStream.write("bottom_mils", 1120986464260L, paramMargins.getBottomMils());
    paramDualDumpOutputStream.end(paramLong);
  }
  
  public static void writeMediaSize(Context paramContext, DualDumpOutputStream paramDualDumpOutputStream, String paramString, long paramLong, PrintAttributes.MediaSize paramMediaSize)
  {
    paramLong = paramDualDumpOutputStream.start(paramString, paramLong);
    paramDualDumpOutputStream.write("id", 1138166333441L, paramMediaSize.getId());
    paramDualDumpOutputStream.write("label", 1138166333442L, paramMediaSize.getLabel(paramContext.getPackageManager()));
    paramDualDumpOutputStream.write("height_mils", 1120986464259L, paramMediaSize.getHeightMils());
    paramDualDumpOutputStream.write("width_mils", 1120986464260L, paramMediaSize.getWidthMils());
    paramDualDumpOutputStream.end(paramLong);
  }
  
  public static void writePageRange(DualDumpOutputStream paramDualDumpOutputStream, String paramString, long paramLong, PageRange paramPageRange)
  {
    paramLong = paramDualDumpOutputStream.start(paramString, paramLong);
    paramDualDumpOutputStream.write("start", 1120986464257L, paramPageRange.getStart());
    paramDualDumpOutputStream.write("end", 1120986464258L, paramPageRange.getEnd());
    paramDualDumpOutputStream.end(paramLong);
  }
  
  public static void writePrintAttributes(Context paramContext, DualDumpOutputStream paramDualDumpOutputStream, String paramString, long paramLong, PrintAttributes paramPrintAttributes)
  {
    paramLong = paramDualDumpOutputStream.start(paramString, paramLong);
    paramString = paramPrintAttributes.getMediaSize();
    if (paramString != null)
    {
      writeMediaSize(paramContext, paramDualDumpOutputStream, "media_size", 1146756268033L, paramString);
      paramDualDumpOutputStream.write("is_portrait", 1133871366146L, paramPrintAttributes.isPortrait());
    }
    paramContext = paramPrintAttributes.getResolution();
    if (paramContext != null) {
      writeResolution(paramDualDumpOutputStream, "resolution", 1146756268035L, paramContext);
    }
    paramContext = paramPrintAttributes.getMinMargins();
    if (paramContext != null) {
      writeMargins(paramDualDumpOutputStream, "min_margings", 1146756268036L, paramContext);
    }
    paramDualDumpOutputStream.write("color_mode", 1159641169925L, paramPrintAttributes.getColorMode());
    paramDualDumpOutputStream.write("duplex_mode", 1159641169926L, paramPrintAttributes.getDuplexMode());
    paramDualDumpOutputStream.end(paramLong);
  }
  
  public static void writePrintDocumentInfo(DualDumpOutputStream paramDualDumpOutputStream, String paramString, long paramLong, PrintDocumentInfo paramPrintDocumentInfo)
  {
    paramLong = paramDualDumpOutputStream.start(paramString, paramLong);
    paramDualDumpOutputStream.write("name", 1138166333441L, paramPrintDocumentInfo.getName());
    int i = paramPrintDocumentInfo.getPageCount();
    if (i != -1) {
      paramDualDumpOutputStream.write("page_count", 1120986464258L, i);
    }
    paramDualDumpOutputStream.write("content_type", 1120986464259L, paramPrintDocumentInfo.getContentType());
    paramDualDumpOutputStream.write("data_size", 1112396529668L, paramPrintDocumentInfo.getDataSize());
    paramDualDumpOutputStream.end(paramLong);
  }
  
  public static void writePrintJobInfo(Context paramContext, DualDumpOutputStream paramDualDumpOutputStream, String paramString, long paramLong, PrintJobInfo paramPrintJobInfo)
  {
    paramLong = paramDualDumpOutputStream.start(paramString, paramLong);
    paramDualDumpOutputStream.write("label", 1138166333441L, paramPrintJobInfo.getLabel());
    paramString = paramPrintJobInfo.getId();
    if (paramString != null) {
      paramDualDumpOutputStream.write("print_job_id", 1138166333442L, paramString.flattenToString());
    }
    int i = paramPrintJobInfo.getState();
    boolean bool = true;
    if ((i >= 1) && (i <= 7)) {
      paramDualDumpOutputStream.write("state", 1159641169923L, i);
    } else {
      paramDualDumpOutputStream.write("state", 1159641169923L, 0);
    }
    paramString = paramPrintJobInfo.getPrinterId();
    if (paramString != null) {
      writePrinterId(paramDualDumpOutputStream, "printer", 1146756268036L, paramString);
    }
    paramString = paramPrintJobInfo.getTag();
    if (paramString != null) {
      paramDualDumpOutputStream.write("tag", 1138166333445L, paramString);
    }
    paramDualDumpOutputStream.write("creation_time", 1112396529670L, paramPrintJobInfo.getCreationTime());
    paramString = paramPrintJobInfo.getAttributes();
    if (paramString != null) {
      writePrintAttributes(paramContext, paramDualDumpOutputStream, "attributes", 1146756268039L, paramString);
    }
    paramString = paramPrintJobInfo.getDocumentInfo();
    if (paramString != null) {
      writePrintDocumentInfo(paramDualDumpOutputStream, "document_info", 1146756268040L, paramString);
    }
    paramDualDumpOutputStream.write("is_canceling", 1133871366153L, paramPrintJobInfo.isCancelling());
    paramString = paramPrintJobInfo.getPages();
    if (paramString != null) {
      for (i = 0; i < paramString.length; i++) {
        writePageRange(paramDualDumpOutputStream, "pages", 2246267895818L, paramString[i]);
      }
    }
    if (paramPrintJobInfo.getAdvancedOptions() == null) {
      bool = false;
    }
    paramDualDumpOutputStream.write("has_advanced_options", 1133871366155L, bool);
    paramDualDumpOutputStream.write("progress", 1108101562380L, paramPrintJobInfo.getProgress());
    paramContext = paramPrintJobInfo.getStatus(paramContext.getPackageManager());
    if (paramContext != null) {
      paramDualDumpOutputStream.write("status", 1138166333453L, paramContext.toString());
    }
    paramDualDumpOutputStream.end(paramLong);
  }
  
  public static void writePrinterCapabilities(Context paramContext, DualDumpOutputStream paramDualDumpOutputStream, String paramString, long paramLong, PrinterCapabilitiesInfo paramPrinterCapabilitiesInfo)
  {
    paramLong = paramDualDumpOutputStream.start(paramString, paramLong);
    writeMargins(paramDualDumpOutputStream, "min_margins", 1146756268033L, paramPrinterCapabilitiesInfo.getMinMargins());
    int i = paramPrinterCapabilitiesInfo.getMediaSizes().size();
    int j = 0;
    for (int k = 0; k < i; k++) {
      writeMediaSize(paramContext, paramDualDumpOutputStream, "media_sizes", 2246267895810L, (PrintAttributes.MediaSize)paramPrinterCapabilitiesInfo.getMediaSizes().get(k));
    }
    i = paramPrinterCapabilitiesInfo.getResolutions().size();
    for (k = j; k < i; k++) {
      writeResolution(paramDualDumpOutputStream, "resolutions", 2246267895811L, (PrintAttributes.Resolution)paramPrinterCapabilitiesInfo.getResolutions().get(k));
    }
    if ((paramPrinterCapabilitiesInfo.getColorModes() & 0x1) != 0) {
      paramDualDumpOutputStream.write("color_modes", 2259152797700L, 1);
    }
    if ((paramPrinterCapabilitiesInfo.getColorModes() & 0x2) != 0) {
      paramDualDumpOutputStream.write("color_modes", 2259152797700L, 2);
    }
    if ((paramPrinterCapabilitiesInfo.getDuplexModes() & 0x1) != 0) {
      paramDualDumpOutputStream.write("duplex_modes", 2259152797701L, 1);
    }
    if ((paramPrinterCapabilitiesInfo.getDuplexModes() & 0x2) != 0) {
      paramDualDumpOutputStream.write("duplex_modes", 2259152797701L, 2);
    }
    if ((paramPrinterCapabilitiesInfo.getDuplexModes() & 0x4) != 0) {
      paramDualDumpOutputStream.write("duplex_modes", 2259152797701L, 4);
    }
    paramDualDumpOutputStream.end(paramLong);
  }
  
  public static void writePrinterId(DualDumpOutputStream paramDualDumpOutputStream, String paramString, long paramLong, PrinterId paramPrinterId)
  {
    paramLong = paramDualDumpOutputStream.start(paramString, paramLong);
    com.android.internal.util.dump.DumpUtils.writeComponentName(paramDualDumpOutputStream, "service_name", 1146756268033L, paramPrinterId.getServiceName());
    paramDualDumpOutputStream.write("local_id", 1138166333442L, paramPrinterId.getLocalId());
    paramDualDumpOutputStream.end(paramLong);
  }
  
  public static void writePrinterInfo(Context paramContext, DualDumpOutputStream paramDualDumpOutputStream, String paramString, long paramLong, PrinterInfo paramPrinterInfo)
  {
    paramLong = paramDualDumpOutputStream.start(paramString, paramLong);
    writePrinterId(paramDualDumpOutputStream, "id", 1146756268033L, paramPrinterInfo.getId());
    paramDualDumpOutputStream.write("name", 1138166333442L, paramPrinterInfo.getName());
    paramDualDumpOutputStream.write("status", 1159641169923L, paramPrinterInfo.getStatus());
    paramDualDumpOutputStream.write("description", 1138166333444L, paramPrinterInfo.getDescription());
    paramString = paramPrinterInfo.getCapabilities();
    if (paramString != null) {
      writePrinterCapabilities(paramContext, paramDualDumpOutputStream, "capabilities", 1146756268037L, paramString);
    }
    paramDualDumpOutputStream.end(paramLong);
  }
  
  public static void writeResolution(DualDumpOutputStream paramDualDumpOutputStream, String paramString, long paramLong, PrintAttributes.Resolution paramResolution)
  {
    paramLong = paramDualDumpOutputStream.start(paramString, paramLong);
    paramDualDumpOutputStream.write("id", 1138166333441L, paramResolution.getId());
    paramDualDumpOutputStream.write("label", 1138166333442L, paramResolution.getLabel());
    paramDualDumpOutputStream.write("horizontal_DPI", 1120986464259L, paramResolution.getHorizontalDpi());
    paramDualDumpOutputStream.write("veritical_DPI", 1120986464260L, paramResolution.getVerticalDpi());
    paramDualDumpOutputStream.end(paramLong);
  }
}
