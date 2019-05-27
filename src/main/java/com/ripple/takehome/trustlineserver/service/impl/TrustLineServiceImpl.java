package com.ripple.takehome.trustlineserver.service.impl;

import com.ripple.takehome.trustlineserver.exception.BadRequestException;
import com.ripple.takehome.trustlineserver.payload.TransactionRequest;
import com.ripple.takehome.trustlineserver.service.TrustLineService;
import com.ripple.takehome.trustlineserver.util.TransactionRouter;
import com.ripple.takehome.trustlineserver.util.TrustLineBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Somnath Nirakari
 */
@Service
public class TrustLineServiceImpl implements TrustLineService {

    @Value("${node.name}")
    String nodeName;

    // Ideally this should be an external component that keeps track of relationships between nodes and
    // knows how to route transaction between them
    @Autowired
    TransactionRouter transactionRouter;

    Map<String,TrustLineBalance> trustLineBalanceMap = new HashMap<>();

    //private static final Logger LOGGER = LoggerFactory.getLogger(TrustLineServiceImpl.class);

    @Override
    public void sendTransaction(TransactionRequest transactionRequest) {
        if (transactionRequest.getTo().equals(nodeName)) {
            throw new BadRequestException("Sender and receiver cannot be identical");
        }

        if (!transactionRouter.doesTrustLineExistBetween(nodeName, transactionRequest.getTo())) {
            throw new BadRequestException("Trustline does not exist between " + nodeName + " and " + transactionRequest.getTo());
        }

        TrustLineBalance trustLineBalance = trustLineBalanceMap.get(transactionRequest.getTo());
        if (trustLineBalance==null) {
            trustLineBalanceMap.put(transactionRequest.getTo(),new TrustLineBalance());
            trustLineBalance = trustLineBalanceMap.get(transactionRequest.getTo());
        }

        System.out.println("Trustline balance between " + nodeName + " and " + transactionRequest.getTo() + " is: " + trustLineBalance.getAmount());
        //LOGGER.info("Trustline balance is: " + trustLineBalance.getAmount());

        System.out.println("Starting new transaction(Send)..");
        System.out.println("Paying " + transactionRequest.getAmount() + " to " + transactionRequest.getTo() + "...");
        //LOGGER.info("Paying {} to {}...", transactionRequest.getAmount(), transactionRequest.getTo());
        transactionRequest.setFrom(nodeName);

        try {
            transactionRouter.route(transactionRequest);
        } catch (Exception ex) {
            System.out.println("Error sending transaction to " + transactionRequest.getTo());
            throw new RuntimeException("Error sending transaction to " + transactionRequest.getTo());
        }

        trustLineBalance.update(transactionRequest.getAmount() * -1);
        System.out.println("Sent");
        //LOGGER.info("Sent");

        System.out.println("Trustline balance is: " + trustLineBalance.getAmount()+ "\n");
        //LOGGER.info("Trustline balance is: " + trustLineBalance.getAmount());
    }

    @Override
    public void receiveTransaction(TransactionRequest transactionRequest) {
        if (!transactionRequest.getTo().equals(nodeName)) {
            throw new BadRequestException("Invalid receiver");
        }
        TrustLineBalance trustLineBalance = trustLineBalanceMap.get(transactionRequest.getFrom());
        if (trustLineBalance==null) {
            trustLineBalanceMap.put(transactionRequest.getFrom(),new TrustLineBalance());
            trustLineBalance = trustLineBalanceMap.get(transactionRequest.getFrom());
        }

        System.out.println("Trustline balance between " + nodeName + " and " + transactionRequest.getFrom() + " is: " + trustLineBalance.getAmount());
        //LOGGER.info("Trustline balance is: " + trustLineBalance.getAmount());

        System.out.println("Starting new transaction(Receive)..");
        System.out.println("You were paid " + transactionRequest.getAmount() +"!");
        //LOGGER.info("You were paid {}!", transactionRequest.getAmount());
        trustLineBalance.update(transactionRequest.getAmount());

        System.out.println("Trustline balance is: " + trustLineBalance.getAmount()+ "\n");
        //LOGGER.info("Trustline balance is: " + trustLineBalance.getAmount());
    }
}
