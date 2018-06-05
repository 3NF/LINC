
function checkForm(loginForm)
{
    document.alertBox = document.getElementById("alertBox");
    document.alertBox.innerHTML = "";
    let result = true;
    let fields = loginForm.querySelector(".input-group").querySelectorAll("input");
    fields.forEach(function (value, index, listObj) {
        if(value.value === "")
        {
            result = false;
            value.style.borderColor = "red";
        }else value.style.borderColor = "";
    });

    if(result === false) displayError("Please Fill All Fields");


    if(loginForm.password.value.length < 8)
    {
        displayError("Password Length Should Be At Least 8!");
        result = false;
    }

    if(!checkInputs(loginForm.password,loginForm.confirmPassword))
    {
        displayError("Passwords Not Match!");
        result = false;
    }

    if(!checkInputs(loginForm.email,loginForm.confirmEmail))
    {
        displayError("Emails Not Match");
        result = false;
    }

    return result
}



function checkInputs(input1, input2)
{
    if(input1.value !== input2.value)
    {
        input1.style.borderColor  = "red";
        input2.style.borderColor = "red";
        return false;
    }

    return true;
}


function displayError(text)
{
    document.alertBox.innerHTML += "<li class='text text-danger' style='width: 100%; margin-bottom: 5px'>"  + text +  "</li>";
}