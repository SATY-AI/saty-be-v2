package com.ims.IMS.controller.admin.crud_location;


import com.ims.IMS.api.location.DeleteLocationResponse;
import com.ims.IMS.api.location.LocationIDResponse;
import com.ims.IMS.api.location.LocationRegisterRequest;
import com.ims.IMS.api.location.LocationRegisterResponse;
import com.ims.IMS.api.location.UpdateLocationRequest;
import com.ims.IMS.api.location.UpdateLocationResponse;
import com.ims.IMS.auth.all.AuthenticationAllService;
import com.ims.IMS.auth.user.AuthenticationCustomerService;
import com.ims.IMS.config.JwtService;
import com.ims.IMS.exception.LoginWrongRole;
import com.ims.IMS.exception.UsernameAlreadyExistsException;
import com.ims.IMS.lib.api.ResponseApi;
import com.ims.IMS.lib.api.ResponseStatus;
import com.ims.IMS.mapper.ImsMapping;
import com.ims.IMS.model.groupuser.Admin;
import com.ims.IMS.model.imsprocessing.Location;
import com.ims.IMS.repository.AdminRepository;
import com.ims.IMS.repository.CustomerRepository;
import com.ims.IMS.repository.DeviceRepository;
import com.ims.IMS.repository.LocationRepository;
import com.ims.IMS.service.AdminService;
import com.ims.IMS.service.CustomerService;
import com.ims.IMS.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ims/admin")
public class LocationCrud {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationAllService authenticationAllService;
    @Autowired
    private AuthenticationCustomerService authenticationCustomerService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DeviceService deviceService;
    private final ImsMapping imsMapping;
    @Autowired
    public LocationCrud(ImsMapping imsMapping) {
        this.imsMapping = imsMapping;
    }
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/location-id")
    public ResponseApi<List<LocationIDResponse>> getLocationID(@RequestHeader("Authorization") String token) {
        // Validate the token and get admin data
        Admin adminData = adminService.validateTokenAndGetAdmin(token);
        // Fetch all locations from the repository
        List<Location> locations = locationRepository.findAll();
        List<LocationIDResponse> listLocationsID = locations.stream()
                .map(imsMapping::mapToLocationID)
                .collect(Collectors.toList());
        return ResponseApi.success(listLocationsID);
    }

    @PostMapping("/create-location")
    public ResponseApi<LocationRegisterResponse> createLocation(
            @RequestHeader("Authorization") String token,
            @RequestBody LocationRegisterRequest locationRegisterRequest) {
        try {
            Admin adminData = adminService.validateTokenAndGetAdmin(token); // Ensure the admin has valid permissions
            // Check if a device with the same deviceID already exists
            if (locationRepository.existsByLocationID(locationRegisterRequest.locationID())) {
                ResponseStatus status = new ResponseStatus("400", "Location ID already exists");
                return ResponseApi.success(status, null, null);
            }
            // Create and save the new device
            Location locationRegister = Location.fromAdminCreate(locationRegisterRequest);
            locationRepository.save(locationRegister);
            LocationRegisterResponse locationRegisterResponse = transformToLocationRegisterResponse(locationRegister);
            return ResponseApi.success(locationRegisterResponse);
        } catch (UsernameAlreadyExistsException e) {
            ResponseStatus status = new ResponseStatus("400", "Email and Phone Number already exists");
            return ResponseApi.success(status, null, null);
        } catch (LoginWrongRole e) {
            ResponseStatus status = new ResponseStatus("403", "Login with wrong role");
            return ResponseApi.success(status, null, null);
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            ResponseStatus status = new ResponseStatus("500", "Internal server error");
            return ResponseApi.success(status, null, null);
        }
    }

    @DeleteMapping("/delete-location/{locationID}")
    public ResponseApi<DeleteLocationResponse> deleteLocation(
            @RequestHeader("Authorization") String token,
            @PathVariable String locationID) {
        try {
            // Validate the token and get admin data
            Admin adminData = adminService.validateTokenAndGetAdmin(token);
            // Fetch the device by its ID
            Optional<Location> locationOptional = locationRepository.findByLocationID(locationID);
            if (locationOptional.isEmpty()) {
                // Device with the specified ID does not exist
                ResponseStatus status = new ResponseStatus("404", "Device not found");
                return ResponseApi.success(status, null, null);
            }
            // Device exists, delete it
            Location location = locationOptional.get();
            locationRepository.delete(location);
            // Prepare the response
            DeleteLocationResponse deleteLocationResponse = new DeleteLocationResponse("Location Deleted Successfully");
            return ResponseApi.success(deleteLocationResponse);
        } catch (UsernameAlreadyExistsException e) {
            ResponseStatus status = new ResponseStatus("400", "Email and Phone Number already exists");
            return ResponseApi.success(status, null, null);
        } catch (LoginWrongRole e) {
            ResponseStatus status = new ResponseStatus("403", "Login with wrong role");
            return ResponseApi.success(status, null, null);
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            ResponseStatus status = new ResponseStatus("500", "Internal server error");
            return ResponseApi.success(status, null, null);
        }
    }

    @PutMapping("/update-location")
    public ResponseApi<UpdateLocationResponse> updateLocation(
            @RequestHeader("Authorization") String token,
            @RequestBody UpdateLocationRequest updateLocationRequest) {
        try {
            // Validate the token and get admin data
            Admin adminData = adminService.validateTokenAndGetAdmin(token);
            // Fetch the location by its ID
            Optional<Location> locationOptional = locationRepository.findByLocationID(updateLocationRequest.locationID());
            if (locationOptional.isEmpty()) {
                // Location with the specified ID does not exist
                ResponseStatus status = new ResponseStatus("404", "Device not found");
                return ResponseApi.success(status, null, null);
            }
            // Location exists, update its details
            Location location = locationOptional.get();
            location.setDescription(updateLocationRequest.description());
            location.setLocationProvince(updateLocationRequest.locationProvince());
            location.setUrlLocationImage(updateLocationRequest.urlLocationImage());
            // Save the updated location
            Location updatedLocation = locationRepository.save(location);
            // Prepare the response
            UpdateLocationResponse updateLocationResponse = new UpdateLocationResponse("Location Updated Successfully", updatedLocation);
            return ResponseApi.success(updateLocationResponse);
        } catch (UsernameAlreadyExistsException e) {
            ResponseStatus status = new ResponseStatus("400", "Location ID already exists");
            return ResponseApi.success(status, null, null);
        } catch (LoginWrongRole e) {
            ResponseStatus status = new ResponseStatus("403", "Login with wrong role");
            return ResponseApi.success(status, null, null);
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            ResponseStatus status = new ResponseStatus("500", "Internal server error");
            return ResponseApi.success(status, null, null);
        }
    }

    private LocationRegisterResponse transformToLocationRegisterResponse(Location location) {
        return new LocationRegisterResponse(location.getLocationProvince(), location.getLocationID(), location.getDescription(),
                location.getUrlLocationImage(), location.getCreatedAt(), location.getUpdatedAt(), "Add Location Successfully");
    }

}
