App.EditPhoneController = (function (appConstants, utils) {
    var _messageErrorElement,
        _containerElement,
        _mustacheTemplate,
        _modalContainer,
        _modal,
        _phoneData,
        _lookupsData,
        _form,
        _countryCodeElement,
        _operatorCodeElement,
        _phoneNumberElement,
        _phoneTypeElement,
        _commentElement,
        _callbacks;
    var _nextPhoneId = -1;

    function init(phoneData, settings) {
        _containerElement = document.getElementById("editPhoneContainer");
        _mustacheTemplate = document.getElementById("editPhoneTemplate").innerHTML;
        _phoneData = phoneData
            ? utils.merge({}, phoneData, {isNew: false})
            : {
                id: _nextPhoneId--,
                isNew: true,
                countryCode: null,
                operatorCode: null,
                phoneNumber: null,
                phoneType: null,
                comment: null
            };
        _lookupsData = (settings && settings.lookupsData) ? settings.lookupsData : {};
        render();
    }

    function render() {
        var rendered = Mustache.render(_mustacheTemplate, utils.merge({
            modalTitle: _phoneData.isNew ? 'Add phone' : 'Edit phone',
            phoneTypesSelected: function () {
                if (this.id == _phoneData.phoneType) {
                    return "selected";
                }
                return '';
            }
        }, _phoneData, _lookupsData));
        _containerElement.innerHTML = rendered;
        _modalContainer = document.getElementById("editPhoneModal");
        _modal = new Modal(_modalContainer);
    }

    function showModal() {
        _modal.show();
        assignEvents();
    }

    function hideModal() {
        _modal.hide();
    }

    function assignEvents() {
        _form = document.getElementById('editPhoneForm');
        _messageErrorElement = document.getElementById("phoneEditorMessageError");
        _countryCodeElement = document.getElementById('countryCode');
        _operatorCodeElement = document.getElementById('operatorCode');
        _phoneNumberElement = document.getElementById('phoneNumber');
        _phoneTypeElement = document.getElementById('phoneType');
        _commentElement = document.getElementById('comment');

        _form.onsubmit = function (e) {
            e.preventDefault();

            var phoneData = utils.merge({}, _phoneData, {
                countryCode: _countryCodeElement.value,
                operatorCode: _operatorCodeElement.value,
                phoneNumber: _phoneNumberElement.value,
                phoneType: _phoneTypeElement.value,
                comment: _commentElement.value
            });
            var fullNumber = phoneData.countryCode + "" + phoneData.operatorCode + "" + phoneData.phoneNumber;
            if (fullNumber.length > 15) {
                showMessageError(appConstants.messages.FULL_PHONE_LENGTH_INVALID);
                return;
            }
            if (_callbacks.onSavePhone && typeof _callbacks.onSavePhone === "function") {
                _callbacks.onSavePhone(phoneData);
            }
            hideModal();
        };
    }

    function showMessageError(error) {
        _messageErrorElement.classList.remove('hidden');
        _messageErrorElement.innerText = error;
    }

    _callbacks = {
        onSavePhone: false
    };

    return {
        init: init,
        showEditor: showModal,
        callbacks: _callbacks
    }
})(App.Constants, App.Utils);