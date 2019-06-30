App.EmailController = function (appConstants, utils) {
    var _messageErrorElement,
        _containerElement,
        _mustacheTemplate,
        _errorMessage,

        _toContactsEmailsElement,
        _textElement,
        _subjectElement,
        _templateElement,

        _sendEmailForm,
        _cancelButton,

        _templateList,
        _emailList;

    function init(contactIdList) {
        _messageErrorElement = document.getElementById("messageError");
        _containerElement = document.getElementById("mainContainer");
        _mustacheTemplate = document.getElementById("sendEmailTemplate").innerHTML;

        _errorMessage = appConstants.messages.ERROR_MESSAGE;
        fetch(appConstants.URL.email + "?ids=" + contactIdList)
            .then(function (response) {
                return response.json();
            }).then(function (data) {
            if (data) {
                _emailList = data.emailList;
                return loadTemplate();
            }
        }).then(function (data) {
            if (data) {
                _templateList = data.templateList;
                render();
            }
        });
    }

    function loadTemplate() {
        return fetch(appConstants.URL.email)
            .then(function (response) {
                return response.json();
            })
    }

    function render() {

        var rendered = Mustache.render(_mustacheTemplate, {
            templateList: _templateList,
            emailList: _emailList
        });
        _containerElement.innerHTML = rendered;

        _toContactsEmailsElement = document.getElementById('to');
        _subjectElement = document.getElementById('subject');
        _templateElement = document.getElementById('template');
        _textElement = document.getElementById('text');

        _sendEmailForm = document.getElementById("sendEmailForm");
        _cancelButton = document.getElementById("cancelButton");

        _cancelButton.onclick = function () {
            cancel();
        };

        _templateElement.onchange = function (e) {
            var templateName = e.target.value;
            if (templateName) {
                _textElement.value = _templateList.filter(function (val) {
                    return val.name == templateName;
                })[0].template;
                _textElement.setAttribute("disabled", "disabled");
            } else {
                _textElement.value = "";
                _textElement.removeAttribute("disabled");
            }
        }

        _sendEmailForm.onsubmit = function (e) {
            e.preventDefault();
            var subject = _subjectElement.value;
            var text = _textElement.value;
            if (text || subject) {
                var data = {
                    emailList: _emailList,
                    subject: subject,
                    text: text,
                    template: _templateElement.value
                }
                fetch(appConstants.URL.email, {
                    method: 'POST',
                    body: JSON.stringify(data)
                })
                    .then(function (response) {
                        if(response.ok){
                            alert(appConstants.messages.SUCCESS_EMAIL_SENDING);
                            cancel();
                        }else {
                            _messageErrorElement.value = +_errorMessage;
                        }
                    })
            }
        };
    }

    function cancel() {
        if (_callbacks.onCancel && typeof _callbacks.onCancel == 'function') {
            _callbacks.onCancel();
        }
    }

    _callbacks = {
        onCancel: false
    };

    return {
        init: init,
        callbacks: _callbacks
    }
}(App.Constants, App.Utils);