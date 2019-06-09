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
    <div id="mainContainer">
    </div>
    <p id="messageError" class="error"></p>
    <script type="template/mustache" id="contactsListTemplate">
        <h1>Contacts List</h1>
        <div class="controls">
            <div id="pageSizeSelectBlock">
                <span>Items per page:</span>
                <select id="pageSizeSelect" onchange="reloadListByPageSize(this)">
                    <option value="10">10 items</option>
                    <option value="20">20 items</option>
                </select>
            </div>
            <div id="controlButtonsBlock">
                <div class="button marginLeft5">Add Contact</div>
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
                {{#list}}
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
                        <span class="link" onclick="deleteContact({{id}})">
                            <img src="img/delete.png" alt="Edit contact" class="editImage marginLeft25">
                        </span>
                    </td>
                </tr>
                {{/list}}
            </table>

        </div>
        <div class="controls">
            <div id="deleteBlock">
                <div class="button" onclick="deleteContacts()">Delete Contacts</div>
                <div class="button marginLeft5" onclick="sendEmail()">Send email</div>
            </div>
            <div id="pagingBlock">
                <div class="button marginLeft5" onclick="showPrevPage(this)"><<</div>
                <div class="button pageNumber disabledBtn">{{pageNumber}}</div>
                <div class="button" onclick="showNextPage(this)">>></div>
            </div>
        </div>
    </script>
    <script type="text/javascript" src="js/contactsController.js"></script>
</body>
</html>
