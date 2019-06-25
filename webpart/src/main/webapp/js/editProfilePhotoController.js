App.EditProfilePhotoController = (function (appConstants) {
    var _containerElement,
        _mustacheTemplate,
        _messageErrorElement,
        _modalContainer,
        _modal,
        _profilePhotoData,
        _fileUploadElement,
        _callbacks,
        _saveButton;

    function init() {
        _containerElement = document.getElementById("editProfilePhotoContainer");
        _mustacheTemplate = document.getElementById("editProfilePhotoTemplate").innerHTML;
        _profilePhotoData = {
            uploadedFile: null
        };
        render();
    }

    function render() {
        var rendered = Mustache.render(_mustacheTemplate, _profilePhotoData);
        _containerElement.innerHTML = rendered;
        _modalContainer = document.getElementById("editProfilePhotoModal");
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
        _fileUploadElement = document.getElementById('photoUpload');
        _messageErrorElement = document.getElementById('profilePhotoEditorMessageError');
        _saveButton = document.getElementById("submitProfilePhotoFormButton");

        _fileUploadElement.onchange = function (e) {
            hideErrorMessage();
            var files = e.target.files;
            var f = files[0];
            if (f && !f.type.match('image.*')) {
                showMessageError(appConstants.messages.INVALID_PROFILE_PHOTO_FILE)
                return;
            }
            _profilePhotoData.uploadedFile = f;
            _profilePhotoData.fileName = f.name;
        }

        _saveButton.onclick = function () {
            if (!_profilePhotoData.uploadedFile) {
                showMessageError(appConstants.messages.SELECT_PHOTO_WARNING);
                return;
            }
            if (_callbacks.onSavePhoto && typeof _callbacks.onSavePhoto === "function") {
                _callbacks.onSavePhoto(_profilePhotoData);
            }
            hideModal();
        }
    }

    function showMessageError(error) {
        _messageErrorElement.classList.remove('hidden');
        _messageErrorElement.innerText = error;
    }

    function hideErrorMessage() {
        _messageErrorElement.classList.add('hidden');
    }

    _callbacks = {
        onSavePhoto: false
    };

    return {
        init: init,
        showEditor: showModal,
        callbacks: _callbacks
    }
})(App.Constants);