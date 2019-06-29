(function (appConstants, contactsController, editContactController, editPhoneController, editAttachmentController,
           editProfilePhotoController, searchController, emailController) {
    window.onload = function () {
        contactsController.init();
        contactsController.render();
        contactsController.callbacks.onAddContact = function (contactId) {
            editContactController.init(contactId);
        };
        contactsController.callbacks.onSearchContact = function(){
            searchController.init();
        };
        contactsController.callbacks.onSendEmail = function(data){
            emailController.init(data);
        };
        editContactController.callbacks.onCancel = function () {
            contactsController.render();
        };
        editContactController.callbacks.onAddPhone = function (phoneData, settings) {
            editPhoneController.init(phoneData, settings);
            editPhoneController.showEditor();
        };
        editContactController.callbacks.onChangeProfilePhoto = function () {
            editPhoneController.init();
            editPhoneController.showEditor();
        };
        editPhoneController.callbacks.onSavePhone = function (data) {
            editContactController.updatePhone(data);
        };
        editContactController.callbacks.onAddAttachment = function (attachData) {
            editAttachmentController.init(attachData);
            editAttachmentController.showEditor();
        };
        editContactController.callbacks.onChangeProfilePhoto = function () {
            editProfilePhotoController.init();
            editProfilePhotoController.showEditor();
        };
        emailController.callbacks.onCancel = function(){
            contactsController.render();
        };
        editAttachmentController.callbacks.onSaveAttachment = function (data) {
            editContactController.updateAttachment(data);
        };
        editProfilePhotoController.callbacks.onSavePhoto = function (data) {
            editContactController.updatePhoto(data);
        };
        searchController.callbacks.onCancel = function(){
            contactsController.render();
        };
        searchController.callbacks.onSearch = function(data){
            contactsController.render(data);
        }
    };
})(App.Constants, App.ContactsController, App.EditContactController, App.EditPhoneController,
    App.EditAttachmentController, App.EditProfilePhotoController, App.SearchController, App.EmailController);