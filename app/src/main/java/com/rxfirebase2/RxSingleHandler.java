package com.rxfirebase2;


import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.rxfirebase2.exceptions.RxFirebaseNullDataException;

import io.reactivex.SingleEmitter;

public class RxSingleHandler<T> implements OnSuccessListener<T>, OnFailureListener, OnCompleteListener<T> {

    private final SingleEmitter<? super T> emitter;

    private RxSingleHandler(SingleEmitter<? super T> emitter) {
        this.emitter = emitter;
    }

    public static <T> void assignOnTask(SingleEmitter<? super T> emitter, Task<T> task) {
        RxSingleHandler handler = new RxSingleHandler(emitter);
        task.addOnSuccessListener(handler);
        task.addOnFailureListener(handler);
        try {
            task.addOnCompleteListener(handler);
        } catch (Throwable t) {
            // ignore
        }
    }

    @Override
    public void onSuccess(T res) {
        if (res != null) {
            emitter.onSuccess(res);
        } else {
            emitter.onError(new RxFirebaseNullDataException("Observables can't emit null values"));
        }
    }

    @Override
    public void onComplete(@NonNull Task<T> task) {
        emitter.onSuccess(task.getResult());
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        if (!emitter.isDisposed())
            emitter.onError(e);
    }
}