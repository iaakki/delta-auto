package dev.shadoe.delta.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bluetooth
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.shadoe.delta.R

@Composable
internal fun BluetoothDeviceSelectionDialog(
  pairedDevices: List<Pair<String, String>>, // List of (MAC address, device name)
  selectedDeviceMac: String?,
  onDeviceSelected: (String, String) -> Unit,
  onDismiss: () -> Unit,
) {
  var tempSelection by remember { mutableStateOf(selectedDeviceMac) }

  AlertDialog(
    onDismissRequest = onDismiss,
    icon = {
      Icon(
        imageVector = Icons.Rounded.Bluetooth,
        contentDescription = null,
      )
    },
    title = {
      Text(text = stringResource(R.string.bt_device_selection_field_title))
    },
    text = {
      if (pairedDevices.isEmpty()) {
        Text(
          text = "No paired Bluetooth devices found. Please pair a device first.",
          style = MaterialTheme.typography.bodyMedium,
        )
      } else {
        LazyColumn {
          items(pairedDevices) { (macAddress, deviceName) ->
            Row(
              modifier =
                Modifier.fillMaxWidth()
                  .clickable { tempSelection = macAddress }
                  .padding(vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              RadioButton(
                selected = tempSelection == macAddress,
                onClick = { tempSelection = macAddress },
              )
              Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                  text = deviceName,
                  style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                  text = macAddress,
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }
            }
          }
        }
      }
    },
    confirmButton = {
      TextButton(
        onClick = {
          tempSelection?.let { mac ->
            val deviceName =
              pairedDevices.find { it.first == mac }?.second ?: "Unknown"
            onDeviceSelected(mac, deviceName)
          }
          onDismiss()
        },
        enabled = tempSelection != null && pairedDevices.isNotEmpty(),
      ) {
        Text(stringResource(android.R.string.ok))
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text(stringResource(android.R.string.cancel))
      }
    },
  )
}
