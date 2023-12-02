window.addEventListener("DOMContentLoaded", function() {
    ClassicEditor.create(document.getElementById("content"), {
        height: 450
    })
    .then(editor => {
        window.editor = editor;
    })
    .catch(err => console.error(err));
});

/**
 * 파일 업로드 콜백 처리
 *
 */
function fileUploadCallback (files) {
    if (!files || files.length == 0 ) {
        return;
    }

    const tpl1 = document.getElementById("tpl_editor").innerHTML;
    const tpl2 = document.getElementById("tpl_file").innerHTML;

    for (const file of files) {
        if (file.location == 'editor') { // 에디터
            editor.execute('insertImage', {source: file.fileUrl});

        } else { // 첨부파일

        }
    }
}