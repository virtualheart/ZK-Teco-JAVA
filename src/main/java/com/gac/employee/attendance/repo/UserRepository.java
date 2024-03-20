package com.gac.employee.attendance.repo;

import com.gac.employee.attendance.model.AppUserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUserModel,Integer> {
    Optional<AppUserModel> findByUserName(String userName);

}
