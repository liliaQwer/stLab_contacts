App.LookupRepository = (function (appConstants) {
    function getLookups(){
        return fetch(appConstants.URL.lookups).then(function (response) {
            return response.json();
        });
    }
    return {
        getLookups: getLookups
    }
})(App.Constants);