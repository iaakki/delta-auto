package dev.shadoe.delta

import android.app.Application
import android.content.Intent
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import dev.shadoe.delta.crash.CrashHandlerUtils
import dev.shadoe.delta.data.FlagsRepository
import javax.inject.Inject
import kotlin.system.exitProcess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.lsposed.hiddenapibypass.HiddenApiBypass

@HiltAndroidApp
class Application : Application() {
  @Inject lateinit var flagsRepository: FlagsRepository

  private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

  override fun onCreate() {
    super.onCreate()

    Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
      Log.e(packageName, "Uncaught exception", throwable)
      CrashHandlerUtils.sendCrashNotification(applicationContext, throwable)
      exitProcess(1)
    }

    HiddenApiBypass.setHiddenApiExemptions("L")

    // Start Bluetooth service if Auto Enable on BT is enabled
    applicationScope.launch {
      val isAutoEnableEnabled: Boolean = flagsRepository.isAutoEnableOnBtEnabled()
      if (isAutoEnableEnabled) {
        val serviceIntent = Intent(this@Application, BluetoothAutoEnableService::class.java)
        startForegroundService(serviceIntent)
        Log.d("Application", "Started BluetoothAutoEnableService")
      }
    }
  }
}
