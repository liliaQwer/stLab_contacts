var editContactController = (function (appConstants) {
    var _messageErrorElement,
        _containerElement,
        _mustacheTemplate,
        _cancelButton,
        _callbacks;

    function init(contactId) {
        _messageErrorElement = document.getElementById("messageError");
        _containerElement = document.getElementById("mainContainer");

        _mustacheTemplate = document.getElementById("contactTemplate").innerHTML;
        render();
    }

    function render() {
        var rendered = Mustache.render(_mustacheTemplate, {});
        _containerElement.innerHTML = rendered;
        assignEvents();
    }

    function assignEvents() {
        _cancelButton = document.getElementById('cancelButton');
        _cancelButton.onclick = function () {
            if (_callbacks.onCancel && typeof _callbacks.onCancel === "function") {
                _callbacks.onCancel();
            }
        }
    }

    var _callbacks = {
        onCancel: false
    }


    return {
        init: init,
        callbacks: _callbacks
    }
})(appConstants);