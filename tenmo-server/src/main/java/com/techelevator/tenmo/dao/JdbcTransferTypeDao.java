package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.TransferType;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcTransferTypeDao implements TransferTypeDao{
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferTypeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TransferType getTransferTypeById(int id) {
        TransferType transferType = null;
        String sql = "SELECT transfer_type_id, transfer_type_desc FROM transfer_type WHERE transfer_type_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                int transferTypeId = results.getInt("transfer_type_id");
                String transferTypeDescription = results.getString("transfer_type_desc");
                transferType = new TransferType(transferTypeId, transferTypeDescription);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transferType;
    }
}