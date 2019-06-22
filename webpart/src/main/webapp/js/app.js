(function (appConstants, contactsController, editContactController, editPhoneController, editAttachmentController, editProfilePhotoController) {
    window.onload = function () {
        editContactController.init();
        contactsController.callbacks.onAddContact = function () {
            editContactController.init();
        };
        editContactController.callbacks.onCancel = function () {
            contactsController.init();
        }
        editContactController.callbacks.onAddPhone = function (phoneData, settings) {
            editPhoneController.init(phoneData, settings);
            editPhoneController.showEditor();
        }
        editContactController.callbacks.onChangeProfilePhoto = function () {
            editPhoneController.init();
            editPhoneController.showEditor();
        }
        editPhoneController.callbacks.onSavePhone = function (data) {
            editContactController.updatePhone(data);
        }
        editContactController.callbacks.onAddAttachment = function (attachData) {
            editAttachmentController.init(attachData);
            editAttachmentController.showEditor();
        }

        editContactController.callbacks.onChangeProfilePhoto = function () {
            editProfilePhotoController.init();
            editProfilePhotoController.showEditor();
        }

        editAttachmentController.callbacks.onSaveAttachment = function (data) {
            editContactController.updateAttachment(data);
        }

        editProfilePhotoController.callbacks.onSavePhoto = function (data) {
            editContactController.updatePhoto(data);
        }
    };
})(App.Constants, App.ContactsController, App.EditContactController, App.EditPhoneController, App.EditAttachmentController, App.EditProfilePhotoController);