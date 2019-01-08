package edu.knoldus.project.impl.utils;

import rx.Observable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class RxJavaUtil {
    
    public static <T> CompletableFuture<T> toCompletableFuture(Observable<T> observable) {
        CompletableFuture<T> future = new CompletableFuture<>();
        observable.single().subscribe(future::complete, future::completeExceptionally);
        return future;
    }
}
