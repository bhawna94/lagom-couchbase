package edu.knoldus.project.impl.utils;

import rx.Observable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class RxJavaUtil {
    
    public static <T> CompletableFuture<T> fromSingleObservable(Observable<T> observable) {
        final CompletableFuture<T> future = new CompletableFuture<>();
        observable
                .doOnError(future::completeExceptionally)
                .single()
                .forEach(future::complete);
        return future;
    }
    
    public static <T> CompletableFuture<Optional<T>> fromSingleOptObservable(Observable<T> observable) {
        final CompletableFuture<Optional<T>> future = new CompletableFuture<>();
        observable
                .map(Optional::ofNullable)
                .doOnError(future::completeExceptionally)
                .singleOrDefault(Optional.empty())
                .forEach(future::complete);
        return future;
    }
    public static <T> CompletableFuture<T> toCompletableFuture(Observable<T> observable) {
        CompletableFuture<T> future = new CompletableFuture<>();
        observable.single().subscribe(future::complete, future::completeExceptionally);
        return future;
    }
}
