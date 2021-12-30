package pavel.ivanov.pictureoftheday.viewmodel

import pavel.ivanov.pictureoftheday.repository.PictureOfTheDayResponseData

sealed class PictureOfTheDayState {
    data class Success(val pictureOfTheDayResponseData: PictureOfTheDayResponseData) : PictureOfTheDayState()
    data class Loading(val progress: Int?) : PictureOfTheDayState()
    data class Error(val error: Throwable) : PictureOfTheDayState()
}