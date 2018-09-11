package com.alekseyvalyakin.roleplaysystem.ribs.main

import com.alekseyvalyakin.roleplaysystem.data.game.GameRepository
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.Disposables

class CreateEmptyGameObservableProviderImpl(
        private val gameRepository: GameRepository
) : CreateEmptyGameObservableProvider {

    private var disposable = Disposables.disposed()
    private val relay = PublishRelay.create<CreateEmptyGameObservableProvider.CreateGameModel>()

    override fun createEmptyGameModel() {
        if (disposable.isDisposed) {
            disposable = gameRepository.createDocument()
                    .doOnSubscribe {
                        relay.accept(CreateEmptyGameObservableProvider.CreateGameModel.InProgress)
                    }
                    .subscribe({
                        relay.accept(CreateEmptyGameObservableProvider.CreateGameModel.GameCreateSuccess(it))
                    }, {
                        relay.accept(CreateEmptyGameObservableProvider.CreateGameModel.GameCreateFail(it))
                    })
        }
    }

    override fun observeCreateGameModel(): Flowable<CreateEmptyGameObservableProvider.CreateGameModel> {
        val flowable = relay.toFlowable(BackpressureStrategy.LATEST)
        if (!disposable.isDisposed) {
            return flowable.startWith(CreateEmptyGameObservableProvider.CreateGameModel.InProgress)
        }
        return flowable
    }


}