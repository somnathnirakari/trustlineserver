package com.ripple.takehome.trustlineserver.util;

import com.ripple.takehome.trustlineserver.payload.TransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

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
    public boolean doesTrustLineExistBetween(String nodeA, String nodeB) {
        return trustLineMap.getOrDefault(nodeA.toLowerCase(), new HashSet<>()).contains(nodeB.toLowerCase());
    }

    public void route(TransactionRequest transactionRequest) {
        String url = addressResolver.resolve(transactionRequest.getTo());
        if (url == null) {
            throw new RuntimeException("Unable to resolve address for receiver " + transactionRequest.getTo());
        }
        restUtil.postTransaction(url, transactionRequest);
    }
}
