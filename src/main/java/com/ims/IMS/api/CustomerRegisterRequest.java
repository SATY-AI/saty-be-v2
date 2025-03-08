package com.ims.IMS.api;

import com.ims.IMS.common.Role;

public record CustomerRegisterRequest(
         String password,
         String email,
         String fullName,
         String phoneNumber,
         Role role
) {}
