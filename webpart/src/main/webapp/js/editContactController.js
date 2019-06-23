App.EditContactController = (function (appConstants, utils) {
    var _messageErrorElement,
        _containerElement,
        _mustacheTemplate,

        /* controls  buttons */
        _addPhoneButton,
        _editPhoneButton,
        _deletePhoneButton,
        _addAttachmentButton,
        _editAttachmentButton,
        _deleteAttachmentButton,

        /* contact info elements */
        _profilePhotoElement,
        _nameElement,
        _surnameElement,
        _patronymicElement,
        _birthdayElement,
        _genderElement,
        _marital_statusElement,
        _siteElement,
        _emailElement,
        _companyElement,
        _nationalityElement,

        /* address info elements */
        _countryElement,
        _cityElement,
        _streetElement,
        _postalCodeElement,

        _cancelButton,

        _contactData,
        _contactForm,
        _lookupsData,
        _callbacks;

    function init(contactId) {
        _messageErrorElement = document.getElementById("messageError");
        _containerElement = document.getElementById("mainContainer");
        _mustacheTemplate = document.getElementById("contactTemplate").innerHTML;
        _contactData = {
            name: null,
            surname: null,
            patronymic: null,
            birthday: null,
            gender: null,
            maritalStatus: null,
            site: null,
            email: null,
            company: null,
            nationality: null,
            profilePhoto: {
                uploadedFile: null,
                imgSrc: "img/no-user.jpg"
            },
            addressInfo: {
                country: null,
                city: null,
                street: null,
                postalCode: null
            },
            phoneInfo: {
                phonesList: [],
                hasPhones: false,
                deletedIds: [],
                updatedIds: []
            },
            attachmentsInfo: {
                attachmentsList: [],
                hasAttachments: false,
                deletedIds: [],
                updatedIds: []
            }
        };
        loadLookups()
            .then(function (data) {
                _lookupsData = data;
                if (contactId) {
                    return loadContactInfo(contactId);
                }
            })
            .then(function () {
                render(true);
            });
    }

    function render(isInitialRender) {
        if (!isInitialRender) {
            refreshContactData();
        }
        var rendered = Mustache.render(_mustacheTemplate, utils.merge({
            genderSelected: function () {
                if (this.id == _contactData.gender) {
                    return "selected";
                }
                return '';
            },
            maritalStatusSelected: function () {
                if (this.id == _contactData.maritalStatus) {
                    return "selected";
                }
                return '';
            }
        }, _contactData, _lookupsData));
        _containerElement.innerHTML = rendered;
        assignEvents();
    }

    function loadLookups() {
        return fetch(appConstants.URL.lookups).then(function (response) {
            return response.json();
        });
    }

    function loadContactInfo(contactId) {
        return fetch(appConstants.URL.contact + "/" + contactId)
            .then(function (response) {
                return response.json();
            })
            .then(function (data) {
                _contactData = utils.merge({}, _contactData, data.contact, {
                    addressInfo: data.address,
                    attachmentsInfo: {
                        attachmentsList: data.attachmentList || [],
                        hasAttachments: data.attachmentList && data.attachmentList.length,
                        deletedIds: [],
                        updatedIds: []
                    },
                    phoneInfo: {
                        phonesList: data.phoneList || [],
                        hasPhones: data.phoneList && data.phoneList.length,
                        deletedIds: [],
                        updatedIds: []
                    }
                });
                return _contactData;
            });

    }

    function updatePhone(data) {
        if (!data) {
            return;
        }
        var phoneType = _lookupsData.phoneTypesList.filter(function (phType) {
            return phType.id == data.phoneType
        })[0];

        var phoneData = utils.merge({}, data, {
            phoneTypeText: phoneType ? phoneType.description : null
        });
        var phonesList = _contactData.phoneInfo.phonesList;
        var existingPhone = phonesList.filter(function (phone) {
            return phone.id == data.id;
        })[0];
        if (existingPhone) {
            var updatedPhone = utils.merge({}, existingPhone, phoneData);
            phonesList[phonesList.indexOf(existingPhone)] = updatedPhone;
        } else {
            var newPhone = utils.merge({}, phoneData);
            phonesList.push(newPhone);
        }
        _contactData.phoneInfo.hasPhones = phonesList.length > 0;
        if (data.id > 0 && _contactData.phoneInfo.updatedIds.indexOf(data.id) === -1) {
            _contactData.phoneInfo.updatedIds.push(data.id);
        }
        render();
    }

    function updateAttachment(data) {
        if (!data) {
            return;
        }
        var attachmentList = _contactData.attachmentsInfo.attachmentsList;
        var existingAttach = attachmentList.filter(function (attach) {
            return attach.id == data.id;
        })[0];
        if (existingAttach) {
            var updatedAttach = utils.merge({}, existingAttach, data);
            attachmentList[attachmentList.indexOf(existingAttach)] = updatedAttach;
        } else {
            var newAttach = utils.merge({}, data);
            attachmentList.push(newAttach);
        }
        _contactData.attachmentsInfo.hasAttachments = attachmentList.length > 0;
        if (data.id > 0 && _contactData.attachmentsInfo.updatedIds.indexOf(data.id) === -1) {
            _contactData.phoneInfo.updatedIds.push(data.id);
        }
        render();
    }

    function editPhone(phoneId) {
        var phoneToEdit = _contactData.phoneInfo.phonesList.filter(function (phone) {
            return phone.id == phoneId;
        })[0];

        if (phoneToEdit) {
            if (_callbacks.onAddPhone && typeof _callbacks.onAddPhone === 'function') {
                _callbacks.onAddPhone(phoneToEdit, {
                    lookupsData: {
                        phoneTypesList: _lookupsData.phoneTypesList
                    }
                })
            }
        }
    }

    function editAttachment(attachId) {
        var attachToEdit = _contactData.attachmentsInfo.attachmentsList.filter(function (attach) {
            return attach.id == attachId;
        })[0];
        if (attachToEdit) {
            if (_callbacks.onAddAttachment && typeof _callbacks.onAddAttachment === 'function') {
                _callbacks.onAddAttachment(attachToEdit)
            }
        }
    }

    function deletePhones(phoneIds) {
        var remainingPhones = _contactData.phoneInfo.phonesList.filter(function (phone) {
            return phoneIds.indexOf(phone.id) === -1
        });
        var markedForDeletion = phoneIds.filter(function (id) {
            return id > 0;
        });
        _contactData.phoneInfo.phonesList = remainingPhones;
        _contactData.phoneInfo.hasPhones = remainingPhones.length > 0;
        _contactData.phoneInfo.deletedIds.concat(markedForDeletion);
        _contactData.phoneInfo.updatedIds = _contactData.phoneInfo.updatedIds.filter(function (updatedId) {
            return markedForDeletion.indexOf(updatedId) === -1;
        });
        render();
    }

    function deleteAttachments(ids) {
        var remainingAttachments = _contactData.attachmentsInfo.attachmentsList.filter(function (att) {
            return ids.indexOf(att.id) === -1;
        });
        var markedForDeletion = ids.filter(function (id) {
            return id > 0;
        });
        _contactData.attachmentsInfo.attachmentsList = remainingAttachments;
        _contactData.attachmentsInfo.hasAttachments = remainingAttachments.length > 0;
        _contactData.attachmentsInfo.deletedIds.concat(markedForDeletion);
        _contactData.attachmentsInfo.updatedIds = _contactData.attachmentsInfo.updatedIds.filter(function (updatedId) {
            return markedForDeletion.indexOf(updatedId) === -1;
        });
        render();
    }

    function updatePhoto(data) {
        if (!data) {
            return;
        }
        var f = data.uploadedFile;
        var reader = new FileReader();
        reader.onloadend = function () {
            _contactData.profilePhoto.imgSrc = reader.result;
            render();
        };
        reader.readAsDataURL(f);
        _contactData.profilePhoto.uploadedFile = data.uploadedFile;
    }

    function assignEvents() {
        _addPhoneButton = document.getElementById('addPhone');
        _editPhoneButton = document.getElementById('editPhone');
        _deletePhoneButton = document.getElementById('removePhone');

        _addAttachmentButton = document.getElementById('addAttachment');
        _editAttachmentButton = document.getElementById('editAttachment');
        _deleteAttachmentButton = document.getElementById('removeAttachment');

        _profilePhotoElement = document.getElementById('profilePhoto');
        _nameElement = document.getElementById('name');
        _surnameElement = document.getElementById('surname');
        _patronymicElement = document.getElementById('patronymic');
        _birthdayElement = document.getElementById('birthday');
        _genderElement = document.getElementById('gender');
        _marital_statusElement = document.getElementById('marital_status');
        _siteElement = document.getElementById('site');
        _emailElement = document.getElementById('email');
        _companyElement = document.getElementById('company');
        _nationalityElement = document.getElementById('nationality');

        _countryElement = document.getElementById('country');
        _cityElement = document.getElementById('city');
        _streetElement = document.getElementById('street');
        _postalCodeElement = document.getElementById('postalCode');

        _contactForm = document.getElementById("contactForm");

        _cancelButton = document.getElementById('cancelButton');

        _addPhoneButton.onclick = function () {
            if (_callbacks.onAddPhone && typeof _callbacks.onAddPhone === 'function') {
                _callbacks.onAddPhone(null, {
                    lookupsData: {
                        phoneTypesList: _lookupsData.phoneTypesList
                    }
                })
            }
        }

        if (_deletePhoneButton) {
            _deletePhoneButton.onclick = function () {
                var inputElements = document.getElementsByClassName('phone_check');
                var idList = new Array();
                for (var i = 0; inputElements[i]; ++i) {
                    if (inputElements[i].checked) {
                        idList.push(parseInt(inputElements[i].value));
                    }
                }
                if (idList.length == 0) {
                    alert(appConstants.messages.SELECT_PHONE_WARNING);
                    return;
                }
                if (!window.confirm(appConstants.messages.DELETE_PHONE_CONFIRMATION)) {
                    return;
                }
                deletePhones(idList);
            }
        }
        if (_editPhoneButton) {
            _editPhoneButton.onclick = function () {
                var inputElements = document.getElementsByClassName('phone_check');
                var idList = new Array();
                for (var i = 0; inputElements[i]; ++i) {
                    if (inputElements[i].checked) {
                        idList.push(parseInt(inputElements[i].value));
                    }
                }
                if (idList.length == 0) {
                    alert(appConstants.messages.SELECT_PHONE_WARNING);
                    return;
                }
                if (idList.length > 1) {
                    alert(appConstants.messages.SELECT_SINGLE_PHONE_WARNING);
                    return;
                }
                editPhone(idList[0]);
            }
        }

        _addAttachmentButton.onclick = function () {
            if (_callbacks.onAddAttachment && typeof _callbacks.onAddAttachment === 'function') {
                _callbacks.onAddAttachment(null);
            }
        }
        if (_deleteAttachmentButton) {
            _deleteAttachmentButton.onclick = function () {
                var inputElements = document.getElementsByClassName('attachment_check');
                var idList = new Array();
                for (var i = 0; inputElements[i]; ++i) {
                    if (inputElements[i].checked) {
                        idList.push(parseInt(inputElements[i].value));
                    }
                }
                if (idList.length == 0) {
                    alert(appConstants.messages.SELECT_ATTACHMENT_WARNING);
                    return;
                }
                if (!window.confirm(appConstants.messages.DELETE_ATTACHMENT_CONFIRMATION)) {
                    return;
                }
                deleteAttachments(idList);
            }
        }
        if (_editAttachmentButton) {
            _editAttachmentButton.onclick = function () {
                var inputElements = document.getElementsByClassName('attachment_check');
                var idList = new Array();
                for (var i = 0; inputElements[i]; ++i) {
                    if (inputElements[i].checked) {
                        idList.push(parseInt(inputElements[i].value));
                    }
                }
                if (idList.length == 0) {
                    alert(appConstants.messages.SELECT_ATTACHMENT_WARNING);
                    return;
                }
                if (idList.length > 1) {
                    alert(appConstants.messages.SELECT_SINGLE_ATTACHMENT_WARNING);
                    return;
                }
                editAttachment(idList[0]);
            }
        }

        _profilePhotoElement.onclick = function () {
            if (_callbacks.onChangeProfilePhoto && typeof _callbacks.onChangeProfilePhoto === 'function') {
                _callbacks.onChangeProfilePhoto(_profilePhotoElement);
            }
        }

        _contactForm.onsubmit = function (e) {
            e.preventDefault();
            refreshContactData();
            saveContact();
        }

        _cancelButton.onclick = function () {
            if (_callbacks.onCancel && typeof _callbacks.onCancel === "function") {
                _callbacks.onCancel();
            }
        }
    }

    function refreshContactData() {
        _contactData = utils.merge({}, _contactData, {
            name: _nameElement.value,
            surname: _surnameElement.value,
            patronymic: _patronymicElement.value,
            birthday: _birthdayElement.value,
            gender: _genderElement.value,
            maritalStatus: _marital_statusElement.value,
            site: _siteElement.value,
            email: _emailElement.value,
            company: _companyElement.value,
            nationality: _nationalityElement.value,
            addressInfo: {
                country: _countryElement.value,
                city: _cityElement.value,
                street: _streetElement.value,
                postalCode: _postalCodeElement.value
            }
        });
    }

    function saveContact() {
        var formData = new FormData();
        var attachmentsToSend = {};
        var data = {
            birthday: _contactData.birthday,
            company: _contactData.company,
            email: _contactData.email,
            gender: _contactData.gender,
            maritalStatus: _contactData.maritalStatus,
            name: _contactData.name,
            nationality: _contactData.nationality,
            patronymic: _contactData.patronymic,
            site: _contactData.site,
            surname: _contactData.surname,
            addressInfo: {
                city: _contactData.addressInfo.city,
                country: _contactData.addressInfo.country,
                postalCode: _contactData.addressInfo.postalCode,
                street: _contactData.addressInfo.street
            },
            phoneInfo: {
                deletedIds: _contactData.phoneInfo.deletedIds,
                phonesList: _contactData.phoneInfo.phonesList.filter(function (phone) {
                    var isNew = phone.id < 0;
                    var isUpdated = _contactData.phoneInfo.updatedIds.indexOf(phone.id) > -1;
                    return isNew || isUpdated;
                }).map(function (phone) {
                    return {
                        id: phone.id,
                        comment: phone.comment,
                        countryCode: phone.countryCode,
                        operatorCode: phone.operatorCode,
                        phoneNumber: phone.phoneNumber,
                        phoneType: phone.phoneType
                    }
                })
            },
            attachmentsInfo: {
                deletedIds: _contactData.attachmentsInfo.deletedIds,
                attachmentsList: _contactData.attachmentsInfo.attachmentsList.filter(function (attachment) {
                    var isNew = attachment.id < 0;
                    var isUpdated = _contactData.attachmentsInfo.updatedIds.indexOf(attachment.id) > -1;
                    return isNew || isUpdated;
                }).map(function (attachment) {
                    if (attachment.uploadedFile) {
                        attachmentsToSend[attachment.id] = attachment.uploadedFile
                    }
                    return {
                        id: attachment.id,
                        comment: attachment.comment,
                        fileName: attachment.fileName,
                    }
                })
            }
        }
        if (_contactData.profilePhoto.uploadedFile) {
            formData.append("profilePhoto", _contactData.profilePhoto.uploadedFile);
        }
        for (var attId in attachmentsToSend) {
            var file = attachmentsToSend[attId];
            formData.append("attachment_" + attId, file);
        }
        formData.append("data", JSON.stringify(data));
        log(data);
        fetch(appConstants.URL.contact, {
            method: 'POST',
            body: formData
        })
    }

    function log(data) {
        console.log(data);
    }

    _callbacks = {
        onAddPhone: false,
        onAddAttachment: false,
        onChangeProfilePhoto: false,
        onCancel: false
    };

    return {
        init: init,
        updatePhone: updatePhone,
        updateAttachment: updateAttachment,
        updatePhoto: updatePhoto,
        callbacks: _callbacks
    }
})(App.Constants, App.Utils);