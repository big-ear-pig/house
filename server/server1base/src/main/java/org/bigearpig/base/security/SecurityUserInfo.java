package org.bigearpig.base.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bigearpig.common.UserInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@EqualsAndHashCode(callSuper = true)
public class SecurityUserInfo extends UserInfo implements Authentication {

    private static final long serialVersionUID = 570L;

    private String details;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 合并 roleCodeList 和 permissionCodeList，转换为 GrantedAuthority
        List<String> roles = getRoleCodeList() != null ? getRoleCodeList().stream().map(roleCode->{
            return "ROLE_" + roleCode;
        }).collect(Collectors.toList()) : Collections.emptyList();
        List<String> permissions = getPermissionCodeList() != null ? getPermissionCodeList() : Collections.emptyList();
        return Stream.concat(roles.stream(), permissions.stream())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Object getCredentials() {
        return getPassWord();
    }



    @Override
    public Object getDetails() {
        return details;
    }
    @JsonIgnore
    @Override
    public Object getPrincipal() {
        return this;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (!isAuthenticated) {
            throw new IllegalArgumentException("Cannot set authenticated to false as SecurityUserInfo is always authenticated after creation");
        }
    }

    @Override
    public String getName() {
        return getTableId().toString();
    }
}
