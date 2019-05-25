package com.ripple.takehome.trustlineserver.util

import org.springframework.core.env.Environment
import spock.lang.Specification
import spock.lang.Subject

class AddressResolverSpec extends Specification {
    @Subject AddressResolver addressResolver

    Environment env = Mock()

    def setup() {
        addressResolver = new AddressResolver(env)
    }

    def "resolve endpoint - happy case"() {
        given:
            String serverName = "testServer"
            String dummyEndpoint = "dummy.endpoint"
            1 * env.getProperty("trustline.node." + serverName.toLowerCase() + ".endpoint") >> dummyEndpoint
        when:
            String endpoint = addressResolver.resolve(serverName)
        then:
            endpoint
            endpoint == dummyEndpoint
    }

    def "resolve endpoint - not found"() {
        given:
            String serverName = "testServer"
            String dummyEndpoint = "dummy.endpoint"
            0 * env.getProperty("trustline.node." + serverName.toLowerCase() + ".endpoint") >> dummyEndpoint
        when:
            String endpoint = addressResolver.resolve(serverName+"fake")
        then:
            endpoint == null
    }
}
