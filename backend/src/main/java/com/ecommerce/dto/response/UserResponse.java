package com.ecommerce.dto.response;

import com.ecommerce.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private AddressDto address;
    private Set<String> roles;
    private String role; // Primary role for admin display
    private String profileImage;
    private boolean active;
    private LocalDateTime createdAt;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressDto {
        private String street;
        private String city;
        private String state;
        private String zipCode;
        private String country;
    }
    
    public static UserResponse fromUser(User user) {
        AddressDto addressDto = null;
        if (user.getAddress() != null) {
            addressDto = AddressDto.builder()
                    .street(user.getAddress().getStreet())
                    .city(user.getAddress().getCity())
                    .state(user.getAddress().getState())
                    .zipCode(user.getAddress().getZipCode())
                    .country(user.getAddress().getCountry())
                    .build();
        }
        
        Set<String> roleNames = user.getRoles().stream().map(Enum::name).collect(Collectors.toSet());
        String primaryRole = roleNames.contains("ADMIN") ? "ADMIN" : "USER";
        
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(addressDto)
                .roles(roleNames)
                .role(primaryRole)
                .profileImage(user.getProfileImage())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
