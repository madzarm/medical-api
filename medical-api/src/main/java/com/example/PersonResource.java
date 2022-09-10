//package com.example;
//
//import com.example.service.PersonService;
//import com.example.service.request.CreatePersonRequest;
//import com.example.service.result.ValidationResult;
//import io.smallrye.mutiny.Uni;
//import org.eclipse.microprofile.openapi.annotations.Operation;
//import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
//
//import javax.inject.Inject;
//import javax.transaction.Transactional;
//import javax.validation.ConstraintViolation;
//import javax.validation.Validator;
//import javax.ws.rs.Consumes;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import java.util.Set;
//
//@Path("/person")
//public class PersonResource {
//
//    @Inject
//    Validator validator;
//    @Inject
//    PersonService personService;
//
//
//
//
//
//    private <T> ValidationResult validate(T request) {
//        Set<ConstraintViolation<T>> violations = validator.validate(request);
//
//        if (violations.isEmpty()) {
//            return new ValidationResult("Request is valid! It was validated by manual validation.");
//        } else {
//            return new ValidationResult(violations);
//        }
//    }
//}
