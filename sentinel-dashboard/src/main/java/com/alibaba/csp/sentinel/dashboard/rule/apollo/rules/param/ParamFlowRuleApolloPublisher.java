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
package com.alibaba.csp.sentinel.dashboard.rule.apollo.rules.param;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.AbstractRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.AppSentinelApolloConfig;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.dashboard.rule.apollo.ApolloConfigService;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author darren
 */
@Component("paramFlowRuleApolloPublisher")
public class ParamFlowRuleApolloPublisher implements DynamicRulePublisher<List<ParamFlowRuleEntity>> {

    @Autowired
    private ApolloConfigService apolloConfigService;

    @Override
    public void publish(String app, List<ParamFlowRuleEntity> ruleEntities) throws Exception {
        AssertUtil.notEmpty(app, "app name cannot be empty");
        if (ruleEntities == null) {
            return;
        }
        AppSentinelApolloConfig appSentinelConfig = apolloConfigService.getOrInitAppSentinelConfig(app);
        final List<ParamFlowRule> rules = ruleEntities.stream().map(ParamFlowRuleEntity::toRule).collect(Collectors.toList());
        apolloConfigService.saveConfigAndPublish(app, JSON.toJSONString(rules, SerializerFeature.WriteClassName), appSentinelConfig, appSentinelConfig.getParamFlowRuleKey());
    }
}