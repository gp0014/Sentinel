package com.alibaba.csp.sentinel.dashboard.rule.apollo.convert;

import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenNamespaceDTO;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;

/**
 * @author darren
 */
public class OpenNamespaceDTOConvertor {
    public static boolean hasKey(OpenNamespaceDTO openNamespaceDTO, String key){
        return openNamespaceDTO.getItems().stream().anyMatch(e-> StringUtils.equals(e.getKey(),key));
    }
    public static String getByKey(OpenNamespaceDTO openNamespaceDTO,String key){
        OpenItemDTO openItemDTO = convertApolloConfig2Map(openNamespaceDTO.getItems()).get(key);
        return openItemDTO==null?null:openItemDTO.getValue();
    }
    public static Map<String, OpenItemDTO> convertApolloConfig2Map(List<OpenItemDTO> configs){
        return configs.stream().filter(e -> !StringUtils.isEmpty(e.getKey()))
                .collect(Collectors.toMap(OpenItemDTO::getKey, Function.identity()));
    }
}
