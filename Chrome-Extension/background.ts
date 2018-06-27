class ReqDetails
{
    url : string;
    requestId : string;
    requestBody : Body;
    initiator : string;
    timeStamp : number;
}

class Body
{
    formData : any;
    error : string;
}


declare var chrome : any;


chrome.webRequest.onBeforeRequest.addListener
(
    function callback(details : ReqDetails)
    {
        let regExp  = /https:\/\/classroom\.google\.com\/u\/1\/c\/.*\/a\/.*\/details/;
        if(regExp.test(details.url))
        {

        }
    },
    {urls: ["<all_urls>"]},
    ["blocking"]
);