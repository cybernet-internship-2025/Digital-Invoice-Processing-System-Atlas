package az.cybernet.usermanagement.util.handler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

@Slf4j
public class UUIDTypeHandler implements TypeHandler<UUID> {

    public void setParameter(PreparedStatement ps, int i, UUID parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            ps.setObject(i, (Object)null, 1111);
        } else {
            ps.setObject(i, parameter.toString(), 1111);
        }

    }

    public UUID getResult(ResultSet rs, String columnName) throws SQLException {
        return toUUID(rs.getString(columnName));
    }

    public UUID getResult(ResultSet rs, int columnIndex) throws SQLException {
        return toUUID(rs.getString(columnIndex));
    }

    public UUID getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return toUUID(cs.getString(columnIndex));
    }

    private static UUID toUUID(String val) {
        if (!Objects.isNull(val) && !val.isEmpty()) {
            try {
                return UUID.fromString(val);
            } catch (IllegalArgumentException e) {
                log.error("Provided string cannot be converted to UUID. Invalid value: '{}'", val, e);

                return null;
            }
        } else {
            return null;
        }
    }
}