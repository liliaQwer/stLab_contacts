App.Utils = (function () {
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
            ? "?" + keys.map(function(key){
                    return encodeURIComponent(key) + "=" + encodeURIComponent(params[key]);
                }).join("&")
            : "";
        console.log(str);
        return str;
    }

    return {
        merge: mergeObjects,
        encodeQueryString: encodeQueryString
    }
})();