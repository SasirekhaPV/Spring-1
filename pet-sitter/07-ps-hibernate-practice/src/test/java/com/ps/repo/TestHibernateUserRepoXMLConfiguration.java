package com.ps.repo;

import static com.ps.util.RecordBuilder.buildUser;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ps.base.PetType;
import com.ps.base.UserType;
import com.ps.ents.Pet;
import com.ps.ents.User;
import com.ps.repos.UserRepo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:hibernate-test-data-config.xml")
@ActiveProfiles("dev")
public class TestHibernateUserRepoXMLConfiguration {

    @Autowired
    private UserRepo userRepo;

    @Before
    public void setUp() {
        assertNotNull(userRepo);
        create();
    }

    @Test
    public void testFindById() {
        List<User> johns = userRepo.findAllByUserName("johncusack", true);
        assertTrue(johns.size() == 1);
    }

    @Test
    public void testNoFindById() {
        User user = userRepo.findById(99L);
        assertNull(user);
    }

    @Test
    public void testCreate() {
        final User diana = buildUser("diana.ross@pet.com");
        diana.setPassword("test");
        diana.setUserType(UserType.SITTER);

        userRepo.save(diana);

        final List<User> dianas = userRepo.findAllByUserName("dianaross", true);
        assertTrue(dianas.size() == 1);
    }

    @Test
    public void testUpdate() {
        List<User> johns = userRepo.findAllByUserName("johncusack", true);
        User john = johns.get(0);
        userRepo.updatePassword(john.getId(), "newpass");
    }

    @Test
    public void testDelete() {
        List<User> gigis = userRepo.findAllByUserName("gigipedala", true);
        User gigi = gigis.get(0);

        userRepo.deleteById(gigi.getId());
    }

    @After
    public void cleanUp() {
        final List<User> users = userRepo.findAll();
        for (User u : users) {
            userRepo.deleteById(u.getId());
        }
    }

    private void create() {
        final User john = buildUser("john.cusack@pet.com");
        john.setPassword("test");
        john.setUserType(UserType.OWNER);

        final Pet max = new Pet();
        max.setName("Max");
        max.setAge(10);
        max.setPetType(PetType.DOG);
        max.setRfid("1122334455");
        john.addPet(max);

        final Pet mona = new Pet();
        mona.setName("Mona");
        mona.setAge(2);
        mona.setPetType(PetType.CAT);
        mona.setRfid("1100223344");
        john.addPet(mona);
        userRepo.save(john);

        final User gigi = buildUser("gigi.pedala@pet.com");
        gigi.setPassword("test");
        gigi.setUserType(UserType.SITTER);
        userRepo.save(gigi);
    }
}
