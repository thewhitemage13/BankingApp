package org.springcorebankapp.account;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findByUserId(int userId);
}
