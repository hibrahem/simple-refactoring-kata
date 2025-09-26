package advancedTDDKata.repositories;

import advancedTDDKata.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);

    @Query("SELECT user FROM UserEntity user WHERE user.verificationCode = ?1")
    UserEntity findByVerificationCode(String verificationCode);
}
