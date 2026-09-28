function InitDragAndDrop() {

    $("#dropZone").on("dragover", function (e) {
        e.preventDefault();
        $(this).css("background", "grey");
    });

    $("#dropZone").on("dragleave", function () {
        $(this).css("background", "white");
    });

    $("#dropZone").on("drop", function (e) {
        e.preventDefault();
        $(this).css("background", "white");

        var Files = e.originalEvent.dataTransfer.files;
        if (Files.length == 0) return;

        var monInput = document.getElementById('mainContent_DocFileUpload');
        var transfer = new DataTransfer();
        transfer.items.add(Files[0]);
        monInput.files = transfer.files;

        document.getElementById('mainContent_tbFileName').value = Files[0].name;

        checkAndUploadFile();
    });
