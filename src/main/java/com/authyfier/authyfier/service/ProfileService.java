package com.authyfier.authyfier.service;

import com.authyfier.authyfier.io.ProfileRequest;
import com.authyfier.authyfier.io.ProfileResponse;

public interface ProfileService {


	ProfileResponse createProfile(ProfileRequest profileRequest);


}
