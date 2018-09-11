package com.alekseyvalyakin.roleplaysystem.ribs.main

import com.alekseyvalyakin.roleplaysystem.data.game.GameRepository
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.Disposables

class CreateGameObservableProviderImpl(
        private val gameRepository: GameRepository
) : CreateGameObservableProvider {

    private var disposable = Disposables.disposed()
    private val relay = PublishRelay.create<CreateGameObservableProvider.CreateGameModel>()

    override fun createEmptyGameModel() {
        if (disposable.isDisposed) {
            disposable = gameRepository.createDocument()
                    .subscribe({
                        relay.accept(CreateGameObservableProvider.CreateGameModel.GameCreateSuccess())
                    }, {

                    })
        }
    }

    override fun observeCreateGameModel(): Flowable<CreateGameObservableProvider.CreateGameModel> {
        return relay.toFlowable(BackpressureStrategy.LATEST)
    }


}