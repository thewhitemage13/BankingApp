package org.springcorebankapp.account;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a bank account entity in the system.
 * <p>
 * This class is a JPA entity that maps to the "accounts" table in the database.
 * It stores key information about a user's account, including the unique account ID,
 * the user ID of the account holder, and the current balance.
 * <p>
 * Annotations from Lombok are used to reduce boilerplate code for getters,
 * setters, and constructors.
 * </p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *     <li>Unique account identification with an auto-generated ID.</li>
 *     <li>Storage of user ID to link the account to its owner.</li>
 *     <li>Tracking of the account's current monetary balance.</li>
 * </ul>
 *
 * @author Mukhammed Lolo
 * @version 1.0.0
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
//@RedisHash("Account")
@Table(name = "accounts")
public class Account implements Serializable {

    /**
     * Unique identifier for the account.
     * <p>
     * This field is the primary key in the "accounts" table and is automatically
     * generated using the IDENTITY strategy.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Identifier of the user who owns the account.
     * <p>
     * Maps to the "user_id" column in the "accounts" table. This serves as a
     * foreign key to link accounts to users.
     * </p>
     */
    @Column(name = "user_id")
    private int userId;

    /**
     * The monetary balance currently held in the account.
     * <p>
     * Maps to the "money_amount" column in the "accounts" table and represents
     * the amount of funds available in the account.
     * </p>
     */
    @Column(name = "money_amount")
    private int moneyAmount;

    /**
     * Constructs a new {@code Account} with the specified user ID and initial balance.
     *
     * @param userId      the unique identifier of the user who owns the account
     * @param moneyAmount the initial monetary balance of the account
     */
    public Account(int userId, int moneyAmount) {
        this.userId = userId;
        this.moneyAmount = moneyAmount;
    }

    /**
     * Compares this account with another object for equality.
     * <p>
     * Two accounts are considered equal if their {@code id}, {@code userId},
     * and {@code moneyAmount} are identical.
     * </p>
     *
     * @param o the object to be compared with this account
     * @return {@code true} if the objects are equal; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id && userId == account.userId && moneyAmount == account.moneyAmount;
    }

    /**
     * Computes a hash code for this account based on its fields.
     * <p>
     * The hash code is calculated using the {@code id}, {@code userId},
     * and {@code moneyAmount} fields to ensure consistent results.
     * </p>
     *
     * @return the hash code value of this account
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, userId, moneyAmount);
    }

    /**
     * Returns a string representation of this account.
     * <p>
     * The string contains the account's {@code id}, {@code userId}, and {@code moneyAmount}.
     * </p>
     *
     * @return a string representation of the account
     */
    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userId=" + userId +
                ", moneyAmount=" + moneyAmount +
                '}';
    }
}
