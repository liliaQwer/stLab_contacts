<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Contacts List</title>
    <script type="text/javascript" src="js/modal.js"></script>
    <script type="text/javascript" src="js/mustache.js"></script>
    <script type="text/javascript" src="js/fetch.umd.js"></script>
    <script type="text/javascript" src="js/promise.min.js"></script>
    <link rel="stylesheet" href="css/modal.css">
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
                <th>Edit/Delete</th>
            </tr>
            {{#contactsList}}
            <tr>
                <td><input type="checkbox" class="check" value="{{id}}" id="checkFor_{{id}}"></td>
                <td><span class="link editContact" data-id="{{id}}">{{fullName}}</span></td>
                <td align="right">{{birthday}}</td>
                <td>{{address}}</td>
                <td>{{company}}</td>
                <td align="center">
                        <span class="cursor-pointer editContact" data-id="{{id}}">
                            <img src="img/edit.png" alt="Edit contact" class="editImage">
                        </span>
                    <span class="cursor-pointer deleteContact" data-id="{{id}}">
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

    <form class="marginBottom5" id="contactForm">
        <fieldset class="marginBottom5">
            <legend>Basic info</legend>
            <div class="flex flex-wrap flex-justify-center">
                {{#profilePhoto}}
                <div class="margin5 width200">
                    <img src={{imgSrc}} id="profilePhoto" class="profilePhoto" alt="Profile picture"/>
                </div>
                {{/profilePhoto}}
                <div class="flex-grow-1 marginRight5">
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
                            <option value="{{id}}" {{genderSelected}}>{{description}}</option>
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
                            <option value="{{id}}" {{maritalStatusSelected}}>{{description}}</option>
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
        <fieldset class="marginBottom5">
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
                                <td><input type="checkbox" class="check phone_check" value="{{id}}"
                                           id="checkFor_{{id}}"></td>
                                <td><a href="tel:{{phoneNumber}}">+{{countryCode}}({{operatorCode}}){{phoneNumber}}</a>
                                </td>
                                <td>{{phoneTypeText}}</td>
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
        <fieldset class="marginBottom5">
            <legend>Attachments</legend>
            <div class="flex">
                <div class="flex-grow-1">
                    {{#attachmentsInfo}}
                    <div class="controls">
                        <div id="attachmentsButtonsBlock">
                            <div class="button marginLeft5" id="addAttachment">Add Attachment</div>
                            {{#hasAttachments}}
                            <div class="button marginLeft5" id="editAttachment">Edit Attachment</div>
                            <div class="button marginLeft5" id="removeAttachment">Delete Attachment</div>
                            {{/hasAttachments}}
                        </div>
                    </div>
                    {{#hasAttachments}}
                    <div id="attachmentsListBlock">
                        <table id="attachmentsListTable">
                            <tr>
                                <th></th>
                                <th>File name</th>
                                <th>Upload date</th>
                                <th>Comment</th>
                            </tr>
                            {{#attachmentsList}}
                            <tr>
                                <td><input type="checkbox" class="check attachment_check" value="{{id}}"
                                           id="checkFor_{{id}}"></td>
                                <td>{{^isNew}}<a href="attachments/{{contactId}}/{{fileName}}">{{fileName}}</a>{{/isNew}}
                                    {{#isNew}}{{fileName}}{{/isNew}}
                                </td>
                                <td>{{uploadDate}}</td>
                                <td>{{comment}}</td>
                            </tr>
                            {{/attachmentsList}}
                        </table>
                    </div>
                    {{/hasAttachments}}
                    {{^hasAttachments}}
                    <div class="warning">There are no attachments added yet</div>
                    {{/hasAttachments}}
                </div>
                {{/attachmentsInfo}}
            </div>
        </fieldset>
        <input type="button" class="button" id="cancelButton" value="Cancel"/>
        <input type="submit" class="button" value="Save"/>
    </form>
    <div id="editPhoneContainer"></div>
    <div id="editAttachmentContainer"></div>
    <div id="editProfilePhotoContainer"></div>
</script>

<script type="template/mustache" id="editPhoneTemplate">
    <div id="editPhoneModal" class="modal">
        <div class="header">
            <div class="title">{{modalTitle}}</div>
            <span class="close" data-role="close">X</span>
        </div>
        <hr/>
        <div class="body">
            <form id="editPhoneForm">
                <p id="phoneEditorMessageError" class="error hidden"></p>
                <fieldset class="marginBottom5">
                    <legend>Phone info</legend>
                    <div class="form-group">
                        <label for="countryCode">Country code</label>
                        <input id="countryCode" min="0" required type="number" value="{{countryCode}}">
                    </div>
                    <div class="form-group">
                        <label for="operatorCode">Operator code</label>
                        <input id="operatorCode" min="0" required type="number" value="{{operatorCode}}">
                    </div>
                    <div class="form-group">
                        <label for="phoneNumber">Phone number</label>
                        <input id="phoneNumber" min="0" required type="number" value="{{phoneNumber}}">
                    </div>
                    <div class="form-group">
                        <label for="phoneType">Phone type:</label>
                        <select id="phoneType" required>
                            <option value="" disabled selected>Select phone type</option>
                            {{#phoneTypesList}}
                            <option value="{{id}}" {{phoneTypesSelected}}>{{description}}</option>
                            {{/phoneTypesList}}
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="comment">Comment</label>
                        <textarea id="comment">{{comment}}</textarea>
                    </div>
                </fieldset>
                <input type="submit" id="submitFormButton" class="hidden"/>
            </form>
        </div>
        <div class="footer">
            <button type="button" class="button" data-role="close">Close</button>
            <label for="submitFormButton">Save</label>
        </div>
    </div>
</script>

<script type="template/mustache" id="editAttachmentTemplate">
    <div id="editAttachmentModal" class="modal">
        <div class="header">
            <div class="title">{{modalTitle}}</div>
            <span class="close" data-role="close">X</span>
        </div>
        <hr/>
        <div class="body">
            <form id="editAttachmentForm">
                <p id="attachmentEditorMessageError" class="error hidden"></p>
                <fieldset class="marginBottom5">
                    <legend>Attachment info</legend>
                    <div class="form-group">
                        <label for="fileUpload">Upload file</label>
                        <input id="fileUpload" type="file" {{#isNew}}required{{/isNew}} />
                    </div>
                    <div class="form-group">
                        <label for="attachComment">Comment</label>
                        <textarea id="attachComment">{{comment}}</textarea>
                    </div>
                </fieldset>
                <input type="submit" id="submitAttachmentFormButton" class="hidden"/>
            </form>
        </div>
        <div class="footer">
            <button type="button" class="button" data-role="close">Close</button>
            <label for="submitAttachmentFormButton">Save</label>
        </div>
    </div>
</script>

<script type="template/mustache" id="editProfilePhotoTemplate">
    <div id="editProfilePhotoModal" class="modal">
        <div class="header">
            <div class="title">Pick a photo</div>
            <span class="close" data-role="close">X</span>
        </div>
        <hr/>
        <div class="body">
            <p id="profilePhotoEditorMessageError" class="error hidden"></p>
            <fieldset class="marginBottom5">
                <div class="form-group">
                    <label for="fileUpload">Upload photo</label>
                    <input id="photoUpload" accept="image/*" type="file" required/>
                </div>
            </fieldset>
        </div>
        <div class="footer">
            <button type="button" class="button" data-role="close">Close</button>
            <button type="button" class="button" id="submitProfilePhotoFormButton">Save</button>
        </div>
    </div>
</script>

<script type="template/mustache" id="searchTemplate">
    <h1>Contact Search</h1>

    <form class="marginBottom5" id="searchForm">

    </form>
</script>

<script type="text/javascript">
    var App = {};
</script>
<script type="text/javascript" src="js/constants.js"></script>
<script type="text/javascript" src="js/utils.js"></script>
<script type="text/javascript" src="js/editAttachmentController.js"></script>
<script type="text/javascript" src="js/editProfilePhotoController.js"></script>
<script type="text/javascript" src="js/editPhoneController.js"></script>
<script type="text/javascript" src="js/editContactController.js"></script>
<script type="text/javascript" src="js/contactsController.js"></script>
<script type="text/javascript" src="js/app.js"></script>
</body>
</html>
