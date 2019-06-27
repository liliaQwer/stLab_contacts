App.ContactsController = (function (appConstants, appUtils) {

    var _messageErrorElement,
        _containerElement,
        _pageSizeSelector,
        _mustacheTemplate,
        _pageSizeOptions,
        _currentPage,
        _totalContacts,
        _searchCriteria,
        _searchCriteriaValues,
        _contactsList,
        _errorMessage,
        _goNextPageButton,
        _goPrevPageButton,
        _goFirstPageButton,
        _goLastPageButton,
        _deleteContactButtons,
        _editContactButtons,
        _deleteContactsButton,
        _addContactButton,
        _sendEmailButton,
        _searchContactButton,
        _clearSearchCretariaButton,
        _callbacks;


    function init() {
        _messageErrorElement = document.getElementById("messageError");
        _containerElement = document.getElementById("mainContainer");

        _mustacheTemplate = document.getElementById("contactsListTemplate").innerHTML;

        _currentPage = appConstants.PAGE_NUMBER_DEFAULT;
        _contactsList = {};
        _totalContacts = 0;
        _searchCriteria = {
            pageNumber: appConstants.PAGE_NUMBER_DEFAULT,
            pageSize: appConstants.PAGE_SIZE_DEFAULT
        }
        _pageSizeOptions = [
            {val: 10, text: "10 items"},
            {val: 20, text: "20 items"}
        ];
        _errorMessage = appConstants.messages.ERROR_MESSAGE;
    }

    function showContactsList(searchCriteria) {
        if (searchCriteria) {
            _searchCriteria = appUtils.merge(_searchCriteria, searchCriteria);
        }
        hideErrorMessage();
        fetch(appConstants.URL.contact + appUtils.encodeQueryString(_searchCriteria))
            .then(function (response) {
                if (response.ok) {
                    return response.json();
                }
                throw new Error(_errorMessage);
            })
            .then(function (data) {
                fillSearchCriteriaValues();
                _totalContacts = data.totalAmount;
                _currentPage = _searchCriteria.pageNumber;
                _contactsList = data;
                _contactsList.isFirstPage = _searchCriteria.pageNumber <= 1;
                _contactsList.isLastPage = getLastPage() <= _searchCriteria.pageNumber;
                var rendered = Mustache.render(_mustacheTemplate, {
                    contactsList: _contactsList.contactsList,
                    searchCriteriaValues: _searchCriteriaValues,
                    hasSearchCriteria: _searchCriteriaValues.length > 2,
                    pageSize: _searchCriteria.pageSize,
                    isFirstPage: _contactsList.isFirstPage,
                    isLastPage: _contactsList.isLastPage,
                    pageSizeOptions: _pageSizeOptions,
                    pageNumber: _searchCriteria.pageNumber,

                    pageOptionSelected: function () {
                        if (this.val == _searchCriteria.pageSize) {
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
    }

    function getLastPage() {
        return Math.ceil(_totalContacts / _searchCriteria.pageSize);
    }

    function fillSearchCriteriaValues(){
        _searchCriteriaValues = [];
        var keys = Object.keys(_searchCriteria);
        for (var i = 0; i < keys.length; i++) {
            if (_searchCriteria[keys[i]]) {
                _searchCriteriaValues.push(_searchCriteria[keys[i]]);
            }
        }
    }

    function showMessageError(error) {
        _messageErrorElement.classList.remove('hidden');
        _messageErrorElement.innerText = error;
    }

    function hideErrorMessage() {
        _messageErrorElement.classList.add('hidden');
    }

    function log(message) {
        console.log(message);
    }

    function deleteContact(idList) {
        fetch(appConstants.URL.contact + "/" + idList, {method: 'delete'})
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
        _deleteContactButtons = document.getElementsByClassName("deleteContact");
        _editContactButtons = document.getElementsByClassName("editContact");
        _deleteContactsButton = document.getElementById("deleteContacts");
        _sendEmailButton = document.getElementById("sendEmail");
        _addContactButton = document.getElementById("addContact");
        _searchContactButton = document.getElementById("searchContact");
        _clearSearchCretariaButton =  document.getElementsByClassName("clearSearch");

        if(_clearSearchCretariaButton){
            _clearSearchCretariaButton[0].onclick = function(){

            }
        }

        for (var i = 0; i < _deleteContactButtons.length; i++) {
            var deleteButton = _deleteContactButtons[i];
            deleteButton.onclick = function (e) {
                var id = e.currentTarget.dataset.id;
                log(id);
                if (!window.confirm("Are you sure you want to delete selected contact?")) {
                    return;
                }
                deleteContact(id);
            }
        }

        for (var i = 0; i < _editContactButtons.length; i++) {
            var editContact = _editContactButtons[i];
            editContact.onclick = function (e) {
                var id = e.currentTarget.dataset.id;
                log(id);
                if (_callbacks.onAddContact && typeof _callbacks.onAddContact == 'function') {
                    _callbacks.onAddContact(id);
                }
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
        };

        _searchContactButton.onclick = function(){
            if (_callbacks.onSearchContact && typeof _callbacks.onSearchContact == 'function'){
                _callbacks.onSearchContact();
            }
        };

        _addContactButton.onclick = function () {
            if (_callbacks.onAddContact && typeof _callbacks.onAddContact == 'function') {
                _callbacks.onAddContact();
            }
        };

        _sendEmailButton.onclick = function () {
            showMessageError("Not implemented");
        };

        _pageSizeSelector.onchange = function (e) {
            _searchCriteria.pageSize = e.target.value;
            showContactsList();
        };

        if (_goNextPageButton) {
            _goNextPageButton.onclick = function () {
                _searchCriteria.pageNumber = _currentPage + 1;
                showContactsList();
            }
        }
        if (_goLastPageButton) {
            _goLastPageButton.onclick = function () {
                _searchCriteria.pageNumber = getLastPage();
                showContactsList();
            }
        }
        if (_goPrevPageButton) {
            _goPrevPageButton.onclick = function () {
                _searchCriteria.pageNumber = _currentPage - 1;
                showContactsList();
            }
        }
        if (_goFirstPageButton) {
            _goFirstPageButton.onclick = function () {
                _searchCriteria.pageNumber = appConstants.PAGE_NUMBER_DEFAULT;
                showContactsList();
            }
        }
    }

    _callbacks = {
        onAddContact: false,
        onSearchContact: false
    };

    return {
        init: init,
        render: showContactsList,
        callbacks: _callbacks
    }
})(App.Constants, App.Utils);