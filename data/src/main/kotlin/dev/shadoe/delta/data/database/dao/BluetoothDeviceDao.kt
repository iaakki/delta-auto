package dev.shadoe.delta.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.shadoe.delta.data.database.models.BluetoothDevice

@Dao
interface BluetoothDeviceDao {
  /**
   * Get the currently selected Bluetooth device for auto-enable feature.
   * Returns null if no device is selected.
   */
  @Query("SELECT * FROM BluetoothDevice WHERE id = 1")
  suspend fun getSelectedDevice(): BluetoothDevice?

  /**
   * Set or update the selected Bluetooth device.
   * Uses REPLACE strategy to ensure only one device is stored.
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun setSelectedDevice(device: BluetoothDevice)

  /**
   * Clear the selected Bluetooth device (disable device selection).
   */
  @Query("DELETE FROM BluetoothDevice WHERE id = 1")
  suspend fun clearSelectedDevice()
}
