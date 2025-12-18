package dev.shadoe.delta.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
internal fun BluetoothDeviceSelectionField(
  selectedDeviceName: String?,
  pairedDevices: List<Pair<String, String>>,
  selectedDeviceMac: String?,
  onDeviceSelected: (String, String) -> Unit,
  onRequestPermission: () -> Unit,
  onLoadDevices: () -> Unit,
  hasPermission: Boolean,
) {
  var showDialog by remember { mutableStateOf(false) }

  Row(
    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Icon(
      imageVector = Icons.Rounded.Devices,
      contentDescription =
        stringResource(R.string.bt_device_selection_field_icon),
    )
    Column(
      modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
      horizontalAlignment = Alignment.Start,
    ) {
      Text(
        text = stringResource(R.string.bt_device_selection_field_title),
        style = MaterialTheme.typography.titleLarge,
      )
      Text(
        text =
          selectedDeviceName
            ?: stringResource(R.string.bt_device_selection_no_device),
        style = MaterialTheme.typography.bodyMedium,
        color =
          if (selectedDeviceName != null) {
            MaterialTheme.colorScheme.onSurface
          } else {
            MaterialTheme.colorScheme.onSurfaceVariant
          },
      )
    }
    Button(
      onClick = {
        if (hasPermission) {
          onLoadDevices() // Load devices before showing dialog
          showDialog = true
        } else {
          onRequestPermission()
        }
      }
    ) {
      Text(stringResource(R.string.bt_device_selection_button))
    }
  }

  if (showDialog) {
    BluetoothDeviceSelectionDialog(
      pairedDevices = pairedDevices,
      selectedDeviceMac = selectedDeviceMac,
      onDeviceSelected = onDeviceSelected,
      onDismiss = { showDialog = false },
    )
  }
}
