package com.ims.IMS.model.groupuser;

import com.ims.IMS.common.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllRegister {
    private String password;
    private String email;
    private String fullname;
    private String phonenumber;
    @Enumerated(EnumType.STRING)
    private Role role;
}
