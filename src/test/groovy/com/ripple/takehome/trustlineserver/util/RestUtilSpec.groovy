package com.ripple.takehome.trustlineserver.util

import com.ripple.takehome.trustlineserver.payload.TransactionRequest
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Specification
import spock.lang.Subject

class RestUtilSpec extends Specification {
    @Subject RestUtil restUtil
    RestTemplate restTemplate = Mock()

    def setup() {
        restUtil = new RestUtil(new RestTemplateBuilder())
        restUtil.restTemplate = restTemplate
    }

    def "post transaction - happy case"() {
        given:
        String url = "dummyUrl"
        TransactionRequest transactionRequest = new TransactionRequest()
        ResponseEntity<String> responseEntity = new ResponseEntity<>("DummyResponse", HttpStatus.OK)
        1 * restTemplate.postForEntity(url,_,String.class) >> responseEntity

        when:
        restUtil.postTransaction(url, transactionRequest)

        then: "no exception thrown"
        noExceptionThrown()
    }
}