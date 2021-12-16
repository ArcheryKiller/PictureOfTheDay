package pavel.ivanov.pictureoftheday.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pavel.ivanov.pictureoftheday.R
import pavel.ivanov.pictureoftheday.ui.picture.PictureOfTheDayFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                .commit()
        }
    }
}