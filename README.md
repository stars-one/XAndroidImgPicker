<p align="center">
	<img  src="https://github.com/esafirm/android-image-picker/blob/master/art/logo.png?raw=true" width="140" height="140"/> 
</p>

<h2 align="center">xAndroidImgPicker</h2>

一个Android图片选择库,脱身于[esafirm/android-image-picker: Image Picker for Android 🤖](https://github.com/esafirm/android-image-picker)

> 起因是源库已经一年年多不维护了,且3.1.0版本版本进行了gradle和kotlin版本更新,导致我的旧项目依赖后无法成功使用,但3.0.0版本可以,所以就是基于3.0.0版本进行了后续改造和适配

<a href="https://jitpack.io/#esafirm/android-image-picker">
<img src="https://jitpack.io/v/esafirm/android-image-picker.svg" alt="jitpack - android image picker" />
</a>

# 效果动图
<img src="https://raw.githubusercontent.com/esafirm/android-image-picker/master/art/ss.gif" height="460" width="284"/>

## 安装使用

1.添加jitpack仓库源

```groovy
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

2.添加依赖

版本号键上方的jitpack图标或右侧release列表的tag标签

```groovy
dependencies {
  implementation 'com.github.esafirm:android-image-picker:x.y.z'
}
```

# 使用

For full example, please refer to the `sample` app.

Also you can browse the issue labeled as question [here](https://github.com/esafirm/android-image-picker/issues?utf8=%E2%9C%93&q=label%3Aquestion+)

## 打开图片选择Activity
```kotlin
startPickerImg {
    //返回选择的图片列表
    val images : List<Image> = it
    
    //Image对象可获取下面属性
    val id = image.id // 1254
    val uri = image.uri // content://meida/extrnal/images/media/1254
    val path =  image.path // /storage/emulated/0/Pictures/5522xx.jpg
    val name = image.name // 5522xx.jpg
}
```

**startPickerImg为扩展方法,可直接在Activity或Fragment中调用**

### 自定义选择配置参数

```kotlin
val config = ImagePickerConfig {
  mode = ImagePickerMode.SINGLE // default is multi image mode
  language = "in" // Set image picker language
  theme = R.style.ImagePickerTheme

  // set whether pick action or camera action should return immediate result or not. Only works in single mode for image picker
  returnMode = if (returnAfterCapture) ReturnMode.ALL else ReturnMode.NONE

  isFolderMode = folderMode // set folder mode (false by default)
  isIncludeVideo = includeVideo // include video (false by default)
  isOnlyVideo = onlyVideo // include video (false by default)
  arrowColor = Color.RED // set toolbar arrow up color
  folderTitle = "Folder" // folder selection title
  imageTitle = "Tap to select" // image selection title
  doneButtonText = "DONE" // done button text
  limit = 10 // max images can be selected (99 by default)
  isShowCamera = true // show camera or not (true by default)
  savePath = ImagePickerSavePath("Camera") // captured image directory name ("Camera" folder by default)
  savePath = ImagePickerSavePath(Environment.getExternalStorageDirectory().path, isRelative = false) // can be a full path

  excludedImages = images.toFiles() // don't show anything on this selected images
  selectedImages = images  // original selected images, used in multi mode
}


//传递config打开图片选择页面
startPickerImg(config){
    
}
```

如果想要单独使用相机,可以使用`CameraOnlyConfig`,如下代码:
```kotlin
startPickerImg(CameraOnlyConfig()){
    
}
```

## 官方补充的其他方法

### 1.使用ActivityResultApi

```kotlin
//这个得作为Activity或Fragment的成员变量,放在外层
val launcher = registerImagePicker {
  //处理选择图片后的回调结果
}

//在点击事件调用此启动,打开图片选择页面
launcher.launch()
```

### 2.使用传统的startActivityForResult方法

```kotlin
val intent = createImagePickerIntent(context, ImagePickerConfig())
startActivityForResult(intent, RC_IMAGE_PICKER)

```
在onActivityResult生命周期处理结果回调

```kotlin
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (resultCode == Activity.RESULT_OK && requestCode == IpCons.RC_IMAGE_PICKER && data != null) {
        val images = ImagePicker.getImages(data) ?: emptyList()
        return
    }
    super.onActivityResult(requestCode, resultCode, data)
}
```

# Wiki

- [自定义页面组件Custom components](https://github.com/esafirm/android-image-picker/blob/master/docs/custom_components.md)
- [使用其他图片加载库](https://github.com/esafirm/android-image-picker/blob/master/docs/another_image_library.md)
- [返回模式](https://github.com/esafirm/android-image-picker/blob/master/docs/return_mode.md)
- [关于相机拍照图片的存储位置](https://github.com/esafirm/android-image-picker/blob/master/docs/save_location.md)