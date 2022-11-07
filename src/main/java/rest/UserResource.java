package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.User;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserResource {

//    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
//
//    @POST
//    @Consumes({MediaType.APPLICATION_JSON})
//    @Produces({MediaType.APPLICATION_JSON})
//    public Response createPerson(String user) throws EntityNotFoundException {
//
//        String errorMsg = "";
//
//        // Make person from request body
//        User userFromJson = GSON.fromJson(user, User.class);
//        System.out.println(userFromJson);
//
//        //VALIDATE PERSON INFORMATION - FIND UD AF HVAD VI SKAL BRUGE AF OPLYSNINGER NÅR VI LAVER EN PERSON?
//        // validate person email
//        String email = userFromJson.getUserPass();
//        Pattern VALID_EMAIL_ADDRESS_REGEX =
//                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
//
//        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
//        boolean isValidEmail = matcher.find();
//        if(!isValidEmail || email == null) {
//            errorMsg += "Parsed email was in a wrong format. ";
//        }
//
//        if(personFromJson.getFirstName().length() < 2) {
//            errorMsg += "Firstname was not parsed correctly (at least two characters). ";
//        }
//
//        if(personFromJson.getLastName().length() < 2) {
//            errorMsg += "Lastname was not parsed correctly (at least two characters). ";
//        }
//
//        String phoneNumber = personFromJson.getPhone().getNumber();
//        String VALID_PHONE_NUMBER_REGEX = "(?<!\\d)\\d{8}(?!\\d)";
//        boolean isValidNumber = phoneNumber.matches(VALID_PHONE_NUMBER_REGEX);
//        if(!isValidNumber || phoneNumber == null) {
//            errorMsg += "Invalid number parsed. Please enter a valid danish phone number (8 digits). ";
//        }
//
//        if(personFromJson.getAddress().getStreet() == null) {
//            errorMsg += "Street was not parsed. ";
//        }
//        int zipCode = personFromJson
//                .getAddress()
//                .getCityInfo()
//                .getZipCode();
//        String VALID_ZIPCODE_REGEX = "^[0-9]{3,4}$"; // this here can be made to check if the zipcode given is a correct danish zipcode
//        String zipCodeToStr = String.valueOf(zipCode);
//        boolean checkZipCode = zipCodeToStr.matches(VALID_ZIPCODE_REGEX);
//        if(!checkZipCode || zipCodeToStr == null) {
//            errorMsg += "Unknown ZipCode format: " + zipCode + ". Please enter a valid danish zipcode (typically 3 or 4 digits). ";
//        }
//
//        if(errorMsg.length() > 1) {
//            throw new EntityNotFoundException(errorMsg);
//        }
//
//        // get cityInfo
//        CityInfoDTO cityInfoDTO = cityInfoFacade.getCityByZipCode(personFromJson.getAddress().getCityInfo().getZipCode());
//
//        System.out.println(cityInfoDTO);
//        personFromJson.getAddress().setCityInfo(new CityInfo(cityInfoDTO));
//
//        // create address
////        AddressDTO a = addressFacade.create(new AddressDTO(personFromJson.getAddress()));
////        System.out.println(a);
////        personFromJson.setAddress(new Address(a));
//
//        // create phone
////        Phone phone = phoneFacade.createPhone(personFromJson.getPhone());
////        personFromJson.setPhone(phone);
//
//        // create the person
//        Person pNew = FACADE.createPerson(personFromJson);
//        PersonDTO personDTO = new PersonDTO(pNew);
//        String result = GSON.toJson(personDTO);
//        return Response.ok().entity(result).build();
//    }
}
