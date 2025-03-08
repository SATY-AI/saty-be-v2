package com.ims.IMS.model.dto;

import com.ims.IMS.model.groupuser.User;
import com.ims.IMS.common.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllInformation {
    private Integer id;
    private String email;
    private String fullName;
    private String phoneNumber;
    private Role role;
    private boolean statusActive;

    public static AllInformation fromUser(User user) {
        AllInformation allInformation = new AllInformation();
        allInformation.setId(user.getId());
        allInformation.setEmail(user.getEmail());
        allInformation.setFullName(user.getFullName());
        allInformation.setPhoneNumber(user.getPhoneNumber());
        allInformation.setRole(user.getRole());
        allInformation.setStatusActive(user.getStatusActive());
        return allInformation;
    }
}

