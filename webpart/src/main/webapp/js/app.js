(function (appConstants, router, contactsController, editContactController, editPhoneController, editAttachmentController,
           editProfilePhotoController, searchController, emailController) {

    // Listen on hash change:
    window.addEventListener('hashchange', router.process);

    window.onload = function () {
        router.init();

        contactsController.callbacks.onSendEmail = function (data) {
            emailController.setData(data);
            window.location = '#' + appConstants.HASH_URL.email;
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

        editAttachmentController.callbacks.onSaveAttachment = function (data) {
            editContactController.updateAttachment(data);
        };

        editProfilePhotoController.callbacks.onSavePhoto = function (data) {
            editContactController.updatePhoto(data);
        };

        searchController.callbacks.onSearch = function (data) {
            contactsController.setFilters(data);
            window.location = '#' + appConstants.HASH_URL.contacts;
        }
    };
})(App.Constants, App.Router, App.ContactsController, App.EditContactController, App.EditPhoneController,
    App.EditAttachmentController, App.EditProfilePhotoController, App.SearchController, App.EmailController);