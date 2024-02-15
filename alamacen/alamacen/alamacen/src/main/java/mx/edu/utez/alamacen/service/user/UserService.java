package mx.edu.utez.alamacen.service.user;

import lombok.AllArgsConstructor;
import mx.edu.utez.alamacen.config.ApiResponse;
import mx.edu.utez.alamacen.model.category.CategoryBean;
import mx.edu.utez.alamacen.model.person.PersonBean;
import mx.edu.utez.alamacen.model.person.PersonRepository;
import mx.edu.utez.alamacen.model.product.ProductBean;
import mx.edu.utez.alamacen.model.rol.RolBean;
import mx.edu.utez.alamacen.model.rol.RolRepository;
import mx.edu.utez.alamacen.model.user.UserBean;
import mx.edu.utez.alamacen.model.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final RolRepository rolRepository;

    private final PersonRepository personRepository;

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> save(UserBean user){

        if (user.getRolBean() == null || user.getPersonBean() == null) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "El rol o la persona no son válidos"), HttpStatus.BAD_REQUEST);
        }

        Optional<RolBean> foundRol = rolRepository.findById(user.getRolBean().getId());
        if (!foundRol.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "El rol no existe"), HttpStatus.BAD_REQUEST);
        }

        if (user.getPersonBean() != null) {
            Optional<PersonBean> foundPerson = personRepository.findById(user.getPersonBean().getId());
            if (!foundPerson.isPresent()) {
                personRepository.saveAndFlush(user.getPersonBean());
            }
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "La persona asociada al usuario no puede ser nula"), HttpStatus.BAD_REQUEST);
        }


        Optional<UserBean> foundUser = userRepository.findByUser(user.getUser());
        if (foundUser.isPresent()){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "El registro ya existe"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ApiResponse(userRepository.saveAndFlush(user), HttpStatus.OK, false, "Resgistro exitoso"), HttpStatus.OK);
    }

/*
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> save (UserBean user){
        Optional<UserBean> foundUser = userRepository.findByUser(user.getUser());
        if (foundUser.isPresent()){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "El registro ya existe"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse(userRepository.saveAndFlush(user), HttpStatus.OK, false, "Resgistro exitoso"), HttpStatus.OK);
    }

    */


    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> delete(long id){
        Optional<UserBean> foundUser = userRepository.findById(id);
        if (foundUser.isPresent()){
            userRepository.deleteById(id);
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, false, "El registro se ha eliminado"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "El registro no existe"), HttpStatus.BAD_REQUEST);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> update(long id, UserBean entity){
        Optional<UserBean> foundUser = userRepository.findById(id);
        if (foundUser.isPresent()){
            entity.setId(id);
            return new ResponseEntity<>(new ApiResponse(userRepository.saveAndFlush(entity), HttpStatus.OK, false, "Actualizacion exitoso"), HttpStatus.OK);

        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "El registro no existe"), HttpStatus.BAD_REQUEST);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> getAll(){
        return new ResponseEntity<>(new ApiResponse(userRepository.findAll(), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> getByUser(String user){
        Optional<UserBean> foundUser = userRepository.findByUser(user);
        if (foundUser.isPresent()){
            return new ResponseEntity<>(new ApiResponse(foundUser, HttpStatus.OK), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "El registro no existe"), HttpStatus.BAD_REQUEST);
    }

}
