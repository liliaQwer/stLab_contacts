(function (appConstants, contactsController, editContactController) {
    window.onload = function () {
        editContactController.init();
        contactsController.callbacks.onAddContact = function () {
            editContactController.init();
        };
        editContactController.callbacks.onCancel = function () {
            contactsController.init();
        }
    };
})(App.Constants, App.ContactsController, App.EditContactController);