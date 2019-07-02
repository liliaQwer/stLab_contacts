App.LookupRepository = (function (appConstants, utils) {
    function getLookups() {
        return fetch(appConstants.URL.lookups).then(utils.handleError);
    }

    return {
        getLookups: getLookups
    }
})(App.Constants, App.Utils);