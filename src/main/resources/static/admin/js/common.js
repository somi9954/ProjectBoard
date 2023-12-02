window.addEventListener("DOMContentLoaded", function() {
    /* 체크박스 토글 처리 S */
    const checkalls =document.getElementsByClassName("checkall");
    for (const el of checkalls) {
        el.addEventListener("click", function() {
            const name = this.dataset.targetName;

            const chks = document.getElementsByName(name);
            for (const el of chks) {
                el.checked = this.checked;
            }

        });
    }
    /* 체크박스 토클 처리 E */

    /* 양식 처리 S */
    const formActions = document.getElementsByClassName("form-action");
    for (const el of formActions) {
        el.addEventListener("click", function() {
            const mode = this.dataset.mode;
            const formName = this.dataset.formName;

            const frm = document.forms[formName];
            if (!frm._method) return;

            frm._method.value = mode == 'delete' ? 'DELETE' : 'PATCH';
            const confirmMsg = `정말 ${mode == 'delete' ? '삭제':'수정'} 하시겠습니까?`;
            if (confirm(confirmMsg)) {
                frm.submit();
            }
        });
    }
    /* 양식 처리 E */
});