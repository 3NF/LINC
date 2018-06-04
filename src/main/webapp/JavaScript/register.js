
function checkForm(loginForm) {

        if(
            loginForm.email.value === "" || loginForm.firstName.value === "" ||
            loginForm.lastName.value === "" || loginForm.password === "" ||
            loginForm.confirmEmail.value === "" || loginForm.confirmPassword === ""
          )
        {
            let div = document.getElementById("errorAlert");
            div.innerHTML ="Please Fill All Fields!";
            div.removeAttribute("hidden");
            loginForm.insertBefore(div,loginForm.childNodes[2]);
            return false;
        }


        return false;
}