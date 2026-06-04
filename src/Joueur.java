function InitDragAndDrop() {
    $("#dropZone").on("dragenter dragover", function (e) {
        e.preventDefault();
        e.stopPropagation();
        $(this).css("background", "#cce");
    });

    $("#dropZone").on("dragleave", function () {
        $(this).css("background", "#f9f9f9");
    });

    $("#dropZone").on("drop", function (e) {
        e.preventDefault();
        e.stopPropagation();
        $(this).css("background", "#f9f9f9");

        var files = e.originalEvent.dataTransfer.files;
        if (files.length === 0) return;

        // Injecter le fichier dans le FileUpload ASP.NET
        var input = document.getElementById('mainContent_DocFileUpload');
        var dt = new DataTransfer();
        dt.items.add(files[0]);
        input.files = dt.files;

        // Mettre à jour le nom du fichier
        document.getElementById('mainContent_tbFileName').value = files[0].name;

        // Lancer l'upload exactement comme si on cliquait OK
        checkAndUploadFile();
    });
}
