package com.ims.IMS.model.dto;

import com.ims.IMS.model.groupuser.Customer;
import com.ims.IMS.common.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerInformation {
    private Integer id;
    private String email;
    private String fullname;
    private String phonenumber;
    private Role role;
    private boolean statusactive;

    public static CustomerInformation fromUser(Customer customer) {
        CustomerInformation userDTO = new CustomerInformation();
        userDTO.setId(customer.getId());
        userDTO.setEmail(customer.getEmail());
        userDTO.setFullname(customer.getFullName());
        userDTO.setPhonenumber(customer.getPhoneNumber());
        userDTO.setRole(customer.getRole());
        userDTO.setStatusactive(customer.getStatusActive());
        return userDTO;
    }
}
