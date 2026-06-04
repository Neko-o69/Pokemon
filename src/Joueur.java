function InitDragAndDrop() {

    $("#dropZone").on("dragover", function (e) {
        e.preventDefault();
        $(this).css("background", "#cce");
    });

    $("#dropZone").on("dragleave", function () {
        $(this).css("background", "white");
    });

    $("#dropZone").on("drop", function (e) {
        e.preventDefault();
        $(this).css("background", "white");

        var lesfichiers = e.originalEvent.dataTransfer.files;
        if (lesfichiers.length == 0) return;

        var monInput = document.getElementById('mainContent_DocFileUpload');
        var transfer = new DataTransfer();
        transfer.items.add(lesfichiers[0]);
        monInput.files = transfer.files;

        document.getElementById('mainContent_tbFileName').value = lesfichiers[0].name;

        checkAndUploadFile();
    });
}
