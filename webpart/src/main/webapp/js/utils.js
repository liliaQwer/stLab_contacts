App.Utils = (function (appConstants) {
    function mergeObjects() {
        var resObj = {};
        for (var i = 0; i < arguments.length; i += 1) {
            var obj = arguments[i],
                keys = Object.keys(obj);
            for (var j = 0; j < keys.length; j += 1) {
                resObj[keys[j]] = obj[keys[j]];
            }
        }
        return resObj;
    }

    function encodeQueryString(params) {
        var keys = Object.keys(params);
        var str = keys.length
            ? "?" + keys.map(function (key) {
            return encodeURIComponent(key) + "=" + encodeURIComponent(params[key]);
        }).join("&")
            : "";
        console.log(str);
        return str;
    }

    function handleError(response) {
        var json = response.json();
        if (!response.ok) {
            return json.then(function (data) {
                throw new Error(data.message);
            })
        }
        return json;
    }

    function isValidDate(dateString){
        var regex_date = /^\d{4}\-\d{1,2}\-\d{1,2}$/;
        if(!regex_date.test(dateString)){
            return false;
        }
        var parts   = dateString.split("-");
        var day     = parseInt(parts[2], 10);
        var month   = parseInt(parts[1], 10);
        var year    = parseInt(parts[0], 10);
         if(year < 1000 || year > 3000 || month == 0 || month > 12){
            return false;
        }
        var monthLength = [ 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 ];
        if(year % 400 == 0 || (year % 100 != 0 && year % 4 == 0)){
            monthLength[1] = 29;
        }return day > 0 && day <= monthLength[month - 1];
    }

    function validateLength(elem, validationResult, maxLength, fieldName) {
        if (elem.value.length > maxLength) {
            validationResult.errorList.push(appConstants.messages.FIELD_IS_TOO_LONG(fieldName, maxLength));
        }
    }

    function parseRequestURL(){
        var url = location.hash.slice(1).toLowerCase() || '/';
        var r = url.split("/");
        var request = {
            resource: null,
            params: null
        };
        request.resource = r[1];
        request.params = r[2];
        return request;
    }

    return {
        merge: mergeObjects,
        encodeQueryString: encodeQueryString,
        handleError: handleError,
        isValidDate: isValidDate,
        validateLength: validateLength,
        parseRequestURL: parseRequestURL
    }

})(App.Constants);