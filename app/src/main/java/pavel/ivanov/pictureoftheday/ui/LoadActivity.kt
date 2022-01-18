package pavel.ivanov.pictureoftheday.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import pavel.ivanov.pictureoftheday.R

class LoadActivity : AppCompatActivity() {
    private val handler: Handler by lazy {
        Handler(mainLooper)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        findViewById<ImageView>(R.id.splashView).animate().rotationBy(720f).setInterpolator(
            LinearInterpolator()
        ).duration = 2000

        handler.postDelayed(Runnable {
            startActivity(Intent(this@LoadActivity, MainActivity::class.java))
        }, 2000)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}