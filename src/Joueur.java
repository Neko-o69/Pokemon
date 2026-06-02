var selectedFiles;

var box = document.getElementById("dropZone");

box.addEventListener("dragover", function (e) {
    e.preventDefault();
});

box.addEventListener("drop", function (e) {
    e.preventDefault();
    selectedFiles = e.dataTransfer.files;
});

document.getElementById("upload").onclick = function () {

    var data = new FormData();

    data.append("file", selectedFiles[0]);

    $.ajax({
        type: "POST",
        url: "QualityFolder.aspx",
        data: data,
        processData: false,
        contentType: false,
        success: function () {
            location.reload();
        }
    });
};
