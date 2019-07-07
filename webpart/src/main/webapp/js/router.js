App.Router = (function (constants, utils, appContactsController, appEditController, appSearchController, appEmailController) {
    var routes = {};
    routes[constants.HASH_URL.contacts] = appContactsController;
    routes[constants.HASH_URL.editContact] = appEditController;
    routes[constants.HASH_URL.addContact] = appEditController;
    routes[constants.HASH_URL.search] = appSearchController;
    routes[constants.HASH_URL.email] = appEmailController;
    var defaultRoute = constants.HASH_URL.contacts;

    function init() {
        appContactsController.init();
        process();
    }

    function process() {
        var request = utils.parseRequestURL();

        // Parse the URL and if it has an id part, change it with the string "id"
        var parsedURL = (request.resource ? '/' + request.resource : '/') + (request.params ? '/id' : '');

        // Get the page from our hash of supported routes.
        // If the parsed URL is not in our list of supported routes, select the 404 page instead
        var page = routes[parsedURL] ? routes[parsedURL] : routes[defaultRoute];
        page.render(request.params);
    }

    return {
        init: init,
        process: process
    }

})(App.Constants, App.Utils, App.ContactsController, App.EditContactController, App.SearchController, App.EmailController);