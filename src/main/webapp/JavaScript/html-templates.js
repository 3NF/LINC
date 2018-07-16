function stundet_teacher_page_template(data) {
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
                                <li><button type="button" class="btn btn-light" onclick="changeRole(${model.id} , ${classroomId} , 'SeminarReader')">
                                    Add as seminar reader
                                </button>
                                </li>
                                <li><button type="button" class="btn btn-light" onclick="changeRole(${model.id} , ${classroomId} , 'TeacherAssistant')">
                                    Add as teacher assistant
                                </button>
                                </li>
                            </ul>
                        </div>
                    </td>
                </tr>`;
}

function instructor_teacher_page_template(data) {
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
                            <li><button type="button" class="btn btn-light" onclick="changeRole(${model.id} , ${classroomId} , 'SeminarReader')">Remove</button>
                            </li>
                        </ul>
                    </div>
                </td>
            </tr>`
}
