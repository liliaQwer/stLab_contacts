(function (appConstants, contactsController) {
    window.onload = function () {
        contactsController.init();
        contactsController.callbacks.onAddContact = function () {
            editContactController.init();
        };
        editContactController.callbacks.onCancel = function () {
            contactsController.init();
        }
    };
})(appConstants, contactsController, editContactController);