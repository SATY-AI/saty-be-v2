package com.ims.IMS.service.customerRedeemService;


import com.ims.IMS.api.customerRedeem.CustomerRedeemRequest;
import com.ims.IMS.api.customerRedeem.CustomerRedeemResponse;
import com.ims.IMS.mapper.SatyCustomerRedeemMapping;
import com.ims.IMS.model.CustomerRedeem.CustomerRedeem;
import com.ims.IMS.repository.CustomerRedeem.CustomerRedeemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerRedeemService {
    private final CustomerRedeemRepository customerRedeemRepository;
    private final SatyCustomerRedeemMapping customerRedeemMapping;

    // Retrieve all CustomerRedeems
    public List<CustomerRedeemResponse> getAllCustomerRedeems() {
        return customerRedeemRepository.findAll().stream()
                .map(customerRedeemMapping::toResponse)
                .collect(Collectors.toList());
    }

    // Retrieve a single CustomerRedeem by ID
    public Optional<CustomerRedeemResponse> getCustomerRedeemById(Integer id) {
        return customerRedeemRepository.findById(id).map(customerRedeemMapping::toResponse);
    }

    // Create a new CustomerRedeem
    public CustomerRedeemResponse createCustomerRedeem(CustomerRedeemRequest request) {
        CustomerRedeem customerRedeem = customerRedeemMapping.toEntity(request);
        CustomerRedeem saved = customerRedeemRepository.save(customerRedeem);
        return customerRedeemMapping.toResponse(saved);
    }

    // Update an existing CustomerRedeem
    public Optional<CustomerRedeemResponse> updateCustomerRedeem(Integer id, CustomerRedeemRequest request) {
        return customerRedeemRepository.findById(id).map(existing -> {
            CustomerRedeem updated = customerRedeemMapping.updateEntity(existing, request);
            CustomerRedeem saved = customerRedeemRepository.save(updated);
            return customerRedeemMapping.toResponse(saved);
        });
    }

    // Delete a CustomerRedeem
    public boolean deleteCustomerRedeem(Integer id) {
        if (customerRedeemRepository.existsById(id)) {
            customerRedeemRepository.deleteById(id);
            return true;
        }
        return false;
    }
}