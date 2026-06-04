$(document).ready(function () {
    InitDragAndDrop();
});

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

        var filesNames = "Upload en cours :<br />";
        for (var i = 0; i < files.length; i++) {
            filesNames += files[i].name + "<br />";
        }
        $("#dropZone").html(filesNames);

        var data = new FormData();
        for (var i = 0; i < files.length; i++) {
            data.append("file" + i, files[i]);
        }

        var hfFolder = document.getElementById('mainContent_hfFolderTree');
        data.append('__EVENTTARGET', 'uploadFileDrop');
        data.append('__EVENTARGUMENT', '');
        data.append('mainContent_hfFolderTree', hfFolder ? hfFolder.value : 'Quality');

        $.ajax({
            type: "POST",
            url: "QualityFolder.aspx",
            contentType: false,
            processData: false,
            data: data,
            success: function () {
                $("#dropZone").html("Glisser un fichier ici");
                __doPostBack('gotoFolder', hfFolder ? hfFolder.value : '');
            },
            error: function () {
                $("#dropZone").html("Erreur upload");
            }
        });
    });
}
