package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcTransferStatusDao implements TransferStatusDao{
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferStatusDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TransferStatus getTransferStatusByDescription(String description) {
        TransferStatus transferStatus = null;
        String sql = "SELECT transfer_status_id, transfer_status_desc FROM transfer_status WHERE transfer_status_desc = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, description);
            if (results.next()) {
                int transferStatusId = results.getInt("transfer_status_id");
                String transferStatusDescription = results.getString("transfer_status_desc");
                transferStatus = new TransferStatus(transferStatusId, transferStatusDescription);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transferStatus;
    }

    @Override
    public TransferStatus getTransferStatusById(int id) {
        TransferStatus transferStatus = null;
        String sql = "SELECT transfer_status_id, transfer_status_desc FROM transfer_status WHERE transfer_status_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                int transferStatusId = results.getInt("transfer_status_id");
                String transferStatusDescription = results.getString("transfer_status_desc");
                transferStatus = new TransferStatus(transferStatusId, transferStatusDescription);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transferStatus;
    }
}