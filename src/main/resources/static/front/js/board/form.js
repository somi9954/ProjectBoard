window.addEventListener("DOMContentLoaded", function() {
    ClassicEditor.create(document.getElementById("content"), {
        height: 450
    })
    .then(editor => {
        window.editor = editor;
    })
    .catch(err => console.error(err));


    /* 이미지 본문 추가 이벤트 처리 */
    const insertEditors = document.getElementsByClassName("insert_editor");
    for (const el of insertEditors) {
        el.addEventListener("click", (e) => insertEditor(e.currentTarget.dataset.url));
    }
});

/**
* 파일 업로드 콜백 처리
*
*/
function fileUploadCallback(files) {
    if (!files || files.length == 0) {
        return;
    }

    const tpl1 = document.getElementById("tpl_editor").innerHTML;
    const tpl2 = document.getElementById("tpl_file").innerHTML;

    const editorEl = document.getElementById("editor_files");
    const attachEl = document.getElementById("attach_files");

    const domParser = new DOMParser();

    for (const file of files) {
        const loc = file.location;
        let html = loc == 'editor' ? tpl1 : tpl2;
        let targetEl = loc == 'editor' ? editorEl : attachEl;

        if (loc == 'editor') { // 에디터
            insertEditor(file.fileUrl);
        }

        html = html.replace(/\[id\]/g, file.id)
                    .replace(/\[fileName\]/g, file.fileName)
                    .replace(/\[orgUrl\]/g, file.fileUrl);

        const dom = domParser.parseFromString(html, "text/html");
        const span = dom.querySelector("span");
        targetEl.appendChild(span);

        const el = span.querySelector(".insert_editor");
        if (el) {
            el.addEventListener("click", (e) => insertEditor(e.currentTarget.dataset.url));
        }
    }
}

/**
* 이미지 본문 추가
*
*/
function insertEditor(source) {
    editor.execute('insertImage', { source });
}

/**
* 파일 삭제 후 콜백 처리
*
*/
function fileDeleteCallback(fileId) {
    const el = document.getElementById(`file_${fileId}`);
    el.parentElement.removeChild(el);

}