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
    };
    this.isSubmitted = function (assignmentId) {
        return checkedAssignments.includes(assignmentId);
    };
    this.donePercentage = function(assignmentId) {
        if (this.data[assignmentId] === undefined) return 0;
        if (studentsCount === undefined || studentsCount === 0) return 100;
        return (Math.round(Object.keys(this.data[assignmentId]).length / studentsCount * 100));
    }
};
function getAssignmentScores() {
    console.log(getParameter("courseID"));
    $.ajax({
        url: "/user/grades",
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

    });

    if (assignmentsInfo.isSubmitted(assignmentID)) {
        $("#progress-container").hide();
    } else {
        $("#progress-container").show();
    }

    if (assignmentsInfo.isAllDone(assignmentID)) {
        $("#submit-button").show();
        $(".progress").hide();
    } else {
        $("#submit-button").hide();
        $(".progress" ).show();
        let progressBar = $("#checked-percentage");
        let pr = assignmentsInfo.donePercentage(assignmentID);
        progressBar.attr("aria-valuenow", pr);
        progressBar.attr("style", `width:${pr}%`);
        progressBar.text(`შეფასებულია ${pr}%`);
    }

}

function uploadGrades(assignmentId) {
    let dataObj = {
        "classroomId" : getParameter("courseID"),
        "assignmentId" : assignmentId,
        "grades" : assignmentsInfo.data[assignmentId]
    };
    $.ajax({
        url: "/user/grades",
        method: "POST",
        contentType: 'application/json; charset=UTF-8/json',
        data : JSON.stringify(dataObj),
        success: function () {
            checkedAssignments.push(assignmentId);
            fillGrades(1);
        }
    });
}