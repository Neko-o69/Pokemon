$(document).ready(function () {
    InitDragAndDrop();
    DragAndDropOperation();
});
function InitDragAndDrop() {
    $("#dropZone").on("dragenter", function (e) {
        e.preventDefault();
        e.stopPropagation();
    });
    $("#dropZone").on("dragover", function (e) {
        e.preventDefault();
        e.stopPropagation();
    });
    $("#dropZone").on("drop", function (e) {
        e.preventDefault();
        e.stopPropagation();
    });
}
function DragDropOperation() {
    $("#dropZone").on("drop", function (e) {
        e.preventDefault();
        e.stopPropagation();
    });

    var files = e.originalEvent.dataTransfer.files;
    var fileNames = "";
    if (files.length > 0) {
        filesName += "Uploading file <br />";

        for (var i = 0; i < files.length; i++) {
            filesNames += files[i].name + "<br />";
        }
    }
    $("#dropZone").html(filesNames);
    var data = new FormData();
    for (var i = 0; i < files.length; i++) {
        data.append("file" + i, files[i]);
    }
    $.ajax({
        type: "POST",
        url: "QualityFolder.aspx/uploadFileDrop",
        contentType: false,
        processData: false,
        data: data,
        success: function (message) {
            $("#dropZone").html(message);
        },
        error: function () {
            $("#dropZone").html("Error Found");
        },
        beforeSend: function () {
            $("#dropZone").show();
        },

    });



    }
