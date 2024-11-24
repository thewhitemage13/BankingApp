package org.springcorebankapp.redis.repository;

import org.springcorebankapp.account.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRedisRepository extends CrudRepository<Account, Integer> {
}
