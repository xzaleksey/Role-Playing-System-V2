package com.rxfirebase2.exceptions;


import androidx.annotation.NonNull;

public class RxFirebaseNullDataException extends NullPointerException {

    public RxFirebaseNullDataException() {
    }

    public RxFirebaseNullDataException(@NonNull String detailMessage) {
        super(detailMessage);
    }

}
