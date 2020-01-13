package com.sarkisian.gh.util.error


import android.content.Context
import com.sarkisian.gh.R
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class ErrorHandler constructor(private val context: Context) {

    fun readError(error: Throwable, onResult: (String) -> Unit) {
        Timber.e(error)

        onResult(
            when (error) {
                is HttpException -> when (error.code()) {
                    304 -> context.getString(R.string.not_modified_error)
                    400 -> context.getString(R.string.bad_request_error)
                    401 -> context.getString(R.string.unauthorized_error)
                    403 -> context.getString(R.string.forbidden_error)
                    404 -> context.getString(R.string.not_found_error)
                    405 -> context.getString(R.string.method_not_allowed_error)
                    409 -> context.getString(R.string.conflict_error)
                    422 -> context.getString(R.string.unprocessable_error)
                    in 500..511 -> context.getString(R.string.server_error)
                    else -> context.getString(R.string.network_error)
                }
                is IOException -> context.getString(R.string.network_connection_error)
                else -> context.getString(R.string.unknown_network_error)
            }
        )
    }

}
