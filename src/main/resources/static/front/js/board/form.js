window.addEventListener("DOMContentLoaded", function() {
    ClassicEditor.create(document.getElementById("content"), {
        height: 450
    })
    .then(editor => {
        window.editor = editor;
    })
    .catch(err => console.error(err));
});