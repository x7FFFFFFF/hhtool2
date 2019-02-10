package ru.alex.vic;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class Utils {


    public static Properties createProperties(String propertyFileName) {
        try (final InputStream stream = Utils.class.getClassLoader().getResourceAsStream(propertyFileName)) {
            Properties properties = new Properties();
            properties.load(stream);
            return properties;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public interface UserDao {
        @SqlUpdate("CREATE TABLE user (id INTEGER PRIMARY KEY, name VARCHAR)")
        void createTable();

        @SqlUpdate("INSERT INTO user(id, name) VALUES (?, ?)")
        void insertPositional(int id, String name);

        @SqlUpdate("INSERT INTO user(id, name) VALUES (:id, :name)")
        void insertNamed(@Bind("id") int id, @Bind("name") String name);

        @SqlUpdate("INSERT INTO user(id, name) VALUES (:id, :name)")
        void insertBean(@BindBean User user);

        @SqlQuery("SELECT * FROM user ORDER BY name")
        @RegisterBeanMapper(User.class)
        List<User> listUsers();
    }

    public static void main(String[] args) {
        Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test");
        jdbi.installPlugin(new SqlObjectPlugin());
        List<User> userNames = jdbi.withExtension(UserDao.class, dao -> {
            dao.createTable();
            dao.insertPositional(0, "Alice");
            dao.insertPositional(1, "Bob");
            dao.insertNamed(2, "Clarice");
            dao.insertBean(new User(3, "David"));
            return dao.listUsers();
        });
    }

    private static class User {
        int id;
        String name;

        public User(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public User() {
        }
    }
}
