package com.ims.IMS.model.groupuser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String username;
    private String password;

    //Login Email
    private String email;

    //Personal Information
    private String fullname;
    private String phonenumber;


}
