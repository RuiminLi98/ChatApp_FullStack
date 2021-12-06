'use strict';
/**
 * Entry point to the landing page.
 */
window.onload = function() {
    clear();
    $('#registerForm').on('show.bs.modal', clearForm);
    $("#btn_log").click(doLogin);
    $(document).on("click", "#btn_register", doRegister);
    $(document).on("click", "#btn_clear", clearForm);
};

/**
 * A user login to the system.
 */
function doLogin(){
    checkLogin();
    if(($("#usernameLogin").val().trim() !== "") && ($("#passwordLogin").val().trim() !== "")) {
        $.post("/login", {username: $("#usernameLogin").val(),
            password: $("#passwordLogin").val()}, function (data) {
            console.log(data);
            // get a boolean - if true then submit, otherwise do not direct AND show error message
            if (data.username !== "null") {
                console.log("login success");
                localStorage.setItem("username", $("#usernameLogin").val());
                document.getElementById("registerInfo").submit();
            }
            else {
                console.log("login fail");
                // show error messages on the form
                document.getElementById("usernameLoginAlert").innerText = "please check";
                document.getElementById("passwordLoginAlert").innerText = "please check";
            }
        }, "json");
    }
}

/**
 * A user register an account.
 */
function doRegister() {
    checkRegister();
    if(checkComplete()) {
        // assume front-end can compare the password with the password confirmation
        $.post("/register", {username: $("#username").val(), school: $("#school").val(),
            age: $("#age").val(), interests: $("#interests").val(),
            password: $("#password").val()}, function (data) {
            console.log(data);
            // if true then register successfully, otherwise there exists the same username
            if (data === true) {
                console.log("success");
                // close the form
                $("#registerForm").modal('hide');
            } else {
                console.log("fail");
                // show error messages on the form
                document.getElementById("usernameAlert").innerText = "user name already exists";
            }
        }, "json");
    }

}

/**
 * Clear the landing page input.
 */
function clear() {
    document.getElementById("usernameLogin").value = "";
    document.getElementById("passwordLogin").value = "";
}

/**
 * Clear the register page input.
 */
function clearForm() {
    document.getElementById("username").value = "";
    //document.getElementById("zipcode").value="";
    //document.getElementById("email").value="";
    //document.getElementById("phone").value="";
    document.getElementById("school").value = "";
    document.getElementById("age").value = "";
    document.getElementById("interests").value = "";
    document.getElementById("password").value = "";
    document.getElementById("passwordSecond").value = "";
    document.getElementById("usernameAlert").innerText = "";
    document.getElementById("schoolAlert").innerText = "";
    document.getElementById("ageAlert").innerText = "";
    document.getElementById("interestsAlert").innerText = "";
    document.getElementById("passwordAlert").innerText = "";
    document.getElementById("passwordSecondAlert").innerText = "";
}

/**
 * Username should not be empty.
 * @returns {boolean} Whether username is valid or not.
 */
function validateLoginUserName() {
    var a = document.getElementById("usernameLogin");
    var a2 = document.getElementById("usernameLoginAlert");
    if (a.value.trim() === "") {
        a2.innerText = "!";
        return false;
    }
    else {
        a2.innerText = "";
    }
    return true;
}

/**
 * Password should not be empty.
 * @returns {boolean} Whether password is valid or not.
 */
function validateLoginPassword() {
    var a = document.getElementById("passwordLogin");
    var a2 = document.getElementById("passwordLoginAlert");
    if (a.value.trim() === "") {
        a2.innerText = "!";
        return false;
    }
    else {
        a2.innerText = "";
    }
    return true;
}

/**
 * Check info on the log in page.
 */
function checkLogin() {
    validateLoginUserName();
    validateLoginPassword();
}

/**
 * Username should not be empty.
 * @returns {boolean} Whether username is valid or not.
 */
function validateRegisterUserName() {
    var a = document.getElementById("username");
    var a2 = document.getElementById("usernameAlert");
    if (a.value.trim() === "") {
        a2.innerText = "!";
        return false;
    }
    else {
        a2.innerText = ": )";
    }
    return true;
}

/**
 * School should not be empty.
 * @returns {boolean} Whether school is valid or not.
 */
function validateRegisterSchool() {
    var a = document.getElementById("school");
    var a2 = document.getElementById("schoolAlert");
    if (a.value.trim() === "") {
        a2.innerText = "!";
        return false;
    }
    else {
        a2.innerText = ": )";
    }
    return true;
}

/**
 * Age should be an integer between 18 and 110 (inclusive).
 * @returns {boolean} Whether age is valid or not.
 */
function validateRegisterAge() {
    var a = document.getElementById("age");
    var a2 = document.getElementById("ageAlert");
    if (a.value.trim() === "") {
        a2.innerText = "!";
        return false;
    }
    else if(isNaN(a.value.trim())) {
        a2.innerText = "Age should be integer";
        document.getElementById("age").value = "";
        return false;
    }
    else if(parseInt(a.value.trim(),10) < 18 || parseInt(a.value.trim(),10) > 110) {
        a2.innerText = "Age from 18 to 110, please";
        document.getElementById("age").value = "";
        return false;
    }
    else {
        a2.innerText = ": )";
    }
    return true;
}

/**
 * Interests should be one or more words split by comma but not ended by comma.
 * @returns {boolean} Whether interests are valid or not.
 */
function validateRegisterInterests() {
    var a = document.getElementById("interests");
    var a1 = document.getElementById("interestsInstr");
    var a2 = document.getElementById("interestsAlert");
    if (a.value.trim() === "") {
        a2.innerText = "!";
        return false;
    }
    else if(a.validity.patternMismatch && a.value.trim() !== "") {
        a1.innerText = "";
        a2.innerText = "Words split by comma";
    }
    else {
        a2.innerText = ": )";
    }
    return true;
}

/**
 * No format requirements for password but the two passwords should be the same.
 * @returns {boolean} Whether the password is valid.
 */
function validateRegisterPassword() {
    var pass = document.getElementById("password");
    var pass2 = document.getElementById("passwordSecond");
    var alert = document.getElementById("passwordAlert");
    var alert2 = document.getElementById("passwordSecondAlert");
    if(pass.value.trim() === "") {
        alert.innerText = "!";
        return false;
    }
    if(pass2.value.trim() === "") {
        alert2.innerText = "!";
        return false;
    }
    if (pass.value.trim() !== pass2.value.trim()){
        alert.innerText = "";
        alert2.innerText = ">_<";
    }
    else {
        alert.innerText = ": )";
        alert2.innerText = ": )";
    }
    return true;
}

/**
 * To make sure all info for registration is complete.
 * @returns {boolean} Whether info for registration is complete.
 */
function checkComplete() {
    if($("#username").val().trim() !== "" &&
        $("#school").val().trim() !== "" &&
        $("#age").val().trim() !== "" &&
        $("#interests").val().trim() !== "" &&
        ($("#password").val().trim() !== "" && $("#passwordSecond").val().trim() !== "") &&
        ($("#password").val().trim() === $("#passwordSecond").val().trim()))
    {
        return true;
    }
    else {
        return false;
    }
}

/**
 * A function to go through all checks on the register page.
 */
function checkRegister() {
    validateRegisterUserName();
    validateRegisterSchool();
    validateRegisterAge();
    validateRegisterInterests();
    validateRegisterPassword();
}

/* Lize - the code below does not have any function, but do not delete them */
/*
function validateZipcode(){
    var a = document.getElementById("zipcode");
    var a2 = document.getElementById("zipcodeAlert");
    if(a.validity.patternMismatch && a.value != ""){
        a2.innerText="      Expect proper format is 5 digits";
        return false;
    }
    else {
        a2.innerText = "";
    }
    return true;
}

function validatePhone(){
    var a = document.getElementById("phone");
    var a2 = document.getElementById("phoneAlert");
    // console.log(a);
    if(a.validity.patternMismatch && a.value != ""){
        // console.log(a);
        a2.innerText="     Expect proper format is 1231231234";
        return false;
    }
    else
    {
        a2.innerText = "";
    }
    return true;
}

function validateEmail(){
    var a = document.getElementById("email");
    var a2 = document.getElementById("emailAlert");
    if(a.validity.patternMismatch && a.value != ""){
        a2.innerText="      Expect to have @ inside the email";
        return false;
    }
    {
        a2.innerText =""
    }
    return true;
}

function validatePassword(){
    var pass = document.getElementById("password");
    var pass2 = document.getElementById("passwordSecond");
    var pass3 = document.getElementById("passwordAlert");
    if (pass.value != pass2.value && pass != ""){
        pass3.innerText="     The password is not matched";
        return false;
    }
    else {
        pass3.innerText = "";
    }
    return true;
}

function updateInfo(){
    validateZipcode();
    validatePhone();
    validateEmail();
    validatePassword();
    clear();
}
*/
