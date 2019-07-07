package utils;

import com.mysql.cj.util.StringUtils;
import model.Phone;
import view.ContactView;

import java.util.List;
import java.util.regex.Pattern;

public class Validator {
    private static Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile(Message.VALID_EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
    private static Pattern VALID_SITE_ADDRESS_REGEX = Pattern.compile(Message.VALID_SITE_PATTERN, Pattern.CASE_INSENSITIVE);

    public static void validate(ContactView contact)throws ApplicationException{
        if(StringUtils.isNullOrEmpty(contact.getName())){
            throw new ApplicationException(Message.REQUIRED_NAME);
        }
        if(StringUtils.isNullOrEmpty(contact.getSurname())){
            throw new ApplicationException(Message.REQUIRED_SURNAME);
        }
        if (contact.getBirthday() != null){
            try {
                DateFormatter.parseDate(contact.getBirthday());
            }catch (Exception e){
                throw new ApplicationException(Message.INCORRECT_DATE_FORMAT);
            }
        }
        if (!StringUtils.isNullOrEmpty(contact.getEmail()) && !VALID_EMAIL_ADDRESS_REGEX .matcher(contact.getEmail()).find()){
            throw new ApplicationException(Message.INVALID_MAIL);
        }
        if (!StringUtils.isNullOrEmpty(contact.getSite()) && !VALID_SITE_ADDRESS_REGEX .matcher(contact.getSite()).find()){
            throw new ApplicationException(Message.INVALID_SITE);
        }
        String postalCOde = contact.getAddressInfo().getPostalCode();
        if (!StringUtils.isNullOrEmpty(postalCOde)){
            validateLength(postalCOde, 6, "Postal Code");
            try {
                Integer.parseInt(postalCOde);
            }catch (Exception e){
                throw new ApplicationException(Message.INVALID_POSTAL_CODE);
            }
        }
        List<Phone> phones = contact.getPhoneInfo().getPhonesList();
        for(Phone phone: phones){
            validateLength(String.valueOf(phone.getCountryCode()), 3, "Country code");
            validateLength(String.valueOf(phone.getOperatorCode()), 4, "Operator code");
            validateLength(String.valueOf(phone.getPhoneNumber()), 12, "Phone number");
        }
    }

    public static void validateLength(String field, int length, String fieldName) throws ApplicationException {
        if (!StringUtils.isNullOrEmpty(field) && field.length() > length) {
            throw new ApplicationException(fieldName + " is too long. Max length is " + length);
        }
    }

}
