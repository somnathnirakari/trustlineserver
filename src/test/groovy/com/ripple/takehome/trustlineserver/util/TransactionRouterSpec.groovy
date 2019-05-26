package com.ripple.takehome.trustlineserver.util

import com.ripple.takehome.trustlineserver.exception.NodeAddressResolutionFailureException
import com.ripple.takehome.trustlineserver.exception.TrustLineTransactionFailureException
import com.ripple.takehome.trustlineserver.payload.TransactionRequest
import org.springframework.core.env.Environment
import spock.lang.Specification
import spock.lang.Subject

class TransactionRouterSpec extends Specification {
    @Subject
    TransactionRouter transactionRouter
    RestUtil restUtil = Mock()
    AddressResolver addressResolver = Mock()
    Environment env = Mock()

    def setup() {
        transactionRouter = new TransactionRouter(restUtil, addressResolver, env)
    }

    def "route transaction - happy case"() {
        given:
        TransactionRequest transactionRequest = new TransactionRequest()
        transactionRequest.amount = 10
        transactionRequest.from = "Alice"
        transactionRequest.to = "Bob"
        String url = "dummyUrl"
        1 * addressResolver.resolve(transactionRequest.to) >> url
        1 * restUtil.postTransaction(url, transactionRequest)

        when:
        transactionRouter.route(transactionRequest)

        then: 'no exceptions must be thrown'
        noExceptionThrown()

    }

    def "route transaction - address not resolved"() {
        given:
        TransactionRequest transactionRequest = new TransactionRequest()
        transactionRequest.amount = 10
        transactionRequest.from = "Alice"
        transactionRequest.to = "Bob"
        String url = "dummyUrl"
        1 * addressResolver.resolve(transactionRequest.to) >> null
        0 * restUtil.postTransaction(url, transactionRequest)

        when:
        transactionRouter.route(transactionRequest)

        then: 'run time exception thrown'
        NodeAddressResolutionFailureException narfe = thrown()
        narfe.message == "Unable to resolve address for receiver " + transactionRequest.getTo()

    }

    def "route transaction - restUtil throws exception"() {
        given:
        TransactionRequest transactionRequest = new TransactionRequest()
        transactionRequest.amount = 10
        transactionRequest.from = "Alice"
        transactionRequest.to = "Bob"
        String url = "dummyUrl"
        1 * addressResolver.resolve(transactionRequest.to) >> url
        1 * restUtil.postTransaction(url, transactionRequest) >> {
            throw new RuntimeException("Invalid Response from " + transactionRequest.getTo())
        }

        when:
        transactionRouter.route(transactionRequest)

        then: 'run time exceptions thrown'
        TrustLineTransactionFailureException tltfe = thrown()
        tltfe.message == "Failed to process transaction at node " +
                transactionRequest.getTo()
    }
}
