package com.esafirm.imagepicker.features

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.esafirm.imagepicker.features.cameraonly.CameraOnlyConfig
import com.esafirm.imagepicker.features.common.BaseConfig
import com.esafirm.imagepicker.helper.ConfigUtils.checkConfig
import com.esafirm.imagepicker.helper.LocaleManager
import com.esafirm.imagepicker.model.Image
import site.starsone.xandroidutil.util.startActivityForResult

/* --------------------------------------------------- */
/* > Ext */
/* --------------------------------------------------- */

class ImagePickerLauncher(
    private val context: () -> Context,
    private val resultLauncher: ActivityResultLauncher<Intent>
) {
    fun launch(config: BaseConfig = ImagePickerConfig()) {
        val finalConfig = if (config is ImagePickerConfig) checkConfig(config) else config
        val intent = createImagePickerIntent(context(), finalConfig)
        resultLauncher.launch(intent)
    }
}

typealias ImagePickerCallback = (List<Image>) -> Unit

/**
 * 新增的选择图片(无需注意生命周期)
 */
fun ComponentActivity.startPickerImg(config: BaseConfig = ImagePickerConfig(),  callback: ImagePickerCallback) {
    val finalConfig = if (config is ImagePickerConfig) checkConfig(config) else config
    startActivityForResult(createImagePickerIntent(this,finalConfig)){
        val images = ImagePicker.getImages(it.data) ?: emptyList()
        callback.invoke(images)
    }
}

/**
 * 新增的选择图片(无需注意生命周期)
 */
fun Fragment.startPickerImg(config: BaseConfig = ImagePickerConfig(),  callback: ImagePickerCallback) {
    val finalConfig = if (config is ImagePickerConfig) checkConfig(config) else config
    val activity = requireActivity()
    activity.startActivityForResult(createImagePickerIntent(activity,finalConfig)){
        val images = ImagePicker.getImages(it.data) ?: emptyList()
        callback.invoke(images)
    }
}


fun Fragment.registerImagePicker(
    context: () -> Context = { requireContext() },
    callback: ImagePickerCallback
): ImagePickerLauncher {
    return ImagePickerLauncher(context, createLauncher(callback))
}

fun ComponentActivity.registerImagePicker(
    context: () -> Context = { this },
    callback: ImagePickerCallback
): ImagePickerLauncher {
    return ImagePickerLauncher(context, createLauncher(callback))
}

fun createImagePickerIntent(context: Context, config: BaseConfig): Intent {
    val intent = Intent(context, ImagePickerActivity::class.java)
    when (config) {
        is ImagePickerConfig -> {
            config.language?.run { LocaleManager.language = this }
            intent.putExtra(ImagePickerConfig::class.java.simpleName, config)
        }
        is CameraOnlyConfig -> {
            intent.putExtra(CameraOnlyConfig::class.java.simpleName, config)
        }
    }
    return intent
}

/* --------------------------------------------------- */
/* > Launcher Creation */
/* --------------------------------------------------- */

private fun Fragment.createLauncher(callback: ImagePickerCallback) =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val images = ImagePicker.getImages(it.data) ?: emptyList()
        callback(images)
    }

private fun ComponentActivity.createLauncher(callback: ImagePickerCallback) =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val images = ImagePicker.getImages(it.data) ?: emptyList()
        callback(images)
    }
