App.SearchController = (function (appConstants, appLookup, appUtils) {
    var _containerElement,
        _mustacheTemplate,
        _messageErrorElement,
        _searchForm,
        _submitSearchFormButton,
        _cancelButton,
        _nameElement,
        _surnameElement,
        _patronymicElement,
        _birthdayElement,
        _genderElement,
        _marital_statusElement,
        _nationalityElement,
        _callbacks,

        /* address info elements */
        _countryElement,
        _cityElement,
        _streetElement,
        _postalCodeElement,

        _lookupsData,
        _contactData;

    function init() {
        _containerElement = document.getElementById("mainContainer");
        _mustacheTemplate = document.getElementById("searchTemplate").innerHTML;
        _messageErrorElement = document.getElementById("messageError");

        appLookup.getLookups()
            .then(function (data) {
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
        _submitSearchFormButton = document.getElementById("submitSearchFormButton");
        _cancelButton = document.getElementById("cancelButton");

        _searchForm.onsubmit = function (e) {
            e.preventDefault();
            var validationResult = validateData();
            if (!validationResult.isValid){
                showMessageError(validationResult.errorList.join(", "));
                return;
            }

            _contactData = {
                pageSize: appConstants.PAGE_SIZE_DEFAULT,
                pageNumber: appConstants.PAGE_NUMBER_DEFAULT,
                name: _nameElement.value,
                surname: _surnameElement.value,
                patronymic: _patronymicElement.value,
                birthday: _birthdayElement.value,
                gender: _genderElement.value,
                maritalStatus: _marital_statusElement.value,
                nationality: _nationalityElement.value,
                country: _countryElement.value,
                city: _cityElement.value,
                street: _streetElement.value,
                postalCode: _postalCodeElement.value
            }

            if (_callbacks.onSearch && typeof _callbacks.onSearch == 'function') {
                _callbacks.onSearch(_contactData);
            }
        };

        _cancelButton.onclick = function () {
            if (_callbacks.onCancel && typeof _callbacks.onCancel == 'function') {
                _callbacks.onCancel();
            }
        }
    }

    function showMessageError(error) {
        _messageErrorElement.classList.remove('hidden');
        _messageErrorElement.innerText = error;
    }

    function validateData(){
        var validationResult = {
            errorList: [],
            isValid: false
        }
        if (_postalCodeElement.value && isNaN(_postalCodeElement.value)){
            validationResult.errorList.push(appConstants.messages.INVALID_POSTAL_CODE);
        }

        if (_birthdayElement.value && !appUtils.isValidDate(_birthdayElement.value)){
            validationResult.errorList.push(appConstants.messages.INVALID_DATE)
        }
        if (validationResult.errorList.length == 0){
            validationResult.isValid = true;
        }
        return validationResult;
    }

    _callbacks = {
        onCancel: false,
        onSearch: false
    };

    return {
        init: init,
        callbacks: _callbacks
    }
}(App.Constants, App.LookupRepository, App.Utils));