App.SearchController = (function(appConstants){
    var _containerElement,
        _mustacheTemplate,
        _searchForm,
        _submitButton,
        _nameElement,
        _surnameElement,
        _patronymicElement,
        _birthdayElement,
        _genderElement,
        _marital_statusElement,
        _nationalityElement,

        /* address info elements */
        _countryElement,
        _cityElement,
        _streetElement,
        _postalCodeElement,

        _lookupsData;

    function init(){
        _containerElement = document.getElementById("mainContainer");
        _mustacheTemplate = document.getElementById("searchTemplate").innerHTML;

        fetch(appConstants.URL.lookups).then(function (response) {
            return response.json();
        }).then(function (data) {
            _lookupsData = data;
            render();
        });
    }

    function render() {
        var rendered = Mustache.render(_mustacheTemplate, _lookupsData);
        _containerElement.innerHTML = rendered;

        _nameElement = document.getElementById('name');
        _surnameElement = document.getElementById('surname');
        _patronymicElement = document.getElementById('patronymic');
        _birthdayElement = document.getElementById('birthday');
        _genderElement = document.getElementById('gender');
        _marital_statusElement = document.getElementById('marital_status');
        _nationalityElement = document.getElementById('nationality');

        _countryElement = document.getElementById('country');
        _cityElement = document.getElementById('city');
        _streetElement = document.getElementById('street');
        _postalCodeElement = document.getElementById('postalCode');

        _searchForm = document.getElementById("searchForm");
        _submitButton = document.getElementById("submitButton");
        _searchForm.onsubmit = function(e){
            e.preventDefault();
        }
    }


    return {
        init: init
    }
}(App.Constants));