package com.example.travcesadminpanelapp.utils.extensions

import android.Manifest
import android.content.Context
import android.os.Handler
import android.os.SystemClock
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.travcesadminpanelapp.view.activities.base.BaseActivity
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.lang.Math.*
import java.text.SimpleDateFormat
import java.util.*


val TYPE_SCHOOL_DRIVER = "1"
val TYPE_RENT_A_CAR_DRIVER = "0"
fun isEmailValid(target: CharSequence): Boolean {
    return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
}

fun isPhoneValid(phoneNumber: String): Boolean {
    return (!TextUtils.isEmpty(phoneNumber.trim { it <= ' ' })
            && Patterns.PHONE.matcher(phoneNumber.trim { it <= ' ' }).matches()
            && phoneNumber.length > 10)
}

fun loadProfileImage(imageUrl: String, imageView: ImageView) {
    Glide.with(imageView.context).load(imageUrl)
        .apply(RequestOptions().centerCrop().circleCrop().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
        .into(imageView)
}

fun isLocationEnabled(activity: BaseActivity): Boolean {
    var isGranted = false
    var isGrantedCoarse = false
    Dexter.withActivity((activity))
        .withPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                for (response: PermissionGrantedResponse in report!!.grantedPermissionResponses) {
                    if (response.permissionName == Manifest.permission.ACCESS_FINE_LOCATION)
                        isGranted = true
                    else if (response.permissionName == Manifest.permission.ACCESS_COARSE_LOCATION)
                        isGrantedCoarse = true
                }
                for (response: PermissionDeniedResponse in report.deniedPermissionResponses) {
                    if (response.permissionName == Manifest.permission.ACCESS_FINE_LOCATION)
                        isGranted = false
                    else if (response.permissionName == Manifest.permission.ACCESS_COARSE_LOCATION)
                        isGrantedCoarse = false
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
            }

        }).check()

    return isGranted && isGrantedCoarse
}

fun allFieldsFilled(vararg fields: String): Boolean {

    fields.forEach {
        if (it.isEmpty())
            return false
    }

    return true
}

fun anyFieldsFilled(vararg fields: String): Boolean {

    fields.forEach {
        if (it.isNotEmpty())
            return true
    }

    return false
}

//fun showImagePicker(fragment: Fragment) {
//
//    ImagePicker.create(fragment)
//        .limit(1)
//        .theme(R.style.AppTheme)
//        .includeVideo(false)
//        .showCamera(true)
//        .start()
//}

fun hideKeyboard(view: View) {
    try {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    } catch (e: NullPointerException) {
        e.printStackTrace()
    }
}

fun showKeyboard(view: View) {
    try {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    } catch (e: NullPointerException) {
        e.printStackTrace()
    }

}


fun getFormatedDateTime(
    dateStr: String,
    strReadFormat: String = "HH:mm:ss",
    strWriteFormat: String = "hh:mm a"
): String {

    var formattedDate = dateStr

    val readFormat = SimpleDateFormat(strReadFormat, Locale.getDefault())
    val writeFormat = SimpleDateFormat(strWriteFormat, Locale.getDefault())

    var date: Date? = null

    try {
        date = readFormat.parse(dateStr)
    } catch (e: Exception) {
    }

    if (date != null) {
        formattedDate = writeFormat.format(date)
    }

    return formattedDate
}

fun animateMarker(
    marker: Marker,
    finalPosition: LatLng, latLngInterpolator: LatLngInterpolator

) {
    val startPosition = marker.getPosition()
    val handler = Handler()
    val start = SystemClock.uptimeMillis()
    val interpolator = AccelerateDecelerateInterpolator()
    val durationInMs = 2000f
    handler.post(object : Runnable {
        var elapsed: Long = 0
        var t: Float = 0.toFloat()
        var v: Float = 0.toFloat()
        override fun run() {
            // Calculate progress using interpolator
            elapsed = SystemClock.uptimeMillis() - start
            t = elapsed / durationInMs
            v = interpolator.getInterpolation(t)
            marker.setPosition(latLngInterpolator.interpolate(v, startPosition, finalPosition))
            // Repeat till progress is complete.
            if (t < 1) {
                // Post again 16ms later.
                handler.postDelayed(this, 16)
            }
        }
    })

}

interface LatLngInterpolator {
    fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng
    object Spherical : LatLngInterpolator {
        /* From github.com/googlemaps/android-maps-utils */
        override fun interpolate(fraction: Float, from: LatLng, to: LatLng): LatLng {
            // http://en.wikipedia.org/wiki/Slerp
            val fromLat = toRadians(from.latitude)
            val fromLng = toRadians(from.longitude)
            val toLat = toRadians(to.latitude)
            val toLng = toRadians(to.longitude)
            val cosFromLat = cos(fromLat)
            val cosToLat = cos(toLat)
            // Computes Spherical interpolation coefficients.
            val angle = computeAngleBetween(fromLat, fromLng, toLat, toLng)
            val sinAngle = sin(angle)
            if (sinAngle < 1E-6) {
                return from
            }
            val a = sin((1 - fraction) * angle) / sinAngle
            val b = sin(fraction * angle) / sinAngle
            // Converts from polar to vector and interpolate.
            val x = a * cosFromLat * cos(fromLng) + b * cosToLat * cos(toLng)
            val y = a * cosFromLat * sin(fromLng) + b * cosToLat * sin(toLng)
            val z = a * sin(fromLat) + b * sin(toLat)
            // Converts interpolated vector back to polar.
            val lat = atan2(z, sqrt(x * x + y * y))
            val lng = atan2(y, x)
            return LatLng(toDegrees(lat), toDegrees(lng))
        }

        private fun computeAngleBetween(
            fromLat: Double,
            fromLng: Double,
            toLat: Double,
            toLng: Double
        ): Double {
            // Haversine's formula
            val dLat = fromLat - toLat
            val dLng = fromLng - toLng
            return 2 * asin(
                sqrt(
                    pow(
                        sin(dLat / 2),
                        2.0
                    ) + cos(fromLat) * cos(toLat) * pow(sin(dLng / 2), 2.0)
                )
            )
        }
    }
}

fun getRequestCode(): Int {
    val rnd = Random()
    return 100 + rnd.nextInt(900000)
}