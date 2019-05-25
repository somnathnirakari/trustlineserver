package com.ripple.takehome.trustlineserver.api

import com.ripple.takehome.trustlineserver.payload.TransactionRequest
import com.ripple.takehome.trustlineserver.util.TransactionRouter
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Import
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

import spock.lang.Specification

@SpringBootTest(webEnvironment = RANDOM_PORT, properties = ["server.name=Alice"])
//@Import(value = {IntegrationTestConfiguration})
class TrustLineControllerSpec extends Specification{

    //@Autowired MockMvc mvc

    @SpringBean
    TransactionRouter transactionRouter = Mock()

    @Autowired
    TestRestTemplate restTemplate

    def setup() {
        System.setProperty("node.name", "Alice")
    }

    def "TrustLineController - Client request Happy path"() {
        given:
        TransactionRequest transactionRequest = new TransactionRequest()
        transactionRequest.from="Alice"
        transactionRequest.to="Bob"
        transactionRequest.amount=10

        when:
        1 * transactionRouter.doesTrustLineExistBetween("Alice","Bob") >> true
        1 * transactionRouter.route(transactionRequest)
        HttpEntity<TransactionRequest> request = new HttpEntity<>(transactionRequest);
        ResponseEntity<String> resp = restTemplate.postForEntity("/api/transactions", request, String.class)

        then:
        noExceptionThrown()
        resp.statusCode == HttpStatus.OK
    }

    def "TrustLineController - Client request Receiver node is unreachable"() {
        given:
        TransactionRequest transactionRequest = new TransactionRequest()
        transactionRequest.from="Alice"
        transactionRequest.to="Bob"
        transactionRequest.amount=10

        when:
        1 * transactionRouter.doesTrustLineExistBetween("Alice","Bob") >> true
        1 * transactionRouter.route(transactionRequest) >> { throw new RuntimeException()}
        HttpEntity<TransactionRequest> request = new HttpEntity<>(transactionRequest);
        ResponseEntity<String> resp = restTemplate.postForEntity("/api/transactions", request, String.class)

        then:
        resp.statusCode == HttpStatus.INTERNAL_SERVER_ERROR
        resp.body.indexOf("Error sending transaction to " + transactionRequest.getTo()) != -1

    }

    def "TrustLineController - Trustline receive Happy path"() {
        given:
        TransactionRequest transactionRequest = new TransactionRequest()
        transactionRequest.from="Bob"
        transactionRequest.to="Alice"
        transactionRequest.amount=10

        when:
        HttpEntity<TransactionRequest> request = new HttpEntity<>(transactionRequest);
        ResponseEntity<String> resp = restTemplate.postForEntity("/api/trustline/transactions", request, String.class)

        then:
        noExceptionThrown()
        resp.statusCode == HttpStatus.OK
    }
}
