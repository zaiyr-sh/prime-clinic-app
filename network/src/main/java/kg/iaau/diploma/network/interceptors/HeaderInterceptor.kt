package kg.iaau.diploma.network.interceptors

import kg.iaau.diploma.local_storage.StoragePreferences
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(val prefs: StoragePreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (prefs.token.isEmpty())
            return chain.proceed(request)

        return chain.proceed(
            request
                .newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authentication", "Bearer ${prefs.token}")
                .build()
        )
    }
}