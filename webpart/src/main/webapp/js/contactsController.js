var pageConstants ={
    PAGE_NUMBER_DEFAULT: 1,
    PAGE_SIZE_DEFAULT: 10,
    PREVIOUS_PAGE: "prev",
    NEXT_PAGE: "next",
    URL:  "contact"
};
var controller = new ContactsController();

function ContactsController() {
    this.messageErrorElement = document.getElementById("messageError");
    this.containerElement = document.getElementById("mainContainer");
    this.mustacheTemplates = {
        contactsList: document.getElementById("contactsListTemplate").innerHTML
    };
    this.pageSize = 10;
    this.currentPage = 0;
    this.contactsList = {};
    this.ERROR_MESSAGE = "Error occurred during request";
    this.showMessageError = function(error){
        this.messageErrorElement.innerText = error;
    };
    this.showContactsList = function(nextPageNumber){
        var _this = this;
        if (!nextPageNumber) {
            nextPageNumber = _this.currentPage;
        }
        fetch(pageConstants.URL + "?pageNumber=" + nextPageNumber + "&pageSize=" + _this.pageSize)
            .then(function(response) {
                if (response.ok){
                    return response.json();
                }
                throw new Error(_this.ERROR_MESSAGE);
            })
            .then(function(data) {
                _this.contactsList.list = data;
                _this.contactsList.pageNumber = nextPageNumber;
                _this.currentPage = nextPageNumber;
                var rendered = Mustache.render(_this.mustacheTemplates.contactsList, _this.contactsList);
                _this.containerElement.innerHTML = rendered;
                if (data === null || data.length == 0) {
                    throw new Error("There is no data");
                }
            })
            .catch(function(error) {
                _this.showMessageError(error);
            });
        console.log("this.currentPage = " + this.currentPage);

    };
    this.deleteContact = function(idList){
        var _this = this;
        fetch(pageConstants.URL + "?id=" + idList,{method: 'delete'})
            .then(function(response) {
                if (response.ok) {
                    return _this.showContactsList();
                }
                throw new Error(_this.ERROR_MESSAGE);
            })
            .catch( function(error) {
                _this.showMessageError(error);
            } );
    }
}
window.onload = function() {
    controller.showContactsList(pageConstants.PAGE_NUMBER_DEFAULT, pageConstants.PAGE_SIZE_DEFAULT);
};

function deleteContact(id){
    console.log(id);
    if (!window.confirm("Are you sure you want to delete selected contact?")){
        return;
    }
    controller.deleteContact(id);
}

function deleteContacts(){
    var inputElements = document.getElementsByClassName('check');
    var idList = new Array();
    for (var i = 0; inputElements[i]; ++i){
        if (inputElements[i].checked){
            idList.push(parseInt(inputElements[i].value));
        }
    }
    if (idList.length == 0){
        alert("You should check a contact!");
        return;
    }
    if (!window.confirm("Are you sure you want to delete selected contacts?")){
        return;
    }
    console.log(idList.join(","));
    controller.deleteContact(idList.join(","));
}

function reloadListByPageSize(pageSizeElement){
    controller.pageSize = pageSizeElement.value;
    controller.showContactsList(pageConstants.PAGE_NUMBER_DEFAULT, pageSizeElement.value);
}

function showNextPage(element){
    if (element.className.indexOf("disabledBtn") >= 0){
        return;
    }
    controller.showContactsList(controller.currentPage + 1);
}

function showPrevPage(element){
    if (element.className.indexOf("disabledBtn") >= 0){
        return;
    }
    controller.showContactsList(controller.currentPage - 1);
}

function sendEmail(){

}