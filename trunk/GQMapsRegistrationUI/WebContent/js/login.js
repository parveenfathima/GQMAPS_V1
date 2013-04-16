// JavaScript Document

//binding the click event to the submit button
$(document).ready(function() {
	$("#submit").bind("click", userLogin);
});

// login validation for both admin and customer
function userLogin() {
	var user = $.trim($('#txtUserId').val());
	var pwd = $.trim($('#pwdPassword').val());
	var isValid = 1;

	if (user.length === 0) {
		alert("Enter Username");
		$('#txtUserId').focus();
		isValid = 0;
	} else if (pwd.length === 0) {
		alert("Enter Password");
		$('#pwdPassword').focus();
		isValid = 0;
	} else if ((user == 'admin') && (pwd == 'admin')) {
		window.location.href = "edit_registration.html";
		$.jStorage.set("jsUserID", user);
	} else {

		var vUrl = 'http://localhost:8080/GQMapsRegistrationServices/gqm-gk/gatekeeper/getregistration';

		alert("before ajax call");
		$.ajax({
			type : "GET",
			url : vUrl,
			dataType : "json",
			success : function(json) {
				alert("Record length is :" + json.length);
				//alert(JSON.stringify(json));
				
			},
			error : function(json) {
				alert("Error: " + json.status + " " + json.responseText);
			}

		});
	}
}

function checkForValue() {
	var user = $.trim($('#txtUserId').val());

	if (user.length == 0) {
		alert("Enter your user id to change password");
		window.location.href = "login.html"
		$("#txtUserId").focus();
	} else {
		$.jStorage.set("jsUserID", user);

		window.location.href = "change_password.html";
	}
}
