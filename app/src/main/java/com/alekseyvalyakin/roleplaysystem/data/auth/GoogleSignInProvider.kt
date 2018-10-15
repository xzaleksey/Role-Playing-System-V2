package com.alekseyvalyakin.roleplaysystem.data.auth

import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.app.MainActivity
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.jakewharton.rxrelay2.PublishRelay
import com.uber.rib.core.lifecycle.ActivityCallbackEvent
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import timber.log.Timber


class GoogleSignInProvider constructor(
        private val activity: MainActivity,
        private val stringRepository: StringRepository
) : GoogleApiClient.OnConnectionFailedListener {

    private val relay = PublishRelay.create<GoogleSignInResult>()

    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(
                    activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

    private val googleApiClient = GoogleApiClient.Builder(activity)
            .enableAutoManage(activity, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()


    fun subscribeListeningGoogleSignInEvents(): Disposable {
        return activity.callbacks()
                .filter { it.type == ActivityCallbackEvent.Type.ACTIVITY_RESULT }
                .cast(ActivityCallbackEvent.ActivityResult::class.java)
                .subscribeWithErrorLogging { activityCallbackEvent ->
                    if (activityCallbackEvent.requestCode == RC_SIGN_IN) {
                        Timber.d("google sign in event")
                        val signInResultFromIntent = Auth.GoogleSignInApi.getSignInResultFromIntent(activityCallbackEvent.data)
                        if (signInResultFromIntent == null || !signInResultFromIntent.isSuccess) {
                            relay.accept(GoogleSignInResult(
                                    success = true,
                                    throwable = RuntimeException(signInResultFromIntent?.status?.statusMessage
                                            ?: stringRepository.getError())
                            ))
                        } else {
                            relay.accept(GoogleSignInResult(
                                    signInResultFromIntent.signInAccount,
                                    true
                            ))
                        }
                    }
                }

    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Timber.e(p0.errorMessage)
    }

    fun googleSignIn(): Observable<GoogleSignInResult> {
        return Completable.fromAction {
            val authorizeIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
            activity.startActivityForResult(authorizeIntent, RC_SIGN_IN)
        }.andThen(relay.take(1))
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    class GoogleSignInResult(
            val googleSignInAccount: GoogleSignInAccount? = null,
            val success: Boolean,
            val throwable: Throwable? = null
    )
}