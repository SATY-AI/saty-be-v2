package com.ims.IMS.controller.customer.crud_insect;


import com.ims.IMS.api.InsectListDataResponse;
import com.ims.IMS.exception.InvalidTokenException;
import com.ims.IMS.lib.api.FieldError;
import com.ims.IMS.lib.api.ResponseApi;
import com.ims.IMS.model.groupuser.Customer;
import com.ims.IMS.service.CustomerService;
import com.ims.IMS.service.insect.InsectListDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ims/customer/insect")
public class InsectCustomerCrud {
    @Autowired
    private InsectListDataService insectListDataService;
    @Autowired
    private CustomerService customerService;


    @GetMapping("/get-list-insect")
    public ResponseApi<List<InsectListDataResponse>> getListInsectData(
            @RequestHeader("Authorization") String token
    ) {
        try {
            // Validate the token and get customer data
            Customer customerData = customerService.validateTokenAndGetCustomer(token);
            // Get the list of insects from the service
            List<InsectListDataResponse> insectList = insectListDataService.getAllInsects();
            // Return the successful response
            return ResponseApi.success(insectList);
        } catch (InvalidTokenException e) {
            // Handle token validation exceptions
            List<FieldError> errors = List.of(new FieldError("field1", "must not be empty"));
            return ResponseApi.error("400", "Validation failed", errors);
        } catch (Exception e) {
            // Handle any unexpected exceptions
            List<FieldError> errors = List.of(new FieldError("field1", "must not be empty"));
            return ResponseApi.error("500", "Internal Server Error", errors);
        }
    }

}
