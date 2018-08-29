let menuIsVisible = false;

function toggleLoading() {
    $("#loader-wrapper").fadeToggle();
}

const gradeMap = {
    '0': '0',
    'Plus': 'Plus',
    'Check': 'Check',
    'Minus': 'Minus',
    'none': 'Not graded',
    'PlusPlus': 'Plus Plus',
    'CheckPlus': 'Check Plus',
    'CheckMinus': 'Check Minus',
    'MinusMinus': 'Minus Minus'
};

$(document).ready(function () {
    $(".fill").css("display", "block");
    $(".sidenav").css("display", "block");

    $('#goHome').on("click", function () {
        location.assign("/user/choose-room.jsp");
    });

    document.getElementById('menuBar').addEventListener('click', function () {
        if (this.className === 'on') this.classList.remove('on');
        else this.classList.add('on');
    });
});


function toggleNav() {
    if (!menuIsVisible)
        $("#mySidenav")[0].style.width = "250px";
    else
        $("#mySidenav")[0].style.width = "0";
    menuIsVisible = !menuIsVisible;
}

function signOut() {
    document.location.href = "https://www.google.com/accounts/Logout?continue=https://appengine.google.com/_ah/logout?continue=http://localhost:8080/logout";
}

function getParameter(name) {
    try {
        let result = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
        return result[1] || 0;
    } catch (e) {
        return null;
    }
}

function getAssignment(assignmentId) {
    if (getParameter("assignmentID") != null) {
        location.href = changeParameter("assignmentID", assignmentId);
    } else {
        assignmentID = assignmentId;
    }

}

function changeParameter(paramName, paramVal) {
    const oldLink = location.href;
    let ind_of_param = oldLink.indexOf(paramName);
    let old_value_start = oldLink.indexOf("=", ind_of_param + 1) + 1;
    let old_value_end = oldLink.indexOf("&", old_value_start + 1);
    if (old_value_end === -1) old_value_end = oldLink.length;
    return oldLink.substr(0, old_value_start) + paramVal + oldLink.substr(old_value_end);
}

/**
 * Shuffles array in place. ES6 version
 * @param {Array} a items An array containing the items.
 */
function shuffle(a) {
    for (let i = a.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [a[i], a[j]] = [a[j], a[i]];
    }
    return a;
}

function mouseOver(grade) {
    let etarget = event.target;
    etarget.innerHTML = gradeMap[grade];
}