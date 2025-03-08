package com.ims.IMS.controller.admin.crud_image;


import com.ims.IMS.api.image.DeleteImageResponse;
import com.ims.IMS.api.image.ImageRegisterRequest;
import com.ims.IMS.api.image.ImageRegisterResponse;
import com.ims.IMS.api.image.ListImageResponse;
import com.ims.IMS.api.image.UpdateImageRequest;
import com.ims.IMS.api.image.UpdateImageResponse;
import com.ims.IMS.auth.all.AuthenticationAllService;
import com.ims.IMS.auth.user.AuthenticationCustomerService;
import com.ims.IMS.config.JwtService;
import com.ims.IMS.exception.LoginWrongRole;
import com.ims.IMS.exception.UsernameAlreadyExistsException;
import com.ims.IMS.lib.api.ResponseApi;
import com.ims.IMS.lib.api.ResponseStatus;
import com.ims.IMS.mapper.ImsMapping;
import com.ims.IMS.model.groupuser.Admin;
import com.ims.IMS.model.imsprocessing.Image;
import com.ims.IMS.repository.AdminRepository;
import com.ims.IMS.repository.DeviceRepository;
import com.ims.IMS.repository.Image.ImageRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ims/admin")
public class ImageCrud {
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
    private ImageRepository imageRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DeviceService deviceService;
    private final ImsMapping imsMapping;
    @Autowired
    public ImageCrud(ImsMapping imsMapping) {
        this.imsMapping = imsMapping;
    }

    @GetMapping("/list-image")
    public ResponseApi<List<ListImageResponse>> getListImage(@RequestHeader("Authorization") String token) {
        // Validate the token and get admin data
        Admin adminData = adminService.validateTokenAndGetAdmin(token);
        // Fetch all images from the repository
        List<Image> images = imageRepository.findAll();
        List<ListImageResponse> listImages = images.stream()
                .map(imsMapping::mapFromImage)
                .collect(Collectors.toList());
        // Return the mapped list as a successful response
        return ResponseApi.success(listImages);
    }

    @PostMapping("/create-image-bucket")
    public ResponseApi<ImageRegisterResponse> createDevice(
            @RequestHeader("Authorization") String token,
            @RequestBody ImageRegisterRequest imageRegisterRequest) {
        try {
            Admin adminData = adminService.validateTokenAndGetAdmin(token); // Ensure the admin has valid permissions
            // Check if a device with the same deviceID already exists
            if (imageRepository.existsByImageBucket(imageRegisterRequest.imageBucket())) {
                ResponseStatus status = new ResponseStatus("400", "ImageBucket already exists");
                return ResponseApi.success(status, null, null);
            }
            Image imageRegister = Image.fromAdminCreate(imageRegisterRequest);
            imageRepository.save(imageRegister);
            ImageRegisterResponse imageRegisterResponse = transformToImageRegisterResponse(imageRegister);
            return ResponseApi.success(imageRegisterResponse);
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

    @DeleteMapping("/delete-image-bucket/{imageBucket}")
    public ResponseApi<DeleteImageResponse> deleteLocation(
            @RequestHeader("Authorization") String token,
            @PathVariable String imageBucket) {
        try {
            // Validate the token and get admin data
            Admin adminData = adminService.validateTokenAndGetAdmin(token);
            // Fetch the device by its ID
            Optional<Image> imageOptional = imageRepository.findByImageBucket(imageBucket);
            if (imageOptional.isEmpty()) {
                // Device with the specified ID does not exist
                ResponseStatus status = new ResponseStatus("404", "Image Not Found");
                return ResponseApi.success(status, null, null);
            }
            // Image exists, delete it
            Image image = imageOptional.get();
            imageRepository.delete(image);
            // Prepare the response
            DeleteImageResponse deleteImageResponse = new DeleteImageResponse("Image Deleted Successfully");
            return ResponseApi.success(deleteImageResponse);
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

    @PutMapping("/update-image-bucket")
    public ResponseApi<UpdateImageResponse> updateImage(
            @RequestHeader("Authorization") String token,
            @RequestBody UpdateImageRequest updateImageRequest) {
        try {
            // Validate the token and get admin data
            Admin adminData = adminService.validateTokenAndGetAdmin(token);

            // Fetch the image by its bucket name
            Optional<Image> imageOptional = imageRepository.findByImageBucket(updateImageRequest.imageBucket());
            if (imageOptional.isEmpty()) {
                // Image with the specified bucket does not exist
                ResponseStatus status = new ResponseStatus("404", "Image not found");
                return ResponseApi.success(status, null, null);
            }
            // Image exists, update its details
            Image image = imageOptional.get();
            // Fetch the existing image list
            List<String> existingImageList = image.getImageList();
            if (existingImageList == null) {
                existingImageList = new ArrayList<>();
            }
            // Add new elements to the existing list
            existingImageList.addAll(updateImageRequest.imageList());
            // Update the image entity with the new details
            image.setDescription(updateImageRequest.description());
            image.setTopic(updateImageRequest.topic());
            image.setDeviceID(updateImageRequest.deviceID());
            image.setUpdated_at(updateImageRequest.updated_at());
            image.setImageList(existingImageList); // Set the updated list back to the entity
            // Save the updated image
            Image updatedImage = imageRepository.save(image);
            // Prepare the response
            UpdateImageResponse updateImageResponse = new UpdateImageResponse("Image Updated Successfully", updatedImage.getImageList());
            return ResponseApi.success(updateImageResponse);
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


    private ImageRegisterResponse transformToImageRegisterResponse(Image image) {
        return new ImageRegisterResponse(
                image.getImageBucket(),
                image.getDescription(),
                image.getTopic(),
                image.getDeviceID(),
                image.getCreated_at(),
                image.getUpdated_at());
    }

}
