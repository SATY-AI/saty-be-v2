package com.ims.IMS.service;

import com.ims.IMS.model.groupuser.Admin;
import com.ims.IMS.model.groupuser.Customer;
import com.ims.IMS.repository.AdminRepository;
import com.ims.IMS.repository.CustomerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;

    public CustomUserDetailsService(CustomerRepository customerRepository, AdminRepository adminRepository) {
        this.customerRepository = customerRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try to find a user in the customer repository
        Customer customerUser = customerRepository.findByEmail(username);
        if (customerUser != null) {
            return customerUser;
        }
        // If not found in the customer repository, try to find in the admin repository
        Admin adminUser = adminRepository.findByEmail(username);
        if (adminUser != null) {
            return adminUser;
        }
        // If not found in both repositories, throw an exception
        throw new UsernameNotFoundException("User not found");
    }
}