function studentTeacherPageTemplate(data) {
    let model = data.profile;
    return `<tr>
                    <td>${model.name.givenName}</td>
                    <td>${model.name.familyName}</td>
                    <td>${model.emailAddress}</td>
                    <td>
                        <div class="btn-group-vertical">
                            <button type="button" class="btn btn-light dropdown-toggle" data-toggle="dropdown">
                                <span class="glyphicon glyphicon-option-vertical"></span></button>
                            <ul class="dropdown-menu" >
                                <li><button type="button" class="btn btn-light" onclick="changeRole('${model.id}' , '${classroomId}' , 'SeminarReader')">
                                    Add as seminar reader
                                </button>
                                </li>
                                <li><button type="button" class="btn btn-light" onclick="changeRole('${model.id}' , '${classroomId}' , 'TeacherAssistant')">
                                    Add as teacher assistant
                                </button>
                                </li>
                            </ul>
                        </div>
                    </td>
                </tr>`;
}

function instructorTeacherPageTemplate(data) {
    let model = data.profile;
    console.log(model);
    return `<tr>
                <td>${model.name.givenName}</td>
                <td>${model.name.familyName}</td>
                <td>${model.emailAddress}</td>
                <td>
                    <div class="btn-group-vertical">
                        <button type="button" class="btn btn-light dropdown-toggle" data-toggle="dropdown">
                            <span class="glyphicon glyphicon-option-vertical"></span></button>
                        <ul class="dropdown-menu">
                            <li><button type="button" class="btn btn-light" onclick="changeRole('${model.id}' , '${classroomId}' , 'SeminarReader')">Remove</button>
                            </li>
                        </ul>
                    </div>
                </td>
            </tr>`
}

function dashboardReplyTemplate(data) {
    return `<div class = "reply-panel-wrapper" data-full = "${data.content}">
                <div class = "reply-panel">
                    <img class = "reply-profile-picture" src="${data.user.picturePath}">
                    <div class = "reply-content">
                        <p class = "reply-user-name">${data.user.firstName + ' ' + data.user.lastName}</p>
                        <p class = "reply-text">${data.content.substr(0, Math.min(data.content.length, replyContentThreshold))}</p>
                        ${data.content.length > replyContentThreshold ? '<button type = "button" class="btn btn-default btn-xs" onclick = "toggleReplyContent()">See more</button>'
                                                                        : ''}
                        <p class = "reply-date">${data.timeStamp}</p>
                    </div>
                </div>
            </div>`;
}

function gradeContainerTemplate(data) {
    return `<div class="grade_${data}">
                <p>${data}</p>
            </div>`;
}
