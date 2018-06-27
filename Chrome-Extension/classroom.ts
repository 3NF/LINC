
declare var $ : any;




$(document).ready
(
    function()
    {
        const regExp : RegExp  = /https:\/\/classroom\.google\.com\/u\/1\/c\/.*\/a\/.*\/details/;

        if(regExp.test(window.location.href))
        {
            let fileInput : HTMLInputElement = $("input[type='file']")[0];
            let files = fileInput.files
        }
    }
);


function checkFile()
{
    let fileInput : HTMLInputElement = $("input[type='file']");

    if(fileInput === null) return;

    let files = fileInput.files
}



