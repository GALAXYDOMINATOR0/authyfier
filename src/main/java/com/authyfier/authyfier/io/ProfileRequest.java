package com.authyfier.authyfier.io;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileRequest {

	@NotBlank(message = "Name cannot be blank")
	private String name;
	@Email(message = "Email should be valid")
	@NotBlank(message = "Email cannot be blank")
	private String email;
	@Size(min = 6, message = "Password must be at least 6 characters long")
	private String password;

}
