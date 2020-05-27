package chat.rocket.android.util.extensions

import android.app.Activity
import android.content.Intent

fun Intent.isSupportedLink(activity: Activity): Boolean {
    return (action == Intent.ACTION_VIEW && data != null && (data?.isDynamicLink(activity) == true ||
            data?.isAuthenticationDeepLink(activity) == true ||
            data?.isCustomSchemeRoomLink() == true ||
            data?.isWebSchemeRoomLink() == true))
}