App.EditAttachmentController = (function (appConstants, utils) {
    var _containerElement,
        _mustacheTemplate,
        _modalContainer,
        _modal,
        _attachmentData,
        _form,
        _fileUploadElement,
        _commentElement,
        _uploadedFile,
        _callbacks;
    var _nextAttachId = -1;

    function init(attachData) {
        _containerElement = document.getElementById("editAttachmentContainer");
        _mustacheTemplate = document.getElementById("editAttachmentTemplate").innerHTML;
        _attachmentData = attachData
            ? utils.merge({}, attachData, {isNew: false})
            : {
                id: _nextAttachId--,
                isNew: true,
                fileName: null,
                comment: null,
                uploadDate: null,
                uploadedFile: null
            };
        render();
    }

    function render() {
        var rendered = Mustache.render(_mustacheTemplate, utils.merge({
            modalTitle: _attachmentData.isNew ? 'Add attachment' : 'Edit attachment'
        }, _attachmentData));
        _containerElement.innerHTML = rendered;
        _modalContainer = document.getElementById("editAttachmentModal");
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
        _form = document.getElementById('editAttachmentForm');
        _commentElement = document.getElementById('attachComment');
        _fileUploadElement = document.getElementById('fileUpload');

        _fileUploadElement.onchange = function (e) {
            var files = e.target.files;
            _uploadedFile = files[0];
        }

        _form.onsubmit = function (e) {
            e.preventDefault();
            var today = new Date();
            var date = today.getDate();
            var year = today.getFullYear();
            var month = today.getMonth() + 1;
            month = (month < 10)  ? '0' + month : month;
            var formattedDate = year + "-" + month + "-" + date;
            var attachmentData = utils.merge({}, _attachmentData, {
                uploadDate: formattedDate,
                comment: _commentElement.value
            });

            if (_uploadedFile) {
                attachmentData.uploadedFile = _uploadedFile;
                attachmentData.fileName = _uploadedFile.name;
            }

            if (_callbacks.onSaveAttachment && typeof _callbacks.onSaveAttachment === "function") {
                _callbacks.onSaveAttachment(attachmentData);
            }
            hideModal();
        };
    }

    _callbacks = {
        onSaveAttachment: false
    };

    return {
        init: init,
        showEditor: showModal,
        callbacks: _callbacks
    }
})(App.Constants, App.Utils);