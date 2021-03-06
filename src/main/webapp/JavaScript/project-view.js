$(document).ready(function () {
    $('#jstree_demo_div').on("select_node.jstree", function (e, data) {
        let file_id = data.node.li_attr["file-id"];
        if (file_id !== undefined)
            fetchCode(data.node.li_attr["file-id"]);

        localStorage.setItem(assignmentID + "_recentParArr", JSON.stringify(data.node.parents));
        localStorage.setItem(assignmentID + "_recentSelected", data.node.id);
    });
});

function continueRecentView () {
    var parArr = JSON.parse(localStorage.getItem(assignmentID + "_recentParArr"));
    var recentSelected = localStorage.getItem(assignmentID + "_recentSelected");

    if (parArr === null) {
        return;
    }

    var jstree = $('#jstree_demo_div').jstree(true);
    for (let i = parArr.length - 2; i > -1; i--) {
        jstree.toggle_node(parArr[i]);
    }


    jstree.select_node(recentSelected);
    projectViewIsVisible = false;
    toggleProjectView();
}

let projectViewIsVisible = false;
function toggleProjectView() {
    if (projectViewIsVisible) {
        $("#jstree_demo_div_container").toggle("slide");
        $("#file-open").html("&#xe117;");
    } else {
        $("#jstree_demo_div_container").toggle("slide");
        $("#file-open").html("&#xe118;");
    }
    projectViewIsVisible = !projectViewIsVisible;
}


function build_project_view() {
    $('#jstree_demo_div').jstree();
    continueRecentView();
}
function draw_view_rec(dv, l, r) {
    let new_folder_begins = [];
    let files = [];
    let prev_folder_name = "";
    for (let i = l; i <= r; i++) {
        let file_path = codeInfo[i].value;
        let ind = file_path.indexOf("/");
        if (ind === -1) {
            files.push(codeInfo[i]);
            continue;
        }
        let cur_folder_name = file_path.substr(0, ind);
        if (i === l || cur_folder_name !== prev_folder_name) {
            new_folder_begins.push([i, cur_folder_name]);
            prev_folder_name = cur_folder_name;
        }
    }
    for (let i = 0; i < new_folder_begins.length; i++) {
        let L = new_folder_begins[i][0];
        let R;
        if (i < new_folder_begins.length - 1) {
            R = new_folder_begins[i + 1][0] - 1;
        } else {
            R = r;
        }

        for (let j = L; j <= R; j++) {
            codeInfo[j].value = codeInfo[j].value.substr(codeInfo[j].value.indexOf("/") + 1);
        }
        let lst = document.createElement("li");
        lst.append(new_folder_begins[i][1]);
        lst.setAttribute("data-jstree", `{"icon":"/Images/icons/folder.svg"}`);
        let ul = document.createElement("ul");
        lst.appendChild(ul);
        draw_view_rec(ul, L, R);
        dv.appendChild(lst);
    }

    for (let i = 0; i < files.length; i++) {
        let lst_file = document.createElement("li");
        lst_file.append(files[i].value);
        lst_file.setAttribute("file-id", files[i].key);
        lst_file.setAttribute("data-jstree", `{"icon":"${getIconForFile(files[i].value)}"}`);
        dv.appendChild(lst_file);
    }
}

const iconByExt = {
    "cpp"   : "cpp.svg",
    "cc"    : "cpp.svg",
    "c"     : "c.svg",
    "java"  : "java.svg",
    "js"    : "javascript.svg",
    "jpg"   : "image.svg",
    "css"   : "css.svg",
    "html"  : "html.svg",
    "py"    : "python.svg"
};

function getIconForFile(fileName) {
    let extension = fileName.substr(fileName.lastIndexOf(".") + 1);
    if (iconByExt.hasOwnProperty(extension)) {
        return `/Images/icons/${iconByExt[extension]}`;
    } else {
        return `/Images/icons/default.svg`;
    }
}