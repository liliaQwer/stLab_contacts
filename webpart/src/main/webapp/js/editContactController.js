App.EditContactController = (function (appConstants) {
    var _messageErrorElement,
        _containerElement,
        _mustacheTemplate,
        _cancelButton,
        _contactData,
        _lookupsData,
        _callbacks;

    function init(contactId) {
        _messageErrorElement = document.getElementById("messageError");
        _containerElement = document.getElementById("mainContainer");

        _mustacheTemplate = document.getElementById("contactTemplate").innerHTML;
        _contactData = {
            name: 'lili',
            surname: 'kar',
            patronymic: 'eduardovna',
            birthday: '1986-00-12',
            gender: null,
            maritalStatus: null,
            site: 'www.google.com',
            email: 'lili.qwer@gmail.com',
            company: 'iTechArt',
            nationality: 'belarus',
            addressInfo: {
                country: 'belarus',
                city: 'minsk',
                street: 'prospect nezavisimosti',
                postalCode: '220140'
            },
            phoneInfo: {
                phonesList: [{id: 1, phoneNumber: "+375447319525", phoneType: "Home", comment: 'text'}],
                hasPhones: true
            }
        }
        loadLookups()
            .then(function (data) {
                if (contactId) {
                    return loadContactInfo(contactId);
                }
                return data;
            })
            .then(function () {
                render();
            });
    }

    function render() {
        var rendered = Mustache.render(_mustacheTemplate, Object.assign({
            genderSelected: function () {
                if (this.id === _contactData.gender) {
                    return "selected";
                }
                return '';
            },
            maritalStatusSelected: function () {
                if (this.id === _contactData.maritalStatus) {
                    return "selected";
                }
                return '';
            }
        }, _contactData, _lookupsData));
        _containerElement.innerHTML = rendered;
        assignEvents();
    }

    function loadLookups() {
        var tempData = {
            genderList: [
                {id: 1, name: "Man"},
                {id: 2, name: "Woman"}
            ],
            maritalStatusList: [
                {id: 1, name: "Married"},
                {id: 2, name: "Single"},
                {id: 3, name: "Divorced"}
            ]
        };
        _lookupsData = tempData;
        return Promise.resolve(tempData);
        return fetch(appConstants.URL.lookups).then(function (response) {
            _lookupsData = response.json();
            return _lookupsData;
        })
    }

    function loadContactInfo(contactId) {
        return fetch(appConstants.URL.contactInfo + "?id=" + contactId)
            .then(function (response) {
                _contactData = response.json();
                return _contactData;
            })
    }

    function assignEvents() {
        _cancelButton = document.getElementById('cancelButton');
        _cancelButton.onclick = function () {
            if (_callbacks.onCancel && typeof _callbacks.onCancel === "function") {
                _callbacks.onCancel();
            }
        }
    }

    _callbacks = {
        onCancel: false
    };

    return {
        init: init,
        callbacks: _callbacks
    }
})(App.Constants);