function chooseStudent (id) {
    window.location.href = "dashboard.jsp?courseID=" + getParameter("courseID") + "&assignmentID=" + assignmentID + "&userID=" + id;
}

function getParameter (name) {
    let result = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
    return result[1] || 0;
}

let assignmentsInfo = new function() {
    this.getScore = function (userId, assignmentId) {
        if (this.data[assignmentId] !== undefined && this.data[assignmentId][userId] !== undefined)
            return this.data[assignmentId][userId];
        else
            return "Not Graded";
    };
    this.isAllDone = function (assignmentId) {
        if (this.data[assignmentId] === undefined) return false;
        return (Object.keys(this.data[assignmentId]).length === studentsCount)
    }
};
function getAssignmentScores() {
    console.log(getParameter("courseID"));
    $.ajax({
        url: "/user/get-grades",
        method: "GET",
        data: {
            'courseID' : getParameter("courseID"),
        },
        success: function (data) {
            assignmentsInfo.data = data;
            fillGrades(0);
        }
    });
}

/**
 * This function paints grades
 * @param repaint   if it equals to 1 than grade must repaint
 *                  otherwise it paints for the first time
 */
function fillGrades(repaint) {
    $('.user-box').each(function () {
        let item = this;
        let studentId = item.getAttribute("studentId");
        if (repaint === 1) {
            item.lastChild.remove();
        }
        $(item).append(gradeContainerTemplate(assignmentsInfo.getScore(studentId, assignmentID)));
        if (assignmentsInfo.isAllDone(assignmentID)) {
            $("#submit-button").removeAttr("disabled");
        } else {
            $("#submit-button").attr('disabled', 'disabled');
        }
    });
}
