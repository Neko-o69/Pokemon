var zone = document.getElementById('dropZone');

if (zone) {
    zone.ondragover = function(e) {
        e.preventDefault();
        zone.style.background = '#cce';
    }

    zone.ondragleave = function() {
        zone.style.background = '#f9f9f9';
    }

    zone.ondrop = function(e) {
        e.preventDefault();
        zone.style.background = '#f9f9f9';

        var fichier = e.dataTransfer.files[0];
        if (!fichier) return;

        var formData = new FormData();
        formData.append('DocFileUpload', fichier);
        formData.append('hfFolderTree', document.getElementById('mainContent_hfFolderTree').value);

        $.ajax({
            type: "POST",
            url: "QualityFolder.aspx",
            data: formData,
            contentType: false,
            processData: false,
            success: function(response) {
                __doPostBack('gotoFolder', document.getElementById('mainContent_hfFolderTree').value);
            },
            error: onError
        });
    }
}
