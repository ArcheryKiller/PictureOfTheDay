package pavel.ivanov.pictureoftheday.ui.utils

import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.google.android.material.appbar.CollapsingToolbarLayout
import pavel.ivanov.pictureoftheday.databinding.BottomSheetLayoutBinding
import pavel.ivanov.pictureoftheday.databinding.FragmentOfTheDayBinding


open class Animation {
    private var isExpend = false
    private val transitionSet = TransitionSet()
    private val transitionCB = ChangeBounds()
    private val transitionImage = ChangeImageTransform()

    fun animatePicture (binding: FragmentOfTheDayBinding) {
        binding.imageView.setOnClickListener {
            isExpend = !isExpend

            val params = binding.imageView.layoutParams as ConstraintLayout.LayoutParams

            transitionCB.duration = 2000
            transitionImage.duration = 2000
            transitionSet.addTransition(transitionCB)
            transitionSet.addTransition(transitionImage)
            TransitionManager.beginDelayedTransition(binding.root, transitionSet)

            if (isExpend) {
                binding.imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                params.height = ConstraintLayout.LayoutParams.MATCH_PARENT
            } else {
                binding.imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
                params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
            }

            binding.imageView.layoutParams = params
        }
    }

    fun animatePicture (binding: BottomSheetLayoutBinding) {
        binding.mainBackdrop.setOnClickListener {
            isExpend = !isExpend

            val params = binding.mainBackdrop.layoutParams as CollapsingToolbarLayout.LayoutParams

            transitionCB.duration = 2000
            transitionImage.duration = 2000
            transitionSet.addTransition(transitionCB)
            transitionSet.addTransition(transitionImage)
            TransitionManager.beginDelayedTransition(binding.mainAppbar, transitionSet)

            if (isExpend) {
                binding.mainBackdrop.scaleType = ImageView.ScaleType.CENTER_CROP
                params.height = CollapsingToolbarLayout.LayoutParams.WRAP_CONTENT
            } else {
                binding.mainBackdrop.scaleType = ImageView.ScaleType.CENTER_INSIDE
                params.height = CollapsingToolbarLayout.LayoutParams.WRAP_CONTENT
            }

            binding.mainBackdrop.layoutParams = params
        }
    }
}