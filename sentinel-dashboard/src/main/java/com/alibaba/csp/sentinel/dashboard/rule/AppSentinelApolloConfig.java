package com.alibaba.csp.sentinel.dashboard.rule;

import com.alibaba.csp.sentinel.dashboard.rule.constant.SentinelApolloConstant;

/**
 * @author darren
 */
public class AppSentinelApolloConfig {
    private String namespace;

    private String flowRuleKey = SentinelApolloConstant.DEFAULT_FLOW_RULE_KEY;

    private String degradeRuleKey =SentinelApolloConstant.DEFAULT_DEGRADE_RULE_KEY;

    private String paramFlowRuleKey = SentinelApolloConstant.DEFAULT_PARAM_FLOW_RULE_KEY;

    private String authorityRuleKey = SentinelApolloConstant.DEFAULT_AUTHORITY_RULE_KEY;

    public AppSentinelApolloConfig(String namespace) {
        super();
        this.namespace=namespace;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getFlowRuleKey() {
        return flowRuleKey;
    }

    public void setFlowRuleKey(String flowRuleKey) {
        this.flowRuleKey = flowRuleKey;
    }

    public String getDegradeRuleKey() {
        return degradeRuleKey;
    }

    public void setDegradeRuleKey(String degradeRuleKey) {
        this.degradeRuleKey = degradeRuleKey;
    }

    public String getParamFlowRuleKey() {
        return paramFlowRuleKey;
    }

    public void setParamFlowRuleKey(String paramFlowRuleKey) {
        this.paramFlowRuleKey = paramFlowRuleKey;
    }

    public String getAuthorityRuleKey() {
        return authorityRuleKey;
    }

    public void setAuthorityRuleKey(String authorityRuleKey) {
        this.authorityRuleKey = authorityRuleKey;
    }



}
