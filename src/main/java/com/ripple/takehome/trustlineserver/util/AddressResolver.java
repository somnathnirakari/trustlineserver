package com.ripple.takehome.trustlineserver.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AddressResolver {

    private Environment env;

    @Autowired
    public AddressResolver(Environment env) {
        this.env = env;
    }

    String resolve(String serverName) {
        return env.getProperty("trustline.node." + serverName.toLowerCase() + ".endpoint");
    }
}
