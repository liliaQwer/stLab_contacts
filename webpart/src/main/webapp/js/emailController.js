App.EmailController = function (appConstants, utils) {
    var _messageErrorElement,
        _containerElement,
        _mustacheTemplate,
        _errorMessage,
        _loaderContainer,
        _loaderModal,
        _toContactsEmailsElement,
        _textElement,
        _subjectElement,
        _templateElement,

        _sendEmailForm,
        _cancelButton,

        _templateList,
        _emailList,
        _callbacks;

    function init(contactIdList) {
        _messageErrorElement = document.getElementById("messageError");
        _containerElement = document.getElementById("mainContainer");
        _mustacheTemplate = document.getElementById("sendEmailTemplate").innerHTML;
        _loaderContainer = document.getElementById("loaderContainer");
        _loaderModal = new Modal(_loaderContainer, {
            closeOnEscape: false,
            closeOnClickOutside: false}
            );

        hideErrorMessage();

        _errorMessage = appConstants.messages.ERROR_MESSAGE;
        fetch(appConstants.URL.email + "?ids=" + contactIdList)
            .then(utils.handleError)
            .then(function (data) {
                if (data) {
                    _emailList = data.emailList;
                    return loadTemplate();
                }
            })
            .then(function (data) {
                if (data) {
                    _templateList = data.templateList;
                    render();
                }
            })
            .catch(function (error) {
                showMessageError(error || appConstants.messages.ERROR_MESSAGE);
            })
    }

    function loadTemplate() {
        return fetch(appConstants.URL.email)
            .then(utils.handleError)
            .catch(function (error) {
                showMessageError(error || appConstants.messages.ERROR_MESSAGE);
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
        };

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
                };
                _loaderModal.show();
                fetch(appConstants.URL.email, {
                    method: 'POST',
                    body: JSON.stringify(data)
                })
                    .then(utils.handleError)
                    .then(function (data) {
                        _loaderModal.hide();
                        setTimeout(function () {
                            alert(data.message);
                            cancel();
                        }, 1);

                    })
                    .catch(function (error) {
                        _loaderModal.hide();
                        showMessageError(error || appConstants.messages.ERROR_MESSAGE);
                    });

            }
        };
    }

    function showMessageError(error) {
        _messageErrorElement.classList.remove('hidden');
        _messageErrorElement.innerText = error;
    }

    function hideErrorMessage() {
        _messageErrorElement.classList.add('hidden');
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