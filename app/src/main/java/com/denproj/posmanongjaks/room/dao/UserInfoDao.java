package com.denproj.posmanongjaks.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.denproj.posmanongjaks.model.Branch;
import com.denproj.posmanongjaks.model.Role;
import com.denproj.posmanongjaks.model.User;

import java.util.List;

@Dao
public interface UserInfoDao {

    @Insert
    void saveUser(User user);

    @Insert
    void saveRoleInfo(Role role);

    @Insert
    void saveBranchInfo(Branch branch);

    @Query("SELECT * FROM Branch WHERE Branch.branch_id = :branchId LIMIT 1")
    List<Branch> getBranchInfoWithBranchId(String branchId);

    @Query("SELECT * FROM Role WHERE Role.role_name = :role_id LIMIT 1")
    List<Role> getRoleWithRoleName(String role_id);

    @Query("SELECT * FROM User LIMIT 1")
    List<User> getUser();

    @Query("DELETE FROM Branch")
    void clearBranch();
    @Query("DELETE FROM User")
    void clearUser();
    @Query("DELETE FROM Role")
    void clearRole();

}
