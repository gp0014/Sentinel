/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.dashboard.rule.apollo.rules.flow;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.AppSentinelApolloConfig;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.dashboard.rule.apollo.ApolloConfigService;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenNamespaceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author darren
 */
@Component("flowRuleApolloProvider")
public class FlowRuleApolloProvider implements DynamicRuleProvider<List<FlowRuleEntity>> {

    @Autowired
    private ApolloConfigService apolloConfigService;

    @Override
    public List<FlowRuleEntity> getRules(String appId) throws Exception {
        //动态从apollo拉取应用的Sentinel配置信息
        AppSentinelApolloConfig appSentinelConfig = apolloConfigService.getOrInitAppSentinelConfig(appId);
        //动态拉取namespace
        OpenNamespaceDTO openNamespaceDTO = apolloConfigService.getNameSpaceByAppIdAndSentinelApolloConfig(appId, appSentinelConfig);
        String rules = openNamespaceDTO
                .getItems()
                .stream()
                .filter(p -> p.getKey().equals(appSentinelConfig.getFlowRuleKey()))
                .map(OpenItemDTO::getValue)
                .findFirst()
                .orElse("");
        if (StringUtil.isEmpty(rules)) {
            return new ArrayList<>();
        }
        final List<FlowRule> flowRules = JSON.parseArray(rules, FlowRule.class);
        return flowRules.stream()
                .map(rule -> FlowRuleEntity.fromFlowRule(appId, null, null, rule))
                .collect(Collectors.toList());
    }
}
