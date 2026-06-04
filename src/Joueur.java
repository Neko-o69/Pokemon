$.ajax({
    type: "POST",
    url: "QualityFolder.aspx",
    contentType: false,
    processData: false,
    data: data,
    success: function (response) {
        $("#dropZone").html("Glisser un fichier ici");
        location.reload();
    },
    error: function () {
        $("#dropZone").html("Erreur upload");
    }
});
