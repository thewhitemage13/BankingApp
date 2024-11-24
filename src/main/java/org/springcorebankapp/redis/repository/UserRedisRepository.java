package org.springcorebankapp.redis.repository;

import org.springcorebankapp.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("userRedisRepository")
public interface UserRedisRepository extends CrudRepository<User, Integer> {
}
