package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transaction;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;

public class JdbcTransferDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public Transaction getTransactionByTransferId(int transferId) {
        Transaction transaction = null;
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount FROM transfer WHERE " +
                "transfer_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            if (results.next()){
                transaction = mapRowToTransaction(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transaction;
    }


    public Transaction createTransaction(Transaction transaction) {
        Transaction createdTransaction = null;
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount) VALUES (?, ?, ?, ?, ?) " +
                "RETURNING transfer_id;";
        // Writing a method the will create a new row in the transaction table (transaction type will be 2 (send) and
        // transfer status will be 2 (ApprovedS))
        try {
            int createdTransactionId = jdbcTemplate.queryForObject(sql, int.class, 2, 2, transaction.getAccountFrom(), transaction.getAccountFrom(), transaction.getAmount());
            createdTransaction = getTransactionByTransferId(createdTransactionId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return createdTransaction;
    }

    public Transaction mapRowToTransaction(SqlRowSet rs){
        Transaction transaction = new Transaction();
        transaction.setTransferId(rs.getInt("transfer_id"));
        transaction.setTransferTypeId(rs.getInt("transfer_type_id"));
        transaction.setTransferStatusId(rs.getInt("transfer_status_id"));
        transaction.setAccountFrom(rs.getInt("account_from"));
        transaction.setAccountTo(rs.getInt("account_to")) ;
        transaction.setAmount(rs.getBigDecimal("amount"));
        return transaction;
    }
}
