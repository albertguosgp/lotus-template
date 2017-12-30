package com.lotus.kfd;

import com.hazelcast.core.IQueue;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.ext.web.Router;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

import java.time.LocalDateTime;

public class MainEntry {

    public static void main(String... args) {
        HazelcastClusterManager hazelcastClusterManager = new HazelcastClusterManager();

        ClusterManager mgr = hazelcastClusterManager;
        Vertx.clusteredVertx(new VertxOptions()
                .setWorkerPoolSize(40)
                .setClusterManager(mgr), clusterRes -> {
            if (clusterRes.succeeded()) {
                Vertx vertx = clusterRes.result();
                HttpServer httpServer = vertx.createHttpServer();
                IQueue<Object> queue = hazelcastClusterManager.getHazelcastInstance().getQueue("car-sell");

                for (int i = 0; i< 2000; i++) {
                    try {
                        if (queue.size() < 2000) {
                            queue.put("resource");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("Confirmed queue size is " + queue.size());
                Router router = Router.router(vertx);
                router.route("/p/ta")
                                .handler(httpServerRequest -> {

                                    Object resource = queue.poll();
                                    System.out.println("received request " + resource
                                            + " " + LocalDateTime.now());
                                    if (resource == null) {
                                        httpServerRequest.response().setStatusCode(500).end();
                                    } else

                                        httpServerRequest.response()
                                                .putHeader("content-length", String.valueOf("success".length()))
                                                .putHeader("content-type", "text/html")
                                                .setStatusCode(200)
                                                .write("success")
                                                .end();
                                    });
                router.route("/abc/d")
                                .handler((context) -> {
                                    context.response()
                                            .putHeader("content-length", String.valueOf("heihei".length()))
                                            .putHeader("content-type", "text/html")
                                            .setStatusCode(200)
                                            .write("heihei")
                                            .end();
                                });
                httpServer.requestHandler(router::accept).listen(Integer.parseInt(args[0]));
            }
        });
    }
}
