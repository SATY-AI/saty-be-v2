package com.ims.IMS.model.groupuser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerUpdate {
    private String fullName;
    private String phoneNumber;
    private String email;
    private String password;
    private Boolean statusactive;
}
