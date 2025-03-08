package com.ims.IMS.model.dto;


import com.ims.IMS.model.groupuser.Admin;
import com.ims.IMS.common.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminInformation {
    private Integer id;
    private String email;
    private String fullname;
    private String phonenumber;
    private Role role;
    private boolean statusactive;

    public static AdminInformation fromAdmin(Admin admin) {
        AdminInformation adminDTO = new AdminInformation();
        adminDTO.setId(admin.getId());
        adminDTO.setEmail(admin.getEmail());
        adminDTO.setFullname(admin.getFullName());
        adminDTO.setPhonenumber(admin.getPhoneNumber());
        adminDTO.setRole(admin.getRole());
        adminDTO.setStatusactive(admin.getStatusActive());
        return adminDTO;
    }
}
