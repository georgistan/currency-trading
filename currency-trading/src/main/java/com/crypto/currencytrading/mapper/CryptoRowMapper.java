package com.crypto.currencytrading.mapper;

import com.crypto.currencytrading.domain.TickerDataDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CryptoRowMapper implements RowMapper<TickerDataDTO> {

    @Override
    public TickerDataDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        TickerDataDTO tickerDataDTO = new TickerDataDTO();
        tickerDataDTO.setSymbol(rs.getString("symbol"));
        tickerDataDTO.setPrice(rs.getDouble("price"));

        return tickerDataDTO;
    }

}