$(document).ready(function () {

});

function build_project_view() {
    $('#jstree_demo_div').jstree();
}
let ss = 0;
function draw_view_rec(dv, l, r) {
    let new_folder_begins = [];
    let files = [];
    let prev_folder_name = "";
    for (let i = l; i <= r; i++) {
        let file_path = codeInfo[i].value;
        let ind = file_path.indexOf("/");
        if (ind === -1) {
            files.push(file_path);
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
        let ul = document.createElement("ul");
        lst.appendChild(ul);
        draw_view_rec(ul, L, R);
        dv.appendChild(lst);
    }

    for (let i = 0; i < files.length; i++) {
        let lst_file = document.createElement("li");
        lst_file.append(files[i]);

        lst_file.setAttribute("id", "123" + ss);
        ss++;
        dv.appendChild(lst_file);
    }
}