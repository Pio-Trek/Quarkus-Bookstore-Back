package quarkus.bookstore.application;

import javax.inject.Singleton;

@Singleton
public class Startup {

    // Only for use in dev env for technical users tests purposes and should not be used in production

/*
    @Transactional
    public void loadUsers(@Observes StartupEvent evt) {
        User.deleteAll();
        User.add("TU001", "passw0rd", Role.TECHNICAL_USER);
    }
*/

}
