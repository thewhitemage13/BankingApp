package org.springcorebankapp.account;

import org.springcorebankapp.exception.AccountNotFoundException;
import org.springcorebankapp.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create-account")
    public ResponseEntity<String> createAccount(@RequestParam("login") String login) {
        try {
            accountService.createAccount(login);
            return ResponseEntity.ok("Account for user with login = %s created".formatted(login));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with login = %s not found".formatted(login));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/find-account-by-id")
    public ResponseEntity<Account> findAccountById(@RequestParam("id") Integer id) {
        try {
            return ResponseEntity.ok(accountService.findAccountById(id));
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/deposit-account")
    public ResponseEntity<String> depositAccount(@RequestParam("id") Integer id, @RequestParam("amount") Integer amount) {
        try {
            accountService.depositAccount(id, amount);
            return ResponseEntity.ok("Account deposited successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/withdraw-from-account")
    public ResponseEntity<String> withdrawAccount(@RequestParam("id") Integer id, @RequestParam("amount") Integer amount) {
        try {
            accountService.withdrawFromAccount(id, amount);
            return ResponseEntity.ok("Account withdrawn successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/close-account")
    public ResponseEntity<String> deleteAccount(@RequestParam("id") Integer id) {
        try {
            accountService.closeAccount(id);
            return ResponseEntity.ok("Account closed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestParam("fromAccountId") int fromAccountId, @RequestParam("toAccountId") int toAccountId, @RequestParam("amountToTransfer") int amountToTransfer) {
        try {
            accountService.transfer(fromAccountId, toAccountId, amountToTransfer);
            return ResponseEntity.ok("Account transfer successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
