package advancedTDDKata.services;

import advancedTDDKata.entities.UserEntity;

import java.util.List;

public interface UserRegistrationService {

    UserEntity saveNewUserData(UserEntity data, String siteURL) throws Exception;

    List<UserEntity> getAll();
}
