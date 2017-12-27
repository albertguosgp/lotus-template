package com.lotus.kfd;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class MainEntry {

    public static void main(String... args) {
        ClusterManager mgr = new HazelcastClusterManager();
        Vertx.clusteredVertx(new VertxOptions()
                .setWorkerPoolSize(40)
                .setClusterManager(mgr), clusterRes -> {
                    if (clusterRes.succeeded()) {
                        Vertx vertx = clusterRes.result();
                        vertx.createHttpServer()
                                .requestHandler(httpServerRequest ->
                                        httpServerRequest.response()
                                        .putHeader("content-length", String.valueOf("success".length()))
                                        .putHeader("content-type", "text/html")
                                        .setStatusCode(200)
                                        .write("success")
                                        .end()).listen(9090);
                    }
        });
    }
}
