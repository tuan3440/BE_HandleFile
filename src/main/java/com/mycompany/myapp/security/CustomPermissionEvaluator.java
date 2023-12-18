package com.mycompany.myapp.security;

import com.mycompany.myapp.utils.DataUtil;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * author: tamdx
 */
@Service
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        if ((auth == null) || (targetDomainObject == null) || !(permission instanceof String)) {
            return false;
        }
        return hasPrivilege(auth, targetDomainObject.toString().toUpperCase(), permission.toString().toUpperCase());
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }
        return hasPrivilege(auth, targetType.toUpperCase(), permission.toString().toUpperCase());
    }

    private boolean hasPrivilege(Authentication auth, String targetType, String permission) {
        for (CustomGrantedAuthority grantedAuth : (Collection<CustomGrantedAuthority>) auth.getAuthorities()) {
            for (String grantedPermission : grantedAuth.getPermissions()) {
                if (grantedPermission.startsWith(targetType + "_") && checkAction(grantedPermission, permission)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkAction(String grantedPermission, String permission) {
        return (
            DataUtil.isNullOrEmpty(permission) ||
            grantedPermission.contains(permission) ||
            grantedPermission.endsWith("_*") ||
            "*".equalsIgnoreCase(permission)
        );
    }
}
