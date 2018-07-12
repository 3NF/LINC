function chooseStudent (id) {
    //console.log("dashboard.jsp?courseID=" + getParameter("courseID") + "&assignmentID=" + assignmentID + "&=userID=" + id);
    window.location.href = "dashboard.jsp?courseID=" + getParameter("courseID") + "&assignmentID=" + assignmentID + "&userID=" + id;
}

function getParameter (name) {
    let result = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
    return result[1] || 0;
}