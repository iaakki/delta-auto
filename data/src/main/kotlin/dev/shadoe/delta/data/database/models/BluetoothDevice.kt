package dev.shadoe.delta.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Stores the selected Bluetooth device for auto-enable hotspot feature.
 * Only one device can be selected at a time (enforced by single-row table).
 */
@Entity
data class BluetoothDevice(
  @PrimaryKey(autoGenerate = false) val id: Int = 1,
  val macAddress: String,
  val deviceName: String,
)
