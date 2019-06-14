var contactsController = (function (appConstants) {

    var _messageErrorElement,
        _containerElement,
        _pageSizeSelector,
        _mustacheTemplate,
        _pageSize,
        _pageSizeOptions,
        _currentPage,
        _totalContacts,
        _contactsList,
        _errorMessage,
        _goNextPageButton,
        _goPrevPageButton,
        _goFirstPageButton,
        _goLastPageButton,
        _deleteContactGridButtons,
        _deleteContactsButton,
        _addContactButton,
        _sendEmailButton,
        _callbacks;


    function init() {
        _messageErrorElement = document.getElementById("messageError");
        _containerElement = document.getElementById("mainContainer");

        _mustacheTemplate = document.getElementById("contactsListTemplate").innerHTML;

        _pageSize = appConstants.PAGE_SIZE_DEFAULT;
        _currentPage = 0;
        _contactsList = {};
        _totalContacts = 0;
        _pageSizeOptions = [
            {val: 10, text: "10 items"},
            {val: 20, text: "20 items"}
        ];
        _errorMessage = appConstants.messages.ERROR_MESSAGE;
        showContactsList(appConstants.PAGE_NUMBER_DEFAULT);
    }

    function showContactsList(nextPageNumber) {
        if (!nextPageNumber) {
            nextPageNumber = _currentPage;
        }
        fetch(appConstants.URL.contact + "?pageNumber=" + nextPageNumber + "&pageSize=" + _pageSize)
            .then(function (response) {
                if (response.ok) {
                    return response.json();
                }
                throw new Error(_errorMessage);
            })
            .then(function (data) {
                if (data.contactsList.length == 0) {
                    throw new Error("There is no data");
                }
                _totalContacts = data.totalAmount;
                _currentPage = nextPageNumber;
                _contactsList = data;
                _contactsList.isFirstPage = nextPageNumber <= 1;
                _contactsList.isLastPage = getLastPage() <= nextPageNumber;
                var rendered = Mustache.render(_mustacheTemplate, {
                    contactsList: _contactsList.contactsList,
                    pageSize: _pageSize,
                    isFirstPage: _contactsList.isFirstPage,
                    isLastPage: _contactsList.isLastPage,
                    pageSizeOptions: _pageSizeOptions,
                    pageNumber: _currentPage,
                    pageOptionSelected: function () {
                        if (this.val == _pageSize) {
                            return 'selected';
                        }
                        return '';
                    }
                });
                _containerElement.innerHTML = rendered;
                assignEvents();
            })
            .catch(function (error) {
                showMessageError(error);
            });
        log("currentPage = " + _currentPage);
    }

    function getLastPage() {
        return Math.ceil(_totalContacts / _pageSize);
    }

    function showMessageError(error) {
        _messageErrorElement.innerText = error;
    }

    function log(message) {
        console.log(message);
    }

    function deleteContact(idList) {
        fetch(appConstants.URL.contact + "?id=" + idList, {method: 'delete'})
            .then(function (response) {
                if (response.ok) {
                    return showContactsList();
                }
                throw new Error(_errorMessage);
            })
            .catch(function (error) {
                showMessageError(error);
            });
    }

    function assignEvents() {
        _pageSizeSelector = document.getElementById("pageSizeSelect");
        _goNextPageButton = document.getElementById("goNextPage");
        _goLastPageButton = document.getElementById("goLastPage");
        _goFirstPageButton = document.getElementById("goFirstPage");
        _goPrevPageButton = document.getElementById("goPrevPage");
        _deleteContactGridButtons = document.getElementsByClassName("js-delete-contact");
        _deleteContactsButton = document.getElementById("deleteContacts");
        _sendEmailButton = document.getElementById("sendEmail");
        _addContactButton = document.getElementById("addContact");

        for (var i = 0; i < _deleteContactGridButtons.length; i++) {
            var deleteButton = _deleteContactGridButtons[i];
            deleteButton.onclick = function (e) {
                var id = e.currentTarget.dataset.id;
                log(id);
                if (!window.confirm("Are you sure you want to delete selected contact?")) {
                    return;
                }
                deleteContact(id);
            }
        }

        _deleteContactsButton.onclick = function () {
            var inputElements = document.getElementsByClassName('check');
            var idList = new Array();
            for (var i = 0; inputElements[i]; ++i) {
                if (inputElements[i].checked) {
                    idList.push(parseInt(inputElements[i].value));
                }
            }
            if (idList.length == 0) {
                alert(appConstants.messages.SELECT_CONTACT_WARNING);
                return;
            }
            if (!window.confirm(appConstants.messages.DELETE_CONTACT_CONFIRMATION)) {
                return;
            }
            log(idList.join(","));
            deleteContact(idList.join(","));
        }

        _addContactButton.onclick = function () {
            if (_callbacks.onAddContact && typeof _callbacks.onAddContact == 'function') {
                _callbacks.onAddContact();
            }
        }

        _sendEmailButton.onclick = function () {
            alert("TBD");
        }

        _pageSizeSelector.onchange = function (e) {
            _pageSize = e.target.value;
            showContactsList(appConstants.PAGE_NUMBER_DEFAULT);
        }

        if (_goNextPageButton) {
            _goNextPageButton.onclick = function () {
                showContactsList(_currentPage + 1);
            }
        }
        if (_goLastPageButton) {
            _goLastPageButton.onclick = function () {
                showContactsList(getLastPage());
            }
        }
        if (_goPrevPageButton) {
            _goPrevPageButton.onclick = function () {
                showContactsList(_currentPage - 1);
            }
        }
        if (_goFirstPageButton) {
            _goFirstPageButton.onclick = function () {
                showContactsList(appConstants.PAGE_NUMBER_DEFAULT);
            }
        }
    }

    _callbacks = {
        onAddContact: false
    };

    return {
        init: init,
        callbacks: _callbacks
    }
})(appConstants);