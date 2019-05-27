package com.ripple.takehome.trustlineserver.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Class for resolving node's logical name to routing address
 * @author Somnath Nirakari
 */
@Component
public class AddressResolver {

    private Environment env;

    @Autowired
    public AddressResolver(Environment env) {
        this.env = env;
    }

    /**
     * resolves nodeName to its routing address
     * @param nodeName
     * @return routing address
     */
    String resolve(String nodeName) {
        return env.getProperty("trustline.node." + nodeName.toLowerCase() + ".endpoint");
    }
}
