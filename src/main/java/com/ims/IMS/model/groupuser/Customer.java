package com.ims.IMS.model.groupuser;


import com.ims.IMS.api.CustomerRegisterRequest;
import com.ims.IMS.common.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Table(name = "customers")
public class Customer extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String password;
    private String email;
    private String fullName;
    private String phoneNumber;
    private Boolean statusActive;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String secret_key;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static Customer fromAllRegister(AllRegister allRegister) {
        return Customer.builder()
                .password(allRegister.getPassword())
                .email(allRegister.getEmail())
                .fullName(allRegister.getFullname())
                .phoneNumber(allRegister.getPhonenumber())
                .role(allRegister.getRole())
                .statusActive(true) // You may set a default value or handle it differently
                .build();
    }

    public static Customer fromAdminCreate(CustomerRegisterRequest customerRegisterRequest) {
        return Customer.builder()
                .password(customerRegisterRequest.password())
                .email(customerRegisterRequest.email())
                .fullName(customerRegisterRequest.fullName())
                .phoneNumber(customerRegisterRequest.phoneNumber())
                .role(customerRegisterRequest.role())
                .statusActive(true) // You may set a default value or handle it differently
                .build();
    }
}
