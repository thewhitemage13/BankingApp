package org.springcorebankapp.account;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for account management.
 * <p>
 * This class retrieves and stores configurable properties for account-related operations,
 * such as the default initial amount for new accounts and the commission rate for money transfers.
 * The values are injected from the application's configuration files (e.g., application.properties or application.yml).
 * </p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *     <li>Manages the default amount assigned to new accounts.</li>
 *     <li>Defines the commission percentage for account-to-account transfers.</li>
 * </ul>
 *
 * <p>
 * This class is a Spring {@link Component}, meaning it is managed by the Spring IoC container.
 * The values for its fields are injected using Spring's {@link Value} annotation.
 * </p>
 *
 * @author Mukhammed Lolo
 * @version 1.0.0
 */
@Component
public class AccountProperties {

    /**
     * The default amount of money assigned to a new account.
     * <p>
     * This value is injected from the configuration property {@code account.default-amount}.
     * </p>
     */
    private final int defaultAccountAmount;

    /**
     * The commission rate for money transfers.
     * <p>
     * This value is injected from the configuration property {@code account.transfer.commission}.
     * </p>
     */
    private final double transferCommission;

    /**
     * Constructs a new {@code AccountProperties} instance with the specified default account amount
     * and transfer commission rate.
     *
     * @param defaultAccountAmount the default monetary amount assigned to new accounts,
     *                             injected from {@code account.default-amount}
     * @param transferCommission   the commission rate for transfers, injected from {@code account.transfer.commission}
     */
    public AccountProperties(@Value("${account.default-amount}") int defaultAccountAmount,
                             @Value("${account.transfer.commission}") double transferCommission) {
        this.defaultAccountAmount = defaultAccountAmount;
        this.transferCommission = transferCommission;
    }

    /**
     * Returns the default monetary amount assigned to new accounts.
     *
     * @return the default account amount
     */
    public int getDefaultAccountAmount() {
        return defaultAccountAmount;
    }

    /**
     * Returns the commission rate applied to account-to-account transfers.
     *
     * @return the transfer commission rate
     */
    public double getTransferCommission() {
        return transferCommission;
    }
}
