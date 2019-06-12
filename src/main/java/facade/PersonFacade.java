/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;


import dto.CityInfoDTO;
import dto.PersonDTO;
import entity.CityInfo;
import entity.Hobby;
import entity.Person;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import entity.FacadeInterface;
import exception.MyException;
import javax.persistence.TypedQuery;

/**
 *
 * @author claudia
 */
public class PersonFacade implements FacadeInterface
{

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");

    public EntityManager getManager()
    {
        return emf.createEntityManager();
    }

    public PersonFacade()
    {
    }

    public PersonFacade(EntityManagerFactory emf)
    {
        this.emf = emf;
    }

    EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    @Override
    public PersonDTO getPerson(String phoneNumber)
    {
        EntityManager em = getEntityManager();
        try
        {
            TypedQuery<PersonDTO> tq = em.createQuery("select new dto.PersonDTO(p) From Person as p join p.phones ph where ph.number = :phoneNumber", PersonDTO.class);
            tq.setParameter("phoneNumber", phoneNumber);
            PersonDTO person = tq.getSingleResult();
            if (person == null) {
                throw new MyException("Could not find person with that number", 404);
            }
            return person;
        } finally
        {
            em.close();
        }
    }

    @Override
    public List<PersonDTO> getPersonsByHobby(String hobby)
    {
        EntityManager em = getEntityManager();
            try {
            TypedQuery<PersonDTO> tq = em.createQuery("select new dto.PersonDTO(p) From Person as p join p.hobbies h where h.name = :hobby", PersonDTO.class);
            tq.setParameter("hobby", hobby);
            if (hobby == null) {
                throw new MyException("Could not find any persons with that hobby", 404);
            }
            return tq.getResultList();

        } finally
        {
            em.close();
        }
    }

    public Hobby getHobbyById(int id)
    {
        EntityManager em = getEntityManager();
        try
        {
            Hobby h = em.find(Hobby.class, id);
            if (h == null) {
                throw new MyException("Could not find any hobbies matching the id", 404);
            }
            return h;
        } finally
        {
            em.close();
        }
    }

    @Override
    public List<PersonDTO> getPersonsByCity(String city)
    {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            TypedQuery<PersonDTO> q = em.createQuery(
                    "SELECT DISTINCT new dto.PersonDTO(p) FROM Person p LEFT JOIN p.addresses.cityInfo c "
                    + "WHERE c.city= :city", PersonDTO.class);
            q.setParameter("city", city);
            em.getTransaction().commit();
            if (city == null) {
                throw new MyException("Could not find any persons living in that city", 404);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<PersonDTO> getPersonsByZip(Integer zip)
    {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            TypedQuery<PersonDTO> q = em.createQuery(
                    "SELECT DISTINCT new dto.PersonDTO(p) FROM Person p LEFT JOIN p.addresses.cityInfo c "
                    + "WHERE c.zip= :zip", PersonDTO.class);
            q.setParameter("zip", zip);
            em.getTransaction().commit();
            if (zip == null) {
                throw new MyException("Could not find any persons with that zipcode", 404);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<CityInfoDTO> getAllzipCodes()
    {
        EntityManager em = getEntityManager();
        try
        {
            List<CityInfoDTO> dtol = new ArrayList();
            List<CityInfo> cl = em.createQuery("select c FROM CityInfo c", CityInfo.class).getResultList();
            for (CityInfo c : cl){
                CityInfoDTO dtoc = new CityInfoDTO(c);
                dtol.add(dtoc);
            }
            if (dtol == null) {
                throw new MyException("Could not find any zipcodes", 404);
            }
            return dtol;
            
        } finally
        {
            em.close();
        }
    }

    @Override
    public int getPersonCountByHobby(String name)
    {
        EntityManager em = getEntityManager();
        try
        {
            em.getTransaction().begin();
            Query q = em.createQuery("select count(p) FROM Person p LEFT JOIN p.hobbies h "
                    + "WHERE h.name = :name");
            q.setParameter("name", name);
            em.getTransaction().commit();
            if (name == null) {
                throw new MyException("Could not find any persons with that hobby", 404);
            }
            return (int) q.getSingleResult();
        } finally
        {
            em.close();
        }
    }

    @Override
    public Person addPerson(Person p)
    {
        EntityManager em = getEntityManager();
        try
        {
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
            if (p == null) {
                throw new MyException("Could not add person", 404);
            }
            return p;
        } finally
        {
            em.close();
        }
    }

    @Override
    public Person deletePerson(int id)
    {
        EntityManager em = getEntityManager();
        try
        {
            em.getTransaction().begin();
            Person p = em.find(Person.class, id);
            em.remove(p);
            em.getTransaction().commit();
            if (p == null) {
                throw new MyException("Could not delete person", 404);
            }
            return p;
        } finally
        {
            em.close();
        }
    }

    @Override
    public Person updatePerson(Person p)
    {
        EntityManager em = getEntityManager();
        try
        {
            em.getTransaction().begin();
            em.merge(p);
            em.getTransaction().commit();
            if (p == null) {
                throw new MyException("Could not update person", 404);
            }
            return p;
        } finally
        {
            em.close();
        }
    }

    public List<PersonDTO> getAllPersonsDTO()
    {
        EntityManager em = getManager();
        List<PersonDTO> dtol = new ArrayList();

        List<Person> cs = em.createQuery("SELECT p FROM Person p").getResultList();
        for (Person c : cs)
        {
            PersonDTO dto = new PersonDTO(c);
            dtol.add(dto);
        }
        if (dtol == null) {
                throw new MyException("Could not find any persons", 404);
            }
        return dtol;
    }
    
    

}
