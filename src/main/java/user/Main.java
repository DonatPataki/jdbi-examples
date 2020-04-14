package user;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test");
        jdbi.installPlugin(new SqlObjectPlugin());

        User testUser1 = User.builder()
                .username("username1")
                .password("password1")
                .name("name1")
                .email("email1")
                .gender(User.Gender.FEMALE)
                .dob(LocalDate.parse("2020-04-13"))
                .enabled(true)
                .build();

        User testUser2 = User.builder()
                .username("username2")
                .password("password2")
                .name("name2")
                .email("email2")
                .gender(User.Gender.MALE)
                .dob(LocalDate.parse("2020-04-12"))
                .enabled(true)
                .build();

        try (Handle handle = jdbi.open()) {
            UserDao dao = handle.attach(UserDao.class);
            dao.createTable();
            dao.insert(testUser1);
            dao.insert(testUser2);
            dao.list().stream().forEach(System.out::println);
            System.out.println("---findByid---");
            System.out.println(dao.findById(2).get().toString());
            System.out.println("---findByUsername---");
            System.out.println(dao.findByUsername("username2").get().toString());
            dao.delete(dao.findById(2).get());
            System.out.println("---after deletion---");
            dao.list().stream().forEach(System.out::println);
        }
    }

}
