package com.alibaba.csp.sentinel.dashboard.rule.constant;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author darren
 */
public class SentinelApolloConstant {
    public static final Map<String, String> DEFAULT_SENTINEL_APOLLO_CONFIG = initMap();

    private static Map<String, String> initMap() {
        Map<String, String> map = new HashMap<>(4);
        map.put(APOLLO_FLOW_RULE_KEY, DEFAULT_FLOW_RULE_KEY);
        map.put(APOLLO_NAMESPACE_KEY, DEFAULT_NAMESPACE);
        map.put(APOLLO_DEGRADE_RULE_KEY, DEFAULT_DEGRADE_RULE_KEY);
        map.put(APOLLO_PARAM_FLOW_RULE_KEY, DEFAULT_PARAM_FLOW_RULE_KEY);
        map.put(APOLLO_AUTHORITY_RULE_KEY, DEFAULT_AUTHORITY_RULE_KEY);
        return Collections.unmodifiableMap(map);
    }


    /**
     * 流控规则apollo key
     */
    public static final String APOLLO_FLOW_RULE_KEY = "kucoin.sentinel.apollo.flowRules";
    public static final String APOLLO_NAMESPACE_KEY = "kucoin.sentinel.apollo.namespace";
    /**
     * 降级规则apollo key
     */
    public static final String APOLLO_DEGRADE_RULE_KEY = "kucoin.sentinel.apollo.degradeRules";
    /**
     * 热点规则apollo key
     */
    public static final String APOLLO_PARAM_FLOW_RULE_KEY = "kucoin.sentinel.apollo.paramFlowRules";
    /**
     * 授权规则apollo key
     */
    public static final String APOLLO_AUTHORITY_RULE_KEY = "kucoin.sentinel.apollo.authorityRules";

    public static final String APOLLO_GATEWAY_FLOW_RULE_KEY = "kucoin.sentinel.apollo.gatewayFlowRules";

    public static final String APOLLO_API_DEFINITION_RULE_KEY = "kucoin.sentinel.apollo.apiDefinitionRules";


    /**
     * 默认的namespace
     */
    public static final String DEFAULT_NAMESPACE = "sentinel.rules";
    /**
     * 默认的流控规则key
     */
    public static final String DEFAULT_FLOW_RULE_KEY = "flowRules";
    /**
     * 默认的降级规则key
     */
    public static final String DEFAULT_DEGRADE_RULE_KEY = "degradeRules";
    /**
     * 默认的热点规则key
     */
    public static final String DEFAULT_PARAM_FLOW_RULE_KEY = "paramFlowRules";
    /**
     * 默认的授权规则key
     */
    public static final String DEFAULT_AUTHORITY_RULE_KEY = "authorityRules";

    /**
     * 默认的网关流控规则key
     */
    public static final String DEFAULT_GATEWAY_FLOW_RULE_KEY = "gatewayFlowRules";

    /**
     * 默认的api分组规则key
     */
    public static final String DEFAULT_API_DEFINITION_RULE_KEY = "apiDefinitionRules";
}

