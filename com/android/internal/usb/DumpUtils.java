package com.android.internal.usb;

import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbConfiguration;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbPort;
import android.hardware.usb.UsbPortStatus;
import com.android.internal.util.dump.DualDumpOutputStream;

public class DumpUtils
{
  public DumpUtils() {}
  
  public static void writeAccessory(DualDumpOutputStream paramDualDumpOutputStream, String paramString, long paramLong, UsbAccessory paramUsbAccessory)
  {
    paramLong = paramDualDumpOutputStream.start(paramString, paramLong);
    paramDualDumpOutputStream.write("manufacturer", 1138166333441L, paramUsbAccessory.getManufacturer());
    paramDualDumpOutputStream.write("model", 1138166333442L, paramUsbAccessory.getModel());
    com.android.internal.util.dump.DumpUtils.writeStringIfNotNull(paramDualDumpOutputStream, "description", 1138166333443L, paramUsbAccessory.getManufacturer());
    paramDualDumpOutputStream.write("version", 1138166333444L, paramUsbAccessory.getVersion());
    com.android.internal.util.dump.DumpUtils.writeStringIfNotNull(paramDualDumpOutputStream, "uri", 1138166333445L, paramUsbAccessory.getUri());
    paramDualDumpOutputStream.write("serial", 1138166333446L, paramUsbAccessory.getSerial());
    paramDualDumpOutputStream.end(paramLong);
  }
  
  private static void writeConfiguration(DualDumpOutputStream paramDualDumpOutputStream, String paramString, long paramLong, UsbConfiguration paramUsbConfiguration)
  {
    paramLong = paramDualDumpOutputStream.start(paramString, paramLong);
    paramDualDumpOutputStream.write("id", 1120986464257L, paramUsbConfiguration.getId());
    paramDualDumpOutputStream.write("name", 1138166333442L, paramUsbConfiguration.getName());
    paramDualDumpOutputStream.write("attributes", 1155346202627L, paramUsbConfiguration.getAttributes());
    paramDualDumpOutputStream.write("max_power", 1120986464260L, paramUsbConfiguration.getMaxPower());
    int i = paramUsbConfiguration.getInterfaceCount();
    for (int j = 0; j < i; j++) {
      writeInterface(paramDualDumpOutputStream, "interfaces", 2246267895813L, paramUsbConfiguration.getInterface(j));
    }
    paramDualDumpOutputStream.end(paramLong);
  }
  
  private static void writeDataRole(DualDumpOutputStream paramDualDumpOutputStream, String paramString, long paramLong, int paramInt)
  {
    if (paramDualDumpOutputStream.isProto()) {
      paramDualDumpOutputStream.write(paramString, paramLong, paramInt);
    } else {
      paramDualDumpOutputStream.write(paramString, paramLong, UsbPort.dataRoleToString(paramInt));
    }
  }
  
  public static void writeDevice(DualDumpOutputStream paramDualDumpOutputStream, String paramString, long paramLong, UsbDevice paramUsbDevice)
  {
    paramLong = paramDualDumpOutputStream.start(paramString, paramLong);
    paramDualDumpOutputStream.write("name", 1138166333441L, paramUsbDevice.getDeviceName());
    paramDualDumpOutputStream.write("vendor_id", 1120986464258L, paramUsbDevice.getVendorId());
    paramDualDumpOutputStream.write("product_id", 1120986464259L, paramUsbDevice.getProductId());
    paramDualDumpOutputStream.write("class", 1120986464260L, paramUsbDevice.getDeviceClass());
    paramDualDumpOutputStream.write("subclass", 1120986464261L, paramUsbDevice.getDeviceSubclass());
    paramDualDumpOutputStream.write("protocol", 1120986464262L, paramUsbDevice.getDeviceProtocol());
    paramDualDumpOutputStream.write("manufacturer_name", 1138166333447L, paramUsbDevice.getManufacturerName());
    paramDualDumpOutputStream.write("product_name", 1138166333448L, paramUsbDevice.getProductName());
    paramDualDumpOutputStream.write("version", 1138166333449L, paramUsbDevice.getVersion());
    paramDualDumpOutputStream.write("serial_number", 1138166333450L, paramUsbDevice.getSerialNumber());
    int i = paramUsbDevice.getConfigurationCount();
    for (int j = 0; j < i; j++) {
      writeConfiguration(paramDualDumpOutputStream, "configurations", 2246267895819L, paramUsbDevice.getConfiguration(j));
    }
    paramDualDumpOutputStream.end(paramLong);
  }
  
  private static void writeEndpoint(DualDumpOutputStream paramDualDumpOutputStream, String paramString, long paramLong, UsbEndpoint paramUsbEndpoint)
  {
    paramLong = paramDualDumpOutputStream.start(paramString, paramLong);
    paramDualDumpOutputStream.write("endpoint_number", 1120986464257L, paramUsbEndpoint.getEndpointNumber());
    paramDualDumpOutputStream.write("direction", 1159641169922L, paramUsbEndpoint.getDirection());
    paramDualDumpOutputStream.write("address", 1120986464259L, paramUsbEndpoint.getAddress());
    paramDualDumpOutputStream.write("type", 1159641169924L, paramUsbEndpoint.getType());
    paramDualDumpOutputStream.write("attributes", 1155346202629L, paramUsbEndpoint.getAttributes());
    paramDualDumpOutputStream.write("max_packet_size", 1120986464262L, paramUsbEndpoint.getMaxPacketSize());
    paramDualDumpOutputStream.write("interval", 1120986464263L, paramUsbEndpoint.getInterval());
    paramDualDumpOutputStream.end(paramLong);
  }
  
  private static void writeInterface(DualDumpOutputStream paramDualDumpOutputStream, String paramString, long paramLong, UsbInterface paramUsbInterface)
  {
    paramLong = paramDualDumpOutputStream.start(paramString, paramLong);
    paramDualDumpOutputStream.write("id", 1120986464257L, paramUsbInterface.getId());
    paramDualDumpOutputStream.write("alternate_settings", 1120986464258L, paramUsbInterface.getAlternateSetting());
    paramDualDumpOutputStream.write("name", 1138166333443L, paramUsbInterface.getName());
    paramDualDumpOutputStream.write("class", 1120986464260L, paramUsbInterface.getInterfaceClass());
    paramDualDumpOutputStream.write("subclass", 1120986464261L, paramUsbInterface.getInterfaceSubclass());
    paramDualDumpOutputStream.write("protocol", 1120986464262L, paramUsbInterface.getInterfaceProtocol());
    int i = paramUsbInterface.getEndpointCount();
    for (int j = 0; j < i; j++) {
      writeEndpoint(paramDualDumpOutputStream, "endpoints", 2246267895815L, paramUsbInterface.getEndpoint(j));
    }
    paramDualDumpOutputStream.end(paramLong);
  }
  
  public static void writePort(DualDumpOutputStream paramDualDumpOutputStream, String paramString, long paramLong, UsbPort paramUsbPort)
  {
    paramLong = paramDualDumpOutputStream.start(paramString, paramLong);
    paramDualDumpOutputStream.write("id", 1138166333441L, paramUsbPort.getId());
    int i = paramUsbPort.getSupportedModes();
    if (paramDualDumpOutputStream.isProto())
    {
      if (i == 0)
      {
        paramDualDumpOutputStream.write("supported_modes", 2259152797698L, 0);
      }
      else
      {
        if ((i & 0x3) == 3) {
          paramDualDumpOutputStream.write("supported_modes", 2259152797698L, 3);
        } else if ((i & 0x2) == 2) {
          paramDualDumpOutputStream.write("supported_modes", 2259152797698L, 2);
        } else if ((i & 0x1) == 1) {
          paramDualDumpOutputStream.write("supported_modes", 2259152797698L, 1);
        }
        if ((i & 0x4) == 4) {
          paramDualDumpOutputStream.write("supported_modes", 2259152797698L, 4);
        }
        if ((i & 0x8) == 8) {
          paramDualDumpOutputStream.write("supported_modes", 2259152797698L, 8);
        }
      }
    }
    else {
      paramDualDumpOutputStream.write("supported_modes", 2259152797698L, UsbPort.modeToString(i));
    }
    paramDualDumpOutputStream.end(paramLong);
  }
  
  public static void writePortStatus(DualDumpOutputStream paramDualDumpOutputStream, String paramString, long paramLong, UsbPortStatus paramUsbPortStatus)
  {
    paramLong = paramDualDumpOutputStream.start(paramString, paramLong);
    paramDualDumpOutputStream.write("connected", 1133871366145L, paramUsbPortStatus.isConnected());
    if (paramDualDumpOutputStream.isProto()) {
      paramDualDumpOutputStream.write("current_mode", 1159641169922L, paramUsbPortStatus.getCurrentMode());
    } else {
      paramDualDumpOutputStream.write("current_mode", 1159641169922L, UsbPort.modeToString(paramUsbPortStatus.getCurrentMode()));
    }
    writePowerRole(paramDualDumpOutputStream, "power_role", 1159641169923L, paramUsbPortStatus.getCurrentPowerRole());
    writeDataRole(paramDualDumpOutputStream, "data_role", 1159641169924L, paramUsbPortStatus.getCurrentDataRole());
    int i = paramUsbPortStatus.getSupportedRoleCombinations();
    while (i != 0)
    {
      int j = Integer.numberOfTrailingZeros(i);
      i &= 1 << j;
      int k = j / 3;
      long l = paramDualDumpOutputStream.start("role_combinations", 2246267895813L);
      writePowerRole(paramDualDumpOutputStream, "power_role", 1159641169921L, k + 0);
      writeDataRole(paramDualDumpOutputStream, "data_role", 1159641169922L, j % 3);
      paramDualDumpOutputStream.end(l);
    }
    paramDualDumpOutputStream.end(paramLong);
  }
  
  private static void writePowerRole(DualDumpOutputStream paramDualDumpOutputStream, String paramString, long paramLong, int paramInt)
  {
    if (paramDualDumpOutputStream.isProto()) {
      paramDualDumpOutputStream.write(paramString, paramLong, paramInt);
    } else {
      paramDualDumpOutputStream.write(paramString, paramLong, UsbPort.powerRoleToString(paramInt));
    }
  }
}
