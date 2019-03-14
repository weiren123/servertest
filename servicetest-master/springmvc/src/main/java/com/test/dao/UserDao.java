package com.test.dao;

import com.test.po.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Repository
public class UserDao {
    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public List<Account> queryAll() {
        String sql = "select * from t_user";
        return jdbcTemplate.query(sql, new UserMapper());
    }
    public List<Account> getAccountByUserName(String name){
        String sql = "select id,username from t_user where name like '%"+name+"%'";
        return jdbcTemplate.query(sql,new UserMapper());
    }
    public boolean addUser(Account account) {
        String sql = "insert into t_user(id,username,pwd) values(0,?,?)";
        return jdbcTemplate.update(sql, new Object[]{account.getUsername(), account.getPwd()}, new int[]{Types.VARCHAR, Types.VARCHAR}) == 1;
    }

    class UserMapper implements RowMapper<Account> {
        /**
         * Implementations must implement this method to map each row of data
         * in the ResultSet. This method should not call {@code next()} on
         * the ResultSet; it is only supposed to map values of the current row.
         *
         * @param rs     the ResultSet to map (pre-initialized for the current row)
         * @param rowNum the number of the current row
         * @return the result object for the current row
         * @throws SQLException if a SQLException is encountered getting
         *                      column values (that is, there's no need to catch SQLException)
         */
        @Override
        public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
            Account account = new Account();
            account.setId(rs.getInt("id"));
            account.setUsername(rs.getString("username"));
            account.setPwd(rs.getString("pwd"));
            return account;
        }
    }
}
