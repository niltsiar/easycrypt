package dev.niltsiar.easycrypt

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.pm.ProviderInfo
import android.database.Cursor
import android.net.Uri

class EasyCryptInitProvider : ContentProvider() {

    companion object {

        @JvmSynthetic
        internal const val EMPTY_APPLICATION_ID_PROVIDER_AUTHORITY = "dev.niltsiar.easycrypt.easycryptinitprovider"

        @JvmStatic
        lateinit var applicationContext: Context
            private set
    }

    override fun onCreate(): Boolean {
        return context?.let {
            applicationContext = it
            true
        } ?: false
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? = null

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0

    override fun getType(uri: Uri): String? = null

    override fun attachInfo(context: Context?, info: ProviderInfo?) {
        checkContentProviderAuthority(info)
        super.attachInfo(context, info)
    }

    private fun checkContentProviderAuthority(info: ProviderInfo?) {
        checkNotNull(info) { "EasyCryptInitProvider ProviderInfo cannot be null" }
        check(EMPTY_APPLICATION_ID_PROVIDER_AUTHORITY != info.authority) {
            "Incorrect provider authority in manifest. Most likely due to a missing applicationId variable in application's gradle script"
        }
    }
}
