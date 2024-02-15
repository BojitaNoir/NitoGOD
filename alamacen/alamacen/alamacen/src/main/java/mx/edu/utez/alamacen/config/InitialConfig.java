package mx.edu.utez.alamacen.config;

import mx.edu.utez.alamacen.model.person.PersonBean;
import mx.edu.utez.alamacen.model.person.PersonRepository;
import mx.edu.utez.alamacen.model.rol.RolBean;
import mx.edu.utez.alamacen.model.rol.RolRepository;
import mx.edu.utez.alamacen.model.user.UserBean;
import mx.edu.utez.alamacen.model.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;

@Configuration
public class InitialConfig implements CommandLineRunner {
private final RolRepository rolRepository;
private final UserRepository userRepository;
private final PersonRepository personRepository;

 //private final PasswordEncoder encoder;

    public InitialConfig(RolRepository rolRepository, UserRepository userRepository, PersonRepository personRepository) {
        this.rolRepository = rolRepository;
        this.userRepository = userRepository;
        this.personRepository = personRepository;

    }
@Transactional(rollbackFor = {SQLException.class})
    @Override
    public void run(String... args) throws Exception {
        RolBean adminrole = getOrSaveRole(new RolBean("ADMIN_ROLE"));
        getOrSaveRole(new RolBean("USER_ROLE"));
    getOrSaveRole(new RolBean("CLIENT_ROLE"));

    PersonBean person = getOrSaveRole(new PersonBean("Isai,""))
    }
    @Transactional
    public RolBean getOrSaveRole(RolBean rol){
        //or else get es para decir si no tienes algo se hara un saveAndFlush
        Optional<RolBean> foundRole = rolRepository.findByRol(rol.getRol());
        return foundRole.orElseGet(() ->rolRepository.saveAndFlush(rol));
    }
    @Transactional
    public PersonBean getOrSaveRole(PersonBean person){
        //or else get es para decir si no tienes algo se hara un saveAndFlush
        Optional<PersonBean> foundPerson = personRepository.findByCurp(person.getCurp());
        return foundPerson.orElseGet(() ->personRepository.saveAndFlush(person));
    }
    @Transactional
    public UserBean getOrSaveRole(UserBean user){
        //or else get es para decir si no tienes algo se hara un saveAndFlush
        Optional<UserBean> foundUser = userRepository.findByUser(user.getUser());
        return foundUser.orElseGet(() ->userRepository.saveAndFlush(user));
    }
    public void saveUserRole(Long id, Long roleId){
        Long userRoleId = userRepository.getIdUsersRoles(id,roleId);
        if (userRoleId==null)
            userRepository.saveUserRole(id, roleId);
    }
}
