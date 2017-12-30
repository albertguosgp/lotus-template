package com.lotus.kfd;

import io.vertx.core.AbstractVerticle;

public class KFDVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        System.out.println(this.getClass().getName() + " started");
    }
}
