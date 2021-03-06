package com.ripple.takehome.trustlineserver.util;

import com.ripple.takehome.trustlineserver.exception.NodeAddressResolutionFailureException;
import com.ripple.takehome.trustlineserver.exception.TrustLineTransactionFailureException;
import com.ripple.takehome.trustlineserver.payload.TransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Routes the transactions between trustline nodes
 * @author Somnath Nirakari
 */
@Component
public class TransactionRouter {

    private RestUtil restUtil;
    private AddressResolver addressResolver;
    private Environment env;
    private Map<String, Set<String>> trustLineMap = new HashMap<>();

    @Value("${trustline.nodes}")
    String nodes;

    @Autowired
    public TransactionRouter(RestUtil restUtil, AddressResolver addressResolver, Environment env) {
        this.restUtil = restUtil;
        this.addressResolver = addressResolver;
        this.env = env;
    }

    /**
     * This method preprocesses each node's trustline connections to figure out each node's implicit trustlines
     * so that future calls to {link doesTrustLineExistBetween} becomes constant time.
     *
     * This is basically a BFS on nodes trustline connections.
     */
    @PostConstruct
    void init() {
        String[] nodesArr = nodes.split(",");
        Queue<String> nodesQ = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        for(String node: nodesArr) {
            trustLineMap.put(node, new HashSet<>());
            Set<String> trustLineSet = trustLineMap.get(node);
            nodesQ.offer(node);
            visited.add(node);
            while(!nodesQ.isEmpty()) {
                node = nodesQ.poll();
                nodes = env.getProperty("trustline.node." + node.toLowerCase() + ".trusts");
                nodesArr = nodes.split(",");
                for (String childNode: nodesArr){
                    if (visited.contains(childNode)) continue;
                    visited.add(childNode);
                    trustLineSet.add(childNode);
                    nodesQ.offer(childNode);
                }
            }
            visited = new HashSet<>();
        }
    }

    /**
     * @param nodeA
     * @param nodeB
     * @return {@code true} if there exists a trustline between nodeA and nodeB else {@code false}
     */
    public boolean doesTrustLineExistBetween(String nodeA, String nodeB) {
        return trustLineMap.getOrDefault(nodeA.toLowerCase(), new HashSet<>()).contains(nodeB.toLowerCase());
    }

    /**
     * routes the request between two trustline nodes
     * @param transactionRequest
     */
    public void route(TransactionRequest transactionRequest) {
        String url = addressResolver.resolve(transactionRequest.getTo());
        if (url == null) {
            throw new NodeAddressResolutionFailureException("Unable to resolve address for receiver " + transactionRequest.getTo());
        }
        try {
            restUtil.postTransaction(url, transactionRequest);
        } catch(Exception ex) {
            throw new TrustLineTransactionFailureException("Failed to process transaction at node " +
                    transactionRequest.getTo(), ex);
        }
    }
}
