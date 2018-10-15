package com.alekseyvalyakin.roleplaysystem.data.payment

import android.app.Activity
import com.alekseyvalyakin.roleplaysystem.app.MainActivity
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*
import com.uber.rib.core.lifecycle.ActivityCallbackEvent
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import timber.log.Timber


class PaymentsInteractorImpl(
        private val activity: MainActivity
) : PaymentsInteractor {

    private val paymentClient = Wallet.getPaymentsClient(
            activity,
            Wallet.WalletOptions.Builder()
                    .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                    .build()
    )

    override fun subscribeListeningPaymentEvents(): Disposable {
        return activity.callbacks()
                .filter { it.type == ActivityCallbackEvent.Type.ACTIVITY_RESULT }
                .cast(ActivityCallbackEvent.ActivityResult::class.java)
                .subscribeWithErrorLogging { activityCallbackEvent ->
                    when (activityCallbackEvent.requestCode) {
                        // value passed in AutoResolveHelper
                        LOAD_PAYMENT_DATA_REQUEST_CODE -> when (activityCallbackEvent.resultCode) {
                            Activity.RESULT_OK -> {
                                val paymentData = PaymentData.getFromIntent(activityCallbackEvent.data!!)
                                val json = paymentData!!.toJson()
                                Timber.d("json $json")
                            }
                            AutoResolveHelper.RESULT_ERROR -> {
                                val status = AutoResolveHelper.getStatusFromIntent(activityCallbackEvent.data!!)!!
                                Timber.e("status $status")
                            }
                        }
                    }
                }

    }

    /**
     * Display the Google Pay payment sheet after interaction with the Google Pay payment button
     *
     * @param view optionally uniquely identify the interactive element prompting for payment
     */
    override fun requestPayment() {
        val paymentDataRequestJson = GooglePay.paymentDataRequest
                ?: return
        val request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString())
        if (request != null) {
            AutoResolveHelper.resolveTask(paymentClient.loadPaymentData(request), activity, LOAD_PAYMENT_DATA_REQUEST_CODE)
        }
    }

    override fun canPaySingle(): Single<Boolean> {
        return Single.create { emitter ->
            try {
                val isReadyToPayJson = GooglePay.isReadyToPayRequest
                        ?: throw RuntimeException("Unable to parse payJson")
                val request = IsReadyToPayRequest.fromJson(isReadyToPayJson.toString())
                        ?: throw throw RuntimeException("Unable to parse IsReadyToPay")
                val taskReady = paymentClient.isReadyToPay(request)!!

                taskReady.addOnCompleteListener { task ->
                    try {
                        val result = task.getResult(ApiException::class.java)
                                ?: throw RuntimeException("No result from isReadyToPay")
                        if (result) {
                            emitter.onSuccess(true)
                        } else {
                            emitter.onSuccess(false)
                        }
                    } catch (exception: ApiException) {
                        Timber.e(exception)
                        emitter.onError(exception)
                    }
                }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }

    }


    companion object {
        private const val LOAD_PAYMENT_DATA_REQUEST_CODE = 42
    }
}


interface PaymentsInteractor {
    fun canPaySingle(): Single<Boolean>

    fun requestPayment()

    fun subscribeListeningPaymentEvents(): Disposable
}