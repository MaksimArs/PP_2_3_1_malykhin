package web.dao;

import org.springframework.stereotype.Repository;
import web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager em;
    @Override
    //@SuppressWarnings("unchecked")
    public List<User> getAllUsers() {
        return em.createQuery("FROM User ").getResultList();
    }

    @Override
    //@SuppressWarnings("unchecked")
    public User getUserById(int id) {
        return em.find(User.class, id);
    }

    @Override
    public void save(User user) {
        em.persist(user);

    }

    @Override
    public void update(User updatedUser) {
        em.merge(updatedUser);
        em.flush();
    }

    @Override
    public void delete(int id) {
        em.remove(getUserById(id));

    }
}
