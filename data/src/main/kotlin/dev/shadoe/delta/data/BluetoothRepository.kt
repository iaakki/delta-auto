package dev.shadoe.delta.data

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.shadoe.delta.data.database.dao.BluetoothDeviceDao
import dev.shadoe.delta.data.database.models.BluetoothDevice
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for managing Bluetooth device selection for auto-enable hotspot feature.
 */
@Singleton
class BluetoothRepository
@Inject
constructor(
  private val bluetoothDeviceDao: BluetoothDeviceDao,
  @ApplicationContext private val context: Context,
) {
  /**
   * Get the currently selected Bluetooth device for auto-enable feature.
   * Returns null if no device is selected.
   */
  suspend fun getSelectedDevice(): BluetoothDevice? {
    return bluetoothDeviceDao.getSelectedDevice()
  }

  /**
   * Set the selected Bluetooth device for auto-enable feature.
   * Only one device can be selected at a time.
   */
  suspend fun setSelectedDevice(macAddress: String, deviceName: String) {
    bluetoothDeviceDao.setSelectedDevice(
      BluetoothDevice(id = 1, macAddress = macAddress, deviceName = deviceName)
    )
  }

  /**
   * Clear the selected Bluetooth device (disables device-specific auto-enable).
   */
  suspend fun clearSelectedDevice() {
    bluetoothDeviceDao.clearSelectedDevice()
  }

  /**
   * Get list of paired Bluetooth devices.
   * Returns a list of (MAC address, device name) pairs.
   * Requires BLUETOOTH_CONNECT permission on Android 12+.
   */
  @RequiresPermission(
    value = Manifest.permission.BLUETOOTH_CONNECT,
    conditional = true,
  )
  fun getPairedDevices(): List<Pair<String, String>> {
    val bluetoothManager =
      context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
        ?: return emptyList()

    val bluetoothAdapter = bluetoothManager.adapter ?: return emptyList()

    // On Android 12+, BLUETOOTH_CONNECT permission is required
    // On earlier versions, no special permission is needed for bonded devices
    return try {
      bluetoothAdapter.bondedDevices?.map { device ->
        device.address to (device.name ?: "Unknown Device")
      } ?: emptyList()
    } catch (e: SecurityException) {
      // Permission not granted
      emptyList()
    }
  }

  /**
   * Check if Bluetooth is available and enabled on the device.
   */
  fun isBluetoothAvailable(): Boolean {
    val bluetoothManager =
      context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
        ?: return false
    val bluetoothAdapter = bluetoothManager.adapter ?: return false
    return bluetoothAdapter.isEnabled
  }
}
