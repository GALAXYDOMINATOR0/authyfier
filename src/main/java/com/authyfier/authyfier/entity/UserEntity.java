package com.authyfier.authyfier.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name ="users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String name;
	private String userId;

	@Column(unique = true, nullable = false)
	private String email;
	private String password;
	private String VerifyOTP;
	private Boolean isVerified;
	private Long VerifyOtpExpiry;
	private String resetOTP;
	private Long resetOtpExpiryAt;

	@CreationTimestamp
	@Column(updatable = false)
	private Long createdAt;
	@UpdateTimestamp
	private Long updatedAt;


}
