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
<p id="messageError" class="error hidden"></p>
<div id="mainContainer"></div>

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
                    <span class="link deleteContact" data-id="{{id}}">
                            <img src="img/delete.png" alt="Delete contact" class="editImage marginLeft25">
                        </span>
                </td>
            </tr>
            {{/contactsList}}
        </table>
        {{^contactsList}}
            <div class="warning">There are no contacts</div>
        {{/contactsList}}
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

    <form class="marginBottom5">
        <fieldset class="marginBottom5">
            <legend>Basic info</legend>
            <div class="flex">
                <div class="flex-grow-1">
                    <div class="form-group">
                        <label for="name">Name:</label>
                        <input id="name" type="text" required placeholder="Enter name" value="{{name}}"/>
                    </div>

                    <div class="form-group">
                        <label for="surname">Surname:</label>
                        <input id="surname" type="text" required placeholder="Enter surname" value="{{surname}}"/>
                    </div>

                    <div class="form-group">
                        <label for="patronymic">Patronymic:</label>
                        <input id="patronymic" type="text" placeholder="Enter patronymic" value="{{patronymic}}"/>
                    </div>

                    <div class="form-group">
                        <label for="birthday">Birthday:</label>
                        <input id="birthday" type="date" placeholder="Enter birthday" value="{{birthday}}"/>
                    </div>
                    <div class="form-group">
                        <label for="gender">Gender</label>
                        <select id="gender">
                            <option value="" disabled selected>Select gender</option>
                            {{#genderList}}
                            <option value="{{id}}" {{genderSelected}}>{{name}}</option>
                            {{/genderList}}
                        </select>
                    </div>
                </div>
                <div class="flex-grow-1">
                    <div class="form-group">
                        <label for="marital_status">Marital status:</label>
                        <select id="marital_status">
                            <option value="" disabled selected>Select marital status</option>
                            {{#maritalStatusList}}
                            <option value="{{id}}" {{maritalStatusSelected}}>{{name}}</option>
                            {{/maritalStatusList}}
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="site">Site:</label>
                        <input id="site" type="url" placeholder="Enter web site" value="{{site}}"/>
                    </div>
                    <div class="form-group">
                        <label for="email">Email:</label>
                        <input id="email" type="email" placeholder="Enter email" value="{{email}}"/>
                    </div>
                    <div class="form-group">
                        <label for="company">Company:</label>
                        <input id="company" type="text" placeholder="Enter company" value="{{company}}"/>
                    </div>

                    <div class="form-group">
                        <label for="nationality">Nationality:</label>
                        <input id="nationality" type="text" placeholder="Enter nationality" value="{{nationality}}"/>
                    </div>
                </div>
            </div>
        </fieldset>
        <fieldset class="marginBottom5">
            <legend>Address info</legend>
            <div class="flex">
                {{#addressInfo}}
                <div class="flex-grow-1">
                    <div class="form-group">
                        <label for="country">Country:</label>
                        <input id="country" type="text" placeholder="Enter country" value="{{country}}"/>
                    </div>

                    <div class="form-group">
                        <label for="city">City:</label>
                        <input id="city" type="text" placeholder="Enter city" value="{{city}}"/>
                    </div>

                    <div class="form-group">
                        <label for="street">Street:</label>
                        <input id="street" type="text" placeholder="Enter street" value="{{street}}"/>
                    </div>

                    <div class="form-group">
                        <label for="postalCode">Postal code:</label>
                        <input id="postalCode" type="text" placeholder="Enter postal code" value="{{postalCode}}"/>
                    </div>
                </div>
                {{/addressInfo}}
            </div>
        </fieldset>
        <fieldset>
            <legend>Contact phones</legend>
            <div class="flex">
                <div class="flex-grow-1">
                    {{#phoneInfo}}
                    <div class="controls">
                        <div id="phoneControlsButtonsBlock">
                            <div class="button marginLeft5" id="addPhone">Add Phone</div>
                            {{#hasPhones}}
                            <div class="button marginLeft5" id="editPhone">Edit Phone</div>
                            <div class="button marginLeft5" id="removePhone">Delete Phone</div>
                            {{/hasPhones}}
                        </div>
                    </div>
                    {{#hasPhones}}
                    <div id="phonesListBlock">
                        <table id="phonesListTable">
                            <tr>
                                <th></th>
                                <th>Phone number</th>
                                <th>Phone type</th>
                                <th>Comment</th>
                            </tr>
                            {{#phonesList}}
                            <tr>
                                <td><input type="checkbox" class="check" value="{{id}}" id="checkFor_{{id}}"></td>
                                <td><a href="tel:{{phoneNumber}}">{{phoneNumber}}</a></td>
                                <td>{{phoneType}}</td>
                                <td>{{comment}}</td>
                            </tr>
                            {{/phonesList}}
                        </table>
                    </div>
                    {{/hasPhones}}
                    {{^hasPhones}}
                    <div class="warning">There are no phones added yet</div>
                    {{/hasPhones}}
                </div>
                {{/phoneInfo}}
            </div>
        </fieldset>
        <input type="submit" value="Save" />
    </form>

    <input type="button" class="button" id="cancelButton" value="Cancel"/>
</script>
<script type="text/javascript">
    var App = {};
</script>
<script type="text/javascript" src="js/constants.js"></script>
<script type="text/javascript" src="js/editContactController.js"></script>
<script type="text/javascript" src="js/contactsController.js"></script>
<script type="text/javascript" src="js/app.js"></script>
</body>
</html>
