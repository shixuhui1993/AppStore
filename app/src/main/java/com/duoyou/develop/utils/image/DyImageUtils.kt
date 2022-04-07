package com.duoyou.develop.utils.image

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.provider.MediaStore
import android.util.Base64
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.TimeUtils
import com.duoyou.develop.application.DyApplication
import com.duoyou.develop.view.ToastHelper
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt


fun createImageFile(isSave: Boolean): File? {
    DyApplication.getApp()?.let {


        val fileDir = it.getExternalFilesDir(if (isSave) "image/save" else "image/share")

        if (!isSave && FileUtils.isFileExists(fileDir)) {
            FileUtils.deleteAllInDir(fileDir)
        }

        val createOrExistsDir =
            FileUtils.createOrExistsDir(fileDir)
        if (!createOrExistsDir) {
            return null
        }
        val imgName = "share_" + TimeUtils.getNowMills() + ".jpg"
        val filePath = fileDir?.absolutePath + "/" + imgName
        return File(filePath)
    } ?: return null
}

fun saveImage2SDCARD(imageBase64: String) {
    if (StringUtils.isEmpty(imageBase64)) {
        ToastHelper.showShort("图片不能为空")
        return
    }
    val file = createImageFile(true)
    file?.let { fi ->
        save2Album(getBitmap(imageBase64), fi)
        if (fi.length() > 10) {
            // 其次把文件插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(
                    DyApplication.getApp()?.contentResolver,


                    fi.absolutePath, fi.name, null
                )
                ToastHelper.showShort("保存图片成功")
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                ToastHelper.showShort("保存图片失败")
            }
        }
    } ?: ToastHelper.showShort("保存图片失败")
}

fun save2Album(bitmap: Bitmap?, fileName: File) {
    bitmap ?: return
    DyApplication.getApp()?.let {
        try {
            FileOutputStream(fileName).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
                fos.close()
                MediaScannerConnection.scanFile(it, arrayOf(fileName.toString()), null, null)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }


    }

}

fun shareImage(activity: Activity?, imageBase64: String) {
    val createImageFile = createImageFile(false)
    createImageFile?.let {
        save2Album(getBitmap(imageBase64), it)
        FileShareUtil.shareFile(activity, it)
    } ?: ToastHelper.showShort("创建图片失败")
}


//将返回的base64转换成图片
fun getBitmap(imageStr: String): Bitmap? {
    val decode = Base64.decode(
        imageStr.split(",".toRegex()).toTypedArray()[1],
        Base64.DEFAULT
    )
    val bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.size)
//    getBitmapByteSize(bitmap)
    return bitmap

}


/**
 * 获取bitmap转化为字节的大小
 * @param bitmap
 * @return
 */
fun getBitmapByteSize(bitmap: Bitmap?): Int {
    return if (bitmap == null) {
        0
    } else {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val size = baos.toByteArray().size
        try {
            baos.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        size
    }
}


/**
 * 根据压缩图片到固定的大小,因为会进行多次压缩可能会比较耗时，建议在异步线程调用
 * @param bitmap    原始图片
 * @param maxSize   压缩后的大小
 * @param needRecycle   是否需要回收被压的图片
 * @return
 */
fun compressBitmap(bitmap: Bitmap?, maxSize: Double, needRecycle: Boolean): ByteArray? {
    return if (bitmap == null) {
        null
    } else {
        val width = bitmap.width
        val height = bitmap.height
        //计算等比缩放
        val x = sqrt(maxSize / (width * height))
        val tmp = Bitmap.createScaledBitmap(
            bitmap,
            floor(width * x).toInt(),
            floor(height * x).toInt(),
            true
        )
        val baos = ByteArrayOutputStream()
        var options = 100
        //生产byte[]
        tmp.compress(Bitmap.CompressFormat.JPEG, options, baos)
        //判断byte[]与上线存储空间的大小
        if (baos.toByteArray().size > maxSize) {
            //根据内存大小的比例，进行质量的压缩
            options = ceil(maxSize / baos.toByteArray().size * 100).toInt()
            baos.reset()
            tmp.compress(Bitmap.CompressFormat.JPEG, options, baos)
            //循环压缩
            while (baos.toByteArray().size > maxSize) {
                baos.reset()
                val m = options * 1.0f - 1.5
                options = m.toInt()
                tmp.compress(Bitmap.CompressFormat.JPEG, options, baos)
            }
            recycle(tmp)
            if (needRecycle) {
                recycle(bitmap)
            }
        }
        val data = baos.toByteArray()
        try {
            baos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        data
    }
}

/**
 * 回收Bitmap
 * @param thumbBmp  需要被回收的bitmap
 */
fun recycle(thumbBmp: Bitmap?) {
    if (thumbBmp != null && !thumbBmp.isRecycled) {
        thumbBmp.recycle()
    }
}