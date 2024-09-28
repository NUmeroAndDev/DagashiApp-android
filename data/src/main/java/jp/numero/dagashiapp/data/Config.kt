package jp.numero.dagashiapp.data

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

data class Config(
    val theme: Theme = Theme.default,
    val applyDynamicColor: Boolean = false
) {
    companion object {
        @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
        val enableDynamicColor: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }
}

enum class Theme {
    Light,
    Dark,
    FollowSystem;

    companion object {
        val default: Theme
            get() = if (isEnableFollowSystem) {
                FollowSystem
            } else {
                Light
            }

        fun toList(): List<Theme> {
            return buildList {
                add(Light)
                add(Dark)
                if (isEnableFollowSystem) {
                    add(FollowSystem)
                }
            }
        }

        @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
        val isEnableFollowSystem = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }
}