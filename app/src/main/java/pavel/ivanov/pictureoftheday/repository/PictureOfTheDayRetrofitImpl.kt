package pavel.ivanov.pictureoftheday.repository

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PictureOfTheDayRetrofitImpl {
    private val baseUrl = "https://api.nasa.gov/"

    private val api by lazy {
        val retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(
                GsonConverterFactory.create(GsonBuilder().setLenient().create())
            ).build()
        retrofit.create(PictureOfTheDayAPI::class.java)
    }

    fun getRetrofitImpl(): PictureOfTheDayAPI {
        return api
    }
}