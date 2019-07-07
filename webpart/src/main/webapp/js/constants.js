App.Constants = {
    PAGE_NUMBER_DEFAULT: 1,
    PAGE_SIZE_DEFAULT: 10,

    messages: {
        ERROR_MESSAGE: "Error has occurred during request",
        DELETE_CONTACT_CONFIRMATION: "Are you sure you want to delete selected contacts?",
        DELETE_PHONE_CONFIRMATION: "Are you sure you want to delete selected phones?",
        DELETE_ATTACHMENT_CONFIRMATION: "Are you sure you want to delete selected attachments?",
        SELECT_CONTACT_WARNING: "You should check a contact!",
        NO_CONTACTS_EMAIL: "Selected contacts don't have emails!",
        SELECT_PHONE_WARNING: "You should check a phone!",
        SELECT_PHOTO_WARNING: "You should select a profile photo!",
        SELECT_ATTACHMENT_WARNING: "You should check an attachment!",
        SELECT_SINGLE_PHONE_WARNING: "You should check only 1 phone for editing!",
        SELECT_SINGLE_ATTACHMENT_WARNING: "You should check only 1 attachment for editing!",
        FULL_PHONE_LENGTH_INVALID: "Full phone number should be 15 digits or shorter",
        INVALID_PROFILE_PHOTO_FILE: "Only images are allowed for a profile photo",
        REQUIRED_NAME: "Name is required",
        FIELD_IS_TOO_LONG: function (fieldName, maxLength) {
            return fieldName + " is too long. Max length is " + maxLength
        },
        REQUIRED_SURNAME: "Surname is required",
        INVALID_MAIL: "Email is invalid",
        INVALID_SITE: "Site is invalid",
        INVALID_DATE: "Date is invalid",
        INVALID_POSTAL_CODE: "PostalCode is invalid",
        INVALID_COUNTRY_CODE: "Country code is invalid",
        INVALID_OPERATOR_CODE: "Operator code is invalid",
        INVALID_PHONE_NUMBER: "Phone number is invalid"
    },
    URL: {
        contact: "contacts",
        lookups: "lookups",
        profilePhoto: "profilePhotos",
        attachment: "attachments",
        email: "email"
    },
    patterns: {
        email: /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i,
        site: /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/
    }
};