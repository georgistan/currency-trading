package com.crypto.currencytrading.mapper;

import com.crypto.currencytrading.domain.Crypto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CryptoRowMapper implements RowMapper<Crypto> {

    @Override
    public Crypto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Crypto(
            rs.getString("crypto_symbol"),
            rs.getString("crypto_name"),
            rs.getDouble("quantity"),
            rs.getDouble("price")
        );
    }

}