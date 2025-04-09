package aprendiendo.spring.Services;

import aprendiendo.spring.Models.Persona;
import aprendiendo.spring.Models.Username;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface ServicioPerson {

    ObjectNode registerPerson(Persona person);

    ObjectNode getPerson(int id);

    ObjectNode updatePerson(Persona person);

    ObjectNode deletePerson(int id);

    ObjectNode getPeople();

    ObjectNode loginPerson(Username user);
}
