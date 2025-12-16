package cn.edu.zju.temp.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class MybatisContext {

    /**
     * 缓存key
     */
    private static final String KEY_CURRENT_TENANT_ID = "KEY_CURRENT_PROVIDER_ID";

    private static final Map<String, Object> M_CONTEXT = new ConcurrentHashMap<>();

    public void setCurrentTenantId(Integer tenantId) {
        M_CONTEXT.put(KEY_CURRENT_TENANT_ID, tenantId.longValue());
    }

    public Long getCurrentTenantId() {
        return (Long) M_CONTEXT.get(KEY_CURRENT_TENANT_ID);
    }

}