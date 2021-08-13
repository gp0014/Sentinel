package com.alibaba.csp.sentinel.dashboard.rule.apollo.repository;


import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.NamespaceReleaseDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenAppNamespaceDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenNamespaceDTO;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



/**
 * @author darren
 */
@Component
public class ApolloRepository {



    @Value("${apollo.openapi.env:dev}")
    private String env;
    @Value("${apollo.openapi.clusterName:default}")
    private String clusterName;
    @Value("${apollo.openapi.auto.join.author:apollo}")
    private  String apolloAuthor;

    private static  final  String AUTO_JOIN_COMMENT="auto-join by sentinel dashboard";

    @Autowired
    private ApolloOpenApiClient apolloOpenApiClient;

    public void publishNamespace(String appId, String nameSpace, NamespaceReleaseDTO releaseDTO) {
        apolloOpenApiClient.publishNamespace(appId, env, clusterName, nameSpace, releaseDTO);
    }

    public void createOrUpdateItem(String appId, String namespaceName, OpenItemDTO itemDTO) {
        apolloOpenApiClient.createOrUpdateItem(appId, env, clusterName, namespaceName, itemDTO);
    }

    public void createAppNamespace(OpenAppNamespaceDTO appNamespaceDTO) {
        apolloOpenApiClient.createAppNamespace(appNamespaceDTO);
    }

    public List<OpenNamespaceDTO> getNamespaces(String appId) {
        return apolloOpenApiClient.getNamespaces(appId, env, clusterName);
    }



    public  void initNameSpaceAndPublish(String appId, String namespace){
        createNameSpace(appId,namespace);
        publishConfig(appId,namespace);

    }
    public void  createNameSpace(String appId,String namespace){
        OpenAppNamespaceDTO appNamespaceDTO=new OpenAppNamespaceDTO();
        appNamespaceDTO.setAppId(appId);
        appNamespaceDTO.setName(namespace);
        appNamespaceDTO.setPublic(false);
        appNamespaceDTO.setComment(AUTO_JOIN_COMMENT);
        appNamespaceDTO.setDataChangeCreatedBy(apolloAuthor);
        appNamespaceDTO.setDataChangeLastModifiedBy(apolloAuthor);
        appNamespaceDTO.setDataChangeLastModifiedTime(new Date());
        appNamespaceDTO.setDataChangeCreatedTime(new Date());
        createAppNamespace(appNamespaceDTO);
    }

    public void saveConfigAndPublish(String appId, String rules,String nameSpace , String ruleKey){
        OpenItemDTO openItemDTO = new OpenItemDTO();
        openItemDTO.setKey(ruleKey);
        openItemDTO.setValue(rules);
        openItemDTO.setComment(AUTO_JOIN_COMMENT);
        openItemDTO.setDataChangeCreatedBy(apolloAuthor);
        createOrUpdateItem(appId, nameSpace, openItemDTO);
        publishConfig(appId,nameSpace);
    }
    private void publishConfig(String appId,String nameSpace){
        NamespaceReleaseDTO namespaceReleaseDTO = new NamespaceReleaseDTO();
        namespaceReleaseDTO.setEmergencyPublish(true);
        namespaceReleaseDTO.setReleaseComment(AUTO_JOIN_COMMENT);
        namespaceReleaseDTO.setReleasedBy(apolloAuthor);
        namespaceReleaseDTO.setReleaseTitle(AUTO_JOIN_COMMENT);
        publishNamespace(appId,  nameSpace, namespaceReleaseDTO);
    }

}
