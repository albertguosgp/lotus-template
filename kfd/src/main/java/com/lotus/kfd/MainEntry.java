package com.lotus.kfd;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class MainEntry {

    public static void main(String... args) {
        Vertx.vertx(new VertxOptions())
                .createHttpServer()
                .requestHandler(httpServerRequest -> {
            httpServerRequest.response()
                    .putHeader("content-length", String.valueOf("success".length()))
                    .putHeader("content-type", "text/html")
                    .setStatusCode(200)
                    .write("success")
                    .end();
        }).listen(9090);
    }
}
