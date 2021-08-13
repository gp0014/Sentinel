package com.alibaba.csp.sentinel.dashboard.rule.apollo.convert;

import com.alibaba.csp.sentinel.dashboard.rule.AppSentinelApolloConfig;
import com.ctrip.framework.apollo.openapi.dto.OpenNamespaceDTO;
import java.util.Optional;

import static com.alibaba.csp.sentinel.dashboard.rule.constant.SentinelApolloConstant.APOLLO_AUTHORITY_RULE_KEY;
import static com.alibaba.csp.sentinel.dashboard.rule.constant.SentinelApolloConstant.APOLLO_DEGRADE_RULE_KEY;
import static com.alibaba.csp.sentinel.dashboard.rule.constant.SentinelApolloConstant.APOLLO_FLOW_RULE_KEY;
import static com.alibaba.csp.sentinel.dashboard.rule.constant.SentinelApolloConstant.APOLLO_PARAM_FLOW_RULE_KEY;
import static com.alibaba.csp.sentinel.dashboard.rule.constant.SentinelApolloConstant.DEFAULT_AUTHORITY_RULE_KEY;
import static com.alibaba.csp.sentinel.dashboard.rule.constant.SentinelApolloConstant.DEFAULT_DEGRADE_RULE_KEY;
import static com.alibaba.csp.sentinel.dashboard.rule.constant.SentinelApolloConstant.DEFAULT_FLOW_RULE_KEY;
import static com.alibaba.csp.sentinel.dashboard.rule.constant.SentinelApolloConstant.DEFAULT_PARAM_FLOW_RULE_KEY;

/**
 * @author darren
 */
public  class AppSentinelApolloConfigConvertor {
    public static AppSentinelApolloConfig convertOpenNamespace2AppSentinelApolloConfig(OpenNamespaceDTO openNamespaceDTO) {
        AppSentinelApolloConfig appSentinelApolloConfig=new AppSentinelApolloConfig(openNamespaceDTO.getNamespaceName());
        appSentinelApolloConfig.setFlowRuleKey(Optional.ofNullable(OpenNamespaceDTOConvertor.getByKey(openNamespaceDTO,APOLLO_FLOW_RULE_KEY)).orElse(DEFAULT_FLOW_RULE_KEY));
        appSentinelApolloConfig.setDegradeRuleKey(Optional.ofNullable(OpenNamespaceDTOConvertor.getByKey(openNamespaceDTO,APOLLO_DEGRADE_RULE_KEY)).orElse(DEFAULT_DEGRADE_RULE_KEY));
        appSentinelApolloConfig.setParamFlowRuleKey(Optional.ofNullable(OpenNamespaceDTOConvertor.getByKey(openNamespaceDTO,APOLLO_PARAM_FLOW_RULE_KEY)).orElse(DEFAULT_PARAM_FLOW_RULE_KEY));
        appSentinelApolloConfig.setAuthorityRuleKey(Optional.ofNullable(OpenNamespaceDTOConvertor.getByKey(openNamespaceDTO,APOLLO_AUTHORITY_RULE_KEY)).orElse(DEFAULT_AUTHORITY_RULE_KEY));
        return appSentinelApolloConfig;
    }


}
