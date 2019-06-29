App.EmailController = function (appConstants){
    var _messageErrorElement,
        _containerElement,
        _mustacheTemplate,
        _errorMessage,

        _toContactsEmailsElement,
        _textElement,
        _subjectElement,
        _templateElement,

        _sendEmailForm,

        _templateList,
        _emailList;

    function init(contactIdList) {
        _messageErrorElement = document.getElementById("messageError");
        _containerElement = document.getElementById("mainContainer");
        _mustacheTemplate = document.getElementById("sendEmailTemplate").innerHTML;

        _errorMessage = appConstants.messages.ERROR_MESSAGE;
        fetch(appConstants.URL.email+ "?ids=" + contactIdList)
            .then(function (response){
             return response.json();
        }).then(function(data){
            if (data){
                _emailList = data.emailList;
                return loadTemplate();
            }
        }).then(function (data) {
            if (data){
                _templateList = data;
                render();
            }
        });
    }

    function loadTemplate(){
        return fetch(appConstants.URL.email)
            .then (function (response){
                return response.json();
            })
    }

    function render() {

        var rendered = Mustache.render(_mustacheTemplate, {
            templates: _templateList,
            emailList: _emailList
        });
        _containerElement.innerHTML = rendered;

        _toContactsEmailsElement = document.getElementById('to');
        _subjectElement = document.getElementById('subject');
        _templateElement = document.getElementById('template');
        _textElement = document.getElementById('text');

        _sendEmailForm = document.getElementById("_sendEmailForm");
        _cancelButton = document.getElementById("cancelButton");

        _cancelButton.onclick = function(){
            if (_callbacks.onCancel && typeof _callbacks.onCancel == 'function'){
                _callbacks.onCancel();
            }
        }

        _sendEmailForm.onsubmit = function(e){
            e.preventDefault();

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
        };
    }


    _callbacks = {
        onCancel: false
    };

    return {
        init: init,
        callbacks: _callbacks
    }
}(App.Constants);