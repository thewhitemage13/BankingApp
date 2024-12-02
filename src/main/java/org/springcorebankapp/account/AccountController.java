package org.springcorebankapp.account;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springcorebankapp.exception.AccountNotFoundException;
import org.springcorebankapp.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Account Controller", description = "Operations related to account management")
@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(
            summary = "Create a new account",
            description = "Creates a new account for a user specified by their login.",
            tags = {"Account Controller"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account successfully created"),
            @ApiResponse(responseCode = "404", description = "User with the specified login not found"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error")
    })
    @PostMapping("/create-account")
    public ResponseEntity<String> createAccount(
            @Parameter(description = "Login of the user for whom the account is created", required = true)
            @RequestParam("login") String login) {
        try {
            accountService.createAccount(login);
            return ResponseEntity.ok("Account for user with login = %s created".formatted(login));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with login = %s not found".formatted(login));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(
            summary = "Find an account by ID",
            description = "Retrieves an account's details using its unique ID.",
            tags = {"Account Controller"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found and returned successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error")
    })
    @GetMapping("/find-account-by-id")
    public ResponseEntity<Account> findAccountById(
            @Parameter(description = "Unique ID of the account", required = true)
            @RequestParam("id") Integer id) {
        try {
            return ResponseEntity.ok(accountService.findAccountById(id));
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(
            summary = "Deposit money into an account",
            description = "Adds a specified amount to the account balance.",
            tags = {"Account Controller"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deposit successful"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error")
    })
    @PutMapping("/deposit-account")
    public ResponseEntity<String> depositAccount(
            @Parameter(description = "ID of the account", required = true)
            @RequestParam("id") Integer id,
            @Parameter(description = "Amount to deposit", required = true)
            @RequestParam("amount") Integer amount) {
        try {
            accountService.depositAccount(id, amount);
            return ResponseEntity.ok("Account deposited successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(
            summary = "Withdraw money from an account",
            description = "Removes a specified amount from the account balance.",
            tags = {"Account Controller"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Withdrawal successful"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error")
    })
    @PutMapping("/withdraw-from-account")
    public ResponseEntity<String> withdrawAccount(
            @Parameter(description = "ID of the account", required = true)
            @RequestParam("id") Integer id,
            @Parameter(description = "Amount to withdraw", required = true)
            @RequestParam("amount") Integer amount) {
        try {
            accountService.withdrawFromAccount(id, amount);
            return ResponseEntity.ok("Account withdrawn successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(
            summary = "Close an account",
            description = "Deletes an account specified by its ID.",
            tags = {"Account Controller"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account closed successfully"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error")
    })
    @DeleteMapping("/close-account")
    public ResponseEntity<String> deleteAccount(
            @Parameter(description = "ID of the account to close", required = true)
            @RequestParam("id") Integer id) {
        try {
            accountService.closeAccount(id);
            return ResponseEntity.ok("Account closed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(
            summary = "Transfer money between accounts",
            description = "Transfers a specified amount of money from one account to another.",
            tags = {"Account Controller"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transfer successful"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error")
    })
    @PutMapping("/transfer")
    public ResponseEntity<String> transfer(
            @Parameter(description = "ID of the account to transfer money from", required = true)
            @RequestParam("fromAccountId") int fromAccountId,
            @Parameter(description = "ID of the account to transfer money to", required = true)
            @RequestParam("toAccountId") int toAccountId,
            @Parameter(description = "Amount to transfer", required = true)
            @RequestParam("amountToTransfer") int amountToTransfer) {
        try {
            accountService.transfer(fromAccountId, toAccountId, amountToTransfer);
            return ResponseEntity.ok("Account transfer successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
