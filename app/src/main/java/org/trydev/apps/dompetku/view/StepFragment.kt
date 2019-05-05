package org.trydev.apps.dompetku.view

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stepstone.stepper.BlockingStep
import com.stepstone.stepper.Step
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import org.trydev.apps.dompetku.R

class StepFragment:Fragment(), BlockingStep {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.stepper, container, false)
    }

    override fun onSelected() {

    }

    override fun verifyStep() = null

    override fun onError(error: VerificationError) {

    }

    override fun onBackClicked(callback: StepperLayout.OnBackClickedCallback?) {
        callback?.goToPrevStep()
    }

    override fun onCompleteClicked(callback: StepperLayout.OnCompleteClickedCallback?) {
        Handler().postDelayed({
            callback?.complete()
        },100L)
    }

    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        Handler().postDelayed({
            callback?.goToNextStep()
        },0L)
    }
}
