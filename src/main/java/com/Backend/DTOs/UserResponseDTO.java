package com.Backend.DTOs;

import com.Backend.Auth.Models.Provider;



import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;






import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {
   
    private UUID id; 
    private String username; 
    
    private String email; 
    @Builder.Default
    private Boolean isEnabled = true;
    private String imageUrl; 
    // @CreationTimestamp 
    private LocalDateTime createdAt; 
    // @UpdateTimestamp
    private LocalDateTime  updatedAt; 

    @Builder.Default
    private Provider provider = Provider.LOCAL; 

    @Builder.Default
    private Set<RoleDTO> roles = new HashSet<>();  
    
}
