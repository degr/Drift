package org.forweb.drift.dao;

import org.forweb.drift.entity.User;
import org.forweb.database.AbstractDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends AbstractDao<User> {

    @Query("select u from User u where u.username = :username")
    User loadUserByUsername(@Param("username") String username);
}
