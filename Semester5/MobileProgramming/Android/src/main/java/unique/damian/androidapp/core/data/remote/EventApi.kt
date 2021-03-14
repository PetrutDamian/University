package unique.damian.androidapp.core.data.remote

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import unique.damian.androidapp.Others.Api
import unique.damian.androidapp.core.data.Event


object EventApi {
    private const val URL = "http://192.168.56.1:3000/"

    interface Service {
        @GET("/api/events/")
        suspend fun getAll(): List<Event>

        @Headers("Content-Type: application/json")
        @POST("/api/events/")
        suspend fun create(@Body event: Event): Event

        @Headers("Content-Type: application/json")
        @PUT("/api/events/{id}")
        suspend fun update(@Path("id") eventId: String, @Body event: Event): Event

        @Headers("Content-Type: application/json")
        @DELETE("/api/events/{id}")
        suspend fun delete(@Path("id") id:String):String

    }

    val service: Service = Api.retrofit.create(
        Service::class.java)
}