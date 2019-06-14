<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Contacts List</title>
    <script type="text/javascript" src="js/mustache.js"></script>
    <script type="text/javascript" src="js/fetch.umd.js"></script>
    <script type="text/javascript" src="js/promise.min.js"></script>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div id="mainContainer"></div>
<p id="messageError" class="error"></p>

<script type="template/mustache" id="contactsListTemplate">
    <h1>Contacts List</h1>
    <div class="controls">
        <div id="pageSizeSelectBlock">
            <span>Items per page:</span>
            <select id="pageSizeSelect">
                {{#pageSizeOptions}}
                <option value="{{val}}" {{pageOptionSelected}}>{{text}}</option>
                {{/pageSizeOptions}}
            </select>
        </div>
        <div id="controlButtonsBlock">
            <div class="button marginLeft5" id="addContact">Add Contact</div>
            <div class="button marginLeft5">Search Contact</div>
        </div>
    </div>
    <div id="contactsListBlock">
        <table id="contactsListTable">
            <tr>
                <th></th>
                <th>Full Name</th>
                <th>Birthday</th>
                <th>Address</th>
                <th>Company</th>
                <th>Status</th>
                <th>Edit/Delete</th>
            </tr>
            {{#contactsList}}
            <tr>
                <td><input type="checkbox" class="check" value="{{id}}" id="checkFor_{{id}}"></td>
                <td><a href="#">{{fullName}}</a></td>
                <td align="right">{{birthDay}}</td>
                <td>{{address}}</td>
                <td>{{company}}</td>
                <td></td>
                <td align="center">
                        <span>
                            <img src="img/edit.png" alt="Edit contact" class="editImage">
                        </span>
                    <span class="link js-delete-contact" data-id="{{id}}">
                            <img src="img/delete.png" alt="Edit contact" class="editImage marginLeft25">
                        </span>
                </td>
            </tr>
            {{/contactsList}}
        </table>

    </div>
    <div class="controls">
        <div id="deleteBlock">
            <div class="button" id="deleteContacts">Delete Contacts</div>
            <div class="button marginLeft5" id="sendEmail">Send email</div>
        </div>
        <div id="pagingBlock">
            {{#isFirstPage}}
            <div class="button disabledPaging"><</div>
            <div class="button disabledPaging"><<</div>
            {{/isFirstPage}}
            {{^isFirstPage}}
            <div class="button" id="goFirstPage"><</div>
            <div class="button" id="goPrevPage"><<</div>
            {{/isFirstPage}}
            <div class="button disabledPaging">{{pageNumber}}</div>
            {{#isLastPage}}
            <div class="button disabledPaging">>></div>
            <div class="button disabledPaging">></div>
            {{/isLastPage}}
            {{^isLastPage}}
            <div class="button" id="goNextPage">>></div>
            <div class="button" id="goLastPage">></div>
            {{/isLastPage}}
        </div>
    </div>
</script>

<script type="template/mustache" id="contactTemplate">
    <h1>Contact Information</h1>
    <div>
        <label for="name">Name:</label><input id="name" type="text"/>
    </div>

    <div>
        <label for="surname">Surname:</label><input id="surname" type="text"/>
    </div>
    <div>
        <label for="patronymic">Patronymic:</label><input id="patronymic" type="text"/>
    </div>
    <div>
        <label for="birthday">Birthday:</label><input id="birthday" type="text"/>
    </div>
    <div>
        <label for="gender">Gender</label><input id="gender" type="text"/>
    </div>
    <div>
        <label for="marital_status">Marital status:</label><input id="marital_status" type="text"/>
    </div>
    <div>
        <label for="site"></label>Site:<input id="site" type="text"/>
    </div>
    <div>
        <label for="email"></label>Email:<input id="email" type="text"/>
    </div>
    <div>
        <label for="company"></label>Company:<input id="company" type="text"/>
    </div>
    <div>
        <label for="nationality">Nationality:</label><input id="nationality" type="text"/>
    </div>
    <input type="button" class="button" id="cancelButton" value="Cancel"/>
</script>
<script type="text/javascript" src="js/constants.js"></script>
<script type="text/javascript" src="js/editContactController.js"></script>
<script type="text/javascript" src="js/contactsController.js"></script>
<script type="text/javascript" src="js/app.js"></script>
</body>
</html>
