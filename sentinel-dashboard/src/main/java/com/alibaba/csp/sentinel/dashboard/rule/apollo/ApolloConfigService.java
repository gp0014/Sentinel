package com.alibaba.csp.sentinel.dashboard.rule.apollo;

import com.alibaba.csp.sentinel.dashboard.rule.AppSentinelApolloConfig;
import com.alibaba.csp.sentinel.dashboard.rule.apollo.cache.ApolloConfigLocalCache;
import com.alibaba.csp.sentinel.dashboard.rule.apollo.convert.AppSentinelApolloConfigConvertor;
import com.alibaba.csp.sentinel.dashboard.rule.apollo.convert.OpenNamespaceDTOConvertor;
import com.alibaba.csp.sentinel.dashboard.rule.apollo.repository.ApolloRepository;
import com.ctrip.framework.apollo.openapi.dto.OpenNamespaceDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import static com.alibaba.csp.sentinel.dashboard.rule.constant.SentinelApolloConstant.APOLLO_NAMESPACE_KEY;
import static com.alibaba.csp.sentinel.dashboard.rule.constant.SentinelApolloConstant.DEFAULT_NAMESPACE;

/**
 * @author darren
 */
@Service
public class ApolloConfigService{

    @Autowired
    private ApolloRepository apolloRepository;

    @Autowired
    private ApolloConfigLocalCache apolloConfigLocalCache;

    /**
     * 根据appId获取app的SentinelApolloConfig
     * 应用配了就直接取，没配就创建默认配置namespace
     * @param appId appId
     * @return 应用的SentinelApolloConfig
     */
    public AppSentinelApolloConfig getOrInitAppSentinelConfig(String appId){
        List<OpenNamespaceDTO> candidateNamespaces = apolloConfigLocalCache.getCacheValue(appId);
        //无appId的情况异常会直接吐到前端弹窗 无需处理
        if(CollectionUtils.isEmpty(candidateNamespaces)){
            //app无任何namespace的情况
            apolloRepository.initNameSpaceAndPublish(appId,DEFAULT_NAMESPACE);
            //更新缓存 此处不用保证apollo和本地缓存强一致
            apolloConfigLocalCache.refreshCacheValue(appId);
            return new AppSentinelApolloConfig(DEFAULT_NAMESPACE);
        }
        //从namespace里面获取配置
        return chooseConfig(appId,candidateNamespaces);
    }

    public OpenNamespaceDTO getNameSpaceByAppIdAndSentinelApolloConfig(String appId,AppSentinelApolloConfig appSentinelConfig){
        return filterNamespace(apolloConfigLocalCache.getCacheValue(appId), appSentinelConfig.getNamespace());
    }
    private OpenNamespaceDTO filterNamespace( List<OpenNamespaceDTO> namespaces, String namespaceName) {
        if (CollectionUtils.isEmpty(namespaces)) {
            return null;
        }
        return namespaces.stream().filter(e -> e.getNamespaceName().equals(namespaceName)).findFirst().orElse(null);
    }
    private String getConfigNamespaceNameOrDefault(List<OpenNamespaceDTO> candidateNamespaces) {
        for (OpenNamespaceDTO candidateNamespace : candidateNamespaces) {
            if(OpenNamespaceDTOConvertor.hasKey(candidateNamespace,DEFAULT_NAMESPACE)){
                return OpenNamespaceDTOConvertor.convertApolloConfig2Map(candidateNamespace.getItems()).get(APOLLO_NAMESPACE_KEY).getValue() ;
            }
        }
        return DEFAULT_NAMESPACE;
    }

    private AppSentinelApolloConfig chooseConfig(String appId, List<OpenNamespaceDTO> candidateNamespaces) {
        final String configNamespace = getConfigNamespaceNameOrDefault(candidateNamespaces);
        if (candidateNamespaces.stream().noneMatch(e -> e.getNamespaceName().equals(configNamespace))) {
            //未配置namespace的时候 需要去初始化namespace 其他的参数当然也没配 返回带namespace的默认配置
            apolloRepository.initNameSpaceAndPublish(appId, configNamespace);
            //更新缓存 此处不用保证apollo和本地缓存强一致
            apolloConfigLocalCache.refreshCacheValue(appId);
            return new AppSentinelApolloConfig(configNamespace);
        } else {
            OpenNamespaceDTO openNamespaceDTO =filterNamespace(candidateNamespaces,configNamespace);
            assert openNamespaceDTO != null;
            return AppSentinelApolloConfigConvertor.convertOpenNamespace2AppSentinelApolloConfig(openNamespaceDTO);
        }
    }

    public void saveConfigAndPublish(String appId, String rules,AppSentinelApolloConfig appSentinelConfig , String ruleKey){
       apolloRepository.saveConfigAndPublish(appId,rules,appSentinelConfig.getNamespace(),ruleKey);
        //更新缓存 此处不用保证apollo和本地缓存强一致
       apolloConfigLocalCache.refreshCacheValue(appId);
    }


}
