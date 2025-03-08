package com.ims.IMS.controller.customer.crud_image;

import com.ims.IMS.api.CustomerImageDetailRequest;
import com.ims.IMS.api.CustomerImageDetailResponse;
import com.ims.IMS.api.image.FileUploadResponse;
import com.ims.IMS.api.image.ImageDetailRequestDTO;
import com.ims.IMS.api.image.ImageListDeviceResponse;
import com.ims.IMS.api.image.ImageResponse;
import com.ims.IMS.auth.all.AuthenticationAllService;
import com.ims.IMS.auth.user.AuthenticationCustomerService;
import com.ims.IMS.config.JwtService;
import com.ims.IMS.exception.CustomerDeviceNotFound;
import com.ims.IMS.exception.DatabaseException;
import com.ims.IMS.exception.InvalidTokenException;
import com.ims.IMS.lib.api.ResponseApi;
import com.ims.IMS.lib.api.ResponseStatus;
import com.ims.IMS.mapper.ImsMappingImpl;
import com.ims.IMS.model.CusDev.CustomerDevice;
import com.ims.IMS.model.groupuser.Customer;
import com.ims.IMS.model.imsprocessing.Image;
import com.ims.IMS.model.imsprocessing.ImageDetail;
import com.ims.IMS.repository.CustomerDeviceRepository;
import com.ims.IMS.repository.DeviceRepository;
import com.ims.IMS.repository.Image.ImageDetailRepository;
import com.ims.IMS.repository.Image.ImageRepository;
import com.ims.IMS.service.CustomerService;
import com.ims.IMS.service.storage.StorageService;
import com.ims.IMS.service.storage.StorageServiceWasabi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ims/customer")
public class ImageCustomerCrud {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationAllService authenticationAllService;
    @Autowired
    private AuthenticationCustomerService authenticationCustomerService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private CustomerDeviceRepository customerDeviceRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ImageDetailRepository imageDetailRepository;
    @Autowired
    private ImsMappingImpl imsMapping;
    @Autowired
    private StorageService storageService;

    private final StorageServiceWasabi storageServiceWasabi;

    public ImageCustomerCrud(StorageServiceWasabi storageServiceWasabi) {
        this.storageServiceWasabi = storageServiceWasabi;
    }

    @PostMapping("/get-detail-image")
    public ResponseApi<List<CustomerImageDetailResponse>> getImageDevice(
            @RequestHeader("Authorization") String token,
            @RequestBody CustomerImageDetailRequest customerImageDetailRequest
    ) {
        try {
            String urlBase = "https://saty-dataset.s3.ap-southeast-1.amazonaws.com/";

            // Validate the token and get customer data
            Customer customerData = customerService.validateTokenAndGetCustomer(token);

            // Handle invalid or expired tokens
            if (customerData == null) {
                return ResponseApi.error("401", "Invalid or expired token", null);
            }

            // Validate that the imageName is not null or empty
            if (customerImageDetailRequest.imageName() == null || customerImageDetailRequest.imageName().trim().isEmpty()) {
                return ResponseApi.error("400", "Image name cannot be null or empty", null);
            }

            // Fetch the image details by imageName
            List<ImageDetail> imageDetails = imageDetailRepository.findByImageName(customerImageDetailRequest.imageName());

            // Handle case where no images are found
            if (imageDetails.isEmpty()) {
                return ResponseApi.error("404", "No image detail found for the provided image name", null);
            }

            // Map the fetched image details to the response object
            List<CustomerImageDetailResponse> responseList = imageDetails.stream()
                    .map(imsMapping::mapToCustomerImageDetailResponse)
                    .collect(Collectors.toList());

            return ResponseApi.success(responseList);

        } catch (InvalidTokenException e) {
            // Handle invalid token exception
            return ResponseApi.error("401", "Invalid token", null);
        } catch (DatabaseException e) {
            // Handle database-related exceptions
            return ResponseApi.error("500", "Database error occurred", null);
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            ResponseStatus status = new ResponseStatus("500", "Internal server error");
            return ResponseApi.error(status.message(), null);
        }
    }


    @PostMapping("/add-image-detail")
    public ResponseApi<?> addImageDetail(
            @RequestHeader("Authorization") String token,
            @RequestBody ImageDetailRequestDTO imageDetailRequestDTO) {
        try {
            // Validate the token and get customer data
            Customer customerData = customerService.validateTokenAndGetCustomer(token);
            // Check if the imageName already exists in the ImageDetail table
            List<ImageDetail> existingImageDetails = imageDetailRepository.findByImageName(imageDetailRequestDTO.imageName());
            if (!existingImageDetails.isEmpty()) {
                return ResponseApi.error("400", "Image name already exists in the database");
            }
            // Validate description, number, and length
            if (imageDetailRequestDTO.description().size() != imageDetailRequestDTO.number().size() ||
                    imageDetailRequestDTO.description().size() != imageDetailRequestDTO.length().size()) {
                return ResponseApi.error("400", "Description, Number, and Length lists must have the same size");
            }
            // Check if the deviceID exists in the Image table
            List<Image> images = imageRepository.findByDeviceID(imageDetailRequestDTO.deviceID());
            if (images.isEmpty()) {
                return ResponseApi.error("404", "Device ID not found.");
            }
            // Check if imageName exists in the imageList
            Image image = images.get(0);
            if (image.getImageList().contains(imageDetailRequestDTO.imageName())) {
                return ResponseApi.error("400", "Image name already exists in the image list.");
            }
            // Add imageName and update Image entity
            image.getImageList().add(imageDetailRequestDTO.imageName());
            image.setUpdated_at(ZonedDateTime.now());
            imageRepository.save(image);

            ImageDetail imageDetail = ImageDetail.builder()
                    .imageName(imageDetailRequestDTO.imageName())
                    .description(imageDetailRequestDTO.description())
                    .number(imageDetailRequestDTO.number())
                    .length(imageDetailRequestDTO.length())
                    .build();
            imageDetailRepository.save(imageDetail);
            return ResponseApi.success("Image detail and Image updated successfully");
        } catch (InvalidTokenException e) {
            return ResponseApi.error("401", "Invalid token");
        } catch (Exception e) {
            return ResponseApi.error("500", "Internal server error");
        }
    }

    //AWS
    @PostMapping("/upload-image")
    public ResponseApi<?> uploadFile(
            @RequestHeader("Authorization") String token,
            @RequestParam("deviceID") String deviceID,
            @RequestParam("imageName") String imageName,
            @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseApi.error("400", "File is missing.");
        }
        try {
            // Validate the token and get customer data
            Customer customerData = customerService.validateTokenAndGetCustomer(token);
            List<Image> images = imageRepository.findByDeviceID(deviceID);
            if (images.isEmpty()) {
                return ResponseApi.error("404", "Device ID not found.");
            }
            // Check if imageName exists in the imageList
            Image image = images.get(0);
            if (image.getImageList().contains(imageName)) {
                // Upload the file
                List<ImageDetail> optionalImageDetail = imageDetailRepository.findByImageName(imageName);
                if (optionalImageDetail.isEmpty()) {
                    return ResponseApi.error("404", "Image Name not found in the Database Image List");
                }
                FileUploadResponse fileId = storageService.uploadFile(file, image.getImageBucket(), image.getTopic(), deviceID, imageName);
                if ("File already exists in the bucket.".equals(fileId.getMessage())) {
                    return ResponseApi.success("File already exists on S3 Bucket: " + fileId.getFileName());
                } else {
                    return ResponseApi.success("File uploaded successfully: " + fileId.getFileName());
                }
            }
            return ResponseApi.error("400", "Image Name exists in the Image List.");
        } catch (InvalidTokenException e) {
            return ResponseApi.error("401", "Invalid token");
        } catch (Exception e) {
            return ResponseApi.error("500", "Internal server error");
        }
    }

    //WASABI
    @PostMapping("/upload-image-ws")
    public ResponseApi<?> uploadFileWSB(
            @RequestHeader("Authorization") String token,
            @RequestParam("deviceID") String deviceID,
            @RequestParam("imageName") String imageName,
            @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseApi.error("400", "File is missing.");
        }
        try {
            // Validate the token and get customer data
            Customer customerData = customerService.validateTokenAndGetCustomer(token);
            List<Image> images = imageRepository.findByDeviceID(deviceID);
            if (images.isEmpty()) {
                return ResponseApi.error("404", "Device ID not found.");
            }
            // Check if imageName exists in the imageList
            Image image = images.get(0);
            if (image.getImageList().contains(imageName)) {
                // Upload the file
                List<ImageDetail> optionalImageDetail = imageDetailRepository.findByImageName(imageName);
                if (optionalImageDetail.isEmpty()) {
                    return ResponseApi.error("404", "Image Name not found in the Database Image List");
                }
                FileUploadResponse fileId = storageServiceWasabi.uploadFile(file, image.getImageBucket(), image.getTopic(), deviceID, imageName);
                if ("File already exists in the bucket.".equals(fileId.getMessage())) {
                    return ResponseApi.success("File already exists on S3 Bucket: " + fileId.getFileName());
                } else {
                    return ResponseApi.success("File uploaded successfully: " + fileId.getFileName());
                }
            }
            return ResponseApi.error("400", "Image Name exists in the Image List.");
        } catch (InvalidTokenException e) {
            return ResponseApi.error("401", "Invalid token");
        } catch (Exception e) {
            return ResponseApi.error("500", "Internal server error");
        }
    }

    @GetMapping("/get-list-image")
    public ResponseApi<ImageListDeviceResponse> getListImage(@RequestHeader("Authorization") String token) {
        try {
            String urlBase = "https://saty-dataset.s3.ap-southeast-1.amazonaws.com/";

            // Validate the token and get customer data
            Customer customerData = customerService.validateTokenAndGetCustomer(token);

            // Check if the customer is valid
            if (customerData == null) {
                return ResponseApi.error("401", "Customer not found");
            }

            // Get the customer devices using the email
            CustomerDevice customerDevice = customerDeviceRepository
                    .findByEmail(customerData.getEmail())
                    .orElseThrow(() -> new CustomerDeviceNotFound("CustomerDevice not found, Please Register Device"));

            // Get the list of device IDs
            Set<String> deviceIDs = customerDevice.getDeviceID();

            // Handle case where no devices are found
            if (deviceIDs.isEmpty()) {
                return ResponseApi.error("404", "No devices found for the customer");
            }

            // Get the images for all device IDs
            List<Image> images = imageRepository.findByDeviceIDIn(deviceIDs);

            // Handle case where no images are found
            if (images.isEmpty()) {
                return ResponseApi.error("404", "No images found for the given device IDs");
            }

            // Map the images to a response object
            List<ImageResponse> imageResponses = images.stream()
                    .map(image -> new ImageResponse(image.getImageBucket(), image.getDescription(), image.getTopic(), image.getImageList(), image.getDeviceID()))
                    .collect(Collectors.toList());

            // Create the response with images
            ImageListDeviceResponse response = new ImageListDeviceResponse(customerData.getEmail(), imageResponses);
            return ResponseApi.success(response);
        } catch (InvalidTokenException e) {
            return ResponseApi.error("401", "Invalid or expired token");
        } catch (CustomerDeviceNotFound e) {
            return ResponseApi.error("404", e.getMessage()); // Adjusting error code and message
        } catch (Exception e) {
            // Log the exception for debugging purposes
            // log.error("An unexpected error occurred: ", e);
            return ResponseApi.error("500", "Internal server error");
        }
    }




}
