package com.wqlworld.fszs.comm;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;

public class MyPhysicalNamingStrategy extends SpringPhysicalNamingStrategy{
	@Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        String tableName = name.getText();
        return Identifier.toIdentifier(tableName);
    }

}
